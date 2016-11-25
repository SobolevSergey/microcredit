package ru.simplgroupp.fixtures;

import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Payment;

import java.util.Date;

/**
 * Created by kds on 16.07.14.
 */
public class PaymentFixture {

    public static Payment createPayment(Credit credit) {

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setCreditId(credit.getEntity());
        paymentEntity.setAmount(credit.getCreditSumBack());
        paymentEntity.setCreateDate(new Date());
        paymentEntity.setId(Long.valueOf(System.currentTimeMillis()).intValue());

        return new Payment(paymentEntity);
    }
}
