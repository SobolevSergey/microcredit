package ru.simplgroupp.action;

import junit.framework.Assert;
import org.apache.openejb.api.LocalClient;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.IdentityQuestionDAO;
import ru.simplgroupp.interfaces.IdentityQuestionLocal;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.IdentityOption;
import ru.simplgroupp.transfer.IdentityQuestion;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;


@LocalClient
public class IdentityQuestionTest {
    private static final Logger logger = LoggerFactory.getLogger(IdentityQuestionTest.class);


    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private IdentityQuestionLocal identityQuestion;

    @EJB
    private IdentityQuestionDAO identityQuestionDAO;

    private Integer creditRequestId = 30;


    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
        System.setProperty("openejb.validation.output.level","VERBOSE");

        Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
    }

    //@Test
    public void testFindQuestions() {
        List<IdentityTemplateEntity> all = identityQuestion.findTemplates(null, null);
        List<IdentityTemplateEntity> fake = identityQuestion.findTemplates(true, null);
        List<IdentityTemplateEntity> nonFake = identityQuestion.findTemplates(false, null);
        List<IdentityTemplateEntity> active = identityQuestion.findTemplates(null, true);
        List<IdentityTemplateEntity> nonActive = identityQuestion.findTemplates(null, false);

        collector.checkThat(all.size(), CoreMatchers.equalTo(fake.size() + nonFake.size()));
        collector.checkThat(all.size(), CoreMatchers.equalTo(active.size() + nonActive.size()));

        for (IdentityTemplateEntity template : fake) {
            collector.checkThat(template.getFake(), CoreMatchers.is(true));
        }
        for (IdentityTemplateEntity template : nonFake) {
            collector.checkThat(template.getFake(), CoreMatchers.is(false));
        }
        for (IdentityTemplateEntity template : active) {
            collector.checkThat(template.getActive(), CoreMatchers.is(true));
        }
        for (IdentityTemplateEntity template : nonActive) {
            collector.checkThat(template.getActive(), CoreMatchers.is(false));
        }

        System.out.println("all = " + all.size());
    }

    //@Test
    public void testQuestionGeneration() {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<CreditEntity> creditEntityList = identityQuestion.getCreditEntitiesFromKb(creditRequest);
        List<IdentityQuestionEntity> questions = identityQuestion.generateRealQuestions(creditEntityList, 5, 10);

        Integer questionSize = questions.size();
        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsDb = identityQuestion.findQuestions(creditRequestId, null, null, null);
        System.out.println("questionsDb = " + questionsDb.size());

        collector.checkThat(questionSize, CoreMatchers.equalTo(questionsDb.size()));


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testFakeQuestion() {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<IdentityQuestionEntity> questions = identityQuestion.generateFakeQuestions(creditRequest.getPeopleMainId(), creditRequest, 5, 10);

        Integer questionSize = questions.size();
        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsDb = identityQuestion.findQuestions(creditRequestId, null, null, null);
        System.out.println("questionsDb = " + questionsDb.size());

        collector.checkThat(questionSize, CoreMatchers.equalTo(questionsDb.size()));

        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testGetQuestion() {
        Integer mustQuestions = 5;

        List<IdentityQuestionEntity> questions = identityQuestion.getQuestions(creditRequestId, mustQuestions, 10, true);

        Integer questionSize = questions.size();
        collector.checkThat(questionSize, CoreMatchers.equalTo(mustQuestions));


        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsDb = identityQuestion.findQuestions(creditRequestId, null, null, null);
        System.out.println("questionsDb = " + questionsDb.size());

        collector.checkThat(questionSize, CoreMatchers.equalTo(questionsDb.size()));


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testAnswer() {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<IdentityQuestionEntity> questions = identityQuestion.generateFakeQuestions(creditRequest.getPeopleMainId(), creditRequest, 5, 10);


        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsFound = identityQuestion.findQuestions(
                creditRequestId, null, null, Utils.setOf(
                        IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));

        IdentityOption correctOption = null;
        for (IdentityOption option : questionsFound.get(0).getOptions()) {
            if (option.getCorrect()) {
                correctOption = option;
                identityQuestion.saveAnswerAndCalculate(correctOption.getId());
                break;
            }
        }


        IdentityOption nonCorrectOption = null;
        for (IdentityOption option : questionsFound.get(1).getOptions()) {
            if (!option.getCorrect()) {
                nonCorrectOption = option;
                identityQuestion.saveAnswerAndCalculate(nonCorrectOption.getId());
                break;
            }
        }


        if (correctOption != null) {
            collector.checkThat(identityQuestion.isCorrect(correctOption.getQuestion().getId()), CoreMatchers.is(true));
        }

        if (nonCorrectOption != null) {
            collector.checkThat(identityQuestion.isCorrect(nonCorrectOption.getQuestion().getId()), CoreMatchers.is(false));
        }


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testDoubleSaveAnswer() {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<IdentityQuestionEntity> questions = identityQuestion.generateFakeQuestions(creditRequest.getPeopleMainId(), creditRequest, 5, 10);


        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsFound = identityQuestion.findQuestions(creditRequestId, null, null,
                Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));


        IdentityOption optionWithAnswer = null;
        outerLoop:
        for (IdentityQuestion question : questionsFound) {
            for (IdentityOption option : question.getOptions()) {
                if (option.getCorrect()) {
                    optionWithAnswer = option;
                    identityQuestion.saveAnswerAndCalculate(optionWithAnswer.getId());
                    break outerLoop;
                }
            }
        }


        if (optionWithAnswer != null) {
            IdentityAnswerEntity answer = identityQuestion.saveAnswerAndCalculate(optionWithAnswer.getId());
            collector.checkThat(answer, CoreMatchers.nullValue());
        }


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

//    @Test
    public void testAllQuestions() {
        Integer answersAmount = 10;

        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<CreditEntity> creditEntityList = identityQuestion.getCreditEntitiesFromKb(creditRequest);
        List<IdentityTemplateEntity> nonFakeTemplates = identityQuestion.findTemplates(false, null);
        List<IdentityTemplateEntity> fakeTemplates = identityQuestion.findTemplates(true, null);


        for (CreditEntity credit : creditEntityList) {
            System.out.println("credit = " + credit);
            for (IdentityTemplateEntity template : nonFakeTemplates) {
                try {
                    IdentityQuestionEntity question = new IdentityQuestionEntity();
                    question.setTemplate(template);
                    question.setPeopleMain(credit.getPeopleMainId());
                    question.setCreditRequest(credit.getCreditRequestId());


                    if (template.getAnswerType().equals("B")) {
                        question = identityQuestion.buildBooleanQuestion(question, creditEntityList, template);
                    } else {
                        question.setCredit(credit);
                        question = identityQuestion.buildQuestion(question, credit, template);
                        question.setOptions(identityQuestion.buildOptions(question, answersAmount));
                    }

                    System.out.print("question = " + question + " = [");
                    for (IdentityOptionEntity option : question.getOptions()) {
                        System.out.print(option.getValue() + "; ");
                    }
                    System.out.println("]");
                } catch (Exception e) {
                    logger.error("ошибка генерации вопроса для идентификации " + e);
                }
            }
            System.out.println();
        }


        for (IdentityTemplateEntity template : fakeTemplates) {
            try {
                IdentityQuestionEntity question = new IdentityQuestionEntity();
                question.setTemplate(template);


                question = identityQuestion.buildFakeQuestion(template);
                question.setOptions(identityQuestion.buildFakeOptions(question, answersAmount));

                System.out.print("question = " + question + " = [");
                for (IdentityOptionEntity option : question.getOptions()) {
                    System.out.print(option.getValue() + "; ");
                }
                System.out.println("]");
            } catch (Exception e) {
                logger.error("ошибка генерации вопроса для идентификации " + e);
            }
        }
    }

    //@Test
    public void testGetAnswerByQuestion() {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);

        List<IdentityQuestionEntity> questions = identityQuestion.generateFakeQuestions(creditRequest.getPeopleMainId(), creditRequest, 5, 10);


        for (IdentityQuestionEntity question : questions) {
            System.out.println("question = " + question);
            identityQuestion.saveQuestion(question);
        }

        List<IdentityQuestion> questionsFound = identityQuestion.findQuestions(creditRequestId, null, null,
                Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));
        Integer questionId = questionsFound.get(0).getId();

        IdentityAnswerEntity answer0 = identityQuestion.saveAnswerAndCalculate(questionsFound.get(0).getOptions().get(0).getId());
        IdentityAnswerEntity answer1 = identityQuestion.getAnswerByQuestion(questionId);

        collector.checkThat(answer0, CoreMatchers.equalTo(answer1));


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    @Test
    public void testGetAndSaveQuestion() {
        Integer mustQuestions = 5;

        List<IdentityQuestionEntity> questions = identityQuestion.getAndSaveQuestions(creditRequestId, mustQuestions, 10, true);

        Integer questionSize = questions.size();
        collector.checkThat(questionSize, CoreMatchers.equalTo(mustQuestions));

        List<IdentityQuestion> questionsDb = identityQuestion.findQuestions(creditRequestId, null, null,
                Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));

        for (IdentityQuestion question : questionsDb) {
            for (IdentityOption option : question.getOptions()) {
                System.out.println(option.getId() + " = " + option.getValue());
            }
        }

        collector.checkThat(questionSize, CoreMatchers.equalTo(questionsDb.size()));


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testHasQuestions() {
        PeopleMainEntity peopleMainEntity = creditRequestDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();

        collector.checkThat(identityQuestion.hasQuestions(peopleMainEntity.getId()), CoreMatchers.is(false));
        identityQuestion.getAndSaveQuestions(creditRequestId, 5, 10, true);
        collector.checkThat(identityQuestion.hasQuestions(peopleMainEntity.getId()), CoreMatchers.is(true));


        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    //@Test
    public void testSavePoints() {
        identityQuestion.getAndSaveQuestions(creditRequestId, 5, 5, true);

        List<IdentityQuestion> questionsFound = identityQuestion.findQuestions(
                creditRequestId, null, null, Utils.setOf(
                        IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));


        collector.checkThat(identityQuestion.calculatePoints(creditRequestId), CoreMatchers.equalTo(0));

        int points = 0;
        for (IdentityQuestion question : questionsFound) {
            points += question.getTemplate().getPoints();
            for (IdentityOption option : question.getOptions()) {
                if (option.getCorrect()) {
                    identityQuestion.saveAnswer(option.getId());
                }
            }
        }

        collector.checkThat(identityQuestion.calculatePoints(creditRequestId), CoreMatchers.equalTo(points));

        collector.checkThat(identityQuestion.savePoints(points, creditRequestId), CoreMatchers.equalTo(true));

        Boolean isRemoved = identityQuestion.removeQuestionsByCreditRequestId(creditRequestId);
        collector.checkThat(isRemoved, CoreMatchers.is(true));
    }

    // @Test
    public void testSaveTemplate() {
        IdentityTemplateEntity template = identityQuestion.getTemplate(23);

        // Выберите сумму полученного кредита на обучение, выданного Вам 09/08/2008.
        String oldValue = template.getValue();
        String newValue = "test";
        template.setValue(newValue);

        IdentityTemplateEntity templateDb = identityQuestion.saveTemplate(template);
        collector.checkThat(templateDb.getId(), CoreMatchers.equalTo(template.getId()));
        collector.checkThat(templateDb.getValue(), CoreMatchers.equalTo(newValue));

        templateDb.setValue(oldValue);
        templateDb = identityQuestion.saveTemplate(templateDb);
        collector.checkThat(templateDb.getId(), CoreMatchers.equalTo(template.getId()));
        collector.checkThat(templateDb.getValue(), CoreMatchers.equalTo(oldValue));
    }

//    @Test
    public void testCounter() {
        Integer mustQuestions = 5;

        identityQuestion.getAndSaveQuestions(creditRequestId, mustQuestions, 10, true);
        collector.checkThat(identityQuestion.countQuestions(creditRequestId, null, null), CoreMatchers.equalTo(mustQuestions));

        List<IdentityQuestion> questionsFound = identityQuestion.findQuestions(
                creditRequestId, null, null, Utils.setOf(
                        IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));

        for (IdentityOption option : questionsFound.get(0).getOptions()) {
            if (option.getCorrect()) {
                identityQuestion.saveAnswerAndCalculate(option.getId());
                break;
            }
        }

        collector.checkThat(identityQuestion.countRightAnswers(creditRequestId, null, null), CoreMatchers.equalTo(1));
    }

//    @Test
    public void writeQuestions() {
        Integer mustQuestions = 5;
        Integer creditReqId = 31404;

        identityQuestion.getAndSaveQuestions(creditReqId, mustQuestions, 5, true);
    }

//    @Test
    public void isCompleted() {
        boolean isOk = identityQuestion.isComplete(8, null, null);
        Assert.assertEquals(isOk, true);
    }
}
