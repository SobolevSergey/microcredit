package ru.simplgroupp.contact.protocol.v2.response.unpacked;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by aro on 17.09.2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResponse {
    @XmlAttribute
    private String ID;
    @XmlAttribute
    private String SIGN_IT;
    @XmlAttribute
    private String VERSION;
    @XmlAttribute
    private String FULL;
    @XmlAttribute
    private String RE;
    @XmlAttribute
    private String ERR_TEXT;
    @XmlAttribute
    private String NOLOG;
    @XmlAttribute
    private String GLOBAL_VERSION;
    @XmlAttribute
    private String GLOBAL_VERSION_SERVER;
    @XmlAttribute
    private String STATE;



    public String getSIGN_IT() {
        return SIGN_IT;
    }

    public void setSIGN_IT(String SIGN_IT) {
        this.SIGN_IT = SIGN_IT;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getFULL() {
        return FULL;
    }

    public void setFULL(String FULL) {
        this.FULL = FULL;
    }

    public String getRE() {
        return RE;
    }

    public void setRE(String RE) {
        this.RE = RE;
    }

    public String getERR_TEXT() {
        return ERR_TEXT;
    }

    public void setERR_TEXT(String ERR_TEXT) {
        this.ERR_TEXT = ERR_TEXT;
    }

    public String getNOLOG() {
        return NOLOG;
    }

    public void setNOLOG(String NOLOG) {
        this.NOLOG = NOLOG;
    }

    public String getGLOBAL_VERSION() {
        return GLOBAL_VERSION;
    }

    public void setGLOBAL_VERSION(String GLOBAL_VERSION) {
        this.GLOBAL_VERSION = GLOBAL_VERSION;
    }

    public String getGLOBAL_VERSION_SERVER() {
        return GLOBAL_VERSION_SERVER;
    }

    public void setGLOBAL_VERSION_SERVER(String GLOBAL_VERSION_SERVER) {
        this.GLOBAL_VERSION_SERVER = GLOBAL_VERSION_SERVER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

}
