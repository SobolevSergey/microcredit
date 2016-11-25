package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.VerificationConfigEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class VerificationConfig extends BaseTransfer<VerificationConfigEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 613240370788138014L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = VerificationConfig.class.getConstructor(VerificationConfigEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public static final int MARK_FOR_BANK=25;

    private VerificationReference fieldId;

    public VerificationConfig() {
        super();
        entity = new VerificationConfigEntity();
    }

    public VerificationConfig(VerificationConfigEntity value) {
        super(value);
        fieldId = new VerificationReference(value.getFieldId());
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    public Double getDiff() {
        return entity.getDiff();
    }

    public void setDiff(Double diff) {
        entity.setDiff(diff);
    }

    public Double getRating() {
        return entity.getRating();
    }

    public void setRating(Double rating) {
        entity.setRating(rating);
    }

    public boolean isUseForMatching() {
        return entity.isUseForMatching();
    }

    public void setUseForMatching(boolean useForMatching) {
        entity.setUseForMatching(useForMatching);
    }

    public VerificationReference getFieldId() {
        return fieldId;
    }

    public void setFieldId(VerificationReference fieldId) {
        this.fieldId = fieldId;
    }
}

