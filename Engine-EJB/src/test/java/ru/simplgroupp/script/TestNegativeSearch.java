package ru.simplgroupp.script;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Negative;

public class TestNegativeSearch {

	@EJB
	CreditRequestDAO crDAO;
	
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
	public void testSearchNegative() {
			CreditRequest cRequest=crDAO.getCreditRequest(17, Utils.setOf(CreditRequest.Options.INIT_NEGATIVE));
			if (cRequest!=null){
				NegativeSearchScriptObject searchNegative=new NegativeSearchScriptObject();
				SearchResult<Negative> negatives=searchNegative.search(cRequest, "article.groupId",5);
				int i=negatives.getCount();
				System.out.println(i);
				
			}
	}

}
