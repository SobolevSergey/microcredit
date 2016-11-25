package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Winpay выдача кредита (через iframe)
 */
public interface WinpayFramePayLocal extends PaymentSystemLocal {

    String SYSTEM_NAME = "winpayFramePay";

    String SYSTEM_DESCRIPTION = "Выдача заёма через Winpay (iframe)";

    /**
     * Баланс в системе win-pay
     * @param context контекст
     * @return баланс в сисете win-pay
     * @throws ActionException
     * @throws KassaException
     */
    double balance(PluginExecutionContext context) throws ActionException, KassaException;
}
