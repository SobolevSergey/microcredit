
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicantCH complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicantCH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Bank_CH_L3M_Num_of_Delinquency_1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L3M_Num_of_Delinquency_30" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L3M_Num_of_Delinquency_60" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L3M_Num_of_Delinquency_90" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L12M_Num_of_Delinquency_1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L12M_Num_of_Delinquency_30" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L12M_Num_of_Delinquency_60" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L12M_Num_of_Delinquency_90" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L24M_Num_of_Delinquency_1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L24M_Num_of_Delinquency_30" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L24M_Num_of_Delinquency_60" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_L24M_Num_of_Delinquency_90" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Ever_Num_of_Delinquency_1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Ever_Num_of_Delinquency_30" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Ever_Num_of_Delinquency_60" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Ever_Num_of_Delinquency_90" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Total_Current_Arrears_Balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Credit_History_Length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Total_Monthly_Installment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Bank_CH_Credit_History_Exists" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicantCH", propOrder = {
    "bankCHL3MNumOfDelinquency1",
    "bankCHL3MNumOfDelinquency30",
    "bankCHL3MNumOfDelinquency60",
    "bankCHL3MNumOfDelinquency90",
    "bankCHL12MNumOfDelinquency1",
    "bankCHL12MNumOfDelinquency30",
    "bankCHL12MNumOfDelinquency60",
    "bankCHL12MNumOfDelinquency90",
    "bankCHL24MNumOfDelinquency1",
    "bankCHL24MNumOfDelinquency30",
    "bankCHL24MNumOfDelinquency60",
    "bankCHL24MNumOfDelinquency90",
    "bankCHEverNumOfDelinquency1",
    "bankCHEverNumOfDelinquency30",
    "bankCHEverNumOfDelinquency60",
    "bankCHEverNumOfDelinquency90",
    "bankCHTotalCurrentArrearsBalance",
    "bankCHCreditHistoryLength",
    "bankCHTotalMonthlyInstallment",
    "bankCHCreditHistoryExists"
})
public class ApplicantCH {

    @XmlElement(name = "Bank_CH_L3M_Num_of_Delinquency_1")
    protected Integer bankCHL3MNumOfDelinquency1;
    @XmlElement(name = "Bank_CH_L3M_Num_of_Delinquency_30")
    protected Integer bankCHL3MNumOfDelinquency30;
    @XmlElement(name = "Bank_CH_L3M_Num_of_Delinquency_60")
    protected Integer bankCHL3MNumOfDelinquency60;
    @XmlElement(name = "Bank_CH_L3M_Num_of_Delinquency_90")
    protected Integer bankCHL3MNumOfDelinquency90;
    @XmlElement(name = "Bank_CH_L12M_Num_of_Delinquency_1")
    protected Integer bankCHL12MNumOfDelinquency1;
    @XmlElement(name = "Bank_CH_L12M_Num_of_Delinquency_30")
    protected Integer bankCHL12MNumOfDelinquency30;
    @XmlElement(name = "Bank_CH_L12M_Num_of_Delinquency_60")
    protected Integer bankCHL12MNumOfDelinquency60;
    @XmlElement(name = "Bank_CH_L12M_Num_of_Delinquency_90")
    protected Integer bankCHL12MNumOfDelinquency90;
    @XmlElement(name = "Bank_CH_L24M_Num_of_Delinquency_1")
    protected Integer bankCHL24MNumOfDelinquency1;
    @XmlElement(name = "Bank_CH_L24M_Num_of_Delinquency_30")
    protected Integer bankCHL24MNumOfDelinquency30;
    @XmlElement(name = "Bank_CH_L24M_Num_of_Delinquency_60")
    protected Integer bankCHL24MNumOfDelinquency60;
    @XmlElement(name = "Bank_CH_L24M_Num_of_Delinquency_90")
    protected Integer bankCHL24MNumOfDelinquency90;
    @XmlElement(name = "Bank_CH_Ever_Num_of_Delinquency_1")
    protected Integer bankCHEverNumOfDelinquency1;
    @XmlElement(name = "Bank_CH_Ever_Num_of_Delinquency_30")
    protected Integer bankCHEverNumOfDelinquency30;
    @XmlElement(name = "Bank_CH_Ever_Num_of_Delinquency_60")
    protected Integer bankCHEverNumOfDelinquency60;
    @XmlElement(name = "Bank_CH_Ever_Num_of_Delinquency_90")
    protected Integer bankCHEverNumOfDelinquency90;
    @XmlElement(name = "Bank_CH_Total_Current_Arrears_Balance")
    protected BigDecimal bankCHTotalCurrentArrearsBalance;
    @XmlElement(name = "Bank_CH_Credit_History_Length")
    protected Integer bankCHCreditHistoryLength;
    @XmlElement(name = "Bank_CH_Total_Monthly_Installment")
    protected BigDecimal bankCHTotalMonthlyInstallment;
    @XmlElement(name = "Bank_CH_Credit_History_Exists")
    protected String bankCHCreditHistoryExists;

