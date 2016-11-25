package ru.simplgroupp.reports.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Payment;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 *
 */
public class PaymentModel {
    private static final Logger logger = LoggerFactory.getLogger(PaymentModel.class);
    public static Constructor getWrapConstructor() {
        return wrapConstructor;
    }
    protected static Constructor wrapConstructor;
    static {
        try {
            wrapConstructor = PaymentModel.class.getConstructor(Payment.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public PaymentModel() {
    }

    public PaymentModel(Payment payment) {
        createDate = payment.getCreateDate();
        processDate = payment.getProcessDate();
        client = payment.getCredit().getCreditRequest().getPeopleMain().getPeoplePersonalActive().getFullName();
        amount = payment.getAmount();
        partner = payment.getPartner() != null ? payment.getPartner().getName() : "";
        direction = payment.getPaymentType().getName();
        status = PaymentStatus.getStatusName(payment.getStatus());
    }
    private Date createDate;
    private Date processDate;
    private String client;
    private double amount;
    private String partner;
    private String direction;
    private String status;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
