package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.antifraud.RefHunterRuleEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class RefHunterRule extends BaseTransfer<RefHunterRuleEntity> implements Serializable, Initializable, Identifiable {
    public static final String HUNTER_OBJECT_MAIN_APPLICANT = "MA";
    public static final String HUNTER_OBJECT_PASSPORT = "MA_PAS";
    public static final String HUNTER_OBJECT_REGISTRATION_ADDRESS = "MA_RAD";
    public static final String HUNTER_OBJECT_REGISTER_PHONE = "MA_RTEL";
    public static final String HUNTER_OBJECT_RESIDENCE_ADDRESS = "MA_CAD";
    public static final String HUNTER_OBJECT_RESIDENCE_PHONE = "MA_CTEL";
    public static final String HUNTER_OBJECT_MOBILE_PHONE = "MA_MTEL";
    public static final String HUNTER_OBJECT_EMPLOYMENT = "MA_EMP";
    public static final String HUNTER_OBJECT_EMPLOYMENT_PHONE = "MA_ETEL";
    public static final String HUNTER_OBJECT_EMPLOYMENT_ADDRESS = "MA_EAD";
    public static final String HUNTER_OBJECT_EMAIL = "MA_EMA";
    private static final long serialVersionUID = 3180894000424079359L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = RefHunterRule.class.getConstructor(RefHunterRuleEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public RefHunterRule() {
        super();
        entity = new RefHunterRuleEntity();
    }

    public RefHunterRule(RefHunterRuleEntity value) {
        super(value);
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
    }

    @Override
    public Object getId() {
        return entity.getId();
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public String getCode() {
        return entity.getCode();
    }

    public void setCode(String code) {
        entity.setCode(code);
    }

    public Integer getIsActive() {
        return entity.getIsActive();
    }

    public void setIsActive(Integer isActive) {
        entity.setIsActive(isActive);
    }

    public enum Options {
    }
}

