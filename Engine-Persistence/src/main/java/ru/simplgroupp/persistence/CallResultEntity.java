package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * результат разговора с клиентом
 * используется как правило только для коллектора
 *
 */
public class CallResultEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1810166760314215240L;
	
	protected Integer txVersion = 0;
  
    /**
     * дата звонка
     */
    private Date dateCall;
    /**
     * дата обещания оплаты
     */
    private Date datePromise;
    /**
     * дата следующего контакта
     */
    private Date dateNextContact;
    /**
     * описание 
     */
    private String comment;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * результат
     */
    private ReferenceEntity resultTypeId;
    /**
     * пользователь
     */
    private UsersEntity userId;
    
    public CallResultEntity(){
    	
    }
    
	
	public Date getDateCall() {
		return dateCall;
	}
	public void setDateCall(Date dateCall) {
		this.dateCall = dateCall;
	}
	public Date getDatePromise() {
		return datePromise;
	}
	public void setDatePromise(Date datePromise) {
		this.datePromise = datePromise;
	}
	public Date getDateNextContact() {
		return dateNextContact;
	}
	public void setDateNextContact(Date dateNextContact) {
		this.dateNextContact = dateNextContact;
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
	public ReferenceEntity getResultTypeId() {
		return resultTypeId;
	}
	public void setResultTypeId(ReferenceEntity resultTypeId) {
		this.resultTypeId = resultTypeId;
	}
	public UsersEntity getUserId() {
		return userId;
	}
	public void setUserId(UsersEntity userId) {
		this.userId = userId;
	}
  
}
