package org.admnkz.crypto.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.admnkz.crypto.app.ICryptoService.ParseCMSResult;
import org.admnkz.crypto.app.ICryptoService.ParseCertResult;
import org.admnkz.crypto.data.Certificate;
import org.admnkz.crypto.data.Settings;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.utils.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCryptoService 
{

    @EJB
    ICryptoService crw;
		
	@Before
	public void setUp() throws Exception {
		System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
		
		final Properties p = new Properties();
        p.put("CryptosignDS", "new://Resource?type=DataSource");
        p.put("CryptosignDS.JdbcDriver", "org.postgresql.Driver");
        p.put("CryptosignDS.JdbcUrl", "jdbc:postgresql://localhost:5432/criptosign?user=sa&password=58025");
        p.setProperty("openejb.localcopy", "false");

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        crw = (ICryptoService) context.lookup("java:global/cryptoservice/CryptoService");
        
	}

	@After
	public void tearDown() throws Exception {
	}
	
	//@Test
	public void testSignAndVerify() throws Exception {
		byte[] data = "у попа была собака".getBytes();
		byte[] signature = crw.sign(data, "sp.RStandard");
		boolean bres = crw.verify(data, signature, "sp.RStandard", false);
		Assert.assertTrue(bres);
	}
/*
	@Test
	public void testLoadPVK() throws Exception {
		PrivateKey pvk = crw.decodePrivateKeyJCP("test123");
	}
*/	

//	@Test
	public void testIsPair() throws Exception 
	{
		Certificate cc = crw.getCertificate("5c655b71e76dd2dd8ab335686b1dd306cf5176e");
		boolean bres = crw.checkIsPair(cc);
		Assert.assertTrue(bres);
	}
	
//	@Test
	public void testSoap() throws Exception 
	{
		
		String sxmlUnsigned = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			      "<SOAP-ENV:Envelope" + " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			      "<SOAP-ENV:Body>" +
			      "    <sayHello xmlns=\"http://jeffhanson.com/services/helloworld\">" +
			      "        <value xmlns=\"\">Hello world!</value>" +
			      "    </sayHello>" +
			      "</SOAP-ENV:Body>" +
			      "</SOAP-ENV:Envelope>";
		
		SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(sxmlUnsigned);
		
		crw.signSOAP(ms, "smev.ov.4216005979");		
		String sxmlSigned = CryptoUtils.SOAPMessageToString(ms);
		System.out.println();
		System.out.println(sxmlSigned);
		System.out.println();
		
		boolean bres = crw.verifySOAP(ms, "smev.ov.4216005979", false);
		Assert.assertTrue(bres);
	}
	
//	@Test
	public void testVerifyFNS1() throws Exception 
	{
//		String sbase64 = "PFNPQVAtRU5WOkVudmVsb3BlIHhtbG5zOlNPQVAtRU5WPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy9zb2FwL2VudmVsb3BlLyIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiIHhtbG5zOndzc2U9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtd3NzZWN1cml0eS1zZWNleHQtMS4wLnhzZCIgeG1sbnM6d3N1PSJodHRwOi8vZG9jcy5vYXNpcy1vcGVuLm9yZy93c3MvMjAwNC8wMS9vYXNpcy0yMDA0MDEtd3NzLXdzc2VjdXJpdHktdXRpbGl0eS0xLjAueHNkIj48U09BUC1FTlY6SGVhZGVyPjx3c3NlOlNlY3VyaXR5IFNPQVAtRU5WOmFjdG9yPSJodHRwOi8vc21ldi5nb3N1c2x1Z2kucnUvYWN0b3JzL3NtZXYiIHhtbG5zOndzc2U9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtd3NzZWN1cml0eS1zZWNleHQtMS4wLnhzZCI+PHdzc2U6QmluYXJ5U2VjdXJpdHlUb2tlbiBFbmNvZGluZ1R5cGU9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtc29hcC1tZXNzYWdlLXNlY3VyaXR5LTEuMCNCYXNlNjRCaW5hcnkiIFZhbHVlVHlwZT0iaHR0cDovL2RvY3Mub2FzaXMtb3Blbi5vcmcvd3NzLzIwMDQvMDEvb2FzaXMtMjAwNDAxLXdzcy14NTA5LXRva2VuLXByb2ZpbGUtMS4wI1g1MDl2MyIgd3N1OklkPSJDZXJ0SWQiPk1JSUVRRENDQSsrZ0F3SUJBZ0lLSHlPK2lnQUNBQUFTRHpBSUJnWXFoUU1DQWdNd2dlTXhIakFjQmdrcWhraUc5dzBCQ1FFV0QzVmoKTFhObWIwQm5ibWwyWXk1eWRURUxNQWtHQTFVRUJoTUNVbFV4R1RBWEJnTlZCQWNNRU5DYTBMWFF2TkMxMFlEUXZ0Q3kwTDR4TURBdQpCZ05WQkFvTUo5Q2swSlBRbzlDZklOQ1QwSjNRbU5DUzBLWWcwS1RRbmRDaElOQ2cwTDdSZ2RHQjBMalF1REZQTUUwR0ExVUVDd3hHCjBLVFF1TkM3MExqUXNOQzdJTkN5SU5DaDBMalFzZEM0MFlEUmdkQzYwTDdRdkNEUXBOQzEwTFRRdGRHQTBMRFF1OUdNMEwzUXZ0QzgKSU5DKzBMclJnTkdEMExQUXRURVdNQlFHQTFVRUF4TU5SMDVKVmtNZ1JrNVRJRk5HVHpBZUZ3MHhNakV3TVRjd09EQXhNREJhRncweApNekV3TVRjd09ERXhNREJhTUlJQlBqRWpNQ0VHQ1NxR1NJYjNEUUVKQVJZVWNHOXpkRzFoYzNSbGNrQmhaRzF1YTNvdWNuVXhDekFKCkJnTlZCQVlUQWxKVk1UVXdNd1lEVlFRSUhpd0FOQUF5QUNBRUdnUTFCRHdFTlFSQUJENEVNZ1JCQkRvRU1BUlBBQ0FFUGdReEJEc0UKTUFSQkJFSUVUREVmTUIwR0ExVUVCeDRXQkIwRVBnUXlCRDRFT2dSREJEY0VQUVExQkVZRU9qRkxNRWtHQTFVRUNoNUNCQkFFTkFROApCRGdFUFFRNEJFRUVRZ1JBQkRBRVJnUTRCRThBSUFRekJENEVRQVErQkRRRU1BQWdCQjBFUGdReUJENEVPZ1JEQkRjRVBRUTFCRVlFCk9nUXdNUmd3RmdZRktvVURaQUVURFRFd01qUXlNREUwTnpBMU5UWXhTekJKQmdOVkJBTWVRZ1FRQkRRRVBBUTRCRDBFT0FSQkJFSUUKUUFRd0JFWUVPQVJQQUNBRU13UStCRUFFUGdRMEJEQUFJQVFkQkQ0RU1nUStCRG9FUXdRM0JEMEVOUVJHQkRvRU1EQmpNQndHQmlxRgpBd0lDRXpBU0JnY3FoUU1DQWlRQUJnY3FoUU1DQWg0QkEwTUFCRUFpdC9vOThsZy95bGVYUjQzOTNmWDFXOXQ0VWxLdHM3K2lTYUVGCkdBdTQzWTJxTmF2Nit2YTNVSjRuSmVEcWJXZm51SkdFRkZmdEdqWG94TURXUVZzQ280SUJJekNDQVI4d0RnWURWUjBQQVFIL0JBUUQKQWdUd01DNEdBMVVkSlFRbk1DVUdCeXFGQXdJQ0lnWUdDQ3NHQVFVRkJ3TUVCZ2dyQmdFRkJRY0RBZ1lHS29VRFpBSUNNQjBHQTFVZApEZ1FXQkJTWFFuTkJzeEk5eWR6WnNOUTl1S0h5cmY5NUN6QWZCZ05WSFNNRUdEQVdnQlQxUHlKeTY5ZkdKdVk2bHBTSkVuZFhiNmxICnp6QThCZ05WSFI4RU5UQXpNREdnTDZBdGhpdG9kSFJ3T2k4dmNtRXVjMlp2TG1kdWFYWmpMbkoxTDJkdWFYWmpabTV6YzJadlh6SXcKTVRFdVkzSnNNRWNHQ0NzR0FRVUZCd0VCQkRzd09UQTNCZ2dyQmdFRkJRY3dBb1lyYUhSMGNEb3ZMM0poTG5ObWJ5NW5ibWwyWXk1eQpkUzluYm1sMlkyWnVjM05tYjE4eU1ERXhMbU5sY2pBV0JnTlZIU0FFRHpBTk1Bc0dDU3FGQXdOU0FRRUZCREFJQmdZcWhRTUNBZ01EClFRQXlaY3Z1emRtWnI0c1pQd2lGMzRxT1p3c1NMU2RNelhhcmY4NWx2WnVNdUdVbHdDdzFkallKWXZCTmgrRHR5NUpzSUFGbUZrMWwKdVFvMW9uakN5U0FCPC93c3NlOkJpbmFyeVNlY3VyaXR5VG9rZW4+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PGRzOlNpZ25lZEluZm8+PGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48ZHM6U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjZ29zdHIzNDEwMjAwMS1nb3N0cjM0MTEiLz48ZHM6UmVmZXJlbmNlIFVSST0iI2JvZHkiPjxkczpUcmFuc2Zvcm1zPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48L2RzOlRyYW5zZm9ybXM+PGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI2dvc3RyMzQxMSIvPjxkczpEaWdlc3RWYWx1ZT51K0VPVEFCa1dKQnlUU0owU0NQVG1ZSzBEVnV2Tm5GQzdRZFJxMlhCNU1jPTwvZHM6RGlnZXN0VmFsdWU+PC9kczpSZWZlcmVuY2U+PC9kczpTaWduZWRJbmZvPjxkczpTaWduYXR1cmVWYWx1ZT4vdEhHaVJWczBVQVNlNWZ3MGhrUlVjeThRVmNGdWhtZnQ4ZWxSZTF5ek5rK3NxYktmVlNiM1I0VnQ2QmVQWVk0NEFsOFdnWUFpVnE2CktRVFB4bnlkRnc9PTwvZHM6U2lnbmF0dXJlVmFsdWU+PGRzOktleUluZm8+PHdzc2U6U2VjdXJpdHlUb2tlblJlZmVyZW5jZT48d3NzZTpSZWZlcmVuY2UgVVJJPSIjQ2VydElkIiBWYWx1ZVR5cGU9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3MteDUwOS10b2tlbi1wcm9maWxlLTEuMCNYNTA5djMiLz48L3dzc2U6U2VjdXJpdHlUb2tlblJlZmVyZW5jZT48L2RzOktleUluZm8+PC9kczpTaWduYXR1cmU+PC93c3NlOlNlY3VyaXR5PjxyZXF1ZXN0IHhtbG5zPSJodHRwOi8vd3d3LmFkbW5rei5ydS9zbWV2IiBJRD0iOTcyMSIgU09BUC1FTlY6bXVzdFVuZGVyc3RhbmQ9IjAiIHNpZ25JRD0ic21ldi5vdi40MjE2MDA1OTc5Ii8+PC9TT0FQLUVOVjpIZWFkZXI+PFNPQVAtRU5WOkJvZHkgd3N1OklkPSJib2R5Ij48bnM0OlN5bmNSZXF1ZXN0IHhtbG5zOm5zND0iaHR0cDovL3NtZXYuZ29zdXNsdWdpLnJ1L01zZ0V4YW1wbGUveHNkL3R5cGVzIiB4bWxuczpuczI9Imh0dHA6Ly93d3cudzMub3JnLzIwMDQvMDgveG9wL2luY2x1ZGUiIHhtbG5zOm5zMz0iaHR0cDovL3NtZXYuZ29zdXNsdWdpLnJ1L3JldjExMDgwMSI+PG5zMzpNZXNzYWdlPjxuczM6U2VuZGVyPjxuczM6Q29kZT5OVktaMDE0MjE8L25zMzpDb2RlPjxuczM6TmFtZT7QkNC00LzQuNC90LjRgdGC0YDQsNGG0LjRjyDQs9C+0YDQvtC00LAg0J3QvtCy0L7QutGD0LfQvdC10YbQutCwPC9uczM6TmFtZT48L25zMzpTZW5kZXI+PG5zMzpSZWNpcGllbnQ+PG5zMzpDb2RlPjE8L25zMzpDb2RlPjxuczM6TmFtZT5Gb2l2MTwvbnMzOk5hbWU+PC9uczM6UmVjaXBpZW50PjxuczM6T3JpZ2luYXRvcj48bnMzOkNvZGU+TlZLWjAxNDIxPC9uczM6Q29kZT48bnMzOk5hbWU+0JDQtNC80LjQvdC40YHRgtGA0LDRhtC40Y8g0LPQvtGA0L7QtNCwINCd0L7QstC+0LrRg9C30L3QtdGG0LrQsDwvbnMzOk5hbWU+PC9uczM6T3JpZ2luYXRvcj48bnMzOlR5cGVDb2RlPkdTUlY8L25zMzpUeXBlQ29kZT48bnMzOkRhdGU+MjAxMi0xMS0wN1QxNjo1Njo1Ni4yODFaPC9uczM6RGF0ZT48bnMzOlNlcnZpY2VDb2RlPlNJRDAwMDMwMDE8L25zMzpTZXJ2aWNlQ29kZT48bnMzOkNhc2VOdW1iZXI+OTcyMTwvbnMzOkNhc2VOdW1iZXI+PFRlc3RNc2cgeG1sbnM9Imh0dHA6Ly9zbWV2Lmdvc3VzbHVnaS5ydS9yZXYxMTA4MDEiLz48L25zMzpNZXNzYWdlPjxuczM6TWVzc2FnZURhdGE+PG5zMzpBcHBEYXRhLz48L25zMzpNZXNzYWdlRGF0YT48L25zNDpTeW5jUmVxdWVzdD48L1NPQVAtRU5WOkJvZHk+PC9TT0FQLUVOVjpFbnZlbG9wZT4=";
		String sbase64 = "PFM6RW52ZWxvcGUgeG1sbnM6Uz0iaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvc29hcC9lbnZlbG9wZS8iIHhtbG5zOndzc2U9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtd3NzZWN1cml0eS1zZWNleHQtMS4wLnhzZCIgeG1sbnM6d3N1PSJodHRwOi8vZG9jcy5vYXNpcy1vcGVuLm9yZy93c3MvMjAwNC8wMS9vYXNpcy0yMDA0MDEtd3NzLXdzc2VjdXJpdHktdXRpbGl0eS0xLjAueHNkIj48UzpIZWFkZXI+PHdzc2U6U2VjdXJpdHkgUzphY3Rvcj0iaHR0cDovL3NtZXYuZ29zdXNsdWdpLnJ1L2FjdG9ycy9zbWV2Ij48ZHM6U2lnbmF0dXJlIHhtbG5zOmRzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj4KPGRzOlNpZ25lZEluZm8+CjxkczpDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+CjxkczpTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNnb3N0cjM0MTAyMDAxLWdvc3RyMzQxMSIvPgo8ZHM6UmVmZXJlbmNlIFVSST0iI2JvZHkiPgo8ZHM6VHJhbnNmb3Jtcz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIi8+CjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz4KPC9kczpUcmFuc2Zvcm1zPgo8ZHM6RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjZ29zdHIzNDExIi8+CjxkczpEaWdlc3RWYWx1ZT4zaUV6bEZVN1ZmblIrMXFLNUdBS0p1N3pWYzRHekJHVUd3enBibTJlYXZRPTwvZHM6RGlnZXN0VmFsdWU+CjwvZHM6UmVmZXJlbmNlPgo8L2RzOlNpZ25lZEluZm8+CjxkczpTaWduYXR1cmVWYWx1ZT4KYUxqWVhUNU5Sc01qRml5RDVkQTlMMVdLS24wWU8xdVNVVVhvZ0hya2s4eUplQWhER2tnQWhuQSszdWpPRzdvMmhyVzhlbjJ0RkJXegpDcXNITmxER2h3PT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPGRzOktleUluZm8+Cjx3c3NlOlNlY3VyaXR5VG9rZW5SZWZlcmVuY2U+PHdzc2U6UmVmZXJlbmNlIFVSST0iI0ZOU0NlcnRpZmljYXRlIi8+PC93c3NlOlNlY3VyaXR5VG9rZW5SZWZlcmVuY2U+CjwvZHM6S2V5SW5mbz4KPC9kczpTaWduYXR1cmU+PHdzc2U6QmluYXJ5U2VjdXJpdHlUb2tlbiBFbmNvZGluZ1R5cGU9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtc29hcC1tZXNzYWdlLXNlY3VyaXR5LTEuMCNCYXNlNjRCaW5hcnkiIFZhbHVlVHlwZT0iaHR0cDovL2RvY3Mub2FzaXMtb3Blbi5vcmcvd3NzLzIwMDQvMDEvb2FzaXMtMjAwNDAxLXdzcy14NTA5LXRva2VuLXByb2ZpbGUtMS4wI1g1MDl2MyIgd3N1OklkPSJGTlNDZXJ0aWZpY2F0ZSI+TUlJRzB6Q0NCb0tnQXdJQkFnSUtHd2VBSmdBQUFBQUJTakFJQmdZcWhRTUNBZ013Z2dFeE1SZ3dGZ1lGS29VRFpBRVREVEV5TXpRMU5qYzRPVEF4TWpNeEdEQVdCZ2dxaFFNRGdRTUJBUk1LTVRJek5EVTJOemc1TURFb01DWUdBMVVFQ1F3ZjBLSFJnOUdKMExYUXN0R0IwTHJRdU5DNUlOQ3kwTERRdXlEUXRDNHlOakVYTUJVR0NTcUdTSWIzRFFFSkFSWUlZMkZBY25RdWNuVXhDekFKQmdOVkJBWVRBbEpWTVJVd0V3WURWUVFJREF6UW5OQyswWUhRdXRDeTBMQXhGVEFUQmdOVkJBY01ETkNjMEw3UmdkQzYwTExRc0RFa01DSUdBMVVFQ2d3YjBKN1FrTkNlSU5DZzBMN1JnZEdDMExYUXU5QzEwTHJRdnRDOE1UQXdMZ1lEVlFRTERDZlFvOUMwMEw3UmdkR0MwTDdRc3RDMTBZRFJqOUdPMFluUXVOQzVJTkdHMExYUXZkR0MwWUF4SlRBakJnTlZCQU1NSE5DaTBMWFJnZEdDMEw3UXN0R0wwTGtnMEtQUXBpRFFvTkNpMEpvd0hoY05NVEl3TnpBek1UTXdNakF3V2hjTk1UTXdOekF6TVRNeE1UQXdXakNCb1RFTE1Ba0dBMVVFQmhNQ1VsVXhhekJwQmdOVkJBb2VZZ1FrQkRVRU5BUTFCRUFFTUFRN0JFd0VQUVF3QkU4QUlBUWRCREFFT3dRK0JETUVQZ1F5QkRBRVR3QWdCQ0VFT3dSREJEWUVNUVF3QUNBRUlBUStCRUVFUVFRNEJEa0VRUVE2QkQ0RU9RQWdCQ1FFTlFRMEJEVUVRQVF3QkVZRU9BUTRNU1V3SXdZRFZRUURIaHdFSVFRY0JCSUFJQVFrQkIwRUlRQWdCQ0FFUGdSQkJFRUVPQVE0TUdNd0hBWUdLb1VEQWdJVE1CSUdCeXFGQXdJQ0pBQUdCeXFGQXdJQ0hnRURRd0FFUUlBRzUzR3IvY1hhMWtmTFprYUNSN0ExdTZWZ1dEcXQzNkdwNmZxUi8xVHpoTHd1aW5Gakp2MldhVHdLcVN1NTJHczhHSi9aa3RJL0FBUHFXUy8wOGhxamdnUUZNSUlFQVRBT0JnTlZIUThCQWY4RUJBTUNCUEF3SkFZRFZSMGxCQjB3R3dZR0tvVURaQUlDQmdjcWhRTUNBaUlHQmdnckJnRUZCUWNEQWpBZEJnTlZIUTRFRmdRVUcrWXNRZGtJOEdKTDB3eEV5OHU1S2JJTmVpVXdnZ0Z5QmdOVkhTTUVnZ0ZwTUlJQlpZQVVHcXpzcUpxeU1FdG5adVdyTENYcXF3cUJ3ZE9oZ2dFNXBJSUJOVENDQVRFeEdEQVdCZ1VxaFFOa0FSTU5NVEl6TkRVMk56ZzVNREV5TXpFWU1CWUdDQ3FGQXdPQkF3RUJFd294TWpNME5UWTNPRGt3TVNnd0pnWURWUVFKREIvUW9kR0QwWW5RdGRDeTBZSFF1dEM0MExrZzBMTFFzTkM3SU5DMExqSTJNUmN3RlFZSktvWklodmNOQVFrQkZnaGpZVUJ5ZEM1eWRURUxNQWtHQTFVRUJoTUNVbFV4RlRBVEJnTlZCQWdNRE5DYzBMN1JnZEM2MExMUXNERVZNQk1HQTFVRUJ3d00wSnpRdnRHQjBMclFzdEN3TVNRd0lnWURWUVFLREJ2UW50Q1EwSjRnMEtEUXZ0R0IwWUxRdGRDNzBMWFF1dEMrMEx3eE1EQXVCZ05WQkFzTUo5Q2owTFRRdnRHQjBZTFF2dEN5MExYUmdOR1AwWTdSaWRDNDBMa2cwWWJRdGRDOTBZTFJnREVsTUNNR0ExVUVBd3djMEtMUXRkR0IwWUxRdnRDeTBZdlF1U0RRbzlDbUlOQ2cwS0xRbW9JUVlreEhiemdVY3FaR3YzRExEUjQwR3pCWkJnTlZIUjhFVWpCUU1FNmdUS0JLaGtob2RIUndPaTh2TVRnNExqSTFOQzR4Tmk0NE9TOXlZUzlqWkhBdk1XRmhZMlZqWVRnNVlXSXlNekEwWWpZM05qWmxOV0ZpTW1NeU5XVmhZV0l3WVRneFl6RmtNeTVqY213d1J3WUlLd1lCQlFVSEFRRUVPekE1TURjR0NDc0dBUVVGQnpBQ2hpdG9kSFJ3T2k4dk1UZzRMakkxTkM0eE5pNDRPUzl5WVM5alpIQXZkR1Z6ZEY5allWOXlkR3N1WTNKME1FOEdCU3FGQTJSdkJFWU1SQ0xRbXRHQTBMalF2OUdDMEw3UW45R0EwTDRnUTFOUUlpQW8wTExRdGRHQTBZSFF1TkdQSURNdU5pa2dLTkM0MFlIUXY5QyswTHZRdmRDMTBMM1F1TkMxSURFcE1Dc0dBMVVkRUFRa01DS0FEekl3TVRJd056QXpNVE13TWpBd1dvRVBNakF4TXpBM01ETXhNekF5TURCYU1Db0dBMVVkSUFRak1DRXdDQVlHS29VRFpBSUNNQWtHQnlxRkF3SUNJZ1l3Q2dZSUt3WUJCUVVIQXdJd2dlVUdCU3FGQTJSd0JJSGJNSUhZRENzaTBKclJnTkM0MEwvUmd0QyswSi9SZ05DK0lFTlRVQ0lnS05DeTBMWFJnTkdCMExqUmp5QXpMallwREZRaTBLUFF0TkMrMFlIUmd0QyswTExRdGRHQTBZL1JqdEdKMExqUXVTRFJodEMxMEwzUmd0R0FJQ0xRbXRHQTBMalF2OUdDMEw3UW45R0EwTDRnMEtQUXBpSWlJTkN5MExYUmdOR0IwTGpSanlBeExqVU1MTkNoMEtRdk1USTBMVEUxTkRNZzBMN1JnaUEwSU5DKzBMclJndEdQMExIUmdOR1BJREl3TVRBZzBMTXVEQ1hRb2RDa0x6RXlPQzB4TmpVNElOQyswWUlnTURFZzBMelFzTkdQSURJd01URWcwTE11TUFnR0JpcUZBd0lDQXdOQkFMR0xoR0VBUE8yZTlGN2JwUEs4eGZaSTRMOWxqeU9HVGMwb1I2MmcrUmgwV1pENCszLy9LTExMU2ExTjFWZTd5eTBmbG1MQVh4UUxXMUpUbHpmdThQST08L3dzc2U6QmluYXJ5U2VjdXJpdHlUb2tlbj48L3dzc2U6U2VjdXJpdHk+PC9TOkhlYWRlcj48UzpCb2R5IHdzdTpJZD0iYm9keSI+PHdzOlNlbmRSZXF1ZXN0UnMgeG1sbnM6d3M9Imh0dHA6Ly93cy51bmlzb2Z0LyIgeG1sbnM6Uz0iaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvc29hcC9lbnZlbG9wZS8iPgo8c21ldjpNZXNzYWdlIHhtbG5zOnNtZXY9Imh0dHA6Ly9zbWV2Lmdvc3VzbHVnaS5ydS9yZXYxMTExMTEiPjxzbWV2OlNlbmRlcj48c21ldjpDb2RlPkZOUzAwMTAwMTwvc21ldjpDb2RlPjxzbWV2Ok5hbWU+0KTQndChINCg0L7RgdGB0LjQuDwvc21ldjpOYW1lPjwvc21ldjpTZW5kZXI+PHNtZXY6UmVjaXBpZW50PjxzbWV2OkNvZGU+MTMyNDI8L3NtZXY6Q29kZT48c21ldjpOYW1lPk1JTlNWWUFaX1NZU18xPC9zbWV2Ok5hbWU+PC9zbWV2OlJlY2lwaWVudD48c21ldjpPcmlnaW5hdG9yPjxzbWV2OkNvZGU+MTMyNDI8L3NtZXY6Q29kZT48c21ldjpOYW1lPk1JTlNWWUFaX1NZU18xPC9zbWV2Ok5hbWU+PC9zbWV2Ok9yaWdpbmF0b3I+PHNtZXY6VHlwZUNvZGU+R1NSVjwvc21ldjpUeXBlQ29kZT48c21ldjpTdGF0dXM+QUNDRVBUPC9zbWV2OlN0YXR1cz48c21ldjpEYXRlPjIwMTItMTAtMjVUMTA6NDg6NDNaPC9zbWV2OkRhdGU+PHNtZXY6RXhjaGFuZ2VUeXBlPjI8L3NtZXY6RXhjaGFuZ2VUeXBlPjxzbWV2OlNlcnZpY2VDb2RlPlNJRDAwMDM0NTE8L3NtZXY6U2VydmljZUNvZGU+PHNtZXY6Q2FzZU51bWJlcj44OTE2PC9zbWV2OkNhc2VOdW1iZXI+PHNtZXY6VGVzdE1zZy8+PC9zbWV2Ok1lc3NhZ2U+PHNtZXY6TWVzc2FnZURhdGEgeG1sbnM6c21ldj0iaHR0cDovL3NtZXYuZ29zdXNsdWdpLnJ1L3JldjExMTExMSI+PHNtZXY6QXBwRGF0YSB3c3U6SWQ9ImZucy1BcHBEYXRhIiB4bWxuczp3c3U9Imh0dHA6Ly9kb2NzLm9hc2lzLW9wZW4ub3JnL3dzcy8yMDA0LzAxL29hc2lzLTIwMDQwMS13c3Mtd3NzZWN1cml0eS11dGlsaXR5LTEuMC54c2QiPgo80JTQvtC60YPQvNC10L3RgiB4bWxucz0iaHR0cDovL3dzLnVuaXNvZnQvRk5TWkRML1JzMSIg0JLQtdGA0YHQpNC+0YDQvD0iNC4wMiI+PNCY0LTQl9Cw0L/RgNC+0YHQpD41NzgzNDwv0JjQtNCX0LDQv9GA0L7RgdCkPjwv0JTQvtC60YPQvNC10L3Rgj4KPC9zbWV2OkFwcERhdGE+PC9zbWV2Ok1lc3NhZ2VEYXRhPgo8L3dzOlNlbmRSZXF1ZXN0UnM+PC9TOkJvZHk+PC9TOkVudmVsb3BlPg==";

		SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(new String(Base64.decode(sbase64.getBytes())));
		
		boolean bres = crw.verifySOAP(ms, "smev.ov.verify", false);
		Assert.assertTrue(bres);
		
	}
	
//	@Test
	public void testSoap1() throws Exception 
	{
		String sxmlUnsigned = FileUtils.readFileToString(new File("/home/irina/v/workspace/smevrequest-all/smevrequest/doc/минрегион/SID0001012/msgAsyncPingR.xml"), "UTF-8");
//		String sxmlUnsigned = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ><SOAP-ENV:Header><request xmlns=\"http://www.admnkz.ru/smev\" ID=\"9756\" SOAP-ENV:mustUnderstand=\"0\" signID=\"smev.ov.4216005979\"/></SOAP-ENV:Header><SOAP-ENV:Body><ns4:SyncRequest xmlns:ns4=\"http://smev.gosuslugi.ru/MsgExample/xsd/types\" xmlns:ns2=\"http://www.w3.org/2004/08/xop/include\" xmlns:ns3=\"http://smev.gosuslugi.ru/rev110801\"><ns3:Message><ns3:Sender><ns3:Code>NVKZ01421</ns3:Code><ns3:Name>Администрация города Новокузнецка</ns3:Name></ns3:Sender><ns3:Recipient><ns3:Code>1</ns3:Code><ns3:Name>Foiv1</ns3:Name></ns3:Recipient><ns3:Originator><ns3:Code>NVKZ01421</ns3:Code><ns3:Name>Администрация города Новокузнецка</ns3:Name></ns3:Originator><ns3:TypeCode>GSRV</ns3:TypeCode><ns3:Date>2012-11-07T17:31:41.480Z</ns3:Date><ns3:ServiceCode>SID0003001</ns3:ServiceCode><ns3:CaseNumber>9756</ns3:CaseNumber><TestMsg xmlns=\"http://smev.gosuslugi.ru/rev110801\"/></ns3:Message><ns3:MessageData><ns3:AppData/></ns3:MessageData></ns4:SyncRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		
		SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(sxmlUnsigned);
		
		crw.signSOAP(ms, "smev.ov.4217121181");		
		String sxmlSigned = CryptoUtils.SOAPMessageToString(ms);
		System.out.println();
		System.out.println(sxmlSigned);
		System.out.println();
		
		boolean bres = crw.verifySOAP(ms, "smev.ov.4217121181", false);
		Assert.assertTrue(bres);
		
		File fout = new File("/home/irina/v/workspace/smevrequest-all/smevrequest/doc/минрегион/SID0001012/msgAsyncPingRS.xml");
		if (fout.exists()) {
		    fout.delete();
		}
		fout.createNewFile();
		FileUtils.writeStringToFile(fout, sxmlSigned);
	}	
	
//	@Test 
	public void testXML() throws Exception
	{
		String sxmlUnsigned = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			      "      <ex:sayHello xmlns:ex=\"http://jeffhanson.com/services/helloworld\">" +
			      "         <ex:value>Hello world!</ex:value>" +
			      "      </ex:sayHello>";
	
		DocumentBuilderFactory dbuf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbu = dbuf.newDocumentBuilder();
		Document xdoc = dbu.parse(new ByteArrayInputStream(sxmlUnsigned.getBytes()));
		Element ev = (Element) CryptoUtils.findNodeByNameNS(xdoc, null, "value");
		Element eds = crw.signXML(ev, "smev.ov.4216005979", "");
		
		Assert.assertNotNull(eds);
		
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(xdoc),
		      new StreamResult(buffer));
		String str = buffer.toString();
		
		System.out.println(str);
		
		boolean bValid = crw.verifyXML(ev, "smev.ov.4216005979", true);
		Assert.assertTrue(bValid);
	}
	
