package ru.simplgroupp.ejb.plugins.payment;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.*;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.PayonlineAcquiringBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import java.net.URLEncoder;
import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Реализация плугина payonline для оплаты кредита
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PayonlineAcquiringBean extends PaymentProcessorBean implements PayonlineAcquiringBeanLocal {

    private static final Logger logger = LoggerFactory.getLogger(PayonlineAcquiringBean.class);

    @EJB
    PaymentDAO payDAO;
    
    @Override
    public String getSystemName() {
        return PayonlineAcquiringBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return PayonlineAcquiringBeanLocal.SYSTEM_DESCRIPTION;
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
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int paymentStatus(Long paymentId, PluginExecutionContext context) throws KassaException {
        PayonlineAcquiringPluginConfig config = (PayonlineAcquiringPluginConfig) context.getPluginConfig();

        String url = config.getStatusUrl();

        StringBuilder parameters = new StringBuilder();
        try {

            String stringForSecurityKey = "MerchantId=" + config.getMerchantId() + "&OrderId=" + paymentId
                    + "&PrivateSecurityKey=" + config.getPrivateSecurityKey();
            String securityKey = DigestUtils.md5Hex(stringForSecurityKey);

            parameters.append("MerchantId=").append(URLEncoder.encode(String.valueOf(config.getMerchantId()), "UTF-8"))
                    .append("&OrderId=").append(URLEncoder.encode(String.valueOf(paymentId), "UTF-8"))
                    .append("&SecurityKey=").append(URLEncoder.encode(String.valueOf(securityKey), "UTF-8"))
                    .append("&ContentType=xml")
            ;

            byte[] responseByte = HTTPUtils.sendHttp("POST", url, parameters.toString().getBytes(), null,
                    HTTPUtils.createEmptyTrustSSLContext());

            String response = new String(responseByte);
            if (response.length() < 1) {
                return ErrorKeys.ORDER_NOT_FOUND;
            }
            Document xml = XmlUtils.getDocFromString(response);
            String status = XmlUtils.getNodeValueText(xml.getElementsByTagName("status").item(0));

            return "Settled".equals(status) ? ErrorKeys.SUCCESS : ErrorKeys.WAIT;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new KassaException(e);
        }
    }

    @Override
    public void paymentCancel(String orderId, String transactionId, PluginExecutionContext context) throws KassaException {
        PayonlineAcquiringPluginConfig config = (PayonlineAcquiringPluginConfig) context.getPluginConfig();

        String url = config.getCancelUrl();

        StringBuilder parameters = new StringBuilder();
        try {

            String choosenSecurityKey;
            Integer merchantId;
            try {
                UUID.fromString(orderId);
                choosenSecurityKey = config.getVerificationCardPrivateSecurityKey();
                merchantId = config.getVerificationCardMerchantId();
            } catch (IllegalArgumentException e) {
                choosenSecurityKey = config.getPrivateSecurityKey();
                merchantId = config.getMerchantId();
            }

            String stringForSecurityKey = "MerchantId=" + merchantId + "&TransactionId=" + transactionId
                    + "&PrivateSecurityKey=" + choosenSecurityKey;
            String securityKey = DigestUtils.md5Hex(stringForSecurityKey);

            parameters.append("MerchantId=").append(URLEncoder.encode(String.valueOf(merchantId), "UTF-8"))
                    .append("&TransactionId=").append(URLEncoder.encode(String.valueOf(transactionId), "UTF-8"))
                    .append("&SecurityKey=").append(URLEncoder.encode(String.valueOf(securityKey), "UTF-8"))
                    .append("&ContentType=xml")
            ;
            logger.info("Cancel request: {}",  parameters);
            byte[] responseByte = HTTPUtils.sendHttp("POST", url, parameters.toString().getBytes(), null,
                    HTTPUtils.createEmptyTrustSSLContext());

            String response = new String(responseByte);
            logger.info("Cancel response: {}", response);
            if (response.length() < 1) {
                throw new KassaException("Invalid xml");
            }

            Document xml = XmlUtils.getDocFromString(response, "<?xml version='1.0' encoding='UTF-8'?>");
            String result = XmlUtils.getNodeValueText(xml.getElementsByTagName("result").item(0));

            if ("Ok".equals(result)) {
                return;
            } else if ("Error".equals(result)) {
                return;
            }

            throw new KassaException("Invalid xml");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new KassaException(e);
        }
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId
            , PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        logger.info("Payonline payment for creditrequest " + businessObjectId);

        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity payment = null;
        if (paymentId != null) {
            payment = payDAO.getPayment(paymentId);
        }

        if (payment != null) {

            if (data.get("errorCode") != null) {
                int errorCode = Convertor.toInteger(data.get("errorCode"));
                ExceptionInfo errorInfo = mapError(errorCode);
                handleError(payment, new Date(), errorInfo);
                logger.error("Произошла ошибка при проведении платежа " + errorCode);
                throw new ActionException(errorInfo.getCode(), errorInfo.getMessage(), errorInfo.getType(),
                        errorInfo.getResultType(), null);

            } else {
            	if (context.getNumRepeats()<=5){
                    handleSuccess(payment, (Date) data.get("date"));
            	} else {
            		throw new ActionException(ErrorKeys.TECH_ERROR, "Произошла техническая ошибка с платежом Payonline "+paymentId, 
            				Type.TECH,  ResultType.FATAL, null);
            	}
            }

        } else {
            logger.info("Ошибка платежа " + businessObjectId + ". Причина: платёж не найден");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.BUSINESS,
                    ResultType.FATAL, null);
        }


    }

    private ExceptionInfo mapError(int errorCode) {
        switch (errorCode) {
            case 1:
                return new ExceptionInfo(ErrorKeys.TECH_ERROR, "Техническая ошибка payonline", Type.TECH, ResultType.FATAL);
            case 2:
                return new ExceptionInfo(ErrorKeys.BAD_CARD, "Провести платеж по банковской карте невозможно.", Type.BUSINESS, ResultType.FATAL);
            case 3:
                return new ExceptionInfo(ErrorKeys.BANK_ERROR, "Платеж отклоняется банком-эмитентом карты.", Type.BUSINESS, ResultType.FATAL);
        }

        return new ExceptionInfo(ErrorKeys.UNKNOWN, "Неизвестная ошибка.", Type.BUSINESS, ResultType.FATAL);
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
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

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.CARD_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.PAYONLINE;
	}    
}
