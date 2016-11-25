package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.app.ICryptoService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.QiwiAcquiringBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.Pair;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Плугин для оплаты чере qiwi
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(QiwiAcquiringBeanLocal.class)
public class QiwiAcquiringBean extends PaymentProcessorBean implements QiwiAcquiringBeanLocal {

	@Inject Logger logger;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @EJB
    private ICryptoService cryptoService;

    @EJB
    PeopleBeanLocal peopleBean;
    
	@EJB
	WorkflowBeanLocal workflow; 
	
	@EJB
    private PaymentDAO paymentDAO;

    protected String buildOrderRequest(String phone, PaymentEntity payment, PluginExecutionContext context) throws Exception {

        QiwiAcquiringPluginConfig config = (QiwiAcquiringPluginConfig) context.getPluginConfig();

        int lifetime = config.getLifeTime();

        CreditEntity credit = payment.getCreditId();

        if (phone == null) {
            throw new ActionException(ErrorKeys.INVALID_ACCOUNT, "Не указан номер телефона", Type.TECH, ResultType.FATAL, null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, lifetime);

        StringBuilder req = new StringBuilder();

        String user = "tel:+"+phone;
        req.append("user=").append(URLEncoder.encode(user, "utf-8")).append("&amount=").append(payment.getAmount())
                .append("&ccy=RUB").append("&comment=").append(URLEncoder.encode("Погашение займа и процентов по договору займа № ", "utf-8"))
                .append(credit.getId()).append(URLEncoder.encode(" от ", "utf-8"))
                .append(credit.getDatabeg()).append(URLEncoder.encode(" г.", "utf-8"))
                .append("&lifetime=").append(URLEncoder.encode(df.format(calendar.getTime()), "utf-8")).append("&pay_source=qw");

        return req.toString();
    }

    protected void parseSingleResponse(PaymentEntity payment, Object response) throws ActionException {

        try {
            Document doc = XmlUtils.getDocFromString(new String((byte[])response));
            if (doc!=null) {
                int errorCode = Convertor.toInteger((XmlUtils.findElement(true, doc, "result_code").getTextContent()));

                if (errorCode == 0) {

                    String status = XmlUtils.getNodeValueText(XmlUtils.findElement(true, doc, "status"));

                    if ("paid".equals(status)) {

                        String externalId = XmlUtils.findElement(true, doc, "bill_id").getTextContent();
                        payment.setExternalId(externalId);
                        handleSuccess(payment, new Date());
                    } else if ("rejected".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_REJECTED, "Счет отклонен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_REJECTED, "Счет отклонен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("unpaid".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_UNPAID, "Ошибка при проведении оплаты. Счет не оплачен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_UNPAID, "Ошибка при проведении оплаты. Счет не оплачен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("expired".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_EXPIRED, "Время жизни счета истекло. Счет не оплачен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_EXPIRED, "Время жизни счета истекло. Счет не оплачен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("fail".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_FAIL, " Платеж неуспешен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_FAIL, "Платеж неуспешен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("waiting".equals(status) || "processing".equals(status)) {

                        String externalId = XmlUtils.findElement(true, doc, "bill_id").getTextContent();
                        payment.setExternalId(externalId);
                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.WAIT, "Платеж в проведении", Type.BUSINESS, ResultType.NON_FATAL));
                        throw new ActionException(ErrorKeys.WAIT, "Платеж в проведении", Type.BUSINESS, ResultType.NON_FATAL, null);
                    }

                } else {
                    logger.severe("Error for payment "+payment.getId());

                    String description = null;
                    Element el = XmlUtils.findElement(false, doc, "description");
                    description = XmlUtils.getNodeValueText(el);
                    
                    Pair<Integer, ResultType> error = mapError(errorCode);
                    handleError(payment, new Date(), new ExceptionInfo(error.getFirst(), description, Type.BUSINESS, error.getSecond()));
                    throw new ActionException(error.getFirst(), description, Type.BUSINESS, error.getSecond(), null);
                }

            } else {
                logger.severe("Invalid xml for payment "+payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, null);
            }
        } catch (Exception e) {
            logger.severe("Can't parse response for payment \"+businessObjectId+\". Cause: " + e.getMessage());
            handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    private void parseOrderResponse(PaymentEntity payment, Object response) throws ActionException {

        try {
            Document doc = XmlUtils.getDocFromString((String)response);
            if (doc!=null) {
                int errorCode = Convertor.toInteger(XmlUtils.findElement(true, doc, "result_code").getTextContent());

                if (errorCode == 0) {

                    String status = XmlUtils.findElement(true, doc, "status").getTextContent();
                    String externalId = XmlUtils.findElement(true, doc, "bill_id").getTextContent();
                    payment.setExternalId(externalId);

                    if ("paid".equals(status)) {

                        handleSuccess(payment, new Date());
                    } else if ("rejected".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_REJECTED, "Счет отклонен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_REJECTED, "Счет отклонен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("unpaid".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_UNPAID, "Ошибка при проведении оплаты. Счет не оплачен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_UNPAID, "Ошибка при проведении оплаты. Счет не оплачен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("expired".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_EXPIRED, "Время жизни счета истекло. Счет не оплачен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_EXPIRED, "Время жизни счета истекло. Счет не оплачен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("fail".equals(status)) {

                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.ORDER_FAIL, " Платеж неуспешен", Type.BUSINESS, ResultType.FATAL));
                        throw new ActionException(ErrorKeys.ORDER_FAIL, "Платеж неуспешен", Type.BUSINESS, ResultType.FATAL, null);
                    } else if ("waiting".equals(status) || "processing".equals(status)) {
                        handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.WAIT, "Платеж в проведении", Type.BUSINESS, ResultType.NON_FATAL));
                    }

                } else {
                    logger.severe("Error for payment "+payment.getId());

                    String description = null;
                    Element el = XmlUtils.findElement(false, doc, "description");
                    description = XmlUtils.getNodeValueText(el);
                    
                    Pair<Integer, ResultType> error = mapError(errorCode);
                    handleError(payment, new Date(), new ExceptionInfo(error.getFirst(), description, Type.BUSINESS, error.getSecond()));
                    throw new ActionException(error.getFirst(), description, Type.BUSINESS, error.getSecond(), null);
                }

            } else {
                logger.severe("Invalid xml for payment "+payment.getId());
                handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, null);
            }
        } catch (Exception e) {
            logger.severe("Can't parse response for payment \"+businessObjectId+\". Cause: " + e.getMessage());
            handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL));
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    private Pair<Integer, ResultType> mapError(int errorCode) {
        switch (errorCode) {
            case 5: return new Pair<Integer, ResultType>(ErrorKeys.BAD_REQUEST, ResultType.FATAL);
            case 13: return new Pair<Integer, ResultType>(ErrorKeys.WAIT, ResultType.NON_FATAL);
            case 78: return new Pair<Integer, ResultType>(ErrorKeys.BAD_REQUEST, ResultType.FATAL);
            case 150: return new Pair<Integer, ResultType>(ErrorKeys.AUTH_ERROR, ResultType.FATAL);
            case 152: return new Pair<Integer, ResultType>(ErrorKeys.DISABLED_PROTOCOL, ResultType.FATAL);
            case 210: return new Pair<Integer, ResultType>(ErrorKeys.ORDER_NOT_FOUND, ResultType.FATAL);
            case 215: return new Pair<Integer, ResultType>(ErrorKeys.DUPLICATE_ORDER, ResultType.FATAL);
            case 241: return new Pair<Integer, ResultType>(ErrorKeys.INVALID_AMOUNT, ResultType.FATAL);
            case 242: return new Pair<Integer, ResultType>(ErrorKeys.INVALID_AMOUNT, ResultType.FATAL);
            case 298: return new Pair<Integer, ResultType>(ErrorKeys.INVALID_ACCOUNT, ResultType.FATAL);
            case 300: return new Pair<Integer, ResultType>(ErrorKeys.TECH_ERROR, ResultType.NON_FATAL);
            case 303: return new Pair<Integer, ResultType>(ErrorKeys.INVALID_ACCOUNT, ResultType.FATAL);
            case 316: return new Pair<Integer, ResultType>(ErrorKeys.AUTH_ERROR, ResultType.FATAL);
            case 319: return new Pair<Integer, ResultType>(ErrorKeys.ACCESS_DENIED, ResultType.FATAL);
            case 341: return new Pair<Integer, ResultType>(ErrorKeys.BAD_REQUEST, ResultType.FATAL);
            case 1001: return new Pair<Integer, ResultType>(ErrorKeys.BAD_REQUEST, ResultType.FATAL);
        }

        return new Pair<Integer, ResultType>(ErrorKeys.UNKNOWN, ResultType.FATAL);
    }

    protected Object doRequestSingle(Object request, PluginExecutionContext context) throws Exception {

        QiwiAcquiringPluginConfig config = (QiwiAcquiringPluginConfig) context.getPluginConfig();

        return HTTPUtils.sendHttp("GET", config.getStatusUrl()+request, null, buildHeaders(config), HTTPUtils.createEmptyTrustSSLContext());
    }

    /**
     * Всегда отвечаем false, так как платёж создан в doOrder и нужно узнавать статус в querySingleRequest
     * @param businessObjectClass
     * @param businessObjectId
     * @param context
     * @return
     * @throws ActionException
     */
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        Integer paymentId = Convertor.toInteger(context.getData().get("paymentId"));
        PaymentEntity payment = paymentDAO.getPayment(paymentId);

        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL, null);
        }

        Object request;
        Object response;
        try {
            request = buildSingleRequest(payment, context);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {

            response = doRequestSingle(request, context);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseSingleResponse(payment, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            if (e instanceof ActionException) {
                throw (ActionException)e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        return PaymentStatus.SUCCESS.equals(payment.getStatus());
    }

    protected String buildSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws Exception {
        return ""+payment.getId();
    }

    @Override
    public String getSystemName() {
        return QiwiAcquiringBeanLocal.SYSTEM_NAME;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE, Mode.PACKET);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.ASYNC, SyncMode.SYNC);
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
    	logger.info("Qiwi в пакетном режиме.");
    	List<BusinessObjectResult> lstRes = Collections.EMPTY_LIST;
    	// TODO связаться с сервером, получить результаты
        return lstRes;
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
	public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
	}

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public String getPayUrl(PluginExecutionContext context) {
        QiwiAcquiringPluginConfig config = (QiwiAcquiringPluginConfig) context.getPluginConfig();

        return config.getPayUrl();
    }

    private Map<String, String> buildHeaders(QiwiAcquiringPluginConfig config) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic "+javax.xml.bind.DatatypeConverter.printBase64Binary((config.getLogin() + ":" + config.getPassword()).getBytes()));
        headers.put("Accept", "text/xml");
        return headers;
    }

    @Override
    public String doOrder(String phone, Integer paymentId, PluginExecutionContext context) throws ActionException {
        PaymentEntity payment = paymentDAO.getPayment(paymentId);

        if (payment == null) {
            logger.info("PaymentEntity with id " + paymentId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL, null);
        }

        String response;
        try {

            QiwiAcquiringPluginConfig config = (QiwiAcquiringPluginConfig) context.getPluginConfig();

            String request = buildOrderRequest(phone, payment, context);

            response = new String(HTTPUtils.sendHttp("PUT", config.getOrderUrl()+payment.getId(), request.getBytes(), buildHeaders(config), HTTPUtils.createEmptyTrustSSLContext()));

        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseOrderResponse(payment, response);
            payment.setStatus(PaymentStatus.SENDED);
            return payment.getExternalId();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            if (e instanceof ActionException) {
                throw (ActionException)e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }
    }

	@Override
	public int[] getSupportedAccountTypes() {
		return new int[] {Account.QIWI_TYPE};
	
	}

	@Override
	public String getSystemDescription() {
		return QiwiAcquiringBeanLocal.SYSTEM_DESCRIPTION;
	}    
	
	@Override
	public Integer getPartnerId() {
		return Partner.QIWI;
	} 	
}
