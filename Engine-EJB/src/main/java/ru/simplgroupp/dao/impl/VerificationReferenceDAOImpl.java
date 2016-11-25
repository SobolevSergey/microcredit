package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.VerificationReferenceDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.VerificationReference;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Verification reference DAO implementation
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VerificationReferenceDAOImpl implements VerificationReferenceDAO {

    private static final String FIND_FOR_RATE_QUERY = "findVerificationReferenceForRate";

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;


    @Override
    public List<VerificationReferenceEntity> findForRate(String entityName) {
    	return JPAUtils.getResultList(emMicro,FIND_FOR_RATE_QUERY,Utils.mapOf("entityName", entityName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<VerificationReference> findAllReference(Set options) {
        Query query = emMicro.createNamedQuery("findAllVerificationReference");

        List<VerificationReferenceEntity> entities = query.getResultList();

        List<VerificationReference> list = new ArrayList<>();
        for (VerificationReferenceEntity entity : entities) {
            list.add(new VerificationReference(entity));
        }
        Utils.initCollection(list, options);


        return list;
    }

    @Override
    public VerificationReferenceEntity saveReference(VerificationReferenceEntity entity) {
        return emMicro.merge(entity);
    }

    @Override
    public VerificationReferenceEntity getReferenceEntity(int id) {
        return emMicro.find(VerificationReferenceEntity.class, id);
    }

    @Override
    public VerificationReference getReference(int id, Set options) {
        VerificationReferenceEntity entity = getReferenceEntity(id);
        if (entity != null) {
            VerificationReference transfer = new VerificationReference(entity);
            transfer.init(options);
            return transfer;
        }

        return null;
    }
    
    @Override
    public boolean removeReference(int id) {
        VerificationReferenceEntity entity = getReferenceEntity(id);
        if (entity != null) {
            emMicro.remove(entity);
            return true;
        }

        return false;
    }
}
