package ru.simplgroupp.contact.protocol.v2.response.unpacked;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by aro on 17.09.2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RowUnp {

    @XmlAttribute(name = "VERSION")
    private Integer version;
    @XmlAttribute(name = "ERASED")
    private Integer erased;
    @XmlAttribute(name = "ID")
    private Integer id;
    @XmlAttribute(name = "PARENT_ID")
    private Integer parentId;
    @XmlAttribute(name = "COUNTRY")
    private String country;
    @XmlAttribute(name = "DELETED")
    private Integer deleted;
    @XmlAttribute(name = "CONTACT")
    private Integer contact;
    @XmlAttribute(name = "REGION")
    private Integer region;
    @XmlAttribute(name = "IS_KFM")
    private Integer kfm;
    @XmlAttribute(name = "IS_ONLINE")
    private Integer online;
    @XmlAttribute(name = "CAN_REVOKE")
    private Integer canRevoke;
    @XmlAttribute(name = "CAN_CHANGE")
    private Integer canChange;
    @XmlAttribute(name = "GET_MONEY")
    private Integer getMoney;

    @XmlAttribute(name = "CO_IN_RUR")
    private Integer inRur;
    @XmlAttribute(name = "CO_IN_USD")
    private Integer inUsd;
    @XmlAttribute(name = "CO_IN_EUR")
    private Integer inEur;
    @XmlAttribute(name = "CO_OUT_RUR")
    private Integer outRur;
    @XmlAttribute(name = "CO_OUT_USD")
    private Integer outUsd;
    @XmlAttribute(name = "CO_OUT_EUR")
    private Integer outEur;
    @XmlAttribute(name = "SCEN_ID")
    private Integer sceneId;
    @XmlAttribute(name = "CITY_ID")
    private Integer cityId;
    @XmlAttribute(name = "LOGO_ID")
    private Integer logoId;
    @XmlAttribute(name = "UNIQUE_TRN")
    private Integer uniqueTrn;
    @XmlAttribute(name = "METRO")
    private Integer metro;


    @XmlAttribute(name = "PP_CODE")
    private String ppCode;
    @XmlAttribute(name = "BIC")
    private String bic;
    @XmlAttribute(name = "NAME")
    private String name;
    @XmlAttribute(name = "CITY_HEAD")
    private String cityHead;
    @XmlAttribute(name = "ADDRESS1")
    private String adr1;
    @XmlAttribute(name = "ADDRESS2")
    private String adr2;
    @XmlAttribute(name = "ADDRESS3")
    private String adr3;
    @XmlAttribute(name = "ADDRESS4")
    private String adr4;
    @XmlAttribute(name = "PHONE")
    private String phone;
    @XmlAttribute(name = "NAME_RUS")
    private String nameRus;
    @XmlAttribute(name = "CITY_LAT")
    private String cityLat;
    @XmlAttribute(name = "ADDR_LAT")
    private String adrLat;
    @XmlAttribute(name = "SEND_CURR")
    private String sendCurr;
    @XmlAttribute(name = "REC_CURR")
    private String recCurr;
    @XmlAttribute(name = "ATTR_GRPS")
    private String attrGRPS;


    @XmlAttribute(name = "NAMEU_F")
    private String nameF;
    @XmlAttribute(name = "NAMEU_I")
    private String nameI;
    @XmlAttribute(name = "NAMEU_O")
    private String nameO;
    @XmlAttribute(name = "BIRTHDAY")
    private String birthday;
    @XmlAttribute(name = "PASSPORT")
    private String passport;
    @XmlAttribute(name = "ADDRESS")
    private String adr;
    @XmlAttribute(name = "NAMEU")
    private String nameU;


    @XmlAttribute(name = "S_DOC")
    private String sdoc;
    @XmlAttribute(name = "N_DOC")
    private String ndoc;
    @XmlAttribute(name = "INFO")
    private String info;
    @XmlAttribute(name = "INFO1")
    private String info1;
    @XmlAttribute(name = "TYPDOC")
    private Integer typdoc;
    @XmlAttribute(name = "BLOCKED")
    private Integer blocked;

    @XmlAttribute(name = "CAN_IN")
    private Integer canIn;
    @XmlAttribute(name = "CAN_PAY")
    private Integer canPay;

    @XmlAttribute(name = "CAPTION")
    private String caption;
    @XmlAttribute(name = "COMMENT")
    private String comment;
    @XmlAttribute(name = "CAPTION_LA")
    private String captionLa;
    @XmlAttribute(name = "COMMENT_LA")
    private String commentLa;
    @XmlAttribute(name = "CS_IN")
    private String csIn;
    @XmlAttribute(name = "CS_IN_FEE")
    private String csInFee;
    @XmlAttribute(name = "CS_PAY")
    private String csPay;


    @XmlAttribute(name = "BANK_ID")
    private Integer bankId;
    @XmlAttribute(name = "SERV_ID")
    private Integer servId;





    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getErased() {
        return erased;
    }

    public void setErased(Integer erased) {
        this.erased = erased;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getKfm() {
        return kfm;
    }

    public void setKfm(Integer kfm) {
        this.kfm = kfm;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getCanRevoke() {
        return canRevoke;
    }

    public void setCanRevoke(Integer canRevoke) {
        this.canRevoke = canRevoke;
    }

    public Integer getCanChange() {
        return canChange;
    }

    public void setCanChange(Integer canChange) {
        this.canChange = canChange;
    }

    public Integer getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(Integer getMoney) {
        this.getMoney = getMoney;
    }

    public Integer getInRur() {
        return inRur;
    }

    public void setInRur(Integer inRur) {
        this.inRur = inRur;
    }

    public Integer getInUsd() {
        return inUsd;
    }

    public void setInUsd(Integer inUsd) {
        this.inUsd = inUsd;
    }

    public Integer getInEur() {
        return inEur;
    }

    public void setInEur(Integer inEur) {
        this.inEur = inEur;
    }

    public Integer getOutRur() {
        return outRur;
    }

    public void setOutRur(Integer outRur) {
        this.outRur = outRur;
    }

    public Integer getOutUsd() {
        return outUsd;
    }

    public void setOutUsd(Integer outUsd) {
        this.outUsd = outUsd;
    }

    public Integer getOutEur() {
        return outEur;
    }

    public void setOutEur(Integer outEur) {
        this.outEur = outEur;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getLogoId() {
        return logoId;
    }

    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    public Integer getUniqueTrn() {
        return uniqueTrn;
    }

    public void setUniqueTrn(Integer uniqueTrn) {
        this.uniqueTrn = uniqueTrn;
    }

    public Integer getMetro() {
        return metro;
    }

    public void setMetro(Integer metro) {
        this.metro = metro;
    }

    public String getPpCode() {
        return ppCode;
    }

    public void setPpCode(String ppCode) {
        this.ppCode = ppCode;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityHead() {
        return cityHead;
    }

    public void setCityHead(String cityHead) {
        this.cityHead = cityHead;
    }

    public String getAdr1() {
        return adr1;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getAdr2() {
        return adr2;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public String getAdr3() {
        return adr3;
    }

    public void setAdr3(String adr3) {
        this.adr3 = adr3;
    }

    public String getAdr4() {
        return adr4;
    }

    public void setAdr4(String adr4) {
        this.adr4 = adr4;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNameRus() {
        return nameRus;
    }

    public void setNameRus(String nameRus) {
        this.nameRus = nameRus;
    }

    public String getCityLat() {
        return cityLat;
    }

    public void setCityLat(String cityLat) {
        this.cityLat = cityLat;
    }

    public String getAdrLat() {
        return adrLat;
    }

    public void setAdrLat(String adrLat) {
        this.adrLat = adrLat;
    }

    public String getSendCurr() {
        return sendCurr;
    }

    public void setSendCurr(String sendCurr) {
        this.sendCurr = sendCurr;
    }

    public String getRecCurr() {
        return recCurr;
    }

    public void setRecCurr(String recCurr) {
        this.recCurr = recCurr;
    }

    public String getAttrGRPS() {
        return attrGRPS;
    }

    public void setAttrGRPS(String attrGRPS) {
        this.attrGRPS = attrGRPS;
    }

    public String getNameF() {
        return nameF;
    }

    public void setNameF(String nameF) {
        this.nameF = nameF;
    }

    public String getNameI() {
        return nameI;
    }

    public void setNameI(String nameI) {
        this.nameI = nameI;
    }

    public String getNameO() {
        return nameO;
    }

    public void setNameO(String nameO) {
        this.nameO = nameO;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getNameU() {
        return nameU;
    }

    public void setNameU(String nameU) {
        this.nameU = nameU;
    }

    public String getSdoc() {
        return sdoc;
    }

    public void setSdoc(String sdoc) {
        this.sdoc = sdoc;
    }

    public String getNdoc() {
        return ndoc;
    }

    public void setNdoc(String ndoc) {
        this.ndoc = ndoc;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public Integer getTypdoc() {
        return typdoc;
    }

    public void setTypdoc(Integer typdoc) {
        this.typdoc = typdoc;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    public Integer getCanIn() {
        return canIn;
    }

    public void setCanIn(Integer canIn) {
        this.canIn = canIn;
    }

    public Integer getCanPay() {
        return canPay;
    }

    public void setCanPay(Integer canPay) {
        this.canPay = canPay;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCaptionLa() {
        return captionLa;
    }

    public void setCaptionLa(String captionLa) {
        this.captionLa = captionLa;
    }

    public String getCommentLa() {
        return commentLa;
    }

    public void setCommentLa(String commentLa) {
        this.commentLa = commentLa;
    }

    public String getCsIn() {
        return csIn;
    }

    public void setCsIn(String csIn) {
        this.csIn = csIn;
    }

    public String getCsInFee() {
        return csInFee;
    }

    public void setCsInFee(String csInFee) {
        this.csInFee = csInFee;
    }

    public String getCsPay() {
        return csPay;
    }

    public void setCsPay(String csPay) {
        this.csPay = csPay;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getServId() {
        return servId;
    }

    public void setServId(Integer servId) {
        this.servId = servId;
    }
}
