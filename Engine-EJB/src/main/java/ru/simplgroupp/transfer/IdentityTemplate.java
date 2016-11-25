package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.IdentityTemplateEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class IdentityTemplate extends BaseTransfer<IdentityTemplateEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 8624051166656209616L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = IdentityTemplate.class.getConstructor(IdentityTemplateEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }


    public IdentityTemplate() {
        super();
        entity = new IdentityTemplateEntity();
    }

    public IdentityTemplate(IdentityTemplateEntity value) {
        super(value);
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

    public Integer getPoints() {
        return entity.getPoints();
    }

    public void setPoints(Integer points) {
        entity.setPoints(points);
    }

    public String getValue() {
        return entity.getValue();
    }

    public void setValue(String value) {
        entity.setValue(value);
    }

    public Boolean getFake() {
        return entity.getFake();
    }

    public void setFake(Boolean fake) {
        entity.setFake(fake);
    }
}

