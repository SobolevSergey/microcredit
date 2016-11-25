package ru.simplgroupp.transfer.kzegovdata;

import java.io.Serializable;

/**
 * Класс инкапсулирует сущность сервиса Адресный регистр
 * "Административно-территориальное устройство Республики Казахстан"
 * Портал Открытые данные Электронного правительства Республики Казахстан
 * http://data.egov.kz
 *
 * Пример:
 * {
 * "d_ats_type_id": 7,
 * "id": 125189,
 * "name_kaz": "ҚОСТАНАЙ",
 * "rco": "0003000000008690",
 * "cato": "390000000",
 * "name_rus": "КОСТАНАЙСКАЯ",
 * "actual": true,
 * "parent_id": 1
 * },
 *
 * @author  Rustem Saidaliyev
 *
 */
public class Ats implements Serializable {

    private Long atsTypeId; // ID Тип административно-территориального устройства. Примеры: 2 - Поселок, 7 - Область, 1 - Село и т.д.

    private String atsTypeNameRu; // Наименование на русском языке Тип административно-территориального устройства

    private Long id; // ID

    private String nameKz; // Наименование на казахском языке

    private String rco; // TODO пока неизвестно что за параметр

    private String cato; // Классификатор административно-территориальных объектов

    private String nameRu; // Наименование на русском языке

    private Boolean actual; // Признак актуальности

    private Long parentId; // ID родителя


    public Long getAtsTypeId() {
        return atsTypeId;
    }

    public void setAtsTypeId(Long atsTypeId) {
        this.atsTypeId = atsTypeId;
    }

    public String getAtsTypeNameRu() {
        return atsTypeNameRu;
    }

    public void setAtsTypeNameRu(String atsTypeNameRu) {
        this.atsTypeNameRu = atsTypeNameRu;
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

    @Override
    public String toString() {
        return String.format("atsTypeId = %s, atsTypeNameRu = %s, id = %s, nameKz = %s, rco = %s, cato = %s, nameRu = %s, actual = %s, parentId = %s",
                atsTypeId, atsTypeNameRu, id, nameKz, rco, cato, nameRu, actual, parentId);
    }

}
