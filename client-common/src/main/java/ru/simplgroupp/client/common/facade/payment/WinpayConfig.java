package ru.simplgroupp.client.common.facade.payment;

/**
 * Winpay config
 */
public class WinpayConfig {

    private String paymentUrl;

    public WinpayConfig(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
