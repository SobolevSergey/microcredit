package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Оплаты по кредитам (из КБ)
 */
public class CreditHistoryPayEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5862170074731884127L;
	/**
	 * 
	 */
	
	protected Integer txVersion = 0;
	/**
	 * кредит
	 */
	private CreditEntity creditId;
	/**
	 * вид истории
	 */
	private ReferenceEntity paymentHistoryId;
	/**
	 * сумма на счету
	 */
	private Double accountSum;
	/**
	 * сумма просрочки
	 */
	private Double overdueSum;
	/**
	 * выплаченная сумма
	 */
	private Double paidSum;
	/**
	 * лимит кредитной карты
	 */
	private Double creditlimitSum;
	/**
	 * дата
	 */
	private Date historyDate;
	
	public CreditHistoryPayEntity(){
		
	}
	
	public CreditEntity getCreditId() {
    	return creditId;
    }
    
    public void setCreditId(CreditEntity creditId){
    	this.creditId=creditId;
    }
    
    public Date getHistoryDate(){
    	return historyDate;
    }
    
    public void setHistoryDate(Date historyDate){
    	this.historyDate=historyDate;
    }
    
    public Double getAccountSum(){
    	return accountSum;
    }
    
    public void setAccountSum(Double accountSum){
    	this.accountSum=accountSum;
    }
    
    public Double getOverdueSum(){
    	return overdueSum;
    }
    
    public void setOverdueSum(Double overdueSum){
    	this.overdueSum=overdueSum;
    }
    
    public Double getPaidSum(){
    	return paidSum;
    }
    
    public void setPaidSum(Double paidSum){
    	this.paidSum=paidSum;
    }
    
    public Double getCreditlimitSum(){
    	return creditlimitSum;
    }
    
    public void setCreditlimitSum(Double creditlimitSum){
    	this.creditlimitSum=creditlimitSum;
    }
    
    public ReferenceEntity getPaymentHistoryId() {
        return paymentHistoryId;
    }

    public void setPaymentHistoryId(ReferenceEntity paymentHistoryId) {
        this.paymentHistoryId = paymentHistoryId;
    }
    
   
}
