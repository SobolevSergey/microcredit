package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Получение платежей из банка в формате 1С
 * @author irina
 *
 */
public interface Account1CAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "account1CAcq";

    String SYSTEM_DESCRIPTION = "Получение платежей из банка в формате 1С";
}
