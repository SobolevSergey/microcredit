package ru.simplgroupp.fixtures;

import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;

import java.util.Calendar;

/**
 * Created by kds on 16.07.14.
 */
public class CreditFixture {

    public static Credit createCredit(CreditRequest creditRequest) {

        CreditEntity entity = new CreditEntity();
        entity.setCreditRequestId(creditRequest.getEntity());
        entity.setCreditsum(creditRequest.getCreditSum());
        entity.setCreditsumback(creditRequest.getCreditSum());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        entity.setCreditdatabeg(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, +5);
        entity.setCreditdataend(calendar.getTime());

        return new Credit(entity);
    }
}
