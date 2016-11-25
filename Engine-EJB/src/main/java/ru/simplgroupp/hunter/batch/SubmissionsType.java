
package ru.simplgroupp.hunter.batch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmissionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmissionsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Submission" type="{}SubmissionType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="acceptRecCount" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *       &lt;attribute name="errorRecCount" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *       &lt;attribute name="duplicateRecCount" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *       &lt;attribute name="warningRecCount" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmissionsType", propOrder = {
    "submission"
})
public class SubmissionsType {

    @XmlElement(name = "Submission", required = true)
    protected List<SubmissionType> submission;
    @XmlAttribute(name = "acceptRecCount", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger acceptRecCount;
    @XmlAttribute(name = "errorRecCount", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger errorRecCount;
    @XmlAttribute(name = "duplicateRecCount", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger duplicateRecCount;
    @XmlAttribute(name = "warningRecCount", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger warningRecCount;

    /**
     * Gets the value of the submission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubmissionType }
     * 
     * 
     */
    public List<SubmissionType> getSubmission() {
        if (submission == null) {
            submission = new ArrayList<SubmissionType>();
        }
        return this.submission;
    }

    /**
     * Gets the value of the acceptRecCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAcceptRecCount() {
        return acceptRecCount;
    }

    /**
     * Sets the value of the acceptRecCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAcceptRecCount(BigInteger value) {
        this.acceptRecCount = value;
    }

    /**
     * Gets the value of the errorRecCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorRecCount() {
        return errorRecCount;
    }

    /**
     * Sets the value of the errorRecCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorRecCount(BigInteger value) {
        this.errorRecCount = value;
    }

    /**
     * Gets the value of the duplicateRecCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDuplicateRecCount() {
        return duplicateRecCount;
    }

    /**
     * Sets the value of the duplicateRecCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDuplicateRecCount(BigInteger value) {
        this.duplicateRecCount = value;
    }

    /**
     * Gets the value of the warningRecCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWarningRecCount() {
        return warningRecCount;
    }

    /**
     * Sets the value of the warningRecCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWarningRecCount(BigInteger value) {
        this.warningRecCount = value;
    }

}
