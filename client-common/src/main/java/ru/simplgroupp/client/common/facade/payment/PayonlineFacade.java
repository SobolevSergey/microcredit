package ru.simplgroupp.client.common.facade.payment;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PaymentEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.naming.AuthenticationException;

/**
 * Payonline service
 */
@Local
public interface PayonlineFacade {

    PayonlineConfig getPayonlineConfig();

    PaymentEntity createPayment(double sum) throws AuthenticationException, ObjectNotFoundException;

    String signPaymentRequest(String orderId, String amount);

    String signVerificationRequest(String orderId) throws AuthenticationException, ObjectNotFoundException;

    boolean checkAmount(String guid, double amount) throws AuthenticationException, ObjectNotFoundException;

    void deleteVerification(String guid) throws AuthenticationException, ObjectNotFoundException;
}
