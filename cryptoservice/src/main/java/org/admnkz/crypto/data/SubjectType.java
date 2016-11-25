package org.admnkz.crypto.data;

import java.io.Serializable;

public class SubjectType implements Serializable 
{
	private static final long serialVersionUID = -7627142204692153475L;
	
	public static final SubjectType END_USER = new SubjectType(1,"Конечный пользователь");
	public static final SubjectType SUB_CA = new SubjectType(2, "Промежуточный УЦ");
	public static final SubjectType ROOT_CA = new SubjectType(3, "Корневой УЦ");
	
	private Integer ID;
	private String Name;
	
	public SubjectType() {
		super();
	}
	
	public SubjectType(int aid, String aname) {
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
	    
	    SubjectType cast = (SubjectType) other;
	    
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
