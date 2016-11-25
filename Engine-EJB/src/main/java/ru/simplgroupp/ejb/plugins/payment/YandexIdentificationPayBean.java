
package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.admnkz.crypto.data.Settings;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexIdentificationPayBeanLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CountryEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Плугин для передачи займа через yandex с передачей данных пользователя
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class YandexIdentificationPayBean extends PaymentProcessorBean implements YandexIdentificationPayBeanLocal {

    private static final Logger logger = LoggerFactory.getLogger(YandexIdentificationPayBean.class);

    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final DecimalFormat df2 = new DecimalFormat("#########0.00");

    static {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        df2.setDecimalFormatSymbols(dfs);
    }

    private static final String testDepositionUri = "/webservice/deposition/api/testIdentificationDeposition";

    private static final String makeDepositionUri = "/webservice/deposition/api/makeIdentificationDeposition";

    private static final String balanceUri = "webservice/deposition/api/balance";

    @EJB
    private ICryptoService cryptoService;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    WorkflowBeanLocal workflow;

    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
                                     PluginExecutionContext context) throws ActionException {
        logger.info("Вызов sendSingleRequest " + businessObjectId);
        PaymentEntity payment = emMicro.find(PaymentEntity.class, businessObjectId);
        if (payment == null) {
            logger.warn("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }
        return sendSingleRequest(payment, context);
    }

    public boolean sendSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        String request;
        String response;
        try {
            request = buildSingleRequest(payment, context);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            response = doSingleRequest(request, context);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseSingleResponse(payment, response, context);
        } catch (Exception e) {
            logger.error("Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        return !PaymentStatus.SENDED.equals(payment.getStatus());
    }

    @Override
    public String status(Integer paymentId, PluginExecutionContext context) throws ActionException {
        PaymentEntity payment = emMicro.find(PaymentEntity.class, paymentId);
        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();
        if (payment == null) {
            logger.warn("PaymentEntity with id " + paymentId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }
        String request;
        try {
            request = buildSingleRequest(payment, context);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            String cryptedResponse= doSingleRequest(request, context);
            String response;
            try {
                response = decrypt(config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings
                        .YANDEX_PAY_SIGN_CLIENT_TEST, cryptedResponse);
                
            } catch (CryptoException e) {
                logger.error("Ошибка разбора подписи", e);
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Ошибка разбора подписи", Type.TECH,
                        ResultType.NON_FATAL, e);
            }
            return response;
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<balanceRequest agentId=\"").append(config.getAgentId()).append("\"")
                .append(" clientOrderId=\"").append("b").append(System.currentTimeMillis()).append("\"")
                .append(" requestDT=\"").append(DATETIME_FORMAT.format(new Date())).append("\"")
                .append("/>")
        ;
        logger.info(xml.toString());

        String request;
        try {
            request = new String(Base64.encodeBase64(cryptoService.signYandexCMS(xml.toString().getBytes(),
                            config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings
                                    .YANDEX_PAY_CLIENT_TEST),
                    true));
            request = "-----BEGIN PKCS7-----\n" + request + "-----END PKCS7-----";
        } catch (Exception e) {
            logger.error("Ошибка подписи ", e);
            throw new KassaException("Ошибка подписи", e);
        }


        SSLContext sslContext;
        try {
            if (config.isUseWork()) {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT, CryptoSettings.YANDEX_PAY_SERVER);
            } else {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT_TEST,
                        CryptoSettings.YANDEX_PAY_SERVER_TEST);
            }
        } catch (CryptoException e) {
            logger.error("Не удалось создать sslContext ", e);
            throw new KassaException("Не удалось создать sslContext ", e);
        }

        String url = (config.isUseWork() ? config.getWorkUrl() : config.getTestUrl()) + balanceUri;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/pkcs7-mime");

        String response;
        try {
            response = new String(HTTPUtils.sendHttp("POST", url,
                    request.getBytes(), headers, sslContext));
            response = decrypt(config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings
                    .YANDEX_PAY_SIGN_CLIENT_TEST, response);
            logger.info(response);
        } catch (IOException e) {
            logger.error("Ошибка отправки запроса " + e);
            throw new KassaException("Ошибка отправки запроса", e);
        } catch (CryptoException e) {
            logger.error("Ошибка расшифровки ответа ", e);
            throw new KassaException("Ошибка расшифровки ответа", e);
        }

        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "balanceResponse");

            Integer status = Convertor.toInteger((element.getAttribute("status")));

            if (status == 0) {
                double balance = Convertor.toDouble(element.getAttribute("balance"));
                paymentService.saveBalance(null, Partner.YANDEX, Account.YANDEX_TYPE, balance, new Date());
                return balance;
            } else {
                logger.error("Неуспешный запрос баланса, статус " + status);
                throw new KassaException("Неуспешный запрос баланса, статус " + status);
            }

        } catch (KassaException e) {
            logger.error("Произошла ошибка ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Неверный XML ", e);
            throw new KassaException("Неверный XML", e);
        }
    }

    private String buildSingleRequest(PaymentEntity payment, PluginExecutionContext context)
            throws ActionException, ParserConfigurationException, TransformerException {

        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        CreditEntity credit = payment.getCreditId();

        AccountEntity borrowerAccount = credit.getCreditRequestId().getAccountId();

        Document doc = XmlUtils.createDoc();

        Element rootElement = doc.createElement("testIdentificationDepositionRequest");
        doc.appendChild(rootElement);
        rootElement.setAttribute("agentId", config.getAgentId());
        rootElement.setAttribute("clientOrderId", String.valueOf(payment.getId()));
        rootElement.setAttribute("requestDT", DATETIME_FORMAT.format(payment.getCreateDate()));
        rootElement.setAttribute("dstAccount", borrowerAccount.getAccountnumber());
        rootElement.setAttribute("amount", df2.format(payment.getAmount()));
        rootElement.setAttribute("currency", config.isUseWork() ? "643" : "10643");
        rootElement.setAttribute("contract",
                "Предоставление процентного займа № " + credit.getCreditRequestId().getUniquenomer() + " от " +
                        credit.getDatabeg() + " г.");
        rootElement.appendChild(buildIdentification(doc, credit.getPeopleMainId()));

        String xml = XmlUtils.docToString(doc);

        logger.info(xml.toString());

        try {
            String result = new String(Base64.encodeBase64(cryptoService.signYandexCMS(xml.toString().getBytes(),
                            config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings
                                    .YANDEX_PAY_CLIENT_TEST),
                    true));
            return "-----BEGIN PKCS7-----\n" + result + "-----END PKCS7-----";
        } catch (Exception e) {
            logger.error("Ошибка подписи", e);
            throw new ActionException(ErrorKeys.CANT_CREATE_SIGN, null, Type.TECH, ResultType.FATAL, e);
        }
    }

    private Element buildIdentification(Document doc, PeopleMainEntity people) throws
            ParserConfigurationException {
        DocumentEntity document = peopleBean.findPassportActive(people);
        PeoplePersonalEntity personal = peopleBean.findPeoplePersonalActive(people);
        AddressEntity address = peopleBean.findAddressActive(people, BaseAddress.REGISTER_ADDRESS);

        Element identificationElement = doc.createElement("identification");

        if (document != null) {
            identificationElement.setAttribute("docType", adaptDocumentType(document.getDocumenttypeId().getCodeinteger()));
            identificationElement.setAttribute("docNumber", document.getSeries() + " " + document.getNumber());
            identificationElement.setAttribute("issueDate", DATE_FORMAT.format(document.getDocdate()));
            identificationElement.setAttribute("authorityName", document.getDocorg());
            identificationElement.setAttribute("authorityCode", document.getDocorgcode());
        }
        if (address != null) {
            identificationElement.setAttribute("residence", address.getDescription());
        }
        if (personal != null) {
            identificationElement.setAttribute("nationality", adaptNationality(personal.getCitizen()));
            identificationElement.setAttribute("birthDate", DATE_FORMAT.format(personal.getBirthdate()));
            identificationElement.setAttribute("birthPlace", personal.getBirthplace());
            identificationElement.setAttribute("surname", personal.getSurname());
            identificationElement.setAttribute("name", personal.getSurname());
            identificationElement.setAttribute("patronymic", personal.getMidname());
        }
        return identificationElement;
    }

    private String adaptNationality(CountryEntity country) {
        switch (country.getCode()) {
            case BaseAddress.COUNTRY_RUSSIA:
                return "643";
        }
        return "";
    }

    private String adaptDocumentType(Integer code) {
        switch (code) {
            case Documents.PASSPORT_RF:
                return "21";
        }
        return "";
    }

    private String doSingleRequest(String request, PluginExecutionContext context) throws Exception {

        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        SSLContext sslContext;
        try {
            if (config.isUseWork()) {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT, CryptoSettings.YANDEX_PAY_SERVER);
            } else {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT_TEST,
                        CryptoSettings.YANDEX_PAY_SERVER_TEST);
            }
        } catch (CryptoException e) {
            logger.error("Не удалось создать ssl context ", e);
            throw new KassaException("Не удалось создать sslContext " + e);
        }

        String url = (config.isUseWork() ? config.getWorkUrl() : config.getTestUrl()) + testDepositionUri;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/pkcs7-mime");
        byte[] response = HTTPUtils.sendHttp("POST", url, request.getBytes(), headers, sslContext);
        return response == null ? null : new String(response);
    }

    private SSLContext createSSLContext(String clientCert, String serverCert) throws CryptoException {
        Settings s = cryptoService.getSettings(serverCert);
        org.admnkz.crypto.data.Certificate ca = s.getCertificate();

        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(Base64
                    .decodeBase64(ca.getBody())));

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustks = KeyStore.getInstance("JKS");
            trustks.load(null);

            trustks.setCertificateEntry("ca", cert);

            tmf.init(trustks);

            SSLContext context = SSLContext.getInstance("SSLv3");

            s = cryptoService.getSettings(clientCert);
            org.admnkz.crypto.data.Certificate certificate = s.getCertificate();

            cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64
                    (certificate.getBody())));

            KeyFactory keyFac8 = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec pvks = new PKCS8EncodedKeySpec(Base64.decodeBase64(certificate.getPrivateKey()));
            PrivateKey key = keyFac8.generatePrivate(pvks);

            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null);
            keystore.setCertificateEntry("alias", cert);
            keystore.setKeyEntry("key-alias", key, "".toCharArray(), new X509Certificate[]{cert});

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, "".toCharArray());

            KeyManager[] km = kmf.getKeyManagers();


            context.init(km, tmf.getTrustManagers(), null);
            return context;
        } catch (Exception e) {
            logger.error("Не удалось создать SSLContext", e);
            throw new CryptoException("Не удалось создать SSLContext " + e, e);
        }
    }

    protected void parseSingleResponse(PaymentEntity payment, String response, PluginExecutionContext context) throws
            ActionException {

        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        try {
            response = decrypt(config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings
                    .YANDEX_PAY_SIGN_CLIENT_TEST, response);
            logger.info("Response: " + response);
        } catch (CryptoException e) {
            logger.error("Ошибка разбора подписи", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Ошибка разбора подписи", Type.TECH,
                    ResultType.NON_FATAL, e);
        }
        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "testIdentificationDepositionResponse");

            Integer status = Convertor.toInteger(element.getAttribute("status"));

            if (status == 0) {
                Integer id = Convertor.toInteger(element.getAttribute("clientOrderId"));

                if (!payment.getId().equals(id)) {
                    logger.error("Неверный номер платежа в XML - отправили " + payment.getId() + " получили " + id);
                    handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML",
                            Type.TECH, ResultType.NON_FATAL));
                    throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH,
                            ResultType.NON_FATAL, null);
                }

                payment.setProcessDate(DATETIME_FORMAT.parse(element.getAttribute("processedDT")));
                payment.setStatus(PaymentStatus.SENDED);

            } else if (status == 1) {
                logger.error("Произошла нефатальная ошибка при проведении платежа, повторяем ");
                handleError(payment, DATETIME_FORMAT.parse(element.getAttribute("processedDT")), new ExceptionInfo(ErrorKeys.WAIT,
                        "",
                        Type.BUSINESS, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
            } else {

                Integer errorCode = Convertor.toInteger(element.getAttribute("error"));
                ExceptionInfo errorInfo = mapError(errorCode);
                logger.error("Произошла ошибка при проведении платежа {}: {}, {}", payment.getId(), errorCode, errorInfo);
                handleError(payment, DATETIME_FORMAT.parse(element.getAttribute("processedDT")), errorInfo);
                throw new ActionException(errorInfo.getCode(), errorInfo.getMessage(), errorInfo.getType(),
                        errorInfo.getResultType(), null);
            }

        } catch (ActionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Неверный XML", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }

    }

    private String decrypt(String clientCert, String sign) throws CryptoException {
        BufferedReader reader = new BufferedReader(new StringReader(sign));
        StringBuilder data = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.contains("PKCS7")) {
                    data.append(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            logger.error("Произошла ошибка при расшифровании сообщения " + e);

        }

        return new String(cryptoService.decryptCMS(Base64.decodeBase64(data.toString().getBytes()), clientCert));
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
                                       PluginExecutionContext context) throws ActionException {
        PaymentEntity payment = emMicro.find(PaymentEntity.class, businessObjectId);

        logger.info("Обрабатывается платеж PaymentEntity with id " + businessObjectId);
        if (payment == null) {
            logger.error("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }

        return querySingleResponse(payment, context);
    }

    public boolean querySingleResponse(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        String request;
        String response;
        try {
            request = buildQuerySingleResponse(payment, context);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {

            response = doQueryRequest(request, context);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseQuerySingleResponse(payment, response, context);
        } catch (Exception e) {
            logger.error("Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        logger.info("Статус платежа после зачисления " + payment.getId() + ": " + payment.getStatus());
        return PaymentStatus.SUCCESS.equals(payment.getStatus());
    }

    private String doQueryRequest(String request, PluginExecutionContext context) throws Exception {
        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        SSLContext sslContext;
        try {
            if (config.isUseWork()) {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT, CryptoSettings.YANDEX_PAY_SERVER);
            } else {
                sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT_TEST,
                        CryptoSettings.YANDEX_PAY_SERVER_TEST);
            }
        } catch (CryptoException e) {
            logger.error("Не удалось создать sslContext", e);
            throw new KassaException("Не удалось создать sslContext " + e);
        }

        String url = (config.isUseWork() ? config.getWorkUrl() : config.getTestUrl()) + makeDepositionUri;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/pkcs7-mime");
        byte[] response = HTTPUtils.sendHttp("POST", url, request.getBytes(), headers, sslContext);
        return response == null ? null : new String(response);
    }

    private void parseQuerySingleResponse(PaymentEntity payment, String response,
                                          PluginExecutionContext context) throws ActionException {
        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        try {
            response = decrypt(config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings
                    .YANDEX_PAY_SIGN_CLIENT_TEST, response);
            logger.info("Response: " + response);
        } catch (CryptoException e) {
            logger.error("Ошибка разбора подписи ", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Ошибка разбора подписи", Type.TECH,
                    ResultType.NON_FATAL, e);
        }

        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "makeIdentificationDepositionResponse");

            Integer status = Convertor.toInteger(element.getAttribute("status"));

            if (status == 0) {
                Integer id = Convertor.toInteger(element.getAttribute("clientOrderId"));

                if (!payment.getId().equals(id)) {
                    logger.error("Неверный номер платежа в XML - отправили " + payment.getId() + " получили " + id);
                    handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML",
                            Type.TECH, ResultType.NON_FATAL));
                    throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH,
                            ResultType.NON_FATAL, null);
                }

                handleSuccess(payment, DATETIME_FORMAT.parse(element.getAttribute("processedDT")));

            } else if (status == 1) {
                logger.error("Произошла нефатальная ошибка при проведении платежа, повторяем ");
                handleError(payment, DATETIME_FORMAT.parse(element.getAttribute("processedDT")), new ExceptionInfo(ErrorKeys.WAIT,
                        "",
                        Type.BUSINESS, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
            } else {
                Integer errorCode = Convertor.toInteger(element.getAttribute("error"));
                ExceptionInfo errorInfo = mapError(errorCode);
                logger.error("Произошла ошибка при проведении платежа " + errorCode);
                handleError(payment, DATETIME_FORMAT.parse(element.getAttribute("processedDT")), errorInfo);
                throw new ActionException(errorInfo.getCode(), errorInfo.getMessage(), errorInfo.getType(),
                        errorInfo.getResultType(), null);
            }

        } catch (ActionException e) {
            logger.error("Ошибка разбора ответа", e);
            throw e;
        } catch (Exception e) {
            logger.error("Неверный XML", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    private String buildQuerySingleResponse(PaymentEntity payment, PluginExecutionContext context)
            throws ActionException, ParserConfigurationException, TransformerException {
        YandexIdentificationPayPluginConfig config = (YandexIdentificationPayPluginConfig) context.getPluginConfig();

        CreditEntity credit = payment.getCreditId();
        AccountEntity borrowerAccount = credit.getCreditRequestId().getAccountId();

        Document doc = XmlUtils.createDoc();

        Element rootElement = doc.createElement("makeIdentificationDepositionRequest");
        doc.appendChild(rootElement);

        rootElement.setAttribute("agentId", config.getAgentId());
        rootElement.setAttribute("clientOrderId", String.valueOf(payment.getId()));
        rootElement.setAttribute("requestDT", DATETIME_FORMAT.format(payment.getCreateDate()));
        rootElement.setAttribute("dstAccount", borrowerAccount.getAccountnumber());
        rootElement.setAttribute("amount", df2.format(payment.getAmount()));
        rootElement.setAttribute("currency", config.isUseWork() ? "643" : "10643");
        rootElement.setAttribute("contract",
                "Предоставление процентного займа № " + credit.getCreditRequestId().getUniquenomer() + " от " +
                        credit.getDatabeg() + " г.");
        rootElement.appendChild(buildIdentification(doc, credit.getPeopleMainId()));

        String xml = XmlUtils.docToString(doc);
        logger.info(xml.toString());

        try {
            String result = new String(Base64.encodeBase64(cryptoService.signYandexCMS(xml.toString().getBytes(),
                            config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings
                                    .YANDEX_PAY_CLIENT_TEST),
                    true));
            return "-----BEGIN PKCS7-----\n" + result + "-----END PKCS7-----";
        } catch (Exception e) {
            logger.error("Ошибка подписи", e);
            throw new ActionException(ErrorKeys.CANT_CREATE_SIGN, null, Type.TECH, ResultType.FATAL, e);
        }
    }

    private ExceptionInfo mapError(int errorCode) {
        switch (errorCode) {
            case 10:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Ошибка синтаксического разбора XML-документа. " +
                        "Синтаксис документа нарушен, либо отсутствуют обязательные элементы XML.",
                        Type.BUSINESS, ResultType.FATAL);
            case 11:
                return new ExceptionInfo(ErrorKeys.INVALID_AGENT, "Отсутствует или неверно задан идентификатор " +
                        "Контрагента (agentId).",
                        Type.BUSINESS, ResultType.FATAL);
            case 12:
                return new ExceptionInfo(ErrorKeys.INVALID_AGENT, "Отсутствует или неверно задан идентификатор канала" +
                        " приема переводов (subAgentId).",
                        Type.BUSINESS, ResultType.FATAL);
            case 14:
                return new ExceptionInfo(ErrorKeys.INVALID_CURRENCY, "Отсутствует или неверно задана валюта " +
                        "(currency).",
                        Type.BUSINESS, ResultType.FATAL);
            case 15:
                return new ExceptionInfo(ErrorKeys.INVALID_DATE, "Отсутствует или неверно задано время формирования " +
                        "документа (requestDT).",
                        Type.BUSINESS, ResultType.FATAL);
            case 16:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Отсутствует или неверно задан идентификатор " +
                        "получателя средств (dstAccount).",
                        Type.BUSINESS, ResultType.FATAL);
            case 17:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT, "Отсутствует или неверно задана сумма (amount).",
                        Type.BUSINESS, ResultType.FATAL);
            case 18:
                return new ExceptionInfo(ErrorKeys.INVALID_ID, "Отсутствует или неверно задан номер транзакции " +
                        "(clientOrderId).",
                        Type.BUSINESS, ResultType.FATAL);
            case 19:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано поле текст контракта " +
                        "(contract).",
                        Type.BUSINESS, ResultType.FATAL);
            case 21:
                return new ExceptionInfo(ErrorKeys.ACCESS_DENIED, "Запрашиваемая операция запрещена для данного типа " +
                        "подключения Контрагента.",
                        Type.BUSINESS, ResultType.FATAL);
            case 26:
                return new ExceptionInfo(ErrorKeys.DUPLICATE_ORDER, "Операция с таким номером транзакции " +
                        "(clientOrderId), но другими параметрами уже выполнялась.",
                        Type.BUSINESS, ResultType.FATAL);
            case 50:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN, "Невозможно открыть криптосообщение, " +
                        "ошибка целостности пакета.",
                        Type.BUSINESS, ResultType.FATAL);
            case 51:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "АСП не подтверждена (данные подписи не совпадают с " +
                        "документом).",
                        Type.BUSINESS, ResultType.FATAL);
            case 53:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN, "Запрос подписан неизвестным Системе сертификатом.",
                        Type.BUSINESS, ResultType.FATAL);
            case 55:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN, "Истек срок действия сертификата ИС Контрагента.",
                        Type.BUSINESS, ResultType.FATAL);

            case 60:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "docType.",
                        Type.BUSINESS, ResultType.FATAL);
            case 61:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "docNumber.",
                        Type.BUSINESS, ResultType.FATAL);
            case 62:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "issueDate.",
                        Type.BUSINESS, ResultType.FATAL);
            case 63:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "authorityName.",
                        Type.BUSINESS, ResultType.FATAL);
            case 64:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "authorityCode.",
                        Type.BUSINESS, ResultType.FATAL);
            case 65:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра expirationDate.",
                        Type.BUSINESS, ResultType.FATAL);
            case 66:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "residence.",
                        Type.BUSINESS, ResultType.FATAL);
            case 67:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "nationality.",
                        Type.BUSINESS, ResultType.FATAL);
            case 68:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "birthDate.",
                        Type.BUSINESS, ResultType.FATAL);
            case 69:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "surname.",
                        Type.BUSINESS, ResultType.FATAL);
            case 70:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "name.",
                        Type.BUSINESS, ResultType.FATAL);
            case 71:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра patronymic.",
                        Type.BUSINESS, ResultType.FATAL);
            case 72:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра comment.",
                        Type.BUSINESS, ResultType.FATAL);
            case 73:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Отсутствует или неверно задано значение параметра " +
                        "birthPlace.",
                        Type.BUSINESS, ResultType.FATAL);

            case 40:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Счет закрыт.",
                        Type.BUSINESS, ResultType.FATAL);
            case 41:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Счет в Системе заблокирован. Данная операция для" +
                        " счета запрещена.",
                        Type.BUSINESS, ResultType.FATAL);
            case 42:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Счета с таким идентификатором не существует.",
                        Type.BUSINESS, ResultType.FATAL);
            case 43:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT, "Превышено ограничение на единовременно " +
                        "зачисляемую сумму.",
                        Type.BUSINESS, ResultType.FATAL);
            case 44:
                return new ExceptionInfo(ErrorKeys.MAX_TOTAL_AMOUNT, "Превышено ограничение на максимальную сумму " +
                        "зачислений за период времени.",
                        Type.BUSINESS, ResultType.FATAL);
            case 45:
                return new ExceptionInfo(ErrorKeys.LOW_BALANCE, "Недостаточно средств для проведения операции.",
                        Type.BUSINESS, ResultType.FATAL);
            case 46:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT, "Сумма операции слишком мала.",
                        Type.BUSINESS, ResultType.FATAL);

            case 30:
                return new ExceptionInfo(ErrorKeys.TECH_ERROR, "Технические проблемы на стороне Системы.",
                        Type.BUSINESS, ResultType.FATAL);
        }

        return new ExceptionInfo(ErrorKeys.UNKNOWN, "Неизвестная ошибка.",
                Type.BUSINESS, ResultType.FATAL);
    }

    @Override
    public String getSystemName() {
        return YandexIdentificationPayBeanLocal.SYSTEM_NAME;
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
        return EnumSet.of(SyncMode.ASYNC);
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.YANDEX_TYPE};
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId,
                              PluginExecutionContext context) throws ActionException {
        logger.warn("Метод executeSingle не поддерживается");
    }

    @Override
    public String getSystemDescription() {
        return YandexIdentificationPayBeanLocal.SYSTEM_DESCRIPTION;
    }

    @Override
    public Integer getPartnerId() {
        return Partner.YANDEX;
    }
}
