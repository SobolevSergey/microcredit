package ru.simplgroupp.persistence;

import java.io.Serializable;

public class UploadingDetailEntity implements Serializable {

	/**
	 * детали загрузки
	 */
	private static final long serialVersionUID = -622120142591262537L;
	
	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * загрузка
	 */
	private UploadingEntity uploadingId;
	/**
	 * кредит
	 */
	private CreditEntity creditId;
	/**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;
	 /**
     * статус загрузки записи
     */
    private Integer status;
    
    public UploadingDetailEntity(){
    	
	}

	public UploadingDetailEntity(Integer id){
	    	this.id=id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UploadingEntity getUploadingId() {
		return uploadingId;
	}
	public void setUploadingId(UploadingEntity uploadingId) {
		this.uploadingId = uploadingId;
	}
	public CreditEntity getCreditId() {
		return creditId;
	}
	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}
	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}
	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
		    
		    UploadingDetailEntity cast = (UploadingDetailEntity) other;
		    
		    if (this.id == null) return false;
		       
		    if (cast.getId() == null) return false;       
		       
		    if (this.id.intValue() != cast.getId().intValue())
		    	return false;
		    
		    return true;
	    }
}
