package ru.simplgroupp.hunter.changepassword;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="changePasswordResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
    "changePasswordResult"
})
@XmlRootElement(name = "changePasswordResponse")
public class ChangePasswordResponse {

    private int changePasswordResult;

    /**
     * Gets the value of the changePasswordResult property.
     * 
     */
    public int getChangePasswordResult() {
        return changePasswordResult;
    }

    /**
     * Sets the value of the changePasswordResult property.
     * 
     */
    public void setChangePasswordResult(int value) {
        this.changePasswordResult = value;
    }

}
