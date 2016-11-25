package ru.simplgroupp.ejb;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class PaymentFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date from;

    private Date to;

    /**
     * 0 - время создания
     * 1 - время проведения
     */
    private int dateKind = 0;
    
    private Integer partnerId;

    private Integer status;

    /**
     * Фамилия
     */
    private String lastName;
    /**
     * Имя
     */
    private String name;
    /**
     * Отчество
     */
    private String midname;

    private Integer paymentTypeId;
    
    private Integer creditId;
    
    private Integer accountId;
    
    private Integer accountTypeId;
    
    private Boolean isPaid;

    private Integer workplace_id;

    /**
     * Установлено хоть одно значение в фильтре
     */
    private Boolean cleared = true;
    /**
     * Оператор колл центра
     */
    private Boolean isCallCenterOnly = false;

    /**
     *  клиент
     */
    private Integer peopleMainId;

    public PaymentFilter() {
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
        if (from != null) cleared = false;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
        if (to != null) cleared = false;
    }

    public int getDateKind() {
        return dateKind;
    }

    public void setDateKind(int dateKind) {
        this.dateKind = dateKind;
    }

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
        if (partnerId != null) cleared = false;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) cleared = false;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        if (lastName != null) cleared = false;
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        this.midname = midname;
        if (midname != null) cleared = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null) cleared = false;
    }

    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
        if (paymentTypeId != null) cleared = false;
    }

	public Integer getCreditId() {
		return creditId;
	}

	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
        if (creditId != null) cleared = false;
    }

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
        if (accountId != null) cleared = false;
	}

	public Integer getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Integer accountTypeId) {
		this.accountTypeId = accountTypeId;
        if (accountTypeId != null) cleared = false;
    }

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
        if (isPaid != null) cleared = false;
    }

    public Integer getWorkplace_id() {
        return workplace_id;
    }

    public void setWorkplace_id(Integer workplace_id) {
        this.workplace_id = workplace_id;
        if (workplace_id != null) cleared = false;
    }

    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    public Integer getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(Integer peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public Boolean isCallCenterOnly() {
        return isCallCenterOnly;
    }

    public void setCallCenterOnly(Boolean callCenterOnly) {
        isCallCenterOnly = callCenterOnly;
    }
}
