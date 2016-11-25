
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicantVehicle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicantVehicle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Car_Reg_Number" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Car_VIN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
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
@XmlType(name = "ApplicantVehicle", propOrder = {
    "carRegNumber",
    "carVIN"
})
public class ApplicantVehicle {

    @XmlElement(name = "Car_Reg_Number")
    protected String carRegNumber;
    @XmlElement(name = "Car_VIN")
    protected String carVIN;

    /**
     * Gets the value of the carRegNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarRegNumber() {
        return carRegNumber;
    }

    /**
     * Sets the value of the carRegNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarRegNumber(String value) {
        this.carRegNumber = value;
    }

    /**
     * Gets the value of the carVIN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarVIN() {
        return carVIN;
    }

    /**
     * Sets the value of the carVIN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarVIN(String value) {
        this.carVIN = value;
    }

}
