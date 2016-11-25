package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Платёжная система Контакт
 * @author irina
 *
 */
public interface ContactAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "contactAcq";

    String SYSTEM_DESCRIPTION = "Получение платежей через Контакт";
}
