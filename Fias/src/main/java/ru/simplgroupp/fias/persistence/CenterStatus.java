package ru.simplgroupp.fias.persistence;

import java.io.Serializable;

import ru.simplgroupp.persistence.IDNameRecord;

public class CenterStatus extends IDNameRecord implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3668862693657069739L;
	public static final CenterStatus NO = new CenterStatus(0,"не является центром");
	public static final CenterStatus RAION = new CenterStatus(1,"центр района");
	public static final CenterStatus REGION = new CenterStatus(2,"центр региона");
	public static final CenterStatus RAION_REGION = new CenterStatus(2,"центр района и региона");
	
	public CenterStatus() {
		super();
	}

	public CenterStatus(int aid, String aname) {
		super(aid, aname);
	}

}
