package ru.simplgroupp.interfaces.plugins.payment;


/**
 * Приём платежей через yandex money
 */
public interface YandexAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "yandexAcq";

    String SYSTEM_DESCRIPTION = "Приём платежей через Яндекс";
}
