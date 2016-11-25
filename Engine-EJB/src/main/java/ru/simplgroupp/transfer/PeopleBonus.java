package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.PeopleBonusEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class PeopleBonus extends BaseTransfer<PeopleBonusEntity> implements Serializable, Initializable, Identifiable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2305156854995227161L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeopleBonus.class.getConstructor(PeopleBonusEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
    public static final int OPERATION_ADDED = 1;
    public static final int OPERATION_REMOVED = 2;

	public static final int BONUS_CODE_INVITE = 1;
	public static final int BONUS_CODE_MANUAL = 2;
	public static final int BONUS_CODE_CREDIT_PAY = 3;
	public static final int BONUS_CODE_SOC_ADD = 4;
	public static final int BONUS_CODE_INN_ADD = 5;
	public static final int BONUS_CODE_SNILS_ADD = 6;
	public static final int BONUS_CODE_DOC_ADD = 7;
	public static final int BONUS_CODE_CONTACT_ADD = 8;
	public static final int BONUS_CODE_CLOSE_CREDIT_WITHOUT_DELAY = 9;

	protected RefBonus bonus;
	protected Reference operation;
	protected PeopleMain peopleMain;
	protected Credit credit;
	protected PeopleMain peopleMainBonus;
	
	public PeopleBonus(){
		super();
		entity=new PeopleBonusEntity();
	}
	
	public PeopleBonus(PeopleBonusEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
    	bonus = new RefBonus(entity.getBonusId());
    	operation = new Reference(entity.getOperationId());
    	
	   	if (entity.getCreditId()!=null){
	      	  credit=new Credit(entity.getCreditId());
	   	}
	   	if (entity.getPeopleMainBonusId()!=null){
	   		peopleMainBonus=new PeopleMain(entity.getPeopleMainBonusId());
	   	}
	}
	
    public Integer getId() {
	    return entity.getId();
	}
	    
	public void setId(Integer id) {
	    entity.setId(id);
	}

    public RefBonus getBonus() {
        return bonus;
    }

    public void setBonus(RefBonus bonus) {
        this.bonus = bonus;
    }

    public PeopleMain getPeopleMain() {
	    return peopleMain;
	}

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
	   
    public PeopleMain getPeopleMainBonus() {
	    return peopleMainBonus;
	}

    public void setPeopleMainBonus(PeopleMain peopleMainBonus) {
	    this.peopleMainBonus=peopleMainBonus;
    }
	
    public Credit getCredit() {
	   	return credit;
	}
	    
	public void setCredit(Credit credit) {
	   	this.credit=credit;
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
	
	public Double getAmountrub(){
	   	return entity.getAmountrub();
	}
	    
	public void setAmountrub(Double amountrub) {
	   	entity.setAmountrub(amountrub);
	}
	
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

	public Reference getOperation() {
		return operation;
	}

	public void setOperation(Reference operation) {
		this.operation = operation;
	}

}
