package ru.simplgroupp.ejb.service.impl;

import org.apache.commons.lang3.StringUtils;
import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.ApplicationAction;
import ru.simplgroupp.ejb.arius.*;
import ru.simplgroupp.ejb.plugins.payment.AriusPayPluginConfig;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.payment.AriusPayBeanLocal;
import ru.simplgroupp.interfaces.service.AppServiceBeanLocal;
import ru.simplgroupp.interfaces.service.AriusService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.services.UserProvider;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.WebUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Имплементация {@link ru.simplgroupp.services.PaymentService}
 */
@Singleton
public class AriusServiceImpl implements AriusService {
    public static final String BORROWER_CREDIT_NOT_FOUND = "BORROWER_CREDIT_NOT_FOUND";
    public static final String BORROWER_ADDRESS_NOT_FOUND = "BORROWER_ADDRESS_NOT_FOUND";
    public static final String BORROWER_EXTERNAL_ID_NOT_FOUND = "BORROWER_EXTERNAL_ID_NOT_FOUND";

    private static final String prefix = AriusPayBeanLocal.SYSTEM_NAME+".";

    public static final String ARIUS_MERCHANT_KEY = prefix + "merchantKey";
    public static final String ARIUS_TEST_URL = prefix + "testUrl";
    public static final String ARIUS_WORK_URL = prefix + "workUrl";
    public static final String ARIUS_TEST_LOGIN = prefix + "testLogin";
    public static final String ARIUS_WORK_LOGIN = prefix + "workLogin";
    public static final String ARIUS_PURPOSE = prefix + "purpose";
    public static final String ARIUS_ENDPOINT_3DS = prefix + "endpoint3ds";
    public static final String ARIUS_ENDPOINT_CARDREG = prefix + "endpointcardreg";
    public static final String ARIUS_ENDPOINT_DEPOSIT_2_CARD = prefix + "endpointdeposit2card";
    public static final String ARIUS_ENDPOINT_RECURRENT = prefix + "endpointrecurrent";


    public static final String ARIUS_SERVER_CALLBACK_URL = "/main/ariuscallback/preauth";
    public static final String ARIUS_REDIRECT_URL =        "/main/ariusredirect/preauth";

    public static final String ARIUS_SALE_CALLBACK_URL =   "/main/ariuscallback/sale";
    public static final String ARIUS_SALE_REDIRECT_URL =   "/main/ariusredirect/sale";

    private static final Logger logger = Logger.getLogger(AriusServiceImpl.class.getName());


    @EJB
    CreditDAO creditDAO;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private PeopleBeanLocal peopleBean;
    

    @EJB
    private CreditBeanLocal creditBean;
    
    @EJB
    private EventLogService eventLog;

    @EJB
    AccountDAO accDAO;

    @EJB
    ProductDAO productDAO;
    
    @EJB
    CreditRequestDAO creditRequestDAO;

    @EJB
    ActionProcessorBeanLocal actProc;
    @EJB
    AppServiceBeanLocal appServ;

    @EJB
    WorkflowBeanLocal workflow;

    @EJB
    PaymentService paymentService;

    @EJB
    private UserProvider userProvider;

    @EJB
    PaymentDAO paymentDAO;


    private String ipaddress = null;

