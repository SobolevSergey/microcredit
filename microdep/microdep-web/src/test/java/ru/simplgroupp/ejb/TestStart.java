package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

@LocalClient
public class TestStart {
	
	@PersistenceContext(unitName="MicroPUStart")
	EntityManager emMicro;
	
//	@PersistenceContext(unitName="DesignPUStart")
//	EntityManager emDesign;		

	@Before
	public void setUp() throws Exception {
		final Properties p = new Properties();

        p.load(this.getClass().getResourceAsStream("/test.properties"));
        
        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);		
	}
	
	@Test
	public void testStart() {
		
	}
}
