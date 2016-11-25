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
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Partner;

public class TestDebtSearch {

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
	public void testSearchDebt() {
			CreditRequest cRequest=crDAO.getCreditRequest(13626, Utils.setOf(CreditRequest.Options.INIT_DEBT));
			if (cRequest!=null){
				DebtSearchScriptObject searchDebt=new DebtSearchScriptObject();
				SearchResult<Debt> debts=searchDebt.search(cRequest, "partner.id",Partner.SYSTEM);
				int i=debts.getCount();
				System.out.println(i);
				
			}
		}


}
