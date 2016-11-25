package ru.simplgroupp.interfaces.plugins.payment;

/**
 * Плугин для автоматического списания с карты через payonline
 */
public interface PayonlineRebillBeanLocal extends AcquiringBeanLocal {

    String SYSTEM_NAME = "payonlineRebillAcq";

    String SYSTEM_DESCRIPTION = "Автоматическое списание с карты через Payonline";
}
