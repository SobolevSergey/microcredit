
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RsltCode"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;minInclusive value="1"/&gt;
 *               &lt;maxInclusive value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Scores" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="SpecificScores" type="{urn:mclsoftware.co.uk:hunterII}SubmissionScores" minOccurs="0"/&gt;
 *                   &lt;element name="AllScores" type="{urn:mclsoftware.co.uk:hunterII}AllScoresType" minOccurs="0"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultsType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "rsltCode",
    "scores"
})
public class ResultsType {

    @XmlElement(name = "RsltCode", namespace = "urn:mclsoftware.co.uk:hunterII")
    protected int rsltCode;
    @XmlElement(name = "Scores", namespace = "urn:mclsoftware.co.uk:hunterII")
    protected ResultsType.Scores scores;

    /**
     * Gets the value of the rsltCode property.
     * 
     */
    public int getRsltCode() {
        return rsltCode;
    }

    /**
     * Sets the value of the rsltCode property.
     * 
     */
    public void setRsltCode(int value) {
        this.rsltCode = value;
    }

    /**
     * Gets the value of the scores property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsType.Scores }
     *     
     */
    public ResultsType.Scores getScores() {
        return scores;
    }

    /**
     * Sets the value of the scores property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsType.Scores }
     *     
     */
    public void setScores(ResultsType.Scores value) {
        this.scores = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="SpecificScores" type="{urn:mclsoftware.co.uk:hunterII}SubmissionScores" minOccurs="0"/&gt;
     *         &lt;element name="AllScores" type="{urn:mclsoftware.co.uk:hunterII}AllScoresType" minOccurs="0"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "specificScores",
        "allScores"
    })
    public static class Scores {

        @XmlElement(name = "SpecificScores", namespace = "urn:mclsoftware.co.uk:hunterII")
        protected SubmissionScores specificScores;
        @XmlElement(name = "AllScores", namespace = "urn:mclsoftware.co.uk:hunterII")
        protected AllScoresType allScores;

        /**
         * Gets the value of the specificScores property.
         * 
         * @return
         *     possible object is
         *     {@link SubmissionScores }
         *     
         */
        public SubmissionScores getSpecificScores() {
            return specificScores;
        }

        /**
         * Sets the value of the specificScores property.
         * 
         * @param value
         *     allowed object is
         *     {@link SubmissionScores }
         *     
         */
        public void setSpecificScores(SubmissionScores value) {
            this.specificScores = value;
        }

        /**
         * Gets the value of the allScores property.
         * 
         * @return
         *     possible object is
         *     {@link AllScoresType }
         *     
         */
        public AllScoresType getAllScores() {
            return allScores;
        }

        /**
         * Sets the value of the allScores property.
         * 
         * @param value
         *     allowed object is
         *     {@link AllScoresType }
         *     
         */
        public void setAllScores(AllScoresType value) {
            this.allScores = value;
        }

    }

}
