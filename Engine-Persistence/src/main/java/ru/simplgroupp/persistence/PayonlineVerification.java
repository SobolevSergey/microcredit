package ru.simplgroupp.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Payonline verification
 */
@Entity
@Table(name = "payonline_verification")
public class PayonlineVerification {

    @Id
    private String guid;

    @ManyToOne
    private PeopleMainEntity people;

    @Column(name = "masked_card_number")
    private String maskedCardNumber;

    @Column(name = "rebill_anchor")
    private String rebillAnchor;

    public PayonlineVerification() {
    }

    public PayonlineVerification(String guid, PeopleMainEntity people) {
        this.guid = guid;
        this.people = people;
    }

    public PeopleMainEntity getPeople() {
        return people;
    }

    public String getGuid() {
        return guid;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getRebillAnchor() {
        return rebillAnchor;
    }

    public void setRebillAnchor(String rebillAnchor) {
        this.rebillAnchor = rebillAnchor;
    }
}
