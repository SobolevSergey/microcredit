package ru.simplgroupp.interfaces.plugins.payment;

import javax.ejb.Local;

/**
 * Плугин для выдачи займов через qiwi
 */
@Local
public interface QiwiPayBeanLocal extends PaymentSystemLocal {

    String SYSTEM_NAME = "qiwiGatePay";

    String SYSTEM_DESCRIPTION = "Выдача займов через Qiwi";
}
