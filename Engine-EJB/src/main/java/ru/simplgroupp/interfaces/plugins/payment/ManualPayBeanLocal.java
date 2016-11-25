package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

/**
 * Интерфейс для ручного перечисления денег клиенту
 * @author irina
 *
 */
public interface ManualPayBeanLocal extends PluginSystemLocal, PaymentSystemLocal {

    String SYSTEM_NAME = "manualPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей вручную";
}
