
package ru.simplgroupp.hunter.hashing;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ENS.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ENS"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="1"/&gt;
 *     &lt;enumeration value="2"/&gt;
 *     &lt;enumeration value="5"/&gt;
 *     &lt;enumeration value="7"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ENS", namespace = "")
@XmlEnum
public enum ENS {


    /**
     * Clear
     * 
     */
    @XmlEnumValue("1")
    Clear(1),

    /**
     * Auto Suspect
     * 
     */
    @XmlEnumValue("2")
    Auto_Suspect(2),

    /**
     * Manual Suspect
     * 
     */
    @XmlEnumValue("5")
    Manual_Suspect(5),

    /**
     * Refer
     * 
     */
    @XmlEnumValue("7")
    Refer(7);

    private static final Map<Integer, ENS> map = new HashMap<>();

    static {
        for (ENS ens : ENS.values()) {
            map.put(ens.value(), ens);
        }
    }

    private final int value;

    ENS(int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static ENS getEnsByCode(int code) {
        return map.get(code);
    }
}
