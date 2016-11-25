package ru.simplgroupp.fias.persistence;

import java.io.Serializable;

import ru.simplgroupp.persistence.IDNameRecord;

public class ActualStatus extends IDNameRecord implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4114824434432462688L;
	public static final ActualStatus ACTUAL = new ActualStatus(1,"актуальная");
	public static final ActualStatus NOT_ACTUAL = new ActualStatus(0,"не актуальная");
	
	public ActualStatus() {
		super();
	}

	public ActualStatus(int aid, String aname) {
		super(aid, aname);
	}

	
}
