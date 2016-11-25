package ru.simplgroupp.client.common.facade.impl;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.client.common.facade.AccountAlreadyExistException;
import ru.simplgroupp.client.common.facade.AccountFacade;
import ru.simplgroupp.client.common.facade.payment.InvalidCardException;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.AccountService;
import ru.simplgroupp.services.PeopleService;
import ru.simplgroupp.services.UserProvider;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.List;

/**
 * Account facade implementation
 */
@Singleton
@TransactionManagement
public class AccountFacadeImpl implements AccountFacade {

    @Inject
    private UserProvider userProvider;

    @Inject
    private AccountService accountService;

    @Inject
    private transient ActionProcessorBeanLocal actionProcessor;

    @Override
    @TransactionAttribute
    public AccountEntity createCardAccount(String number)
            throws InvalidCardException, AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException {
        UsersEntity user = userProvider.getAuthorizedUser();
        if (!checkCard(number)) {
            throw new InvalidCardException();
        }

        List<AccountEntity> cards =  accountService.findCard(user.getPeopleMainId(), number);
        if (cards.size() > 0) {
            throw new AccountAlreadyExistException();
        }
        return accountService.createCard(user.getPeopleMainId(), number);
    }

    @Override
    public AccountEntity createBankAccount(String bankBik, String bankName, String bankKpp, String bankCorAccount,
            String accountNumber) throws AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException {
        UsersEntity user = userProvider.getAuthorizedUser();
        List<AccountEntity> accounts =  accountService.findBankAccount(user.getPeopleMainId(), accountNumber);
        if (accounts.size() > 0) {
            throw new AccountAlreadyExistException();
        }

        return accountService.createBankAccount(user.getPeopleMainId(), bankBik, bankName, bankKpp, bankCorAccount,
                accountNumber);
    }

    private boolean checkCard(String number) {
        if (StringUtils.isBlank(number) || !StringUtils.isNumeric(number) || number.length() != 16) {
            return false;
        }
        int sum = 0;
        int n = number.length();

        for (int i = 0; i < n; i++) {
            int p = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                p = 2 * p;
                if (p > 9) {
                    p = p - 9;
                }
            }
            sum = sum + p;
        }

        return sum % 10 == 0;
    }

    @Override
    @TransactionAttribute
    public AccountEntity createYandexAccount(String number)
            throws YandexWalletNotIdentifiedException, ActionException, AuthenticationException, ObjectNotFoundException,
            AccountAlreadyExistException, YandexWalletVerificationException {
        UsersEntity user = userProvider.getAuthorizedUser();
        List<AccountEntity> wallets =  accountService.findYandexWallet(user.getPeopleMainId(), number);
        if (wallets.size() > 0) {
            throw new AccountAlreadyExistException();
        }

        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext context = new PluginExecutionContext(actionContext.getPlugins().getPluginConfig
                (YandexPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), actionContext
                .getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
        YandexPayBeanLocal yandexPayBean = actionContext.getPlugins().getYandexPay();

        if (!yandexPayBean.isWalletIdentified(number, context)) {
            throw new YandexWalletNotIdentifiedException();
        }

        if (!yandexPayBean.checkWalletData(number, user.getPeopleMainId(), context)) {
            throw new YandexWalletVerificationException();
        }

        return accountService.createYandexWallet(user.getPeopleMainId(), number);
    }

    @Override
    public AccountEntity createYandexCardAccount(String cardSynonim, String cardMask)
            throws ActionException, AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException {
        UsersEntity user = userProvider.getAuthorizedUser();
        List<AccountEntity> wallets =  accountService.findYandexCard(user.getPeopleMainId(), cardMask);
        if (wallets.size() > 0) {
            throw new AccountAlreadyExistException();
        }
        return accountService.createYandexCard(user.getPeopleMainId(), cardSynonim, cardMask);
    }
}
