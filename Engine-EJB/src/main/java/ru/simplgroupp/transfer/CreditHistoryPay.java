package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.CreditHistoryPayEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class CreditHistoryPay extends BaseTransfer<CreditHistoryPayEntity> implements Serializable, Initializable{

	
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
			wrapConstructor = CreditHistoryPay.class.getConstructor(CreditHistoryPayEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected Credit credit; 
    protected Reference paymentHistoryId;
    
	public CreditHistoryPay(){
		super();
		entity = new CreditHistoryPayEntity();
	}
	
	public CreditHistoryPay(CreditHistoryPayEntity entity){
		super(entity);
		credit=new Credit(entity.getCreditId());
		paymentHistoryId=new Reference(entity.getPaymentHistoryId());
	}
	
	 public Integer getId() {
	        return entity.getId();
	 }

	 @XmlElement
	 public void setId(Integer id) {
	        entity.setId(id);
	 }
	 
	 public Double getPaidSum() {
	        return entity.getPaidSum();
	 }

	 @XmlElement
	 public void setPaidSum(Double paidsum) {
	        entity.setPaidSum(paidsum);
	 }

	 public Double getAccountSum() {
	        return entity.getAccountSum();
	 }

	 @XmlElement
	 public void setAccountSum(Double accountsum) {
	        entity.setAccountSum(accountsum);
	 }

	 public Double getCreditLimitSum() {
	        return entity.getCreditlimitSum();
	 }

	 @XmlElement
	 public void setCreditLimitSum(Double creditlimitsum) {
	        entity.setCreditlimitSum(creditlimitsum);
	 }

	 public Double getOverdueSum() {
	        return entity.getOverdueSum();
	 }

	 @XmlElement
	 public void setOverdueSum(Double overduesum) {
	        entity.setOverdueSum(overduesum);
	 }

	 
	 public Date getHistoryDate() {
	       return entity.getHistoryDate();
	 }
	 
	 @XmlElement
	 public void setHistoryDate(Date historydate) {
	        entity.setHistoryDate(historydate);
	} 
	
	public Reference getPaymentHistoryId(){
		return paymentHistoryId;
	}
	
	@XmlElement
	public void setPaymentHistoryId(Reference paymentHistoryId){
		this.paymentHistoryId=paymentHistoryId;
	}
	
	public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit creditId) {
        credit=creditId;
    }
    
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
