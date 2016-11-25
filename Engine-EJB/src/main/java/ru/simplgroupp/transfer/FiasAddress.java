package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.fias.persistence.IAddress;
import ru.simplgroupp.fias.persistence.LevelRecord;
import ru.simplgroupp.fias.persistence.Utils;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.RegionsEntity;

public class FiasAddress extends BaseAddress implements IAddress {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 597239275872803747L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = FiasAddress.class.getConstructor(AddressEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public FiasAddress() {
        super();
        entity = new AddressEntity();
    }

    public FiasAddress(AddressEntity entity) {
        super(entity);
    }

    @Override
    public void init(Set options) {
        entity.getHouse();
    }
    
    public String getRegionName() {
        return entity.getRegionName();
    }

    @XmlElement
    public void setRegionName(String regionName) {
        entity.setRegionName(regionName);
    }

    public String getRegion() {
        return entity.getRegion();
    }

    @XmlElement
    public void setRegion(String region) {
        entity.setRegion(region);
    }

    public String getArea() {
        return entity.getArea();
    }

    @XmlElement
    public void setArea(String area) {
        entity.setArea(area);
    }

    public String getAreaName() {
        return entity.getAreaName();
    }

    @XmlElement
    public void setAreaName(String areaName) {
        entity.setAreaName(areaName);
    }

    public String getAreaExt() {
        return entity.getAreaExt();
    }

    @XmlElement
    public void setAreaExt(String areaExt) {
        entity.setAreaExt(areaExt);
    }
    
    public String getPlace() {
        return entity.getPlace();
    }

    @XmlElement
    public void setPlace(String place) {
        entity.setPlace(place);
    }

    public String getPlaceName() {
        return entity.getPlaceName();
    }

    @XmlElement
    public void setPlaceName(String placeName) {
        entity.setPlaceName(placeName);
    }
    
    public String getPlaceExt() {
        return entity.getPlaceExt();
    }

    @XmlElement
    public void setPlaceExt(String placeExt) {
        entity.setPlaceExt(placeExt);
    }

    public String getCity() {
        return entity.getCity();
    }

    @XmlElement
    public void setCity(String city) {
        entity.setCity(city);
    }

    public String getCityName() {
        return entity.getCityName();
    }

    @XmlElement
    public void setCityName(String cityName) {
        entity.setCityName(cityName);
    }

    public String getCityExt() {
        return entity.getCityExt();
    }

    @XmlElement
    public void setCityExt(String cityExt) {
        entity.setCityExt(cityExt);
    }
    
    public String getDistrict() {
        return entity.getDistrict();
    }

    @XmlElement
    public void setDistrict(String district) {
        entity.setDistrict(district);
    }

    public String getDistrictName() {
        return entity.getDistrictName();
    }

    @XmlElement
    public void setDistrictName(String districtName) {
        entity.setDistrictName(districtName);
    }

    public String getStreet() {
        return entity.getStreet();
    }
    
    @XmlElement
    public void setStreet(String street) {
        entity.setStreet(street);
    }

    public String getStreetName() {
        return entity.getStreetName();
    }

    @XmlElement
    public void setStreetName(String streetName) {
        entity.setStreetName(streetName);
    }

    public String getStreetExt() {
        return entity.getStreetExt();
    }

    @XmlElement
    public void setStreetExt(String streetExt) {
        entity.setStreetExt(streetExt);
    }
    
    public String getHouse() {
        return entity.getHouse();
    }

    @XmlElement
    public void setHouse(String house) {
        entity.setHouse(house);
    }

    public String getCorpus() {
        return entity.getCorpus();
    }

    @XmlElement
    public void setCorpus(String corpus) {
        entity.setCorpus(corpus);
    }

    public String getBuilding() {
        return entity.getBuilding();
    }

    @XmlElement
    public void setBuilding(String building) {
        entity.setBuilding(building);
    }

    public String getFlat() {
        return entity.getFlat();
    }

    @XmlElement
    public void setFlat(String flat) {
        entity.setFlat(flat);
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
	public Integer getID() {
		return entity.getId();
	}

	@Override
	@XmlElement
	public void setID(Integer iD) {
		entity.setId(iD);
		
	}

	@Override
	public String getLiter() {
		return entity.getLiter();
	}

	@Override
	@XmlElement
	public void setLiter(String liter) {
		entity.setLiter(liter);		
	}

	@Override
	public String getRegionGUID() {
		return this.getRegion();
	}

	@Override
	@XmlElement
	public void setRegionGUID(String regionGUID) {
		this.setRegion(regionGUID);
	}

	@Override
	public String getAutoGUID() {
		return entity.getAuto();
	}

	@Override
	@XmlElement
	public void setAutoGUID(String autoGUID) {
		entity.setAuto(autoGUID);
	}

	@Override
	public String getAreaGUID() {
		return this.getArea();
	}

	@Override
	@XmlElement
	public void setAreaGUID(String areaGUID) {
		this.setArea(areaGUID);
	}

	@Override
	public String getCityGUID() {
		return this.getCity();
	}

	@Override
	@XmlElement
	public void setCityGUID(String cityGUID) {
		this.setCity(cityGUID);
	}

	@Override
	public String getCtarGUID() {
		return this.getDistrict();
	}

	@Override
	@XmlElement
	public void setCtarGUID(String ctarGUID) {
		this.setDistrict(ctarGUID);

	}

	@Override
	public String getPlaceGUID() {
		return this.getPlace();
	}

	@Override
	@XmlElement
	public void setPlaceGUID(String placeGUID) {
		this.setPlace(placeGUID);
	}

	@Override
	public String getStreetGUID() {
		return this.getStreet();
	}

	@Override
	@XmlElement
	public void setStreetGUID(String streetGUID) {
		this.setStreet(streetGUID);
		
	}

	@Override
	public String getExtrGUID() {
		return entity.getExtr();
	}

	@Override
	@XmlElement
	public void setExtrGUID(String extrGUID) {
		entity.setExtr(extrGUID);
	}

	@Override
	public String getSextGUID() {
		return entity.getSext();
	}

	@Override
	@XmlElement
	public void setSextGUID(String sextGUID) {
		entity.setSext(sextGUID);
	}

	@Override
	public String getAutoName() {
		return entity.getAutoName();
	}

	@Override
	@XmlElement
	public void setAutoName(String autoName) {
		entity.setAutoName(autoName);
		
	}

	@Override
	public String getCtarName() {
		return this.getDistrictName();
	}

	@Override
	@XmlElement
	public void setCtarName(String ctarName) {
		this.setDistrictName(ctarName);
		
	}

	@Override
	public String getExtrName() {
		return entity.getExtrName();
	}

	@Override
	@XmlElement
	public void setExtrName(String extrName) {
		entity.setExtrName(extrName);		
	}

	@Override
	public String getSextName() {
		return entity.getSextName();
	}

	@Override
	@XmlElement
	public void setSextName(String sextName) {
		entity.setSextName(sextName);
		
	}

	@Override
	public String getRegionCode() {
		if (entity == null || entity.getRegionShort() == null) {
			return null;
		} else {
			return entity.getRegionShort().getCode();
		}
	}

	@Override
	@XmlElement
	public void setRegionCode(String regionCode) {
		if (regionCode == null) {
			entity.setRegionShort(null);
		} else {
			RegionsEntity reg = new RegionsEntity();
			reg.setCode(regionCode);
			entity.setRegionShort(reg);
		}
	
	}

	public String getRegionCodeReg() {
		if (entity == null || entity.getRegionShort() == null) {
			return null;
		} else {
			return entity.getRegionShort().getCodereg();
		}
	}
	
	@Override
	public String getAutoCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setAutoCode(String autoCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAreaCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setAreaCode(String areaCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCityCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setCityCode(String cityCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCtarCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setCtarCode(String ctarCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPlaceCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setPlaceCode(String placeCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStreetCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setStreetCode(String streetCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getExtrCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setExtrCode(String extrCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSextCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@XmlElement
	public void setSextCode(String sextCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LevelRecord[] toLevels() {
		return Utils.toLevels(this);
	}

	@Override
	public void fromLevels(LevelRecord[] source) {
		Utils.fromLevels(source, this);		
	}

	@Override
	public void clearAddress() {
		Utils.clearAddress(this);		
	}
	
}