    /**
     * Gets the value of the bankCHL3MNumOfDelinquency1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL3MNumOfDelinquency1() {
        return bankCHL3MNumOfDelinquency1;
    }

    /**
     * Sets the value of the bankCHL3MNumOfDelinquency1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL3MNumOfDelinquency1(Integer value) {
        this.bankCHL3MNumOfDelinquency1 = value;
    }

    /**
     * Gets the value of the bankCHL3MNumOfDelinquency30 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL3MNumOfDelinquency30() {
        return bankCHL3MNumOfDelinquency30;
    }

    /**
     * Sets the value of the bankCHL3MNumOfDelinquency30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL3MNumOfDelinquency30(Integer value) {
        this.bankCHL3MNumOfDelinquency30 = value;
    }

    /**
     * Gets the value of the bankCHL3MNumOfDelinquency60 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL3MNumOfDelinquency60() {
        return bankCHL3MNumOfDelinquency60;
    }

    /**
     * Sets the value of the bankCHL3MNumOfDelinquency60 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL3MNumOfDelinquency60(Integer value) {
        this.bankCHL3MNumOfDelinquency60 = value;
    }

    /**
     * Gets the value of the bankCHL3MNumOfDelinquency90 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL3MNumOfDelinquency90() {
        return bankCHL3MNumOfDelinquency90;
    }

    /**
     * Sets the value of the bankCHL3MNumOfDelinquency90 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL3MNumOfDelinquency90(Integer value) {
        this.bankCHL3MNumOfDelinquency90 = value;
    }

    /**
     * Gets the value of the bankCHL12MNumOfDelinquency1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL12MNumOfDelinquency1() {
        return bankCHL12MNumOfDelinquency1;
    }

    /**
     * Sets the value of the bankCHL12MNumOfDelinquency1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL12MNumOfDelinquency1(Integer value) {
        this.bankCHL12MNumOfDelinquency1 = value;
    }

    /**
     * Gets the value of the bankCHL12MNumOfDelinquency30 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL12MNumOfDelinquency30() {
        return bankCHL12MNumOfDelinquency30;
    }

    /**
     * Sets the value of the bankCHL12MNumOfDelinquency30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL12MNumOfDelinquency30(Integer value) {
        this.bankCHL12MNumOfDelinquency30 = value;
    }

    /**
     * Gets the value of the bankCHL12MNumOfDelinquency60 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL12MNumOfDelinquency60() {
        return bankCHL12MNumOfDelinquency60;
    }

    /**
     * Sets the value of the bankCHL12MNumOfDelinquency60 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL12MNumOfDelinquency60(Integer value) {
        this.bankCHL12MNumOfDelinquency60 = value;
    }

    /**
     * Gets the value of the bankCHL12MNumOfDelinquency90 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL12MNumOfDelinquency90() {
        return bankCHL12MNumOfDelinquency90;
    }

    /**
     * Sets the value of the bankCHL12MNumOfDelinquency90 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL12MNumOfDelinquency90(Integer value) {
        this.bankCHL12MNumOfDelinquency90 = value;
    }

    /**
     * Gets the value of the bankCHL24MNumOfDelinquency1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL24MNumOfDelinquency1() {
        return bankCHL24MNumOfDelinquency1;
    }

    /**
     * Sets the value of the bankCHL24MNumOfDelinquency1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL24MNumOfDelinquency1(Integer value) {
        this.bankCHL24MNumOfDelinquency1 = value;
    }

    /**
     * Gets the value of the bankCHL24MNumOfDelinquency30 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL24MNumOfDelinquency30() {
        return bankCHL24MNumOfDelinquency30;
    }

    /**
     * Sets the value of the bankCHL24MNumOfDelinquency30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL24MNumOfDelinquency30(Integer value) {
        this.bankCHL24MNumOfDelinquency30 = value;
    }

    /**
     * Gets the value of the bankCHL24MNumOfDelinquency60 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL24MNumOfDelinquency60() {
        return bankCHL24MNumOfDelinquency60;
    }

    /**
     * Sets the value of the bankCHL24MNumOfDelinquency60 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL24MNumOfDelinquency60(Integer value) {
        this.bankCHL24MNumOfDelinquency60 = value;
    }

    /**
     * Gets the value of the bankCHL24MNumOfDelinquency90 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHL24MNumOfDelinquency90() {
        return bankCHL24MNumOfDelinquency90;
    }

    /**
     * Sets the value of the bankCHL24MNumOfDelinquency90 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHL24MNumOfDelinquency90(Integer value) {
        this.bankCHL24MNumOfDelinquency90 = value;
    }

    /**
     * Gets the value of the bankCHEverNumOfDelinquency1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHEverNumOfDelinquency1() {
        return bankCHEverNumOfDelinquency1;
    }

    /**
     * Sets the value of the bankCHEverNumOfDelinquency1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHEverNumOfDelinquency1(Integer value) {
        this.bankCHEverNumOfDelinquency1 = value;
    }

    /**
     * Gets the value of the bankCHEverNumOfDelinquency30 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHEverNumOfDelinquency30() {
        return bankCHEverNumOfDelinquency30;
    }

    /**
     * Sets the value of the bankCHEverNumOfDelinquency30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHEverNumOfDelinquency30(Integer value) {
        this.bankCHEverNumOfDelinquency30 = value;
    }

    /**
     * Gets the value of the bankCHEverNumOfDelinquency60 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHEverNumOfDelinquency60() {
        return bankCHEverNumOfDelinquency60;
    }

    /**
     * Sets the value of the bankCHEverNumOfDelinquency60 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHEverNumOfDelinquency60(Integer value) {
        this.bankCHEverNumOfDelinquency60 = value;
    }

    /**
     * Gets the value of the bankCHEverNumOfDelinquency90 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHEverNumOfDelinquency90() {
        return bankCHEverNumOfDelinquency90;
    }

    /**
     * Sets the value of the bankCHEverNumOfDelinquency90 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHEverNumOfDelinquency90(Integer value) {
        this.bankCHEverNumOfDelinquency90 = value;
    }

    /**
     * Gets the value of the bankCHTotalCurrentArrearsBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBankCHTotalCurrentArrearsBalance() {
        return bankCHTotalCurrentArrearsBalance;
    }

    /**
     * Sets the value of the bankCHTotalCurrentArrearsBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBankCHTotalCurrentArrearsBalance(BigDecimal value) {
        this.bankCHTotalCurrentArrearsBalance = value;
    }

    /**
     * Gets the value of the bankCHCreditHistoryLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBankCHCreditHistoryLength() {
        return bankCHCreditHistoryLength;
    }

    /**
     * Sets the value of the bankCHCreditHistoryLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBankCHCreditHistoryLength(Integer value) {
        this.bankCHCreditHistoryLength = value;
    }

    /**
     * Gets the value of the bankCHTotalMonthlyInstallment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBankCHTotalMonthlyInstallment() {
        return bankCHTotalMonthlyInstallment;
    }

    /**
     * Sets the value of the bankCHTotalMonthlyInstallment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBankCHTotalMonthlyInstallment(BigDecimal value) {
        this.bankCHTotalMonthlyInstallment = value;
    }

    /**
     * Gets the value of the bankCHCreditHistoryExists property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankCHCreditHistoryExists() {
        return bankCHCreditHistoryExists;
    }

    /**
     * Sets the value of the bankCHCreditHistoryExists property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankCHCreditHistoryExists(String value) {
        this.bankCHCreditHistoryExists = value;
    }

}