//	@Test
    public void testXMLSoap() throws Exception 
    {
        
        String sxmlUnsigned = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                  "<SOAP-ENV:Envelope" + " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                  "<SOAP-ENV:Body>" +
                  "<def:sayHello xmlns:def=\"http://jeffhanson.com/services/helloworld\">" +
                  "<def:value>Hello world!</def:value>" +
                  "</def:sayHello>" +
                  "</SOAP-ENV:Body>" +
                  "</SOAP-ENV:Envelope>";
        
        SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(sxmlUnsigned);
        
        Document xdoc = ms.getSOAPPart().getEnvelope().getOwnerDocument();
        Element ev = (Element) CryptoUtils.findNodeByNameNS(xdoc, null, "sayHello");
        Element eds = crw.signXML(ev, "smev.ov.4216005979", "qq");        
        Assert.assertNotNull(eds);
        boolean bValid1 = crw.verifyXML(ev, "smev.ov.4216005979", true);
        Assert.assertTrue(bValid1);  
        String sxml1 = CryptoUtils.SOAPMessageToString(ms);
        System.out.println();
        System.out.println(sxml1);
        System.out.println();        
        
        crw.signSOAP(ms, "smev.ov.4216005979");     
        String sxmlSigned = CryptoUtils.SOAPMessageToString(ms);
        System.out.println();
        System.out.println(sxmlSigned);
        System.out.println();
        
        boolean bres = crw.verifySOAP(ms, "smev.ov.4216005979", false);
        Assert.assertTrue(bres);
        
        xdoc = ms.getSOAPPart().getEnvelope().getOwnerDocument();
        ev = (Element) CryptoUtils.findNodeByNameNS(xdoc, null, "sayHello");
        boolean bValid2 = crw.verifyXML(ev, "smev.ov.4216005979", true);
        Assert.assertTrue(bValid2);         
    }	
    
  //  @Test
    public void testXMLSoapAsDoc() throws Exception 
    {
        
        String sxmlUnsigned = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                  "<SOAP-ENV:Envelope" + " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                  "<SOAP-ENV:Body>" +
                  "<def:sayHello xmlns:def=\"http://jeffhanson.com/services/helloworld\">" +
                  "<def:value>Hello world!</def:value>" +
                  "</def:sayHello>" +
                  "</SOAP-ENV:Body>" +
                  "</SOAP-ENV:Envelope>";
        
        SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(sxmlUnsigned);
        ms = crw.signXMLAsDoc(ms, "def", "sayHello", "smev.ov.4216005979");
        
        Document xdoc = ms.getSOAPPart().getEnvelope().getOwnerDocument();
        Element ev = (Element) CryptoUtils.findNodeByNameNS(xdoc, null, "sayHello");
        boolean bValid1 = crw.verifyXML(ev, "smev.ov.4216005979", true);
//        Assert.assertTrue(bValid1);  
        String sxml1 = CryptoUtils.SOAPMessageToString(ms);
        System.out.println();
        System.out.println(sxml1);
        System.out.println();        
        
        crw.signSOAP(ms, "smev.ov.4216005979");     
        String sxmlSigned = CryptoUtils.SOAPMessageToString(ms);
        System.out.println();
        System.out.println(sxmlSigned);
        System.out.println();
        
        boolean bres = crw.verifySOAP(ms, "smev.ov.4216005979", false);
        Assert.assertTrue(bres);
        
        xdoc = ms.getSOAPPart().getEnvelope().getOwnerDocument();
        ev = (Element) CryptoUtils.findNodeByNameNS(xdoc, null, "sayHello");
        boolean bValid2 = crw.verifyXML(ev, "smev.ov.4216005979", true);
        Assert.assertTrue(bValid2);         
    }    
	
