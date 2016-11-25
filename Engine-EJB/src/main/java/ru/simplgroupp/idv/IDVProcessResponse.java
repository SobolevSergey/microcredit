
package ru.simplgroupp.idv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Response" type="{http://experian.com/bureau/hosted/nbsm}IDVResponseRoot"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "response"
})
@XmlRootElement(name = "IDVProcessResponse")
public class IDVProcessResponse {

    @XmlElement(name = "Response", required = true, nillable = true)
    protected IDVResponseRoot response;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link IDVResponseRoot }
     *     
     */
    public IDVResponseRoot getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDVResponseRoot }
     *     
     */
    public void setResponse(IDVResponseRoot value) {
        this.response = value;
    }

}
