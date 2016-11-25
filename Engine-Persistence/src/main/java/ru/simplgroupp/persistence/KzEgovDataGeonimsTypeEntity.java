package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Портал Открытые данные Электронного правительства Республики Казахстан
 * http://data.egov.kz
 * Адресный регистр. Типы составных частей населенных пунктов (улицы, проспекты, переулки и т.д.)
 */
public class KzEgovDataGeonimsTypeEntity implements Serializable {

    // TODO перегенерить
	private static final long serialVersionUID = -5549318813737357298L;

    private Long id;
    private String thisIs;
    private String shortValueRu;
    private String valueRu;
    private String shortValueKz;
    private Boolean actual;
    private String valueKz;
    private Long code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThisIs() {
        return thisIs;
    }

    public void setThisIs(String thisIs) {
        this.thisIs = thisIs;
    }

    public String getShortValueRu() {
        return shortValueRu;
    }

    public void setShortValueRu(String shortValueRu) {
        this.shortValueRu = shortValueRu;
    }

    public String getValueRu() {
        return valueRu;
    }

    public void setValueRu(String valueRu) {
        this.valueRu = valueRu;
    }

    public String getShortValueKz() {
        return shortValueKz;
    }

    public void setShortValueKz(String shortValueKz) {
        this.shortValueKz = shortValueKz;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public String getValueKz() {
        return valueKz;
    }

    public void setValueKz(String valueKz) {
        this.valueKz = valueKz;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

}
