package ru.simplgroupp.fixtures;

import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;

/**
 * Created by kds on 16.07.14.
 */
public class CreditRequestFixture {

    public static CreditRequest createCreditRequest(Account account) {

        CreditRequestEntity entity = new CreditRequestEntity();
        entity.setCreditsum(100.0);
        entity.setConfirmed(true);
        entity.setAccountId(account.getEntity());

        return new CreditRequest(entity);
    }
}
