package ru.simplgroupp.persistence;

/**
 * класс, от которого наследуют основные entity
 */
public abstract class ExtendedBaseEntity extends BaseEntity {
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;


    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }


}
