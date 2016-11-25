package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.CollectorEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class Collector extends BaseTransfer<CollectorEntity> implements Serializable, Initializable,Identifiable{
	public static final Integer SOFT = 1;
	public static final Integer HARD = 2;
	public static final Integer LEGAL = 3;
	public static final Integer AGENCY = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6936305306686658349L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
	    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	    	return wrapConstructor;
	    } 	
	    static {
	    	try {
				wrapConstructor = Collector.class.getConstructor(CollectorEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	    }	
		
		protected PeopleMain peopleMain;
		protected Credit credit;
		protected Reference collectionType;
		protected Users user;

		private Boolean selected;
		
		public Collector(){
			super();
			entity=new CollectorEntity();
		}
		
		public Collector(CollectorEntity value){
			super(value);
			peopleMain=new PeopleMain(entity.getPeopleMainId());
			credit=new Credit(entity.getCreditId());
			if (entity.getCollectionTypeId()!=null){
				collectionType=new Reference(entity.getCollectionTypeId());
			}
			if (entity.getUserId()!=null){     
			    user = new Users(entity.getUserId());
			}
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
	    
	    public Credit getCredit() {
	    	return credit;
	    }
	    
	    public void setCredit(Credit credit) {
	    	this.credit=credit;
	    }
	    
	    public Reference getCollectionType(){
	    	return collectionType;
	    }
	    
	    public void setCollectionType(Reference collectionType){
	    	this.collectionType=collectionType;
	    }
	    
	    public Date getDatabeg() {
	        return entity.getDatabeg();
	    }
	    
	    public void setDatabeg(Date databeg) {
	        entity.setDatabeg(databeg);
	    }

	    public Date getDataend() {
	        return entity.getDataend();
	    }

	    public void setDataend(Date dataend) {
	        entity.setDataend(dataend);
	    }
	    
	    public Integer getIsActive() {
	        return entity.getIsActive();
	    }

	    public void setIsActive(Integer isactive) {
	        entity.setIsActive(getIsActive());
	    }
	    
	    public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}
		
		@Override
		public void init(Set options) {
			peopleMain.init(options);
			if (user != null) {
				user.init(options);
			}
			if (credit != null) {
				credit.init(options);
			}
		}

		public Boolean getSelected() {
			return selected;
		}

		public void setSelected(Boolean selected) {
			this.selected = selected;
		}
}
