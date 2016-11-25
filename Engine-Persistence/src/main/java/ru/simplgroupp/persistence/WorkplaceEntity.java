package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Рабочее место пользователя
 */
public class WorkplaceEntity extends BaseEntity implements Serializable{

    private static final long serialVersionUID = -6181963250521377894L;

    protected Integer txVersion = 0;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
