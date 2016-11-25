package ru.simplgroupp.interfaces;

import ru.simplgroupp.contact.protocol.v2.response.unpacked.RowUnp;
import ru.simplgroupp.exception.ReferenceException;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.*;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface ReferenceBooksLocal {

    /**
     * возвращает список активных значений из справочника в зависимости от id
     *
     * @param refHeaderId
     */
    public List<Reference> listReference(int refHeaderId);

    /**
     * возвращает список всех из справочника в зависимости от id
     *
     * @param refHeaderId
     */
    public List<Reference> listReferenceAll(int refHeaderId);

    /**
     * возвращает список документов, удостоверяющих личность (id=14 в ref_header)
     */
    public List<Reference> getDocverTypes();

    /**
     * возвращает список документов, подтверждающих пенсию (id=86 в ref_header)
     */
    public List<Reference> getPensDocTypes();

    /**
     * Возвращает тип документа по идентификатору
     */
    public Reference getPensDocType(Integer codeInteger);

    /**
     * возвращает список типов контактов (например, мобильный телефон, и др.) (id=16 в ref_header)
     */
    public List<Reference> getContactTypes();

    /**
     * возвращает список состояний кредитной заявки
     */
    public List<CreditStatus> getCreditRequestStatuses();

    /**
     * возвращает список стран (необходим для гражданства)
     */
    public List<Country> getCountriesList();

    /**
     * возвращает список видов семейного положения (id=5 в ref_header)
     */
    public List<Reference> getMarriageTypes();

    /**
     * возвращает список видов образования (id=9 в ref_header)
     */
    public List<Reference> getEducationTypes();

    /**
     * возвращает список типов занятости (id=6 в ref_header)
     */
    public List<Reference> getEmployTypes();

    /**
     * возвращает список типов профессии (id=7 в ref_header)
     */
    public List<Reference> getProfessionTypes();

    /**
     * возвращает список отношения к недвижимости (id=10 в ref_header)
     */
    public List<Reference> getRealtyTypes();

    /**
     * возвращает показатели поведения пользователя GA (id=61 в ref_header)
     */
    public List<Reference> getBehaviorGATypes();

    /**
     * возвращает запись для определенного показателя поведения пользователя GA (code)
     */
    public Reference getBehaviorGAType(String code);

    /**
     * возвращает список видов деятельности предприятия (id=11 в ref_header)
     */
    public List<Reference> getOrganizationTypes();

    /**
     * возвращает список видов деятельности предприятия (id=11 в ref_header)
     */
    public Reference getOrganizationType(int codeInteger);

    /**
     * возвращает список валют (id=3 в ref_header)
     */
    public List<Reference> getCurrencyTypes();

    /**
     * возвращает справочник типов состояния кредита (id=13 в ref_header)
     */
    public List<Reference> getCreditStatusTypes();

    /**
     * возвращает список видов счета (id=17 в ref_header)
     */
    public List<Reference> getAccountTypes();

    /**
     * возвращает вид адреса (id=18 в ref_header)
     */
    public List<Reference> getAddressTypes();

    /**
     * возвращает список причин отказа в кредите (id=15 в ref_header)
     */
    public List<Reference> getRefusalReasonTypes();

    /**
     * возвращает список типов кредита (id=2 в ref_header)
     */
    public List<Reference> getCreditTypes();

    /**
     * возвращает список типов платежа - кто кому платит (id=36 в ref_header)
     */
    public List<Reference> getPaymentTypes();

    /**
     * возвращает вид типа платежа - кто кому платит - по коду
     */
    public Reference getPaymentType(int codeInteger);

    /**
     * возвращает запись для конкретного статуса заявки (id)
     */
    public CreditStatusEntity getCreditRequestStatus(int id);

    /**
     * возвращает запись для конкретного вида контакта (codeInteger)
     */
    public Reference getContactType(int codeInteger);

    /**
     * возвращает список партнеров
     */
    public List<Partner> getPartners();

    /**
     * сохраняем данные партнера
     */
    public void savePartner(Partner partner);

    /**
     * возвращает запись для конкретного партнера
     */
    public Partner getPartner(int id);

    /**
     * возвращает список полов (id=20 в ref_header)
     */
    public List<Reference> getGenders();

    /**
     * возвращает запись для конкретной страны (code2letter)
     */
    public Country getCountry(String code2letter);

    /**
     * возвращает запись для определенного пола (codeInteger)
     */
    public Reference getGender(int codeInteger);

    /**
     * возвращает запись для определенного вида адреса (codeInteger)
     */
    public Reference getAddressType(int codeInteger);

    /**
     * возвращает запись для определенного вида документа (codeInteger)
     */
    public Reference getDocverType(int codeInteger);

    /**
     * возвращает список регионов, справочник нужен исключительно для работы с КБ, не используется в адресных данных
     */
    public List<FMSRegion> getRegionsList();

    /**
     * возвращает список регионов для заявки
     */
    public List<FMSRegion> getRegionsNewList();

    /**
     * добавляем новый регион
     *
     * @param code    - код октмо
     * @param name    - название
     * @param codeReg - код автомобильный
     */
    public void addRegionNew(String code, String name, String codeReg, String codeIso);

    /**
     * возвращает регион по коду
     *
     * @param code - код региона по октмо
     * @return
     */
    public RegionsEntity findRegion(String code);

    /**
     * возвращает запись для определенного региона (codeReg)
     */
    public FMSRegion getRegion(String codeReg);

    /**
     * ищем запись в любом справочнике (refHeaderId) по числовому коду (codeInteger)
     */
    public Reference findByCodeInteger(Integer refHeaderId, Integer codeInteger);

    /**
     * ищем запись в любом справочнике (refHeaderId) по символьному коду (code)
     */
    public Reference findByCode(Integer refHeaderId, String code);

    /**
     * возвращает запись для определенного вида занятости (codeInteger)
     */
    public Reference getEmployType(int codeInteger);

    /**
     * возвращает запись для определенного вида образования (codeInteger)
     */
    public Reference getEducationType(int codeInteger);

    /**
     * возвращает запись для определенного вида семейного положения (codeInteger)
     */
    public Reference getMarriageType(int codeInteger);

    /**
     * возвращает запись для определенного типа отношения к недвижимости (codeInteger)
     */
    public Reference getRealtyType(int codeInteger);

    /**
     * возвращает список частоты получения доходов (id=4 в ref_header)
     */
    public List<Reference> getIncomeFreqTypes();

    /**
     * возвращает запись для определенного типа частоты получения доходов (codeInteger)
     */
    public Reference getIncomeFreqType(int codeInteger);

    /**
     * возвращает запись конкретного типа счета (codeInteger)
     */
    public Reference getAccountType(int codeInteger);

    /**
     * возвращает codeinteger, соответствующий id
     */
    public Integer getCodeIntegerById(int id);

    /**
     * возвращает code, соответствующий id
     */
    public String getCodeById(int id);

    /**
     * возвращает запись конкретного вида валюты по символьному коду (code)
     */
    public Reference getCurrency(String code);

    /**
     * возвращает запись конкретного вида валюты по числовому коду (codeInteger)
     */
    public Reference getCurrency(int codeInteger);

    /**
     * возвращает запись конкретного типа кредита по символьному коду (code)
     */
    public Reference getCreditType(String code);

    /**
     * возвращает запись конкретного типа кредита по числовому коду (codeInteger)
     */
    public Reference getCreditType(int codeInteger);

    /**
     * возвращает запись конкретной скоринговой модели
     *
     * @param id - id модели
     * @return
     */
    public ScoreModelEntity getScoreModel(int id);

    /**
     * возвращает запись конкретной скоринговой модели по ее коду
     *
     * @param partnersId - id партнера
     * @param code       - код модели
     */
    public ScoreModel getModelByCode(PartnersEntity partnersId, String code);

    /**
     * возвращает запись конкретной причины отказа (codeInteger)
     */
    public Reference getRefusalReason(int codeInteger);

    /**
     * возвращает список банков
     */
    public List<Bank> getBanksList();

    /**
     * Обновляет справочник банков
     */
    public void updateBanks(List<Bank> b);

    /**
     * возвращает конкретный банк по имени
     */
    public Bank getBankByName(String name);

    /**
     * возвращает конкретный банк (bik)
     */
    public BankEntity getBank(String bik);

    /**
     * Ищет банки с бик, начинающимся с @bikPattern
     *
     * @param bikPattern начало БИК
     * @param limit      кол-во возвращаемых банков
     * @return список банков
     */
    public List<BankEntity> findBankByBik(String bikPattern, int limit);

    /**
     * возвращает запись конкретного статуса кредита по числовому коду (codeInteger)
     */
    public Reference getCreditStatusType(int codeInteger);

    /**
     * возвращает запись конкретной профессии по числовому коду (codeInteger)
     */
    public Reference getProfessionType(int codeInteger);

    /**
     * возвращает запись для определенного региона (codeReg)
     */
    public RegionsEntity getRegions(String codeReg);

    /**
     * возвращает запись конкретного типа несоответствия по символьному коду (code)
     */
    public Reference getMismatchType(String code);


    /**
     * возвращает конкретный справочник по id
     */
    public Reference getReference(int id);

    /**
     * возвращает конкретный вид партнера
     */
    public Reference getSpouseType(int codeInteger);

    /**
     * возвращает список видов партнера
     */
    public List<Reference> getSpouseTypes();

    /**
     * возвращает запись из таблицы partner
     */
    public PartnersEntity getPartnerEntity(int id);

    /**
     * возвращает конкретную страну из справочника
     */
    public CountryEntity getCountryEntity(String code2letter);

    /**
     * возвращает конкретный справочник по id
     */
    public ReferenceEntity getReferenceEntity(int id);

    /**
     * возвращает список видов документов
     */
    public List<Reference> getDocumentTypes();

    /**
     * возвращает вид документа по коду
     */
    public Reference getDocumentType(int codeInteger);

    /**
     * возвращает список видов дополнительного дохода
     */
    public List<Reference> getExtSalaryTypes();

    /**
     * возвращает вид дополнительного дохода по коду
     */
    public Reference getExtSalaryType(int codeInteger);

    /**
     * возвращает список видов причин недействительности документа
     */
    public List<Reference> getReasonEndTypes();

    /**
     * возвращает вид причины недействительности документа по коду
     */
    public Reference getReasonEndType(int codeInteger);

    /**
     * возвращает список федералов
     */
    public List<Reference> getAuthorityTypes();

    /**
     * возвращает федерала по коду
     */
    public Reference getAuthorityType(int codeInteger);

    /**
     * возвращает список видов кредита ОКБ
     */
    public List<Reference> getCreditOkbTypes();

    /**
     * возвращает вид кредита ОКБ по коду
     */
    public Reference getCreditOkbType(int codeInteger);

    /**
     * возвращает Entity по числовому коду
     */
    public ReferenceEntity findByCodeIntegerEntity(Integer refHeaderId, Integer codeInteger);

    /**
     * возвращает Entity по символьному коду
     */
    public ReferenceEntity findByCodeEntity(Integer refHeaderId, String code);

    /**
     * возвращает Entity причин отказа по коду
     */
    public ReferenceEntity getRefusalReasonEntity(int codeInteger);

    /**
     * возвращает список отношения к кредиту
     */
    public List<Reference> getCreditRelationTypes();

    /**
     * возвращает отношение к кредиту
     */
    public Reference getCreditRelationType(int codeInteger);

    /**
     * возвращает список частоты выплаты кредиту
     */
    public List<Reference> getCreditFreqPaymentTypes();

    /**
     * возвращает частоту выплаты кредита
     */
    public Reference getCreditFreqPaymentType(int codeInteger);

    /**
     * возвращает список атрибутов для суммарной части отчета из КО
     */
    public List<RefSummary> getRefSummaryList();

    /**
     * возвращает атрибут по коду
     */
    public RefSummary getRefSummaryItem(int id);

    /**
     * возвращает значение верификации или валидации из справочника
     */
    public VerificationMark getVerificationMarks(double mark, int kind);

    /**
     * ищем запись в справочнике по основному(refHeaderId) по числовому коду (codeInteger)
     * для передачи данных в КБ, если справочники не совпадают
     */
    public ReferenceEntity findByCodeIntegerMain(int refHeaderId, int codeInteger);

    /**
     * ищем запись в справочнике по основному(refHeaderId) по символьному коду (code)
     * для передачи данных в КБ, если справочники не совпадают
     */
    public ReferenceEntity findByCodeMain(int refHeaderId, String code);

    /**
     * ищем в справочнике по негативу запись по определенной статье
     */
    public RefNegative findByArticle(String article);

    /**
     * ищем в справочнике по маппингу для перевода значения КБ в наш формат
     */
    public ReferenceEntity findFromKb(int refHeaderId, int codeInteger) throws ReferenceException;

    /**
     * ищем в справочнике по маппингу для перевода значения КБ в наш формат
     */
    public ReferenceEntity findFromKbCode(int refHeaderId, String code) throws ReferenceException;

    /**
     * возвращаем партнера по значению в справочнике из таблицы partnerlinks
     */
    public PartnersEntity getPartnerFromLink(ReferenceEntity ref);

    /**
     * возвращаем партнера по имени
     */
    public Partner findPartnerByName(String name);

    /**
     * возвращает список видов сумм платежа
     */
    public List<Reference> getPaySumTypes();

    /**
     * возвращает вид суммы платежа
     */
    public Reference getPaySumType(int codeInteger);

    /**
     * возвращает список видов просрочки
     */
    public List<Reference> getCreditOverdueTypes();

    /**
     * возвращает вид просрочки
     */
    public Reference getCreditOverdueType(String code);

    /**
     * возвращает список целей кредита
     */
    public List<Reference> getCreditPurposeTypes();

    /**
     * возвращает список статусов платежа
     */
    public List<Reference> getPaymentStatusTypes();

    /**
     * возвращает статус платежа по коду
     */
    public Reference getPaymentStatusType(String code);

    /**
     * возвращает список партнеров для платежей
     */
    public List<Partner> getPartnersForPayment();

    /**
     * возвращает список партнеров для кредитных историй
     */
    public List<Partner> getPartnersForCreditHistory();

    /**
     * возвращает список партнеров для скоринга
     */
    public List<Partner> getPartnersForScoring();

    /**
     * возвращает справочник причин отказа
     */
    public List<RefuseReason> getRefuseReasons();

    /**
     * возвращает справочник причин отказа для кредитного менеджера
     */
    public List<RefuseReason> getRefuseReasonsForDecision();

    /**
     * Метод возвращает список справочника бонусов
     *
     * @return список бонусов
     */
    List<RefBonus> getRefBonus();

    /**
     * Метод сохранят список бонусов
     *
     * @param bonusList - список бонусов
     */
    void saveRefBonus(List<RefBonus> bonusList);

    /**
     * Метод удаляет бонус
     *
     * @param id - ID бонуса для удаления
     */
    void removeRefBonus(Integer id);

    /**
     * Метод создает бонус
     *
     * @param bonus бонус
     * @return созданый бонус
     */
    RefBonus addBonus(RefBonus bonus);

    /**
     * возвращает причину отказа по id
     */
    public RefuseReason getRefuseReason(int id);

    /**
     * возвращает причину отказа по id
     */
    public RefuseReasonEntity getRefuseReasonEntity(int id);

    /**
     * возвращает список причин отказа по статусу
     */
    public List<RefuseReason> getRefuseReasonByType(int reasonId);

    /**
     * возвращает список стадий для отказа в кредите
     */
    public List<Reference> getRefuseReasonStateTypes();

    /**
     * возвращает конкретную стадию для отказа в кредите
     */
    public Reference getRefuseReasonStateType(int codeInteger);

    /**
     * добавляем причину отказа
     *
     * @param type         - стадия отказа
     * @param name         - короткое название
     * @param nameFull     - полное название
     * @param constantName - имя константы для использования в модели
     * @param forDecision  - используется в интерфейсе менеджера для принятия решения
     */
    public void addRefuseReason(int type, String name, String nameFull,
                                String constantName, Integer forDecision);

    /**
     * добавляем партнера
     *
     * @param name     - имя в системе
     * @param realName - название для человека
     */
    public PartnersEntity addPartner(String name, String realName);

    /**
     * удаляем причину отказа
     */
    public void removeRefuseReason(int id);

    /**
     * список справочников
     *
     * @param partnerId - партнер
     * @param name      - название справочника
     * @param isInclude - включаем мы его в поиск или исключаем
     */
    public List<RefHeader> listRefHeaders(Integer partnerId, String name, Boolean isInclude);

    /**
     * кол-во справочников
     *
     * @param partnerId - партнер
     * @param name      - название
     * @param isInclude - включаем мы его в поиск или исключаем
     */
    public int countRefHeaders(Integer partnerId, String name, Boolean isInclude);

    /**
     * возвращает справочник по id
     */
    public RefHeaderEntity getRefHeaderEntity(int id);

    /**
     * возвращает справочник по id
     */
    public RefHeader getRefHeader(int id, Set options);

    /**
     * сохраняем справочник причин отказа списком
     */
    public void saveRefuseReasons(List<RefuseReason> lst);

    /**
     * добавляем значение в справочник системы
     *
     * @param refId - id справочника в refheader
     */
    public void addReferenceItem(int refId, String name, String code, Integer codeInteger);

    /**
     * удаляем значение из справочника
     */
    public void removeReferenceItem(int refId);

    /**
     * список значений справочника
     *
     * @param refHeader       - id справочника
     * @param name            - название
     * @param code            - код символьный
     * @param codeInteger     - код числовой
     * @param refHeaderMain   - id справочника системы (для маппинга)
     * @param codeMain        - код из основного справочника
     * @param codeIntegerMain - код числовой из основного справочника
     */
    public List<Reference> listReferences(Integer refHeader, String name, String code, Integer codeInteger, Integer refHeaderMain, String codeMain, Integer codeIntegerMain, Integer isActive);

    /**
     * кол-во значений справочника
     *
     * @param refHeader       - id справочника
     * @param name            - название
     * @param code            - код символьный
     * @param codeInteger     - код числовой
     * @param refHeaderMain   - id справочника системы (для маппинга)
     * @param codeMain        - код из основного справочника
     * @param codeIntegerMain - код числовой из основного справочника
     */
    public int countReferences(Integer refHeader, String name, String code, Integer codeInteger, Integer refHeaderMain, String codeMain, Integer codeIntegerMain, Integer isActive);

    /**
     * возвращает справочник по id
     */
    public Reference getReference(int id, Set options);

    /**
     * сохраняем стандартный справочник списком
     */
    public void saveReference(List<Reference> lst);

    /**
     * сохраняем справочник регионов списком
     */
    public void saveRegionsNew(List<FMSRegion> lst);

    /**
     * список пунктов Контакт
     * по городу
     */
    public List<ContactPoints> getContactPoints(String cityName);

    /**
     * список пунктов Контакт
     * по городу и региону
     */
    public List<ContactPoints> getContactPoints(String cityName, Integer regionId);

    /**
     * возвращает список пунктов Контакта по сервису
     *
     * @param serviceId
     */
    public List<ContactPoints> getContactPoints(int serviceId, String cityName, Integer regionId);

    /**
     * возвращает пункт Контакта по id
     */
    public ContactPoints getContactPoint(int id);

    /**
     * сохраняем справочник статуса заявки списком
     */
    public void saveCreditRequestStatus(List<CreditStatus> lst);

    /**
     * добавляем статус заявки
     */
    public void addCreditRequestStatus(String name);

    /**
     * добавляем точку приема контакта,где можно деньги положить
     */
    void addContactPoint(RowUnp input);

    /**
     * возвращает список причин попадания в черный список (id=56 в ref_header)
     */
    public List<Reference> getBlackListReasonTypes();

    /**
     * возвращает причину попадания в черный список (id=56 в ref_header)
     */
    public Reference getBlackListReasonType(int codeInteger);

    /**
     * возвращает список источников попадания в черный список (id=87 ref_header)
     */
    List<Reference> getBlackListSourceTypes();

    /**
     * возвращает источник попадания в черный список (id=87 ref_header)
     */
    List<Reference> getBlackListSourceType(int codeInteger);

    /**
     * возвращает список способов выполнения (id=69 ref_header)
     */
    List<Reference> getDecisionWayTypes();

    /**
     * возвращает способ выполнения (id=69 ref_header)
     */
    Reference getDecisionWayType(int codeInteger);

    /**
     * возвращает источник попадания в черный список (id=87 ref_header)
     * возвращает один объект, иначе null
     */
    Reference getBlackListOneSourceType(int codeInteger);

    /**
     * возвращает вид рассылок (id=70 ref_header)
     */
    Reference getMessageType(int codeInteger);

    /**
     * добавляем справочник возможных сервисов,которые может осуществлять тот или иной пункт Контакта
     */
    void addContactServ(RowUnp input);

    /**
     * добавляем связь между сервисом и пунктом, т.е какие операции конкретный пункт может осуществлять
     */
    void addContactBankServ(RowUnp input);

    /**
     * ищем сервис точки Контакта по id
     *
     * @param id
     */
    ContactServiceEntity getContactService(int id);

    /**
     * добавляем запись во внутренний справочник для черного списка
     */
    RefBlacklistEntity addRefBlacklist();

    /**
     * список людей из внутреннего черного списка
     *
     * @param surname           - фамилия
     * @param name              - имя
     * @param midname           - отчество
     * @param maidenname        - девичья фамилия
     * @param birthdate         - дата рождения
     * @param reason            - причина
     * @param series            - серия
     * @param number            - номер
     * @param mobilephone       - телефон
     * @param email             - email
     * @param sourceId          - источник информации
     * @param employerFullName  - работодатель, полное название
     * @param employerShortName - работодатель, краткое название
     * @param regionName        - название региона
     * @param areaName          - название сельской территории
     * @param cityName          - название города
     * @param placeName         - название села
     * @param streetName        - название улицы
     * @param house             - дом
     * @param corpus            - корпус
     * @param building          - строение
     * @param flat              - квартира
     * @return
     */
    List<RefBlacklistEntity> listRefBlacklist(int nFirst, int nCount, SortCriteria[] sorting, String surname, String name, String midname,
                                              String maidenname, Date birthdate, Integer reason, String series, String number,
                                              Integer isactive, String mobilephone, String email, Integer sourceId, String employerFullName,
                                              String employerShortName, String regionName, String areaName, String cityName, String placeName,
                                              String streetName, String house, String corpus, String building, String flat);

    /**
     * ищет человека во внутреннем черном списке
     *
     * @param surname           - фамилия
     * @param name              - имя
     * @param midname           - отчество
     * @param birthdate         - дата рождения
     * @param series            - серия
     * @param number            - номер
     * @param mobilephone       - телефон
     * @param email             - email
     * @param employerFullName  - работодатель, полное название
     * @param employerShortName - работодатель, краткое название
     * @param regionName        - название региона
     * @param areaName          - название сельской территории
     * @param cityName          - название города
     * @param placeName         - название села
     * @param streetName        - название улицы
     * @param house             - дом
     * @param corpus            - корпус
     * @param building          - строение
     * @param flat              - квартира
     */
    List<RefBlacklistEntity> findInRefBlacklist(String surname, String name, String midname, Date birthdate,
                                                String series, String number, String mobilephone, String email, String employerFullName,
                                                String employerShortName, String regionName, String areaName, String cityName, String placeName,
                                                String streetName, String house, String corpus, String building, String flat);

    /**
     * ищет человека во внутреннем черном списке по ПД
     *
     * @param surname   - фамилия
     * @param name      - имя
     * @param midname   - отчество
     * @param birthdate - дата рождения
     */
    List<RefBlacklistEntity> findInRefBlacklistByPersonalData(String surname, String name, String midname, Date birthdate);

    /**
     * ищет человека во внутреннем черном списке по паспорту
     *
     * @param series - серия
     * @param number - номер
     */
    List<RefBlacklistEntity> findInRefBlacklistByPassport(String series, String number);

    /**
     * ищет человека во внутреннем черном списке по телефону
     *
     * @param mobilephone - телефон
     */
    List<RefBlacklistEntity> findInRefBlacklistByPhone(String phone);

    /**
     * ищет человека во внутреннем черном списке по email
     *
     * @param email - email
     */
    List<RefBlacklistEntity> findInRefBlacklistByEmail(String email);

    /**
     * ищет человека во внутреннем черном списке по работодателю
     *
     * @param employerFullName  - работодатель, полное название
     * @param employerShortName - работодатель, краткое название
     */
    List<RefBlacklistEntity> findInRefBlacklistByEmployer(String employerFullName, String employerShortName);

    /**
     * ищет человека во внутреннем черном списке по адресу
     *
     * @param regionName - название региона
     * @param areaName   - название сельской территории
     * @param cityName   - название города
     * @param placeName  - название села
     * @param streetName - название улицы
     * @param house      - дом
     * @param corpus     - корпус
     * @param building   - строение
     * @param flat       - квартира
     */
    List<RefBlacklistEntity> findInRefBlacklistByAddress(String regionName, String areaName, String cityName, String placeName,
                                                         String streetName, String house, String corpus, String building, String flat);

    /**
     * количество людей из внутреннего черного списка
     *
     * @param surname           - фамилия
     * @param name              - имя
     * @param midname           - отчество
     * @param maidenname        - девичья фамилия
     * @param birthdate         - дата рождения
     * @param reason            - причина
     * @param series            - серия
     * @param number            - номер
     * @param mobilephone       - телефон
     * @param email             - email
     * @param sourceId          - источник информации
     * @param employerFullName  - работодатель, полное название
     * @param employerShortName - работодатель, краткое название
     * @param regionName        - название региона
     * @param areaName          - название сельской территории
     * @param cityName          - название города
     * @param placeName         - название села
     * @param streetName        - название улицы
     * @param house             - дом
     * @param corpus            - корпус
     * @param building          - строение
     * @param flat              - квартира
     * @return
     */
    int countRefBlacklist(String surname, String name, String midname, String maidenname, Date birthdate,
                          Integer reason, String series, String number, Integer isactive, String mobilephone,
                          String email, Integer sourceId, String employerFullName,
                          String employerShortName, String regionName, String areaName, String cityName, String placeName,
                          String streetName, String house, String corpus, String building, String flat);

    /**
     * ищем запись во внутреннем черном списке по id
     *
     * @param id  - id в черном списке
     * @param set - что инициализируем
     */
    RefBlacklistEntity getRefBlacklist(int id, Set options);

    /**
     * ищем запись во внутреннем черном списке по id
     *
     * @param id - id в черном списке
     */
    public RefBlacklistEntity getRefBlacklistEntity(int id);

    /**
     * удаляем из внутреннего черного списка
     */
    public void removeRefBlacklist(int id);

    /**
     * Массовое сохранение черного списка в бд
     *
     * @param entities список
     */
    void saveBulkRefBlacklist(List<RefBlacklistEntity> entities);

    /**
     * Сохранение одной записи в бд
     *
     * @param entity запись
     */
    void saveRefBlacklist(RefBlacklistEntity entity);

    /**
     * сохраняет трансферный объект черного списка
     *
     * @param blacklist трансферный объект
     */
    void saveRefBlacklist(RefBlacklist blacklist);

    /**
     * Метод ищет по блокам в черных списках
     *
     * @param entity запись по которой ищем
     * @return true если найден
     */
    Boolean findRefBlacklistByBlock(RefBlacklistEntity entity);

    /**
     * редактируем запись во внутреннем справочнике для черного списка
     *
     * @param id                - id записи
     * @param surname           - фамилия
     * @param name              - имя
     * @param midname           - отчество
     * @param birthdate         - дата рождения
     * @param databeg           - дата начала
     * @param dataend           - дата окончания
     * @param isactive          - активно
     * @param series            - серия
     * @param number            - номер
     * @param mobilephone       - телефон
     * @param reason            - причина
     * @param email             - email
     * @param source            - источник информации
     * @param comment           - комментарий
     * @param employerFullName  - работодатель, полное название
     * @param employerShortName - работодатель, краткое название
     * @param regionName        - название региона
     * @param areaName          - название сельской территории
     * @param cityName          - название города
     * @param placeName         - название села
     * @param streetName        - название улицы
     * @param house             - дом
     * @param corpus            - корпус
     * @param building          - строение
     * @param flat              - квартира
     */
    void saveRefBlacklist(Integer id, String surname, String name, String midname, String maidenname, Date birthdate,
                          Date databeg, Date dataend, Integer reason, String series, String number, Integer isactive,
                          String mobilephone, String email, Integer source, String comment, String employerFullName,
                          String employerShortName, String regionName, String areaName, String cityName, String placeName,
                          String streetName, String house, String corpus, String building, String flat);

    /**
     * возвращает список операций по счетам человека (id=57 в ref_header)
     */
    public List<Reference> getOperations();

    /**
     * возвращает операцию по счетам человека (id=57 в ref_header)
     */
    public Reference getOperation(int codeInteger);

    /**
     * возвращает список типов операций по счетам человека (id=58 в ref_header)
     */
    public List<Reference> getOperationTypes();

    /**
     * возвращает тип операции по счетам человека (id=58 в ref_header)
     */
    public Reference getOperationType(int codeInteger);

    /**
     * поставить оценку валидации исходя из данных
     *
     * @param verifyPersonal - верификация ПД
     * @param verifyDocument - верификация документа
     */
    public Integer getValidationMarkFromVerify(Integer verifyPersonal, Integer verifyDocument);

    /**
     * оценка
     *
     * @param id - оценка
     */
    public VerificationMarkEntity getValidationMark(Integer id);

    /**
     * удалить партнера
     *
     * @param id - партнер
     */
    public void removePartner(Integer id);

    /**
     * ищем регион по названию
     *
     * @param name - название
     * @return
     */
    public RegionsEntity findRegionByName(String name);

    /**
     * возвращает виды организаций для таблицы с организациями
     *
     * @return
     */
    public List<Reference> getOrganizationByTypes();

    /**
     * возвращает вид организации для таблицы с оргпнизациями
     *
     * @param codeInteger - код справочника
     * @return
     */
    public Reference getOrganizationByType(int codeInteger);

    /**
     * возвращает виды операции по кредиту
     *
     * @return
     */
    public List<Reference> getCreditDetailsOperations();

    /**
     * возвращает виды операции по кредиту
     *
     * @param codeInteger - код справочника
     * @return
     */
    public Reference getCreditDetailsOperation(int codeInteger);

    /**
     * возвращает виды бонусов
     *
     * @return
     */
    public List<RefBonus> getBonusTypeList();

    /**
     * возвращает вид бонуса
     *
     * @param code - код справочника
     * @return
     */
    public RefBonus getBonusByCodeInteger(Integer code);

    /**
     * обновляем вид бонуса
     */
    public void updateStatusBonusType(RefBonus ent);

    /**
     * возвращает вид бонуса
     *
     * @param id - id
     * @return
     */
    public RefBonusEntity getBonusType(int id);

    /**
     * возвращает id или null из справочника
     *
     * @param reference - справочник
     * @return
     */
    public Integer referenceIdOrNull(Reference reference);

    /**
     * возвращает числовой код или null из справочника
     *
     * @param reference - справочник
     * @return
     */
    public Integer referenceCodeIntegerOrNull(Reference reference);

    /**
     * возвращает символьный код или null из справочника
     *
     * @param reference - справочник
     * @return
     */
    public String referenceCodeOrNull(Reference reference);

    List<Reference> listReferenceTop(int refHeaderId);

    List<Reference> listReferenceSub(int refHeaderId, int parentId);

    List<Reference> getApplicationWays();

    List<Bank> getBanksList(String term, Integer page, Integer pageSize);

    /**
     * возвращает типы результатов звонка
     *
     * @return список результатов
     */
    List<Reference> getCallResultTypesList();

    /**
     * возвращает доступные временные диапазоны
     *
     * @return список диапазонов
     */
    List<Reference> getTimeRanges();

    /**
     * возвращает временной диапазон по его коду
     *
     * @param codeInteger код диапазона
     * @return объект диапазона
     */
    Reference getTimeRange(int codeInteger);

    /**
     * возвращает список сущностей ОКБ "Национальный Хантер"
     *
     * @return список сущностей
     */
    List<Reference> getHunterObjects();

    /**
     * возвращает сущность по текстовому коду
     *
     * @param code код
     * @return сущность или null
     */
    Reference getHunterObject(String code);

    /**
     * возвращает статусы мошенничества
     *
     * @return список статусов
     */
    List<Reference> getAntifraudStatuses();

    /**
     * возвращает статус мошенничества
     *
     * @param codeInteger цифровой код статуса
     * @return сущность или null
     */
    Reference getAntifraudStatus(int codeInteger);

    /**
     * Изменение статуса (isactive) в справочнике
     *
     * @param refHeaderID ID справочника
     * @param codeInteger код
     * @param status      статус
     */
    void changeReferenceStatus(Integer refHeaderID, Integer codeInteger, Integer status);

    /**
     * возвращает типы вопроса верификатора
     */
    List<Reference> getVerifierQuestionTypes();

    /**
     * возвращает тип вопроса верификатора
     *
     * @param codeInteger цифровой код
     */
    Reference getVerifierQuestionType(int codeInteger);

    /**
     * Метод удаляет записи из ЧС по коду источника информации
     *
     * @param sourceCode источник информации
     */
    void deleteBlackListBySourceCode(Integer sourceCode);
}

