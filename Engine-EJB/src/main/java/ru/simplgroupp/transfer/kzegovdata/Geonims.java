package ru.simplgroupp.transfer.kzegovdata;

import java.io.Serializable;

/**
 * Класс инкапсулирует сущность сервиса Адресный регистр
 * "Устройство населенных пунктов"
 * Портал Открытые данные Электронного правительства Республики Казахстан
 * http://data.egov.kz
 *
 * Пример:
 * {
 * "id": 177417,
 * "name_kaz": "Алғабас",
 * "rco": "0004000000061013",
 * "cato": "",
 * "name_rus": "Алгабас",
 * "s_ats_id": 177415,
 * "actual": true,
 * "d_geonims_type_id": 11,
 * "parent_id": 0
 * },
 *
 * @author  Rustem Saidaliyev
 *
 */
public class Geonims implements Serializable {

    private Long geonimsTypeId; // ID Тип составных частей населенных пунктов. Примеры: 11 - Улица, 12 - Бульвар, 17 - Дорога и т.д.

    private String geonimsTypeNameRu; // Наименование на русском языке Тип составных частей населенных пунктов

    private Long id; // ID

    private String nameKz; // Наименование на казахском языке

    private String rco; // TODO пока неизвестно что за параметр

    private String cato; // Классификатор административно-территориальных объектов

    private String nameRu; // Наименование на русском языке

    private Boolean actual; // Признак актуальности

    private Long parentId; // ID родителя

    private Long atsId; // ID Тип административно-территориального устройства. Примеры: 2 - Поселок, 7 - Область, 1 - Село и т.д.


    public Long getGeonimsTypeId() {
        return geonimsTypeId;
    }

    public void setGeonimsTypeId(Long geonimsTypeId) {
        this.geonimsTypeId = geonimsTypeId;
    }

    public String getGeonimsTypeNameRu() {
        return geonimsTypeNameRu;
    }

    public void setGeonimsTypeNameRu(String geonimsTypeNameRu) {
        this.geonimsTypeNameRu = geonimsTypeNameRu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameKz() {
        return nameKz;
    }

    public void setNameKz(String nameKz) {
        this.nameKz = nameKz;
    }

    public String getRco() {
        return rco;
    }

    public void setRco(String rco) {
        this.rco = rco;
    }

    public String getCato() {
        return cato;
    }

    public void setCato(String cato) {
        this.cato = cato;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getAtsId() {
        return atsId;
    }

    public void setAtsId(Long atsId) {
        this.atsId = atsId;
    }

    @Override
    public String toString() {
        return String.format("geonimsTypeId = %s, geonimsTypeNameRu = %s, id = %s, nameKz = %s, rco = %s, cato = %s, nameRu = %s, actual = %s, parentId = %s, atsId = %s",
                geonimsTypeId, geonimsTypeNameRu, id, nameKz, rco, cato, nameRu, actual, parentId, atsId);
    }

}
