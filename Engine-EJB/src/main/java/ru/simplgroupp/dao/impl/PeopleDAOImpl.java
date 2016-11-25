package ru.simplgroupp.dao.impl;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ListContainer;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PeopleDAOImpl implements PeopleDAO {
	
	@Inject Logger logger;
	
	private static HashMap<String, Object> peopleSortMapping = new HashMap<String, Object>(4);	
    static {
    	peopleSortMapping.put("Initials", "e1.surname");
    	peopleSortMapping.put("DateBirth", "e1.birthdate");
    }	
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
	@Override
	public PeopleMainEntity getPeopleMainEntity(int id) {
		 PeopleMainEntity ent = emMicro.find(PeopleMainEntity.class, new Integer(id));
	     return ent;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public PeopleMain getPeopleMain(int id, Set options) {
		PeopleMainEntity ent = getPeopleMainEntity(id);
		if (ent == null) {
			return null;
		} else {
			PeopleMain pm = new PeopleMain(ent);
			pm.init(options);
			return pm;
		}
	}
    
    @Override
    public PeopleBonusEntity addBonus(PeopleMainEntity peopleMainId, RefBonusEntity bonusId, ReferenceEntity operationId, Double amount, Double amountrub, Date dateBonus, CreditEntity creditId, PeopleMainEntity peopleMainBonusId) {
    	PeopleBonusEntity bon = new PeopleBonusEntity();
    	bon.setPeopleMainId(peopleMainId);
    	bon.setBonusId(bonusId);
    	bon.setOperationId(operationId);
    	bon.setEventDate(dateBonus);
    	bon.setAmount(amount);
    	bon.setAmountrub(amountrub);
    	bon.setCreditId(creditId);
    	bon.setPeopleMainBonusId(peopleMainBonusId);
    	emMicro.persist(bon);

    	return bon;
    }
    
    @Override
    public Double getPeopleBonusInSystem(Integer peopleMainId) {
    	Query qry=emMicro.createNamedQuery("getPeopleBonusInSystem");
    	qry.setParameter("peopleMainId", peopleMainId);
    	qry.setParameter("eventdate", new Date());
    	List<Double> lst=qry.getResultList();
    	if (lst.size() == 0||lst.get(0)==null) {
    		return new Double(0);
    	} else {
    		return lst.get(0);
    	}
    }	
    
    @Override
    public int updatePeopleBonusProperties(Integer peopleMainId, Integer bonuspay) {
    	Query qry=emMicro.createNamedQuery("updatePeopleBonusProperties");
    	qry.setParameter("peopleMainId", peopleMainId);
    	qry.setParameter("bonuspay", bonuspay);
    	qry.setParameter("actionDate", new Date());
    			
    	return qry.executeUpdate();
    }
    
    @Override
    public Integer getUserBonusPayProperties(Integer peopleMainId) {
    	Query qry=emMicro.createNamedQuery("getUserBonusPayProperties");
    	qry.setParameter("peopleMainId", peopleMainId);
    	List<Integer> lst=qry.getResultList();
    	if (lst.size() == 0||lst.get(0)==null) {
    		return new Integer(0);
    	} else {
    		return lst.get(0);
    	}
    }

	@Override
	public int countData(ListContainer<PeopleMain> listCon) {
		int nCount = listCon.calcCount(emMicro);
		return nCount;
	}

	@Override
	public List<PeopleMain> listData(int nFirst, int nCount,
			ListContainer<PeopleMain> listCon) {
		return listCon.calcList(nFirst, nCount, emMicro);
	}

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount,
			ListContainer<PeopleMain> listCon) {
		return listCon.calcIds(nFirst, nCount, emMicro);
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(
			Class<T> clz) {
		try {
			T lstCon = clz.newInstance();
			lstCon.setSortMapping(peopleSortMapping);
			lstCon.setItemClass(PeopleMain.class);
			return lstCon;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Не создали список " + clz.getName(), e);
			return null;
		}
	}

	@Override
	public void deleteFriend(Integer id) {
		 Query qry = emMicro.createNamedQuery("deleteFriend");
		 qry.setParameter("id", id);
	     qry.executeUpdate();	
		
	}	
	
	@Override
	public PeoplePersonalEntity getPeoplePersonalEntity(Integer id) {
		return emMicro.find(PeoplePersonalEntity.class,id);
	}
	

	@Override
	public PeopleMiscEntity getPeopleMisc(Integer id) {
		return emMicro.find(PeopleMiscEntity.class, id);
	}

	@Override
	public PeopleOtherEntity getPeopleOther(Integer id) {
		return emMicro.find(PeopleOtherEntity.class, id);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public PeopleContactEntity getPeopleContactEntity(int id) {
		return emMicro.find(PeopleContactEntity.class, new Integer(id));
	}
	
	@Override
	public EmploymentEntity getEmployment(Integer id) {
		return emMicro.find(EmploymentEntity.class, id);
	}

	@Override
	public CallBackEntity getCallBackEntity(Integer id) {
		return emMicro.find(CallBackEntity.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CallBack getCallBack(Integer id) {
		return getCallBack(id, null);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CallBack getCallBack(Integer id, Set options) {
		CallBackEntity callBack=getCallBackEntity(id);
		if (callBack!=null){
			CallBack result = new CallBack(callBack);
			result.init(options);
			return result;
		}
		return null;
	}

	@Override
	public PeopleFriendEntity getPeopleFriend(Integer id) {
		return emMicro.find(PeopleFriendEntity.class, id);
	}
	
	 @Override
	 public PeopleMainEntity findPeopleMain( String inn, String snils) {
	    	String hql = "from ru.simplgroupp.persistence.PeopleMainEntity where (1=1)";
	    	if (StringUtils.isNotEmpty(inn)) {
	    		hql = hql + " and (inn = :inn)";
	    	}
	    	if (StringUtils.isNotEmpty(snils )) {
	    		hql = hql + " and (snils = :snils)";
	    	}

	    	Query qry = emMicro.createQuery(hql);
	    	if (StringUtils.isNotEmpty(inn )) {
	    		qry.setParameter("inn", inn);
	    	}
	    	if (StringUtils.isNotEmpty(snils)) {
	    		qry.setParameter("snils", snils);
	    	}

	    	List<PeopleMainEntity> lst = qry.getResultList();
	    	return (PeopleMainEntity) Utils.firstOrNull(lst);

	}

	@Override
	public void removeBonus(Integer peopleMainID, Integer bonusCode) {
		TypedQuery<PeopleBonusEntity> qry = emMicro.createNamedQuery("getBonusByPeopleAndCode", PeopleBonusEntity.class);
		qry.setParameter("peopleMainID", peopleMainID);
		qry.setParameter("bonusCode", bonusCode);
		qry.setMaxResults(1);
		List<PeopleBonusEntity> result = qry.getResultList();
		if (!result.isEmpty()) {
			PeopleBonusEntity first = result.get(0);
			Query q = emMicro.createNamedQuery("removeBonus");
			q.setParameter("bonusID", first.getId());
			q.executeUpdate();
		}
	}

	@Override
	public List<PeopleBonusEntity> findCreditPayBonus(Integer credit_id, Date paydate, Integer ref_id){

		DateRange dateRange=DatesUtils.makeOneDayDateRange(paydate);
	
		return JPAUtils.getResultList(emMicro, "findCreditPayBonus", Utils.mapOf("credit_id",credit_id,
				"operation_id",ref_id,
				"start",dateRange.getFrom(),
				"end",dateRange.getTo()));
		
	}

	@Override
	public PeopleMainEntity findPeopleMainByIin(String iin) {
		String hql = "from ru.simplgroupp.persistence.PeopleMainEntity where iin = :iin";
		TypedQuery<PeopleMainEntity> qry = emMicro.createQuery(hql, PeopleMainEntity.class);
		qry.setParameter("iin", iin);

		List<PeopleMainEntity> lst = qry.getResultList();
		return (PeopleMainEntity) Utils.firstOrNull(lst);
	}

	@Override
	public List<PeoplePersonalEntity> getPeoplePersonalByPeopleMainIDs(List<Integer> peopleMainIDs) {
		TypedQuery<PeoplePersonalEntity> qry = emMicro.createNamedQuery("findPeoplePersonalByPeopleIDs", PeoplePersonalEntity.class);
		qry.setParameter("peopleMainIDs", peopleMainIDs);
		return qry.getResultList();
	}

	@Override
	public List<Integer> getPeopleMainIDsByRoleIDAndEventCodeID(Integer eventCode, Integer roleID) {
		Query qry = emMicro.createNamedQuery("findUserIDsByEventCodeAndRoleID");
		qry.setParameter("eventCode", eventCode);
		qry.setParameter("roleID", roleID);

		return qry.getResultList();
	}
}
