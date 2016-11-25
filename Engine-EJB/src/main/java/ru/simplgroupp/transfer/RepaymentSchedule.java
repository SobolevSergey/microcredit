package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RepaymentScheduleEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class RepaymentSchedule extends BaseTransfer<RepaymentScheduleEntity> implements Serializable, Initializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2068498392751294423L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = RepaymentSchedule.class.getConstructor(RepaymentScheduleEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
    private Credit credit;
    
    public RepaymentSchedule() {
    	super();
    	entity = new RepaymentScheduleEntity();
    }
    
    public RepaymentSchedule(RepaymentScheduleEntity value) {
    	super(value);
    	credit=new Credit(entity.getCreditId());
    }
     
    public Integer getId() {
        return entity.getId();
    }
    
    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Credit getCredit() {
    	return credit;
    }
    
    public void setCredit(Credit credit){
    	this.credit=credit;
    }
    
   
    public Date getDatabeg() {
        return entity.getDatabeg();
    }

    @XmlElement
    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDataend();
    }

    @XmlElement
    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }

    public Double getCreditstake() {
        return entity.getCreditstake();
    }

    @XmlElement
    public void setCreditstake(Double creditstake) {
        entity.setCreditstake(creditstake);
    }

    public Double getCreditsumback() {
        return entity.getCreditsumback();
    }
    
    @XmlElement
    public void setCreditsumback(Double creditsumback) {
        entity.setCreditsumback(creditsumback);
    }

    public Double getCreditsum() {
        return entity.getCreditsum();
    }
    
    @XmlElement
    public void setCreditsum(Double creditsum) {
        entity.setCreditsum(creditsum);
    }
    
    public Integer getReasonEndId() {
        return entity.getReasonEndId();
    }
    
    @XmlElement
    public void setReasonEndId(Integer reasonEndId) {
        entity.setReasonEndId(reasonEndId);
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }
    
	@Override
	public void init(Set options) {
	    entity.getCreditsumback();		
	}

}
