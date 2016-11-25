package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.ejb.WorkflowEngineBean;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;

public class TestListContainerCR {
	
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	WorkflowEngineBean wfEngine;
    
    @Test
    public void testEmpty() {    
    	Assert.assertNotNull(crDAO);
    }
    
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }    

    @Test
    public void testLC() {    
    	Assert.assertNotNull(crDAO);
    	
    	RuntimeServices rs = new RuntimeServices(null, null, null, null);
    	rs.setWfEngine(wfEngine);
    	
    	ListCRContainer con = crDAO.genData(ListCRContainer.class);
    	con.setRuntimeServices(rs);
    	con.setObjectOptions(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
    	
    	int n = crDAO.countData(con);
    	Assert.assertTrue(n > 0);
    	
    	List<CreditRequest> lst = crDAO.listData(0, 10, con);
    	Assert.assertTrue(lst.size() <= n);
    }
    
    @Test
    public void testLCPersonal() {    
    	Assert.assertNotNull(crDAO);
    	
    	RuntimeServices rs = new RuntimeServices(null, null, null, null);
    	rs.setWfEngine(wfEngine);
    	
    	ListCRContainer con = crDAO.genData(ListCRContainer.class);
    	con.setRuntimeServices(rs);
    	con.setObjectOptions(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
    	con.setPeopleSurname("иванов");
    	
    	int n = crDAO.countData(con);
    	Assert.assertTrue(n > 0);
    	
    	List<CreditRequest> lst = crDAO.listData(0, 10, con);
    	Assert.assertTrue(lst.size() <= n);
    }    
    
}
