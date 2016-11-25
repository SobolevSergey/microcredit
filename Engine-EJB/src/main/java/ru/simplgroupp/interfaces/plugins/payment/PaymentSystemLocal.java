package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

/**
 * Внешняя платёжная система по перечислению денег на счёт клиента
 */
public interface PaymentSystemLocal extends PluginSystemLocal {

    /**
     * Возвращает типы счетов, на которые система может перечислять деньги.
     * Типы счетов берём из ru.simplgroupp.transfer.Account.*_TYPE;
     *
     * @return идентификаторы поддерживаемых типов аккаунтов
     */
    int[] getSupportedAccountTypes();
}
