package ru.simplgroupp.transfer.antifraud;

import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class AntifraudOccasion extends BaseTransfer<AntifraudOccasionEntity> implements Serializable, Initializable {
    public static final int STATUS_CLEAR = 1;
    public static final int STATUS_AUTO_SUSPECT = 2;
    public static final int STATUS_MANUAL_SUSPECT = 5;
    public static final int STATUS_REFER = 7;
    private static final long serialVersionUID = 3180894000424079359L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = AntifraudOccasion.class.getConstructor(AntifraudSuspicionEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private CreditRequest creditRequestId;

    private PeopleMain peopleMainId;

    private Reference hunterObject;

    private Reference status;

    private Users user;

    private QuestionVariant questionOption;


    public AntifraudOccasion() {
        super();
        entity = new AntifraudOccasionEntity();
    }

    public AntifraudOccasion(AntifraudOccasionEntity value) {
        super(value);

        if (entity.getCreditRequestId() != null) {
            creditRequestId = new CreditRequest(entity.getCreditRequestId());
        }

        if (entity.getPeopleMainId() != null) {
            peopleMainId = new PeopleMain(entity.getPeopleMainId());
        }

        if (entity.getHunterObject() != null) {
            hunterObject = new Reference(entity.getHunterObject());
        }

        if (entity.getStatus() != null) {
            status = new Reference(entity.getStatus());
        }

        if (entity.getUser() != null) {
            user = new Users(entity.getUser());
        }

        if (entity.getQuestionOption() != null) {
            questionOption = new QuestionVariant(entity.getQuestionOption());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        //персональные данные людей
        if (options != null && options.contains(PeopleMain.Options.INIT_PEOPLE_PERSONAL)) {
            if (user != null) {
                user.init(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
            }
            peopleMainId.init(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
        }
    }

    public Integer getId() {
        return entity.getId();
    }

    public Reference getHunterObject() {
        return hunterObject;
    }

    public void setHunterObject(Reference hunterObject) {
        this.hunterObject = hunterObject;
    }

    public Date getCreatedAt() {
        return entity.getCreatedAt();
    }

    public void setCreatedAt(Date createdAt) {
        entity.setCreatedAt(createdAt);
    }

    public Date getUpdatedAt() {
        return entity.getUpdatedAt();
    }

    public void setUpdatedAt(Date updatedAt) {
        entity.setUpdatedAt(updatedAt);
    }

    public CreditRequest getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequest creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public PeopleMain getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMain peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public Reference getStatus() {
        return status;
    }

    public void setStatus(Reference status) {
        this.status = status;
    }

    public Integer getIsActive() {
        return entity.getIsActive();
    }

    public void setIsActive(Integer active) {
        entity.setIsActive(active);
    }

    public Date getDataEnd() {
        return entity.getDataEnd();
    }

    public void setDataEnd(Date dataEnd) {
        entity.setDataEnd(dataEnd);
    }

    public String getComment() {
        return entity.getComment();
    }

    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public Users getUser() {
        return user;
    }

    public QuestionVariant getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(QuestionVariant questionOption) {
        this.questionOption = questionOption;
    }

    public enum Options {
    }
}

