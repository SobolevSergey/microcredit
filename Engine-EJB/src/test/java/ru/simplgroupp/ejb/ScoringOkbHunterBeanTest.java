package ru.simplgroupp.ejb;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.hunter.hashing.SCL;
import ru.simplgroupp.hunter.onlinematching.wsdl.Match;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringOkbHunterBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.workflow.PluginExecutionContext;

@LocalClient
public class ScoringOkbHunterBeanTest {

    private PluginExecutionContext pluginExecutionContext;

    private ScoringOkbHunterBeanLocal scoringOkbHunterBean;

    private CreditRequestDAO creditRequestDAO;

    private PeopleDAO peopleDAO;

    private ReferenceBooksLocal referenceBooks;

    @Before
    public void setUp() throws Exception {
        //System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        WorkflowEngineBeanLocal workflowEngineBean = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        ActionProcessorBeanLocal actionProcessorBean = workflowEngineBean.getActionProcessor();

        ActionContext actionContext = actionProcessorBean.createActionContext(null, true);
        pluginExecutionContext = new PluginExecutionContext(actionContext.getPlugins()
                .getPluginConfig(ScoringOkbHunterBean.SYSTEM_NAME), null, 0, Utils.<String, Object>mapOf(), actionContext.getPluginState(
                ScoringOkbHunterBean.SYSTEM_NAME));
        scoringOkbHunterBean = actionContext.getPlugins().getOkbHunter();

        creditRequestDAO = (CreditRequestDAO) context.lookup("java:global/Engine-EJB/CreditRequestDAOImpl!ru.simplgroupp.dao.interfaces.CreditRequestDAO");
        peopleDAO = (PeopleDAO) context.lookup("java:global/Engine-EJB/PeopleDAOImpl!ru.simplgroupp.dao.interfaces.PeopleDAO");
        referenceBooks = (ReferenceBooksLocal) context.lookup("java:global/Engine-EJB/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal");
    }

    @Test
    public void testExecuteSingle() throws ActionException {
        scoringOkbHunterBean.executeSingle(null, 11, pluginExecutionContext);
    }

    @Test
    public void testCreateRequest() throws Exception {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequest(11, Utils.setOf()).getEntity();
        PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
        PartnersEntity partner = referenceBooks.getPartnerEntity(Partner.OKB_HUNTER);

        Match match = scoringOkbHunterBean.createRequest(creditRequest, peopleMain, partner, false, SCL.LM);

        JAXBContext jaxbContext = JAXBContext.newInstance(Match.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        ByteArrayOutputStream outputStreamActual = new ByteArrayOutputStream();
        marshaller.marshal(match, outputStreamActual);
        System.out.println(String.format("match:\n%s", new String(outputStreamActual.toByteArray())));

        outputStreamActual.close();
    }

    @Test
    public void testCreateMatch() throws Exception {
        CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequest(11, Utils.setOf()).getEntity();
        PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
        PartnersEntity partner = referenceBooks.getPartnerEntity(Partner.OKB_HUNTER);

        Match match = scoringOkbHunterBean.createMatch(creditRequest, peopleMain, partner, false, SCL.LM);

        JAXBContext jaxbContext = JAXBContext.newInstance(Match.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        ByteArrayOutputStream outputStreamActual = new ByteArrayOutputStream();
        marshaller.marshal(match, outputStreamActual);
        System.out.println(String.format("match:\n%s", new String(outputStreamActual.toByteArray())));

        outputStreamActual.close();
    }

    @Test
    public void testChangePassword() throws ActionException {
        scoringOkbHunterBean.changePassword(pluginExecutionContext);
    }

    @Test
    public void testUploadAllCreditRequests() throws ActionException {
        scoringOkbHunterBean.uploadCreditRequests(pluginExecutionContext);
    }

    @Test
    public void testUploadCreditRequests() throws ActionException {
        List<CreditRequestEntity> creditRequests = new ArrayList<>();

        creditRequests.add(creditRequestDAO.getCreditRequestEntity(11));
        creditRequests.add(creditRequestDAO.getCreditRequestEntity(12));

        scoringOkbHunterBean.uploadCreditRequests(creditRequests, pluginExecutionContext);
    }

}
