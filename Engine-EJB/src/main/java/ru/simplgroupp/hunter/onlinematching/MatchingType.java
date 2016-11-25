
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MatchScheme" type="{urn:mclsoftware.co.uk:hunterII}MatchSchemeType"/&gt;
 *         &lt;element name="PersistMatches" type="{urn:mclsoftware.co.uk:hunterII}EnumType"/&gt;
 *         &lt;element name="WorklistInsert" type="{urn:mclsoftware.co.uk:hunterII}SwitchType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchingType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "matchScheme",
    "persistMatches",
    "worklistInsert"
})
public class MatchingType {

    @XmlElement(name = "MatchScheme", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected ControlMatchSchemeType matchScheme;
    @XmlElement(name = "PersistMatches", namespace = "urn:mclsoftware.co.uk:hunterII")
    @XmlSchemaType(name = "integer")
    protected int persistMatches;
    @XmlElement(name = "WorklistInsert", namespace = "urn:mclsoftware.co.uk:hunterII")
    @XmlSchemaType(name = "integer")
    protected int worklistInsert;

    /**
     * Gets the value of the matchScheme property.
     * 
     * @return
     *     possible object is
     *     {@link ControlMatchSchemeType }
     *     
     */
    public ControlMatchSchemeType getMatchScheme() {
        return matchScheme;
    }

    /**
     * Sets the value of the matchScheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlMatchSchemeType }
     *     
     */
    public void setMatchScheme(ControlMatchSchemeType value) {
        this.matchScheme = value;
    }

    /**
     * Gets the value of the persistMatches property.
     * 
     */
    public int getPersistMatches() {
        return persistMatches;
    }

    /**
     * Sets the value of the persistMatches property.
     * 
     */
    public void setPersistMatches(int value) {
        this.persistMatches = value;
    }

    /**
     * Gets the value of the worklistInsert property.
     * 
     */
    public int getWorklistInsert() {
        return worklistInsert;
    }

    /**
     * Sets the value of the worklistInsert property.
     * 
     */
    public void setWorklistInsert(int value) {
        this.worklistInsert = value;
    }

}
