package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Справочник для видов бонусов
 */
public class RefBonusEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3801103747936620845L;
    protected Integer txVersion = 0;

    /**
     * название
     */
    private String name;

    /**
     * количество бонусов
     */
    private Double amount;

    /**
     * флаг активности
     */
    private Integer isactive;

    /**
     * код числовой
     */
    private Integer codeinteger;

    /**
     * код символьный
     */
    private String code;

    /**
     * процент бонуса, когда количество баллов нам надо высчитывать
     */
    private Double rate;


    public RefBonusEntity() {
    }

    public RefBonusEntity(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Integer getCodeinteger() {
        return codeinteger;
    }

    public void setCodeinteger(Integer codeinteger) {
        this.codeinteger = codeinteger;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
