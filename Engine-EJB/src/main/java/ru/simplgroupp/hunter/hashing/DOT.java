
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DOT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DOT"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="1"/&gt;
 *     &lt;enumeration value="2"/&gt;
 *     &lt;enumeration value="3"/&gt;
 *     &lt;enumeration value="4"/&gt;
 *     &lt;enumeration value="5"/&gt;
 *     &lt;enumeration value="6"/&gt;
 *     &lt;enumeration value="99"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DOT", namespace = "")
@XmlEnum
public enum DOT {


    /**
     * Military Service ID
     * 
     */
    @XmlEnumValue("1")
    Military_Service_ID("1"),

    /**
     * Foreign Passport
     * 
     */
    @XmlEnumValue("2")
    Foreign_Passport("2"),

    /**
     * Pension Insurance
     * 
     */
    @XmlEnumValue("3")
    Pension_Insurance("3"),

    /**
     * Driving Licence
     * 
     */
    @XmlEnumValue("4")
    Driving_Licence("4"),

    /**
     * Former Passport RF
     * 
     */
    @XmlEnumValue("5")
    Former_Passport_RF("5"),

    /**
     * Not citizen Document
     * 
     */
    @XmlEnumValue("6")
    Not_citizen_Document("6"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("99")
    Other("99");
    private final String value;

    DOT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DOT fromValue(String v) {
        for (DOT c: DOT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
