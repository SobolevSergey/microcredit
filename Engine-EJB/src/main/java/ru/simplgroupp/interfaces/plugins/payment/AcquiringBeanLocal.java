package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

/**
 * Платёжная система по приёму платежей
 * @author irina
 *
 */
public interface AcquiringBeanLocal extends PluginSystemLocal {

    /**
	 * Возвращает типы счетов, с которых система может получать деньги. 
	 * Типы счетов берём из ru.simplgroupp.transfer.Account.*_TYPE;
	 * @return идентификаторы поддерживаемых типов аккаунтов
     */
	int[] getSupportedAccountTypes();
}
