package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * данные по работе коллектора
 */
public class CollectorEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 909498012434764210L;

	protected Integer txVersion = 0;
 
    /**
     * дата начала
     */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * описание 
     */
    private String comment;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * вид коллектора
     */
    private ReferenceEntity collectionTypeId;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * пользователь
     */
    private UsersEntity userId;
    /**
     * запись в работе или нет
     */
    private Integer isActive;
    
    public CollectorEntity(){
    	
    }

	public Date getDatabeg() {
		return databeg;
	}

	public void setDatabeg(Date databeg) {
		this.databeg = databeg;
	}

	public Date getDataend() {
		return dataend;
	}

	public void setDataend(Date dataend) {
		this.dataend = dataend;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public ReferenceEntity getCollectionTypeId() {
		return collectionTypeId;
	}

	public void setCollectionTypeId(ReferenceEntity collectionTypeId) {
		this.collectionTypeId = collectionTypeId;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	public UsersEntity getUserId() {
		return userId;
	}

	public void setUserId(UsersEntity userId) {
		this.userId = userId;
	}
    
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	
}
