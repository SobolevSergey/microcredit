package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class Prolong extends BaseTransfer<ProlongEntity> implements Serializable, Initializable, Identifiable {
	
		

    /**
	 * 
	 */
	private static final long serialVersionUID = -149671381898126054L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Prolong.class.getConstructor(ProlongEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
    private Credit credit;
   
    public Prolong() {
    	super();
    	entity = new ProlongEntity();
    }
    
    public Prolong(ProlongEntity value) {
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
    
    @XmlElement
    public void setCredit(Credit credit){
    	this.credit=credit;
    }
    
    public Date getLongDate() {
        return entity.getLongdate();
    }

    @XmlElement
    public void setLongdate(Date longdate) {
        entity.setLongdate(longdate);
    }

    public Integer getLongdays() {
        return entity.getLongdays();
    }

    @XmlElement
    public void setLongdays(Integer longdays) {
        entity.setLongdays(longdays);
    }

    public Double getLongstake() {
        return entity.getLongstake();
    }

    @XmlElement
    public void setLongstake(Double longstake) {
        entity.setLongstake(longstake);
    }

    public Double getLongamount() {
        return entity.getLongamount();
    }

    @XmlElement
    public void setLongamount(Double longamount) {
        entity.setLongamount(longamount);
    }
       
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
    	entity.setIsactive(isactive);
    }
    
    public String getSmsCode() {
    	return entity.getSmsCode();
    }
    
    public void setSmsCode(String value) {
    	entity.setSmsCode(value);
    }
     
    public String getAgreement() {
        return entity.getAgreement();
    }

    public void setAgreement(String agreement) {
        entity.setAgreement(agreement);        
    }
    
    public String getUniqueNomer() {
        return entity.getUniquenomer();
    }

    public void setUniqueNomer(String uniquenomer) {
        entity.setUniquenomer(uniquenomer);
    }
    
	@Override
	public void init(Set options) {
	    entity.getLongdate();

	}

}
