package ru.simplgroupp.interfaces.plugins.payment;

/**
 * @author aro
 */
public interface ContactCancelPayBeanLocal extends PaymentSystemLocal {
    String SYSTEM_NAME = "contactCancelPay";

    String SYSTEM_DESCRIPTION = "Отмена платежа,отправленного ранее через систему Контакт";

}
