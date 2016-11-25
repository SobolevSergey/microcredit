
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDVResponseRoot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDVResponseRoot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusInfo" type="{http://experian.com/bureau/hosted/nbsm}RespTechnicalInfo" minOccurs="0"/>
 *         &lt;element name="Result" type="{http://experian.com/bureau/hosted/nbsm}IDVRespBusinesInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IDVResponseRoot", propOrder = {
    "statusInfo",
    "result"
})
public class IDVResponseRoot {

    @XmlElement(name = "StatusInfo")
    protected RespTechnicalInfo statusInfo;
    @XmlElement(name = "Result")
    protected IDVRespBusinesInfo result;

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
     *     {@link IDVRespBusinesInfo }
     *     
     */
    public IDVRespBusinesInfo getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDVRespBusinesInfo }
     *     
     */
    public void setResult(IDVRespBusinesInfo value) {
        this.result = value;
    }

}
