package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.AntifraudDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageTypesEntity;
import ru.simplgroupp.toolkit.common.Utils;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AntifraudDAOImpl implements AntifraudDAO {

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    CreditRequestDAO creditRequestDAO;

    @Override
    public AntifraudFieldEntity saveAntifraudField(String fieldName, String fieldValueBefore, String fieldValueAfter, Integer creditRequestId, String sessionId) {

        AntifraudFieldEntity entity = new AntifraudFieldEntity();
        entity.setValueBefore(fieldValueBefore);
        entity.setValueAfter(fieldValueAfter);
        entity.setCreateAt(new Date());
        entity.setType(getAntifraudTypeByName(fieldName));
        if (creditRequestId != null) {
            CreditRequestEntity creditRequestEntity = creditRequestDAO.getCreditRequestEntity(creditRequestId);
            if (creditRequestEntity != null) {
                entity.setCreditRequestId(creditRequestEntity);
                entity.setPeopleMainId(creditRequestEntity.getPeopleMainId());
            }
        }
        entity.setSessionId(sessionId);

        emMicro.persist(entity);
        return entity;
    }

    @Override
    public AntifraudPageEntity saveAntifraudPage(String pageName, boolean isLeaving, Integer creditRequestId, String sessionId) {

        AntifraudPageEntity entity = null;
        AntifraudPageTypesEntity type = getAntifraudPageTypeByName(pageName);
        if(type == null) return null;

        if (isLeaving) {

            entity = getAntifraudPageEntityBySessionId(type, sessionId);

            if (entity != null) {
                entity.setDateEnd(new Date());
            }
        }

        else {

            entity = new AntifraudPageEntity();
            entity.setDateStart(new Date());
            entity.setType(type);

            if (creditRequestId != null) {
                CreditRequestEntity creditRequestEntity = creditRequestDAO.getCreditRequestEntity(creditRequestId);
                if (creditRequestEntity != null) {
                    entity.setCreditRequestId(creditRequestEntity);
                    entity.setPeopleMainId(creditRequestEntity.getPeopleMainId());
                }
            }
            entity.setSessionId(sessionId);
        }

        if(entity != null) {
            emMicro.persist(entity);
        }
        return entity;
    }

    @Override
    public void updateFieldsRequestId(CreditRequestEntity creditRequestEntity, String sessionId) {

        Query qry = emMicro.createNamedQuery("updateFieldsRequestId");
        qry.setParameter("creditRequestEntity", creditRequestEntity);
        qry.setParameter("peopleMainEntity", creditRequestEntity.getPeopleMainId());
        qry.setParameter("sessionId", sessionId);
        qry.executeUpdate();
    }

    public AntifraudFieldTypesEntity getAntifraudTypeByName(String typeName) {
        Query qry = emMicro.createNamedQuery("findAntifraudTypeByName");
        qry.setParameter("typeName", typeName);
        List<AntifraudFieldTypesEntity> lst = qry.getResultList();
        return (AntifraudFieldTypesEntity) Utils.firstOrNull(lst);
    }

    public AntifraudPageTypesEntity getAntifraudPageTypeByName(String typeName) {
        Query qry = emMicro.createNamedQuery("findAntifraudPageTypeByName");
        qry.setParameter("typeName", typeName);
        List<AntifraudPageTypesEntity> lst = qry.getResultList();
        return (AntifraudPageTypesEntity) Utils.firstOrNull(lst);
    }

    public AntifraudPageEntity getAntifraudPageEntityBySessionId(AntifraudPageTypesEntity typeName, String sessionId) {
        Query qry = emMicro.createNamedQuery("findAntifraudPageEntityBySessionId");
        qry.setParameter("typeName", typeName);
        qry.setParameter("sessionId", sessionId);
        List<AntifraudPageTypesEntity> lst = qry.getResultList();
        return (AntifraudPageEntity) Utils.firstOrNull(lst);
    }
}
