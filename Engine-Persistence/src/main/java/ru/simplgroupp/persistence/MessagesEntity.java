package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * разные сообщения - авто и ручные
 *
 */
public class MessagesEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5798268890121735664L;
	protected Integer txVersion = 0;
	
	 /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * сотрудник (пользователь)
     */
    private UsersEntity userId;
    /**
     * вид сообщения
     */
    private ReferenceEntity messageTypeId;
    /**
     * как было послано
     */
    private ReferenceEntity messageWayId;
    /**
     * дата сообщения
     */
    private Date messageDate;
    /**
     * заголовок сообщения
     */
    private String messageHeader;
    /**
     * текст сообщения
     */
    private String messageBody;

	/**
	 * Контакт
	 */
	private PeopleContactEntity peopleContactId;

	/**
	 * Статус сообщения
	 * Прочитано	- 1
	 * Непрочитано	- 0
	 */
	private Integer status;

	/**
	 * Тип сообщения
	 * Входящее 	- 1
	 * Исходящее 	- 0
	 */
    private Integer inOut;

    public MessagesEntity(){
    	
    }
    
  	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public UsersEntity getUserId() {
		return userId;
	}

	public void setUserId(UsersEntity userId) {
		this.userId = userId;
	}

	public ReferenceEntity getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(ReferenceEntity messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	public ReferenceEntity getMessageWayId() {
		return messageWayId;
	}

	public void setMessageWayId(ReferenceEntity messageWayId) {
		this.messageWayId = messageWayId;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public String getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public PeopleContactEntity getPeopleContactId() {
		return peopleContactId;
	}

	public void setPeopleContactId(PeopleContactEntity peopleContactId) {
		this.peopleContactId = peopleContactId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getInOut() {
		return inOut;
	}

	public void setInOut(Integer inOut) {
		this.inOut = inOut;
	}
}
