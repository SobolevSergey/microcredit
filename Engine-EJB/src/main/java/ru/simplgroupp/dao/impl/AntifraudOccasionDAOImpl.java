package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.QuestionVariant;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AntifraudOccasionDAOImpl implements AntifraudOccasionDAO {
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private ReferenceBooksLocal referenceBooks;

    @EJB
    private RefAntifraudRulesDAO refAntifraudRulesDAO;

    @EJB
    private UsersDAO usersDAO;


    @Override
    public AntifraudOccasionEntity getEntity(int id) {
        return emMicro.find(AntifraudOccasionEntity.class, id);
    }

    @Override
    public AntifraudOccasion getMain(int id, Set options) {
        AntifraudOccasionEntity entity = getEntity(id);
        if (entity != null) {
            AntifraudOccasion suspicion = new AntifraudOccasion(entity);
            suspicion.init(options);
            return suspicion;
        }

        return null;
    }

    @Override
    public AntifraudOccasionEntity buildEntity(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                                               int statusCode, Date createdAt, Date updatedAt, String comment) {
        AntifraudOccasionEntity entity = new AntifraudOccasionEntity();

        if (userId != null) {
            entity.setUser(usersDAO.getUsersEntity(userId));
        }
        entity.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
        entity.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleId));
        entity.setHunterObject(referenceBooks.getHunterObject(hunterObjectCode).getEntity());
        entity.setStatus(referenceBooks.getAntifraudStatus(statusCode).getEntity());
        entity.setUpdatedAt(updatedAt);
        entity.setComment(comment);

        if (createdAt == null) {
            createdAt = new Date();
        }
        entity.setCreatedAt(createdAt);

        return entity;
    }

    @Override
    public AntifraudOccasionEntity buildEntity(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                                               int statusCode, Date createdAt, Date updatedAt, String comment,
                                               QuestionVariantEntity questionVariantEntity) {
        AntifraudOccasionEntity entity = buildEntity(userId, creditRequestId, peopleId, hunterObjectCode, statusCode,
                createdAt, updatedAt, comment);
        entity.setQuestionOption(questionVariantEntity);

        return entity;
    }

    @Override
    public AntifraudOccasionEntity save(AntifraudOccasionEntity entity) {
        return emMicro.merge(entity);
    }

    @Override
    public List<AntifraudOccasion> find(Integer creditRequestId, Integer peopleMainId, String hunterObjectCode,
                                        Integer statusCode, Integer isActive, Set options) {
        String hql = "select s from ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity s where (1=1) ";

        if (creditRequestId != null) {
            hql += "and (s.creditRequestId.id = :creditRequestId) ";
        }

        if (peopleMainId != null) {
            hql += "and (s.peopleMainId.id = :peopleMainId) ";
        }

        if (statusCode != null) {
            hql += "and (s.status.codeinteger = :statusCode) ";
        }

        if (hunterObjectCode != null) {
            hql += "and (s.hunterObject.code = :hunterObjectCode) ";
        }

        if (isActive != null) {
            hql += "and (s.isActive = :isActive) ";
        }

        Query query = emMicro.createQuery(hql);

        if (creditRequestId != null) {
            query.setParameter("creditRequestId", creditRequestId);
        }

        if (peopleMainId != null) {
            query.setParameter("peopleMainId", peopleMainId);
        }

        if (statusCode != null) {
            query.setParameter("statusCode", statusCode);
        }

        if (hunterObjectCode != null) {
            query.setParameter("hunterObjectCode", hunterObjectCode);
        }

        if (isActive != null) {
            query.setParameter("isActive", isActive);
        }


        List<AntifraudOccasionEntity> entities = query.getResultList();

        List<AntifraudOccasion> list = new ArrayList<>();
        for (AntifraudOccasionEntity entity : entities) {
            list.add(new AntifraudOccasion(entity));
        }
        Utils.initCollection(list, options);


        return list;
    }

    @Override
    public void delete(int id) {
        AntifraudOccasionEntity occasionEntity = getEntity(id);
        if (occasionEntity.getUpdatedAt() != null) {
            occasionEntity.setIsActive(0);
            occasionEntity.setDataEnd(new Date());
            save(occasionEntity);
            return;
        }

        emMicro.remove(occasionEntity);
    }

    @Override
    public void saveWithCheck(int creditRequestId, int peopleMainId, String hunterObjectCode, String comment) {
        boolean manualSuspectEmpty = find(creditRequestId, peopleMainId, hunterObjectCode,
                AntifraudOccasion.STATUS_MANUAL_SUSPECT, 1, null).isEmpty();
        boolean referEmpty = find(creditRequestId, peopleMainId, hunterObjectCode,
                AntifraudOccasion.STATUS_REFER, 1, null).isEmpty();

        // если уже добавленных мошеннических событий по этому поводу нет, то добавляем новое
        if (manualSuspectEmpty && referEmpty) {
            AntifraudOccasionEntity antifraudOccasion = buildEntity(null, creditRequestId,
                    peopleMainId, hunterObjectCode, AntifraudOccasion.STATUS_AUTO_SUSPECT, new Date(), null, comment);

            save(antifraudOccasion);
        }
    }

    @Override
    public void saveWithCheck(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                              int statusCode, Date createdAt, Date updatedAt, String comment) {
        boolean currentOccasionEmpty = find(creditRequestId, peopleId, hunterObjectCode, statusCode, 1, null).isEmpty();

        // если уже добавленных мошеннических событий по этому поводу нет, то добавляем новое
        if (currentOccasionEmpty) {
            AntifraudOccasionEntity antifraudOccasion = buildEntity(userId, creditRequestId,
                    peopleId, hunterObjectCode, statusCode, createdAt, updatedAt, comment);

            save(antifraudOccasion);
        }
    }

    @Override
    public void saveWithCheck(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                              int statusCode, Date createdAt, Date updatedAt, String comment,
                              QuestionVariantEntity questionVariantEntity) {
        boolean currentOccasionEmpty = find(creditRequestId, peopleId, hunterObjectCode, statusCode, 1, null).isEmpty();

        // если уже добавленных мошеннических событий по этому поводу нет, то добавляем новое
        if (currentOccasionEmpty) {
            AntifraudOccasionEntity antifraudOccasion = buildEntity(userId, creditRequestId,
                    peopleId, hunterObjectCode, statusCode, createdAt, updatedAt, comment, questionVariantEntity);

            save(antifraudOccasion);
        }
    }

    @Override
    public List<AntifraudOccasion> getByQuestion(QuestionEntity questionEntity, Set options) {
        Query query = emMicro.createNamedQuery("findAntifraudOccasionByQuestion");
        query.setParameter("question", questionEntity);

        List<AntifraudOccasionEntity> entities = query.getResultList();

        List<AntifraudOccasion> list = new ArrayList<>();
        for (AntifraudOccasionEntity entity : entities) {
            list.add(new AntifraudOccasion(entity));
        }
        Utils.initCollection(list, options);


        return list;
    }
}
