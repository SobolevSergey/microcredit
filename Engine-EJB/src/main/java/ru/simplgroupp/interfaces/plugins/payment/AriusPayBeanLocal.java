package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.exception.ActionException;

/**
 * @author sniffl
 */
public interface AriusPayBeanLocal extends PaymentSystemLocal {
    String SYSTEM_NAME = "ariusPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через систему arius";

    /**todo
     */
    boolean doSale(boolean test) throws ActionException;

}
