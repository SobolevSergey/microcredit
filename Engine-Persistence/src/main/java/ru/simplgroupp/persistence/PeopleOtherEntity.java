package ru.simplgroupp.persistence;

import ru.simplgroupp.toolkit.common.Utils;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by cobalt on 18.09.15.
 */
public class PeopleOtherEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5357133715162476074L;

    private Integer txVersion = 0;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * Публичная должность если есть
     */
    private String public_role;

    /**
     * Публичная должность родственника если есть
     */
    private String public_relative_role;

    /**
     * ФИО публичного родственника если есть
     */
    private String public_relative_fio;

    /**
     * Фио бенефициарного владельца если есть
     */
    private String benific_fio;

    /**
     * Согласен получать спам на телефон
     */
    private Boolean reclam_accept;

    /**
     * Действует в интересах другого лица
     */
    private Boolean other;

    /**
     * активная запись или нет
     */
    private Integer isactive;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;

    @Transient
    private boolean isDirty;

    @Override
    public String toString() {
        return "Еще дополнительные данные по человеку " + peopleMainId.getId().toString();
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        PeopleOtherEntity ent = (PeopleOtherEntity) other;
        return Utils.equalsNull(other, ent.getOther()) && Utils.equalsNull(reclam_accept, ent.getReclam_accept())
                && Utils.equalsNull(public_role, ent.getPublic_role()) && Utils.equalsNull(public_relative_role, ent.getPublic_relative_role())
                && Utils.equalsNull(public_relative_fio, ent.getPublic_relative_fio()) && Utils.equalsNull(benific_fio, ent.getBenific_fio());
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public String getPublic_role() {
        return public_role;
    }

    public void setPublic_role(String public_role) {
        this.public_role = public_role;
    }

    public String getPublic_relative_role() {
        return public_relative_role;
    }

    public void setPublic_relative_role(String public_relative_role) {
        this.public_relative_role = public_relative_role;
    }

    public String getPublic_relative_fio() {
        return public_relative_fio;
    }

    public void setPublic_relative_fio(String public_relative_fio) {
        this.public_relative_fio = public_relative_fio;
    }

    public String getBenific_fio() {
        return benific_fio;
    }

    public void setBenific_fio(String benific_fio) {
        this.benific_fio = benific_fio;
    }

    public Boolean getReclam_accept() {
        return reclam_accept;
    }

    public void setReclam_accept(Boolean reclam_accept) {
        this.reclam_accept = reclam_accept;
    }

    public Boolean getOther() {
        return other;
    }

    public void setOther(Boolean other) {
        this.other = other;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public PeopleOtherEntity clean() {
        this.isDirty = false;
        return this;
    }

    public PeopleOtherEntity archive() {
        this.isactive = 0;
        return this;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }
}
