
package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Справочник видов скоринга
 */
public class ScoreModelEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    /**
     * название
     */
    private String name;
    /**
     * код
     */
    private String code;
    /**
     * патнер
     */
    private PartnersEntity partnersId;
    
    public ScoreModelEntity() {
    }

    public ScoreModelEntity(Integer id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
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
  	    
  	    ScoreModelEntity cast = (ScoreModelEntity) other;
  	    
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
