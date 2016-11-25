package ru.simplgroupp.services;

import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.ejb.PaymentFilter;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.RepaymentScheduleEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PaymentService {

    /**
     * Создать платёж
     *
     * @param creditId id кредита
     * @param mode     - кто кому платит, система человеку или человек системе
     * @param type - вид платежа, например, банковский перевод
     * @param typesum - вид суммы, например, комиссия
     */
    Payment createPayment(int creditId, int type, int typesum, Double sumpay, Integer mode, int partnersId);

    /**
     * создать платеж
     * 
     * @param creditId id кредита
     * @param mode     - кто кому платит, система человеку или человек системе
     * @param type - вид платежа, например, банковский перевод
     * @param typesum - вид суммы, например, комиссия
     * @param externalId - идентификатор платежа в платежной системе, например в Контакт
     */
    Payment createPayment(int creditId, int type, int typesum, Double sumpay, Integer mode, String externalId, int partnersId, AccountEntity account, Date createDate);

    /**
     * создать платеж
     *
     * @param creditId id кредита
     * @param mode     - кто кому платит, система человеку или человек системе
     * @param type - вид платежа, например, банковский перевод
     * @param typesum - вид суммы, например, комиссия
     * @param externalId - идентификатор платежа в платежной системе, например в Контакт
     * @param externalId2 - идентификатор платежа в платежной системе, например в Ариус
     */
    Payment createPayment(int creditId, int type, int typesum, Double sumpay, Integer mode, String externalId,String externalId2, int partnersId, AccountEntity account, Date createDate);

    /**
     * Сохранить платёж
     *
     * @param pay
     */
    PaymentEntity savePayment(PaymentEntity pay);

    /**
     * сохранить платеж
     * @param payment - платеж
     */
    void savePaymentWithAccount(Payment payment);
    /**
     * Список платежей
     *
     * @param firstRow - первая запись
     * @param rows - записей на страницу
     * @param sorting - сортировка
     * @param options - что инициализируем
     * @param filter - условия поиска
     */
    List<Payment> findPayments(int firstRow, int rows, SortCriteria[] sorting, Set options, PaymentFilter filter);

    List<PaymentEntity> findPayments(int firstRow, int rows,SortCriteria[] sorting, PaymentFilter filter);
    /**
     * Кол-во платежей
     *
     * @param filter - условия поиска
     */
    int countPayments(PaymentFilter filter);

    /**
     * Найти платеж от клиента через определенную систему и определенного вида, 
     * если берем все системы, то <code>partnerId</code> = 0
     * если берем все виды, то <code>paysumId</code> = 0
     *
     * @param creditRequestId id заявки на кредит
     * @param partnerId       id партнёра
     * @param paysumId        id суммы платежа
     * @param dt              дата платежа
     * @return платёж
     */
    PaymentEntity getPaymentFromClient(int creditRequestId, int partnerId, int paysumId,Date dt);
    /**
     * Найти платеж от клиента через определенную систему и определенного вида, 
     * если берем все системы, то <code>partnerId</code> = 0
     * если берем все виды, то <code>paysumId</code> = 0
     *
     * @param creditRequestId id заявки на кредит
     * @param partnerId       id партнёра
     * @param paysumId        id суммы платежа
     * @param dt              дата платежа
     * @param amount          сумма платежа      
     * @return платёж
     */
    PaymentEntity getPaymentFromClient(int creditRequestId, int partnerId, int paysumId,Date dt,Double amount);
    /**
     * Успешное проведение платежа
     *
     * @param paymentId   id обрабатываемого платёжа
     * @param processDate время обработки платежа
     */
    PaymentEntity processSuccessPayment(Integer paymentId, Date processDate);

    /**
     * Неуспешное проведение платежа
     *
     * @param paymentId     id обрабатываемого платёжа
     * @param processDate   время обработки платежа
     * @param exceptionInfo информация об ошибке проведения
     */
    void processFailPayment(Integer paymentId, Date processDate, ExceptionInfo exceptionInfo);

    /**
     * Суммы, пришедшие от клиента в счёт погашения кредита
     *
     * @param creditId id кредита
     * @return сумма всех платежей по кредиту
     */
    Double getCreditPaymentSum(int creditId);
    
    /**
     * сохраняем запись в график платежей
     * @param cr - данные кредита
     * @param stake - ставка
     * @param sm - сумма 
     * @param smback - сумма к возврату
     * @param days - дни
     * @param dt -дата начала периода
     */
    RepaymentScheduleEntity savePaymentSchedule(CreditEntity cr,Double stake,int days,Double smback,Date dt,Double sm);
    /**
     * закрываем активную запись расписания платежей
     * @param cr - кредит
     * @param reason - причина закрытия
     * @param date - дата закрытия
     */
    void closePaymentSchedule(CreditEntity cr,Integer reason,Date date);

     /**
      * возвращает последнюю запись по коду закрытия
      * @param credit - кредит
      * @param reason - причина закрытия записи
      */
     RepaymentScheduleEntity findSchedule(CreditEntity credit,Integer reason);
     /**
      * возвращает выплаченную просроченную сумму
      * @param creditId - id кредита
      */
     Double getOverduePaymentSum(int creditId);

    /**
     * сохраняем id, который нам пришел в качестве ответа от платежной системы
     * @param paymentId - id платежа
     * @param externalId - id из контакта
     */
    void saveExternalId(Integer paymentId, String externalId);

    /**
     * сохраняем ids, которые нам пришли в качестве ответа от платежной системы
     * @param paymentId - id платежа
     * @param externalId - id1
     * @param externalId2 - id2
     */
    void saveExternalId(Integer paymentId, String externalId, String externalId2);

    /**
     * Сохранить хэш карты payonline
     *
     * @param creditRequestId - id заявки
     * @param cardNumberMasked - маскированный номер карты
     * @param cardNumberHash - хэш карты
     * @param cardHolder - владелец карты
     */
    void savePayonlineCardNumberHash(Integer creditRequestId, String cardNumberMasked, 
    		String cardNumberHash, String cardHolder);

    /**
     * Создать успешный платёж от клиента, используем для qiwiGate
     *
     * @param creditId id кредита
     * @param partnerId id партнёра
     * @param amount сумма
     * @param date дата
     * @param externalId внешний id
     * @return Платёж
     */
    Payment createSuccessClientPayment(Integer creditId, Integer partnerId, Double amount, Date date, String externalId);

    /**
     * Суммы, пришедшие от клиента в счёт погашения кредита
     *
     * @param creditId id кредита
     * @param createDate - интервал дат создания платежа
     * @param processDate - интервал дат оплаты платежа
     * @return сумма всех платежей по кредиту
     */    
	Double getCreditPaymentSum(int creditId, DateRange createDate, DateRange processDate);

	/**
	 * пересчитать кредит
	 * @param credit - кредит
	 * @param processDate - дата проведения платежа
	 * @param payment -  платеж
	 */
	void calcCredit(Credit credit, Date processDate,PaymentEntity payment);

    /**
     * ищем последнюю сумму платежа по кредиту
     * @param creditId - кредит
     */
    Double getLastCreditPaymentSum(int creditId,Date processDate);
    /**
     * ищем последний платеж по кредиту
     * @param creditId - кредит
     */
    PaymentEntity getLastCreditPayment(int creditId,Date processDate);
    /**
     * сохраняем баланс
     * @param paymentId - платеж
     * @param partnerId - партнер
     * @param accountTypeId - вид счета
     * @param amount - сумма
     * @param date - дата баланса
     */
    public void saveBalance(Integer paymentId,Integer partnerId,Integer accountTypeId,Double amount,Date date);
    /**
     * ищем последний баланс
     * @param partnerId - партнер
     * @param accountTypeId - тип счета
     */
    public Balance findLastBalance(Integer partnerId,Integer accountTypeId);
    /**
     * возвращает список активных счетов человека
     * @param peopleMainId - id человека
     */
	public List<Account> listAccounts(int peopleMainId);

	void processSuccessPaymentWait(Integer paymentId, Date processDate);

	void paymentEvent(BusinessEvent event);
	/**
	 * создаем проведенный платеж системы
	 * @param creditId - id кредита
	 * @param processDate - дата проведения
	 * @param amount - сумма
	 * @param paySumId - вид суммы
	 * @return
	 */
	public PaymentEntity createSuccessSystemPayment(Integer creditId,Date processDate,Double amount,Integer paySumId);

	/**
	 * расчет кредита после платежа
	 * @param creditId - id кредита
	 * @param payment - платеж
	 */
    void calcCreditAfterPayment(Integer creditId,PaymentEntity payment);

    /**
     * Получает список платежных систем для первого запроса
     *
     * @return codeinteger систем
     */
    List<Integer> getFirstRequestPaymentSystems();

    /**
     * сколько раз человеку давать только на ограниченный список платежных систем
     * @param creditRequestId - id заявки
     * @return
     */
    int getFirstRequestPaySysTimes(Integer creditRequestId);

    /**
     * список платежных систем для первого займа по заявке
     * @return
     */
    int getFirstRequestPaySysTimes();

    /**
     * Получает список платежных систем для первого займа
     * 
     * @param creditRequestId - id заявки
     * @return codeinteger систем
     */
    List<Integer> getFirstRequestPaymentSystems(Integer creditRequestId);
    
    /**
     * количество платежей в месяц
     * @param creditId - id кредита
     * @param date - дата платежа
     * @return
     */
    Integer getCountPaymentsInMonth(Integer creditId,Date date);
}
