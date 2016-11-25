package ru.simplgroupp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.RepaymentScheduleEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.RepaymentSchedule;
import ru.simplgroupp.util.ListContainer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PaymentDAOImpl implements PaymentDAO {
	
	@Inject Logger logger;
	
	private static HashMap<String, Object> paymentSortMapping = new HashMap<String, Object>(4);	
    static {
    	paymentSortMapping.put("CreateDate", "c.createDate");
    	paymentSortMapping.put("ProcessDate", "c.processDate");
    	paymentSortMapping.put("Amount", "c.amount");
    }		
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;	

	@Override
	public PaymentEntity findPaymentToClient(int creditId) {
        Query qry = emMicro.createNamedQuery("findPaymentToClient");
        qry.setParameter("creditId", creditId);
        qry.setParameter("code2", Payment.FROM_SYSTEM);
        qry.setParameter("code", Payment.MAIN_SUM_TO_CLIENT);
        List<PaymentEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return lst.get(0);
        }
	}

	@Override
	public PaymentEntity findSendedPayment(Integer creditId, int partnerId) {
        List<PaymentEntity> payments = emMicro.createNamedQuery("findSendedPayment")
                .setParameter("creditId", creditId).setParameter("partnerId", partnerId).setMaxResults(1).getResultList();

        return payments.size() > 0 ? payments.get(0) : null;
	}
	
	@Override
	public List<PaymentEntity> getPaymentsByCredit(int creditId, int paymenttypeId) {
		Query qry = emMicro.createNamedQuery("getPaymentsByCredit");
		qry.setParameter("creditId", creditId);
		qry.setParameter("paymenttypeId", paymenttypeId);
		return qry.getResultList();
	}

    @Override
    public PaymentEntity savePayment(PaymentEntity pay) {
    	PaymentEntity pay1 = emMicro.merge(pay);
    	emMicro.persist(pay1);
    	return pay1;
    }
    
    @Override
    public Payment getPayment(int id, Set options) {
        PaymentEntity entity = emMicro.find(PaymentEntity.class, id);
        if (entity == null) {
            return null;
        } else {
            Payment payment = new Payment(entity);
            payment.init(options);
            return payment;
        }
    }
    
	@Override
	public List<Payment> listPayments(int creditId) {
		Query qry = emMicro.createNamedQuery("listPayments");
		qry.setParameter("creditId", creditId);
		qry.setParameter("paymenttype", Payment.TO_SYSTEM);
		List<PaymentEntity> lst=qry.getResultList();
		
		if (lst.size()>0) {
		   	List<Payment> lst1=new ArrayList<Payment>(lst.size());
		    for (PaymentEntity sch:lst) {
		    	Payment cr = new Payment(sch); 
		    	lst1.add(cr);
		   	}
		   	return lst1;
		}
		else
			return new ArrayList<Payment>(0);	
	}    
    
	@Override
	public List<RepaymentScheduleEntity> listScheduleEntities(int creditId) {
		Query qry = emMicro.createNamedQuery("listScheduleEntities");
		qry.setParameter("creditId", creditId);
		return qry.getResultList();	
	}     
    
	@Override
	public List<RepaymentSchedule> listSchedules(int creditId) {
		List<RepaymentScheduleEntity> lst= listScheduleEntities(creditId);
		
		if (lst.size()>0) {
		   	List<RepaymentSchedule> lst1=new ArrayList<RepaymentSchedule>(lst.size());
		    for (RepaymentScheduleEntity sch:lst) {
		    	RepaymentSchedule cr = new RepaymentSchedule(sch); 
		    	lst1.add(cr);
		   	}
		   	return lst1;
		}
		else
			return new ArrayList<RepaymentSchedule>(0);	
	
	}   
	
	@Override
	public RepaymentScheduleEntity saveSchedule(RepaymentScheduleEntity rep) {
		RepaymentScheduleEntity ent = emMicro.merge(rep);
		emMicro.persist(rep);
		return ent;
	}

    @Override
    public PaymentEntity getPayment(int id) {
        return emMicro.find(PaymentEntity.class, id);
    }

	@Override
	public RepaymentScheduleEntity findScheduleActive(Integer creditId) {
		Query qry = emMicro.createNamedQuery("findScheduleActive");
        qry.setParameter("creditId", creditId);
		List<RepaymentScheduleEntity> lst=qry.getResultList();
		if (lst.size()>0) {
			return lst.get(0);
		} else {
			return null;
		}
	}  
	
    @Override
    public Payment findPaymentByExternalId(String externalId) {
        List<PaymentEntity> payments = (List<PaymentEntity>) emMicro.createNamedQuery("findPaymentByExternalId")
                .setParameter("externalId", externalId).getResultList();
        return payments.size() > 0 ? new Payment(payments.get(0)) : null;
    }

	@Override
	public int countData(ListContainer<Payment> listCon) {
		int nCount = listCon.calcCount(emMicro);
		return nCount;
	}

	@Override
	public List<Payment> listData(int nFirst, int nCount, ListContainer<Payment> listCon) {
		return listCon.calcList(nFirst, nCount, emMicro);
	}

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount, ListContainer<Payment> listCon) {
		return listCon.calcIds(nFirst, nCount, emMicro);
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(Class<T> clz) {
		try {
			T lstCon = clz.newInstance();
			lstCon.setSortMapping(paymentSortMapping);
			lstCon.setItemClass(PeopleMain.class);
			return lstCon;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Не создали список " + clz.getName(), e);
			return null;
		}
	}

	@Override
	public List<PaymentEntity> findExecutedPayments(PaymentStatus status,
			Partner partner) {
		return emMicro.createQuery("select p from ru.simplgroupp.persistence.PaymentEntity where status = ? and partnersId=?")
                .setParameter(0, status).setParameter(1, partner.getEntity())
                .getResultList();
	}	
}
