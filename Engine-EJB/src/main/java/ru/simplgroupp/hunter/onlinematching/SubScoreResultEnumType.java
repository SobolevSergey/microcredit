
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubScoreResultEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubScoreResultEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="all"/&gt;
 *     &lt;enumeration value="most significant"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SubScoreResultEnumType", namespace = "urn:mclsoftware.co.uk:hunterII")
@XmlEnum
public enum SubScoreResultEnumType {

    @XmlEnumValue("all")
    ALL("all"),
    @XmlEnumValue("most significant")
    MOST_SIGNIFICANT("most significant");
    private final String value;

    SubScoreResultEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SubScoreResultEnumType fromValue(String v) {
        for (SubScoreResultEnumType c: SubScoreResultEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
