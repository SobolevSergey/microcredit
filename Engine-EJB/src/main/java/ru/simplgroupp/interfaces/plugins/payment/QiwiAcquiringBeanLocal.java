package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Плугин для оплаты чере qiwi
 */
public interface QiwiAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "qiwiAcq";

    String SYSTEM_DESCRIPTION = "Получение платежей через Qiwi";

    /**
     * Выставить счёт
     * @return
     */
    String doOrder(String phone, Integer paymentId, PluginExecutionContext context) throws ActionException;

	String getPayUrl(PluginExecutionContext context);

}
