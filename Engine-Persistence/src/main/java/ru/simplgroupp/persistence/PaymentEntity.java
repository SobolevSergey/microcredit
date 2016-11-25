package ru.simplgroupp.persistence;

import ru.simplgroupp.exception.ExceptionInfo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Платежи
 */
public class PaymentEntity extends BaseEntity implements Serializable {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 7979382559642271214L;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    protected Integer txVersion = 0;
     /**
     * Сумма платежа
     */
    private Double amount;

    /**
     * Дата создания
     */
    private Date createDate;

    /**
     * Дата проведения
     */
    private Date processDate;

    /**
     * Направление платежа
     */
    private ReferenceEntity paymenttypeId;

    /**
     * Вид суммы из справочника
     */
    private ReferenceEntity paysumId;

    /**
     * Провайдер платежа
     */
    private PartnersEntity partnersId;

    private Boolean isPaid = false;

    /**
     * По какому кредиту платёж
     */
    private CreditEntity creditId;

    /**
     * Статус платежа
     */
    private PaymentStatus status = PaymentStatus.NEW;

    /**
     * ID во внешней системе (в системе провайдера платежа)
     */
    private String externalId;
    /**
     * ID во внешней системе (в системе провайдера платежа), если их 2, как в Ариусе
     */
    private String externalId2;
    /**
     * счет для перечисления денег
     */
    private AccountEntity accountId;    
    
    /**
     * вид счета
     */
    private ReferenceEntity accountTypeId;    

    private ExceptionInfo errorInfo;

    public PaymentEntity() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
    
    public String getDescription(){
    	String st="платеж, дата создания "+sdf.format(this.createDate);
    	if (this.isPaid){
    		st+=", оплачен, дата проведения "+sdf.format(this.processDate);
    	}
    	st+=", сумма "+this.amount.toString();
        return st;
    }
    
 
    @Override
    public String toString() {
        return "сумма "+amount.toString();
    }

    public CreditEntity getCreditId() {
        return creditId;
    }

    public void setCreditId(CreditEntity creditId) {
        this.creditId = creditId;
    }

    public ReferenceEntity getPaymenttypeId() {
        return paymenttypeId;
    }

    public void setPaymenttypeId(ReferenceEntity paymenttypeId) {
        this.paymenttypeId = paymenttypeId;
    }

    public ReferenceEntity getPaysumId() {
        return paysumId;
    }

    public void setPaysumId(ReferenceEntity paysumId) {
        this.paysumId = paysumId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public int getStatusId() {
    	return getStatus().ordinal();
    }
    
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public ExceptionInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ExceptionInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

	public AccountEntity getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountEntity accountId) {
		this.accountId = accountId;
	}

	public ReferenceEntity getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(ReferenceEntity accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
    public String getExternalId2() {
        return externalId2;
    }

    public void setExternalId2(String externalId2) {
        this.externalId2 = externalId2;
    }
}
