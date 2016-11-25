package ru.simplgroupp.ejb.plugins.payment;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.*;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.PayonlineRebillBeanLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.net.URLEncoder;
import java.util.*;

/**
 * Имплементация {@link PayonlineRebillBeanLocal}
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PayonlineRebillBean extends PaymentProcessorBean implements PayonlineRebillBeanLocal {

    private static final Logger logger = LoggerFactory.getLogger(PayonlineRebillBean.class);

    @EJB
    PaymentDAO payDAO;
    
    @EJB
    CreditDAO creditDAO;
    
    @EJB
    protected CreditCalculatorBeanLocal creditCalc;
    
    @Override
    public String getSystemName() {
        return PayonlineRebillBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return PayonlineRebillBeanLocal.SYSTEM_DESCRIPTION;
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
        return CreditRequest.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        PayonlineAcquiringPluginConfig config = (PayonlineAcquiringPluginConfig) context.getPluginConfig();

        Collection<Integer> creditIdList = null;

        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(creditIdList.size());

        for(Integer creditId: creditIdList) {

            CreditEntity credit = creditDAO.getCreditEntity(creditId);

            String rebillAnchor = "TODO";

            if (credit == null) {
                throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Кредит " + creditId + " не найден", Type.BUSINESS,
                        ResultType.FATAL, null);
            }

            PaymentEntity existenPayment = payDAO.findSendedPayment(creditId, Partner.PAYONLINE);

            if (existenPayment != null) {
                try {
                    int status = paymentStatus(existenPayment, config);
                    if (status == ErrorKeys.SUCCESS) {
                        result.add(handleSuccess(existenPayment, new Date()));
                        continue;
                    } else if (status == ErrorKeys.WAIT) {
                        ExceptionInfo exceptionInfo = new ExceptionInfo(ErrorKeys.UNKNOWN, "", Type.BUSINESS, ResultType.NON_FATAL);
                        result.add(new BusinessObjectResult(existenPayment.getClass().getName(), existenPayment.getId(), true, exceptionInfo));
                        continue;
                    }
                } catch (KassaException e) {
                    logger.error(e.getMessage());
                    ExceptionInfo exceptionInfo = new ExceptionInfo(ErrorKeys.UNKNOWN, e.getMessage(), Type.TECH, ResultType.FATAL);
                    result.add(new BusinessObjectResult(existenPayment.getClass().getName(), existenPayment.getId(), true, exceptionInfo));
                    continue;
                }
            }

            Map<String, Object> resCalc = creditCalc.calcCredit(credit.getId(), new Date());
            Double amount = (Double) resCalc.get(CreditCalculatorBeanLocal.SUM_BACK);

            int partnerId = getPartnerId();             
            Payment payment = paymentService.createPayment(creditId, Account.PAYONLINE_CARD_TYPE,
                    Payment.SUM_FROM_CLIENT, amount, Payment.TO_SYSTEM, partnerId);

            String url = config.getStatusUrl();

            StringBuilder parameters = new StringBuilder();
            try {

                String stringForSecurityKey = "MerchantId=" + config.getMerchantId()
                        + "&RebillAnchor=" + rebillAnchor
                        + "&OrderId=" + payment.getId()
                        + "&Amount=" + payment.getAmount()
                        + "&Currency=RUB"
                        + "&PrivateSecurityKey=" + config.getPrivateSecurityKey();
                String securityKey = DigestUtils.md5Hex(stringForSecurityKey);

                parameters.append("MerchantId=").append(URLEncoder.encode(String.valueOf(config.getMerchantId()), "UTF-8"))
                        .append("RebillAnchor=").append(URLEncoder.encode(String.valueOf(config.getMerchantId()), "UTF-8"))
                        .append("&OrderId=").append(URLEncoder.encode(String.valueOf(payment.getId()), "UTF-8"))
                        .append("&Amount=").append(URLEncoder.encode(String.valueOf(amount), "UTF-8"))
                        .append("&Currency=RUB")
                        .append("&SecurityKey=").append(URLEncoder.encode(String.valueOf(securityKey), "UTF-8"))
                        .append("&ContentType=xml")
                ;

                try {
                    byte[] responseByte = HTTPUtils.sendHttp("POST", url, parameters.toString().getBytes(), null,
                            HTTPUtils.createEmptyTrustSSLContext());


                    String response = String.valueOf(responseByte);
                    Document xml = XmlUtils.getDocFromString(response);
                    String paymentResult = XmlUtils.getNodeValueText(xml.getElementsByTagName("Result").item(0));
                    String paymentMessage = XmlUtils.getNodeValueText(xml.getElementsByTagName("Message").item(0));

                    if ("Ok".equals(paymentResult)) {
                        result.add(handleSuccess(payment.getEntity(), new Date()));
                    } else {
                        ExceptionInfo exceptionInfo = new ExceptionInfo(ErrorKeys.UNKNOWN, paymentMessage, Type.BUSINESS, ResultType.FATAL);
                        result.add(handleError(payment.getEntity(), new Date(), exceptionInfo));
                    }
                } catch (Exception e) {
                    ExceptionInfo exceptionInfo = new ExceptionInfo(ErrorKeys.UNKNOWN, e.getMessage(), Type.TECH, ResultType.FATAL);
                    result.add(new BusinessObjectResult(payment.getClass().getName(), payment.getId(), true, exceptionInfo));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                ExceptionInfo exceptionInfo = new ExceptionInfo(ErrorKeys.UNKNOWN, e.getMessage(), Type.TECH, ResultType.FATAL);
                result.add(new BusinessObjectResult(payment.getClass().getName(), payment.getId(), true, exceptionInfo));
            }
        }
        return result;
    }

    private int paymentStatus(PaymentEntity payment, PayonlineAcquiringPluginConfig config) throws KassaException {

        String url = config.getStatusUrl();

        StringBuilder parameters = new StringBuilder();
        try {

            String stringForSecurityKey = "MerchantId=" + config.getMerchantId()
                    + "&OrderId=" + payment.getId()
                    + "&PrivateSecurityKey=" + config.getPrivateSecurityKey();
            String securityKey = DigestUtils.md5Hex(stringForSecurityKey);

            parameters.append("MerchantId=").append(URLEncoder.encode(String.valueOf(config.getMerchantId()), "UTF-8"))
                    .append("&OrderId=").append(URLEncoder.encode(String.valueOf(payment.getId()), "UTF-8"))
                    .append("&SecurityKey=").append(URLEncoder.encode(String.valueOf(securityKey), "UTF-8"))
                    .append("&ContentType=xml")
            ;

            byte[] responseByte = HTTPUtils.sendHttp("POST", url, parameters.toString().getBytes(), null,
                    HTTPUtils.createEmptyTrustSSLContext());

            String response = String.valueOf(responseByte);
            if (response.length() < 1) {
                return ErrorKeys.ORDER_NOT_FOUND;
            }
            Document xml = XmlUtils.getDocFromString(response);
            String status = XmlUtils.getNodeValueText(xml.getElementsByTagName("Status").item(0));

            return "Settled".equals(status) ? ErrorKeys.SUCCESS : ErrorKeys.WAIT;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new KassaException(e);
        }
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
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
        return new int[]{Account.PAYONLINE_CARD_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.PAYONLINE;
	}     
}
