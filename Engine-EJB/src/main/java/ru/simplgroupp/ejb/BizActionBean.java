package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.BizActionEventEntity;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.SearchUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(BizActionBeanLocal.class)
public class BizActionBean implements BizActionBeanLocal {
	
	private static HashMap<String, Object> bizActionSortMapping = new HashMap<String, Object>(4);	
	
    static {
    	bizActionSortMapping.put("BusinessObjectClass", "c.BusinessObjectClass");
    	bizActionSortMapping.put("ProcessDefKey", "c.ProcessDefKey");
    }	
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;
    
    @EJB
    BizActionDAO bizDAO;
    
    @EJB
    BusinessEventSender senderServ;
    
    @Override
    public BizAction getBizAction(int aid, Set options) {
    	return bizDAO.getBizAction(aid, options);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int countBizActions(Integer bizActionTypeId, String signalRef, String businessObjectClass, Integer forOne, Integer forMany, 
    		Integer isSystem, Boolean hasErrors, DateRange lastStart, DateRange lastEnd, Boolean hasProcessDefKey, String processDefKey) {
    	String sql = "select count(c) from ru.simplgroupp.persistence.BizActionEntity as c where (1=1) ";
    	if (bizActionTypeId != null) {
    		sql = sql + " and (c.bizActionType = :bizActionTypeId)";
    	}
    	if (signalRef != null) {
    		sql = sql + " and (c.signalRef = :signalRef)";
    	}
    	if (businessObjectClass != null) {
    		sql = sql + " and (c.businessObjectClass = :businessObjectClass)";
    	}
    	if (forOne != null) {
    		sql = sql + " and (c.forOne = :forOne)";
    	}
    	if (forMany != null) {
    		sql = sql + " and (c.forMany = :forMany)";
    	} 
    	if (isSystem != null) {
    		sql = sql + " and (c.isSystem = :isSystem)";
    	}
    	if (hasErrors != null) {
    		if (hasErrors) {
    			sql = sql + " and (c.errorCode is not null)";
    		} else {
    			sql = sql + " and (c.errorCode is null)";
    		}
    	}
    	if (lastStart != null && lastStart.getFrom() != null) {
    		sql = sql + " and (c.lastStart >= :lastStartFrom)";
    	}
    	if (lastStart != null && lastStart.getTo() != null) {
    		sql = sql + " and (c.lastStart <= :lastStartTo)";
    	}  
    	if (lastEnd != null && lastEnd.getFrom() != null) {
    		sql = sql + " and (c.lastEnd >= :lastEndFrom)";
    	}
    	if (lastEnd != null && lastEnd.getTo() != null) {
    		sql = sql + " and (c.lastEnd <= :lastEndTo)";
    	}    	
    	if (hasProcessDefKey != null) {
    		if (hasProcessDefKey) {
    			sql = sql + " and (c.processDefKey is not null)";
    		} else {
    			sql = sql + " and (c.processDefKey is null)";
    		}
    	}
    	if (processDefKey != null) {
    		sql = sql + " and (c.processDefKey = :processDefKey)";
    	}
    	Query qry = emMicro.createQuery(sql);
    	if (bizActionTypeId != null) {
    		qry.setParameter("bizActionTypeId", bizActionTypeId);
    	}
    	if (signalRef != null) {
    		qry.setParameter("signalRef", signalRef);
    	}
    	if (businessObjectClass != null) {
    		qry.setParameter("businessObjectClass", businessObjectClass);
    	}    	
    	if (forOne != null) {
    		qry.setParameter("forOne", forOne);
    	}  
    	if (forMany != null) {
    		qry.setParameter("forMany", forMany);
    	}   
    	if (isSystem != null) {
    		qry.setParameter("isSystem", isSystem);
    	}     
    	if (lastStart != null && lastStart.getFrom() != null) {
    		qry.setParameter("lastStartFrom", lastStart.getFrom());
    	}     	
    	if (lastStart != null && lastStart.getTo() != null) {
    		qry.setParameter("lastStartTo", lastStart.getTo());
    	}  
    	if (lastEnd != null && lastEnd.getFrom() != null) {
    		qry.setParameter("lastEndFrom", lastEnd.getFrom());
    	}     	
    	if (lastEnd != null && lastEnd.getTo() != null) {
    		qry.setParameter("lastEndTo", lastEnd.getTo());
    	}    	
    	if (processDefKey != null) {
    		qry.setParameter("processDefKey", processDefKey);
    	}
	    Number res = (Number) qry.getSingleResult();
	    if (res==null)
	      return 0;
	    else
	      return res.intValue();	
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<BizAction> listBizActions(int nFirst, int nCount, SortCriteria[] sorting, Set options, 
    		Integer bizActionTypeId, String signalRef, String businessObjectClass, Integer forOne, Integer forMany, 
    		Integer isSystem, Boolean hasErrors, DateRange lastStart, DateRange lastEnd, Boolean hasProcessDefKey, String processDefKey) {
    	String sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.BizActionEntity as c where (1=1) ";    	
    	if (bizActionTypeId != null) {
    		sql = sql + " and (c.bizActionType = :bizActionTypeId)";
    	}
    	if (signalRef != null) {
    		sql = sql + " and (c.signalRef = :signalRef)";
    	}
    	if (businessObjectClass != null) {
    		sql = sql + " and (c.businessObjectClass = :businessObjectClass)";
    	}
    	if (forOne != null) {
    		sql = sql + " and (c.forOne = :forOne)";
    	}
    	if (forMany != null) {
    		sql = sql + " and (c.forMany = :forMany)";
    	} 
    	if (isSystem != null) {
    		sql = sql + " and (c.isSystem = :isSystem)";
    	}
    	if (hasErrors != null) {
    		if (hasErrors) {
    			sql = sql + " and (c.errorCode is not null)";
    		} else {
    			sql = sql + " and (c.errorCode is null)";
    		}
    	}
    	if (lastStart != null && lastStart.getFrom() != null) {
    		sql = sql + " and (c.lastStart >= :lastStartFrom)";
    	}
    	if (lastStart != null && lastStart.getTo() != null) {
    		sql = sql + " and (c.lastStart <= :lastStartTo)";
    	}  
    	if (lastEnd != null && lastEnd.getFrom() != null) {
    		sql = sql + " and (c.lastEnd >= :lastEndFrom)";
    	}
    	if (lastEnd != null && lastEnd.getTo() != null) {
    		sql = sql + " and (c.lastEnd <= :lastEndTo)";
    	} 
    	if (hasProcessDefKey != null) {
    		if (hasProcessDefKey) {
    			sql = sql + " and (c.processDefKey is not null)";
    		} else {
    			sql = sql + " and (c.processDefKey is null)";
    		}
    	}
    	if (processDefKey != null) {
    		sql = sql + " and (c.processDefKey = :processDefKey)";
    	}
    	
        sql = sql + SearchUtil.sortToString(bizActionSortMapping, sorting);
        String sortSelect = SearchUtil.sortSelectToString(bizActionSortMapping, sorting);
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);    
        
        Query qry = emMicro.createQuery(sql);
    	if (bizActionTypeId != null) {
    		qry.setParameter("bizActionTypeId", bizActionTypeId);
    	}
    	if (signalRef != null) {
    		qry.setParameter("signalRef", signalRef);
    	}
    	if (businessObjectClass != null) {
    		qry.setParameter("businessObjectClass", businessObjectClass);
    	}    	
    	if (forOne != null) {
    		qry.setParameter("forOne", forOne);
    	}  
    	if (forMany != null) {
    		qry.setParameter("forMany", forMany);
    	}   
    	if (isSystem != null) {
    		qry.setParameter("isSystem", isSystem);
    	}     
    	if (lastStart != null && lastStart.getFrom() != null) {
    		qry.setParameter("lastStartFrom", lastStart.getFrom());
    	}     	
    	if (lastStart != null && lastStart.getTo() != null) {
    		qry.setParameter("lastStartTo", lastStart.getTo());
    	}  
    	if (lastEnd != null && lastEnd.getFrom() != null) {
    		qry.setParameter("lastEndFrom", lastEnd.getFrom());
    	}     	
    	if (lastEnd != null && lastEnd.getTo() != null) {
    		qry.setParameter("lastEndTo", lastEnd.getTo());
    	} 
    	if (processDefKey != null) {
    		qry.setParameter("processDefKey", processDefKey);
    	}    	
    	
        if (nFirst >= 0)
            qry.setFirstResult(nFirst);
        if (nCount > 0)
            qry.setMaxResults(nCount); 
        
        List<BizActionEntity> lst = null;
        if (sorting == null || sorting.length == 0) {
            lst = qry.getResultList();
        } else {
            List<Object[]> lstSource = qry.getResultList();
            lst = new ArrayList<BizActionEntity>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, lst);
            lstSource = null;
        }        
        
        List<BizAction> lstRes = new ArrayList<BizAction>(lst.size());
        for (BizActionEntity ent: lst) {
        	BizAction act = new BizAction(ent);
        	act.init(options);
        	lstRes.add(act);
        }
    	return lstRes;
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public BizAction saveBizAction(BizAction source) {
    	// проверяем события
    	List<BizActionEventEntity> lstEvt = new ArrayList<BizActionEventEntity>(source.getEntity().getEvents().size());
    	lstEvt.addAll(source.getEntity().getEvents());
    	for (BizActionEventEntity evt: lstEvt) {
    		int n = source.getEvents().indexOf(evt.getEventCode());
    		if (n < 0) {
    			// событие было удалено
//    			source.getEntity().getEvents().remove(evt);    			
    			bizDAO.removeBizActionEventEntity(evt);
    		} 
    		source.getEvents().remove(evt.getEventCode());
    	}
    	// добавить новые события
    	for (Integer evtCode: source.getEvents()) {
    		BizActionEventEntity evt = new BizActionEventEntity();
    		evt.setBizAction(source.getEntity());
    		evt.setEventCode(evtCode);
    		bizDAO.saveBizActionEventEntity(evt);
    	}
    	
    	BizActionEntity ent = bizDAO.saveBizActionEntity(source.getEntity());    	
    	
    	BizAction res = new BizAction(ent);
    	res.init(Utils.setOf(BizAction.Options.INIT_EVENTS));
    	
    	senderServ.fireEvent(res, EventCode.BIZACTION_CHANGED);
    	return res;
    }
    
    @Override
    public String buildHQL(BizActionEntity bizAct, int listId) {
    	// TODO
		String hql = "from " + AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()) + " as c where (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId))  ";
/*		
		if (bizAct.getSQLFilter() != null) {
			hql = hql + " and " + bizAct.getSQLFilter();
		}
*/		
		return hql;
    }    
  
    @Override
    public String buildHQL(BizActionEntity bizAct) {
		String hql = "from " + AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()) + " as c where (1=1) ";
		if (bizAct.getSQLFilter() != null) {
			hql = hql + " and " + bizAct.getSQLFilter();
		}
		return hql;
    }
    
    @Override
    public String buildHQLOne(BizActionEntity bizAct, int listId) {
    	// TODO
		String hql = "from " + AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()) + " as c where (c.id = :businessObjectId) and (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
		return hql;
    }     
    
    @Override
    public String buildHQLOne(BizActionEntity bizAct) {
		String hql = "from " + AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()) + " as c where (c.id = :businessObjectId) ";
		return hql;
    }    
    
}
