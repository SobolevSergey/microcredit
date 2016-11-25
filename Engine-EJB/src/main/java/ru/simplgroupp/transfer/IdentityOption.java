package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.IdentityAnswerEntity;
import ru.simplgroupp.persistence.IdentityOptionEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class IdentityOption extends BaseTransfer<IdentityOptionEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = -7354895560593252312L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = IdentityOption.class.getConstructor(IdentityOptionEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private IdentityQuestion question;


    public IdentityOption() {
        super();
        entity = new IdentityOptionEntity();
    }

    public IdentityOption(IdentityOptionEntity value) {
        super(value);

        question = new IdentityQuestion(entity.getQuestion());
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public IdentityOptionEntity getEntity() {
        return entity;
    }

    public IdentityQuestion getQuestion() {
        return question;
    }

    public void setQuestion(IdentityQuestion question) {
        this.question = question;
    }

    public IdentityAnswerEntity getAnswer() {
        return entity.getAnswer();
    }

    public void setAnswer(IdentityAnswerEntity answer) {
        entity.setAnswer(answer);
    }

    public Boolean getCorrect() {
        return entity.getCorrect();
    }

    public void setCorrect(Boolean correct) {
        entity.setCorrect(correct);
    }

    public String getValue() {
        return entity.getValue();
    }

    public void setValue(String value) {
        entity.setValue(value);
    }
}

