package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PeopleFriendEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PeopleFriend extends BaseTransfer<PeopleFriendEntity> implements Serializable, Initializable{

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4112895538881133392L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    }     
	static {
    	try {
			wrapConstructor = PeopleBehavior.class.getConstructor(PeopleFriendEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int FOR_BONUS=1;
	public static final int NOT_FOR_BONUS=0;
	
	protected PeopleMain peopleMain;
	
	public PeopleFriend(){
		super();
		entity = new PeopleFriendEntity();
	}
	
	public PeopleFriend(PeopleFriendEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
	}
	
	public Integer getId() {
	     return entity.getId();
	 }
	    
	@XmlElement
	public void setId(Integer id) {
	     entity.setId(id);
	}
	 
	 public PeopleMain getPeopleMain() {
	      return peopleMain;
	}

	public void setPeopleMain(PeopleMain peopleMain) {
	      this.peopleMain=peopleMain;
	}
	    
	public String getSurname() {
        return entity.getSurname();
    }

	@XmlElement
    public void setSurname(String surname) {
    	entity.setSurname(surname);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
    	entity.setName(name);;
    }

    public String getEmail() {
        return entity.getEmail();
    }

    @XmlElement
    public void setEmail(String email) {
    	entity.setEmail(email);;
    }

    public Date getDateactual() {
		return entity.getDateactual();
	}
	
	@XmlElement
	public void setDateactual(Date dateactual) {
		entity.setDateactual(dateactual);
	}
 
	public String getPhone() {
        return entity.getPhone();
    }

    @XmlElement
    public void setPhone(String phone) {
    	entity.setPhone(phone);
    }

    public Boolean getAvailable() {
    	return entity.getAvailable();
    }
    
    @XmlElement
    public void setAvailable(Boolean available) {
    	entity.setAvailable(available);
    }
    
    public Integer getForBonus() {
        return entity.getForBonus();
    }

    @XmlElement
    public void setForBonus(Integer forBonus) {
        entity.setForBonus(forBonus);
    }
    
    public String getInitials(){
    	return entity.getInitials();
    }
    
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
	
	

}
