package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Справочник стран
 */

public class CountryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1820975930701284834L;

    private Integer txVersion = 0;

    /**
     * код
     */
    private String code;

    /**
     * название
     */
    private String name;

    /**
     * числовой код
     */
    private Integer codeinteger;

    /**
     * расширенный код
     */
    private String codeextend;

    public CountryEntity() {
    }

    public CountryEntity(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCodeinteger() {
        return codeinteger;
    }

    public void setCodeinteger(Integer codeinteger) {
        this.codeinteger = codeinteger;
    }

    public String getCodeextend() {
        return codeextend;
    }

    public void setCodeextend(String codeextend) {
        this.codeextend = codeextend;
    }

    public CountryEntity clone() throws CloneNotSupportedException {
        CountryEntity ent = (CountryEntity) super.clone();
        return ent;
    }

}
