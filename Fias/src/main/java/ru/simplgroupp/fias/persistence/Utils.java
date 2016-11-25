package ru.simplgroupp.fias.persistence;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class Utils {
	public static LevelRecord[] toLevels(IAddress address) {
		String[] values = new String[] {address.getRegionGUID(), address.getAutoGUID(), address.getAreaGUID(), address.getCityGUID(), address.getCtarGUID(), address.getPlaceGUID(), address.getStreetGUID(), address.getExtrGUID(), address.getSextGUID()};
		String[] names = new String[] {address.getRegionName(), address.getAutoName(), address.getAreaName(), address.getCityName(), address.getCtarName(), address.getPlaceName(), address.getStreetName(), address.getExtrName(), address.getSextName()};
		String[] codes = new String[] {address.getRegionCode(), address.getAutoCode(), address.getAreaCode(), address.getCityCode(), address.getCtarCode(), address.getPlaceCode(), address.getStreetCode(), address.getExtrCode(), address.getSextCode()};
		
		LevelRecord[] res = new LevelRecord[9];
		int n = -1;
		for (int i = 0; i < values.length; i++) {
			if (StringUtils.isEmpty( values[i]))
				continue;

			n++;
			LevelRecord rec = new LevelRecord();
			rec.nlevel = n;
			rec.ID = values[i];
			rec.name = names[i];
			rec.code = codes[i];
			rec.aoLevel = Level.getInstanceByOrder(i);
			res[n] = rec;
			
		}
		if (n < 0)
			return new LevelRecord[0];
		else
			return Arrays.copyOf(res, n+1);		
	}
	
	public static void clearAddress(IAddress address) {
		address.setRegionCode(null);
		address.setRegionGUID(null);
		address.setRegionName(null);
		
		address.setAutoCode(null);
		address.setAutoGUID(null);
		address.setAutoName(null);
		
		address.setAreaCode(null);
		address.setAreaGUID(null);
		address.setAreaName(null);
		
		address.setCityCode(null);
		address.setCityGUID(null);
		address.setCityName(null);
		
		address.setCtarCode(null);
		address.setCtarGUID(null);
		address.setCtarName(null);
		
		address.setStreetCode(null);
		address.setStreetGUID(null);
		address.setStreetName(null);
		
		address.setExtrCode(null);
		address.setExtrGUID(null);
		address.setExtrName(null);
		
		address.setSextCode(null);
		address.setSextGUID(null);
		address.setSextName(null);
		
		address.setPlaceCode(null);
		address.setPlaceGUID(null);
		address.setPlaceName(null);
		

	}
	
	public static void fromLevels(LevelRecord[] source, IAddress address)
	{	
		for (int i = 0; i < source.length; i++) 
		{
			if (source[i].getAoLevel() == null)
				continue;
			
			switch (source[i].getAoLevel().getNOrder())
			{
			  case 0:{
				  address.setRegionGUID(source[i].getID() );
				  address.setRegionName(source[i].getName());
				  address.setRegionCode(source[i].getCode());
			  };break;
			  case 1:{
				  address.setAutoGUID(source[i].getID());
				  address.setAutoName(source[i].getName());
				  address.setAutoCode(source[i].getCode());
			  };break;
			  case 2:{
				  address.setAreaGUID(source[i].getID());
				  address.setAreaName(source[i].getName());
				  address.setAreaCode(source[i].getCode());
			  };break;
			  case 3:{
				  address.setCityGUID(source[i].getID());
				  address.setCityName(source[i].getName());
				  address.setCityCode(source[i].getCode());
			  };break;
			  case 4:{
				  address.setCtarGUID(source[i].getID());
				  address.setCtarName(source[i].getName());
				  address.setCtarCode(source[i].getCode());
			  };break;
			  case 5:{
				  address.setPlaceGUID(source[i].getID());
				  address.setPlaceName(source[i].getName());
				  address.setPlaceCode(source[i].getCode());
			  };break;
			  case 6:{
				  address.setStreetGUID(source[i].getID());
				  address.setStreetName(source[i].getName());
				  address.setStreetCode(source[i].getCode());
			  };break;
			  case 7:{
				  address.setExtrGUID(source[i].getID());
				  address.setExtrName(source[i].getName());
				  address.setExtrCode(source[i].getCode());
			  };break;
			  case 8:{
				  address.setSextGUID(source[i].getID());
				  address.setSextName(source[i].getName());
				  address.setSextCode(source[i].getCode());
			  };break;
			}
		}
	}		
	
	/**
	 * Дом + корпус + литера
	 * @return
	 */
	public static String getHouseCaption(IAddress address) {
		String sres = address.getHouse();
		
		if (! StringUtils.isEmpty(address.getLiter()) ) {
			sres = sres + address.getLiter();
		}		
		if (! StringUtils.isEmpty(address.getCorpus()) ) {
			sres = sres + ", корп. " + address.getCorpus();
		}
	
		if (! StringUtils.isEmpty(address.getBuilding()) ) {
			sres = sres + ", строение " + address.getBuilding();
		}
		
		return sres;
	}	
	
	/**
	 * Дом + корпус + литера + квартира
	 * @return
	 */
	public static String getHouseFlatCaption(IAddress address) {
		String sres = getHouseCaption(address);

		if (! StringUtils.isEmpty(address.getFlat()) ) {
			sres = sres + " кв." + address.getFlat();
		}
		return sres;
	}
	
	public static String getStreetCaption(IAddress address) {
		return address.getStreetName();
	}
}
