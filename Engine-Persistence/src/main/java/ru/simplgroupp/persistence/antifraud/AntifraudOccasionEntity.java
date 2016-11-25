package ru.simplgroupp.persistence.antifraud;

import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.UsersEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * подозрение в мошенничестве
 */
public class AntifraudOccasionEntity extends ExtendedBaseEntity implements Serializable {
    private static final long serialVersionUID = -3357232413440497541L;

    protected Integer txVersion = 0;

    /**
     * объект из Национального Хантера
     */
    private ReferenceEntity hunterObject;

    /**
     * статус мошенничества
     */
    private ReferenceEntity status;

    /**
     * создано
     */
    private Date createdAt;

    /**
     * обновлено
     */
    private Date updatedAt;

    /**
     * активная ли запись
     */
    private Integer isActive = 1;

    /**
     * дата выставляется когда мы передали в Хантер но разотметили в интерфейсе
     */
    private Date dataEnd;

    /**
     * комментарий к записи
     */
    private String comment;

    /**
     * пользователь добавивший запись
     */
    private UsersEntity user;

    /**
     * вариант ответа по которому могла быть добавлена запись
     */
    private QuestionVariantEntity questionOption;


    public AntifraudOccasionEntity() {
    }

    public ReferenceEntity getHunterObject() {
        return hunterObject;
    }

    public void setHunterObject(ReferenceEntity hunterObject) {
        this.hunterObject = hunterObject;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ReferenceEntity getStatus() {
        return status;
    }

    public void setStatus(ReferenceEntity status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public QuestionVariantEntity getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(QuestionVariantEntity questionOption) {
        this.questionOption = questionOption;
    }
}
