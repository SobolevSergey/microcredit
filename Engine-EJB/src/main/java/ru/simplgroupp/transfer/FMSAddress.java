package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.toolkit.common.Utils;

public class FMSAddress extends BaseAddress {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1261155726667965749L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 
	static {
    	try {
			wrapConstructor = FMSAddress.class.getConstructor(AddressEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
    
    public enum Options {
        INIT_FMS_COUNTRY,
        INIT_FMS_REGION;
    }
    
    private FMSRegion regionShort;
    private Country country;
    
    public FMSAddress() {
        super();
        entity = new AddressEntity();
    }
    
    public FMSAddress(AddressEntity entity) {
        super(entity);
        regionShort = new FMSRegion( entity.getRegionShort());
        country = new Country ( entity.getCountry());
    }

    @Override
    public void init(Set options) {
        Utils.initRelation(regionShort, options, Options.INIT_FMS_REGION);
        Utils.initRelation(country, options, Options.INIT_FMS_COUNTRY);        
    }

    public String getAddressText() {
        return entity.getAddressText();
    }

    @XmlElement
    public void setAddressText(String addressText) {
        entity.setAddressText(addressText);
    }

    public FMSRegion getRegionShort() {
        return regionShort;
    }

    @XmlElement
    public void setRegionShort(FMSRegion regionShort) {
        this.regionShort = regionShort;
    }

    public String getRegionName() {
        return entity.getRegionName();
    }

    @XmlElement
    public void setRegionName(String regionName) {
        entity.setRegionName(regionName);
    }

    public String getAreaName() {
        return entity.getAreaName();
    }

    @XmlElement
    public void setAreaName(String areaName) {
        entity.setAreaName(areaName);
    }

    public String getPlaceName() {
        return entity.getPlaceName();
    }
    
    @XmlElement
    public void setPlaceName(String placeName) {
        entity.setPlaceName(placeName);
    }

    public String getCityName() {
        return entity.getCityName();
    }

    @XmlElement
    public void setCityName(String cityName) {
        entity.setCityName(cityName);
    }

    public String getDistrictName() {
        return entity.getDistrictName();
    }

    @XmlElement
    public void setDistrictName(String districtName) {
        entity.setDistrictName(districtName);
    }

    public String getStreetName() {
        return entity.getStreetName();
    }

    @XmlElement
    public void setStreetName(String streetName) {
        entity.setStreetName(streetName);
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

    public Country getCountry() {
        return country;
    }

    @XmlElement
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public String getStreetExt() {
        return entity.getStreetExt();
    }

    @XmlElement
    public void setStreetExt(String streetExt) {
        entity.setStreetExt(streetExt);
    }
    
    public String getPlaceExt() {
        return entity.getPlaceExt();
    }

    @XmlElement
    public void setPlaceExt(String placeExt) {
        entity.setPlaceExt(placeExt);
    }
    
    public String getCityExt() {
        return entity.getCityExt();
    }

    @XmlElement
    public void setCityExt(String cityExt) {
        entity.setCityExt(cityExt);
    }
    
    public String getAreaExt() {
        return entity.getAreaExt();
    }

    @XmlElement
    public void setAreaExt(String areaExt) {
        entity.setAreaExt(areaExt);
    }
}