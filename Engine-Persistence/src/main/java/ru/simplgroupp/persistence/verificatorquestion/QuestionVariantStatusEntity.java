package ru.simplgroupp.persistence.verificatorquestion;

import ru.simplgroupp.persistence.BaseEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.ReferenceEntity;

import java.io.Serializable;

/**
 * сущность и ее статус если выбран определенный вариант ответа
 */
public class QuestionVariantStatusEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6518505811207505077L;
    protected Integer txVersion = 0;

    /**
     * сущность мошенничества
     */
    private ReferenceEntity antifraudEntity;

    /**
     * какой статус для этой сущности
     */
    private ReferenceEntity antifraudStatus;

    /**
     * вариант ответа
     */
    private QuestionVariantEntity option;


    public QuestionVariantStatusEntity() {
    }

    public ReferenceEntity getAntifraudEntity() {
        return antifraudEntity;
    }

    public void setAntifraudEntity(ReferenceEntity antifraudEntity) {
        this.antifraudEntity = antifraudEntity;
    }

    public ReferenceEntity getAntifraudStatus() {
        return antifraudStatus;
    }

    public void setAntifraudStatus(ReferenceEntity antifraudStatus) {
        this.antifraudStatus = antifraudStatus;
    }

    public QuestionVariantEntity getOption() {
        return option;
    }

    public void setOption(QuestionVariantEntity option) {
        this.option = option;
    }
}
