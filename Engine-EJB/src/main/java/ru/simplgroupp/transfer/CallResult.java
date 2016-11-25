package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.CallResultEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class CallResult extends BaseTransfer<CallResultEntity> implements Serializable, Initializable,Identifiable{

	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = CallResult.class.getConstructor(CallResultEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected PeopleMain peopleMain;
	protected Reference resultType;
	protected Users user; 
	
	public CallResult(){
		super();
		entity=new CallResultEntity();
	}
	
	public CallResult(CallResultEntity value){
		super(value);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		if (entity.getResultTypeId()!=null){
			resultType=new Reference(entity.getResultTypeId());
		}
		user = new Users(entity.getUserId());
		
	}
	
	public Integer getId() {
	    return entity.getId();
	}
	    
	public void setId(Integer id) {
	    entity.setId(id);
	}
	    
	public PeopleMain getPeopleMain() {
	    return peopleMain;
	}


	public void setPeopleMain(PeopleMain peopleMain) {
	    this.peopleMain=peopleMain;
	}
	    
	public Reference getResultType(){
		return resultType;
	}
	    
	public void setResultType(Reference resultType){
	 	this.resultType=resultType;
	}
	    
	public Date getDateCall() {
	    return entity.getDateCall();
	}
	    
	public void setDateCall(Date dateCall) {
	    entity.setDateCall(dateCall);
	}

	public Date getDatePromise() {
	    return entity.getDatePromise();
	}

	public void setDatePromise(Date datePromise) {
	    entity.setDatePromise(datePromise);
    }
	   
	public Date getDateNextContact() {
	    return entity.getDateNextContact();
	}

	public void setDateNextContact(Date dateNextContact) {
	    entity.setDateNextContact(dateNextContact);
    }
	
	public Users getUser() {
	    return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
		
	public String getComment() {
	    return entity.getComment();
	}

	public void setComment(String comment) {
	    entity.setComment(comment);
	}
	
	@Override
	public void init(Set options) {
			// TODO Auto-generated method stub
			
	}
}
