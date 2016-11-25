//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.19 at 04:50:07 PM OMST 
//


package ru.simplgroupp.odins.model.sberfond;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PaymentExpense complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentExpense">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Num" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Payer" type="{urn:asap.digital:SputnikExchange}Organization"/>
 *         &lt;element name="Payee" type="{urn:asap.digital:SputnikExchange}Person"/>
 *         &lt;element name="Contract" type="{urn:asap.digital:SputnikExchange}Contract"/>
 *         &lt;element name="Agent" type="{urn:asap.digital:SputnikExchange}Сompany" minOccurs="0"/>
 *         &lt;element name="Sum" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentExpense", propOrder = {
    "date",
    "num",
    "payer",
    "payee",
    "contract",
    "agent",
    "sum"
})
public class PaymentExpense {

    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "date")
    protected String date;
    @XmlElement(name = "Num", required = true)
    protected String num;
    @XmlElement(name = "Payer", required = true)
    protected Organization payer;
    @XmlElement(name = "Payee", required = true)
    protected Person payee;
    @XmlElement(name = "Contract", required = true)
    protected Contract contract;
    @XmlElement(name = "Agent")
    protected Сompany agent;
    @XmlElement(name = "Sum", required = true)
    protected String sum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Organization getPayer() {
        return payer;
    }

    public void setPayer(Organization payer) {
        this.payer = payer;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Person getPayee() {
        return payee;
    }

    public void setPayee(Person payee) {
        this.payee = payee;
    }

    public Сompany getAgent() {
        return agent;
    }

    public void setAgent(Сompany agent) {
        this.agent = agent;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}