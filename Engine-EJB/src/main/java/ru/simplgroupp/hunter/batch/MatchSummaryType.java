
package ru.simplgroupp.hunter.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}MatchSummaryTypeBase"&gt;
 *       &lt;attribute name="HasMatches" type="{}SWITCH" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchSummaryType")
public class MatchSummaryType
    extends MatchSummaryTypeBase
{

    @XmlAttribute(name = "HasMatches")
    protected Integer hasMatches;

    /**
     * Gets the value of the hasMatches property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHasMatches() {
        return hasMatches;
    }

    /**
     * Sets the value of the hasMatches property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHasMatches(Integer value) {
        this.hasMatches = value;
    }

}
