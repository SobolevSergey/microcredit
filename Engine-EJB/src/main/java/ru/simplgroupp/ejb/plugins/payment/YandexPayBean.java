package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.admnkz.crypto.data.Settings;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.BankEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleContact;
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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Плугин для передачи займа через yandex
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class YandexPayBean extends PaymentProcessorBean implements YandexPayBeanLocal {

	@EJB
	private PaymentDAO paymentDAO;

    private static final Logger logger = LoggerFactory.getLogger(YandexPayBean.class);

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private static final DecimalFormat df2 = new DecimalFormat("#########0.00");

    static {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        df2.setDecimalFormatSymbols(dfs);
    }

    private static final String RUBLE_CURRENCY = "643";

    private static final String TEST_CURRENCY  = "10643";

    private static final String testDepositionUri = "/webservice/deposition/api/testDeposition";

    private static final String makeDepositionUri = "/webservice/deposition/api/makeDeposition";

    private static final String balanceUri = "webservice/deposition/api/balance";

    private static final String idDataCheckUri = "webservice/iddata/api/idDataCheck";

    @EJB
    PeopleBeanLocal peopleBean;

    @EJB
    private ICryptoService cryptoService;

    @EJB
    private ReferenceBooksLocal referenceBooks;

    private RequestBuilderFactory requestBuilderFactory = new RequestBuilderFactory();

    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
            PluginExecutionContext context) throws ActionException {
        logger.info("Вызов sendSingleRequest " + businessObjectId);
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
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
    	 PaymentEntity payment = paymentDAO.getPayment(paymentId);
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();
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
            String cryptedResponse = doSingleRequest(request, context);
            String response;
            try {
                response = decrypt(
                        config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings.YANDEX_PAY_SIGN_CLIENT_TEST,
                        cryptedResponse);

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
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").append("<balanceRequest agentId=\"").append(
                config.getAgentId()).append("\"").append(" clientOrderId=\"").append("b").append(
                System.currentTimeMillis()).append("\"").append(" requestDT=\"").append(df.format(new Date())).append(
                "\"").append("/>");
        logger.info("Request: " + xml.toString());

        String request;
        try {
            request = new String(Base64.encodeBase64(cryptoService.signYandexCMS(xml.toString().getBytes(),
                            config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings.YANDEX_PAY_CLIENT_TEST),
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
            response = new String(HTTPUtils.sendHttp("POST", url, request.getBytes(), headers, sslContext));
            response = decrypt(
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings.YANDEX_PAY_SIGN_CLIENT_TEST,
                    response);
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

    @Override
    public boolean isWalletIdentified(String number, PluginExecutionContext context) throws ActionException {

        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        String id = "t" + System.currentTimeMillis();
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").append("<testDepositionRequest agentId=\"").append(
                config.getAgentId()).append("\"").append(" clientOrderId=\"").append(id).append("\"").append(
                " requestDT=\"").append(df.format(new Date())).append("\"").append(" dstAccount=\"").append(
                number).append("\"").append(" amount=\"").append(df2.format(100)).append("\"").append(
                " currency=\"").append(config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY).append("\"").append(" contract=\"").append(
                "Проверка кошелька").append("\"/>");
        logger.info(xml.toString());

        try {
            String request = new String(Base64.encodeBase64(cryptoService.signYandexCMS(xml.toString().getBytes(),
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings.YANDEX_PAY_CLIENT_TEST),
                    true));
            request = "-----BEGIN PKCS7-----\n" + request + "-----END PKCS7-----";

            String response = doSingleRequest(request, context);

            response = decrypt(
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings.YANDEX_PAY_SIGN_CLIENT_TEST,
                    response);
            logger.info("Response: " + response);

            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "testDepositionResponse");

            String identification = element.getAttribute("identification");
            return "identified".equals(identification);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    /**
     * Получение хеш кода персональных данных для проверки кошелька
     * @param surname - фамилия
     * @param name - имя
     * @param midname - отчество
     * @param docSeries - серия паспорта
     * @param docNumber - номер паспорта
     * @return base64 строка с хеш кодом
     */
    protected String getPersonalDataHashCode(String surname, String name, String midname, String docSeries, String docNumber) throws UnsupportedEncodingException {
        StringBuilder normalized = new StringBuilder();
        normalized.append(surname.toUpperCase().replace('Ё', 'Е')).append("_")
                .append(name.toUpperCase().replace('Ё', 'Е')).append("_")
                .append(midname.toUpperCase().replace('Ё', 'Е')).append("_")
                .append(docSeries).append(docNumber);
        logger.info("Normalized: " + normalized.toString());
        byte[] str = normalized.toString().getBytes("Cp1251");
        byte[] sha = DigestUtils.sha(str);
        byte[] b64 = Base64.encodeBase64(sha);
        return new String(b64);
    }

    protected byte[] postCheckWalletRequest(String url, Map<String, String> headers, SSLContext sslContext) throws ActionException, IOException {
        try {
            return HTTPUtils.sendHttp("POST", url, new byte[0], headers, sslContext);
        } catch (KassaException ke) {
            throw new ActionException(ke.getMessage(), ke);
        }
    }

    @Override
    public boolean checkWalletData(String number, PeopleMainEntity person, PluginExecutionContext context) throws ActionException {
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();
        if (config.isUseVerification()) {
            MultiValueMap<String, String> params;
            String url;
            try {
                params = new LinkedMultiValueMap<>();
                params.add("ACT_CD", URLEncoder.encode("idDataCheck", "UTF-8"));
                params.add("VERSION", URLEncoder.encode("2.50", "UTF-8"));
                params.add("DSTACNT_NR", URLEncoder.encode(number, "UTF-8"));

                PeoplePersonalEntity personal = peopleBean.findPeoplePersonalActive(person);
                DocumentEntity document = peopleBean.findPassportActive(person);

                StringBuilder iddata = new StringBuilder();
                iddata.append("<?xml version=\"1.0\" encoding=\"windows-1251\"?><ID_DATA><IDDOC_HASH>")
                        .append(getPersonalDataHashCode(personal.getSurname(), personal.getName(), personal.getMidname(), document.getSeries(), document.getNumber()))
                        .append("</IDDOC_HASH></ID_DATA>");
                params.add("ID_DATA", URLEncoder.encode(iddata.toString(), "UTF-8"));
                logger.info(iddata.toString());
                url = (config.isUseWork() ? config.getWorkUrl() : config.getTestUrl()) + idDataCheckUri;
                url = UriComponentsBuilder.newInstance().uri(new URI(url))
                        .queryParams(params).build().toUriString();
                logger.info("uri: " + url);
            } catch (Exception e) {
                logger.error("Не удалось сформировать запрос", e);
                throw new ActionException("Не удалось сформировать запрос ", e);
            }

            SSLContext sslContext = null;
            try {
                if (config.isUseWork()) {
                    sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT, CryptoSettings.YANDEX_PAY_SERVER);
                } else {
                    sslContext = createSSLContext(CryptoSettings.YANDEX_PAY_CLIENT_TEST,
                            CryptoSettings.YANDEX_PAY_SERVER_TEST);
                }
            } catch (Exception e) {
                logger.error("Не удалось создать sslContext ", e);
                throw new ActionException("Не удалось создать sslContext ", e);
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");

            String response;
            try {
                response = new String(postCheckWalletRequest(url, headers, sslContext), "Cp1251");
                logger.info(response);
            } catch (IOException e) {
                logger.error("Ошибка отправки запроса " + e);
                throw new ActionException("Ошибка отправки запроса", e);
            }

            try {
                Document doc = XmlUtils.getDocFromString(response);
                Element responseElement = XmlUtils.findElement(true, doc, "response");
                Integer status = Convertor.toInteger((responseElement.getAttribute("code")));

                if (status == 0) {
                    return true;
                } else {
                    Element error = XmlUtils.findElement(true, responseElement, "error");
                    String errorCode = null;
                    String msg = null;
                    String msgTech = null;
                    if (error != null) {
                        errorCode = error.getAttribute("code");
                        Element msgElement = XmlUtils.findElement(true, error, "message");
                        msg = (msgElement != null) ? msgElement.getTextContent() : null;
                        Element msgTechElement = XmlUtils.findElement(true, error, "tech-message");
                        msgTech = (msgTechElement != null) ? msgTechElement.getTextContent() : null;
                        if (104 == Convertor.toInteger(errorCode)) {
                            return false;
                        }
                    }
                    logger.error("Неуспешный запрос верификации кошелька, статус " + status + ". Код ошибки: " + errorCode + "; сообщение: " + msg + "; детали: " + msgTech);
                    throw new ActionException("Неуспешный запрос верификации кошелька, статус " + status, new Exception());
                }

            } catch (Exception e) {
                logger.error("Неверный XML ", e);
                throw new ActionException("Неверный XML", e);
            }
        } else {
            logger.info("Верификация данных кошелька отключена в опциях.");
            return true;
        }
    }

    private String buildSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        String request = requestBuilderFactory.getRequestBuilder(
                payment.getAccountId().getAccountTypeId().getCodeinteger()).buildTestDepositionRequest(payment, context);
        if (request == null) {
            logger.error("Ошибка создания запроса: неверный тип счёта");
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, null);
        }

        logger.info("Request: " + request);
        String result;
        try {
            result = new String(Base64.encodeBase64(cryptoService.signYandexCMS(request.getBytes(),
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings.YANDEX_PAY_CLIENT_TEST),
                    true));
            result = "-----BEGIN PKCS7-----\n" + result + "-----END PKCS7-----";
        } catch (Exception e) {
            logger.error("Ошибка подписи", e);
            throw new ActionException(ErrorKeys.CANT_CREATE_SIGN, null, Type.TECH, ResultType.FATAL, e);
        }
        return result;
    }

    private String doSingleRequest(String request, PluginExecutionContext context) throws KassaException, IOException {

        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

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
        return new String(HTTPUtils.sendHttp("POST", url, request.getBytes(), headers, sslContext));
    }

    protected SSLContext createSSLContext(String clientCert, String serverCert) throws CryptoException {
        Settings s = cryptoService.getSettings(serverCert);
        org.admnkz.crypto.data.Certificate ca = s.getCertificate();

        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(Base64.decodeBase64(ca.getBody())));

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustks = KeyStore.getInstance("JKS");
            trustks.load(null);

            trustks.setCertificateEntry("ca", cert);

            tmf.init(trustks);

            SSLContext context = SSLContext.getInstance("SSLv3");

            s = cryptoService.getSettings(clientCert);
            org.admnkz.crypto.data.Certificate certificate = s.getCertificate();

            cert = (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(Base64.decodeBase64(certificate.getBody())));

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

    protected void parseSingleResponse(PaymentEntity payment, String response, PluginExecutionContext context)
            throws ActionException {

        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        try {
            response = decrypt(
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings.YANDEX_PAY_SIGN_CLIENT_TEST,
                    response);
            logger.info("Response: " + response);
        } catch (CryptoException e) {
            logger.error("Ошибка разбора подписи", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Ошибка разбора подписи", Type.TECH, ResultType.NON_FATAL,
                    e);
        }
        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "testDepositionResponse");

            Integer status = Convertor.toInteger(element.getAttribute("status"));

            if (status == 0) {
                Integer id = Convertor.toInteger(element.getAttribute("clientOrderId"));

                if (!payment.getId().equals(id)) {
                    logger.error("Неверный номер платежа в XML - отправили " + payment.getId() + " получили " + id);
                    handleError(payment, new Date(),
                            new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
                    throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL,
                            null);
                }

                payment.setProcessDate(df.parse(element.getAttribute("processedDT")));
                payment.setStatus(PaymentStatus.SENDED);

            } else if (status == 1) {
                logger.error("Произошла нефатальная ошибка при проведении платежа, повторяем ");
                handleError(payment, df.parse(element.getAttribute("processedDT")),
                        new ExceptionInfo(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
            } else {

                Integer errorCode = Convertor.toInteger(element.getAttribute("error"));
                ExceptionInfo errorInfo = mapError(errorCode);
                logger.error("Произошла ошибка при проведении платежа {}: {}, {}", payment.getId(), errorCode,
                        errorInfo);
                handleError(payment, df.parse(element.getAttribute("processedDT")), errorInfo);
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
    	PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));

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
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

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

        return response == null ? "" : new String(response);
    }

    private void parseQuerySingleResponse(PaymentEntity payment, String response, PluginExecutionContext context)
            throws ActionException {
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        try {
            response = decrypt(
                    config.isUseWork() ? CryptoSettings.YANDEX_PAY_SIGN_CLIENT : CryptoSettings.YANDEX_PAY_SIGN_CLIENT_TEST,
                    response);
            logger.info("Response: " + response);
        } catch (CryptoException e) {
            logger.error("Ошибка разбора подписи ", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Ошибка разбора подписи", Type.TECH, ResultType.NON_FATAL,
                    e);
        }

        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "makeDepositionResponse");

            Integer status = Convertor.toInteger(element.getAttribute("status"));

            if (status == 0) {
                Integer id = Convertor.toInteger(element.getAttribute("clientOrderId"));

                if (!payment.getId().equals(id)) {
                    logger.error("Неверный номер платежа в XML - отправили " + payment.getId() + " получили " + id);
                    handleError(payment, new Date(),
                            new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
                    throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL,
                            null);
                }

                handleSuccess(payment, df.parse(element.getAttribute("processedDT")));

            } else if (status == 1) {
                logger.error("Произошла нефатальная ошибка при проведении платежа, повторяем ");
                handleError(payment, df.parse(element.getAttribute("processedDT")),
                        new ExceptionInfo(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
            } else {
                Integer errorCode = Convertor.toInteger(element.getAttribute("error"));
                ExceptionInfo errorInfo = mapError(errorCode);
                logger.error("Произошла ошибка при проведении платежа " + errorCode);
                handleError(payment, df.parse(element.getAttribute("processedDT")), errorInfo);
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
            throws ActionException {
        YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

        String request = requestBuilderFactory.getRequestBuilder(
                payment.getAccountId().getAccountTypeId().getCodeinteger()).buildMakeDepositionRequest(payment, context);

        if (request == null) {
            logger.error("Ошибка создания запроса: неверный тип счёта");
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, null);
        }
        logger.info("Request: " + request);

        String result;
        try {
            result = new String(Base64.encodeBase64(cryptoService.signYandexCMS(request.getBytes(),
                            config.isUseWork() ? CryptoSettings.YANDEX_PAY_CLIENT : CryptoSettings.YANDEX_PAY_CLIENT_TEST),
                    true));
            result = "-----BEGIN PKCS7-----\n" + result + "-----END PKCS7-----";
        } catch (Exception e) {
            logger.error("Ошибка подписи", e);
            throw new ActionException(ErrorKeys.CANT_CREATE_SIGN, null, Type.TECH, ResultType.FATAL, e);
        }
        return result;
    }

    private ExceptionInfo mapError(int errorCode) {
        switch (errorCode) {
            case 10:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Ошибка синтаксического разбора XML-документа. " +
                        "Синтаксис документа нарушен, либо отсутствуют обязательные элементы XML.", Type.BUSINESS,
                        ResultType.FATAL);
            case 11:
                return new ExceptionInfo(ErrorKeys.INVALID_AGENT,
                        "Отсутствует или неверно задан идентификатор " + "Контрагента (agentId).", Type.BUSINESS,
                        ResultType.FATAL);
            case 12:
                return new ExceptionInfo(ErrorKeys.INVALID_AGENT,
                        "Отсутствует или неверно задан идентификатор канала" + " приема переводов (subAgentId).",
                        Type.BUSINESS, ResultType.FATAL);
            case 14:
                return new ExceptionInfo(ErrorKeys.INVALID_CURRENCY,
                        "Отсутствует или неверно задана валюта " + "(currency).", Type.BUSINESS, ResultType.FATAL);
            case 15:
                return new ExceptionInfo(ErrorKeys.INVALID_DATE,
                        "Отсутствует или неверно задано время формирования " + "документа (requestDT).", Type.BUSINESS,
                        ResultType.FATAL);
            case 16:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT,
                        "Отсутствует или неверно задан идентификатор " + "получателя средств (dstAccount).",
                        Type.BUSINESS, ResultType.FATAL);
            case 17:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT, "Отсутствует или неверно задана сумма (amount).",
                        Type.BUSINESS, ResultType.FATAL);
            case 18:
                return new ExceptionInfo(ErrorKeys.INVALID_ID,
                        "Отсутствует или неверно задан номер транзакции " + "(clientOrderId).", Type.BUSINESS,
                        ResultType.FATAL);
            case 19:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано поле текст контракта " + "(contract).", Type.BUSINESS,
                        ResultType.FATAL);
            case 21:
                return new ExceptionInfo(ErrorKeys.ACCESS_DENIED,
                        "Запрашиваемая операция запрещена для данного типа " + "подключения Контрагента.",
                        Type.BUSINESS, ResultType.FATAL);
            case 26:
                return new ExceptionInfo(ErrorKeys.DUPLICATE_ORDER, "Операция с таким номером транзакции " +
                        "(clientOrderId), но другими параметрами уже выполнялась.", Type.BUSINESS, ResultType.FATAL);
            case 50:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN,
                        "Невозможно открыть криптосообщение, " + "ошибка целостности пакета.", Type.BUSINESS,
                        ResultType.FATAL);
            case 51:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "АСП не подтверждена (данные подписи не совпадают с " + "документом).", Type.BUSINESS,
                        ResultType.FATAL);
            case 53:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN, "Запрос подписан неизвестным Системе сертификатом.",
                        Type.BUSINESS, ResultType.FATAL);
            case 55:
                return new ExceptionInfo(ErrorKeys.INVALID_SIGN, "Истек срок действия сертификата ИС Контрагента.",
                        Type.BUSINESS, ResultType.FATAL);

            case 60:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "docType.", Type.BUSINESS,
                        ResultType.FATAL);
            case 61:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "docNumber.", Type.BUSINESS,
                        ResultType.FATAL);
            case 62:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "issueDate.", Type.BUSINESS,
                        ResultType.FATAL);
            case 63:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "authorityName.", Type.BUSINESS,
                        ResultType.FATAL);
            case 64:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "authorityCode.", Type.BUSINESS,
                        ResultType.FATAL);
            case 65:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра expirationDate.",
                        Type.BUSINESS, ResultType.FATAL);
            case 66:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "residence.", Type.BUSINESS,
                        ResultType.FATAL);
            case 67:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "nationality.", Type.BUSINESS,
                        ResultType.FATAL);
            case 68:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "birthDate.", Type.BUSINESS,
                        ResultType.FATAL);
            case 69:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "surname.", Type.BUSINESS,
                        ResultType.FATAL);
            case 70:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "name.", Type.BUSINESS,
                        ResultType.FATAL);
            case 71:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра patronymic.",
                        Type.BUSINESS, ResultType.FATAL);
            case 72:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST, "Неверно задано значение параметра comment.",
                        Type.BUSINESS, ResultType.FATAL);
            case 73:
                return new ExceptionInfo(ErrorKeys.BAD_REQUEST,
                        "Отсутствует или неверно задано значение параметра " + "birthPlace.", Type.BUSINESS,
                        ResultType.FATAL);

            case 40:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Счет закрыт.", Type.BUSINESS, ResultType.FATAL);
            case 41:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT,
                        "Счет в Системе заблокирован. Данная операция для" + " счета запрещена.", Type.BUSINESS,
                        ResultType.FATAL);
            case 42:
                return new ExceptionInfo(ErrorKeys.INVALID_ACCOUNT, "Счета с таким идентификатором не существует.",
                        Type.BUSINESS, ResultType.FATAL);
            case 43:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT,
                        "Превышено ограничение на единовременно " + "зачисляемую сумму.", Type.BUSINESS,
                        ResultType.FATAL);
            case 44:
                return new ExceptionInfo(ErrorKeys.MAX_TOTAL_AMOUNT,
                        "Превышено ограничение на максимальную сумму " + "зачислений за период времени.", Type.BUSINESS,
                        ResultType.FATAL);
            case 45:
                return new ExceptionInfo(ErrorKeys.LOW_BALANCE, "Недостаточно средств для проведения операции.",
                        Type.BUSINESS, ResultType.FATAL);
            case 46:
                return new ExceptionInfo(ErrorKeys.INVALID_AMOUNT, "Сумма операции слишком мала.", Type.BUSINESS,
                        ResultType.FATAL);
            case 48:
                return new ExceptionInfo(ErrorKeys.BANK_ERROR, "Ошибка запроса зачисления перевода на банковский " +
                        "счет, карту, мобильный телефон.", Type.BUSINESS, ResultType.FATAL);

            case 30:
                return new ExceptionInfo(ErrorKeys.TECH_ERROR, "Технические проблемы на стороне Системы.",
                        Type.BUSINESS, ResultType.FATAL);
        }

        return new ExceptionInfo(ErrorKeys.UNKNOWN, "Неизвестная ошибка.", Type.BUSINESS, ResultType.FATAL);
    }

    @Override
    public String getSystemName() {
        return YandexPayBeanLocal.SYSTEM_NAME;
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
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {

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
        return new int[]{Account.BANK_TYPE, Account.YANDEX_TYPE, Account.YANDEX_CARD_TYPE};
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        logger.warn("Метод executeSingle не поддерживается");
    }

    @Override
    public String getSystemDescription() {
        return YandexPayBeanLocal.SYSTEM_DESCRIPTION;
    }

    @Override
    public Integer getPartnerId() {
        return Partner.YANDEX;
    }

    private class RequestBuilderFactory {
        public RequestBuilder getRequestBuilder(int accountType) {
            switch (accountType) {
                case Account.YANDEX_TYPE:
                    return new WalletRequestBuilder();

                case Account.YANDEX_CARD_TYPE:
                    return new CardRequestBuilder();

                case Account.BANK_TYPE:
                    return new BankRequestBuilder();
            }
            return null;
        }
    }

    private interface RequestBuilder {
        String buildTestDepositionRequest(PaymentEntity payment, PluginExecutionContext context);

        String buildMakeDepositionRequest(PaymentEntity payment, PluginExecutionContext context);
    }

    private class WalletRequestBuilder implements RequestBuilder {

        public String buildTestDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();
            AccountEntity account = credit.getCreditRequestId().getAccountId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<testDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + account.getAccountnumber() + "\"" +
                    " amount=\"" + df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\"/>";
        }

        public String buildMakeDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();
            AccountEntity account = credit.getCreditRequestId().getAccountId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<makeDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + account.getAccountnumber() + "\"" +
                    " amount=\"" + df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\"/>";
        }
    }

    private class BankRequestBuilder implements RequestBuilder {

        private static final String DST_ACCOUNT = "2570066962077";
        @Override
        public String buildTestDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<testDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + DST_ACCOUNT + "\"" + " amount=\"" +
                    df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\">" + buildPaymentParams(payment) + "</testDepositionRequest>";
        }

        public String buildMakeDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<makeDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + DST_ACCOUNT + "\"" + " amount=\"" +
                    df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\">" + buildPaymentParams(payment) + "</makeDepositionRequest>";
        }

        private String buildPaymentParams(PaymentEntity payment) {
            CreditEntity credit = payment.getCreditId();
            AccountEntity account = credit.getCreditRequestId().getAccountId();
            PeopleMainEntity people = credit.getPeopleMainId();
            PeoplePersonalEntity personal = peopleBean.findPeoplePersonalActive(people);
            DocumentEntity document = peopleBean.findPassportActive(people);
            AddressEntity address = peopleBean.findAddressActive(people, FiasAddress.REGISTER_ADDRESS);
            String phone = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, people.getId());
            BankEntity bank = referenceBooks.getBank(account.getBik());

            StringBuilder xml = new StringBuilder();
            xml.append("<paymentParams>");
            if (personal != null) {
                xml.append("<pdr_lastName>").append(StringEscapeUtils.escapeXml11(personal.getSurname())).append("</pdr_lastName>");
                xml.append("<pdr_firstName>").append(StringEscapeUtils.escapeXml11(personal.getName())).append("</pdr_firstName>");
                xml.append("<pdr_middleName>").append(StringEscapeUtils.escapeXml11(personal.getMidname())).append("</pdr_middleName>");
            }
            xml.append("<BankName>").append(StringEscapeUtils.escapeXml11(account.getBankname())).append("</BankName>");
            xml.append("<BankCity>");
            if (bank != null && StringUtils.isNotBlank(bank.getAddress())) {
                xml.append(bank.getAddress().substring(0, bank.getAddress().indexOf(",")));
            }
            xml.append("</BankCity>");
            xml.append("<BankBIK>").append(StringEscapeUtils.escapeXml11(account.getBik())).append("</BankBIK>");
            xml.append("<BankCorAccount>").append(StringEscapeUtils.escapeXml11(account.getCorraccountnumber())).append("</BankCorAccount>");
            xml.append("<BankKPP>").append(StringEscapeUtils.escapeXml11(account.getKpp())).append("</BankKPP>");
            xml.append("<CustAccount>").append(StringEscapeUtils.escapeXml11(account.getAccountnumber())).append(
                    "</CustAccount>");
            xml.append("<payment_purpose></payment_purpose>");

