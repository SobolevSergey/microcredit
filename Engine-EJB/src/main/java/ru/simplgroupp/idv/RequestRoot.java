
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequestRoot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestRoot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Application" type="{http://experian.com/bureau/hosted/nbsm}ReqBusinesInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestRoot", propOrder = {
    "application"
})
public class RequestRoot {

    @XmlElement(name = "Application", required = true)
    protected ReqBusinesInfo application;

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link ReqBusinesInfo }
     *     
     */
    public ReqBusinesInfo getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqBusinesInfo }
     *     
     */
    public void setApplication(ReqBusinesInfo value) {
        this.application = value;
    }

}
