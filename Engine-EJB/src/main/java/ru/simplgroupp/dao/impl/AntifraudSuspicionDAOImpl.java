package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.AntifraudSuspicionDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.persistence.antifraud.RefHunterRuleEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.antifraud.AntifraudSuspicion;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AntifraudSuspicionDAOImpl implements AntifraudSuspicionDAO {
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private ReferenceBooksLocal referenceBooks;


    @Override
    public AntifraudSuspicionEntity getEntity(int id) {
        return emMicro.find(AntifraudSuspicionEntity.class, id);
    }

    @Override
    public AntifraudSuspicion getSuspicion(int id, Set options) {
        AntifraudSuspicionEntity entity = getEntity(id);
        if (entity != null) {
            AntifraudSuspicion suspicion = new AntifraudSuspicion(entity);
            suspicion.init(options);
            return suspicion;
        }

        return null;
    }

    @Override
    public AntifraudSuspicionEntity buildSuspicion(int creditRequestId, int peopleId, int partnerId, RefAntifraudRulesEntity rule,
                                                   Integer secondPeopleId, Boolean fraud, String comment,
                                                   Double score, Date createdAt,String hunterCode) {
        AntifraudSuspicionEntity entity = new AntifraudSuspicionEntity();
        entity.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
        entity.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleId));
        entity.setPartnersId(referenceBooks.getPartnerEntity(partnerId));
        if (rule!=null){
            entity.setRule(findHunterRule(rule.getCode()));
            entity.setInternalRule(rule);
        } else if (StringUtils.isNotEmpty(hunterCode)){
        	entity.setRule(findHunterRule(hunterCode));
        }
        
        entity.setComment(comment);
        entity.setScore(score);

        if (createdAt == null) {
            createdAt = new Date();
        }
        entity.setCreatedAt(createdAt);

        if (secondPeopleId != null) {
            entity.setSecondPeople(peopleDAO.getPeopleMainEntity(secondPeopleId));
        }

        if (fraud != null) {
            entity.setFraud(fraud);
        }


        return entity;
    }

    @Override
    public AntifraudSuspicionEntity buildSuspicion(int creditRequestId, int peopleId, int partnerId, RefAntifraudRulesEntity rule,
                                                   Integer secondPeopleId, Boolean fraud, String comment,
                                                   Double score, Date createdAt) {
    	return buildSuspicion(creditRequestId,peopleId,partnerId,rule,
                secondPeopleId,fraud, comment,score, createdAt,null);
    }
    
    @Override
    public AntifraudSuspicionEntity saveSuspicion(AntifraudSuspicionEntity entity) {
        return emMicro.merge(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AntifraudSuspicion> find(Integer creditRequestId, Integer peopleId, Integer partnerId,
                                         Integer ruleId, Integer secondPeopleId, Boolean fraud, Set options) {
        String hql = "select s from ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity s where (1=1) ";

        if (creditRequestId != null) {
            hql += "and (s.creditRequestId.id = :creditRequestId) ";
        }

        if (peopleId != null) {
            hql += "and (s.peopleMainId.id = :peopleId) ";
        }

        if (partnerId != null) {
            hql += "and (s.partnersId.id = :partnerId) ";
        }

        if (ruleId != null) {
            hql += "and (s.rule.id = :ruleId) ";
        }

        if (secondPeopleId != null) {
            hql += "and (s.secondPeople.id = :secondPeopleId) ";
        }

        if (fraud != null) {
            hql += "and (s.fraud = :fraud) ";
        }

        Query query = emMicro.createQuery(hql);

        if (creditRequestId != null) {
            query.setParameter("creditRequestId", creditRequestId);
        }

        if (peopleId != null) {
            query.setParameter("peopleId", peopleId);
        }

        if (partnerId != null) {
            query.setParameter("partnerId", partnerId);
        }

        if (ruleId != null) {
            query.setParameter("ruleId", ruleId);
        }

        if (secondPeopleId != null) {
            query.setParameter("secondPeopleId", secondPeopleId);
        }

        if (fraud != null) {
            query.setParameter("fraud", fraud);
        }


        List<AntifraudSuspicionEntity> entities = query.getResultList();

        List<AntifraudSuspicion> list = new ArrayList<>();
        for (AntifraudSuspicionEntity entity : entities) {
            list.add(new AntifraudSuspicion(entity));
        }
        Utils.initCollection(list, options);


        return list;
    }

    @Override
    public RefHunterRuleEntity findHunterRule(String code) {
    	return (RefHunterRuleEntity) JPAUtils.getSingleResult(emMicro, "findHunterRuleByCode", Utils.mapOf("ruleCode", code));
    }

    @Override
    public void delete(int id) {
        emMicro.remove(getEntity(id));
    }
}
