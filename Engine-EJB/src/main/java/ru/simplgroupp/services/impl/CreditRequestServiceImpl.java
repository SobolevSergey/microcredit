package ru.simplgroupp.services.impl;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.services.CreditRequestService;
import ru.simplgroupp.transfer.CreditStatus;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * Credit request service implementation
 */
@Singleton
@TransactionManagement
public class CreditRequestServiceImpl implements CreditRequestService {

    @Inject
    private CreditRequestDAO creditRequestDAO;

    @Override
    public CreditRequestEntity findCreditRequest(Integer creditRequestId) {
        return creditRequestDAO.getCreditRequestEntity(creditRequestId);
    }

    @Override
    public CreditRequestEntity findActiveCreditRequest(Integer peopleId) {
        List<CreditRequestEntity> creditRequests = creditRequestDAO.findCreditRequestActive(peopleId,
                Arrays.asList(CreditStatus.IN_PROCESS, CreditStatus.FILLED));
        return creditRequests.size() > 0 ? creditRequests.get(0) : null;
    }

    @Override
    public CreditRequestEntity findCreditRequestWaitingSign(int peopleId) {
        List<CreditRequestEntity> creditRequests = creditRequestDAO.findCreditRequestWaitingSign(peopleId);
        return creditRequests.size() > 0 ? creditRequests.get(0) : null;
    }

    @Override
    public void assignAccount(CreditRequestEntity creditRequest, AccountEntity account) {
        creditRequest.setAccountId(account);
        creditRequestDAO.saveCreditRequest(creditRequest);
    }
}
