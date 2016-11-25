package ru.simplgroupp.ejb;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.dao.interfaces.AntifraudOccasionDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.QuestionBeanLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.verificatorquestion.QuestionVariantStatusEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Question;
import ru.simplgroupp.transfer.QuestionAnswer;
import ru.simplgroupp.transfer.QuestionVariant;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;
import ru.simplgroupp.transfer.verificatorquestion.QuestionVariantStatus;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.SearchUtil;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(QuestionBeanLocal.class)
public class QuestionBean implements QuestionBeanLocal {
    private static HashMap<String, Object> questionSortMapping = new HashMap<>();

    static {
        questionSortMapping.put("QuestionCode", "c.questionCode");
        questionSortMapping.put("QuestionText", "c.questionText");
    }

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @EJB
    private ProductDAO productDAO;

    @EJB
    private CreditRequestDAO crDAO;

    @EJB
    private AntifraudOccasionDAO antifraudOccasionDAO;


    @Override
    public QuestionEntity getQuestion(int id) {
        return emMicro.find(QuestionEntity.class, id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Question getQuestion(int id, Set options) {
        QuestionEntity ent = getQuestion(id);
        if (ent == null) {
            return null;
        }
        Question res = new Question(ent);
        res.init(options);
        return res;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeQuestionVariants(QuestionEntity ent) {
        QuestionEntity res = emMicro.merge(ent);
        for (QuestionVariantEntity vent : res.getVariants()) {
            emMicro.remove(vent);
        }
        res.getVariants().clear();
        emMicro.persist(res);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeQuestionVariant(int varId) {
        QuestionVariantEntity vent = getQuestionVariant(varId);
        if (vent != null) {
            String sql = "delete from questionvariant where id=:id";
            Query qry = emMicro.createNativeQuery(sql);
            qry.setParameter("id", varId);
            qry.executeUpdate();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionEntity saveQuestion(QuestionEntity source) {
        return emMicro.merge(source);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionEntity saveQuestion(Question question) {
        if (question.getProduct() != null) {
            question.getEntity().setProductId(question.getProduct().getEntity());
        }

        if (question.getType() != null) {
            question.getEntity().setType(question.getType().getEntity());
        }

        List<QuestionVariantEntity> options = new ArrayList<>();
        for (QuestionVariant option : question.getOptions()) {
            List<QuestionVariantStatusEntity> statuses = new ArrayList<>();
            for (QuestionVariantStatus status : option.getStatuses()) {
                status.getEntity().setAntifraudEntity(status.getAntifraudEntity().getEntity());
                status.getEntity().setAntifraudStatus(status.getAntifraudStatus().getEntity());

                statuses.add(status.getEntity());
            }
            option.getEntity().setStatuses(statuses);

            options.add(option.getEntity());
        }
        question.getEntity().setVariants(options);


        return saveQuestion(question.getEntity());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeQuestion(int id) {
        String sql = "delete from question where id=:id";
        Query qry = emMicro.createNativeQuery(sql);
        qry.setParameter("id", id);
        qry.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionEntity createQuestion(String code) {
        return createQuestion(code, null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionEntity createQuestion(String code, Integer productId) {
        QuestionEntity ent = findByCode(code, productId);
        if (ent != null) {
            return ent;
        }

        ent = new QuestionEntity();
        ent.setQuestionCode(code);
        if (productId != null) {
            ent.setProductId(productDAO.getProduct(productId));
        }
        ent.setQuestionText("новый вопрос");
        ent.setAnswerType(Question.ANSWER_SINGLE);
        emMicro.persist(ent);
        return ent;
    }

    @Override
    public QuestionEntity findByCode(String code) {
        return findByCode(code, null);
    }

    @Override
    public QuestionEntity findByCode(String code, Integer productId) {
        String sql = "from ru.simplgroupp.persistence.QuestionEntity where (questionCode = :code) ";
        if (productId != null) {
            sql += "and (productId.id=:productId)";
        }
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("code", code);
        if (productId != null) {
            qry.setParameter("productId", productId);
        }
        List<QuestionEntity> lst = qry.getResultList();
        return (QuestionEntity) Utils.firstOrNull(lst);
    }

    @Override
    public int countQuestion(String questionCodeTpl, String questionTextTpl,
                             Integer answerType, Integer productId) {
        String sql = "select count(*) from ru.simplgroupp.persistence.QuestionEntity where (1=1) ";
        if (StringUtils.isNotEmpty(questionCodeTpl)) {
            sql = sql + " and (upper(questionCode) like :questionCodeTpl)";
        }
        if (StringUtils.isNotEmpty(questionTextTpl)) {
            sql = sql + " and (upper(questionText) like :questionTextTpl)";
        }
        if (answerType != null && answerType != 0) {
            sql = sql + " and (answerType = :answerType)";
        }
        if (productId != null && productId != 0) {
            sql = sql + " and (c.productId.id = :productId)";
        }
        Query qry = emMicro.createQuery(sql);
        if (StringUtils.isNotEmpty(questionCodeTpl)) {
            qry.setParameter("questionCodeTpl", questionCodeTpl.trim().toUpperCase() + "%");
        }
        if (StringUtils.isNotEmpty(questionTextTpl)) {
            qry.setParameter("questionTextTpl", "%" + questionTextTpl.trim().toUpperCase() + "%");
        }
        if (answerType != null && answerType != 0) {
            qry.setParameter("answerType", answerType);
        }
        if (productId != null && productId != 0) {
            qry.setParameter("productId", productId);
        }
        Number res = (Number) qry.getSingleResult();
        if (res == null) {
            return 0;
        } else {
            return res.intValue();
        }

    }

    @Override
    public List<Question> listQuestion(int nFirst, int nCount, SortCriteria[] sorting, Set options,
                                       String questionCodeTpl, String questionTextTpl, Integer answerType, Integer productId) {
        String sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.QuestionEntity as c where (1=1) ";
        if (StringUtils.isNotEmpty(questionCodeTpl)) {
            sql = sql + " and (upper(c.questionCode) like :questionCodeTpl)";
        }
        if (StringUtils.isNotEmpty(questionTextTpl)) {
            sql = sql + " and (upper(c.questionText) like :questionTextTpl)";
        }
        if (answerType != null && answerType != 0) {
            sql = sql + " and (c.answerType = :answerType)";
        }
        if (productId != null && productId != 0) {
            sql = sql + " and (c.productId.id = :productId)";
        }

        sql = sql + SearchUtil.sortToString(questionSortMapping, sorting);
        String sortSelect = SearchUtil.sortSelectToString(questionSortMapping, sorting);
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

        Query qry = emMicro.createQuery(sql);

        if (StringUtils.isNotEmpty(questionCodeTpl)) {
            qry.setParameter("questionCodeTpl", questionCodeTpl.trim().toUpperCase() + "%");
        }
        if (StringUtils.isNotEmpty(questionTextTpl)) {
            qry.setParameter("questionTextTpl", "%" + questionTextTpl.trim().toUpperCase() + "%");
        }
        if (answerType != null && answerType != 0) {
            qry.setParameter("answerType", answerType);
        }
        if (productId != null && productId != 0) {
            qry.setParameter("productId", productId);
        }
        if (nFirst >= 0) {
            qry.setFirstResult(nFirst);
        }
        if (nCount > 0) {
            qry.setMaxResults(nCount);
        }

        List<QuestionEntity> lst = null;
        if (sorting == null || sorting.length == 0) {
            lst = qry.getResultList();
        } else {
            List<Object[]> lstSource = qry.getResultList();
            lst = new ArrayList<>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, lst);
            lstSource = null;
        }

        List<Question> lstRes = new ArrayList<>();
        for (QuestionEntity ent : lst) {
            Question quest = new Question(ent);
            quest.init(options);
            lstRes.add(quest);
        }
        return lstRes;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionAnswerEntity addQAFake(int creditRequestId, String key) {

        CreditRequestEntity cre = crDAO.getCreditRequestEntity(creditRequestId);
        if (cre == null) {
            return null;
        }

        Integer productId = null;
        if (cre.getProductId() != null) {
            productId = cre.getProductId().getId();
        }

        QuestionEntity quest = findByCode(key, productId);
        if (quest == null) {
            return null;
        }

        QuestionAnswerEntity ent = new QuestionAnswerEntity();
        ent.setQuestionId(quest);
        ent.setCreditRequestId(cre);
        ent.setPeopleMainId(cre.getPeopleMainId());
        ent.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_NOT_ASKED);
        return ent;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionAnswerEntity addQA(int creditRequestId, String key) {

        CreditRequestEntity cre = crDAO.getCreditRequestEntity(creditRequestId);
        if (cre == null) {
            return null;
        }

        Integer productId = null;
        if (cre.getProductId() != null) {
            productId = cre.getProductId().getId();
        }

        QuestionEntity quest = findByCode(key, productId);
        if (quest == null) {
            return null;
        }


        QuestionAnswerEntity ent = new QuestionAnswerEntity();
        ent.setQuestionId(quest);
        ent.setCreditRequestId(cre);
        ent.setCreatedAt(new Date());
        ent.setPeopleMainId(cre.getPeopleMainId());
        ent.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_NOT_ASKED);
        emMicro.persist(ent);
        return ent;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeQA(QuestionAnswerEntity ent) {
        emMicro.merge(ent);
        emMicro.remove(ent);
    }

    @Override
    public QuestionAnswerEntity findQA(int creditRequestId, String key) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        Integer productId = null;
        if (creditRequest != null) {
            if (creditRequest.getProductId() != null) {
                productId = creditRequest.getProductId().getId();
            }
        }

        String sql = "from ru.simplgroupp.persistence.QuestionAnswerEntity where (creditRequestId.id = :creditRequestId) and (questionId.questionCode = :key)";
        if (productId != null) {
            sql += " and (questionId.productId.id=:productId)";
        }
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("creditRequestId", creditRequestId);
        qry.setParameter("key", key);
        if (productId != null) {
            qry.setParameter("productId", productId);
        }
        List<QuestionAnswerEntity> lst = qry.getResultList();
        return (QuestionAnswerEntity) Utils.firstOrNull(lst);

    }

    @Override
    public int countQuestionAnswer(Integer creditRequestId, int[] statuses) {
        String sql = "select count(*) from ru.simplgroupp.persistence.QuestionAnswerEntity where (1=1) ";
        if (creditRequestId != null) {
            sql = sql + " and (creditRequestId.id = :creditRequestId)";
        }
        if (statuses != null) {
            sql = sql + " and (answerStatus in (:statuses))";
        }
        Query qry = emMicro.createQuery(sql);
        if (creditRequestId != null) {
            qry.setParameter("creditRequestId", creditRequestId);
        }
        if (statuses != null) {
            ArrayList<Integer> lst = new ArrayList<>(statuses.length);
            for (int n : statuses) {
                lst.add(n);
            }
            qry.setParameter("statuses", lst);
        }
        Number res = (Number) qry.getSingleResult();
        if (res == null) {
            return 0;
        } else {
            return res.intValue();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<QuestionAnswer> listQuestionAnswer(int nFirst, int nCount, SortCriteria[] sorting, Set options,
                                                   Integer creditRequestId, int[] statuses) {
        String sql = "select c from ru.simplgroupp.persistence.QuestionAnswerEntity as c where (1=1) ";

        if (creditRequestId != null) {
            sql = sql + " and (c.creditRequestId.id = :creditRequestId)";
        }
        if (statuses != null) {
            sql = sql + " and (c.answerStatus in (:statuses))";
        }

        Query qry = emMicro.createQuery(sql);

        if (creditRequestId != null) {
            qry.setParameter("creditRequestId", creditRequestId);
        }
        if (statuses != null) {
            ArrayList<Integer> lst = new ArrayList<>();
            for (int n : statuses) {
                lst.add(n);
            }
            qry.setParameter("statuses", lst);
        }

        if (nFirst >= 0) {
            qry.setFirstResult(nFirst);
        }
        if (nCount > 0) {
            qry.setMaxResults(nCount);
        }


        List<QuestionAnswerEntity> entities = qry.getResultList();

        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();
        for (QuestionAnswerEntity ent : entities) {
            QuestionAnswer qa = new QuestionAnswer(ent);
            qa.init(options);
            questionAnswers.add(qa);
        }

        return questionAnswers;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionAnswer saveQuestionAnswer(QuestionAnswer questionAnswer) {
        if (questionAnswer.getUser() != null) {
            questionAnswer.getEntity().setUser(questionAnswer.getUser().getEntity());
        } else {
            questionAnswer.getEntity().setUser(null);
        }

        if (questionAnswer.isAnswerDenied()) {
            questionAnswer.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_DENIAL);
        }

        if (questionAnswer.isAnsweredByValue()) {
            questionAnswer.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_ANSWERED);
        }

        saveAntifraudOccasions(questionAnswer);


        return new QuestionAnswer(saveQuestionAnswerEntity(questionAnswer.getEntity()));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionAnswerEntity saveQuestionAnswerEntity(QuestionAnswerEntity entity) {
        entity.setUpdatedAt(new Date());

        return emMicro.merge(entity);
    }

    @Override
    public void saveAntifraudOccasions(QuestionAnswer questionAnswer) {
        // если ответ это не выбор, то дальше делать нечего
        if (questionAnswer.getQuestion().getAnswerType() != Question.ANSWER_SINGLE) {
            return;
        }



        // первым делом надо удалить прошлые случаи мошенничества по этому варианту
        // потому что это может быть сохранение не первого ответа на вопрос
        // а изменение варианта ответа на уже отвеченный вопрос
        // удаляем по вопросу, ищем все случаи с вариантами ответа у которых указан наш вопрос
        List<AntifraudOccasion> currentOccasions = antifraudOccasionDAO.getByQuestion(
                questionAnswer.getQuestion().getEntity(), null);

        for (AntifraudOccasion antifraudOccasion : currentOccasions) {
            antifraudOccasionDAO.delete(antifraudOccasion.getId());
        }

        // вообще есть ли выбранный вариант ответа на вопрос
        // если нет, то удалили и хватит
        if (questionAnswer.getAnswerValueRef() == null) {
            return;
        }

        // получаем вариант ответа который мы выбрали
        QuestionVariantEntity questionVariantEntity = getQuestionVariant(questionAnswer.getAnswerValueRef());

        // теперь добавляем новые случие которые есть у варианта
        // может быть мы и не меняли вариант ответа, но пересохранение это не big deal,
        // ничего опасного не случится, сохраняем указывая при этом у случая вариант ответа!
        if (questionVariantEntity != null) {
            for (QuestionVariantStatusEntity statusEntity : questionVariantEntity.getStatuses()) {
                antifraudOccasionDAO.saveWithCheck(null,
                        questionAnswer.getCreditRequest().getId(),
                        questionAnswer.getCreditRequest().getPeopleMain().getId(),
                        statusEntity.getAntifraudEntity().getCode(),
                        statusEntity.getAntifraudStatus().getCodeinteger(),
                        new Date(), null,
                        "Добавлено по вопросу верификатора - " + questionAnswer.getQuestion().getQuestionText(),
                        questionVariantEntity);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveQA(int creditRequestId, String key, Object answer) {
        saveQA(creditRequestId, key, answer, null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveQA(int creditRequestId, String key, Object answer, String comment) {
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        if (creditRequest == null) {
            return;
        }


        //берем продукт из заявки
        Integer productId = null;
        if (creditRequest.getProductId() != null) {
            productId = creditRequest.getProductId().getId();
        }
        QuestionAnswerEntity qa = findQA(creditRequestId, key);

        if (qa == null) {
            //ищем вопрос по коду и продукту
            QuestionEntity quest = findByCode(key, productId);
            if (quest == null) {
                //пробуем найти просто по коду
                quest = findByCode(key);
                if (quest == null) {
                    return;
                }
            }

            qa = new QuestionAnswerEntity();
            qa.setQuestionId(quest);
            qa.setCreditRequestId(creditRequest);
            qa.setPeopleMainId(creditRequest.getPeopleMainId());
            qa.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_ANSWERED);
            emMicro.persist(qa);

        }

        if (answer != null) {
            int ansType = qa.getQuestionId().getAnswerType();
            if (ansType == Question.ANSWER_STRING && answer instanceof String) {
                qa.setAnswerValueString(answer.toString());
            } else if (ansType == Question.ANSWER_NUMERIC && answer instanceof Integer) {
                qa.setAnswerValueNumber(Convertor.toInteger(answer).longValue());
            } else if (ansType == Question.ANSWER_NUMERIC && answer instanceof Long) {
                qa.setAnswerValueNumber(Convertor.toLong(answer));
            } else if (ansType == Question.ANSWER_DATE && answer instanceof Date) {
                qa.setAnswerValueDate(Convertor.toDate(answer, DatesUtils.FORMAT_ddMMYYYY));
            } else if (ansType == Question.ANSWER_MONEY && answer instanceof BigDecimal) {
                qa.setAnswerValueMoney(((BigDecimal) answer).doubleValue());
            } else if (ansType == Question.ANSWER_MONEY && answer instanceof Double) {
                qa.setAnswerValueMoney(Convertor.toDouble(answer));
            } else if (answer instanceof Number && ansType == Question.ANSWER_SINGLE) {
                qa.setAnswerValueRef(Convertor.toInteger(answer));

                // тут теоретически мы можем ответить на вопрос который имеет
                // мошеннические сущности со статусами
                // поэтому ищем вариант ответа и смотрим что у него есть
                QuestionVariantEntity questionVariantEntity = getQuestionVariant(Convertor.toInteger(answer));
                if (questionVariantEntity != null) {
                    for (QuestionVariantStatusEntity statusEntity : questionVariantEntity.getStatuses()) {
                        antifraudOccasionDAO.saveWithCheck(null,
                                creditRequestId, creditRequest.getPeopleMainId().getId(),
                                statusEntity.getAntifraudEntity().getCode(),
                                statusEntity.getAntifraudStatus().getCodeinteger(),
                                new Date(), null, "Добавлено из вопросов верификатора");
                    }
                }
            }
            // TODO если ответ с мультивыбором из справочника
        }

        qa.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_ANSWERED);

        if (StringUtils.isNotEmpty(comment)) {
            qa.setComment(comment);
        }
        emMicro.merge(qa);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveQARefuse(int creditRequestId, String key) {

        CreditRequestEntity cre = crDAO.getCreditRequestEntity(creditRequestId);
        if (cre == null) {
            return;
        }

        QuestionAnswerEntity qa = null;
        Integer productId = null;

        //берем продукт из заявки
        if (cre.getProductId() != null) {
            productId = cre.getProductId().getId();
        }
        qa = findQA(creditRequestId, key);

        if (qa == null) {
            //ищем вопрос по коду и продукту
            QuestionEntity quest = findByCode(key, productId);
            if (quest == null) {
                //пробуем найти просто по коду
                quest = findByCode(key);
                if (quest == null) {
                    return;
                }
            }

            qa = new QuestionAnswerEntity();
            qa.setQuestionId(quest);
            qa.setCreditRequestId(cre);
            qa.setPeopleMainId(cre.getPeopleMainId());
            qa.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_DENIAL);
            emMicro.persist(qa);
        }
        qa.setAnswerStatus(QuestionAnswer.ANSWER_STATUS_DENIAL);
        emMicro.merge(qa);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<QuestionAnswer> addQAList(int creditRequestId, List<String> questionCodes) {
        return addQAList(creditRequestId, questionCodes, null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<QuestionAnswer> addQAList(int creditRequestId, List<String> questionCodes, Set options) {
        List<Question> questions = listQuestion(0, 0, null, null, null, null, null, null);
        List<QuestionAnswerEntity> entities = new ArrayList<>();


        for (Question q : questions) {
            if (questionCodes == null) {
                entities.add(addQA(creditRequestId, q.getQuestionCode()));
            } else {
                if (questionCodes.contains(q.getQuestionCode())) {
                    entities.add(addQA(creditRequestId, q.getQuestionCode()));
                }
            }
        }

        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();

        for (QuestionAnswerEntity ent : entities) {
            QuestionAnswer qa = new QuestionAnswer(ent);
            qa.init(options);
            questionAnswers.add(qa);
        }

        return questionAnswers;
    }

    @Override
    public Integer findQuestionVariantId(int questionId, String value, String text) {
        String sql = "from ru.simplgroupp.persistence.QuestionVariantEntity where (questionId.id = :questionId) and (answerText=:text) and (answerValue=:value)  ";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("questionId", questionId);
        qry.setParameter("text", text.trim());
        qry.setParameter("value", value.trim());
        List<QuestionVariantEntity> lst = qry.getResultList();
        if (lst.size() > 0) {
            return lst.get(0).getId();
        } else {
            return null;
        }
    }

    @Override
    public List<QuestionVariant> getQuestionVariants(int questionId) {
        String sql = "from ru.simplgroupp.persistence.QuestionVariantEntity where (questionId.id = :questionId)";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("questionId", questionId);
        List<QuestionVariantEntity> lst = qry.getResultList();

        if (lst.size() > 0) {
            ArrayList<QuestionVariant> lst1 = new ArrayList<QuestionVariant>(lst.size());

            for (QuestionVariantEntity ent : lst) {
                QuestionVariant qa = new QuestionVariant(ent);
                qa.init(null);
                lst1.add(qa);
            }
            return lst1;
        }
        return null;
    }

    @Override
    public QuestionVariantEntity getQuestionVariant(int id) {
        QuestionVariantEntity ent = emMicro.find(QuestionVariantEntity.class, new Integer(id));
        return ent;
    }

    @Override
    public String findQuestionVariantText(int id) {
        QuestionVariantEntity ent = getQuestionVariant(id);
        if (ent != null) {
            return ent.getAnswerText();
        }
        return "";
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<QuestionAnswer> listQuestionAnswer(int nFirst, int nCount, Integer creditRequestId, Integer userId,
                                                   Integer peopleMainId, int[] statuses, int[] types, Set options) {
        String sql = "select c from ru.simplgroupp.persistence.QuestionAnswerEntity as c where (1=1) ";

        if (creditRequestId != null) {
            sql = sql + " and (c.creditRequestId.id = :creditRequestId)";
        }
        if (peopleMainId != null) {
            sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
        }
        if (userId != null) {
            sql = sql + " and (c.user.id = :userId)";
        }
        if (statuses != null) {
            sql = sql + " and (c.answerStatus in (:statuses))";
        }
        if (types != null) {
            sql = sql + " and (c.questionId.type.codeinteger in (:types))";
        }

        Query qry = emMicro.createQuery(sql);

        if (creditRequestId != null) {
            qry.setParameter("creditRequestId", creditRequestId);
        }
        if (peopleMainId != null) {
            qry.setParameter("peopleMainId", peopleMainId);
        }
        if (userId != null) {
            qry.setParameter("userId", userId);
        }
        if (statuses != null) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int n : statuses) {
                list.add(n);
            }
            qry.setParameter("statuses", list);
        }
        if (types != null) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int n : types) {
                list.add(n);
            }
            qry.setParameter("types", list);
        }

        if (nFirst >= 0) {
            qry.setFirstResult(nFirst);
        }
        if (nCount > 0) {
            qry.setMaxResults(nCount);
        }

        List<QuestionAnswerEntity> entities = qry.getResultList();

        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();
        for (QuestionAnswerEntity ent : entities) {
            QuestionAnswer qa = new QuestionAnswer(ent);
            qa.init(options);
            questionAnswers.add(qa);
        }

        return questionAnswers;
    }
}
