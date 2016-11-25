package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.ejb.arius.*;

/**
 * Сервис для осуществления запросов к платежной системе Arius
 */
public interface AriusService {

    /**
     * Операция по блокировке небольшой суммы, например 2 рубля
     * Номер карты вводится в форме на сайте Ариуса
     * @param creditRequestId
     * @return Ответ от Ариуса, в котором содержится URL формы Ариуса для ввода карты
     */
    AriusPreauthFormResponse doPreauth(Integer creditRequestId);

    /**
     * Операция по возврату блокированной суммы на карте клиента системой Ариус
     * @param orderId - идентификатор Ариуса операции блокировки суммы, который приходит как параметр на сервлет,
     *                  который ловит редирект от Ариуса после заполнения формы
     * @return Ответ от Ариуса со статусом операции
     */
    AriusReturnResponse doReturn(String orderId);

    /**
     * Операция по регистрации карты клиента в системе Ариус
     * @param orderId - идентификатор Ариуса операции блокировки суммы, который приходит как параметр на сервлет,
     *                  который ловит редирект от Ариуса после заполнения формы
     * @return Ответ от Ариуса, где главное это card_ref_id - это псевдоним карты для проведения по нему операций
     */
    AriusCreateCardRefResponse doCreateCardRef(String orderId);

    /**
     * Операция по переводу денег от нас на карту клиента, используя  card_ref_id
     * @param creditRequestId - id кредитной заявки
     * @return Ответ, в котором ВАЖНЫ 2 идентификатора, котоорые нужно сохранить: paynet-order-id и serial-number
     */
    AriusTransferResponse doTransfer(Integer creditRequestId);

    /**
     * Операция определения статуса перевода денег
     * @param externalId - это paynet-order-id, полученный в ответе операции перевода
     * @param externalId2 - это serial-number, полученный в ответе операции перевода
     * @return
     */
    AriusTransferStatusResponse doTransferStatus(String externalId,String externalId2);

    /**
     * Стартует процесс обработки параметров редиректа от Ариуса, стартует процесс кредита, который запускает AriusPayBean
     * @param redirectResponse
     * @return результат выполнения
     */
    boolean startProcessAfterPreauthRedirect(AriusRedirectResponse redirectResponse);


    /**
     * Операция по переводу денег к нам с пластиковой карты клиента
     * @param creditId ID средита
     * @param sum сумма, которую клиент хочет перевести
     * @return Ответ от Ариуса, в котором содержится URL формы Ариуса для ввода карты
     */
    AriusSaleFormResponse doSale(Integer creditId, Double sum);



    /**
     * Стартует процесс обработки параметров редиректа от Ариуса, стартует процесс погашения кредита, который запускает AriusAcquiringBean
     * @param redirectResponse
     * @return результат выполнения
     */
    boolean startProcessAfterSaleRedirect(AriusRedirectResponse redirectResponse);

}
