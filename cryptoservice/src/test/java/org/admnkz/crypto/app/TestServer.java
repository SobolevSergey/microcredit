package org.admnkz.crypto.app;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Assert;

import org.admnkz.common.IOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestServer {
	
	ICryptoService crw;	

	@Before
	public void setUp() throws Exception {
		final Properties p = new Properties();
        p.put("CryptoDS", "new://Resource?type=DataSource");
        p.put("CryptoDS.JdbcDriver", "org.postgresql.Driver");
        p.put("CryptoDS.JdbcUrl", "jdbc:postgresql://localhost:5432/criptosign?user=sa&password=drakon");

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        
//        opts = (IOptions) context.lookup("java:global/optionsholder/Options");
        
        crw = (ICryptoService) context.lookup("java:global/cryptoservice/CryptoService");
//        crw.setServiceRootPath("/home/irina/v/workspace/cryptoservice/data/securerandoms/");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Assert.assertNotNull(crw);
	}

}
