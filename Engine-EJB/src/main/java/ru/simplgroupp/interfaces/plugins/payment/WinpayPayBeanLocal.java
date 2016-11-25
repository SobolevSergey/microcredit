package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Плугин для выдачи займов через win-pay
 */
public interface WinpayPayBeanLocal extends PaymentSystemLocal {

    String SYSTEM_NAME = "winpayPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через WinPay";

    /**
     * Отправить платёж в win-pay
     * @param payment платёж
     * @param context контекст
     * @return Возвращает true, если ответ получен сразу же, и повторный запрос не нужен
     * @throws ActionException
     */
    boolean pay(PaymentEntity payment, PluginExecutionContext context) throws ActionException;

    /**
     * Запросить статус в win-pay
     * @param payment платёж
     * @param context контекст
     * @return Возвращает true, если ответ получен
     * @throws ActionException
     */
    boolean status(PaymentEntity payment, PluginExecutionContext context) throws ActionException;

    /**
     * Запросить статус в win-pay
     * @param paymentId ID платёжа
     * @param context контекст
     * @return Возвращает строку ответа winpay
     * @throws ActionException
     */
    String status(Integer paymentId, PluginExecutionContext context) throws ActionException;

    /**
     * Баланс в системе win-pay
     * @param context контекст
     * @return баланс в сисете win-pay
     * @throws ActionException
     * @throws KassaException
     */
    double balance(PluginExecutionContext context) throws ActionException, KassaException;
}
