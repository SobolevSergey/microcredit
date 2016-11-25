package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.PayonlineVerification;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;
import java.util.List;

/**
 * Payonline verification DAO
 */
@Local
public interface PayonlineVerificationDAO {

    PayonlineVerification findOne(String guid);

    List<PayonlineVerification> findByPeople(PeopleMainEntity people);

    PayonlineVerification save(PayonlineVerification payonlineVerification);

    void delete(PayonlineVerification payonlineVerification);
}
