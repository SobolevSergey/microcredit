
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicantJob complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicantJob">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Job_Name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_INN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Type" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="6"/>
 *               &lt;enumeration value="7"/>
 *               &lt;enumeration value="8"/>
 *               &lt;enumeration value="9"/>
 *               &lt;enumeration value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Occupation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;enumeration value="00"/>
 *               &lt;enumeration value="01"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="03"/>
 *               &lt;enumeration value="04"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Tel_Number1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Tel_Number2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Country" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Postal_Code" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Region" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_District" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_City" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Street" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Settlement" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_House_Number" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Building" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Construction" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Job_Apartment_Number" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicantJob", propOrder = {
    "jobName",
    "jobINN",
    "jobType",
    "jobOccupation",
    "jobTelNumber1",
    "jobTelNumber2",
    "jobCountry",
    "jobPostalCode",
    "jobRegion",
    "jobDistrict",
    "jobCity",
    "jobStreet",
    "jobSettlement",
    "jobHouseNumber",
    "jobBuilding",
    "jobConstruction",
    "jobApartmentNumber"
})
public class ApplicantJob {

    @XmlElement(name = "Job_Name")
    protected String jobName;
    @XmlElement(name = "Job_INN")
    protected String jobINN;
    @XmlElement(name = "Job_Type")
    protected String jobType;
    @XmlElement(name = "Job_Occupation")
    protected String jobOccupation;
    @XmlElement(name = "Job_Tel_Number1")
    protected String jobTelNumber1;
    @XmlElement(name = "Job_Tel_Number2")
    protected String jobTelNumber2;
    @XmlElement(name = "Job_Country")
    protected String jobCountry;
    @XmlElement(name = "Job_Postal_Code")
    protected String jobPostalCode;
    @XmlElement(name = "Job_Region")
    protected String jobRegion;
    @XmlElement(name = "Job_District")
    protected String jobDistrict;
    @XmlElement(name = "Job_City")
    protected String jobCity;
    @XmlElement(name = "Job_Street")
    protected String jobStreet;
    @XmlElement(name = "Job_Settlement")
    protected String jobSettlement;
    @XmlElement(name = "Job_House_Number")
    protected String jobHouseNumber;
    @XmlElement(name = "Job_Building")
    protected String jobBuilding;
    @XmlElement(name = "Job_Construction")
    protected String jobConstruction;
    @XmlElement(name = "Job_Apartment_Number")
    protected String jobApartmentNumber;

    /**
     * Gets the value of the jobName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the value of the jobName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobName(String value) {
        this.jobName = value;
    }

    /**
     * Gets the value of the jobINN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobINN() {
        return jobINN;
    }

    /**
     * Sets the value of the jobINN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobINN(String value) {
        this.jobINN = value;
    }

    /**
     * Gets the value of the jobType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * Sets the value of the jobType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobType(String value) {
        this.jobType = value;
    }

    /**
     * Gets the value of the jobOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobOccupation() {
        return jobOccupation;
    }

    /**
     * Sets the value of the jobOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobOccupation(String value) {
        this.jobOccupation = value;
    }

    /**
     * Gets the value of the jobTelNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTelNumber1() {
        return jobTelNumber1;
    }

    /**
     * Sets the value of the jobTelNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTelNumber1(String value) {
        this.jobTelNumber1 = value;
    }

    /**
     * Gets the value of the jobTelNumber2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTelNumber2() {
        return jobTelNumber2;
    }

    /**
     * Sets the value of the jobTelNumber2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTelNumber2(String value) {
        this.jobTelNumber2 = value;
    }

    /**
     * Gets the value of the jobCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobCountry() {
        return jobCountry;
    }

    /**
     * Sets the value of the jobCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobCountry(String value) {
        this.jobCountry = value;
    }

    /**
     * Gets the value of the jobPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobPostalCode() {
        return jobPostalCode;
    }

    /**
     * Sets the value of the jobPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobPostalCode(String value) {
        this.jobPostalCode = value;
    }

    /**
     * Gets the value of the jobRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobRegion() {
        return jobRegion;
    }

    /**
     * Sets the value of the jobRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobRegion(String value) {
        this.jobRegion = value;
    }

    /**
     * Gets the value of the jobDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobDistrict() {
        return jobDistrict;
    }

    /**
     * Sets the value of the jobDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobDistrict(String value) {
        this.jobDistrict = value;
    }

    /**
     * Gets the value of the jobCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobCity() {
        return jobCity;
    }

    /**
     * Sets the value of the jobCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobCity(String value) {
        this.jobCity = value;
    }

    /**
     * Gets the value of the jobStreet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobStreet() {
        return jobStreet;
    }

    /**
     * Sets the value of the jobStreet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobStreet(String value) {
        this.jobStreet = value;
    }

    /**
     * Gets the value of the jobSettlement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobSettlement() {
        return jobSettlement;
    }

    /**
     * Sets the value of the jobSettlement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobSettlement(String value) {
        this.jobSettlement = value;
    }

    /**
     * Gets the value of the jobHouseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobHouseNumber() {
        return jobHouseNumber;
    }

    /**
     * Sets the value of the jobHouseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobHouseNumber(String value) {
        this.jobHouseNumber = value;
    }

    /**
     * Gets the value of the jobBuilding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobBuilding() {
        return jobBuilding;
    }

    /**
     * Sets the value of the jobBuilding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobBuilding(String value) {
        this.jobBuilding = value;
    }

    /**
     * Gets the value of the jobConstruction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobConstruction() {
        return jobConstruction;
    }

    /**
     * Sets the value of the jobConstruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobConstruction(String value) {
        this.jobConstruction = value;
    }

    /**
     * Gets the value of the jobApartmentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobApartmentNumber() {
        return jobApartmentNumber;
    }

    /**
     * Sets the value of the jobApartmentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobApartmentNumber(String value) {
        this.jobApartmentNumber = value;
    }

}
