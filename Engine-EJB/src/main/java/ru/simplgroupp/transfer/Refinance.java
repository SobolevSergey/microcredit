package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class Refinance extends BaseTransfer<RefinanceEntity> implements Serializable, Initializable, Identifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6001327284997342219L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Refinance.class.getConstructor(RefinanceEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	private Credit credit;
	private Credit creditNew;
	
	public Refinance(){
		super();
		entity=new RefinanceEntity();
	}
	
	public Refinance(RefinanceEntity entity){
		super(entity);
		credit=new Credit(entity.getCreditId());
		if (entity.getCreditNewId()!=null){
			creditNew=new Credit(entity.getCreditNewId());
		}
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
	
	public Credit getCreditNew() {
		return creditNew;
	}
	    
	@XmlElement
	public void setCreditNew(Credit creditNew){
	  	this.creditNew=creditNew;
	}
	
	 public Date getRefinanceDate() {
	    return entity.getRefinanceDate();
	 }

	 @XmlElement
	 public void setRefinanceDate(Date refinanceDate) {
	     entity.setRefinanceDate(refinanceDate);
	 }

	 public Integer getRefinanceDays() {
	     return entity.getRefinanceDays();
	 }

	 @XmlElement
	 public void setRefinanceDays(Integer refinanceDays) {
	     entity.setRefinanceDays(refinanceDays);
	 }

	 public Double getRefinanceStake() {
	     return entity.getRefinanceStake();
	 }

	 @XmlElement
	 public void setRefinanceStake(Double refinanceStake) {
	     entity.setRefinanceStake(refinanceStake);
	 }

	 public Double getRefinanceAmount() {
	     return entity.getRefinanceAmount();
	 }

	 @XmlElement
	 public void setRefinanceAmount(Double refinanceAmount) {
	     entity.setRefinanceAmount(refinanceAmount);
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
		// TODO Auto-generated method stub
		
	}
	
	public boolean isOfertaSigned() {
		return StringUtils.isNotBlank(entity.getSmsCode());
	}

}
