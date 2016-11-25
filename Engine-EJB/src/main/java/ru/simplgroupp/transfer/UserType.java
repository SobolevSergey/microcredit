package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.util.Set;

import ru.simplgroupp.persistence.UsertypeEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class UserType implements Serializable, Initializable {

    private static final long serialVersionUID = 1834941929946368971L;

    protected UsertypeEntity entity;

    public UserType() {
        entity = new UsertypeEntity();
    }

    public UserType(UsertypeEntity value) {
        entity = value;

    }

    public Integer getId() {
        return entity.getId();
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public UsertypeEntity getEntity() {
        return entity;
    }

    @Override
    public void init(Set options) {
        entity.getName();
    }
}

