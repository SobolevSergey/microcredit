package org.admnkz.crypto.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Before;
import org.junit.Test;


public class TestCryptoConnection {

	@EJB
	ICryptoService crw;
	 
	@Before
	public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
		
		final Properties p = new Properties();
        p.put("CryptosignDS", "new://Resource?type=DataSource");
        p.put("CryptosignDS.JdbcDriver", "org.postgresql.Driver");
        p.put("CryptosignDS.JdbcUrl", "jdbc:postgresql://localhost:5432/criptosign?user=sa&password=drakon");
        p.setProperty("openejb.localcopy", "false");

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        crw = (ICryptoService) context.lookup("java:global/cryptoservice/CryptoService");
	}

	@Test
	public void test() throws Exception {
		SSLContext sslContext=crw.createTrustedSSLContext("ssl.OkbCais.client.work", "ssl.OkbCais.server");
		Security.setProperty("ssl.SocketFactory.provider", "ru.CryptoPro.ssl.SSLSocketFactoryImpl");
   	    SSLSocketFactory fact = (SSLSocketFactory) sslContext.getSocketFactory();
        
   	    URL url = new URL("https://www.rb-ei.com/");
  
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(fact);
        conn.setHostnameVerifier(allHostsValid());
 
       conn.setRequestMethod("GET");
       conn.setDoOutput(true);
    
       if (conn!=null){
           InputStream inputstream = conn.getInputStream();
           InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
           BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
   

           String string = null;
           while ((string = bufferedreader.readLine()) != null) {
               System.out.println("Received " + string);
           } 
       }

	}

	 private static HostnameVerifier allHostsValid(){
	    	HostnameVerifier allHostsValid = new HostnameVerifier() {
	  			public boolean verify(String hostname, SSLSession session) {
	  				return true;
	  			}
	  		};    
	  		return allHostsValid;
	    }
}
