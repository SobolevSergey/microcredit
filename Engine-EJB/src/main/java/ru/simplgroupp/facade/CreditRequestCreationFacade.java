package ru.simplgroupp.facade;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.CreditDataDto;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;

/**
 * Credit request creation service
 */
@Local
public interface CreditRequestCreationFacade {

    /**
     * Создание заявки на кредит для нового заёмщика
     *
     * @param creditData   данные кредита
     * @param peopleId     идентификатор заёмщика
     * @param personalData персональные данные
     * @param isNewPeople  новый ли заёмщик
     * @return Заявка на кредит
     */
    CreditRequestEntity createCreditRequest(CreditDataDto creditData, Integer peopleId, PersonalDataDto personalData,
                                            boolean isNewPeople) throws WorkflowException, ObjectNotFoundException;

    void activateCreditRequest(Integer creditRequestId, UsersEntity manager) throws KassaException, WorkflowException;
}
