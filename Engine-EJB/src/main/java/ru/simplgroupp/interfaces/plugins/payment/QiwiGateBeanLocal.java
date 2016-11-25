package ru.simplgroupp.interfaces.plugins.payment;

import javax.ejb.Local;

/**
 * Плугин приёма платежей через qiwi
 */
@Local
public interface QiwiGateBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "qiwiGateAcq";

    String SYSTEM_DESCRIPTION = "Приём платежей через Qiwi";
}
