package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectStateDefExt;

import javax.ejb.Local;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface CreditBeanLocal {

    /**
     * возвращает активный кредит человека
     * peopleMain - человек
     */
    public Credit creditActive(Integer peopleMainId);

    /**
     * возвращает список кредитов человека
     *
     * @param peopleMain человек
     */
    public List<Credit> creditArchive(PeopleMainEntity peopleMain);

    /**
     * Метод возвращает список кредитов
     *
     * @param filter Фильтр
     * @return список кредитов
     */
    List<Credit> listCredit(CreditFilter filter);

    /**
     * возвращает список кредитов по условиям
     *
     * @param nFirst
     * @param nCount            кол-во записей
     * @param sorting           сортировка
     * @param options           коллекции
     * @param peopleMainId      человек
     * @param accountTypeId     вид счета
     * @param partners          партнер
     * @param creditDataBeg     дата начала кредита
     * @param creditDataEnd     дата окончания кредита
     * @param creditTypes       вид кредита
     * @param isOver            кредит закончен
     * @param id_credit         № кредита
     * @param isSameOrg         выдан нашей организацией
     * @param creditDataEndFact дата окончания кредита фактическая
     * @param creditStatuses    статус кредита
     * @param creditSum         сумма кредита
     * @param stake             ставка кредита
     * @param isActive          активен или приостановлен
     * @deprecated заменен на listCredit(CreditFilter filter);
     */
    @Deprecated
    public List<Credit> listCredit(int nFirst, int nCount, SortCriteria[] sorting,
                                   Set options, Integer peopleMainId, Integer accountTypeId,
                                   Integer partners, DateRange creditDataBeg,
                                   DateRange creditDataEnd, Integer creditTypes, Boolean isOver,
                                   String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,
                                   Integer creditStatuses, MoneyRange creditSum, MoneyRange stake,
                                   String surname, Integer isActive);

    /**
     * возвращает список кредитов по условиям
     *
     * @param nFirst
     * @param nCount            кол-во записей
     * @param sorting           сортировка
     * @param options           коллекции
     * @param peopleMainId      человек
     * @param accountTypeId     вид счета
     * @param partners          партнер
     * @param creditDataBeg     дата начала кредита
     * @param creditDataEnd     дата окончания кредита
     * @param creditTypes       вид кредита
     * @param isOver            кредит закончен
     * @param id_credit         № кредита
     * @param isSameOrg         выдан нашей организацией
     * @param creditDataEndFact дата окончания кредита фактическая
     * @param creditStatuses    статус кредита
     * @param creditSum         сумма кредита
     * @param stake             ставка кредита
     * @param isActive          активен или приостановлен
     * @param workplace_id      подразделение
     * @deprecated заменен на listCredit(CreditFilter filter);
     */
    @Deprecated
    public List<Credit> listCredit(int nFirst, int nCount, SortCriteria[] sorting,
                                   Set options, Integer peopleMainId, Integer accountTypeId,
                                   Integer partners, DateRange creditDataBeg,
                                   DateRange creditDataEnd, Integer creditTypes, Boolean isOver,
                                   String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,
                                   Integer creditStatuses, MoneyRange creditSum, MoneyRange stake,
                                   String surname, Integer isActive, Integer workplace_id);


    /**
     * возвращает кол=во кредитов по условиям
     *
     * @param peopleMainId      человек
     * @param accountTypeId     вид счета
     * @param partners          партнер
     * @param creditDataBeg     дата начала кредита
     * @param creditDataEnd     дата окончания кредита
     * @param creditTypes       вид кредита
     * @param isOver            кредит окончен
     * @param id_credit         № кредита
     * @param isSameOrg         выдан нашей организацией
     * @param creditDataEndFact фактическая дата окончания кредита
     * @param creditStatuses    статус кредита
     * @param creditSum         сумма кредита
     * @param stake             ставка кредита
     * @deprecated              Заменен на countCredit(CreditFilter filter)
     */
    @Deprecated
    public int countCredit(Integer peopleMainId, Integer accountTypeId,
                           Integer partners, DateRange creditDataBeg,
                           DateRange creditDataEnd, Integer creditTypes, Boolean isOver,
                           String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,
                           Integer creditStatuses, MoneyRange creditSum, MoneyRange stake,
                           String surname, Integer isActive);

    /**
     * возвращает кол=во кредитов по условиям
     */
    int countCredit(CreditFilter filter);

    /**
     * возвращает кол=во кредитов по условиям
     *
     * @param peopleMainId      человек
     * @param accountTypeId     вид счета
     * @param partners          партнер
     * @param creditDataBeg     дата начала кредита
     * @param creditDataEnd     дата окончания кредита
     * @param creditTypes       вид кредита
     * @param isOver            кредит окончен
     * @param id_credit         № кредита
     * @param isSameOrg         выдан нашей организацией
     * @param creditDataEndFact фактическая дата окончания кредита
     * @param creditStatuses    статус кредита
     * @param creditSum         сумма кредита
     * @param stake             ставка кредита
     * @param workplace_id      подразделение
     * @deprecated              Заменен на countCredit(CreditFilter filter)
     */
    @Deprecated
    public int countCredit(Integer peopleMainId, Integer accountTypeId,
                           Integer partners, DateRange creditDataBeg,
                           DateRange creditDataEnd, Integer creditTypes, Boolean isOver,
                           String id_credit, Boolean isSameOrg, DateRange creditDataEndFact,
                           Integer creditStatuses, MoneyRange creditSum, MoneyRange stake,
                           String surname, Integer isActive, Integer workplace_id);

    /**
     * удаляем кредит по id, если по нему не было никакой информации
     *
     * @param id кредита
     * @throws KassaException
     */
    public void removeCredit(int id) throws KassaException;

    /**
     * активный кредит человека
     *
     * @param peopleMainId id человека
     * @param options      какие коллекции инициализируем
     */
    public Credit findCreditActive(int peopleMainId, Set options) throws KassaException;

    /**
     * сохраняем статус кредита
     *
     * @param creditId   id
     * @param statusId   статус, если не меняем статус, а только ставим дату, то null
     * @param statusDate дата статуса, если не ставим дату, а только меняем статус, то null
     */
    public void saveCreditStatus(int creditId, Integer statusId, Date statusDate);

    /**
     * ищем продление в статусе черновика
     *
     * @param creditId кредит
     */
    public ProlongEntity findProlongDraft(int creditId);

    /**
     * Закрываем полностью выплаченный кредит
     *
     * @param credit         кредит
     * @param dateClose      дата закрытия
     * @param sumOverPayment сумма переплаты, если есть
     * @param paymentId      id платежа
     * @param sumBack        сумма к возврату на текущее время
     * @param systemPay      платеж из кошелька
     * @param bonusPay       платеж бонусами
     * @param sumMinPayment  сумма оплаченных процентов
     */
    public void closeCredit(CreditEntity credit, Date dateClose, Double sumOverPayment, Integer paymentId, 
    		Double sumBack,PaymentEntity systemPay,PaymentEntity bonusPay,Double sumMinPayment);

    /**
     * возвращает запись продления
     *
     * @param prolongId продление
     */
    public ProlongEntity getProlongEntity(int prolongId);

    /**
     * возвращает запись продления
     *
     * @param prolongId продление
     */
    public Prolong getProlong(int prolongId, Set options);

    /**
     * удаляем черновик продления
     *
     * @param creditId id кредита
     * @throws KassaException
     */
    public void cancelProlongDraft(Integer creditId) throws KassaException;

    /**
     * удаляем черновик рефинансирования
     *
     * @param creditId id кредита
     * @throws KassaException
     */
    public void cancelRefinanceDraft(Integer creditId) throws KassaException;

    /**
     * сохраняет сумму долга на дату, считает дни просрочки, использовать в цикле в процессе
     *
     * @param creditId кредит
     * @param dt       дата, на которую считаем
     */
    public void saveOverdueSum(Integer creditId, Date dt);

    /**
     * возвращает список кредитов человека
     *
     * @param peopleMain    id человека
     * @param creditRequest id заявки
     * @param partner       id партнера
     * @param sameOrg       брал кредит в этой организации или нет
     * @param status        статус кредита
     */
    public List<CreditEntity> findCredits(PeopleMainEntity peopleMain,
                                          CreditRequestEntity creditRequest, PartnersEntity partner,
                                          Boolean sameOrg, ReferenceEntity status);

    /**
     * меняем  данные кредита, добавленного клиентом
     */
    public void changeCredit(Credit cr, PeopleMain pmain, Integer crequestId, int partnerId) throws PeopleException;

    /**
     * Передать кредит внутреннему коллектору
     *
     * @param creditId id кредита
     * @param date     дата передачи
     */
    public void creditToCollector(int creditId, Date date);

    /**
     * Передать кредит внутреннему коллектору
     *
     * @param creditId id кредита
     */
    public void creditToCollector(int creditId);

    /**
     * Передать кредит внутреннему коллектору
     *
     * @param creditId id кредита
     * @param date     дата передачи
     */
    public void creditToOuterCollector(int creditId, Date date);

    /**
     * Приостановить кредит
     *
     * @param creditId id кредита
     * @param date     дата приостановки
     */
    public void stopCredit(int creditId, Date date, StateRef stopPoint);

    /**
     * Возобновить кредит
     *
     * @param creditId id кредита
     * @param date     дата возобновления
     */
    public void restartCredit(int creditId, Date date);

    /**
     * Удалить кредит
     *
     * @param creditId id кредита
     */
    public void deleteCredit(int creditId);

    /**
     * сохранить кредит
     *
     * @param credit данные по кредиту
     */
    void saveCredit(Credit credit);

    /**
     * возвращает список сумм человека
     *
     * @param peopleMainId человек
     * @param sumId        вид суммы
     */
    public List<PeopleSums> listPeopleSums(Integer peopleMainId, Integer sumId);

    /**
     * проверяет наличие суммы человека
     *
     * @param credit_id идентификатор кредита
     * @param date дата
     * @param summ сумма
     */
    public Boolean checkPeopleSums(Integer credit_id, DateRange date, Double amount);

    /**
     * сохраняем сумму человека в системе
     *
     * @param peopleMainId  человек
     * @param credit        кредит
     * @param operation     операция
     * @param operationType тип операции
     * @param amount        сумма
     */
    public void savePeopleSum(Integer peopleMainId, CreditEntity credit, Integer operation, Integer operationType, Double amount, Date date);

    /**
     * сохраняем подробности по кредиту
     *
     * @param creditId        кредит
     * @param operationId     тип операции
     * @param eventDate       дата события
     * @param eventEndDate    дата окончания события
     * @param amountMain      основной долг
     * @param amountPercent   проценты
     * @param amountAll       общий долг
     * @param amountOperation сумма операции
     * @param anotherId       id из таблицы, соотв. по смыслу
     */
    public CreditDetailsEntity saveCreditDetail(CreditEntity creditId, Integer operationId, Date eventDate, Date eventEndDate,
                                                Double amountMain, Double amountPercent, Double amountAll, Double amountOperation, 
                                                Double amountPenalty,Integer anotherId) throws KassaException;

    /**
     * сохраняем подробности по кредиту
     *
     * @param creditId        кредит
     * @param operationId     тип операции
     * @param eventDate       дата события
     * @param eventEndDate    дата окончания события
     * @param amountMain      основной долг
     * @param amountPercent   проценты
     * @param amountAll       общий долг
     * @param amountOperation сумма операции
     * @param anotherId       id из таблицы, соотв. по смыслу
     * @param delay           дней просрочки
     * @param amountOverpay
     */
    public CreditDetailsEntity saveCreditDetail(CreditEntity creditId, Integer operationId, Date eventDate, Date eventEndDate,
                                                Double amountMain, Double amountPercent, Double amountAll, Double amountOperation, 
                                                Double amountPenalty,Integer anotherId,Integer delay,
                                                Double amountOverpay) throws KassaException;
    /**
     * сохраняем разбивку сумм для кредита
     *
     * @param creditId       id кредита
     * @param sumId          id суммы
     * @param operationId    id операции (приход-расход)
     * @param amountDate     дата суммы
     * @param amount         сумма`
     * @param creditDetailId id операции из деталей кредита
     * @throws KassaException
     */
    public CreditSumsEntity saveCreditSum(Integer creditId, Integer sumId, Integer operationId,
                                          Date amountDate, Double amount, Integer creditDetailId) throws KassaException;

    /**
     * ищем детали по кредиту
     *
     * @param creditId    кредит
     * @param operationId вид операции
     * @param eventDate   диапазон дат
     */
    public List<CreditDetailsEntity> findCreditDetails(Integer creditId, Integer operationId, DateRange eventDate);
    /**
     * ищем операцию выдачи кредита
     *
     * @param creditId    кредит
     * @param eventDate   диапазон дат
     */
    public CreditDetailsEntity findCreditOperation(Integer creditId, DateRange eventDate);
    /**
     * ищем детали по кредиту выданному через менеджера
     *
     * @param creditId    кредит
     * @param operationId вид операции
     * @param eventDate   диапазон дат
     */
    public List<CreditDetailsEntity> findCreditDetailsWithUnit(Integer creditId, Integer operationId, DateRange eventDate);

    /**
     * ищем последнюю запись в деталях, по которой кредит закрылся
     *
     * @param creditId  кредит
     * @param eventDate диапазон дат
     */
    public CreditDetailsEntity findLastCreditDetail(Integer creditId, DateRange eventDate);

    /**
     * возвращаем сумму по определенному полю, по определенной операции, за определенное время
     *
     * @param sumName     название поля суммы в БД
     * @param creditId    кредит
     * @param operationId вид операции
     * @param eventDate   диапазон дат
     */
    public Double getSumCreditDetail(String sumName, Integer creditId, Integer operationId, DateRange eventDate);

    /**
     * возвращает список статусов кредита
     */
    Map<String, WorkflowObjectStateDefExt> getCreditStates();

    /**
     * Передаём кредит в суд
     *
     * @param creditId id кредита
     * @param date     дата передачи
     */
    void creditToCourt(int creditId, Date date);

    void rejectCredit(int id) throws KassaException;

    /**
     * меняем даты у кредита и сопутствующих таблиц
     *
     * @param id          id кредита
     * @param processDate дата проведения платежа
     * @param paymentId   id платежа
     */
    public void changeCreditStart(Integer id, Date processDate, Integer paymentId);

    /**
     * кредиты для расчета процентов в 1С
     *
     * @param date на какую дату считаем
     */
    public List<CreditEntity> listCreditsForCalcPercent(Date date);

    /**
     * кредиты для расчета процентов в 1С выданные в подразделении
     *
     * @param date на какую дату считаем
     */
    public List<CreditEntity> listCreditsForCalcPercentWithUnit(Date date);

    /**
     * сохраняем запрос на рефинансирование
     *
     * @param creditId  id кредита
     * @param refinance данные рефинансирования
     * @param date      дата
     * @throws KassaException
     */
    public RefinanceEntity saveRefinanceRequest(Integer creditId, Refinance refinance, Date date) throws KassaException;

    /**
     * стартуем рефинансирование
     *
     * @param creditId id кредита
     * @throws KassaException
     */
    public void startRefinance(Integer creditId) throws KassaException;

    /**
     * список сумм по кредиту
     *
     * @param creditId       id кредита
     * @param sumId          id суммы
     * @param operationId    id операции
     * @param amountDate     даты суммы
     * @param creditDetailId id деталей кредита
     */
    public List<CreditSumsEntity> findCreditSums(Integer creditId, Integer sumId, Integer operationId,
                                                 DateRange amountDate, Integer creditDetailId);


    /**
     * создать черновик для рефинансирования
     *
     * @param creditId      id кредита
     * @param dateStart     дата начала
     * @param refinanceDays количество дней для рефинансирования
     * @param amount        сумма
     * @param stake         ставка
     */
    RefinanceEntity createRefinanceDraft(Integer creditId, Date dateStart,
                                         Integer refinanceDays, Double amount, Double stake);

    /**
     * инициализируем рефинансирование
     *
     * @param credit рефинансируемый кредит
     * @param limits константы
     * @throws KassaException
     */
    public Refinance initRefinance(Credit credit, Map<String, Object> limits) throws KassaException;

    /**
     * меняем статус кредита
     *
     * @param creditId         id кредита
     * @param statusId         статус
     * @param crequestStatusId статус заявки, если не надо менять, то null
     * @param date             дата
     * @param eventCode        код события для лога
     */
    public CreditEntity changeCreditStatus(int creditId, int statusId, Integer crequestStatusId, 
    		Date date,Integer eventCode);
    /**
     * новый кредит 
     * 
     * @param credit - кредит
     * @param peopleMainId - id человека
     * @param creditRequestId - id заявки
     * @param partnerId - id партнера
     * @param sameOrg - выдан нашей организацией
     * @param overdueId - максимальная просрочка
     * @param creditOrganizationId - организация, выдавшая кредит
     * @param creditSum - сумма кредита
     * @param creditDatabeg - дата начала кредита
     * @param isOver - кредит погашен
     * @param creditTypeId - вид кредита
     * @param creditCurrencyId - валюта кредита
     * @param creditRelationId - отношение заемщика к кредиту
     * @param creditPurposeId - цель кредита
     * @param creditDataend - дата окончания по графику
     * @param creditDataendfact - дата окончания фактическая
     * @param creditMonthlyPayment - ежемесячный платеж
     * @return
     */
    public CreditEntity newCredit(CreditEntity credit,Integer peopleMainId, Integer creditRequestId, Integer partnerId,
    		Boolean sameOrg,String overdueId,Integer creditOrganizationId,Double creditSum,Date creditDatabeg,
    		Boolean isOver,Integer creditTypeId,Integer creditCurrencyId,Integer creditRelationId,Integer creditPurposeId,
    		Date creditDataend,Date creditDataendfact,Double creditMonthlyPayment) throws PeopleException;
    /**
     * инициализируем кредит клиента
     * 
     * @param peopleMain - человек
     * @param creditRequestId - id заявки
     * @return
     */
    public Credit initCredit(PeopleMain peopleMain,Integer creditRequestId);
    /**
     * добавляем кредит
     * 
     * @param credit - кредит
     * @param peopleMainId - id человека
     * @return
     */
    public Credit addCredit(Credit credit,Integer peopleMainId) throws PeopleException; 

}
