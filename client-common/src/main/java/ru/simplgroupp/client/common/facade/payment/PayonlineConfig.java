package ru.simplgroupp.client.common.facade.payment;

import java.io.Serializable;

/**
 * Payonline config
 */
public class PayonlineConfig implements Serializable {

    private static final long serialVersionUID = -8743754919771760948L;

    private Integer merchantId;

    private Integer verificationMerchantId;

    private String cardUrl;

    private String verificationUrl;

    public PayonlineConfig(Integer merchantId, Integer verificationMerchantId, String cardUrl, String verificationUrl) {
        this.merchantId = merchantId;
        this.verificationMerchantId = verificationMerchantId;
        this.cardUrl = cardUrl;
        this.verificationUrl = verificationUrl;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public Integer getVerificationMerchantId() {
        return verificationMerchantId;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }
}
