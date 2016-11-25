package ru.simplgroupp.ejb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.w3c.dom.Document;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.exception.*;
import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.AddressBeanLocal;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ReportBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RefAntifraudRulesService;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.toolkit.common.GenUtils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.*;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)

public class KassaBean implements KassaBeanLocal {


    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    CreditRequestDAO crDAO;

    @EJB
    CreditDAO creditDAO;

    @EJB
    ProlongDAO prolongDAO;
    
    @EJB
    AccountDAO accDAO;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    PeopleBeanLocal peopleBean;

    @EJB
    CreditBeanLocal creditBean;

    @EJB
    DocumentOtherDAO documentOtherDAO;

    @EJB
    IFIASService fiasServ;

    @EJB
    UsersBeanLocal userBean;

    @EJB
    UsersDAO userDAO;

    @EJB
    MailBeanLocal mailBean;

    @EJB
    AddressBeanLocal addressBean;

    @EJB
    ReportBeanLocal reportBean;

    @EJB
    RulesBeanLocal rulesBean;

    @EJB
    WorkflowBeanLocal workflowBean;

    @EJB
    WorkflowEngineBeanLocal wfEngine;

    @EJB
    PaymentService paymentService;

    @EJB
    CreditCalculatorBeanLocal creditCalc;

    @EJB
    EventLogService eventLog;

    @EJB
    BizActionBeanLocal bizBean;

    @EJB
    ActionProcessorBeanLocal actProc;

    @EJB
    ServiceBeanLocal servBean;

    @EJB
    ProductDAO productDAO;

    @EJB
    PeopleDAO peopleDAO;

    @EJB
    ProductBeanLocal productBean;

    @EJB
    OfficialDocumentsDAO officialDocumentsDAO;

    @EJB
    RefAntifraudRulesService antifraudRulesService;
    
    @EJB
    SqlDAO sqlDAO;

    @EJB
    CollectorDAO collectorDao;
    
    @Inject Logger logger;

    private static HashMap<String, Object> ccRequestSortMapping = new HashMap<String, Object>(4);
    private static HashMap<String, Object> creditSortMapping = new HashMap<String, Object>(4);


    static {
        ccRequestSortMapping.put("DateContest", "c.datecontest");
        ccRequestSortMapping.put("Status", "c.statusId");
        ccRequestSortMapping.put("CreditSum", "c.creditsum");
        ccRequestSortMapping.put("CreditDays", "c.creditdays");

        creditSortMapping.put("CreditDataBeg", "c.creditdatabeg");
        creditSortMapping.put("CreditSum", "c.creditsum");

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeCreditRequest(CreditRequest ccRequest) throws KassaException {
        String sql = "delete from creditrequest where id=:id";
        Query qry = emMicro.createNativeQuery(sql);
        qry.setParameter("id", ccRequest.getEntity().getId());
        qry.executeUpdate();

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeCreditRequest(int id) throws KassaException {

        CreditRequestEntity ent = crDAO.getCreditRequestEntity(id);
        if (ent != null) {
            String sql = "delete from creditrequest where id=:id";
            Query qry = emMicro.createNativeQuery(sql);
            qry.setParameter("id", id);
            qry.executeUpdate();
        }

    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteCreditRequest(int creditRequestId) throws KassaException {

        CreditRequestEntity cRequest = crDAO.getCreditRequestEntity(creditRequestId);
        String processDefKey = ProcessKeys.DEF_CREDIT_REQUEST;
        if (cRequest != null && cRequest.getWayId() != null && cRequest.getWayId().getCodeinteger() == RefCreditRequestWay.DIRECT) {
            processDefKey = ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE;
        }
        String sql = "select count(*) from delete_crequest(:id)";
        Query qry = emMicro.createNativeQuery(sql);
        qry.setParameter("id", creditRequestId);
        Object i = qry.getSingleResult();

        try {
            wfEngine.removeProcessByBusinessKey(processDefKey, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId));

        } catch (WorkflowException e) {
            throw new KassaException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest createOffline(double creditSum, int creditDays, PeopleMain pmain, String productCode, Integer user_id) {
        CreditRequestEntity creditRequest = null;

        ProductsEntity product = productDAO.findProductByCode(productCode);
        //константы для расчета ставки
        Map<String, Object> limits = productBean.getNewRequestProductConfig(product.getId());
        //считаем ставку
        double stake = creditCalc.calcInitialStake(creditSum, creditDays, limits);

        //пишем данные заявки
        creditRequest = newCreditRequest(creditRequest, pmain.getId(),
                CreditStatus.IN_PROCESS, null, null, null, null, null, Partner.SYSTEM,
                product != null ? product.getId() : null, new Date(), null, new Date(), null, creditSum, creditDays,
                stake, null, null, null, null, null, RefCreditRequestWay.DIRECT, user_id);

        return crDAO.getCreditRequest(creditRequest.getId(), null);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Map<String, Object> saveStep2(Map<String, Object> params) throws KassaException {

        Map<String, Object> mp = new HashMap<String, Object>();

        mp.put("creditRequestId", null);
        mp.put("peopleMainId", null);

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));

        PeopleMainEntity pMain = ccRequest.getPeopleMainId();

        //ищем, нет ли такого документа в БД
        DocumentEntity existingPasport = peopleBean.findDocument(Documents.PASSPORT_RF, (String) params.get("seria"), (String) params.get("nomer"));
        if (existingPasport != null) {
            PeopleMainEntity peopleMain = existingPasport.getPeopleMainId();
            int cnt = findManWithPrivateCabinet(peopleMain);
            if (cnt > 0) {
                logger.severe("Человек с паспортом " + (String) params.get("seria") + " " + (String) params.get("nomer") + " уже существует в системе");
                throw new KassaException(ErrorKeys.DOCUMENT_EXISTS, "Человек с паспортом " + (String) params.get("seria") + " " + (String) params.get("nomer") + " уже существует в системе");
            }
        }

        //сохраняем документ
        DocumentEntity pasp = null;
        pasp = peopleBean.findPassportActive(pMain);
        try {
            peopleBean.newDocument(pasp, pMain.getId(), null, Partner.CLIENT, (String) params.get("seria"),
                    (String) params.get("nomer"), Convertor.toDate((String) params.get("whenIssued"), DatesUtils.FORMAT_ddMMYYYY),
                    (String) params.get("code_dep"), (String) params.get("whoIssued"), ActiveStatus.ACTIVE);
        } catch (Exception e2) {
            logger.severe("Не удалось сохранить паспорт, ошибка " + e2.getMessage());
        }

        //сохраняем веб аналитику

        Date todaydate = new Date();
        if ((StringUtils.isNotEmpty((String) params.get("source_from")))) {
            peopleBean.savePeopleBehavior(ccRequest.getId(), pMain.getId(), "REFER.FROM", params.get("source_from").toString(),
                    null, null, Partner.GOOGLEANALYTICS, todaydate);
        }

        if (Convertor.toLong(params.get("visit_count")) != null) {
            peopleBean.savePeopleBehavior(ccRequest.getId(), pMain.getId(), "HITS", null,
                    params.get("visit_count").toString(), null, Partner.GOOGLEANALYTICS, todaydate);
        }

        if (Convertor.toLong(params.get("ga_visitor_id")) != null) {
            peopleBean.savePeopleBehavior(ccRequest.getId(), pMain.getId(), "GA.ID", null,
                    params.get("ga_visitor_id"), null, Partner.GOOGLEANALYTICS, todaydate);
        }


        peopleBean.savePeopleBehavior(ccRequest.getId(), pMain.getId(), "DATE.LAST", null, null,
                todaydate, Partner.GOOGLEANALYTICS, todaydate);


        if ((StringUtils.isNotEmpty((String) params.get("first_vizit_date")))) {
            peopleBean.savePeopleBehavior(ccRequest.getId(), pMain.getId(), "DATE.FIRST", null, null,
                    Convertor.fromUnixTimestampToDate(params.get("first_vizit_date")), Partner.GOOGLEANALYTICS, todaydate);
        }


        //пишем персональные данные
        PeoplePersonalEntity peoplePers = peopleBean.findPeoplePersonalActive(pMain);

        try {
            peopleBean.newPeoplePersonal(peoplePers, pMain.getId(), null, Partner.CLIENT,
                    peoplePers.getSurname(), peoplePers.getName(), peoplePers.getMidname(), peoplePers.getMaidenname(),
                    peoplePers.getBirthdate(), (String) params.get("place"), Convertor.toInteger(params.get("gender")),
                    peoplePers.getDatabeg(), ActiveStatus.ACTIVE);
        } catch (Exception e) {
            logger.severe("Не удалось сохранить персональные данные по человеку, ошибка " + e.getMessage());
        }

        //запишем снилс и инн, если они есть
        try {
            peopleBean.savePeopleMain(pMain, (String) params.get("inn"), (String) params.get("snils"));
        } catch (PeopleException e2) {
            logger.severe("Не удалось записать снилс и инн по человеку " + pMain.getId() + ", ошибка " + e2);
        }

        //save register address data
        AddressEntity registerationAddress = peopleBean.findAddressActive(pMain, FiasAddress.REGISTER_ADDRESS);

        try {
            peopleBean.newAddressFias(registerationAddress, pMain.getId(), null, Partner.CLIENT, FiasAddress.REGISTER_ADDRESS,
                    null, new Date(), null, null, ActiveStatus.ACTIVE, (String) params.get("registrationFias"),
                    (String) params.get("home"), (String) params.get("korpus"),
                    (String) params.get("builder"), (String) params.get("apartment"));
        } catch (Exception e1) {
            logger.severe("Не удалось сохранить адрес, ошибка " + e1);
        }


        //сохраняем дополнительные данные
        PeopleMiscEntity pMisc = peopleBean.findPeopleMiscActive(pMain);

        try {
            peopleBean.newPeopleMisc(pMisc, pMain.getId(), null, Partner.CLIENT,
                    Convertor.toInteger(params.get("marriage")), Convertor.toInteger(params.get("children")),
                    Convertor.toInteger(params.get("underage")), null, Convertor.toDate(params.get("RealtyDate"), DatesUtils.FORMAT_ddMMYYYY),
                    null, pMisc == null ? null : pMisc.getRegDate(), null, false, new Date(), ActiveStatus.ACTIVE, false);
        } catch (Exception e) {
            logger.severe("Произошла ошибка при сохранении доп.данных, ошибка " + e);
        }

        //сохраняем информацию о партнере
        if (StringUtils.isNotEmpty((String) params.get("surname"))) {
            Integer itype = null;
            if (params.get("typework") != null) {
                itype = Convertor.toInteger(params.get("typework"));
            }
            Date dt = new Date();
            if (params.get("databeg") != null) {
                dt = Convertor.toDate(params.get("databeg"), DatesUtils.FORMAT_ddMMYYYY);
            }

            peopleBean.addSpouse(pMain, Convertor.toRightString((String) params.get("surname")), Convertor.toRightString((String) params.get("name")), Convertor.toRightString((String) params.get("midname")), Convertor.toDate(params.get("birthdate"), DatesUtils.FORMAT_ddMMYYYY), Convertor.fromMask((String) params.get("mobilephone")), Spouse.CODE_SPOUSE, dt, itype);
        } else {
            peopleBean.closeSpouse(pMain, new Date());

        }

        mp.put("creditRequestId", ccRequest.getId());
        return mp;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveStep3(Map<String, Object> params) throws KassaException {

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));

        PeopleMainEntity pMain = ccRequest.getPeopleMainId();
        AddressEntity register_addr = null;

        //сохраняем информацию о совпадении адреса регистрации с адресом проживания
        register_addr = peopleBean.findAddressActive(pMain, FiasAddress.REGISTER_ADDRESS);
        Integer matuche = 0;
        if (params.containsKey("matuche")) {
            matuche = Convertor.toInteger(params.get("matuche"));
        }
        register_addr.setIsSame(matuche);

        emMicro.persist(register_addr);

        //если адреса не совпадают, запишем новый адрес
        if (matuche != BaseAddress.IS_SAME) {
            AddressEntity residentAddress = peopleBean.findAddressActive(pMain, FiasAddress.RESIDENT_ADDRESS);

            try {
                peopleBean.newAddressFias(residentAddress, pMain.getId(), null, Partner.CLIENT, FiasAddress.RESIDENT_ADDRESS,
                        null, new Date(), null, null, ActiveStatus.ACTIVE, (String) params.get("livingFias"),
                        (String) params.get("home"), (String) params.get("korpus"),
                        (String) params.get("builder"), (String) params.get("apartment"));
            } catch (Exception e1) {
                logger.severe("Не удалось сохранить адрес проживания, ошибка " + e1);
            }


        } else {
            //если вводили адрес проживания, закроем его
            AddressEntity resident_addr = peopleBean.findAddressActive(pMain, FiasAddress.RESIDENT_ADDRESS);
            if (resident_addr != null) {
                peopleBean.closeAddress(resident_addr, new Date());
            }
        }


        //save people home phone
        if (StringUtils.isNotEmpty((String) params.get("phone"))) {
            peopleBean.setPeopleContactClient(pMain, PeopleContact.CONTACT_HOME_PHONE, Convertor.fromMask((String) params.get("phone")), (Boolean) params.get("available"));
        } else {
            PeopleContactEntity pc = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_HOME_PHONE, pMain.getId());
            if (pc != null) {
                try {
                    peopleBean.changeContactActive(pc.getId());
                } catch (PeopleException e) {
                    logger.severe("Не удалось изменить домашний телефон " + e);
                    throw new KassaException(e);
                }
            }
        }

        //save people home registration phone

        if (StringUtils.isNotEmpty((String) params.get("regphone"))) {
            peopleBean.setPeopleContactClient(pMain, PeopleContact.CONTACT_HOME_REGISTER_PHONE, Convertor.fromMask((String) params.get("regphone")), (Boolean) params.get("regavailable"));
        } else {
            PeopleContactEntity pc = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_HOME_REGISTER_PHONE, pMain.getId());
            if (pc != null) {
                try {

                    peopleBean.changeContactActive(pc.getId());
                } catch (PeopleException e) {
                    logger.severe("Не удалось изменить домашний телефон по месту регистрации " + e);
                    throw new KassaException(e);
                }
            }
        }


        //save misc data
        PeopleMiscEntity pMisc = peopleBean.findPeopleMiscActive(pMain);

        try {
            peopleBean.newPeopleMisc(pMisc, pMain.getId(), null, Partner.CLIENT,
                    null, pMisc.getChildren(), pMisc.getUnderage(), null, pMisc.getRealtyDate(), null,
                    Convertor.toDate(params.get("RegDate"), DatesUtils.FORMAT_ddMMYYYY),
                    Convertor.toInteger(params.get("ground")), false, new Date(), ActiveStatus.ACTIVE, false);
        } catch (Exception e) {
            logger.severe("Произошла ошибка при сохранении доп.данных, ошибка " + e);
        }

        return ccRequest.getId();

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveStep4(Map<String, Object> params) throws KassaException {

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));
        PeopleMainEntity peopleMain = ccRequest.getPeopleMainId();

        //сохраняем информацию о занятости

        EmploymentEntity employ = peopleBean.findEmployment(peopleMain, null, refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);

        try {
            peopleBean.newEmployment(employ, peopleMain.getId(), null, Partner.CLIENT, (String) params.get("workplace"),
                    (String) params.get("occupation"), Convertor.toInteger(params.get("education")),
                    Convertor.toInteger(params.get("employment")), Convertor.toInteger(params.get("freqType")),
                    Convertor.toInteger(params.get("profession")), Convertor.toInteger(params.get("extsalarysource")),
                    Convertor.toDouble(params.get("monthlyincome")), Convertor.toDouble(params.get("extincome")),
                    Convertor.toDate(params.get("experience"), DatesUtils.FORMAT_ddMMYYYY),
                    null, Convertor.toDate(params.get("datestartwork"), DatesUtils.FORMAT_ddMMYYYY),
                    Employment.CURRENT, new Date(), null, null, null);
        } catch (Exception e) {
            logger.severe("Не удалось сохранить данные о занятости");
        }

        //save misc data
        PeopleMiscEntity pMisc = peopleBean.findPeopleMiscActive(peopleMain);
        boolean car = false;
        if (params.get("car").toString().equals("1") || params.get("car").toString().equals("true")) {
            car = true;
        }
        try {
            peopleBean.newPeopleMisc(pMisc, peopleMain.getId(), null, Partner.CLIENT,
                    null, pMisc.getChildren(), pMisc.getUnderage(), null, pMisc.getRealtyDate(), null, pMisc.getRegDate(),
                    null, car, new Date(), ActiveStatus.ACTIVE, false);
        } catch (Exception e) {
            logger.severe("Произошла ошибка при сохранении доп.данных, ошибка " + e);
        }

