package ru.simplgroupp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.RefAntifraudRulesDAO;
import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;

public class TestAntifraudRulesDAO {

	@EJB
	RefAntifraudRulesDAO rules;
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
	}

	@Test
	public void testRequestSql() {
		RefAntifraudRulesEntity rule=rules.getRefAntifraudRulesEntity(1);
		ExtendedBaseEntity contact= (ExtendedBaseEntity) rules.createAndExecuteSqlForRequest(rule, 28938);
	    Assert.assertNotNull(contact);
	}

	@Test
	public void testResponseSql(){
		Map<String,Object> queryParams=new HashMap<String,Object>(1);
		//подходит по условию на октябрь 2015
		//queryParams.put("value", "74565676878");
		//не подходит по условию на октябрь 2015
		queryParams.put("value","77686989898");
		List<ExtendedBaseEntity> contact=rules.createAndExecuteSqlForResponse(1, 28938, queryParams);
		Assert.assertTrue(contact.size()>0);
	}
}
