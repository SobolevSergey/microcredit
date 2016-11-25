package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.BankEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Bank extends BaseTransfer<BankEntity> implements Serializable, Initializable{

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
			wrapConstructor = Bank.class.getConstructor(BankEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public Bank()
	{
		super();
		entity = new BankEntity();
	}
	
	public Bank(BankEntity value)
	{
		super(value);
		
	}
	
	 public String getBik() {
	        return entity.getBik();
	 }

	 @XmlElement
	 public void setBik(String bik) {
	        entity.setBik(bik);
	 }
	    
	 public String getName() {
	        return entity.getName();
	 }

	 @XmlElement
	 public void setName(String name) {
	        entity.setName(name);
	 }
	 
	 public String getAddress() {
	        return entity.getAddress();
	 }

	 @XmlElement
	 public void setAddress(String address) {
	        entity.setAddress(address);
	 }
	 
	 public String getCorAccount() {
	        return entity.getCorAccount();
	 }

	 @XmlElement
	 public void setCorAccount(String corAccount) {
	        entity.setCorAccount(corAccount);
	 }
	 
	 public Integer getIsActive() {
	        return entity.getIsActive();
	 }

	 @XmlElement
	 public void setIsActive(Integer isActive) {
	        entity.setIsActive(isActive);
	 }
	 
	 public Integer getIsBank() {
	        return entity.getIsBank();
	 }

	 @XmlElement
	 public void setIsBank(Integer isBank) {
	        entity.setIsBank(isBank);
	 }
	 
	@Override
	public void init(Set options) {
		entity.getName();
		
	}

	public String getDescription(){
		return entity.getDescription();
	}
}