//	@Test
	public void testListProviders() {
		String[] sprovs = crw.listDSigProviders();
		Set<String> aset = crw.listDSigAlgorithms();
		
		Assert.assertTrue(sprovs.length > 0);
	}
	
//	@Test
	public void testListCerts() {
		List<Certificate> lst = crw.listCertificates(0, Integer.MAX_VALUE, null, null, null, "SC", null, null, null, null,null,null);
		
		Assert.assertNotNull(lst);
	}	
	
//	@Test
	public void testVerifyFNS() throws Exception
	{
		String sxmlUnsigned = FileUtils.readFileToString(new File("/home/irina/v/workspace/cryptoservice/data/msg2.xml"));
		SOAPMessage ms = CryptoUtils.getSOAPMessageFromString(sxmlUnsigned);
		boolean bres = crw.verifySOAP(ms, "smev.ov.verify", false);
		Assert.assertTrue(bres);		
	}
	
/*	
	@Test
	public void testAddCertJCP() throws Exception {
		byte[] cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/test123/test123.cer"));
//		byte[] cpriv = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/alice2/key.p8"));
		byte[] cpriv = null;
//		byte[] csr = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/alice2/sr.zip"));
		byte[] csr = null;
		Certificate acert = crw.addCertificate(null, "JCP", cbody, "/home/irina/v/workspace/cryptoservice/data/test123/test123.000");
		// TODO
	}
*/	

