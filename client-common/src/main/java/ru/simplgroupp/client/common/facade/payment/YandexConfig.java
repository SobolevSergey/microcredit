package ru.simplgroupp.client.common.facade.payment;

import java.io.Serializable;

/**
 * Yandex config
 */
public class YandexConfig implements Serializable {

    private static final long serialVersionUID = 4470774373314441190L;

    private Long shopId;

    private Long scid;

    private Integer customerNumber;

    private String paymentUrl;

    public YandexConfig(Long shopId, Long scid, Integer customerNumber, String paymentUrl) {
        this.shopId = shopId;
        this.scid = scid;
        this.customerNumber = customerNumber;
        this.paymentUrl = paymentUrl;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getScid() {
        return scid;
    }

    public void setScid(Long scid) {
        this.scid = scid;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}