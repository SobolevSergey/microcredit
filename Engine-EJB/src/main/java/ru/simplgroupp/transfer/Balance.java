package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.BalanceEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Balance extends BaseTransfer<BalanceEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5748387623316725768L;
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Balance.class.getConstructor(BalanceEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	protected Partner partner;
	protected Payment payment;
	protected Reference accountType;
	
	public Balance(){
		super();
		entity=new BalanceEntity();
	}
	
	public Balance(BalanceEntity entity){
		super(entity);
		partner=new Partner(entity.getPartnersId());
		if (entity.getPaymentId()!=null){
			payment=new Payment(entity.getPaymentId());
		}
		if (entity.getAccountTypeId()!=null){
			accountType=new Reference(entity.getAccountTypeId());
		}
	}
	
	public Integer getId() {
	     return entity.getId();
	 }
	    
	 public void setId(Integer id) {
	     entity.setId(id);
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
	 
	 public Partner getPartner() {
		return partner;
	 }
	    
	 public void setPartner(Partner partner) {
	     this.partner=partner;
	 }

	 public Reference getAccountType() {
		return accountType;
 	 }
	 
	public void setAccountType(Reference accountType) {
       this.accountType=accountType;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
