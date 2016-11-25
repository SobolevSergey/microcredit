package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.RefBlacklistEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class RefBlacklist extends BaseTransfer<RefBlacklistEntity> implements Serializable, Initializable, Identifiable {
    public static final int SOURCE_VERIFIER = 1;
    public static final int SOURCE_COLLECTOR = 2;
    public static final int SOURCE_SYSTEM = 3;
    public static final int SOURCE_EXTERNAL_BLACK_LISTS = 4;
    public static final int SOURCE_NATIONAL_HUNTER = 5;
    public static final int SOURCE_OTHER = 6;

    private static final long serialVersionUID = -1318928279011124478L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = RefBlacklist.class.getConstructor(RefBlacklistEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private Reference reason;

    private Reference source;


    public RefBlacklist() {
        super();
        entity = new RefBlacklistEntity();
    }

    public RefBlacklist(RefBlacklistEntity value) {
        super(value);


        if (entity.getReasonId() != null) {
            reason = new Reference(entity.getReasonId());
        }

        if (entity.getSourceId() != null) {
            source = new Reference(entity.getSourceId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
    }

    public Integer getId() {
        return entity.getId();
    }

    public Reference getReason() {
        return reason;
    }

    public void setReason(Reference reason) {
        this.reason = reason;
    }

    public Reference getSource() {
        return source;
    }

    public void setSource(Reference source) {
        this.source = source;
    }

    public String getSurname() {
        return entity.getSurname();
    }

    public void setSurname(String surname) {
        entity.setSurname(surname);
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public String getMidname() {
        return entity.getMidname();
    }

    public void setMidname(String midname) {
        entity.setMidname(midname);
    }

    public String getMaidenname() {
        return entity.getMaidenname();
    }

    public void setMaidenname(String maidenname) {
        entity.setMaidenname(maidenname);
    }

    public Date getBirthdate() {
        return entity.getBirthdate();
    }

    public void setBirthdate(Date birthdate) {
        entity.setBirthdate(birthdate);
    }

    public Integer getIsactive() {
        return entity.getIsactive();
    }

    public void setIsactive(Integer isactive) {
        entity.setIsactive(isactive);
    }

    public Date getDatabeg() {
        return entity.getDatabeg();
    }

    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDatabeg();
    }

    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }

    public String getSeries() {
        return entity.getSeries();
    }

    public void setSeries(String series) {
        entity.setSeries(series);
    }

    public String getNumber() {
        return entity.getNumber();
    }

    public void setNumber(String number) {
        entity.setNumber(number);
    }

    public String getMobilePhone() {
        return entity.getMobilePhone();
    }

    public void setMobilePhone(String mobilePhone) {
        entity.setMobilePhone(mobilePhone);
    }

    public String getEmail() {
        return entity.getEmail();
    }

    public void setEmail(String email) {
        entity.setEmail(email);
    }

    public String getComment() {
        return entity.getComment();
    }

    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public String getEmployerFullName() {
        return entity.getEmployerFullName();
    }

    public void setEmployerFullName(String employerFullName) {
        entity.setEmployerFullName(employerFullName);
    }

    public String getEmployerShortName() {
        return entity.getEmployerShortName();
    }

    public void setEmployerShortName(String employerShortName) {
        entity.setEmployerShortName(employerShortName);
    }

    public String getRegionName() {
        return entity.getRegionName();
    }

    public void setRegionName(String regionName) {
        entity.setRegionName(regionName);
    }

    public String getAreaName() {
        return entity.getAreaName();
    }

    public void setAreaName(String areaName) {
        entity.setAreaName(areaName);
    }

    public String getCityName() {
        return entity.getCityName();
    }

    public void setCityName(String cityName) {
        entity.setCityName(cityName);
    }

    public String getPlaceName() {
        return entity.getPlaceName();
    }

    public void setPlaceName(String placeName) {
        entity.setPlaceName(placeName);
    }

    public String getStreetName() {
        return entity.getStreetName();
    }

    public void setStreetName(String streetName) {
        entity.setStreetName(streetName);
    }

    public String getHouse() {
        return entity.getHouse();
    }

    public void setHouse(String house) {
        entity.setHouse(house);
    }

    public String getCorpus() {
        return entity.getCorpus();
    }

    public void setCorpus(String corpus) {
        entity.setCorpus(corpus);
    }

    public String getBuilding() {
        return entity.getBuilding();
    }

    public void setBuilding(String building) {
        entity.setBuilding(building);
    }

    public String getFlat() {
        return entity.getFlat();
    }

    public void setFlat(String flat) {
        entity.setFlat(flat);
    }

    public enum Options {
    }
}

