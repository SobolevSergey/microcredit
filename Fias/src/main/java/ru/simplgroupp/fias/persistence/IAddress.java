package ru.simplgroupp.fias.persistence;

public interface IAddress {
	public Integer getID();
	
	public void setID(Integer iD);
	
	public String getHouse();
	public void setHouse(String house);
	public String getLiter();
	public void setLiter(String liter);
	public String getCorpus();
	public void setCorpus(String corpus);
	public String getBuilding();
	public void setBuilding(String building);
	public String getFlat();
	public void setFlat(String flat);
	public String getRegionGUID();
	public void setRegionGUID(String regionGUID);
	public String getAutoGUID();
	public void setAutoGUID(String autoGUID);
	public String getAreaGUID();
	public void setAreaGUID(String areaGUID);
	public String getCityGUID();
	public void setCityGUID(String cityGUID);
	public String getCtarGUID();
	public void setCtarGUID(String ctarGUID);
	public String getPlaceGUID();
	public void setPlaceGUID(String placeGUID);
	public String getStreetGUID();
	public void setStreetGUID(String streetGUID);
	public String getExtrGUID();
	public void setExtrGUID(String extrGUID);
	public String getSextGUID();
	public void setSextGUID(String sextGUID);
	public String getRegionName();
	public void setRegionName(String regionName);
	public String getAutoName();
	public void setAutoName(String autoName);
	public String getAreaName();
	public void setAreaName(String areaName);
	public String getCityName();
	public void setCityName(String cityName);
	public String getCtarName();
	public void setCtarName(String ctarName);
	public String getPlaceName();
	public void setPlaceName(String placeName);
	public String getStreetName();
	public void setStreetName(String streetName);
	public String getExtrName();
	public void setExtrName(String extrName);
	public String getSextName();
	public void setSextName(String sextName);
	public String getRegionCode();
	public void setRegionCode(String regionCode);
	public String getAutoCode();
	public void setAutoCode(String autoCode);
	public String getAreaCode();
	public void setAreaCode(String areaCode);
	public String getCityCode();
	public void setCityCode(String cityCode);
	public String getCtarCode();
	public void setCtarCode(String ctarCode);
	public String getPlaceCode();
	public void setPlaceCode(String placeCode);
	public String getStreetCode();
	public void setStreetCode(String streetCode);
	public String getExtrCode();
	public void setExtrCode(String extrCode);
	public String getSextCode();
	public void setSextCode(String sextCode);
	public LevelRecord[] toLevels();
	public void fromLevels(LevelRecord[] source);
	public void clearAddress();
	
	/**
	 * Дом + корпус + литера
	 * @return
	 */
	public String getHouseCaption();	
	
	/**
	 * Дом + корпус + литера + квартира
	 * @return
	 */
	public String getHouseFlatCaption();
	
	public String getStreetCaption();
}
