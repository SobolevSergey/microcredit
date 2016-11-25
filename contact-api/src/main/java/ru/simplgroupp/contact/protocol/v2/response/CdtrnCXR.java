package ru.simplgroupp.contact.protocol.v2.response;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by aro on 15.09.2014.
 */
@XmlRootElement(name = "CDTRN")
@XmlAccessorType(XmlAccessType.NONE)
public class CdtrnCXR {
    //Version="1" CorrespondentID="-1" Code="BASE64" Lang="ru" Stamp="2014-09-15T13:59:04Z"

    @XmlAttribute(name = "Version")
    private String version;
    @XmlAttribute(name = "CorrespondentID")
    private String correspondentID;
    @XmlAttribute(name = "Code")
    private String code;
    @XmlAttribute(name = "Lang")
    private String lang;
    @XmlAttribute(name = "Stamp")
    private String stamp;

    @XmlElement(name = "DATA")
    private DataCXR data;

    @XmlElementWrapper(name = "SIGNATURES")
    @XmlElement(name = "SIGNATURE")
    private List<SignatureCXR> signatures;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCorrespondentID() {
        return correspondentID;
    }

    public void setCorrespondentID(String correspondentID) {
        this.correspondentID = correspondentID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public DataCXR getData() {
        return data;
    }

    public void setData(DataCXR data) {
        this.data = data;
    }

    public List<SignatureCXR> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignatureCXR> signatures) {
        this.signatures = signatures;
    }
}
