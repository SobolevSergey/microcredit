package ru.simplgroupp.client.common.rest.payment;

import java.io.Serializable;

/**
 * Payment request
 */
public class PaymentRequest implements Serializable {

    private static final long serialVersionUID = 1799937748775336946L;

    private Double sum;

    private String account;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
