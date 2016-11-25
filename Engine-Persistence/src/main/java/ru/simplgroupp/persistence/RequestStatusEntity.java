package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Справочник статуса запроса
 */
public class RequestStatusEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * название
     */
    private String name;
   

    public RequestStatusEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
   	    
   	    RequestStatusEntity cast = (RequestStatusEntity) other;
   	    
   	    if (this.id == null) return false;
   	       
   	    if (cast.getId() == null) return false;       
   	       
   	    if (this.id.intValue() != cast.getId().intValue())
   	    	return false;
   	    
   	    return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
