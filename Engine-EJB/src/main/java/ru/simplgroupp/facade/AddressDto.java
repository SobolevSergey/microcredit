package ru.simplgroupp.facade;

import java.io.Serializable;

/**
 * Address data DTO
 */
public class AddressDto implements Serializable {

    private static final long serialVersionUID = 8403545417707720982L;

    private String guid;

    private String house;

    private String corpus;

    private String building;

    private String flat;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public void clear() {
        guid = null;
        house = null;
        corpus = null;
        building = null;
        flat = null;
    }
}
