package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Точки Контакта
 */
public class ContactPointsEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer txVersion = 0;
	private Integer id;
	private Integer parentId;
	private Integer version;
	private Integer erased;
	/**
	 * код точки
	 */
	private String ppCode;
	/**
	 * свифт
	 */
	private String bic;
	/**
	 * название точки
	 */
	private String name;
	/**
	 * город
	 */
	private String cityHead;
	/**
	 * адрес
	 */
	private String address1;
	/**
	 * часы работы
	 */
	private String address2;
	/**
	 * выходные
	 */
	private String address3;
	private String address4;
	/**
	 * телефон
	 */
	private String phone;
	/**
	 * название по-русски
	 */
	private String nameRus;
	/**
	 * страна
	 */
	private Integer country;
	private Integer deleted;
	/**
	 * город по латыни
	 */
	private String cityLat;
	/**
	 * адрес по латыни
	 */
	private String addrLat;
	/**
	 * контакт
	 */
	private Integer contact;
	/**
	 * регион
	 */
	private Integer region;
	private Integer canRevoke;
	private Integer canChange;
	private Integer getMoney;
	private String sendCurr;
	private String recCurr;
	private String attrGrps;
	private Integer cityId;
	private Integer logoId;
	private Integer scenId;
	private Integer uniqueTrn;
	private Integer metro;
      
    public ContactPointsEntity() {
	
	}

    public ContactPointsEntity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
	@Override
	public int hashCode() {
		int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
	}

	@Override
	public boolean equals(Object other) {
		   if (other == null) return false;
	       
    	    if (other == this) return true;
    	       
    	    if (! (other.getClass() ==  getClass()))
    	    	return false;
    	    
    	    ContactPointsEntity cast = (ContactPointsEntity) other;
    	    
    	    if (this.id == null) return false;
    	       
    	    if (cast.getId() == null) return false;       
    	       
    	    if (this.id.intValue() != cast.getId().intValue())
    	    	return false;
    	    
    	    return true;
	}

	

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getErased() {
		return erased;
	}

	public void setErased(Integer erased) {
		this.erased = erased;
	}

	public String getPpCode() {
		return ppCode;
	}

	public void setPpCode(String ppCode) {
		this.ppCode = ppCode;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityHead() {
		return cityHead;
	}

	public void setCityHead(String cityHead) {
		this.cityHead = cityHead;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNameRus() {
		return nameRus;
	}

	public void setNameRus(String nameRus) {
		this.nameRus = nameRus;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getCityLat() {
		return cityLat;
	}

	public void setCityLat(String cityLat) {
		this.cityLat = cityLat;
	}

	public String getAddrLat() {
		return addrLat;
	}

	public void setAddrLat(String addrLat) {
		this.addrLat = addrLat;
	}

	public Integer getContact() {
		return contact;
	}

	public void setContact(Integer contact) {
		this.contact = contact;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Integer getCanRevoke() {
		return canRevoke;
	}

	public void setCanRevoke(Integer canRevoke) {
		this.canRevoke = canRevoke;
	}

	public Integer getCanChange() {
		return canChange;
	}

	public void setCanChange(Integer canChange) {
		this.canChange = canChange;
	}

	public Integer getGetMoney() {
		return getMoney;
	}

	public void setGetMoney(Integer getMoney) {
		this.getMoney = getMoney;
	}

	public String getSendCurr() {
		return sendCurr;
	}

	public void setSendCurr(String sendCurr) {
		this.sendCurr = sendCurr;
	}

	public String getRecCurr() {
		return recCurr;
	}

	public void setRecCurr(String recCurr) {
		this.recCurr = recCurr;
	}

	public String getAttrGrps() {
		return attrGrps;
	}

	public void setAttrGrps(String attrGrps) {
		this.attrGrps = attrGrps;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getLogoId() {
		return logoId;
	}

	public void setLogoId(Integer logoId) {
		this.logoId = logoId;
	}

	public Integer getScenId() {
		return scenId;
	}

	public void setScenId(Integer scenId) {
		this.scenId = scenId;
	}

	public Integer getUniqueTrn() {
		return uniqueTrn;
	}

	public void setUniqueTrn(Integer uniqueTrn) {
		this.uniqueTrn = uniqueTrn;
	}

	public Integer getMetro() {
		return metro;
	}

	public void setMetro(Integer metro) {
		this.metro = metro;
	}

   public String getDescription(){
	   String descr="";
	   if (StringUtils.isNotEmpty(address1)){
		   descr+="адрес: "+address1;
	   }
	   if (StringUtils.isNotEmpty(phone)){
		   descr+=", тел: "+phone;
	   }
	   if (StringUtils.isNotEmpty(address2)){
		   descr+=", часы работы: "+address2;
	   }
	   if (StringUtils.isNotEmpty(address3)){
		   descr+=", "+address3;
	   }
	   return descr;
   }
}
