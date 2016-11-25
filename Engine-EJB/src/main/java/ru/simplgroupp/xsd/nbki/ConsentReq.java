package ru.simplgroupp.xsd.nbki;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsentReq", propOrder = {
    "consentFlag",
    "consentDate",
    "consentExpireDate",
    "consentPurpose",
    "otherConsentPurpose",
    "reportUser",
    "liability",
})
public class ConsentReq {

	protected String consentFlag;
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar consentDate;
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar consentExpireDate;
	protected String consentPurpose;  
	protected String otherConsentPurpose;
	protected String reportUser;
	protected String liability;
	 
	public String getConsentFlag() {
		return consentFlag;
	}
	public void setConsentFlag(String consentFlag) {
		this.consentFlag = consentFlag;
	}
	public XMLGregorianCalendar getConsentDate() {
		return consentDate;
	}
	public void setConsentDate(XMLGregorianCalendar consentDate) {
		this.consentDate = consentDate;
	}
	public XMLGregorianCalendar getConsentExpireDate() {
		return consentExpireDate;
	}
	public void setConsentExpireDate(XMLGregorianCalendar consentExpireDate) {
		this.consentExpireDate = consentExpireDate;
	}
	public String getConsentPurpose() {
		return consentPurpose;
	}
	public void setConsentPurpose(String consentPurpose) {
		this.consentPurpose = consentPurpose;
	}
	public String getOtherConsentPurpose() {
		return otherConsentPurpose;
	}
	public void setOtherConsentPurpose(String otherConsentPurpose) {
		this.otherConsentPurpose = otherConsentPurpose;
	}
	public String getReportUser() {
		return reportUser;
	}
	public void setReportUser(String reportUser) {
		this.reportUser = reportUser;
	}
	public String getLiability() {
		return liability;
	}
	public void setLiability(String liability) {
		this.liability = liability;
	} 
	 
	 
}

