package ru.simplgroupp.fias.persistence;

import java.io.Serializable;

import ru.simplgroupp.persistence.IDNameRecord;

public class Level extends IDNameRecord implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4042613028772517896L;
	public static final Level REGION = new Level(1, "уровень региона", 0);
	public static final Level AUTO_OKRUG = new Level(2, "уровень автономного округа", 1);
	public static final Level RAION = new Level(3, "уровень района", 2);
	public static final Level CITY = new Level(4, "уровень города", 3);
	public static final Level CITY_TERR = new Level(5, "уровень внутригородской территории", 4);
	public static final Level NP = new Level(6, "уровень населенного пункта", 5);
	public static final Level STREET = new Level(7, "уровень улицы", 6);
	public static final Level DOP_TERR = new Level(90, "уровень дополнительных территорий", 7);
	public static final Level DOP_DOP_TERR = new Level(91, "уровень подчиненных дополнительным территориям объектов", 8);
	
	private static final Level[] levels = new Level[] {REGION, AUTO_OKRUG, RAION, CITY, CITY_TERR, NP, STREET, DOP_TERR, DOP_DOP_TERR}; 
	
	private int norder;
	
	public Level() {
		super();
	}
	public Level(int aid, String aname) {
		super(aid, aname);
	}
	public Level(int aid, String aname, int aorder) {
		super(aid, aname);
		norder = aorder;
	}	
	
	public static Level getInstance(int aid) {
		for (Level alevel: levels)
			if (alevel.getID() == aid)
				return alevel;
		return null;
	}
	
	public static Level getInstanceByOrder(int aorder) {
		return levels[aorder];
	}	
	public int getNOrder() {
		return norder;
	}
	public void setNOrder(int norder) {
		this.norder = norder;
	}
	
}
