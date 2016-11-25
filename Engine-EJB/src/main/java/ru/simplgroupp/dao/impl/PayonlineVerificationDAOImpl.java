package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.PayonlineVerificationDAO;
import ru.simplgroupp.persistence.PayonlineVerification;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Payonline verification DAO implementation
 */
@Singleton
@TransactionManagement
public class PayonlineVerificationDAOImpl implements PayonlineVerificationDAO {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Override
    public PayonlineVerification findOne(String guid) {
        return emMicro.find(PayonlineVerification.class, guid);
    }

    @Override
    public List<PayonlineVerification> findByPeople(PeopleMainEntity people) {
        CriteriaBuilder builder = emMicro.getCriteriaBuilder();
        CriteriaQuery<PayonlineVerification> criteriaQuery = builder.createQuery(PayonlineVerification.class);
        Root<PayonlineVerification> payonlineVerification = criteriaQuery.from(PayonlineVerification.class);
        criteriaQuery.where(builder.equal(payonlineVerification.get("people"), people));

        return emMicro.createQuery(criteriaQuery).getResultList();
    }

    @Override
    @TransactionAttribute
    public PayonlineVerification save(PayonlineVerification payonlineVerification) {
        return emMicro.merge(payonlineVerification);
    }

    @Override
    @TransactionAttribute
    public void delete(PayonlineVerification payonlineVerification) {
        emMicro.remove(
                emMicro.contains(payonlineVerification) ? payonlineVerification : emMicro.merge(payonlineVerification));
    }
}
