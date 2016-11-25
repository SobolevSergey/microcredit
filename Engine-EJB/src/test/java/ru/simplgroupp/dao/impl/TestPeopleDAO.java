package ru.simplgroupp.dao.impl;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.PeopleMainEntity;

public class TestPeopleDAO {

	@EJB
	PeopleDAO peopleDAO;
	
	@EJB 
	ReferenceBooksLocal refBook;
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
	}

	@Test
	public void testFindPeopleMain() {
		PeopleMainEntity people=peopleDAO.findPeopleMain("1234567890", "");
		Assert.assertNotNull(people);
	}


}
