package ru.simplgroupp.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.util.AppUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ListDAOImpl implements ListDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @Override
    public BusinessListEntity getList(int id) {
    	return emMicro.find(BusinessListEntity.class, new Integer(id));
    }
    
    @Override
    public BusinessListEntity saveList(BusinessListEntity source) {
    	BusinessListEntity ent = emMicro.merge(source);
    	return ent;
    }
    
    @Override
    public List<BusinessListEntity> findLists(String businessObjectClass, String subtype, Integer isExplicit) {
    	Query qry = emMicro.createNamedQuery("findLists");
    	qry.setParameter("businessObjectClass", businessObjectClass);
    	qry.setParameter("SubType", subtype);
    	qry.setParameter("isExplicit", isExplicit);
    	return qry.getResultList();
    }
    
    @Override
    public void clearListItems(int listId) {
    	Query qry = emMicro.createNamedQuery("clearListItems");
    	qry.setParameter("listId", listId);
    	qry.executeUpdate();
    }
    
    @Override
    public void saveCond(int listId, String cond) {
    	Query qry1 = emMicro.createNamedQuery("removeListConds");
    	qry1.setParameter("listId", listId);
    	qry1.executeUpdate();
    	
    	Query qry = emMicro.createNamedQuery("saveListCondSingle");
    	qry.setParameter("listId", listId);
    	qry.setParameter("body", cond);
    	qry.executeUpdate();
    }
    
    @Override
    public String loadCond(int listId) {
    	Query qry = emMicro.createNamedQuery("getListCondSingle");
    	qry.setParameter("listId", listId); 
    	List lstRes = qry.getResultList();
    	if (lstRes.size() == 0 || lstRes.get(0) == null) {
    		return null;
    	} else {
    		return lstRes.get(0).toString();
    	}
    }
    
    @Override
    public void addListItems(BusinessListEntity biz, List source) {
    	String tableName = AppUtil.bizNameToTableName(biz.getBusinessObjectClass());
    	if (tableName == null) {
    		return;
    	}
    	String sql = "insert into bolistitems (bolist_id, businessobjectid) select :listId, id from " + tableName + " where (id in (:ids))";
    	Query qry = emMicro.createNativeQuery(sql);
    	qry.setParameter("listId", biz.getId());
    	qry.setParameter("ids", source);
    	qry.executeUpdate();
    }
    
    @Override
    public void removeListItem(Integer listId, Integer itemId) {
    	Query qry = emMicro.createNamedQuery("removeListItem");
    	qry.setParameter("listId", listId);
    	qry.setParameter("itemId", itemId);
    	qry.executeUpdate();    	
    }
    
    @Override
    public void addListItem(Integer listId, Integer itemId) {
    	Query qry = emMicro.createNamedQuery("removeListItem");
    	qry.setParameter("listId", listId);
    	qry.setParameter("itemId", itemId);
    	qry.executeUpdate();    	

    	qry = emMicro.createNamedQuery("insertListItem");
    	qry.setParameter("listId", listId);
    	qry.setParameter("itemId", itemId);
    	qry.executeUpdate();    	
    }    
    
    @Override
    public boolean isItemInList(Integer listId, Integer itemId) {
    	Query qry = emMicro.createNamedQuery("isItemInList");
    	qry.setParameter("listId", listId);
    	qry.setParameter("itemId", itemId);
    	List lst =  qry.getResultList();
    	if (lst.size() > 0 && lst.get(0) instanceof Number) {
    		Number nn = (Number) lst.get(0);
    		return (nn != null && nn.intValue() == 1);
    	} else {
    		return false;
    	}
    }
    
    @Override
    public Integer countItems(int listId) {
    	Query qry = emMicro.createNamedQuery("countItemInList");
    	qry.setParameter("listId", listId); 
    	List lst =  qry.getResultList();
    	if (lst.size() > 0 && lst.get(0) instanceof Number) {
    		Number nn = (Number) lst.get(0);
    		if (nn == null) {
    			return null;
    		} else {
    			return nn.intValue();
    		}
    	} else {
    		return null;
    	}    	
    }
    
    @Override
    public void removeList(int listId) {
    	BusinessListEntity biz = getList(listId);
    	emMicro.remove(biz);
    }
    
    @Override
    public void removeList(BusinessListEntity biz) {
    	BusinessListEntity biz1 = emMicro.merge(biz);
    	emMicro.remove(biz1);
    }
}
