package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * баланс по разным платежным системам
 */
public class BalanceEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8577619963335326285L;
	protected Integer txVersion = 0;
	
    /**
     * дата
     */
    private Date eventDate;
    /**
     * сумма
     */
    private Double amount;
    /**
     * источник информации (партнер)
     */
    private PartnersEntity partnersId;
    /**
     * платеж, если данные пришли с платежом
     */
    private PaymentEntity paymentId;
    /**
     * вид счета
     */
    private ReferenceEntity accountTypeId;    
    
    public BalanceEntity(){
    	
    }
    
    public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public PartnersEntity getPartnersId() {
		return partnersId;
	}

	public void setPartnersId(PartnersEntity partnersId) {
		this.partnersId = partnersId;
	}

	public PaymentEntity getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(PaymentEntity paymentId) {
		this.paymentId = paymentId;
	}
    
	public ReferenceEntity getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(ReferenceEntity accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

}
