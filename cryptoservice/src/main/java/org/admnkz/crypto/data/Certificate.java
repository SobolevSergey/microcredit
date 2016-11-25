package org.admnkz.crypto.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Certificate implements Serializable, Initializable, Identifiable 
{

	public enum Options 
	{
		INIT_SETTINGS,
		INIT_SIGNER
	}
	
	private static final long serialVersionUID = 3086391076535945374L;
	
	public static final int SUBJECT_END_USER = 1;
	public static final int SUBJECT_SUB_CA = 2;
	public static final int SUBJECT_ROOT_CA = 3;
	
	private String id;
	private Status status;
	private Certificate Signer;
	private Integer SubjectType;
	private String SubjectCN;
	private String SubjectDN;
	private Date DateStart;
	private Date DateFinish;
	private String Body;
	private String PrivateKey;
	private String PrivateKeyAlias;
	private String PrivateKeyPath;
	private String PrivateKeyPassword;
	private String ProviderName;
	private String SecureRandom;
	private Date ValidationLastTime;
	private Integer ValidationCode;
	private String ValidationMessage;
	
	private List<Settings> settings = new ArrayList<Settings>(0);
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id=id;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Certificate getSigner() {
		return Signer;
	}
	public void setSigner(Certificate signer) {
		Signer = signer;
	}
	public Integer getSubjectType() {
		return SubjectType;
	}
	public void setSubjectType(Integer subjectType) {
		SubjectType = subjectType;
	}
	public String getSubjectCN() {
		return SubjectCN;
	}
	public void setSubjectCN(String subjectCN) {
		SubjectCN = subjectCN;
	}
	public Date getDateStart() {
		return DateStart;
	}
	public void setDateStart(Date dateStart) {
		DateStart = dateStart;
	}
	public Date getDateFinish() {
		return DateFinish;
	}
	public void setDateFinish(Date dateFinish) {
		DateFinish = dateFinish;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}
	public String getPrivateKey() {
		return PrivateKey;
	}
	public void setPrivateKey(String privateKey) {
		PrivateKey = privateKey;
	}
	public String getPrivateKeyAlias() {
		return PrivateKeyAlias;
	}
	public void setPrivateKeyAlias(String privateKeyAlias) {
		PrivateKeyAlias = privateKeyAlias;
	}
	
	@Override
	public boolean equals(Object other) 
	{
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    Certificate cast = (Certificate) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (! this.id.equals(cast.getId()))
	    	return false;
	    
	    return true;
	}
	@Override
	public int hashCode() {
	    return id.hashCode();
	}
	
	public void clearPrivateKey() {
		PrivateKey = null;
		PrivateKeyPath = null;
	}
	
	public boolean hasPrivateKey() {
		return ( ((PrivateKey != null && PrivateKey.trim().length() > 0)) || ((PrivateKeyPath != null && PrivateKeyPath.trim().length() > 0)) );
	}
	public String getPrivateKeyPath() {
		return PrivateKeyPath;
	}
	public void setPrivateKeyPath(String privateKeyPath) {
		PrivateKeyPath = privateKeyPath;
	}
	public String getProviderName() {
		return ProviderName;
	}
	public void setProviderName(String providerName) {
		ProviderName = providerName;
	}
	public String getSubjectDN() {
		return SubjectDN;
	}
	public void setSubjectDN(String subjectDN) {
		SubjectDN = subjectDN;
	}
	public List<Settings> getSettings() {
		return settings;
	}
	public void setSettings(List<Settings> settings) {
		this.settings = settings;
	}
	@Override
	public void init(Set options) 
	{
		if (options.contains(Options.INIT_SIGNER) && Signer != null)
			Signer.getSubjectCN();
		if (options.contains(Options.INIT_SETTINGS) && settings != null)
			 Utils.initCollection(getSettings(),options);	
	}
	public String getSecureRandom() {
		return SecureRandom;
	}
	public void setSecureRandom(String secureRandom) {
		SecureRandom = secureRandom;
	}
	public Date getValidationLastTime() {
		return ValidationLastTime;
	}
	public void setValidationLastTime(Date validationLastTime) {
		ValidationLastTime = validationLastTime;
	}
	public Integer getValidationCode() {
		return ValidationCode;
	}
	public void setValidationCode(Integer validationCode) {
		ValidationCode = validationCode;
	}
	public String getValidationMessage() {
		return ValidationMessage;
	}
	public void setValidationMessage(String validationMessage) {
		ValidationMessage = validationMessage;
	}
	public String getPrivateKeyPassword() {
		return PrivateKeyPassword;
	}
	public void setPrivateKeyPassword(String privateKeyPassword) {
		PrivateKeyPassword = privateKeyPassword;
	}
	
}
