
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespBusinesInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespBusinesInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Results_Bank_CH_Type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Bureau_CH_Type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Total_Monthly_Installment" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Total_Expences" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Total_Income" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Disposable_Income" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Application_Scoring_Score" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Results_Application_Scoring_Zone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Results_Behavioural_Scoring_Score" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Results_Behavioural_Scoring_Zone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Results_Offer_Term" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Offer_Card_Grade" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Offer_Credit_Limit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Offer_Monthly_Installment" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_Total_Risk_Level" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Fraud_Risk_Level" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Age_Year" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results_Bureau_Scoring_Score" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Results_SM1_Reason_Codes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Results_SM2_Reason_Codes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Results_Credit_Limit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Results_United_Scoring_Score" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespBusinesInfo", propOrder = {
    "resultsBankCHType",
    "resultsBureauCHType",
    "resultsTotalMonthlyInstallment",
    "resultsTotalExpences",
    "resultsTotalIncome",
    "resultsDisposableIncome",
    "resultsApplicationScoringScore",
    "resultsApplicationScoringZone",
    "resultsBehaviouralScoringScore",
    "resultsBehaviouralScoringZone",
    "resultsOfferTerm",
    "resultsOfferCardGrade",
    "resultsOfferCreditLimit",
    "resultsOfferMonthlyInstallment",
    "resultsTotalRiskLevel",
    "resultsFraudRiskLevel",
    "resultsAgeYear",
    "resultsBureauScoringScore",
    "resultsSM1ReasonCodes",
    "resultsSM2ReasonCodes",
    "resultsCreditLimit",
    "resultsUnitedScoringScore"
})
public class RespBusinesInfo {

    @XmlElement(name = "Results_Bank_CH_Type", required = true, type = Integer.class, nillable = true)
    protected Integer resultsBankCHType;
    @XmlElement(name = "Results_Bureau_CH_Type", required = true, type = Integer.class, nillable = true)
    protected Integer resultsBureauCHType;
    @XmlElement(name = "Results_Total_Monthly_Installment", required = true, nillable = true)
    protected BigDecimal resultsTotalMonthlyInstallment;
    @XmlElement(name = "Results_Total_Expences", required = true, nillable = true)
    protected BigDecimal resultsTotalExpences;
    @XmlElement(name = "Results_Total_Income", required = true, nillable = true)
    protected BigDecimal resultsTotalIncome;
    @XmlElement(name = "Results_Disposable_Income", required = true, nillable = true)
    protected BigDecimal resultsDisposableIncome;
    @XmlElement(name = "Results_Application_Scoring_Score", required = true, type = Float.class, nillable = true)
    protected Float resultsApplicationScoringScore;
    @XmlElement(name = "Results_Application_Scoring_Zone")
    protected String resultsApplicationScoringZone;
    @XmlElement(name = "Results_Behavioural_Scoring_Score", required = true, type = Float.class, nillable = true)
    protected Float resultsBehaviouralScoringScore;
    @XmlElement(name = "Results_Behavioural_Scoring_Zone")
    protected String resultsBehaviouralScoringZone;
    @XmlElement(name = "Results_Offer_Term", required = true, type = Integer.class, nillable = true)
    protected Integer resultsOfferTerm;
    @XmlElement(name = "Results_Offer_Card_Grade", required = true, type = Integer.class, nillable = true)
    protected Integer resultsOfferCardGrade;
    @XmlElement(name = "Results_Offer_Credit_Limit", required = true, nillable = true)
    protected BigDecimal resultsOfferCreditLimit;
    @XmlElement(name = "Results_Offer_Monthly_Installment", required = true, nillable = true)
    protected BigDecimal resultsOfferMonthlyInstallment;
    @XmlElement(name = "Results_Total_Risk_Level", required = true, type = Integer.class, nillable = true)
    protected Integer resultsTotalRiskLevel;
    @XmlElement(name = "Results_Fraud_Risk_Level", required = true, type = Integer.class, nillable = true)
    protected Integer resultsFraudRiskLevel;
    @XmlElement(name = "Results_Age_Year", required = true, type = Integer.class, nillable = true)
    protected Integer resultsAgeYear;
    @XmlElement(name = "Results_Bureau_Scoring_Score", required = true, type = Float.class, nillable = true)
    protected Float resultsBureauScoringScore;
    @XmlElement(name = "Results_SM1_Reason_Codes")
    protected List<String> resultsSM1ReasonCodes;
    @XmlElement(name = "Results_SM2_Reason_Codes")
    protected List<String> resultsSM2ReasonCodes;
    @XmlElement(name = "Results_Credit_Limit", required = true, nillable = true)
    protected BigDecimal resultsCreditLimit;
    @XmlElement(name = "Results_United_Scoring_Score", required = true, type = Integer.class, nillable = true)
    protected Integer resultsUnitedScoringScore;

