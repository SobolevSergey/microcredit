
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllScoresType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllScoresType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
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
@XmlType(name = "AllScoresType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "resultType"
})
public class AllScoresType {

    @XmlElement(name = "ResultType", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    @XmlSchemaType(name = "string")
    protected SubScoreResultEnumType resultType;
    @XmlAttribute(name = "currentVersion", required = true)
    protected ControlBooleanType currentVersion;

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
