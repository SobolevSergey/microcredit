package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * Справочник для верификации займов и ПД в системе
 */
public class VerificationReferenceEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8671424300889766982L;

    private Integer txVersion = 0;

    /**
     * название
     */
    private String name;

    /**
     * название таблицы
     */
    private String entityName;

    /**
     * название поля
     */
    private String fieldName;

    /**
     * используется ли для расчета оценки
     */
    private boolean useForRate;

    /**
     * необходимое поле для расчета
     */
    private boolean necessary;

    /**
     * конфигурации
     */
    private List<VerificationConfigEntity> configurations;


    public VerificationReferenceEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isUseForRate() {
        return useForRate;
    }

    public void setUseForRate(boolean useForRate) {
        this.useForRate = useForRate;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public void setNecessary(boolean necessary) {
        this.necessary = necessary;
    }

    public List<VerificationConfigEntity> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<VerificationConfigEntity> configurations) {
        this.configurations = configurations;
    }
}
