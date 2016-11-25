package ru.simplgroupp.dao.impl;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditOfferDAO;
import ru.simplgroupp.persistence.CreditOfferEntity;

public class TestCreditOfferDAO {


	@EJB
	CreditOfferDAO creditOfferDAO;
	
	@Before
	public void setUp() throws Exception {
		 System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	     final Properties p = new Properties();
	     p.load(this.getClass().getResourceAsStream("/test.properties"));

	     final Context context = EJBContainer.createEJBContainer(p).getContext();
	     context.bind("inject", this);

	}

	@Test
	public void testNewCreditOffer() {
		CreditOfferEntity creditOffer=creditOfferDAO.newCreditOffer(39487, 13578, 518, 2,null, 10, new Double(2000),null, null);
		Assert.assertNotNull(creditOffer);
	}

}
