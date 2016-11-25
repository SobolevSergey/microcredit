
package ru.simplgroupp.hunter.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultBlockType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultBlockType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Submissions" type="{}SubmissionsType"/&gt;
 *         &lt;element name="ErrorWarnings" type="{}ErrorWarningType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="BatchStatus" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="CustomerNumber" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultBlockType", propOrder = {
    "submissions",
    "errorWarnings"
})
public class ResultBlockType {

    @XmlElement(name = "Submissions", required = true)
    protected SubmissionsType submissions;
    @XmlElement(name = "ErrorWarnings", required = true)
    protected ErrorWarningType errorWarnings;
    @XmlAttribute(name = "BatchStatus")
    protected String batchStatus;
    @XmlAttribute(name = "CustomerNumber")
    protected String customerNumber;

    /**
     * Gets the value of the submissions property.
     * 
     * @return
     *     possible object is
     *     {@link SubmissionsType }
     *     
     */
    public SubmissionsType getSubmissions() {
        return submissions;
    }

    /**
     * Sets the value of the submissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmissionsType }
     *     
     */
    public void setSubmissions(SubmissionsType value) {
        this.submissions = value;
    }

    /**
     * Gets the value of the errorWarnings property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorWarningType }
     *     
     */
    public ErrorWarningType getErrorWarnings() {
        return errorWarnings;
    }

    /**
     * Sets the value of the errorWarnings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorWarningType }
     *     
     */
    public void setErrorWarnings(ErrorWarningType value) {
        this.errorWarnings = value;
    }

    /**
     * Gets the value of the batchStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchStatus() {
        return batchStatus;
    }

    /**
     * Sets the value of the batchStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchStatus(String value) {
        this.batchStatus = value;
    }

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

}
