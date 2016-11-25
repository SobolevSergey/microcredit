package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * расписываем суммы кредита подробно - штрафы, комиссии, проценты итд
 */
public class CreditSumsEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 761474047778695097L;
	
	protected Integer txVersion = 0;
     /**
     * дата
     */
    private Date amountDate;
    /**
     * тип суммы 
     */
    private ReferenceEntity sumId;
    /**
     * тип операции 
     */
    private ReferenceEntity operationId;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * детали кредита
     */
    private CreditDetailsEntity creditDetailsId;
    /**
     * сумма 
     */
    private Double amount;
    
    public CreditSumsEntity(){
    	
    }
   
	public Date getAmountDate() {
		return amountDate;
	}

	public void setAmountDate(Date amountDate) {
		this.amountDate = amountDate;
	}

	public ReferenceEntity getSumId() {
		return sumId;
	}

	public void setSumId(ReferenceEntity sumId) {
		this.sumId = sumId;
	}

	public ReferenceEntity getOperationId() {
		return operationId;
	}

	public void setOperationId(ReferenceEntity operationId) {
		this.operationId = operationId;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	public CreditDetailsEntity getCreditDetailsId() {
		return creditDetailsId;
	}

	public void setCreditDetailsId(CreditDetailsEntity creditDetailsId) {
		this.creditDetailsId = creditDetailsId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
    
}
