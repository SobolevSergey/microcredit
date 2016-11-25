package ru.simplgroupp.fixtures;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.transfer.Account;

/**
 * Created by kds on 16.07.14.
 */
public class AccountFixture {

    private static Account yandexInvalidAccount;
    private static Account yandexValidAccount;

    public static Account getYandexInvalidAccount() {

        if (yandexInvalidAccount != null) {
            return yandexInvalidAccount;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountnumber("410039303350");
        yandexInvalidAccount = new Account(accountEntity);

        return yandexInvalidAccount;
    }

    public static Account getYandexValidAccount() {

        if (yandexValidAccount != null) {
            return yandexValidAccount;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountnumber("4100322407607");
        yandexValidAccount = new Account(accountEntity);

        return yandexValidAccount;
    }
}
