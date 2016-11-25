package ru.simplgroupp.services;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PayonlineVerification;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.List;

/**
 * Account service
 */
@Local
public interface AccountService {

    AccountEntity createPayonlineCard(PeopleMainEntity people, PayonlineVerification payonlineVerification);

    /**
     * Сохранить хэш карты payonline
     *
     * @param creditRequestId - id заявки
     * @param cardNumberMasked - маскированный номер карты
     * @param rebillAnchor - RebillAnchor
     * @param cardHolder - владелец карты
     */
    AccountEntity savePayonlineCard(Integer creditRequestId, String cardNumberMasked, String rebillAnchor, String cardHolder)
            throws ObjectNotFoundException;

    AccountEntity createBankAccount(PeopleMainEntity people, String bankBik, String bankName, String bankKpp,
            String bankCorAccount, String accountNumber);

    AccountEntity createCard(PeopleMainEntity people, String number);

    AccountEntity createYandexWallet(PeopleMainEntity people, String number);

    AccountEntity createYandexCard(PeopleMainEntity people, String cardSynonim, String cardMask);

    List<AccountEntity> findYandexWallet(PeopleMainEntity people, String number);

    List<AccountEntity> findCard(PeopleMainEntity people, String number);

    List<AccountEntity> findBankAccount(PeopleMainEntity people, String account);

    List<AccountEntity> findYandexCard(PeopleMainEntity people, String cardMask);
}
