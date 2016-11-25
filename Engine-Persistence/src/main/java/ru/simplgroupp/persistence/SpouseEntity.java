package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * Супруг(а)
 */
public class SpouseEntity implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * активная запись или нет
	 */
	 private Integer isactive;
	 /**
	  * дата начала
	  */
	 private Date databeg;
	 /**
	  * дата окончания
	  */
	 private Date dataend;
	 /**
	  * тип отношений по справочнику
	  */
	 private ReferenceEntity spouseId;
	 /**
	  * человек
	  */
     private PeopleMainEntity peopleMainId;
     /**
      * супруг
      */
     private PeopleMainEntity peopleMainSpouseId;
     /**
      * тип занятости
      */
     private ReferenceEntity typeworkId;
     
     public SpouseEntity(){
    	 
     }
     
     public SpouseEntity(Integer id){
    	 this.id=id;
     }
     
     public Integer getId() {
         return id;
     }

     public void setId(Integer id) {
         this.id = id;
     }
     
     public ReferenceEntity getSpouseId() {
         return spouseId;
     }

     public void setSpouseId(ReferenceEntity spouseId) {
         this.spouseId = spouseId;
     }
     
     public Integer getIsactive() {
         return isactive;
     }

     public void setIsactive(Integer isactive) {
         this.isactive = isactive;
     }

     public Date getDatabeg() {
         return databeg;
     }

     public void setDatabeg(Date databeg) {
         this.databeg = databeg;
     }

     public Date getDataend() {
         return dataend;
     }

     public void setDataend(Date dataend) {
         this.dataend = dataend;
     }

     public PeopleMainEntity getPeopleMainId() {
         return peopleMainId;
     }

     public void setPeopleMainId(PeopleMainEntity peopleMainId) {
         this.peopleMainId = peopleMainId;
     }
     
     public PeopleMainEntity getPeopleMainSpouseId() {
         return peopleMainSpouseId;
     }

     public void setPeopleMainSpouseId(PeopleMainEntity peopleMainSpouseId) {
         this.peopleMainSpouseId = peopleMainSpouseId;
     }
     
     public  ReferenceEntity getTypeworkId() {
         return typeworkId;
     }

     public void setTypeworkId( ReferenceEntity typeworkId) {
         this.typeworkId = typeworkId;
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
  	    
  	   SpouseEntity cast = (SpouseEntity) other;
  	    
  	    if (this.id == null) return false;
  	       
  	    if (cast.getId() == null) return false;       
  	       
  	    if (this.id.intValue() != cast.getId().intValue())
  	    	return false;
  	    
  	    return true;
     }
}

