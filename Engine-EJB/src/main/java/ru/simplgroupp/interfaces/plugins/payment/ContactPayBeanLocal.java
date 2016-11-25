package ru.simplgroupp.interfaces.plugins.payment;

import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.ejb.plugins.payment.ContactPayPluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * @author sniffl
 */
public interface ContactPayBeanLocal extends PaymentSystemLocal {
    String SYSTEM_NAME = "contactPay";

    String SYSTEM_DESCRIPTION = "Отправка платежей через систему Контакт";

    /**
     * Загружает словари из Контакта,метод устарел, поскольку пытается загрузить весь объем данных из справочников и это вызывает ReadTimeout
     * @param test - режим работы
     * @return true если результат положительный
     * @throws ActionException
     *
     */
    boolean getDictionaries(boolean test) throws ActionException;

    /**
     * Метод для проверки связи с сервером Контакта
     * @param test - режим работы
     * @return ответ от Контакта в виде структуры CdtrnCXR
     * @throws ActionException
     */
    CdtrnCXR doPing(boolean test) throws ActionException;

    /**
     * Загружает словари из Контакта. Загружает словари по частям. Работает довольно долго, объем большой.
     * @return true если результат положительный
     * @throws ActionException
     */
    boolean getFullDicByParts() throws ActionException;

    /**
     * Возвращает остаток денег на счете компании в системе Контакт
     * @param config - контекст
     * @return число- остаток на счете, в рублях
     * @throws ActionException
     */
    double doCheckRest(PluginExecutionContext config) throws ActionException;

    /**
     * Делает запрос к контакту, возвращает ответ от Контакта, в котором отражено состояние платежа с ID = paymentId
     * PaymentId - идентификатор в нашей базе.
     * @param paymentId идентификатор платежа в нашей базе.
     * @param pluginConfig - контекст
     * @return строка, которую возвращает Контакт
     * @throws ActionException
     */
    String doGetPaymentInfoString(Integer paymentId,ContactPayPluginConfig pluginConfig) throws ActionException;
}
