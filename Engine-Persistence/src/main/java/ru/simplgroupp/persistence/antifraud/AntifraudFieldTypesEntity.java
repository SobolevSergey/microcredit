package ru.simplgroupp.persistence.antifraud;

import java.io.Serializable;

/**
 * справочник типов полей
 */
public class AntifraudFieldTypesEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8061951132160736740L;

	
	public AntifraudFieldTypesEntity() {

	}

	private Integer id;

    private String type;

    private String description;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
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
     	    
     	    AntifraudFieldTypesEntity cast = (AntifraudFieldTypesEntity) other;
     	    
     	    if (this.id == null) return false;
     	       
     	    if (cast.getId() == null) return false;       
     	       
     	    if (this.id.intValue() != cast.getId().intValue())
     	    	return false;
     	    
     	    return true;
    }
}
