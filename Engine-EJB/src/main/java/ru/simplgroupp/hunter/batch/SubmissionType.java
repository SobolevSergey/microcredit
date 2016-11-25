
package ru.simplgroupp.hunter.batch;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmissionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmissionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MatchSummary" type="{}MatchSummaryType"/&gt;
 *         &lt;element name="ErrorWarnings" type="{}ErrorWarningType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="submissionID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmissionType", propOrder = {
    "matchSummary",
    "errorWarnings"
})
public class SubmissionType {

    @XmlElement(name = "MatchSummary", required = true)
    protected MatchSummaryType matchSummary;
    @XmlElement(name = "ErrorWarnings", required = true)
    protected ErrorWarningType errorWarnings;
    @XmlAttribute(name = "submissionID", required = true)
    protected String submissionID;
    @XmlAttribute(name = "version", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger version;

    /**
     * Gets the value of the matchSummary property.
     * 
     * @return
     *     possible object is
     *     {@link MatchSummaryType }
     *     
     */
    public MatchSummaryType getMatchSummary() {
        return matchSummary;
    }

    /**
     * Sets the value of the matchSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchSummaryType }
     *     
     */
    public void setMatchSummary(MatchSummaryType value) {
        this.matchSummary = value;
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
     * Gets the value of the submissionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubmissionID() {
        return submissionID;
    }

    /**
     * Sets the value of the submissionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubmissionID(String value) {
        this.submissionID = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVersion(BigInteger value) {
        this.version = value;
    }

}
