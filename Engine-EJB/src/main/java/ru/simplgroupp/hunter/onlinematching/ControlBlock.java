
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Customer" type="{urn:mclsoftware.co.uk:hunterII}CustomerType"/&gt;
 *         &lt;element name="Loading" type="{urn:mclsoftware.co.uk:hunterII}LoadingType"/&gt;
 *         &lt;element name="Matching" type="{urn:mclsoftware.co.uk:hunterII}MatchingType"/&gt;
 *         &lt;element name="Results" type="{urn:mclsoftware.co.uk:hunterII}ResultsType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "customer",
    "loading",
    "matching",
    "results"
})
@XmlRootElement(name = "ControlBlock", namespace = "urn:mclsoftware.co.uk:hunterII")
public class ControlBlock {

    @XmlElement(name = "Customer", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected CustomerType customer;
    @XmlElement(name = "Loading", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected LoadingType loading;
    @XmlElement(name = "Matching", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected MatchingType matching;
    @XmlElement(name = "Results", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected ResultsType results;

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerType }
     *     
     */
    public CustomerType getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerType }
     *     
     */
    public void setCustomer(CustomerType value) {
        this.customer = value;
    }

    /**
     * Gets the value of the loading property.
     * 
     * @return
     *     possible object is
     *     {@link LoadingType }
     *     
     */
    public LoadingType getLoading() {
        return loading;
    }

    /**
     * Sets the value of the loading property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoadingType }
     *     
     */
    public void setLoading(LoadingType value) {
        this.loading = value;
    }

    /**
     * Gets the value of the matching property.
     * 
     * @return
     *     possible object is
     *     {@link MatchingType }
     *     
     */
    public MatchingType getMatching() {
        return matching;
    }

    /**
     * Sets the value of the matching property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchingType }
     *     
     */
    public void setMatching(MatchingType value) {
        this.matching = value;
    }

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsType }
     *     
     */
    public ResultsType getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsType }
     *     
     */
    public void setResults(ResultsType value) {
        this.results = value;
    }

}
