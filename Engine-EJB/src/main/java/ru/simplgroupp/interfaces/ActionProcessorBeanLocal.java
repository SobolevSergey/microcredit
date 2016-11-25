package ru.simplgroupp.interfaces;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.exception.WorkflowRuntimeException;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.workflow.DecisionState;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface ActionProcessorBeanLocal extends ActionProcessorBeanMini {

    /**
     * удаляет заявку
     *
     * @param creditRequestId заявка
     */
    void removeCreditRequest(Integer creditRequestId);

    void test(Integer creditRequestId);

    /**
     * Существует ли активная модель с данным именем.
     *
     * @param target название модели
     */
    boolean isModelEnabled(String target);

    /**
     * Сохранить в базу принятое решение по кредитной заявке, но не начислять сам кредит.
     *
     * @param creditRequestId id запроса
     * @param bAccepted       решение (отказано или принято)
     * @param rejectCode      код отказа
     * @throws ActionException
     */
    void saveCreditRequestDecision(Integer creditRequestId, boolean bAccepted,
                                   Integer rejectCode) throws ActionException;

    /**
     * одобрена ли заявка
     *
     * @param creditRequestId кредитная заявка
     */
    boolean crAccepted(Integer creditRequestId) throws ActionException;

    /**
     * оплачено ли продление
     *
     * @param creditRequestId id заявки
     */
    boolean prolongPayed(Integer creditRequestId);

    /**
     * создаем кредит
     *
     * @param creditRequestId id заявки
     * @throws ActionException
     */
    int createCredit(Integer creditRequestId) throws ActionException;

    /**
     * Начислить кредит, но не выплачивать его. Возвращает id кредита.
     *
     * @param creditRequestId кредитная заявка
     * @throws ActionException
     */
    int createCreditNew(Integer creditRequestId) throws ActionException;

    /**
     * Отправляет смс по номеру телефона
     *
     * @param number            Номер телефона
     * @param paymentSystemCode Код платежной системы
     * @param balance           баланс
     * @return true если успешно отправлено
     */
    boolean sendLowBalanceSms(String number, Integer paymentSystemCode, Double balance);

    /**
     * Удаляет запись об отправке сообщения о низком балансе
     *
     * @param paymentSystemCode Код платежной системы
     */
    void deletePaymentMessageByPaymentCode(Integer paymentSystemCode);

    /**
     * Изменение статус в справочнике
     *
     * @param refHeaderID ID справочника
     * @param codeInteger код платежной системы
     * @param status      статус
     */
    void changeReferenceStatus(Integer refHeaderID, Integer codeInteger, Integer status);

    /**
     * Проверка баланса платежных систем
     *
     * @return баланс всех платежных систем
     */
    Map<String, Double> checkBalancePaymentSystem();

    /**
     * Метод возвращает кол-во дней для подписания оферты
     *
     * @param request Кредитная заявка
     * @return кол-во дней для подписания оферты
     */
    Integer daysWaitToSignOferta(CreditRequest request);

    /**
     * Отключение, включение, отправка смс о низком балансе платежных систем
     *
     * @param systemBalances балансы всех платежных систем
     * @param params         параметры
     */
    void checkPaymentSystems(Map<String, Double> systemBalances, Map<String, Object> params);

    /**
     * посылаем смс,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param peopleMainId человек
     * @param smsText      текст смс
     */
    boolean sendSMS(Integer peopleMainId, String smsText);
    /**
     * посылаем смс,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param peopleMainId человек
     * @param smsText      текст смс
     * @param phone        телефон
     */
    boolean sendSMS(Integer peopleMainId, String smsText,String phone);
    /**
     * посылаем смс по заявке,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param creditRequestId заявка
     * @param smsText         текст смс
     */
    boolean sendSMSByCreditRequest(int creditRequestId, String smsText);

    /**
     * посылаем смс,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param productId     id продукта
     * @param peopleMainId  человек
     * @param bpName        название БП
     * @param smsMessageKey название сообщения
     * @param params        передаваемые параметры
     */
    boolean sendSMSKProduct(Integer productId, int peopleMainId, String bpName, String smsMessageKey, Object[] params);

    /**
     * посылаем смс,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param creditRequestId заявка
     * @param bpName          название БП
     * @param smsMessageKey   -
     * @param params          передаваемые параметры
     */
    boolean sendSMSKByCreditRequest(Integer productId, int creditRequestId, String bpName, String smsMessageKey, Object[] params);

    /**
     * возвращает интерфейс RulesBeanLocal
     */
    RulesBeanLocal getRulesBean();

    /**
     * возвращает интерфейс ProductBeanLocal
     */
    ProductBeanLocal getProductBean();
//	 PluginsSupport getPlugins();

    /**
     * Рассчитать модель для заданного бизнес-объекта
     *
     * @param modelKey            название модели
     * @param decisionState       параметры для принятия решения
     * @param businessObjectClass класс бизнес-объекта
     * @param businessObjectId    id бизнес-объекта
     * @param actionContext       контекст для выполнения
     * @param modelId             id модели
     * @param productId           id продукта
     * @param wayId               id способа подачи заявки
     * @return map с результатами
     */
    Map<String, Object> calcModel(String modelKey, DecisionState decisionState, String businessObjectClass,
                                  Object businessObjectId, ActionContext actionContext, Integer modelId, Integer productId, Integer wayId, Map<String, Object> mpVars);

    /**
     * Нужно ли вызывать хотя бы одну внешнюю подсистему
     *
     * @param ds
     * @param actionContext
     */
    boolean needPluginCalls(DecisionState ds, ActionContext actionContext);

    /**
     * запуск плагина
     *
     * @param pluginName          название
     * @param businessObjectClass класс бизнес-объекта
     * @param businessObjectId    id бизнес-объекта
     * @param params              параметры плагина
     * @param actionContext
     * @throws ActionException
     */
    void runPlugin(String pluginName, String businessObjectClass, Object businessObjectId, Map<String,
            Object> params, ActionContext actionContext) throws ActionException;

    /**
     * Конвертирует заданный интервал в мс в формат времени ISO 8601
     */
    String calcISOduration(long ms);

    /**
     * Ищем подходящую платёжную систему, чтобы перечислить деньги клиенту
     *
     * @param creditRequestId
     * @param actionContext
     * @return имя плагина этой платёжной системы, или null, если система не найдена
     * @throws ActionException
     */
    String findPaymentSystem(int creditRequestId, ActionContext actionContext) throws ActionException;

    /**
     * Возвращает ID платежа клиенту
     *
     * @param creditId id кредита
     */
    Integer getPaymentToClientId(Integer creditId);

    /**
     * дата окончания кредита
     *
     * @param creditId кредит
     */
    Date getCreditDataEndPlan(int creditId);

    /**
     * Конвертирует дату (без времени) в формат ISO 8601
     *
     * @param source дата
     * @return дата в формате ISO 8601
     */
    String calcISOdate(Date source);

    /**
     * обновляем конфиг плагинов
     *
     * @param plc     конфиг плагина
     * @param context
     * @return
     */
    PluginConfig refreshPluginConfig(PluginConfig plc, ActionContext context);

    /**
     * Ищем для данного кредита наиболее подходящую систему для приёма денег
     *
     * @param creditId        id кредита
     * @param accountTypeCode тип счёта
     * @param payId           если известен id платежа, передаём здесь. Или null.
     * @param actionContext
     * @return имя плугина
     */
    String findAcquiringSystem(int creditId, int accountTypeCode, Integer payId, ActionContext actionContext);

    /**
     * Отказать в кредите
     *
     * @param creditRequestId id заявки на кредит
     * @param comment         примечание
     * @param reason          причина отказа
     */
    void refuseCredit(Integer creditRequestId, String comment, Integer reason)
            throws ActionException;

    /**
     * Одобрить кредит
     *
     * @param creditRequestId id заявки на кредит
     * @param comment         примечание
     */
    void approveCredit(Integer creditRequestId, String comment) throws ActionException;


    String calcISOcycle(long ms);

    /**
     * создаем ActionContext
     *
     * @param customizationKey
     * @param bPersistent
     * @return
     */
    ActionContext createActionContext(String customizationKey, boolean bPersistent);

    /**
     * устанавливает разрешения на определенные объекты
     *
     * @param businessObjectClass класс объекта
     * @param bisinessObjectId    id объекта
     * @param roleName            роль
     * @param permissions         права
     */
    void setBusinessPermission(String businessObjectClass,
                               Object bisinessObjectId, String roleName, String permissions);

    /**
     * есть ли ответы на вопросы
     *
     * @param creditRequestId заявка
     */
    public boolean hasCRQuestions(int creditRequestId);

    /**
     * Все имена внешних подсистем, которые надо вызывать
     *
     * @param ds
     * @param modelKey
     * @param actionContext
     */
    public List<String> getActivePluginCalls(DecisionState ds, String modelKey, ActionContext actionContext);

    /**
     * Отменить продление
     *
     * @param creditId id кредита
     */
    public boolean prolongCRCancel(Integer creditId);

    /**
     * Подтвердить продление
     *
     * @param creditId кредит
     */
    public boolean prolongCRSave(Integer creditId);

    /**
     * отменить рефинансирование
     *
     * @param creditId id кредита
     */
    public boolean refinanceCancel(Integer creditId);

    /**
     * подтвердить рефинансирование
     *
     * @param creditId id кредита
     */
    public boolean refinanceSave(Integer creditId);

    /**
     * возвращает оставшуюся сумму возврата
     *
     * @param creditId кредит
     */
    public Double getCreditSumBack(int creditId);

    /**
     * возвращает оставшуюся сумму основного долга
     *
     * @param creditId кредит
     * @return
     */
    public Double getCreditSumDebt(int creditId);

    /**
     * остаток денег после оплаты всего долга
     *
     * @param creditId id кредита
     * @return
     */
    public Double getRemainingMoney(int creditId);

    /**
     * Вернуть переплату
     *
     * @param paymentId платеж
     */
    public void returnRepayCR(Integer paymentId);

    public PaymentService getPaymentService();

    /**
     * подписана ли оферта
     *
     * @param creditRequestId номер заявки
     */
    public boolean crOfertaAccepted(Integer creditRequestId) throws ActionException;

    /**
     * послать email
     *
     * @param peopleMainId id человека
     * @param subject      тема письма
     * @param body         письмо
     * @return
     */
    public boolean sendEmail(Integer peopleMainId, String subject, String body);

    /**
     * послать email
     *
     * @param peopleMainId id человека
     * @param email        email
     * @param subject      тема письма
     * @param body         письмо
     * @return
     */
    public boolean sendEmail(Integer peopleMainId, String email, String subject, String body);

    /**
     * посылаем email,
     * возвращаем true если отправлено успешно, false в остальных случаях
     *
     * @param productId       id продукта
     * @param peopleMainId    человек
     * @param bpName          название БП
     * @param emailMessageKey название сообщения
     * @param params          передаваемые параметры
     * @return
     */
    public boolean sendEmailKProduct(Integer productId, int peopleMainId, String bpName,
                                     String emailMessageKey, Object[] params);

    /**
     * Был ли явный отказ от оферты
     *
     * @param creditRequestId заявка
     */
    public boolean crOfertaRejected(Integer creditRequestId) throws ActionException;

    /**
     * Требуется ли заполнение анкеты
     *
     * @param creditRequestId заявка
     */
    public boolean crVerificatorMustEdit(Integer creditRequestId) throws ActionException;

    /**
     * Возвращает последний запрос на продление в статусе черновика
     *
     * @param creditId кредит
     */
    public Integer getProlongNew(Integer creditId);

    /**
     * Возвращает последний запрос на рефинансирование в статусе черновика
     *
     * @param creditId кредит
     */
    public Integer getRefinanceNew(Integer creditId);

    /**
     * Рассчитать параметры для возможного продления по кредиту
     *
     * @param prolongId продление
     * @param creditId  кредит
     * @return
     */
    public Map<String, Object> calcProlongData(Integer prolongId, Integer creditId);

    /**
     * считаем, когда заканчивается срок выплаты процентов по продлению
     *
     * @param prolongId продление
     * @param days      дней
     */
    public String calcISODateProlong(Integer prolongId, int days);

    void dummy();

    /**
     * Определяет, можем ли мы принять платёж от Контакта по данному платежу.
     * Если принять возможно, exception не возникает. Если невозможно, то возникает.
     *
     * @param creditId      id кредита, по которому платим
     * @param paymentParams параметры платежа
     * @throws ActionException - Возникает, если платёж невозможен. Причину отказа смотрите в exception.getCode() и exception.getMessage()
     */
    public void canReceivePaymentContact(int creditId, Map<String, Object> paymentParams) throws ActionException;

    public String calcISOCycleBizAction(Integer bizActionId);

    /**
     * пересчитываем просрочку
     *
     * @param creditId кредит
     * @param dateCalc дата расчета
     * @throws ActionException
     */
    public void recalcOverdue(Integer creditId, Date dateCalc) throws ActionException;

    public String calcISOduration(String schedule, long ms);

    /**
     * проверяем на терроризм
     *
     * @param peopleMainId    - человек
     * @param creditRequestId - заявка
     * @throws PeopleException
     */
    public boolean checkTerrorism(int peopleMainId, int creditRequestId) throws PeopleException;

    /**
     * проверяем на валидность паспорта
     *
     * @param creditRequestId заявка
     * @throws PeopleException
     */
    public boolean checkPassportValidity(int creditRequestId) throws PeopleException;

    /**
     * возвращает пользователя
     *
     * @param peopleMainId человек
     * @return
     */
    Users getUserByPeople(int peopleMainId);

    /**
     * передать кредит коллектору
     *
     * @param creditId кредит
     */
    public void toCollector(int creditId);

    /**
     * возвращаем пользователя-гостя
     */
    Users getGuestUser();

    /**
     * ищем платежную систему по виду счета
     *
     * @param accountType   вид счета
     * @param actionContext
     * @throws ActionException
     */
    public String findPaymentSystemAcc(int accountType, ActionContext actionContext) throws ActionException;

    /**
     * созвращает ошибку bpm
     *
     * @param errorCode код ошибки
     * @throws Exception
     */
    public void bpmnError(String errorCode) throws Exception;

    /**
     * ищем счет у кредитной заявки
     *
     * @param creditRequestId заявка
     */
    int getAccountTypeCR(int creditRequestId);

    /**
     * остановить кредит
     *
     * @param creditId  кредит
     * @param stopPoint момент остановки
     */
    void stopCredit(int creditId, String stopPoint);

    /**
     * перезапустить кредит
     *
     * @param creditId кредит
     * @param data     параметры для восстановления
     */
    String restartCredit(int creditId, Object data);

    /**
     * найти пользователя по заявке
     *
     * @param creditRequestId заявка
     */
    Users getUserByCreditRequest(int creditRequestId);

    String calcISOdateTime(Date source);

    /**
     * ищем платежную систему по записи в платежах
     *
     * @param paymentId     платеж
     * @param actionContext
     * @throws ActionException
     */
    public String findPaymentSystemPay(int paymentId, ActionContext actionContext)
            throws ActionException;

    public void savePluginConfigs(ActionContext context, Map<String, Object> mpChanged);

    /**
     * сохраняем разные переменные из расчета модели
     *
     * @param creditRequestId id заявки
     * @param vars            переменные
     */
    public void saveCRMiscVariables(int creditRequestId, Map<String, Object> vars);

    /**
     * есть ли процесс по данной заявке
     *
     * @param creditRequestId id заявки
     * @return
     */
    public boolean hasProcess(Integer creditRequestId);

    /**
     * восстановить процесс
     *
     * @param creditRequestId id заявки
     * @param activityId      id activity
     * @throws WorkflowRuntimeException
     * @throws WorkflowException
     */
    public void restoreProcess(Integer creditRequestId, String activityId) throws WorkflowRuntimeException, WorkflowException;

    /**
     * послать письмо
     *
     * @param peopleMainId         id человека
     * @param formattedTextSubject заголовок
     * @param formattedTextBody    текст письма
     * @param params               параметры для письма
     * @return
     */
    boolean sendEmailCommon(Integer peopleMainId, String formattedTextSubject, String formattedTextBody, Object... params);

    /**
     * послать письмо
     *
     * @param peopleMainId         id человека
     * @param email                email
     * @param formattedTextSubject заголовок
     * @param formattedTextBody    текст письма
     * @param params               параметры для письма
     * @return
     */
    boolean sendEmailCommon(Integer peopleMainId, String email, String formattedTextSubject, String formattedTextBody, Object... params);

    /**
     * послать смс
     *
     * @param peopleMainId  id человека
     * @param formattedText текст смс
     * @param params        параметры для смс
     * @return
     */
    boolean sendSMSCommon(Integer peopleMainId, String formattedText, Object... params);
    /**
     * послать смс
     *
     * @param peopleMainId  id человека
     * @param phone         телефон
     * @param formattedText текст смс
     * @param params        параметры для смс
     * @return
     */
    boolean sendSMSCommon(Integer peopleMainId, String phone,String formattedText, Object... params);
    /**
     * перезапустить заявку
     *
     * @param creditRequestId id заявки
     */
    void restartCreditRequest(int creditRequestId);

    /**
     * платеж клиенту
     *
     * @param creditId   id кредита
     * @param accountId  id счета
     * @param creditSum  сумма
     * @param createDate дата создания
     * @param paysumId   вид суммы
     * @return
     */
    public Payment createPaymentToClient(int creditId, int accountId,
                                         Double creditSum, Date createDate, Integer paysumId);

    /**
     * отказ клиента
     *
     * @param creditRequestId id заявки
     * @param userId          id пользователя
     * @throws ActionException
     * @throws WorkflowException
     */
    public void refuseClient(int creditRequestId, int userId) throws ActionException,
            WorkflowException;

    /**
     * отменить платеж в Контакт
     *
     * @param paymentId id платежа
     */
    public void cancelPaymentContact(int paymentId);

    /**
     * ищем платеж контакта
     *
     * @param businessObjectId id заявки
     * @return
     */
    public Integer findContactPaymentId(Object businessObjectId);

    /**
     * отказ клиента
     *
     * @param creditRequestId id заявки
     * @param userId          id пользователя
     * @throws ActionException
     * @throws WorkflowException
     */
    void saveClientRefuse(Integer creditRequestId, Integer userId)
            throws ActionException, WorkflowException;

    /**
     * ищем платеж по id
     *
     * @param paymentId id платежа
     * @return
     */
    Payment getPayment(int paymentId);

    /**
     * Передать кредит в суд
     *
     * @param creditId id кредита
     */
    public void toCourt(int creditId);

    /**
     * возможно ли выполнение действия по объекту
     *
     * @param businessObjectClass класс объекта
     * @param businessObjectId    id объекта
     * @param actionName          действие
     * @return
     */
    public boolean isActionAvailable(String businessObjectClass, Integer businessObjectId, String actionName);

    /**
     * Послать бизнес-событие
     *
     * @param event
     */
    void fireBusinessEvent(BusinessEvent event);

    /**
     * ищем платеж по id
     *
     * @param id      id платежа
     * @param options что инициализируем
     * @return
     */
    Payment getPayment(int id, Set options);

    /**
     * возвращает кредит по id
     *
     * @param id      id кредита
     * @param options что инициализируем
     * @return
     */
    Credit getCredit(int id, Set options);

    /**
     * возвращает продление по id
     *
     * @param prolongId id продления
     * @param options   что инициализируем
     * @return
     */
    Prolong getProlong(int prolongId, Set options);

    /**
     * возвращает заявку по id
     *
     * @param id      id заявки
     * @param options что инициализируем
     * @return
     */
    CreditRequest getCreditRequest(int id, Set options);

    /**
     * возвращает рефинансирование по id
     *
     * @param refinanceId id рефинансирования
     * @param options     что инициализируем
     * @return
     */
    Refinance getRefinance(int refinanceId, Set options);

    /**
     * выполняем успешное проведение платежа
     *
     * @param paymentId id платежа
     */
    void processSuccessPayment(Integer paymentId);

    /**
     * возвращает бизнес-действие по id
     *
     * @param id id бизнес-действия
     * @return
     */
    BizActionEntity getBizActionEntity(int id);

    /**
     * Возвращает бонус по его коду
     * @param bonusCode код бонуса
     * @return возвразщает бонус иначе null
     */
    RefBonus getBonusByCodeInteger(int bonusCode);

    /**
     * Выдать бонус
     *
     * @param peopleMainId        id человека
     * @param relatedPeopleMainId id связанного человека
     * @param relatedCreditId     связанный кредит
     * @param bonusCode           код бонуса
     * @param operarionCode       код операции
     */
    void addBonus(Integer peopleMainId, Integer relatedPeopleMainId, Integer relatedCreditId, Integer bonusCode,
                  Integer operarionCode) throws ActionException;

    /**
     * Выдать бонус с явным указанием количества бонусов
     *
     * @param peopleMainId        id человека
     * @param relatedPeopleMainId id связанного человека
     * @param relatedCreditId     связанный кредит
     * @param bonusCode           код бонуса
     * @param amount              количество баллов
     * @param operarionCode       код операции
     * @throws ActionException
     */
    void addBonus(Integer peopleMainId, Integer relatedPeopleMainId, Integer relatedCreditId, Integer bonusCode,
                  Double amount, Integer operarionCode) throws ActionException;

    /**
     * считаем параметры для рефинансирования
     *
     * @param refinanceId id рефинансирования
     * @param creditId    id кредита
     * @return
     */
    Map<String, Object> calcRefinanceData(Integer refinanceId, Integer creditId);

    /**
     * считаем, когда заканчивается срок выплаты суммы за рефинансирование
     *
     * @param refinanceId продление
     * @param days        дней
     */
    String calcISODateRefinance(Integer refinanceId, int days);


    /**
     * возвращает рефинансирование по id
     *
     * @param id id рефинансирования
     * @return
     */
    Refinance getRefinance(int id);

    /**
     * ищем сделанное рефинансирование
     *
     * @param creditId id кредита
     * @return
     */
    Refinance findRefinanceCompleted(Integer creditId);

    /**
     * Посчитать мин-макс суммы возврата займа
     *
     * @param creditId
     * @param variablePrefix
     * @return
     */
    Map<String, Object> calcReturnData(Integer creditId, String variablePrefix);

    /**
     * выполняем выгрузку для equifax
     */
    void uploadScoringEquifax() throws ActionException;

    /**
     * выполняем выгрузку для Skb
     */
    void uploadScoringSkb() throws ActionException;

    /**
     * выполняем выгрузку для NBKI
     */
    void uploadScoringNBKI() throws ActionException;

    /**
     * выполняем выгрузку для OkbCais
     */
    void uploadScoringOkbCais() throws ActionException;

    /**
     * выполняем выгрузку для RS
     */
    void uploadScoringRS() throws ActionException;

    /**
     * была ли сделана идентификация в системе
     *
     * @param creditRequestId
     * @return
     */
    boolean hasSystemVerification(Integer creditRequestId);

    /**
     * все вопросы уже отвечены (либо их не было)
     *
     * @param creditRequestId - id заявки
     * @return
     */
    boolean hasAnsweredIdentityQuestions(Integer creditRequestId);

    /**
     * Изменить тип коллекторской записи
     *
     * @param collectorID ID коллекторской записи
     * @param typeID      тип коллекторской деятельности
     */
    void changeCollectorType(Integer collectorID, Integer typeID);

    /**
     * Метод меняет статус коллекторской записи
     *
     * @param collectorID ID коллекторской записи
     */
    void closeCollector(Integer collectorID);

    /**
     * Отправка человека в блэклист
     *
     * @param peopleMainID ID человека
     */
    void peopleToBlackList(Integer peopleMainID);

    /**
     * проверяем на внутренние АМ правила
     *
     * @param creditRequestId - id заявки
     */
    void checkAntifraudRules(Integer creditRequestId);

    /**
     * проверяем, успешно ли сделан платеж
     *
     * @param creditId - id кредита
     * @return
     */
    boolean checkPaid(Integer creditId);

    public Map<String, Object> getAIModelParams(int modelId, ActionContext actionContext);

    /**
     * отправляет смс уведомление на номер менеджера
     *
     * @param productId     id продукта
     * @param peopleMainId  id человека
     * @param bpName
     * @param smsMessageKey ключ смс сообщения
     */
    void sendSmsNotification(Integer productId, Integer peopleMainId, String bpName, String smsMessageKey);

    void setAIModelParamValues(int modelId, ActionContext actionContext, Map<String, Object> mpVars, boolean bSaveValues);

    void checkDefaultModelParams(int modelId, ActionContext actionContext);

    Integer findActiveModelId(String modelKey, Integer productId, Integer wayId);

    void deleteResults(String businessObjectClass, Integer businessObjectId);

    int setAIModelParamValues(int modelId, ActionContext actionContext, Map<String, Object> mpVars, Integer run_id, boolean bSaveValues);

    void uploadScoringEquifax1() throws ActionException;
    String findAcquiringPlasticCardSystem();
}
