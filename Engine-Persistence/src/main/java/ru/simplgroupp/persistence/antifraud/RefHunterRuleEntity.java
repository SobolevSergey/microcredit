package ru.simplgroupp.persistence.antifraud;

import ru.simplgroupp.persistence.BaseEntity;

import java.io.Serializable;

/**
 * правила внутреннего АМ
 */
public class RefHunterRuleEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8556918433806286030L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * код
     */
    private String code;

    /**
     * название
     */
    private String name;

    /**
     * активное правило
     */
    private Integer isActive;


    public RefHunterRuleEntity() {

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
