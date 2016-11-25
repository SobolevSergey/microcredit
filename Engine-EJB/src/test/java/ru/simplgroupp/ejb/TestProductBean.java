package ru.simplgroupp.ejb;

import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.transfer.Products;

public class TestProductBean {

	@EJB
	ProductBeanLocal productBean;
	
	@Before
	public void setUp() throws Exception {
	     System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);
	}

	//@Test
	public void testNewRequestConfig() {
		 Map<String, Object> limits =productBean.getNewRequestProductConfig(Products.PRODUCT_PDL);
		 for (Map.Entry<String, Object> obj: limits.entrySet()) {
			 System.out.println(obj.getKey()+"-"+obj.getValue());
		 }
	}

	//@Test
	public void testMessageForBA() {
		ProductMessagesEntity message=productBean.getMessageForBusinessAction(Products.PRODUCT_PDL, "msgOverdueEmail");
		if (message!=null){
			System.out.println("subject - "+message.getSubject());
			System.out.println("body - "+message.getBody());
		}
		
	}
	
	@Test
	public void testCopyProduct(){
		Products product=productBean.copyProduct(2);
		if (product!=null){
			System.out.println("Продукт "+product.getId()+" название "+product.getName());
		}
	}
}
