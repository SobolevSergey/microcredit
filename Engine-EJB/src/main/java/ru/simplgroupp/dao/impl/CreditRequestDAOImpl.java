package ru.simplgroupp.dao.impl;

import java.util.Arrays;
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

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.ListContainer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CreditRequestDAOImpl implements CreditRequestDAO {
	
	@Inject Logger logger;
	
	private static HashMap<String, Object> ccRequestSortMapping = new HashMap<String, Object>(4);	
    static {
        ccRequestSortMapping.put("DateContest", "c.datecontest");
        ccRequestSortMapping.put("Status", "c.statusId");
        ccRequestSortMapping.put("CreditSum", "c.creditsum");
        ccRequestSortMapping.put("CreditDays", "c.creditdays");         
    }	
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;	
    
    @Override
    public CreditRequest getCreditRequest(int id, Set options) {

        CreditRequestEntity ent = getCreditRequestEntity(id);
        if (ent == null) {
            return null;
        }
        CreditRequest ccRequest = new CreditRequest(ent);
        ccRequest.init(options);
        return ccRequest;
    }
    
    @Override
    public CreditRequestEntity getCreditRequestEntity(int id) {
    	return emMicro.find(CreditRequestEntity.class, id);
    }
    
    @Override
    public List<CreditRequest> findCreditRequestActive(int peopleMainId, Set options) throws Exception {
    	List<CreditRequestEntity> lst = findCreditRequestActive(peopleMainId,Arrays.asList(CreditStatus.IN_PROCESS, CreditStatus.FILLED));
    	List<CreditRequest> lstRes = AppUtil.wrapCollection(CreditRequest.getWrapConstructor(), lst);
    	Utils.initCollection(lstRes, options);
    	return lstRes;
    }
    
    @Override
    public List<CreditRequestEntity> findCreditRequestActive(int peopleMainId,List<Integer> statuses) {
    	return JPAUtils.getResultList(emMicro, "findCreditRequestActive", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"statuses", statuses
    			));
    
    }

    @Override
    public List<CreditRequestEntity> findCreditRequestsForMan(int peopleMainId) {
    	return JPAUtils.getResultList(emMicro, "findCreditRequestsForMan", Utils.mapOf(
    			"peopleMainId", peopleMainId
    			));
    
    }
    
	@Override
	public int countData(ListContainer<CreditRequest> listCon) {
		int nCount = listCon.calcCount(emMicro);
		return nCount;
	}

	@Override
	public List<CreditRequest> listData(int nFirst, int nCount,
			ListContainer<CreditRequest> listCon) {
		return listCon.calcList(nFirst, nCount, emMicro);
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(
			Class<T> clz) {
		try {
			T lstCon = clz.newInstance();
			lstCon.setSortMapping(ccRequestSortMapping);
			lstCon.setItemClass(CreditRequest.class);
			return lstCon;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Не создали список " + clz.getName(), e);
			return null;
		}
	}

    @Override
    public CreditRequestEntity saveCreditRequest(CreditRequestEntity creditRequest) {
    	CreditRequestEntity ent = emMicro.merge(creditRequest);
    	emMicro.persist(ent);
    	return ent;
    }

	@Override
	public List<CreditRequestEntity> findPartnersCreditRequests(
			int peopleMainId, int partnerId) {
		List<CreditRequestEntity> lst = JPAUtils.getResultList(emMicro, "findPartnersCreditRequests", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"partnerId", partnerId
    			));
    	return lst;
	}

	@Override
	public boolean findCreditRequestsWithProduct(Integer productId) {
		List<CreditRequestEntity> lst = JPAUtils.getResultList(emMicro, "findCreditRequestsWithProduct", Utils.mapOf(
    			"productId", productId
    			));
		if (lst.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public List<CreditRequestEntity> findCreditRequestWaitingSign(int peopleMainId) {
		return JPAUtils.getResultList(this.emMicro, "findCreditRequestWaitingSign",
				Utils.mapOf(new Object[]{"peopleMainId", Integer.valueOf(peopleMainId), "accepted", 1, "status", 4}));
	}

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount,
			ListContainer<CreditRequest> listCon) {
		return listCon.calcIds(nFirst, nCount, emMicro);
	}

	@Override
	public CreditRequestEntity saveAnotherSumAndDays(Integer creditRequestId, Double creditSum, Integer creditDays) {
		return saveAnotherSumAndDays(creditRequestId, creditSum, null, creditDays);
	}

	@Override
	public CreditRequestEntity saveAnotherSumAndDays(Integer creditRequestId, Double creditSum, Double creditStake, Integer creditDays) {
		CreditRequestEntity creditRequest=getCreditRequestEntity(creditRequestId);
		if (creditRequest!=null){
			creditRequest.setCreditdays(creditDays);
			creditRequest.setCreditsum(creditSum);
			creditRequest.setStake(creditStake);
			emMicro.merge(creditRequest);
			return creditRequest;
		}
		return null;
	}

    @Override
    public List<CreditRequestEntity> findCreditRequestsToUpload(int partnerId, int requestStatusId) {
        List<CreditRequestEntity> result = JPAUtils.getResultList(emMicro, "findCreditRequestsToUpload", Utils.mapOf(
                "partnerId", partnerId, "requestStatusId", requestStatusId
        ));
        return result;
    }
}
