package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.TaskEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

/**
 * 10.09.2015
 * 17:29
 */

public class Task extends BaseTransfer<TaskEntity> implements Serializable, Initializable, Identifiable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -6202377836780844237L;

	public static Integer CLIENT_CALL_TASK = 1;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = Collector.class.getConstructor(TaskEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private PeopleMain peopleMainId;
    private Users userId;
    private Reference taskType;
    private Collector collectorId;
    private Credit creditId;
    private CreditRequest creditRequestId;

    public Task() {
        super();
        entity = new TaskEntity();
    }

    public Task(TaskEntity value){
        super(value);
        if (entity.getPeopleMainId() != null) {
            peopleMainId = new PeopleMain(entity.getPeopleMainId());
        }
        if (entity.getUserId() != null) {
            userId = new Users(entity.getUserId());
        }
        taskType = new Reference(entity.getTaskType());
        if (entity.getCollectorId() != null) {
            collectorId = new Collector(entity.getCollectorId());
        }
        if (entity.getCreditId() != null) {
            creditId = new Credit(entity.getCreditId());
        }
        if (entity.getCreditRequestId() != null) {
            creditRequestId = new CreditRequest(entity.getCreditRequestId());
        }
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public Reference getTaskType() {
        return taskType;
    }

    public void setTaskType(Reference taskType) {
        this.taskType = taskType;
    }

    public Date getEventDate() {
        return entity.getEventDate();
    }

    public void setEventDate(Date eventDate) {
        entity.setEventDate(eventDate);
    }

    public Date getEndDate() {
        return entity.getEndDate();
    }

    public void setEndDate(Date endDate) {
        entity.setEndDate(endDate);
    }

    public PeopleMain getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMain peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Collector getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Collector collectorId) {
        this.collectorId = collectorId;
    }

    public Integer getStatus() {
        return entity.getStatus();
    }

    public void setStatus(Integer status) {
        entity.setStatus(status);
    }

    public Credit getCreditId() {
        return creditId;
    }

    public void setCreditId(Credit creditId) {
        this.creditId = creditId;
    }

    public CreditRequest getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequest creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    @Override
    public void init(Set options) {
        peopleMainId.init(options);
        userId.init(options);
        if (collectorId != null) {
            collectorId.getUser().init(options);
            collectorId.getPeopleMain().init(options);
        }
    }
}
