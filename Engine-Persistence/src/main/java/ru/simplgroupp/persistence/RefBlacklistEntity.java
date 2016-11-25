package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
/**
 * справочник черного списка
 *
 */
public class RefBlacklistEntity extends BaseEntity implements Serializable , Identifiable, Initializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -45974024539796209L;

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
     * девичья фамилия
     */
    private String maidenname;
    /**
     * дата рождения
     */
    private Date birthdate;
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
     * серия
     */
    private String series;
    /**
     * номер
     */
    private String number;
    /**
     * причина
     */
    private ReferenceEntity reasonId;
    /**
     * источник информации
     */
    private ReferenceEntity sourceId;
    /**
     * телефон
     */
    private String mobilePhone;
    /**
     * email
     */
    private String email;
    /**
     * комментарий
     */
    private String comment;
    /**
     * полное название работодателя
     */
    private String employerFullName;
    /**
     * краткое название работодателя
     */
    private String employerShortName;
    /**
     * регион
     */
    private String regionName;
    /**
     * сельский район
     */
    private String areaName;
    /**
     * город
     */
    private String cityName;
    /**
     * село
     */
    private String placeName;
    /**
     * улица
     */
    private String streetName;
    /**
     * дом
     */
    private String house;
    /**
     * корпус
     */
    private String corpus;
    /**
     * строение
     */
    private String building;
    /**
     * квартира
     */
    private String flat;

	/**
	 * название файла ЧС, из которого эти данные загружали
	 */
	private String fileName;
    
    public RefBlacklistEntity() {
	
	}
    
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMidname() {
		return midname;
	}

	public void setMidname(String midname) {
		this.midname = midname;
	}

	public String getMaidenname() {
		return maidenname;
	}

	public void setMaidenname(String maidenname) {
		this.maidenname = maidenname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
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

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
  
	public ReferenceEntity getReasonId() {
	    return reasonId;
	}

	public void setReasonId(ReferenceEntity reasonId) {
	    this.reasonId = reasonId;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public ReferenceEntity getSourceId() {
		return sourceId;
	}

	public void setSourceId(ReferenceEntity sourceId) {
		this.sourceId = sourceId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEmployerFullName() {
		return employerFullName;
	}

	public void setEmployerFullName(String employerFullName) {
		this.employerFullName = employerFullName;
	}

	public String getEmployerShortName() {
		return employerShortName;
	}

	public void setEmployerShortName(String employerShortName) {
		this.employerShortName = employerShortName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * возвращает описание адреса из ЧС
	 * @return
	 */
	public String getAddressDescription(){
		String st="";
		if (StringUtils.isNotEmpty(this.regionName)){
			st+="регион "+this.regionName;
		}
		if (StringUtils.isNotEmpty(this.areaName)){
			st+=", сельский район "+this.areaName;
		}
		if (StringUtils.isNotEmpty(this.cityName)){
			st+=", город "+this.cityName;
		}
		if (StringUtils.isNotEmpty(this.placeName)){
			st+=", село "+this.placeName;
		}
		if (StringUtils.isNotEmpty(this.streetName)){
			st+=", улица "+this.streetName;
		}
		if (StringUtils.isNotEmpty(this.house)){
			st+=", дом "+this.house;
		}
		if (StringUtils.isNotEmpty(this.corpus)){
			st+=", корпус "+this.corpus;
		}
		if (StringUtils.isNotEmpty(this.building)){
			st+=", строение "+this.building;
		}
		if (StringUtils.isNotEmpty(this.flat)){
			st+=", квартира "+this.flat;
		}
		return st;
	}
	@Override
	public void init(Set options) {
				
	}
}
