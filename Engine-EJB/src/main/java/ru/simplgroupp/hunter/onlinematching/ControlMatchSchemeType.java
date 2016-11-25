
package ru.simplgroupp.hunter.onlinematching;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchSchemeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchSchemeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SchemeID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchSchemeType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "schemeID"
})
public class ControlMatchSchemeType {

    @XmlElement(name = "SchemeID", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected List<BigInteger> schemeID;

    /**
     * Gets the value of the schemeID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schemeID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchemeID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getSchemeID() {
        if (schemeID == null) {
            schemeID = new ArrayList<BigInteger>();
        }
        return this.schemeID;
    }

}
