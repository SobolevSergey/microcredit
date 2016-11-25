package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Плугин payonline для оплаты кредита
 */
public interface PayonlineAcquiringBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "payonlineAcq";

    String SYSTEM_DESCRIPTION = "Приём платежей через Payonline";

    int paymentStatus(Long paymentId, PluginExecutionContext context) throws KassaException;

    void paymentCancel(String orderId, String transactionId, PluginExecutionContext context) throws KassaException;
}
