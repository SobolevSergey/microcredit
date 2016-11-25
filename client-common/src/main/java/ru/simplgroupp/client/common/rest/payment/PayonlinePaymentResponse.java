package ru.simplgroupp.client.common.rest.payment;

import java.io.Serializable;

/**
 * Payonline payment response
 */
public class PayonlinePaymentResponse implements Serializable {

    private static final long serialVersionUID = -7498257776072724038L;

    private String orderId;

    private Integer merchantId;

    private String amount;

    private String url;

    private String securityKey;

    private String currency = "RUB";

    public PayonlinePaymentResponse(String orderId, Integer merchantId, String amount, String url, String
            securityKey) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.url = url;
        this.securityKey = securityKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
