package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.kzegovdata.Address;

import javax.ejb.ObjectNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PeopleBeanLocal {

    /**
     * возвращает список людей с контактами
     *
     * @param contactTypeId   - вид контакта (по справочнику id=16)
     * @param value           - значение контакта
     * @param peopleMainId    - id человека
     * @param partnerId       - id партнера, от которого поступил этот контакт
     * @param creditRequestId - id заявки
     */
    public List<PeopleContactEntity> findPeopleByContact(Integer contactTypeId, String value, Integer peopleMainId,
                                                         Integer partnerId, Integer isActive, Integer creditRequestId);

    /**
     * записывает данные о контакте человека
     *
     * @param peopleMain    - id человека
     * @param contactTypeId - вид контакта (по справочнику id=16)
     * @param value         - значение контакта
     */
    public PeopleContactEntity setPeopleContactClient(PeopleMainEntity peopleMain,
                                                      int contactTypeId, String value, Boolean available);

    /**
     * записывает данные о контакте человека от любого партнера
     *
     * @param peopleMain    - id человека
     * @param partner       - id партнера
     * @param contactTypeId - вид контакта (по справочнику id=16)
     * @param value         - значение контакта
     */
    public PeopleContactEntity setPeopleContact(PeopleMainEntity peopleMain,
                                                PartnersEntity partner, int contactTypeId,
                                                String value, Boolean available, Integer creditRequestId, Date date);

    public PeopleContactEntity setPeopleContact(PeopleMainEntity peopleMain, PartnersEntity partner, int contactTypeId, String value,
    		Boolean available,Integer creditRequestId,Date date,Boolean needCheck);
    /**
     * добавляем контакт человека
     *
     * @param contact      - контакт
     * @param peopleMainId - человек
     * @return
     * @throws PeopleException
     */
    public PeopleContact addPeopleContact(PeopleContact contact, Integer peopleMainId) throws PeopleException;

    /**
     * инициализируем контакт человека
     *
     * @param peopleMain    - человек
     * @param contactTypeId - вид контакта
     * @return
     */
    public PeopleContact initPeopleContact(PeopleMain peopleMain, Integer contactTypeId);

    /**
     * возвращает список ПД человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна или нет запись
     */
    public List<PeoplePersonalEntity> findPeoplePersonal(PeopleMainEntity peopleMain,
                                                         CreditRequestEntity creditRequest, Integer partnerId, Integer active);

    /**
     * возвращает контакт человека
     *
     * @param contactTypeId - вид контакта (по справочнику id=16)
     * @param value         - значение контакта
     */
    public PeopleContactEntity findPeopleByContactClient(Integer contactTypeId,
                                                         String value);

    /**
     * возвращает адрес человека
     *
     * @param peopleMain    - id человека
     * @param partnerId     - id партнера
     * @param creditRequest - id заявки
     * @param addrType      - вид адреса
     * @param active        - активность
     */
    public AddressEntity findAddress(PeopleMainEntity peopleMain, Integer partnerId,
                                     CreditRequestEntity creditRequest, Integer addrType, Integer active);

    /**
     * возвращает адреса человека
     *
     * @param peopleMain    - id человека
     * @param partnerId     - id партнера
     * @param creditRequest - id заявки
     * @param addrType      - вид адреса
     * @param active        - активность
     */
    public List<AddressEntity> findAddresses(PeopleMainEntity peopleMain, Integer partnerId,
                                     CreditRequestEntity creditRequest, Integer addrType, Integer active);
    /**
     * возвращает документ человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна запись или нет
     * @param docType       - вид документа
     */
    public DocumentEntity findDocument(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType);

    /**
     * возвращает документы человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна запись или нет
     * @param docType       - вид документа
     */
    public List<DocumentEntity> findDocuments(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType);
    
    /**
     * возвращает документы человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна запись или нет
     * @param docType       - вид документа
     * @param excludeClient - все документы кроме документов клиента
     */
    public List<DocumentEntity> findDocuments(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType,Boolean excludeClient);
    
    public DocumentOtherEntity findDocumentOther(PeopleMainEntity peopleMain,
                                                 CreditRequestEntity creditRequest, Integer partnerId,
                                                 Integer active, Integer docType);

    /**
     * возвращает список дополнительных данных человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна запись или нет
     */
    public List<PeopleMiscEntity> findPeopleMisc(PeopleMainEntity peopleMain,
                                                 CreditRequestEntity creditRequest, Integer partnerId, Integer active);

    /**
     * возвращает список других дополнительных данных человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partnerId     - id партнера
     * @param active        - активна запись или нет
     */
    public List<PeopleOtherEntity> findPeopleOther(PeopleMainEntity peopleMain,
                                                   CreditRequestEntity creditRequest, Integer partnerId, Integer active);

    /**
     * возвращает список счетов для человека
     *
     * @param peopleMain    - id человека
     * @param accountTypeId - вид счета
     * @param isActive      - активна запись или нет
     */
    public List<AccountEntity> findAccounts(PeopleMainEntity peopleMain,
                                            Integer accountTypeId, Integer isActive);

    /**
     * возвращает счет для человека
     *
     * @param peopleMain    - id человека
     * @param accountTypeId - вид счета
     */
    public AccountEntity findAccountActive(PeopleMainEntity peopleMain,
                                           Integer accountTypeId);

    /**
     * возвращает занятость для человека
     *
     * @param peopleMain    - id человека
     * @param creditRequest - id заявки
     * @param partner       - id партнера
     * @param current       - текущее место или прошлое
     */
    public EmploymentEntity findEmployment(PeopleMainEntity peopleMain, CreditRequestEntity creditRequest, PartnersEntity partner, Integer current);

    /**
     * возвращает активные ПД человека
     *
     * @param peopleMain - id человека
     */
    public PeoplePersonalEntity findPeoplePersonalActive(PeopleMainEntity peopleMain);

    /**
     * возвращает дополнительные активные данные человека
     *
     * @param peopleMain - id человека
     */
    public PeopleMiscEntity findPeopleMiscActive(PeopleMainEntity peopleMain);

    /**
     * возвращает другие дополнительные активные данные человека
     *
     * @param peopleMain - id человека
     */
    public PeopleOtherEntity findPeopleOtherActive(PeopleMainEntity peopleMain);

    /**
     * ищет активный контакт человека, возвращает его значение
     *
     * @param contactTypeId - вид контакта
     */
    public String findContactClient(Integer contactTypeId, Integer peopleMainId);

    /**
     * ищем человека по ПД (могут быть и архивные)
     *
     * @param surname   - фамилия
     * @param name      - имя
     * @param midname   - отчество
     * @param birthdate - дата рождения
     */
    public PeoplePersonalEntity findPeopleByPersonalData(String surname, String name, String midname, Date birthdate);

    /**
     * ищем человека в черном списке по состоянию на дату
     *
     * @param peopleMain
     * @param dt
     * @param reasonId
     */
    public BlacklistEntity findInBlackList(PeopleMainEntity peopleMain, Date dt, Integer reasonId) throws PeopleException;

    /**
     * ищем человека по идентификатору - инн или снилс
     *
     * @param inn   - инн
     * @param snils - снилс
     */
    public PeopleMainEntity findPeopleMain(String inn, String snils);

    /**
     * ищем брачного партнера человека
     *
     * @param peopleMain - человек
     */
    public SpouseEntity findSpouseActive(PeopleMainEntity peopleMain);

    /**
     * добавляем брачного партнера,
     *
     * @param databeg - дата начала совместного проживания
     */
    public void addSpouse(PeopleMainEntity peopleMain, String surname, String name, String midname, Date birthdate, String
            mobilephone, Integer spouseId, Date databeg, Integer typeworkId);

    /**
     * меняем персональные данные человека
     *
     * @param dateChange - дата изменений
     */
    public void changePersonalData(PeoplePersonal pper, Integer peopleMainId, int partnerId, Date dateChange);

    PeoplePersonalEntity changePersonalData(Integer peopleId, String lastName, String firstName, String middleName, Date birthDate,
                                            String birthPlace, Integer genderId);

    /**
     * меняем дополнительные данные человека
     *
     * @param dateChange - дата изменений
     */
    public void changeMiscData(PeopleMisc pmisc, Integer peopleId, int partnerId, Date dateChange);

    PeopleMiscEntity changeMiscData(Integer peopleId, Integer maritalStatus, int childrenCount, int underage, boolean hasCar);

    PeopleMiscEntity changeMiscAddressData(Integer peopleId, Integer realtyDateId, Date realtyDate, Integer regDateId,
    		Date regDate, Integer realtyCode);

    public void changeOtherData(PeopleOther pmisc, Integer peopleId, int partnerId, 	Date dateChange);

    /**
     * меняем  данные человека - персональные, дополнительные, документ
     *
     * @param dateChange - дата изменений
     */
    public void changePeopleData(PeoplePersonal pper, PeopleMisc pmisc, Documents activePassportRF, PeopleMain pmain, int partnerId, Date dateChange) throws PeopleException;

    /**
     * меняем  данные  документа
     */
    public void changeDocument(Documents dct, Integer peopleId, int partnerId);

    DocumentEntity changeDocument(Integer peopleId, String passportSerial, String passportNumber, String passportDepartmentCode,
                                  Date passportIssueDate, String passportIssueOrganization);

    public void changeDocumentOther(DocumentsOther dct, Integer peopleId, int partnerId);

    public void removeDocumentOther(Integer peopleId, int partnerId);

    /**
     * делаем контакт архивным
     *
     * @param pc - данные контакта
     */
    public void removeContact(PeopleContact pc) throws PeopleException;

    /**
     * меняем контакт
     */
    public void changeContact(PeopleContact pc, Integer peopleId, int partnerId);

    PeopleContactEntity changeContact(Integer id, Integer peopleId, String type, boolean available)
            throws ObjectNotFoundException;

    /**
     * добавляем данные о партнере
     *
     * @param dateChange - дата изменений
     */
    public void changeSpouse(Spouse sp, PeopleMain pmain, int partnerId, Date dateChange) throws PeopleException;

    /**
     * меняем данные о занятости
     *
     * @param empl     - занятость
     * @param peopleId - человек
     * @param dt       - дата изменений
     */
    public void changeEmployment(Employment empl, Integer peopleId, Date dt);

    EmploymentEntity changeEmployment(Integer peopleId, Integer educationId, Integer occupationId, Integer professionId,
                                      Integer salaryFrequenceId, Integer additionalIncomeSourceId, String employmentPlace,
                                      String position, Double salary, Double additionalIncome, Date enterEmploymentDate,
                                      Date enterPositionDate);

    /**
     * меняем данные адреса
     *
     * @param adr       - адрес
     * @param peopleId  - человек
     * @param partnerId - партнер
     * @param dt
     */
    public void changeAddress(FiasAddress adr, Integer peopleId, int partnerId, Date dt);

    /**
     * меняем данные адреса, полученного от сервиса
     * Портал Открытые данные Электронного правительства Республики Казахстан
     * http://data.egov.kz
     *
     * @param address   - адрес
     * @param peopleId  - человек
     * @param partnerId - партнер
     * @param dt
     */
    public void changeAddressKzEgovData(Address address, Integer peopleId, int partnerId, Date dt);

    /**
     * добавляем счет
     *
     * @param acc      - счет
     * @param peopleId - человек
     */
    public Account addAccount(Account acc, Integer peopleId);

    /**
     * новый счет
     *
     * @param account          - счет
     * @param peopleMainId     - id человека
     * @param date             - дата добавления
     * @param isWork           - карту выдал работодатель
     * @param contest          - согласие
     * @param accountNumber    - номер счета
     * @param accountTypeId    - вид счета
     * @param bik              - бик банка
     * @param corAccountNumber - кор.счет
     * @param cardNumber       - номер карты
     * @param cardNumberMasked - номер карты с маской
     * @param payonlineHash    - хэш payonline
     * @param cardName         - название карты
     * @param cardMonthEnd     - до какого месяца действительна
     * @param cardYearEnd      - до какого года действительна
     * @param isActive         - активность
     * @return
     */
    public AccountEntity newAccount(AccountEntity account, Integer peopleMainId, Date date,
                                    Integer isWork, Boolean contest, String accountNumber, Integer accountTypeId,
                                    String bik, String corAccountNumber, String cardNumber, String cardNumberMasked,
                                    String payonlineHash, String cardName, Integer cardMonthEnd, Integer cardYearEnd,
                                    Integer isActive);

    /**
     * сделать счет архивным
     *
     * @param accountId - вид счета
     */
    public void changeAccountActive(Integer accountId);

    /**
     * сделать контакт архивным
     *
     * @param contactId - вид контакта
     */
    public void changeContactActive(Integer contactId) throws PeopleException;

    /**
     * меняем счет
     *
     * @param acc      - счет
     * @param peopleId - человек
     */
    public Account changeAccount(Account acc, Integer peopleId);

    /**
     * инициализируем счет
     *
     * @param peopleMain  - человек
     * @param accountType - вид счета
     * @return
     */
    public Account initAccount(PeopleMain peopleMain, Integer accountType);

    /**
     * ищем активный контакт человека
     *
     * @param contactTypeId - вид контакта
     * @param peopleId      - id человека
     */
    public PeopleContactEntity findPeopleByContactMan(Integer contactTypeId, Integer peopleId);

    /**
     * возвращает счет для человека
     *
     * @param peopleMain - id человека
     */
    public AccountEntity findLastAccountActive(PeopleMainEntity peopleMain);

    /**
     * убрать брачного партнера для человека
     *
     * @param dt - дата изменений
     */
    public void closeSpouse(PeopleMainEntity peopleMain, Date dt);

    void closeSpouse(Integer peopleId, Date dt);

    /**
     * делаем адрес неактивным на текущую дату
     */
    public void closeAddress(AddressEntity addr, Date dt);

    /**
     * сохраняем контакт
     *
     * @param contactId - вид контакта
     */
    public void savePeopleContact(PeopleMain pmain, String userId, Integer contactId) throws PeopleException;

    /**
     * ищем действительный документ, введенный клиентом
     */
    public DocumentEntity findPassportActive(PeopleMainEntity pmain);

    public DocumentOtherEntity findDocumentOtherActive(PeopleMainEntity pmain);

    /**
     * ищем действительный адрес, введенный клиентом, по виду адреса
     */
    public AddressEntity findAddressActive(PeopleMainEntity pmain, Integer typeId);

    /**
     * сохраняем данные в черном списке
     *
     * @param pmain    - человек
     * @param reasonId - причина
     * @param date     - дата начала
     * @param comment  - текст
     */
    public BlacklistEntity saveBlackList(PeopleMainEntity pmain, Integer reasonId, Date date, String comment) throws PeopleException;

    /**
     * закрываем запись черного списка
     *
     * @param pmain - человек
     * @param date  - дата
     */
    public void removeFromBlackList(PeopleMainEntity pmain, Date date) throws PeopleException;

    /**
     * Находим документ по серии и номеру
     *
     * @param docTypeId - id вида документа
     * @param serial    - серия
     * @param number    - номер
     * @return документ, если не найден, то null
     */
    DocumentEntity findDocument(Integer docTypeId, String serial, String number);

    /**
     * выбираем кол-во заемщиков по условиям
     *
     * @param dateBirth  - дата рождения
     * @param surname    name, midname - фио
     * @param paspNumber paspSeries - документ
     * @param inn        - инн
     * @param snils      - снилс
     * @deprecated заменен на countPeopleMain(PeopleFilter filter)
     */
    @Deprecated
    int countPeopleMain(DateRange dateBirth, String surname, String name, String midname, String paspNumber,
                        String paspSeries, String inn, String snils, String email, String phone);

    /**
     * выбираем кол-во заемщиков по условиям
     *
     * @param filter Фильтр
     */
    int countPeopleMain(PeopleFilter filter);

    /**
     * выбираем список заемщиков по условиям
     *
     * @param dateBirth  - дата рождения
     * @param surname    name, midname - фио
     * @param paspNumber paspSeries - документ
     * @param inn        - инн
     * @param snils      - снилс
     * @deprecated заменен за listPeopleMain(PeopleFilter filter)
     */
    @Deprecated
    List<PeopleMain> listPeopleMain(int nFirst, int nCount, SortCriteria[] sorting, Set options,
                                    DateRange dateBirth, String surname, String name, String midname,
                                    String paspNumber, String paspSeries, String inn, String snils,
                                    String email, String phone);

    /**
     * выбираем список заемщиков по условиям
     *
     * @param filter Фильтр
     * @return список заемщиков
     */
    List<PeopleMain> listPeopleMain(PeopleFilter filter);

    /**
     * сохраняем поведение при заполнении заявки
     *
     * @param peopleMainId       - id человека
     * @param parameterId        - id параметра
     * @param parameterValue     - строковой параметр
     * @param parameterValueLong - числовой параметр
     * @param parameterValueDate - параметр даты
     * @param partnerId          - id партнера
     * @param date               - дата
     */
    void savePeopleBehavior(Integer ccRequestid, Integer peopleMainId, String parameterId, String parameterValue,
                            Object parameterValueLong, Date parameterValueDate, Integer partnerId, Date date);

    /**
     * Перегруженый метод для добавления бонусов
     *
     * @param peopleMainID  кому начисляем бонус
     * @param bonusCode     код вида бонуса
     * @param operationCode код операции
     * @return добавленный бонус
     * @throws PeopleException
     */
    public PeopleBonus addBonus(Integer peopleMainID, Integer bonusCode, Integer operationCode) throws PeopleException;

    /**
     * Добавляет бонус для человека
     *
     * @param peopleMainId        кому начисляем бонус
     * @param bonusCode           код вида бонуса
     * @param operarionCode       код операции
     * @param dateBonus           дата операции
     * @param amount              кол-во бонусов
     * @param relatedCreditId     id связанного кредита
     * @param relatedPeopleMainId id связанного человека
     * @return возвращает сохраненный бонус или null
     * @throws PeopleException
     */
    public PeopleBonus addBonus(int peopleMainId, int bonusCode, int operarionCode,
                                Date dateBonus, Double amount, Integer relatedCreditId,
                                Integer relatedPeopleMainId) throws PeopleException;

    /**
     * Метод удаляет бонус, если записей с таким кодом несколько удаляется 1 запись
     *
     * @param peopleMainID человек
     * @param bonusCode    код бонуса который надо удалить
     */
    public void removeBonus(Integer peopleMainID, Integer bonusCode);

    /**
     * Метод находит все списания бонусов при платеже по кредиту
     * @param credit_id
     * @param paydate
     * @return
     */
    public List<PeopleBonusEntity> findCreditPayBonus(Integer credit_id, Date paydate);

    /**
     * Сохраняет данные о приглашенном друге
     *
     * @param peopleMainId - кому начисляем бонус
     * @param name         - имя
     * @param surname      - фамилия
     * @param email        - email
     * @return false, если такой уже есть, в противном случае true
     * @throws KassaException
     */
    public boolean saveFriend(Integer peopleMainId, String name, String surname,
                              String email, String phone, Boolean available, Integer forBonus) throws KassaException;

    /**
     * добавляем новые ПД человека
     *
     * @param people          - ПД человека
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param surname         - фамилия
     * @param name            - имя
     * @param midname         - отчество
     * @param maidenname      - девичья фамилия
     * @param birthdate       - дата рождения
     * @param gender          - пол
     * @param databeg         - дата начала записи
     * @param active          - активность
     * @throws PeopleException
     */
    public PeoplePersonalEntity newPeoplePersonal(PeoplePersonalEntity people, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                                  String surname, String name, String midname, String maidenname, Date birthdate, String birthplace,
                                                  Integer gender, Date databeg, Integer active);

    /**
     * добавляем ПД человека
     *
     * @param people       - ПД
     * @param peopleMainId - id человека
     * @param date         - дата
     * @return
     * @throws PeopleException
     */
    public PeoplePersonal addPeoplePersonal(PeoplePersonal people, Integer peopleMainId, Date date) throws PeopleException;

    /**
     * инициализируем ПД
     *
     * @param peopleMain - заголовок для человека
     * @return
     */
    public PeoplePersonal initPeoplePersonal(PeopleMain peopleMain);

    /**
     * добавляем новый паспорт человека
     *
     * @param passport        - документ человека
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param series          - серия
     * @param number          - номер
     * @param docdate         - дата выдачи
     * @param docorgcode      - код подр.
     * @param docorg          - организация
     * @param active          - активность
     * @throws PeopleException
     */
    public DocumentEntity newDocument(DocumentEntity passport, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                      String series, String number, Date docdate, String docorgcode, String docorg, Integer active);

    /**
     * добавляем новый другой документ человека
     *
     * @param document     - документ человека
     * @param peopleMainId - id человека
     * @param partnerId    - id партнера
     * @param number       - номер
     * @param docdate      - дата выдачи
     * @param active       - активность
     * @throws PeopleException
     */
    public DocumentOtherEntity newOtherDocument(DocumentOtherEntity document, ReferenceEntity type, Integer peopleMainId, Integer partnerId,
                                                String number, Date docdate, Integer active);


    /**
     * новый подписанный документ
     *
     * @param document        - документ
     * @param peopleMainId    - id человека
     * @param docNumber       - номер документа
     * @param docDate         - дата документа
     * @param smsCode         - смс-код
     * @param docText         - текст документа
     * @param active          - активность
     * @param anotherId       - к чему относится
     * @param typeId          - вид документа
     * @param creditRequestId - id заявки
     * @param creditId        - id кредита
     * @return
     */
    public OfficialDocumentsEntity newOfficialDocument(OfficialDocumentsEntity document, Integer peopleMainId,
                                                       Integer creditRequestId, Integer creditId, String docNumber, Date docDate, String smsCode,
                                                       String docText, Integer active, Integer anotherId, Integer typeId);

    /**
     * меняем данные подписанного документа
     *
     * @param document        - документ
     * @param peopleId        - id человека
     * @param creditRequestId - id заявки
     * @param creditId        - id кредита
     */
    public void changeOfficialDocument(OfficialDocuments document, Integer peopleId,
                                       Integer creditRequestId, Integer creditId, Date dateChange);

    /**
     * инициализируем документ
     *
     * @param peopleMain      - заголовок для человека
     * @param typeId          - вид документа
     * @param creditRequestId - id заявки
     * @param creditId        - id кредита
     * @return
     */
    public OfficialDocuments initOfficialDocument(PeopleMain peopleMain, Integer creditRequestId,
                                                  Integer creditId, Integer typeId);

    /**
     * добавляем документ
     *
     * @param passport     - данные документа
     * @param peopleMainId - id человека
     * @return
     * @throws PeopleException
     */
    public Documents addDocument(Documents passport, Integer peopleMainId) throws PeopleException;

    /**
     * инициализируем документ
     *
     * @param peopleMain - заголовок для человека
     * @return
     */
    public Documents initDocument(PeopleMain peopleMain);

    /**
     * добавляем новый адрес текстом
     * этот метод можно считать устаревшим так как не имеет параметра isSame, предлается использовать метод
     * {@link #newAddressFias(AddressEntity, Integer, Integer, Integer, Integer, String, Date, Date, String, Integer, String, String, String, String, String, Integer)}
     * новый метод идентичный алгоритм за одним изменением - указывает параметр isSame,
     * текущий оставлен для совместимости других проектов
     *
     * @param addr            - данные адреса
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param addressTypeId   - вид адреса
     * @param addressText     - адрес текстом
     * @param databeg         - дата начала записи
     * @param country         - страна
     * @param active          - активность
     * @param fiasGuid        - guid из фиас для конечной точки адреса
     * @param house           - дом
     * @param corpus          - корпус
     * @param building        - строение
     * @param flat            - квартира
     * @throws PeopleException
     */
    public AddressEntity newAddressFias(AddressEntity addr, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                        Integer addressTypeId, String addressText, Date databeg, Date dataend, String country,
                                        Integer active, String fiasGuid, String house, String corpus, String building, String flat);

    /**
     * добавляем новый адрес текстом
     *
     * @param addr            данные адреса
     * @param peopleMainId    id человека
     * @param creditRequestId id заявки
     * @param partnerId       id партнера
     * @param addressTypeId   вид адреса
     * @param addressText     адрес текстом
     * @param databeg         дата начала записи
     * @param dataend         дата окончания записи
     * @param country         страна
     * @param active          активность
     * @param fiasGuid        guid из фиас для конечной точки адреса
     * @param house           дом
     * @param corpus          корпус
     * @param building        строение
     * @param flat            квартира
     * @param isSame          идентичны ли адреса проживания и прописки
     * @throws PeopleException
     */
    AddressEntity newAddressFias(AddressEntity addr, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                        Integer addressTypeId, String addressText, Date databeg, Date dataend, String country,
                                        Integer active, String fiasGuid, String house, String corpus, String building, String flat,
                                        Integer isSame);

    /**
     * Создание адреса, полученного от сервиса
     * Портал Открытые данные Электронного правительства Республики Казахстан
     * http://data.egov.kz
     *
     * @param currentAddress        данные адреса
     * @param peopleMainId          id человека
     * @param creditRequestId       id заявки
     * @param partnerId             id партнера
     * @param addressTypeId         вид адреса
     * @param addressText           адрес текстом
     * @param databeg               дата начала записи
     * @param dataend               дата окончания записи
     * @param active                активность
     * @param kzegovdataCato        код Кдассификатор административно-территориальных объектов Республики Казахстан
     * @param kzegovdataAts         ID Ats (Административно-территориальное устройство Республики Казахстан) конечной части адреса
     * @param kzegovdataGeonims     ID Geonims (Устройство населенных пунктов Республики Казахстан) конечной части адреса
     * @param addressTextToStreet   адрес текстом до уровня улицы
     * @param streetText            улица
     * @param house                 дом
     * @param corpus                корпус
     * @param building              строение
     * @param flat                  квартира
     * @param isSame                идентичны ли адреса проживания и прописки
     * @throws PeopleException
     */
    AddressEntity newAddressKzEgovData(
            AddressEntity currentAddress,
            Integer peopleMainId,
            Integer creditRequestId,
            Integer partnerId,
            Integer addressTypeId,
            String addressText,
            Date databeg,
            Date dataend,
            Integer active,
            String kzegovdataCato,
            Long kzegovdataAts,
            Long kzegovdataGeonims,
            String addressTextToStreet,
            String streetText,
            String house,
            String corpus,
            String building,
            String flat,
            Integer isSame
    );


    /**
     * добавляем адрес в формате фиас
     *
     * @param address      - адресные данные
     * @param peopleMainId - id человека
     * @param fiasGuid     - гуид из ФИАС
     * @param date         - дата изменений
     * @return
     * @throws PeopleException
     */
    public FiasAddress addAddressFias(FiasAddress address, Integer peopleMainId, String fiasGuid, Date date) throws PeopleException;

    /**
     * инициализируем адрес
     *
     * @param peopleMain    - человек
     * @param addressTypeId - вид адреса
     * @return
     */
    public FiasAddress initAddress(PeopleMain peopleMain, Integer addressTypeId);

    /**
     * сохраняем доп.данные по человеку
     *
     * @param peopleMisc      - доп.данные
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param marriageTypeId  - id семейного положения
     * @param children        - кол-во детей
     * @param underage        - несовершеннолетних детей
     * @param realtyDate      - дата регистрации
     * @param regDate         - дата начала проживания
     * @param groundTypeId    - id отношения к собственности на жилье
     * @param car             - есть ли авто
     * @param databeg         - дата начала изменений
     * @param active          - активность записи
     * @param driverLicense   - водительское удостоверение
     * @param regDateId       - значение из справочника диапазон даты прописки
     * @param realtyDateId    - значение из справочника диапазон даты начала пребывания
     * @return
     */
    public PeopleMiscEntity newPeopleMisc(PeopleMiscEntity peopleMisc, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                          Integer marriageTypeId, Integer children, Integer underage, Integer realtyDateId, Date realtyDate, Integer regDateId,
                                          Date regDate, Integer groundTypeId, boolean car, Date databeg,
                                          Integer active, boolean driverLicense);


    /**
     * Сохраняем другие доп. данные по человеку
     *
     * @param peopleOther          - доп данные
     * @param peopleMainId         id человека
     * @param public_role          публичная должность
     * @param public_relative_role публичная должность родственника
     * @param public_relative_fio  фио родственника
     * @param benific_fio          фио бенифициарного владельца
     * @param reclam_accept        согласен на спам
     * @param other                действует в пользу другого
     * @return
     */
    public PeopleOtherEntity newPeopleOther(PeopleOtherEntity peopleOther, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                            String public_role, String public_relative_role, String public_relative_fio, String benific_fio,
                                            boolean reclam_accept, boolean other, Integer active);

    /**
     * добавляем доп.данные человека
     *
     * @param misc         - доп.данные
     * @param peopleMainId - id человека
     * @param date         - дата
     * @return
     * @throws PeopleException
     */
    public PeopleMisc addPeopleMisc(PeopleMisc misc, Integer peopleMainId, Date date) throws PeopleException;

    /**
     * добавляем другие доп.данные человека
     *
     * @param other        - доп.данные
     * @param peopleMainId - id человека
     * @param date         - дата
     * @return
     * @throws PeopleException
     */
    public PeopleOther addPeopleOther(PeopleOther other, Integer peopleMainId, Date date) throws PeopleException;

    /**
     * инициализируем доп.данные
     *
     * @param peopleMain - заголовок для человека
     * @return
     */
    public PeopleMisc initPeopleMisc(PeopleMain peopleMain);

    /**
     * сохраняем занятость человека
     *
     * @param employ          - данные занятости
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param placeWork       - место работы
     * @param occupation      - должность
     * @param educationId     - образование
     * @param typeWorkId      - вид деятельности
     * @param durationId      - частота получения зарплаты
     * @param professionId    - профессия
     * @param extSalaryId     - вид доп.дохода
     * @param salary          - зарплата
     * @param extSalary       - доп.доход
     * @param experience      - с какого времени работает
     * @param dateStartWork   - дата начала работы на текущем месте
     * @param current         - текущая работа или прошлая
     * @param databeg         - дата начала
     * @param extCreditSum    - сумма доп.кредитных обязательств
     * @param nextSalaryDate  - дата следующей зарплаты
     * @param occupationId    - занятость по справочнику
     * @return
     */
    public EmploymentEntity newEmployment(EmploymentEntity employ, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                          String placeWork, String occupation, Integer educationId, Integer typeWorkId, Integer durationId, Integer professionId,
                                          Integer extSalaryId, Double salary, Double extSalary, Date experience, Integer dateStartWorkId, Date dateStartWork, Integer current, Date databeg,
                                          Double extCreditSum, Date nextSalaryDate, Integer occupationId);

    /**
     * добавляем занятость
     *
     * @param employ       - данные занятости
     * @param peopleMainId - id человека
     * @param date         - дата
     * @return
     */
    public Employment addEmployment(Employment employ, Integer peopleMainId, Date date) throws PeopleException;

    /**
     * инициализируем занятость
     *
     * @param peopleMain - заголовок человека
     * @return
     */
    public Employment initEmployment(PeopleMain peopleMain);

    /**
     * добавляем недееспособность человека
     *
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @param incapacityDate  - дата недееспособности
     * @param incapacityId    - код недееспособности
     * @throws PeopleException
     */
    public void newPeopleIncapacity(Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                    Date incapacityDate, Integer incapacityId) throws PeopleException;

    /**
     * ищем приглашенного друга по номеру телефона
     *
     * @param phone - телефон друга
     * @return приглашенный друг
     */
    PeopleFriend findCalledFriendByPhone(String phone);

    /**
     * ищем приглашенного друга
     *
     * @param friendEmail - email друга
     * @return
     */
    public PeopleFriend findCalledFriend(String friendEmail);

    /**
     * ищем недееспособность
     *
     * @param peopleMainId    - id человека
     * @param creditRequestId - id заявки
     * @param partnerId       - id партнера
     * @return
     */
    public List<PeopleIncapacityEntity> findPeopleIncapacity(Integer peopleMainId, Integer creditRequestId, Integer partnerId);

    /**
     * Сохраняем параметры
     *
     * @param peopleMainId - id человека
     * @param bonuspay     - платим ли бонусами
     */
    public void saveUserBonusProperties(Integer peopleMainId, Integer bonuspay);

    /**
     * сохраняем информацию из БД телефонов
     *
     * @param peopleContact контакт человека
     * @throws PeopleException
     */
    public PeopleContactEntity savePhoneInfo(PeopleContactEntity peopleContact) throws PeopleException;

    /**
     * ищем настройки для пользователя
     *
     * @param peopleMainId - id человека
     * @return
     */
    public UserPropertiesEntity findUserProperties(Integer peopleMainId);

    /**
     * ищем в БД террористов
     *
     * @param people - ПД
     * @return
     * @throws PeopleException
     */
    public BlacklistEntity saveTerrorist(PeoplePersonalEntity people) throws PeopleException;

    /**
     * сохраняем заголовок человека
     *
     * @param inn   - инн
     * @param snils - снилс
     * @return
     * @throws PeopleException
     */
    public PeopleMainEntity savePeopleMain(PeopleMainEntity peopleMain, String inn, String snils) throws PeopleException;

    /**
     * возвращает, есть ли такой номер телефона (email) у определенного клиента
     *
     * @param peopleMainId - id человека
     * @param value        - значение контакта
     * @return
     */
    public boolean phoneClientExists(Integer peopleMainId, String value);

    /**
     * возвращает, есть ли такой номер телефона (email) у определенного клиента от любого партнера
     *
     * @param peopleMainId - id человека
     * @param value        - значение контакта
     * @return
     */
    public boolean phoneExists(Integer peopleMainId, String value);

    /**
     * сохраняем человека, оставившего заявку на обратный звонок
     *
     * @param surname     - фамилия
     * @param name        - имя
     * @param phone       - телефон
     * @param email       - email
     * @param dateRequest - дата запроса
     * @param type        - вид обратившегося
     * @throws PeopleException
     */
    CallBackEntity newCallBack(String surname, String name, String phone, String email,
                                      Date dateRequest, String message, Integer type) throws PeopleException;

    /**
     * добавляем данные на обратный звонок
     *
     * @param callBack - данные человека на обратный звонок
     * @return
     * @throws PeopleException
     */
    CallBack addCallBack(CallBack callBack) throws PeopleException;

    /**
     * сохраняем данные по обратному звонку, когда его взял конкретный пользователь
     *
     * @param callBack - данные по обратному звонку
     * @param userId   - id пользователя
     * @return
     */
    public CallBack saveCallBackWithUser(CallBack callBack, Integer userId);

    /**
     * список обратных звонков
     *
     * @param nFirst      -
     * @param nCount
     * @param sorting
     * @param surname     - фамилия
     * @param userName    - пользователь
     * @param phone       - телефон
     * @param email       - email
     * @param dateRequest - дата запроса
     * @param dateCall    - дата взятия в работу
     * @param isActive    - статус активности
     * @return
     */
    public List<CallBack> listCallBack(int nFirst, int nCount, SortCriteria[] sorting, String surname,
                                       String userName, String phone, String email, DateRange dateRequest, DateRange dateCall,
                                       Integer isActive, Integer type);

    /**
     * перегруженый список обратных звонков
     *
     * @param surname     - фамилия
     * @param name        - имя
     * @param midname     - отчество
     * @param dateRequest - дата запроса
     * @param dateCall    - дата взятия в работу
     * @param isActive    - статус активности
     * @return
     */
    public List<CallBack> listCallBack(int nFirst, int nCount, String surname, String name,
                                       String midname, DateRange dateRequest, DateRange dateCall,
                                       Integer isActive, Set options);

    /**
     * сколько обратных звонков
     *
     * @param surname     - фамилия
     * @param userName    - пользователь
     * @param phone       - телефон
     * @param email       - email
     * @param dateRequest - дата запроса
     * @param dateCall    - дата взятия в работу
     * @param isActive    - статус активности
     * @return
     */
    public int countCallBack(String surname, String userName, String phone, String email,
                             DateRange dateRequest, DateRange dateCall, Integer isActive, Integer type);

    /**
     * перегруженый метод сколько обратных звонков
     *
     * @param surname     - фамилия
     * @param userName    - пользователь
     * @param name        - имя
     * @param midname     - отчество
     * @param dateRequest - дата запроса
     * @param dateCall    - дата взятия в работу
     * @param isActive    - статус активности
     * @param options     - параметры инициализации
     * @return
     */
    public int countCallBack(String surname, String name, String midname,
                             DateRange dateRequest, DateRange dateCall, Integer isActive, Set options);

    /**
     * Метод изменяет статус коллбэку
     *
     * @param callBack     колбэк
     * @param activeStatus статус
     */
    public void changeCallBackStatus(CallBack callBack, Integer activeStatus);

    /**
     * Обновить данные друга
     *
     * @param friendId  id друга
     * @param name      имя
     * @param surname   фамилия
     * @param email     почта
     * @param phone     телефон
     * @param available доступен ли по телефону
     * @return если удалось то true, если нет то false
     */
    boolean updateFriend(Integer friendId, String name, String surname, String email,
                         String phone, Boolean available);

    /**
     * сохраняем данные для адреса, если их передали текстом и по полям
     *
     * @param address    - адрес
     * @param regionCode - код региона
     * @param regionName - регион название
     * @param areaName   - сельский район название
     * @param areaExt    - сельский района вид
     * @param cityName   - город название
     * @param cityExt    - город вид
     * @param placeName  - сельское поселение название
     * @param placeExt   - сельское поселение вид
     * @param streetName - улица название
     * @param streetExt  - улица вид
     * @return
     */
    public AddressEntity saveAddressDataWithStrings(AddressEntity address, String regionCode, String regionName, String areaName, String areaExt,
                                                    String cityName, String cityExt, String placeName, String placeExt, String streetName, String streetExt);

     /**
     * ищет последний архивный адрес клиента
     *
     * @param peopleMainId - id человека
     * @param addrType     - вид адреса
     * @return
     */
    AddressEntity findLastArchiveAddress(Integer peopleMainId, Integer addrType);

    /**
     * ищет последнюю архивную занятость клиента
     *
     * @param peopleMainId - id человека
     * @return
     */
    EmploymentEntity findLastArchiveEmployment(Integer peopleMainId);

    /**
     * ищет последний архивный документ клиента
     *
     * @param peopleMainId - id человека
     * @return
     */
    DocumentEntity findLastArchivePassport(Integer peopleMainId);

    /**
     * ищет последний архивный контакт клиента
     *
     * @param peopleMainId - id человека
     * @param contactType  - вид контакта
     * @return
     */
    PeopleContactEntity findLastArchiveContact(Integer peopleMainId, Integer contactType);

    /**
     * ищет последние архивные ПД клиента
     *
     * @param peopleMainId - id человека
     * @return
     */
    PeoplePersonalEntity findLastArchivePersonalData(Integer peopleMainId);
    /**
     * ищем вид контакта для сохранения логина
     * @return
     */
    Integer findContactTypeForLogin();

    /**
     * Найти человека по его телефону
     * @param phone номер телефона
     * @return сущность или null
     */
    PeopleMainEntity findPeopleByPhone(String phone);

    /**
     * Метод сохраняет человека c ИИН, если человек = null, то будет создан новый
     *
     * @param peopleMain человек, если null будет создан новый
     * @param iin        ИИН
     * @return сохраненый человек
     */
    PeopleMain savePeopleIin(PeopleMainEntity peopleMain, String iin);

    /**
     * Метод достает персональные данные по eventcode и роли
     *
     * @param eventCode код
     * @param roleID    ID роли
     * @return список персональных данных
     */
    List<PeoplePersonal> getPeoplePersonalByRoleIDAndEventCodeID(Integer eventCode, Integer roleID);
}
