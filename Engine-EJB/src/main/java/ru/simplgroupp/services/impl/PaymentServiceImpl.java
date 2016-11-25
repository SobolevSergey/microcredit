package ru.simplgroupp.services.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.ejb.PaymentFilter;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.KassaRuntimeException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.BalanceEntity;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.persistence.RepaymentScheduleEntity;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.annotation.ABusinessEvent;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.SearchUtil;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Имплементация {@link PaymentService}
 */
@Singleton
@TransactionManagement
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PaymentServiceImpl implements PaymentService {

	@Inject Logger logger;

    private static HashMap<String, Object> paymentSortMapping = new HashMap<String, Object>(4);

    static {
        paymentSortMapping.put("createDate", "p.createDate");
        paymentSortMapping.put("processDate", "p.processDate");
    }

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @EJB
    PaymentDAO payDAO;
    
    @EJB
    PeopleDAO peopleDAO;
    
    @EJB
    CreditDAO creditDAO;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private KassaBeanLocal kassaBean;
    
    @EJB
    private RulesBeanLocal rulesBean;
    
    @EJB
    private MailBeanLocal mailBean;
    
    @EJB
    private PeopleBeanLocal peopleBean;
    
    @EJB
    private CreditCalculatorBeanLocal calcBean;
    
    @EJB
    private CreditBeanLocal creditBean;
    
    @EJB
    private EventLogService eventLog;

    @EJB
    BusinessEventSender senderServ;

    @EJB
    UsersBeanLocal usersBean;

    @EJB
    AccountDAO accDAO;

    @EJB
    ProductBeanLocal productBean;

    @EJB
    ProductDAO productDAO;
    
    @EJB
    CreditRequestDAO creditRequestDAO;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createPayment(int creditId, int type, int typesum, Double amount, Integer mode, int partnersId) {

    	Payment payment=createPayment(creditId,type,typesum,amount,mode,null, partnersId, null, null);
        return payment;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createPayment(int creditId, int type, int typesum, Double sumpay, Integer mode, 
    		String externalId, int partnersId, AccountEntity account, Date createDate) {
        CreditEntity credit = creditDAO.getCreditEntity(creditId);
        if (credit == null) {
        	logger.severe("Credit with id "+creditId+" does not exist");
            throw new KassaRuntimeException("Credit with id "+creditId+" does not exist");
        }

        if (createDate == null) {
        	createDate = new Date();
        }
        PaymentEntity pay=new PaymentEntity();
        pay.setCreditId(credit);
        pay.setCreateDate(createDate);
        pay.setIsPaid(false);
        pay.setPartnersId(refBooks.getPartnerEntity(partnersId));
        pay.setAmount(sumpay);
        pay.setPaysumId(refBooks.findByCodeIntegerEntity(RefHeader.PAY_SUM_TYPE,typesum));
        pay.setPaymenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.PAYMENT_TYPE, mode));
        pay.setExternalId(externalId);
        pay.setAccountId(account);
        if (account == null) {
        	pay.setAccountTypeId(refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, type));	
        } else {
        	pay.setAccountTypeId(account.getAccountTypeId());
        }
        pay = payDAO.savePayment(pay);
        
        //пишем лог
        try {
			   eventLog.saveLog(EventType.INFO, EventCode.CREATE_PAYMENT, "Создан платеж "+pay.getId().toString(), 
					null,new Date(),credit.getCreditRequestId(), credit.getPeopleMainId(), credit);
		    } catch (KassaException e) {
			    logger.severe("Не удалось сохранить лог "+e);
		    }
        return new Payment(pay);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createPayment(int creditId, int type, int typesum, Double sumpay, Integer mode,
                                 String externalId,String externalId2, int partnersId, AccountEntity account, Date createDate) {
        CreditEntity credit = creditDAO.getCreditEntity(creditId);
        if (credit == null) {
            logger.severe("Credit with id "+creditId+" does not exist");
            throw new KassaRuntimeException("Credit with id "+creditId+" does not exist");
        }

        if (createDate == null) {
            createDate = new Date();
        }
        PaymentEntity pay=new PaymentEntity();
        pay.setCreditId(credit);
        pay.setCreateDate(createDate);
        pay.setIsPaid(false);
        pay.setPartnersId(refBooks.getPartnerEntity(partnersId));
        pay.setAmount(sumpay);
        pay.setPaysumId(refBooks.findByCodeIntegerEntity(RefHeader.PAY_SUM_TYPE,typesum));
        pay.setPaymenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.PAYMENT_TYPE, mode));
        pay.setExternalId(externalId);
        pay.setExternalId2(externalId2);
        pay.setAccountId(account);
        if (account == null) {
            pay.setAccountTypeId(refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, type));
        } else {
            pay.setAccountTypeId(account.getAccountTypeId());
        }
        pay = payDAO.savePayment(pay);

        //пишем лог
        try {
            eventLog.saveLog(EventType.INFO, EventCode.CREATE_PAYMENT, "Создан платеж "+pay.getId().toString(),
                    null,new Date(),credit.getCreditRequestId(), credit.getPeopleMainId(), credit);
        } catch (KassaException e) {
            logger.severe("Не удалось сохранить лог "+e);
        }
        return new Payment(pay);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PaymentEntity savePayment(PaymentEntity pay) {
    	PaymentEntity pay1 = payDAO.getPayment(pay.getId());
    	if (pay1 == null) {
    		return null;
    	}
  
    	pay1.setExternalId(pay.getExternalId());
    	pay1.setIsPaid(pay.getIsPaid());
    	pay1.setProcessDate(pay.getProcessDate());
    	pay1.setStatus(pay.getStatus());
    	pay1 = payDAO.savePayment(pay1);
        return pay1;
    }

    @Override
    public List<Payment> findPayments(int firstRow, int rows, SortCriteria[] sorting, Set options,
                                      PaymentFilter filter) {

        StringBuilder hql = paymentsHql(filter);

        hql.insert(0, "select p [$SELECT_SORTING$] ");
        String sql = hql.toString();
        String sortSelect = SearchUtil.sortSelectToString(paymentSortMapping, sorting);
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

        if (StringUtils.isEmpty(sortSelect)) {
            sql += " order by p.createDate desc ";
        }

        Query q = emMicro.createQuery(sql);
        if (firstRow >= 0) {
            q.setFirstResult(firstRow);
        }
        if (rows > 0) {
            q.setMaxResults(rows);
        }

        if(filter.getWorkplace_id()!=null){
            q.setParameter("workplace_id", filter.getWorkplace_id());
        }

        if (filter.getCreditId() != null) {
            q.setParameter("creditId", filter.getCreditId());
        }
        if (filter.getFrom() != null) {
            q.setParameter("from", filter.getFrom(), TemporalType.DATE);
        }
        if (filter.getTo() != null) {
            q.setParameter("to", DateUtils.addDays(filter.getTo(), 1), TemporalType.DATE);
        }

        if (StringUtils.isNotEmpty(filter.getLastName())) {
            String s = filter.isCallCenterOnly() ? filter.getLastName().trim().toUpperCase() : "%" + filter.getLastName().trim().toUpperCase() + "%";
            q.setParameter("lastName", s);
        }

        if (StringUtils.isNotEmpty(filter.getName())) {
            String s = filter.isCallCenterOnly() ? filter.getName().trim().toUpperCase() : "%" + filter.getName().trim().toUpperCase() + "%";
            q.setParameter("name", s);
        }

        if (StringUtils.isNotEmpty(filter.getMidname())) {
            String s = filter.isCallCenterOnly() ? filter.getMidname().trim().toUpperCase() : "%" + filter.getMidname().trim().toUpperCase() + "%";
            q.setParameter("midname", "%" + s);
        }

        if (filter.getPartnerId() != null && filter.getPartnerId() > 0) {
            q.setParameter("partnerId", filter.getPartnerId());
        }

        if (filter.getPaymentTypeId() != null && filter.getPaymentTypeId() > 0) {
            q.setParameter("paymentTypeId", filter.getPaymentTypeId());
        }

        if (filter.getAccountId() != null) {
        	q.setParameter("accountId", filter.getAccountId());
        }

        if (filter.getAccountTypeId() != null) {
        	q.setParameter("accountTypeId", filter.getAccountTypeId());
        }

        if (filter.getIsPaid() != null) {
        	q.setParameter("isPaid", filter.getIsPaid());
        }

        if (filter.getPeopleMainId() != null) {
            q.setParameter("peopleMainId", filter.getPeopleMainId());
        }

        List<PaymentEntity> list;
        if (sorting == null || sorting.length == 0) {
            list = q.getResultList();
        } else {
            List<Object[]> lstSource = q.getResultList();
            list = new ArrayList<PaymentEntity>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, list);
        }

        if (list.size() > 0) {
            List<Payment> result = new ArrayList<Payment>(list.size());
            for (PaymentEntity paymentEntity : list) {
                Payment payment = new Payment(paymentEntity);
                result.add(payment);
                if (options != null && options.size() > 0) {
                    payment.init(options);
                }
            }
            return result;
        } else
            return new ArrayList<Payment>(0);

    }

    @Override
    public List<PaymentEntity> findPayments(int firstRow, int rows,SortCriteria[] sorting, PaymentFilter filter) {

        StringBuilder hql = paymentsHql(filter);

        hql.insert(0, "select p [$SELECT_SORTING$] ");
        String sql = hql.toString();
        String sortSelect = SearchUtil.sortSelectToString(paymentSortMapping, sorting);
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

        Query q = emMicro.createQuery(sql);
        if (firstRow >= 0) {
            q.setFirstResult(firstRow);
        }
        if (rows > 0) {
            q.setMaxResults(rows);
        }

        if(filter.getWorkplace_id()!=null){
            q.setParameter("workplace_id",filter.getWorkplace_id());
        }

        if (filter.getCreditId() != null) {
            q.setParameter("creditId", filter.getCreditId());
        }
        if (filter.getFrom() != null) {
            q.setParameter("from", filter.getFrom(), TemporalType.DATE);
        }
        if (filter.getTo() != null) {
            q.setParameter("to", DateUtils.addDays(filter.getTo(),1), TemporalType.DATE);
        }

        if (StringUtils.isNotEmpty(filter.getLastName())) {
            q.setParameter("lastName", "%" + filter.getLastName().trim().toUpperCase() + "%");
        }

        if (StringUtils.isNotEmpty(filter.getName())) {
            q.setParameter("name", "%" + filter.getName().trim().toUpperCase() + "%");
        }

        if (StringUtils.isNotEmpty(filter.getMidname())) {
            q.setParameter("midname", "%" + filter.getMidname().trim().toUpperCase() + "%");
        }

        if (filter.getPartnerId() != null && filter.getPartnerId() > 0) {
            q.setParameter("partnerId", filter.getPartnerId());
        }

        if (filter.getPaymentTypeId() != null && filter.getPaymentTypeId() > 0) {
            q.setParameter("paymentTypeId", filter.getPaymentTypeId());
        }
        
        if (filter.getAccountId() != null) {
        	q.setParameter("accountId", filter.getAccountId());
        }
        
        if (filter.getAccountTypeId() != null) {
        	q.setParameter("accountTypeId", filter.getAccountTypeId());
        }
        
        if (filter.getIsPaid() != null) {
        	q.setParameter("isPaid", filter.getIsPaid());
        }

        if (filter.getPeopleMainId() != null) {
            q.setParameter("peopleMainId", filter.getPeopleMainId());
        }

        List<PaymentEntity> list= q.getResultList();
        return list;

    }
    
    private StringBuilder paymentsHql(PaymentFilter filter) {
        StringBuilder hql = new StringBuilder();

        hql.append(" (1=1) ");
        if (filter.getCreditId() != null) {
            hql.append(" and ");
            hql.append(" p.creditId.id = :creditId");
        }

        if(filter.getWorkplace_id()!=null){
            hql.append(" and ");
            hql.append(" p.creditId.creditRequestId.workplace.id = :workplace_id");
        }

        if (filter.getDateKind() == 1) {
            if (filter.getFrom() != null) {
                if (hql.length() > 0) {
                    hql.append(" and ");
                }
                hql.append(" p.processDate >= :from");
            }
            if (filter.getTo() != null) {
                if (hql.length() > 0) {
                    hql.append(" and ");
                }
                hql.append(" p.processDate < :to");
            }
        } else {
            if (filter.getFrom() != null) {
                if (hql.length() > 0) {
                    hql.append(" and ");
                }
                hql.append(" p.createDate >= :from");
            }
            if (filter.getTo() != null) {
                if (hql.length() > 0) {
                    hql.append(" and ");
                }
                hql.append(" p.createDate < :to");
            }
        }

        if (filter.getStatus() != null) {
            if (filter.getStatus() >= 1) {
                if (hql.length() > 0) {
                    hql.append(" and ");
                }
                if (filter.getStatus() == 1) {
                    hql.append(" p.status = " + PaymentStatus.SUCCESS.ordinal());
                } else if (filter.getStatus() == 2) {
                    hql.append(" p.status = " + PaymentStatus.ERROR.ordinal());
                } else if (filter.getStatus() == 3) {
                    hql.append(" (p.status = ").append(PaymentStatus.NEW.ordinal()).append(" or p.status = ").append(PaymentStatus
                            .SENDED.ordinal()).append(")");
                } else if (filter.getStatus() == 4) {
                    hql.append(" p.status = " + PaymentStatus.REVOKED.ordinal());
                } else if (filter.getStatus() == 5) {
                    hql.append(" p.status = " + PaymentStatus.DELETED.ordinal());
                }
            }
        }

        if (StringUtils.isNotEmpty(filter.getLastName()) && filter.getLastName().trim().length() > 0) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" ( (select count(*) from p.creditId.peopleMainId.peoplepersonal as pp where upper(pp.surname)" +
                    " like :lastName ) > 0 )");
        }
        if (StringUtils.isNotEmpty(filter.getName()) && filter.getName().trim().length() > 0) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" ( (select count(*) from p.creditId.peopleMainId.peoplepersonal as pp where upper(pp.name)" +
                    " like :name ) > 0 )");
        }
        if (StringUtils.isNotEmpty(filter.getMidname()) && filter.getMidname().trim().length() > 0) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" ( (select count(*) from p.creditId.peopleMainId.peoplepersonal as pp where upper(pp.midname)" +
                    " like :midname ) > 0 )");
        }

        if (filter.getPartnerId() != null && filter.getPartnerId() > 0) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" p.partnersId.id = :partnerId");
        }
        
        if (filter.getAccountId() != null) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" p.accountId.id = :accountId");
        }  
        
        if (filter.getAccountTypeId() != null) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" (p.accountTypeId.id = :accountTypeId)");
        }
        
        if (filter.getIsPaid() != null) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" p.isPaid = :isPaid");
        }        

        if (filter.getPaymentTypeId() != null && filter.getPaymentTypeId() > 0) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" p.paymenttypeId.id = :paymentTypeId");
        }

        if (filter.getPeopleMainId() != null) {
            if (hql.length() > 0) {
                hql.append(" and ");
            }
            hql.append(" p.creditId.peopleMainId.id = :peopleMainId");
        }

        if (hql.length() > 0) {
            hql.insert(0, "where ");
        }

        hql.insert(0, "from ru.simplgroupp.persistence.PaymentEntity p ");

        return hql;
    }

    @Override
    public int countPayments(PaymentFilter filter) {
        StringBuilder hql = paymentsHql(filter);
        hql.insert(0, "select count(*) ");
        Query q = emMicro.createQuery(hql.toString());

        if (filter.getCreditId() != null) {
            q.setParameter("creditId", filter.getCreditId());
        }
        if(filter.getWorkplace_id()!=null){
            q.setParameter("workplace_id", filter.getWorkplace_id());
        }
        if (filter.getFrom() != null) {
            q.setParameter("from", filter.getFrom(), TemporalType.TIMESTAMP);
        }
        if (filter.getTo() != null) {
            q.setParameter("to", DateUtils.addDays(filter.getTo(), 1), TemporalType.TIMESTAMP);
        }

        if (StringUtils.isNotEmpty(filter.getLastName())) {
            String s = filter.isCallCenterOnly() ? filter.getLastName().trim().toUpperCase() : "%" + filter.getLastName().trim().toUpperCase() + "%";
            q.setParameter("lastName", s);
        }

        if (StringUtils.isNotEmpty(filter.getName())) {
            String s = filter.isCallCenterOnly() ? filter.getName().trim().toUpperCase() : "%" + filter.getName().trim().toUpperCase() + "%";
            q.setParameter("name", s);
        }

        if (StringUtils.isNotEmpty(filter.getMidname())) {
            String s = filter.isCallCenterOnly() ? filter.getMidname().trim().toUpperCase() : "%" + filter.getMidname().trim().toUpperCase() + "%";
            q.setParameter("midname", s);
        }

        if (filter.getPartnerId() != null && filter.getPartnerId() > 0) {
            q.setParameter("partnerId", filter.getPartnerId());
        }

        if (filter.getPaymentTypeId() != null && filter.getPaymentTypeId() > 0) {
            q.setParameter("paymentTypeId", filter.getPaymentTypeId());
        }

        if (filter.getIsPaid() != null) {
        	q.setParameter("isPaid", filter.getIsPaid());
        }

        if (filter.getPeopleMainId() != null) {
            q.setParameter("peopleMainId", filter.getPeopleMainId());
        }
        
        Number res = (Number) q.getSingleResult();
        return res == null ? 0 : res.intValue();
    }
   
    @Override
    public PaymentEntity getPaymentFromClient(int creditRequestId, int partnerId, int paysumId, Date dt) {
       return getPaymentFromClient(creditRequestId, partnerId, paysumId, dt,null);
    }
    
    @Override
    public PaymentEntity getPaymentFromClient(int creditRequestId, int partnerId, int paysumId, 
    		Date dt,Double amount) {
        String jql = "from ru.simplgroupp.persistence.PaymentEntity where (creditId.creditRequestId.id = :creditRequestId) and (paymenttypeId.codeinteger = :paymenttypeId) and (isPaid=true) ";
        if (partnerId != 0) {
            jql = jql + " and (partnersId.id = :partnerId) ";
        }

        if (paysumId != 0) {
            jql = jql + " and (paysumId.codeinteger = :paysumId) ";
        }

        if (dt != null) {
            jql = jql + " and date_part('day',processDate)=:day and date_part('month',processDate)=:month and date_part('year',processDate)=:year";
        }

        if (amount!=null){
        	jql+=" and amount=:amount ";
        }
        jql = jql + " order by processDate desc";

        Query query = emMicro.createQuery(jql);
        query.setParameter("creditRequestId", creditRequestId);
        query.setParameter("paymenttypeId", Payment.TO_SYSTEM);

        if (partnerId != 0) {
            query.setParameter("partnerId", partnerId);
        }

        if (paysumId != 0) {
            query.setParameter("paysumId", paysumId);
        }

        if (dt != null) {
          
            query.setParameter("day", DatesUtils.getDay(dt));
            query.setParameter("month", DatesUtils.getMonth(dt));
            query.setParameter("year", DatesUtils.getYear(dt));
        }
        if (amount!=null){
        	query.setParameter("amount", amount);
        }
        List<PaymentEntity> payments = query.getResultList();
        return (PaymentEntity) Utils.firstOrNull(payments);
      
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void processSuccessPaymentWait(Integer paymentId, Date processDate) {

        PaymentEntity payment = payDAO.getPayment(paymentId);

        if (payment == null) {
            logger.warning("Платёж " + paymentId + " не может быть помечен как успешный. Причина: не найден");
            return;
        }

        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            return;
        }

        payment.setProcessDate(processDate == null ? new Date() : processDate);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setIsPaid(false);
        payment = payDAO.savePayment(payment);
        
        logger.info("Платёж " + payment.getId() + " успешно проведён");
         
       	try {
		    eventLog.saveLog(EventType.INFO, EventCode.PAY_MONEY, "Была произведена выплата клиенту", 
		    	null,new Date(),payment.getCreditId().getCreditRequestId(), 
				payment.getCreditId().getCreditRequestId().getPeopleMainId(), payment.getCreditId());
	    } catch (KassaException e) {
		    logger.severe("Не удалось сохранить лог "+e);
	    }
    }    

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PaymentEntity processSuccessPayment(Integer paymentId, Date processDate) {

        PaymentEntity payment = payDAO.getPayment(paymentId);

        if (payment == null) {
            logger.warning("Платёж " + paymentId + " не может быть помечен как успешный. Причина: не найден");
            return null;
        }

        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            return null;
        }

        payment.setProcessDate(processDate == null ? new Date() : processDate);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setIsPaid(true);
        payment = payDAO.savePayment(payment);
        
        logger.info("Платёж " + payment.getId() + " успешно проведён");
        
        //если это платит клиент 
        if (payment.getPaymenttypeId().getCodeinteger()==Payment.TO_SYSTEM){
            //пишем лог
            try {
    			eventLog.saveLog(EventType.INFO, EventCode.GET_MONEY, "Была получена оплата от клиента", 
    					null,payment.getProcessDate(),payment.getCreditId().getCreditRequestId(), 
    					payment.getCreditId().getCreditRequestId().getPeopleMainId(), payment.getCreditId());
    		    } catch (KassaException e) {
    			    logger.severe("Не удалось сохранить лог "+e);
    		    }        	
        	
        	
        } else {
        	try {
                senderServ.fireEvent(new Users(usersBean.findUserByPeopleId(payment.getCreditId().getPeopleMainId().getId()))
                        , EventCode.PAY_MONEY, Utils.mapOfSO());

       			    eventLog.saveLog(EventType.INFO,EventCode.PAY_MONEY, "Была произведена выплата клиенту", 
       			    		null,payment.getProcessDate(),payment.getCreditId().getCreditRequestId(), 
       					payment.getCreditId().getCreditRequestId().getPeopleMainId(), payment.getCreditId());
       		    } catch (KassaException e) {
       			    logger.severe("Не удалось сохранить лог "+e);
       		    }
        }
        
        return payment;    
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public void calcCredit(Credit credit, Date processDate,PaymentEntity payment) {
		// есть ли активная заявка на продление
    	ProlongEntity prolong = creditBean.findProlongDraft(credit.getId());
    	logger.info("-------> считаем данные по кредиту calcCredit " + credit.getId());
    	Date dateChange = processDate;
    	if (prolong != null) {
    		dateChange = prolong.getLongdate();
    	}
    	
    	RefinanceEntity refinance=creditDAO.findRefinanceDraft(credit.getId());
    	if (refinance != null) {
    		dateChange = refinance.getRefinanceDate();
    	}
    	
    	Map<String, Object> res = calcBean.calcCredit(credit, dateChange);
    	logger.info("-------> dateChange " + dateChange);
    	//в каком интервале находимся
    	Integer intervalCurrent = Convertor.toInteger(res.get(CreditCalculatorBeanLocal.INVERVAL_CURRENT));
    	logger.info("-------> intervalCurrent " + intervalCurrent);
    	//сумма к возврату
    	Double sumBack = CalcUtils.roundFloor(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_BACK)),0);
    	logger.info("-------> sumBack1 " + sumBack);
    	//сумма штрафов
    	Double sumPenalty = CalcUtils.roundFloor(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_PENALTY)),0);
    	logger.info("-------> sumPenalty " + sumPenalty);
    	// сумма минимального платежа
    	double sumMinPayment = CalcUtils.roundFloor(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_PERCENT)),0);
    	logger.info("-------> sumMinPayment " + sumMinPayment);
     	logger.info("-------> dateMainRemain " + credit.getDateMainRemain());
		// сумма полученных платежей с credit.getDateMainRemain по dateChange включительно
    	// TODO проверить, что будет, если клиент платит в один день несколько раз
    	double sumPays =new Double(0);
    	if (DatesUtils.isSameDay(processDate, credit.getDateLastPayment())&&credit.getDateLastUpdate()!=null) {
    		sumPays = getLastCreditPaymentSum(credit.getId(),processDate);
    	} else {
    		sumPays = getCreditPaymentSum(credit.getId(), null, new DateRange(DatesUtils.makeDate(credit.getDateLastPayment()), processDate));
    	}
		logger.info("-------> sumPays " + sumPays);
		//если почему-то не нашлось, возьмем просто сумму платежа
		if (sumPays==0){
			logger.info("-------> sumPays is 0, payment sum is " + payment.getAmount());
			if (payment.getAmount()!=null){
			    sumPays=payment.getAmount();
			}
			logger.info("-------> sumPays " + sumPays);
		}
		double sumOfThisPayment=sumPays;
		//если сумма платежа меньше суммы возврата, попробуем посмотреть, есть ли остатки на счетах
		double sumSystem=0d;
		PaymentEntity systemPay=null;
		if (sumBack > sumPays) {
				   
		    sumSystem = creditDAO.getPeopleSumsInSystem(credit.getPeopleMain().getId());
		    //если есть деньги, добавляем к платежу
		    if (sumSystem>0) {
		    	//если сумма из копилки больше суммы возврата, сумму надо уменьшить
			    if (sumPays+sumSystem>sumBack){
				    sumSystem=sumBack-sumPays;
			    }
		    	//записываем проведенный платеж
		       	systemPay=createSuccessSystemPayment(credit.getId(),
		    			 processDate, sumSystem, Payment.SUM_FROM_CLIENT);
		        //пересчитывем суммы платежей
		        sumPays = sumPays+sumSystem;
		        //списываем суммы со счета
		        creditBean.savePeopleSum(credit.getPeopleMain().getId(), credit.getEntity(), BaseCredit.OPERATION_OUT, 
		        		PeopleSums.CREDIT_PAID, -1*sumSystem, processDate);
		        logger.info("-------> sumSystem " + sumSystem);
		        logger.info("-------> sumPays " + sumPays);
		    }
		}
		//если сумма платежа меньше суммы возврата, попробуем посмотреть, есть ли бонусы
		double sumBonusPay=0d;
		PaymentEntity bonusPay=null;
		if (sumBack > sumPays) {
			Integer payByBonus=peopleDAO.getUserBonusPayProperties(credit.getPeopleMain().getId());
			//если в настройках стоит платеж бонусами и нет рефинансирования и продления
			if (payByBonus==1&&prolong==null&&refinance==null){
				Map<String,Object> limits=rulesBean.getBonusConstants();
				//если в настройках системы стоит работа с бонусами
				if (Convertor.toInteger(limits.get(CreditCalculatorBeanLocal.BONUS_ISACTIVE))==1){
			      double sumBonus=0d;	
			      sumBonus=peopleDAO.getPeopleBonusInSystem(credit.getPeopleMain().getId());
			      //если есть бонусы, посчитаем, сколько можно использовать
			      if (sumBonus>0){
				    
			    	Double sumForBonus=sumPays;
			    	if (Convertor.toInteger(limits.get(CreditCalculatorBeanLocal.BONUS_USE_PERCENT_ONLY))==1){
			    		  sumForBonus=sumMinPayment;
			      	}
			    	  
				    Map<String, Object> bres=calcBean.calcBonusPaymentSum(sumForBonus,sumBonus, limits);
				    sumBonusPay=CalcUtils.roundFloor(Convertor.toDouble(bres.get(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT)),0);
				    double bonus=CalcUtils.roundFloor(Convertor.toDouble(bres.get(CreditCalculatorBeanLocal.SUM_BONUS_FOR_PAYMENT)),0);
				    //если сумма с бонусами больше суммы возврата, сумму бонусов надо уменьшить
				    if (sumPays+sumBonusPay>sumBack){
					    sumBonusPay=sumBack-sumPays;
				    }
				    //записываем проведенный платеж
		       	    bonusPay=createSuccessSystemPayment(credit.getId(),
		    			 processDate, sumBonusPay, Payment.SUM_FROM_CLIENT);
		            //пересчитывем суммы платежей
		            sumPays = sumPays+sumBonusPay;
		            //списываем суммы со счета
		            try {
		        	    peopleDAO.addBonus(peopleDAO.getPeopleMainEntity(credit.getPeopleMain().getId()), 
		        			refBooks.getBonusByCodeInteger(PeopleBonus.BONUS_CODE_CREDIT_PAY).getEntity(), 
		        			refBooks.findByCodeIntegerEntity(RefHeader.PEOPLE_SUM_OPERATION, BaseCredit.OPERATION_OUT), 
		        			-1*bonus, -1*sumBonusPay, new Date(), creditDAO.getCreditEntity(credit.getId()), null);
					
				    } catch (Exception e) {
					    logger.severe("Не удалось сохранить операцию списания бонусов по кредиту "+credit.getId());
				    }
		            logger.info("-------> sumBonusPay " + sumBonusPay);
		            logger.info("-------> bonus " + bonus);
		            logger.info("-------> sumPays " + sumPays);
			      }//end if sumBonus>0
				}//end если вообще система работает с бонусами
			}//end если есть в настройках
		}//end если сумма платежа меньше суммы возврата
			
		Date dtEndPlan = null;
    	if (sumBack > sumPays) {
    		if (sumPays < sumMinPayment) {
    			//если это не продление, то посчитаем
    			if (prolong==null){
    				
    				if (intervalCurrent == 0) {
    					dtEndPlan = credit.getCreditDataEnd();	
    				} else {
    					dtEndPlan = processDate;
    				}
    				if (intervalCurrent==0){
    					sumBack=credit.getCreditSumBack();
    				}
    			    Double sumBack2 = CalcUtils.roundFloor(CalcUtils.calcSumBack(sumBack, sumPays),0);
	    		    logger.info("-------> sumBack2 " + sumBack2);
	    		    saveRealPaymentLess(dateChange, credit.getEntity(), credit.getSumMainRemain(), intervalCurrent > 0, 
	    		    		sumBack2, false,dtEndPlan,sumMinPayment,sumOfThisPayment,payment.getId(),false,sumPenalty,
	    		    		systemPay,bonusPay,processDate);
    			}
    			// запрещённая ситуация, ничего не пересчитываем и ожидаем, когда сумма принятых платежей превысит минимальный платёж
    			logger.warning("Сумма платежа по кредиту "+credit.getId()+" составляет "+sumPays+" и меньше суммы процентов "+sumMinPayment);
    		} else {
	    		// выплатили не всю сумму к возврату
	    		
	    		if (intervalCurrent == 0) {
	    			// нет просрочки
	    			
	    			if (prolong == null) {
	    				dtEndPlan = credit.getCreditDataEnd();	
	    			} else {
	    				dtEndPlan = DateUtils.addDays( credit.getCreditDataEnd(), prolong.getLongdays() );
	    			}
	    		} else {
	    			// есть просрочка
	    			if (prolong == null) {
	    				dtEndPlan = processDate;
	    			} else {
	    				dtEndPlan = DateUtils.addDays( prolong.getLongdate(), prolong.getLongdays() );
	    			}
	    		}
	    		double sumMain=sumBack-sumPays;
	    		logger.info("-------> sumMain " + sumMain);
	    		logger.info("-------> dtEndPlan " + dtEndPlan);
	    		res = calcBean.calcCredit(credit, dtEndPlan);
	    		sumBack = CalcUtils.roundFloor(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_BACK)),0);
	    		logger.info("-------> sumBack2 " + sumBack);
	    		saveRealPaymentLess(dateChange, credit.getEntity(), sumMain, intervalCurrent > 0, sumBack, (prolong != null),
	    				dtEndPlan,sumMinPayment,sumOfThisPayment,payment.getId(),true,sumPenalty,systemPay,bonusPay,processDate);
    		  }
	    	} else {
	    		creditBean.closeCredit(credit.getEntity(), processDate, sumBack - sumPays,
	    				payment.getId(),payment.getAmount(),systemPay,bonusPay,sumMinPayment);
	    	}
	           
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void processFailPayment(Integer paymentId, Date processDate, ExceptionInfo exceptionInfo) {
        PaymentEntity payment = payDAO.getPayment(paymentId);

        if (payment == null) {
            logger.warning("Платёж " + paymentId + " не может быть помечен как неуспешный. Причина: не найден");
            return;
        }

        payment.setProcessDate(processDate);
        payment.setIsPaid(false);
        payment.setErrorInfo(exceptionInfo);
        if (ResultType.FATAL.equals(exceptionInfo.getResultType())) {
            payment.setStatus(PaymentStatus.ERROR);
            //пишем лог
            try {
   			    eventLog.saveLog(EventType.ERROR,EventCode.ERROR_PAYMENT, "Не удалось сделать платеж "+paymentId.toString(), 
   			    		null,payment.getCreateDate(),payment.getCreditId().getCreditRequestId(), 
   					payment.getCreditId().getCreditRequestId().getPeopleMainId(), payment.getCreditId());
   		    } catch (KassaException e) {
   			    logger.severe("Не удалось сохранить лог "+e);
   		    }
        }
        emMicro.merge(payment);
    }

    /**
     * сохраняем значения по кредиту, если он еще не закрылся
     * @param dateChange - дата изменений
     * @param credit - кредит
     * @param sumRemain - оставшаяся сумма
     * @param bOverdue - есть ли просрочка
     * @param sumBack - сумма к возврату
     * @param bProlong - есть ли сейчас продление
     * @param dtEndPlan - плановая дата окончания
     * @param sumMinPayment - сумма процентов
     * @param sumPays - сумма платежа 
     * @param paymentId - id платежа
     * @param enoughForMinPayment - заплачено достаточно для мин.платежа
     * @param sumPenalty - сумма штрафов
     * @param systemPay - платеж по сумме из кошелька
     * @param bonusPay -  платеж по сумме бонусов
     * @param processDate - дата проведения платежа
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveRealPaymentLess(Date dateChange, CreditEntity credit, Double sumRemain, boolean bOverdue, Double sumBack, 
    		boolean bProlong,Date dtEndPlan,Double sumMinPayment,Double sumPays,Integer paymentId,
    		boolean enoughForMinPayment,Double sumPenalty,PaymentEntity systemPay,
    		PaymentEntity bonusPay,Date processDate) {

       //если платеж меньше положенного, сумма возврата будет другой	
       Double sumRemainNew=sumRemain;
  	   if (!enoughForMinPayment){
  	    	sumRemainNew=sumBack;
  	   }  
  	   double sumSystem=0d;
  	   double sumBonusPay=0d;
  	   if (systemPay!=null){
  		   sumSystem=systemPay.getAmount();
  	   }
  	   if (bonusPay!=null){
  		   sumBonusPay=bonusPay.getAmount();
  	   }
  	   //есть просрочка 
       if ( bOverdue){
    	  
         	// есть просрочка - ставим текущую сумму, потом ее будем увеличивать при неоплате
           	credit.setCreditsumback(CalcUtils.roundFloor(Convertor.toDouble(sumRemainNew,CalcUtils.dformat),0));
            //меняем сумму просроченного долга
           	credit.setCurrentOverdueDebt(CalcUtils.roundFloor(Convertor.toDouble(sumRemainNew,CalcUtils.dformat),0));
            //меняем сумму максимальной просрочки
           	if (credit.getMaxOverdueDebt()==null||(credit.getMaxOverdueDebt()!=null&&credit.getMaxOverdueDebt()<sumRemainNew)){
            	credit.setMaxOverdueDebt(CalcUtils.roundFloor(Convertor.toDouble(sumRemainNew,CalcUtils.dformat),0));
            }
           	
       }
        
       //ставим изменение суммы
       if (!bOverdue||(bOverdue&&bProlong)){	
        	credit.setChangedSum(1);
       }
       credit.setCreditsumdebt(Convertor.toDouble(sumRemain,CalcUtils.dformat));
       credit.setCreditDateDebt(dateChange);
       credit.setDateStatus(processDate);
       credit.setDatelastupdate(processDate);
       logger.info("dateChange " + dateChange);
        
       credit = creditDAO.saveCreditEntity(credit);
         
       //если нет просрочки, или это продление из просрочки, надо посчитать сумму к возврату
       if (!bOverdue||(bOverdue &&bProlong)){
        	
          Credit cred = creditDAO.getCredit(credit.getId(), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
          if (cred == null) {
        	  logger.severe("Не удалось инициализировать кредит "+credit.getId());
          }

          logger.info("sumMain " + sumRemain);
          logger.info("dtEndPlan " + dtEndPlan);
          
          //если заплатили больше минимальной суммы и нет просрочки
          if (enoughForMinPayment&&!bOverdue){
        	  Map<String, Object> res = calcBean.calcCredit(cred, dtEndPlan);
        	  sumBack = (Double) res.get(CreditCalculatorBeanLocal.SUM_BACK); 
        	  logger.info("sumBack " + sumBack);
        	  Double sumPercent = (Double) res.get(CreditCalculatorBeanLocal.SUM_PERCENT); 
        	  logger.info("sumPercent " + sumPercent);
    		  credit.setCreditsumback(CalcUtils.roundFloor(sumBack, 0));
          } 
		  
          //если выплатили меньше суммы процентов
          if ( !enoughForMinPayment){
              credit.setCreditsumback(CalcUtils.roundFloor(Convertor.toDouble(sumRemainNew,CalcUtils.dformat),0));
          }
          
		  if (!bProlong){
              closePaymentSchedule(credit, ReasonEnd.PAYMENT, dateChange);
              savePaymentSchedule(credit,credit.getCreditpercent(),DatesUtils.daysDiff(credit.getCreditdataend(), dateChange),credit.getCreditsumback(), dateChange,sumRemain);
              credit.setChangedSum(0);
          } 
		 		  
		  credit = creditDAO.saveCreditEntity(credit);
	
        }//end not overdue
      	
        try {
        	//проценты по платежу
        	double sumDiff=0d;
        	//сумма процентов по платежу системы
    	    double sumDiffSystem=sumSystem;
        	//если оплатили все проценты
        	if (enoughForMinPayment){
        		//отнимаем от суммы платежа (процентов) сумму бонусов, если она была оплачена
        	    sumDiff=sumMinPayment-sumBonusPay;
        	   	//если сумма процентов все еще больше нуля
        	    if (sumDiff>0&&sumSystem>0){
        		    //разница процентов больше суммы из кошелька
        		    if (sumDiff>sumSystem){
        			    sumDiff=sumDiff-sumSystem;
        		    } else {
        			    sumDiffSystem=sumDiff;
        			    sumDiff=0;
        		    }
        	    }
        	} else {
        		sumDiff=sumPays;
        	}
        	//пишем детали кредита о платеже
        	CreditDetailsEntity creditDetail=creditBean.saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, 
        			dateChange, null, sumRemain, sumDiff,sumRemainNew, sumPays,sumPenalty,paymentId);
        	//пишем детали о сумммах из кошелька, если есть
        	if (sumSystem>0){
        	    creditBean.saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, dateChange, 
        	    	null, 0d, sumDiffSystem, 0d, sumSystem,null,systemPay.getId());
           	}
        	//пишем детали о суммах бонусов, если они есть
        	if (sumBonusPay>0){
        	    creditBean.saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, dateChange, 
        		   	null, 0d, sumBonusPay, 0d, sumBonusPay,null,bonusPay.getId());
        		
        	}
        	//пишем оплаченные проценты кредита
        	creditBean.saveCreditSum(credit.getId(), BaseCredit.SUM_PERCENT, BaseCredit.OPERATION_OUT, 
	        	new Date(), -1*sumDiff, creditDetail.getId());
       	        
	        if (!sumPays.equals(sumMinPayment)&&enoughForMinPayment){
	        	//пишем оплаченную основную сумму кредита
    	        creditBean.saveCreditSum(credit.getId(), BaseCredit.SUM_MAIN, BaseCredit.OPERATION_OUT, 
    	        		new Date(),sumMinPayment-sumDiff, creditDetail.getId());
	        }
        	//пишем основную оставшуюся сумму кредита - вопрос надо ли???
	        creditBean.saveCreditSum(credit.getId(), BaseCredit.SUM_MAIN, null, 
	        		new Date(), sumRemain, creditDetail.getId());
	        
		} catch (Exception e) {
			logger.severe("Не удалось записать данные в creditDetail");
		}
    }
    
    /**
     * Фактически выплаченные суммы по кредиту
     */
    @Override
    public Double getCreditPaymentSum(int creditId) {
    	return getCreditPaymentSum(creditId, null, null);
    }
    
    /**
     * Фактически выплаченные суммы по кредиту
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Double getCreditPaymentSum(int creditId, DateRange createDate, DateRange processDate ) {
        String sql = "select sum(amount) from ru.simplgroupp.persistence.PaymentEntity where (creditId.id = :creditId) and (paymenttypeId.codeinteger = :paymenttypeId) and (processDate is not null) and (isPaid = :isPaid) and (amount > 0)";
        if (createDate != null && createDate.getFrom() != null) {
        	sql = sql + " and (CreateDate >= :createDateFrom)";
        }
        if (createDate != null && createDate.getTo() != null) {
        	sql = sql + " and (CreateDate <= :createDateTo)";
        }
        if (processDate != null && processDate.getFrom() != null) {
        	sql = sql + " and (ProcessDate > :processDateFrom)";
        }
        if (processDate != null && processDate.getTo() != null) {
        	sql = sql + " and (ProcessDate <= :processDateDateTo)";
        }        
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("creditId", creditId);
        qry.setParameter("paymenttypeId",  Payment.TO_SYSTEM);
        qry.setParameter("isPaid", true);
        if (createDate != null && createDate.getFrom() != null) {
        	qry.setParameter("createDateFrom", createDate.getFrom());
        }
        if (createDate != null && createDate.getTo() != null) {
        	qry.setParameter("createDateTo", createDate.getTo());
        }
        if (processDate != null && processDate.getFrom() != null) {
        	qry.setParameter("processDateFrom", DateUtils.addDays(processDate.getFrom(),1));
        }
        if (processDate != null && processDate.getTo() != null) {
        	qry.setParameter("processDateDateTo", processDate.getTo());
        }
        List<Double> lst = qry.getResultList();
      
        if (lst.size() == 0||lst.get(0)==null) {
            return (double) 0;
        } else {
            return  lst.get(0);
        }
    }    

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RepaymentScheduleEntity savePaymentSchedule(CreditEntity cr ,Double stake,int days,Double smback,Date dt,Double sm) {
		RepaymentScheduleEntity sched = new RepaymentScheduleEntity();
		sched.setCreditId(cr);
        sched.setIsactive(ActiveStatus.ACTIVE);
        sched.setCreditsum(sm);
        sched.setCreditsumback(smback);
        sched.setCreditstake(stake);
        sched.setDatabeg(dt);
        sched.setDataend(DateUtils.addDays(dt, days));
       // sched = payDAO.saveSchedule(sched);
        emMicro.persist(sched);
		return sched;
	}
	
	@Override
	public Double getOverduePaymentSum(int creditId) {
		String sql="select sum(p.amount) from payment p,credit c where (p.createdate>c.creditdataend) and (p.credit_id=c.id) and (c.id=?1) and (p.paymenttype_id=?2)";
		Query qry = emMicro.createNativeQuery(sql);
		qry.setParameter(1, creditId);
		qry.setParameter(2, Payment.SUM_FROM_CLIENT);
		List<Double> lst = qry.getResultList();
	    if (lst.size() == 0||lst.get(0)==null) {
	        return new Double(0);
	    } else {
	        return  lst.get(0);
	   }
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void closePaymentSchedule(CreditEntity cr, Integer reason,Date date) {
		 RepaymentScheduleEntity sched = payDAO.findScheduleActive(cr.getId());
         if (sched!=null) {
           sched.setDataend(date);	 
           sched.setIsactive(ActiveStatus.ARCHIVE);
           sched.setReasonEndId(reason);
           payDAO.saveSchedule(sched);
         }
		
	}

	@Override
	public RepaymentScheduleEntity findSchedule(CreditEntity credit, Integer reason) {
		
		String hql = "from ru.simplgroupp.persistence.RepaymentScheduleEntity where (1=1)";
		if (credit != null) {
	            hql = hql + " and (creditId.id = :credit)";
	      }
		 if (reason != null) {
	            hql = hql + " and (reasonEndId = :reason)";
	      }
		  hql=hql+" order by databeg desc";
		  Query qry = emMicro.createQuery(hql);
		  if (credit != null) {
	            qry.setParameter("credit", credit.getId());
	        }
		  if (reason != null) {
	            qry.setParameter("reason", reason);
	        }
		  List<RepaymentScheduleEntity> lst=qry.getResultList();
		  if (lst.size()>0) {
			  return lst.get(0);
		  }
		  return null;
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveExternalId(Integer paymentId, String externalId) {
    	PaymentEntity entity = payDAO.getPayment(paymentId);
        if(entity!=null){
            entity.setExternalId(externalId);
            entity.setProcessDate(new Date());
            entity.setStatus(PaymentStatus.SENDED);
            payDAO.savePayment(entity);
        }
    }
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveExternalId(Integer paymentId, String externalId, String externalId2) {
        PaymentEntity entity = payDAO.getPayment(paymentId);
        if(entity!=null){
            entity.setExternalId(externalId);
            entity.setExternalId2(externalId2);
            entity.setProcessDate(new Date());
            entity.setStatus(PaymentStatus.SENDED);
            payDAO.savePayment(entity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void savePayonlineCardNumberHash(Integer accountId, String cardNumberMasked, 
    		String cardNumberHash,String cardHolder) {
        AccountEntity account = accDAO.getAccountEntity(accountId);
        if (account == null) {
            return;
        }

        account.setIsactive(ActiveStatus.ACTIVE);
        account.setPayonlineCardHash(cardNumberHash);
        account.setAccountnumber(cardNumberMasked);
        account.setCardHolder(cardHolder);
        
        accDAO.saveAccountEntity(account);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createSuccessClientPayment(Integer creditId, Integer partnerId, Double amount, Date date, String externalId) {
        CreditEntity credit = creditDAO.getCreditEntity(creditId);
        if (credit == null) {
            throw new KassaRuntimeException("Credit with id "+creditId+" does not exist");
        }

        PaymentEntity payment=new PaymentEntity();
        payment.setCreditId(credit);
        payment.setCreateDate(new Date());
        payment.setIsPaid(false);
        payment.setPartnersId(refBooks.getPartnerEntity(partnerId));
        payment.setAmount(amount);
        payment.setPaysumId(refBooks.findByCodeIntegerEntity(RefHeader.PAY_SUM_TYPE, Payment.SUM_FROM_CLIENT));
        payment.setPaymenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.PAYMENT_TYPE, Payment.TO_SYSTEM));
        payment.setExternalId(externalId);
        payment = payDAO.savePayment(payment);

        return new Payment(payment);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void calcCreditAfterPayment(Integer creditId,PaymentEntity payment){
    	 Credit cred = creditDAO.getCredit(creditId, 
         		Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST,BaseCredit.Options.INIT_REFINANCES)); 
         if (cred == null) {
 			logger.severe("Не удалось найти заем "+creditId);
             		
     	} else {
     		//если нет черновика рефинансирования, пересчитаем
     		if (cred.getDraftRefinance()==null){
                 calcCredit(cred, new Date(), payment);
                 logger.info("Пересчитали суммы по платежу " + payment.getId() );   
     		}
     	}
    }

    @Override
    public List<Integer> getFirstRequestPaymentSystems() {
       return getFirstRequestPaymentSystems(null);
    }

    @Override
    public int getFirstRequestPaySysTimes(Integer creditRequestId) {
    	Map<String, Object> params =productBean.getCreditPaymentProductConfig(productDAO.getProductDefault().getId());
    	if (creditRequestId!=null){
    	    CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
    	    if (creditRequest!=null&&creditRequest.getProductId()!=null){
    	    	params = productBean.getCreditPaymentProductConfig(creditRequest.getProductId().getId());
    	    }
    	}
        return (Integer) params.get(ProductKeys.FIRST_REQUEST_PAY_SYS_TIMES);
    }
    @Override
    public int getFirstRequestPaySysTimes() {
        return getFirstRequestPaySysTimes(null);
    }

    @Override
    public List<Integer> getFirstRequestPaymentSystems(Integer creditRequestId) {
        Map<String, Object> params =productBean.getCreditPaymentProductConfig(productDAO.getProductDefault().getId());
        if (creditRequestId!=null){
            CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
            if (creditRequest!=null&&creditRequest.getProductId()!=null){
                params = productBean.getCreditPaymentProductConfig(creditRequest.getProductId().getId());
            }
        }
        String str = (String) params.get(ProductKeys.FIRST_REQUEST_PAYMENT_SYSTEMS);
        return Convertor.stringToIntegersList(str);
    }

    @Override
	public Double getLastCreditPaymentSum(int creditId,Date processDate) {
		
		PaymentEntity payment=getLastCreditPayment(creditId,processDate);
		if (payment!=null){
			return payment.getAmount();
		} else {
			return new Double(0);
		}
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PaymentEntity getLastCreditPayment(int creditId,Date processDate) {
		String sql = "from ru.simplgroupp.persistence.PaymentEntity where (creditId.id = :creditId) and (paymenttypeId.codeinteger = :paymenttypeId) and (processDate is not null) and (isPaid = :isPaid) and (amount > 0) ";
        
		if (processDate!=null) {
			sql=sql+" and date_part('day',processDate)=:day and date_part('month',processDate)=:month and date_part('year',processDate)=:year";
		}
		sql=sql+" order by processDate desc";
		Query qry = emMicro.createQuery(sql);
        qry.setParameter("creditId", creditId);
        qry.setParameter("paymenttypeId", Payment.TO_SYSTEM);
        qry.setParameter("isPaid", true);
        if (processDate!=null) {
        	qry.setParameter("day", DatesUtils.getDay(processDate));
        	qry.setParameter("month", DatesUtils.getMonth(processDate));
        	qry.setParameter("year", DatesUtils.getYear(processDate));
        }
        List<PaymentEntity> lst = qry.getResultList();
        return (PaymentEntity) Utils.firstOrNull(lst);
  
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBalance(Integer paymentId, Integer partnerId,
			Integer accountTypeId, Double amount, Date date) {
		BalanceEntity balance=new BalanceEntity();
		if (paymentId!=null){
			PaymentEntity payment= payDAO.getPayment(paymentId);
			balance.setPaymentId(payment);
		}
		if (partnerId!=null){
			PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			balance.setPartnersId(partner);
		}
		if (accountTypeId!=null){
			
			ReferenceEntity account=refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, accountTypeId);
			if (account!=null){
				balance.setAccountTypeId(account);
			}
		}
		balance.setEventDate(date);
		balance.setAmount(amount);
		emMicro.persist(balance);
	}

	@Override
	public Balance findLastBalance(Integer partnerId, Integer accountTypeId) {
		String hql="from ru.simplgroupp.persistence.BalanceEntity where (1=1)";
		if (partnerId!=null){
			hql+=" and partnersId.id=:partnerId ";
		}
		if (accountTypeId!=null){
			hql+=" and accountTypeId.codeinteger=:accountTypeId";
		}
		hql+=" order by eventDate desc";
		Query qry=emMicro.createQuery(hql);
		if (partnerId!=null){
			qry.setParameter("partnerId", partnerId);
		}
		if (accountTypeId!=null){
			qry.setParameter("accountTypeId", accountTypeId);
		}
		List<BalanceEntity> lst=qry.getResultList();
		if (lst.size()>0){
			Balance balance=new Balance(lst.get(0));
			return balance;
		}
		return null;
	}
	
	@Override
	public List<Account> listAccounts(int peopleMainId) {
		
		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		if (peopleMain!=null) {
		    List<AccountEntity> lst = peopleBean.findAccounts(peopleMain, null, ActiveStatus.ACTIVE);
		    List<Account> lstRes = new ArrayList<Account>(lst.size());
		    if (lst.size()>0) {
		      for (AccountEntity ent: lst) {
			    Account acc = new Account(ent);
			    lstRes.add(acc);
		      }
		    }
		    return lstRes;
		} else {
			return new ArrayList<Account>(0);
		}
	}
	    
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void savePaymentWithAccount(Payment payment) {
		PaymentEntity paymentEntity= payDAO.getPayment(payment.getId());
		
		if (paymentEntity!=null){
			//запишем новый счет
			if (payment.getAccount()!=null){
			    AccountEntity account= accDAO.getAccountEntity(payment.getAccount().getId());
			    paymentEntity.setAccountId(account);
			    paymentEntity.setAccountTypeId(account.getAccountTypeId());
			    PartnersEntity partner=refBooks.getPartnerFromLink(account.getAccountTypeId());
			    paymentEntity.setPartnersId(partner);
			}
			 
		    paymentEntity.setAmount(payment.getAmount());
		    paymentEntity.setCreateDate(payment.getCreateDate());
		    paymentEntity.setStatus(payment.getStatus());
		    paymentEntity.setIsPaid(payment.getIsPaid());
		    payDAO.savePayment(paymentEntity);
		}
		
	}

	@Override
	@Asynchronous	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void paymentEvent(@Observes @ABusinessEvent(className = "ru.simplgroupp.transfer.Payment", eventCode = EventCode.PAYMENT_COMPLETED_SUCCESS) BusinessEvent event) {
		if (EventCode.PAYMENT_COMPLETED_SUCCESS == event.getEventCode()) {
			// сдвинуть дату начала кредита, если платеж делали не в этот день
			PaymentEntity payment = payDAO.getPayment(Convertor.toInteger(event.getBusinessObjectId()));
			if (payment != null && payment.getPaymenttypeId().getCodeinteger().equals( Payment.FROM_SYSTEM )) {
				creditBean.changeCreditStart(payment.getCreditId().getId(), payment.getCreateDate(),payment.getId());
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PaymentEntity createSuccessSystemPayment(Integer creditId,
			Date processDate, Double amount, Integer paySumId) {
		PaymentEntity pay=new PaymentEntity();
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
        pay.setCreditId(credit);
        pay.setCreateDate(processDate);
        pay.setProcessDate(processDate);
        pay.setIsPaid(true);
        pay.setStatus(PaymentStatus.SUCCESS);
        pay.setPartnersId(refBooks.getPartnerEntity(Partner.SYSTEM));
        pay.setAmount(amount);
        pay.setPaysumId(refBooks.findByCodeIntegerEntity(RefHeader.PAY_SUM_TYPE, paySumId));
        pay.setPaymenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.PAYMENT_TYPE, Payment.TO_SYSTEM));		
        emMicro.persist(pay);
        return pay;
	}

	@Override
	public Integer getCountPaymentsInMonth(Integer creditId,Date date) {
		 String hql = "select count(*) from ru.simplgroupp.persistence.PaymentEntity where (creditId.id = :creditId) and (paymenttypeId.codeinteger = :paymenttypeId) "
		 		+ "and (isPaid=true) and date_part('month',processDate)=:month and date_part('year',processDate)=:year";
	     
		 return JPAUtils.getSingleResultSqlInteger(emMicro, hql, Utils.mapOf("creditId", creditId,
				 "paymenttypeId", Payment.TO_SYSTEM,
				 "month", DatesUtils.getMonth(date),
				 "year", DatesUtils.getYear(date)));
       
	}
}
