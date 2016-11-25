
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductParamsData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductParamsData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Product_Params_Cost_Of_Living" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Product_Params_Interest_Rate" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="5"/>
 *         &lt;element name="Product_Params_Max_Credit_Limit" type="{http://www.w3.org/2001/XMLSchema}decimal" maxOccurs="5"/>
 *         &lt;element name="Product_Params_Min_Credit_Limit" type="{http://www.w3.org/2001/XMLSchema}decimal" maxOccurs="5"/>
 *         &lt;element name="Product_Params_Max_Term" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="5"/>
 *         &lt;element name="Product_Params_Min_Term" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="5"/>
 *         &lt;element name="Product_Params_Exchange_Rate_USD" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Product_Params_Exchange_Rate_EUR" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Product_Params_Number_of_Card_Grades" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Product_Params_Term_Step" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductParamsData", propOrder = {
    "productParamsCostOfLiving",
    "productParamsInterestRate",
    "productParamsMaxCreditLimit",
    "productParamsMinCreditLimit",
    "productParamsMaxTerm",
    "productParamsMinTerm",
    "productParamsExchangeRateUSD",
    "productParamsExchangeRateEUR",
    "productParamsNumberOfCardGrades",
    "productParamsTermStep"
})
public class ProductParamsData {

    @XmlElement(name = "Product_Params_Cost_Of_Living", required = true)
    protected BigDecimal productParamsCostOfLiving;
    @XmlElement(name = "Product_Params_Interest_Rate", type = Integer.class)
    protected List<Integer> productParamsInterestRate;
    @XmlElement(name = "Product_Params_Max_Credit_Limit", required = true)
    protected List<BigDecimal> productParamsMaxCreditLimit;
    @XmlElement(name = "Product_Params_Min_Credit_Limit", required = true)
    protected List<BigDecimal> productParamsMinCreditLimit;
    @XmlElement(name = "Product_Params_Max_Term", type = Integer.class)
    protected List<Integer> productParamsMaxTerm;
    @XmlElement(name = "Product_Params_Min_Term", type = Integer.class)
    protected List<Integer> productParamsMinTerm;
    @XmlElement(name = "Product_Params_Exchange_Rate_USD")
    protected int productParamsExchangeRateUSD;
    @XmlElement(name = "Product_Params_Exchange_Rate_EUR")
    protected int productParamsExchangeRateEUR;
    @XmlElementRef(name = "Product_Params_Number_of_Card_Grades", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<Integer> productParamsNumberOfCardGrades;
    @XmlElementRef(name = "Product_Params_Term_Step", namespace = "http://experian.com/bureau/hosted/nbsm", type = JAXBElement.class, required = false)
    protected JAXBElement<Integer> productParamsTermStep;

    /**
     * Gets the value of the productParamsCostOfLiving property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProductParamsCostOfLiving() {
        return productParamsCostOfLiving;
    }

    /**
     * Sets the value of the productParamsCostOfLiving property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProductParamsCostOfLiving(BigDecimal value) {
        this.productParamsCostOfLiving = value;
    }

    /**
     * Gets the value of the productParamsInterestRate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productParamsInterestRate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductParamsInterestRate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getProductParamsInterestRate() {
        if (productParamsInterestRate == null) {
            productParamsInterestRate = new ArrayList<Integer>();
        }
        return this.productParamsInterestRate;
    }

    /**
     * Gets the value of the productParamsMaxCreditLimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productParamsMaxCreditLimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductParamsMaxCreditLimit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigDecimal }
     * 
     * 
     */
    public List<BigDecimal> getProductParamsMaxCreditLimit() {
        if (productParamsMaxCreditLimit == null) {
            productParamsMaxCreditLimit = new ArrayList<BigDecimal>();
        }
        return this.productParamsMaxCreditLimit;
    }

    /**
     * Gets the value of the productParamsMinCreditLimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productParamsMinCreditLimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductParamsMinCreditLimit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigDecimal }
     * 
     * 
     */
    public List<BigDecimal> getProductParamsMinCreditLimit() {
        if (productParamsMinCreditLimit == null) {
            productParamsMinCreditLimit = new ArrayList<BigDecimal>();
        }
        return this.productParamsMinCreditLimit;
    }

    /**
     * Gets the value of the productParamsMaxTerm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productParamsMaxTerm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductParamsMaxTerm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getProductParamsMaxTerm() {
        if (productParamsMaxTerm == null) {
            productParamsMaxTerm = new ArrayList<Integer>();
        }
        return this.productParamsMaxTerm;
    }

    /**
     * Gets the value of the productParamsMinTerm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productParamsMinTerm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductParamsMinTerm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getProductParamsMinTerm() {
        if (productParamsMinTerm == null) {
            productParamsMinTerm = new ArrayList<Integer>();
        }
        return this.productParamsMinTerm;
    }

    /**
     * Gets the value of the productParamsExchangeRateUSD property.
     * 
     */
    public int getProductParamsExchangeRateUSD() {
        return productParamsExchangeRateUSD;
    }

    /**
     * Sets the value of the productParamsExchangeRateUSD property.
     * 
     */
    public void setProductParamsExchangeRateUSD(int value) {
        this.productParamsExchangeRateUSD = value;
    }

    /**
     * Gets the value of the productParamsExchangeRateEUR property.
     * 
     */
    public int getProductParamsExchangeRateEUR() {
        return productParamsExchangeRateEUR;
    }

    /**
     * Sets the value of the productParamsExchangeRateEUR property.
     * 
     */
    public void setProductParamsExchangeRateEUR(int value) {
        this.productParamsExchangeRateEUR = value;
    }

    /**
     * Gets the value of the productParamsNumberOfCardGrades property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getProductParamsNumberOfCardGrades() {
        return productParamsNumberOfCardGrades;
    }

    /**
     * Sets the value of the productParamsNumberOfCardGrades property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setProductParamsNumberOfCardGrades(JAXBElement<Integer> value) {
        this.productParamsNumberOfCardGrades = value;
    }

    /**
     * Gets the value of the productParamsTermStep property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getProductParamsTermStep() {
        return productParamsTermStep;
    }

    /**
     * Sets the value of the productParamsTermStep property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setProductParamsTermStep(JAXBElement<Integer> value) {
        this.productParamsTermStep = value;
    }

}
