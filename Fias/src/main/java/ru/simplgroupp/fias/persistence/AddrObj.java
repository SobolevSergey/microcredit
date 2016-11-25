package ru.simplgroupp.fias.persistence;

import java.io.Serializable;
import java.util.Date;

public class AddrObj implements Serializable {

  private static final long serialVersionUID = -8667148619828875419L;

  private String ID;
  private String AOGUID;
  private String formalName;
  private String regionCode;
  private String autoCode;
  private String areaCode;
  private String cityCode;
  private String ctarCode;
  private String placeCode;
  private String streetCode;
  private String extrCode;
  private String sextCode;
  private String officialName;
  private String postalCode;
  private String okato;
  private String oktmo;
  private Date updateDate;
  private String shortName;
  private Integer aoLevel;
  private String parentAOGUID;
  private String prevId;
  private String nextId;
  private String kladrCode;
  private String plainKladrCode;
  private Integer actualStatus;
  private Integer centerStatus;
  private Integer operStatus;
  private Integer currStatus;
  private Date startDate;
  private Date endDate;
  private Integer liveStatus;

  private AddrObjType type; // Для чего это поле?

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getAOGUID() {
    return AOGUID;
  }

  public void setAOGUID(String AOGUID) {
    this.AOGUID = AOGUID;
  }

  public String getFormalName() {
    return formalName;
  }

  public void setFormalName(String formalName) {
    this.formalName = formalName;
  }

  public String getOfficialName() {
    return officialName;
  }

  public void setOfficialName(String officialName) {
    this.officialName = officialName;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public Integer getActualStatus() {
    return actualStatus;
  }

  public void setActualStatus(Integer actualStatus) {
    this.actualStatus = actualStatus;
  }

  public Integer getCenterStatus() {
    return centerStatus;
  }

  public void setCenterStatus(Integer centerStatus) {
    this.centerStatus = centerStatus;
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

  public String getCtarCode() {
    return ctarCode;
  }

  public void setCtarCode(String ctarCode) {
    this.ctarCode = ctarCode;
  }

  public String getParentAOGUID() {
    return parentAOGUID;
  }

  public void setParentAOGUID(String parentAOGUID) {
    this.parentAOGUID = parentAOGUID;
  }

  public String getOktmo() {
    return oktmo;
  }

  public void setOktmo(String oktmo) {
    this.oktmo = oktmo;
  }

  public String getKladrCode() {
    return kladrCode;
  }

  public void setKladrCode(String kladrCode) {
    this.kladrCode = kladrCode;
  }

  public String getPlainKladrCode() {
    return plainKladrCode;
  }

  public void setPlainKladrCode(String plainKladrCode) {
    this.plainKladrCode = plainKladrCode;
  }

  public String getOkato() {
    return okato;
  }

  public void setOkato(String okato) {
    this.okato = okato;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public Integer getAoLevel() {
    return aoLevel;
  }

  public void setAoLevel(Integer aoLevel) {
    this.aoLevel = aoLevel;
  }

  public String getPrevId() {
    return prevId;
  }

  public void setPrevId(String prevId) {
    this.prevId = prevId;
  }

  public String getNextId() {
    return nextId;
  }

  public void setNextId(String nextId) {
    this.nextId = nextId;
  }

  public Integer getOperStatus() {
    return operStatus;
  }

  public void setOperStatus(Integer operStatus) {
    this.operStatus = operStatus;
  }

  public Integer getCurrStatus() {
    return currStatus;
  }

  public void setCurrStatus(Integer currStatus) {
    this.currStatus = currStatus;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getLiveStatus() {
    return liveStatus;
  }

  public void setLiveStatus(Integer liveStatus) {
    this.liveStatus = liveStatus;
  }

  public AddrObjType getType() {
    return type;
  }

  public void setType(AddrObjType type) {
    this.type = type;
  }

  /**
   * Код адресного объекта в зависимости от его уровня
   *
   * @return
   */
  public String getCode() {
    if (getType().getLevel() == Level.REGION.getID()) {
      return regionCode;
    }
    if (getType().getLevel() == Level.AUTO_OKRUG.getID()) {
      return autoCode;
    }
    if (getType().getLevel() == Level.RAION.getID()) {
      return areaCode;
    }
    if (getType().getLevel() == Level.CITY.getID()) {
      return cityCode;
    }
    if (getType().getLevel() == Level.CITY_TERR.getID()) {
      return ctarCode;
    }
    if (getType().getLevel() == Level.NP.getID()) {
      return placeCode;
    }
    if (getType().getLevel() == Level.STREET.getID()) {
      return streetCode;
    }
    if (getType().getLevel() == Level.STREET.getID()) {
      return streetCode;
    }
    if (getType().getLevel() == Level.DOP_TERR.getID()) {
      return extrCode;
    }
    if (getType().getLevel() == Level.DOP_DOP_TERR.getID()) {
      return sextCode;
    }
    return null;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    if (!(other.getClass() == getClass())) {
      return false;
    }
    AddrObj cast = (AddrObj) other;
    if (this.ID == null) {
      return false;
    }
    if (cast.getID() == null) {
      return false;
    }
    if (!this.ID.equals(cast.getID())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return ID.hashCode();
  }

}
