package ru.simplgroupp.services;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Credit data dto
 */
public class CreditDataDto implements Serializable {

    private static final long serialVersionUID = -126841368250729197L;

    @NotNull
    private Double amount;

    @NotNull
    private Integer days;

    @NotNull
    private Integer productId;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
