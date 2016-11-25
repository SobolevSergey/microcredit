package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * недееспособность человека
 */
public class PeopleIncapacityEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6106011855904565925L;

	protected Integer txVersion = 0;
	
	 /**
     * дата актуальности
     */
    private Date incapacityDate;
    /**
     * вид недееспособности
     */
    private ReferenceEntity incapacityId;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;   
    /**
     * источник информации (партнер)
     */
    private PartnersEntity partnersId;
    /**
     * Заявка
     */
    private CreditRequestEntity creditRequestId;
    
    public PeopleIncapacityEntity(){
    	
    }
    
  
	public Date getIncapacityDate() {
		return incapacityDate;
	}

	public void setIncapacityDate(Date incapacityDate) {
		this.incapacityDate = incapacityDate;
	}

	public ReferenceEntity getIncapacityId() {
		return incapacityId;
	}

	public void setIncapacityId(ReferenceEntity incapacityId) {
		this.incapacityId = incapacityId;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public PartnersEntity getPartnersId() {
		return partnersId;
	}

	public void setPartnersId(PartnersEntity partnersId) {
		this.partnersId = partnersId;
	}

	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}

	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
	}
   
}
