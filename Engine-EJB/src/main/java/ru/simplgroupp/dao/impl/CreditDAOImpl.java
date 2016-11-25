package ru.simplgroupp.dao.impl;

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

import org.jboss.annotation.ejb.TransactionTimeout;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditSumsEntity;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Refinance;
import ru.simplgroupp.util.ListContainer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CreditDAOImpl implements CreditDAO {
	
	@Inject Logger logger;
	
	private static HashMap<String, Object> creditSortMapping = new HashMap<String, Object>(4);	
    static {
    	creditSortMapping.put("CreditSum", "c.creditsum");
    	creditSortMapping.put("CreditDataBeg", "c.creditdatabeg");
    	creditSortMapping.put("CreditDataEnd", "c.creditdataend");
    }		
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;	
    
	 @Override
	 public Credit getCredit(int id, Set options) {
        CreditEntity ent =getCreditEntity(id);
        if (ent == null) {
            return null;
        }

        Credit aobj = new Credit(ent);
        if (options!=null){
          aobj.init(options);
        }
        return aobj;
	 }
	 
	 @Override
	 public CreditEntity getCreditEntity(int creditId) {
		 CreditEntity ent = emMicro.find(CreditEntity.class, creditId);
		 return ent;
	 }	 
    
    @Override
    public CreditEntity saveCreditEntity(CreditEntity cred) {
		 CreditEntity cr1 = emMicro.merge(cred);
		 emMicro.persist(cr1);    	
		 return cr1;
    }
    
    @Override
    public void removeCredit(int id) {
        Query qry = emMicro.createNamedQuery("removeCredit");
        qry.setParameter("id", new Integer(id));
        qry.getResultList();    	
    }
    
    @Override
    public CreditDetailsEntity saveCreditDetails(CreditDetailsEntity creditDetail) {
    	CreditDetailsEntity ent = emMicro.merge(creditDetail);
    	emMicro.persist(ent);
    	return ent;
    }
    

	@Override
	public int getProlongDaysSum(int creditId) {
		return JPAUtils.getSingleResultInteger(emMicro, "getProlongDaysSum", Utils.mapOf("creditId", creditId));

	}    
	
	@Override
	public Double getPeopleSumsInSystem(Integer peopleMainId) {
		Query qry=emMicro.createNamedQuery("getPeopleSumsInSystem");
		qry.setParameter("peopleMainId", peopleMainId);
		List<Double> lst=qry.getResultList();
		if (lst.size() == 0||lst.get(0)==null) {
			return new Double(0);
		} else {
			return lst.get(0);
		}
	}	
	
    @Override
    public CreditEntity findCreditByAccountNumber(String accountNumber) {
    	return (CreditEntity) JPAUtils.getSingleResult(emMicro, "findCreditByAccountNumber", Utils.mapOf("accountNumber", accountNumber));
    }

	@Override
	@TransactionTimeout(1500) 
	public void removePartnerCredits(Integer partnersId) throws KassaException {
		 Query qry = emMicro.createNamedQuery("sqlRemovePartnerCredits");
		 qry.setParameter("id", partnersId);
	     qry.executeUpdate();
	}	
	
	
	@Override
	public CreditEntity findFirstSystemCredit() {
		Query qry = emMicro.createNamedQuery("findFirstSystemCredit");
		qry.setParameter("partnersId", Partner.SYSTEM);
		qry.setParameter("isactive", ActiveStatus.ACTIVE);
		List<CreditEntity> lstc = qry.getResultList();
		return (CreditEntity) Utils.firstOrNull(lstc);
	   
	}
	
	@Override
	public List<CreditEntity> findCreditByPeople(int peopleMainId, int partner, boolean issameorg, boolean isover) {
		Query qry = emMicro.createNamedQuery("findCreditByPeople");
		qry.setParameter("peopleMainId", peopleMainId);
		qry.setParameter("partner", partner);
		qry.setParameter("issameorg", issameorg);
		qry.setParameter("isover", isover);
		return qry.getResultList();
	}

	@Override
	public RefinanceEntity findRefinanceDraft(int creditId) {
		return  (RefinanceEntity) JPAUtils.getSingleResult(emMicro, "findRefinanceDraft", Utils.mapOf("creditId", creditId,
				  "isactive", ActiveStatus.DRAFT));
	    
	}

	@Override
	public void deleteRefinance(int id) {
		Query qry = emMicro.createNamedQuery("deleteRefinance");
		qry.setParameter("id", id);
	    qry.executeUpdate();	
		
	}

	@Override
	public RefinanceEntity getRefinanceEntity(int refinanceId) {
		return emMicro.find(RefinanceEntity.class,refinanceId);
	}

	@Override
	public Refinance getRefinance(int refinanceId, Set options) {
		RefinanceEntity ent = getRefinanceEntity(refinanceId);
		if (ent == null) {
			return null;
		} 
		Refinance refinance = new Refinance(ent);
		refinance.init(options);
		return refinance;
	}

	@Override
	public CreditSumsEntity saveCreditSums(CreditSumsEntity creditSums) {
		CreditSumsEntity ent = emMicro.merge(creditSums);
    	emMicro.persist(ent);
    	return ent;
	}
	
	@Override
	public RefinanceEntity saveRefinance(RefinanceEntity source) {
		RefinanceEntity ent = emMicro.merge(source);
		return ent;
	}

	@Override
	public CreditDetailsEntity getCreditDetailsEntity(int id) {
		CreditDetailsEntity creditDetails=emMicro.find(CreditDetailsEntity.class, id);
		return creditDetails;
	}

	
	
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<RefinanceEntity> findRefinance(int creditId, DateRange refDate, Integer isActive) {
		String hql = "from ru.simplgroupp.persistence.RefinanceEntity where (creditId.id = :creditId)";
		if (isActive!=null){
			hql=hql+" and isactive=:isActive ";
		}
		if (refDate!=null && refDate.getFrom()!=null){
			hql = hql + " and (refinanceDate >= :longDateFrom)";
		}
		if (refDate!=null && refDate.getTo()!=null){
			hql = hql + " and (refinanceDate <= :longDateTo)";
		}
		hql+=" order by refinanceDate desc";
		Query qry = emMicro.createQuery(hql);
		qry.setParameter("creditId", creditId);
		if (isActive!=null){
			qry.setParameter("isActive", isActive);
		}
		if (refDate!=null && refDate.getFrom()!=null){
			qry.setParameter("longDateFrom", refDate.getFrom());
		}
		if (refDate!=null && refDate.getTo()!=null){
			qry.setParameter("longDateTo", refDate.getTo());
		}
		List<RefinanceEntity> lst=qry.getResultList();
		
		return lst;
	}

	@Override
	public int countData(ListContainer<Credit> listCon) {
		int nCount = listCon.calcCount(emMicro);
		return nCount;
	}

	@Override
	public List<Credit> listData(int nFirst, int nCount,
			ListContainer<Credit> listCon) {
		return listCon.calcList(nFirst, nCount, emMicro);
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(
			Class<T> clz) {
		try {
			T lstCon = clz.newInstance();
			lstCon.setSortMapping(creditSortMapping);
			lstCon.setItemClass(Credit.class);
			return lstCon;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Не создали список " + clz.getName(), e);
			return null;
		}
	}

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount,
			ListContainer<Credit> listCon) {
		return listCon.calcIds(nFirst, nCount, emMicro);
	}

	@Override
	public void makePartnerCreditsNotActive(Integer partnersId,Integer peopleMainId) {
		Query qry = JPAUtils.createNamedQuery(emMicro, "makePartnerCreditsNotActive", Utils.mapOf("partner", partnersId,
				"peoplemain", peopleMainId)) ;
		qry.executeUpdate();
		
	}

	@Override
	public CreditDetailsEntity getCreditDetailByAnotherId(Integer anotherId) {
		return (CreditDetailsEntity) JPAUtils.getSingleResult(emMicro, "getCreditDetailByAnotherId", Utils.mapOf("anotherId",anotherId));
	}	
	
}
