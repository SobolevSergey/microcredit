
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SCL.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SCL"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LM"/&gt;
 *     &lt;enumeration value="LO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SCL", namespace = "")
@XmlEnum
public enum SCL {


    /**
     * Load and Match
     * 
     */
    LM,

    /**
     * Load only
     * 
     */
    LO;

    public String value() {
        return name();
    }

    public static SCL fromValue(String v) {
        return valueOf(v);
    }

}
