package ru.simplgroupp.persistence;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.toolkit.common.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
/**
 * Адреса
 */
@XmlRootElement
public class AddressEntity extends ExtendedBaseEntity implements Serializable {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -5549318813737357298L;
	
	protected Integer txVersion = 0;
    private Integer id;
    /**
     * почтовый индекс
     */
    private String index;
    /**
     * Адрес текстом
     */
    private String addressText;
    /**
     * регион
     */
    private String regionName;
    private String region;
    /**
     * автономия
     */
    private String autoName;
    private String auto;    
    /**
     * район сельский
     */
    private String area;
    private String areaName;
    private String areaExt;
    /**
     * село, деревня
     */
    private String place;
    private String placeName;
    private String placeExt;
    /**
     * город
     */
    private String city;
    private String cityName;
    private String cityExt;
    /**
     * район города
     */
    private String district;
    private String districtName;
    /**
     * улица
     */
    private String street;
    private String streetName;
    private String streetExt;
    /**
     * доп.данные
     */
    private String extr;
    private String extrName;
    private String sext;
    private String sextName; 
    /**
     * дом
     */
    private String house;
    /**
     * буква
     */
    private String liter;
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
     * совпадает с адресом регистрации
     */
    private Integer isSame;
    /**
     * тип адреса
     */
    private ReferenceEntity addrtype;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    /**
     * партнер
     */
    private PartnersEntity partnersId;
    /**
     * активная запись или нет
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
     * страна
     */
    private CountryEntity country;
    /**
     * код региона
     */
    private RegionsEntity regionShort;
    /**
     * код Классификатор административно-территориальных объектов Республики Казахстан
     */
    private String kzegovdataCato;
    /**
     * ID Ats (Административно-территориальное устройство Республики Казахстан) конечной части адреса
     */
    private Long kzegovdataAts;
    /**
     * ID Geonims (Устройство населенных пунктов Республики Казахстан) конечной части адреса
     */
    private Long kzegovdataGeonims;
    /**
     * адрес текстом до уровня улицы
     */
    private String addressTextToStreet;


    public AddressEntity() {
    	super();
    }
    
    public AddressEntity(Integer id) {
    	super();
    	this.id = id;
    }    
    
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public RegionsEntity getRegionShort() {
        return regionShort;
    }

