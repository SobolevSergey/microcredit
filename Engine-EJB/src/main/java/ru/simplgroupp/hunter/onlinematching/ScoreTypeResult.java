
package ru.simplgroupp.hunter.onlinematching;

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
 * <p>Java class for ScoreTypeResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScoreTypeResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ScoreType" type="{}ScoreValueType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="scoreCount" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScoreTypeResult", propOrder = {
    "scoreType"
})
public class ScoreTypeResult {

    @XmlElement(name = "ScoreType", required = true)
    protected List<ScoreValueType> scoreType;
    @XmlAttribute(name = "scoreCount", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger scoreCount;

    /**
     * Gets the value of the scoreType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scoreType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScoreType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScoreValueType }
     * 
     * 
     */
    public List<ScoreValueType> getScoreType() {
        if (scoreType == null) {
            scoreType = new ArrayList<ScoreValueType>();
        }
        return this.scoreType;
    }

    /**
     * Gets the value of the scoreCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getScoreCount() {
        return scoreCount;
    }

    /**
     * Sets the value of the scoreCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setScoreCount(BigInteger value) {
        this.scoreCount = value;
    }

}
