package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

public class PeopleBonusEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9027304990538400930L;
	
	protected Integer txVersion = 0;
  
    /**
     * дата
     */
    private Date eventDate;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * кол-во бонусов
     */
    private Double amount;
    /**
     * кол-во бонусов в рублях
     */
    private Double amountrub;
    /**
     * название бонусов
     */
    private RefBonusEntity bonusId;
    /**
     * начисление или снятие бонусов
     */
    private ReferenceEntity operationId;    
    /**
     * кредит, за который дали бонус
     */
    private CreditEntity creditId;
    /**
     * человек, за которого дали бонус
     */
    private PeopleMainEntity peopleMainBonusId;
    
    public PeopleBonusEntity(){
    	
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	public PeopleMainEntity getPeopleMainBonusId() {
		return peopleMainBonusId;
	}

	public void setPeopleMainBonusId(PeopleMainEntity peopleMainBonusId) {
		this.peopleMainBonusId = peopleMainBonusId;
	}

	public ReferenceEntity getOperationId() {
		return operationId;
	}

	public void setOperationId(ReferenceEntity operationId) {
		this.operationId = operationId;
	}

	public RefBonusEntity getBonusId() {
		return bonusId;
	}

	public void setBonusId(RefBonusEntity bonusId) {
		this.bonusId = bonusId;
	}

	public Double getAmountrub() {
		return amountrub;
	}

	public void setAmountrub(Double amountrub) {
		this.amountrub = amountrub;
	}
}
