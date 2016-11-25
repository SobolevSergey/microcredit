package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

/**
 * Интерфейс бухгалтерской системы 1С
 * @author irina
 *
 */
public interface Account1CBeanLocal extends PluginSystemLocal, AccountingBeanLocal {

    String SYSTEM_NAME = "account1C";

    String SYSTEM_DESCRIPTION = "Передача данных в 1С-бухгалтерию";
}
