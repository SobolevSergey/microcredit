
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseRoot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseRoot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusInfo" type="{http://experian.com/bureau/hosted/nbsm}RespTechnicalInfo" minOccurs="0"/>
 *         &lt;element name="Result" type="{http://experian.com/bureau/hosted/nbsm}RespBusinesInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseRoot", propOrder = {
    "statusInfo",
    "result"
})
public class ResponseRoot {

    @XmlElement(name = "StatusInfo")
    protected RespTechnicalInfo statusInfo;
    @XmlElement(name = "Result")
    protected RespBusinesInfo result;

    /**
     * Gets the value of the statusInfo property.
     * 
     * @return
     *     possible object is
     *     {@link RespTechnicalInfo }
     *     
     */
    public RespTechnicalInfo getStatusInfo() {
        return statusInfo;
    }

    /**
     * Sets the value of the statusInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespTechnicalInfo }
     *     
     */
    public void setStatusInfo(RespTechnicalInfo value) {
        this.statusInfo = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link RespBusinesInfo }
     *     
     */
    public RespBusinesInfo getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespBusinesInfo }
     *     
     */
    public void setResult(RespBusinesInfo value) {
        this.result = value;
    }

}
