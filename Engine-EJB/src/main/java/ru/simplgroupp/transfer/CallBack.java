package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;




import ru.simplgroupp.persistence.CallBackEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class CallBack extends BaseTransfer<CallBackEntity> implements Serializable, Initializable,Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -546651409922715278L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
	
	public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	    	return wrapConstructor;
	} 	
		static {
	    	try {
				wrapConstructor = CallBack.class.getConstructor(CallBackEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	    }	
	    
	protected Users user; 
	protected Reference type;

	public static final int BORROWER_TYPE=1;
	public static final int INVESTOR_TYPE=2;
	
	public CallBack() {
	   super();
	   entity = new CallBackEntity();
	}
	    
	public CallBack(CallBackEntity entity) {
	   super(entity);
	   if (entity.getUserId()!=null){     
	        user = new Users(entity.getUserId());
	   }
	   if (entity.getTypeId()!=null){
		   type=new Reference(entity.getTypeId());
	   }
	}    
	
	public Integer getId() {
	    return entity.getId();
	}

	public void setId(Integer id) {
	    entity.setId(id);
	}

	public Date getDateRequest() {
	    return entity.getDateRequest();
	}

	public void setDateRequest(Date dateRequest) {
	    entity.setDateRequest(dateRequest);
	}

	public Date getDateCall() {
	    return entity.getDateCall();
	}

	public void setDateCall(Date dateCall) {
	    entity.setDateCall(dateCall);
	}
	
	public String getSurname() {
        return entity.getSurname();
    }

	public void setSurname(String surname) {
    	entity.setSurname(surname);
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
    	entity.setName(name);
    }

    public String getEmail() {
        return entity.getEmail();
    }

    public void setEmail(String email) {
    	entity.setEmail(email);;
    }
    
    public String getPhone() {
        return entity.getPhone();
    }

    public void setPhone(String phone) {
    	entity.setPhone(phone);
    }
    
    public Integer getIsActive() {
        return entity.getIsActive();
    }

    public void setIsActive(Integer isActive) {
    	entity.setIsActive(isActive);
    }
    
    public String getMessage() {
        return entity.getMessage();
    }

    public void setMessage(String message) {
    	entity.setMessage(message);
    }
    
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Reference getType() {
		return type;
	}

	public void setType(Reference type) {
		this.type = type;
	}

	@Override
	public void init(Set options) {

	}
}
