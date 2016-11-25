package ru.simplgroupp.services;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;

/**
 * Credit request service
 */
@Local
public interface CreditRequestService {

    CreditRequestEntity findCreditRequest(Integer creditRequestId);

    CreditRequestEntity findActiveCreditRequest(Integer peopleId);

    CreditRequestEntity findCreditRequestWaitingSign(int peopleId);

    void assignAccount(CreditRequestEntity creditRequest, AccountEntity account);
}
