package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

public class UserPropertiesEntity implements Serializable{

	
	private static final long serialVersionUID = 1L;
	protected Integer txVersion = 0;
    private Integer id;
    /**
     * дата
     */
    private Date actionDate;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * Учитывать бонусы при оплате 1- учитывать, 0 - не учитывать
     */
    private Integer payByBonus;
   
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
    	 if (other == null) return false;
	       
 	    if (other == this) return true;
 	       
 	    if (! (other.getClass() ==  getClass()))
 	    	return false;
 	    
 	   UserPropertiesEntity cast = (UserPropertiesEntity) other;
 	    
 	    if (this.id == null) return false;
 	       
 	    if (cast.getId() == null) return false;       
 	       
 	    if (this.id.intValue() != cast.getId().intValue())
 	    	return false;
 	    
 	    return true;
    }

	public Integer getPayByBonus() {
		return payByBonus;
	}

	public void setPayByBonus(Integer payByBonus) {
		this.payByBonus = payByBonus;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
}
