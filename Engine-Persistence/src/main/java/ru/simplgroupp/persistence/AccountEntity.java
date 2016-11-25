package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * Счета
 */
public class AccountEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5818445214959898138L;

    protected Integer txVersion = 0;

    /**
     * согласие для счета яндекс
     */
    private Boolean contest;

    /**
     * вид счета
     */
    private ReferenceEntity accountTypeId;

    /**
     * номер счета
     */
    private String accountnumber;

    /**
     * активная запись или нет
     */
    private Integer isactive;

    /**
     * счет открыл работодатель
     */
    private Integer isWork;

    /**
     * бик банка
     */
    private String bik;

    /**
     * свифт банка
     */
    private String swift;

    /**
     * название банка
     */
    private String bankname;

    /**
     * кор.счет
     */
    private String corraccountnumber;

    /**
     * КПП банка
     */
    private String kpp;

    /**
     * название карты
     */
    private String cardname;

    /**
     * номер карты
     */
    private String cardnumber;

    /**
     * месяц окончания карты
     */
    private Integer cardmonthend;

    /**
     * год окончания карты
     */
    private Integer cardyearend;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * организация
     */
    private OrganizationEntity organizationId;

    /**
     * дата добавления
     */
    private Date dateAdd;

    /**
     * пункт Контакта
     */
    private ContactPointsEntity pointId;

    /**
     * номер карты с маской
     */
    private String cardNumberMasked;

    /**
     * хэш для payonline
     */
    private String payonlineCardHash;

    /**
     * владелец карты
     */
    private String cardHolder;

    /**
     * Ключ повторной транзакции payonline
     */
    private String rebillAnchor;

    /**
     * Синоним карты
     */
    private String cardSynonim;

    public AccountEntity() {
    }

    public Boolean getContest() {
        return contest;
    }

    public void setContest(Boolean contest) {
        this.contest = contest;
    }

    public ReferenceEntity getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(ReferenceEntity accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Integer getIsWork() {
        return isWork;
    }

    public void setIsWork(Integer isWork) {
        this.isWork = isWork;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getCorraccountnumber() {
        return corraccountnumber;
    }

    public void setCorraccountnumber(String corraccountnumber) {
        this.corraccountnumber = corraccountnumber;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public Integer getCardmonthend() {
        return cardmonthend;
    }

    public void setCardmonthend(Integer cardmonthend) {
        this.cardmonthend = cardmonthend;
    }


    public Integer getCardyearend() {
        return cardyearend;
    }

    public void setCardyearend(Integer cardyearend) {
        this.cardyearend = cardyearend;
    }

    public OrganizationEntity getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(OrganizationEntity organizationId) {
        this.organizationId = organizationId;
    }

    public String getRebillAnchor() {
        return rebillAnchor;
    }

    public void setRebillAnchor(String rebillAnchor) {
        this.rebillAnchor = rebillAnchor;
    }

    @Override
    public String toString() {
        return this.accountTypeId.getName();
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public ContactPointsEntity getPointId() {
        return pointId;
    }

    public void setPointId(ContactPointsEntity pointId) {
        this.pointId = pointId;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getDescription() {
        String st = "вид зачисления - " + accountTypeId.getName();
        if (StringUtils.isNotEmpty(bik)) {
            st += ", бик банка - " + bik;
        }
        if (StringUtils.isNotEmpty(accountnumber)) {
            st += ", номер счета - " + accountnumber;
        }
        if (StringUtils.isNotEmpty(cardnumber)) {
            st += ", номер карты - ";
            if (StringUtils.isNotEmpty(cardNumberMasked)) {
                st += cardNumberMasked;
            } else if (cardnumber.length() > 12) {
                st += "************" + cardnumber.substring(12);
            }
        }
        return st;
    }

    public String getBankDescription() {
        String st = "";
        if (StringUtils.isNotEmpty(this.bik)) {
            st += this.bik.trim();
        }
        if (StringUtils.isNotEmpty(this.bankname)) {
            st += ", " + this.bankname.toUpperCase();
        }
        return st;
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        AccountEntity ent = (AccountEntity) other;
        return Utils.equalsNull(accountTypeId, ent.accountTypeId) && Utils.equalsNull(bik, ent.bik) &&
                Utils.equalsNull(accountnumber, ent.accountnumber) && Utils.equalsNull(bankname, ent.bankname) &&
                Utils.equalsNull(cardnumber, ent.cardnumber);

    }

    public String getPayonlineCardHash() {
        return payonlineCardHash;
    }

    public void setPayonlineCardHash(String payonlineCardHash) {
        this.payonlineCardHash = payonlineCardHash;
    }

    public String getCardNumberMasked() {
        if (StringUtils.isNotEmpty(cardNumberMasked)) {
            return cardNumberMasked;
        }

        if (StringUtils.isNotEmpty(cardnumber)) {
            if (cardnumber.length() > 12) {
                return "************" + cardnumber.substring(12);
            }
        }
        return null;
    }

    public void setCardNumberMasked(String cardNumberMasked) {
        this.cardNumberMasked = cardNumberMasked;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardSynonim() {
        return cardSynonim;
    }

    public void setCardSynonim(String cardSynonim) {
        this.cardSynonim = cardSynonim;
    }
}