    private AriusPayPluginConfig config;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AriusPreauthFormResponse doPreauth(Integer creditRequestId) {
        AriusPreauthFormResponse response = new AriusPreauthFormResponse();
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
        refreshConfig();

        if(creditRequest == null){
            response.setVal(AriusConstants.ERROR_CODE,BORROWER_CREDIT_NOT_FOUND);
            return response;
        }
        try {
            //2 рубля блокирую
            AriusTransferFormParameters parameters = fillFormPaymentParameters(creditRequest,2d,AriusConstants.ARIUS_OPERATION_PREAUTH_FORM,config.getEndpointcardreg());
            doAriusCall(parameters,response);
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }

    @Override
    public AriusReturnResponse doReturn(String orderId) {
        AriusReturnResponse response = new AriusReturnResponse();
        refreshConfig();
        try {
            //возвращаю 2 рубля юзеру
            AriusReturnParameters parameters = fillReturnParameters(config,orderId);
            doAriusCall(parameters,response);
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }

    @Override
    public AriusCreateCardRefResponse doCreateCardRef(String orderId) {
        AriusCreateCardRefResponse response = new AriusCreateCardRefResponse();
        refreshConfig();

        try {
            //возвращаю 2 рубля юзеру
            AriusCreateCardRefParameters parameters = fillCardRefParameters(config,orderId);
            doAriusCall(parameters,response);
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }




    private AriusTransferFormParameters fillFormPaymentParameters(CreditRequestEntity requestCredit,Double sum, String operation,String endpoint) throws AriusParametersException {
        PeopleMainEntity borrower = requestCredit.getPeopleMainId();
        PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);
        PeopleContactEntity phoneContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, borrower.getId());
        PeopleContactEntity emailContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, borrower.getId());

        AddressEntity borAddress = peopleBean.findAddressActive(borrower,BaseAddress.RESIDENT_ADDRESS);
        if (borAddress == null){
            borAddress = peopleBean.findAddressActive(borrower,BaseAddress.REGISTER_ADDRESS);
        }
        CountryEntity borCountry = null;
        if (borAddress != null) {
            borCountry = borAddress.getCountry();
        }

        return fillFormPaymentParameters(config, requestCredit.getAcceptedcreditId(), requestCredit, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry, sum, operation, endpoint);

    }

