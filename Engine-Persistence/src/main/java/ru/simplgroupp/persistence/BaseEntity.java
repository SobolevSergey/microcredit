package ru.simplgroupp.persistence;

import ru.simplgroupp.toolkit.common.Identifiable;

/**
 * основной класс, от которого наследуют все entity
 */
public abstract class BaseEntity implements Cloneable, Identifiable {
    protected Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Одинаковое ли содержание у объектов, исключая служебные поля.
     *
     * @param other
     * @return [null - неизвестно|true|false]
     */
    public Boolean equalsContent(BaseEntity other) {
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (!(other.getClass() == getClass())) {
            return false;
        }

        BaseEntity entity = (BaseEntity) other;

        if (this.id == null) {
            return false;
        }

        if (entity.getId() == null) {
            return false;
        }

        return this.id.equals(entity.getId());
    }

    @Override
    public BaseEntity clone() throws CloneNotSupportedException {
        BaseEntity clone = (BaseEntity) super.clone();
        clone.id = null;
        return clone;
    }
}
