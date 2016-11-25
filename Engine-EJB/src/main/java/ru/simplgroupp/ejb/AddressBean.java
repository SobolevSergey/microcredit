package ru.simplgroupp.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.fias.persistence.Level;
import ru.simplgroupp.interfaces.AddressBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.RegionsEntity;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AddressBean implements AddressBeanLocal {
    @EJB
    protected ReferenceBooksLocal refBooks;
    @EJB
    protected IFIASService fiasServ;
    
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @Override
    public void setNameForRegion(String param, AddressEntity aadr) {
        aadr.setRegion(param);
        AddrObj aobjr = fiasServ.findAddressObject(param);
        if (aobjr != null) {
        	aadr.setRegionName(aobjr.getOfficialName()+" "+aobjr.getType().getCode());
            RegionsEntity codeReg = refBooks.getRegions(aobjr.getRegionCode());
            if (codeReg != null){
                aadr.setRegionShort(codeReg);
            }
        }
    }

    @Override
    public String setNameFromAddrObj(String param) {
        String st = "";
        if (StringUtils.isNotEmpty(param)) {

            AddrObj aobj = fiasServ.findAddressObject(param);
            if (aobj != null){
                  st = aobj.getOfficialName();
            }
           
        }
        return st;
    }    
    
    @Override
    public String setExtFromAddrObj(String param) {
        String st = "";
        if (StringUtils.isNotEmpty(param)) {

            AddrObj aobj = fiasServ.findAddressObject(param);
            if (aobj != null){
                 st = aobj.getType().getCode();
            }
              
        }
        return st;
    }

    public void fillAddress(AddressEntity address, AddrObj fiasAddressObject) {
        clearAddress(address);
        while (fiasAddressObject != null) {
            fillAddressInternal(address, fiasAddressObject);
            fiasAddressObject = fiasServ.findAddressObjectByAoguid(fiasAddressObject.getParentAOGUID());
        }
    }

    private void clearAddress(AddressEntity address) {
        address.setRegion(null);
        address.setRegionName(null);
        address.setRegionShort(null);

        address.setArea(null);
        address.setAreaName(null);
        address.setAreaExt(null);

        address.setCity(null);
        address.setCityName(null);
        address.setCityExt(null);

        address.setDistrict(null);
        address.setDistrictName(null);

        address.setPlace(null);
        address.setPlaceName(null);
        address.setPlaceExt(null);

        address.setStreet(null);
        address.setStreetName(null);
        address.setStreetExt(null);
    }

    private void fillAddressInternal(AddressEntity address, AddrObj fiasAddressObject) {
        int level = fiasAddressObject.getType().getLevel();
        if (Level.REGION.getID().equals(level) || Level.AUTO_OKRUG.getID().equals(level)) {
            address.setRegion(fiasAddressObject.getID());
            address.setRegionName(fiasAddressObject.getOfficialName() + " " + fiasAddressObject.getType().getCode());
            RegionsEntity codeReg = refBooks.getRegions(fiasAddressObject.getRegionCode());
            if (codeReg != null) {
                address.setRegionShort(codeReg);
            }
        } else if (Level.RAION.getID().equals(level)) {
            address.setArea(fiasAddressObject.getID());
            address.setAreaName(fiasAddressObject.getFormalName());
            address.setAreaExt(fiasAddressObject.getType().getCode());
            if (StringUtils.isEmpty(address.getIndex())){
            	address.setIndex(fiasAddressObject.getPostalCode());
            }
        } else if (Level.CITY.getID().equals(level)) {
            address.setCity(fiasAddressObject.getID());
            address.setCityName(fiasAddressObject.getFormalName());
            address.setCityExt(fiasAddressObject.getType().getCode());
            if (StringUtils.isEmpty(address.getIndex())){
            	address.setIndex(fiasAddressObject.getPostalCode());
            }
        } else if (Level.CITY_TERR.getID().equals(level)) {
            address.setDistrict(fiasAddressObject.getID());
            address.setDistrictName(fiasAddressObject.getFormalName());
            if (StringUtils.isEmpty(address.getIndex())){
            	address.setIndex(fiasAddressObject.getPostalCode());
            }
        } else if (Level.NP.getID().equals(level)) {
            address.setPlace(fiasAddressObject.getID());
            address.setPlaceName(fiasAddressObject.getFormalName());
            address.setPlaceExt(fiasAddressObject.getType().getCode());
            if (StringUtils.isEmpty(address.getIndex())){
            	address.setIndex(fiasAddressObject.getPostalCode());
            }
        } else if (Level.STREET.getID().equals(level)) {
            address.setStreet(fiasAddressObject.getID());
            address.setStreetName(fiasAddressObject.getFormalName());
            address.setStreetExt(fiasAddressObject.getType().getCode());
            address.setIndex(fiasAddressObject.getPostalCode());
        } else if (Level.DOP_TERR.getID().equals(level)) {
        } else if (Level.DOP_DOP_TERR.getID().equals(level)) {
        }
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AddressEntity saveAddressEntity(AddressEntity address) {
		AddressEntity newAddress=emMicro.merge(address);
		emMicro.persist(newAddress);
		return newAddress;
	}

	@Override
	public String findLastFiasGuid(AddressEntity address) {
		if(StringUtils.isNotEmpty(address.getStreet())){
			return address.getStreet();
		} else if(StringUtils.isNotEmpty(address.getPlace())){
			return address.getPlace();
		} else if(StringUtils.isNotEmpty(address.getCity())){
			return address.getCity();
		} else if(StringUtils.isNotEmpty(address.getArea())){
			return address.getArea();
		} else if(StringUtils.isNotEmpty(address.getRegion())){
			return address.getRegion();
		}
		return null;
	}

	@Override
	public AddressEntity getAddress(Integer id) {
		return emMicro.find(AddressEntity.class, id);
	}
	
}
