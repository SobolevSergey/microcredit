package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class AntifraudFieldType extends BaseTransfer<AntifraudFieldTypesEntity> implements Serializable, Initializable, Identifiable {

    private static final long serialVersionUID = -9185539090398687601L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = AntifraudFieldType.class.getConstructor(AntifraudFieldTypesEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public AntifraudFieldType() {
        super();
        entity = new AntifraudFieldTypesEntity();
    }

    public AntifraudFieldType(AntifraudFieldTypesEntity ent) {
        super(ent);
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getType() {
        return entity.getType();
    }

    public void setType(String type) {
        entity.setType(type);
    }

    public String getDescription() {
        return entity.getDescription();
    }

    public void setDescription(String description) {
        entity.setDescription(description);
    }

    @Override
    public void init(Set options) {

    }
}
