package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.transfer.Refinance;

public class TestRefinace {
	
	@EJB
	CreditBeanLocal credit;	
	
	@EJB
	ActionProcessorBeanLocal actProc;
	
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    
  //@Test
  	public void testSaveRefinance() throws Exception{
  		 Refinance refinance=new Refinance();
  		 refinance.setSmsCode("123456");
         refinance.setRefinanceDays(30);
         refinance.setRefinanceStake(0.001);
         refinance.setAgreement("Согласен"); 
         refinance.setRefinanceAmount(new Double(125));
         refinance.setUniqueNomer("1040009021500001");
  		 credit.saveRefinanceRequest(14298, refinance, new Date());
  		 credit.startRefinance(14298);
  	}
}
