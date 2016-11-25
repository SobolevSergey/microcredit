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

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.SqlDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.toolkit.common.Utils;

/**
 * Общие SQL операции над базой данных 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SqlDAOImpl implements SqlDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;
    
    @Override
    public boolean isTableOrViewExists(String tableName) {
    	List lst = JPAUtils.getResultList(emMicro, "isTableOrViewExists", Utils.mapOf("schemaName", "public",
    			"tableName", tableName));
    	if (lst.size() == 0) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    @Override
    public void createView(String viewName, String sql) {
    	Query qry = emMicro.createNativeQuery("create or replace view " + viewName + " as " + sql);
    	qry.executeUpdate();
    }
    
    @Override
    public void dropView(String viewName) {
    	if (! isTableOrViewExists(viewName)) {
    		return;
    	}
    	try {
    		Query qry = emMicro.createNativeQuery("drop view " + viewName);
    		qry.executeUpdate();
    	} catch (Throwable ex) {
    		
    	}
    }    
    
    @Override
    public List<Object[]> selectSingle(String table, String where, String columns) {
        String sql = "select * from " + table + " where (1=1) ";

        if (StringUtils.isNotEmpty(columns)) {
            sql = sql.replace("*", columns);
        }

        if (StringUtils.isNotEmpty(where)) {
            sql += " and " + where;
        }
        Query qry = emMicro.createNativeQuery(sql);
        List<Object[]> lst = qry.getResultList();
        return lst;
    }
}
