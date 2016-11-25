package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Платёжная система Arius
 * @author
 *
 */
public interface AriusAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "ariusAcq";

    String SYSTEM_DESCRIPTION = "Получение платежей через Ариус";
}
