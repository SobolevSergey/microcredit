package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Звонки
 */

public class CallsEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1476861347798923461L;
	protected Integer txVersion = 0;
  
    /**
     * начало звонка
     */
    private Date calldatabeg;
    /**
     * окончание звонка
     */
    private Date calldataend;

    /**
     * разговор
     */
    private byte[] calldata;

    /**
     * комментарии
     */
    private String comment;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * сотрудник (пользователь)
     */
    private UsersEntity userId;

    /**
     * статус звонка
     */
    private ReferenceEntity statusId;

    /**
     * номер, на который звонили
     */
    private String phone;

    /**
     * звонил ли сам клиент
     */
    private Integer isClientCall;

    /**
     * true - входящий, иначе исходящий звонок
     */
    private Boolean incoming;


    public CallsEntity() {
    }

    public Date getCalldatabeg() {
        return calldatabeg;
    }

    public void setCalldatabeg(Date calldatabeg) {
        this.calldatabeg = calldatabeg;
    }

    public Date getCalldataend() {
        return calldataend;
    }

    public void setCalldataend(Date calldataend) {
        this.calldataend = calldataend;
    }

    public byte[] getCalldata() {
        return calldata;
    }

    public void setCalldata(byte[] calldata) {
        this.calldata = calldata;
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

    public UsersEntity getUserId() {
        return userId;
    }

    public void setUserId(UsersEntity userId) {
        this.userId = userId;
    }

	public ReferenceEntity getStatusId() {
		return statusId;
	}

	public void setStatusId(ReferenceEntity statusId) {
		this.statusId = statusId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getIsClientCall() {
		return isClientCall;
	}

	public void setIsClientCall(Integer isClientCall) {
		this.isClientCall = isClientCall;
	}


    public Boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(Boolean incoming) {
        this.incoming = incoming;
    }
}
