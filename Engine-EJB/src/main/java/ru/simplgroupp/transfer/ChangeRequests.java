package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.ChangeRequestsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ChangeRequests extends BaseTransfer<ChangeRequestsEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ChangeRequests.class.getConstructor(ChangeRequestsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public static final int NOT_EDITED=0;
	public static final int EDITED=1;
	public static final int WAIT_EDIT=2;
	
	protected CreditRequest creditRequest;
	protected Users user;
	
	public ChangeRequests(){
		super();
	}
	
	public ChangeRequests(ChangeRequestsEntity entity){
		 super(entity);
		 if (entity.getCreditRequestId()!=null){
		   creditRequest=new CreditRequest(entity.getCreditRequestId());
		 }
	     if (entity.getUserId() != null) {
	        	user=new Users(entity.getUserId());
	     }
	}
	
	public Integer getId() {
	        return entity.getId();
	}

	public void setId(Integer id) {
	        entity.setId(id);
	}
	 
	 public Integer getIsEdited() {
	        return entity.getIsEdited();
	 }

	 public void setIsEdited(Integer isEdited) {
	        entity.setIsEdited(isEdited);
	}
	 
	 public Date getRequestDate() {
	     return entity.getRequestDate();
	 }

	 public void setRequestDate(Date requestDate) {
	      entity.setRequestDate(requestDate);;
	 }

	 public String getDescription() {
	        return entity.getDescription();
	 }

	 public void setDescription(String description) {
	        entity.setDescription(description);
	 }
 
	 public CreditRequest getCreditRequest() {
	        return creditRequest;
	    }


	 public void setCreditRequest(CreditRequest creditRequest) {
	        this.creditRequest=creditRequest;
	  }
	    
	 public Users getUser(){
	    	return user;
	 }
	    
	public void setUser(Users user){
	    	this.user=user;
	}
	    
	@Override
	public void init(Set options) {
		entity.getDescription();
		
	}

}
