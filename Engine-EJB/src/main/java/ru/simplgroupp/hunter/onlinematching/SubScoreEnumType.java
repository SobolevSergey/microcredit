
package ru.simplgroupp.hunter.onlinematching;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubScoreEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubScoreEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SCSS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SubScoreEnumType", namespace = "urn:mclsoftware.co.uk:hunterII")
@XmlEnum
public enum SubScoreEnumType {

    SCSS;

    public String value() {
        return name();
    }

    public static SubScoreEnumType fromValue(String v) {
        return valueOf(v);
    }

}
