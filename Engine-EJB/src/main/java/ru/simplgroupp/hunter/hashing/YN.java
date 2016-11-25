
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for YN.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="YN"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="N"/&gt;
 *     &lt;enumeration value="Y"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "YN", namespace = "")
@XmlEnum
public enum YN {


    /**
     * No
     * 
     */
    N,

    /**
     * Yes
     * 
     */
    Y;

    public String value() {
        return name();
    }

    public static YN fromValue(String v) {
        return valueOf(v);
    }

}
