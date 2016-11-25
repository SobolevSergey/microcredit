package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;


public class TestMisc {
	
	@EJB
	KassaBeanLocal kassaBean;
	
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
    	kassaBean.saveMiscVariables(CreditRequest.class.getName(), 18, Utils.mapOfSO("qqq",1));
    }
}
