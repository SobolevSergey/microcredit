package ru.simplgroupp.dao.impl;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.DocumentsDAO;
import ru.simplgroupp.persistence.DocumentMediaEntity;

public class TestDocumentDAO {

	@EJB
	DocumentsDAO documentDAO;
	
	@Before
	public void setUp() throws Exception {
		 System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
	     final Properties p = new Properties();
	     p.load(this.getClass().getResourceAsStream("/test.properties"));

	     final Context context = EJBContainer.createEJBContainer(p).getContext();
	     context.bind("inject", this);
	}

	@Test
	public void testSave() {
		DocumentMediaEntity docMedia=documentDAO.saveDocumentPage(null,10,2,null, null, "image/jpeg", null, null);
		Assert.assertNotNull(docMedia);
	}

	//@Test
	public void testDelete() {
		documentDAO.deleteDocumentMedia(25632);
		documentDAO.deleteDocumentMediaAll(24806);
	}
}
