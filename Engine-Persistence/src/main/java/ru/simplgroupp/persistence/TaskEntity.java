package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * задачи
 */

public class TaskEntity extends BaseEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3007410474785540061L;
	/**
     * дата назначения 
     */
    private Date eventDate;
    /**
     * дата выполнения
     */
    private Date endDate;
    /**
     * тип задачи
     */
    private ReferenceEntity taskType;
    /**
     * пользователь
     */
    private UsersEntity userId;
    /**
     * связанный с задачей человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * запись коллектора
     */
    private CollectorEntity collectorId;
    /**
     * статус задачи
     */
    private Integer status;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * версия
     */
    protected Integer txVersion = 0;

    public TaskEntity() {
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ReferenceEntity getTaskType() {
        return taskType;
    }

    public void setTaskType(ReferenceEntity taskType) {
        this.taskType = taskType;
    }

    public UsersEntity getUserId() {
        return userId;
    }

    public void setUserId(UsersEntity userId) {
        this.userId = userId;
    }

    public CreditEntity getCreditId() {
        return creditId;
    }

    public void setCreditId(CreditEntity creditId) {
        this.creditId = creditId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public CollectorEntity getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(CollectorEntity collectorId) {
        this.collectorId = collectorId;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

   
}
