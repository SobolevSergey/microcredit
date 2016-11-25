package ru.simplgroupp.client.common.facade.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PaymentEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.naming.AuthenticationException;

/**
 * Yandex facade
 */
@Local
public interface YandexFacade {

    YandexConfig getYandexConfig() throws AuthenticationException, ObjectNotFoundException;

    PaymentEntity createPayment(double sum) throws AuthenticationException, ObjectNotFoundException;
}
