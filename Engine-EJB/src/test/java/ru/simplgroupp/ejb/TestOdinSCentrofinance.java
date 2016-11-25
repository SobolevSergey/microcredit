package ru.simplgroupp.ejb;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.service.OdinSServiceCentrofinance;
import ru.simplgroupp.odins.model.centrofinance.SputnikExchange;
import ru.simplgroupp.util.DatesUtils;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import java.util.Date;
import java.util.Properties;

public class TestOdinSCentrofinance {

	@EJB
    protected OdinSServiceCentrofinance odinS;
	
	@Before
	public void setUp() throws Exception {
	 
		System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	    final Properties p = new Properties();
	    p.load(this.getClass().getResourceAsStream("/test.properties"));

	    final Context context = EJBContainer.createEJBContainer(p).getContext();
	    context.bind("inject", this);

	}

	//@Test
	public void testPercent() {
		SputnikExchange ce=odinS.generateXMLAccruedInterest(new Date());
		System.out.println(ce.getVersion());
	}

	@Test
	public void testContract() {
		SputnikExchange ce=odinS.generateXMLResponse(DatesUtils.makeDate(2016, 1, 24));
		System.out.println(ce.getVersion());
	}
}
