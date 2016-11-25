package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Плугин для выдачи займов через yandex
 */
public interface YandexPayBeanLocal extends PaymentSystemLocal {

    String  SYSTEM_NAME = "yandexPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через Яндекс";

    String status(Integer paymentId, PluginExecutionContext context) throws ActionException;

    boolean sendSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException;

    double balance(PluginExecutionContext context) throws ActionException, KassaException;

    boolean isWalletIdentified(String number, PluginExecutionContext context) throws ActionException;

    boolean checkWalletData(String number, PeopleMainEntity person, PluginExecutionContext context) throws ActionException;
}
