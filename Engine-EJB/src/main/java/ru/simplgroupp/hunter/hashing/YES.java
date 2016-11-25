
package ru.simplgroupp.hunter.hashing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for YES.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="YES"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="N"/&gt;
 *     &lt;enumeration value="Y"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "YES", namespace = "")
@XmlEnum
public enum YES {


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

    public static YES fromValue(String v) {
        return valueOf(v);
    }

}
