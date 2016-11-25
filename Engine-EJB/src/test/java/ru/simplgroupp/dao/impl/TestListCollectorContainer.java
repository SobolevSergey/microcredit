package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CollectorDAO;
import ru.simplgroupp.ejb.WorkflowEngineBean;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Collector;
import ru.simplgroupp.transfer.PeopleMain;

public class TestListCollectorContainer {

	@EJB
	WorkflowEngineBean wfEngine;
	
	@EJB
	CollectorDAO collectorDAO;
	
	@Before
	public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
	}

	@Test
	public void test() {
        Assert.assertNotNull(collectorDAO);
    	
    	RuntimeServices rs = new RuntimeServices(null, null, null, null);
    	rs.setWfEngine(wfEngine);
    	ListCollectorContainer container=collectorDAO.genData(ListCollectorContainer.class);
    	container.setRuntimeServices(rs);
    	container.setPrmSurname("Воробьянинов");
    	container.setObjectOptions(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
    	int n=collectorDAO.countData(container);
    	Assert.assertTrue(n > 0);
    	System.out.println(n);
    	List<Collector> lst=collectorDAO.listData(0, 10, container);
    	Assert.assertTrue(lst.size() <= n);
    	
	}

}
