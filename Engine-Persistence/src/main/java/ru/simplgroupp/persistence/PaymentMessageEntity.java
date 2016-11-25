package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Сообщение по платежу
 */
public class PaymentMessageEntity extends BaseEntity implements Serializable {
    /**
     * Платежная система
     */
    private ReferenceEntity paymentId;
    /**
     * Дата сообщения
     */
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ReferenceEntity getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(ReferenceEntity paymentId) {
        this.paymentId = paymentId;
    }
}
