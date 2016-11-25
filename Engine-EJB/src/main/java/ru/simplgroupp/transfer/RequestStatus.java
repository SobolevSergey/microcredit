package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.RequestStatusEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class RequestStatus extends BaseTransfer<RequestStatusEntity> implements Serializable, Initializable{

	
	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = RequestStatus.class.getConstructor(RequestStatusEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
    
	public static final int STATUS_SESSION_OPENED=1;
	public static final int STATUS_IN_WORK=2;
	public static final int STATUS_IS_DONE=3;
	public static final int STATUS_ERROR=4;
	public static final int STATUS_IS_DONE_WITH_SESSION=5;
	
    public RequestStatus()
    {
    	super();
    	entity = new RequestStatusEntity();
    }
    
    public RequestStatus(RequestStatusEntity value)
    {
    	super(value);
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }    

    public RequestStatusEntity getEntity() {
        return entity;
    }
    
	@Override
	public void init(Set options) {
	    entity.getName();
	}

}
