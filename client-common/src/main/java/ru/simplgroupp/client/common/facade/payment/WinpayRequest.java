package ru.simplgroupp.client.common.facade.payment;

/**
 * WinpayRequest
 */
public class WinpayRequest {

    private String data;

    private String sign;

    public WinpayRequest(String data, String sign) {
        this.data = data;
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public String getSign() {
        return sign;
    }
}
