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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LegalItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LegalItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fid" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="serialNum" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="acctNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filingNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caseNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="srcCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="srcCountryCodeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberTypeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberNameText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalTypeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="courtTypeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberContactText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileSinceDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="narrativeCode1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="narrativeCode1Text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="narrativeCode2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="narrativeCode2Text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="court" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="courtType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportedDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="satisfiedDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="consideredDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="originalAmt" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="curBalance" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyCodeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="plaintiff" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lawyer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="thirdParty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="freezeFlag" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="suppressFlag" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="suppressStrDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="suppressEndDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="lastUpdatedDt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="resolution" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disputedStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disputedDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="disputedStatusText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disputedRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LegalItem", propOrder = {
    "fid",
    "serialNum",
    "acctNum",
    "filingNum",
    "caseNumber",
    "srcCountryCode",
    "srcCountryCodeText",
    "memberCode",
    "memberTypeText",
    "memberNameText",
    "legalType",
    "legalTypeText",
    "courtTypeText",
    "status",
    "statusText",
    "memberContactText",
    "fileSinceDt",
    "type",
    "narrativeCode1",
    "narrativeCode1Text",
    "narrativeCode2",
    "narrativeCode2Text",
    "court",
    "courtType",
    "reportedDt",
    "satisfiedDt",
    "consideredDt",
    "originalAmt",
    "curBalance",
    "currencyCode",
    "currencyCodeText",
    "plaintiff",
    "lawyer",
    "thirdParty",
    "freezeFlag",
    "suppressFlag",
    "suppressStrDt",
    "suppressEndDt",
    "lastUpdatedDt",
    "resolution",
    "disputedStatus",
    "disputedDate",
    "disputedStatusText",
    "disputedRemarks"
})
public class LegalItem {

    protected BigInteger fid;
    protected Long serialNum;
    protected String acctNum;
    protected String filingNum;
    protected String caseNumber;
    protected String srcCountryCode;
    protected String srcCountryCodeText;
    protected String memberCode;
    protected String memberTypeText;
    protected String memberNameText;
    protected String legalType;
    protected String legalTypeText;
    protected String courtTypeText;
    protected String status;
    protected String statusText;
    protected String memberContactText;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fileSinceDt;
    protected String type;
    protected String narrativeCode1;
    protected String narrativeCode1Text;
    protected String narrativeCode2;
    protected String narrativeCode2Text;
    protected String court;
    protected String courtType;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar reportedDt;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar satisfiedDt;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar consideredDt;
    protected BigInteger originalAmt;
    protected BigInteger curBalance;
    protected String currencyCode;
    protected String currencyCodeText;
    protected String plaintiff;
    protected String lawyer;
    protected String thirdParty;
    protected BigInteger freezeFlag;
    protected BigInteger suppressFlag;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar suppressStrDt;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar suppressEndDt;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lastUpdatedDt;
    protected String resolution;
    protected String disputedStatus;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar disputedDate;
    protected String disputedStatusText;
    protected String disputedRemarks;

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
     * Gets the value of the acctNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Sets the value of the acctNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcctNum(String value) {
        this.acctNum = value;
    }

