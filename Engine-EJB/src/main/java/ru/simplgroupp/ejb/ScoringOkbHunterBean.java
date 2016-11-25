package ru.simplgroupp.ejb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.ws.rs.HttpMethod;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.AntifraudOccasionDAO;
import ru.simplgroupp.dao.interfaces.AntifraudSuspicionDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.hunter.changepassword.ChangePassword;
import ru.simplgroupp.hunter.changepassword.ChangePasswordResponse;
import ru.simplgroupp.hunter.hashing.CTR;
import ru.simplgroupp.hunter.hashing.ENS;
import ru.simplgroupp.hunter.hashing.PRODUCT_CODES;
import ru.simplgroupp.hunter.hashing.RGC;
import ru.simplgroupp.hunter.hashing.SCL;
import ru.simplgroupp.hunter.hashing.in.BATCH_IN;
import ru.simplgroupp.hunter.onlinematching.ControlBlock;
import ru.simplgroupp.hunter.onlinematching.ControlMatchSchemeType;
import ru.simplgroupp.hunter.onlinematching.CustomerType;
import ru.simplgroupp.hunter.onlinematching.ErrorType;
import ru.simplgroupp.hunter.onlinematching.LoadingType;
import ru.simplgroupp.hunter.onlinematching.MatchingType;
import ru.simplgroupp.hunter.onlinematching.ResultBlock;
import ru.simplgroupp.hunter.onlinematching.ResultsType;
import ru.simplgroupp.hunter.onlinematching.RulesType;
import ru.simplgroupp.hunter.onlinematching.wsdl.Match;
import ru.simplgroupp.hunter.onlinematching.wsdl.MatchResponse;
import ru.simplgroupp.hunter.onlinematching.wsdl.ObjectFactory;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringOkbHunterBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Employment;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.RefHunterRule;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MimeTypeKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Класс для работы с ОКБ Национальный Хантер
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScoringOkbHunterBean extends AbstractPluginBean implements ScoringOkbHunterBeanLocal {

    @Inject Logger LOG;

    private static final String SOAP_ENV_MATCHING = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<soap:Body><Match xmlns=\"http://www.mclsoftware.co.uk/HunterII/WebServices\">"
            + "<controlXml>%s</controlXml><batchXml>%s</batchXml>"
            + "<username>%s</username><password>%s</password></Match></soap:Body></soap:Envelope>";

    private static final String SOAP_ENV_CHANGEPASSWORD = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<soap:Body><changePassword xmlns=\"http://www.e-i.ru/WebServices\"><username>%s</username>"
            + "<oldpassword>%s</oldpassword><newpassword>%s</newpassword></changePassword></soap:Body></soap:Envelope>";

    private static final String CITY_SHORT_NAME= "г";
    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private AntifraudOccasionDAO antifraudOccasionDAO;

    @EJB
    private AntifraudSuspicionDAO antifraudSuspicionDAO;

    @EJB
    private RequestsService requestsService;

    @EJB
    private CreditInfoService creditInfoService;

    @EJB
    private EventLogService eventLogService;

    @EJB
    private IFIASService fiasService;

    @EJB
    private ReferenceBooksLocal referenceBooks;

    @EJB
    private ServiceBeanLocal serviceBean;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private HunterHashBean hunterHashBean;

    private JAXBContext jaxbContextControl;

    private JAXBContext jaxbContextBatchIn;

    private JAXBContext jaxbContextMatchResponse;

    private JAXBContext jaxbContextResult;

    private JAXBContext jaxbContextChangePasswordResponse;

    private ObjectFactory objectFactory = new ObjectFactory();

    @PostConstruct
    private void init() {
        try {
            jaxbContextControl = JAXBContext.newInstance(ControlBlock.class);

            jaxbContextBatchIn = JAXBContext.newInstance(BATCH_IN.class);

            jaxbContextMatchResponse = JAXBContext.newInstance(MatchResponse.class);

            jaxbContextResult = JAXBContext.newInstance(ResultBlock.class);

            jaxbContextChangePasswordResponse = JAXBContext.newInstance(ChangePasswordResponse.class);
        } catch (JAXBException ex) {
            LOG.severe("Не удалось создать jaxbContext: " + ex.getMessage());
            throw new IllegalStateException(new ActionException(ErrorKeys.CANT_CREATE_JAXB_CONTEXT,
                    "Не удалось создать jaxbContext ", Type.TECH, ResultType.FATAL, ex));
        }
    }

    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return SYSTEM_DESCRIPTION;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.SYNC);
    }

    @Override
    public Set<String> getModelTargetsSupported() {
        return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }
    // if sync request
    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        Integer creditRequestId = Convertor.toInteger(businessObjectId);

        LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequestId + " передана на скоринг.");
        try {
            CreditRequest ccRequest;
            try {
                ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
            } catch (Exception ex) {
                throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку",
                        Type.TECH, ResultType.FATAL, ex);
            }
            if (ccRequest == null){
                throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку, заявка null",
                        Type.TECH, ResultType.NON_FATAL, null);
            }
            OkbHunterPluginConfig cfg = (OkbHunterPluginConfig) context.getPluginConfig();

            if (context.getNumRepeats() < cfg.getNumberRepeats()) {
                newRequest(ccRequest.getEntity(), cfg.isUseWork(), cfg.getCacheDays());
            } else {
                LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequestId + " не обработана.");
                throw new ActionException(ErrorKeys.CANT_MAKE_SKORING, "Не удалось выполнить скоринговый запрос в КБ за 5 попыток",
                        Type.TECH, ResultType.FATAL, null);
            }
        } catch (ActionException ex) {
            LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequestId + " не обработана.");
            throw new ActionException("Произошла ошибка ", ex);
        } catch (Throwable ex) {
            LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequestId + " не обработана. Ошибка " + ex.getMessage());
            throw new ActionException("Произошла ошибка ", ex);
        }
        LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequestId + " обработана.");

    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    // if async
    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork, Integer cacheDays) throws ActionException {
        return doRequest(cre, isWork, SCL.LM, cacheDays);
    }

    private RequestsEntity doRequest(CreditRequestEntity cre, Boolean isWork, SCL scl, Integer cacheDays) throws ActionException {
        //проверяем, кеширован ли запрос
        boolean inCache = requestsService.isRequestInCache(cre.getPeopleMainId().getId(),
                Partner.OKB_HUNTER, new Date(), cacheDays);
        if (inCache){
            LOG.info("По человеку " + cre.getPeopleMainId().getId() +
                    " уже производился запрос в ОКБ Национальный Хантер, он кеширован");
            //save log

            eventLogService.saveLogEx(EventType.INFO, EventCode.OUTER_SCORING_OKB_HUNTER,
                    "Запрос к внешнему партнеру ОКБ Национальный Хантер не производился, данные кешированы",
                    null, new Date(), creditRequestDAO.getCreditRequestEntity(cre.getId()), null, null);

            return null;
        }

        //preparing for making request
        PartnersEntity partner = referenceBooks.getPartnerEntity(Partner.OKB_HUNTER);

        PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());

        //new request
        RequestsEntity result = requestsService.addRequest(cre.getPeopleMainId().getId(), cre.getId(),
                Partner.OKB_HUNTER, RequestStatus.STATUS_IN_WORK, new Date());

        //инициализируем ssl context
        SSLContext sslContext = null;
        try {
            sslContext = initSSLContext(isWork);
        } catch (KassaException ex){
            requestsService.saveRequestError(result, ex, ErrorKeys.CANT_CREATE_SSL_CONTEXT,
                    "Не удалось создать ssl context", Type.TECH, ResultType.NON_FATAL);
        }

        String urlString;
        String login;
        String password;
        if (isWork) {
            urlString = partner.getUrlwork();
            login = partner.getLoginwork();
            password = partner.getPasswordwork();
        } else {
            urlString = partner.getUrltest();
            login = partner.getLogintest();
            password = partner.getPasswordtest();
        }
        try {
            Match requestObject = createRequest(cre, peopleMain, partner, isWork, scl);
            requestObject.setUsername(login);
            requestObject.setPassword(password);

            String requestMessage = String.format(SOAP_ENV_MATCHING, requestObject.getControlXml(), requestObject.getBatchXml(),
                    requestObject.getUsername(), requestObject.getPassword());

            result.setRequestbody(requestMessage.getBytes());

            String response = match(requestMessage, urlString, sslContext);
            LOG.info("ОКБ Национальный Хантер, Заявка " + cre.getId() + " Ответ:" + response);

            result.setResponsedate(new Date());
            result.setResponsebodystring(response);
            result.setResponsebody(response.getBytes());

            ResultBlock resultBlock = unmarshallResponse(response);

            if (resultBlock.getErrorWarnings().getErrors().getErrorCount().compareTo(BigInteger.ZERO) > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                LOG.info("ошибки в ответе от Национальный Хантер:");
                for (ErrorType errorType : resultBlock.getErrorWarnings().getErrors().getError()) {
                    LOG.info(errorType.getMessage());
                    stringBuilder.append(errorType.getMessage()).append(";");
                }
                result.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
                result.setResponsecode(resultBlock.getErrorWarnings().getErrors().getError().get(0).getNumber().toString());
                result.setResponsemessage(Convertor.toLimitString(stringBuilder.toString(), 250));
            } else {
                // anyway write warnings to log file
                if (resultBlock.getErrorWarnings().getWarnings().getWarningCount().compareTo(BigInteger.ZERO) > 0) {
                    LOG.info("предупреждения в ответе от Национальный Хантер:");
                    for (ErrorType errorType : resultBlock.getErrorWarnings().getWarnings().getWarning()) {
                        LOG.info(errorType.getMessage());
                    }
                }

                saveSuspicions(cre, peopleMain, partner, resultBlock);

                result.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
                result.setResponsemessage("Данные найдены");
            }
            //create message
            result = requestsService.saveRequestEx(result);
        } catch (Throwable ex) {
            LOG.severe("Не удалось создать сообщение: " + ex.getMessage());
            throw new ActionException(ErrorKeys.CANT_CREATE_SOAP_MESSAGE, "Не удалось создать сообщение",
                    Type.TECH, ResultType.NON_FATAL, ex);
        } finally {
            try {
                returnSSLContext(isWork, sslContext);
            } catch (KassaException ex) {
                LOG.severe("Не удалось вернуть ssl context: " + ex.getMessage());
            }
        }
        //save log
        eventLogService.saveLogEx(EventType.INFO, EventCode.OUTER_SCORING_OKB_HUNTER,
                "Был выполнен запрос к сервису Национальный Хантер",
                null, new Date(), creditRequestDAO.getCreditRequestEntity(cre.getId()), null, null);

        return result;
     }

    @Override
    public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uploadHistory(UploadingEntity uploading, Date sendingDate, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkUploadStatus(UploadingEntity uploading, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork, List<Integer> creditIds) throws KassaException {
        throw new UnsupportedOperationException();
    }

    private SSLContext initSSLContext(Boolean isWork) throws KassaException{
        String[] settings;

        if (isWork) {
            settings = new String[] {CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK};
        } else {
            settings = new String[] {CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST};
        }
        try {
            return serviceBean.borrowSSLContext(settings);
        } catch (Exception ex) {
            LOG.severe("Не удалось инициализировать ssl context: " + ex.getMessage());
            throw new KassaException("Не удалось инициализировать ssl context: " + ex);
        }
    }

    private void returnSSLContext(Boolean isWork, SSLContext sslCon) throws KassaException {
        String[] settings;
        if (isWork) {
            settings = new String[] {CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK};
        } else {
            settings = new String[] {CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST};
        }
        try {
            serviceBean.returnSSLContext(settings, sslCon);
        } catch (Exception ex) {
            LOG.severe("Не удалось вернуть ssl context: " + ex.getMessage());
            throw new KassaException("Не удалось вернуть ssl context: " + ex);
        }
    }

    @Override
    public Match createRequest(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException {
        Match result = createMatch(creditRequest, peopleMain, partner, isWork, scl);
        LOG.info("batchIn:" + result.getBatchXml());
        hash(result);
        LOG.info("batchIn-hashed:" + result.getBatchXml());
        result.setControlXml(StringEscapeUtils.escapeXml(result.getControlXml()));
        result.setBatchXml(StringEscapeUtils.escapeXml(result.getBatchXml()));
        return result;
    }

    @Override
    public Match createMatch(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException {
        Match result = objectFactory.createMatch();
        //login and password
        if (isWork) {
            result.setUsername(partner.getLoginwork());
            result.setPassword(partner.getPasswordwork());
        } else {
            result.setUsername(partner.getLogintest());
            result.setPassword(partner.getPasswordtest());
        }

        String controlBlock;
        String batchIn;
        try (ByteArrayOutputStream osControlBlock = new ByteArrayOutputStream();
             ByteArrayOutputStream osBatchIn = new ByteArrayOutputStream()) {

            Marshaller marshallerControl = jaxbContextControl.createMarshaller();
            Marshaller marshallerBatchIn = jaxbContextBatchIn.createMarshaller();

            marshallerControl.marshal(createControlBlock(partner, isWork), osControlBlock);
            marshallerBatchIn.marshal(createBatchIn(creditRequest, peopleMain, partner, isWork, scl), osBatchIn);

            controlBlock = osControlBlock.toString(XmlUtils.ENCODING_UTF);
            batchIn = osBatchIn.toString(XmlUtils.ENCODING_UTF);
        } catch (JAXBException ex) {
            throw new ActionException(ErrorKeys.CANT_CREATE_MARSHALLER,
                    "Не удалось создать marshaller для ControlBlock/BatchIn", Type.TECH, ResultType.FATAL, ex);
        } catch (IOException ex) {
            throw new ActionException("Ошибка при работе с ControlBlock/BatchIn потоком", Type.TECH, ResultType.FATAL);
        }

        result.setControlXml(controlBlock);
        result.setBatchXml(batchIn);
        return result;
    }

    @Override
    public void hash(Match match) throws ActionException {
        try (ByteArrayInputStream isBatchIn = new ByteArrayInputStream(match.getBatchXml().getBytes(XmlUtils.ENCODING_UTF));
                ByteArrayOutputStream outHashedBatchIn = new ByteArrayOutputStream()) {
            boolean successful = hunterHashBean.hash(isBatchIn, outHashedBatchIn);
            if (!successful) {
                throw new ActionException("Ошибка хеширования", Type.TECH, ResultType.FATAL);
            }

            match.setBatchXml(outHashedBatchIn.toString(XmlUtils.ENCODING_UTF));
        } catch (IOException e) {
            throw new ActionException("Ошибка при работе с BatchIn потоком", Type.TECH, ResultType.FATAL);
        }
    }

    private String match(String requestMessage, String url, SSLContext sslContext) throws IOException, KassaException {
        Map<String, String> params = new HashMap<>();
        params.put("Encoding", XmlUtils.ENCODING_UTF);
        params.put("Content-Type", MimeTypeKeys.XML);
        params.put("SOAPAction", "http://www.mclsoftware.co.uk/HunterII/WebServices/Match");

        return new String(HTTPUtils.sendHttp(HttpMethod.POST , url, requestMessage.getBytes(), params, sslContext),
                XmlUtils.ENCODING_UTF);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void uploadCreditRequests(PluginExecutionContext context) throws ActionException {
        PartnersEntity partner = referenceBooks.getPartnerEntity(Partner.OKB_HUNTER);

        LOG.info("ОКБ Национальный Хантер, поиск незагруженных заявок");
        List<CreditRequestEntity> creditRequests = creditRequestDAO.findCreditRequestsToUpload(partner.getId(),
                RequestStatus.STATUS_IS_DONE_WITH_SESSION);

        uploadCreditRequests(creditRequests, context);

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void uploadCreditRequests(List<CreditRequestEntity> creditRequestEntities, PluginExecutionContext context) throws ActionException {
        OkbHunterPluginConfig cfg = (OkbHunterPluginConfig) context.getPluginConfig();
        for (CreditRequestEntity creditRequest : creditRequestEntities) {
            LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequest.getId() + " передана на скоринг.");
            try {
                doRequest(creditRequest, cfg.isUseWork(), SCL.LO, cfg.getCacheDays());
                LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequest.getId() + " обработана.");
            } catch (Throwable ex) {
                LOG.info("ОКБ Национальный Хантер, верификация. Заявка " + creditRequest.getId() + " не обработана. Ошибка " + ex.getMessage());
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changePassword(PluginExecutionContext context) throws ActionException {
        LOG.info("ОКБ Национальный Хантер, смена пароля");

        OkbHunterPluginConfig cfg = (OkbHunterPluginConfig) context.getPluginConfig();
        boolean isWork = cfg.isUseWork();
        String urlString = cfg.getChangePasswordUrl();
        LOG.info(String.format("ОКБ Национальный Хантер, url адрес для смены пароля: %s", urlString));

        PartnersEntity partner = referenceBooks.getPartnerEntity(Partner.OKB_HUNTER);

        SSLContext sslContext;
        try {
            sslContext = initSSLContext(isWork);
        } catch (KassaException ex){
            throw new ActionException("Не удалось создать ssl context", Type.TECH, ResultType.NON_FATAL);
        }

        String login;
        String password;
        if (isWork) {
            login = partner.getLoginwork();
            password = partner.getPasswordwork();
        } else {
            login = partner.getLogintest();
            password = partner.getPasswordtest();
        }

        try {
            ChangePassword changePassword = new ChangePassword();
            changePassword.setUsername(login);
            changePassword.setOldpassword(password);
            changePassword.setNewpassword(RandomStringUtils.randomAscii(12)
                    .replaceAll("(<|\\{)", "(").replaceAll("(>|\\})", ")"));
            LOG.info(String.format("ОКБ Национальный Хантер новый пароль: %s", changePassword.getNewpassword()));

            String response = doChangePassword(changePassword, urlString, sslContext);
            LOG.info(String.format("ОКБ Национальный Хантер, ответ от сервиса смены пароля: %s", response));

            int changePasswordResult = unmarshallChangePasswordResponse(response);
            LOG.info(String.format("ОКБ Национальный Хантер, ответ сервиса по смене пароля: %d", changePasswordResult));
            if (changePasswordResult == 0) {
                if (isWork) {
                    partner.setPasswordwork(changePassword.getNewpassword());
                } else {
                    partner.setPasswordtest(changePassword.getNewpassword());
                }
                LOG.info("ОКБ Национальный Хантер, смена пароля произведена успешно");
            }
        } catch (Throwable ex) {
            LOG.severe("Не удалось создать сообщение: " + ex.getMessage());
            throw new ActionException(ErrorKeys.CANT_CREATE_SOAP_MESSAGE, "Не удалось создать сообщение",
                    Type.TECH, ResultType.NON_FATAL, ex);
        } finally {
            try {
                returnSSLContext(isWork, sslContext);
            } catch (KassaException ex) {
                LOG.severe("Не удалось вернуть ssl context: " + ex.getMessage());
            }
        }
    }

    /**
     * Создание ControlBlock для control части документа
     * Согласно документации "HUNTER II Web Service Interface Developers Guide-ap edition.pdf"
     * RsltCode = 1 (стр. 30)
     * SubmissionLoad  = 1 (стр. 31)
     * Flag = 0 (стр. 32)
     * PersistMatches = 1 (стр. 32)
     * WorklistInsert = 0 или 1 (стр. 33)
     * @return {@link ControlBlock}
     */
    private ControlBlock createControlBlock(PartnersEntity partner, boolean isWork) {
        ControlBlock result = new ControlBlock();

        long customerId;
        String customerName;
        if (isWork) {
            customerId = Long.valueOf(partner.getGroupWork());
            customerName = partner.getCodework();
        } else {
            customerId = Long.valueOf(partner.getGroupTest());
            customerName = partner.getCodetest();
        }

        CustomerType customerType = new CustomerType();
        customerType.setCustomerID(BigInteger.valueOf(customerId));
        customerType.setCustomerName(customerName);
        result.setCustomer(customerType);

        LoadingType loadingType = new LoadingType();
        loadingType.setSubmissionLoad(1);// page 31
        LoadingType.SuppressVersion suppressVersion = new LoadingType.SuppressVersion();
        suppressVersion.setFlag(0); // page 32
        loadingType.setSuppressVersion(suppressVersion);
        result.setLoading(loadingType);

        MatchingType matchingType = new MatchingType();
        ControlMatchSchemeType controlMatchSchemeType = new ControlMatchSchemeType();
        controlMatchSchemeType.getSchemeID().add(BigInteger.valueOf(Long.valueOf(partner.getRequestVersion())));
        matchingType.setMatchScheme(controlMatchSchemeType);
        matchingType.setPersistMatches(1); // page 32
        matchingType.setWorklistInsert(0);
        result.setMatching(matchingType);

        ResultsType resultsType = new ResultsType();
        resultsType.setRsltCode(1); // page 30
        result.setResults(resultsType);

        return result;
    }

    private BATCH_IN createBatchIn(CreditRequestEntity cre, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException {
        BATCH_IN result = new BATCH_IN();

        result.setHEADER(createHeader(partner, isWork));
        result.setSUBMISSIONS(createSubmissions(cre, peopleMain, scl));

        return result;
    }

    private BATCH_IN.HEADER createHeader(PartnersEntity partner, boolean isWork) {
        BATCH_IN.HEADER result = new BATCH_IN.HEADER();

        result.setCOUNT(BigDecimal.ONE);
        String customerName;
        if (isWork) {
            customerName = partner.getCodework();
        } else {
            customerName = partner.getCodetest();
        }
        result.setORIGINATOR(customerName);
        //result.setRESULT(ENUM_YES.Y);

        return result;
    }

    private BATCH_IN.SUBMISSIONS createSubmissions(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, SCL scl) throws ActionException {
        BATCH_IN.SUBMISSIONS result = new BATCH_IN.SUBMISSIONS();

        BATCH_IN.SUBMISSIONS.SUBMISSION submission = new BATCH_IN.SUBMISSIONS.SUBMISSION();

        // FIXME
        submission.setIDENTIFIER(creditRequest.getId().toString());
        submission.setPRODUCT(PRODUCT_CODES.GP);
        submission.setCLASSIFICATION(scl);
        submission.setDATE(Convertor.dateToGreg(new Date(), false));
        submission.setAPP_DATE(Convertor.dateToGreg(creditRequest.getDateFill(), false));
        submission.setMA(createMainApplicant(creditRequest, peopleMain));

        result.getSUBMISSION().add(submission);

        return result;
    }

    private BATCH_IN.SUBMISSIONS.SUBMISSION.MA createMainApplicant(CreditRequestEntity creditRequest,
            PeopleMainEntity peopleMain) throws ActionException {
        BATCH_IN.SUBMISSIONS.SUBMISSION.MA result = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA();

        result.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_MAIN_APPLICANT));

        fillPersonal(result, peopleMain);

        fillPassport(result, creditRequest, peopleMain);

        fillAddress(result, creditRequest, peopleMain);

        fillEmployment(result, creditRequest, peopleMain);

        fillContacts(result, creditRequest, peopleMain);

        return result;
    }

    private void fillPersonal(BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma, PeopleMainEntity peopleMain) throws ActionException {
        PeoplePersonalEntity personal = peopleBean.findPeoplePersonalActive(peopleMain);

        if (personal.getName() == null) {
            throw new ActionException("Отсутствует имя ", Type.BUSINESS, ResultType.FATAL);
        }
        ma.setFNAME(personal.getName());

        ma.setPNAME(personal.getMidname());

        if (personal.getSurname() == null) {
            throw new ActionException("Отсутствует фамилия ", Type.BUSINESS, ResultType.FATAL);
        }
        ma.setLNAME(personal.getSurname());

        if (personal.getBirthdate() == null) {
            throw new ActionException("Отсутствует дата рождения ", Type.BUSINESS, ResultType.FATAL);
        }
        ma.setDOB(Convertor.dateToGreg(personal.getBirthdate(), false));

        ma.setPER_INN(peopleMain.getInn());
    }

    private void fillPassport(BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma, CreditRequestEntity creditRequest,
            PeopleMainEntity peopleMain) throws ActionException {
        DocumentEntity document = peopleBean.findPassportActive(peopleMain);
        if (document == null ) {
            throw new ActionException("Отсутствует паспорт ", Type.BUSINESS, ResultType.FATAL);
        }

        BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS maPas = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS();

        maPas.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_PASSPORT));

        if (document.getSeries() == null || document.getNumber() == null) {
            throw new ActionException("Отсутствует серия/номер паспорта ", Type.BUSINESS, ResultType.FATAL);
        }
        maPas.setDOC_NUMBER(document.getSeries() + document.getNumber());

        if (document.getDocdate() == null) {
            throw new ActionException("Отсутствует дата выдачи документа ", Type.BUSINESS, ResultType.FATAL);
        }
        maPas.setDOC_DATE(Convertor.dateToGreg(document.getDocdate(), false));

        ma.setMA_PAS(maPas);
    }

    private void fillAddress(BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma, CreditRequestEntity creditRequest,
            PeopleMainEntity peopleMain) throws ActionException {
        // адрес по прописке
        AddressEntity address = peopleBean.findAddressActive(peopleMain, BaseAddress.REGISTER_ADDRESS);

        if (address == null) {
            throw new ActionException("Отсутствует адрес регистрации ", Type.BUSINESS, ResultType.FATAL);
        }

        BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD maRad = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD();

        maRad.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(),
                RefHunterRule.HUNTER_OBJECT_REGISTRATION_ADDRESS));

        maRad.setCOUNTRY(CTR.RU);

        if (StringUtils.isNotBlank(address.getIndex())) {
            maRad.setPOST_CODE(address.getIndex());
        }

        if (address.getRegionShort() != null) {
            RGC region = RGC.getRegionByCode(address.getRegionShort().getCodereg());
            if (region == null) {
                region = RGC.Other;
            }
            maRad.setREGION(region);
        }

        if (StringUtils.isNotBlank(address.getAreaName())) {
            maRad.setDISTRICT(address.getAreaName());
        }

        maRad.setCITY(getCity(address));

        if (StringUtils.isNotBlank(address.getPlaceName())) {
            maRad.setSETTLEMENT(address.getPlaceName());
        }

        if (StringUtils.isNotBlank(address.getStreetName())) {
            maRad.setSTREET(address.getStreetName());
        }

        if (StringUtils.isNotBlank(address.getHouse())) {
            maRad.setHOUSE(address.getHouse());
        }

        if (StringUtils.isNotBlank(address.getBuilding())) {
            maRad.setBUILD(address.getBuilding());
        }

        if (StringUtils.isNotBlank(address.getCorpus())) {
            maRad.setCONSTR(address.getCorpus());
        }

        if (StringUtils.isNotBlank(address.getFlat())) {
            maRad.setAPART(address.getFlat());
        }

        ma.setMA_RAD(maRad);

        // адрес проживания
        address = peopleBean.findAddressActive(peopleMain, BaseAddress.RESIDENT_ADDRESS);
        if (address != null) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD maCad = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD();

            maCad.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(),
                    RefHunterRule.HUNTER_OBJECT_RESIDENCE_ADDRESS));

            maCad.setCOUNTRY(CTR.RU);

            if (StringUtils.isNotBlank(address.getIndex())) {
                maCad.setPOST_CODE(address.getIndex());
            }

            if (address.getRegionShort() != null) {
                RGC region = RGC.getRegionByCode(address.getRegionShort().getCodereg());
                if (region == null) {
                    region = RGC.Other;
                }
                maCad.setREGION(region);
            }

            if (StringUtils.isNotBlank(address.getAreaName())) {
                maCad.setDISTRICT(address.getAreaName());
            }

            maCad.setCITY(getCity(address));

            if (StringUtils.isNotBlank(address.getPlaceName())) {
                maCad.setSETTLEMENT(address.getPlaceName());
            }

            if (StringUtils.isNotBlank(address.getStreetName())) {
                maCad.setSTREET(address.getStreetName());
            }

            if (StringUtils.isNotBlank(address.getHouse())) {
                maCad.setHOUSE(address.getHouse());
            }

            if (StringUtils.isNotBlank(address.getBuilding())) {
                maCad.setBUILD(address.getBuilding());
            }

            if (StringUtils.isNotBlank(address.getCorpus())) {
                maCad.setCONSTR(address.getCorpus());
            }

            if (StringUtils.isNotBlank(address.getFlat())) {
                maCad.setAPART(address.getFlat());
            }

            ma.setMA_CAD(maCad);
        }


        // адрес места работы
        address = peopleBean.findAddressActive(peopleMain, BaseAddress.WORKING_ADDRESS);
        if (address != null) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD maEad = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD();

            maEad.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(),
                    RefHunterRule.HUNTER_OBJECT_EMPLOYMENT_ADDRESS));

            maEad.setCOUNTRY(CTR.RU);

            if (StringUtils.isNotBlank(address.getIndex())) {
                maEad.setPOST_CODE(address.getIndex());
            }

            if (address.getRegionShort() != null) {
                RGC region = RGC.getRegionByCode(address.getRegionShort().getCodereg());
                if (region == null) {
                    region = RGC.Other;
                }
                maEad.setREGION(region);
            }

            if (StringUtils.isNotBlank(address.getAreaName())) {
                maEad.setDISTRICT(address.getAreaName());
            }

            maEad.setCITY(getCity(address));

            if (StringUtils.isNotBlank(address.getPlaceName())) {
                maEad.setSETTLEMENT(address.getPlaceName());
            }

            if (StringUtils.isNotBlank(address.getStreetName())) {
                maEad.setSTREET(address.getStreetName());
            }

            if (StringUtils.isNotBlank(address.getHouse())) {
                maEad.setHOUSE(address.getHouse());
            }

            if (StringUtils.isNotBlank(address.getBuilding())) {
                maEad.setBUILD(address.getBuilding());
            }

            if (StringUtils.isNotBlank(address.getCorpus())) {
                maEad.setCONSTR(address.getCorpus());
            }

            if (StringUtils.isNotBlank(address.getFlat())) {
                maEad.setAPART(address.getFlat());
            }

            ma.setMA_EAD(maEad);
        }
    }

    /**
     * Параметр City - передавать только объект с типом город, при этом:
     * <ul>
     * <li>при наличие только в уровне 1 (Федеральный) – передаем город из 1 уровня
     * <li>при наличии в 1 и 3 уровне или при наличии города только в 3 уровне – передаем город из уровня 3
     * <li>аналогично вышеописанному пункту действуем с городом 4 уровня, при его наличии – в  city передаем город из 4 уровня
     * <li>при отсутствии города – в city ничего не передаем.
     * </ul>
     * @param address адрес
     * @return актуальный город
     */
    private String getCity(AddressEntity address) {
        if (address.getCityName() != null) {
            return address.getCityName();
        }
        AddrObj region = fiasService.findAddressObject(address.getRegion());
        if (region.getShortName().equals(CITY_SHORT_NAME)) {
            return region.getFormalName();
        }

        return null;
    }

    private void fillEmployment(BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma, CreditRequestEntity creditRequest,
            PeopleMainEntity peopleMain) throws ActionException {
        EmploymentEntity employment = peopleBean.findEmployment(peopleMain, creditRequest,
                referenceBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);
        if (employment == null) {
            return;
        }

        ma.setINC_DOC(new BigDecimal(employment.getSalary()).toBigInteger());
        ma.setINC_ADD(new BigDecimal(employment.getExtsalary()).toBigInteger());
        ma.setINC_TM(new BigDecimal(employment.getSalary() + employment.getExtsalary()).toBigInteger());

        BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP maEmp = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP();
        maEmp.setSTATUS(getStatus(creditRequest.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_EMPLOYMENT));
        maEmp.setORG_NAME(employment.getPlaceWork());
        ma.setMA_EMP(maEmp);
    }

    private void fillContacts(BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma, CreditRequestEntity creditRequestId,
            PeopleMainEntity peopleMain) throws ActionException {
        // телефон по месту прописки
        String tel = peopleBean.findContactClient(PeopleContact.CONTACT_HOME_PHONE, peopleMain.getId());
        if (StringUtils.isNotBlank(tel)) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL maRtel = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL();
            maRtel.setSTATUS(getStatus(creditRequestId.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_REGISTER_PHONE));
            maRtel.setTEL_NUM(tel);
            ma.setMA_RTEL(maRtel);
        }

        // телефон по месту фактического проживания
        tel = peopleBean.findContactClient(PeopleContact.CONTACT_HOME_REGISTER_PHONE, peopleMain.getId());
        if (StringUtils.isNotBlank(tel)) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL maCtel = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL();
            maCtel.setSTATUS(getStatus(creditRequestId.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_RESIDENCE_PHONE));
            maCtel.setTEL_NUM(tel);
            ma.setMA_CTEL(maCtel);
        }

        // телефон мобильный
        tel = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, peopleMain.getId());
        if (StringUtils.isNotBlank(tel)) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL maMtel = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL();
            maMtel.setSTATUS(getStatus(creditRequestId.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_MOBILE_PHONE));
            maMtel.setTEL_NUM(tel);
            ma.getMA_MTEL().add(maMtel);
        }

        // телефон рабочий
        tel = peopleBean.findContactClient(PeopleContact.CONTACT_WORK_PHONE, peopleMain.getId());
        if (StringUtils.isNotBlank(tel)) {
            for (int i = 0; i < tel.length(); i++) {
                if (!Character.isDigit(tel.charAt(i))) {
                    tel = tel.substring(0, i);
                    break;
                }
            }
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL maEtel = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL();
            maEtel.setSTATUS(getStatus(creditRequestId.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_EMPLOYMENT_PHONE));
            maEtel.setTEL_NUM(tel);
            ma.getMA_ETEL().add(maEtel);
        }

        // e-mail
        String email = peopleBean.findContactClient(PeopleContact.CONTACT_EMAIL, peopleMain.getId());
        if (StringUtils.isNotBlank(email)) {
            BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA maEma = new BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA();
            maEma.setSTATUS(getStatus(creditRequestId.getId(), peopleMain.getId(), RefHunterRule.HUNTER_OBJECT_EMAIL));
            maEma.setEMAIL(email);
            ma.setMA_EMA(maEma);
        }
    }

    private ENS getStatus(Integer creditRequestId, Integer peopleMainId, String hunterObjectCode) throws ActionException {
        List<AntifraudOccasion> antifraudOccasions = antifraudOccasionDAO.find(creditRequestId, peopleMainId,
                hunterObjectCode, null, 1, null);
        if (antifraudOccasions == null || antifraudOccasions.isEmpty()) {
            return ENS.Clear;
        }
        if (antifraudOccasions.size() > 1) {
            throw new ActionException(String.format("Найдено более одного AntifraudOccasion." +
                    "creditRequestId = %s, peopleMainId = %s, hunterObjectCode = %s",
                    creditRequestId, peopleMainId, hunterObjectCode),
                    Type.BUSINESS, ResultType.FATAL);
        }

        return ENS.getEnsByCode(antifraudOccasions.get(0).getStatus().getCodeInteger());
    }

    private ResultBlock unmarshallResponse(String response) throws ActionException {
        XMLInputFactory xif = XMLInputFactory.newFactory();

        XMLStreamReader xsr = null;
        String matchResult;
        try (StringReader reader = new StringReader(response)) {
            xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag();
            while(!xsr.getLocalName().equals("MatchResponse")) {
                xsr.nextTag();
            }

            Unmarshaller unmarshaller = jaxbContextMatchResponse.createUnmarshaller();
            matchResult = unmarshaller.unmarshal(xsr, MatchResponse.class).getValue().getMatchResult();

            xsr.close();
        } catch (XMLStreamException ex) {
            throw new ActionException("Не удалось распарсить SoapEnvelope", Type.TECH, ResultType.FATAL);
        } catch (JAXBException ex) {
            throw new ActionException("Не удалось создать unmarshaller для MatchResponse", Type.TECH, ResultType.FATAL);
        } finally {
            if (xsr != null) {
                try {
                    xsr.close();
                } catch (XMLStreamException ex) {
                    LOG.severe("Ошибка при закрытии XMLStreamReader "+ ex);
                }
            }
        }

        try (StringReader reader = new StringReader(matchResult)) {
            Unmarshaller unmarshaller = jaxbContextResult.createUnmarshaller();
            return (ResultBlock) unmarshaller.unmarshal(reader);
        } catch (JAXBException ex) {
            throw new ActionException("Не удалось создать unmarshaller для ResultBlock", Type.TECH, ResultType.FATAL);
        }
    }

    private void saveSuspicions(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            ResultBlock resultBlock) {

        if (resultBlock.getMatchSummary().getRules() == null) {
            return;
        }

        for (RulesType.Rule rule : resultBlock.getMatchSummary().getRules().getRule()) {
            AntifraudSuspicionEntity entity = antifraudSuspicionDAO.buildSuspicion(creditRequest.getId(),
                    peopleMain.getId(), partner.getId(), null, null, null, null, rule.getScore().doubleValue(),
                    new Date(), rule.getRuleID());
            antifraudSuspicionDAO.saveSuspicion(entity);
        }
    }

    private String doChangePassword(ChangePassword changePassword, String url, SSLContext sslContext)
            throws IOException, KassaException {
        Map<String, String> params = new HashMap<>();
        params.put("Encoding", XmlUtils.ENCODING_UTF);
        params.put("Content-Type", MimeTypeKeys.XML);
        params.put("SOAPAction", "http://www.e-i.ru/WebServices/changePassword");
        String message = String.format(SOAP_ENV_CHANGEPASSWORD, changePassword.getUsername(),
                changePassword.getOldpassword(), changePassword.getNewpassword());

        LOG.info(String.format("ОКБ Национальный Хантер, сообщение для смены пароля: %s", message));
        return new String(HTTPUtils.sendHttp(HttpMethod.POST , url, message.getBytes(), params, sslContext),
                XmlUtils.ENCODING_UTF);
    }

    private int unmarshallChangePasswordResponse(String response) throws ActionException {
        XMLInputFactory xif = XMLInputFactory.newFactory();

        XMLStreamReader xsr = null;

        try (StringReader reader = new StringReader(response)) {
            xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag();
            while(!xsr.getLocalName().equals("changePasswordResponse")) {
                xsr.nextTag();
            }

            Unmarshaller unmarshaller = jaxbContextChangePasswordResponse.createUnmarshaller();
            ChangePasswordResponse changePasswordResponse = unmarshaller.unmarshal(xsr, ChangePasswordResponse.class).getValue();
            int result = changePasswordResponse.getChangePasswordResult();
            xsr.close();

            return result;
        } catch (XMLStreamException ex) {
            throw new ActionException("Не удалось распарсить SoapEnvelope", Type.TECH, ResultType.FATAL);
        } catch (JAXBException ex) {
            throw new ActionException("Не удалось создать unmarshaller для ChangePasswordResponse",
                    Type.TECH, ResultType.FATAL);
        } finally {
            if (xsr != null) {
                try {
                    xsr.close();
                } catch (XMLStreamException ex) {
                    LOG.severe("Ошибка при закрытии XMLStreamReader"+ ex);
                }
            }
        }
    }
}
