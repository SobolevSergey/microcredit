package ru.simplgroupp.ejb;

import java.util.*;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.toolkit.common.*;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.*;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;
import ru.simplgroupp.workflow.WorkflowObjectStateDefExt;
/**
 * работа с кредитами
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(CreditBeanLocal.class)
public class CreditBean implements CreditBeanLocal {

	@PersistenceContext(unitName = "MicroPU")
	protected EntityManager emMicro;
	
	@EJB
	CreditDAO creditDAO;
	
    @EJB
    CreditRequestDAO crDAO;	
	
	@EJB
	PaymentDAO payDAO;

	@EJB
	ReferenceBooksLocal refBooks;
	
	@EJB
	PaymentService paymentServ;
	
    @EJB
    CreditCalculatorBeanLocal creditCalc;	

    @EJB 
    EventLogService eventLog;
    
    @EJB
	CreditInfoService creditInfo;
    
    @EJB
    PeopleBeanLocal peopleBean;

    @EJB
    BusinessEventSender senderServ;

    @EJB
    UsersBeanLocal usersBean;

    @EJB
    KassaBeanLocal kassaBean;

    @EJB
    MailBeanLocal mailBean;
    
    @EJB
	PeopleDAO peopleDAO;
    
    @EJB
    ProductDAO productDAO;
    
    @EJB
    OrganizationService orgService;

	@EJB
	CollectorDAO collectorDAO;

	@EJB
	UsersDAO usersDAO;

	@EJB
	ProductBeanLocal productBeanLocal;

	@EJB
	RulesBeanLocal rules;
    
	@EJB
	OfficialDocumentsDAO officialDocumentsDAO;
	
	@EJB
	ProlongDAO prolongDAO;
	
	private static HashMap<String, Object> creditSortMapping = new HashMap<String, Object>(4);
	
	@Inject Logger logger;
	
	private static Map<String, WorkflowObjectStateDefExt> creditStates = new HashMap<>(9);
	
	static {
	      creditSortMapping.put("CreditDataBeg", "c.creditdatabeg");
	      creditSortMapping.put("CreditSum", "c.creditsum");
	      creditSortMapping.put("CreditDataEnd", "c.creditdataend");
	      
	      WorkflowObjectStateDefExt def = new WorkflowObjectStateDefExt(new WorkflowObjectStateDef("procCR::callPaymentInitial"));
	      def.getStateDef().setDescription("Перед началом платежа");
	      creditStates.put(def.getStateDef().getName(), def);
	      
	      def = new WorkflowObjectStateDefExt(new WorkflowObjectStateDef("procCR::eventgateway1"));
	      def.getStateDef().setDescription("Кредит отправлен и не просрочен");
	      def.getStateDef().addActionDef("procCR::callPaymentInitial", "Платёж клиенту");
	      def.getStateDef().addActionDef("procCR::callactivity4", "Платёж от клиента");
	      def.getStateDef().addActionDef("procCR::eventgateway1", "Сделать кредит активным");
	      def.getStateDef().addActionDef("procCR::callProlong", "Повторить продление");
	      def.getStateDef().addActionDef("procCR::endOk", "Успешно завершить кредит");
	      
	      creditStates.put(def.getStateDef().getName(), def);	      
    }
	
	@Override
	public Map<String, WorkflowObjectStateDefExt> getCreditStates() {
		return creditStates;
	}

	 
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelProlongDraft(Integer creditId) throws KassaException {
		     ProlongEntity pl= prolongDAO.findProlongDraft(creditId);	    	 
		     if (pl == null) {
		    	 return;
		     }
		     Integer peopleMainId=pl.getCreditId().getPeopleMainId().getId();
		     if (peopleMainId!=null){
		    	 OfficialDocumentsEntity document=officialDocumentsDAO.findOfficialDocumentCreditDraft(peopleMainId, 
		    			 creditId,OfficialDocuments.OFERTA_PROLONG);
		    	 if (document!=null){
		    		 officialDocumentsDAO.deleteOfficialDocument(document.getId());
		    	 }
		     }
		     prolongDAO.deleteProlong(pl.getId());

	}	 
	 
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelRefinanceDraft(Integer creditId) throws KassaException {
		     RefinanceEntity refinance= creditDAO.findRefinanceDraft(creditId);	    	 
		     if (refinance == null) {
		    	 return;
		     }
		     Integer peopleMainId=refinance.getCreditId().getPeopleMainId().getId();
		     if (peopleMainId!=null){
		    	 OfficialDocumentsEntity document=officialDocumentsDAO.findOfficialDocumentCreditDraft(peopleMainId, 
		    			 creditId,OfficialDocuments.OFERTA_REFINANCE);
		    	 if (document!=null){
		    		 officialDocumentsDAO.deleteOfficialDocument(document.getId());
		    	 }
		     }
		     creditDAO.deleteRefinance(refinance.getId());

	}	 
 
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveCredit(Credit credit) {
		 ReferenceEntity ref = refBooks.getReferenceEntity(credit.getCreditStatus().getId());
		 credit.getEntity().setCreditStatusId(ref);
			 
		 creditDAO.saveCreditEntity(credit.getEntity());
	 }
	 
     @Override
	 public Credit creditActive(Integer peopleMainId) {     
	        List<CreditEntity> lstc = creditDAO.findCreditByPeople(peopleMainId, Partner.SYSTEM, true, false);
	        if (lstc.size() > 0) {
	            List<Credit> lst = new ArrayList<Credit>(1);
	            lst.add(new Credit(lstc.get(0)));

	            return lst.get(0);
	        } else {
	            return null;
	        }
	 }
	  
	 @Override
	 public List<Credit> creditArchive(PeopleMainEntity peopleMain) {
	        List<CreditEntity> lstc = creditDAO.findCreditByPeople(peopleMain.getId(), Partner.SYSTEM, true, true);
	        if (lstc.size() > 0) {
	            List<Credit> lst = new ArrayList<Credit>(lstc.size());
	            for (CreditEntity cre : lstc)
	                lst.add(new Credit(cre));

	            return lst;
	        } else {
	            return null;
	        }
	}

	@Override
	public List<Credit> listCredit(CreditFilter filter) {
		String sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.CreditEntity as c where (1=1)";
		if (filter.getPeopleMainId() != null) {
			sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
		}
		if (filter.getAccountTypeId() != null) {
			sql = sql + " and (c.accountTypeId.id = :accountTypeId)";
		}
		if (filter.getPartnerId() != null) {
			sql = sql + " and (c.partnersId.id = :partners)";
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getFrom() != null) {
			sql = sql + " and (c.creditdatabeg >= :creditDataBegFrom)";
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getTo() != null) {
			sql = sql + " and (c.creditdatabeg < :creditDataBegTo)";
		}
		if (filter.getDataend() != null && filter.getDataend().getFrom() != null) {
			sql = sql + " and (c.creditdataend >= :creditDataEndFrom)";
		}
		if (filter.getDataend() != null && filter.getDataend().getTo() != null) {
			sql = sql + " and (c.creditdataend < :creditDataEndTo)";
		}
		if (filter.getTypeId() != null) {
			sql = sql + " and (c.credittypeId.id = :creditTypes)";
		}
		if (filter.getIsOver() != null) {
			sql = sql + " and (c.isover = :isOver)";
		}
		if (filter.getIsActive() != null) {
			sql = sql + " and (c.isactive = :isActive)";
		}
		if (StringUtils.isNotEmpty(filter.getNomer())) {
			sql = sql + " and (c.creditAccount = :id_credit)";
		}
		if (filter.getIsSameorg() != null) {
			sql = sql + " and (c.issameorg = :isSameOrg)";
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getFrom() != null) {
			sql = sql + " and (c.creditdataendfact >= :creditDataEndFactFrom)";
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getTo() != null) {
			sql = sql + " and (c.creditdataendfact < :creditDataEndFactTo)";
		}
		if (filter.getStatusId() != null) {
			sql = sql + " and (c.creditStatusId.id =:creditStatuses)";
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getFrom() != null) {
			sql = sql + " and (c.creditsum >= :creditSumFrom)";
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getTo() != null) {
			sql = sql + " and (c.creditsum <= :creditSumTo)";
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getFrom() != null) {
			sql = sql + " and (c.creditpercent >= :stakeFrom)";
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getTo() != null) {
			sql = sql + " and (c.creditpercent <= :stakeTo)";
		}
		if (StringUtils.isNotEmpty(filter.getSurname()))
			sql = sql + " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.surname) like :peopleSurname))>0 )";

		if (filter.getWorkPlaceId() != null) {
			sql += " and (c.creditRequestId.workplace.id = :workplace_id)";
		}
		if (filter.getCreditManagerID() != null) {
			sql = sql + " and ((select count(*) from c.creditRequestId.logs as l where l.userId.peopleMainId.id = :creditManagerID and l.eventcodeid.id = 4) > 0)";
		}

		sql = sql + SearchUtil.sortToString(creditSortMapping, filter.getSorting());
		String sortSelect = SearchUtil.sortSelectToString(creditSortMapping, filter.getSorting());
		sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

		if (StringUtils.isEmpty(sortSelect)) {
			sql += " order by c.creditdatabeg desc";
		}
		Query qry = emMicro.createQuery(sql);
		if (filter.getPeopleMainId() != null) {
			qry.setParameter("peopleMainId", filter.getPeopleMainId());
		}
		if (filter.getAccountTypeId() != null) {
			qry.setParameter("accountTypeId", filter.getAccountTypeId());
		}
		if (filter.getPartnerId() != null) {
			qry.setParameter("partners", filter.getPartnerId());
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getFrom() != null) {
			qry.setParameter("creditDataBegFrom", filter.getDatabeg().getFrom(), TemporalType.DATE);
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getTo() != null) {
			qry.setParameter("creditDataBegTo", DateUtils.addDays(filter.getDatabeg().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getDataend() != null && filter.getDataend().getFrom() != null) {
			qry.setParameter("creditDataEndFrom", filter.getDataend().getFrom(), TemporalType.DATE);
		}
		if (filter.getDataend() != null && filter.getDataend().getTo() != null) {
			qry.setParameter("creditDataEndTo", DateUtils.addDays(filter.getDataend().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getTypeId() != null) {
			qry.setParameter("creditTypes", filter.getTypeId());
		}
		if (filter.getIsOver() != null) {
			qry.setParameter("isOver", filter.getIsOver());
		}
		if (filter.getIsActive() != null) {
			qry.setParameter("isActive", (filter.getIsActive() ? ActiveStatus.ACTIVE : ActiveStatus.ARCHIVE));
		}
		if (StringUtils.isNotEmpty(filter.getNomer())) {
			qry.setParameter("id_credit", filter.getNomer());
		}
		if (filter.getIsSameorg() != null) {
			qry.setParameter("isSameOrg", filter.getIsSameorg());
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getFrom() != null) {
			qry.setParameter("creditDataEndFactFrom", filter.getDataendfact().getFrom(), TemporalType.DATE);
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getTo() != null) {
			qry.setParameter("creditDataEndFactTo", DateUtils.addDays(filter.getDataendfact().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getStatusId() != null) {
			qry.setParameter("creditStatuses", filter.getStatusId());
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getFrom() != null) {
			qry.setParameter("creditSumFrom", filter.getCreditsum().getFrom().doubleValue());
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getTo() != null) {
			qry.setParameter("creditSumTo", filter.getCreditsum().getTo().doubleValue());
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getFrom() != null) {
			qry.setParameter("stakeFrom", filter.getCreditStake().getFrom().doubleValue());
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getTo() != null) {
			qry.setParameter("stakeTo", filter.getCreditStake().getTo().doubleValue());
		}
		if (StringUtils.isNotEmpty(filter.getSurname())) {
			String q = filter.isCallCenterOnly() ? filter.getSurname().trim().toUpperCase() : "%" + filter.getSurname().trim().toUpperCase() + "%";
			qry.setParameter("peopleSurname", q);
		}
		if (filter.getWorkPlaceId() != null) {
			qry.setParameter("workplace_id", filter.getWorkPlaceId());
		}
		if (filter.getCreditManagerID() != null) {
			qry.setParameter("creditManagerID", filter.getCreditManagerID());
		}
		if (filter.getFirstRow() >= 0)
			qry.setFirstResult(filter.getFirstRow());
		if (filter.getRows() > 0)
			qry.setMaxResults(filter.getRows());

		List<CreditEntity> lst = null;
		if (filter.getSorting() == null || filter.getSorting().length == 0) {
			lst = qry.getResultList();
		} else {
			List<Object[]> lstSource = qry.getResultList();
			lst = new ArrayList<CreditEntity>(lstSource.size());
			SearchUtil.extractColumn(lstSource, 0, lst);
			lstSource = null;
		}

		if (lst.size() > 0) {
			List<Credit> lst1 = new ArrayList<Credit>(lst.size());
			for (CreditEntity cre : lst) {
				Credit cr = new Credit(cre);
				lst1.add(cr);
				if (filter.getOptions() != null && filter.getOptions().size() > 0) {
					cr.init(filter.getOptions());
				}
			}
			return lst1;
		} else {
			return new ArrayList<Credit>(0);
		}
	}
	
	@Override
	@Deprecated
	public List<Credit> listCredit(int nFirst, int nCount, SortCriteria[] sorting, Set options, 
				Integer peopleMainId, Integer accountTypeId, Integer partners, DateRange creditDataBeg, DateRange creditDataEnd,
				Integer creditTypes, Boolean isOver, String id_credit, Boolean isSameOrg, DateRange creditDataEndFact, Integer creditStatuses,
				MoneyRange creditSum, MoneyRange stake,String surname,Integer isActive) {
		    return listCredit(nFirst,nCount,sorting,options,peopleMainId,accountTypeId,partners,creditDataBeg,creditDataEnd,creditTypes,isOver,
					id_credit,isSameOrg,creditDataEndFact,creditStatuses,creditSum,stake,surname,isActive,null);
   }

	@Override
	@Deprecated
	public List<Credit> listCredit(int nFirst, int nCount, SortCriteria[] sorting, Set options,
								   Integer peopleMainId, Integer accountTypeId, Integer partners, DateRange creditDataBeg, DateRange creditDataEnd,
								   Integer creditTypes, Boolean isOver, String id_credit, Boolean isSameOrg, DateRange creditDataEndFact, Integer creditStatuses,
								   MoneyRange creditSum, MoneyRange stake,String surname,Integer isActive, Integer workplace_id) {
		CreditFilter filter = new CreditFilter();
		filter.setFirstRow(nFirst);
		filter.setRows(nCount);
		filter.setSorting(sorting);
		filter.setOptions(options);
		filter.setPeopleMainId(peopleMainId);
		filter.setAccountTypeId(accountTypeId);
		filter.setPartnerId(partners);
		filter.setDatabeg(creditDataBeg);
		filter.setDataend(creditDataEnd);
		filter.setTypeId(creditTypes);
		filter.setIsOver(isOver);
		filter.setNomer(id_credit);
		filter.setIsSameorg(isSameOrg);
		filter.setDataendfact(creditDataEndFact);
		filter.setStatusId(creditStatuses);
		filter.setCreditsum(creditSum);
		filter.setCreditStake(stake);
		filter.setSurname(surname);
		filter.setIsActive(Utils.triStateToBoolean(isActive));
		filter.setWorkPlaceId(workplace_id);

		return listCredit(filter);
	}

	@Override
	public int countCredit(CreditFilter filter) {
		String sql = "select count(c) from ru.simplgroupp.persistence.CreditEntity as c where (1=1)";
		if (filter.getPeopleMainId() != null) {
			sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
		}
		if (filter.getAccountTypeId() != null) {
			sql = sql + " and (c.accountTypeId.id = :accountTypeId)";
		}
		if (filter.getPartnerId() != null) {
			sql = sql + " and (c.partnersId.id = :partners)";
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getFrom() != null) {
			sql = sql + " and (c.creditdatabeg >= :creditDataBegFrom)";
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getTo() != null) {
			sql = sql + " and (c.creditdatabeg < :creditDataBegTo)";
		}
		if (filter.getDataend() != null && filter.getDataend().getFrom() != null) {
			sql = sql + " and (c.creditdataend >= :creditDataEndFrom)";
		}
		if (filter.getDataend() != null && filter.getDataend().getTo() != null) {
			sql = sql + " and (c.creditdataend < :creditDataEndTo)";
		}
		if (filter.getTypeId() != null) {
			sql = sql + " and (c.credittypeId.id = :creditTypes)";
		}
		if (filter.getIsOver() != null) {
			sql = sql + " and (c.isover = :isOver)";
		}
		if (filter.getIsActive() != null) {
			sql = sql + " and (c.isactive = :isActive)";
		}
		if (StringUtils.isNotEmpty(filter.getNomer())) {
			sql = sql + " and (c.creditAccount = :id_credit)";
		}
		if (filter.getIsSameorg() != null) {
			sql = sql + " and (c.issameorg = :isSameOrg)";
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getFrom() != null) {
			sql = sql + " and (c.creditdataendfact >= :creditDataEndFactFrom)";
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getTo() != null) {
			sql = sql + " and (c.creditdataendfact < :creditDataEndFactTo)";
		}
		if (filter.getStatusId() != null) {
			sql = sql + " and (c.creditStatusId.id =:creditStatuses)";
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getFrom() != null) {
			sql = sql + " and (c.creditsum >= :creditSumFrom)";
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getTo() != null) {
			sql = sql + " and (c.creditsum <= :creditSumTo)";
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getFrom() != null) {
			sql = sql + " and (c.creditpercent >= :stakeFrom)";
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getTo() != null) {
			sql = sql + " and (c.creditpercent <= :stakeTo)";
		}
		if (StringUtils.isNotEmpty(filter.getSurname()))
			sql = sql + " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.surname) like :peopleSurname))>0 )";
		if (filter.getWorkPlaceId() != null) {
			sql += " and (c.creditRequestId.workplace.id = :workplace_id)";
		}
		if (filter.getCreditManagerID() != null) {
			sql = sql + " and ((select count(*) from c.creditRequestId.logs as l where l.userId.peopleMainId.id = :creditManagerID and l.eventcodeid.id = 4) > 0)";
		}

		Query qry = emMicro.createQuery(sql);
		if (filter.getPeopleMainId() != null) {
			qry.setParameter("peopleMainId", filter.getPeopleMainId());
		}
		if (filter.getAccountTypeId() != null) {
			qry.setParameter("accountTypeId", filter.getAccountTypeId());
		}
		if (filter.getPartnerId() != null) {
			qry.setParameter("partners", filter.getPartnerId());
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getFrom() != null) {
			qry.setParameter("creditDataBegFrom", filter.getDatabeg().getFrom(), TemporalType.DATE);
		}
		if (filter.getDatabeg() != null && filter.getDatabeg().getTo() != null) {
			qry.setParameter("creditDataBegTo", DateUtils.addDays(filter.getDatabeg().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getDataend() != null && filter.getDataend().getFrom() != null) {
			qry.setParameter("creditDataEndFrom", filter.getDataend().getFrom(), TemporalType.DATE);
		}
		if (filter.getDataend() != null && filter.getDataend().getTo() != null) {
			qry.setParameter("creditDataEndTo", DateUtils.addDays(filter.getDataend().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getTypeId() != null) {
			qry.setParameter("creditTypes", filter.getTypeId());
		}
		if (filter.getIsOver() != null) {
			qry.setParameter("isOver", filter.getIsOver());
		}
		if (filter.getIsActive() != null) {
			qry.setParameter("isActive", (filter.getIsActive() ? ActiveStatus.ACTIVE : ActiveStatus.ARCHIVE));
		}
		if (StringUtils.isNotEmpty(filter.getNomer())) {
			qry.setParameter("id_credit", filter.getNomer());
		}
		if (filter.getIsSameorg() != null) {
			qry.setParameter("isSameOrg", filter.getIsSameorg());
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getFrom() != null) {
			qry.setParameter("creditDataEndFactFrom", filter.getDataendfact().getFrom(), TemporalType.DATE);
		}
		if (filter.getDataendfact() != null && filter.getDataendfact().getTo() != null) {
			qry.setParameter("creditDataEndFactTo", DateUtils.addDays(filter.getDataendfact().getTo(), 1), TemporalType.DATE);
		}
		if (filter.getStatusId() != null) {
			qry.setParameter("creditStatuses", filter.getStatusId());
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getFrom() != null) {
			qry.setParameter("creditSumFrom", filter.getCreditsum().getFrom().doubleValue());
		}
		if (filter.getCreditsum() != null && filter.getCreditsum().getTo() != null) {
			qry.setParameter("creditSumTo", filter.getCreditsum().getTo().doubleValue());
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getFrom() != null) {
			qry.setParameter("stakeFrom", filter.getCreditStake().getFrom().doubleValue());
		}
		if (filter.getCreditStake() != null && filter.getCreditStake().getTo() != null) {
			qry.setParameter("stakeTo", filter.getCreditStake().getTo().doubleValue());
		}
		if (StringUtils.isNotEmpty(filter.getSurname())) {
			String q = filter.isCallCenterOnly() ? filter.getSurname().trim().toUpperCase() : "%" + filter.getSurname().trim().toUpperCase() + "%";
			qry.setParameter("peopleSurname", q);
		}
		if (filter.getWorkPlaceId() != null) {
			qry.setParameter("workplace_id", filter.getWorkPlaceId());
		}
		if (filter.getCreditManagerID() != null) {
			qry.setParameter("creditManagerID", filter.getCreditManagerID());
		}

		List lst = qry.getResultList();
		if (lst.size() == 0 || lst.get(0) == null) {
			return 0;
		} else {
			return ((Number) lst.get(0)).intValue();
		}
	}

	@Override
	@Deprecated
	public int countCredit(Integer peopleMainId, Integer accountTypeId, Integer partners, DateRange creditDataBeg, DateRange creditDataEnd,
	                       Integer creditTypes, Boolean isOver, String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,Integer creditStatuses,
	                       MoneyRange creditSum, MoneyRange stake,String surname,Integer isActive) {
		return countCredit(peopleMainId,accountTypeId,partners,creditDataBeg,creditDataEnd,creditTypes,isOver,
				id_credit,isSameOrg,creditDataEndFact,creditStatuses,creditSum,stake,surname,isActive,null);
	}

	@Override
	@Deprecated
	public int countCredit(Integer peopleMainId, Integer accountTypeId, Integer partners, DateRange creditDataBeg, DateRange creditDataEnd,
						   Integer creditTypes, Boolean isOver, String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,Integer creditStatuses,
						   MoneyRange creditSum, MoneyRange stake,String surname,Integer isActive, Integer workplace_id) {

		CreditFilter filter = new CreditFilter();
		filter.setPeopleMainId(peopleMainId);
		filter.setAccountTypeId(accountTypeId);
		filter.setPartnerId(partners);
		filter.setDatabeg(creditDataBeg);
		filter.setDataend(creditDataEnd);
		filter.setTypeId(creditTypes);
		filter.setIsOver(isOver);
		filter.setNomer(id_credit);
		filter.setIsSameorg(isSameOrg);
		filter.setDataendfact(creditDataEndFact);
		filter.setStatusId(creditStatuses);
		filter.setCreditsum(creditSum);
		filter.setCreditStake(stake);
		filter.setSurname(surname);
		filter.setIsActive(Utils.triStateToBoolean(isActive));
		filter.setWorkPlaceId(workplace_id);

		return countCredit(filter);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void rejectCredit(int id) throws KassaException {
        CreditEntity ent = creditDAO.getCreditEntity(id);
        if (ent!=null){
            ent.setCreditStatusId(refBooks.getCreditStatusType(Credit.CREDIT_CANCELLED).getEntity());
            ent.setIsover(true);
            ent.setDateStatus(new Date());
            ent = creditDAO.saveCreditEntity(ent);
        
            List<PaymentEntity> lstPay = payDAO.getPaymentsByCredit(id, Payment.FROM_SYSTEM);
            for (PaymentEntity pay: lstPay) {
        	    pay.setStatus(PaymentStatus.DELETED);
        	    payDAO.savePayment(pay);
            }
        } else {
        	logger.severe("Не удалось найти кредит "+id);
        }
	}	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeCredit(int id) throws KassaException {

	        CreditEntity ent = creditDAO.getCreditEntity(id);
	        if (ent != null) {
	        	creditDAO.removeCredit(id);
	        }
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Credit findCreditActive(int peopleMainId, Set options) throws KassaException {
	    
		Credit credit=creditActive(peopleMainId);
		if (credit!=null){
			credit.init(options);
			return credit;
		}
		return null;
    }
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void closeCredit(CreditEntity credit, Date dateClose, Double sumOverPayment,
			Integer paymentId,Double sumBack,PaymentEntity systemPay,
			PaymentEntity bonusPay,Double sumMinPayment) {
        
		double sumSystem=0d;
	  	double sumBonusPay=0d;
	  	if (systemPay!=null){
	  	   sumSystem=systemPay.getAmount();
	  	}
	  	if (bonusPay!=null){
	  	   sumBonusPay=bonusPay.getAmount();
	  	}
	  	   
	 	//проценты по платежу
    	double sumDiff=0d;
    	//сумма процентов по платежу системы
	    double sumDiffSystem=sumSystem;
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
    	
		//пишем детали кредита, ставим в оставшиеся суммы нули
	    try {
	    	//пишем детали о сумммах из кошелька, если есть
        	if (sumSystem>0){
        	    saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, dateClose, 
        	    	null, 0d, sumDiffSystem, 0d, sumSystem,null,systemPay.getId());
           	}
        	//пишем детали о суммах бонусов, если они есть
        	if (sumBonusPay>0){
        	    saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, dateClose, 
        		   	null, 0d, sumBonusPay, 0d, sumBonusPay,null,bonusPay.getId());
        		
        	}
			saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, dateClose, null,new Double(0), sumDiff, 
					new Double(0), sumBack,new Double(0),paymentId,0,Math.abs(sumOverPayment));
		} catch (Exception e) {
			logger.severe("Не удалось записать данные в creditDetail");
		}
	    
		Integer crequestId = credit.getCreditRequestId().getId();
        CreditRequestEntity cre = crDAO.getCreditRequestEntity(crequestId);
        cre.setDateStatus(dateClose);
               
        credit.setCreditdataendfact(dateClose);
        
        // если осталась ненулевая сумма, положим ее в копилку
        if (sumOverPayment != 0) {
        	
        	    savePeopleSum(credit.getPeopleMainId().getId(),credit,BaseCredit.OPERATION_IN,
        	    		PeopleSums.CREDIT_OVERPAY,Math.abs(sumOverPayment),dateClose);
          
        }
        
        credit.setCreditsumdebt(null);
              
        //поставим в сумму возврата сумму платежей по данному кредиту
        double sumBackAll=paymentServ.getCreditPaymentSum(credit.getId())-(sumOverPayment!=null?Math.abs(sumOverPayment):new Double(0));
        credit.setCreditsumback(sumBackAll);
            
        credit.setIsover(true);
        credit.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_CLOSED).getEntity());
        credit.setDateStatus(dateClose);
        //если была просрочка
        if (credit.getMaxDelay()!=null){
        	//закрытая просрочка до 5 дней
        	if (credit.getMaxDelay()<6){
        	    credit.setDelay5(Utils.defaultIntegerFromNull(credit.getDelay5())+1);
        	    credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
        	}else if (credit.getMaxDelay()>=6&&credit.getMaxDelay()<30){
        		//закрытая просрочка до 30
        		credit.setDelay30(Utils.defaultIntegerFromNull(credit.getDelay30())+1);
        		credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
        	}else if (credit.getMaxDelay()>=30&&credit.getMaxDelay()<60){
        		//закрытая просрочка до 60
        		credit.setDelay60(Utils.defaultIntegerFromNull(credit.getDelay60())+1);
        		credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
        	}else if (credit.getMaxDelay()>=60&&credit.getMaxDelay()<90){
        		//закрытая просрочка до 90
        		credit.setDelay90(Utils.defaultIntegerFromNull(credit.getDelay90())+1);
        		credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
        	}else if (credit.getMaxDelay()>=90&&credit.getMaxDelay()<120){
        		//закрытая просрочка до 120
        		credit.setDelaymore(Utils.defaultIntegerFromNull(credit.getDelay120())+1);
        		credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
        	} else if (credit.getMaxDelay()>=120){
        		//закрытая просрочка более 120
        		credit.setDelaymore(Utils.defaultIntegerFromNull(credit.getDelaymore())+1);
        		credit.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_FOUR_MONTH));
        	}
        }
        //кредит закрыт, текущей просрочки нет
        credit.setCurrentOverdueDebt(null);
        credit.setCreditDateDebt(null);
        credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.WITHOUT_OVERDUE).getEntity());
        //закрываем график
        paymentServ.closePaymentSchedule(credit, ReasonEnd.CREDIT_END, dateClose);
        //закрываем заявку
        cre.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.CLOSED));

        creditDAO.saveCreditEntity(credit);
        emMicro.merge(cre);

		// если кредит был у коллектора, меняем статус на архивный
		collectorDAO.changeCollectorStatus(credit.getId(), credit.getPeopleMainId().getId(), ActiveStatus.ARCHIVE);
		// все задания для коллектора удаляем
		collectorDAO.removeTasksByCreditID(credit.getId());

        //пишем лог о закрытии кредита
        try {
			eventLog.saveLog(EventType.INFO, EventCode.CREDIT_CLOSE, "Был успешно закрыт кредит", 
				   null,credit.getCreditdataendfact(), crDAO.getCreditRequestEntity(credit.getCreditRequestId().getId()), 
				   peopleDAO.getPeopleMainEntity(credit.getPeopleMainId().getId()), credit);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить лог о закрытии кредита");
		}
 	    
        
        //кредит закрыли досрочно и не было продлений
        if (credit.getCreditdataendfact().before(credit.getCreditdataend())&&
        		(credit.getCreditlong()==null||credit.getCreditlong()==0)) {

		   // senderServ.fireEvent(new Credit(credit), EventCode.EARLY_CLOSE_CREDIT, Utils.mapOfSO());
		    // TODO поменять 
            senderServ.fireEvent(new Credit(credit), EventCode.CLOSE_CREDIT_WITHOUT_DELAY, Utils.mapOfSO());
        } else {

            boolean closedWithoutDelay=false;
            CreditDetailsEntity creditInform=null;
            //ищем событие выдачи кредита
            List<CreditDetailsEntity> lstCreditInfo=findCreditDetails(credit.getId(), BaseCredit.OPERATION_CREDIT, null);
            if (lstCreditInfo.size()>0){
            	creditInform=lstCreditInfo.get(0);                    	
            }
            //ищем первое продление
            List<ProlongEntity> lst = prolongDAO.findProlong(credit.getId(), null, ActiveStatus.ACTIVE);
            ProlongEntity prolong=null;
            if (lst.size()>0){
        	    prolong=lst.get(lst.size()-1);
            }
            //если продления были
            if (prolong != null) {
                closedWithoutDelay = new DateTime(prolong.getLongdate()).withTimeAtStartOfDay()
                        .isBefore(creditInform.getEventEndDate().getTime());
            } else {
                closedWithoutDelay = new DateTime(credit.getCreditdataendfact()).withTimeAtStartOfDay()
                        .isBefore(credit.getCreditdataend().getTime());
            }
            //если закрыли без просрочки
            if (closedWithoutDelay) {
                senderServ.fireEvent(new Credit(credit), EventCode.CLOSE_CREDIT_WITHOUT_DELAY, Utils.mapOfSO());
             
            } else {
            	// TODO поменять на общее закрытие кредита 
                senderServ.fireEvent(new Credit(credit), EventCode.CLOSE_CREDIT_WITHOUT_DELAY, Utils.mapOfSO());
                
            }
        }
        
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCreditStatus(int creditId, Integer statusId, Date statusDate) {
		  CreditEntity ent = creditDAO.getCreditEntity(creditId);
	      if (ent != null) {
	         if (statusId!=null){
	        	 ent.setCreditStatusId(refBooks.getCreditStatusType(statusId).getEntity());
	         }
	         ent.setDateStatus(statusDate);
	         creditDAO.saveCreditEntity(ent);
	      }
		
	}

	@Override
	public ProlongEntity getProlongEntity(int prolongId) {
		return prolongDAO.getProlongEntity(prolongId);
	}
	
	@Override
	public Prolong getProlong(int prolongId, Set options) {
		return prolongDAO.getProlong(prolongId, options);
	}

	@Override
	public ProlongEntity findProlongDraft(int creditId) {
		return prolongDAO.findProlongDraft(creditId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOverdueSum(Integer creditId, Date dt) {
		
		logger.info("Считаем просроченные суммы по кредиту "+creditId);
		CreditEntity credit= creditDAO.getCreditEntity(creditId);
		
		if (credit==null) {
			logger.severe("Не найден кредит "+creditId);
			return;
		}
		
		//посчитаем данные
		Map<String, Object> resCalc = creditCalc.calcCredit(creditId, dt);			
		Double sm = CalcUtils.roundFloor(Convertor.toDouble(resCalc.get(CreditCalculatorBeanLocal.SUM_BACK),CalcUtils.dformat),0);
		
		//*******проверим, как считается
		//ставка текущая
		Double stake=(Double) resCalc.get(CreditCalculatorBeanLocal.STAKE_CURRENT);
		logger.info("stake "+stake);
		//ставка штрафа
		Double stakePenalty=CalcUtils.roundFloor(stake==null?credit.getCreditpercent():stake-credit.getCreditpercent(),10);
		logger.info("stakePenalty "+stakePenalty);
		//сумма общая
		logger.info("sumBackOld "+credit.getCreditsumback());
		logger.info("sumMain "+credit.getSumMainRemain());
		logger.info("sumBackNew "+sm);
		//сумма штрафов
		Double sumPenalty=CalcUtils.roundFloor((Double) resCalc.get(CreditCalculatorBeanLocal.SUM_PENALTY),2);
		//********
		if (sm!=null){
		  credit.setCreditsumback(sm);
		  credit.setMaxOverdueDebt(sm);
		  credit.setCurrentOverdueDebt(sm);
		}
		
		//ставим дату статуса, чтобы не было проблем с выгрузкой 
		credit.setDateStatus(new Date());
		
		//ставим статус, если кредит не передали коллектору
		if (credit.getCreditStatusId().getCodeinteger()!=BaseCredit.CREDIT_INNER_COLLECTOR){
		    credit.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_OVERDUE).getEntity());
		}
		
		//пишем сколько дней просрочки
		credit.setMaxDelay(Convertor.toInteger(resCalc.get(CreditCalculatorBeanLocal.DAYS_OVERDUE)));
	
		//поставим статус просрочки
		if (credit.getMaxDelay()!=null&&credit.getMaxDelay()<30){
			//до месяца
			credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.OVERDUE_BEFORE_MONTH).getEntity());
			
		} else if (credit.getMaxDelay()>=30&&credit.getMaxDelay()<60){
			//до 2 месяцев
			credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.OVERDUE_MONTH).getEntity());
			
		} else if (credit.getMaxDelay()>=60&&credit.getMaxDelay()<90){
			//до 3 месяцев
			credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.OVERDUE_TWO_MONTH).getEntity());
			
		} else if (credit.getMaxDelay()>=90&&credit.getMaxDelay()<120){
			//до 4 месяцев
			credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.OVERDUE_THREE_MONTH).getEntity());
			
		} else if (credit.getMaxDelay()>=120){
			//свыше 4 месяцев
			credit.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.OVERDUE_FOUR_MONTH).getEntity());
			
		}
		credit = creditDAO.saveCreditEntity(credit);
		
		//пишем детали кредита
        try {
			CreditDetailsEntity creditDetail=saveCreditDetail(credit, BaseCredit.OPERATION_OVERDUE, 
					new Date(), null,credit.getSumMainRemain(), null, credit.getCreditsumback(), 
					null,sumPenalty,null,credit.getMaxDelay(),null);
			//пишем основную сумму кредита
	        saveCreditSum(credit.getId(), BaseCredit.SUM_MAIN, null, 
	        		new Date(), credit.getSumMainRemain(), creditDetail.getId());
	        //пишем общую сумму кредита
	        saveCreditSum(credit.getId(), BaseCredit.SUM_BACK, null, 
	        		new Date(), sm, creditDetail.getId());
	        //пишем сумму процентов кредита вместе со штрафами
	        saveCreditSum(credit.getId(), BaseCredit.SUM_PERCENT, null, 
	        		new Date(), sm-credit.getSumMainRemain(), creditDetail.getId());  
	        if (sumPenalty>0){
	        	//пишем сумму штрафов
		        saveCreditSum(credit.getId(), BaseCredit.SUM_PENALTY, null, 
		        		new Date(), sumPenalty, creditDetail.getId()); 
	        }
		} catch (Exception e) {
			logger.severe("Не удалось записать данные в creditDetail и creditSum по кредиту id "+credit.getId());
		}
        
		//запишем сумму в график платежей
		RepaymentScheduleEntity sched = payDAO.findScheduleActive(credit.getId());
		if (sched!=null){
			sched.setCreditsumback(sm);
			sched = payDAO.saveSchedule(sched);
		}	
		logger.info("Успешно посчитали просроченные суммы по кредиту "+creditId);
	} 
	
	@Override
	public List<CreditEntity> findCredits(PeopleMainEntity peopleMain,
			CreditRequestEntity creditRequest, PartnersEntity partner,
			Boolean sameOrg, ReferenceEntity status) {
		String hql = "from ru.simplgroupp.persistence.CreditEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partner != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }   
        if (sameOrg != null) {
            hql = hql + " and (issameorg = :issameorg)";
        }
        if (status != null) {
            hql = hql + " and (creditStatusId.codeinteger = :status)";
        }
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partner != null) {
            qry.setParameter("partner", partner.getId());
        }    
        if (sameOrg != null) {
            qry.setParameter("issameorg", sameOrg);
        }
        if (status != null) {
            qry.setParameter("status", status.getCodeinteger());
        }
    List<CreditEntity> lst=qry.getResultList();
    
   	return lst;
   
}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeCredit(Credit cr, PeopleMain pmain, Integer crequestId,int partnerId)
			throws PeopleException {
		
	    int id=pmain.getId();
	    PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(id);
	    CreditRequestEntity cre=crDAO.getCreditRequestEntity(crequestId);
        //проверяем, есть ли кредит
        List<CreditEntity> lstcred = findCredits(pMain, cre, refBooks.getPartnerEntity(partnerId), false, null);
        
        CreditEntity crednew=null;
        
        if (lstcred.size()!=0){
           crednew=lstcred.get(0);
        }
        //если данные совпадают, возвращаемся
        if (crednew!=null){
        	if (cr.equalsContent(crednew)){
        		return;
        	}	
        }
        //добавим новый кредит
        CreditEntity credit=newCredit(crednew,pMain.getId(),crequestId,partnerId,cr.getIsSameOrg(),
        		cr.getOverdueState()==null?null:cr.getOverdueState().getCode(),
        		cr.getCreditOrganization()==null?null:cr.getCreditOrganization().getId(),
        		cr.getCreditSum(),cr.getCreditDataBeg(),cr.getIsOver(),
        		cr.getCreditType()==null?null:cr.getCreditType().getCodeInteger(),
        		cr.getCurrency()==null?null:cr.getCurrency().getCodeInteger(),
        		cr.getCreditRelation()==null?null:cr.getCreditRelation().getCodeInteger(),
        		cr.getCreditPurpose()==null?null:cr.getCreditPurpose().getCodeInteger(),
        		cr.getCreditDataEnd(),cr.getCreditDataEndFact(),
        		cr.getCreditMonthlyPayment());
        
        List<CreditEntity> creditList=findCredits(pMain, cre, null, null, null);
        VerificationEntity verification=null;
		if (credit.getPartnersId().getId()==Partner.CLIENT){
			verification=creditInfo.findOneVerification(cre.getId(), pMain.getId(), Partner.SYSTEM, null);
		}
        //если есть кредиты от партнеров по заявке
        if (creditList.size()>0){
        	for (CreditEntity cred:creditList){
        		if (cred.getPartnersId().getId()==Partner.CLIENT){
        			continue;
        		} else {
        			//пишем системную верификацию
        			verification=creditInfo.saveSystemVerification(verification, cre.getId(), pMain.getId(), 
                			credit, cred,VerificationConfig.MARK_FOR_BANK);
        		}
        	}
        }//end если есть кредиты от партнеров
     		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void creditToCollector(int creditId, Date date) {
		logger.info("Передаем внутреннему коллектору кредит " + creditId);
		
		changeCreditStatus(creditId,Credit.CREDIT_INNER_COLLECTOR,
				CreditStatus.FOR_COLLECTOR,date,EventCode.CREDIT_TO_COLLECTOR);
	
		// берем коллектора у которого меньше всего клиентов
		UsersEntity collector = usersDAO.getFreeCollector();
		if (collector != null) {
			CreditEntity credit = creditDAO.getCreditEntity(creditId);
			PeopleMainEntity user = credit.getPeopleMainId();
			// берем все просрочки, которые попали к коллекторам
			List<CollectorEntity> delaysBeforeList = collectorDAO.getDelaysBefore(user.getId());

			// складываем нужные параметры для посчета
			Map<String, Object> params = productBeanLocal.getCollectorProductConfig(credit.getProductId().getId());
			params.put(CollectorKeys.IS_FIRST_DELAY, delaysBeforeList.isEmpty());
			if (delaysBeforeList.size() > 0) {
				CollectorEntity collectorEntity = delaysBeforeList.get(0);
				params.put(CollectorKeys.MAX_COLLECTOR_TYPE, collectorEntity.getCollectionTypeId().getName());
			}

			// вычисляем вид коллекторской деятельности по правилам
			NameValueRuleResult ruleResult = rules.calcCreditToCollector(new Credit(credit), params);
			String collectorTypeStr = (String) ruleResult.getValue(CollectorKeys.COLLECTOR_TYPE);

			// создаем запись в коллекторах
			collectorDAO.newCollectorRecord(user.getId(), credit.getId(), collector.getId(), 
					collectorTypeStr.toUpperCase(), new Date(), ActiveStatus.DRAFT, null);
			logger.info("Передали внутреннему коллектору кредит " + creditId);
		} else {
			logger.severe("Не найдено ни одного коллектора для передачи кредита "+creditId);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void creditToOuterCollector(int creditId,Date date) {
		logger.info("Передаем внешнему коллектору кредит "+creditId);
		//поменяли статус кредита
		CreditEntity credit = changeCreditStatus(creditId,Credit.CREDIT_COLLECTOR,
				CreditStatus.FOR_COLLECTOR,date,EventCode.CREDIT_TO_OUTER_COLLECTOR);
		
		//записываем запись коллектора в таблицу с долгами
		try {
			creditInfo.newDebt(credit.getCreditRequestId().getId(), credit.getPeopleMainId().getId(), Partner.SYSTEM, 
					creditId,credit.getCreditsumback(), null,"неоплата по займу "+credit.getId(), "", new Date(), 
					"", "", "", null, Debt.DEBT_COLLECTOR,null,ActiveStatus.ACTIVE);
		} catch (Exception e) {
			logger.severe("Не удалось добавить запись в таблицу с долгами по кредиту "+credit.getId());
		}
		
		logger.info("Передали внешнему коллектору кредит "+creditId);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void creditToCourt(int creditId,Date date) {
		logger.info("Передаем кредит "+creditId + " в суд");
		
		CreditEntity credit = changeCreditStatus(creditId,BaseCredit.CREDIT_COURT,
				CreditStatus.FOR_COURT,date,EventCode.CREDIT_TO_COURT);
		
		//записываем запись в таблицу с долгами, после решения из суда будем в нее добавлять
		try {
			creditInfo.newDebt(credit.getCreditRequestId().getId(), credit.getPeopleMainId().getId(), Partner.SYSTEM, 
					creditId,credit.getCreditsumback(),null, "неоплата по займу "+credit.getId(), "", new Date(), 
					"", "", "", null, Debt.DEBT_COURT,null,ActiveStatus.ACTIVE);
		} catch (Exception e) {
			logger.severe("Не удалось добавить запись в таблицу с долгами по кредиту "+credit.getId());
		}
		
		logger.info("Передали кредит "+creditId + " в суд");
	}	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditEntity changeCreditStatus(int creditId,int statusId,
			Integer crequestStatusId,Date date,Integer eventCode) {
		CreditEntity credit = creditDAO.getCreditEntity(creditId);
		if (credit==null){
			logger.severe("Не найден кредит "+creditId);
			return null;
		}
		CreditRequestEntity crequest = credit.getCreditRequestId();
		
		//ставим статус у кредита, если он еще не был поставлен
		if(credit.getCreditStatusId().getCodeinteger() != statusId){
		    credit.setCreditStatusId(refBooks.getCreditStatusType(statusId).getEntity());
		    credit.setDateStatus(date);
		
		    //ставим статус у заявки
		    if (crequest!=null&&crequestStatusId!=null){
		      crequest.setStatusId(refBooks.getCreditRequestStatus(crequestStatusId));
		      crequest.setDateStatus(date);
		      emMicro.persist(crequest);
		    }
		    credit = creditDAO.saveCreditEntity(credit);
		    //если есть код события, запишем в лог
		    if (eventCode!=null){
		    	try {
		    		eventLog.saveLog(EventType.INFO, eventCode, "Смена статуса кредита ", null, date, 
		    				crequest, credit.getPeopleMainId(), credit);
		    	} catch (Exception e){
		    		logger.severe("Не удалось записать лог по смене статуса кредита "+credit.getId());
		    	}
		    }
		}
		return credit;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void creditToCollector(int creditId) {
		creditToCollector(creditId,new Date());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void stopCredit(int creditId,Date date, StateRef stopPoint) {
		
		CreditEntity cred = creditDAO.getCreditEntity(creditId);
		if (cred==null){
			logger.severe("Не удалось остановить кредит, не найден кредит с id "+creditId);
			return;
		}
		cred.setIsactive(ActiveStatus.ARCHIVE);
		cred = creditDAO.saveCreditEntity(cred);
	
		//пишем лог
        try {
			eventLog.saveLog(EventType.INFO,EventCode.STOP_CREDIT, (stopPoint == null)?null:stopPoint.toString(), 
				 null, date, cred.getCreditRequestId(), cred.getPeopleMainId(), cred);
		} catch (KassaException e) {
			logger.severe("Не удалось сохранить лог по приостановке кредита "+creditId);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void restartCredit(int creditId,Date date) {
		
		CreditEntity cred = creditDAO.getCreditEntity(creditId);
		if (cred==null){
			logger.severe("Не удалось перезапустить кредит, не найден кредит с id "+creditId);
			return;
		}
		cred.setIsactive(ActiveStatus.ACTIVE);
		
		cred = creditDAO.saveCreditEntity(cred);
			
		//пишем лог
        try {
			eventLog.saveLog(EventType.INFO,EventCode.RESTART_CREDIT, "перезапуск займа", 
				 null,date,cred.getCreditRequestId(), cred.getPeopleMainId(), cred);
		} catch (KassaException e) {
			logger.severe("Не удалось сохранить лог по рестарту кредита "+creditId);
		}
		
	}

	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void deleteCredit(int creditId)  {
		 creditDAO.removeCredit(creditId);
		 // TODO fire event

     }


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePeopleSum(Integer peopleMainId, CreditEntity credit,
			Integer operation, Integer operationType, Double amount,Date date) {
		
		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		if (peopleMain==null){
			logger.severe("Не нашелся человек "+peopleMainId);
			return;
		}
		//пишем в базу остаток на счет человека
		PeopleSumsEntity peopleSums=new PeopleSumsEntity();
		peopleSums.setCreditId(credit);
		peopleSums.setPeopleMainId(peopleMain);
		peopleSums.setAmount(amount);
		peopleSums.setEventDate(date);
		peopleSums.setOperationId(refBooks.getOperation(operation).getEntity());
		peopleSums.setOperationTypeId(refBooks.getOperationType(operationType).getEntity());
		peopleSums=emMicro.merge(peopleSums);
		emMicro.persist(peopleSums);
		
	}

	@Override
	public List<PeopleSums> listPeopleSums(Integer peopleMainId, Integer sumId) {
		String hql="from ru.simplgroupp.persistence.PeopleSumsEntity where peopleMainId.id=:peopleMainId";
		if (sumId!=null){
			hql=hql+" and operationId.codeinteger=:sumId ";
		}
		hql=hql+" order by eventDate ";
		Query qry=emMicro.createQuery(hql);
		qry.setParameter("peopleMainId", peopleMainId);
		if (sumId!=null){
			qry.setParameter("sumId", sumId);
		}
		List<PeopleSumsEntity> lst=qry.getResultList();
		if (lst.size()>0){
			List<PeopleSums> lst1=new ArrayList<PeopleSums>(lst.size());
	    	for (PeopleSumsEntity psums:lst) {
	    		PeopleSums psum = new PeopleSums(psums); 
	    		lst1.add(psum);
	    	}
	    	return lst1;
		} else {
		    return new ArrayList<PeopleSums>(0);
		}
	}

	@Override
	public Boolean checkPeopleSums(Integer credit_id, DateRange date, Double amount){
		String hql="from ru.simplgroupp.persistence.PeopleSumsEntity where creditId.id=:credit_id and eventDate >= :dateFrom and eventDate <= :dateTo and amount=:amount";
		List<PeopleSumsEntity> lst=JPAUtils.getResultListFromSql(emMicro, hql, Utils.mapOf("credit_id", credit_id,
				"dateFrom", date.getFrom(),
				"dateTo", date.getTo(),
				"amount", amount));
		return (lst.size()>0);
	}
    
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditDetailsEntity saveCreditDetail(CreditEntity creditId, Integer operationId,
			Date eventDate, Date eventEndDate,Double amountMain, Double amountPercent,
			Double amountAll,Double amountOperation,Double amountPenalty,
			Integer anotherId) throws KassaException{
		  
		    return saveCreditDetail(creditId, operationId,
					eventDate, eventEndDate,amountMain, amountPercent,
					amountAll,amountOperation,amountPenalty,anotherId,0,0d);
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditDetailsEntity saveCreditDetail(CreditEntity creditId, Integer operationId,
			Date eventDate, Date eventEndDate,Double amountMain, Double amountPercent,
			Double amountAll,Double amountOperation,Double amountPenalty,
			Integer anotherId,Integer delay,Double amountOverpay) throws KassaException{
		
		    CreditDetailsEntity creditDetail=new CreditDetailsEntity();
		    creditDetail.setCreditId(creditId);
		    creditDetail.setEventDate(eventDate);
		    creditDetail.setEventEndDate(eventEndDate);
		    creditDetail.setAmountAll(amountAll);
		    creditDetail.setAmountMain(amountMain);
		    creditDetail.setAmountOperation(amountOperation);
		    creditDetail.setAmountPercent(amountPercent);
		    creditDetail.setAmountPenalty(amountPenalty);
		    creditDetail.setAmountOverpay(amountOverpay);
		    creditDetail.setAnotherId(anotherId);
		    creditDetail.setDelay(delay);
		    Reference operation=refBooks.getCreditDetailsOperation(operationId);
		    if (operation!=null){
		    	creditDetail.setOperationId(operation.getEntity());
		    } else {
		    	logger.severe("Не удалось найти в справочнике операцию с кодом "+operationId);
		    	throw new KassaException("Не удалось найти в справочнике операцию с кодом "+operationId);
		    }
		    return creditDAO.saveCreditDetails(creditDetail);
		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<CreditDetailsEntity> findCreditDetails(Integer creditId,
			Integer operationId, DateRange eventDate) {
		String sql = "from ru.simplgroupp.persistence.CreditDetailsEntity where (1=1) ";
        if (eventDate != null && eventDate.getFrom() != null) {
        	sql+= " and (eventDate >= :eventDateFrom)";
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	sql+= " and (eventDate <= :eventDateTo)";
        }
        if (operationId!=null){
        	sql+=" and operationId.codeinteger=:operationId ";
        }
        if (creditId!=null){
            sql+=" and creditId.id=:creditId ";
        }
        sql+=" order by eventDate desc ";
        Query qry=emMicro.createQuery(sql);
        if (eventDate != null && eventDate.getFrom() != null) {
        	qry.setParameter("eventDateFrom", eventDate.getFrom());
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	qry.setParameter("eventDateTo", eventDate.getTo());
        }
        if (operationId!=null){
        	qry.setParameter("operationId", operationId);
        }
        if (creditId!=null){
            qry.setParameter("creditId", creditId);
        }
		return qry.getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditDetailsEntity findCreditOperation(Integer creditId,DateRange eventDate){
		return (CreditDetailsEntity) Utils.firstOrNull(findCreditDetails(creditId,BaseCredit.OPERATION_CREDIT,eventDate));
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<CreditDetailsEntity> findCreditDetailsWithUnit(Integer creditId, Integer operationId, DateRange eventDate){
		String sql = "from ru.simplgroupp.persistence.CreditDetailsEntity where (1=1) ";
		if (eventDate != null && eventDate.getFrom() != null) {
			sql+= " and (eventDate >= :eventDateFrom)";
		}
		if (eventDate != null && eventDate.getTo() != null) {
			sql+= " and (eventDate <= :eventDateTo)";
		}
		if (operationId!=null){
			sql+=" and operationId.codeinteger=:operationId ";
		}
		if (creditId!=null){
			sql+=" and creditId.id=:creditId ";
		}
		sql += " and creditId.creditRequestId.workplace is not null ";
		sql+=" order by eventDate desc ";
		Query qry=emMicro.createQuery(sql);
		if (eventDate != null && eventDate.getFrom() != null) {
			qry.setParameter("eventDateFrom", eventDate.getFrom());
		}
		if (eventDate != null && eventDate.getTo() != null) {
			qry.setParameter("eventDateTo", eventDate.getTo());
		}
		if (operationId!=null){
			qry.setParameter("operationId", operationId);
		}
		if (creditId!=null){
			qry.setParameter("creditId", creditId);
		}
		return qry.getResultList();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditDetailsEntity findLastCreditDetail(Integer creditId, DateRange eventDate) {
		String sql = "from ru.simplgroupp.persistence.CreditDetailsEntity where (creditId.id = :creditId) and (operationId.codeinteger=:operationId) "
				+ "and (amountMain=:amountMain) and (amountAll=:amountAll) and amountPercent<>amountOperation ";
        if (eventDate != null && eventDate.getFrom() != null) {
        	sql+= " and (eventDate >= :eventDateFrom)";
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	sql+= " and (eventDate <= :eventDateTo)";
        }
        Query qry=emMicro.createQuery(sql);
        if (eventDate != null && eventDate.getFrom() != null) {
        	qry.setParameter("eventDateFrom", eventDate.getFrom());
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	qry.setParameter("eventDateTo", eventDate.getTo());
        }
        qry.setParameter("creditId", creditId);
       	qry.setParameter("operationId", BaseCredit.OPERATION_PAYMENT);
       	qry.setParameter("amountAll", new Double(0));
       	qry.setParameter("amountMain", new Double(0));
       	
		List<CreditDetailsEntity> lst= qry.getResultList();
		return (CreditDetailsEntity) Utils.firstOrNull(lst);
	
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Double getSumCreditDetail(String sumName, Integer creditId,
			Integer operationId, DateRange eventDate) {
		String sql="select sum("+sumName+") from ru.simplgroupp.persistence.CreditDetailsEntity where (creditId.id = :creditId) ";
		if (eventDate != null && eventDate.getFrom() != null) {
        	sql+= " and (eventDate >= :eventDateFrom)";
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	sql+= " and (eventDate <= :eventDateTo)";
        }
        if (operationId!=null){
        	sql+=" and operationId.codeinteger=:operationId ";
        }
        Query qry=emMicro.createQuery(sql);
        qry.setParameter("creditId", creditId);
        if (eventDate != null && eventDate.getFrom() != null) {
        	qry.setParameter("eventDateFrom", eventDate.getFrom());
        }
        if (eventDate != null && eventDate.getTo() != null) {
        	qry.setParameter("eventDateTo", eventDate.getTo());
        }
        if (operationId!=null){
        	qry.setParameter("operationId", operationId);
        }
        List<Double> lst=qry.getResultList();
		if (lst.size() == 0||lst.get(0)==null) {
			return new Double(0);
		} else {
			return lst.get(0);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeCreditStart(Integer id, Date processDate,Integer paymentId) {
		CreditEntity cred = creditDAO.getCreditEntity(id);
		//разница во времени между датой начала кредита и датой проведения платежа
		long shift = processDate.getTime() - cred.getCreditdatabeg().getTime();
		//меняем даты начала-окончания у кредита
		cred.setCreditdatabeg(processDate);
		cred.setDateStatus(processDate);
		cred.setCreditdataend(new Date(cred.getCreditdataend().getTime() + shift));
		
		creditDAO.saveCreditEntity(cred);
		
		//ищем запись в creditdetails и меняем даты начала-окончания кредита
		List<CreditDetailsEntity> lstDet = findCreditDetails(id, Credit.OPERATION_CREDIT,null);
		if (lstDet.size()>0){
			CreditDetailsEntity creditDetail=lstDet.get(0);
			if (creditDetail.getEventDate() != null) {
				creditDetail.setEventDate( new Date( creditDetail.getEventDate().getTime() + shift ));
			}
			if (creditDetail.getEventEndDate() != null) {
				creditDetail.setEventEndDate(new Date( creditDetail.getEventEndDate().getTime() + shift ));
			}
			creditDetail.setAnotherId(paymentId);
			creditDAO.saveCreditDetails(creditDetail);
		
		}
		
		//ищем запись в графике платежей и меняем даты начала-окончания кредита
		RepaymentScheduleEntity rep = payDAO.findScheduleActive(id);
		if (rep!=null) {
			if (rep.getDatabeg() != null) {
				rep.setDatabeg( new Date(rep.getDatabeg().getTime() + shift));
			}
			if (rep.getDataend() != null) {
				rep.setDataend( new Date(rep.getDataend().getTime() + shift));
			}
			payDAO.saveSchedule(rep);
		}
	}


	@Override
	public List<CreditEntity> listCreditsForCalcPercent(Date date) {
		String sql = "from ru.simplgroupp.persistence.CreditEntity where isactive=:isactive and partnersId.id=:partner"
				+ " and creditdatabeg<=:date and (creditdataendfact is null or creditdataendfact>=:date)";
		 Query qry=emMicro.createQuery(sql);
	     qry.setParameter("isactive", ActiveStatus.ACTIVE);
	     qry.setParameter("date", date);
	     qry.setParameter("partner", Partner.SYSTEM);
	     List<CreditEntity> lst=qry.getResultList();
	     return lst;
	}

	@Override
	public List<CreditEntity> listCreditsForCalcPercentWithUnit(Date date){
		String sql = "from ru.simplgroupp.persistence.CreditEntity where isactive=:isactive and partnersId.id=:partner"
				+ " and creditdatabeg<=:date and (creditdataendfact is null or creditdataendfact>=:date) and creditRequestId.workplace is not null";
		Query qry=emMicro.createQuery(sql);
		qry.setParameter("isactive", ActiveStatus.ACTIVE);
		qry.setParameter("date", date);
		qry.setParameter("partner", Partner.SYSTEM);
		List<CreditEntity> lst=qry.getResultList();
		return lst;
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RefinanceEntity saveRefinanceRequest(Integer creditId, Refinance refinance,	Date date) throws KassaException {
		 CreditEntity cred=creditDAO.getCreditEntity(creditId);
	     if (cred!=null) {
	    	 //записываем рефинансирование, пока черновик
             RefinanceEntity refinanceNew = new RefinanceEntity();
             refinanceNew.setCreditId(cred);
             refinanceNew.setRefinanceDate(date);
             refinanceNew.setSmsCode(refinance.getSmsCode());
             refinanceNew.setRefinanceDays(refinance.getRefinanceDays());
             refinanceNew.setRefinanceStake(refinance.getRefinanceStake());
             refinanceNew.setAgreement(refinance.getAgreement()); 
             refinanceNew.setRefinanceAmount(refinance.getRefinanceAmount());
             refinanceNew.setUniquenomer(refinance.getUniqueNomer());
             refinanceNew.setIsactive(ActiveStatus.DRAFT);
             emMicro.persist(refinanceNew);
             
             return refinanceNew;
	     }
		return null;
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void startRefinance(Integer creditId) throws KassaException {
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
		
	    if (credit!=null) {
	       //ищем черновик рефинансирования	
	       RefinanceEntity refinance=creditDAO.findRefinanceDraft(creditId);
	       if (refinance!=null){
	    	   //закроем старый кредит
	    	   credit.setCreditdataendfact(refinance.getRefinanceDate());
	    	   credit.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_REFINANCE).getEntity());
	    	   credit.setIsover(true);
	    	   credit.setDateStatus(refinance.getRefinanceDate());
	    	   //если кредит не просрочен,надо посчитать сумму возврата, вряд ли это понадобится, но все же
	    	   if (credit.getMaxDelay()==null){
	    		   Map<String, Object> res = creditCalc.calcCredit(credit.getId(), refinance.getRefinanceDate());
	    		   Double sumBack = CalcUtils.roundFloor(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_BACK)),0);
	    		   logger.info("sumBack "+sumBack);
	    		   credit.setCreditsumback(sumBack);
	    	   }
	    	   emMicro.merge(credit);
	    	   
	    	   //ставим id рефинансирования в оферту
	    	   OfficialDocumentsEntity document=officialDocumentsDAO.findOfficialDocumentCreditDraft(refinance.getCreditId().getPeopleMainId().getId(), 
	    			   creditId,OfficialDocuments.OFERTA_REFINANCE);
	    	   if (document!=null){
	    	        	document.setIsActive(ActiveStatus.ACTIVE);
	    	        	document.setAnotherId(refinance.getId());
	    	        	emMicro.merge(document);
	    	   }
	    	   CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(credit.getCreditRequestId().getId());
	    	   PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(credit.getPeopleMainId().getId());
	    	   ProductsEntity product=productDAO.getProduct(credit.getProductId().getId());
	    	   //добавим новый
	    	   CreditEntity creditNew=new CreditEntity();
	    	   creditNew.setCreditdatabeg(refinance.getRefinanceDate());
	    	   creditNew.setCreditdataend(DateUtils.addDays(refinance.getRefinanceDate(), refinance.getRefinanceDays()));
	    	   creditNew.setIsactive(ActiveStatus.ACTIVE);
	    	   creditNew.setIsover(false);
	    	   creditNew.setIssameorg(true);
	    	   creditNew.setPartnersId(refBooks.getPartnerEntity(Partner.SYSTEM));
	    	   creditNew.setCreditRequestId(creditRequest);
	    	   creditNew.setPeopleMainId(peopleMain);
	    	   creditNew.setProductId(product);
	    	   creditNew.setIdCredit(String.valueOf(kassaBean.findMaxCreditNumber()));
	    	   creditNew.setCreditpercent(refinance.getRefinanceStake());
	    	   creditNew.setCreditFullCost(CalcUtils.calcYearStake(refinance.getRefinanceStake(),refinance.getRefinanceDate()));
	    	   creditNew.setCreditsum(credit.getCreditsumback()-refinance.getRefinanceAmount());
	    	   
	    	   Double sumPercent=CalcUtils.roundHalfUp(CalcUtils.calcSumPercentSimple(creditNew.getCreditsum(), refinance.getRefinanceDays(), 
	    			   true, refinance.getRefinanceStake()),1);
	    	   Double sumBack=creditNew.getCreditsum()+sumPercent;
	    	   creditNew.setCreditsumback(sumBack);
	    	   creditNew.setOwner(credit);
	    	   creditNew.setCreditAccount(refinance.getUniquenomer());
	           creditNew.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE,BaseCredit.CREDIT_ACTIVE));
	           creditNew.setDateStatus(refinance.getRefinanceDate());
	           creditNew.setCredittypeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_TYPE,BaseCredit.MICRO_CREDIT));
	           creditNew.setCreditrelationId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_RELATION_STATE,BaseCredit.CREDIT_OWNER));
	           creditNew.setIsUploaded(false);
	           creditNew.setIdCurrency(refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE,BaseCredit.CURRENCY_RUR));
	    	   emMicro.persist(creditNew);
	    	   
	    	   //запишем в creditdetails
	    	   saveCreditDetail(credit, BaseCredit.OPERATION_REFINANCE, refinance.getRefinanceDate(), 
	    			   creditNew.getCreditdataend(),creditNew.getCreditsum(), null, 
	    			   creditNew.getCreditsumback(), null,null,creditNew.getId());
	    	   
	    	   //просрочка на дату рефинансирования
	    	   CreditDetailsEntity overdue=null;
	    	   List<CreditDetailsEntity> lstOvrd=findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, 
           			DatesUtils.makeOneDayDateRange(refinance.getRefinanceDate()));
               if (lstOvrd.size()>0){
           	      overdue=lstOvrd.get(0);
               }
               Double sumP=0d;
               Double sumPen=0d;
               if (overdue!=null){
            	   sumP=overdue.getAmountAll()-overdue.getAmountMain();
            	   if (sumP>refinance.getRefinanceAmount()){
            	       sumP=refinance.getRefinanceAmount();
            	   }
            	   sumPen=overdue.getAmountPenalty();
            	   if (overdue.getAmountPenalty()!=null&&overdue.getAmountPenalty()>refinance.getRefinanceAmount()){
            	       sumPen=refinance.getRefinanceAmount();
            	   }
               }
               //платеж
               PaymentEntity payment=paymentServ.getLastCreditPayment(creditId, null);
	    	   //запишем проведенный платеж
	    	   saveCreditDetail(credit, BaseCredit.OPERATION_PAYMENT, refinance.getRefinanceDate(), 
	    			   null,creditNew.getCreditsum(), sumP, 
	    			   creditNew.getCreditsum(), refinance.getRefinanceAmount(),sumPen,
                       payment==null?null:payment.getId());
	    	   
	    	   //поставим активность рефинансированию
	    	   refinance.setIsactive(ActiveStatus.ACTIVE);
	    	   refinance.setCreditNewId(creditNew);
	    	   emMicro.merge(refinance);
	    	   
	    	   //запишем график платежей
	    	   paymentServ.savePaymentSchedule(creditNew, refinance.getRefinanceStake(), refinance.getRefinanceDays(),
	    			   creditNew.getCreditsumback(),new Date(),creditNew.getCreditsum());
	    	   
	    	   //записали лог - подписание оферты
    	       eventLog.saveLog(EventType.INFO, EventCode.OFERTA_SIGNED, "Была подписан оферта на рефинансирование займа", 
	    			   null,refinance.getRefinanceDate(),creditRequest, peopleMain, credit);
	    	   
	    	   //пишем лог
	           eventLog.saveLog(EventType.INFO,EventCode.START_REFINANCE,
	        		   "Было сделано рефинансирование", null, new Date(), creditRequest, peopleMain, credit);
	       }
	    }
		
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditSumsEntity saveCreditSum(Integer creditId, Integer sumId,
			Integer operationId, Date amountDate, Double amount,
			Integer creditDetailId) throws KassaException {
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
		if (credit!=null){
			CreditSumsEntity creditSum=new CreditSumsEntity();
			creditSum.setCreditId(credit);
			//если есть операция credit detail
			if (creditDetailId!=null){
				CreditDetailsEntity creditDetail=creditDAO.getCreditDetailsEntity(creditDetailId);
				creditSum.setCreditDetailsId(creditDetail);
			}
			creditSum.setOperationId(refBooks.findByCodeIntegerEntity(RefHeader.PEOPLE_SUM_OPERATION, operationId));
			creditSum.setSumId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_SUMS, sumId));
			creditSum.setAmount(amount);
			creditSum.setAmountDate(amountDate);
			emMicro.persist(creditSum);
			return creditSum;
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<CreditSumsEntity> findCreditSums(Integer creditId,
			Integer sumId, Integer operationId, DateRange amountDate,
			Integer creditDetailId) {
		String sql = "from ru.simplgroupp.persistence.CreditSumsEntity where (1=1) ";
        if (amountDate != null && amountDate.getFrom() != null) {
        	sql+= " and (amountDate >= :amountDateFrom)";
        }
        if (amountDate != null && amountDate.getTo() != null) {
        	sql+= " and (amountDate <= :amountDateTo)";
        }
        if (operationId!=null){
        	sql+=" and operationId.codeinteger=:operationId ";
        }
        if (sumId!=null){
        	sql+=" and sumId.codeinteger=:sumId ";
        }
        if (creditId!=null){
            sql+=" and creditId.id=:creditId ";
        }
        if (creditDetailId!=null){
            sql+=" and creditDetailsId.id=:creditDetailId ";
        }
        sql+=" order by amountDate desc ";
        Query qry=emMicro.createQuery(sql);
        if (amountDate != null && amountDate.getFrom() != null) {
        	qry.setParameter("amountDateFrom", amountDate.getFrom());
        }
        if (amountDate != null && amountDate.getTo() != null) {
        	qry.setParameter("amountDateTo", amountDate.getTo());
        }
        if (operationId!=null){
        	qry.setParameter("operationId", operationId);
        }
        if (sumId!=null){
        	qry.setParameter("sumId", sumId);
        }
        if (creditId!=null){
            qry.setParameter("creditId", creditId);
        }
        if (creditDetailId!=null){
            qry.setParameter("creditDetailId", creditDetailId);
        }
		return qry.getResultList();
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Refinance initRefinance(Credit credit, Map<String, Object> limits)
			throws KassaException {
		//инициализируем заявку на рефинансирование, берем константы из базы
    	RefinanceEntity refinance = new RefinanceEntity();
    	refinance.setCreditId(credit.getEntity());
    	refinance.setRefinanceDate(new Date());
    	refinance.setRefinanceStake((Double) limits.get(ProductKeys.STAKE_REFINANCE));
    	refinance.setRefinanceDays(Convertor.toInteger(limits.get(ProductKeys.DAYS_REFINANCE_MAX)));
    	refinance.setRefinanceAmount((Double) limits.get(ProductKeys.SUM_REFINANCE));
    	refinance.setUniquenomer(credit.getCreditAccount()+"-P");
    	
    	Refinance res=new Refinance(refinance);
		return res;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RefinanceEntity createRefinanceDraft(Integer creditId, Date dateStart, Integer refinanceDays, Double amount, Double stake) {
		CreditEntity cred = creditDAO.getCreditEntity(creditId);
		RefinanceEntity refin = new RefinanceEntity();
		refin.setCreditId(cred);
		refin.setRefinanceDate(dateStart);
		refin.setIsactive(ActiveStatus.DRAFT);
		refin.setRefinanceDays(refinanceDays);
		refin.setRefinanceAmount(amount);
		refin.setRefinanceStake(stake);
		refin = creditDAO.saveRefinance(refin);
		return refin;
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditEntity newCredit(CreditEntity credit, Integer peopleMainId,
			Integer creditRequestId, Integer partnerId, Boolean sameOrg,
			String overdueId, Integer creditOrganizationId, Double creditSum,
			Date creditDatabeg, Boolean isOver, Integer creditTypeId,
			Integer creditCurrencyId, Integer creditRelationId,
			Integer creditPurposeId,Date creditDataend,Date creditDataendfact,
			Double creditMonthlyPayment) throws PeopleException{
		CreditEntity newCredit=null;
		//если это новая запись
		if (credit==null){
			newCredit=new CreditEntity();
			if (partnerId!=null){
				PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			    newCredit.setPartnersId(partner);
			}
			if (peopleMainId!=null){
				PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			    newCredit.setPeopleMainId(peopleMain);
			}
			if (creditRequestId!=null){
				CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
			    newCredit.setCreditRequestId(creditRequest);
			}
		} else {
			newCredit=credit;
		}
		newCredit.setIssameorg(sameOrg);
		if (StringUtils.isNotEmpty(overdueId)){
		    newCredit.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, overdueId));
		}
		if (creditOrganizationId!=null){
			newCredit.setCreditOrganizationId(orgService.getCreditOrganizationEntityActive(creditOrganizationId));
		}
		if (creditTypeId!=null){
			newCredit.setCredittypeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_TYPE, creditTypeId));
		}
		if (creditCurrencyId!=null){
			newCredit.setIdCurrency(refBooks.findByCodeIntegerEntity(RefHeader.CURRENCY_TYPE,creditCurrencyId));
		}
		if (creditRelationId!=null){
			newCredit.setCreditrelationId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_RELATION_STATE,creditRelationId));
		}
		if (creditPurposeId!=null){
			newCredit.setCreditPurposeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_PURPOSE,creditPurposeId));
		}
		newCredit.setCreditsum(creditSum);
		newCredit.setCreditdatabeg(creditDatabeg);
		newCredit.setCreditdataend(creditDataend);
		newCredit.setCreditdataendfact(creditDataendfact);
		newCredit.setIsover(isOver);
		newCredit.setCreditMonthlyPayment(creditMonthlyPayment);
		if (isOver!=null){
			if (isOver){
				newCredit.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE, BaseCredit.CREDIT_CLOSED));
			} else {
				newCredit.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE, BaseCredit.CREDIT_ACTIVE));
			}
		}
		emMicro.persist(newCredit);
		return newCredit;
	}


	@Override
	public Credit initCredit(PeopleMain peopleMain, Integer creditRequestId) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
		 //проверяем, есть ли кредит
        List<CreditEntity> lstcred = findCredits(people, creditRequest, refBooks.getPartnerEntity(Partner.CLIENT), false, null);
        
        CreditEntity crednew=null;
        
        if (lstcred.size()!=0){
            crednew=lstcred.get(0);
        } else {
        	crednew=new CreditEntity();
        	crednew.setPeopleMainId(people);
        	crednew.setCreditRequestId(creditRequest);
        }
		return new Credit(crednew);
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Credit addCredit(Credit credit, Integer peopleMainId) throws PeopleException{
		CreditEntity creditEntity=null;
		if (credit.getId()!=null){
			creditEntity=creditDAO.getCreditEntity(credit.getId());
		}
		try {
			CreditEntity newCredit=newCredit(creditEntity,peopleMainId,credit.getCreditRequest().getId(), 
					Partner.CLIENT, false,refBooks.referenceCodeOrNull(credit.getOverdueState()), 
					credit.getCreditOrganization()!=null?credit.getCreditOrganization().getId():null, 
					credit.getCreditSum(),credit.getCreditDataBeg(),credit.getIsOver(), 
					refBooks.referenceCodeIntegerOrNull(credit.getCreditType()),
			        refBooks.referenceCodeIntegerOrNull(credit.getCurrency()), 
			        BaseCredit.CREDIT_OWNER,refBooks.referenceCodeIntegerOrNull(credit.getCreditPurpose()),
			        credit.getCreditDataEnd(),credit.getCreditDataEndFact(),credit.getCreditMonthlyPayment());
			return new Credit(newCredit);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить данные кредита по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить данные кредита по человеку "+peopleMainId+",ошибка "+e);
		}
	
	}
}
