package ru.simplgroupp.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.transfer.ProductRules;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.util.ProductKeys;

public class TestProductDAO {

	@EJB
	ProductDAO productDAO;
	
	@Before
	public void setUp() throws Exception {
	       System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);
	}

	//@Test
	public void testProductConfig() {
		List<ProductConfigEntity> lst=productDAO.findProductConfigActiveByType(Products.PRODUCT_PDL, ProductKeys.PRODUCT_CREDITREQUEST);
		System.out.println(lst.size());
		for (ProductConfigEntity config:lst){
			System.out.println(config.getName()+"="+config.getDataValue());
		}
		
	}

	//@Test
	public void testProductRule(){
		ProductRulesEntity productRule=productDAO.findProductRulesActiveByScriptTypeAndName(Products.PRODUCT_PDL,ProductKeys.PRODUCT_CREDITREQUEST,
				ProductRules.SCRIPT_JS,new Date(),ProductKeys.CREDIT_STAKE_INITIAL_UNKNOWN);
		if (productRule!=null){
			System.out.println(productRule.getRuleBody());
		}
	}
	
	//@Test
	public void testDefaultProduct(){
		ProductsEntity product=productDAO.getProductDefault();
		Assert.assertNotNull(product);
	}
	
	//@Test
	public void testProductMessage(){
		ProductMessagesEntity message=productDAO.findProductMessageByName(Products.PRODUCT_PDL, "procConsiderCR", "sms.rejected");
		System.out.println("Test message");
		if(message!=null){
			System.out.println(message.getDescription());
			System.out.println(message.getBody());
		}
	}
	
	//@Test
	public void testProductRules(){
		List<ProductRulesEntity> rules=productDAO.findAllRulesActiveByKbase("kbCreditCalc", new Date());
		System.out.println("Test rules");
		if (rules.size()>0){
			for (ProductRulesEntity rule:rules){
			    System.out.println(rule.getName());
			}
		}
	}
	
	//@Test
	public void testProductRulesKbase(){
		List<String> rules=productDAO.listKbase();
		System.out.println("Test rules kbase");
		if (rules.size()>0){
			for (String rule:rules){
			    System.out.println(rule);
			}
		}
	}
	
	//@Test
	public void testProductConfigForBP() {
		List<ProductConfigEntity> lst=productDAO.findProductConfigActiveByBPName(Products.PRODUCT_PDL, "procProlongCR");
		System.out.println(lst.size());
		for (ProductConfigEntity config:lst){
			System.out.println(config.getName()+"="+config.getDataValue());
		}
		
	}
	
	@Test
	public void testProductMessagesForProcess(){
		List<ProductMessagesEntity> messages=productDAO.findProductMessagesForProcess(Products.PRODUCT_PDL, "procConsiderCR");
		System.out.println("Test message");
		if(messages.size()>0){
			ProductMessagesEntity msg=productDAO.getProductMessageFromList(messages, "sms.rejected");
			System.out.println(msg.getDescription());
			System.out.println(msg.getSubject());
			System.out.println(msg.getBody());
			for (ProductMessagesEntity message:messages){
			    System.out.println(message.getDescription());
			    System.out.println(message.getBody());
			}
		}
	}
}