//	@Test
	public void testAddCert() throws Exception {
		//byte[] cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/kuznetsov/1.cer"));
		//byte[] cpriv = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/kuznetsov/private/keys/00000001.key"));
		//byte[] csr = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/kuznetsov/private/sr.zip"));
		byte[] cbody = FileUtils.readFileToByteArray(new File("d:/2/cert.cer"));
		byte[] cpriv = FileUtils.readFileToByteArray(new File("d:/2/00000006"));
		byte[] csr = FileUtils.readFileToByteArray(new File("d:/2/1.zip"));
		Certificate acert = crw.addCertificate(null, "SC", cbody, cpriv, csr, null);
	}
	
	@Test
	public void testAddRsACert() throws Exception {
		//byte[] cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/testrsa/izenpe.cer"));
		byte[] cbody = FileUtils.readFileToByteArray(new File("/tmp/innerca.cer"));
		Certificate acert = crw.addCertificate(null, null, cbody, null, null);
	}	
	
//	@Test
	public void testScan() {
		crw.updateSelfCheck(new Date());
	}

//	@Test
	public void testAddCACert() throws Exception {
		byte[] cbody = FileUtils.readFileToByteArray(new File("d:/2/1.crt"));
		byte[] cpriv = null;
		byte[] csr = null;
		Certificate acert = crw.addCertificate(null, "SC", cbody, cpriv, csr, null);
	}	
	
