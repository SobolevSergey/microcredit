package ru.simplgroupp.persistence;


import java.io.Serializable;
import java.util.Date;

/**
 * История смены рабочих мест пользователя
 */
public class UserWorkplaceHistoryEntity extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 3461050638211218524L;

    protected Integer txVersion = 0;

    /**
     * пользователь
     */
    private UsersEntity user;

    /**
     * место работы
     */
    private WorkplaceEntity workplace;

    /**
     * дата изменения
     */
    private Date modifiedDate;

    /**
     * кто менял
     */
    private UsersEntity modifiedBy;

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public WorkplaceEntity getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UsersEntity getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UsersEntity modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
