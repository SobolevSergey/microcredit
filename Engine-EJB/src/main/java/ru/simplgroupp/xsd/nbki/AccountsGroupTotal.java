//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.04 at 04:17:01 PM MSK 
//


package ru.simplgroupp.xsd.nbki;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AccountsGroupTotal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountsGroupTotal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountTypeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalAccts" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="zeroBalance" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="negativeRating" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="positiveRating" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="highCredit" type="{}CurrencyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="installment" type="{}CurrencyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="currentBalance" type="{}CurrencyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pastDueBalance" type="{}CurrencyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mostRecentAcctDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="oldestAcctDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountsGroupTotal", propOrder = {
    "accountType",
    "accountTypeText",
    "totalAccts",
    "zeroBalance",
    "negativeRating",
    "positiveRating",
    "highCredit",
    "installment",
    "currentBalance",
    "pastDueBalance",
    "mostRecentAcctDate",
    "oldestAcctDate"
})
public class AccountsGroupTotal {

    protected String accountType;
    protected String accountTypeText;
    protected BigInteger totalAccts;
    protected BigInteger zeroBalance;
    protected BigInteger negativeRating;
    protected BigInteger positiveRating;
    protected List<CurrencyCode> highCredit;
    protected List<CurrencyCode> installment;
    protected List<CurrencyCode> currentBalance;
    protected List<CurrencyCode> pastDueBalance;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar mostRecentAcctDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar oldestAcctDate;

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the accountTypeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountTypeText() {
        return accountTypeText;
    }

    /**
     * Sets the value of the accountTypeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountTypeText(String value) {
        this.accountTypeText = value;
    }

    /**
     * Gets the value of the totalAccts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalAccts() {
        return totalAccts;
    }

    /**
     * Sets the value of the totalAccts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalAccts(BigInteger value) {
        this.totalAccts = value;
    }

    /**
     * Gets the value of the zeroBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getZeroBalance() {
        return zeroBalance;
    }

    /**
     * Sets the value of the zeroBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setZeroBalance(BigInteger value) {
        this.zeroBalance = value;
    }

    /**
     * Gets the value of the negativeRating property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNegativeRating() {
        return negativeRating;
    }

    /**
     * Sets the value of the negativeRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNegativeRating(BigInteger value) {
        this.negativeRating = value;
    }

    /**
     * Gets the value of the positiveRating property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPositiveRating() {
        return positiveRating;
    }

    /**
     * Sets the value of the positiveRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPositiveRating(BigInteger value) {
        this.positiveRating = value;
    }

    /**
     * Gets the value of the highCredit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the highCredit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHighCredit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyCode }
     * 
     * 
     */
    public List<CurrencyCode> getHighCredit() {
        if (highCredit == null) {
            highCredit = new ArrayList<CurrencyCode>();
        }
        return this.highCredit;
    }

    /**
     * Gets the value of the installment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the installment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstallment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyCode }
     * 
     * 
     */
    public List<CurrencyCode> getInstallment() {
        if (installment == null) {
            installment = new ArrayList<CurrencyCode>();
        }
        return this.installment;
    }

    /**
     * Gets the value of the currentBalance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the currentBalance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCurrentBalance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyCode }
     * 
     * 
     */
    public List<CurrencyCode> getCurrentBalance() {
        if (currentBalance == null) {
            currentBalance = new ArrayList<CurrencyCode>();
        }
        return this.currentBalance;
    }

    /**
     * Gets the value of the pastDueBalance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pastDueBalance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPastDueBalance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyCode }
     * 
     * 
     */
    public List<CurrencyCode> getPastDueBalance() {
        if (pastDueBalance == null) {
            pastDueBalance = new ArrayList<CurrencyCode>();
        }
        return this.pastDueBalance;
    }

    /**
     * Gets the value of the mostRecentAcctDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMostRecentAcctDate() {
        return mostRecentAcctDate;
    }

    /**
     * Sets the value of the mostRecentAcctDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMostRecentAcctDate(XMLGregorianCalendar value) {
        this.mostRecentAcctDate = value;
    }

    /**
     * Gets the value of the oldestAcctDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOldestAcctDate() {
        return oldestAcctDate;
    }

    /**
     * Sets the value of the oldestAcctDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOldestAcctDate(XMLGregorianCalendar value) {
        this.oldestAcctDate = value;
    }

}