//	@Test
	public void testDigest() throws Exception {
		String sbody = "у попа была собака";
		byte[] dig = crw.digest(sbody.getBytes(), "sp.RStandard");
		
		Assert.assertNotNull(dig);
		Assert.assertTrue(dig.length > 0);
	}
	
//	@Test
	public void testCMS() throws Exception
	{
		String sbody = "у попа была собака";
		byte[] data = sbody.getBytes();
		byte[] sig = crw.signCMS(data, "sp.RStandard");

		Assert.assertNotNull(sig);
		Assert.assertTrue(sig.length > 0);
		
		boolean bres = crw.verifyCMS(sig, data, "sp.RStandard", false);
		Assert.assertTrue(bres);
	}
	
//	@Test
	public void testVerifyCMS() throws Exception
	{
		byte[] data = Base64.decode("uojm5AVxbQrtz3F6eaNcyU3OHp5gQSWfyB8LDWVTJTY=");
		String sbase64 = FileUtils.readFileToString(new File("/home/irina/v/workspace/cryptoservice/data/ts.p7s"));
		byte[] sig = Base64.decode(sbase64);
		Assert.assertNotNull(sig);
		Assert.assertTrue(sig.length > 0); 
		
		boolean bres = crw.verifyCMS(sig, data, "smev.ov.verify", true);
		Assert.assertTrue(bres);
	}	
	
