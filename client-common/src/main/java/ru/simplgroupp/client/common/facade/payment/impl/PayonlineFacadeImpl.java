package ru.simplgroupp.client.common.facade.payment.impl;

import org.apache.commons.codec.digest.DigestUtils;
import ru.simplgroupp.client.common.facade.payment.PayonlineConfig;
import ru.simplgroupp.client.common.facade.payment.PayonlineFacade;
import ru.simplgroupp.dao.interfaces.PayonlineVerificationDAO;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.plugins.payment.PayonlineAcquiringPluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.PayonlineAcquiringBeanLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PayonlineVerification;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.AccountService;
import ru.simplgroupp.services.CreditRequestService;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.services.UserProvider;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.naming.AuthenticationException;

/**
 * Payonline service impl
 */
@Singleton
@TransactionManagement
public class PayonlineFacadeImpl implements PayonlineFacade {

    @Inject
    private UserProvider userProvider;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CreditBeanLocal creditBean;

    @Inject
    private CreditRequestService creditRequestService;

    @Inject
    private AccountService accountService;

    @Inject
    private PayonlineVerificationDAO payonlineVerificationDAO;

    @Inject
    protected transient ActionProcessorBeanLocal actionProcessor;

    @Override
    public PayonlineConfig getPayonlineConfig() {
        PayonlineAcquiringPluginConfig pluginConfig = getPayonlinePluginConfig();
        return new PayonlineConfig(pluginConfig.getMerchantId(),pluginConfig.getVerificationCardMerchantId(),
                pluginConfig.getBankCardUrl(), pluginConfig.getVerificationCardUrl());
    }

    @TransactionAttribute
    public PaymentEntity createPayment(double sum) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();

        CreditEntity credit = creditBean.creditActive(user.getPeopleMainId().getId()).getEntity();
        if (credit == null) {
            throw new IllegalStateException("User " + user.getId() + " has no active credit");
        }

        return paymentService.createPayment(credit.getId(), Account.CARD_TYPE, Payment.SUM_FROM_CLIENT, sum,
                Payment.TO_SYSTEM, Partner.PAYONLINE).getEntity();
    }

    @Override
    public String signPaymentRequest(String orderId, String amount) {
        PayonlineAcquiringPluginConfig pluginConfig = getPayonlinePluginConfig();

        return signPaymentRequestInternal(orderId, amount, pluginConfig.getMerchantId(), pluginConfig.getPrivateSecurityKey());
    }

    @Override
    public String signVerificationRequest(String orderId) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();
        payonlineVerificationDAO.save(new PayonlineVerification(orderId, user.getPeopleMainId()));

        PayonlineAcquiringPluginConfig pluginConfig = getPayonlinePluginConfig();

        return signPaymentRequestInternal(orderId, orderId, pluginConfig.getVerificationCardMerchantId(),
                pluginConfig.getVerificationCardPrivateSecurityKey());
    }

    @Override
    public boolean checkAmount(String guid, double amount) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();
        PayonlineAcquiringPluginConfig payonlinePluginConfig = getPayonlinePluginConfig();

        byte[] f = DigestUtils.sha("Amount=" + guid
                + "&PrivateSecurityKey=" + payonlinePluginConfig.getVerificationCardPrivateSecurityKey());

        long s = 100;
        for (int i = 0; i < 20; i++) {
            s += ((int) f[i] & 0xFF) % 31;
        }
        double expectedAmount = s/100.0;
        if (expectedAmount == amount) {
            PayonlineVerification payonlineVerification = payonlineVerificationDAO.findOne(guid);
            if (payonlineVerification != null && payonlineVerification.getRebillAnchor() != null) {
                accountService.createPayonlineCard(user.getPeopleMainId(), payonlineVerification);
                payonlineVerificationDAO.delete(payonlineVerification);
            }
            return true;
        }
        return false;
    }

    @Override
    public void deleteVerification(String guid) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();
        PayonlineVerification payonlineVerification = payonlineVerificationDAO.findOne(guid);
        if (payonlineVerification != null && payonlineVerification.getPeople().equals(user.getPeopleMainId())) {
            payonlineVerificationDAO.delete(payonlineVerification);
        }
    }

    private String signPaymentRequestInternal(String orderId, String amount, Integer merchantId,
            String privateSecurityKey) {
        String data = "MerchantId=" + merchantId
                + "&OrderId=" + orderId
                + "&Amount=" + amount
                + "&Currency=RUB"
                + "&PrivateSecurityKey=" + privateSecurityKey;

        return signInternal(data);
    }

    private String signInternal(String data) {
        return DigestUtils.md5Hex(data);
    }

    private PayonlineAcquiringPluginConfig getPayonlinePluginConfig() {
        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext payonlineExecutionContext = PluginExecutionContext.createExecutionContext(actionContext,
                PayonlineAcquiringBeanLocal.SYSTEM_NAME);

        return (PayonlineAcquiringPluginConfig) payonlineExecutionContext.getPluginConfig();
    }
}
