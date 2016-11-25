package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.UserPropertiesEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class UserProperties extends BaseTransfer<UserPropertiesEntity> implements Serializable, Initializable, Identifiable {


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
			wrapConstructor = UserProperties.class.getConstructor(UserPropertiesEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
	protected PeopleMain peopleMain;
	
	public UserProperties(){
		super();
		entity=new UserPropertiesEntity();
	}
	
	public UserProperties(UserPropertiesEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
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
	    
	public Date getActionDate(){
	   	return entity.getActionDate();
	}
	    
	public void setActionDate(Date actionDate){
	  	entity.setActionDate(actionDate);
	}
	    
	public Integer getPayByBonus(){
	   	return entity.getPayByBonus();
	}
	    
	public void setPayByBonus(Integer payByBonus) {
	   	entity.setPayByBonus(payByBonus);
	}
	
	@Override
	public void init(Set options) {
		
	}

}
