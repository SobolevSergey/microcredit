package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.services.CreditService;

public class TestCreditService {

	@EJB
	CreditService creditService;
	
	@Before
	public void setUp() throws Exception {
		 System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	     final Properties p = new Properties();
	     p.load(this.getClass().getResourceAsStream("/test.properties"));

	     final Context context = EJBContainer.createEJBContainer(p).getContext();
	     context.bind("inject", this);
	}

	@Test
	public void testIsMatch() throws ObjectNotFoundException, KassaException {
		boolean b=creditService.isMatch(41126, 41074);
		System.out.println(b);
	}
	
	@Test
	public void testRating() throws ObjectNotFoundException, KassaException {
		Double d=creditService.creditRating(41030, 41074);
		System.out.println(d);
	}


}