//            if (personal != null) {
//                xml.append("<tmpLastName>").append(StringEscapeUtils.escapeXml11(personal.getSurname())).append("</tmpLastName>");
//                xml.append("<tmpFirstName>").append(StringEscapeUtils.escapeXml11(personal.getName())).append("</tmpFirstName>");
//                xml.append("<tmpMiddleName>").append(StringEscapeUtils.escapeXml11(personal.getMidname())).append("</tmpMiddleName>");
//                xml.append("<pdr_birthDate>").append(StringEscapeUtils.escapeXml11(DatesUtils.DATE_FORMAT_ddMMYYYY.format(personal.getBirthdate()))).append("</pdr_birthDate>");
//                xml.append("<pdr_birthPlace>").append(StringEscapeUtils.escapeXml11(personal.getBirthplace())).append("</pdr_birthPlace>");
//            }

            if (document != null) {
                xml.append("<pdr_docType>21</pdr_docType>");
                xml.append("<pdr_docNumber>").append(StringEscapeUtils.escapeXml11(document.getSeries())).append
                        (document.getNumber()).append("</pdr_docNumber>");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(document.getDocdate());
                xml.append("<pdr_docIssueYear>").append(calendar.get(Calendar.YEAR)).append("</pdr_docIssueYear>");
                xml.append("<pdr_docIssueMonth>").append(calendar.get(Calendar.MONTH)).append("</pdr_docIssueMonth>");
                xml.append("<pdr_docIssueDay>").append(calendar.get(Calendar.DAY_OF_MONTH)).append("</pdr_docIssueDay>");
                xml.append("<pdr_docIssuedBy>").append(StringEscapeUtils.escapeXml11(document.getDocorg())).append(
                        "</pdr_docIssuedBy>");
            }

