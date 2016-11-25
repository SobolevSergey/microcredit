package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Справочник с оценками для верификации кредитов
 */
public class VerificationConfigEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6881881278443748379L;

    private Integer txVersion = 0;

    /**
     * максимально допустимое расхождение
     */
    private Double diff;

    /**
     * оценка
     */
    private Double rating;

    /**
     * Используется ли для соответствия кредитов
     */
    private boolean useForMatching;

    /**
     * поле, по которому сравниваем
     */
    private VerificationReferenceEntity fieldId;


    public VerificationConfigEntity() {

    }

    public Double getDiff() {
        return diff;
    }

    public void setDiff(Double diff) {
        this.diff = diff;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isUseForMatching() {
        return useForMatching;
    }

    public void setUseForMatching(boolean useForMatching) {
        this.useForMatching = useForMatching;
    }

    public VerificationReferenceEntity getFieldId() {
        return fieldId;
    }

    public void setFieldId(VerificationReferenceEntity fieldId) {
        this.fieldId = fieldId;
    }

}
