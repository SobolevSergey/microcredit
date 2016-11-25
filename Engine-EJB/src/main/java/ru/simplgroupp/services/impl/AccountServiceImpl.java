package ru.simplgroupp.services.impl;

import ru.simplgroupp.dao.interfaces.AccountDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PayonlineVerification;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.services.AccountService;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.RefHeader;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.List;

/**
 * Account service implementation
 */
@Singleton
@TransactionManagement
public class AccountServiceImpl implements AccountService {

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Inject
    private AccountDAO accountDAO;

    @Override
    public AccountEntity createPayonlineCard(PeopleMainEntity people,
            PayonlineVerification payonlineVerification) {
        AccountEntity account = new AccountEntity();
        account.setPeopleMainId(people);
        account.setIsactive(ActiveStatus.ACTIVE);
        account.setAccountTypeId(referenceBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.PAYONLINE_CARD_TYPE));
        account.setCardNumberMasked(payonlineVerification.getMaskedCardNumber());
        account.setRebillAnchor(payonlineVerification.getRebillAnchor());

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public AccountEntity savePayonlineCard(Integer accountId, String cardNumberMasked, String rebillAnchor,
            String cardHolder) throws ObjectNotFoundException {
        AccountEntity account = accountDAO.getAccountEntity(accountId);
        if (account == null) {
            throw new ObjectNotFoundException("Account with id " + accountId + " not found");
        }

        account.setIsactive(ActiveStatus.ACTIVE);
        account.setRebillAnchor(rebillAnchor);
        account.setAccountnumber(cardNumberMasked);
        account.setCardHolder(cardHolder);

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public AccountEntity createBankAccount(PeopleMainEntity people, String bankBik, String bankName,
            String bankKpp, String bankCorAccount, String accountNumber) {
        AccountEntity account = new AccountEntity();
        account.setPeopleMainId(people);
        account.setIsactive(ActiveStatus.ACTIVE);
        account.setAccountTypeId(referenceBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.BANK_TYPE));
        account.setBik(bankBik);
        account.setBankname(bankName);
        account.setKpp(bankKpp);
        account.setCorraccountnumber(bankCorAccount);
        account.setAccountnumber(accountNumber);

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public AccountEntity createCard(PeopleMainEntity people, String number) {
        AccountEntity account = new AccountEntity();
        account.setPeopleMainId(people);
        account.setIsactive(ActiveStatus.ACTIVE);
        account.setAccountTypeId(referenceBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE));
        account.setAccountnumber(number);

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public AccountEntity createYandexWallet(PeopleMainEntity people, String number) {
        AccountEntity account = new AccountEntity();
        account.setPeopleMainId(people);
        account.setIsactive(ActiveStatus.ACTIVE);
        account.setAccountTypeId(referenceBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.YANDEX_TYPE));
        account.setAccountnumber(number);

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public AccountEntity createYandexCard(PeopleMainEntity people, String cardSynonim, String cardMask) {
        AccountEntity account = new AccountEntity();
        account.setPeopleMainId(people);
        account.setIsactive(ActiveStatus.ACTIVE);
        account.setAccountTypeId(referenceBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.YANDEX_CARD_TYPE));
        account.setCardNumberMasked(cardMask);
        account.setCardSynonim(cardSynonim);

        return accountDAO.saveAccountEntity(account);
    }

    @Override
    public List<AccountEntity> findYandexWallet(PeopleMainEntity peopleMainId, String number) {
        ReferenceEntity yandexType = referenceBooks.getAccountType(Account.YANDEX_TYPE).getEntity();
        return accountDAO.findAccountsByPeople(peopleMainId, yandexType, number);
    }

    @Override
    public List<AccountEntity> findCard(PeopleMainEntity peopleMainId, String number) {
        ReferenceEntity cardType = referenceBooks.getAccountType(Account.CARD_TYPE).getEntity();
        return accountDAO.findAccountsByPeople(peopleMainId, cardType, number);
    }

    @Override
    public List<AccountEntity> findBankAccount(PeopleMainEntity peopleMainId, String account) {
        ReferenceEntity bankType = referenceBooks.getAccountType(Account.BANK_TYPE).getEntity();
        return accountDAO.findAccountsByPeople(peopleMainId, bankType, account);
    }

    @Override
    public List<AccountEntity> findYandexCard(PeopleMainEntity peopleMainId, String cardMask) {
        ReferenceEntity yandexCardType = referenceBooks.getAccountType(Account.YANDEX_CARD_TYPE).getEntity();
        return accountDAO.findAccountsByPeopleAndCardMask(peopleMainId, yandexCardType, cardMask);
    }
}
