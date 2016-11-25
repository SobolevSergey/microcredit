package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class AntifraudField extends BaseTransfer<AntifraudFieldEntity> implements Serializable, Initializable, Identifiable {

    private static final long serialVersionUID = -9185539090398687600L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    protected PeopleMain peopleMain;
    protected CreditRequest creditRequest;
    protected AntifraudFieldType antifraudFieldType;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = AntifraudField.class.getConstructor(AntifraudFieldEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public AntifraudField() {
        super();
        entity = new AntifraudFieldEntity();
    }

    public AntifraudField(AntifraudFieldEntity ent) {
        super(ent);
        if (ent.getPeopleMainId() != null) {
            peopleMain = new PeopleMain(ent.getPeopleMainId());
        }
        if (ent.getCreditRequestId() != null) {
            creditRequest = new CreditRequest(ent.getCreditRequestId());
        }
        antifraudFieldType = new AntifraudFieldType(ent.getType());
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public AntifraudFieldType getAntifraudFieldType() {
        return antifraudFieldType;
    }

    public void setAntifraudFieldType(AntifraudFieldType antifraudFieldType) {
        this.antifraudFieldType = antifraudFieldType;
    }

    public String getValueBefore() {
        return entity.getValueBefore();
    }

    public void setValueBefore(String valueBefore) {
        entity.setValueBefore(valueBefore);
    }

    public String getValueAfter() {
        return entity.getValueAfter();
    }

    public void setValueAfter(String valueAfter) {
        entity.setValueAfter(valueAfter);
    }

    public Date getCreateAt() {
        return entity.getCreateAt();
    }

    public void setCreateAt(Date createAt) {
        entity.setCreateAt(createAt);
    }

    public PeopleMainEntity getPeopleMainId() {
        return entity.getPeopleMainId();
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        entity.setPeopleMainId(peopleMainId);
    }

    public CreditRequestEntity getCreditRequestId() {
        return entity.getCreditRequestId();
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        entity.setCreditRequestId(creditRequestId);
    }

    public String getSessionId() {
        return entity.getSessionId();
    }

    public void setSessionId(String sessionId) {
        entity.setSessionId(sessionId);
    }

    @Override
    public void init(Set options) {

    }
}
