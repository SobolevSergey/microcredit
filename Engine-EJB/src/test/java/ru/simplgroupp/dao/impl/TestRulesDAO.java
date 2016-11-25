package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.RulesDAO;
import ru.simplgroupp.persistence.AIConstantEntity;

public class TestRulesDAO {

	@EJB
	RulesDAO rulesDAO;
	
	 @Before
	 public void setUp() throws Exception {
	        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);

	 }   
	    
	 @Test
	 public void testFind1() {
	   List<AIConstantEntity> lst= rulesDAO.listConstants("ru.simplgroupp.crypto.common", null);
	   if (lst.size()>0){
		   System.out.println(lst.size());
	   }
	 }
}
