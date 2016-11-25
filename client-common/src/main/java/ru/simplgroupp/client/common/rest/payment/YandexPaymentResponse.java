package ru.simplgroupp.client.common.rest.payment;

import java.io.Serializable;

/**
 * Yandex payment response
 */
public class YandexPaymentResponse implements Serializable {

    private static final long serialVersionUID = 1830774102050817967L;

    private Long shopId;

    private Long scid;

    private Integer customerNumber;

    private String paymentUrl;

    private Integer orderNumber;

    public YandexPaymentResponse(Long shopId, Long scid, Integer customerNumber, String paymentUrl,
            Integer orderNumber) {
        this.shopId = shopId;
        this.scid = scid;
        this.customerNumber = customerNumber;
        this.paymentUrl = paymentUrl;
        this.orderNumber = orderNumber;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
