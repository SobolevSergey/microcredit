package ru.simplgroupp.client.common.facade;

import ru.simplgroupp.client.common.facade.impl.YandexWalletNotIdentifiedException;
import ru.simplgroupp.client.common.facade.impl.YandexWalletVerificationException;
import ru.simplgroupp.client.common.facade.payment.InvalidCardException;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.persistence.AccountEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.naming.AuthenticationException;

/**
 * Account facade
 */
@Local
public interface AccountFacade {

    AccountEntity createCardAccount(String number)
            throws InvalidCardException, AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException;

    AccountEntity createBankAccount(String bankBik, String bankName, String bankKpp, String bankCorAccount,
            String accountNumber)
            throws AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException;

    AccountEntity createYandexAccount(String number)
            throws YandexWalletNotIdentifiedException, ActionException, AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException, YandexWalletVerificationException;

    AccountEntity createYandexCardAccount(String cardSynonim, String cardMask)
            throws ActionException, AuthenticationException, ObjectNotFoundException, AccountAlreadyExistException;
}
