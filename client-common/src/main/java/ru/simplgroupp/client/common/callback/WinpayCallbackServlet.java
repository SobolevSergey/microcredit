package ru.simplgroupp.client.common.callback;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.plugins.payment.WinpayAcquiringPluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.WinpayAcquiringBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Winpay callback servlet
 */
public class WinpayCallbackServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WinpayCallbackServlet.class);

    private static final String DATA_PARAMETER = "data";

    private static final String SIGN_PARAMETER = "sign";

    @EJB
    private transient ActionProcessorBeanLocal actionProcessor;

    @EJB
    private PaymentDAO payDAO;

    @EJB
    private WorkflowBeanLocal workflowBean;

    private String secretKey;

    private String apiKey;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext winpayExecutionContext = PluginExecutionContext.createExecutionContext(actionContext,
                WinpayAcquiringBeanLocal.SYSTEM_NAME);

        WinpayAcquiringPluginConfig winpayAcquiringPluginConfig = (WinpayAcquiringPluginConfig) winpayExecutionContext
                .getPluginConfig();
        secretKey = winpayAcquiringPluginConfig.getSecretKey();
        apiKey = winpayAcquiringPluginConfig.getApiKey();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String data = request.getParameter(DATA_PARAMETER);
        String sign = request.getParameter(SIGN_PARAMETER);

        if (data == null || sign == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("BAD_REQUEST");
            response.getWriter().flush();
            return;
        }

        try {
            if (!sign.equals(hmacMd5Encode(secretKey, data))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print("BAD_REQUEST");
                return;
            }

            String jsonData = new String(DatatypeConverter.parseBase64Binary(data));
            logger.info("Winpay callback: " + jsonData);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            WinpayCallback callback = objectMapper.readValue(jsonData.getBytes(), WinpayCallback.class);

            if (callback.getApiKey() == null || !callback.getApiKey().equals(apiKey)) {
                logger.info("Winpay callback: неверная подпись");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print("BAD_REQUEST");
                return;
            }

            if (3 == callback.getStatus()) {
                Payment payment = payDAO.getPayment(callback.getReference(),
                        Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
                if (payment == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().print("BAD_REQUEST");
                    return;
                }

                Map<String, Object> pluginData = new HashMap<>();
                pluginData.put("paymentId", callback.getReference());
                workflowBean.repaymentReceivedWinpay(payment.getCredit().getCreditRequest().getId(), pluginData);
            }

            response.getWriter().print("OK");
            return;
        } catch (Exception e) {
            logger.error("Check sign error", e);
        }
    }

    private String hmacMd5Encode(String key, String message) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes());

        return Hex.encodeHexString(rawHmac);
    }

    public static class WinpayCallback {

        private String transactionId;

        private Integer reference;

        private String apiKey;

        private Double amount;

        private String currency;

        private Integer status;

        private Double systemAmount;

        private String systemCurrency;

        private Double commission;

        private String paymentSystemId;

        private String paymentSystemName;

        private String cardNumber;

        private String processingErrorMsg;

        private String authorizationCode;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public Integer getReference() {
            return reference;
        }

        public void setReference(Integer reference) {
            this.reference = reference;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Double getSystemAmount() {
            return systemAmount;
        }

        public void setSystemAmount(Double systemAmount) {
            this.systemAmount = systemAmount;
        }

        public String getSystemCurrency() {
            return systemCurrency;
        }

        public void setSystemCurrency(String systemCurrency) {
            this.systemCurrency = systemCurrency;
        }

        public Double getCommission() {
            return commission;
        }

        public void setCommission(Double commission) {
            this.commission = commission;
        }

        public String getPaymentSystemId() {
            return paymentSystemId;
        }

        public void setPaymentSystemId(String paymentSystemId) {
            this.paymentSystemId = paymentSystemId;
        }

        public String getPaymentSystemName() {
            return paymentSystemName;
        }

        public void setPaymentSystemName(String paymentSystemName) {
            this.paymentSystemName = paymentSystemName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getProcessingErrorMsg() {
            return processingErrorMsg;
        }

        public void setProcessingErrorMsg(String processingErrorMsg) {
            this.processingErrorMsg = processingErrorMsg;
        }

        public String getAuthorizationCode() {
            return authorizationCode;
        }

        public void setAuthorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
        }
    }
}
