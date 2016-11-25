
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
 * <p>Java class for MatchSchemeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchSchemeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Scheme" type="{}SchemeIDType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="schemeCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchSchemeType", propOrder = {
    "scheme"
})
public class MatchSchemeType {

    @XmlElement(name = "Scheme")
    protected List<SchemeIDType> scheme;
    @XmlAttribute(name = "schemeCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger schemeCount;

    /**
     * Gets the value of the scheme property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scheme property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScheme().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SchemeIDType }
     * 
     * 
     */
    public List<SchemeIDType> getScheme() {
        if (scheme == null) {
            scheme = new ArrayList<SchemeIDType>();
        }
        return this.scheme;
    }

    /**
     * Gets the value of the schemeCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSchemeCount() {
        return schemeCount;
    }

    /**
     * Sets the value of the schemeCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSchemeCount(BigInteger value) {
        this.schemeCount = value;
    }

}
