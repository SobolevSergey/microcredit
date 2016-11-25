
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MWS.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MWS"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="0"/&gt;
 *     &lt;enumeration value="1"/&gt;
 *     &lt;enumeration value="2"/&gt;
 *     &lt;enumeration value="3"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MWS", namespace = "")
@XmlEnum
public enum MWS {


    /**
     * Unworked
     * 
     */
    @XmlEnumValue("0")
    Unworked("0"),

    /**
     * Discard
     * 
     */
    @XmlEnumValue("1")
    Discard("1"),

    /**
     * In-Progress
     * 
     */
    @XmlEnumValue("2")
    In_Progress("2"),

    /**
     * Worked
     * 
     */
    @XmlEnumValue("3")
    Worked("3");
    private final String value;

    MWS(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MWS fromValue(String v) {
        for (MWS c: MWS.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
