
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ApplicantData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicantData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="App_Type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="01"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Last_Name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="First_Name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Patronymic_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Sex">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Date_of_Birth" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Place_of_Birth">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Citizenship">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;enumeration value="RU"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Consent_Flag">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Date_Consent_Given" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Income_Primary" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Income_Additional" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Income_Primary_Freq">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="8"/>
 *               &lt;enumeration value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Income_Pramary_Flag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Application_Num_Of_Dependants" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Application_Education">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="6"/>
 *               &lt;enumeration value="8"/>
 *               &lt;enumeration value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Application_Total_Exp_work" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Application_Last_Exp_work" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Alias_Name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Previous_Last_Name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Previous_First_Name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Previous_Patronymic_name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Marital_Status" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;enumeration value="01"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *               &lt;enumeration value="04"/>
 *               &lt;enumeration value="05"/>
 *               &lt;enumeration value="06"/>
 *               &lt;enumeration value="07"/>
 *               &lt;enumeration value="98"/>
 *               &lt;enumeration value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Residential_Status" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Income_Total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Income_Family" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="INN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Spouse_Fname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Spouse_Lname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Spouse_Pname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Spouse_DOB" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Mobile_Tel_Number1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Mobile_Tel_Number2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Email" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Application_Is_military_doc" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Private_Entrepreneur_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Issue_date_Entrepreneur" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="Number_of_Employees" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="6"/>
 *               &lt;enumeration value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Car_in_Property_flag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Real_Estate_in_Property_flag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Doc" type="{http://experian.com/bureau/hosted/nbsm}ApplicantDoc" maxOccurs="3"/>
 *         &lt;element name="Job" type="{http://experian.com/bureau/hosted/nbsm}ApplicantJob" minOccurs="0"/>
 *         &lt;element name="Vehicle" type="{http://experian.com/bureau/hosted/nbsm}ApplicantVehicle" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CreditHistory" type="{http://experian.com/bureau/hosted/nbsm}ApplicantCH" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://experian.com/bureau/hosted/nbsm}ApplicantAddress" maxOccurs="3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicantData", propOrder = {
    "appType",
    "lastName",
    "firstName",
    "patronymicName",
    "sex",
    "dateOfBirth",
    "placeOfBirth",
    "citizenship",
    "consentFlag",
    "dateConsentGiven",
    "incomePrimary",
    "incomeAdditional",
    "incomePrimaryFreq",
    "incomePramaryFlag",
    "applicationNumOfDependants",
    "applicationEducation",
    "applicationTotalExpWork",
    "applicationLastExpWork",
    "aliasName",
    "previousLastName",
    "previousFirstName",
    "previousPatronymicName",
    "maritalStatus",
    "residentialStatus",
    "incomeTotal",
    "incomeFamily",
    "inn",
    "spouseFname",
    "spouseLname",
    "spousePname",
    "spouseDOB",
    "mobileTelNumber1",
    "mobileTelNumber2",
    "email",
    "applicationIsMilitaryDoc",
    "privateEntrepreneurID",
    "issueDateEntrepreneur",
    "numberOfEmployees",
    "carInPropertyFlag",
    "realEstateInPropertyFlag",
    "doc",
    "job",
    "vehicle",
    "creditHistory",
    "address"
})
public class ApplicantData {

