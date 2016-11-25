package ru.simplgroupp.client.common.rest.payment;

/**
 * Winpay request
 */
public class WinpayPaymentResponse {

    private String paymentUrl;

    private String data;

    private String sign;

    public WinpayPaymentResponse(String paymentUrl, String data, String sign) {
        this.paymentUrl = paymentUrl;
        this.data = data;
        this.sign = sign;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getData() {
        return data;
    }

    public String getSign() {
        return sign;
    }
}