    public void setRegionShort(RegionsEntity regionShort) {
        this.regionShort = regionShort;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaExt() {
        return areaExt;
    }

    public void setAreaExt(String areaExt) {
        this.areaExt = areaExt;
    }
    
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceExt() {
        return placeExt;
    }

    public void setPlaceExt(String placeExt) {
        this.placeExt = placeExt;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityExt() {
        return cityExt;
    }

    public void setCityExt(String cityExt) {
        this.cityExt = cityExt;
    }
    
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetExt() {
        return streetExt;
    }

    public void setStreetExt(String streetExt) {
        this.streetExt = streetExt;
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

    public Integer getIsSame() {
    	return isSame;
    }
    
    public void setIsSame(Integer isSame) {
    	this.isSame=isSame;
    }
    
    public ReferenceEntity getAddrtype() {
        return addrtype;
    }

    public void setAddrtype(ReferenceEntity addrtype) {
        this.addrtype = addrtype;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    /**
     * возвращает название, которое нужно записать в поле город, если это поле обязательное
     * @return
     */
    public String getMandatoryCity(){
    	if (StringUtils.isNotEmpty(city)){
    		return cityName;
    	} else if (StringUtils.isNotEmpty(place)){
    		return placeName;
    	} else if (StringUtils.isNotEmpty(area)){
    		return areaName;
    	} else{
    		return regionName;
    	}
    }
    /**
     * адрес, начиная с улицы
     * @return
     */
    public String getDescriptionFromStreet(){
    	
     String st="";
     if (StringUtils.isNotEmpty(street)){
   	   if (StringUtils.isEmpty(streetExt))
   	  	   st+=streetName;
   		else
   		   st+=streetExt+" "+streetName;
   	  }
   	  if (StringUtils.isNotEmpty(house))
   		  st+=", дом "+house;
   	  if (StringUtils.isNotEmpty(corpus))
   		  st+=", корпус "+corpus;
   	  if (StringUtils.isNotEmpty(building))
   		  st+=", строение "+building;
   	  if (StringUtils.isNotEmpty(flat))
   		  st+=", квартира "+flat;
   	
   	  return st;
    }
    
    /**
     * адрес до улицы включительно
     * @return
     */
    public String getDescriptionToStreet() {
        StringBuilder st = new StringBuilder(getDescriptionToCity());
        if (StringUtils.isNotEmpty(streetName)) {
            if (st.length() > 0) {
                st.append(", ");
            }
            if (StringUtils.isEmpty(streetExt)) {
                st.append(streetName);
            } else {
                st.append(streetExt).append(" ").append(streetName);
            }
        }
        return st.toString();
   }
    
    /**
     * адрес до улицы, не включая ее
     * @return
     */
    public String getDescriptionToCity() {
        StringBuilder st = new StringBuilder();
        if (StringUtils.isNotEmpty(index)) {
            st.append(index);
        }

        if (StringUtils.isNotEmpty(regionName)) {
            if (st.length() > 0) {
                st.append(", ");
            }
            st.append(regionName);
        }
  	  if (StringUtils.isNotEmpty(areaName)){
          if (st.length() > 0) {
              st.append(", ");
          }
          if (StringUtils.isEmpty(areaExt)) {
  			  st.append(areaName);
  		  } else {
  		      st.append(areaExt).append(" ").append(areaName);
  		  }
  	  }
  	  if (StringUtils.isNotEmpty(cityName)){
          if (st.length() > 0) {
              st.append(", ");
          }
  		  if (StringUtils.isEmpty(cityExt)) {
  			  st.append(cityName);
  		  } else {
  		      st.append(cityExt).append(" ").append(cityName);
  		  }
  	  }
  	  if (StringUtils.isNotEmpty(placeName)){
          if (st.length() > 0) {
              st.append(", ");
          }
  		  if (StringUtils.isEmpty(placeExt)) {
  			  st.append(placeName);
  		  } else {
  		      st.append(placeExt).append(" ").append(placeName);
  		  }
  	  }
  	 
  	  return st.toString();
   }
    
    /**
     * полный адрес
     * @return
     */
    public String getDescription()
    {
    	if (StringUtils.isNotEmpty(addressText)) {
    		return addressText;
    	} else	{
      	  
    	  String st=getDescriptionToStreet();

          st += getFromHouseText();

    	  return st;
    	}
    }

    /**
     * часть адреса от номера дома
     * @return
     */
    public String getFromHouseText()
    {

        String st = "";
        if (StringUtils.isNotEmpty(house))
            st+=", дом "+house;
        if (StringUtils.isNotEmpty(corpus))
            st+=", корпус "+corpus;
        if (StringUtils.isNotEmpty(building))
            st+=", строение "+building;
        if (StringUtils.isNotEmpty(flat))
            st+=", квартира "+flat;

        return st;
    }

    
    /**
     * полный адрес с полным описанием
     * @return
     */
    public String getDescriptionFull()
    {
    	if (StringUtils.isNotEmpty(addressText)){
    		return addressText;
    	} else 	{
      	  String st="регион - "+regionName+",";
    	  if (StringUtils.isNotEmpty(area)){
    		  st+=" сельский район - "+areaName+",";
    	  }
    	  if (StringUtils.isNotEmpty(city)){
    		  st+=" город - "+cityName+",";
    	  }
    	  if (StringUtils.isNotEmpty(place)){
    		  st+=" населенный пункт - "+placeName+",";
    	  }
    	  if (StringUtils.isNotEmpty(street)){
    		  st+=" улица - "+streetName;
    	  }
    	  if (StringUtils.isNotEmpty(house)){
    		  st+=" дом "+house+",";
    	  }
    	  if (StringUtils.isNotEmpty(corpus)){
    		  st+=" корпус "+corpus+",";
    	  }
    	  if (StringUtils.isNotEmpty(building)){
    		  st+=" строение "+building+",";
    	  }
    	  if (StringUtils.isNotEmpty(flat)){
    		  st+=" квартира "+flat;
    	  }
    	
    	  return st;
    	}
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
  	    
  	    AddressEntity cast = (AddressEntity) other;
  	    
  	    if (this.id == null) return false;
  	       
  	    if (cast.getId() == null) return false;       
  	       
  	    if (this.id.intValue() != cast.getId().intValue())
  	    	return false;
  	    
  	    return true;
    }

    @Override
    public String toString() {
    	return getDescription();
    }
    
    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }
 
    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
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
    
    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }
    
	@Override
	public Boolean equalsContent(BaseEntity other) {
		AddressEntity ent=(AddressEntity) other;
		return Utils.equalsNull(auto, ent.auto) && Utils.equalsNull(area, ent.area) 
				&& Utils.equalsNull(building, ent.building) && Utils.equalsNull(city, ent.city) 
				&& Utils.equalsNull(corpus, ent.corpus) && Utils.equalsNull(district, ent.district) 
				&& Utils.equalsNull(flat, ent.flat) && Utils.equalsNull(house, ent.house) 
				&& Utils.equalsNull(index, ent.index) && Utils.equalsNull(liter, ent.liter) 
				&& Utils.equalsNull(place, ent.place) && Utils.equalsNull(region, ent.region) 
				&& Utils.equalsNull(street, ent.street) && Utils.equalsNull(extr, ent.extr) 
				&& Utils.equalsNull(sext, ent.sext) && Utils.equalsNull(kzegovdataCato, ent.kzegovdataCato)
                && Utils.equalsNull(kzegovdataAts, ent.kzegovdataAts) && Utils.equalsNull(kzegovdataGeonims, ent.kzegovdataGeonims);
	}

	public String getLiter() {
		return liter;
	}

	public void setLiter(String liter) {
		this.liter = liter;
	}

	public String getAutoName() {
		return autoName;
	}

	public void setAutoName(String autoName) {
		this.autoName = autoName;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getExtr() {
		return extr;
	}

	public void setExtr(String extr) {
		this.extr = extr;
	}

	public String getExtrName() {
		return extrName;
	}

	public void setExtrName(String extrName) {
		this.extrName = extrName;
	}

	public String getSext() {
		return sext;
	}

	public void setSext(String sext) {
		this.sext = sext;
	}

	public String getSextName() {
		return sextName;
	}

	public void setSextName(String sextName) {
		this.sextName = sextName;
	}

    public String getKzegovdataCato() {
        return kzegovdataCato;
    }

    public void setKzegovdataCato(String kzegovdataCato) {
        this.kzegovdataCato = kzegovdataCato;
    }

    public String getAddressTextToStreet() {
        return addressTextToStreet;
    }

    public void setAddressTextToStreet(String addressTextToStreet) {
        this.addressTextToStreet = addressTextToStreet;
    }
    public Long getKzegovdataAts() {
        return kzegovdataAts;
    }

    public void setKzegovdataAts(Long kzegovdataAts) {
        this.kzegovdataAts = kzegovdataAts;
    }

    public Long getKzegovdataGeonims() {
        return kzegovdataGeonims;
    }

    public void setKzegovdataGeonims(Long kzegovdataGeonims) {
        this.kzegovdataGeonims = kzegovdataGeonims;
    }
}