    @XmlElement(name = "App_Type", required = true)
    protected String appType;
    @XmlElement(name = "Last_Name", required = true)
    protected String lastName;
    @XmlElement(name = "First_Name", required = true)
    protected String firstName;
    @XmlElement(name = "Patronymic_name", required = true)
    protected String patronymicName;
    @XmlElement(name = "Sex", required = true)
    protected String sex;
    @XmlElement(name = "Date_of_Birth", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(name = "Place_of_Birth", required = true)
    protected String placeOfBirth;
    @XmlElement(name = "Citizenship", required = true)
    protected String citizenship;
    @XmlElement(name = "Consent_Flag", required = true)
    protected String consentFlag;
    @XmlElement(name = "Date_Consent_Given", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateConsentGiven;
    @XmlElement(name = "Income_Primary", required = true)
    protected BigDecimal incomePrimary;
    @XmlElement(name = "Income_Additional", required = true)
    protected BigDecimal incomeAdditional;
    @XmlElement(name = "Income_Primary_Freq", required = true)
    protected String incomePrimaryFreq;
    @XmlElement(name = "Income_Pramary_Flag", required = true)
    protected String incomePramaryFlag;
    @XmlElement(name = "Application_Num_Of_Dependants")
    protected int applicationNumOfDependants;
    @XmlElement(name = "Application_Education", required = true)
    protected String applicationEducation;
    @XmlElement(name = "Application_Total_Exp_work")
    protected int applicationTotalExpWork;
    @XmlElement(name = "Application_Last_Exp_work")
    protected int applicationLastExpWork;
    @XmlElement(name = "Alias_Name")
    protected String aliasName;
    @XmlElement(name = "Previous_Last_Name")
    protected String previousLastName;
    @XmlElement(name = "Previous_First_Name")
    protected String previousFirstName;
    @XmlElement(name = "Previous_Patronymic_name")
    protected String previousPatronymicName;
    @XmlElement(name = "Marital_Status")
    protected String maritalStatus;
    @XmlElement(name = "Residential_Status")
    protected String residentialStatus;
    @XmlElementRef(name = "Income_Total", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<BigDecimal> incomeTotal;
    @XmlElementRef(name = "Income_Family", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<BigDecimal> incomeFamily;
    @XmlElement(name = "INN")
    protected String inn;
    @XmlElement(name = "Spouse_Fname")
    protected String spouseFname;
    @XmlElement(name = "Spouse_Lname")
    protected String spouseLname;
    @XmlElement(name = "Spouse_Pname")
    protected String spousePname;
    @XmlElement(name = "Spouse_DOB")
    protected String spouseDOB;
    @XmlElement(name = "Mobile_Tel_Number1")
    protected String mobileTelNumber1;
    @XmlElement(name = "Mobile_Tel_Number2")
    protected String mobileTelNumber2;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "Application_Is_military_doc")
    protected String applicationIsMilitaryDoc;
    @XmlElement(name = "Private_Entrepreneur_ID")
    protected String privateEntrepreneurID;
    @XmlElementRef(name = "Issue_date_Entrepreneur", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> issueDateEntrepreneur;
    @XmlElement(name = "Number_of_Employees")
    protected String numberOfEmployees;
    @XmlElement(name = "Car_in_Property_flag")
    protected String carInPropertyFlag;
    @XmlElement(name = "Real_Estate_in_Property_flag")
    protected String realEstateInPropertyFlag;
    @XmlElement(name = "Doc", required = true)
    protected List<ApplicantDoc> doc;
    @XmlElementRef(name = "Job", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<ApplicantJob> job;
    @XmlElement(name = "Vehicle", nillable = true)
    protected List<ApplicantVehicle> vehicle;
    @XmlElementRef(name = "CreditHistory", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<ApplicantCH> creditHistory;
    @XmlElement(name = "Address", required = true)
    protected List<ApplicantAddress> address;

    /**
     * Gets the value of the appType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppType() {
        return appType;
    }

    /**
     * Sets the value of the appType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppType(String value) {
        this.appType = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the patronymicName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatronymicName() {
        return patronymicName;
    }

    /**
     * Sets the value of the patronymicName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatronymicName(String value) {
        this.patronymicName = value;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSex(String value) {
        this.sex = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfBirth(String value) {
        this.placeOfBirth = value;
    }

    /**
     * Gets the value of the citizenship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenship() {
        return citizenship;
    }

    /**
     * Sets the value of the citizenship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenship(String value) {
        this.citizenship = value;
    }

    /**
     * Gets the value of the consentFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsentFlag() {
        return consentFlag;
    }

    /**
     * Sets the value of the consentFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsentFlag(String value) {
        this.consentFlag = value;
    }

    /**
     * Gets the value of the dateConsentGiven property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateConsentGiven() {
        return dateConsentGiven;
    }

    /**
     * Sets the value of the dateConsentGiven property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateConsentGiven(XMLGregorianCalendar value) {
        this.dateConsentGiven = value;
    }

    /**
     * Gets the value of the incomePrimary property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIncomePrimary() {
        return incomePrimary;
    }

    /**
     * Sets the value of the incomePrimary property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIncomePrimary(BigDecimal value) {
        this.incomePrimary = value;
    }

    /**
     * Gets the value of the incomeAdditional property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIncomeAdditional() {
        return incomeAdditional;
    }

    /**
     * Sets the value of the incomeAdditional property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIncomeAdditional(BigDecimal value) {
        this.incomeAdditional = value;
    }

    /**
     * Gets the value of the incomePrimaryFreq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomePrimaryFreq() {
        return incomePrimaryFreq;
    }

    /**
     * Sets the value of the incomePrimaryFreq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomePrimaryFreq(String value) {
        this.incomePrimaryFreq = value;
    }

    /**
     * Gets the value of the incomePramaryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomePramaryFlag() {
        return incomePramaryFlag;
    }

    /**
     * Sets the value of the incomePramaryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomePramaryFlag(String value) {
        this.incomePramaryFlag = value;
    }

    /**
     * Gets the value of the applicationNumOfDependants property.
     * 
     */
    public int getApplicationNumOfDependants() {
        return applicationNumOfDependants;
    }

    /**
     * Sets the value of the applicationNumOfDependants property.
     * 
     */
    public void setApplicationNumOfDependants(int value) {
        this.applicationNumOfDependants = value;
    }

    /**
     * Gets the value of the applicationEducation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationEducation() {
        return applicationEducation;
    }

    /**
     * Sets the value of the applicationEducation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationEducation(String value) {
        this.applicationEducation = value;
    }

    /**
     * Gets the value of the applicationTotalExpWork property.
     * 
     */
    public int getApplicationTotalExpWork() {
        return applicationTotalExpWork;
    }

    /**
     * Sets the value of the applicationTotalExpWork property.
     * 
     */
    public void setApplicationTotalExpWork(int value) {
        this.applicationTotalExpWork = value;
    }

    /**
     * Gets the value of the applicationLastExpWork property.
     * 
     */
    public int getApplicationLastExpWork() {
        return applicationLastExpWork;
    }

    /**
     * Sets the value of the applicationLastExpWork property.
     * 
     */
    public void setApplicationLastExpWork(int value) {
        this.applicationLastExpWork = value;
    }

    /**
     * Gets the value of the aliasName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Sets the value of the aliasName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliasName(String value) {
        this.aliasName = value;
    }

    /**
     * Gets the value of the previousLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousLastName() {
        return previousLastName;
    }

    /**
     * Sets the value of the previousLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousLastName(String value) {
        this.previousLastName = value;
    }

    /**
     * Gets the value of the previousFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousFirstName() {
        return previousFirstName;
    }

    /**
     * Sets the value of the previousFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousFirstName(String value) {
        this.previousFirstName = value;
    }

    /**
     * Gets the value of the previousPatronymicName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousPatronymicName() {
        return previousPatronymicName;
    }

    /**
     * Sets the value of the previousPatronymicName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousPatronymicName(String value) {
        this.previousPatronymicName = value;
    }

    /**
     * Gets the value of the maritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the residentialStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidentialStatus() {
        return residentialStatus;
    }

    /**
     * Sets the value of the residentialStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidentialStatus(String value) {
        this.residentialStatus = value;
    }

    /**
     * Gets the value of the incomeTotal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getIncomeTotal() {
        return incomeTotal;
    }

    /**
     * Sets the value of the incomeTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setIncomeTotal(JAXBElement<BigDecimal> value) {
        this.incomeTotal = value;
    }

    /**
     * Gets the value of the incomeFamily property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getIncomeFamily() {
        return incomeFamily;
    }

    /**
     * Sets the value of the incomeFamily property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setIncomeFamily(JAXBElement<BigDecimal> value) {
        this.incomeFamily = value;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINN() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINN(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the spouseFname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseFname() {
        return spouseFname;
    }

    /**
     * Sets the value of the spouseFname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseFname(String value) {
        this.spouseFname = value;
    }

    /**
     * Gets the value of the spouseLname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseLname() {
        return spouseLname;
    }

    /**
     * Sets the value of the spouseLname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseLname(String value) {
        this.spouseLname = value;
    }

    /**
     * Gets the value of the spousePname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpousePname() {
        return spousePname;
    }

    /**
     * Sets the value of the spousePname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpousePname(String value) {
        this.spousePname = value;
    }

    /**
     * Gets the value of the spouseDOB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseDOB() {
        return spouseDOB;
    }

    /**
     * Sets the value of the spouseDOB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseDOB(String value) {
        this.spouseDOB = value;
    }

    /**
     * Gets the value of the mobileTelNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileTelNumber1() {
        return mobileTelNumber1;
    }

    /**
     * Sets the value of the mobileTelNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileTelNumber1(String value) {
        this.mobileTelNumber1 = value;
    }

    /**
     * Gets the value of the mobileTelNumber2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileTelNumber2() {
        return mobileTelNumber2;
    }

    /**
     * Sets the value of the mobileTelNumber2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileTelNumber2(String value) {
        this.mobileTelNumber2 = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the applicationIsMilitaryDoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationIsMilitaryDoc() {
        return applicationIsMilitaryDoc;
    }

    /**
     * Sets the value of the applicationIsMilitaryDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationIsMilitaryDoc(String value) {
        this.applicationIsMilitaryDoc = value;
    }

    /**
     * Gets the value of the privateEntrepreneurID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrivateEntrepreneurID() {
        return privateEntrepreneurID;
    }

    /**
     * Sets the value of the privateEntrepreneurID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrivateEntrepreneurID(String value) {
        this.privateEntrepreneurID = value;
    }

    /**
     * Gets the value of the issueDateEntrepreneur property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getIssueDateEntrepreneur() {
        return issueDateEntrepreneur;
    }

    /**
     * Sets the value of the issueDateEntrepreneur property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setIssueDateEntrepreneur(JAXBElement<XMLGregorianCalendar> value) {
        this.issueDateEntrepreneur = value;
    }

    /**
     * Gets the value of the numberOfEmployees property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    /**
     * Sets the value of the numberOfEmployees property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfEmployees(String value) {
        this.numberOfEmployees = value;
    }

    /**
     * Gets the value of the carInPropertyFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarInPropertyFlag() {
        return carInPropertyFlag;
    }

    /**
     * Sets the value of the carInPropertyFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarInPropertyFlag(String value) {
        this.carInPropertyFlag = value;
    }

    /**
     * Gets the value of the realEstateInPropertyFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealEstateInPropertyFlag() {
        return realEstateInPropertyFlag;
    }

    /**
     * Sets the value of the realEstateInPropertyFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealEstateInPropertyFlag(String value) {
        this.realEstateInPropertyFlag = value;
    }

    /**
     * Gets the value of the doc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the doc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDoc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicantDoc }
     * 
     * 
     */
    public List<ApplicantDoc> getDoc() {
        if (doc == null) {
            doc = new ArrayList<ApplicantDoc>();
        }
        return this.doc;
    }

    /**
     * Gets the value of the job property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ApplicantJob }{@code >}
     *     
     */
    public JAXBElement<ApplicantJob> getJob() {
        return job;
    }

    /**
     * Sets the value of the job property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ApplicantJob }{@code >}
     *     
     */
    public void setJob(JAXBElement<ApplicantJob> value) {
        this.job = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehicle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehicle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicantVehicle }
     * 
     * 
     */
    public List<ApplicantVehicle> getVehicle() {
        if (vehicle == null) {
            vehicle = new ArrayList<ApplicantVehicle>();
        }
        return this.vehicle;
    }

    /**
     * Gets the value of the creditHistory property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ApplicantCH }{@code >}
     *     
     */
    public JAXBElement<ApplicantCH> getCreditHistory() {
        return creditHistory;
    }

    /**
     * Sets the value of the creditHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ApplicantCH }{@code >}
     *     
     */
    public void setCreditHistory(JAXBElement<ApplicantCH> value) {
        this.creditHistory = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the address property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicantAddress }
     * 
     * 
     */
    public List<ApplicantAddress> getAddress() {
        if (address == null) {
            address = new ArrayList<ApplicantAddress>();
        }
        return this.address;
    }

}
