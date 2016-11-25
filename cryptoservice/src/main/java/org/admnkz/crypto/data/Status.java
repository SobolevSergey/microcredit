package org.admnkz.crypto.data;

import java.io.Serializable;

public class Status implements Serializable 
{
	private static final long serialVersionUID = -7627142204692153475L;
	
	public static final Status UNDEFINED = new Status(0, "Не определён");
	public static final Status ACTIVE = new Status(1,"Активен");
	public static final Status OUT_OF_DATE = new Status(2, "Закончился или не начался срок действия");
	public static final Status CLOSED = new Status(3, "Закрыт");
	
	private Integer ID;
	private String Name;
	
	public Status() {
		super();
	}
	
	public Status(int aid, String aname) {
		super();
		ID = aid;
		Name = aname;
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
	
	@Override
	public boolean equals(Object other) 
	{
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    Status cast = (Status) other;
	    
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
}
