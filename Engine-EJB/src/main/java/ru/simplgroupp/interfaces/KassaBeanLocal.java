package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DeviceInfoEntity;
import ru.simplgroupp.persistence.Misc;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KassaBeanLocal {

    /**
     * удаляем пустую заявку, если был сделан только 1 шаг
     */
    void removeCreditRequest(CreditRequest ccRequest) throws KassaException;

    /**
     * удаляем пустую заявку по id, если был сделан только 1 шаг
     */
    void removeCreditRequest(int id) throws KassaException;

   
    /**
     * сохраняем 2 шаг, возвращаем id заявки и id человека, если он существует в бд
     *
     * @param params описание в вики
     */
    Map<String, Object> saveStep2(Map<String, Object> params) throws KassaException;

    /**
     * сохраняем 3 шаг, возвращаем id заявки
     *
     * @param params описание в вики
     */
    Integer saveStep3(Map<String, Object> params) throws KassaException;

    /**
     * сохраняем 4 шаг, возвращаем id заявки
     *
     * @param params описание в вики
     */
    Integer saveStep4(Map<String, Object> params) throws KassaException;

    /**
     * сохраняем 7 шаг, возвращаем id заявки
     *
     * @param params описание в вики
     */
    Integer saveStep7(Map<String, Object> params) throws KassaException;

    /**
     * одобрение кредита. Возвращает id созданного кредита.
     *
     * @param creditRequestId id заявки
     * @param comment         комментарий
     */
    int saveCreditApprove(Integer creditRequestId, String comment) throws KassaException;

    /**
     * сохраняем кредит и начисляем выплату
     *
     * @param creditRequestId id заявки
     */
    int startCredit(Integer creditRequestId) throws KassaException;

    /**
     * одобрение кредитной заявки без записи кредита и начисления выплаты
     *
     * @param creditRequestId id заявки
     * @param comment         комментарий
     */
    void saveCreditApproveWithoutCredit(Integer creditRequestId, String comment) throws KassaException;

    /**
     * отказ в кредите
     *
     * @param creditRequestId id заявки
     * @param comment         комментарий
     * @param reason          причина
     */
    void saveCreditRefuse(Integer creditRequestId, String comment, Integer reason) throws KassaException;

    /**
     * отказ клиента от кредита
     *
     * @param creditRequestId id заявки
     * @param userId          id пользователя
     */
    void saveClientRefuse(Integer creditRequestId, Integer userId) throws KassaException;

    /**
     * сохраняем кредитную заявку
     *
     * @param cre       заявка
     * @param ipAddress ip адрес
     * @param userAgent строка user-agent клиента
     */
    int saveCreditRequest(CreditRequest cre, String ipAddress, String userAgent) throws KassaException;
    /**
     * добавляем и сохраняем кредитную заявку
     * 
     * @param creditRequest - кредитная заявка
     * @return
     * @throws KassaException
     */
    CreditRequestEntity addCreditRequest(CreditRequest creditRequest) throws KassaException;
    /**
     * создаем новую кредитную заявку
     * 
     * @param creditRequest - заявка
     * @param statusId - статуч заявки
     * @param contest - согласие
     * @param contestAsp - согласие 
     * @param contestCb - согласие
     * @param contestPd - согласие
     * @param partnerId - id партнера
     * @param productId - id продукта
     * @param dateContest - дата согласия
     * @param dateFill - дата заполнения
     * @param dateStatus - дата статуса
     * @param nomer - номер числовой
     * @param creditSum - сумма кредита
     * @param creditDays - дней кредита
     * @param stake - ставка
     * @param agreement - оферта
     * @param smsCode - смс код
     * @param uniqueNomer - уникальный номер
     * @param accountId - id счета
     * @param purposeId - цель кредита
     * @param wayId - способ получения заявки
     * @param user_id - id пользователя создающего заявку
     * @return
     * @throws KassaException
     */
    CreditRequestEntity newCreditRequest(CreditRequestEntity creditRequest,Integer peopleMainId,Integer statusId,Boolean contest,
    		Boolean contestAsp,Boolean contestCb,Boolean contestPd,Boolean confirmed,Integer partnerId,Integer productId,
    		Date dateContest,Date dateFill,Date dateStatus,Integer nomer,Double creditSum,Integer creditDays,
    		Double stake,String agreement,String smsCode,String uniqueNomer,Integer accountId,String purpose, Integer wayId, Integer user_id);
    /**
     * сохраняем кредитную заявку
     *
     * @param cre       заявка
     * @param ipAddress ip адрес
     * @param userAgent строка user-agent клиента
     * @param behdata   данные о поведении пользователя
     * @throws KassaException
     */

    int saveCreditRequest(CreditRequest cre, String ipAddress, String userAgent, Map<String, String> behdata) throws KassaException;

    /**
     * сохраняем кредитную заявку
     *
     * @param creditRequest кредитная заявка
     * @throws KassaException
     */

    CreditRequestEntity saveCreditRequestWithAccount(CreditRequest creditRequest) throws KassaException;
     /**
     * список кредитных заявок - для просмотра в вебе
     *
     * @param nFirst         первый номер
     * @param nCount         на страницу
     * @param sorting        сортировка
     * @param options        что инициализируем
     * @param peopleMainId   id человека
     * @param rejectReasonId причина отказа
     * @param statusId       статус заявки
     * @param accepted       одобрена или нет
     * @param date           дата подачи
     * @param days           дней кредита
     * @param sum            сумма кредита
     * @param isover         закрыта
     * @param creditDateEnd  дата окончания кредита
     * @param peopleSurname  фамилия
     * @param peopleName     имя
     * @param peopleMidname  отчество
     * @param docTypeId      вид документа
     * @param docSeries      серия документа
     * @param docNomer       номер документа
     * @param peopleEmail    email
     * @param peoplePhone    мобильный телефон
     * @param peopleSNILS    снилс
     * @param peopleINN      инн
     * @param uniqueNomer    номер заявки
     * @param wayId          способ подачи заявки
     *
     * @deprecated
     * заменен на KassaBeanLocal.listCreditRequests(CreditRequestFilter filter)
     */
    @Deprecated
    List<CreditRequest> listCreditRequests(int nFirst, int nCount,
                                           SortCriteria[] sorting, Set options, Integer peopleMainId,
                                           Integer rejectReasonId, Integer statusId, Integer accepted,
                                           DateRange date, IntegerRange days, MoneyRange sum, Boolean isover,
                                           DateRange creditDateEnd, String peopleSurname, String peopleName,
                                           String peopleMidname, Integer docTypeId, String docSeries,
                                           String docNomer, String peopleEmail, String peoplePhone,
                                           String peopleSNILS, String peopleINN, String uniqueNomer, 
                                           String taskId,Integer wayId);

    /**
     * список кредитных заявок - для просмотра в вебе
     *
     * @param filter - Фильтр для списка
     * @return - список кредитных заявок
     */
    List<CreditRequest> listCreditRequests(CreditRequestFilter filter);

    /**
     * список кредитных заявок - для просмотра в вебе
     *
     * @param nFirst         первый номер
     * @param nCount         на страницу
     * @param sorting        сортировка
     * @param options        что инициализируем
     * @param peopleMainId   id человека
     * @param rejectReasonId причина отказа
     * @param statusId       статус заявки
     * @param accepted       одобрена или нет
     * @param date           дата подачи
     * @param days           дней кредита
     * @param sum            сумма кредита
     * @param isover         закрыта
     * @param creditDateEnd  дата окончания кредита
     * @param peopleSurname  фамилия
     * @param peopleName     имя
     * @param peopleMidname  отчество
     * @param docTypeId      вид документа
     * @param docSeries      серия документа
     * @param docNomer       номер документа
     * @param peopleEmail    email
     * @param peoplePhone    мобильный телефон
     * @param peopleSNILS    снилс
     * @param peopleINN      инн
     * @param uniqueNomer    номер заявки
     * @param wayId          способ подачи заявки
     * @param workplace_id   подразделение где выдана заявка
     *
     * @deprecated
     * заменен на KassaBeanLocal.listCreditRequests(CreditRequestFilter filter)
     */
    @Deprecated
    List<CreditRequest> listCreditRequests(int nFirst, int nCount,
                                           SortCriteria[] sorting, Set options, Integer peopleMainId,
                                           Integer rejectReasonId, Integer statusId, Integer accepted,
                                           DateRange date, IntegerRange days, MoneyRange sum, Boolean isover,
                                           DateRange creditDateEnd, String peopleSurname, String peopleName,
                                           String peopleMidname, Integer docTypeId, String docSeries,
                                           String docNomer, String peopleEmail, String peoplePhone,
                                           String peopleSNILS, String peopleINN, String uniqueNomer,
                                           String taskId,Integer wayId,Integer workplace_id);

    /**
     * количество кредитных заявок - для просмотра в вебе
     *
     * @param peopleMainId   id человека
     * @param rejectReasonId причина отказа
     * @param statusId       статус заявки
     * @param accepted       одобрена или нет
     * @param date           дата подачи
     * @param days           дней кредита
     * @param sum            сумма кредита
     * @param isover         закрыта
     * @param creditDateEnd  дата окончания кредита
     * @param peopleSurname  фамилия
     * @param peopleName     имя
     * @param peopleMidname  отчество
     * @param docTypeId      вид документа
     * @param docSeries      серия документа
     * @param docNomer       номер документа
     * @param peopleEmail    email
     * @param peoplePhone    мобильный телефон
     * @param peopleSNILS    снилс
     * @param peopleINN      инн
     * @param uniqueNomer    номер заявки
     * @param wayId          способ подачи заявки
     *
     * @deprecated
     * заменен на KassaBeanLocal.countCreditRequests(CreditRequestFilter filter)
     */
    @Deprecated
    int countCreditRequests(Integer peopleMainId, Integer rejectReasonId,
                            Integer statusId, Integer accepted, DateRange date,
                            IntegerRange days, MoneyRange sum, Boolean isover,
                            DateRange creditDateEnd, String peopleSurname, String peopleName,
                            String peopleMidname, Integer docTypeId, String docSeries,
                            String docNomer, String peopleEmail, String peoplePhone,
                            String peopleSNILS, String peopleINN, String uniqueNomer, 
                            String taskId,Integer wayId);

    /**
     * количество кредитных заявок - для просмотра в вебе
     *
     * @param filter - Фильтр для подсчета
     * @return - количество кредитных заявок
     */
    Integer countCreditRequests(CreditRequestFilter filter);

    /**
     * количество кредитных заявок - для просмотра в вебе
     *
     * @param peopleMainId   id человека
     * @param rejectReasonId причина отказа
     * @param statusId       статус заявки
     * @param accepted       одобрена или нет
     * @param date           дата подачи
     * @param days           дней кредита
     * @param sum            сумма кредита
     * @param isover         закрыта
     * @param creditDateEnd  дата окончания кредита
     * @param peopleSurname  фамилия
     * @param peopleName     имя
     * @param peopleMidname  отчество
     * @param docTypeId      вид документа
     * @param docSeries      серия документа
     * @param docNomer       номер документа
     * @param peopleEmail    email
     * @param peoplePhone    мобильный телефон
     * @param peopleSNILS    снилс
     * @param peopleINN      инн
     * @param uniqueNomer    номер заявки
     * @param wayId          способ подачи заявки
     * @param workplace_id   подразделение заявки
     *
     * @deprecated
     * заменен на KassaBeanLocal.countCreditRequests(CreditRequestFilter filter)
     */
    @Deprecated
    int countCreditRequests(Integer peopleMainId, Integer rejectReasonId,
                            Integer statusId, Integer accepted, DateRange date,
                            IntegerRange days, MoneyRange sum, Boolean isover,
                            DateRange creditDateEnd, String peopleSurname, String peopleName,
                            String peopleMidname, Integer docTypeId, String docSeries,
                            String docNomer, String peopleEmail, String peoplePhone,
                            String peopleSNILS, String peopleINN, String uniqueNomer,
                            String taskId,Integer wayId, Integer workplace_id);

    /**
     * статистика по кредитным заявкам для кабинета менеджера
     */
    Map<Integer, Integer> overview();

    /**
     * поменять статус заявки
     *
     * @param creditRequestId id заявки
     * @param statusId        id статуса
     * @param comment         комментарии
     */
    void changeStatus(Integer creditRequestId, Integer statusId, String comment) throws KassaException;

    /**
     * статистика по суммам для кабинета менеджера
     */
    Map<Integer, Double> overviewSum();
    /**
     * статистика по суммам для кабинета менеджера
     */
    Map<Integer, Double> overviewSum(Date date);
    /**
     * удаление заявки
     *
     * @param creditRequestId id заявки
     */
    void deleteCreditRequest(int creditRequestId) throws KassaException;

    /**
     * возвращаем активную кредитную заявку
     *
     * @param peopleMainId id человека
     * @param options      какие коллекции инициализируем
     */
    CreditRequest findCreditRequestActive(int peopleMainId, Set options) throws KassaException;

    /**
     * возвращаем параметры для возвращения на шаг 2
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep2(Integer creditRequestId);

    /**
     * возвращаем параметры для возвращения на шаг 3
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep3(Integer creditRequestId);

    /**
     * возвращаем параметры для возвращения на шаг 4
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep4(Integer creditRequestId);

    /**
     * возвращаем параметры для возвращения на шаг 5
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep5(Integer creditRequestId);

    /**
     * возвращаем параметры для возвращения на шаг 7
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep7(Integer creditRequestId);

    /**
     * возвращаем параметры для возвращения на шаг 8
     *
     * @param creditRequestId id заявки
     */
    Map<String, Object> paramsStep8(Integer creditRequestId);

    /**
     * инициализируем заявку
     *
     * @param people     лицо
     * @param dateCredit дата
     * @param limits     разные параметры
     */
    CreditRequest initCreditRequestForPeople(PeopleMain people, Date dateCredit, Map<String,
            Object> limits) throws KassaException;

    /**
     * генерируем оферту
     *
     * @param ccRequest  заявка
     * @param dateCredit дата кредита
     * @param mode       0 - первоначально, 1 - при подписании
     */
    String generateAgreement(CreditRequest ccRequest, Date dateCredit, Integer mode) throws KassaException;

    /**
     * генерируем оферту
     *
     * @param credit  кредит
     * @param smsCodeOferta код для подписания оферты
     */
    String generateAgreement(Credit credit,String smsCodeOferta) throws KassaException;
    /**
     * изменение оферты
     *
     * @param ccRequest  заявка
     * @param dateOferta дата заявки
     * @param agreement  оферта строкой
     */
    void changeAgreement(CreditRequest ccRequest, Date dateOferta, String agreement);

    /**
     * генерируем согласие
     * @param reportId - id отчета
     * @param initials - ФИО
     * @param birthDate - дата рождения
     * @param birthPlace - место рождения
     * @param docDescription - паспорт
     * @param addressRegDescription - адрес регистрации
     * @param addressResDescription - адрес проживания
     * @param smsCode - код из смс
     * @param dateConsent - дата согласия
     * @return
     * @throws KassaException
     */
    String generateConsent(String reportId,String initials,Date birthDate,String birthPlace,
    		String docDescription,String addressRegDescription,String addressResDescription,
    		String smsCode,Date dateConsent) throws KassaException;
    /**
     * заявка человека в состоянии заполнения
     *
     * @param options      какие коллекции инициализируем
     * @param peopleMainId id человека
     */
    CreditRequest findCreditRequestInProcess(int peopleMainId, Set options) throws KassaException;

    /**
     * ищем последнюю отказанную заявку
     *
     * @param peopleMainId id человека
     * @param options      что инициализируем
     * @throws KassaException
     */
    CreditRequest findCreditRequestRejected(int peopleMainId, Set options) throws KassaException;

    /**
     * заявка человека, ожидающая подписания оферты
     *
     * @param options      какие коллекции инициализируем
     * @param peopleMainId id человека
     */
    CreditRequest findCreditRequestWaitingSign(int peopleMainId, Set options) throws KassaException;

    /**
     * генерируем оферту для продления
     *
     * @param people        лицо
     * @param prolong       продление
     * @param sumpercent    сумма процентов
     * @param creditDataend дата окончания кредита
     * @param dateSign      дата подписания оферты
     */
    String generateAgreement(PeopleMain people, Prolong prolong, Double sumpercent, Date creditDataend, Date dateSign) throws KassaException;

    /**
     * генерируем оферту для продления
     *
     * @param people        лицо
     * @param refinance     рефинансирование
     * @param sum           сумма для нового кредита
     * @param refinanceDate дата окончания кредита
     * @param dateSign      дата подписания оферты
     */
    String generateAgreement(PeopleMain people, Refinance refinance, Double sum,
                             Date dateSign, Date refinanceDate) throws KassaException;
    /**
     * инициализируем продление
     *
     * @param people   лицо
     * @param credit   кредит
     * @param dateLong дата
     * @param limits   разные параметры
     */
    Prolong initProlong(PeopleMain people, Credit credit, Date dateLong, Map<String,
            Object> limits) throws KassaException;

    /**
     * ищем человека хотя бы с одной заполненной до конца заявкой
     *
     * @param ppl человек
     */
    int findManWithPrivateCabinet(PeopleMainEntity ppl);

    /**
     * возвращаем список соц.сетей с id приложений
     */
    Map<String, String> getSocNetworkApp();

    /**
     * сохраняем id пользователя в соц.сети
     *
     * @param creditRequestId id заявки
     * @param userId          id пользователя в соц.сети
     * @param socNetwork      id соц.сети
     */
    void saveSocialId(Integer creditRequestId, String userId, Integer socNetwork) throws KassaException;

    /**
     * генерируем ссылку по данным пользователей
     *
     * @param username логин
     * @param password пароль
     */
    String getLink(String username, String password) throws KassaException;

    /**
     * генерируем письмо
     *
     * @param ppl      человек
     * @param reportId id отчета
     * @param params   параметры для отчета
     */
    String generateEmail(PeopleMain ppl, String code, Map<String, Object> params) throws KassaException;

    /**
     * инициализируем creditRequest, если известны сумма и дни кредита
     *
     * @param limits     параметры
     * @param people     человек
     * @param dateCredit дата кредита
     * @param sumCredit  сумма
     * @param daysCredit дней кредита
     */
    CreditRequest initCreditRequestForPeople(PeopleMain people, Date dateCredit, Double sumCredit,
                                             Integer daysCredit, Map<String, Object> limits) throws KassaException;

    /**
     * сохраняем решение по кредиту
     *
     * @param creditRequestId id запроса
     * @param bAccepted       отказан или принят
     * @param rejectCode      код отказа
     */
    void saveCreditDecision(Integer creditRequestId, boolean bAccepted, Integer rejectCode) throws KassaException;

    /**
     * ищем номер кредита
     */
    public BigInteger findMaxCreditNumber();

    /**
     * ищем последний номер заявки за текущий день
     */
    Integer findMaxCreditRequestNumber(Date dt);

    /**
     * список заявок без займов
     *
     * @param peopleId id человека
     */
    List<CreditRequest> listCreditRequestWithoutCredits(Integer peopleId) throws KassaException;

    /**
     * запись продления кредита
     *
     * @param dt        дата продления
     * @param creditId  id кредита
     * @param pl        продление
     * @param ipAddr    ip адрес
     * @param userAgent user-agent пользователя
     */
    ProlongEntity saveLongRequestNew(Integer creditId, Prolong pl, Date dt, String ipAddr, String userAgent) throws KassaException;

    /**
     * начать продление
     *
     * @param creditId кредит
     */
    void startProlong(Integer creditId) throws KassaException;

    /**
     * Найти заявку по уникальному номеру
     *
     * @param uniqueNumber уникальный номер
     * @return заявка
     */
    CreditRequest findCreditRequestByUniqueNumber(String uniqueNumber);

    /**
     * ищем последнюю заявку с конечным статусом
     *
     * @param peopleMainId человек
     * @param options
     * @throws KassaException
     */
    public CreditRequest findLastCreditRequestClosed(int peopleMainId, Set options) throws KassaException;

    /**
     * сохраняем переменные
     *
     * @param className название класса
     * @param objectId  id объекта
     * @param vars      параметры
     */
    void saveMiscVariables(String className, int objectId,
                           Map<String, Object> vars);

    /**
     * список заявок для коллектора
     *
     * @param date            на определенную дату
     * @param dateRange       в диапазоне дат
     * @param creditRequestId определенная заявка
     */
    public List<CreditRequest> findCreditRequestsForCollector(Date date, DateRange dateRange, Integer creditRequestId);

    /**
     * сохраняем заявку
     *
     * @param creditRequest заявка
     */
    CreditRequestEntity saveCreditRequest(CreditRequestEntity creditRequest);

    /**
     * платеж клиенту
     *
     * @param creditId   id кредита
     * @param accountId  id счета
     * @param creditSum  сумма
     * @param createDate дата
     * @param paysumId   вид суммы
     * @throws KassaException
     */
    public Payment createPaymentToClient(int creditId, int accountId,
                                         Double creditSum, Date createDate, Integer paysumId)
            throws KassaException;

    /**
     * выгружаем данные в csv
     *
     * @param where   условие
     * @param table   таблица
     * @param header  заголовок для csv
     * @param columns колонки для csv
     */
    public String execCopyToCsv(String where, String table, String header, String columns);
    /**
     * выгружаем данные в csv
     *
     * @param where -  условие
     * @param table -  таблица
     * @param header - заголовок для csv
     * @param columns - колонки для csv
     * @param delimiter - разделитель
     */
    public String execCopyToCsv(String where, String table, String header, String columns,String delimiter);
    /**
     * данные о новом устройстве
     *
     * @param creditRequestId id заявки
     * @param resolutionW     разрешение по горизонтали
     * @param resolutionH     разрешение по вертикали
     * @param userAgent       данные из http header
     */
    public void newDeviceInfo(Integer creditRequestId, Integer resolutionW, Integer resolutionH,
                              String userAgent);

    /**
     * ищем данные об устройстве по заявке
     *
     * @param creditRequestId id заявки
     */
    public DeviceInfoEntity findDeviceInfo(Integer creditRequestId);

    /**
     * Список всех активных заявок
     *
     * @param peopleMainId id человека
     * @param options
     * @throws KassaException
     */
    List<CreditRequest> findCreditRequestActives(int peopleMainId, Set options)
            throws KassaException;

    /**
     * сохранить местоположение
     *
     * @param creditRequest запрос кредита
     * @param ipAddress     ip адрес
     */
    public String saveGeoLocation(CreditRequestEntity creditRequest, String ipAddress);

    /**
     * Создаём офлайновую заявку для данного человека
     * @param creditSum
     * @param creditDays
     * @param pmain
     * @param productCode
     * @param user_id пользователь который создает заявку
     * @return
     * @throws KassaException
     */
	public CreditRequest createOffline(double creditSum, int creditDays,
			PeopleMain pmain, String productCode, Integer user_id);

    /**
     * ищем местоположение
     *
     * @param ipAddress     ip адрес
     */
    public String getGeoLocation(String ipAddress) throws KassaException;
    /**
     * сохраняем заявку
     *
     * @param creditRequest заявка
     */
	CreditRequest saveCreditRequest(CreditRequest creditRequest);
	/**
	 * ищем список переменных из стратегии по id объекта
	 * @param className - название класса
	 * @param id - id объекта
	 * @return
	 */
	List<Misc> findMiscVarsByClassAndId(String className,Integer id);
	/**
	 * ищем список переменных из стратегии по id заявки
	 * @param creditRequestId - id заявки
	 * @return
	 */
	List<Misc> findMiscVarsCreditRequest(Integer creditRequestId);
	
    /**
     * генерируем оферту при минимальных личных данных, для проекта Digital
     *
     * @param ccRequest  заявка
     * @param dateCredit дата кредита
     * @param mode       0 - первоначально, 1 - при подписании
     */
	String generateAgreementWithoutData(CreditRequest ccRequest,
			Date dateCredit, Integer mode) throws KassaException;

    /**
     * генерируем оферту при минимальных личных данных, для проекта Digital
     *
     * @param credit  кредит
     * @param smsCodeOferta код для подписания оферты
     */
    String generateAgreementWithoutData(Credit credit,String smsCodeOferta) throws KassaException;
	
    /**
     * генерируем оферту для продления при минимальных личных данных, для проекта Digital
     *
     * @param people        лицо
     * @param prolong       продление
     * @param sumpercent    сумма процентов
     * @param creditDataend дата окончания кредита
     * @param dateSign      дата подписания оферты
     */
    String generateAgreementWithoutData(PeopleMain people, Prolong prolong, Double sumpercent, Date creditDataend, Date dateSign) throws KassaException;

    /**
     * генерируем оферту для продления при минимальных личных данных, для проекта Digital
     *
     * @param people        лицо
     * @param refinance     рефинансирование
     * @param sum           сумма для нового кредита
     * @param refinanceDate дата окончания кредита
     * @param dateSign      дата подписания оферты
     */
    String generateAgreementWithoutData(PeopleMain people, Refinance refinance, Double sum,
                             Date dateSign, Date refinanceDate) throws KassaException;
}
