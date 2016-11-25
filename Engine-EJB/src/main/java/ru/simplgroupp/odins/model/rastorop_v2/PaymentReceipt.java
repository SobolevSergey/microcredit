//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.08 at 02:08:54 PM MSK 
//


package ru.simplgroupp.odins.model.rastorop_v2;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PaymentReceipt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentReceipt"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="Num" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Payer" type="{urn:rastorop.ru:OnlineCreditExchange}Person"/&gt;
 *         &lt;element name="Payee" type="{urn:rastorop.ru:OnlineCreditExchange}Organization"/&gt;
 *         &lt;element name="Contract" type="{urn:rastorop.ru:OnlineCreditExchange}Contract"/&gt;
 *         &lt;element name="SumOfInterest" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="Overdue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="Unit" type="{urn:rastorop.ru:OnlineCreditExchange}Unit" minOccurs="0"/&gt;
 *         &lt;element name="Agent" type="{urn:rastorop.ru:OnlineCreditExchange}Companies" minOccurs="0"/&gt;
 *         &lt;element name="Sum" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentReceipt", propOrder = {
    "date",
    "num",
    "payer",
    "payee",
    "contract",
    "sumOfInterest",
    "overdue",
    "unit",
    "agent",
    "sum"
})
public class PaymentReceipt {

    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "date")
    protected String date;
    @XmlElement(name = "Num", required = true)
    protected String num;
    @XmlElement(name = "Payer", required = true)
    protected Person payer;
    @XmlElement(name = "Payee", required = true)
    protected Organization payee;
    @XmlElement(name = "Contract", required = true)
    protected Contract contract;
    @XmlElement(name = "SumOfInterest")
    protected String sumOfInterest;
    @XmlElement(name = "Overdue")
    protected Boolean overdue;
    @XmlElement(name = "Unit")
    protected Unit unit;
    @XmlElement(name = "Agent")
    protected Companies agent;
    @XmlElement(name = "Sum", required = true)
    protected String sum;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the num property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNum() {
        return num;
    }

    /**
     * Sets the value of the num property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNum(String value) {
        this.num = value;
    }

    /**
     * Gets the value of the payer property.
     * 
     * @return
     *     possible object is
     *     {@link Person }
     *     
     */
    public Person getPayer() {
        return payer;
    }

    /**
     * Sets the value of the payer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Person }
     *     
     */
    public void setPayer(Person value) {
        this.payer = value;
    }

    /**
     * Gets the value of the payee property.
     * 
     * @return
     *     possible object is
     *     {@link Organization }
     *     
     */
    public Organization getPayee() {
        return payee;
    }

    /**
     * Sets the value of the payee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Organization }
     *     
     */
    public void setPayee(Organization value) {
        this.payee = value;
    }

    /**
     * Gets the value of the contract property.
     * 
     * @return
     *     possible object is
     *     {@link Contract }
     *     
     */
    public Contract getContract() {
        return contract;
    }

    /**
     * Sets the value of the contract property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contract }
     *     
     */
    public void setContract(Contract value) {
        this.contract = value;
    }

    /**
     * Gets the value of the sumOfInterest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSumOfInterest() {
        return sumOfInterest;
    }

    /**
     * Sets the value of the sumOfInterest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSumOfInterest(String value) {
        this.sumOfInterest = value;
    }

    /**
     * Gets the value of the overdue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOverdue() {
        return overdue;
    }

    /**
     * Sets the value of the overdue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverdue(Boolean value) {
        this.overdue = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link Unit }
     *     
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Unit }
     *     
     */
    public void setUnit(Unit value) {
        this.unit = value;
    }

    /**
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link Companies }
     *     
     */
    public Companies getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Companies }
     *     
     */
    public void setAgent(Companies value) {
        this.agent = value;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSum() {
        return sum;
    }

    /**
     * Sets the value of the sum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSum(String value) {
        this.sum = value;
    }

}
