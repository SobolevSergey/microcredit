package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.PeopleSumsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PeopleSums extends BaseTransfer<PeopleSumsEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8007823045964817027L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeopleSums.class.getConstructor(PeopleSumsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected PeopleMain peopleMain;
	protected Credit credit;
	protected Reference operation;
	protected Reference operationType;
	
	public static final int CREDIT_OVERPAY=1;
	public static final int CREDIT_PAID=2;
	
	 public PeopleSums() {
	   	super();
	   	entity = new PeopleSumsEntity();
	 }
		
	 public PeopleSums(PeopleSumsEntity value) {
	   	super(value);
	   	operation=new Reference(entity.getOperationId());
	    	
	   	operationType=new Reference(entity.getOperationTypeId());
	   	peopleMain=new PeopleMain(entity.getPeopleMainId());
	    	
	   	if (entity.getCreditId()!=null){
	      	  credit=new Credit(entity.getCreditId());
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
	    
	    public Reference getOperation(){
	    	return operation;
	    }
	    
	    public void setOperation(Reference operation){
	    	this.operation=operation;
	    }
	    
	    public Reference getOperationType(){
	    	return operationType;
	    }
	    
	    public void setOperationType(Reference operationType){
	    	this.operationType=operationType;
	    }
	    
	    public Date getEventDate(){
	    	return entity.getEventDate();
	    }
	    
	    public void setEventDate(Date eventDate){
	    	entity.setEventDate(eventDate);
	    }
	    
	    public Double getAmount(){
	    	return entity.getAmount();
	    }
	    
	    public void setAmount(Double amount) {
	    	entity.setAmount(amount);
	    }
	    
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
