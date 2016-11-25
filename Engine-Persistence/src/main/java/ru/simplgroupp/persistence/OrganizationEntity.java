package ru.simplgroupp.persistence;

import org.apache.commons.lang3.StringUtils;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Организации
 */
public class OrganizationEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5367614842747826365L;
    protected Integer txVersion = 0;

    /**
     * инн
     */
    private String inn;
    /**
     * название
     */
    private String name;
    /**
     * телефон
     */
    private String phone;
    /**
     * email
     */
    private String email;
    /**
     * кпп
     */
    private String kpp;
    /**
     * огрн
     */
    private String ogrn;
    /**
     * тип организации
     */
    private ReferenceEntity orgTypeId;
    /**
     * запись активная или нет
     */
    private Integer isactive;
    /**
     * дата начала
     */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * является ли кредитной организацией
     */
    private Boolean isCreditOrg;
    /**
     * связь с таблицей партнеров
     */
    private PartnersEntity partnersId;
    /**
     * бесплатный телефон для клиентов
     */
    private String freePhone;
    /**
     * счета организаций
     */
    private List<AccountEntity> accounts = new ArrayList<AccountEntity>(0);

    /**
     * номер куда отправлять смс уведомления для менеджера
     */
    private String notificationPhone;


    public OrganizationEntity() {

    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsCreditOrg() {
        return isCreditOrg;
    }

    public void setIsCreditOrg(Boolean isCreditOrg) {
        this.isCreditOrg = isCreditOrg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Date getDatabeg() {
        return databeg;
    }

    public void setDatabeg(Date databeg) {
        this.databeg = databeg;
    }

    public Date getDataend() {
        return dataend;
    }

    public void setDataend(Date dataend) {
        this.dataend = dataend;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public ReferenceEntity getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(ReferenceEntity orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getFreePhone() {
        return freePhone;
    }

    public void setFreePhone(String freePhone) {
        this.freePhone = freePhone;
    }

    /**
     * организация для выгрузки в формате - название; инн; огрн
     *
     * @return
     */
    public String getDescriptionUpload() {
        String descr = "";
        if (StringUtils.isNotEmpty(this.name)) {
            descr += this.name;
        }
        if (StringUtils.isNotEmpty(this.inn)) {
            descr += "; " + this.inn;
        }
        if (StringUtils.isNotEmpty(this.ogrn)) {
            descr += "; " + this.ogrn;
        }
        return descr;
    }

    /**
     * возвращает подпись для сообщений клиенту
     *
     * @return
     */
    public String getMessageFooter() {
        String footer = " " + this.getName().trim();
        if (StringUtils.isNotEmpty(this.getFreePhone())) {
            footer += ", тел. " + this.getFreePhone().trim();
        }
        return footer;
    }

    public int getHasAccounts() {
        return getAccounts().size();
    }

    public String getNotificationPhone() {
        return notificationPhone;
    }

    public void setNotificationPhone(String notificationPhone) {
        this.notificationPhone = notificationPhone;
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        OrganizationEntity ent = (OrganizationEntity) other;
        return Utils.equalsNull(name, ent.getName())
                && Utils.equalsNull(inn, ent.getInn()) && Utils.equalsNull(kpp, ent.getKpp())
                && Utils.equalsNull(ogrn, ent.getOgrn()) && Utils.equalsNull(email, ent.getEmail())
                && Utils.equalsNull(phone, ent.getPhone()) && Utils.equalsNull(orgTypeId, ent.getOrgTypeId())
                && Utils.equalsNull(freePhone, ent.getFreePhone());
    }
}


