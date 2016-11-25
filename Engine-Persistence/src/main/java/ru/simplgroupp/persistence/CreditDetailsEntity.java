package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * кредит, операции подробно, что когда и как
 */
public class CreditDetailsEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3824588441737625225L;

	protected Integer txVersion = 0;

    /**
     * дата
     */
    private Date eventDate;
    /**
     * дата окончания
     */
    private Date eventEndDate;
    /**
     * тип операции 
     */
    private ReferenceEntity operationId;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * сумма основная
     */
    private Double amountMain;
    /**
     * сумма процентов
     */
    private Double amountPercent;
    /**
     * сумма штрафов
     */
    private Double amountPenalty;
    /**
     * сумма общая
     */
    private Double amountAll;
    /**
     * сумма операции, если есть
     */
    private Double amountOperation;
    /**
     * id из другой таблицы, относящейся к делу
     */
    private Integer anotherId;
    /**
     * дней просрочки
     */
    private Integer delay;
    /**
     * сумма переплаты, если есть
     */
    private Double amountOverpay;
    
    public CreditDetailsEntity(){
    	
    }
    
  	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
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

	public Double getAmountMain() {
		return amountMain;
	}

	public void setAmountMain(Double amountMain) {
		this.amountMain = amountMain;
	}

	public Double getAmountPercent() {
		return amountPercent;
	}

	public void setAmountPercent(Double amountPercent) {
		this.amountPercent = amountPercent;
	}

	public Double getAmountAll() {
		return amountAll;
	}

	public void setAmountAll(Double amountAll) {
		this.amountAll = amountAll;
	}
    
	public Double getAmountOperation() {
		return amountOperation;
	}

	public void setAmountOperation(Double amountOperation) {
		this.amountOperation = amountOperation;
	}

	public Date getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public Integer getAnotherId() {
		return anotherId;
	}

	public void setAnotherId(Integer anotherId) {
		this.anotherId = anotherId;
	}

	public Double getAmountPenalty() {
		return amountPenalty;
	}

	public void setAmountPenalty(Double amountPenalty) {
		this.amountPenalty = amountPenalty;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Double getAmountOverpay() {
		return amountOverpay;
	}

	public void setAmountOverpay(Double amountOverpay) {
		this.amountOverpay = amountOverpay;
	}

	
	
	
}
