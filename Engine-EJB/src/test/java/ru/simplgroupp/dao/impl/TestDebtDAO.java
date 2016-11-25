package ru.simplgroupp.dao.impl;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.DebtDao;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.util.DatesUtils;

public class TestDebtDAO {

	@EJB
	DebtDao debtDao;
	
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}   
	
	@Test
	public void testFindDebt() {
		DebtEntity debt=debtDao.findDebt(13808, 13, new Double(202), 2, DatesUtils.makeDate(2014, 12, 5));
		Assert.assertNotNull(debt);
	}

}
