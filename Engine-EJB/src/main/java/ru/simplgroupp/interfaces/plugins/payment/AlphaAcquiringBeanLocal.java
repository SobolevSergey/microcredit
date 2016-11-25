package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Эквайринговая система Альфа-банка
 * @author irina
 *
 */
public interface AlphaAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "alphaAcq";

    String SYSTEM_DESCRIPTION = "Получение платежей через Альфа-банк";
}
