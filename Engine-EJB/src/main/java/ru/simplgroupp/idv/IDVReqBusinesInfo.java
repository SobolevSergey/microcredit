
package ru.simplgroupp.idv;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for IDVReqBusinesInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDVReqBusinesInfo">
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
 *         &lt;element name="Date_of_Birth" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Birth_Place">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Primary_ID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="\d\d\d\d\d\d\d\d\d\d"/>
 *               &lt;length value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Primary_ID_Issue_Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Primary_ID_Authority">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Primary_ID_AuthorityCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Credit_History" type="{http://experian.com/bureau/hosted/nbsm}IDVApplicantCH" maxOccurs="2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IDVReqBusinesInfo", propOrder = {
    "appNum",
    "appDate",
    "login",
    "password",
    "lastName",
    "firstName",
    "patronymicName",
    "dateOfBirth",
    "birthPlace",
    "primaryID",
    "primaryIDIssueDate",
    "primaryIDAuthority",
    "primaryIDAuthorityCode",
    "creditHistory"
})
public class IDVReqBusinesInfo {

    @XmlElement(name = "App_Num", required = true)
    protected String appNum;
    @XmlElement(name = "App_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar appDate;
    @XmlElement(name = "Login", required = true)
    protected String login;
    @XmlElement(name = "Password", required = true)
    protected String password;
    @XmlElement(name = "Last_Name", required = true)
    protected String lastName;
    @XmlElement(name = "First_Name", required = true)
    protected String firstName;
    @XmlElement(name = "Patronymic_name", required = true)
    protected String patronymicName;
    @XmlElement(name = "Date_of_Birth", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(name = "Birth_Place", required = true)
    protected String birthPlace;
    @XmlElement(name = "Primary_ID", required = true)
    protected String primaryID;
    @XmlElement(name = "Primary_ID_Issue_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar primaryIDIssueDate;
    @XmlElement(name = "Primary_ID_Authority", required = true)
    protected String primaryIDAuthority;
    @XmlElement(name = "Primary_ID_AuthorityCode")
    protected String primaryIDAuthorityCode;
    @XmlElement(name = "Credit_History")
    protected List<IDVApplicantCH> creditHistory;

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
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthPlace(String value) {
        this.birthPlace = value;
    }

    /**
     * Gets the value of the primaryID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryID() {
        return primaryID;
    }

    /**
     * Sets the value of the primaryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryID(String value) {
        this.primaryID = value;
    }

    /**
     * Gets the value of the primaryIDIssueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPrimaryIDIssueDate() {
        return primaryIDIssueDate;
    }

    /**
     * Sets the value of the primaryIDIssueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPrimaryIDIssueDate(XMLGregorianCalendar value) {
        this.primaryIDIssueDate = value;
    }

    /**
     * Gets the value of the primaryIDAuthority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryIDAuthority() {
        return primaryIDAuthority;
    }

    /**
     * Sets the value of the primaryIDAuthority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryIDAuthority(String value) {
        this.primaryIDAuthority = value;
    }

    /**
     * Gets the value of the primaryIDAuthorityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryIDAuthorityCode() {
        return primaryIDAuthorityCode;
    }

    /**
     * Sets the value of the primaryIDAuthorityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryIDAuthorityCode(String value) {
        this.primaryIDAuthorityCode = value;
    }

    /**
     * Gets the value of the creditHistory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditHistory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreditHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDVApplicantCH }
     * 
     * 
     */
    public List<IDVApplicantCH> getCreditHistory() {
        if (creditHistory == null) {
            creditHistory = new ArrayList<IDVApplicantCH>();
        }
        return this.creditHistory;
    }

}
