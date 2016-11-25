package ru.simplgroupp.client.common.facade.payment;

import ru.simplgroupp.persistence.PaymentEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.naming.AuthenticationException;

/**
 * Winpay facade
 */
@Local
public interface WinpayFacade {

    WinpayConfig getWinpayConfig() throws AuthenticationException, ObjectNotFoundException;

    WinpayRequest createPaymentRequest(double sum, String successUrl, String failureUrl) throws AuthenticationException,
            ObjectNotFoundException;

}
