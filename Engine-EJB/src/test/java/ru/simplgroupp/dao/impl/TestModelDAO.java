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

import ru.simplgroupp.dao.interfaces.AIModelDAO;
import ru.simplgroupp.dao.interfaces.CallsDAO;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.persistence.CallResultEntity;
import ru.simplgroupp.util.DatesUtils;

public class TestModelDAO {

	@EJB
	AIModelDAO modelDAO;
	
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}   

//	@Test
	public void testFindParam() throws Exception{
		Assert.assertNotNull(modelDAO);
		AIModelParamEntity prm = modelDAO.findModelParam(38598, "autoSaveVariables", "9367bc82-8612-456a-bd46-3639ee92594d");
		Assert.assertNotNull(prm);
		Assert.assertTrue(prm.getDataValue().equals("1"));
		prm = modelDAO.findModelParam(38598, "autoSaveVariables", null);
		Assert.assertNotNull(prm);		
		Assert.assertTrue(prm.getDataValue() == null);
	}
	
}
