package ru.simplgroupp.script;

import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.AntifraudField;
import ru.simplgroupp.transfer.CreditRequest;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.Properties;

public class TestAntifraudFieldSearch {

    @EJB
    CreditRequestDAO crDAO;

    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }

    @Test
    public void testSearchAntifraud() {
        CreditRequest cRequest = crDAO.getCreditRequest(1, Utils.setOf(CreditRequest.Options.INIT_ANTIFRAUD));
        if (cRequest != null) {
            AntifraudSearchScriptObject antifraudSearchScriptObject = new AntifraudSearchScriptObject();
            SearchResult<AntifraudField> antifraudFieldSearchResult = antifraudSearchScriptObject.search(cRequest, "antifraudFieldType", 15);
            int i = antifraudFieldSearchResult.getCount();
            System.out.println(i);
        }
    }

}
