package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.VerificationConfigEntity;
import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VerificationReference extends BaseTransfer<VerificationReferenceEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 613240370788138014L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = VerificationReference.class.getConstructor(VerificationReferenceEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private List<VerificationConfig> configurations = new ArrayList<>();


    public VerificationReference() {
        super();
        entity = new VerificationReferenceEntity();
    }

    public VerificationReference(VerificationReferenceEntity value) {
        super(value);
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        // конфигурации
        if (options != null && options.contains(Options.INIT_CONFIGURATIONS)) {
            Utils.initCollection(entity.getConfigurations(), options);
            for (VerificationConfigEntity lazyEntity : entity.getConfigurations()) {
                VerificationConfig lazyTransfer = new VerificationConfig(lazyEntity);
                lazyTransfer.init(options);
                configurations.add(lazyTransfer);
            }
        }
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public String getEntityName() {
        return entity.getEntityName();
    }

    public void setEntityName(String entityName) {
        entity.setEntityName(entityName);
    }

    public String getFieldName() {
        return entity.getFieldName();
    }

    public void setFieldName(String fieldName) {
        entity.setFieldName(fieldName);
    }

    public boolean isUseForRate() {
        return entity.isUseForRate();
    }

    public void setUseForRate(boolean useForRate) {
        entity.setUseForRate(useForRate);
    }

    public boolean isNecessary() {
        return entity.isNecessary();
    }

    public void setNecessary(boolean necessary) {
        entity.setNecessary(necessary);
    }

    public List<VerificationConfig> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<VerificationConfig> configurations) {
        this.configurations = configurations;
    }

    public enum Options {
        INIT_CONFIGURATIONS;
    }
}

