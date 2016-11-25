package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.AIModelDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.AIModel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AIModelDAOImpl implements AIModelDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;	
    
    @Override
	public AIModelEntity getModel(int id) {
		return emMicro.find(AIModelEntity.class, new Integer(id));
	}
    
    @Override
    public AIModel getModel(int id, Set options) {
    	AIModelEntity ent = getModel(id);
    	if (ent == null) {
    		return null;
    	}
    	AIModel model = new AIModel(ent);
    	model.init(options);
    	return model;
    }    
    
    @Override
    public AIModelEntity saveModelEntity(AIModelEntity source) {
    	AIModelEntity aobj = emMicro.merge(source);
    	return aobj;
    }
    
    @Override
    public void saveModelParam(AIModelParamEntity source) {
    	emMicro.merge(source);
    }
    
    @Override
    public void copyModelParams(int fromId, String fromCustomKey, int toId, String toCustomKey) {
    	Query qry = emMicro.createNamedQuery("copyModelParams");
    	qry.setParameter("fromId", fromId);
    	qry.setParameter("fromCustomKey", fromCustomKey);
    	qry.setParameter("toId", toId);
    	qry.setParameter("toCustomKey", toCustomKey);
    	qry.executeUpdate();
    }
    
    @Override
    public AIModelParamEntity findModelParam(int modelId, String name, String customKey) {
    	return (AIModelParamEntity) JPAUtils.getSingleResult(emMicro, "findModelParams", Utils.mapOf("modelId", modelId,
    			"name", name,
    			"customKey", customKey));
    }
    
    @Override    
    public List<AIModelParamEntity> listModelParamsCK(int modelId, String customKey) {
    	return JPAUtils.getResultList(emMicro, "findModelParamsCK", Utils.mapOf("modelId", modelId,
    			"customKey", customKey));
    }
    
    @Override
    public void deleteModelParam(int modelId, String name) {
    	Query qry = emMicro.createNamedQuery("deleteModelParam");
    	qry.setParameter("modelId", modelId);
    	qry.setParameter("name", name);
    	qry.executeUpdate();
    }
    
    @Override
    public void deleteModelParams(int modelId, String customKey) {
    	Query qry = emMicro.createNamedQuery("deleteModelParams");
    	qry.setParameter("modelId", modelId);
    	qry.setParameter("customKey", customKey);
    	qry.executeUpdate();
    } 
    
    @Override
    public int getNextRunId() {
    	Query qry = emMicro.createNamedQuery("getNextRunId");
    	List lst = qry.getResultList();
    	if (lst.size() == 0 || lst.get(0) == null) {
    		return 0;
    	} else {
    		Number nres = (Number) lst.get(0);
    		return nres.intValue();
    	}
    }
    
    @Override
    public void putModelParamValue(int runId, int paramId, String paramValue ) {
    	Query qry = emMicro.createNamedQuery("putModelParamValue");
    	qry.setParameter("run_id", runId);
    	qry.setParameter("aimodelparam_id", paramId);
    	qry.setParameter("value", paramValue);
    	qry.executeUpdate();    	
    } 
    
    @Override
    public List<Integer> listModelIdsWithResults() {
    	Query qry = emMicro.createNamedQuery("listModelsWithRun");
    	List<Integer> lst = qry.getResultList();
    	return lst;
    }
    
    @Override
    public void deleteResults(String businessObjectClass, Integer businessObjectId) {
    	Query qry = emMicro.createNamedQuery("deletePrevResults");
    	qry.setParameter("businessObjectClass", businessObjectClass);
    	qry.setParameter("businessObjectId", businessObjectId.toString());
    	qry.executeUpdate();      	
    }
    
    @Override
    public List<Object[]> listLastResults(String businessObjectClass, Integer businessObjectId) {
    	Query qry = emMicro.createNamedQuery("listLastResults");
    	qry.setParameter("businessObjectClass", businessObjectClass);
    	qry.setParameter("businessObjectId", businessObjectId.toString());  
    	return qry.getResultList();
    }
    
    @Override
    public Integer findModelIdByParam(int paramId) {
    	Query qry = emMicro.createNamedQuery("findModelIdByParam");
    	qry.setParameter("id", paramId);
    	List lst = qry.getResultList();
    	if (lst.size() > 0 && lst.get(0) != null) {
    		return ((Number) lst.get(0)).intValue();
    	} else {
    		return null;
    	}
    }
}
