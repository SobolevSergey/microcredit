//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.04 at 04:17:01 PM MSK 
//


package ru.simplgroupp.xsd.nbki;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Inquiry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Inquiry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fid" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="serialNum" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="srcCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="srcCountryCodeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberTypeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberNameText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberContactText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inqControlNum" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="inquiryDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="inquiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inquiryPeriod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inquiryTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inqPurpose" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inqPurposeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inqAmount" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberLastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberFirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberMiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberRegNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberTaxpayerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberOKPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberIDIssueAuth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberIDIssueLoc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberIDIssueDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="memberBusinessCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastUpdatedDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="freezeFlag" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="suppressFlag" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="suppressStrDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="suppressEndDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Inquiry", propOrder = {
	"consentReq",	
    "fid",
    "serialNum",
    "srcCountryCode",
    "srcCountryCodeText",
    "memberCode",
    "memberTypeText",
    "memberNameText",
    "memberContactText",
    "inqControlNum",
    "inquiryDt",
    "inquiryDate",
    "inquiryPeriod",
    "inquiryTime",
    "inqPurpose",
    "inqPurposeText",
    "inqAmount",
    "currencyCode",
    "memberFullName",
    "memberShortName",
    "memberLastName",
    "memberFirstName",
    "memberMiddleName",
    "memberRegNum",
    "memberType",
    "memberTaxpayerID",
    "memberOKPO",
    "memberIDIssueAuth",
    "memberIDIssueLoc",
    "memberIDIssueDate",
    "memberBusinessCode",
    "userReference",
    "lastUpdatedDt",
    "freezeFlag",
    "suppressFlag",
    "suppressStrDt",
    "suppressEndDt"
})
public class Inquiry {

	@XmlElement(name = "ConsentReq")
    protected ConsentReq consentReq;
    protected BigInteger fid;
    protected Long serialNum;
    protected String srcCountryCode;
    protected String srcCountryCodeText;
    protected String memberCode;
    protected String memberTypeText;
    protected String memberNameText;
    protected String memberContactText;
    protected BigInteger inqControlNum;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar inquiryDt;
    protected String inquiryDate;
    protected String inquiryPeriod;
    protected String inquiryTime;
    protected String inqPurpose;
    protected String inqPurposeText;
    protected BigInteger inqAmount;
    protected String currencyCode;
    protected String memberFullName;
    protected String memberShortName;
    protected String memberLastName;
    protected String memberFirstName;
    protected String memberMiddleName;
    protected String memberRegNum;
    protected String memberType;
    protected String memberTaxpayerID;
    protected String memberOKPO;
    protected String memberIDIssueAuth;
    protected String memberIDIssueLoc;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar memberIDIssueDate;
    protected String memberBusinessCode;
    protected String userReference;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lastUpdatedDt;
    protected BigInteger freezeFlag;
    protected BigInteger suppressFlag;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar suppressStrDt;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar suppressEndDt;

