package org.admnkz.crypto.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.admnkz.crypto.CryptoException;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CryptoUtils 
{
	public static final String PROVIDER_CRYPTOPRO = "JCP";
	public static final String PROVIDER_SIGNALCOM = "SC";
	public static final String PROVIDER_SUN = "SUN";
	public static final String PROVIDER_RSA = "RSA";
	public static final String PROVIDER_BC = "BC";
	
	public static final String JCP_HDSTORE = "HDImageStore";
	public static final String KEYS_DIR="cryptoservice.keysDir";
	public static final String CRYPTCP_DIR="cryptoservice.cryptcpDir";
	
	public static final String SIGNATURE_ALG_GOST="GOST3411withGOST3410EL";
	public static final String SIGNATURE_ALG_SHA="SHA1withRSA";
	public static final String XML_SIGNATURE_ALG="http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411";
	
	public static final String DIGEST_ALG_GOST="GOST3411";
	public static final String DIGEST_ALG_SHA="SHA-1";
	public static final String XML_DIGEST_ALG="http://www.w3.org/2001/04/xmldsig-more#gostr3411";
	
	public static final String SECURE_RANDOM_ALG_GOST="GOST28147PRNG";
	public static final String SECURE_RANDOM_ALG_SHA="SHA1PRNG";
	
	public static final String XML_PROVIDER_NAME="org.admnkz.gostxml.dsig.internal.dom.XMLDSigRI";
	
	public static final String PRIVATE_KEY_ALG_GOST="ECGOST3410";
	public static final String PRIVATE_KEY_ALG_RSA="RSA";
	
	public static final String KEY_MANAGER_ALG_GOST_JCP="GostX509";
	public static final String KEY_MANAGER_ALG_GOST_SC="SCX509";
	public static final String KEY_MANAGER_ALG_GOST_SUN="SunX509";
	
	public static final String SSL_PROTOCOL_GOST="GostTLS";
	public static final String SSL_PROTOCOL_SUN="SSLv3";
	
	public static final String JSSE_PROVIDER_NAME_GOST="SCJSSE";
	public static final String JSSE_PROVIDER_NAME_SUN="SunJSSE";
			
	private static MessageFactory msgFactory = null;
	private static DocumentBuilder docBuilder = null;
	
	private static final HashMap<String,String> x500pOptions = new HashMap<String,String>(1);
	static {
		x500pOptions.put("RFC1779", "CN");
		try {
			msgFactory = MessageFactory.newInstance();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
		}
		
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        
        try {
            docBuilder = dbf.newDocumentBuilder();  
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
        }		
	}
	
    public static Document createDocument(String source) throws SAXException, IOException {       
        Document xdoc1 = docBuilder.parse(new ByteArrayInputStream(source.getBytes()));
        return xdoc1;        
    }
	
    public static SOAPMessage getSOAPMessageFromDocument(Document source) throws CryptoException {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tr.transform(new DOMSource(source), new StreamResult(bos));
            
            InputStream input = new ByteArrayInputStream(bos.toByteArray());            
            try {
                SOAPMessage msg = msgFactory.createMessage(null, input);
                return msg;
            } finally {
                input.close();
            }
        } catch (Exception e) {
            throw new CryptoException(e);
        }        
    }
	
	public static Document createDocument() throws SOAPException {
/*	    
	    SOAPMessage msg = msgFactory.createMessage();
	    Document xdoc = msg.getSOAPPart();
	    Document xdoc1 = xdoc.getImplementation().createDocument(null, null, null);
	    return xdoc1;
*/
	    
	    Document xdoc1 = docBuilder.newDocument();
	    return xdoc1;
	    
	}
	
    public static Document createDocument(Element contentNode) throws SOAPException {
        Document xdoc1 = createDocument();
        
        Element econt = xdoc1.createElementNS(contentNode.getNamespaceURI(), contentNode.getTagName());
        xdoc1.appendChild(econt);
        NodeList ndList = contentNode.getChildNodes();
        for (int i = 0; i < ndList.getLength(); i++) {
            Node nd = ndList.item(i);
            Node nda = xdoc1.adoptNode(nd);
            econt.appendChild(nda);
        }
        
//        Node nd = xdoc1.importNode(contentNode, true);
//        xdoc1.appendChild(xdoc1.adoptNode(nd));
        // TODO
        return xdoc1;
    }
    
    public static void cloneNodeTo(Node source, Node dest) {
        
    }
	
	public static boolean needSecureRandom(String providerName) {
		return ("SC".equalsIgnoreCase(providerName));			
	}
	
	public static javax.xml.soap.SOAPEnvelope getSOAPEnvelopeFromString(String message) throws CryptoException 
	{
        
    	InputStream input = new ByteArrayInputStream(message.getBytes());
    	try {
	    	try {
	    		SOAPMessage msg = msgFactory.createMessage(null, input);
	    		return msg.getSOAPPart().getEnvelope();
			} finally {
	    		input.close();
	    	}
    	} catch (Exception e) {
    		throw new CryptoException(e);
    	}
    }
	
	public static String SOAPMessageToString(SOAPMessage msg) throws SOAPException 
	{
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		try {
			msg.writeTo(byteArrayOS);
			return new String(byteArrayOS.toByteArray());
		} catch (IOException e) {
			throw new SOAPException(e);
		}

	}
	
	public static SOAPMessage getSOAPMessageFromString(String message) throws CryptoException 
	{
        
    	InputStream input = new ByteArrayInputStream(message.getBytes());
    	try {
	    	try {
	    		SOAPMessage msg = msgFactory.createMessage(null, input);
	    		return msg;
			} finally {
	    		input.close();
	    	}
    	} catch (Exception e) {
    		throw new CryptoException(e);
    	}
    }		
	
	public static int calcCertType (X509Certificate acert) 
	{
		int pathLen = acert.getBasicConstraints();
		if (pathLen == -1)
			return 1;
		else {
			if (acert.getIssuerX500Principal().equals(acert.getSubjectX500Principal()))
				return 3;
			else
				return 2;
		}
	}
	
	public static boolean isIssuerFor(X509Certificate acert, X509Certificate aissuer, String sigProvider) 
	{
		boolean bres = false;
		try {
			acert.verify( aissuer.getPublicKey(), sigProvider);
			bres = true;
		} catch (Exception e) {
			bres = false;
		}
		return bres;
	}
	
	public static String extractSha1(X509Certificate acert, MessageDigest mdSha1) throws CertificateEncodingException 
	{
		byte[] digest = mdSha1.digest(acert.getEncoded());
		return toHexString(digest);
	}
	
	public static String toHexString(byte[] src) {
		StringBuffer hexString = new StringBuffer();
		for (int i=0; i < src.length; i++) 
		{
		    hexString.append(Integer.toHexString(0xFF & src[i]));
		}
		return hexString.toString();
	}

	public static String extractCN(X509Certificate acert) 
	{
		return acert.getSubjectX500Principal().getName("RFC1779", x500pOptions);
	}
	
	public static String extractDN(X509Certificate acert) 
	{
//		return String.valueOf(acert.getSubjectX500Principal().hashCode() );
		return new String( Base64.encodeBase64(acert.getSubjectX500Principal().getName().getBytes()));
	}
	
	public static String extractIssuerDN(X509Certificate acert) 
	{
//		return String.valueOf(acert.getIssuerX500Principal().hashCode() );
		return new String( Base64.encodeBase64(acert.getIssuerX500Principal().getName().getBytes()));
	}	
	
	/**
	 * Проверяет, подходят ли дрyг к другу данные ключи.
	 * @param pubKey
	 * @param privKey
	 * @return
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static boolean isPair(PublicKey pubKey, PrivateKey privKey, SecureRandom random, String providerName, String sigAlg) 
		throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException 
	{
		byte [] data = "test".getBytes();
		
		// подписание
		Signature sig = Signature.getInstance(sigAlg, providerName);
		if (random == null)
			sig.initSign(privKey);
		else
			sig.initSign(privKey, random);
		sig.update(data);
		byte[] signature = sig.sign();
		
		// проверка
		sig.initVerify(pubKey);
		sig.update(data);
		boolean bres = sig.verify(signature);
		return bres;
	}
	
	public static String[] listProviderServicesFor(Provider prov, String serviceName) 
	{
		String[] res = new String[20];
		int i = 0;
		for (Enumeration e = prov.keys(); e.hasMoreElements();) {
            String skey = e.nextElement().toString();
            if (skey.startsWith(serviceName + ".")) 
            {
            	int nend = skey.indexOf("ImplementedIn");
            	int nstart = (serviceName + ".").length();
            	String svalue = null;
            	if (nend >= 0)
            		svalue = skey.substring(nstart, nend-1);
            	else
            		svalue = skey.substring(nstart);
    			res[i] = svalue;
    			i++;	
            }
		}
		return Arrays.copyOf(res, i);
	}	
	
	public static String[] listPrivKeyAlgs(String providerName) 
	{
		Provider prov = Security.getProvider(providerName);
		return listProviderServicesFor(prov, "KeyFactory");		
	}
	
	public static String[] listSigAlgs(String providerName) 
	{
		Provider prov = Security.getProvider(providerName);
		return listProviderServicesFor(prov, "Signature");		
	}
	
	public static String[] listSecRandAlgs(String providerName) 
	{
		Provider prov = Security.getProvider(providerName);
		return listProviderServicesFor(prov, "SecureRandom");		
	}	
	
	public static void unzipTo(byte[] bzip, File outdir) throws IOException 
	{
		final int BUFFER = 2048;
		BufferedOutputStream dest = null;
		
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(bzip));
		try {
			ZipEntry entry;
			while((entry = zin.getNextEntry()) != null) 
			{
	            int count;
	            byte data[] = new byte[BUFFER];
	            // write the files to the disk
	            String sname = outdir.getCanonicalPath() + "/" + entry.getName();
	            FileOutputStream fos = new FileOutputStream(sname);
	            try {
		            dest = new BufferedOutputStream(fos, BUFFER);
		            while ((count = zin.read(data, 0, BUFFER)) 
		              != -1) {
		               dest.write(data, 0, count);
		            }
	            } finally {
	            	dest.flush();
	            	dest.close();
	            }
			   // extract data
			   // open output streams
			}			
		} finally {
			zin.close();
		}
	}
	
	public static Node findNodeByNameNS(Node parent, String ns, String name) 
    {
        
        NodeList lst = parent.getChildNodes();
        for (int i = 0; i < lst.getLength(); i++) {
            Node nd = lst.item(i);
            if (! (nd instanceof Element))
                continue;
            
            Element elmND = (Element) nd;
            if (ns == null) {
                String ss2 = extractLocalName(elmND.getTagName());
                if (name.equals(ss2)) {
                    return elmND;
                }           
            } else {
                if ((ns + ":"+name).equals(elmND.getTagName())) {
                    return elmND;
                }
            }
            if (elmND.hasChildNodes()) {
                Node ndf = findNodeByNameNS(elmND, ns, name);
                if (ndf != null)
                    return ndf;
            }
        }
        return null;
        
    }
    
    public static String extractLocalName(String tagName) {
        if (tagName == null) {
            return null;
        }
        
        int n = tagName.indexOf(":");
        if (n >= 0) {
            return tagName.substring(n+1);
        } else {
            return tagName;
        }
    }
    
    public static List<Node> findNodeListByNameNS(Node parent, String ns, String name) 
    {
        ArrayList<Node> nres = new ArrayList<Node>(0);
        
/*      
        NodeList lst = parent.getElementsByTagNameNS(ns, name);
        if (lst != null && lst.getLength() > 0)
            return lst.item(0);
        else
            return null;
*/      
        
        NodeList lst = parent.getChildNodes();
        for (int i = 0; i < lst.getLength(); i++) {
            Node nd = lst.item(i);
            if (! (nd instanceof Element))
                continue;
            
            Element elmND = (Element) nd;
            if ((ns + ":"+name).equals(elmND.getTagName())) {
                nres.add(elmND);
            }
            if (elmND.hasChildNodes()) {
                Node ndf = findNodeByNameNS(elmND, ns, name);
                if (ndf != null)
                    nres.add(ndf);
            }
        }
        return nres;
        
    }	
}
