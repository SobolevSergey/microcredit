package ru.simplgroupp.client.common.rest.payment;

import java.io.Serializable;

/**
 * Payonline check amount request
 */
public class PayonlineCheckAmountRequest implements Serializable {

    private static final long serialVersionUID = 3905078414376310725L;

    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
