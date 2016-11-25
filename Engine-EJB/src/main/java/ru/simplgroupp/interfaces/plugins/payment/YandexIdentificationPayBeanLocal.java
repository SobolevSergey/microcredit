package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Плугин для выдачи займов через yandex
 */
public interface YandexIdentificationPayBeanLocal extends PaymentSystemLocal {

    String  SYSTEM_NAME = "yandexIdentificationPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через Яндекс с передачей персональных данных";

    String status(Integer paymentId, PluginExecutionContext context) throws ActionException;

    boolean sendSingleRequest(PaymentEntity entity, PluginExecutionContext plctx) throws ActionException;

    boolean querySingleResponse(PaymentEntity entity, PluginExecutionContext plctx) throws ActionException;

    double balance(PluginExecutionContext context) throws ActionException, KassaException;
}