    @Override
    public AriusTransferResponse doTransfer(Integer creditRequestId) {
        AriusTransferResponse response = new AriusTransferResponse();
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
        refreshConfig();

        if(creditRequest == null){
            response.setVal(AriusConstants.ERROR_CODE,BORROWER_CREDIT_NOT_FOUND);
            return response;
        }
        try {
            //2 рубля блокирую
            AriusTransferParameters parameters = fillTransferParameters(creditRequest,null,AriusConstants.ARIUS_OPERATION_TRANSFER_BY_REF,config.getEndpointdeposit2card());
            doAriusCall(parameters,response);
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }
    private AriusTransferParameters fillTransferParameters(CreditRequestEntity requestCredit,Double sum, String operation,String endpoint) throws AriusParametersException {
        PeopleMainEntity borrower = requestCredit.getPeopleMainId();
        PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);
        PeopleContactEntity phoneContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, borrower.getId());
        PeopleContactEntity emailContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, borrower.getId());

        AddressEntity borAddress = peopleBean.findAddressActive(borrower,BaseAddress.RESIDENT_ADDRESS);
        if (borAddress == null){
            borAddress = peopleBean.findAddressActive(borrower,BaseAddress.REGISTER_ADDRESS);
        }
        CountryEntity borCountry = null;
        if (borAddress != null) {
            borCountry = borAddress.getCountry();
        }

        AccountEntity account = requestCredit.getAccountId();


        return fillTransferParameters(config, requestCredit.getAcceptedcreditId(),  requestCredit, account, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry, sum, operation, endpoint);

    }




    @Override
    public AriusTransferStatusResponse doTransferStatus(String externalId,String externalId2) {
        AriusTransferStatusResponse response = new AriusTransferStatusResponse();
        refreshConfig();

        if(externalId == null){
            response.setVal(AriusConstants.ERROR_CODE,BORROWER_EXTERNAL_ID_NOT_FOUND);
            return response;
        }
        try {
            //2 рубля блокирую
            AriusTransferStatusParameters parameters = fillTransferStatusParameters(externalId, externalId2, config, AriusConstants.ARIUS_OPERATION_STATUS, config.getEndpointdeposit2card());
            doAriusCall(parameters,response);
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }
    public AriusTransferStatusParameters fillTransferStatusParameters(String externalId,String externalId2,AriusPayPluginConfig config, String operation,String endpoint) throws AriusParametersException {
        AriusTransferStatusParameters parameters = new AriusTransferStatusParameters();
        parameters.setConfigParameters(config,operation,endpoint,null);
        parameters.setPaynetOrderId(externalId);
        parameters.setSn(externalId2);

        return parameters;
    }




    public AriusTransferFormParameters fillFormPaymentParameters(
            AriusPayPluginConfig config,
            CreditEntity creditEntity,
            CreditRequestEntity requestCredit,
            PeoplePersonalEntity borrowerPersonal,
            PeopleContactEntity phoneContact,
            PeopleContactEntity emailContact,
            AddressEntity borAddress,
            CountryEntity borCountry,
            Double sum, String operation,String endpoint


    ) throws AriusParametersException{

        if (borAddress == null){
            logger.severe( "Не найден адрес заёмщика");
            throw new AriusParametersException(BORROWER_ADDRESS_NOT_FOUND);
        }


        AriusTransferFormParameters ariusParameters = new AriusTransferFormParameters();


        //set country
        //ariusParameters.setCountry(borrowerPersonal.getCitizen().getCode());
        if(borCountry ==null){
            ariusParameters.setCountry(BaseCredit.COUNTRY_RU);
        }else {
            ariusParameters.setCountry(borCountry.getCode());
        }


        //set zip_code
        ariusParameters.setZipCode(borAddress.getIndex());


        //set email
        ariusParameters.setEmail(emailContact.getValue());

        //set phone
        ariusParameters.setPhone(phoneContact.getValue());

        //set city
        if(StringUtils.isEmpty(borAddress.getCity())){
            ariusParameters.setCity(borAddress.getPlaceName());
        }else {
            ariusParameters.setCity(borAddress.getCity());
        }

        //set address1
        ariusParameters.setAddress1(borAddress.getAddressText()); //todo так ли это


        //set url and login
        ariusParameters.setConfigParameters(config, operation,endpoint,requestCredit.getId());


        //set order desc
        try {
            String s = URLEncoder.encode("Перевод денег на карту", "UTF-8");
            ariusParameters.setOrder_desc(s);      //Не шибко важный параметр,описание просто, хардкожу пока
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //set amount
        if(sum != null && sum >0){
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, sum));
        }else{
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, requestCredit.getCreditsum()));
        }

        //set currency
        String curr = BaseCredit.CURRENCY_RUB;
        if(creditEntity != null && creditEntity.getIdCurrency() != null){
            curr = creditEntity.getIdCurrency().getCode();
        }
        if(curr == null) curr = BaseCredit.CURRENCY_RUB;
        ariusParameters.setCurrency(curr);

        //set ipaddress
        ariusParameters.setIpaddress(getIpaddress());


        //set first name
        ariusParameters.setFirstName(borrowerPersonal.getName());

        //set last name
        ariusParameters.setLastName(borrowerPersonal.getSurname());

        if(!config.getLocalhost()){
            //set redirect url
            ariusParameters.setRedirectUrl("http://"+getIpaddress() + ARIUS_REDIRECT_URL);
            //set server callback url
            ariusParameters.setServerCallbackUrl("http://" + getIpaddress() + ARIUS_SERVER_CALLBACK_URL);
        }else{
            //считаю, что это вызов с локального компа
            //ourdomain.ru - это домен который я тупо прописал в hosts , поскольку песочница ариуса нездорово реагирует на слово localhost
            //порт должен быть 8080
            //set redirect url
            ariusParameters.setRedirectUrl("http://ourdomain.ru:8080" + ARIUS_REDIRECT_URL); //todo remove 8686
            //set server callback url
            ariusParameters.setServerCallbackUrl("http://ourdomain.ru:8080"  + ARIUS_SERVER_CALLBACK_URL); //todo remove 8686
        }


        return ariusParameters;

    }

    public AriusTransferParameters fillTransferParameters(
            AriusPayPluginConfig config,
            CreditEntity creditEntity,
            CreditRequestEntity requestCredit,
            AccountEntity account,
            PeoplePersonalEntity borrowerPersonal,
            PeopleContactEntity phoneContact,
            PeopleContactEntity emailContact,
            AddressEntity borAddress,
            CountryEntity borCountry,
            Double sum, String operation,String endpoint


    ) throws AriusParametersException{

        if (borAddress == null){
            logger.severe( "Не найден адрес заёмщика");
            throw new AriusParametersException(BORROWER_ADDRESS_NOT_FOUND);
        }


        AriusTransferParameters ariusParameters = new AriusTransferParameters();

        //set parameters from config
        ariusParameters.setConfigParameters(config, operation, endpoint,requestCredit.getId());

        //set card ref id
        ariusParameters.setCardRefId(account.getCardNumberMasked());

        //set order desc
        try {
            String s = URLEncoder.encode("Перевод денег на карту", "UTF-8");
            ariusParameters.setOrder_desc(s);      //Не шибко важный параметр,описание просто, хардкожу пока
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        //set amount
        if(sum != null && sum >0){
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, sum));
        }else{
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, requestCredit.getCreditsum()));
        }

        //set currency
        String curr = BaseCredit.CURRENCY_RUB;
        if(creditEntity != null && creditEntity.getIdCurrency() != null){
            curr = creditEntity.getIdCurrency().getCode();
        }
        if(curr == null) curr = BaseCredit.CURRENCY_RUB;
        ariusParameters.setCurrency(curr);

        //set ipaddress
        ariusParameters.setIpaddress(getIpaddress());

        //set first name
        ariusParameters.setFirstName(borrowerPersonal.getName());

        //set last name
        ariusParameters.setLastName(borrowerPersonal.getSurname());



        //set country
        //ariusParameters.setCountry(borrowerPersonal.getCitizen().getCode());
