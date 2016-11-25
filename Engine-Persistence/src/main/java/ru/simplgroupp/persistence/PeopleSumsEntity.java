package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * суммы людей, дебет-кредит
 */
public class PeopleSumsEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4402632626485225630L;
	
	protected Integer txVersion = 0;
    private Integer id;
    /**
     * дата
     */
    private Date eventDate;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * вид операции - плюс или минус
     */
    private ReferenceEntity operationId;
    /**
     * тип операции, на какие цели
     */
    private ReferenceEntity operationTypeId;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * сумма
     */
    private Double amount;
    
    public PeopleSumsEntity(){
    	
    }
    
    public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public ReferenceEntity getOperationId() {
		return operationId;
	}

	public void setOperationId(ReferenceEntity operationId) {
		this.operationId = operationId;
	}

	public ReferenceEntity getOperationTypeId() {
		return operationTypeId;
	}

	public void setOperationTypeId(ReferenceEntity operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}
    
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	
}
