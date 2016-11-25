package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.transfer.Partner;

public class TestCreditDAO {
	
	@EJB
	CreditDAO creditDAO;
	
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }   
    
    @Test
    public void testFind1() {
    	List<CreditEntity> lst = creditDAO.findCreditByPeople(2884, Partner.SYSTEM, true, false);
    	Assert.assertNotNull(lst);
    }
}