    /**
     * Gets the value of the fid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFid() {
        return fid;
    }

    /**
     * Sets the value of the fid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFid(BigInteger value) {
        this.fid = value;
    }

    /**
     * Gets the value of the serialNum property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSerialNum() {
        return serialNum;
    }

    /**
     * Sets the value of the serialNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSerialNum(Long value) {
        this.serialNum = value;
    }

    /**
     * Gets the value of the srcCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcCountryCode() {
        return srcCountryCode;
    }

    /**
     * Sets the value of the srcCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcCountryCode(String value) {
        this.srcCountryCode = value;
    }

    /**
     * Gets the value of the srcCountryCodeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcCountryCodeText() {
        return srcCountryCodeText;
    }

    /**
     * Sets the value of the srcCountryCodeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcCountryCodeText(String value) {
        this.srcCountryCodeText = value;
    }

    /**
     * Gets the value of the memberCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberCode() {
        return memberCode;
    }

    /**
     * Sets the value of the memberCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberCode(String value) {
        this.memberCode = value;
    }

    /**
     * Gets the value of the memberTypeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberTypeText() {
        return memberTypeText;
    }

    /**
     * Sets the value of the memberTypeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberTypeText(String value) {
        this.memberTypeText = value;
    }

    /**
     * Gets the value of the memberNameText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberNameText() {
        return memberNameText;
    }

    /**
     * Sets the value of the memberNameText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberNameText(String value) {
        this.memberNameText = value;
    }

    /**
     * Gets the value of the memberContactText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberContactText() {
        return memberContactText;
    }

    /**
     * Sets the value of the memberContactText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberContactText(String value) {
        this.memberContactText = value;
    }

    /**
     * Gets the value of the inqControlNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInqControlNum() {
        return inqControlNum;
    }

    /**
     * Sets the value of the inqControlNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInqControlNum(BigInteger value) {
        this.inqControlNum = value;
    }

    /**
     * Gets the value of the inquiryDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInquiryDt() {
        return inquiryDt;
    }

    /**
     * Sets the value of the inquiryDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInquiryDt(XMLGregorianCalendar value) {
        this.inquiryDt = value;
    }

    /**
     * Gets the value of the inquiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInquiryDate() {
        return inquiryDate;
    }

    /**
     * Sets the value of the inquiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInquiryDate(String value) {
        this.inquiryDate = value;
    }

    /**
     * Gets the value of the inquiryPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInquiryPeriod() {
        return inquiryPeriod;
    }

    /**
     * Sets the value of the inquiryPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInquiryPeriod(String value) {
        this.inquiryPeriod = value;
    }

    /**
     * Gets the value of the inquiryTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInquiryTime() {
        return inquiryTime;
    }

    /**
     * Sets the value of the inquiryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInquiryTime(String value) {
        this.inquiryTime = value;
    }

    /**
     * Gets the value of the inqPurpose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInqPurpose() {
        return inqPurpose;
    }

    /**
     * Sets the value of the inqPurpose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInqPurpose(String value) {
        this.inqPurpose = value;
    }

    /**
     * Gets the value of the inqPurposeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInqPurposeText() {
        return inqPurposeText;
    }

    /**
     * Sets the value of the inqPurposeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInqPurposeText(String value) {
        this.inqPurposeText = value;
    }

    /**
     * Gets the value of the inqAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInqAmount() {
        return inqAmount;
    }

    /**
     * Sets the value of the inqAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInqAmount(BigInteger value) {
        this.inqAmount = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the memberFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberFullName() {
        return memberFullName;
    }

    /**
     * Sets the value of the memberFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberFullName(String value) {
        this.memberFullName = value;
    }

    /**
     * Gets the value of the memberShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberShortName() {
        return memberShortName;
    }

    /**
     * Sets the value of the memberShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberShortName(String value) {
        this.memberShortName = value;
    }

    /**
     * Gets the value of the memberLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberLastName() {
        return memberLastName;
    }

    /**
     * Sets the value of the memberLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberLastName(String value) {
        this.memberLastName = value;
    }

    /**
     * Gets the value of the memberFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberFirstName() {
        return memberFirstName;
    }

    /**
     * Sets the value of the memberFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberFirstName(String value) {
        this.memberFirstName = value;
    }

    /**
     * Gets the value of the memberMiddleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberMiddleName() {
        return memberMiddleName;
    }

    /**
     * Sets the value of the memberMiddleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberMiddleName(String value) {
        this.memberMiddleName = value;
    }

    /**
     * Gets the value of the memberRegNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberRegNum() {
        return memberRegNum;
    }

    /**
     * Sets the value of the memberRegNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberRegNum(String value) {
        this.memberRegNum = value;
    }

    /**
     * Gets the value of the memberType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberType() {
        return memberType;
    }

    /**
     * Sets the value of the memberType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberType(String value) {
        this.memberType = value;
    }

    /**
     * Gets the value of the memberTaxpayerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberTaxpayerID() {
        return memberTaxpayerID;
    }

    /**
     * Sets the value of the memberTaxpayerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberTaxpayerID(String value) {
        this.memberTaxpayerID = value;
    }

    /**
     * Gets the value of the memberOKPO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberOKPO() {
        return memberOKPO;
    }

    /**
     * Sets the value of the memberOKPO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberOKPO(String value) {
        this.memberOKPO = value;
    }

    /**
     * Gets the value of the memberIDIssueAuth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberIDIssueAuth() {
        return memberIDIssueAuth;
    }

    /**
     * Sets the value of the memberIDIssueAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberIDIssueAuth(String value) {
        this.memberIDIssueAuth = value;
    }

    /**
     * Gets the value of the memberIDIssueLoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberIDIssueLoc() {
        return memberIDIssueLoc;
    }

    /**
     * Sets the value of the memberIDIssueLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberIDIssueLoc(String value) {
        this.memberIDIssueLoc = value;
    }

    /**
     * Gets the value of the memberIDIssueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMemberIDIssueDate() {
        return memberIDIssueDate;
    }

    /**
     * Sets the value of the memberIDIssueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMemberIDIssueDate(XMLGregorianCalendar value) {
        this.memberIDIssueDate = value;
    }

    /**
     * Gets the value of the memberBusinessCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberBusinessCode() {
        return memberBusinessCode;
    }

    /**
     * Sets the value of the memberBusinessCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberBusinessCode(String value) {
        this.memberBusinessCode = value;
    }

    /**
     * Gets the value of the userReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserReference() {
        return userReference;
    }

    /**
     * Sets the value of the userReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserReference(String value) {
        this.userReference = value;
    }

    /**
     * Gets the value of the lastUpdatedDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdatedDt() {
        return lastUpdatedDt;
    }

    /**
     * Sets the value of the lastUpdatedDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdatedDt(XMLGregorianCalendar value) {
        this.lastUpdatedDt = value;
    }

    /**
     * Gets the value of the freezeFlag property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFreezeFlag() {
        return freezeFlag;
    }

    /**
     * Sets the value of the freezeFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFreezeFlag(BigInteger value) {
        this.freezeFlag = value;
    }

    /**
     * Gets the value of the suppressFlag property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSuppressFlag() {
        return suppressFlag;
    }

    /**
     * Sets the value of the suppressFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSuppressFlag(BigInteger value) {
        this.suppressFlag = value;
    }

    /**
     * Gets the value of the suppressStrDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSuppressStrDt() {
        return suppressStrDt;
    }

    /**
     * Sets the value of the suppressStrDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSuppressStrDt(XMLGregorianCalendar value) {
        this.suppressStrDt = value;
    }

    /**
     * Gets the value of the suppressEndDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSuppressEndDt() {
        return suppressEndDt;
    }

    /**
     * Sets the value of the suppressEndDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSuppressEndDt(XMLGregorianCalendar value) {
        this.suppressEndDt = value;
    }

	public ConsentReq getConsentReq() {
		return consentReq;
	}

	public void setConsentReq(ConsentReq consentReq) {
		this.consentReq = consentReq;
	}

    
}
