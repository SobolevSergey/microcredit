package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.IdentityQuestionDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.IdentityQuestionLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class IdentityQuestionBean implements IdentityQuestionLocal {
	@Inject Logger logger;

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    private IdentityQuestionDAO identityQuestionDAO;

    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private CreditInfoService creditInfoService;

    @EJB
    private ReferenceBooksLocal referenceBooks;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private EventLogService eventLog;


    @Override
    @SuppressWarnings("unchecked")
    public List<IdentityTemplateEntity> findTemplates(Boolean fake, Boolean active) {
        String hql = "select t from ru.simplgroupp.persistence.IdentityTemplateEntity t where (1=1) ";

        if (fake != null) {
            hql += "and (t.fake = :fake) ";
        }
        if (active != null) {
            hql += "and (t.active = :active) ";
        }

        Query query = emMicro.createQuery(hql);

        if (fake != null) {
            query.setParameter("fake", fake);
        }
        if (active != null) {
            query.setParameter("active", active);
        }


        return query.getResultList();
    }

    @Override
    public List<IdentityQuestion> findQuestions(Integer creditRequestId, Integer creditId, Integer peopleMainId, Set options) {
        return identityQuestionDAO.findQuestions(creditRequestId, creditId, peopleMainId, options);
    }

    @Override
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<CreditEntity> getCreditEntitiesFromKb(CreditRequestEntity creditRequest) {
        Query query = emMicro.createNamedQuery("findCreditsFromKbByCreditRequest");
        query.setParameter("creditRequestId", creditRequest.getId());
        return query.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean removeQuestionsByCreditRequestId(int creditRequestId) {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
        Query query = emMicro.createNamedQuery("deleteAllQuestionsByCreditRequest");
        query.setParameter("creditRequestId", creditRequest.getId());
        return query.executeUpdate() > 0;
    }

    public IdentityTemplateEntity getTemplate(int id) {
        return identityQuestionDAO.getTemplate(id);
    }

    @Override
    public IdentityOptionEntity getOption(int id) {
        return identityQuestionDAO.getOption(id);
    }

    @Override
    public boolean hasQuestions(int peopleMainId) {
        List<IdentityQuestion> questions = findQuestions(null, null, peopleMainId, null);
        return questions.size() > 0;
    }

    @Override
    public List<IdentityQuestionEntity> getAndSaveQuestions(int creditRequestId,
                                                            int questionsAmount, int answersAmount, boolean useFake) {

        PeopleMainEntity peopleMainEntity = creditRequestDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
        if (hasQuestions(peopleMainEntity.getId())) {
            logger.severe("вопросы идентификации уже были заданы");
            return null;
        }

        logger.info("генерация вопросов идентификации: [кредитный запрос = " + creditRequestId + "; количество вопросов = " + questionsAmount +
                "; вариантов ответов = " + answersAmount + "; использовать фейковые = " + useFake + "]");

        List<IdentityQuestionEntity> questions = new ArrayList<>();
        for (IdentityQuestionEntity question : getQuestions(creditRequestId, questionsAmount, answersAmount, useFake)) {
            logger.info("question = " + question);
            questions.add(saveQuestion(question));
        }

        logger.info("вопросы идентификации сгенерированы, " + questions.size() + " вопросов");

        return questions;
    }

    @Override
    public List<IdentityQuestionEntity> getQuestions(int creditRequestId,
                                                     int questionsAmount, int answersAmount, boolean useFake) {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<CreditEntity> credits = getCreditEntitiesFromKb(creditRequest);


        List<IdentityQuestionEntity> allQuestions = new ArrayList<>();

        List<IdentityQuestionEntity> realQuestions = generateRealQuestions(credits, questionsAmount, answersAmount);

        logger.info("реальных вопросов идентификации сгенерированно = " + realQuestions.size());
        allQuestions.addAll(realQuestions);

        if (useFake && allQuestions.size() < questionsAmount) {
            List<IdentityQuestionEntity> fakeQuestions = generateFakeQuestions(creditRequest.getPeopleMainId(),
                    creditRequest, questionsAmount - allQuestions.size(), answersAmount);

            logger.info("фейковых вопросов идентификации сгенерированно = " + fakeQuestions.size());
            allQuestions.addAll(fakeQuestions);
        }

        return allQuestions;
    }

    @Override
    public List<IdentityQuestionEntity> generateRealQuestions(List<CreditEntity> credits,
                                                              int questionsAmount, int answersAmount) {
        List<IdentityTemplateEntity> templates = findTemplates(false, true);
        List<IdentityQuestionEntity> questions = new ArrayList<>();

        Collections.shuffle(templates);
        Collections.shuffle(credits);


        if (questionsAmount > credits.size()) {
            questionsAmount = credits.size();
        }

        if (questionsAmount > templates.size()) {
            questionsAmount = templates.size();
        }


        for (int i = 0, j = 0; i < questionsAmount; i++, j++) {
            try {
                CreditEntity credit = credits.get(i);
                IdentityTemplateEntity template = templates.get(i);

                IdentityQuestionEntity question = new IdentityQuestionEntity();
                question.setTemplate(template);
                question.setPeopleMain(credit.getPeopleMainId());
                question.setCreditRequest(credit.getCreditRequestId());

                if (template.getAnswerType().equals("B")) {
                    question = buildBooleanQuestion(question, credits, template);
                } else {
                    question.setCredit(credit);
                    question = buildQuestion(question, credit, templates.get(i));
                    question.setOptions(buildOptions(question, answersAmount));
                }

                questions.add(question);
            } catch (Exception e) {
                logger.severe("ошибка генерации вопроса для идентификации " + e);
            }

        }

        return questions;
    }

    @Override
    public List<IdentityQuestionEntity> generateFakeQuestions(PeopleMainEntity peopleMain,
                                                              CreditRequestEntity creditRequest,
                                                              int questionsAmount, int answersAmount) {
        List<IdentityTemplateEntity> templates = findTemplates(true, true);
        List<IdentityQuestionEntity> questions = new ArrayList<>();

        Collections.shuffle(templates);


        // если мы хотим 10 вопросов, а шаблонов 5, то значит будет 5 вопросов
        if (questionsAmount > templates.size()) {
            questionsAmount = templates.size();
        }


        for (int i = 0; i < questionsAmount; i++) {
            // 5 раз мы можем попытаться сгенерировать фейковый вопрос
            // если после 5 раз вопрос не сгенерировался то уж извините
            for (int j = 0; j < 5; j++) {
                try {
                    IdentityQuestionEntity question = buildFakeQuestion(templates.get(i));
                    question.setPeopleMain(peopleMain);
                    question.setCreditRequest(creditRequest);
                    question.setOptions(buildFakeOptions(question, answersAmount));
                    questions.add(question);
                    break;
                } catch (Exception e) {
                    logger.info("ошибка генерации вопроса для идентификации " + e);
                }
            }
        }

        return questions;
    }

    @Override
    public IdentityQuestionEntity buildQuestion(IdentityQuestionEntity question,
                                                CreditEntity creditEntity,
                                                IdentityTemplateEntity template) throws KassaException {
        question.setValue(template.getValue());

        if (template.getForCreditType() != null && template.getForCreditType() != creditEntity.getCredittypeId()) {
            throw new KassaException("вопрос не может быть сгенерирован для данного кредита, тип кредита = " + creditEntity.getCredittypeId() +
                    "; тип вопроса = " + template.getForCreditType() + "; creditId = " + creditEntity.getId());
        }

        Pattern p = Pattern.compile("\\{(\\S*)\\}");
        Matcher m = p.matcher(template.getValue());

        while (m.find()) {
            if (m.groupCount() > 0) {
                String paramFullName = m.group(1);
                String[] paramData = paramFullName.split("#");

                String paramName = paramData[0];
                String paramType = paramData[1];

                Object value = getValueByReflectApi(creditEntity, paramName);
                String paramValue;
                if (value != null) {
                    switch (paramType) {
                        case "T":
                            paramValue = value.toString();
                            break;
                        case "D":
                            Date date = Convertor.toDate(value, null);
                            paramValue = Convertor.dateToString(date, "dd/MM/yyyy");
                            break;
                        case "M":
                            paramValue = value.toString();
                            break;
                        default:
                            throw new KassaException("неизвестный тип для подстановки значения в идентификационный вопрос");
                    }
                } else {
                    throw new KassaException("значение параметра " + paramName + " равно null, вопрос заполнен неполностью; " +
                            "creditId = " + creditEntity.getId());
                }

                String questionText = question.getValue().replaceAll("\\{" + paramFullName + "\\}", paramValue);
                question.setValue(questionText);
            }
        }

        return question;
    }

    @Override
    public IdentityQuestionEntity buildFakeQuestion(IdentityTemplateEntity template) throws KassaException {
        if (!template.getFake()) {
            throw new KassaException("шаблон не для фейкового вопроса");
        }

        IdentityQuestionEntity question = new IdentityQuestionEntity();
        question.setTemplate(template);
        question.setValue(template.getValue());

        return question;
    }

    @Override
    public IdentityQuestionEntity buildBooleanQuestion(IdentityQuestionEntity question,
                                                       List<CreditEntity> credits,
                                                       IdentityTemplateEntity template) throws KassaException {
        question.setValue(template.getValue());


        IdentityOptionEntity yesOption = new IdentityOptionEntity();
        yesOption.setValue("Да");
        yesOption.setQuestion(question);

        IdentityOptionEntity noOption = new IdentityOptionEntity();
        noOption.setValue("Нет");
        noOption.setQuestion(question);

        List<IdentityOptionEntity> options = new ArrayList<>();
        options.add(yesOption);
        options.add(noOption);
        question.setOptions(options);


        String fieldName = template.getAnswerField();


        Pattern p = Pattern.compile("\\{(\\S*)\\}");
        Matcher m = p.matcher(template.getValue());

        // в вопросе обычно используется одна подмена (если вообще используется), поэтому if достаточно (не while)
        // и это тип, а подобный вопрос выводится когда несколько типов кредитов
        if (m.find()) {
            // ищем несколько типов кредитов
            Set<ReferenceEntity> creditTypes = new HashSet<>();
            for (CreditEntity credit : credits) {
                creditTypes.add(credit.getCredittypeId());
            }


            if (creditTypes.size() == 1) {
                throw new KassaException("вопрос только для списка с несколькими типами кредитов");
            }

            if (m.groupCount() > 0) {
                String paramFullName = m.group(1);
                String[] paramData = paramFullName.split("#");

                String paramType = paramData[1];


                if (!paramType.equals("T")) {
                    throw new KassaException("пераметр не тип, генерация невозможна");
                }

                List<Reference> references = referenceBooks.listReference(template.getOptionType().getId());
                Collections.shuffle(references);

                String paramValue = null;
                for (Reference reference : references) {
                    if (reference.getCodeInteger() != 0) {
                        paramValue = reference.getEntity().toString();
                    }
                }

                if (paramValue == null) {
                    throw new KassaException("подходящий тип для подстановки в вопрос не найден");
                }

                String questionText = question.getValue().replaceAll("\\{" + paramFullName + "\\}", paramValue);
                question.setValue(questionText);


                Boolean yesCorrect = false;
                for (CreditEntity credit : credits) {
                    Object entityValue = getValueByReflectApi(credit, fieldName);
                    if (entityValue.toString().equals(paramValue)) {
                        yesCorrect = true;
                        break;
                    }
                }

                yesOption.setCorrect(yesCorrect);
                noOption.setCorrect(!yesCorrect);
            } else {
                throw new KassaException("ошибка в парсинге строки вопроса, нет элементов в группе");
            }
        } else {
            Boolean yesCorrect = false;
            for (CreditEntity credit : credits) {
                Object entityValue = getValueByReflectApi(credit, fieldName);
                if (entityValue != null) {
                    yesCorrect = true;
                    break;
                }
            }

            yesOption.setCorrect(yesCorrect);
            noOption.setCorrect(!yesCorrect);
        }

        return question;
    }

    @Override
    public List<IdentityOptionEntity> buildOptions(IdentityQuestionEntity question, int amount) throws KassaException {
        List<Reference> references = null;
        Set<String> optionValueSet = new HashSet<>();

        String fieldName = question.getTemplate().getAnswerField();
        String fieldType = question.getTemplate().getAnswerType();
        Object correctEntityValue = getValueByReflectApi(question.getCredit(), fieldName);

        if (correctEntityValue == null) {
            throw new KassaException("значение параметра " + fieldName + " равно null, варианты ответов не могут быть сгенерированны; " +
                    "creditId = " + question.getCredit().getId());
        }

        // верный ответ
        String correctOptionValue;
        switch (fieldType) {
            case "D":
                Date date = Convertor.toDate(correctEntityValue, null);
                correctOptionValue = Convertor.dateToString(date, "dd/MM/yyyy");
                break;
            case "M":
                Double number = Convertor.toDouble(correctEntityValue);
                correctOptionValue = number.toString();
                break;
            case "T":
                correctOptionValue = correctEntityValue.toString();
                break;
            default:
                throw new KassaException("неизвестный тип для ответа идентификационного вопроса");
        }
        optionValueSet.add(correctOptionValue);


        for (int i = 2; i < amount; i++) {
            String optionValue;
            switch (fieldType) {
                case "D":
                    // варианты ответа все должны иметь одно число, но разные месяцы и года
                    Date date = Convertor.toDate(correctEntityValue, null);

                    Calendar oldC = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    oldC.setTime(date);
                    while (true) {
                        c.setTime(date);
                        c.add(Calendar.MONTH, randInt(-6, 6));
                        c.add(Calendar.YEAR, randInt(-2, 2));

                        if (oldC.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) {
                            continue;
                        }

                        optionValue = Convertor.dateToString(c.getTime(), "dd/MM/yyyy");
                        if (!optionValueSet.contains(optionValue)) {
                            optionValueSet.add(optionValue);
                            break;
                        }
                    }
                    break;
                case "M":
                    Double number = Convertor.toDouble(correctEntityValue);
                    while (true) {
                        number = number + (randInt(-30, 30) * 100);
                        if (number <= 0) {
                            number = 100D;
                        }
                        optionValue = number.toString();

                        if (!optionValueSet.contains(optionValue)) {
                            optionValueSet.add(optionValue);
                            break;
                        }
                    }
                    break;
                case "T":
                    // если тип то я предполагаю единственный класс для типов это ReferenceEntity
                    if (references == null) {
                        references = referenceBooks.listReference(question.getTemplate().getOptionType().getId());
                    }

                    while (true) {
                        Collections.shuffle(references);
                        ReferenceEntity referenceEntity = references.get(0).getEntity();
                        if (referenceEntity.getCodeinteger() != 0 && referenceEntity.getCodeinteger() != 99) {
                            optionValue = referenceEntity.toString();
                            if (!optionValueSet.contains(optionValue)) {
                                optionValueSet.add(optionValue);
                                break;
                            }
                        }
                    }
                    break;
                default:
                    throw new KassaException("неизвестный тип для варианта ответа идентификационного вопроса");
            }
        }


        List<IdentityOptionEntity> options = new ArrayList<>();
        for (String etcOption : optionValueSet) {
            IdentityOptionEntity option = new IdentityOptionEntity();

            if (etcOption.equals(correctOptionValue)) {
                option.setCorrect(true);
            }

            option.setValue(etcOption);
            option.setQuestion(question);
            options.add(option);
        }

        Collections.shuffle(options, new Random(System.nanoTime()));


        IdentityOptionEntity option = new IdentityOptionEntity();
        option.setValue("ни один из выше перечисленных");
        option.setQuestion(question);
        options.add(option);


        return options;
    }

    @Override
    public List<IdentityOptionEntity> buildFakeOptions(IdentityQuestionEntity question, int amount) throws KassaException {
        List<Reference> references = null;
        Set<String> optionValueSet = new HashSet<>();

        String fieldType = question.getTemplate().getAnswerType();


        for (int i = 1; i < amount; i++) {
            String optionValue;
            switch (fieldType) {
                case "D":
                    // варианты ответа все должны иметь одно число, но разные месяцы и года
                    Calendar c = Calendar.getInstance();
                    if (optionValueSet.isEmpty()) {
                        c.add(Calendar.DAY_OF_MONTH, randInt(-15, 15));
                        optionValue = Convertor.dateToString(c.getTime(), "dd/MM/yyyy");
                        optionValueSet.add(optionValue);
                    } else {
                        Date mainDate = Convertor.toDate(optionValueSet.iterator().next(), "dd/MM/yyyy");
                        Calendar oldC = Calendar.getInstance();
                        oldC.setTime(mainDate);
                        while (true) {
                            c.setTime(mainDate);
                            // генерируем дату где изменяем случайно месяц и год
                            c.add(Calendar.MONTH, randInt(-6, 6));
                            c.add(Calendar.YEAR, randInt(-2, 2));

                            if (oldC.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) {
                                continue;
                            }

                            optionValue = Convertor.dateToString(c.getTime(), "dd/MM/yyyy");
                            if (!optionValueSet.contains(optionValue)) {
                                optionValueSet.add(optionValue);
                                break;
                            }
                        }
                    }
                    break;
                case "M":
                    Double number = 0D;
                    while (true) {
                        number = number + (randInt(10, 50) * 100);
                        if (number <= 0) {
                            number = 100D;
                        }
                        optionValue = number.toString();

                        if (!optionValueSet.contains(optionValue)) {
                            optionValueSet.add(optionValue);
                            break;
                        }
                    }
                    break;
                case "T":
                    // если тип то я предполагаю единственный класс для типов это ReferenceEntity
                    if (references == null) {
                        references = referenceBooks.listReference(question.getTemplate().getOptionType().getId());
                    }

                    while (true) {
                        Collections.shuffle(references);
                        optionValue = references.get(0).getEntity().toString();
                        if (!optionValueSet.contains(optionValue)) {
                            optionValueSet.add(optionValue);
                            break;
                        }
                    }
                    break;
                default:
                    throw new KassaException("неизвестный тип для варианта ответа идентификационного вопроса");
            }
        }


        List<IdentityOptionEntity> options = new ArrayList<>();
        for (String etcOption : optionValueSet) {
            IdentityOptionEntity option = new IdentityOptionEntity();
            option.setValue(etcOption);
            option.setQuestion(question);
            options.add(option);
        }

        Collections.shuffle(options, new Random(System.nanoTime()));


        IdentityOptionEntity option = new IdentityOptionEntity();
        option.setValue("ни один из выше перечисленных");
        option.setCorrect(true);
        option.setQuestion(question);
        options.add(option);


        return options;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean removeTemplate(int id) {
        IdentityTemplateEntity template = emMicro.find(IdentityTemplateEntity.class, id);
        if (template != null) {
            emMicro.remove(template);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public IdentityTemplateEntity saveTemplate(IdentityTemplateEntity template) {
        return emMicro.merge(template);
    }

    @Override
    public IdentityQuestionEntity saveQuestion(IdentityQuestionEntity question) {
//        question.setTemplate(getTemplateById(question.getTemplate().getId()));
//
//        return emMicro.merge(question);
        return identityQuestionDAO.saveQuestion(question);
    }

    @Override
    public IdentityAnswerEntity saveAnswerAndCalculate(int optionId) {
        IdentityAnswerEntity answer = saveAnswer(optionId);

        IdentityOptionEntity option = getOption(optionId);
        int points = calculatePoints(option.getQuestion().getCreditRequest().getId());
        savePoints(points, option.getQuestion().getCreditRequest().getId());

        return answer;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public IdentityAnswerEntity saveAnswer(int optionId) {
        IdentityOptionEntity option = getOption(optionId);

        if (getAnswerByQuestion(option.getQuestion().getId()) != null) {
            logger.info("ответ на вопрос с id = " + option.getQuestion().getId() + " уже получен, повторный ответ невозможен");
            return null;
        }

        return identityQuestionDAO.saveAnswer(optionId);
    }

    @Override
    public IdentityAnswerEntity getAnswerByQuestion(int id) {
        IdentityQuestion question = identityQuestionDAO.getQuestion(id, Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));
        for (IdentityOption option : question.getOptions()) {
            IdentityAnswerEntity answer = option.getAnswer();
            if (answer != null) {
                return answer;
            }
        }

        return null;
    }

    @Override
    public boolean isComplete(Integer creditRequestId, Integer creditId, Integer peopleMainId) {
        boolean complete = true;
        for (IdentityQuestion question : findQuestions(creditRequestId, creditId, peopleMainId, null)) {
            if (getAnswerByQuestion(question.getId()) == null) {
                complete = false;
                break;
            }
        }

        return complete;
    }

    @Override
    public boolean isCompleteForPeople(int creditRequestId) {
        Integer peopleMainId = creditRequestDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId().getId();
        return isComplete(null, null, peopleMainId);
    }

    @Override
    public Boolean isCorrect(int questionId) {
        IdentityQuestion question = identityQuestionDAO.getQuestion(questionId, Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));
        question.getOptions().size();

        IdentityOptionEntity optionAns = null;
        for (IdentityOption option : question.getOptions()) {
            if (option.getAnswer() != null) {
                optionAns = option.getEntity();
                break;
            }
        }

        if (optionAns != null) {
            return optionAns.getCorrect();
        } else {
            return null;
        }
    }

    @Override
    public int calculatePoints(int creditRequestId) {
        List<IdentityQuestion> questions = findQuestions(creditRequestId, null, null, Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));

        int points = 0;
        for (IdentityQuestion question : questions) {
            if (question.getCorrect() != null && question.getCorrect()) {
                points += question.getTemplate().getPoints();
            }
        }

        return points;
    }

    @Override
    public boolean savePoints(int points, int creditRequestId) {
        if (!isComplete(creditRequestId, null, null)) {
            logger.severe("не на все вопросы идентификации даны ответы, сохранение балла верификации невозможно");
            return false;
        }

        CreditRequestEntity creditRequestEntity = creditRequestDAO.getCreditRequestEntity(creditRequestId);
        PeopleMainEntity peopleMainEntity = creditRequestEntity.getPeopleMainId();
        PartnersEntity partnersEntity = referenceBooks.getPartnerEntity(Partner.SYSTEM);

        creditInfoService.saveVerification(creditRequestEntity, peopleMainEntity, partnersEntity, (double) points, 0d, 0, 0, 0, 0, 0);
        logger.info("сохранение балла по идентификационным вопросам прошло успешно");

        try {
            eventLog.saveLog(EventType.INFO, EventCode.SAVE_IDENTIFICATION_POINTS, "Сохранение балла идентификации",
                    null, new Date(), creditRequestEntity, peopleMainEntity, null);
        } catch (Exception e) {
            logger.severe("Не удалось записать лог по сохранении баллов идентификации " + creditRequestId);
        }

        return true;
    }

    @Override
    public Object getValueByReflectApi(Object object, String field) throws KassaException {
        String[] params = field.split("\\.");
        try {
            for (String param : params) {
                object = new PropertyDescriptor(param, object.getClass()).getReadMethod().invoke(object);
            }

            return object;
        } catch (Exception e) {
            throw new KassaException("невозможно получить данные объекта, поле = " + field + "; объект = " + object);
        }
    }

    @Override
    public Integer countRightAnswers(Integer creditRequestId, Integer creditId, Integer peopleMainId) {
        List<IdentityQuestion> questions = findQuestions(creditRequestId, creditId, peopleMainId,
                Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));

        if (questions.size() == 0) {
            return null;
        }

        int rightAnswers = 0;
        for (IdentityQuestion question : questions) {
            if (question.getCorrect()) {
                rightAnswers++;
            }
        }

        return rightAnswers;
    }

    @Override
    public int countQuestions(Integer creditRequestId, Integer creditId, Integer peopleMainId) {
        return findQuestions(creditRequestId, creditId, peopleMainId, null).size();
    }

    @Override
    public int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
