package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PhonePayEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PhonePay extends BaseTransfer<PhonePayEntity> implements Serializable, Initializable{
    
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1383351590592456658L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PhonePay.class.getConstructor(PhonePayEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected Reference paymentType;
	protected PhonePaySummary summaryId;
	
	public PhonePay(){
		super();
		entity = new PhonePayEntity();
	}
	
	public PhonePay(PhonePayEntity entity){
		super(entity);
		if (entity.getPaymentType()!=null){
			paymentType=new Reference(entity.getPaymentType());
		}
		summaryId=new PhonePaySummary(entity.getSummaryId());
	}
	
	public Integer getId() {
	     return entity.getId();
	 }
	 
	@XmlElement
	public void setId(Integer id) {
	     entity.setId(id);
	}
	 
	public Reference getPaymentType(){
	    	return paymentType;
	}

	@XmlElement
	public void setPaymentType(Reference paymentType){
	    	this.paymentType=paymentType;
	}
	 
	public PhonePaySummary getSummaryId(){
		 return summaryId;
	}
	 
	@XmlElement
	public void setSummaryId(PhonePaySummary summaryId){
		 this.summaryId=summaryId;
	}
	 
	public Integer getPaymentCount() {
	     return entity.getPaymentCount();
	}

	@XmlElement
	public void setPaymentCount(Integer paymentCount) {
	     entity.setPaymentCount(paymentCount);
	}
	 
    public Integer getPeriodNumber() {
	     return entity.getPeriodNumber();
	}

	@XmlElement
	public void setPeriodNumber(Integer periodNumber) {
	     entity.setPeriodNumber(periodNumber);
	}
	 
	public Double getPaymentSum() {
	     return entity.getPaymentSum();
	}
	 
	@XmlElement	 
	public void setPaymentSum(Double paymentSum) {
	     entity.setPaymentSum(paymentSum);
	}
	 
	public Date getPeriodFirstDate() {
	     return entity.getPeriodFirstDate();
	}
	 
	@XmlElement	    
	public void setPeriodFirstDate(Date periodFirstDate) {
	     entity.setPeriodFirstDate(periodFirstDate);
	}
	
	@Override
	public void init(Set options) {
		entity.getPaymentSum();
		
	}

}
