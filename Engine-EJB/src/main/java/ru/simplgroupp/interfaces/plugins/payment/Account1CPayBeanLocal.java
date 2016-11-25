package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

/**
 * Интерфейс бухгалтерской системы 1С для перечисления денег клиенту
 * @author irina
 *
 */
public interface Account1CPayBeanLocal extends PluginSystemLocal, PaymentSystemLocal {

    String SYSTEM_NAME = "account1CPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей в банк в формате 1С";
}
