
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ReqBusinesInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReqBusinesInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="App_Num">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="App_Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Login">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Password">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Account_Class">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Reason">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="01"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *               &lt;enumeration value="04"/>
 *               &lt;enumeration value="05"/>
 *               &lt;enumeration value="06"/>
 *               &lt;enumeration value="98"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Finance_Type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *               &lt;enumeration value="04"/>
 *               &lt;enumeration value="05"/>
 *               &lt;enumeration value="23"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Currency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *               &lt;enumeration value="RUB"/>
 *               &lt;enumeration value="USD"/>
 *               &lt;enumeration value="EUR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Credit_Limit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Payment_Frequency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="01"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *               &lt;enumeration value="04"/>
 *               &lt;enumeration value="05"/>
 *               &lt;enumeration value="06"/>
 *               &lt;enumeration value="07"/>
 *               &lt;enumeration value="08"/>
 *               &lt;enumeration value="98"/>
 *               &lt;enumeration value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Application_Monthly_Installment" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Application_Expences" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Application_Requested_Term">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;maxExclusive value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RFPS_Product" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Classification" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Application_Requested_Card_Grade" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Application_Visual_Client_Check" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Application_Visual_Docs_Check" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bureau_Call_Need" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RFPS_Call_Need" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Source_of_App" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="New_or_Used_Car_Autoloan_flag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Collaterial_Value_Amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Applicant" type="{http://experian.com/bureau/hosted/nbsm}ApplicantData"/>
 *         &lt;element name="ProductParams" type="{http://experian.com/bureau/hosted/nbsm}ProductParamsData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReqBusinesInfo", propOrder = {
    "appNum",
    "appDate",
    "login",
    "password",
    "accountClass",
    "reason",
    "financeType",
    "currency",
    "creditLimit",
    "paymentFrequency",
    "applicationMonthlyInstallment",
    "applicationExpences",
    "applicationRequestedTerm",
    "rfpsProduct",
    "classification",
    "applicationRequestedCardGrade",
    "applicationVisualClientCheck",
    "applicationVisualDocsCheck",
    "bureauCallNeed",
    "rfpsCallNeed",
    "sourceOfApp",
    "newOrUsedCarAutoloanFlag",
    "collaterialValueAmount",
    "applicant",
    "productParams"
})
public class ReqBusinesInfo {

