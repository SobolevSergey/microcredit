package ru.simplgroupp.action;

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
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.IdentityQuestion;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;


@LocalClient
public class IdentityQuestionDAOTest {
    private static final Logger logger = LoggerFactory.getLogger(IdentityQuestionDAOTest.class);


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

        Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
    }


    @Test
    public void testGetTransfer() {
        Integer id = 39244;

        IdentityQuestion question = identityQuestionDAO.getQuestion(id, Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));
        collector.checkThat(question.getOptions().size(), CoreMatchers.not(0));
    }

    @Test
    public void testFindByCreditRequest() {
        Integer id = 39240;

        List<IdentityQuestion> questions = identityQuestion.findQuestions(id, null, null, Utils.setOf(IdentityQuestion.Options.INIT_IDENTITY_OPTIONS));
        for (IdentityQuestion question : questions) {
            collector.checkThat(question.getOptions().size(), CoreMatchers.not(0));
        }
    }

}