/*
        if(borCountry ==null){
            ariusParameters.setCountry(BaseCredit.COUNTRY_RU);
        }else {
            ariusParameters.setCountry(borCountry.getCode());
        }
*/


/*
        //set zip_code
        ariusParameters.setZipCode(borAddress.getIndex());


        //set email
        ariusParameters.setEmail(emailContact.getValue());

        //set phone
        ariusParameters.setPhone(phoneContact.getValue());

        //set city
        ariusParameters.setCity(borAddress.getCity());

        //set address1
        ariusParameters.setAddress1(borAddress.getAddressText()); //todo так ли это

        //set redirect url
        ariusParameters.setRedirectUrl("https://"+getIpaddress() + ARIUS_REDIRECT_URL);

        //set server callback url
        ariusParameters.setServerCallbackUrl("https://"+getIpaddress() + ARIUS_SERVER_CALLBACK_URL);

*/





        return ariusParameters;

    }



    public AriusReturnParameters fillReturnParameters(AriusPayPluginConfig config,String orderId) throws AriusParametersException {
        AriusReturnParameters parameters = new AriusReturnParameters();
        parameters.setComment("Return preauth sum");
        parameters.setOrderId(orderId);
        parameters.setConfigParameters(config, AriusConstants.ARIUS_OPERATION_RETURN,config.getEndpointcardreg(),null);
        return parameters;
    }

    public AriusCreateCardRefParameters fillCardRefParameters(AriusPayPluginConfig config,String orderId) throws AriusParametersException {
        AriusCreateCardRefParameters parameters = new AriusCreateCardRefParameters();
        parameters.setOrderId(orderId);
        parameters.setConfigParameters(config, AriusConstants.ARIUS_OPERATION_CREATE_CARD_REF,config.getEndpointcardreg(),null);
        return parameters;
    }



    public void doAriusCall(AriusParameters ariusParameters, AriusResponse responseV2){
        try {
            Map<String,String> rparams=new HashMap<String,String>();
            StringBuilder sb = new StringBuilder();
            ariusParameters.addParameters(sb);
            String reqString = sb.toString();
            logger.info("Request to arius : " + reqString);
            byte[] response = HTTPUtils.sendHttp("POST", ariusParameters.getUrl(), reqString.getBytes(), rparams, null);
            String responseString = URLDecoder.decode(new String(response),"UTF-8");
            logger.info("Response from Arius:" + responseString);
            responseV2.parse(responseString);
        } catch (Exception e) {
            logger.info("Не удалось получить ответ от системы arius, site=" + ariusParameters.getUrl());
            e.printStackTrace();
        }
    }

    private String getIpaddress() {
        if(ipaddress == null){
            ipaddress = WebUtils.findMyIpAddress();
        }
        return ipaddress;
    }

    private void refreshConfig() {
        if(config == null){
            ActionContext context = actProc.createActionContext(null, true);
            PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(AriusPayBeanLocal.SYSTEM_NAME), null, 0,
                    Collections.<String, Object>emptyMap(), context.getPluginState(AriusPayBeanLocal.SYSTEM_NAME));
            config = (AriusPayPluginConfig) plctx.getPluginConfig();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean startProcessAfterPreauthRedirect(AriusRedirectResponse redirectResponse) {
        if(redirectResponse == null){
            logger.severe("Редирект от Ариуса на операцию блокировки суммы пустой");
            return false;
        }
        if(StringUtils.isEmpty(redirectResponse.getOrderId())){
            logger.severe("Редирект от Ариуса на операцию блокировки суммы неуспешный, ORDERID пустой");
            return false;
        }
        //нахожу  creditRequestId
        Integer creditRequestId = null;
        String ourOrderId = redirectResponse.getVal(AriusConstants.MERCHANT_ORDER);
        if(ourOrderId == null) ourOrderId = redirectResponse.getVal(AriusConstants.CLIENT_ORDERID);
        if(ourOrderId == null){
            logger.severe("Редирект от Ариуса на операцию блокировки суммы неуспешный, нет нашего идентификатора в ответе");
            return false;
        }
        String extractedId = AriusUtils.extractRQID(ourOrderId);
        if(extractedId == null){
            logger.severe("Редирект от Ариуса на операцию блокировки суммы неуспешный, наш идентификатор неверный");
            return false;
        }
        creditRequestId = Integer.valueOf(extractedId);


        //регистрирую карту чтобы получить card ref id
        AriusCreateCardRefResponse ariusCreateCardRefResponse = doCreateCardRef(redirectResponse.getOrderId());
        if(ariusCreateCardRefResponse == null){
            logger.severe("Операция по созданию карты неуспешна, пустой ответ");
            return false;
        }
        if(StringUtils.isEmpty(ariusCreateCardRefResponse.getCardRefId())){
            logger.severe("Операция по созданию карты неуспешна, пустой card_ref_id");
            return false;
        }

        //возвращаю заблокированные 2 рубля
        AriusReturnResponse ariusReturnResponse = doReturn(redirectResponse.getOrderId());
        if(ariusReturnResponse == null){
            logger.severe("Возврат заблокированной суммы не прошел, ответ пустой");
        }
        if(ariusReturnResponse.getVal(AriusConstants.ERROR_CODE) != null){
            logger.severe("Возврат заблокированной суммы не прошел, ошибка" + ariusReturnResponse.getVal(AriusConstants.ERROR_CODE) + "; "+ariusReturnResponse.getVal(AriusConstants.ERROR_MESSAGE));
        }


        //нахожу Account, сохраняю там card_ref_id
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
        AccountEntity accountEntity = creditRequest.getAccountId();
        accountEntity.setCardNumberMasked(ariusCreateCardRefResponse.getCardRefId());
        accountEntity.setCardname(redirectResponse.getVal(AriusConstants.CARD_TYPE));
        accountEntity.setCardnumber(redirectResponse.getVal(AriusConstants.LAST_FOUR_DIGITS));
        accountEntity.setCardHolder(redirectResponse.getVal(AriusConstants.CARD_HOLDER_NAME));


        accDAO.saveAccountEntity(accountEntity);

        try {
            startCreditNew(creditRequestId);
        } catch (KassaException e) {
            logger.severe("не запустился процесс кредита" + e);
            e.printStackTrace();
        } catch (WorkflowException e) {
            logger.severe("не запустился процесс кредита" + e);
            e.printStackTrace();
        }

        return true;
    }


    /**
     *  Стартует процесс кредита
     * @param creditRequestId
     * @throws KassaException
     * @throws WorkflowException
     */
    private void startCreditNew(int creditRequestId) throws KassaException,WorkflowException {
        ApplicationAction action = appServ.startApplicationAction(SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST,
                null, ProcessKeys.MSG_ACCEPT), true, "Подписание оферты", new BusinessObjectRef(
                CreditRequest.class.getName(), creditRequestId));
        if (action == null) {
            throw new KassaException("Подписание оферты уже обрабатывается");
        }
        try {
            workflow.goProcCR(creditRequestId, SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST, null, ProcessKeys.MSG_ACCEPT), Collections.<String, Object>emptyMap());
        } catch (WorkflowException e) {
            throw e;
        } finally {
            appServ.endApplicationAction(action);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AriusSaleFormResponse doSale(Integer creditId, Double sum) {
        AriusSaleFormResponse response = new AriusSaleFormResponse();
        CreditEntity creditEntity = creditDAO.getCreditEntity(creditId);
        CreditRequestEntity creditRequest = creditEntity.getCreditRequestId();
        refreshConfig();

        if(creditRequest == null){
            response.setVal(AriusConstants.ERROR_CODE,BORROWER_CREDIT_NOT_FOUND);
            return response;
        }
        try {
            AriusSaleParameters parameters = fillSaleParameters(creditRequest, sum, AriusConstants.ARIUS_OPERATION_SALE_FORM, config.getEndpoint3ds());
            doAriusCall(parameters,response);
            if(response.getVal(AriusConstants.ERROR_CODE) == null){
                //всё таки создаю сначала платеж,хотя не очень хочется это делать,а  всё из-за того что в параметрах редиректа нет параметра AriusConstants.SERIAL_NUMBER
                //приходится его сохранять в Payment,
                Payment payment = paymentService.createPayment(
                        creditEntity.getId(),
                        Account.CARD_TYPE,
                        Payment.SUM_FROM_CLIENT,
                        sum,
                        Payment.TO_SYSTEM,
                        response.getVal(AriusConstants.PAYNET_ORDER_ID),
                        response.getVal(AriusConstants.SERIAL_NUMBER),
                        Partner.ARIUS,
                        creditRequest.getAccountId(),
                        null);

            }
            return response;
        } catch (AriusParametersException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            response.setVal(AriusConstants.ERROR_CODE,e.getMessage());
            return response;
        }
    }

    private AriusSaleParameters fillSaleParameters(CreditRequestEntity requestCredit,Double sum, String operation,String endpoint) throws AriusParametersException {
        PeopleMainEntity borrower = requestCredit.getPeopleMainId();
        PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);
        PeopleContactEntity phoneContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, borrower.getId());
        PeopleContactEntity emailContact =peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, borrower.getId());

        AddressEntity borAddress = peopleBean.findAddressActive(borrower,BaseAddress.RESIDENT_ADDRESS);
        if (borAddress == null){
            borAddress = peopleBean.findAddressActive(borrower,BaseAddress.REGISTER_ADDRESS);
        }
        CountryEntity borCountry = null;
        if (borAddress != null) {
            borCountry = borAddress.getCountry();
        }

        return fillSaleParameters(config, requestCredit.getAcceptedcreditId(), requestCredit, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry, sum, operation, endpoint);

    }


    public AriusSaleParameters fillSaleParameters(
            AriusPayPluginConfig config,
            CreditEntity creditEntity,
            CreditRequestEntity requestCredit,
            PeoplePersonalEntity borrowerPersonal,
            PeopleContactEntity phoneContact,
            PeopleContactEntity emailContact,
            AddressEntity borAddress,
            CountryEntity borCountry,
            Double sum, String operation,String endpoint


    ) throws AriusParametersException{

        if (borAddress == null){
            logger.severe( "Не найден адрес заёмщика");
            throw new AriusParametersException(BORROWER_ADDRESS_NOT_FOUND);
        }


        AriusSaleParameters ariusParameters = new AriusSaleParameters();


        //set url and login
        ariusParameters.setConfigParameters(config, operation,endpoint,requestCredit.getId());

        //set order desc
        try {
            String s = URLEncoder.encode("Погашение кредита", "UTF-8");
            ariusParameters.setOrder_desc(s);      //Не шибко важный параметр,описание просто, хардкожу пока
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //set first name
        ariusParameters.setFirstName(borrowerPersonal.getName());

        //set last name
        ariusParameters.setLastName(borrowerPersonal.getSurname());

        //set address1
        ariusParameters.setAddress1(borAddress.getAddressText()); //todo так ли это

        //set city
        if(StringUtils.isEmpty(borAddress.getCity())){
            ariusParameters.setCity(borAddress.getPlaceName());
        }else {
            ariusParameters.setCity(borAddress.getCity());
        }
        //set phone
        ariusParameters.setPhone(phoneContact.getValue());

        //set zip_code
        ariusParameters.setZipCode(borAddress.getIndex());

        //set country
        //ariusParameters.setCountry(borrowerPersonal.getCitizen().getCode());
        if(borCountry ==null){
            ariusParameters.setCountry(BaseCredit.COUNTRY_RU);
        }else {
            ariusParameters.setCountry(borCountry.getCode());
        }

        //set email
        ariusParameters.setEmail(emailContact.getValue());

        //set amount
        if(sum != null && sum >0){
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, sum));
        }else{
            ariusParameters.setAmount(Convertor.toDecimalString(CalcUtils.dformat, requestCredit.getCreditsum()));
        }

        //set currency
        String curr = BaseCredit.CURRENCY_RUB;
        if(creditEntity != null && creditEntity.getIdCurrency() != null){
            curr = creditEntity.getIdCurrency().getCode();
        }
        if(curr == null) curr = BaseCredit.CURRENCY_RUB;
        ariusParameters.setCurrency(curr);

        //set ipaddress
        ariusParameters.setIpaddress(getIpaddress());



        if(!config.getLocalhost()){
            //set redirect url
            ariusParameters.setRedirectUrl("http://"+getIpaddress() + ARIUS_SALE_REDIRECT_URL);
            //set server callback url
            ariusParameters.setServerCallbackUrl("http://" + getIpaddress() + ARIUS_SALE_CALLBACK_URL);
        }else{
            //считаю, что это вызов с локального компа
            //ourdomain.ru - это домен который я тупо прописал в hosts , поскольку песочница ариуса нездорово реагирует на слово localhost
            //порт должен быть 8080
            //set redirect url
            ariusParameters.setRedirectUrl("http://ourdomain.ru:8080" + ARIUS_SALE_REDIRECT_URL);
            //set server callback url
            ariusParameters.setServerCallbackUrl("http://ourdomain.ru:8080"  + ARIUS_SALE_CALLBACK_URL);
        }


        return ariusParameters;

    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean startProcessAfterSaleRedirect(AriusRedirectResponse redirectResponse) {
        if(redirectResponse == null){
            logger.severe("Редирект от Ариуса на операцию Sale пустой");
            return false;
        }
        if(StringUtils.isEmpty(redirectResponse.getOrderId())){
            logger.severe("Редирект от Ариуса на операцию Sale неуспешный, ORDERID пустой");
            return false;
        }
        //нахожу  creditRequestId
        Integer creditRequestId = null;
        String ourOrderId = redirectResponse.getVal(AriusConstants.MERCHANT_ORDER);
        if(ourOrderId == null) ourOrderId = redirectResponse.getVal(AriusConstants.CLIENT_ORDERID);
        if(ourOrderId == null){
            logger.severe("Редирект от Ариуса на операцию Sale неуспешный, нет нашего идентификатора в ответе");
            return false;
        }
        String extractedId = AriusUtils.extractRQID(ourOrderId);
        if(extractedId == null){
            logger.severe("Редирект от Ариуса на операцию Sale неуспешный, наш идентификатор неверный");
            return false;
        }
        creditRequestId = Integer.valueOf(extractedId);

        Payment payment = paymentDAO.findPaymentByExternalId(redirectResponse.getVal(AriusConstants.ORDERID));
        if(payment == null){
            logger.severe("Ошибка в создании платежа для погашения кредита через Ариус, creditRequestId id=" + creditRequestId);
            return false;
        }


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("paymentId", payment.getId());
        data.put("date",new Date());

        try {
            workflow.repaymentReceivedArius(creditRequestId, data);
        } catch (WorkflowException e) {
            logger.severe("Ошибка в запуске процесса погашения кредита через Ариус, payment id=" + payment.getId());
            e.printStackTrace();
            return false;
        }

        return true;
    }




}



















