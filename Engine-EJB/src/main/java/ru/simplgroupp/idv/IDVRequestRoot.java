
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDVRequestRoot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDVRequestRoot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Application" type="{http://experian.com/bureau/hosted/nbsm}IDVReqBusinesInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IDVRequestRoot", propOrder = {
    "application"
})
public class IDVRequestRoot {

    @XmlElement(name = "Application", required = true)
    protected IDVReqBusinesInfo application;

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link IDVReqBusinesInfo }
     *     
     */
    public IDVReqBusinesInfo getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDVReqBusinesInfo }
     *     
     */
    public void setApplication(IDVReqBusinesInfo value) {
        this.application = value;
    }

}
