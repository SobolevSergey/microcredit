package ru.simplgroupp.dao.interfaces;


import ru.simplgroupp.persistence.VerificationConfigEntity;
import ru.simplgroupp.persistence.VerificationReferenceEntity;

/**
 * Verification config DAO
 */
public interface VerificationConfigDAO {

    /**
     * Найти VerificationConfig по VerificationReference для соответствия
     * @param verificationReference the verification reference
     * @return the verification config
     */
    VerificationConfigEntity findForMatching(VerificationReferenceEntity verificationReference);

    /**
     * Найти VerificationConfig для вычисления рейтинга
     * @param verificationReference the verification reference
     * @param diff расхождение в значении полей
     * @return the verification config
     */
    VerificationConfigEntity findForRaiting(VerificationReferenceEntity verificationReference, double diff);
}