    /**
     * Gets the value of the resultsBankCHType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsBankCHType() {
        return resultsBankCHType;
    }

    /**
     * Sets the value of the resultsBankCHType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsBankCHType(Integer value) {
        this.resultsBankCHType = value;
    }

    /**
     * Gets the value of the resultsBureauCHType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsBureauCHType() {
        return resultsBureauCHType;
    }

    /**
     * Sets the value of the resultsBureauCHType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsBureauCHType(Integer value) {
        this.resultsBureauCHType = value;
    }

    /**
     * Gets the value of the resultsTotalMonthlyInstallment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsTotalMonthlyInstallment() {
        return resultsTotalMonthlyInstallment;
    }

    /**
     * Sets the value of the resultsTotalMonthlyInstallment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsTotalMonthlyInstallment(BigDecimal value) {
        this.resultsTotalMonthlyInstallment = value;
    }

    /**
     * Gets the value of the resultsTotalExpences property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsTotalExpences() {
        return resultsTotalExpences;
    }

    /**
     * Sets the value of the resultsTotalExpences property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsTotalExpences(BigDecimal value) {
        this.resultsTotalExpences = value;
    }

    /**
     * Gets the value of the resultsTotalIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsTotalIncome() {
        return resultsTotalIncome;
    }

    /**
     * Sets the value of the resultsTotalIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsTotalIncome(BigDecimal value) {
        this.resultsTotalIncome = value;
    }

    /**
     * Gets the value of the resultsDisposableIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsDisposableIncome() {
        return resultsDisposableIncome;
    }

    /**
     * Sets the value of the resultsDisposableIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsDisposableIncome(BigDecimal value) {
        this.resultsDisposableIncome = value;
    }

    /**
     * Gets the value of the resultsApplicationScoringScore property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getResultsApplicationScoringScore() {
        return resultsApplicationScoringScore;
    }

    /**
     * Sets the value of the resultsApplicationScoringScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setResultsApplicationScoringScore(Float value) {
        this.resultsApplicationScoringScore = value;
    }

    /**
     * Gets the value of the resultsApplicationScoringZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultsApplicationScoringZone() {
        return resultsApplicationScoringZone;
    }

    /**
     * Sets the value of the resultsApplicationScoringZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultsApplicationScoringZone(String value) {
        this.resultsApplicationScoringZone = value;
    }

    /**
     * Gets the value of the resultsBehaviouralScoringScore property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getResultsBehaviouralScoringScore() {
        return resultsBehaviouralScoringScore;
    }

    /**
     * Sets the value of the resultsBehaviouralScoringScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setResultsBehaviouralScoringScore(Float value) {
        this.resultsBehaviouralScoringScore = value;
    }

    /**
     * Gets the value of the resultsBehaviouralScoringZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultsBehaviouralScoringZone() {
        return resultsBehaviouralScoringZone;
    }

    /**
     * Sets the value of the resultsBehaviouralScoringZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultsBehaviouralScoringZone(String value) {
        this.resultsBehaviouralScoringZone = value;
    }

    /**
     * Gets the value of the resultsOfferTerm property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsOfferTerm() {
        return resultsOfferTerm;
    }

    /**
     * Sets the value of the resultsOfferTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsOfferTerm(Integer value) {
        this.resultsOfferTerm = value;
    }

    /**
     * Gets the value of the resultsOfferCardGrade property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsOfferCardGrade() {
        return resultsOfferCardGrade;
    }

    /**
     * Sets the value of the resultsOfferCardGrade property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsOfferCardGrade(Integer value) {
        this.resultsOfferCardGrade = value;
    }

    /**
     * Gets the value of the resultsOfferCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsOfferCreditLimit() {
        return resultsOfferCreditLimit;
    }

    /**
     * Sets the value of the resultsOfferCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsOfferCreditLimit(BigDecimal value) {
        this.resultsOfferCreditLimit = value;
    }

    /**
     * Gets the value of the resultsOfferMonthlyInstallment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsOfferMonthlyInstallment() {
        return resultsOfferMonthlyInstallment;
    }

    /**
     * Sets the value of the resultsOfferMonthlyInstallment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsOfferMonthlyInstallment(BigDecimal value) {
        this.resultsOfferMonthlyInstallment = value;
    }

    /**
     * Gets the value of the resultsTotalRiskLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsTotalRiskLevel() {
        return resultsTotalRiskLevel;
    }

    /**
     * Sets the value of the resultsTotalRiskLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsTotalRiskLevel(Integer value) {
        this.resultsTotalRiskLevel = value;
    }

    /**
     * Gets the value of the resultsFraudRiskLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsFraudRiskLevel() {
        return resultsFraudRiskLevel;
    }

    /**
     * Sets the value of the resultsFraudRiskLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsFraudRiskLevel(Integer value) {
        this.resultsFraudRiskLevel = value;
    }

    /**
     * Gets the value of the resultsAgeYear property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsAgeYear() {
        return resultsAgeYear;
    }

    /**
     * Sets the value of the resultsAgeYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsAgeYear(Integer value) {
        this.resultsAgeYear = value;
    }

    /**
     * Gets the value of the resultsBureauScoringScore property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getResultsBureauScoringScore() {
        return resultsBureauScoringScore;
    }

    /**
     * Sets the value of the resultsBureauScoringScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setResultsBureauScoringScore(Float value) {
        this.resultsBureauScoringScore = value;
    }

    /**
     * Gets the value of the resultsSM1ReasonCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultsSM1ReasonCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultsSM1ReasonCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResultsSM1ReasonCodes() {
        if (resultsSM1ReasonCodes == null) {
            resultsSM1ReasonCodes = new ArrayList<String>();
        }
        return this.resultsSM1ReasonCodes;
    }

    /**
     * Gets the value of the resultsSM2ReasonCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultsSM2ReasonCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultsSM2ReasonCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResultsSM2ReasonCodes() {
        if (resultsSM2ReasonCodes == null) {
            resultsSM2ReasonCodes = new ArrayList<String>();
        }
        return this.resultsSM2ReasonCodes;
    }

    /**
     * Gets the value of the resultsCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResultsCreditLimit() {
        return resultsCreditLimit;
    }

    /**
     * Sets the value of the resultsCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResultsCreditLimit(BigDecimal value) {
        this.resultsCreditLimit = value;
    }

    /**
     * Gets the value of the resultsUnitedScoringScore property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultsUnitedScoringScore() {
        return resultsUnitedScoringScore;
    }

    /**
     * Sets the value of the resultsUnitedScoringScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultsUnitedScoringScore(Integer value) {
        this.resultsUnitedScoringScore = value;
    }

}