//            if (address != null) {
//                xml.append("<pdr_country>643</pdr_country>");
//                xml.append("<pdr_postcode></pdr_postcode>");
//                xml.append("<pdr_city>").append(StringEscapeUtils.escapeXml11(address.getCityName())).append(
//                        "</pdr_city>");
//                xml.append("<pdr_address>").append(StringEscapeUtils.escapeXml11(address.getDescriptionFromStreet())).append("</pdr_address>");
//            }

            xml.append("<smsPhoneNumber>").append(StringEscapeUtils.escapeXml11(phone)).append("</smsPhoneNumber>");
            xml.append("<pof_offerAccepted>1</pof_offerAccepted>");
            xml.append("</paymentParams>");

            return xml.toString();
        }
    }

    private class CardRequestBuilder implements RequestBuilder {

        private static final String DST_ACCOUNT = "25700130535186";
        @Override
        public String buildTestDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<testDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + DST_ACCOUNT + "\"" + " amount=\"" +
                    df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\">" + buildPaymentParams(payment) + "</testDepositionRequest>";
        }

        public String buildMakeDepositionRequest(PaymentEntity payment, PluginExecutionContext context) {
            YandexPayPluginConfig config = (YandexPayPluginConfig) context.getPluginConfig();

            CreditEntity credit = payment.getCreditId();

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<makeDepositionRequest agentId=\"" +
                    config.getAgentId() + "\"" + " clientOrderId=\"" + payment.getId() + "\"" + " requestDT=\"" +
                    df.format(payment.getCreateDate()) + "\"" + " dstAccount=\"" + DST_ACCOUNT + "\"" + " amount=\"" +
                    df2.format(payment.getAmount()) + "\"" + " currency=\"" +
                    (config.isUseWork() ? RUBLE_CURRENCY : TEST_CURRENCY) + "\"" + " contract=\"" +
                    "Предоставление процентного займа № " + credit.getId() + " от " + credit.getDatabeg() + " г." +
                    "\">" + buildPaymentParams(payment) + "</makeDepositionRequest>";
        }

        private String buildPaymentParams(PaymentEntity payment) {
            CreditEntity credit = payment.getCreditId();
            AccountEntity account = credit.getCreditRequestId().getAccountId();
            PeopleMainEntity people = credit.getPeopleMainId();
            PeoplePersonalEntity personal = peopleBean.findPeoplePersonalActive(people);
            DocumentEntity document = peopleBean.findPassportActive(people);
            AddressEntity address = peopleBean.findAddressActive(people, FiasAddress.REGISTER_ADDRESS);
            String phone = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, people.getId());

            StringBuilder xml = new StringBuilder();
            xml.append("<paymentParams>");
            xml.append("<skr_destinationCardSynonim>").append(account.getCardSynonim()).append(
                    "</skr_destinationCardSynonim>");
            if (personal != null) {
                xml.append("<pdr_lastName>").append(StringEscapeUtils.escapeXml11(personal.getSurname())).append("</pdr_lastName>");
                xml.append("<pdr_firstName>").append(StringEscapeUtils.escapeXml11(personal.getName())).append("</pdr_firstName>");
                xml.append("<pdr_middleName>").append(StringEscapeUtils.escapeXml11(personal.getMidname())).append("</pdr_middleName>");
            }

            if (document != null) {
                xml.append("<pdr_docType>21</pdr_docType>");
                xml.append("<pdr_docNumber>").append(StringEscapeUtils.escapeXml11(document.getSeries())).append
                        (document.getNumber()).append("</pdr_docNumber>");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(document.getDocdate());
                xml.append("<pdr_docIssueYear>").append(calendar.get(Calendar.YEAR)).append("</pdr_docIssueYear>");
                xml.append("<pdr_docIssueMonth>").append(calendar.get(Calendar.MONTH)).append("</pdr_docIssueMonth>");
                xml.append("<pdr_docIssueDay>").append(calendar.get(Calendar.DAY_OF_MONTH)).append("</pdr_docIssueDay>");
                xml.append("<pdr_docIssuedBy>").append(StringEscapeUtils.escapeXml11(document.getDocorg())).append(
                        "</pdr_docIssuedBy>");
            }

            if (address != null) {
                xml.append("<pdr_country>643</pdr_country>");
                xml.append("<pdr_postcode></pdr_postcode>");
                xml.append("<pdr_city>").append(StringEscapeUtils.escapeXml11(address.getCityName())).append("</pdr_city>");
                xml.append("<pdr_address>").append(StringEscapeUtils.escapeXml11(address.getDescriptionFromStreet())).append("</pdr_address>");
            }

            xml.append("<smsPhoneNumber>").append(StringEscapeUtils.escapeXml11(phone)).append("</smsPhoneNumber>");
            xml.append("<pof_offerAccepted>1</pof_offerAccepted>");
            xml.append("</paymentParams>");

            return xml.toString();
        }
    }
}
