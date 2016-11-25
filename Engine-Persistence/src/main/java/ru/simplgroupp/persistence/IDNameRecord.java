package ru.simplgroupp.persistence;

import java.io.Serializable;

public class IDNameRecord implements Serializable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1588769368507312360L;
	protected Integer ID;
	protected String Name;
	
	public IDNameRecord() {
		super();
	}

	public IDNameRecord(int aid, String aname) {
		super();
		ID = aid;
		Name = aname;
	}

	@Override
	public boolean equals(Object other) 
	{
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    IDNameRecord cast = (IDNameRecord) other;
	    
	    if (this.ID == null) return false;
	       
	    if (cast.getID() == null) return false;       
	       
	    if (this.ID.intValue() != cast.getID().intValue())
	    	return false;
	    
	    return true;
	}
	@Override
	public int hashCode() {
		int _hashCode = 0, aid = 0;
	    if (ID != null) aid = ID.intValue();
	    _hashCode = 29 * _hashCode + (new Long(aid)).hashCode();
	    return _hashCode;
	}	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
}