    /**
     * Gets the value of the filingNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilingNum() {
        return filingNum;
    }

    /**
     * Sets the value of the filingNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilingNum(String value) {
        this.filingNum = value;
    }

    /**
     * Gets the value of the caseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseNumber() {
        return caseNumber;
    }

    /**
     * Sets the value of the caseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseNumber(String value) {
        this.caseNumber = value;
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
     * Gets the value of the legalType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalType() {
        return legalType;
    }

    /**
     * Sets the value of the legalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalType(String value) {
        this.legalType = value;
    }

    /**
     * Gets the value of the legalTypeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalTypeText() {
        return legalTypeText;
    }

    /**
     * Sets the value of the legalTypeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalTypeText(String value) {
        this.legalTypeText = value;
    }

    /**
     * Gets the value of the courtTypeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourtTypeText() {
        return courtTypeText;
    }

    /**
     * Sets the value of the courtTypeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourtTypeText(String value) {
        this.courtTypeText = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Sets the value of the statusText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusText(String value) {
        this.statusText = value;
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
     * Gets the value of the fileSinceDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFileSinceDt() {
        return fileSinceDt;
    }

    /**
     * Sets the value of the fileSinceDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFileSinceDt(XMLGregorianCalendar value) {
        this.fileSinceDt = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the narrativeCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarrativeCode1() {
        return narrativeCode1;
    }

    /**
     * Sets the value of the narrativeCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarrativeCode1(String value) {
        this.narrativeCode1 = value;
    }

    /**
     * Gets the value of the narrativeCode1Text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarrativeCode1Text() {
        return narrativeCode1Text;
    }

    /**
     * Sets the value of the narrativeCode1Text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarrativeCode1Text(String value) {
        this.narrativeCode1Text = value;
    }

    /**
     * Gets the value of the narrativeCode2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarrativeCode2() {
        return narrativeCode2;
    }

    /**
     * Sets the value of the narrativeCode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarrativeCode2(String value) {
        this.narrativeCode2 = value;
    }

    /**
     * Gets the value of the narrativeCode2Text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarrativeCode2Text() {
        return narrativeCode2Text;
    }

    /**
     * Sets the value of the narrativeCode2Text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarrativeCode2Text(String value) {
        this.narrativeCode2Text = value;
    }

    /**
     * Gets the value of the court property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourt() {
        return court;
    }

    /**
     * Sets the value of the court property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourt(String value) {
        this.court = value;
    }

    /**
     * Gets the value of the courtType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourtType() {
        return courtType;
    }

    /**
     * Sets the value of the courtType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourtType(String value) {
        this.courtType = value;
    }

    /**
     * Gets the value of the reportedDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReportedDt() {
        return reportedDt;
    }

    /**
     * Sets the value of the reportedDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReportedDt(XMLGregorianCalendar value) {
        this.reportedDt = value;
    }

    /**
     * Gets the value of the satisfiedDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSatisfiedDt() {
        return satisfiedDt;
    }

    /**
     * Sets the value of the satisfiedDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSatisfiedDt(XMLGregorianCalendar value) {
        this.satisfiedDt = value;
    }

    /**
     * Gets the value of the consideredDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsideredDt() {
        return consideredDt;
    }

    /**
     * Sets the value of the consideredDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsideredDt(XMLGregorianCalendar value) {
        this.consideredDt = value;
    }

    /**
     * Gets the value of the originalAmt property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOriginalAmt() {
        return originalAmt;
    }

    /**
     * Sets the value of the originalAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOriginalAmt(BigInteger value) {
        this.originalAmt = value;
    }

    /**
     * Gets the value of the curBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCurBalance() {
        return curBalance;
    }

    /**
     * Sets the value of the curBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCurBalance(BigInteger value) {
        this.curBalance = value;
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
     * Gets the value of the currencyCodeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCodeText() {
        return currencyCodeText;
    }

    /**
     * Sets the value of the currencyCodeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCodeText(String value) {
        this.currencyCodeText = value;
    }

    /**
     * Gets the value of the plaintiff property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaintiff() {
        return plaintiff;
    }

    /**
     * Sets the value of the plaintiff property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaintiff(String value) {
        this.plaintiff = value;
    }

    /**
     * Gets the value of the lawyer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLawyer() {
        return lawyer;
    }

    /**
     * Sets the value of the lawyer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLawyer(String value) {
        this.lawyer = value;
    }

    /**
     * Gets the value of the thirdParty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThirdParty() {
        return thirdParty;
    }

    /**
     * Sets the value of the thirdParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThirdParty(String value) {
        this.thirdParty = value;
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
     * Gets the value of the resolution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Sets the value of the resolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResolution(String value) {
        this.resolution = value;
    }

    /**
     * Gets the value of the disputedStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisputedStatus() {
        return disputedStatus;
    }

    /**
     * Sets the value of the disputedStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisputedStatus(String value) {
        this.disputedStatus = value;
    }

    /**
     * Gets the value of the disputedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDisputedDate() {
        return disputedDate;
    }

    /**
     * Sets the value of the disputedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDisputedDate(XMLGregorianCalendar value) {
        this.disputedDate = value;
    }

    /**
     * Gets the value of the disputedStatusText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisputedStatusText() {
        return disputedStatusText;
    }

    /**
     * Sets the value of the disputedStatusText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisputedStatusText(String value) {
        this.disputedStatusText = value;
    }

    /**
     * Gets the value of the disputedRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisputedRemarks() {
        return disputedRemarks;
    }

    /**
     * Sets the value of the disputedRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisputedRemarks(String value) {
        this.disputedRemarks = value;
    }

}