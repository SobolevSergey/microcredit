package ru.simplgroupp.ejb.plugins.payment;

import org.apache.commons.codec.digest.DigestUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.*;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.WinpayPayBeanLocal;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.util.DatesUtils;
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
import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * Реализация плугина win-pay
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WinpayPayBean extends PaymentProcessorBean implements WinpayPayBeanLocal {

    private static final Logger logger = LoggerFactory.getLogger(WinpayPayBean.class);

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private PaymentDAO paymentDAO;
    
    @Override
    public String getSystemName() {
        return WinpayPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return WinpayPayBeanLocal.SYSTEM_DESCRIPTION;
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
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
                                     PluginExecutionContext context) throws ActionException {
        logger.info("Вызов sendSingleRequest " + businessObjectId);
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.warn("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }
        return pay(payment, context);
    }

    public boolean pay(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        WinpayPayPluginConfig config = (WinpayPayPluginConfig) context.getPluginConfig();
        String request;
        String response;
        try {
            request = buildPayRequest(payment, config);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            response = doPayRequest(request, config);
            logger.info(response);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parsePayResponse(payment, response, config);
        } catch (Exception e) {
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            logger.error("Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        return !PaymentStatus.SENDED.equals(payment.getStatus());
    }

    private String buildPayRequest(PaymentEntity payment, WinpayPayPluginConfig config) throws ActionException {
        String date = DatesUtils.DATE_FORMAT_YYYY_MM_dd_HHmmss.format(new Date());
        String amount = Convertor.toString(payment.getAmount());

        CreditEntity credit = payment.getCreditId();
        AccountEntity cardAccount = credit.getCreditRequestId().getAccountId();
        if (cardAccount == null || cardAccount.getAccountTypeId().getCodeinteger() != Account.CARD_TYPE) {
            throw new ActionException(ErrorKeys.INVALID_ACCOUNT, null, Type.TECH, ResultType.FATAL, null);
        }
        String account = cardAccount.getAccountnumber();
        if (StringUtils.isEmpty(account)) {
            account = cardAccount.getCardnumber();
        }
        if (StringUtils.isEmpty(account)) {
        	throw new ActionException(ErrorKeys.INVALID_ACCOUNT, "Пустой номер счета", Type.TECH, ResultType.FATAL, null);
        }
        PeopleMainEntity peopleMain = credit.getPeopleMainId();
        String phone = peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, peopleMain.getId());
        String destination = "1";
        String hash = DigestUtils.md5Hex(payment.getId() + date + amount + phone + account + destination + config.getSecretKey());

        try {
            StringBuilder request = new StringBuilder();
            request.append("ID=").append(payment.getId()).append("&DT=").append(
                    URLEncoder.encode(date, "UTF-8")).append("&PHONE=").append(
                    URLEncoder.encode(phone, "UTF-8")).append("&AMOUNT=").append(
                    URLEncoder.encode(amount, "UTF-8")).append("&CLIENT=").append(
                    URLEncoder.encode(account, "UTF-8")).append("&DESTINATION=").append(destination).append(
                    "&HASH=").append(URLEncoder.encode(hash, "UTF-8"));

            logger.info("Request: " + request);
            return request.toString();
        } catch (UnsupportedEncodingException e) {
            logger.error("Ошибка формирования запроса", e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, e);
        }
    }

    private String doPayRequest(String request, WinpayPayPluginConfig config) throws Exception {
        SSLContext sslContext = HTTPUtils.createEmptyTrustSSLContext();

        String url = config.getPayUrl();
        return new String(HTTPUtils.sendHttp("POST", url, request.getBytes(), null, sslContext));
    }

    protected void parsePayResponse(PaymentEntity payment, String response, WinpayPayPluginConfig config) throws
            ActionException {
        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "result");
            String result = XmlUtils.getNodeValueText(element);
            element = XmlUtils.findElement(true, doc, "id");
            Integer id = Convertor.toInteger(element.getTextContent());

            if (!payment.getId().equals(id)) {
                logger.error("Неверный номер платежа в XML - отправили {} получили {}", payment.getId(), id);
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML",
                        Type.TECH, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный номер платежа в XML - отправили "
                        + payment.getId() + " получили " + id, Type.TECH, ResultType.NON_FATAL, null);
            }

            if ("PAID".equals(result)) {
                handleSuccess(payment, new Date());
            } else if (result.startsWith("ERROR") || "NOT_PAID".equalsIgnoreCase(result)) {
                if (result.contains("Not enough money at account balance")) {
                    logger.error("Платёж {}: нет средств на счету", payment.getId());
                    handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.LOW_BALANCE, "", Type.BUSINESS,
                            ResultType.FATAL));
                    throw new ActionException(ErrorKeys.LOW_BALANCE, "", Type.BUSINESS, ResultType.FATAL, null);
                }
                logger.error("Выплата платежа {} не произведена", payment.getId());
                handleError(payment, new Date(),
                        new ExceptionInfo(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS,
                                ResultType.FATAL));
                throw new ActionException(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS, ResultType.FATAL, null);
            } else if ("NOT_PAID".equalsIgnoreCase(result)) {
                logger.error("Выплата платежа {} не произведена", payment.getId());
                handleError(payment, new Date(),
                        new ExceptionInfo(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS,
                                ResultType.FATAL));
                throw new ActionException(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS, ResultType.FATAL, null);
            } else if ("OK".equalsIgnoreCase(result) || "PROCESSING".equalsIgnoreCase(result)) {
                logger.error("Платёж {} обрабатывается", payment.getId());
                payment.setProcessDate(new Date());
                payment.setStatus(PaymentStatus.SENDED);
            } else {
                logger.error("Неизвестный результат {} для платежа {}", result, payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.WAIT, "", Type.TECH,
                        ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.TECH, ResultType.NON_FATAL, null);
            }

        } catch (ActionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка разбора ответа", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
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

        return status(payment, context);
    }

    public boolean status(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        WinpayPayPluginConfig config = (WinpayPayPluginConfig) context.getPluginConfig();

        String request;
        String response;
        try {
            request = buildStatusRequest(payment, config);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {

            response = doStatusRequest(request, config);
            logger.info(response);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseStatusResponse(payment, response, config);
        } catch (Exception e) {
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            logger.error("Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        logger.info("Статус платежа {} после зачисления: {}", payment.getId(), payment.getStatus());
        return PaymentStatus.SUCCESS.equals(payment.getStatus());
    }

    public String status(Integer paymentId, PluginExecutionContext context) throws ActionException {

        PaymentEntity payment = paymentDAO.getPayment(paymentId);
        if (payment == null) {
            logger.warn("PaymentEntity with id " + paymentId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }

        WinpayPayPluginConfig config = (WinpayPayPluginConfig) context.getPluginConfig();

        String request;
        try {
            request = buildStatusRequest(payment, config);
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            return doStatusRequest(request, config);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    private String buildStatusRequest(PaymentEntity payment, WinpayPayPluginConfig config) throws ActionException {
        String date = DatesUtils.DATE_FORMAT_YYYY_MM_dd_HHmmss.format(new Date());
        String amount = Convertor.toString(payment.getAmount());
        String destination = "1";
        String hash = DigestUtils.md5Hex(payment.getId() + date + amount + destination + config.getSecretKey());

        try {
            String request = "ID=" + payment.getId()
                    + "&DT=" + URLEncoder.encode(date, "UTF-8")
                    + "&AMOUNT=" + URLEncoder.encode(amount, "UTF-8")
                    + "&DESTINATION=" + destination
                    + "&HASH=" + URLEncoder.encode(hash, "UTF-8");
            logger.info("Request: " + request);
            return request;
        } catch (UnsupportedEncodingException e) {
            logger.error("Ошибка формирования запроса", e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, e);
        }
    }

    private String doStatusRequest(String request, WinpayPayPluginConfig config) throws Exception {
        SSLContext sslContext = HTTPUtils.createEmptyTrustSSLContext();

        return new String(HTTPUtils.sendHttp("POST", config.getStatusUrl(), request.getBytes(), null, sslContext));
    }

    private void parseStatusResponse(PaymentEntity payment, String response, WinpayPayPluginConfig config)
            throws ActionException {
        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "result");
            String result = element.getTextContent();
            element = XmlUtils.findElement(true, doc, "id");
            Integer id = Convertor.toInteger(element.getTextContent());

            if (!payment.getId().equals(id)) {
                logger.error("Неверный номер платежа в XML - отправили {} получили {}", payment.getId(), id);
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML",
                        Type.TECH, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный номер платежа в XML - отправили "
                        + payment.getId() + " получили " + id, Type.TECH, ResultType.NON_FATAL, null);
            }

            if ("PAID".equals(result)) {
                handleSuccess(payment, new Date());

            } else if ("NOT_PAID".equals(result)) {
                logger.error("Выплата платежа {} не произведена", payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BANK_ERROR, "Выплата не произведена",
                        Type.BUSINESS, ResultType.FATAL));
                throw new ActionException(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS, ResultType.FATAL, null);
            } else if ("PROCESSING".equals(result)) {
                logger.error("Платёж {} обрабатывается", payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.WAIT, "", Type.BUSINESS,
                        ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
            } else if (result.startsWith("ERROR") || "NOT_PAID".equalsIgnoreCase(result)) {
                if (result.contains("Not enough money at account balance")) {
                    logger.error("Платёж {}: нет средств на счету", payment.getId());
                    handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.LOW_BALANCE, "", Type.BUSINESS,
                            ResultType.FATAL));
                    throw new ActionException(ErrorKeys.LOW_BALANCE, "", Type.BUSINESS, ResultType.FATAL, null);
                }
                logger.error("Выплата платежа {} не произведена", payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BANK_ERROR, "Выплата не произведена",
                        Type.BUSINESS, ResultType.FATAL));
                throw new ActionException(ErrorKeys.BANK_ERROR, "Выплата не произведена", Type.BUSINESS, ResultType.FATAL, null);
            } else {
                logger.error("Неизвестный результат {} для платежа {}", result, payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.WAIT, "", Type.TECH,
                        ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.WAIT, "", Type.TECH, ResultType.NON_FATAL, null);
            }

        } catch (ActionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка разбора ответа", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
        WinpayPayPluginConfig config = (WinpayPayPluginConfig) context.getPluginConfig();

        String date = DatesUtils.DATE_FORMAT_YYYY_MM_dd_HHmmss.format(new Date());
        String secretKey = config.getSecretKey();
        String hash = DigestUtils.md5Hex(date + secretKey);

        String request;
        try {
            request = "DT=" + URLEncoder.encode(date, "UTF-8") + "&HASH=" + URLEncoder.encode(hash, "UTF-8");

            logger.info(request);
        } catch (UnsupportedEncodingException e) {
            logger.error("Ошибка формирования запроса ", e);
            throw new KassaException("Ошибка формирования запроса", e);
        }


        SSLContext sslContext = HTTPUtils.createEmptyTrustSSLContext();

        String url = config.getBalanceUrl();

        String response;
        try {
            response = new String(HTTPUtils.sendHttp("POST", url,
                    request.getBytes(), null, sslContext));
            logger.info(response);
        } catch (IOException e) {
            logger.error("Ошибка отправки запроса " + e);
            throw new KassaException("Ошибка отправки запроса", e);
        }

        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "balance");

            if (element != null) {
                double balance = Convertor.toDouble(element.getTextContent());
            	paymentService.saveBalance(null, Partner.WINPAY, Account.CARD_TYPE, balance, new Date());
                return balance;
            } else {
                logger.error("Неуспешный запрос баланса ");
                throw new KassaException("Неуспешный запрос баланса");
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
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CARD_TYPE};
    }

    @Override
    public Integer getPartnerId() {
        return Partner.WINPAY;
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext
            context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext
            context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
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
}
