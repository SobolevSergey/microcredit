package ru.simplgroupp.contact.protocol.v2.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by aro on 15.09.2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DataCXR {
    //Pack="NONE" Encryption="PLAIN"

    @XmlAttribute(name = "Pack")
    private String pack;
    @XmlAttribute(name = "Encryption")
    private String encryption;

    @XmlValue
    private String value;

    private String responseAsXml;

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResponseAsXml() {
        return responseAsXml;
    }

    public void setResponseAsXml(String responseAsXml) {
        this.responseAsXml = responseAsXml;
    }
}
