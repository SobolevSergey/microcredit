package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * другое предложение условий клиенту
 */
public class CreditOfferEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7148675527763862300L;

	protected Integer txVersion = 0;
	 /**
     * дней заявки
     */
    private Integer creditDays;

    /**
     * сумма заявки
     */
    private Double creditSum;

    /**
     * ставка заявки
     */
    private Double creditStake;
    /**
     * время предложения
     */
    private Date offerTime;
    /**
     * комментарий
     */
    private String comment;
    /**
     * было ли принято предложение
     */
    private Integer accepted;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    /**
     * пользователь
     */
    private UsersEntity userId;
    /**
     * как было предложено (автоматически или вручную)
     */
    private ReferenceEntity offerWayId;
    
    public CreditOfferEntity(){
    	
    }

	public Integer getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(Integer creditDays) {
		this.creditDays = creditDays;
	}

	public Double getCreditSum() {
		return creditSum;
	}

	public void setCreditSum(Double creditSum) {
		this.creditSum = creditSum;
	}

	public Double getCreditStake() {
		return creditStake;
	}

	public void setCreditStake(Double creditStake) {
		this.creditStake = creditStake;
	}

	public Date getOfferTime() {
		return offerTime;
	}

	public void setOfferTime(Date offerTime) {
		this.offerTime = offerTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getAccepted() {
		return accepted;
	}

	public void setAccepted(Integer accepted) {
		this.accepted = accepted;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}

	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
	}

	public UsersEntity getUserId() {
		return userId;
	}

	public void setUserId(UsersEntity userId) {
		this.userId = userId;
	}

	public ReferenceEntity getOfferWayId() {
		return offerWayId;
	}

	public void setOfferWayId(ReferenceEntity offerWayId) {
		this.offerWayId = offerWayId;
	}
    
    
}
