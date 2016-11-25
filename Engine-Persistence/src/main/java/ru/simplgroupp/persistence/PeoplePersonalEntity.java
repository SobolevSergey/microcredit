package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.toolkit.common.Utils;

import javax.persistence.Transient;

/**
 * Персональные данные
 */
public class PeoplePersonalEntity extends ExtendedBaseEntity implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 117576567820385047L;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    protected Integer txVersion = 0;

    /**
     * фамилия
     */
    private String surname;

    /**
     * имя
     */
    private String name;

    /**
     * отчество
     */
    private String midname;

    /**
     * девичья фамилия, не используется
     */
    private String maidenname;

    /**
     * пол
     */
    private ReferenceEntity gender;

    /**
     * дата рождения
     */
    private Date birthdate;

    /**
     * место рождения
     */
    private String birthplace;

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
     * была ли выгружена запись
     */
    private Boolean isUploaded;

    /**
     * гражданство
     */
    private CountryEntity citizen;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * источник информации (партнер)
     */
    private PartnersEntity partnersId;

    /**
     * Заявка
     */
    private CreditRequestEntity creditRequestId;

    @Transient
    private boolean isDirty = false;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (!Utils.equalsNull(this.surname, surname)) {
            this.surname = surname;
            isDirty = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Utils.equalsNull(this.name, name)) {
            this.name = name;
            isDirty = true;
        }
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        if (!Utils.equalsNull(this.midname, midname)) {
            this.midname = midname;
            isDirty = true;
        }
    }

    public String getMaidenname() {
        return maidenname;
    }

    public void setMaidenname(String maidenname) {
        this.maidenname = maidenname;
    }

    public ReferenceEntity getGender() {
        return gender;
    }

    public void setGender(ReferenceEntity gender) {
        if (!Utils.equalsNull(this.gender, gender)) {
            this.gender = gender;
            isDirty = true;
        }
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        if (!Utils.equalsNull(this.birthdate, birthdate)) {
            this.birthdate = birthdate;
            isDirty = true;
        }
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        if (!Utils.equalsNull(this.birthplace, birthplace)) {
            this.birthplace = birthplace;
            isDirty = true;
        }
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
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

    public CountryEntity getCitizen() {
        return citizen;
    }

    public void setCitizen(CountryEntity citizen) {
        this.citizen = citizen;
    }

    @Override
    public String toString() {
        return getInintials();
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

    public boolean isDirty() {
        return isDirty;
    }

    public PeoplePersonalEntity clean() {
        this.isDirty = false;
        return this;
    }

    public String getDescription() {
        String birthday = birthdate != null ? sdf.format(birthdate) : "";
        return surname.trim() + " " + name.trim() + " " + (StringUtils.isEmpty(midname) ? "" : midname.trim()) +
                ", дата рождения " + birthday;
    }

    public String getIO() {
        return name.trim() + " " + (StringUtils.isEmpty(midname) ? "" : midname.trim());
    }

    public String getFullName() {
        return (StringUtils.isEmpty(surname) ? "" :surname.trim()) + " " + (StringUtils.isEmpty(name) ? "" :name.trim()) + " " + (StringUtils.isEmpty(midname) ? "" : midname.trim());
    }
    
    public String getInintials() {
        String st = "";
        if (StringUtils.isNotEmpty(surname)) {
            st += surname;
        }
        if (StringUtils.isNotEmpty(name)) {
            st += " " + name.substring(0, 1) + ".";
        }
        if (StringUtils.isNotEmpty(midname)) {
            st += " " + midname.substring(0, 1) + ".";
        }
        return st;
    }

    public PeoplePersonalEntity archive() {
        this.isactive = 0;
        dataend = new Date();
        return this;
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        PeoplePersonalEntity ent = (PeoplePersonalEntity) other;
        return Utils.equalsNull(birthplace, ent.getBirthplace()) &&
                Utils.equalsNull(birthdate.getTime(), ent.getBirthdate().getTime()) &&
                Utils.equalsNull(gender, ent.getGender()) && Utils.equalsNull(midname, ent.getMidname()) &&
                Utils.equalsNull(name, ent.getName()) && Utils.equalsNull(surname, ent.getSurname());
    }
}

