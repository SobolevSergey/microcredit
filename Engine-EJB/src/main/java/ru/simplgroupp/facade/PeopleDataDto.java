package ru.simplgroupp.facade;

import java.io.Serializable;

/**
 * People data DTO
 */
public class PeopleDataDto implements Serializable {

    private static final long serialVersionUID = 5291301549314339639L;

    private PeopleDataDto peopleData;

    private AdditionalDataDto additionalData;

    public PeopleDataDto getPeopleData() {
        return peopleData;
    }

    public void setPeopleData(PeopleDataDto peopleData) {
        this.peopleData = peopleData;
    }

    public AdditionalDataDto getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(AdditionalDataDto additionalData) {
        this.additionalData = additionalData;
    }
}
