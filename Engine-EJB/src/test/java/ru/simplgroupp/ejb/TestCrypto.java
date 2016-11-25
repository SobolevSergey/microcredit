package ru.simplgroupp.ejb;

import junit.framework.Assert;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.openejb.api.LocalClient;
import org.drools.core.util.IoUtils;
import org.junit.Before;
import org.junit.Test;

//import ru.CryptoPro.ssl.SSLSocketFactoryImpl;
import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.net.ssl.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.*;

@LocalClient
public class TestCrypto {
	
	@EJB
	ICryptoService crw;
	
	@EJB
    ReferenceBooksLocal refBook;
    
    @EJB
    PeopleBeanLocal ppl;    
    
    @EJB
    ScoringRsBeanLocal rs;
    
    @EJB
    ScoringSkbBeanLocal skb;
    
    @EJB
    ScoringOkbCaisBeanLocal cais;
    
    @EJB
    ScoringOkbIdvBeanLocal idv;
    
    @EJB
    ScoringEquifaxBeanLocal equifax;
    
    @EJB
	CreditRequestDAO crDAO;
    
    @EJB
    ScoringNBKIBeanLocal nbki;
    
    @Before
    public void setUp() throws Exception {
 
		System.setProperty("openejb.validation.output.level","VERBOSE");
		final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
         
       final Context context = EJBContainer.createEJBContainer(p).getContext();
//       Context context = new InitialContext(p);
        context.bind("inject", this);
        
        crw = (ICryptoService) context.lookup("java:global/cryptoservice/CryptoService");
        refBook = (ReferenceBooksLocal) context.lookup("java:global/Engine-EJB/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal");
        ppl = (PeopleBeanLocal) context.lookup("java:global/Engine-EJB/PeopleBean!ru.simplgroupp.interfaces.PeopleBeanLocal");
        rs= (ScoringRsBeanLocal) context.lookup("java:global/Engine-EJB/ScoringRsBean!ru.simplgroupp.interfaces.ScoringRsBeanLocal");
        skb= (ScoringSkbBeanLocal) context.lookup("java:global/Engine-EJB/ScoringSkbBean!ru.simplgroupp.interfaces.ScoringSkbBeanLocal");
        cais= (ScoringOkbCaisBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbCaisBean!ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal");
        nbki= (ScoringNBKIBeanLocal) context.lookup("java:global/Engine-EJB/ScoringNBKIBean!ru.simplgroupp.interfaces.ScoringNBKIBeanLocal");
        idv= (ScoringOkbIdvBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbIdvBean!ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal");
        equifax= (ScoringEquifaxBeanLocal) context.lookup("java:global/Engine-EJB/ScoringEquifaxBean!ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal");
    } 
    
    //@Test
    public void testServ() {
    	Assert.assertNotNull(crw);
    }
    
    
	//@Test
	public void testAddRsACert() throws Exception {

		/*byte[] cbody=FileUtils.readFileToByteArray(new File("d:\\2\\ca1.cer"));
		crw.addCertificate(null, "SC", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\2\\ca2.cer"));
		crw.addCertificate(null, "SC", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\2\\ca3.cer"));
		crw.addCertificate(null, "SC", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\2\\ca4.cer"));
		crw.addCertificate(null, "SC", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\2\\ca5.cer"));
		crw.addCertificate(null, "SC", cbody);*/
		/*byte[]cbody = FileUtils.readFileToByteArray(new File("d:\\2\\2.cer"));
		byte[] cpriv = FileUtils.readFileToByteArray(new File("d:\\2\\00000001.key"));
		byte[] csecr = FileUtils.readFileToByteArray(new File("d:\\2\\sr.zip"));
		Certificate acert = crw.addCertificate(null, null, cbody, cpriv, csecr,null);*/
		
		
	//	byte[] cbody = FileUtils.readFileToByteArray(new File("c:\\Users\\helen1\\1\\test_okb.cer"));
	//	crw.addCertificate(null, "JCP", cbody);
		
		byte[] cbody=FileUtils.readFileToByteArray(new File("c:\\2\\certnew.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("c:\\2\\test.cer"));
		crw.addCertificate(null, "JCP", cbody);
		
		//cbody = FileUtils.readFileToByteArray(new File("c:\\5\\ya.ca.cer"));
        //crw.addCertificate(null, "SUN", cbody, null, null, null);

		//cbody = FileUtils.readFileToByteArray(new File("c:\\5\\ya.cer"));
		//byte[] key  = FileUtils.readFileToByteArray(new File("c:\\5\\private-nopass.key"));
		//crw.addCertificate(null, "BC", cbody, key, null, null);
        
	//	byte[] cbody = FileUtils.readFileToByteArray(new File("c:\\Users\\helen1\\5\\ya.cer"));
	//	byte[] cpriv = FileUtils.readFileToByteArray(new File("c:\\Users\\helen1\\5\\private-nopass.key"));
		
	//	Certificate acert = crw.addCertificate(null, "SUN", cbody, cpriv, null,null);
		
//		byte[] cert = FileUtils.readFileToByteArray(new File("/tmp/cassaonline.cer"));
//        byte[] key  = FileUtils.readFileToByteArray(new File("/tmp/private-nopass.key"));
//        System.out.println("ssl client: "+ crw.addCertificate(null, "SUN", cert, key, null, null).getID());

//        cert = FileUtils.readFileToByteArray(new File("/tmp/deposit.cer"));
//        key  = FileUtils.readFileToByteArray(new File("/tmp/private-nopass.key"));
//        System.out.println("sign client: "+crw.addCertificate(null, "BC", cert, key, null, null).getID());

//        cert = FileUtils.readFileToByteArray(new File("/tmp/ca1.cer"));
//        System.out.println("ssl ca1: "+crw.addCertificate(null, "SUN", cert, key, null, null).getID());
//
//        byte[] cert = FileUtils.readFileToByteArray(new File("/tmp/ca2.cer"));
//        System.out.println("ssl ca2: "+crw.addCertificate(null, "SUN", cert, null, null, null).getID());

        //byte[] cbody=FileUtils.readFileToByteArray(new File("/home/kds/work/Slezin/kassa_common/doc/Alfabank-1C/client-pack-tst-2014/тестовые_клиенты/morengo-1.crt"));
        //crw.addCertificate(null, "JCP", cbody);
      
 		/*byte[] cbody=FileUtils.readFileToByteArray(new File("d:\\3\\CA.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\3\\cer-der.cer"));
		crw.addCertificate(null, "JCP", cbody);*/
		//byte[] cbody=FileUtils.readFileToByteArray(new File("c:\\6\\Latishev.cer"));
		//crw.addCertificate(null, "JCP", cbody);
		/*cbody=FileUtils.readFileToByteArray(new File("d:\\4\\CA2.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\4\\CA3.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\4\\CA4.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\4\\CA5.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\4\\CA6.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\4\\test.cer"));
		crw.addCertificate(null, "JCP", cbody);*/
//		byte[] cbody=FileUtils.readFileToByteArray(new File("d:\\8\\ca1.cer"));
//		crw.addCertificate(null, "SUN", cbody);
//		cbody=FileUtils.readFileToByteArray(new File("d:\\8\\ca2.cer"));
//		crw.addCertificate(null, "SUN", cbody);
//		cbody=FileUtils.readFileToByteArray(new File("d:\\8\\deposit1.cer"));
//		crw.addCertificate(null, "SUN", cbody);
//		cbody=FileUtils.readFileToByteArray(new File("d:\\8\\deposit.cer"));
//		crw.addCertificate(null, "SUN", cbody);
//		cbody=FileUtils.readFileToByteArray(new File("d:\\8\\cassaonline.cer"));
//		crw.addCertificate(null, "SUN", cbody);
 		/*byte[] cbody=FileUtils.readFileToByteArray(new File("d:\\7\\CA1.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\7\\CA2.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\7\\CA3.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\7\\CA4.cer"));
		crw.addCertificate(null, "JCP", cbody);
		cbody=FileUtils.readFileToByteArray(new File("d:\\7\\CA5.cer"));
		crw.addCertificate(null, "JCP", cbody);*/
// 		byte[] cbody=FileUtils.readFileToByteArray(new File("d:\\7\\qiwi-ca.cer"));
//		crw.addCertificate(null, "JCP", cbody);
	/*	byte[] cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/client-pack-tst-2014/тестовые_клиенты/alfabank-root-ca-tst.cer"));
		crw.addCertificate(null, "SC", cbody, null, null);
		cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/client-pack-tst-2014/тестовые_клиенты/ca_alfabank.cer"));
		crw.addCertificate(null, "SC", cbody, null, null);		
		cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/client-pack-tst-2014/тестовые_клиенты/innerca.cer"));
		crw.addCertificate(null, "SC", cbody, null, null);	*/		
/*		
		cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/signal-certs/alfa/director/director.cer"));
		byte[] cpriv = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/signal-certs/alfa/director/keys/00000001.key"));
		byte[] csecr = FileUtils.readFileToByteArray(new File("/home/irina/v/git/kassa_online/kassa_common/doc/Alfabank-1C/signal-certs/alfa/director/sr.zip"));
		Certificate acert = crw.addCertificate(null, null, cbody, cpriv, csecr);
*/		
	}    
    
	//@Test
	public void testCMS() throws Exception
	{
		String sbody = "Добрый день";
		byte[] data = sbody.getBytes();
		byte[] sig = crw.signCMS(data, CryptoSettings.CONTACT_PAY_SIGN_CLIENT_TEST);
        
		Assert.assertNotNull(sig);
		Assert.assertTrue(sig.length > 0);
		
		boolean bres = crw.verifyCMS(sig, data, CryptoSettings.CONTACT_PAY_SIGN_CLIENT_TEST, true);
        Assert.assertTrue(bres);
	}
	
   
          
//    @Test
    public void testSSL3() throws Exception {
    	Provider provider = null;
    	provider = (Provider) Class.forName("ru.signalcom.jsse.Provider").newInstance();    	
    	Security.addProvider(provider);
    	provider = (Provider) Class.forName("ru.signalcom.crypto.provider.SignalCOMProvider").newInstance();    	
    	Security.addProvider(provider);       	
    	Security.setProperty("ocsp.enable", "false");
    	System.setProperty("javax.net.debug", "ssl");
    	System.setProperty("com.sun.security.enableCRLDP", "false");
    	System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    	
    	SSLContext sslCon = crw.createSSLContext("ssl.rusStandart.client", "ssl.rusStandart.server");
    	Assert.assertNotNull(sslCon);
    	SSLSocketFactory fact = sslCon.getSocketFactory();	    	
    	SSLSocket sslSock = (SSLSocket) fact.createSocket("test-crs.rs-cb.ru", 443);
		String[] suites = sslSock.getSupportedCipherSuites();
		sslSock.setEnabledCipherSuites(suites);  
		sslSock.setUseClientMode(true);
		sslSock.addHandshakeCompletedListener(new MyHandshakeListener());

		try {
			sslSock.startHandshake();
		} catch (Exception ex) {
			
		}
    	
        InputStream in = sslSock.getInputStream();
        byte[] bytes = IoUtils.readBytesFromInputStream(in);

        System.out.println("Protocol: " + sslSock.getSession().getProtocol());
        System.out.println("CipherSuite: " + sslSock.getSession().getCipherSuite());
        try {
            System.out.println("PeerPrincipal: " + sslSock.getSession().getPeerPrincipal());
            java.security.cert.Certificate[] serverCerts = sslSock.getSession().getPeerCertificates();
            if (serverCerts.length > 0) {
            	
            }
        } catch (SSLPeerUnverifiedException e) {
        }
        
        sslSock.close();     	
    }
    
       
     
 //@Test
 public void testSSLCryptoConnectionOkb() throws Exception{
    	     
	/*	Security.setProperty("ssl.SocketFactory.provider","ru.CryptoPro.ssl.SSLSocketFactoryImpl");
    	Security.setProperty("ocsp.enable", "false");
    	System.setProperty("javax.net.debug", "all");
    	System.setProperty("com.sun.security.enableCRLDP", "true");
    	System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

    	 SSLContext sslContext=crw.createTrustedSSLContext(CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK);
         SSLSocketFactoryImpl fact = (SSLSocketFactoryImpl) sslContext.getSocketFactory();
         SSLSocket soc = (SSLSocket) fact.createSocket("www.rb-ei.com", 443);*/
  
    	 SSLContext sslContext=crw.createTrustedSSLContext(CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK);
    	 //SSLContext sslContext=crw.createTrustedSSLContext(CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST);
    	 //SSLSocketFactoryImpl fact = (SSLSocketFactoryImpl) sslContext.getSocketFactory();
         
    	 //URL url = new URL("https://nh-test.rb-ei.com/OnlineMatchingService/OnlineMatching.asmx?wsdl");
    	 //URL url = new URL("https://www.rb-ei.com/");
    	 //URL url = new URL("https://test.rb-ei.com/");
         URL url = new URL("https://weblink-2.rb-ei.com");
         //URL url = new URL("https://hs-2.rb-ei.com/HSMSwebservice1.asmx?wsdl");
         //URL url = new URL("https://hs-test.rb-ei.com/HSMSwebservice1.asmx?wsdl");
         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
       //  conn.setSSLSocketFactory(fact);
         conn.setHostnameVerifier(HTTPUtils.allHostsValid());
  
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
     
        if (conn!=null){
            InputStream inputstream = conn.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
       //     BufferedReader bufferedreader =  new BufferedReader(new InputStreamReader(soc.getInputStream(), XmlUtils.ENCODING_WINDOWS));

            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                System.out.println(string);
            } 
        }
 
         
    }
 
    public static String CBprop (String prop_name)
    {
    
         String prop_result = null;         
         Properties properties = new Properties();
   try {
         properties.load(new FileInputStream("c:\\workspace\\kassa-online\\cb.properties"));
   } catch (IOException e) {
         System.out.println("Got exception " + e);
   }
   

    System.out.println("Reading property: " + prop_name);
    prop_result = properties.getProperty(prop_name).trim(); // все пробелы будут удалены      
    return prop_result;
   
    
}
   
 //   @Test
    public void testSSLCryptoConnectionNbki() throws Exception{
       	
     
       	 SSLContext sslContext=crw.createTrustedSSLContext(CryptoSettings.NBKI_CLIENT_TEST, CryptoSettings.NBKI_SERVER_TEST);
      // 	 SSLSocketFactoryImpl fact = (SSLSocketFactoryImpl) sslContext.getSocketFactory();
               	
         URL url = new URL("https://icrs.demo.nbki.ru/");
           
         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
      //   conn.setSSLSocketFactory(fact);
         conn.setHostnameVerifier(HTTPUtils.allHostsValid());
     
         conn.setRequestMethod("GET");
         conn.setDoOutput(true);
        
         InputStream inputstream = conn.getInputStream();
         InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
         BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

         String string = null;
         while ((string = bufferedreader.readLine()) != null) {
               System.out.println("Received " + string);
         } 
                
   }
  
    //@Test
    public void testSSLRusStandard() throws Exception{
    	//Воробьянинов
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(24959);
    	//Конев
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(13626);
    	//Маслова
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(37065);
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(38);
    	//Меньшиков
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(36);
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(50885);
    	rs.newRequest(cre, false,10);
    	System.out.println("Успешно завершен");
    }
  
    //@Test
    public void testSSLSkb() throws Exception{
    	
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(25);
    	skb.newRequest(cre, true,10);
    	System.out.println("Успешно завершен");
    }
    
    //@Test
    public void testSSLRusStandardWork() throws Exception{
    	
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(19);
    	//CreditRequestEntity cre=kassa.getCreditRequestEntity(14650);
    	//обычный запрос
    	//rs.newRequest(cre, true,false,false,false,false,false,false,false,10,0);
    	//CreditRequestEntity cre=kassa.getCreditRequestEntity(15);
    	//сервис платежной истории
    	//rs.newRequest(cre, true,true,false,false,false,false,false,false,10,0);
    	//сервис верификации
    	//rs.newRequest(cre, false,false,false,false,false,true,false,false,10,0);
    	//сервис ФМС
    	//rs.newRequest(cre, true,false,true,false,false,false,false,false,10,0);
    	//ФССП
    	//rs.newRequest(cre, true,false,false,true,false,false,false,false,10,0);
        //Скоринг
    	//rs.newRequest(cre, true,false,false,false,false,true,true,false,10,0);
    	//сервис антифрод
    	rs.newRequest(cre, false,false,false,false,false,false,false,true,10,0);
    	System.out.println("Успешно завершен");
    }
    
   // @Test
    public void testSSLIdv() throws Exception{
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(22245);
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(30);
       	idv.newRequest(cre, false,10);
    	System.out.println("Успешно завершен");
    }
    
   //@Test
    public void testSSLOkb() throws Exception{
    	//тестовый заемщик
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(30);
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(50885);
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(42933);
	   // CreditRequestEntity cre=crDAO.getCreditRequestEntity(48093);
    	//Исаева
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(2);
    	cais.newRequest(cre, false,10);
    	//cais.newRequest(cre, false,10,Requests.FUNCTION_CAIS_SCORING);
    	//cais.newRequest(cre, false,10,Requests.FUNCTION_CAIS_EXTENDED);
    	System.out.println("Успешно завершен");
    }
    
  
  //  @Test
    public void testSSLNbki() throws Exception{
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(32);
    	//nbki.newRequest(cre, false,true,"score",10);
    	nbki.newRequest(cre, false,false,"products/B2BRequestServlet",10,false);
    	System.out.println("Успешно завершен");
    }
    
  //  @Test 
    public void testEquifaxVM() throws IOException{
    	 URL url = new URL("http://10.130.1.2");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setDoOutput(true);
      
         byte[] res=null;
         InputStream instrm = conn.getInputStream();
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         try {
             IOUtils.copy(instrm, bos);
             res = bos.toByteArray();
      } finally {
             IOUtils.closeQuietly(bos);
      }
  		
     
     String answer=new String(res,"Windows-1251");
     System.out.println(answer);
    }
    
  //  @Test
    public void testEquifaxRequest() throws Exception{
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(28);
    	//CreditRequestEntity cre=crDAO.getCreditRequestEntity(16);
    	CreditRequestEntity cre=crDAO.getCreditRequestEntity(50885);
    	equifax.newRequest(cre, false,10);
    	
    	//equifax.newRequest(cre, false,10);
    	System.out.println("Успешно завершен");
    }
    
   
        
  //  @Test
    public void testSSL2() throws Exception {
    	String host = "test-crs.rs-cb.ru";
    	int port= 443;
    	
    	Provider provider = null;
    	provider = (Provider) Class.forName("ru.signalcom.jsse.Provider").newInstance();    	
    	Security.addProvider(provider);
    	provider = (Provider) Class.forName("ru.signalcom.crypto.provider.SignalCOMProvider").newInstance();    	
    	Security.addProvider(provider);       	
    	Security.setProperty("ocsp.enable", "false");
    	System.setProperty("javax.net.debug", "ssl:handshake");
    	System.setProperty("com.sun.security.enableCRLDP", "false");
    	System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    	Assert.assertNotNull(crw);
    	
    	// создаём клиентское хранилище
    	File fks = new File("/home/irina/v/git/irk333/microcredit/cryptoservice/data/ks.keystore");
    	if (fks.exists()) {
    		fks.delete();
    	}
    	fks.createNewFile();
    	FileUtils.writeByteArrayToFile(fks, crw.generateKeyStore("JKS", "ssl.rusStandart.client", null, "1"));
    	
    	// создаём trustStore
    	File fts = new File("/home/irina/v/git/irk333/microcredit/cryptoservice/data/ts.keystore");
    	if (fts.exists()) {
    		fts.delete();
    	}
    	fts.createNewFile();
    	FileUtils.writeByteArrayToFile(fts, crw.generateKeyStore("JKS", "ssl.rusStandart.server", new int[] {3}, "1"));
    	
        Security.setProperty("ssl.SocketFactory.provider", "ru.signalcom.jsse.SSLSocketFactoryImpl");
       
        System.setProperty("javax.net.ssl.trustStore", "/home/irina/v/git/irk333/microcredit/cryptoservice/data/ts.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "1");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");        
        System.setProperty("javax.net.ssl.trustStoreProvider", "SC");
        System.setProperty("ssl.TrustManagerFactory.algorithm", "SCX509");

        System.setProperty("javax.net.ssl.keyStore", "/home/irina/v/git/irk333/microcredit/cryptoservice/data/ks.keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "1");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStoreProvider", "SC");
        System.setProperty("ssl.KeyManagerFactory.algorithm", "SCX509");
        
        System.out.println("Connecting to " + host + ":" + port);
        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

		// Create all-trusting host name verifier
        
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};        
        
        URL url = new URL("https://test-crs.rs-cb.ru/A2A/request.ashx");
//        HttpsURLConnection.setDefaultSSLSocketFactory(sf);
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(sf);
        conn.setHostnameVerifier(allHostsValid);

        InputStream inputstream = conn.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        String string = null;
        while ((string = bufferedreader.readLine()) != null) {
            System.out.println("Received " + string);
        }       
    }
    
//    @Test
    public void testSSL4() throws Exception {
    	Security.setProperty("ocsp.enable", "false");
    	Provider provider = null;
    	provider = (Provider) Class.forName("ru.signalcom.jsse.Provider").newInstance();    	
    	Security.addProvider(provider);
    	provider = (Provider) Class.forName("ru.signalcom.crypto.provider.SignalCOMProvider").newInstance();    	
    	Security.addProvider(provider);    	
    	
    	System.setProperty("javax.net.debug", "ssl");
    	System.setProperty("com.sun.security.enableCRLDP", "false");
    	System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    	
    	Assert.assertNotNull(crw);
    	SSLContext sslCon = crw.createSSLContext("ssl.rusStandart.client", "ssl.rusStandart.server");
    	Assert.assertNotNull(sslCon);
    	SSLSocketFactory fact = sslCon.getSocketFactory();	

		// Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};        
       
           
    	SSLSocket sslSock = (SSLSocket) fact.createSocket("test-crs.rs-cb.ru", 443);
		String[] suites = sslSock.getSupportedCipherSuites();
		sslSock.setEnabledCipherSuites(suites);  
		sslSock.setUseClientMode(true);
		sslSock.addHandshakeCompletedListener(new MyHandshakeListener());

		try {
			sslSock.startHandshake();
		} catch (Exception ex) {
			
		}
    	
//        OutputStream out = sslSock.getOutputStream();
        InputStream in = sslSock.getInputStream();
        byte[] bytes = IoUtils.readBytesFromInputStream(in);

        System.out.println("Protocol: " + sslSock.getSession().getProtocol());
        System.out.println("CipherSuite: " + sslSock.getSession().getCipherSuite());
        try {
            System.out.println("PeerPrincipal: " + sslSock.getSession().getPeerPrincipal());
            java.security.cert.Certificate[] serverCerts = sslSock.getSession().getPeerCertificates();
            if (serverCerts.length > 0) {
            	
            }
        } catch (SSLPeerUnverifiedException e) {
        }
        
        sslSock.close();  
        
    }    
 
    public static String escape(String source) 
    {
      char[] ch = null;
      try
      {
       ch = new String(source.getBytes()).toCharArray();  //ch = new String(source.getBytes(), "UTF-8").toCharArray();
      }
      catch (Exception e) {};
      
      int code;
      StringBuffer result = new StringBuffer();
      for (int i=0; i<ch.length; i++)
      {
        code = ch[i];
        if ((code >= 48 && code <=  57) || 
  					(code >= 65 && code <=  90) ||
  					(code >= 97 && code <= 122))
  				{
  					// Alphanumeric
  					result.append(ch[i]);
  				}
  				else if (code < 256)
  				{
  					// Hex escape - %hh
            String hex = Integer.toHexString(0+ch[i]);
  					result.append("%"); 
            if (hex.length()< 2) result.append("0"); 
            result.append(Integer.toHexString(0+ch[i]));
  				}
  				else if (code != 13)
  				{
  					// HTML escape &#nnnn; (with a Hex escape)
  					result.append("%26%23");	result.append(0+ch[i]); result.append("%3B");
  				}
      }
      return result.toString();
    }
  
   // @Test 
    public void testEncrypt() throws Exception
    {
    	String text="Здравствуйте!";
    	//byte[] bs=crw.createJCPCMS(text.getBytes(), CryptoSettings.OKB_CLIENT_WORK, false);
    	byte[] enc=crw.encryptCMS(CryptoSettings.OKB_CLIENT_WORK,CryptoSettings.EQUIFAX_SIGN_TEST,text.getBytes(),"c:\\Users\\helen1\\123.txt");
    	byte[] ss=crw.decryptMessage(CryptoSettings.EQUIFAX_SIGN_TEST, enc);
        System.out.println(new String(ss));
 //   	byte[] bs=crw.createJCPCMS(text.getBytes(), CryptoSettings.OKB_CLIENT_WORK, false);
 //   	byte[] enc=crw.encryptPKCS7(bs,CryptoSettings.OKB_CLIENT_WORK,"c:\\Users\\helen1\\123.txt");
    //	crw.decryptPKCS7(CryptoSettings.OKB_CLIENT_WORK, enc, null, false, "c:\\Users\\helen1\\321.txt");
    //	crw.encryptCMS(bs, CryptoSettings.EQUIFAX_SIGN_TEST, CryptoSettings.EQUIFAX_CHECK_TEST, "c:\\Users\\helen1\\123.txt");
    //    crw.encryptPKCS7(bs,CryptoSettings.EQUIFAX_CHECK_TEST,"c:\\Users\\helen1\\123.txt");
    //    crw.encryptCMS(bs, CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_ENCRYPT_TEST, "c:\\Users\\helen1\\123.txt");
    //	crw.encryptCMS(bs, CryptoSettings.EQUIFAX_CHECK_TEST);
       	
    }
    
    @Test
    public void testDecrypt() throws IOException, Exception{
    
    	byte[] zip=crw.decryptMessage(CryptoSettings.NBKI_ENCRYPT_CLIENT_WORK,new File("c:\\1\\upload\\123.enc"));
        FileUtils.writeByteArrayToFile(new File("c:\\1\\upload\\123.zip"), zip);
    
    	
    }
    
   // @Test 
    public void testDetached() throws Exception
    {
    	  File file=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+"Strategy.txt");
		  byte sign[]=null;
		  //подписываем, подпись отсоединенная
		  try {
			  sign=crw.signDetachedCMS(CryptoSettings.EQUIFAX_SIGN_WORK, file, XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+"Strategy.txt"+".sgn");
	
	      } catch (Exception e) {
	    
		      throw new KassaException("Не удалось подписать файл "+e);
	      }
		  System.out.println("Успешно завершен");
    }
    
  //  @Test
    public void testSSL() throws Exception {
    	Security.setProperty("ocsp.enable", "false");
    	Provider provider = null;
    	provider = (Provider) Class.forName("ru.signalcom.jsse.Provider").newInstance();    	
    	Security.addProvider(provider);
    	provider = (Provider) Class.forName("ru.signalcom.crypto.provider.SignalCOMProvider").newInstance();    	
    	Security.addProvider(provider);    	
    	
    	System.setProperty("javax.net.debug", "ssl");
    	System.setProperty("com.sun.security.enableCRLDP", "false");
    	System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    	
    	Assert.assertNotNull(crw);
    	SSLContext sslCon = crw.createSSLContext(CryptoSettings.CONTACT_PAY_CLIENT_TEST, CryptoSettings.CONTACT_PAY_SERVER_TEST);
    	
    	Assert.assertNotNull(sslCon);
    	SSLSocketFactory fact = sslCon.getSocketFactory();	

		// Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};        
       
        
        URL url = new URL("https://enter.contact-sys.com:2221/wstrans/wsTrans.exe/soap/ITransmitter");
//        HttpsURLConnection.setDefaultSSLSocketFactory(fact);
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(fact);
        conn.setHostnameVerifier(allHostsValid);
        
        InputStream inputstream = conn.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        String string = null;
        while ((string = bufferedreader.readLine()) != null) {
            System.out.println("Received " + string);
        }    	

    }
   

}


class MyHandshakeListener implements HandshakeCompletedListener {
	public void handshakeCompleted(HandshakeCompletedEvent e) {
		System.out.println("Handshake succesful!");
		System.out.println("Using cipher suite: " + e.getCipherSuite());
	}
}


