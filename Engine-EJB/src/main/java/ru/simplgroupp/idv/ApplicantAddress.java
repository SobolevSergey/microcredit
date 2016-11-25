
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ApplicantAddress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicantAddress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Address_Flag">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Country">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Postal_Code" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Region">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_District">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_City">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Settlement">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_House_Number">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Building">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Construction" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Apartment_Number" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Tel_Number">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Address_Start_Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Type_of_Registration">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
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
@XmlType(name = "ApplicantAddress", propOrder = {
    "addressFlag",
    "addressType",
    "addressCountry",
    "addressPostalCode",
    "addressRegion",
    "addressDistrict",
    "addressCity",
    "addressSettlement",
    "addressHouseNumber",
    "addressBuilding",
    "addressConstruction",
    "addressApartmentNumber",
    "addressTelNumber",
    "addressStartDate",
    "typeOfRegistration"
})
public class ApplicantAddress {

    @XmlElement(name = "Address_Flag", required = true)
    protected String addressFlag;
    @XmlElement(name = "Address_Type", required = true)
    protected String addressType;
    @XmlElement(name = "Address_Country", required = true)
    protected String addressCountry;
    @XmlElement(name = "Address_Postal_Code")
    protected String addressPostalCode;
    @XmlElement(name = "Address_Region", required = true)
    protected String addressRegion;
    @XmlElement(name = "Address_District", required = true)
    protected String addressDistrict;
    @XmlElement(name = "Address_City", required = true)
    protected String addressCity;
    @XmlElement(name = "Address_Settlement", required = true)
    protected String addressSettlement;
    @XmlElement(name = "Address_House_Number", required = true)
    protected String addressHouseNumber;
    @XmlElement(name = "Address_Building", required = true)
    protected String addressBuilding;
    @XmlElement(name = "Address_Construction")
    protected String addressConstruction;
    @XmlElement(name = "Address_Apartment_Number")
    protected String addressApartmentNumber;
    @XmlElement(name = "Address_Tel_Number", required = true)
    protected String addressTelNumber;
    @XmlElement(name = "Address_Start_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar addressStartDate;
    @XmlElement(name = "Type_of_Registration", required = true)
    protected String typeOfRegistration;

    /**
     * Gets the value of the addressFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressFlag() {
        return addressFlag;
    }

    /**
     * Sets the value of the addressFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressFlag(String value) {
        this.addressFlag = value;
    }

    /**
     * Gets the value of the addressType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     * Sets the value of the addressType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressType(String value) {
        this.addressType = value;
    }

    /**
     * Gets the value of the addressCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * Sets the value of the addressCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressCountry(String value) {
        this.addressCountry = value;
    }

    /**
     * Gets the value of the addressPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    /**
     * Sets the value of the addressPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressPostalCode(String value) {
        this.addressPostalCode = value;
    }

    /**
     * Gets the value of the addressRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressRegion() {
        return addressRegion;
    }

    /**
     * Sets the value of the addressRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressRegion(String value) {
        this.addressRegion = value;
    }

    /**
     * Gets the value of the addressDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressDistrict() {
        return addressDistrict;
    }

    /**
     * Sets the value of the addressDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressDistrict(String value) {
        this.addressDistrict = value;
    }

    /**
     * Gets the value of the addressCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * Sets the value of the addressCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressCity(String value) {
        this.addressCity = value;
    }

    /**
     * Gets the value of the addressSettlement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressSettlement() {
        return addressSettlement;
    }

    /**
     * Sets the value of the addressSettlement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressSettlement(String value) {
        this.addressSettlement = value;
    }

    /**
     * Gets the value of the addressHouseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressHouseNumber() {
        return addressHouseNumber;
    }

    /**
     * Sets the value of the addressHouseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressHouseNumber(String value) {
        this.addressHouseNumber = value;
    }

    /**
     * Gets the value of the addressBuilding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressBuilding() {
        return addressBuilding;
    }

    /**
     * Sets the value of the addressBuilding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressBuilding(String value) {
        this.addressBuilding = value;
    }

    /**
     * Gets the value of the addressConstruction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressConstruction() {
        return addressConstruction;
    }

    /**
     * Sets the value of the addressConstruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressConstruction(String value) {
        this.addressConstruction = value;
    }

    /**
     * Gets the value of the addressApartmentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressApartmentNumber() {
        return addressApartmentNumber;
    }

    /**
     * Sets the value of the addressApartmentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressApartmentNumber(String value) {
        this.addressApartmentNumber = value;
    }

    /**
     * Gets the value of the addressTelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressTelNumber() {
        return addressTelNumber;
    }

    /**
     * Sets the value of the addressTelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressTelNumber(String value) {
        this.addressTelNumber = value;
    }

    /**
     * Gets the value of the addressStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAddressStartDate() {
        return addressStartDate;
    }

    /**
     * Sets the value of the addressStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAddressStartDate(XMLGregorianCalendar value) {
        this.addressStartDate = value;
    }

    /**
     * Gets the value of the typeOfRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfRegistration() {
        return typeOfRegistration;
    }

    /**
     * Sets the value of the typeOfRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfRegistration(String value) {
        this.typeOfRegistration = value;
    }

}
