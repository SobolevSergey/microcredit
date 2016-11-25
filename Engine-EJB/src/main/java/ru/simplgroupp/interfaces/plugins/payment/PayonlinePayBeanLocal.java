package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Плугин для выдачи займов через payonline на карту
 */
public interface PayonlinePayBeanLocal extends PaymentSystemLocal {

    String  SYSTEM_NAME = "payonlinePay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через Payonline";
}
