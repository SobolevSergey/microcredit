
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JOT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="JOT"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="1"/&gt;
 *     &lt;enumeration value="2"/&gt;
 *     &lt;enumeration value="3"/&gt;
 *     &lt;enumeration value="4"/&gt;
 *     &lt;enumeration value="5"/&gt;
 *     &lt;enumeration value="6"/&gt;
 *     &lt;enumeration value="7"/&gt;
 *     &lt;enumeration value="8"/&gt;
 *     &lt;enumeration value="9"/&gt;
 *     &lt;enumeration value="99"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "JOT", namespace = "")
@XmlEnum
public enum JOT {


    /**
     * Owner
     * 
     */
    @XmlEnumValue("1")
    Owner("1"),

    /**
     * Top Manager
     * 
     */
    @XmlEnumValue("2")
    Top_Manager("2"),

    /**
     * Middle Level Manager
     * 
     */
    @XmlEnumValue("3")
    Middle_Level_Manager("3"),

    /**
     * Low Level Manager
     * 
     */
    @XmlEnumValue("4")
    Low_Level_Manager("4"),

    /**
     * Self Employed
     * 
     */
    @XmlEnumValue("5")
    Self_Employed("5"),

    /**
     * Expert
     * 
     */
    @XmlEnumValue("6")
    Expert("6"),

    /**
     * Specialist
     * 
     */
    @XmlEnumValue("7")
    Specialist("7"),

    /**
     * Employee
     * 
     */
    @XmlEnumValue("8")
    Employee("8"),

    /**
     * Military
     * 
     */
    @XmlEnumValue("9")
    Military("9"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("99")
    Other("99");
    private final String value;

    JOT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static JOT fromValue(String v) {
        for (JOT c: JOT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
