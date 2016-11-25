package ru.simplgroupp.persistence;

import ru.simplgroupp.toolkit.common.Utils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Дополнительная информация по людям
 */
public class PeopleMiscEntity extends ExtendedBaseEntity implements Serializable {

    private static final long serialVersionUID = 9180329519316676422L;

    protected Integer txVersion = 0;

    private Integer id;
    /**
     * кол-во детей
     */
    private Integer children;

    /**
     * несовершеннолетние дети
     */
    private Integer underage;

    /**
     * кол-во иждевенцев
     */
    private Short dependants;

    /**
     * активная запись или нет
     */
    private Integer isactive;

    /**
     * дата начала записи
     */
    private Date databeg;

    /**
     * дата окончания записи
     */
    private Date dataend;

    /**
     * дата регистрации
     */
    private Date realtyDate;

    /**
     * есть ли авто
     */
    private Boolean car;

    /**
     * есть ли права
     */
    private Boolean driverLicense;

    /**
     * дата проживания
     */
    private Date regDate;

    /**
     * семейное положение
     */
    private ReferenceEntity marriageId;

    /**
     * отношение к собственности жилья
     */
    private ReferenceEntity realtyId;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;

    /**
     * дата прописки из справочника
     */
    private ReferenceEntity regDateId;
    
    /**
     * дата начала пребывания из справочника
     */
    private ReferenceEntity realtyDateId;

    @Transient
    private boolean isDirty;

    public PeopleMiscEntity(){
    	
    }
    
    public PeopleMiscEntity(Integer id){
    	this.id=id;
    }
    
    public ReferenceEntity getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(ReferenceEntity marriageId) {
        if (!Utils.equalsNull(this.marriageId, marriageId)) {
            this.marriageId = marriageId;
            isDirty = true;
        }
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        if (!Utils.equalsNull(this.children, children)) {
            this.children = children;
            isDirty = true;
        }
    }

    public Integer getUnderage() {
        return underage;
    }

    public void setUnderage(Integer underage) {
        this.underage = underage;
    }

    public Short getDependants() {
        return dependants;
    }

    public void setDependants(Short dependants) {
        if (!Utils.equalsNull(this.dependants, dependants)) {
            this.dependants = dependants;
            isDirty = true;
        }
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

    public Date getRealtyDate() {
        return realtyDate;
    }

    public void setRealtyDate(Date realtyDate) {
        if (realtyDate == null && this.realtyDate == null) {
            return;
        }

        if (realtyDate == null || this.realtyDate == null) {
            this.realtyDate = realtyDate;
            isDirty = true;
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(realtyDate);

        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(this.realtyDate);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ||
                calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ||
                calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
            this.realtyDate = realtyDate;
            isDirty = true;
        }
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        if (regDate == null && this.regDate == null) {
            return;
        }

        if (regDate == null || this.regDate == null) {
            this.regDate = regDate;
            isDirty = true;
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(regDate);

        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(this.regDate);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ||
                calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ||
                calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
            this.regDate = regDate;
            isDirty = true;
        }
        this.regDate = regDate;
    }

    public ReferenceEntity getRealtyId() {
        return realtyId;
    }

    public void setRealtyId(ReferenceEntity realtyId) {
        if (!Utils.equalsNull(this.realtyId, realtyId)) {
            this.realtyId = realtyId;
            isDirty = true;
        }
    }

    public Boolean getCar() {
        return car;
    }

    public void setCar(Boolean car) {
        if (!Utils.equalsNull(this.car, car)) {
            this.car = car;
            isDirty = true;
        }
    }

    @Override
    public String toString() {
        return "дополнительные данные по человеку " + peopleMainId.getId().toString();
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
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


    public Boolean getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(Boolean driverLicense) {
        this.driverLicense = driverLicense;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public PeopleMiscEntity clean() {
        this.isDirty = false;
        return this;
    }

    public PeopleMiscEntity archive() {
        this.isactive = 0;
        return this;
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        PeopleMiscEntity ent = (PeopleMiscEntity) other;
        return Utils.equalsNull(car, ent.getCar()) && Utils.equalsNull(children, ent.getChildren())
                && Utils.equalsNull(dependants, ent.getDependants()) && Utils.equalsNull(marriageId, ent.getMarriageId())
                && Utils.equalsNull(realtyDate != null ? realtyDate.getTime() : null, ent.getRealtyDate() != null ? ent.getRealtyDate().getTime() : null)
                && Utils.equalsNull(regDate != null ? regDate.getTime() : null, ent.getRegDate() != null ? ent.getRegDate().getTime() : null)
                && Utils.equalsNull(realtyId, ent.getRealtyId()) && Utils.equalsNull(driverLicense, ent.getDriverLicense());
    }

    public ReferenceEntity getRegDateId() {
        return regDateId;
    }

    public void setRegDateId(ReferenceEntity regDateId) {
        this.regDateId = regDateId;
    }

	public ReferenceEntity getRealtyDateId() {
		return realtyDateId;
	}

	public void setRealtyDateId(ReferenceEntity realtyDateId) {
		this.realtyDateId = realtyDateId;
	}
}
