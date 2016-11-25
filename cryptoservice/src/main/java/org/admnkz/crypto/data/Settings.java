package org.admnkz.crypto.data;

import java.io.Serializable;

public class Settings implements Serializable 
{
	private static final long serialVersionUID = 4608026531348918805L;
	private String ID;
	private Certificate certificate;
	private String ProviderName;
	private String SignatureAlg;
	private String XmlSignatureAlg;
	private String DigestAlg;
	private String XmlDigestAlg;
	private String SecureRandomAlg;
	private String XmlDSigProviderName;
	private String PrivateKeyAlg;
	private String DigestProviderName;
	private String KeyManagerAlg;
	private String SslProtocol;
	private String JsseProviderName;
	
	@Override
	public boolean equals(Object other) 
	{
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    Settings cast = (Settings) other;
	    
	    if (this.ID == null) return false;
	       
	    if (cast.getID() == null) return false;       
	       
	    if (! this.ID.equals(cast.getID()))
	    	return false;
	    
	    return true;
	}
	@Override
	public int hashCode() {
	    return ID.hashCode();
	}	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public String getProviderName() {
		return ProviderName;
	}
	public void setProviderName(String providerName) {
		ProviderName = providerName;
	}
	public String getSignatureAlg() {
		return SignatureAlg;
	}
	public void setSignatureAlg(String signatureAlg) {
		SignatureAlg = signatureAlg;
	}
	public String getXmlSignatureAlg() {
		return XmlSignatureAlg;
	}
	public void setXmlSignatureAlg(String xmlSignatureAlg) {
		XmlSignatureAlg = xmlSignatureAlg;
	}
	public String getDigestAlg() {
		return DigestAlg;
	}
	public void setDigestAlg(String digestAlg) {
		DigestAlg = digestAlg;
	}
	public String getXmlDigestAlg() {
		return XmlDigestAlg;
	}
	public void setXmlDigestAlg(String xmlDigestAlg) {
		XmlDigestAlg = xmlDigestAlg;
	}
	public String getSecureRandomAlg() {
		return SecureRandomAlg;
	}
	public void setSecureRandomAlg(String secureRandomAlg) {
		SecureRandomAlg = secureRandomAlg;
	}
	public String getXmlDSigProviderName() {
		return XmlDSigProviderName;
	}
	public void setXmlDSigProviderName(String xmlDSigProviderName) {
		XmlDSigProviderName = xmlDSigProviderName;
	}
	public String getPrivateKeyAlg() {
		return PrivateKeyAlg;
	}
	public void setPrivateKeyAlg(String privateKeyAlg) {
		PrivateKeyAlg = privateKeyAlg;
	}
	public String getDigestProviderName() {
		return DigestProviderName;
	}
	public void setDigestProviderName(String digestProviderName) {
		DigestProviderName = digestProviderName;
	}
	public String getKeyManagerAlg() {
		return KeyManagerAlg;
	}
	public void setKeyManagerAlg(String keyManagerAlg) {
		KeyManagerAlg = keyManagerAlg;
	}
	public String getSslProtocol() {
		return SslProtocol;
	}
	public void setSslProtocol(String sslProtocol) {
		SslProtocol = sslProtocol;
	}
	public String getJsseProviderName() {
		return JsseProviderName;
	}
	public void setJsseProviderName(String jsseProviderName) {
		JsseProviderName = jsseProviderName;
	}
	
}
