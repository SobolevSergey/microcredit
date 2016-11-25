package ru.simplgroupp.fias.persistence;

import java.io.Serializable;
import java.util.Arrays;

import ru.simplgroupp.fias.persistence.Level;
import org.apache.commons.lang.StringUtils;

public class Address implements Serializable, IAddress 
{
	private static final long serialVersionUID = 4887908280603848348L;
	
	private Integer ID;
	private String fiasGUID;
	private String house;
	private String liter;
	private String corpus;
	private String building;
	private String flat;
	private String regionGUID;
	private String autoGUID;
	private String areaGUID;
	private String cityGUID;
	private String ctarGUID;
	private String placeGUID;
	private String streetGUID;
	private String extrGUID;
	private String sextGUID;
	private String regionName;
	private String autoName;
	private String areaName;
	private String cityName;
	private String ctarName;
	private String placeName;
	private String streetName;
	private String extrName;
	private String sextName;
	private String regionCode;
	private String autoCode;
	private String areaCode;
	private String cityCode;
	private String ctarCode;
	private String placeCode;
	private String streetCode;
	private String extrCode;
	private String sextCode;
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    Address cast = (Address) other;
	    
	    if (this.ID == null) return false;
	       
	    if (cast.getID() == null) return false;       
	       
	    if (this.ID.intValue() != cast.getID().intValue())
	    	return false;
	    
	    return true;
	}
	@Override
	public int hashCode() {
		int _hashCode = 0, aid = 0;
	    if (ID != null) aid = ID.intValue();
	    _hashCode = 29 * _hashCode + (new Long(aid)).hashCode();
	    return _hashCode;
	}
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getFiasGUID() {
		return fiasGUID;
	}
	public void setFiasGUID(String fiasGUID) {
		this.fiasGUID = fiasGUID;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getLiter() {
		return liter;
	}
	public void setLiter(String liter) {
		this.liter = liter;
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
	public String getRegionGUID() {
		return regionGUID;
	}
	public void setRegionGUID(String regionGUID) {
		this.regionGUID = regionGUID;
	}
	public String getAutoGUID() {
		return autoGUID;
	}
	public void setAutoGUID(String autoGUID) {
		this.autoGUID = autoGUID;
	}
	public String getAreaGUID() {
		return areaGUID;
	}
	public void setAreaGUID(String areaGUID) {
		this.areaGUID = areaGUID;
	}
	public String getCityGUID() {
		return cityGUID;
	}
	public void setCityGUID(String cityGUID) {
		this.cityGUID = cityGUID;
	}
	public String getCtarGUID() {
		return ctarGUID;
	}
	public void setCtarGUID(String ctarGUID) {
		this.ctarGUID = ctarGUID;
	}
	public String getPlaceGUID() {
		return placeGUID;
	}
	public void setPlaceGUID(String placeGUID) {
		this.placeGUID = placeGUID;
	}
	public String getStreetGUID() {
		return streetGUID;
	}
	public void setStreetGUID(String streetGUID) {
		this.streetGUID = streetGUID;
	}
	public String getExtrGUID() {
		return extrGUID;
	}
	public void setExtrGUID(String extrGUID) {
		this.extrGUID = extrGUID;
	}
	public String getSextGUID() {
		return sextGUID;
	}
	public void setSextGUID(String sextGUID) {
		this.sextGUID = sextGUID;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getAutoName() {
		return autoName;
	}
	public void setAutoName(String autoName) {
		this.autoName = autoName;
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
	public String getCtarName() {
		return ctarName;
	}
	public void setCtarName(String ctarName) {
		this.ctarName = ctarName;
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
	public String getExtrName() {
		return extrName;
	}
	public void setExtrName(String extrName) {
		this.extrName = extrName;
	}
	public String getSextName() {
		return sextName;
	}
	public void setSextName(String sextName) {
		this.sextName = sextName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getAutoCode() {
		return autoCode;
	}
	public void setAutoCode(String autoCode) {
		this.autoCode = autoCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCtarCode() {
		return ctarCode;
	}
	public void setCtarCode(String ctarCode) {
		this.ctarCode = ctarCode;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public String getStreetCode() {
		return streetCode;
	}
	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}
	public String getExtrCode() {
		return extrCode;
	}
	public void setExtrCode(String extrCode) {
		this.extrCode = extrCode;
	}
	public String getSextCode() {
		return sextCode;
	}
	public void setSextCode(String sextCode) {
		this.sextCode = sextCode;
	}
	
	public LevelRecord[] toLevels() 
	{
		return Utils.toLevels(this);
	}	
	
	public void fromLevels(LevelRecord[] source)
	{	
		Utils.fromLevels(source, this);
	}	
	
	/**
	 * Дом + корпус + литера
	 * @return
	 */
	public String getHouseCaption() {
		return Utils.getHouseCaption(this);
	}	
	
	/**
	 * Дом + корпус + литера + квартира
	 * @return
	 */
	public String getHouseFlatCaption() {
		return Utils.getHouseFlatCaption(this);
	}
	
	public String getStreetCaption() {
		return Utils.getStreetCaption(this);
	}
	@Override
	public void clearAddress() {
		Utils.clearAddress(this);
		
	}	
}
