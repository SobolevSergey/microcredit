package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * виды пользователей
 */
public class UsertypeEntity implements Serializable {

    private static final long serialVersionUID = 2818369746052318558L;

    private Integer id;
    /**
     * название
     */
    private String name;


    public UsertypeEntity() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        UsertypeEntity cast = (UsertypeEntity) other;

        return this.id != null && cast.getId() != null && this.id.intValue() == cast.getId().intValue();

    }

   
}
