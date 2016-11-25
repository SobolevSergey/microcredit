package ru.simplgroupp.client.common.facade.payment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.codec.binary.Hex;
import ru.simplgroupp.client.common.facade.payment.WinpayConfig;
import ru.simplgroupp.client.common.facade.payment.WinpayFacade;
import ru.simplgroupp.client.common.facade.payment.WinpayRequest;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.plugins.payment.WinpayAcquiringPluginConfig;
import ru.simplgroupp.exception.KassaRuntimeException;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.WinpayAcquiringBeanLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.services.UserProvider;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Winpay facade implaementation
 */
@Singleton
@TransactionManagement
public class WinpayFacadeImpl implements WinpayFacade {

    @Inject
    private UserProvider userProvider;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CreditBeanLocal creditBean;

    @Inject
    protected ActionProcessorBeanLocal actionProcessor;

    private DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public WinpayConfig getWinpayConfig() throws AuthenticationException, ObjectNotFoundException {
        userProvider.getAuthorizedUser();
        WinpayAcquiringPluginConfig pluginConfig = getWinpayPluginConfig();

        return new WinpayConfig(
                pluginConfig.isUseWork() ? pluginConfig.getPayUrl() : pluginConfig.getTestUrl());
    }

    @Override
    public WinpayRequest createPaymentRequest(double sum, String successUrl, String failureUrl)
            throws AuthenticationException, ObjectNotFoundException {

        PaymentEntity payment = createPayment(sum);
        WinpayAcquiringPluginConfig pluginConfig = getWinpayPluginConfig();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);

        OrderData data = new OrderData();
        data.setApiKey(pluginConfig.getApiKey());
        data.setExpiration(DF.format(calendar.getTime()));
        data.setAmount(String.valueOf(sum));
        data.setCurrency("RUR");
        data.setReference(String.valueOf(payment.getId()));
        data.setDescription("Погашение займа");
        data.setSuccessUrl(successUrl);
        data.setFailureUrl(failureUrl);
        data.setLang("ru");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        try {
            String jsonData = DatatypeConverter.printBase64Binary(objectMapper.writeValueAsBytes(data));
            String sign = hmacMd5Encode(pluginConfig.getSecretKey(), jsonData);
            return new WinpayRequest(jsonData, sign);
        } catch (Exception e) {
            throw new KassaRuntimeException("Can't create request", e);
        }
    }

    public PaymentEntity createPayment(double sum) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();

        CreditEntity credit = creditBean.creditActive(user.getPeopleMainId().getId()).getEntity();
        if (credit == null) {
            throw new IllegalStateException("User " + user.getId() + " has no active credit");
        }

        return paymentService.createPayment(credit.getId(), Account.CARD_TYPE, Payment.SUM_FROM_CLIENT, sum,
                Payment.TO_SYSTEM, Partner.WINPAY).getEntity();
    }

    private WinpayAcquiringPluginConfig getWinpayPluginConfig() {
        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext winpayExecutionContext = PluginExecutionContext.createExecutionContext(actionContext,
                WinpayAcquiringBeanLocal.SYSTEM_NAME);

        return (WinpayAcquiringPluginConfig) winpayExecutionContext.getPluginConfig();
    }

    private String hmacMd5Encode(String key, String message) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes());

        return Hex.encodeHexString(rawHmac);
    }

    public static class OrderData {

        private String apiKey;

        private String expiration;

        private String amount;

        private String currency;

        private String reference;

        private String description;

        private String successUrl;

        private String failureUrl;

        private String lang;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }
}
