
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ATY.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ATY"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="1"/&gt;
 *     &lt;enumeration value="2"/&gt;
 *     &lt;enumeration value="3"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ATY", namespace = "")
@XmlEnum
public enum ATY {


    /**
     * CoApplicant
     * 
     */
    @XmlEnumValue("1")
    CoApplicant("1"),

    /**
     * Guarantor
     * 
     */
    @XmlEnumValue("2")
    Guarantor("2"),

    /**
     * Collateral Provider
     * 
     */
    @XmlEnumValue("3")
    Collateral_Provider("3");
    private final String value;

    ATY(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ATY fromValue(String v) {
        for (ATY c: ATY.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
