package ru.simplgroupp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.BizActionEventEntity;
import ru.simplgroupp.persistence.BizActionTypeEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.BizActionType;
import ru.simplgroupp.util.ListContainer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BizActionDAOImpl implements BizActionDAO {
	
	
	private static HashMap<String, Object> bizActionSortMapping = new HashMap<String, Object>(4);	
	
    static {
    	bizActionSortMapping.put("BusinessObjectClass", "c.BusinessObjectClass");
    	bizActionSortMapping.put("ProcessDefKey", "c.ProcessDefKey");
    }	
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;	
    
    @Override
    public BizActionEntity getBizActionEntity(int actId) {
    	BizActionEntity ent = emMicro.find(BizActionEntity.class, new Integer(actId));
    	return ent;
    }
    
    @Override
    public BizActionEntity getBizActionEntityLock(int actId) {
    	BizActionEntity ent = getBizActionEntity(actId);
    	if (ent != null) {
    		emMicro.lock(ent, LockModeType.PESSIMISTIC_WRITE);
    	}
    	return ent;
    }    
    
    @Override
    public void removeBizActionEntity(BizActionEntity source) {
    	BizActionEntity ent = emMicro.merge(source);
    	emMicro.remove(ent);
    }
    
    @Override
    public void removeBizActionEventEntity(BizActionEventEntity evt) {
    	BizActionEventEntity evt1 = emMicro.merge(evt);
    	emMicro.remove(evt1);
    }
    
    @Override
    public BizActionEventEntity saveBizActionEventEntity(BizActionEventEntity ent) {
    	BizActionEventEntity ent1 = emMicro.merge(ent);
    	return ent1;
    }
    
    @Override
    public BizActionEntity saveBizActionEntity(BizActionEntity source) {
    	BizActionEntity ent1 = getBizActionEntity(source.getId());
    	if (ent1 == null) {
    		return null;
    	}
    	
    	source.txVersion = ent1.txVersion;
    	BizActionEntity ent = emMicro.merge(source);
//    	emMicro.persist(ent);
    	return ent;
    }    
    
    @Override
    public List<BizActionEntity> listBOActions() {
    	Query qry = emMicro.createNamedQuery("listBOActions");
    	List<BizActionEntity> lst = qry.getResultList();
    	return lst;
    }      
    
    @Override
    public List<BizActionEntity> listBPActions(String processDefKey) {
    	Query qry = emMicro.createNamedQuery("listBPActions");
    	qry.setParameter("processDefKey", processDefKey);
    	List<BizActionEntity> lst = qry.getResultList();
    	return lst;
    }  
    
    @Override
    public List<BizActionEntity> listBPActions() {
    	Query qry = emMicro.createNamedQuery("listBPActionsAll");
    	List<BizActionEntity> lst = qry.getResultList();
    	return lst;
    }      

	@Override
	public int countData(ListContainer<BizAction> listCon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BizAction> listData(int nFirst, int nCount,
			ListContainer<BizAction> listCon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(
			Class<T> clz) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
    public List<BizActionEntity> listBOActions(String businessObjectClass, Boolean forOne, Boolean forMany, Integer isSingleton, String roleName) {
    	List<BizActionEntity> lst = listBOActions(businessObjectClass, forOne, forMany, isSingleton);
    	if (StringUtils.isBlank(roleName)) {
    		return lst;
    	}
    	
    	ArrayList<BizActionEntity> lstRes = new ArrayList<>(lst.size());
    	for (BizActionEntity biz: lst) {
    		if (StringUtils.isBlank(biz.getCandidateGroups())) {
    			continue;
    		}
    		
    		String[] roles = biz.getCandidateGroups().split(",");
    		for (String arole: roles) {
    			if (roleName.equals(arole.trim())) {
    				lstRes.add(biz);
    			}
    		}
    	}
    	return lstRes;
    }
    
    @Override
    public List<BizActionEntity> listBOActions(String businessObjectClass, Boolean forOne, Boolean forMany, Integer isSingleton) {
    	String sql = "from ru.simplgroupp.persistence.BizActionEntity where (processDefKey is null) ";
    	if (businessObjectClass == null) {
    		sql = sql + "  and (businessObjectClass is not null) ";
    	} else {
    		sql = sql + " and (businessObjectClass = :businessObjectClass)";
    	}
    	if (forOne != null) {
    		sql = sql + " and (forOne = :forOne)";
    	}
    	if (forMany != null) {
    		sql = sql + " and (forMany = :forMany)";
    	} 
    	if (isSingleton != null) {
    		sql = sql + " and (isSingleton = :isSingleton)";
    	}    	
    	Query qry = emMicro.createQuery(sql);
    	if (businessObjectClass != null) {
    		qry.setParameter("businessObjectClass", businessObjectClass);
    	}    	
    	if (forOne != null) {
    		qry.setParameter("forOne", (forOne)?1:0);
    	}  
    	if (forMany != null) {
    		qry.setParameter("forMany", (forMany)?1:0);
    	}    
    	if (isSingleton != null) {
    		qry.setParameter("isSingleton", isSingleton);
    	}      	
    	List<BizActionEntity> lst = qry.getResultList();
    	return lst;
    }	
    
    @Override
    public BizActionEntity findBizObjectAction(String businessObjectClass, String signalRef) {
    	return (BizActionEntity) JPAUtils.getSingleResult(emMicro, "findBizObjectAction", Utils.mapOf("businessObjectClass", businessObjectClass,
    			"signalRef", signalRef));
    }  
    
    @Override
    public BizActionEntity findBizObjectActionProduct(String businessObjectClass, 
    		String signalRef,Integer productId) {
    	return (BizActionEntity) JPAUtils.getSingleResult(emMicro, "findBizObjectActionProduct", Utils.mapOf("businessObjectClass", businessObjectClass,
    			"signalRef", signalRef,
    			"productId", productId));
    }  
    
    @Override
    public List<BizActionEntity> findBizObjectActions(String signalRef) {
    	return JPAUtils.getResultList(emMicro, "findBizObjectActionByRef", Utils.mapOf("signalRef", signalRef));
    }    
    
    @Override
    public BizActionEntity findProcessAction(String businessObjectClass, String procDefKey, String plugin, String signalRef) {
    	return (BizActionEntity) JPAUtils.getSingleResult(emMicro, "findProcessAction", Utils.mapOf("businessObjectClass", businessObjectClass,
    			"signalRef", signalRef,
    			"processDefKey", procDefKey,
    			"plugin", plugin));
    }    
    
    @Override
    public List<BizActionType> getBizActionTypes() {
    	Query qry = emMicro.createNamedQuery("getBizActionTypes");
    	
    	List<BizActionTypeEntity> lstEnt = qry.getResultList();
    	List<BizActionType> lstRes = new ArrayList<BizActionType>(lstEnt.size());
    	for (BizActionTypeEntity ent: lstEnt) {
    		BizActionType biz = new BizActionType(ent);
    		lstRes.add(biz);
    	}
    	return lstRes;
    }    
    
    @Override
    public BizActionTypeEntity getBizActionTypeEntity(int id) {
    	return emMicro.find(BizActionTypeEntity.class, new Integer(id));
    }
    
    @Override
    public BizActionType getBizActionType(int id) {
    	BizActionTypeEntity ent = getBizActionTypeEntity(id);
    	if (ent == null) {
    		return null;
    	} else {
    		return new BizActionType(ent);
    	}
    }    

	@Override
	public List<BizActionEventEntity> listBizActionEvents() {
		Query qry = emMicro.createNamedQuery("listBAByEvent");
		List<BizActionEventEntity> lst = qry.getResultList();
		Utils.initCollection(lst, Utils.setOf(BizActionEventEntity.Options.INIT_BIZACTION));
		return lst;
	}
	
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public BizAction getBizAction(int aid, Set options) {
    	BizActionEntity ent = getBizActionEntity(aid);
    	if (ent == null) {
    		return null;
    	}
    	
    	BizAction res = new BizAction(ent);
    	res.init(options);
    	return res;
    }

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount,
			ListContainer<BizAction> listCon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BizActionEntity> listBPActionsProduct(String processDefKey,
			Integer productId) {
		return JPAUtils.getResultList(emMicro, "listBPActionsProduct", Utils.mapOf("processDefKey", processDefKey,
				"productId", productId));
	}	
}
