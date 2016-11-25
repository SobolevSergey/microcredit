package ru.simplgroupp.transfer.verificatorquestion;

import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.verificatorquestion.QuestionVariantStatusEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.transfer.BaseTransfer;
import ru.simplgroupp.transfer.Question;
import ru.simplgroupp.transfer.QuestionVariant;
import ru.simplgroupp.transfer.Reference;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class QuestionVariantStatus extends BaseTransfer<QuestionVariantStatusEntity> implements Initializable, Serializable, Identifiable {
    private static final long serialVersionUID = -2009570443209108115L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = QuestionVariantStatus.class.getConstructor(QuestionVariantEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private QuestionVariant option;

    private Reference antifraudStatus;

    private Reference antifraudEntity;


    public QuestionVariantStatus() {
        super();
        entity = new QuestionVariantStatusEntity();
    }

    public QuestionVariantStatus(QuestionVariantStatusEntity entity) {
        super(entity);

        if (entity.getOption() != null) {
            option = new QuestionVariant(entity.getOption());
        }

        if (entity.getAntifraudEntity() != null) {
            antifraudEntity = new Reference(entity.getAntifraudEntity());
        }

        if (entity.getAntifraudStatus() != null) {
            antifraudStatus = new Reference(entity.getAntifraudStatus());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    @Override
    public void init(Set options) {
    }

    public QuestionVariant getOption() {
        return option;
    }

    public void setOption(QuestionVariant option) {
        this.option = option;
    }

    public Reference getAntifraudStatus() {
        return antifraudStatus;
    }

    public void setAntifraudStatus(Reference antifraudStatus) {
        this.antifraudStatus = antifraudStatus;
    }

    public Reference getAntifraudEntity() {
        return antifraudEntity;
    }

    public void setAntifraudEntity(Reference antifraudEntity) {
        this.antifraudEntity = antifraudEntity;
    }
}
