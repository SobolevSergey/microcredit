package ru.simplgroupp.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.Products;

public class TestCreditRequestDAO {
	
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
    
    //@Test
    public void testFind1() throws Exception {
    	List<CreditRequest> lst = crDAO.findCreditRequestActive(13808, Utils.setOf(CreditRequest.Options.INIT_CREDIT));
    	if (lst.size()>0){
    		for (CreditRequest creditRequest:lst){
    			System.out.println(creditRequest.getUniqueNomer()+" "+creditRequest.getDateContest());
    		}
    	}
    	Assert.assertNotNull(lst);
    	List<CreditRequestEntity> lst1 = crDAO.findCreditRequestActive(13808,Arrays.asList(CreditStatus.CLIENT_REFUSE));
    	if (lst1.size()>0){
    		for (CreditRequestEntity creditRequest:lst1){
    			System.out.println(creditRequest.getUniquenomer()+" "+creditRequest.getDatecontest());
    		}
    	}
    	Assert.assertNotNull(lst1);
    }
    
    @Test
    public void testCreditRequestsWithProduct(){
    	boolean t=crDAO.findCreditRequestsWithProduct(Products.PRODUCT_PDL);
    	Assert.assertTrue(t);
    }
}
