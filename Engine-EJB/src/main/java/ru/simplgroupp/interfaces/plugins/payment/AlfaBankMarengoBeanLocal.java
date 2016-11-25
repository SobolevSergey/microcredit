package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Платёжная система Альфа-банка Маренго
 * @author irina
 *
 */
public interface AlfaBankMarengoBeanLocal extends PaymentSystemLocal {

    String SYSTEM_NAME = "alfaMarengo";

    String SYSTEM_DESCRIPTION = "Отправка платежей через Альфа-банк";
}
