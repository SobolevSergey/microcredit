package ru.simplgroupp.dao.impl;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CallsDAO;
import ru.simplgroupp.persistence.CallResultEntity;
import ru.simplgroupp.util.DatesUtils;

public class TestCallsDAO {

	@EJB
	CallsDAO callsDAO;
	
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}   

	@Test
	public void testSaveCallResult() throws Exception{
		CallResultEntity callResult=callsDAO.newCallResult(32, 518, 1, new Date(),
				DatesUtils.makeDate(2015, 9, 15), DatesUtils.makeDate(2015, 9, 1), "Обещал заплатить через 3 недели");
		Assert.assertNotNull(callResult);
	}

}