        //save work phone
        if (StringUtils.isNotEmpty((String) params.get("workphone"))) {
            peopleBean.setPeopleContactClient(peopleMain, PeopleContact.CONTACT_WORK_PHONE, Convertor.fromMask((String) params.get("workphone")), (Boolean) params.get("available"));
        } else {
            PeopleContactEntity pc = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_WORK_PHONE, peopleMain.getId());
            if (pc != null) {
                try {

                    peopleBean.changeContactActive(pc.getId());
                } catch (PeopleException e) {
                    logger.severe("Не удалось изменить рабочий телефон " + e);
                    throw new KassaException(e);
                }
            }
        }


        //save working address data if it's not empty
        if (StringUtils.isNotEmpty((String) params.get("jobFias"))) {
            AddressEntity jobAddress = peopleBean.findAddressActive(peopleMain, FiasAddress.WORKING_ADDRESS);

            try {
                peopleBean.newAddressFias(jobAddress, peopleMain.getId(), null, Partner.CLIENT, FiasAddress.WORKING_ADDRESS,
                        null, new Date(), null, null, ActiveStatus.ACTIVE, (String) params.get("jobFias"),
                        (String) params.get("home"), (String) params.get("korpus"),
                        (String) params.get("builder"), null);
            } catch (Exception e1) {
                logger.severe("Не удалось сохранить адрес проживания, ошибка " + e1);
            }

        } else {
            AddressEntity register_addr = peopleBean.findAddressActive(peopleMain, FiasAddress.WORKING_ADDRESS);
            if (register_addr != null) {
                peopleBean.closeAddress(register_addr, new Date());
            }


        }
        return ccRequest.getId();
    }


    @Override
    public BigInteger findMaxCreditNumber() {

        String sql = "select nextval('credit_id_credit_seq') ";
        Query qry = emMicro.createNativeQuery(sql);
        List<BigInteger> lst = qry.getResultList();
        if (lst.size() > 0 && lst.get(0) != null) {
            return lst.get(0).add(BigInteger.valueOf(1));
        } else {
            return BigInteger.valueOf(1);
        }

    }

    public Integer findMaxCreditRequestNumber() {
        String sql = "select max (nomer) from ru.simplgroupp.persistence.CreditRequestEntity ";
        Query qry = emMicro.createQuery(sql);
        List<Integer> lst = qry.getResultList();
        if (lst.size() > 0 && lst.get(0) != null) {
            return lst.get(0) + 1;
        } else {
            return 1;
        }
    }

    @Override
    public Integer findMaxCreditRequestNumber(Date dt) {
        String sql = "select max (nomer) from ru.simplgroupp.persistence.CreditRequestEntity where date_part('day',datecontest)=:day and date_part('month',datecontest)=:month and date_part('year',datecontest)=:year";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("day", DatesUtils.getDay(dt));
        qry.setParameter("month", DatesUtils.getMonth(dt));
        qry.setParameter("year", DatesUtils.getYear(dt));
        List<Integer> lst = qry.getResultList();
        if (lst.size() > 0 && lst.get(0) != null) {
            return lst.get(0) + 1;
        } else {
            return new Integer(1);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveStep7(Map<String, Object> params) throws KassaException {

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));
        //сохраняем окончательно заявку
        PeopleMainEntity peopleMain = ccRequest.getPeopleMainId();

        //константы для рассчета 
        Map<String, Object> limits = null;
        if (ccRequest.getProductId() != null) {
            limits = productBean.getNewRequestProductConfig(ccRequest.getProductId().getId());
        } else {
            ProductsEntity product = productDAO.getProductDefault();
            limits = productBean.getNewRequestProductConfig(product.getId());
            ccRequest.setProductId(product);
        }
        //ставка по-новому
        double stake = creditCalc.calcInitialStake(ccRequest.getCreditsum(), ccRequest.getCreditdays(), limits);
        Integer i = findMaxCreditRequestNumber(new Date());

        CreditRequest crt = crDAO.getCreditRequest(ccRequest.getId(), Utils.setOf(PeopleMain.Options.INIT_ACCOUNTACTIVE,
                PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL,
                PeopleMain.Options.INIT_DOCUMENT, PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_PEOPLE_MISC));
        //генерируем оферту
        String agreement = "";
        if (crt != null) {
            agreement = generateAgreement(crt, new Date(), 0);
        }

        //пишем данные заявки
        ccRequest = newCreditRequest(ccRequest, peopleMain.getId(), CreditStatus.FILLED,
                ccRequest.getContest(), ccRequest.getContestAsp(), ccRequest.getContestCb(), ccRequest.getContestPd(),
                ccRequest.getConfirmed(), null, null, ccRequest.getDatecontest(), new Date(), ccRequest.getDateStatus(),
                i, Convertor.toDouble(params.get("creditsum"), CalcUtils.dformat), Convertor.toInteger(params.get("creditdays")),
                stake, agreement, null, GenUtils.genUniqueNumber(new Date(), i, stake * 100), null, null, RefCreditRequestWay.ONLINE, null);

        //ищем логин человека в зависимости от настроек
        PeopleContactEntity Email = peopleBean.findPeopleByContactMan(peopleBean.findContactTypeForLogin(), peopleMain.getId());
        if (Email != null) {
            userBean.addUserClient(peopleMain, Email.getValue());
        }
        //сохранили лог
        eventLog.saveLog((String) params.get("ipAddress"), eventLog.getEventTypeEntity(EventType.INFO), eventLog.getEventCodeEntity(EventCode.SAVE_CREDIT_REQUEST),
                "заполнение заявки успешно завершено", ccRequest, peopleMain, null, null, "", "", "", "");

        return ccRequest.getId();
    }

    @Override
    public List<CreditRequest> listCreditRequestWithoutCredits(Integer peopleId) throws KassaException {
        String hql = "from ru.simplgroupp.persistence.CreditRequestEntity as c where (c.peopleMainId.id = :peopleId) and (c.acceptedcreditId = null) and (c.statusId.id in (:statuses)) order by c.datecontest desc ";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("peopleId", peopleId);
        qry.setParameter("statuses", Arrays.asList(CreditStatus.CLIENT_REFUSE, CreditStatus.REJECTED, CreditStatus.IN_PROCESS));
        List<CreditRequestEntity> lst = qry.getResultList();
        if (lst.size() > 0) {
            List<CreditRequest> lst1 = new ArrayList<CreditRequest>(lst.size());
            for (CreditRequestEntity cre : lst) {
                CreditRequest cr = new CreditRequest(cre);
                lst1.add(cr);
            }
            return lst1;
        } else
            return new ArrayList<CreditRequest>(0);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<CreditRequest> findCreditRequestActives(int peopleMainId, Set options) throws KassaException {
        List<CreditRequest> lst;
        try {
            lst = crDAO.findCreditRequestActive(peopleMainId, options);
            return lst;
        } catch (Exception e) {
            throw new KassaException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest findCreditRequestActive(int peopleMainId, Set options) throws KassaException {
        List<CreditRequest> lst;
        try {
            lst = crDAO.findCreditRequestActive(peopleMainId, options);
            return (CreditRequest) Utils.firstOrNull(lst);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return null;
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest findLastCreditRequestClosed(int peopleMainId, Set options) throws KassaException {

        List<CreditRequestEntity> lst = crDAO.findCreditRequestsForMan(peopleMainId);
        if (lst.size() == 0) {
            return null;
        } else {
            CreditRequest cr = new CreditRequest(lst.get(0));
            cr.init(options);
            return cr;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest findCreditRequestInProcess(int peopleMainId, Set options) throws KassaException {

        List<CreditRequestEntity> lst = crDAO.findCreditRequestActive(peopleMainId, Arrays.asList(CreditStatus.IN_PROCESS));
        if (lst.size() == 0) {
            return null;
        } else {
            CreditRequest cr = new CreditRequest(lst.get(0));
            cr.init(options);
            return cr;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public CreditRequest findCreditRequestRejected(int peopleMainId, Set options) throws KassaException {
        List<CreditRequestEntity> lst = crDAO.findCreditRequestActive(peopleMainId, Arrays.asList(CreditStatus.REJECTED));
        if (lst.size() == 0) {
            return null;
        } else {
            CreditRequest cr = new CreditRequest(lst.get(0));
            cr.init(options);
            return cr;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public CreditRequest findCreditRequestWaitingSign(int peopleMainId, Set options) throws KassaException {

        List<CreditRequestEntity> lst = JPAUtils.getResultList(emMicro, "findCreditRequestWaitingSign", Utils.mapOf(
                "peopleMainId", peopleMainId,
                "accepted", CreditRequest.ACCEPTED,
                "status", CreditStatus.DECISION
        ));
        if (lst.size() == 0) {
            return null;
        } else {
            CreditRequest cr = new CreditRequest(lst.get(0));
            cr.init(options);
            return cr;
        }
    }

    @Override
    @Deprecated
    public List<CreditRequest> listCreditRequests(int nFirst, int nCount, SortCriteria[] sorting, Set options,
                                                  Integer peopleMainId, Integer rejectReasonId, Integer statusId, Integer accepted,
                                                  DateRange date, IntegerRange days, MoneyRange sum, Boolean isover, DateRange creditDateEnd,
                                                  String peopleSurname, String peopleName, String peopleMidname, Integer docTypeId, String docSeries, String docNomer,
                                                  String peopleEmail, String peoplePhone, String peopleSNILS, String peopleINN, String uniqueNomer,
                                                  String taskDefKey, Integer wayId) {
        return listCreditRequests(nFirst,nCount,sorting,options,peopleMainId,rejectReasonId,statusId,accepted,date,days,sum,isover,creditDateEnd,
                peopleSurname,peopleName,peopleMidname,docTypeId,docSeries,docNomer,peopleEmail,peoplePhone,peopleSNILS,peopleINN,
                uniqueNomer,taskDefKey,wayId,null);
    }


    @Override
    public List<CreditRequest> listCreditRequests(CreditRequestFilter filter) {

        if (filter.getPeopleSNILS() != null || filter.getPeopleINN() != null) {
            PeopleMainEntity peopleEnt = peopleBean.findPeopleMain(filter.getPeopleINN(), filter.getPeopleSNILS());
            if (peopleEnt != null) {
                filter.setPeopleMainId(peopleEnt.getId());
            }
        }

        String sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.CreditRequestEntity as c where (1=1)";
        if (filter.getRequestID() != null) {
            sql = sql + " and (c.id = :requestID)";
        }
        if (filter.getPeopleMainId() != null) {
            sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
        }
        if (filter.getStatusId() != null) {
            sql = sql + " and (c.statusId.id = :statusId)";
        }
        if (filter.getWayId() != null && filter.getWayId() > 0) {
            sql = sql + " and (c.wayId.codeinteger = :wayId)";
        }
        if (filter.getRejectReasonId() != null) {
            sql = sql + " and (c.rejectreasonId.reasonId.codeinteger = :rejectreasonId)";
        }
        if (filter.getRefuseReasonId() != null) {
            sql = sql + " and (c.rejectreasonId.id = :refuseReasonID)";
        }
        if (filter.getAccepted() != null) {
            sql = sql + " and (c.accepted = :accepted)";
        }
        if ((filter.getDate() != null) && (filter.getDate().getFrom() != null)) {
            sql = sql + " and (c.datecontest >= :dfrom)";
        }
        if ((filter.getDate() != null) && (filter.getDate().getTo() != null)) {
            sql = sql + " and (c.datecontest < :dto)";
        }
        if ((filter.getDays() != null) && (filter.getDays().getFrom() != null)) {
            sql = sql + " and (c.creditdays>=:daysfrom)";
        }
        if ((filter.getDays() != null) && (filter.getDays().getTo() != null)) {
            sql = sql + " and (c.creditdays<=:daysto)";
        }
        if ((filter.getSum() != null) && (filter.getSum().getFrom() != null)) {
            sql = sql + " and (c.creditsum>=:sumfrom)";
        }
        if ((filter.getSum() != null) && (filter.getSum().getTo() != null)) {
            sql = sql + " and (c.creditsum<=:sumto)";
        }
        if (filter.getCreditManagerID() != null) {
            sql = sql + " and ((select count(*) from c.logs as l where l.userId.peopleMainId.id = :creditManagerID and l.eventcodeid.id = 4) > 0)";
        }
        if (filter.getIsover() != null) {
            if (filter.getIsover()) {
                sql = sql + " and (c.acceptedcreditId.creditdataend < :dateNow)";
            } else {
                sql = sql + " and (c.acceptedcreditId.creditdataend >= :dateNow)";
            }
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getFrom() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend >= :creditDateEndFrom)";
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getTo() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend < :creditDateEndTo)";
        }
        if (filter.getUniqueNomer() != null) {
            sql = sql + " and (c.uniquenomer like :uniqueNomer)";
        }
        if(filter.getWorkplaceID() != null){
            sql += " and (c.workplace.id = :workplace_id)";
        }
        if (filter.getDecisionWayID() != null) {
            sql += " and (c.decisionWayId.codeinteger = :decisionWayID)";
        }
        if(filter.getAcceptedcredit_id()!=null){
            sql += " and (c.acceptedcreditId is "+(filter.getAcceptedcredit_id()==1?"not":"")+" null)";
        }
        if (filter.getPeopleSurname() == null && filter.getPeopleName() == null && filter.getPeopleMidname() == null) {
//	    	sql = sql.replace("[$JOIN_PEOPLE$]", "");
        } else {
            sql = sql + " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where [$WHERE_PEOPLE$] ) > 0 )";

            String swhere = "(1=1)";
            if (filter.getPeopleSurname() != null) {
                swhere = swhere + " and (upper(p.surname) like :peopleSurname)";
            }
            if (filter.getPeopleName() != null) {
                swhere = swhere + " and (upper(p.name) like :peopleName)";
            }
            if (filter.getPeopleMidname() != null) {
                swhere = swhere + " and (upper(p.midname) like :peopleMidname)";
            }
            sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
        }

        if (!(filter.getDocTypeId() == null && filter.getDocSeries() == null && filter.getDocNomer() == null)) {
            sql = sql + " and ( (select count(*) from c.peopleMainId.documents as d where [$WHERE_DOCS$] ) > 0 )";

            String swhere = "(1=1)";
            if (filter.getDocTypeId() != null) {
                swhere = swhere + " and (d.documenttypeId.codeinteger = :docTypeId)";
            }
            if (filter.getDocSeries() != null) {
                swhere = swhere + " and (d.series = :docSeries)";
            }
            if (filter.getDocNomer() != null) {
                swhere = swhere + " and (d.number = :docNomer)";
            }
            sql = sql.replace("[$WHERE_DOCS$]", swhere);
        }

        if (!((filter.getPeopleEmail() == null) && (filter.getPeoplePhone() == null))) {
            sql = sql + " and ( (select count(*) from c.peopleMainId.peoplecontact as t where [$WHERE_CONTACT$] ) > 0 )";

            String swhere = "(1=0)";
            if (filter.getPeopleEmail() != null) {
                swhere = swhere + " or ((t.contactId.codeinteger = :codeConEmail) and (t.value = :peopleEmail))";
            }
            if (filter.getPeoplePhone() != null) {
                swhere = swhere + " or ((t.contactId.codeinteger in (:codeConPhoneHome, :codeConPhoneWork, :codeConPhoneMobile)) and (t.value = :peoplePhone))";
            }

            sql = sql.replace("[$WHERE_CONTACT$]", swhere);
        }
        if (filter.getFirstCredit() != null) {
            if (filter.getFirstCredit()) {
                sql = sql + " and ((select count(sc) from c.peopleMainId.systemCredits as sc where sc.isover = true) = 0)";
            } else {
                sql = sql + " and ((select count(sc) from c.peopleMainId.systemCredits as sc where sc.isover = true) > 0)";
            }
        }

        if (filter.getPeopleAnyPhone() != null) {
            sql = sql + " and ((select count(*) from c.peopleMainId.peoplecontact as pc where " +
                    "pc.contactId.codeinteger in (:anyConPhoneHome, :anyConPhoneWork, " +
                    ":anyConPhoneMobile, :anyConPhoneRegisterHome, :anyConPhoneDop1, :anyConPhoneDop2) and (pc.value = :anyPeoplePhone)) > 0)";
        }

        List<Integer> lstCR = null;
        if (!StringUtils.isBlank(filter.getTaskDefKey())) {
            SignalRef ref = SignalRef.valueOf(filter.getTaskDefKey());
            lstCR = wfEngine.listTasksCR(ref.getName());
            if (lstCR.size() == 0) {
                return new ArrayList<CreditRequest>(0);
            }
            sql = sql + " and (c.id in (:lstCR))";
        }

        sql = sql + SearchUtil.sortToString(ccRequestSortMapping, filter.getSorting());
        String sortSelect = SearchUtil.sortSelectToString(ccRequestSortMapping, filter.getSorting());
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

        if (StringUtils.isEmpty(sortSelect)) {
            sql += " order by c.datecontest desc ";
        }

        Query qry = emMicro.createQuery(sql);
        if (filter.getRequestID() != null) {
            qry.setParameter("requestID", filter.getRequestID());
        }
        if (filter.getPeopleMainId() != null) {
            qry.setParameter("peopleMainId", filter.getPeopleMainId());
        }
        if (filter.getStatusId() != null) {
            qry.setParameter("statusId", filter.getStatusId());
        }
        if (filter.getRejectReasonId() != null) {
            qry.setParameter("rejectreasonId", filter.getRejectReasonId());
        }
        if (filter.getRefuseReasonId() != null) {
            qry.setParameter("refuseReasonID", filter.getRefuseReasonId());
        }
        if (filter.getWayId() != null && filter.getWayId() > 0) {
            qry.setParameter("wayId", filter.getWayId());
        }
        if (filter.getAccepted() != null) {
            qry.setParameter("accepted", filter.getAccepted());
        }
        if ((filter.getDate() != null) && (filter.getDate().getFrom() != null)) {
            qry.setParameter("dfrom", filter.getDate().getFrom(), TemporalType.DATE);
        }
        if ((filter.getDate() != null) && (filter.getDate().getTo() != null)) {
            qry.setParameter("dto", DateUtils.addDays(filter.getDate().getTo(), 1), TemporalType.DATE);
        }
        if ((filter.getDays() != null) && (filter.getDays().getFrom() != null)) {
            qry.setParameter("daysfrom", filter.getDays().getFrom());
        }
        if ((filter.getDays() != null) && (filter.getDays().getTo() != null)) {
            qry.setParameter("daysto", filter.getDays().getTo());
        }
        if ((filter.getSum() != null) && (filter.getSum().getFrom() != null)) {
            qry.setParameter("sumfrom", filter.getSum().getFrom().doubleValue());
        }
        if ((filter.getSum() != null) && (filter.getSum().getTo() != null)) {
            qry.setParameter("sumto", filter.getSum().getTo().doubleValue());
        }
        if (filter.getCreditManagerID() != null) {
            qry.setParameter("creditManagerID", filter.getCreditManagerID());
        }
        if (filter.getIsover() != null) {
            qry.setParameter("dateNow", new Date(), TemporalType.DATE);
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getFrom() != null) {
            qry.setParameter("creditDateEndFrom", filter.getCreditDateEnd().getFrom(), TemporalType.DATE);
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getTo() != null) {
            qry.setParameter("creditDateEndTo", DateUtils.addDays(filter.getCreditDateEnd().getTo(), 1), TemporalType.DATE);
        }
        if (filter.getUniqueNomer() != null) {
            qry.setParameter("uniqueNomer", "%" + filter.getUniqueNomer() + "%");
        }

        if (filter.getPeopleSurname() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleSurname().trim().toUpperCase() : "%" + filter.getPeopleSurname().trim().toUpperCase() + "%";
            qry.setParameter("peopleSurname", q);
        }
        if (filter.getPeopleName() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleName().trim().toUpperCase() : "%" + filter.getPeopleName().trim().toUpperCase() + "%";
            qry.setParameter("peopleName", q);
        }
        if (filter.getPeopleMidname() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleMidname().trim().toUpperCase() : "%" + filter.getPeopleMidname().trim().toUpperCase() + "%";
            qry.setParameter("peopleMidname", q);
        }
        if(filter.getWorkplaceID() != null){
            qry.setParameter("workplace_id", filter.getWorkplaceID());
        }
        if (filter.getDecisionWayID() != null) {
            qry.setParameter("decisionWayID", filter.getDecisionWayID());
        }
        if (!(filter.getDocTypeId() == null && filter.getDocSeries() == null && filter.getDocNomer() == null)) {
            if (filter.getDocTypeId() != null) {
                qry.setParameter("docTypeId", filter.getDocTypeId());
            }
            if (filter.getDocSeries() != null) {
                qry.setParameter("docSeries", filter.getDocSeries());
            }
            if (filter.getDocNomer() != null) {
                qry.setParameter("docNomer", filter.getDocNomer());
            }
        }

        if (!((filter.getPeopleEmail() == null) && (filter.getPeoplePhone() == null))) {
            if (filter.getPeopleEmail() != null) {
                qry.setParameter("codeConEmail", PeopleContact.CONTACT_EMAIL);
                qry.setParameter("peopleEmail", filter.getPeopleEmail());
            }
            if (filter.getPeoplePhone() != null) {
                qry.setParameter("codeConPhoneHome", PeopleContact.CONTACT_HOME_PHONE);
                qry.setParameter("codeConPhoneWork", PeopleContact.CONTACT_WORK_PHONE);
                qry.setParameter("codeConPhoneMobile", PeopleContact.CONTACT_CELL_PHONE);
                qry.setParameter("peoplePhone", filter.getPeoplePhone());
            }
        }

        if (filter.getPeopleAnyPhone() != null) {
            qry.setParameter("anyConPhoneHome", PeopleContact.CONTACT_HOME_PHONE);
            qry.setParameter("anyConPhoneWork", PeopleContact.CONTACT_WORK_PHONE);
            qry.setParameter("anyConPhoneMobile", PeopleContact.CONTACT_CELL_PHONE);
            qry.setParameter("anyConPhoneRegisterHome", PeopleContact.CONTACT_HOME_REGISTER_PHONE);
            qry.setParameter("anyConPhoneDop1", PeopleContact.CONTACT_DOPPHONE1);
            qry.setParameter("anyConPhoneDop2", PeopleContact.CONTACT_DOPPHONE2);
            qry.setParameter("anyPeoplePhone", filter.getPeopleAnyPhone());
        }

        if (lstCR != null) {
            qry.setParameter("lstCR", lstCR);
        }

        if (filter.getnFirst() >= 0)
            qry.setFirstResult(filter.getnFirst());
        if (filter.getnCount() > 0)
            qry.setMaxResults(filter.getnCount());

        List<CreditRequestEntity> lst = null;
        if (filter.getSorting() == null || filter.getSorting().length == 0) {
            lst = qry.getResultList();
        } else {
            List<Object[]> lstSource = qry.getResultList();
            lst = new ArrayList<CreditRequestEntity>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, lst);
            lstSource = null;
        }

        if (lst.size() > 0) {
            List<CreditRequest> lst1 = new ArrayList<>(lst.size());
            for (CreditRequestEntity cre : lst) {
                CreditRequest cr = new CreditRequest(cre);
                lst1.add(cr);
                if (filter.getOptions() != null && filter.getOptions().size() > 0) {
                    cr.init(filter.getOptions());
                }
            }
            return lst1;
        } else
            return new ArrayList<>(0);
    }

    @Override
    @Deprecated
    public List<CreditRequest> listCreditRequests(int nFirst, int nCount, SortCriteria[] sorting, Set options,
                                                  Integer peopleMainId, Integer rejectReasonId, Integer statusId, Integer accepted,
                                                  DateRange date, IntegerRange days, MoneyRange sum, Boolean isover, DateRange creditDateEnd,
                                                  String peopleSurname, String peopleName, String peopleMidname, Integer docTypeId, String docSeries, String docNomer,
                                                  String peopleEmail, String peoplePhone, String peopleSNILS, String peopleINN, String uniqueNomer,
                                                  String taskDefKey, Integer wayId, Integer workplace_id) {

        CreditRequestFilter filter = new CreditRequestFilter();
        filter.setnFirst(nFirst);
        filter.setnCount(nCount);
        filter.setSorting(sorting);
        filter.setOptions(options);
        filter.setPeopleMainId(peopleMainId);
        filter.setRejectReasonId(rejectReasonId);
        filter.setStatusId(statusId);
        filter.setAccepted(accepted);
        filter.setDate(date);
        filter.setDays(days);
        filter.setSum(sum);
        filter.setIsover(isover);
        filter.setCreditDateEnd(creditDateEnd);
        filter.setPeopleSurname(peopleSurname);
        filter.setPeopleName(peopleName);
        filter.setPeopleMidname(peopleMidname);
        filter.setDocTypeId(docTypeId);
        filter.setDocSeries(docSeries);
        filter.setDocNomer(docNomer);
        filter.setPeopleEmail(peopleEmail);
        filter.setPeoplePhone(peoplePhone);
        filter.setPeopleSNILS(peopleSNILS);
        filter.setPeopleINN(peopleINN);
        filter.setUniqueNomer(uniqueNomer);
        filter.setTaskDefKey(taskDefKey);
        filter.setWayId(wayId);
        filter.setWorkplaceID(workplace_id);

        return listCreditRequests(filter);
    }

    @Override
    @Deprecated
    public int countCreditRequests(Integer peopleMainId,
                                   Integer rejectReasonId, Integer statusId, Integer accepted,
                                   DateRange date, IntegerRange days, MoneyRange sum, Boolean isover, DateRange creditDateEnd,
                                   String peopleSurname, String peopleName, String peopleMidname, Integer docTypeId, String docSeries, String docNomer,
                                   String peopleEmail, String peoplePhone, String peopleSNILS, String peopleINN, String uniqueNomer,
                                   String taskDefKey, Integer wayId) {
        return countCreditRequests(peopleMainId,rejectReasonId,statusId,accepted,date,days,sum,isover,creditDateEnd,
                peopleSurname,peopleName,peopleMidname,docTypeId,docSeries,docNomer,peopleEmail,peoplePhone,peopleSNILS,peopleINN,
                uniqueNomer,taskDefKey,wayId,null);
    }

    @Override
    public Integer countCreditRequests(CreditRequestFilter filter) {
        if (filter.getPeopleSNILS() != null || filter.getPeopleINN() != null) {
            PeopleMainEntity peopleEnt = peopleBean.findPeopleMain(filter.getPeopleINN(), filter.getPeopleSNILS());
            if (peopleEnt != null) {
                filter.setPeopleMainId(peopleEnt.getId());
            }
        }

        String sql = "select count(c) from ru.simplgroupp.persistence.CreditRequestEntity as c where (1=1)";
        if (filter.getRequestID() != null) {
            sql = sql + " and (c.id = :requestID)";
        }
        if (filter.getPeopleMainId() != null) {
            sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
        }
        if (filter.getStatusId() != null) {
            sql = sql + " and (c.statusId.id = :statusId)";
        }
        if (filter.getWayId() != null && filter.getWayId() > 0) {
            sql = sql + " and (c.wayId.codeinteger = :wayId)";
        }
        if (filter.getRejectReasonId() != null) {
            sql = sql + " and (c.rejectreasonId.reasonId.codeinteger = :rejectreasonId)";
        }
        if (filter.getRefuseReasonId() != null) {
            sql = sql + " and (c.rejectreasonId.id = :refuseReasonID)";
        }
        if (filter.getAccepted() != null) {
            sql = sql + " and (c.accepted = :accepted)";
        }
        if ((filter.getDate() != null) && (filter.getDate().getFrom() != null)) {
            sql = sql + " and (c.datecontest >= :dfrom)";
        }
        if ((filter.getDate() != null) && (filter.getDate().getTo() != null)) {
            sql = sql + " and (c.datecontest < :dto)";
        }
        if ((filter.getDays() != null) && (filter.getDays().getFrom() != null)) {
            sql = sql + " and (c.creditdays>=:daysfrom)";
        }
        if ((filter.getDays() != null) && (filter.getDays().getTo() != null)) {
            sql = sql + " and (c.creditdays<=:daysto)";
        }
        if ((filter.getSum() != null) && (filter.getSum().getFrom() != null)) {
            sql = sql + " and (c.creditsum>=:sumfrom)";
        }
        if ((filter.getSum() != null) && (filter.getSum().getTo() != null)) {
            sql = sql + " and (c.creditsum<=:sumto)";
        }
        if (filter.getCreditManagerID() != null) {
            sql = sql + " and ((select count(*) from c.logs as l where l.userId.peopleMainId.id = :creditManagerID and l.eventcodeid.id = 4) > 0)";
        }
        if (filter.getIsover() != null) {
            if (filter.getIsover()) {
                sql = sql + " and (c.acceptedcreditId.creditdataend < :dateNow)";
            } else {
                sql = sql + " and (c.acceptedcreditId.creditdataend >= :dateNow)";
            }
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getFrom() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend >= :creditDateEndFrom)";
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getTo() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend < :creditDateEndTo)";
        }
        if (filter.getUniqueNomer() != null) {
            sql = sql + " and (c.uniquenomer like :uniqueNomer)";
        }
        if(filter.getWorkplaceID() != null){
            sql += " and (c.workplace.id = :workplace_id)";
        }
        if (filter.getDecisionWayID() != null) {
            sql += " and (c.decisionWayId.codeinteger = :decisionWayID)";
        }
        if (filter.getPeopleSurname() == null && filter.getPeopleName() == null && filter.getPeopleMidname() == null) {
//	    	sql = sql.replace("[$JOIN_PEOPLE$]", "");
        } else {
            sql = sql + " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where [$WHERE_PEOPLE$] ) > 0 )";

            String swhere = "(1=1)";
            if (filter.getPeopleSurname() != null) {
                swhere = swhere + " and (upper(p.surname) like :peopleSurname)";
            }
            if (filter.getPeopleName() != null) {
                swhere = swhere + " and (upper(p.name) like :peopleName)";
            }
            if (filter.getPeopleMidname() != null) {
                swhere = swhere + " and (upper(p.midname) like :peopleMidname)";
            }
            sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
        }

        if (!(filter.getDocTypeId() == null && filter.getDocSeries() == null && filter.getDocNomer() == null)) {
            sql = sql + " and ( (select count(*) from c.peopleMainId.documents as d where [$WHERE_DOCS$] ) > 0 )";

            String swhere = "(1=1)";
            if (filter.getDocTypeId() != null) {
                swhere = swhere + " and (d.documenttypeId.codeinteger = :docTypeId)";
            }
            if (filter.getDocSeries() != null) {
                swhere = swhere + " and (d.series = :docSeries)";
            }
            if (filter.getDocNomer() != null) {
                swhere = swhere + " and (d.number = :docNomer)";
            }
            sql = sql.replace("[$WHERE_DOCS$]", swhere);
        }

        if (!((filter.getPeopleEmail() == null) && (filter.getPeoplePhone() == null))) {
            sql = sql + " and ( (select count(*) from c.peopleMainId.peoplecontact as t where [$WHERE_CONTACT$] ) > 0 )";

            String swhere = "(1=0)";
            if (filter.getPeopleEmail() != null) {
                swhere = swhere + " or ((t.contactId.codeinteger = :codeConEmail) and (t.value = :peopleEmail))";
            }
            if (filter.getPeoplePhone() != null) {
                swhere = swhere + " or ((t.contactId.codeinteger in (:codeConPhoneHome, :codeConPhoneWork, :codeConPhoneMobile)) and (t.value = :peoplePhone))";
            }

            sql = sql.replace("[$WHERE_CONTACT$]", swhere);
        }
        if (filter.getFirstCredit() != null) {
            if (filter.getFirstCredit()) {
                sql = sql + " and ((select count(sc) from c.peopleMainId.systemCredits as sc where sc.isover = true) = 0)";
            } else {
                sql = sql + " and ((select count(sc) from c.peopleMainId.systemCredits as sc where sc.isover = true) > 0)";
            }
        }

        if (filter.getPeopleAnyPhone() != null) {
            sql = sql + " and ((select count(*) from c.peopleMainId.peoplecontact as pc where " +
                    "pc.contactId.codeinteger in (:anyConPhoneHome, :anyConPhoneWork, " +
                    ":anyConPhoneMobile, :anyConPhoneRegisterHome, :anyConPhoneDop1, :anyConPhoneDop2) and (pc.value = :anyPeoplePhone)) > 0)";
        }

        List<Integer> lstCR = null;
        if (!StringUtils.isBlank(filter.getTaskDefKey())) {
            SignalRef ref = SignalRef.valueOf(filter.getTaskDefKey());
            lstCR = wfEngine.listTasksCR(ref.getName());
            if (lstCR.size() == 0) {
                return 0;
            }
            sql = sql + " and (c.id in (:lstCR))";
        }

        Query qry = emMicro.createQuery(sql);
        if (filter.getRequestID() != null) {
            qry.setParameter("requestID", filter.getRequestID());
        }
        if (filter.getPeopleMainId() != null) {
            qry.setParameter("peopleMainId", filter.getPeopleMainId());
        }
        if (filter.getStatusId() != null) {
            qry.setParameter("statusId", filter.getStatusId());
        }
        if (filter.getRejectReasonId() != null) {
            qry.setParameter("rejectreasonId", filter.getRejectReasonId());
        }
        if (filter.getRefuseReasonId() != null) {
            qry.setParameter("refuseReasonID", filter.getRefuseReasonId());
        }
        if (filter.getWayId() != null && filter.getWayId() > 0) {
            qry.setParameter("wayId", filter.getWayId());
        }
        if (filter.getAccepted() != null) {
            qry.setParameter("accepted", filter.getAccepted());
        }
        if ((filter.getDate() != null) && (filter.getDate().getFrom() != null)) {
            qry.setParameter("dfrom", filter.getDate().getFrom(), TemporalType.DATE);
        }
        if ((filter.getDate() != null) && (filter.getDate().getTo() != null)) {
            qry.setParameter("dto", DateUtils.addDays(filter.getDate().getTo(), 1), TemporalType.DATE);
        }
        if ((filter.getDays() != null) && (filter.getDays().getFrom() != null)) {
            qry.setParameter("daysfrom", filter.getDays().getFrom());
        }
        if ((filter.getDays() != null) && (filter.getDays().getTo() != null)) {
            qry.setParameter("daysto", filter.getDays().getTo());
        }
        if ((filter.getSum() != null) && (filter.getSum().getFrom() != null)) {
            qry.setParameter("sumfrom", filter.getSum().getFrom().doubleValue());
        }
        if ((filter.getSum() != null) && (filter.getSum().getTo() != null)) {
            qry.setParameter("sumto", filter.getSum().getTo().doubleValue());
        }
        if (filter.getCreditManagerID() != null) {
            qry.setParameter("creditManagerID", filter.getCreditManagerID());
        }
        if (filter.getIsover() != null) {
            qry.setParameter("dateNow", new Date(), TemporalType.DATE);
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getFrom() != null) {
            qry.setParameter("creditDateEndFrom", filter.getCreditDateEnd().getFrom(), TemporalType.DATE);
        }
        if (filter.getCreditDateEnd() != null && filter.getCreditDateEnd().getTo() != null) {
            qry.setParameter("creditDateEndTo", DateUtils.addDays(filter.getCreditDateEnd().getTo(), 1), TemporalType.DATE);
        }
        if (filter.getUniqueNomer() != null) {
            qry.setParameter("uniqueNomer", filter.getUniqueNomer());
        }

        if (filter.getPeopleSurname() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleSurname().trim().toUpperCase() : "%" + filter.getPeopleSurname().trim().toUpperCase() + "%";
            qry.setParameter("peopleSurname", q);
        }
        if (filter.getPeopleName() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleName().trim().toUpperCase() : "%" + filter.getPeopleName().trim().toUpperCase() + "%";
            qry.setParameter("peopleName", q);
        }
        if (filter.getPeopleMidname() != null) {
            String q = filter.isCallCenterOnly() ? filter.getPeopleMidname().trim().toUpperCase() : "%" + filter.getPeopleMidname().trim().toUpperCase() + "%";
            qry.setParameter("peopleMidname", q);
        }
        if(filter.getWorkplaceID() != null){
            qry.setParameter("workplace_id", filter.getWorkplaceID());
        }
        if (filter.getDecisionWayID() != null) {
            qry.setParameter("decisionWayID", filter.getDecisionWayID());
        }
        if (!(filter.getDocTypeId() == null && filter.getDocSeries() == null && filter.getDocNomer() == null)) {
            if (filter.getDocTypeId() != null) {
                qry.setParameter("docTypeId", filter.getDocTypeId());
            }
            if (filter.getDocSeries() != null) {
                qry.setParameter("docSeries", filter.getDocSeries());
            }
            if (filter.getDocNomer() != null) {
                qry.setParameter("docNomer", filter.getDocNomer());
            }
        }

        if (!((filter.getPeopleEmail() == null) && (filter.getPeoplePhone() == null))) {
            if (filter.getPeopleEmail() != null) {
                qry.setParameter("codeConEmail", PeopleContact.CONTACT_EMAIL);
                qry.setParameter("peopleEmail", filter.getPeopleEmail());
            }
            if (filter.getPeoplePhone() != null) {
                qry.setParameter("codeConPhoneHome", PeopleContact.CONTACT_HOME_PHONE);
                qry.setParameter("codeConPhoneWork", PeopleContact.CONTACT_WORK_PHONE);
                qry.setParameter("codeConPhoneMobile", PeopleContact.CONTACT_CELL_PHONE);
                qry.setParameter("peoplePhone", filter.getPeoplePhone());
            }
        }

        if (filter.getPeopleAnyPhone() != null) {
            qry.setParameter("anyConPhoneHome", PeopleContact.CONTACT_HOME_PHONE);
            qry.setParameter("anyConPhoneWork", PeopleContact.CONTACT_WORK_PHONE);
            qry.setParameter("anyConPhoneMobile", PeopleContact.CONTACT_CELL_PHONE);
            qry.setParameter("anyConPhoneRegisterHome", PeopleContact.CONTACT_HOME_REGISTER_PHONE);
            qry.setParameter("anyConPhoneDop1", PeopleContact.CONTACT_DOPPHONE1);
            qry.setParameter("anyConPhoneDop2", PeopleContact.CONTACT_DOPPHONE2);
            qry.setParameter("anyPeoplePhone", filter.getPeopleAnyPhone());
        }

        if (lstCR != null) {
            qry.setParameter("lstCR", lstCR);
        }

        List lst = qry.getResultList();
        if (lst.size() == 0) {
            return 0;
        } else {
            return ((Number) lst.get(0)).intValue();
        }
    }

    @Override
    @Deprecated
    public int countCreditRequests(Integer peopleMainId,
                                   Integer rejectReasonId, Integer statusId, Integer accepted,
                                   DateRange date, IntegerRange days, MoneyRange sum, Boolean isover, DateRange creditDateEnd,
                                   String peopleSurname, String peopleName, String peopleMidname, Integer docTypeId, String docSeries, String docNomer,
                                   String peopleEmail, String peoplePhone, String peopleSNILS, String peopleINN, String uniqueNomer,
                                   String taskDefKey, Integer wayId, Integer workplace_id) {

        CreditRequestFilter filter = new CreditRequestFilter();
        filter.setPeopleMainId(peopleMainId);
        filter.setRejectReasonId(rejectReasonId);
        filter.setStatusId(statusId);
        filter.setAccepted(accepted);
        filter.setDate(date);
        filter.setDays(days);
        filter.setSum(sum);
        filter.setIsover(isover);
        filter.setCreditDateEnd(creditDateEnd);
        filter.setPeopleSurname(peopleSurname);
        filter.setPeopleName(peopleName);
        filter.setPeopleMidname(peopleMidname);
        filter.setDocTypeId(docTypeId);
        filter.setDocSeries(docSeries);
        filter.setDocNomer(docNomer);
        filter.setPeopleEmail(peopleEmail);
        filter.setPeoplePhone(peoplePhone);
        filter.setPeopleSNILS(peopleSNILS);
        filter.setPeopleINN(peopleINN);
        filter.setUniqueNomer(uniqueNomer);
        filter.setTaskDefKey(taskDefKey);
        filter.setWayId(wayId);
        filter.setWorkplaceID(workplace_id);

        return countCreditRequests(filter);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCreditDecision(Integer creditRequestId, boolean bAccepted, Integer rejectCode)
            throws KassaException {
        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);
        if (bAccepted) {
            ccRequest.setAccepted(CreditRequest.ACCEPTED);
        } else {
            ccRequest.setAccepted(CreditRequest.NOT_ACCEPTED);
            ccRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.REJECTED));
            RefuseReasonEntity ref = refBooks.getRefuseReason(rejectCode).getEntity();
            if (ref == null) {
                logger.severe("Неизвестный код отказа: " + rejectCode.toString());
                throw new KassaException("Неизвестный код отказа: " + rejectCode.toString());
            }
            ccRequest.setRejectreasonId(ref);
        }
        emMicro.persist(ccRequest);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCreditApproveWithoutCredit(Integer creditRequestId, String comment) throws KassaException {

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);

        //добавляем информацию к заявке
        ccRequest.setDateStatus(new Date());
        if (StringUtils.isNotEmpty(comment)){
            ccRequest.setComment(comment);
        }
        if (ccRequest.getDecisionWayId()==null){
        	ccRequest.setDecisionWayId(refBooks.findByCodeIntegerEntity(RefHeader.EXECUTION_WAY, Reference.AUTO_EXEC));
        }
        if (ccRequest.getDateDecision()==null){
        	ccRequest.setDateDecision(new Date());
        }
        ccRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.DECISION));
        ccRequest.setAccepted(CreditRequest.ACCEPTED);

        crDAO.saveCreditRequest(ccRequest);

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int saveCreditApprove(Integer creditRequestId, String comment)
            throws KassaException {

        CreditRequest crReq = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
        CreditRequestEntity ccRequest = crReq.getEntity();

        //добавляем новый кредит
        CreditEntity cred = new CreditEntity();
        cred.setCreditRequestId(ccRequest);
        cred.setPeopleMainId(ccRequest.getPeopleMainId());
        cred.setPartnersId(refBooks.getPartnerEntity(Partner.SYSTEM));
        cred.setCreditsum(ccRequest.getCreditsum());
        cred.setCreditdataend(DateUtils.addDays(new Date(), ccRequest.getCreditdays()));

        cred.setCreditAccount(ccRequest.getUniquenomer());
        cred.setAccountTypeId(ccRequest.getAccountId().getAccountTypeId());
        cred.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_ACTIVE).getEntity());
        cred.setCredittypeId(refBooks.getCreditType(BaseCredit.MICRO_CREDIT).getEntity());
        cred.setCreditrelationId(refBooks.getCreditRelationType(BaseCredit.CREDIT_OWNER).getEntity());
        cred.setIdCredit(String.valueOf(findMaxCreditNumber()));

        cred.setCreditpercent(ccRequest.getStake());

        // рассчитываем параметры кредита
        Map<String, Object> crparams = creditCalc.calcCreditInitial(crReq);

        cred.setCreditdatabeg((Date) crparams.get(CreditCalculatorBeanLocal.DATE_START_ACTUAL));
        ccRequest.setCreditdays((Integer) crparams.get(CreditCalculatorBeanLocal.DAYS_ACTUAL));
        cred.setCreditsumback((Double) crparams.get(CreditCalculatorBeanLocal.SUM_BACK));
        cred.setIdCurrency(refBooks.getCurrency(BaseCredit.CURRENCY_RUR).getEntity());
        cred.setIssameorg(true);
        cred.setIsover(false);
        emMicro.persist(cred);

        //добавляем информацию к заявке
        ccRequest.setDateStatus(new Date());
        ccRequest.setComment(comment);
        ccRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.DECISION));
        ccRequest.setAccepted(CreditRequest.ACCEPTED);
        ccRequest.setAcceptedcreditId(cred);
        emMicro.merge(ccRequest);

        //добавляем запись в график
        paymentService.savePaymentSchedule(cred, ccRequest.getStake(), ccRequest.getCreditdays(), cred.getCreditsumback(), new Date(), cred.getCreditsum());

        //добавляем запись в платежи, пока ставим, что реально платеж не сделан
        paymentService.createPayment(cred.getId(), ccRequest.getAccountId().getAccountTypeId().getCodeinteger(),
                Payment.MAIN_SUM_TO_CLIENT, ccRequest.getCreditsum(), Payment.FROM_SYSTEM, null, 0, null, null);

        //пишем в лог
        eventLog.saveLog("localhost", eventLog.getEventTypeEntity(EventType.INFO),
                eventLog.getEventCodeEntity(EventCode.GET_CREDIT_REQUEST_RESULT), comment, ccRequest,
                ccRequest.getPeopleMainId(), null, null, "", "", "", "");

        return cred.getId();
    }

    @Override
   // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int startCredit(Integer creditRequestId) throws KassaException {

        CreditRequest crReq = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
        if (crReq == null) {
            return 0;
        }
        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);
        if (ccRequest.getWayId().getCodeinteger() == RefCreditRequestWay.DIRECT) {
            ccRequest.setDateSign(new Date());
            emMicro.merge(ccRequest);
        }
        //добавляем новый кредит
        CreditEntity cred = new CreditEntity();
        cred.setCreditRequestId(ccRequest);
        cred.setProductId(ccRequest.getProductId());
        cred.setPeopleMainId(ccRequest.getPeopleMainId());
        cred.setPartnersId(refBooks.getPartnerEntity(Partner.SYSTEM));
        cred.setCreditsum(ccRequest.getCreditsum());
        cred.setIsactive(ActiveStatus.ACTIVE);
        cred.setCreditdataend(DateUtils.addDays(new Date(), ccRequest.getCreditdays()));

        cred.setCreditAccount(ccRequest.getUniquenomer());
        cred.setAccountTypeId(ccRequest.getAccountId().getAccountTypeId());
        cred.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_ACTIVE).getEntity());
        cred.setDateStatus(new Date());
        cred.setCredittypeId(refBooks.getCreditType(BaseCredit.MICRO_CREDIT).getEntity());
        cred.setCreditrelationId(refBooks.getCreditRelationType(BaseCredit.CREDIT_OWNER).getEntity());
        cred.setIdCredit(String.valueOf(findMaxCreditNumber()));

        cred.setCreditpercent(ccRequest.getStake());
        cred.setCreditFullCost(CalcUtils.calcYearStake(ccRequest.getStake(),new Date()));

        // рассчитываем параметры кредита
        Map<String, Object> crparams = creditCalc.calcCreditInitial(crReq);

        cred.setCreditdatabeg((Date) crparams.get(CreditCalculatorBeanLocal.DATE_START_ACTUAL));
        ccRequest.setCreditdays(Convertor.toInteger(crparams.get(CreditCalculatorBeanLocal.DAYS_ACTUAL)));
        cred.setCreditsumback(Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_BACK)));
        cred.setIdCurrency(refBooks.getCurrency(BaseCredit.CURRENCY_RUR).getEntity());
        cred.setIssameorg(true);
        cred.setIsover(false);
        cred.setIsUploaded(false);
        emMicro.persist(cred);

        //ставим id кредита в оферту
        OfficialDocumentsEntity document = officialDocumentsDAO.findOfficialDocumentCreditRequestDraft(ccRequest.getPeopleMainId().getId(),
                creditRequestId, OfficialDocuments.OFERTA_CREDIT);
        if (document != null) {
            document.setIsActive(ActiveStatus.ACTIVE);
            document.setCreditId(cred);
            emMicro.merge(document);
        }

        //пишем детали кредита
        CreditDetailsEntity creditDetail = creditBean.saveCreditDetail(cred, BaseCredit.OPERATION_CREDIT, cred.getCreditdatabeg(), cred.getCreditdataend(), cred.getCreditsum(),
                null, cred.getCreditsumback(), null, null, null);

        //пишем сумму кредита
        creditBean.saveCreditSum(cred.getId(), BaseCredit.SUM_MAIN, BaseCredit.OPERATION_IN,
                cred.getCreditdatabeg(), cred.getCreditsum(), creditDetail.getId());

        //добавляем информацию к заявке
        ccRequest.setDateStatus(new Date());
        ccRequest.setAcceptedcreditId(cred);
        emMicro.merge(ccRequest);

        //добавляем запись в график
        paymentService.savePaymentSchedule(cred, ccRequest.getStake(), ccRequest.getCreditdays(), cred.getCreditsumback(), new Date(), cred.getCreditsum());

        // TODO лог со стартом кредита

        //записали лог - подписание оферты
        eventLog.saveLog(EventType.INFO, EventCode.OFERTA_SIGNED, "Была подписан оферта на новый заем",
                null, ccRequest.getDateSign(), ccRequest, peopleDAO.getPeopleMainEntity(ccRequest.getPeopleMainId().getId()), null);

        //добавляем запись в платежи, пока ставим, что реально платеж не сделан
        int partnerId = 0;
        try {
            ActionContext ctx = servBean.getActionContext(null);
            String pluginName = actProc.findPaymentSystemAcc(ccRequest.getAccountId().getAccountTypeId().getCodeinteger(), ctx);
            PluginSystemLocal plugin = ctx.getPlugins().getPluginByName(pluginName);
            if (plugin == null) {
                throw new ActionException(ErrorKeys.PLUGIN_NOT_FOUND, "Плугин " + pluginName + " не найден", Type.TECH, ResultType.FATAL, null);
            }
            partnerId = plugin.getPartnerId();
        } catch (ActionException e) {
            logger.severe("Не удалось найти систему для платежа по кредиту " + cred.getId());
            throw new KassaException(e);
        }

        paymentService.createPayment(cred.getId(), ccRequest.getAccountId().getAccountTypeId().getCodeinteger(),
                Payment.MAIN_SUM_TO_CLIENT, ccRequest.getCreditsum(), Payment.FROM_SYSTEM, null, partnerId, ccRequest.getAccountId(), null);

        return cred.getId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createPaymentToClient(int creditId, int accountId, Double creditSum, Date createDate, Integer paysumId) throws KassaException {
        AccountEntity accEnt = accDAO.getAccountEntity(accountId);
        if (accEnt == null) {
            return null;
        }

        //добавляем запись в платежи, пока ставим, что реально платеж не сделан
        int partnerId = 0;
        try {
            ActionContext ctx = servBean.getActionContext(null);
            String pluginName = actProc.findPaymentSystemAcc(accEnt.getAccountTypeId().getCodeinteger(), ctx);
            PluginSystemLocal plugin = ctx.getPlugins().getPluginByName(pluginName);
            if (plugin == null) {
                throw new ActionException(ErrorKeys.PLUGIN_NOT_FOUND, "Плугин " + pluginName + " не найден", Type.TECH, ResultType.FATAL, null);
            }
            partnerId = plugin.getPartnerId();
        } catch (ActionException e) {
            logger.severe("Не удалось найти систему для платежа по кредиту " + String.valueOf(creditId));
            throw new KassaException(e);
        }

        if (paysumId == null) {
            paysumId = Payment.MAIN_SUM_TO_CLIENT;
        }
        Payment payment = paymentService.createPayment(creditId, accEnt.getAccountTypeId().getCodeinteger(),
                paysumId, creditSum, Payment.FROM_SYSTEM, null, partnerId, accEnt, createDate);

        return payment;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCreditRefuse(Integer creditRequestId, String comment,
                                 Integer reason) throws KassaException {

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);
        ccRequest.setRejectreasonId(refBooks.getRefuseReason(reason).getEntity());
        if (StringUtils.isNotEmpty(comment)){
            ccRequest.setComment(comment);
        }
        if (ccRequest.getDecisionWayId()==null){
        	ccRequest.setDecisionWayId(refBooks.findByCodeIntegerEntity(RefHeader.EXECUTION_WAY, Reference.AUTO_EXEC));
        }
        if (ccRequest.getDateDecision()==null){
        	ccRequest.setDateDecision(new Date());
        }
        ccRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.REJECTED));
        ccRequest.setAccepted(CreditRequest.NOT_ACCEPTED);
        ccRequest.setDateStatus(new Date());
        crDAO.saveCreditRequest(ccRequest);

    }


    @Override
    public Map<Integer, Integer> overview() {
        String sql = "select c.statusId.id, count(*) from ru.simplgroupp.persistence.CreditRequestEntity c group by c.statusId.id";

        Query qry = emMicro.createQuery(sql);
        HashMap<Integer, Integer> cnt = new HashMap<>(9);
        //записываем в map статусы заявок
        cnt.put(CreditStatus.FILLED, 0);
        cnt.put(CreditStatus.REJECTED, 0);
        cnt.put(CreditStatus.FOR_COURT, 0);
        cnt.put(CreditStatus.DECISION, 0);
        cnt.put(CreditStatus.IN_PROCESS, 0);
        cnt.put(CreditStatus.FOR_COLLECTOR, 0);
        cnt.put(CreditStatus.CLOSED, 0);
        cnt.put(CreditStatus.CLIENT_REFUSE, 0);

        List<Object[]> lst = qry.getResultList();
        for (Object[] row : lst) {
            Integer stat = (Integer) row[0];
            Number ncnt = (Number) row[1];
            cnt.put(stat, ncnt.intValue());
        }
        return cnt;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeStatus(Integer creditRequestId, Integer statusId, String comment) throws KassaException {
        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);
        if (ccRequest == null) {
            logger.severe("Не найдена заявка " + creditRequestId);
            return;
        }
        ccRequest.setDateStatus(new Date());
        ccRequest.setStatusId(refBooks.getCreditRequestStatus(statusId));
        ccRequest.setComment(comment);
        emMicro.merge(ccRequest);

        //пишем в лог
        eventLog.saveLog(EventType.INFO, EventCode.GET_CREDIT_REQUEST_RESULT, comment,
                null, new Date(), ccRequest, ccRequest.getPeopleMainId(), null);

    }

    @Override
    public Map<Integer, Double> overviewSum() {
        String sql = "select sum(creditsum) from ru.simplgroupp.persistence.CreditEntity c where (c.partnersId.id = :partner) and (c.isactive=:isactive)";
        Query qry = emMicro.createQuery(sql);

        qry.setParameter("partner", Partner.SYSTEM);
        qry.setParameter("isactive", ActiveStatus.ACTIVE);
        HashMap<Integer, Double> cnt = new HashMap<Integer, Double>(2);
        //выбираем сумму выданных кредитов
        cnt.put(1, new Double(0));
        List<Double> lst = qry.getResultList();
        if (lst.size() > 0) {
            cnt.put(1, lst.get(0));
        } else {
            cnt.put(1, 0d);
        }
        sql = "select sum(amount) from ru.simplgroupp.persistence.PaymentEntity where paymenttypeId.codeinteger=:paymenttypeId and isPaid=true";
        qry = emMicro.createQuery(sql);
        qry.setParameter("paymenttypeId", Payment.TO_SYSTEM);
        //выбираем сумму возвращенных кредитов
        cnt.put(2, new Double(0));
        lst = qry.getResultList();
        if (lst.size() > 0) {
            cnt.put(2, lst.get(0));
        } else {
            cnt.put(2, 0d);
        }
        return cnt;
    }

    @Override
    public Map<Integer, Double> overviewSum(Date date) {
        DateRange dateRange=new DateRange();
        if (date!=null){
         	dateRange=DatesUtils.makeOneDayDateRange(date);
        }
        String sql = "select sum(creditsum) from ru.simplgroupp.persistence.CreditEntity c where (c.partnersId.id = :partner) and (c.isactive=:isactive) and (c.owner is null) ";
        if (date!=null){
           	sql+=" and c.creditdatabeg>=:dateFrom and c.creditdatabeg<=:dateTo";
        }
        Query qry = emMicro.createQuery(sql);
        
        if (date!=null){
        	qry.setParameter("dateFrom", dateRange.getFrom());
        }
        if (date!=null){
        	qry.setParameter("dateTo", dateRange.getTo());
        }
        qry.setParameter("partner", Partner.SYSTEM);
        qry.setParameter("isactive", ActiveStatus.ACTIVE);
        HashMap<Integer, Double> cnt = new HashMap<Integer, Double>(5);
        //выбираем сумму выданных кредитов
        cnt.put(1, new Double(0));
        List<Double> lst = qry.getResultList();
        if (lst.size() > 0&&lst.get(0)!=null) {
            cnt.put(1, lst.get(0));
        } 
        sql = "select sum(amount) from ru.simplgroupp.persistence.PaymentEntity where paymenttypeId.codeinteger=:paymenttypeId and isPaid=true";
        if (date!=null){
           	sql+=" and processDate>=:dateFrom and processDate<=:dateTo";
        }
        qry = emMicro.createQuery(sql);
        qry.setParameter("paymenttypeId", Payment.TO_SYSTEM);
        if (date!=null){
        	qry.setParameter("dateFrom", dateRange.getFrom());
        }
        if (date!=null){
        	qry.setParameter("dateTo", dateRange.getTo());
        }
        //выбираем сумму возвращенных кредитов
        cnt.put(2, new Double(0));
        lst = qry.getResultList();
        if (lst.size() > 0&&lst.get(0)!=null) {
            cnt.put(2, lst.get(0));
        } 
        sql = "select sum(creditsum) from ru.simplgroupp.persistence.CreditRequestEntity c where (c.statusId.id <> :status) ";
        if (date!=null){
           	sql+=" and c.datecontest>=:dateFrom and c.datecontest<=:dateTo)";
        }
        qry = emMicro.createQuery(sql);
        
        if (date!=null){
        	qry.setParameter("dateFrom", dateRange.getFrom());
        }
        if (date!=null){
        	qry.setParameter("dateTo", dateRange.getTo());
        }
        qry.setParameter("status", CreditStatus.IN_PROCESS);
            
        //выбираем сумму запрошенных заявок
        cnt.put(3, new Double(0));
        lst = qry.getResultList();
        if (lst.size() > 0&&lst.get(0)!=null) {
            cnt.put(3, lst.get(0));
        } 
        sql = "select sum(creditsum) from ru.simplgroupp.persistence.CreditRequestEntity c where (c.statusId.id = :status) ";
        if (date!=null){
        	sql+=" and c.datecontest>=:dateFrom and c.datecontest<=:dateTo)";
        }
        qry = emMicro.createQuery(sql);
        
        if (date!=null){
        	qry.setParameter("dateFrom", dateRange.getFrom());
        }
        if (date!=null){
        	qry.setParameter("dateTo", dateRange.getTo());
        }
        qry.setParameter("status", CreditStatus.REJECTED);
            
        //выбираем сумму отказанных заявок
        cnt.put(4, new Double(0));
        lst = qry.getResultList();
        if (lst.size() > 0&&lst.get(0)!=null) {
            cnt.put(4, lst.get(0));
        } 
        sql = "select sum(creditsum) from ru.simplgroupp.persistence.CreditRequestEntity c where (c.statusId.id = :status) ";
        if (date!=null){
        	sql+=" and c.datecontest>=:dateFrom and c.datecontest<=:dateTo)";
        }
        qry = emMicro.createQuery(sql);
        
        if (date!=null){
        	qry.setParameter("dateFrom", dateRange.getFrom());
        }
        if (date!=null){
        	qry.setParameter("dateTo", dateRange.getTo());
        }
        qry.setParameter("status", CreditStatus.CLIENT_REFUSE);
            
        //выбираем сумму отказ клиента
        cnt.put(5, new Double(0));
        lst = qry.getResultList();
        if (lst.size() > 0&&lst.get(0)!=null) {
            cnt.put(5, lst.get(0));
        } 
        return cnt;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequestEntity saveCreditRequest(CreditRequestEntity creditRequest) {
        CreditRequestEntity ent = crDAO.saveCreditRequest(creditRequest);
        // TODO событие запустить
        return ent;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest saveCreditRequest(CreditRequest creditRequest) {
        CreditRequestEntity ent = crDAO.saveCreditRequest(creditRequest.getEntity());
        // TODO событие запустить
        return new CreditRequest(ent);
    }

      
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int saveCreditRequest(CreditRequest cre, String ipAddress, String userAgent)
            throws KassaException {

        CreditRequestEntity ccRequest = addCreditRequest(cre);
        //местоположение
        String geoPlace = saveGeoLocation(ccRequest, ipAddress);
        //пишем лог о подаче заявки
        eventLog.saveLog(ipAddress, eventLog.getEventTypeEntity(EventType.INFO), eventLog.getEventCodeEntity(EventCode.SAVE_CREDIT_REQUEST), "успешно подана кредитная заявка",
                ccRequest, peopleDAO.getPeopleMainEntity(cre.getPeopleMain().getId()), null, null,
                HTTPUtils.getShortBrowserName(userAgent), Convertor.toLimitString(userAgent, 200), Convertor.toLimitString(HTTPUtils.getUserOS(userAgent), 50), geoPlace);

        return ccRequest.getId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequestEntity newCreditRequest(CreditRequestEntity creditRequest, Integer peopleMainId, Integer statusId, Boolean contest,
                                                Boolean contestAsp, Boolean contestCb, Boolean contestPd, Boolean confirmed, Integer partnerId, Integer productId,
                                                Date dateContest, Date dateFill, Date dateStatus, Integer nomer, Double creditSum, Integer creditDays,
                                                Double stake, String agreement, String smsCode, String uniqueNomer, Integer accountId, String purpose, Integer wayId, Integer user_id) {

        CreditRequestEntity ccRequest = null;

        //если это совсем новая заявка
        if (creditRequest == null) {
            ccRequest = new CreditRequestEntity();
            if (peopleMainId == null) {
                PeopleMainEntity peopleMain = new PeopleMainEntity();
                emMicro.persist(peopleMain);
                ccRequest.setPeopleMainId(peopleMain);
            } else {
                PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(peopleMainId);
                ccRequest.setPeopleMainId(peopleMain);
            }
            if(user_id!=null){
                UsersEntity user = userBean.getUserEntity(user_id);
                ccRequest.setUser(user);
                ccRequest.setWorkplace(user.getWorkplace());
            }
        } else {
            ccRequest = creditRequest;
            if (ccRequest.getPeopleMainId() == null) {
                ccRequest.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
            }
        }
        if (statusId != null) {
            ccRequest.setStatusId(refBooks.getCreditRequestStatus(statusId));
        }
        ccRequest.setContestAsp(contestAsp);
        ccRequest.setContestPd(contestPd);
        ccRequest.setContestCb(contestCb);
        if (partnerId != null) {
            ccRequest.setPartnersId(refBooks.getPartnerEntity(partnerId));
        }
        if (productId != null) {
            ProductsEntity product = productDAO.getProduct(productId);
            ccRequest.setProductId(product);
        }
        ccRequest.setContest(contest);
        ccRequest.setDatecontest(dateContest);
        ccRequest.setDateFill(dateFill);
        ccRequest.setDateStatus(dateStatus);
        ccRequest.setNomer(nomer);
        ccRequest.setCreditsum(creditSum);
        ccRequest.setCreditsumInitial(creditSum);
        ccRequest.setCreditdays(creditDays);
        ccRequest.setCreditdaysInitial(creditDays);
        ccRequest.setStake(stake);
        ccRequest.setInitialStake(stake);
        ccRequest.setAgreement(agreement);
        ccRequest.setSmsCode(smsCode);
        ccRequest.setConfirmed(confirmed);
        ccRequest.setIsUploaded(false);

        if (accountId != null) {
            AccountEntity accEnt = accDAO.getAccountEntity(accountId);
            ccRequest.setAccountId(accEnt);
        }
        if (purpose != null) {
            ccRequest.setCreditPurposeId(refBooks.findByCodeEntity(RefHeader.CREDIT_PURPOSE, purpose));
        }
        if (wayId != null) {
            ccRequest.setWayId(refBooks.findByCodeIntegerEntity(RefHeader.APPLICATION_WAY, wayId));
        }
        ccRequest.setUniquenomer(uniqueNomer);
        ccRequest = emMicro.merge(ccRequest);
        emMicro.persist(ccRequest);
        return ccRequest;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequestEntity addCreditRequest(CreditRequest creditRequest) throws KassaException {

        CreditRequestEntity ccRequest = null;
        if (creditRequest.getId() == null) {
            ccRequest = new CreditRequestEntity();
        } else {
            ccRequest = crDAO.getCreditRequestEntity(creditRequest.getId());
        }
        // сохраняем
        ccRequest.setClientTimezone(creditRequest.getClientTimezone());
        //пишем данные заявки
        ccRequest = newCreditRequest(ccRequest, creditRequest.getPeopleMain().getId(), creditRequest.getStatus().getId(), creditRequest.getContest(),
                creditRequest.getContestAsp(), creditRequest.getContestCb(), creditRequest.getContestPd(), creditRequest.getConfirmed(),
                Partner.SYSTEM, creditRequest.getProduct() != null ? creditRequest.getProduct().getId() : null,
                creditRequest.getDateContest(), creditRequest.getDateFill(), creditRequest.getDateStatus(),
                creditRequest.getNomer(), creditRequest.getCreditSum(), creditRequest.getCreditDays(),
                creditRequest.getStake(), creditRequest.getAgreement(), creditRequest.getSmsCode(), creditRequest.getUniqueNomer(),
                creditRequest.getAccount() != null ? creditRequest.getAccount().getId() : null,
                refBooks.referenceCodeOrNull(creditRequest.getCreditPurpose()),
                refBooks.referenceCodeIntegerOrNull(creditRequest.getWay()), null
        );

        return ccRequest;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String saveGeoLocation(CreditRequestEntity creditRequest, String ipAddress) {
        String geoPlace = "";

        try {
            //url для поиска местоположения по ip
            Map<String, Object> params = rulesBean.getSiteConstants();
            String geoSite = (String) params.get(SettingsKeys.GEO_URL);

            if (StringUtils.isNotEmpty(geoSite)) {

                Document geoDoc = GeoUtils.geoDescriptionDoc(geoSite.trim() + ipAddress);
                if (geoDoc != null) {
                    RegionsEntity region = null;
                    String city = "";
                    if (XmlUtils.isExistNode(geoDoc, "region")) {
                        region = refBooks.findRegionByName(XmlUtils.getNodeValueText(geoDoc.getElementsByTagName("region").item(0)));
                        creditRequest.setRegion(region);
                    }
                    if (XmlUtils.isExistNode(geoDoc, "city")) {
                        city = XmlUtils.getNodeValueText(geoDoc.getElementsByTagName("city").item(0));
                        creditRequest.setCity(city);
                    }
                    emMicro.merge(creditRequest);
                }
                geoPlace = GeoUtils.geoDescription(geoDoc);
            }
        } catch (Exception e) {
            logger.severe("не удалось определить адрес");
        }
        return geoPlace;
    }

    @Override
    public String getGeoLocation(String ipAddress) throws KassaException {
        if ("127.0.0.1".equals(ipAddress)) {
            return "LOCALHOST";
        }
        Map<String, Object> params = rulesBean.getSiteConstants();
        String geoSite = (String) params.get(SettingsKeys.GEO_URL);
        Document geoDoc = null;
        try {
            geoDoc = GeoUtils.geoDescriptionDoc(geoSite.trim() + ipAddress);
            return GeoUtils.geoDescription(geoDoc);
        } catch (Exception e) {
            throw new KassaException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int saveCreditRequest(CreditRequest cre, String ipAddress, String userAgent, Map<String, String> behdata) throws KassaException {
        int id = saveCreditRequest(cre, ipAddress, userAgent);

        if (behdata != null) {

            Date todaydate = new Date();
            PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(cre.getPeopleMain().getId());
            logger.info("Номер заявки " + id);

            if (behdata.containsKey("ga_visitor_id")) {
                Long p_ga_visitor_id = Convertor.toLong(behdata.get("ga_visitor_id"));
                peopleBean.savePeopleBehavior(id, pMain.getId(), "GA.ID", null,
                        p_ga_visitor_id, null, Partner.GOOGLEANALYTICS, todaydate);
            }

            if (behdata.containsKey("first_vizit_date")) {
                Date p_first_vizit_date = Convertor.fromUnixTimestampToDate(behdata.get("first_vizit_date"));
                peopleBean.savePeopleBehavior(id, pMain.getId(), "DATE.FIRST", null, null,
                        p_first_vizit_date, Partner.GOOGLEANALYTICS, todaydate);
            }


            peopleBean.savePeopleBehavior(id, pMain.getId(), "DATE.LAST", null, null,
                    todaydate, Partner.GOOGLEANALYTICS, todaydate);


            if (behdata.containsKey("visit_count")) {
                Long p_visit_count = Convertor.toLong(behdata.get("visit_count"));
                peopleBean.savePeopleBehavior(id, pMain.getId(), "HITS", null,
                        p_visit_count, null, Partner.GOOGLEANALYTICS, todaydate);

            }

            if (behdata.containsKey("source_from")) {
                peopleBean.savePeopleBehavior(id, pMain.getId(), "REFER.FROM",
                        behdata.get("source_from"), null, null, Partner.GOOGLEANALYTICS, todaydate);
            }

        }

        return id;
    }

    @Override
    public Prolong initProlong(PeopleMain people, Credit credit, Date dateLong, Map<String, Object> limits) throws KassaException {
        //инициализируем заявку на продление, берем константы из базы
        ProlongEntity ent = new ProlongEntity();
        ent.setCreditId(credit.getEntity());
        ent.setLongdate(dateLong);
        ent.setLongstake(credit.getCreditPercent());
        ent.setLongdays(Convertor.toInteger(limits.get(ProductKeys.PROLONG_DAYS_MIN)));
        if (credit.getCreditLong() == null || credit.getCreditLong() == 0) {
            ent.setUniquenomer(credit.getCreditAccount() + "-1");
        } else {
            ent.setUniquenomer(credit.getCreditAccount() + "-" + String.valueOf((credit.getCreditLong() + 1)));
        }
        Prolong res = new Prolong(ent);

        return res;
    }


    @Override
    public Map<String, Object> paramsStep2(Integer creditRequestId) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<>();
        mp.put("id", creditRequestId);
        mp.put("place", "");
        mp.put("gender", null);
        mp.put("seria", "");
        mp.put("nomer", "");
        mp.put("whoIssued", "");
        mp.put("whenIssued", null);
        mp.put("code_dep", "");
        mp.put("inn", "");
        mp.put("snils", "");
        mp.put("marriage", null);
        mp.put("children", null);
        mp.put("underage", null);
        mp.put("registrationFias", "");
        mp.put("home", "");
        mp.put("builder", "");
        mp.put("korpus", "");
        mp.put("apartment", "");
        mp.put("surname", "");
        mp.put("name", "");
        mp.put("midname", "");
        mp.put("birthdate", null);
        mp.put("mobilephone", "");
        mp.put("typework", null);
        mp.put("databeg", null);
        mp.put("RealtyDate", null);

        //если заявка есть
        if (cr != null) {

            PeopleMainEntity pmain = cr.getPeopleMainId();

            if (pmain != null) {
                mp.put("inn", pmain.getInn());
                mp.put("snils", pmain.getSnils());

                //персональные данные
                PeoplePersonalEntity ppl = peopleBean.findPeoplePersonalActive(pmain);
                if (ppl != null) {
                    mp.put("place", ppl.getBirthplace());
                    if (ppl.getGender() != null)
                        mp.put("gender", ppl.getGender().getCodeinteger());
                }
                //паспорт
                DocumentEntity dc = peopleBean.findPassportActive(pmain);
                if (dc != null) {
                    mp.put("seria", dc.getSeries());
                    mp.put("nomer", dc.getNumber());
                    mp.put("whoIssued", dc.getDocorg());
                    mp.put("whenIssued", dc.getDocdate());
                    mp.put("code_dep", dc.getDocorgcode());
                }
                //доп.данные
                PeopleMiscEntity pmisc = peopleBean.findPeopleMiscActive(pmain);
                if (pmisc != null) {
                    if (pmisc.getMarriageId() != null)
                        mp.put("marriage", pmisc.getMarriageId().getCodeinteger());
                    mp.put("children", pmisc.getChildren());
                    mp.put("underage", pmisc.getUnderage());
                    mp.put("RealtyDate", pmisc.getRealtyDate());
                }

                //партнер
                SpouseEntity sp = peopleBean.findSpouseActive(pmain);
                if (sp != null) {
                    mp.put("databeg", sp.getDatabeg());
                    if (sp.getTypeworkId() != null) {
                        mp.put("typework", sp.getTypeworkId().getCodeinteger());
                    }
                    PeopleMainEntity psp = sp.getPeopleMainSpouseId();
                    //персональные данные
                    PeoplePersonalEntity ppl1 = peopleBean.findPeoplePersonalActive(psp);
                    if (ppl != null) {
                        mp.put("surname", ppl1.getSurname());
                        mp.put("name", ppl1.getName());
                        mp.put("midname", ppl1.getMidname());
                        mp.put("birthdate", ppl1.getBirthdate());
                    }

                    //mobile phone
                    String phon = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, psp.getId());
                    if (StringUtils.isNotEmpty(phon)) {
                        mp.put("mobilephone", phon);
                    }
                }
                //адрес регистрации
                AddressEntity address = peopleBean.findAddressActive(pmain, FiasAddress.REGISTER_ADDRESS);
                if (address != null) {
                    StringBuilder registrationFias = new StringBuilder();

                    if (StringUtils.isNotBlank(address.getStreet())) {
                        if (registrationFias.length() > 0) {
                            registrationFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getStreet());
                        if (addrObj != null) {
                            registrationFias.insert(0, addrObj.getAOGUID());
                        }
                    }

                    if (StringUtils.isNotBlank(address.getPlace())) {
                        if (registrationFias.length() > 0) {
                            registrationFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getPlace());
                        if (addrObj != null) {
                            registrationFias.insert(0, addrObj.getAOGUID());
                        }
                    }

                    if (StringUtils.isNotBlank(address.getCity())) {
                        if (registrationFias.length() > 0) {
                            registrationFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getCity());
                        if (addrObj != null) {
                            registrationFias.insert(0, addrObj.getAOGUID());
                        }
                    }

                    if (StringUtils.isNotBlank(address.getArea())) {
                        if (registrationFias.length() > 0) {
                            registrationFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getArea());
                        if (addrObj != null) {
                            registrationFias.insert(0, addrObj.getAOGUID());
                        }
                    }

                    if (StringUtils.isNotBlank(address.getRegion())) {
                        if (registrationFias.length() > 0) {
                            registrationFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getRegion());
                        if (addrObj != null) {
                            registrationFias.insert(0, addrObj.getAOGUID());
                        }
                    }

                    mp.put("registrationFias", registrationFias.toString());
                    mp.put("home", address.getHouse());
                    mp.put("builder", address.getBuilding());
                    mp.put("korpus", address.getCorpus());
                    mp.put("apartment", address.getFlat());
                }
            }
        }

        return mp;
    }

    @Override
    public Map<String, Object> paramsStep3(Integer creditRequestId) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<>();
        mp.put("id", creditRequestId);
        mp.put("matuche", null);
        mp.put("phone", "");
        mp.put("available", null);
        mp.put("regphone", "");
        mp.put("regavailable", null);
        mp.put("livingFias", "");
        mp.put("homeFact", "");
        mp.put("builderFact", "");
        mp.put("korpusFact", "");
        mp.put("apartmentFact", "");
        mp.put("ground", null);
        mp.put("RegDate", null);

        //если заявка есть
        if (cr != null) {

            PeopleMainEntity pmain = cr.getPeopleMainId();

            if (pmain != null) {
                AddressEntity adrr = peopleBean.findAddressActive(pmain, FiasAddress.REGISTER_ADDRESS);
                if (adrr != null) {
                    mp.put("matuche", adrr.getIsSame());
                }

                AddressEntity address = peopleBean.findAddressActive(pmain, FiasAddress.RESIDENT_ADDRESS);
                if (address != null) {
                    StringBuilder livingFias = new StringBuilder();

                    if (StringUtils.isNotBlank(address.getStreet())) {
                        if (livingFias.length() > 0) {
                            livingFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getStreet());
                        if (addrObj != null) {
                            livingFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getPlace())) {
                        if (livingFias.length() > 0) {
                            livingFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getPlace());
                        if (addrObj != null) {
                            livingFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getCity())) {
                        if (livingFias.length() > 0) {
                            livingFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getCity());
                        if (addrObj != null) {
                            livingFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getArea())) {
                        if (livingFias.length() > 0) {
                            livingFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getArea());
                        if (addrObj != null) {
                            livingFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getRegion())) {
                        if (livingFias.length() > 0) {
                            livingFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getRegion());
                        if (addrObj != null) {
                            livingFias.insert(0, addrObj.getAOGUID());
                        }

                    }
                    mp.put("livingFias", livingFias.toString());

                    mp.put("homeFact", address.getHouse());
                    mp.put("builderFact", address.getBuilding());
                    mp.put("korpusFact", address.getCorpus());
                    mp.put("apartmentFact", address.getFlat());
                }
                //доп.данные
                PeopleMiscEntity pmisc = peopleBean.findPeopleMiscActive(pmain);
                if (pmisc != null) {
                    if (pmisc.getRealtyId() != null)
                        mp.put("ground", pmisc.getRealtyId().getCodeinteger());
                    mp.put("RegDate", pmisc.getRegDate());
                }

                //домашний телефон
                String phone = peopleBean.findContactClient(PeopleContact.CONTACT_HOME_PHONE, pmain.getId());
                if (StringUtils.isNotEmpty(phone)) {
                    mp.put("phone", phone);
                    PeopleContactEntity pplcont = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_HOME_PHONE, pmain.getId());
                    if (pplcont != null)
                        mp.put("available", pplcont.getAvailable());
                }
                //домашний телефон по адресу регистрации
                String phone1 = peopleBean.findContactClient(PeopleContact.CONTACT_HOME_REGISTER_PHONE, pmain.getId());
                if (StringUtils.isNotEmpty(phone1)) {
                    mp.put("regphone", phone1);
                    PeopleContactEntity pplcont = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_HOME_REGISTER_PHONE, pmain.getId());
                    if (pplcont != null)
                        mp.put("regavailable", pplcont.getAvailable());
                }

            }
        }
        return mp;
    }

    @Override
    public Map<String, Object> paramsStep4(Integer creditRequestId) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", creditRequestId);
        mp.put("car", null);

        mp.put("employment", null);
        mp.put("workplace", "");
        mp.put("monthlyincome", null);
        mp.put("salarydate", null);
        mp.put("education", null);
        mp.put("profession", null);
        mp.put("occupation", "");
        mp.put("extincome", null);
        mp.put("extsalary", null);
        mp.put("experience", null);
        mp.put("datestartwork", null);
        mp.put("workphone", "");
        mp.put("available", null);
        mp.put("jobFias", "");
        mp.put("home", "");
        mp.put("builder", "");
        mp.put("korpus", "");

        //если заявка есть
        if (cr != null) {

            PeopleMainEntity pmain = cr.getPeopleMainId();

            if (pmain != null) {
                //доп. параметры
                PeopleMiscEntity pmisc = peopleBean.findPeopleMiscActive(pmain);
                if (pmisc != null)
                    mp.put("car", pmisc.getCar());

                //рабочий телефон
                String phone = peopleBean.findContactClient(PeopleContact.CONTACT_WORK_PHONE, pmain.getId());
                if (StringUtils.isNotEmpty(phone)) {
                    mp.put("phone", phone);
                    PeopleContactEntity pplcont = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_WORK_PHONE, pmain.getId());
                    if (pplcont != null)
                        mp.put("available", pplcont.getAvailable());
                }

                AddressEntity address = peopleBean.findAddressActive(pmain, FiasAddress.WORKING_ADDRESS);
                if (address != null) {
                    StringBuilder jobFias = new StringBuilder();

                    if (StringUtils.isNotBlank(address.getStreet())) {
                        if (jobFias.length() > 0) {
                            jobFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getStreet());
                        if (addrObj != null) {
                            jobFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getPlace())) {
                        if (jobFias.length() > 0) {
                            jobFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getPlace());
                        if (addrObj != null) {
                            jobFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getCity())) {
                        if (jobFias.length() > 0) {
                            jobFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getCity());
                        if (addrObj != null) {
                            jobFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getArea())) {
                        if (jobFias.length() > 0) {
                            jobFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getArea());
                        if (addrObj != null) {
                            jobFias.insert(0, addrObj.getAOGUID());
                        }

                    }

                    if (StringUtils.isNotBlank(address.getRegion())) {
                        if (jobFias.length() > 0) {
                            jobFias.insert(0, ";");
                        }
                        AddrObj addrObj = fiasServ.findAddressObject(address.getRegion());
                        if (addrObj != null) {
                            jobFias.insert(0, addrObj.getAOGUID());
                        }

                    }
                    mp.put("jobFias", jobFias.toString());
                    mp.put("home", address.getHouse());
                    mp.put("builder", address.getBuilding());
                    mp.put("korpus", address.getCorpus());
                }
                //занятость
                EmploymentEntity emp = peopleBean.findEmployment(pmain, null, refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);
                if (emp != null) {
                    if (emp.getTypeworkId() != null) {
                        mp.put("employment", emp.getTypeworkId().getCodeinteger());
                    }
                    mp.put("workplace", emp.getPlaceWork());
                    mp.put("monthlyincome", emp.getSalary());
                    if (emp.getDurationId() != null) {
                        mp.put("salarydate", emp.getDurationId().getCodeinteger());
                    }
                    if (emp.getEducationId() != null)
                        mp.put("education", emp.getEducationId().getCodeinteger());
                    if (emp.getProfessionId() != null)
                        mp.put("profession", emp.getProfessionId().getCodeinteger());
                    if (emp.getExtsalaryId() != null) {
                        mp.put("extsalarysource", emp.getExtsalaryId().getCodeinteger());
                    }
                    mp.put("occupation", emp.getOccupation());
                    mp.put("extincome", emp.getExtsalary());
                    mp.put("experience", emp.getExperience());
                    mp.put("datestartwork", emp.getDatestartwork());
                }
            }

        }

        return mp;
    }

    /**
     * Старая версия метода
     *
     * @param creditRequestId
     * @return
     */
    @Deprecated
    private Map<String, Object> paramsStep5_old(Integer creditRequestId) {
        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", creditRequestId);
        mp.put("prevcredits", null);
        mp.put("creditbik", "");
        mp.put("creditorganization", null);
        mp.put("credittype", null);
        mp.put("overdue", null);
        mp.put("currencytype", null);
        mp.put("creditsumprev", null);
        mp.put("creditcardlimit", null);
        mp.put("creditdate", null);
        mp.put("creditisover", null);
        mp.put("prolong", null);

        //если заявка есть
        if (cr != null) {

            PeopleMainEntity pmain = cr.getPeopleMainId();

            if (pmain != null) {
                List<CreditEntity> lstcred = creditBean.findCredits(pmain, cr, refBooks.getPartnerEntity(Partner.CLIENT), false, null);
                if (lstcred.size() > 0) {
                    CreditEntity cre = lstcred.get(0);

                    mp.put("prevcredits", 1);
                    if (cre.getCreditOrganizationId() != null) {
                        mp.put("creditorganization", cre.getCreditOrganizationId().getId());
                    }
                    if (cre.getCredittypeId() != null) {
                        mp.put("credittype", cre.getCredittypeId().getCodeinteger());
                    }
                    if (cre.getIdCurrency() != null) {
                        mp.put("currencytype", cre.getIdCurrency().getCodeinteger());
                    }
                    if (cre.getOverduestateId() != null) {
                        mp.put("overdue", cre.getOverduestateId().getCode());
                    }
                    mp.put("creditsumprev", cre.getCreditsum());
                    mp.put("creditcardlimit", cre.getCreditcardlimit());
                    mp.put("creditdate", cre.getCreditdatabeg());
                    mp.put("creditisover", cre.getIsover());
                    if (cre.getOverduestateId() != null) {
                        mp.put("prolong", cre.getOverduestateId().getCode());
                    }
                } else {
                    mp.put("prevcredits", 0);
                }
            }
        }
        return mp;
    }

    @Override
    public Map<String, Object> paramsStep5(Integer creditRequestId) {
        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", creditRequestId);
        mp.put("pens_doc_number", null);
        mp.put("pens_doc_date", null);
        mp.put("pens_doc", null);
        mp.put("publicpeople_role", null);
        mp.put("publicpeople_relative_fio", null);
        mp.put("publicpeople_relative_role", null);
        mp.put("creditPurpose", null);
        mp.put("benefic_fio", null);
        mp.put("other", false);
        mp.put("reclam_accept", false);

        //если заявка есть
        if (cr != null) {

            PeopleMainEntity pmain = cr.getPeopleMainId();

            if (pmain != null) {
                List<DocumentOtherEntity> lstdocs = documentOtherDAO.findPeopleOtherDocuments(pmain.getId(), RefHeader.PENSDOC_TYPES);
                if (lstdocs.size() > 0) {
                    DocumentOtherEntity cre = lstdocs.get(0);

                    mp.put("pens_doc_date", Convertor.dateToString(cre.getDocdate(), DatesUtils.FORMAT_ddMMYYYY));
                    mp.put("pens_doc_number", cre.getNumber());
                    mp.put("pens_doc", cre.getDocumenttypeId().getId());
                }

                if (cr.getCreditPurposeId() != null)
                    mp.put("creditPurpose", cr.getCreditPurposeId().getCode());

                //доп.данные
                PeopleOtherEntity pother = peopleBean.findPeopleOtherActive(pmain);
                if (pother != null) {
                    mp.put("publicpeople_role", pother.getPublic_role());
                    mp.put("publicpeople_relative_fio", pother.getPublic_relative_fio());
                    mp.put("publicpeople_relative_role", pother.getPublic_relative_role());
                    mp.put("benefic_fio", pother.getBenific_fio());
                    mp.put("other", pother.getOther());
                    mp.put("reclam_accept", pother.getReclam_accept());
                }
            }
        }
        return mp;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void changeAgreement(CreditRequest ccRequest, Date dateOferta, String agreement) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(ccRequest.getId());
        cr.setAgreement(agreement);
        cr.setSmsCode(ccRequest.getSmsCode());
        cr.setDateSign(ccRequest.getDateSign());
        cr = emMicro.merge(cr);
        emMicro.persist(cr);
    }

    @Override
    public String generateEmail(PeopleMain ppl, String code, Map<String, Object> params) throws KassaException {
        ReportGenerated rgen = null;
        try {
            rgen = reportBean.generateReport(null, code, params);
        } catch (Exception e) {
            logger.severe("Не удалось сгенерировать письмо " + e);
            throw new KassaException("Не удалось сгенерировать письмо " + e.getMessage(), e);
        }
        return rgen.getAsString();
    }

    @Override
    public String generateAgreement(CreditRequest ccRequest, Date dateCredit, Integer mode) throws KassaException {
        PeopleMain people = ccRequest.getPeopleMain();
        if (dateCredit == null) {
            dateCredit = new Date();
        }
        ReportGenerated rgen = null;
        try {
            Map<String, Object> crparams = null;
            //первоначальная оферта, при регистрации или создании займа в ЛК
            if (mode == 0) {
                Integer productId = productDAO.getProductDefault().getId();
                if (ccRequest.getProduct() != null) {
                    productId = ccRequest.getProduct().getId();
                }
                crparams = creditCalc.calcCreditInitial(ccRequest.getCreditSum(), ccRequest.getStake(), ccRequest.getCreditDays(), productId);
            }
            //перед подписанием
            else {
                crparams = creditCalc.calcCreditInitial(ccRequest);
            }
            //годовая ставка
            Double percent = CalcUtils.calcYearStake(ccRequest.getStake(),dateCredit);
            //ставка процентов в день
            Double stake = ccRequest.getStake();
            String smsCode = (StringUtils.isEmpty(ccRequest.getSmsCode())) ? "" : Convertor.toDigest(ccRequest.getSmsCode());
            PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(peopleDAO.getPeopleMainEntity(people.getId()));
            DocumentEntity passport = peopleBean.findPassportActive(peopleDAO.getPeopleMainEntity(people.getId()));
            AddressEntity address = peopleBean.findAddressActive(peopleDAO.getPeopleMainEntity(people.getId()), BaseAddress.REGISTER_ADDRESS);
            PeopleMiscEntity peopleMisc = peopleBean.findPeopleMiscActive(peopleDAO.getPeopleMainEntity(people.getId()));
            PeopleContactEntity phone = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, people.getId());
            PeopleContactEntity email = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, people.getId());
            if (peoplePersonal==null||passport==null||address==null||peopleMisc==null){
            	logger.severe("Новая оферта не может быть создана по причине недостаточных данных");
            	return new String("Новая оферта не может быть создана по причине недостаточных данных");
            }
            rgen = reportBean.generateReport(ccRequest.getProduct().getId(), Report.OFERTA_ID, Utils.<String, Object>mapOf(
                    "datesign", Convertor.toWriteDate(dateCredit),
                    "creditnumber", ccRequest.getUniqueNomer(),
                    "year_percent", CalcUtils.pskToString(percent),
                    "stake", CalcUtils.pskToString(stake*100),
                    "percent_string", DigitsToString.printString(new BigDecimal(percent)),
                    "fio", peoplePersonal.getFullName(),
                    "seriesdoc", passport.getSeries(),
                    "numberdoc", passport.getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()),
                    "orgdoc", passport.getDocorg(),
                    "addrreg", address.getDescription(),
                    "datreg", peopleMisc.getRealtyDate() != null ? DatesUtils.DATE_FORMAT_ddMMYYYY.format(peopleMisc.getRealtyDate()) : "",
                    "cellphone", (phone == null) ? "" : phone.getValue(),
                    "email", (email == null) ? "" : email.getValue(),
                    "creditSum", CalcUtils.dformat_XX.format(ccRequest.getCreditSum()),
                    "sumString", MoneyString.num2str(ccRequest.getCreditSum()),
                    "creditPercent", CalcUtils.dformat_XX.format(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT)),
                    "credit_Percentstring", MoneyString.num2str(Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "creditDataend", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(dateCredit, ccRequest.getCreditDays())),
                    "fullSumm", CalcUtils.dformat_XX.format(ccRequest.getCreditSum()+Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "full_Summstring", MoneyString.num2str(ccRequest.getCreditSum()+Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "smscode", smsCode

            ));
        } catch (Exception e) {
            logger.severe("Новая оферта не создана по причине " + e.getMessage());
            throw new KassaException("Новая оферта не создана по причине " + e, e);
        }
        return rgen.getAsString();
    }

    @Override
    public String generateAgreementWithoutData(CreditRequest ccRequest, Date dateCredit, Integer mode) throws KassaException {
        PeopleMain people = ccRequest.getPeopleMain();
        if (dateCredit == null) {
            dateCredit = new Date();
        }
        ReportGenerated rgen = null;
        try {
            Map<String, Object> crparams = null;
            //первоначальная оферта, при регистрации или создании займа в ЛК
            if (mode == 0) {
                Integer productId = productDAO.getProductDefault().getId();
                if (ccRequest.getProduct() != null) {
                    productId = ccRequest.getProduct().getId();
                }
                crparams = creditCalc.calcCreditInitial(ccRequest.getCreditSum(), ccRequest.getStake(), ccRequest.getCreditDays(), productId);
            }
            //перед подписанием
            else {
                crparams = creditCalc.calcCreditInitial(ccRequest);
            }
            //годовая ставка
            Double percent = CalcUtils.calcYearStake(ccRequest.getStake(),dateCredit);
            //ставка процентов в день
            Double stake = ccRequest.getStake();
            String smsCode = (StringUtils.isEmpty(ccRequest.getSmsCode())) ? "" : Convertor.toDigest(ccRequest.getSmsCode());
            PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(peopleDAO.getPeopleMainEntity(people.getId()));
            DocumentEntity passport = peopleBean.findPassportActive(peopleDAO.getPeopleMainEntity(people.getId()));
            AddressEntity address = peopleBean.findAddressActive(peopleDAO.getPeopleMainEntity(people.getId()), BaseAddress.REGISTER_ADDRESS);
            PeopleContactEntity phone = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, people.getId());
            PeopleContactEntity email = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, people.getId());
            if (peoplePersonal==null||passport==null||address==null){
            	logger.severe("Новая оферта не может быть создана по причине недостаточных данных");
            	return new String("Новая оферта не может быть создана по причине недостаточных данных");
            }
            rgen = reportBean.generateReport(ccRequest.getProduct().getId(), Report.OFERTA_ID, Utils.<String, Object>mapOf(
                    "datesign", Convertor.toWriteDate(dateCredit),
                    "creditnumber", ccRequest.getUniqueNomer(),
                    "year_percent", CalcUtils.pskToString(percent),
                    "stake", CalcUtils.pskToString(stake*100),
                    "percent_string", DigitsToString.printString(new BigDecimal(percent)),
                    "fio", peoplePersonal.getFullName(),
                    "numberdoc", passport.getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()),
                    "orgdoc", passport.getDocorg(),
                    "addrreg", address.getDescription(),
                    "cellphone", (phone == null) ? "" : phone.getValue(),
                    "email", (email == null) ? "" : email.getValue(),
                    "creditSum", CalcUtils.dformat_XX.format(ccRequest.getCreditSum()),
                    "sumString", MoneyString.num2str(ccRequest.getCreditSum()),
                    "creditPercent", CalcUtils.dformat_XX.format(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT)),
                    "credit_Percentstring", MoneyString.num2str(Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "creditDataend", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(dateCredit, ccRequest.getCreditDays())),
                    "fullSumm", CalcUtils.dformat_XX.format(ccRequest.getCreditSum()+Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "full_Summstring", MoneyString.num2str(ccRequest.getCreditSum()+Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT))),
                    "smscode", smsCode

            ));
        } catch (Exception e) {
            logger.severe("Новая оферта не создана по причине " + e.getMessage());
            throw new KassaException("Новая оферта не создана по причине " + e, e);
        }
        return rgen.getAsString();
    }

    @Override
    public String generateConsent(String reportId,String initials,Date birthDate,String birthPlace,
    		String docDescription,String addressRegDescription,String addressResDescription,
    		String smsCode,Date dateConsent) throws KassaException{
    	
    	ReportGenerated rgen = null;
    	try{
    		String smsCodeHash = (StringUtils.isEmpty(smsCode)) ? "" : Convertor.toDigest(smsCode);
    		rgen = reportBean.generateReport(null, reportId, Utils.<String, Object>mapOf(
    				"datesign", Convertor.toWriteDate(dateConsent),
                    "initials", initials,
                    "birthPlace", birthPlace,
                    "docDescription", docDescription,
                    "addressRegDescription", addressRegDescription,
                    "addressResDescription", addressResDescription,
                    "birthDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(birthDate),
                    "smscode", smsCodeHash

            ));
    	}catch (Exception e) {
            logger.severe("Новое согласие не создано по причине " + e.getMessage());
            throw new KassaException("Новое согласие не создано по причине " + e, e);
        }
        return rgen.getAsString();
    	
    }
    
    @Override
    public String generateAgreement(Credit credit,String smsCodeOferta) throws KassaException {
        PeopleMain people = credit.getPeopleMain();
       
        ReportGenerated rgen = null;
        try {
          
            //годовая ставка
            Double percent = CalcUtils.calcYearStake(credit.getCreditPercent(),credit.getCreditDataBeg());
            //ставка процентов в день
            Double stake = credit.getCreditPercent();
            String smsCode = (StringUtils.isEmpty(smsCodeOferta)) ? "" : Convertor.toDigest(smsCodeOferta);
            PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(peopleDAO.getPeopleMainEntity(people.getId()));
            DocumentEntity passport = peopleBean.findPassportActive(peopleDAO.getPeopleMainEntity(people.getId()));
            AddressEntity address = peopleBean.findAddressActive(peopleDAO.getPeopleMainEntity(people.getId()), BaseAddress.REGISTER_ADDRESS);
            PeopleMiscEntity peopleMisc = peopleBean.findPeopleMiscActive(peopleDAO.getPeopleMainEntity(people.getId()));
            PeopleContactEntity phone = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, people.getId());
            PeopleContactEntity email = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, people.getId());
            if (peoplePersonal==null||passport==null||address==null||peopleMisc==null){
            	logger.severe("Новая оферта не может быть создана по причине недостаточных данных");
            	return new String("Новая оферта не может быть создана по причине недостаточных данных");
            }
            rgen = reportBean.generateReport(credit.getProduct().getId(), Report.OFERTA_ID, Utils.<String, Object>mapOf(
                    "datesign", Convertor.toWriteDate(credit.getCreditDataBeg()),
                    "creditnumber", credit.getCreditAccount(),
                    "year_percent", CalcUtils.pskToString(percent),
                    "stake", CalcUtils.pskToString(stake*100),
                    "percent_string", DigitsToString.printString(new BigDecimal(percent)),
                    "fio", peoplePersonal.getFullName(),
                    "seriesdoc", passport.getSeries(),
                    "numberdoc", passport.getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()),
                    "orgdoc", passport.getDocorg(),
                    "addrreg", address.getDescription(),
                    "datreg", peopleMisc.getRealtyDate() != null ? DatesUtils.DATE_FORMAT_ddMMYYYY.format(peopleMisc.getRealtyDate()) : "",
                    "cellphone", (phone == null) ? "" : phone.getValue(),
                    "email", (email == null) ? "" : email.getValue(),
                    "creditSum", CalcUtils.dformat_XX.format(credit.getSumMainRemain()),
                    "sumString", MoneyString.num2str(credit.getSumMainRemain()),
                    "creditPercent", CalcUtils.dformat_XX.format(credit.getCreditSumBack()-credit.getSumMainRemain()),
                    "credit_Percentstring", MoneyString.num2str(credit.getCreditSumBack()-credit.getSumMainRemain()),
                    "creditDataend", DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditDataEnd()),
                    "fullSumm", CalcUtils.dformat_XX.format(credit.getCreditSumBack()),
                    "full_Summstring", MoneyString.num2str(credit.getCreditSumBack()),
                    "smscode", smsCode

            ));
        } catch (Exception e) {
            logger.severe("Новая оферта не создана по причине " + e.getMessage());
            throw new KassaException("Новая оферта не создана по причине " + e, e);
        }
        return rgen.getAsString();
    }
    
    @Override
    public String generateAgreement(PeopleMain people, Prolong prolong, Double sumpercent, Date creditDataend, Date dateSign) throws KassaException {
        ReportGenerated rgen = null;

        try {
            String smsCode = (StringUtils.isEmpty(prolong.getSmsCode())) ? "" : Convertor.toDigest(prolong.getSmsCode());

            rgen = reportBean.generateReport(prolong.getCredit().getProduct().getId(), Report.OFERTA_PROLONG_ID, Utils.<String, Object>mapOf(
                    "datelong", Convertor.toWriteDate(prolong.getLongDate()),
                    "fio", people.getPeoplePersonalActive().getFullName(),
                    "seriesdoc", people.getActivePassport().getSeries(),
                    "numberdoc", people.getActivePassport().getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getActivePassport().getDocDate()),
                    "orgdoc", people.getActivePassport().getDocOrg(),
                    "addrreg", people.getRegisterAddress().getDescription(),
                    "datreg", people.getPeopleMiscActive().getRealtyDate() != null ? DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getPeopleMiscActive().getRealtyDate()) : "",
                    "cellphone", (people.getCellPhone() == null) ? "" : people.getCellPhone().getValue(),
                    "email", (people.getEmail() == null) ? "" : people.getEmail().getValue(),
                    "creditdataendnew", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditDataend, prolong.getLongdays())),
                    "creditnumberoferta", prolong.getUniqueNomer(),
                    "creditnumber", prolong.getUniqueNomer().substring(0, prolong.getUniqueNomer().indexOf("-")),
                    "smscode", smsCode,
                    "mainSumm", CalcUtils.dformat_XX.format(prolong.getLongamount()-sumpercent),
                    "sumString", MoneyString.num2str(prolong.getLongamount()-sumpercent),
                    "creditsumbacknew", CalcUtils.dformat_XX.format(prolong.getLongamount()),
                    "sumpercent", CalcUtils.dformat_XX.format(sumpercent),
                    "datesign", DatesUtils.DATE_FORMAT_ddMMYYYY.format(dateSign),
                    "days", prolong.getLongdays(),
                    "newd", prolong.getLongdays()+prolong.getCredit().getCreditDays()+prolong.getCredit().getOverdueInDays(),
                    "newfull", MoneyString.num2str(false,Convertor.toDouble(prolong.getLongdays()+prolong.getCredit().getCreditDays())),
                    "addrfact", (people.getResidentAddress()!=null?people.getResidentAddress().getDescription():people.getRegisterAddress().getDescription()),
                    "berthdate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getPeoplePersonalActive().getBirthDate()),
                    "bethplace", people.getPeoplePersonalActive().getBirthPlace(),
                    "dockp", people.getActivePassport().getDocOrgCode()
            ));

        } catch (Exception e) {
            logger.severe("Не удалось сгенерировать оферту по причине " + e.getMessage());
            throw new KassaException("Не удалось сгенерировать оферту по причине ", e);
        }
        return rgen.getAsString();
    }

    @Override
    public String generateAgreement(PeopleMain people, Refinance refinance, Double sum, Date dateSign,
                                    Date refinanceDate) throws KassaException {
        ReportGenerated rgen = null;

        try {
            String smsCode = (StringUtils.isEmpty(refinance.getSmsCode())) ? "" : Convertor.toDigest(refinance.getSmsCode());
            Double sumBack = sum + CalcUtils.roundFloor(CalcUtils.calcSumPercentSimple(sum, refinance.getRefinanceDays(),
                    false, refinance.getRefinanceStake()), 0);
            rgen = reportBean.generateReport(refinance.getCredit().getProduct().getId(), Report.OFERTA_REFINANCE_ID, Utils.<String, Object>mapOf(
                    "dateref", Convertor.toWriteDate(refinance.getRefinanceDate()),
                    "fio", people.getPeoplePersonalActive().getFullName(),
                    "seriesdoc", people.getActivePassport().getSeries(),
                    "numberdoc", people.getActivePassport().getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getActivePassport().getDocDate()),
                    "orgdoc", people.getActivePassport().getDocOrg(),
                    "addrreg", people.getRegisterAddress().getDescription(),
                    "datreg", people.getPeopleMiscActive().getRealtyDate() != null ? DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getPeopleMiscActive().getRealtyDate()) : "",
                    "cellphone", (people.getCellPhone() == null) ? "" : people.getCellPhone().getValue(),
                    "email", (people.getEmail() == null) ? "" : people.getEmail().getValue(),
                    "creditdataendnew", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(refinance.getRefinanceDate(), refinance.getRefinanceDays())),
                    "creditnumberoferta", refinance.getUniqueNomer(),
                    "creditnumber", refinance.getUniqueNomer().substring(0, refinance.getUniqueNomer().indexOf("-")),
                    "smscode", smsCode,
                    "daysref", refinance.getRefinanceDays().toString(),
                    "paysum", CalcUtils.dformat_XX.format(refinance.getRefinanceAmount()),
                    "creditsum", CalcUtils.dformat_XX.format(sum),
                    "creditsumback", CalcUtils.dformat_XX.format(sumBack),
                    "percentref", CalcUtils.pskToString(refinance.getRefinanceStake() * 100),
                    "datesign", DatesUtils.DATE_FORMAT_ddMMYYYY.format(dateSign),
                    "dateref", DatesUtils.DATE_FORMAT_ddMMYYYY.format(refinance.getRefinanceDate())

            ));

        } catch (Exception e) {
            logger.severe("Не удалось сгенерировать оферту по причине " + e.getMessage());
            throw new KassaException("Не удалось сгенерировать оферту по причине ", e);
        }
        return rgen.getAsString();
    }
    @Override
    public String generateAgreementWithoutData(Credit credit,String smsCodeOferta) throws KassaException {
        PeopleMain people = credit.getPeopleMain();
       
        ReportGenerated rgen = null;
        try {
          
            //годовая ставка
            Double percent = CalcUtils.calcYearStake(credit.getCreditPercent(),credit.getCreditDataBeg());
            //ставка процентов в день
            Double stake = credit.getCreditPercent();
            String smsCode = (StringUtils.isEmpty(smsCodeOferta)) ? "" : Convertor.toDigest(smsCodeOferta);
            PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(peopleDAO.getPeopleMainEntity(people.getId()));
            DocumentEntity passport = peopleBean.findPassportActive(peopleDAO.getPeopleMainEntity(people.getId()));
            AddressEntity address = peopleBean.findAddressActive(peopleDAO.getPeopleMainEntity(people.getId()), BaseAddress.REGISTER_ADDRESS);
            PeopleContactEntity phone = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, people.getId());
            PeopleContactEntity email = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, people.getId());
            if (peoplePersonal==null||passport==null||address==null){
            	logger.severe("Новая оферта не может быть создана по причине недостаточных данных");
            	return new String("Новая оферта не может быть создана по причине недостаточных данных");
            }
            rgen = reportBean.generateReport(credit.getProduct().getId(), Report.OFERTA_ID, Utils.<String, Object>mapOf(
                    "datesign", Convertor.toWriteDate(credit.getCreditDataBeg()),
                    "creditnumber", credit.getCreditAccount(),
                    "year_percent", CalcUtils.pskToString(percent),
                    "stake", CalcUtils.pskToString(stake*100),
                    "percent_string", DigitsToString.printString(new BigDecimal(percent)),
                    "fio", peoplePersonal.getFullName(),
                    "numberdoc", passport.getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()),
                    "orgdoc", passport.getDocorg(),
                    "addrreg", address.getDescription(),
                    "cellphone", (phone == null) ? "" : phone.getValue(),
                    "email", (email == null) ? "" : email.getValue(),
                    "creditSum", CalcUtils.dformat_XX.format(credit.getSumMainRemain()),
                    "sumString", MoneyString.num2str(credit.getSumMainRemain()),
                    "creditPercent", CalcUtils.dformat_XX.format(credit.getCreditSumBack()-credit.getSumMainRemain()),
                    "credit_Percentstring", MoneyString.num2str(credit.getCreditSumBack()-credit.getSumMainRemain()),
                    "creditDataend", DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditDataEnd()),
                    "fullSumm", CalcUtils.dformat_XX.format(credit.getCreditSumBack()),
                    "full_Summstring", MoneyString.num2str(credit.getCreditSumBack()),
                    "smscode", smsCode

            ));
        } catch (Exception e) {
            logger.severe("Новая оферта не создана по причине " + e.getMessage());
            throw new KassaException("Новая оферта не создана по причине " + e, e);
        }
        return rgen.getAsString();
    }
    
    @Override
    public String generateAgreementWithoutData(PeopleMain people, Prolong prolong, Double sumpercent, Date creditDataend, Date dateSign) throws KassaException {
        ReportGenerated rgen = null;

        try {
            String smsCode = (StringUtils.isEmpty(prolong.getSmsCode())) ? "" : Convertor.toDigest(prolong.getSmsCode());

            rgen = reportBean.generateReport(prolong.getCredit().getProduct().getId(), Report.OFERTA_PROLONG_ID, Utils.<String, Object>mapOf(
                    "datelong", Convertor.toWriteDate(prolong.getLongDate()),
                    "fio", people.getPeoplePersonalActive().getFullName(),
                    "seriesdoc", people.getActivePassport().getSeries(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getActivePassport().getDocDate()),
                    "orgdoc", people.getActivePassport().getDocOrg(),
                    "addrreg", people.getRegisterAddress().getDescription(),
                    "cellphone", (people.getCellPhone() == null) ? "" : people.getCellPhone().getValue(),
                    "email", (people.getEmail() == null) ? "" : people.getEmail().getValue(),
                    "creditdataendnew", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditDataend, prolong.getLongdays())),
                    "creditnumberoferta", prolong.getUniqueNomer(),
                    "creditnumber", prolong.getUniqueNomer().substring(0, prolong.getUniqueNomer().indexOf("-")),
                    "smscode", smsCode,
                    "mainSumm", CalcUtils.dformat_XX.format(prolong.getLongamount()-sumpercent),
                    "sumString", MoneyString.num2str(prolong.getLongamount()-sumpercent),
                    "creditsumbacknew", CalcUtils.dformat_XX.format(prolong.getLongamount()),
                    "sumpercent", CalcUtils.dformat_XX.format(sumpercent),
                    "datesign", DatesUtils.DATE_FORMAT_ddMMYYYY.format(dateSign),
                    "days", prolong.getLongdays(),
                    "newd", prolong.getLongdays()+prolong.getCredit().getCreditDays()+prolong.getCredit().getOverdueInDays(),
                    "newfull", MoneyString.num2str(false,Convertor.toDouble(prolong.getLongdays()+prolong.getCredit().getCreditDays())),
                    "addrfact", (people.getResidentAddress()!=null?people.getResidentAddress().getDescription():people.getRegisterAddress().getDescription()),
                    "berthdate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getPeoplePersonalActive().getBirthDate())
            ));

        } catch (Exception e) {
            logger.severe("Не удалось сгенерировать оферту по причине " + e.getMessage());
            throw new KassaException("Не удалось сгенерировать оферту по причине ", e);
        }
        return rgen.getAsString();
    }

    @Override
    public String generateAgreementWithoutData(PeopleMain people, Refinance refinance, Double sum, Date dateSign,
                                    Date refinanceDate) throws KassaException {
        ReportGenerated rgen = null;

        try {
            String smsCode = (StringUtils.isEmpty(refinance.getSmsCode())) ? "" : Convertor.toDigest(refinance.getSmsCode());
            Double sumBack = sum + CalcUtils.roundFloor(CalcUtils.calcSumPercentSimple(sum, refinance.getRefinanceDays(),
                    false, refinance.getRefinanceStake()), 0);
            rgen = reportBean.generateReport(refinance.getCredit().getProduct().getId(), Report.OFERTA_REFINANCE_ID, Utils.<String, Object>mapOf(
                    "dateref", Convertor.toWriteDate(refinance.getRefinanceDate()),
                    "fio", people.getPeoplePersonalActive().getFullName(),
                    "numberdoc", people.getActivePassport().getNumber(),
                    "datedoc", DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getActivePassport().getDocDate()),
                    "orgdoc", people.getActivePassport().getDocOrg(),
                    "addrreg", people.getRegisterAddress().getDescription(),
                    "cellphone", (people.getCellPhone() == null) ? "" : people.getCellPhone().getValue(),
                    "email", (people.getEmail() == null) ? "" : people.getEmail().getValue(),
                    "creditdataendnew", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(refinance.getRefinanceDate(), refinance.getRefinanceDays())),
                    "creditnumberoferta", refinance.getUniqueNomer(),
                    "creditnumber", refinance.getUniqueNomer().substring(0, refinance.getUniqueNomer().indexOf("-")),
                    "smscode", smsCode,
                    "daysref", refinance.getRefinanceDays().toString(),
                    "paysum", CalcUtils.dformat_XX.format(refinance.getRefinanceAmount()),
                    "creditsum", CalcUtils.dformat_XX.format(sum),
                    "creditsumback", CalcUtils.dformat_XX.format(sumBack),
                    "percentref", CalcUtils.pskToString(refinance.getRefinanceStake() * 100),
                    "datesign", DatesUtils.DATE_FORMAT_ddMMYYYY.format(dateSign),
                    "dateref", DatesUtils.DATE_FORMAT_ddMMYYYY.format(refinance.getRefinanceDate())

            ));

        } catch (Exception e) {
            logger.severe("Не удалось сгенерировать оферту по причине " + e.getMessage());
            throw new KassaException("Не удалось сгенерировать оферту по причине ", e);
        }
        return rgen.getAsString();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest initCreditRequestForPeople(PeopleMain people, Date dateCredit, Map<String, Object> limits) throws KassaException {

        //инициализируем данные для новой заявки
        CreditRequest cr = initCreditRequestForPeople(people, dateCredit, (Double) limits.get(ProductKeys.CREDIT_SUM_MIN), (Integer) limits.get(ProductKeys.CREDIT_DAYS_MIN), limits);
        return cr;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequest initCreditRequestForPeople(PeopleMain people, Date dateCredit, Double sumCredit, Integer daysCredit, Map<String, Object> limits) throws KassaException {

        //инициализируем данные для новой заявки
        CreditRequest cr = new CreditRequest(new CreditRequestEntity());
        cr.setPeopleMain(people);
        cr.setStake((Double) limits.get(ProductKeys.CREDIT_STAKE_DEFAULT));
        cr.setCreditSum(sumCredit);
        cr.setCreditDays(daysCredit);
        cr.setCreditDaysInitial(daysCredit);
        cr.setCreditSumInitial(sumCredit);
        cr.setDateContest(dateCredit);
        cr.setContestAsp(false);
        cr.setContestPd(false);
        cr.setContestCb(false);
        cr.setContest(false);
        cr.setConfirmed(false);
        cr.setIsUploaded(false);
        cr.setStatus(new CreditStatus(refBooks.getCreditRequestStatus(CreditStatus.IN_PROCESS)));
        return cr;
    }

    @Override
    public int findManWithPrivateCabinet(PeopleMainEntity ppl) {

        UsersEntity user = userBean.findUserByPeopleId(ppl.getId());

        if (user != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public Map<String, Object> paramsStep7(Integer creditRequestId) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> limits = null;
        if (cr.getProductId() != null) {
            limits = productBean.getNewRequestProductConfig(cr.getProductId().getId());
        } else {
            ProductsEntity product = productDAO.getProductDefault();
            if (product != null) {
                limits = productBean.getNewRequestProductConfig(product.getId());
            } else {
                logger.severe("Не найден продукт по умолчанию ");
            }
        }

        Double stakeMin = (Double) limits.get(ProductKeys.CREDIT_STAKE_MIN);
        Double stakeMax = (Double) limits.get(ProductKeys.CREDIT_STAKE_MAX);
        Integer addDay=Convertor.toInteger(limits.get(ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE));
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", creditRequestId);
        mp.put("creditsum", null);
        mp.put("creditdays", null);
        mp.put("cellphone", "");
        mp.put("agreement", "");
        mp.put("stake", null);
        mp.put("stakeMin", stakeMin);
        mp.put("stakeMax", stakeMax);
        mp.put("addDay", addDay);
        mp.put("creditDaysMin", limits.get(ProductKeys.CREDIT_DAYS_MIN));
        mp.put("creditDaysMax", limits.get(ProductKeys.CREDIT_DAYS_MAX));
        mp.put("creditSumMin", limits.get(ProductKeys.CREDIT_SUM_MIN));
        mp.put("creditSumMax", limits.get(ProductKeys.CREDIT_SUM_MAX_UNKNOWN));

        //если заявка есть
        if (cr != null) {
            mp.put("creditsum", cr.getCreditsum());
            mp.put("creditdays", cr.getCreditdays());
            mp.put("stake", cr.getStake());

            PeopleMainEntity ppl = cr.getPeopleMainId();
            if (ppl != null) {
                //mobile phone
                String phone = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, ppl.getId());
                if (StringUtils.isNotEmpty(phone)) {
                    mp.put("cellphone", phone);
                }
            }

        }
        return mp;
    }

    @Override
    public Map<String, Object> paramsStep8(Integer creditRequestId) {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);

        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", creditRequestId);
        mp.put("surname", "");
        mp.put("name", "");
        mp.put("midname", "");
        mp.put("gender", null);
        mp.put("tempLink", "");

        //если заявка есть
        if (cr != null) {
            PeopleMainEntity ppl = cr.getPeopleMainId();
            if (ppl != null) {
                //выбираем пд человека
                PeoplePersonalEntity pper = peopleBean.findPeoplePersonalActive(ppl);
                if (pper != null) {
                    mp.put("surname", pper.getSurname());
                    mp.put("name", pper.getName());
                    mp.put("midname", pper.getMidname());
                    mp.put("gender", pper.getGender().getCodeinteger());
                }

                //выбираем временный хэш
                UsersEntity usr = userBean.findUserByPeopleId(ppl.getId());
                if (usr != null) {
                    mp.put("templink", usr.getTempLink());
                }
            }
        }
        return mp;
    }

    @Override
    public Map<String, String> getSocNetworkApp() {

        Partner vk = refBooks.getPartner(Partner.VKONTAKTE);
        Partner odk = refBooks.getPartner(Partner.ODNOKLASSNIKI);
        Partner mm = refBooks.getPartner(Partner.MOIMIR);
        Partner fb = refBooks.getPartner(Partner.FACEBOOK);
        Partner tt = refBooks.getPartner(Partner.TWITTER);
        //константы для соц.сетей
        Map<String, Object> params = rulesBean.getSocNetworkConstants();
        String socUrl = params.get(SettingsKeys.SOCNETWORK_URL).toString();

        Map<String, String> mp = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(socUrl)) {
            mp.put("callBackUrl", socUrl);
        }
        mp.put("vk", vk.getApplicationId());
        mp.put("vkpublic", vk.getCodeWork());
        mp.put("vksecret", vk.getPasswordWork());
        mp.put("odk", odk.getApplicationId());
        mp.put("odkpublic", odk.getCodeWork());
        mp.put("odksecret", odk.getPasswordWork());
        mp.put("mm", mm.getApplicationId());
        mp.put("mmpublic", mm.getCodeWork());
        mp.put("mmsecret", mm.getPasswordWork());
        mp.put("fb", fb.getApplicationId());
        mp.put("fbpublic", fb.getCodeWork());
        mp.put("fbsecret", fb.getPasswordWork());
        mp.put("tt", tt.getApplicationId());
        mp.put("ttpublic", tt.getCodeWork());
        mp.put("ttsecret", tt.getPasswordWork());
        return mp;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveSocialId(Integer creditRequestId, String userId, Integer socNetwork) throws KassaException {
        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(creditRequestId);
        PeopleMainEntity people = null;
        if (ccRequest != null)
            people = ccRequest.getPeopleMainId();
        peopleBean.setPeopleContactClient(people, socNetwork, userId, false);

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getLink(String username, String password) throws KassaException {
        Users usr = userBean.findUserByLoginAndPassword(username.toLowerCase(), password);
        if (usr != null) {
            try {
                usr.setTempLink(Convertor.toDigest(mailBean.generateCodeForSending()));
                userBean.saveUsers(usr);
                return "/client/faces/views/login.xhtml?tempLink=" + usr.getTempLink();
            } catch (Exception e) {
                logger.severe("Не удалось получить редирект в ЛК " + e.getMessage());
                throw new KassaException("Не удалось получить редирект в ЛК " + e.getMessage());

            }
        }
        return null;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveClientRefuse(Integer creditRequestId, Integer userId) throws KassaException {

        CreditRequestEntity cr = crDAO.getCreditRequestEntity(creditRequestId);
        if (cr == null) {
            logger.severe("Не найдена кредитная заявка с id " + creditRequestId);
            return;
        }

        if (cr.getAcceptedcreditId() != null) {
            // ставим кредиту статус клиент не забрал деньги и помечаем платеж как удаленный, но не удаляем
            creditBean.rejectCredit(cr.getAcceptedcreditId().getId());
        }
        cr = null;

        cr = crDAO.getCreditRequestEntity(creditRequestId);
        cr.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.CLIENT_REFUSE));
        cr.setDateStatus(new Date());
        cr = crDAO.saveCreditRequest(cr);

        //пишем лог
        eventLog.saveLog(EventType.INFO, EventCode.CLIENT_REFUSE, "Клиент отказался от займа",
                userId, new Date(), cr, cr.getPeopleMainId(), null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ProlongEntity saveLongRequestNew(Integer creditId, Prolong pl, Date dt, String ipAddr, String userAgent) throws KassaException {

        CreditEntity cred = creditDAO.getCreditEntity(creditId);
        ProlongEntity plong = null;
        if (cred != null) {
            // если кредит точно не закрыт
            if (!cred.getIsover()) {

                //записываем продление, пока черновик
                plong = new ProlongEntity();
                plong.setCreditId(cred);
                plong.setLongdate(dt);
                plong.setSmsCode(pl.getSmsCode());
                plong.setLongdays(pl.getLongdays());
                plong.setLongstake(pl.getLongstake());
                plong.setAgreement(pl.getAgreement());
                plong.setLongamount(pl.getLongamount());
                plong.setUniquenomer(pl.getUniqueNomer());
                plong.setIsactive(ActiveStatus.DRAFT);
                emMicro.persist(plong);

            }

        }

        //пишем лог
        String geo = "";
        try {
            //урл для определения местоположения по ip
            Map<String, Object> params = rulesBean.getSiteConstants();
            String geoSite = params.get(SettingsKeys.GEO_URL).toString();
            if (StringUtils.isNotEmpty(geoSite)) {
                Document geoDoc = GeoUtils.geoDescriptionDoc(geoSite.trim() + ipAddr);
                geo = GeoUtils.geoDescription(geoDoc);
            }
        } catch (Exception e) {
            logger.info("Не удалось определить адрес");
        }
        eventLog.saveLog(ipAddr, eventLog.getEventTypeEntity(EventType.INFO), eventLog.getEventCodeEntity(EventCode.LONG_MONEY_REQUEST),
                "сделан запрос на продление", cred.getCreditRequestId(), cred.getPeopleMainId(), null, cred,
                HTTPUtils.getShortBrowserName(userAgent), Convertor.toLimitString(userAgent, 200),
                Convertor.toLimitString(HTTPUtils.getUserOS(userAgent), 50), geo);

        return plong;

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void startProlong(Integer creditId) throws KassaException {
        CreditEntity cred = creditDAO.getCreditEntity(creditId);
        if (cred != null) {
            //ищем черновик продления
            ProlongEntity pl = creditBean.findProlongDraft(creditId);

            if (pl != null) {

                //поставили продлению статус активно
                pl.setIsactive(ActiveStatus.ACTIVE);
                pl=prolongDAO.saveProlongEntity(pl);

                //ставим id продления в оферту
                OfficialDocumentsEntity document = officialDocumentsDAO.findOfficialDocumentCreditDraft(pl.getCreditId().getPeopleMainId().getId(),
                        creditId, OfficialDocuments.OFERTA_PROLONG);
                if (document != null) {
                    document.setIsActive(ActiveStatus.ACTIVE);
                    document.setAnotherId(pl.getId());
                    emMicro.merge(document);
                }
                //меняем график выплат
                paymentService.closePaymentSchedule(cred, ReasonEnd.PROLONG, pl.getLongdate());

                Date dte = cred.getCreditdataend();
                //если делаем продление из просрочки
            
                if (cred.getCreditStatusId().getCodeinteger()!=BaseCredit.CREDIT_ACTIVE) {
                   
                	cred.setCreditStatusId(refBooks.getCreditStatusType(BaseCredit.CREDIT_ACTIVE).getEntity());
                    dte = pl.getLongdate();
                    //поставим закрытую просрочку
                    if (cred.getMaxDelay()!=null){
                      if (cred.getMaxDelay()<30){
                    	 cred.setDelay30(Utils.defaultIntegerFromNull(cred.getDelay30())+1); 
                      } else if (cred.getMaxDelay()>=30&&cred.getMaxDelay()<60){
                     	 cred.setDelay60(Utils.defaultIntegerFromNull(cred.getDelay60())+1); 
                      } else if (cred.getMaxDelay()>=60&&cred.getMaxDelay()<90){
                      	 cred.setDelay90(Utils.defaultIntegerFromNull(cred.getDelay90())+1); 
                      } else if (cred.getMaxDelay()>=90){
                      	 cred.setDelaymore(Utils.defaultIntegerFromNull(cred.getDelaymore())+1); 
                      }
                    }
                    cred.setMaxDelay(null);
                    
                    cred.setCurrentOverdueDebt(null);
                    cred.setOverduestateId(refBooks.getCreditOverdueType(BaseCredit.WITHOUT_OVERDUE).getEntity());
                    CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(cred.getCreditRequestId().getId());
                    if (creditRequest!=null){
                    	creditRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.DECISION));
                    	crDAO.saveCreditRequest(creditRequest);
                    }
                    //удалим из коллектора
                    collectorDao.changeCollectorStatus(cred.getId(), cred.getPeopleMainId().getId(), ActiveStatus.ARCHIVE);
                    // убираем активные задачи у коллектора по этому кредиту
                    collectorDao.removeTasksByCreditID(cred.getId());
                    //запишем в график
                    paymentService.savePaymentSchedule(cred, pl.getLongstake(), pl.getLongdays(), pl.getLongamount(), pl.getLongdate(), cred.getSumMainRemain());
                } else {
                    //запишем в график
                    paymentService.savePaymentSchedule(cred, pl.getLongstake(), DatesUtils.daysDiff(cred.getCreditdataend(), cred.getDateMainRemain()) + pl.getLongdays(), pl.getLongamount(), pl.getLongdate(), cred.getSumMainRemain());
                }

                //меняем дату окончания и сумму кредита, ставим кол-во продлений
                if (cred.getCreditlong() == null || cred.getCreditlong() == 0) {
                    cred.setCreditlong(1);
                } else {
                    cred.setCreditlong(cred.getCreditlong() + 1);
                }
                cred.setCreditdataend(DateUtils.addDays(dte, pl.getLongdays()));

                cred.setDateStatus(pl.getLongdate());
                
                cred=creditDAO.saveCreditEntity(cred);

                Credit credit = creditDAO.getCredit(cred.getId(), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
                Map<String, Object> res = creditCalc.calcCredit(credit, cred.getCreditdataend());
                Double sumBack = (Double) res.get(CreditCalculatorBeanLocal.SUM_BACK);
                logger.info("Sumback "+sumBack);
                Double sumPercent = (Double) res.get(CreditCalculatorBeanLocal.SUM_PERCENT);
                logger.info("Sumpercent "+sumPercent);
                cred.setCreditsumback(cred.getSumMainRemain()+sumPercent);
                pl.setLongamount(cred.getCreditsumback());
                pl=prolongDAO.saveProlongEntity(pl);
                
                cred.setChangedSum(0);
                cred=creditDAO.saveCreditEntity(cred);

                //запишем детали
                creditBean.saveCreditDetail(cred, BaseCredit.OPERATION_PROLONG, pl.getLongdate(), cred.getCreditdataend(), cred.getSumMainRemain(),
                        null, cred.getCreditsumback(), null, null, pl.getId());

                //записали лог - подписание оферты
                eventLog.saveLog(EventType.INFO, EventCode.OFERTA_SIGNED, "Была подписан оферта на продление",
                        null, pl.getLongdate(), crDAO.getCreditRequestEntity(cred.getCreditRequestId().getId()),
                        peopleDAO.getPeopleMainEntity(cred.getPeopleMainId().getId()), null);

                //пишем лог - продление
                eventLog.saveLog(EventType.INFO, EventCode.LONG_MONEY, "Было сделано продление",
                        null, new Date(), crDAO.getCreditRequestEntity(cred.getCreditRequestId().getId()),
                        peopleDAO.getPeopleMainEntity(cred.getPeopleMainId().getId()), cred);

            }
        }

    }

    @Override
    public CreditRequest findCreditRequestByUniqueNumber(final String uniqueNumber) {
        List<CreditRequestEntity> creditRequests = emMicro.createQuery("from CreditRequestEntity where uniquenomer = :uniqueNumber")
                .setParameter("uniqueNumber", uniqueNumber)
                .getResultList();

        return creditRequests.size() > 0 ? new CreditRequest(creditRequests.get(0)) : null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveMiscVariables(String className, int objectId, Map<String, Object> vars) {
        Query qry = emMicro.createNamedQuery("deleteMiscVars");
        qry.setParameter("className", className);
        qry.setParameter("objectId", String.valueOf(objectId));

        List<String> prm = new ArrayList<String>(vars.size());
        prm.addAll(vars.keySet());
        qry.setParameter("names", prm);
        qry.executeUpdate();

        // добавляем
        for (String varName : prm) {
            Object value = vars.get(varName);
            if (value == null) {
                continue;
            }

            Misc misc = new Misc();
            misc.setClassName(className);
            misc.setClassId(String.valueOf(objectId));
            misc.setName(varName);
            if (value instanceof String) {
                misc.setFormat("C");
            } else if (value instanceof Double) {
                misc.setFormat("F");
            } else if (value instanceof Number) {
                misc.setFormat("N");
            } else if (value instanceof Date) {
                misc.setFormat("D");
            }
            misc.setValue(value.toString());
            emMicro.persist(misc);
        }
    }

    @Override
    public List<CreditRequest> findCreditRequestsForCollector(Date date,
                                                              DateRange dateRange, Integer creditRequestId) {
        String hql = "from ru.simplgroupp.persistence.CreditRequestEntity where (statusId.id = :status)";
        if (creditRequestId != null && creditRequestId > 0) {
            hql = hql + " and id=:creditRequestId ";
        }
        if (date != null) {
            hql = hql + " and date_part('day',dateStatus)=:day and date_part('month',dateStatus)=:month and date_part('year',dateStatus)=:year ";
        }
        if (dateRange != null && dateRange.getFrom() != null) {
            hql = hql + " and (dateStatus >= :dateFrom)";
        }
        if (dateRange != null && dateRange.getTo() != null) {
            hql = hql + " and (dateStatus < :dateTo)";
        }
        Query qry = emMicro.createQuery(hql);
        if (creditRequestId != null && creditRequestId > 0) {
            qry.setParameter("creditRequestId", creditRequestId);
        }
        if (date != null) {
            qry.setParameter("day", DatesUtils.getDay(date));
            qry.setParameter("month", DatesUtils.getMonth(date));
            qry.setParameter("year", DatesUtils.getYear(date));
        }
        if ((dateRange != null) && (dateRange.getFrom() != null)) {
            qry.setParameter("dateFrom", dateRange.getFrom(), TemporalType.DATE);
        }
        if ((dateRange != null) && (dateRange.getTo() != null)) {
            qry.setParameter("dateTo", DateUtils.addDays(dateRange.getTo(), 1), TemporalType.DATE);
        }
        qry.setParameter("status", CreditStatus.FOR_COLLECTOR);
        List<CreditRequestEntity> lst = qry.getResultList();
        if (lst.size() > 0) {
            List<CreditRequest> lst1 = new ArrayList<CreditRequest>(lst.size());
            for (CreditRequestEntity cre : lst) {
                CreditRequest cr = new CreditRequest(cre);
                lst1.add(cr);
            }
            return lst1;
        } else {
            return new ArrayList<CreditRequest>(0);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CreditRequestEntity saveCreditRequestWithAccount(CreditRequest creditRequest)
            throws KassaException {
        CreditRequestEntity entity = crDAO.getCreditRequestEntity(creditRequest.getId());
        if (entity != null) {
            AccountEntity account = accDAO.getAccountEntity(creditRequest.getAccount().getId());
            if (account != null) {
                entity.setAccountId(account);
            }
            emMicro.persist(entity);
            return entity;
        }

        return null;
    }

    @Override
    public String execCopyToCsv(String where, String table, String header, String columns) {
       return execCopyToCsv(where,table,header,columns,"|");
    }

    @Override
    public String execCopyToCsv(String where, String table, String header, 
    		String columns,String delimiter) {

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(header)) {
            sb.append(header);
            sb.append("\r\n");
        }

        List<Object[]> lst = sqlDAO.selectSingle(table, where, columns);
        //если найдены записи по условиям, пытаемся их записать в csv
        if (lst.size() > 0) {

            for (Object[] obj : lst) {
                String str = "";
                for (int i = 0; i < obj.length; i++) {

                    if (obj[i] != null) {
                        String s1 = obj[i].toString();
                        str += s1 + delimiter;

                    } else {
                        str += delimiter;
                    }

                }
                sb.append(str);
                sb.append("\r\n");

            }
        }
        return sb.toString();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void newDeviceInfo(Integer creditRequestId, Integer resolutionW,
                              Integer resolutionH, String userAgent) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        DeviceInfoEntity deviceInfo = new DeviceInfoEntity();
        deviceInfo.setCreditRequestId(creditRequest);
        deviceInfo.setResolutionHeight(resolutionH);
        deviceInfo.setResolutionWidth(resolutionW);
        deviceInfo.setDevicePlatform(HTTPUtils.getUserOS(userAgent));
        deviceInfo.setUserAgent(HTTPUtils.getShortBrowserName(userAgent));
        deviceInfo.setInfoDate(new Date());

        emMicro.persist(deviceInfo);

    }

    @Override
    public DeviceInfoEntity findDeviceInfo(Integer creditRequestId) {
        return (DeviceInfoEntity) JPAUtils.getSingleResult(emMicro, "findDeviceinfo", Utils.mapOf(
                "creditRequestId", creditRequestId
        ));
    }

    @Override
    public List<Misc> findMiscVarsByClassAndId(String className, Integer id) {
        return JPAUtils.getResultList(emMicro, "findMiscVarsByClassAndId", Utils.mapOf(
                "className", className,
                "classId", id.toString()
        ));
    }

    @Override
    public List<Misc> findMiscVarsCreditRequest(Integer creditRequestId) {
        return findMiscVarsByClassAndId("ru.simplgroupp.transfer.CreditRequest", creditRequestId);
    }


}


 

