package ru.simplgroupp.script;


import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Verification;

public class TestVerificationSearch {

	@EJB
	CreditRequestDAO crDAO;
		
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}

	
	@Test
	public void testSearchVerification() {
		CreditRequest cRequest=crDAO.getCreditRequest(17, Utils.setOf(CreditRequest.Options.INIT_VERIF));
		if (cRequest!=null){
			VerificationSearchScriptObject searchVer=new VerificationSearchScriptObject();
			SearchResult<Verification> verifications=searchVer.search(cRequest,"verificationScore.from", 100);
			int i=verifications.getCount();
			System.out.println(i);
			
		}
	}

}
