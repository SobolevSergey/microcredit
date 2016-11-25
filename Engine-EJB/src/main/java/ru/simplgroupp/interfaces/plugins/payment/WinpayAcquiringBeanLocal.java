package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Плугин winpay для оплаты кредита
 */
public interface WinpayAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "winpayAcq";

    String SYSTEM_DESCRIPTION = "Приём платежей через WinPay";
}
