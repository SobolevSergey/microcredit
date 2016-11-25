package ru.simplgroupp.script;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BlackList;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.DatesUtils;

public class TestPeopleSearch {

	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@EJB
	PeopleBeanLocal peopleBean;
	
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}
	
	@Test
	public void testPeopleSearch(){
		PeopleMain peopleMain=peopleDAO.getPeopleMain(31297, Utils.setOf(PeopleMain.Options.INIT_BLACKLIST));
		PeopleSearchScriptObject peopleSearch=new PeopleSearchScriptObject(peopleBean);
		SearchResult<BlackList> black=peopleSearch.searchBlackList(peopleMain, "dataBeg.from",DatesUtils.makeDate(2015, 1, 1));
		int i=black.getCount();
		System.out.println(i);

	}
}
