package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * данные людей для перезванивания сотрудников 
 */
public class CallBackEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6357795442808940053L;
	protected Integer txVersion = 0;
	
    /**
     * дата запроса
     */
    private Date dateRequest;
    /**
     * дата звонка
     */
    private Date dateCall;
    /**
     * фамилия
     */
    private String surname;
    /**
     * имя
     */
    private String name;
    /**
     * телефон
     */
    private String phone;
    /**
     * email
     */
    private String email;
    /**
     * сотрудник (пользователь)
     */
    private UsersEntity userId;
    /**
     * запись активна или нет
     */
    private Integer isActive;
    /**
     * сообщение
     */
    private String message;
    /**
     * вид отправителя
     */
    private ReferenceEntity typeId;

    public CallBackEntity() {

    }

	public Date getDateRequest() {
		return dateRequest;
	}

	public void setDateRequest(Date dateRequest) {
		this.dateRequest = dateRequest;
	}

	public Date getDateCall() {
		return dateCall;
	}

	public void setDateCall(Date dateCall) {
		this.dateCall = dateCall;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
    
	
	 public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReferenceEntity getTypeId() {
		return typeId;
	}

	public void setTypeId(ReferenceEntity typeId) {
		this.typeId = typeId;
	}
}
