
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConstTrueType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConstTrueType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="true"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ConstTrueType", namespace = "urn:mclsoftware.co.uk:hunterII")
@XmlEnum
public enum ConstTrueType {

    @XmlEnumValue("true")
    TRUE("true");
    private final String value;

    ConstTrueType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConstTrueType fromValue(String v) {
        for (ConstTrueType c: ConstTrueType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
