package ru.simplgroupp.client.common.rest.payment;

/**
 * Winpay payment request
 */
public class WinpayPaymentRequest extends PaymentRequest {

    private static final long serialVersionUID = -3998788775187235878L;

    private String successUrl;

    private String failureUrl;

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
}
