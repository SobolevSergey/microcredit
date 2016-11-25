
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LoadingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoadingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SubmissionLoad" type="{urn:mclsoftware.co.uk:hunterII}SwitchType"/&gt;
 *         &lt;element name="SuppressVersion"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Identifier" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="1"/&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="flag" use="required" type="{urn:mclsoftware.co.uk:hunterII}SwitchType" /&gt;
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
@XmlType(name = "LoadingType", namespace = "urn:mclsoftware.co.uk:hunterII", propOrder = {
    "submissionLoad",
    "suppressVersion"
})
public class LoadingType {

    @XmlElement(name = "SubmissionLoad", namespace = "urn:mclsoftware.co.uk:hunterII")
    @XmlSchemaType(name = "integer")
    protected int submissionLoad;
    @XmlElement(name = "SuppressVersion", namespace = "urn:mclsoftware.co.uk:hunterII", required = true)
    protected LoadingType.SuppressVersion suppressVersion;

    /**
     * Gets the value of the submissionLoad property.
     * 
     */
    public int getSubmissionLoad() {
        return submissionLoad;
    }

    /**
     * Sets the value of the submissionLoad property.
     * 
     */
    public void setSubmissionLoad(int value) {
        this.submissionLoad = value;
    }

    /**
     * Gets the value of the suppressVersion property.
     * 
     * @return
     *     possible object is
     *     {@link LoadingType.SuppressVersion }
     *     
     */
    public LoadingType.SuppressVersion getSuppressVersion() {
        return suppressVersion;
    }

    /**
     * Sets the value of the suppressVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoadingType.SuppressVersion }
     *     
     */
    public void setSuppressVersion(LoadingType.SuppressVersion value) {
        this.suppressVersion = value;
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
     *         &lt;element name="Identifier" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="1"/&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="flag" use="required" type="{urn:mclsoftware.co.uk:hunterII}SwitchType" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "identifier"
    })
    public static class SuppressVersion {

        @XmlElement(name = "Identifier", namespace = "urn:mclsoftware.co.uk:hunterII")
        protected String identifier;
        @XmlAttribute(name = "flag", required = true)
        protected int flag;

        /**
         * Gets the value of the identifier property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentifier() {
            return identifier;
        }

        /**
         * Sets the value of the identifier property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentifier(String value) {
            this.identifier = value;
        }

        /**
         * Gets the value of the flag property.
         * 
         */
        public int getFlag() {
            return flag;
        }

        /**
         * Sets the value of the flag property.
         * 
         */
        public void setFlag(int value) {
            this.flag = value;
        }

    }

}
