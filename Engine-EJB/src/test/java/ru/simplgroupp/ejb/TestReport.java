package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ReportBeanLocal;
import ru.simplgroupp.persistence.ReportEntity;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.transfer.Report;

public class TestReport {

	@EJB
	ReportBeanLocal reportBean;
	
	@Before
	public void setUp() throws Exception {
		
	   System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

	   final Properties p = new Properties();
	   p.load(this.getClass().getResourceAsStream("/test.properties"));

	   final Context context = EJBContainer.createEJBContainer(p).getContext();
	   context.bind("inject", this);
	}

	@Test
	public void test() {
		ReportEntity report=reportBean.findReportByProductAndCode(Products.PRODUCT_PDL, Report.OFERTA_ID);
		if (report!=null){
			System.out.println(report.getName());
		}
	}

}
