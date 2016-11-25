
package ru.simplgroupp.hunter.onlinematching;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="MatchSummary"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{}MatchSummaryType"&gt;
 *                 &lt;attribute name="matches" use="required" type="{}SWITCH" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ErrorWarnings"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Errors"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="Error" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="errorCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Warnings"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="Warning" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="warningCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
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
@XmlType(name = "", propOrder = {
    "matchSummary",
    "errorWarnings"
})
@XmlRootElement(name = "ResultBlock")
public class ResultBlock {

    @XmlElement(name = "MatchSummary", required = true)
    protected ResultBlock.MatchSummary matchSummary;
    @XmlElement(name = "ErrorWarnings", required = true)
    protected ResultBlock.ErrorWarnings errorWarnings;

    /**
     * Gets the value of the matchSummary property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBlock.MatchSummary }
     *     
     */
    public ResultBlock.MatchSummary getMatchSummary() {
        return matchSummary;
    }

    /**
     * Sets the value of the matchSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBlock.MatchSummary }
     *     
     */
    public void setMatchSummary(ResultBlock.MatchSummary value) {
        this.matchSummary = value;
    }

    /**
     * Gets the value of the errorWarnings property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBlock.ErrorWarnings }
     *     
     */
    public ResultBlock.ErrorWarnings getErrorWarnings() {
        return errorWarnings;
    }

    /**
     * Sets the value of the errorWarnings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBlock.ErrorWarnings }
     *     
     */
    public void setErrorWarnings(ResultBlock.ErrorWarnings value) {
        this.errorWarnings = value;
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
     *       &lt;sequence&gt;
     *         &lt;element name="Errors"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="Error" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="errorCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Warnings"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="Warning" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="warningCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
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
    @XmlType(name = "", propOrder = {
        "errors",
        "warnings"
    })
    public static class ErrorWarnings {

        @XmlElement(name = "Errors", required = true)
        protected ResultBlock.ErrorWarnings.Errors errors;
        @XmlElement(name = "Warnings", required = true)
        protected ResultBlock.ErrorWarnings.Warnings warnings;

        /**
         * Gets the value of the errors property.
         * 
         * @return
         *     possible object is
         *     {@link ResultBlock.ErrorWarnings.Errors }
         *     
         */
        public ResultBlock.ErrorWarnings.Errors getErrors() {
            return errors;
        }

        /**
         * Sets the value of the errors property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResultBlock.ErrorWarnings.Errors }
         *     
         */
        public void setErrors(ResultBlock.ErrorWarnings.Errors value) {
            this.errors = value;
        }

        /**
         * Gets the value of the warnings property.
         * 
         * @return
         *     possible object is
         *     {@link ResultBlock.ErrorWarnings.Warnings }
         *     
         */
        public ResultBlock.ErrorWarnings.Warnings getWarnings() {
            return warnings;
        }

        /**
         * Sets the value of the warnings property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResultBlock.ErrorWarnings.Warnings }
         *     
         */
        public void setWarnings(ResultBlock.ErrorWarnings.Warnings value) {
            this.warnings = value;
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
         *       &lt;sequence&gt;
         *         &lt;element name="Error" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="errorCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "error"
        })
        public static class Errors {

            @XmlElement(name = "Error")
            protected List<ErrorType> error;
            @XmlAttribute(name = "errorCount", required = true)
            protected BigInteger errorCount;

            /**
             * Gets the value of the error property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the error property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getError().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ErrorType }
             * 
             * 
             */
            public List<ErrorType> getError() {
                if (error == null) {
                    error = new ArrayList<ErrorType>();
                }
                return this.error;
            }

            /**
             * Gets the value of the errorCount property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getErrorCount() {
                return errorCount;
            }

            /**
             * Sets the value of the errorCount property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setErrorCount(BigInteger value) {
                this.errorCount = value;
            }

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
         *       &lt;sequence&gt;
         *         &lt;element name="Warning" type="{}ErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="warningCount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "warning"
        })
        public static class Warnings {

            @XmlElement(name = "Warning")
            protected List<ErrorType> warning;
            @XmlAttribute(name = "warningCount", required = true)
            protected BigInteger warningCount;

            /**
             * Gets the value of the warning property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the warning property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getWarning().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ErrorType }
             * 
             * 
             */
            public List<ErrorType> getWarning() {
                if (warning == null) {
                    warning = new ArrayList<ErrorType>();
                }
                return this.warning;
            }

            /**
             * Gets the value of the warningCount property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getWarningCount() {
                return warningCount;
            }

            /**
             * Sets the value of the warningCount property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setWarningCount(BigInteger value) {
                this.warningCount = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{}MatchSummaryType"&gt;
     *       &lt;attribute name="matches" use="required" type="{}SWITCH" /&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class MatchSummary
        extends MatchSummaryType
    {

        @XmlAttribute(name = "matches", required = true)
        protected int matches;

        /**
         * Gets the value of the matches property.
         * 
         */
        public int getMatches() {
            return matches;
        }

        /**
         * Sets the value of the matches property.
         * 
         */
        public void setMatches(int value) {
            this.matches = value;
        }

    }

}
