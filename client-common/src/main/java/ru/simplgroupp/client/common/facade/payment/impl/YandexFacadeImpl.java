package ru.simplgroupp.client.common.facade.payment.impl;

import ru.simplgroupp.client.common.facade.payment.YandexConfig;
import ru.simplgroupp.client.common.facade.payment.YandexFacade;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.plugins.payment.YandexAcquiringPluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexAcquiringBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.UsersEntity;
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
 * Yandex facade implementation
 */
@Singleton
@TransactionManagement
public class YandexFacadeImpl implements YandexFacade {

    @Inject
    private UserProvider userProvider;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CreditBeanLocal creditBean;

    @Inject
    protected transient ActionProcessorBeanLocal actionProcessor;

    @Override
    public YandexConfig getYandexConfig() throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();
        YandexAcquiringPluginConfig pluginConfig = getYandexPluginConfig();

        return new YandexConfig(pluginConfig.getShopId(), pluginConfig.getScid(), user.getPeopleMainId().getId(),
                pluginConfig.isUseWork() ? pluginConfig.getWorkUrl() : pluginConfig.getTestUrl());
    }

    @Override
    @TransactionAttribute
    public PaymentEntity createPayment(double sum) throws AuthenticationException, ObjectNotFoundException {
        UsersEntity user = userProvider.getAuthorizedUser();

        CreditEntity credit = creditBean.creditActive(user.getPeopleMainId().getId()).getEntity();
        if (credit == null) {
            throw new IllegalStateException("User " + user.getId() + " has no active credit");
        }

        return paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE, Payment.SUM_FROM_CLIENT, sum,
                Payment.TO_SYSTEM, Partner.YANDEX).getEntity();
    }

    private YandexAcquiringPluginConfig getYandexPluginConfig() {
        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext yandexExecutionContext = PluginExecutionContext.createExecutionContext(actionContext,
                YandexAcquiringBeanLocal.SYSTEM_NAME);

        return (YandexAcquiringPluginConfig) yandexExecutionContext.getPluginConfig();
    }
}
