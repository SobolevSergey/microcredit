
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ScoreType " type="{urn:mclsoftware.co.uk:hunterII}SubScoreEnumType"/&gt;
 *         &lt;element name="ResultType" type="{urn:mclsoftware.co.uk:hunterII}SubScoreResultEnumType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="currentVersion" use="required" type="{urn:mclsoftware.co.uk:hunterII}BooleanType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScoreType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "scoreType0020",
    "resultType"
})
public class ScoreType {

    @XmlElement(name = "ScoreType ", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    @XmlSchemaType(name = "string")
    protected SubScoreEnumType scoreType0020;
    @XmlElement(name = "ResultType", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    @XmlSchemaType(name = "string")
    protected SubScoreResultEnumType resultType;
    @XmlAttribute(name = "currentVersion", required = true)
    protected ControlBooleanType currentVersion;

    /**
     * Gets the value of the scoreType0020 property.
     * 
     * @return
     *     possible object is
     *     {@link SubScoreEnumType }
     *     
     */
    public SubScoreEnumType getScoreType_0020() {
        return scoreType0020;
    }

    /**
     * Sets the value of the scoreType0020 property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubScoreEnumType }
     *     
     */
    public void setScoreType_0020(SubScoreEnumType value) {
        this.scoreType0020 = value;
    }

    /**
     * Gets the value of the resultType property.
     * 
     * @return
     *     possible object is
     *     {@link SubScoreResultEnumType }
     *     
     */
    public SubScoreResultEnumType getResultType() {
        return resultType;
    }

    /**
     * Sets the value of the resultType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubScoreResultEnumType }
     *     
     */
    public void setResultType(SubScoreResultEnumType value) {
        this.resultType = value;
    }

    /**
     * Gets the value of the currentVersion property.
     * 
     * @return
     *     possible object is
     *     {@link ControlBooleanType }
     *     
     */
    public ControlBooleanType getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Sets the value of the currentVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlBooleanType }
     *     
     */
    public void setCurrentVersion(ControlBooleanType value) {
        this.currentVersion = value;
    }

}
