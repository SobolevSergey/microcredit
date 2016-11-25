package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Ошибки загрузки
 */
public class UploadingErrorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;
	private Integer id;
	/**
	 * загрузка
	 */
	private UploadingEntity uploadingId;
	/**
	 * кредит, по которому произошла ошибка
	 */
	private CreditEntity creditId;
	/**
	 * заявка, по которой произошла ошибка
	 */
	private CreditRequestEntity creditRequestId;
	/**
	 * описание ошибки
	 */
	private String description;
	
	public UploadingErrorEntity(){
	    	
	}

	public UploadingErrorEntity(Integer id){
	    	this.id=id;
	}
	    
	public Integer getId() {
	        return id;
	}

	public void setId(Integer id) {
	        this.id = id;
	}
	
	public UploadingEntity getUploadingId(){
		return uploadingId;
	}
	
	public void setUploadingId(UploadingEntity uploadingId){
		this.uploadingId=uploadingId;
	}
	
	public CreditEntity getCreditId(){
		return creditId;
	}
	
	public void setCreditId(CreditEntity creditId){
		this.creditId=creditId;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}
	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
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
		    
		    UploadingErrorEntity cast = (UploadingErrorEntity) other;
		    
		    if (this.id == null) return false;
		       
		    if (cast.getId() == null) return false;       
		       
		    if (this.id.intValue() != cast.getId().intValue())
		    	return false;
		    
		    return true;
	}
}