    @XmlElement(name = "App_Num", required = true)
    protected String appNum;
    @XmlElement(name = "App_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar appDate;
    @XmlElement(name = "Login", required = true)
    protected String login;
    @XmlElement(name = "Password", required = true)
    protected String password;
    @XmlElement(name = "Account_Class", required = true)
    protected String accountClass;
    @XmlElement(name = "Reason", required = true)
    protected String reason;
    @XmlElement(name = "Finance_Type", required = true)
    protected String financeType;
    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "Credit_Limit", required = true)
    protected BigDecimal creditLimit;
    @XmlElement(name = "Payment_Frequency", required = true)
    protected String paymentFrequency;
    @XmlElement(name = "Application_Monthly_Installment", required = true)
    protected BigDecimal applicationMonthlyInstallment;
    @XmlElement(name = "Application_Expences", required = true)
    protected BigDecimal applicationExpences;
    @XmlElement(name = "Application_Requested_Term")
    protected int applicationRequestedTerm;
    @XmlElement(name = "RFPS_Product")
    protected String rfpsProduct;
    @XmlElement(name = "Classification")
    protected String classification;
    @XmlElementRef(name = "Application_Requested_Card_Grade", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<Integer> applicationRequestedCardGrade;
    @XmlElement(name = "Application_Visual_Client_Check")
    protected String applicationVisualClientCheck;
    @XmlElement(name = "Application_Visual_Docs_Check")
    protected String applicationVisualDocsCheck;
    @XmlElement(name = "Bureau_Call_Need")
    protected String bureauCallNeed;
    @XmlElement(name = "RFPS_Call_Need")
    protected String rfpsCallNeed;
    @XmlElement(name = "Source_of_App")
    protected String sourceOfApp;
    @XmlElement(name = "New_or_Used_Car_Autoloan_flag")
    protected String newOrUsedCarAutoloanFlag;
    @XmlElementRef(name = "Collaterial_Value_Amount", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<BigDecimal> collaterialValueAmount;
    @XmlElement(name = "Applicant", required = true)
    protected ApplicantData applicant;
    @XmlElement(name = "ProductParams", required = true)
    protected ProductParamsData productParams;

    /**
     * Gets the value of the appNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppNum() {
        return appNum;
    }

    /**
     * Sets the value of the appNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppNum(String value) {
        this.appNum = value;
    }

    /**
     * Gets the value of the appDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAppDate() {
        return appDate;
    }

    /**
     * Sets the value of the appDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAppDate(XMLGregorianCalendar value) {
        this.appDate = value;
    }

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the accountClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountClass() {
        return accountClass;
    }

    /**
     * Sets the value of the accountClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountClass(String value) {
        this.accountClass = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the financeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinanceType() {
        return financeType;
    }

    /**
     * Sets the value of the financeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinanceType(String value) {
        this.financeType = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the creditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the value of the creditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditLimit(BigDecimal value) {
        this.creditLimit = value;
    }

    /**
     * Gets the value of the paymentFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    /**
     * Sets the value of the paymentFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentFrequency(String value) {
        this.paymentFrequency = value;
    }

    /**
     * Gets the value of the applicationMonthlyInstallment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getApplicationMonthlyInstallment() {
        return applicationMonthlyInstallment;
    }

    /**
     * Sets the value of the applicationMonthlyInstallment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setApplicationMonthlyInstallment(BigDecimal value) {
        this.applicationMonthlyInstallment = value;
    }

    /**
     * Gets the value of the applicationExpences property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getApplicationExpences() {
        return applicationExpences;
    }

    /**
     * Sets the value of the applicationExpences property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setApplicationExpences(BigDecimal value) {
        this.applicationExpences = value;
    }

    /**
     * Gets the value of the applicationRequestedTerm property.
     * 
     */
    public int getApplicationRequestedTerm() {
        return applicationRequestedTerm;
    }

    /**
     * Sets the value of the applicationRequestedTerm property.
     * 
     */
    public void setApplicationRequestedTerm(int value) {
        this.applicationRequestedTerm = value;
    }

    /**
     * Gets the value of the rfpsProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRFPSProduct() {
        return rfpsProduct;
    }

    /**
     * Sets the value of the rfpsProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRFPSProduct(String value) {
        this.rfpsProduct = value;
    }

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassification(String value) {
        this.classification = value;
    }

    /**
     * Gets the value of the applicationRequestedCardGrade property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getApplicationRequestedCardGrade() {
        return applicationRequestedCardGrade;
    }

    /**
     * Sets the value of the applicationRequestedCardGrade property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setApplicationRequestedCardGrade(JAXBElement<Integer> value) {
        this.applicationRequestedCardGrade = value;
    }

    /**
     * Gets the value of the applicationVisualClientCheck property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationVisualClientCheck() {
        return applicationVisualClientCheck;
    }

    /**
     * Sets the value of the applicationVisualClientCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationVisualClientCheck(String value) {
        this.applicationVisualClientCheck = value;
    }

    /**
     * Gets the value of the applicationVisualDocsCheck property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationVisualDocsCheck() {
        return applicationVisualDocsCheck;
    }

    /**
     * Sets the value of the applicationVisualDocsCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationVisualDocsCheck(String value) {
        this.applicationVisualDocsCheck = value;
    }

    /**
     * Gets the value of the bureauCallNeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBureauCallNeed() {
        return bureauCallNeed;
    }

    /**
     * Sets the value of the bureauCallNeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBureauCallNeed(String value) {
        this.bureauCallNeed = value;
    }

    /**
     * Gets the value of the rfpsCallNeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRFPSCallNeed() {
        return rfpsCallNeed;
    }

    /**
     * Sets the value of the rfpsCallNeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRFPSCallNeed(String value) {
        this.rfpsCallNeed = value;
    }

    /**
     * Gets the value of the sourceOfApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceOfApp() {
        return sourceOfApp;
    }

    /**
     * Sets the value of the sourceOfApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceOfApp(String value) {
        this.sourceOfApp = value;
    }

    /**
     * Gets the value of the newOrUsedCarAutoloanFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewOrUsedCarAutoloanFlag() {
        return newOrUsedCarAutoloanFlag;
    }

    /**
     * Sets the value of the newOrUsedCarAutoloanFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewOrUsedCarAutoloanFlag(String value) {
        this.newOrUsedCarAutoloanFlag = value;
    }

    /**
     * Gets the value of the collaterialValueAmount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getCollaterialValueAmount() {
        return collaterialValueAmount;
    }

    /**
     * Sets the value of the collaterialValueAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setCollaterialValueAmount(JAXBElement<BigDecimal> value) {
        this.collaterialValueAmount = value;
    }

    /**
     * Gets the value of the applicant property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicantData }
     *     
     */
    public ApplicantData getApplicant() {
        return applicant;
    }

    /**
     * Sets the value of the applicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicantData }
     *     
     */
    public void setApplicant(ApplicantData value) {
        this.applicant = value;
    }

    /**
     * Gets the value of the productParams property.
     * 
     * @return
     *     possible object is
     *     {@link ProductParamsData }
     *     
     */
    public ProductParamsData getProductParams() {
        return productParams;
    }

    /**
     * Sets the value of the productParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductParamsData }
     *     
     */
    public void setProductParams(ProductParamsData value) {
        this.productParams = value;
    }

}
