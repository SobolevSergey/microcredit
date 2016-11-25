package ru.simplgroupp.persistence;

import java.io.Serializable;

public class ContactPointsServiceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Integer txVersion = 0;
	
	private Integer id;
	private Integer parentId;
	private Integer version;
	private Integer erased;
	private ContactPointsEntity pointId;
	private ContactServiceEntity serviceId;

    public ContactPointsServiceEntity() {
    }

    public ContactPointsServiceEntity(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getErased() {
		return erased;
	}
	public void setErased(Integer erased) {
		this.erased = erased;
	}
	public ContactPointsEntity getPointId() {
		return pointId;
	}
	public void setPointId(ContactPointsEntity pointId) {
		this.pointId = pointId;
	}
	public ContactServiceEntity getServiceId() {
		return serviceId;
	}
	public void setServiceId(ContactServiceEntity serviceId) {
		this.serviceId = serviceId;
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
   	    
   	    ContactPointsServiceEntity cast = (ContactPointsServiceEntity) other;
   	    
   	    if (this.id == null) return false;
   	       
   	    if (cast.getId() == null) return false;       
   	       
   	    if (this.id.intValue() != cast.getId().intValue())
   	    	return false;
    	    
   	    return true;
	}
}