//	@Test
	public void testMime() throws Exception 
	{
		Session sess = null;
		
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setHeader("Content-Type", "text/plain; charset=UTF-8");
		mbp.setHeader("Content-Transfer-Encoding", "8bit");
		mbp.setText("у попа была собака", "UTF-8");		
		
		MimeMultipart mm = crw.signMime(mbp, "smev.ov.4216005979");
		MimeMessage mansw = new MimeMessage(sess);
		mansw.setContent(mm, mm.getContentType());
		mansw.setSubject("Подтверждение");
		mansw.setRecipient(RecipientType.TO, new InternetAddress("bob@test.com"));
		mansw.setFrom(new InternetAddress("alice@test.com"));
		mansw.setSentDate(new Date());
		mansw.saveChanges();
		
		boolean bres = crw.verifyMime(mm, "smev.ov.verify", false);
		Assert.assertTrue(bres);
	}
	
//	@Test
	public void testConvert() throws Exception 
	{
		Settings setsFrom = crw.getSettings("s2");
		byte[] res = crw.convertPrivateKey(setsFrom, null);
	}
	
//	@Test
	public void testVerifyCert() throws Exception
	{
		boolean bres = crw.verifyCertificate("1bbbb12ca141550e23cf89d78ddbf7f2f4972");
		Assert.assertTrue(bres);
	}
	
