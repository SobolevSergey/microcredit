package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.IdentityQuestionDAO;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.IdentityQuestion;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IdentityQuestionDAOImpl implements IdentityQuestionDAO {
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @EJB
    private CreditRequestDAO creditRequestDAO;


    @Override
    public IdentityTemplateEntity getTemplate(int id) {
        return emMicro.find(IdentityTemplateEntity.class, id);
    }

    @Override
    public IdentityQuestionEntity saveQuestion(IdentityQuestionEntity question) {
        question.setTemplate(getTemplate(question.getTemplate().getId()));
        return emMicro.merge(question);
    }

    @Override
    public IdentityQuestionEntity getQuestionEntity(int id) {
        return emMicro.find(IdentityQuestionEntity.class, id);
    }

    @Override
    public IdentityQuestion getQuestion(int id, Set options) {
        IdentityQuestionEntity entity = getQuestionEntity(id);
        if (entity != null) {
            IdentityQuestion question = new IdentityQuestion(entity);
            question.init(options);
            return question;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<IdentityQuestion> findQuestions(Integer creditRequestId, Integer creditId, Integer peopleMainId, Set options) {
        String hql = "select q from ru.simplgroupp.persistence.IdentityQuestionEntity q where (1=1) ";

        if (creditRequestId != null) {
            hql += "and (q.creditRequest.id = :creditRequestId) ";
        }
        if (creditId != null) {
            hql += "and (q.credit.id = :creditId) ";
        }
        if (peopleMainId != null) {
            hql += "and (q.peopleMain.id = :peopleMainId) ";
        }

        Query query = emMicro.createQuery(hql);

        if (creditRequestId != null) {
            query.setParameter("creditRequestId", creditRequestId);
        }
        if (creditId != null) {
            query.setParameter("creditId", creditId);
        }
        if (peopleMainId != null) {
            query.setParameter("peopleMainId", peopleMainId);
        }

        List<IdentityQuestionEntity> entities = query.getResultList();

        List<IdentityQuestion> list = new ArrayList<>();
        for (IdentityQuestionEntity entity : entities) {
            list.add(new IdentityQuestion(entity));
        }
        Utils.initCollection(list, options);


        return list;
    }

    @Override
    public IdentityAnswerEntity saveAnswer(int id) {
        IdentityAnswerEntity answer = new IdentityAnswerEntity();
        IdentityOptionEntity option = getOption(id);

        answer.setOption(option);
        option.setAnswer(answer);

        option = emMicro.merge(option);
        return option.getAnswer();
    }

    @Override
    public IdentityOptionEntity getOption(int id) {
        return emMicro.find(IdentityOptionEntity.class, id);
    }
}
