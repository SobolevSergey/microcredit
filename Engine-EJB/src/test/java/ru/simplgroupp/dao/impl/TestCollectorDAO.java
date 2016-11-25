package ru.simplgroupp.dao.impl;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CollectorDAO;
import ru.simplgroupp.persistence.CollectorEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.util.DatesUtils;

public class TestCollectorDAO {

	@EJB
	CollectorDAO collectorDAO;
	
	@Before
	public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	}   
	
	@Test
	public void testNewCollector(){
		CollectorEntity collector=collectorDAO.newCollectorRecord(6245, 25188, 518, "SOFT", Convertor.toDate("12.06.2015", DatesUtils.FORMAT_ddMMYYYY), ActiveStatus.ACTIVE, null);
		Assert.assertNotNull(collector);
	}
	  
}