//	@Test
	public void testParseCert() throws Exception
	{
		// ГОСТ
		byte[] cbody = FileUtils.readFileToByteArray(new File("/home/irina/Рабочие/02-проекты/ЭЦП для СМЭВ/2/GNIVCFNSSFO.cer"));
		ParseCertResult res = crw.parseCertificate(cbody);
		
		// RSA
		cbody = FileUtils.readFileToByteArray(new File("/home/irina/v/workspace/cryptoservice/data/testrsa/izenpe.cer"));
		res = crw.parseCertificate(cbody);

	}
	
//	@Test
	public void testExportChain() throws Exception
	{
		byte[] cbody = crw.exportChain("1bbbb12ca141550e23cf89d78ddbf7f2f4972");
		FileUtils.writeByteArrayToFile(new File("/home/irina/v/workspace/cryptoservice/data/test.p7b"), cbody);
	}
	
//	@Test
	public void testExportChainCMS() throws Exception
	{
		String sm7 = FileUtils.readFileToString(new File("/home/irina/v/workspace/cryptoservice/data/sm.p7s"));
		ParseCMSResult res = crw.parseCMS(Base64.decode(sm7));
		byte[] cbody = crw.exportChain(res.certs);
		FileUtils.writeByteArrayToFile(new File("/home/irina/v/workspace/cryptoservice/data/test.p7b"), cbody);
	}
	
}




