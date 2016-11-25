package ru.simplgroupp.ejb.service.impl;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.DocumentOtherDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.CreditRequestWizardService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;
import ru.simplgroupp.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Имплементация {@link CreditRequestWizardService}
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CreditRequestWizardServiceImpl implements CreditRequestWizardService {

	@Inject Logger logger;

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    KassaBeanLocal kassaBean;

    @EJB
    RulesBeanLocal rulesBean;

    @EJB
    CreditCalculatorBeanLocal creditCalc;

    @EJB
    PeopleBeanLocal peopleBean;

    @EJB
    CreditBeanLocal creditBean;

    @EJB
    CreditRequestDAO crDAO;

    @EJB
    DocumentOtherDAO documentOtherDAO;

    @EJB
    ProductDAO prodDAO;

    @EJB
    ProductBeanLocal productBean;

    @EJB
    ReferenceBooksLocal referenceBooks;

    @EJB
    EventLogService eventLogService;

    @EJB
    OrganizationService orgService;

    @EJB
    PeopleDAO peopleDAO;

    @Override
    public Step0Data step0(Integer creditRequestId, boolean isFirstTime) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);

        ProductsEntity product = prodDAO.getProductDefault();
        Map<String, Object> limits = productBean.getNewRequestProductConfig(product.getId());
        Double stakeMin = (Double) limits.get(ProductKeys.CREDIT_STAKE_MIN);
        Double stakeMax = (Double) limits.get(ProductKeys.CREDIT_STAKE_MAX);
        Integer addDays = Convertor.toInteger(limits.get(ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE));
        String jsFunction = productBean.getFunctionStakeNewRequestUnknown(product.getId());
        // TODO отдавать id дефолтного продукта 
        Step0Data step0 = new Step0Data();
        step0.setStakeMin(stakeMin);
        step0.setStakeMax(stakeMax);
        step0.setAddDays(addDays);
        step0.setCreditDaysMin(Convertor.toInteger(limits.get(ProductKeys.CREDIT_DAYS_MIN)));
        step0.setCreditDaysMax(Convertor.toInteger(limits.get(ProductKeys.CREDIT_DAYS_MAX)));
        step0.setCreditSumMin((Double) limits.get(ProductKeys.CREDIT_SUM_MIN));
        step0.setCreditSumMax((Double) limits.get(ProductKeys.CREDIT_SUM_MAX_UNKNOWN));
        step0.setPercentFunction(jsFunction);

        if (creditRequest != null && creditRequest.getStatusId().getId() == CreditStatus.IN_PROCESS) {
            step0.setStake(creditRequest.getStake());
            step0.setCreditsum(creditRequest.getCreditsum());
            step0.setCreditDays(creditRequest.getCreditdays());
        } else {
            Double creditSum = step0.getCreditSumMin();
            Integer creditDays = step0.getCreditDaysMin();

            double stake = creditCalc.calcInitialStake(creditSum, creditDays, limits);

            step0.setStake(stake);
            step0.setCreditsum(creditSum);
            step0.setCreditDays(creditDays);
        }

        return step0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveStep0(Integer creditRequestId, Step0Data step0) throws KassaException {

        CreditRequestEntity creditRequest = null;

        if (creditRequestId != null) {
            creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        }

        ProductsEntity product = null;
        if (creditRequest == null) {
            // TODO поменять, продукт должен приходить в мапе
            product = prodDAO.getProductDefault();
        } else {
            product = creditRequest.getProductId();
        }
        //константы для расчета ставки
        Map<String, Object> limits = productBean.getNewRequestProductConfig(product.getId());
        //считаем ставку
        double stake = creditCalc.calcInitialStake(step0.getCreditSum(), step0.getCreditDays(), limits);

        //пишем данные заявки
        creditRequest = kassaBean.newCreditRequest(creditRequest, creditRequest != null ? creditRequest.getPeopleMainId().getId() : null,
                CreditStatus.IN_PROCESS, null, null, null, null, null, Partner.SYSTEM,
                product != null ? product.getId() : null, new Date(), null, new Date(), null, step0.getCreditSum(), step0.getCreditDays(),
                stake, null, null, null, null, null, RefCreditRequestWay.ONLINE, null);

        //ищем, был ли записан лог по данной заявке      
        EventLogEntity eventLog = eventLogService.findLog(creditRequest.getId(), creditRequest.getPeopleMainId().getId(), EventCode.NEW_CREDIT_REQUEST, null);

        //определим местоположение
        String geoPlace = kassaBean.saveGeoLocation(creditRequest, step0.getClientIp());
        //если это не Россия, дальше не пойдем
        if (StringUtils.isNotEmpty(geoPlace) && !geoPlace.toUpperCase().contains("RU") && !geoPlace.toUpperCase().contains("LOCALHOST")) {
            logger.severe("Заход из другой страны " + geoPlace);
            throw new KassaException(ErrorKeys.ANOTHER_COUNTRY, "Взять заем можно только находясь в РФ");
        }

        //если не писали в лог, то запишем
        if (eventLog == null) {
            eventLogService.saveLog(step0.getClientIp(), eventLogService.getEventTypeEntity(EventType.INFO),
                    eventLogService.getEventCodeEntity(EventCode.NEW_CREDIT_REQUEST),
                    "новая кредитная заявка на сумму " + step0.getCreditSum(), creditRequest,
                    peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId()), null, null,
                    HTTPUtils.getShortBrowserName(step0.getUserAgent()), Convertor.toLimitString(step0.getUserAgent(), 200),
                    HTTPUtils.getUserOS(Convertor.toLimitString(step0.getUserAgent(), 50)), geoPlace);
        }

        //ищем, писали ли данные об устройстве
        DeviceInfoEntity deviceInfo = kassaBean.findDeviceInfo(creditRequest.getId());
        if (deviceInfo == null) {
            // сохраняем данные об устройстве, с которого подается заявка
            kassaBean.newDeviceInfo(creditRequest.getId(), step0.getResW(), step0.getResH(), step0.getUserAgent());
        }
        return creditRequest.getId();
    }

    @Override
    public Step1Data step1(Integer creditRequestId) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);

        Step1Data step1 = new Step1Data();
        //если заявка есть
        if (creditRequest != null && creditRequest.getStatusId().getId() == CreditStatus.IN_PROCESS) {
            if (creditRequest.getConfirmed() != null) {
                step1.setConfirmed(creditRequest.getConfirmed());
            } else {
                step1.setConfirmed(false);
            }

            PeopleMainEntity peopleMain = creditRequest.getPeopleMainId();

            if (peopleMain != null) {
                //персональные данные
                PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(peopleMain);
                if (peoplePersonal != null) {
                    step1.setLastName(peoplePersonal.getSurname());
                    step1.setFirstName(peoplePersonal.getName());
                    step1.setMiddleName(peoplePersonal.getMidname());
                    step1.setBirthday(peoplePersonal.getBirthdate());
                }

                step1.setDopphone1(peopleBean.findContactClient(PeopleContact.CONTACT_DOPPHONE1, peopleMain.getId()));
                step1.setDopphone2(peopleBean.findContactClient(PeopleContact.CONTACT_DOPPHONE2, peopleMain.getId()));

                step1.setEmail(peopleBean.findContactClient(PeopleContact.CONTACT_EMAIL, peopleMain.getId()));
                step1.setPhone(peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, peopleMain.getId()));
                step1.setHasVk(peopleBean.findContactClient(PeopleContact.NETWORK_VK, peopleMain.getId()) != null);
                step1.setHasFacebook(peopleBean.findContactClient(PeopleContact.NETWORK_FB, peopleMain.getId()) != null);
                step1.setHasMoyMir(peopleBean.findContactClient(PeopleContact.NETWORK_MM, peopleMain.getId()) != null);
                step1.setHasOdnoklassniki(peopleBean.findContactClient(PeopleContact.NETWORK_OK, peopleMain.getId()) != null);
            }

        }

        return step1;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveStep1(Integer creditRequestId, Step1Data step1) throws KassaException {

        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);

        //пишем данные заявки
        creditRequest = kassaBean.newCreditRequest(creditRequest, creditRequest.getPeopleMainId().getId(),
                null, true, true, true, true, true, null,
                null, new Date(), null, creditRequest.getDateStatus(), null, creditRequest.getCreditsum(), creditRequest.getCreditdays(),
                creditRequest.getStake(), null, null, null, null, null, RefCreditRequestWay.ONLINE, null);

        PeopleMainEntity people = creditRequest.getPeopleMainId();

        int cnt;
        // ищем человека по контактам
        //почта
        if (StringUtils.isNotEmpty(step1.getEmail())) {
            PeopleContactEntity contactEmail = peopleBean.findPeopleByContactClient(PeopleContact.CONTACT_EMAIL, step1.getEmail().toLowerCase());
            if (contactEmail != null) {
                PeopleMainEntity peopleWithEmail = contactEmail.getPeopleMainId();
                cnt = kassaBean.findManWithPrivateCabinet(peopleWithEmail);
                //уже есть человек с такой почтой, дальше не пойдем
                if (cnt > 0) {
                    logger.severe("Email " + step1.getEmail() + " зарегистрирован на другого пользователя");
                    throw new KassaException(ErrorKeys.EMAIL_EXISTS, "Email " + step1.getEmail() + " зарегистрирован на другого пользователя");
                }
            }
        }

        //телефон
        PeopleContactEntity contactPhone = peopleBean.findPeopleByContactClient(PeopleContact.CONTACT_CELL_PHONE, Convertor.fromMask(step1.getPhone()));
        if (contactPhone != null) {
            PeopleMainEntity peopleWithPhone = contactPhone.getPeopleMainId();
            cnt = kassaBean.findManWithPrivateCabinet(peopleWithPhone);
            //уже есть человек с таким телефоном, дальше не пойдем
            if (cnt > 0) {
                logger.severe("Телефон " + step1.getPhone() + " зарегистрирован на другого пользователя");
                throw new KassaException(ErrorKeys.PHONE_EXISTS, "Телефон " + step1.getPhone() + " зарегистрирован на другого пользователя");
            }
        }

        // сохраняем контакты
        try {
            peopleBean.setPeopleContactClient(people, PeopleContact.CONTACT_DOPPHONE1, Convertor.fromMask(step1.getDopphone1()), false);
            peopleBean.setPeopleContactClient(people, PeopleContact.CONTACT_DOPPHONE2, Convertor.fromMask(step1.getDopphone2()), false);
        } catch (Exception ex) {
            logger.severe("Не удалось сохранить дополнительные контакты "+ex);
            throw new KassaException(ex);
        }

        // сохраняем контакты
        try {
            if(StringUtils.isNotEmpty(step1.getEmail())){
                peopleBean.setPeopleContactClient(people, PeopleContact.CONTACT_EMAIL, step1.getEmail().toLowerCase(), false);
            }
            peopleBean.setPeopleContactClient(people, PeopleContact.CONTACT_CELL_PHONE,
                    Convertor.fromMask(step1.getPhone()), false);

        } catch (Exception ex) {
            logger.severe("Не удалось сохранить контакты " + ex);
            throw new KassaException(ex);
        }

        //ищем, вдруг такой человек уже есть
        PeoplePersonalEntity existingPerson = peopleBean.findPeopleByPersonalData(step1.getLastName(), step1.getFirstName(), step1.getMiddleName(), step1.getBirthday());
        if (existingPerson != null) {
            PeopleMainEntity peopleMain = existingPerson.getPeopleMainId();
            cnt = kassaBean.findManWithPrivateCabinet(peopleMain);
            //уже есть человек с такими ФИО и ДР, дальше не пойдем
            if (cnt > 0) {
                logger.severe("Человек " + step1.getLastName() + " " + step1.getFirstName() + " " + step1.getMiddleName() + " уже существует в системе");
                throw new KassaException(ErrorKeys.PERSON_EXISTS, "Человек " + step1.getLastName() + " " + step1.getFirstName() + " " + step1.getMiddleName() + " уже существует в системе");
            }
        }

        //пишем персональные данные
        PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(people);
        try {
            peopleBean.newPeoplePersonal(peoplePersonal, people.getId(), (peoplePersonal == null || peoplePersonal.getCreditRequestId() == null) ? null : peoplePersonal.getCreditRequestId().getId(), Partner.CLIENT,
                    step1.getLastName(), step1.getFirstName(), step1.getMiddleName(), peoplePersonal == null ? null : peoplePersonal.getMaidenname(),
                    step1.getBirthday(), peoplePersonal == null ? null : peoplePersonal.getBirthplace(), (peoplePersonal == null || peoplePersonal.getGender() == null) ? null : peoplePersonal.getGender().getCodeinteger(), new Date(), ActiveStatus.ACTIVE);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Не удалось сохранить персональные данные по человеку, ошибка " + e.getMessage(),e);
        }

    }

    @Override
    public Step6Data step6(Integer creditRequestId) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);

        Step6Data step6Data = new Step6Data();


        //если заявка есть
        if (creditRequest != null) {

            PeopleMainEntity pmain = creditRequest.getPeopleMainId();

            if (pmain != null) {
                //ищем счета
                AccountEntity accEnt = creditRequest.getAccountId();
                if (accEnt != null) {
                    if (accEnt.getAccountTypeId() != null) {
                        Integer i = accEnt.getAccountTypeId().getCodeinteger();
                        step6Data.setOption(i);
                        switch (i) {
                            case Account.BANK_TYPE: {
                                step6Data.setBik(accEnt.getBik());
                                step6Data.setBankaccount(accEnt.getAccountnumber());
                                step6Data.setCorrespondentAccount(accEnt.getCorraccountnumber());
                            }
                            break;
                            case Account.CARD_TYPE: {
                                step6Data.setBik(accEnt.getBik());
                                step6Data.setBankaccount(accEnt.getAccountnumber());
                                step6Data.setCardnomer(accEnt.getCardnumber());
                                step6Data.setContest(accEnt.getContest());
                                step6Data.setOption2(accEnt.getIsWork());
                                step6Data.setCorrespondentAccount(accEnt.getCorraccountnumber());
                            }
                            break;
                            case Account.YANDEX_TYPE: {
                                step6Data.setYandexcardnomer(accEnt.getAccountnumber());
                                if (accEnt.getContest() != null) {
                                    step6Data.setAccept(accEnt.getContest() ? 1 : 0);
                                } else {
                                    step6Data.setAccept(0);
                                }
                            }
                            break;
                            case Account.QIWI_TYPE: {
                                step6Data.setQiwiPhone(accEnt.getAccountnumber());
                                if (accEnt.getContest() != null) {
                                    step6Data.setAccept(accEnt.getContest() ? 1 : 0);
                                } else {
                                    step6Data.setAccept(0);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return step6Data;
    }

    /**
     * Старая версия патого шага
     *
     * @param params
     * @return
     * @throws KassaException
     */
    private Integer saveStep5_old(Map<String, Object> params) throws KassaException {
        PartnersEntity parClient = referenceBooks.getPartnerEntity(Partner.CLIENT);

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));
        PeopleMainEntity peopleMain = ccRequest.getPeopleMainId();

        //сохраняем предыдущий кредит
        if (Convertor.toInteger(params.get("prevcredits")) > 0) {

            List<CreditEntity> lstcred = creditBean.findCredits(peopleMain, ccRequest, parClient, false, null);
            CreditEntity cred = null;
            if (lstcred.size() > 0) {
                cred = lstcred.get(0);
            }
            try {
                creditBean.newCredit(cred, peopleMain.getId(), ccRequest.getId(), Partner.CLIENT, false,
                        (String) params.get("overdue"), Convertor.toInteger(params.get("creditorganization")),
                        Convertor.toDouble(params.get("creditsumprev"), CalcUtils.dformat),
                        Convertor.toDate(params.get("creditdate"), DatesUtils.FORMAT_ddMMYYYY),
                        Utils.defaultBooleanFromIntegerNull(Convertor.toInteger(params.get("creditisover"))),
                        Convertor.toInteger(params.get("credittype")), Convertor.toInteger(params.get("currencytype")),
                        BaseCredit.CREDIT_OWNER, null, null, null, null);
            } catch (Exception e) {
                logger.severe("Не удалось сохранить кредит по заявке " + ccRequest.getId() + ", ошибка " + e);
                throw new KassaException("Не удалось сохранить кредит по заявке " + ccRequest.getId() + ", ошибка " + e);
            }

        } else {
            List<CreditEntity> lstcred = creditBean.findCredits(peopleMain, ccRequest, parClient, false, null);
            if (lstcred != null && lstcred.size() > 0) {
                CreditEntity cred = lstcred.get(0);
                creditBean.removeCredit(cred.getId());

            }
        }
        return ccRequest.getId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveStep5(Map<String, Object> params) throws KassaException {
        PartnersEntity parClient = referenceBooks.getPartnerEntity(Partner.CLIENT);

        CreditRequestEntity ccRequest = crDAO.getCreditRequestEntity(Convertor.toInteger(params.get("id")));
        PeopleMainEntity peopleMain = ccRequest.getPeopleMainId();

        List<DocumentOtherEntity> od_list = documentOtherDAO.findPeopleOtherDocuments(peopleMain.getId(), RefHeader.PENSDOC_TYPES);
        if (params.get("pens_doc") != null) {
            // сохраняем документ
            DocumentOtherEntity d = null;
            if (od_list.size() > 0) {
                d = od_list.get(0);
            }
            peopleBean.newOtherDocument(d, referenceBooks.getReferenceEntity(Convertor.toInteger(params.get("pens_doc"))),
                    peopleMain.getId(), parClient.getId(), Convertor.toString(params.get("pens_doc_number")), Convertor.toDate(params.get("pens_doc_date"), DatesUtils.FORMAT_ddMMYYYY),
                    ActiveStatus.ACTIVE);
        } else {
            for (DocumentOtherEntity d : od_list) {
                documentOtherDAO.deleteDocument(d.getId());
            }
        }

        // сохраняем цель
        try {
            ccRequest = kassaBean.newCreditRequest(ccRequest, ccRequest.getPeopleMainId().getId(),
                    null, true, true, true, true, true, null,
                    null, new Date(), null, ccRequest.getDateStatus(), null, ccRequest.getCreditsum(), ccRequest.getCreditdays(),
                    ccRequest.getStake(), null, null, null, null, Convertor.toString(params.get("creditPurpose")), RefCreditRequestWay.ONLINE, null);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Произошла ошибка при сохранении цели кредита, ошибка " + e.getMessage(), e);
        }

        //сохраняем дополнительные данные
        PeopleOtherEntity pOther = peopleBean.findPeopleOtherActive(peopleMain);
        try {
            peopleBean.newPeopleOther(pOther, peopleMain.getId(), null, Partner.CLIENT,
                    Convertor.toString(params.get("publicpeople_role")), Convertor.toString(params.get("publicpeople_relative_role")),
                    Convertor.toString(params.get("publicpeople_relative_fio")), Convertor.toString(params.get("benefic_fio")),
                    Convertor.toBoolean(params.get("reclam_accept")), Convertor.toBoolean(params.get("other")),
                    ActiveStatus.ACTIVE);
        } catch (Exception e) {
            logger.severe("Произошла ошибка при сохранении других доп.данных, ошибка " + e);
        }

        // сохраняем тип оплаты на карту
        AccountEntity account = peopleBean.findAccountActive(peopleMain, Account.CARD_TYPE);
        try {
            account = peopleBean.newAccount(account, peopleMain.getId(), new Date(), 0, true, null, Account.CARD_TYPE,
                    null, null, null, null, null, null, null, null, ActiveStatus.ACTIVE);
        } catch (Exception e) {
            logger.severe("Не удалось сохранить кредитную карту, ошибка " + e);
        }

        return ccRequest.getId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AccountEntity saveStep6(Integer creditRequestId, Step6Data step6) throws KassaException {

        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        PeopleMainEntity peopleMain = creditRequest.getPeopleMainId();

        //сохраняем счета
        AccountEntity account = peopleBean.findAccountActive(peopleMain, step6.getOption());

        switch (step6.getOption()) {
            case Account.BANK_TYPE: {

                try {
                    account = peopleBean.newAccount(account, peopleMain.getId(), new Date(),
                            null, null, step6.getBankaccount(), Account.BANK_TYPE,
                            step6.getBik(), step6.getCorrespondentAccount(), null,
                            null, null, null, null, null, ActiveStatus.ACTIVE);
                } catch (Exception e) {
                    logger.severe("Не удалось сохранить счет, ошибка " + e);
                }

           /*   AccountEntity payonlineAccount = peopleBean.findAccountActive(peopleMain, Account.PAYONLINE_CARD_TYPE);
                if (payonlineAccount != null) {
                    payonlineAccount.setIsactive(ActiveStatus.ACTIVE);
                }*/
            }

            break;
            case Account.CARD_TYPE: {

                try {
                    account = peopleBean.newAccount(account, peopleMain.getId(), new Date(),
                            Convertor.toInteger(step6.getOption2()), step6.getContest(), null, Account.CARD_TYPE,
                            null, null, step6.getCardnomer(), null, null, null, null, null, ActiveStatus.ACTIVE);
                } catch (Exception e) {
                    logger.severe("Не удалось сохранить счет, ошибка " + e);
                }
            }

            break;
            case Account.CONTACT_TYPE: {
                try {
                    account = peopleBean.newAccount(account, peopleMain.getId(), new Date(),
                            null, null, null, Account.CONTACT_TYPE,
                            null, null, null, null, null, null, null, null, ActiveStatus.ACTIVE);
                } catch (Exception e) {
                    logger.severe("Не удалось сохранить счет, ошибка " + e);
                }
            }

            break;
            case Account.YANDEX_TYPE: {
                try {
                    account = peopleBean.newAccount(account, peopleMain.getId(), new Date(),
                            null, (1 == step6.getAccept()), step6.getYandexcardnomer(), Account.YANDEX_TYPE,
                            null, null, null, null, null, null, null, null, ActiveStatus.ACTIVE);
                } catch (Exception e) {
                    logger.severe("Не удалось сохранить счет, ошибка " + e);
                }
            }
            break;

            case Account.QIWI_TYPE: {

                try {
                    account = peopleBean.newAccount(account, peopleMain.getId(), new Date(),
                            null, (1 == step6.getAccept()), step6.getQiwiPhone(), Account.QIWI_TYPE,
                            null, null, null, null, null, null, null, null, ActiveStatus.ACTIVE);
                } catch (Exception e) {
                    logger.severe("Не удалось сохранить счет, ошибка " + e);
                }
            }

            break;
        }

        creditRequest.setAccountId(account);
        emMicro.merge(creditRequest);

        return account;
    }
}
