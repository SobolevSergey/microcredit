package ru.simplgroupp.action;

import org.apache.openejb.api.LocalClient;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.CreditRequestWizardService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.CreditStatusEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;
import ru.simplgroupp.workflow.SignalRef;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;


@LocalClient
public class KassaBeanTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @EJB
    private WorkflowBeanLocal workflowBean;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private CreditRequestWizardService wizardService;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private CreditRequestDAO crDAO;

    private Integer creditRequestId;
    private Integer creditId;


    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        assertNotNull(kassaBean);
        assertNotNull(wizardService);
        assertNotNull(workflowBean);
        assertNotNull(refBooks);
    }

    @After
    public void tearDown() throws Exception {
        if (creditId != null) {
            creditBean.deleteCredit(creditId);
            Credit credit = creditBean.creditActive(crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId().getId());
            collector.checkThat(credit, CoreMatchers.nullValue());
        }

        collector.checkThat(creditRequestId, CoreMatchers.notNullValue());
        kassaBean.deleteCreditRequest(creditRequestId);
    }

    private Integer createTestCreditReq() {
        Integer creditRequestId = null;

        try {
            // step0
            Step0Data step0 = wizardService.step0(0, true);
            creditRequestId = wizardService.saveStep0(null, step0);
            workflowBean.startOrFindProcCR(creditRequestId, null);


            // step1
            Step1Data step1 = wizardService.step1(creditRequestId);
            step1.setFirstName("TestFirstName");
            step1.setMiddleName("TestMiddleName");
            step1.setLastName("TestLastName");
            step1.setPhone("+70000000000");
            step1.setEmail("testemail@testemail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse("08.08.1988");
            step1.setBirthday(date);
            wizardService.saveStep1(creditRequestId, step1);


            // step2
            Map<String, Object> step2 = kassaBean.paramsStep2(creditRequestId);
            step2.put("gender", 1);
            step2.put("place", "TestPlace");
            step2.put("seria", "1337");
            step2.put("nomer", "031337");
            step2.put("whoIssued", "TestIssued");
            step2.put("whenIssued", "01.01.2001");
            step2.put("code_dep", "031337");
            step2.put("registrationFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step2.put("home", "13371");
            step2.put("builder", "13372");
            step2.put("korpus", "13373");
            step2.put("apartment", "13374");
            step2.put("RealtyDate", "01.01.2001");
            step2.put("marriage", 1);
            step2.put("children", 2);
            step2.put("name", "Testspousename");
            step2.put("surname", "Testspousesurname");
            step2.put("midname", "Testspousemidname");
            step2.put("mobilephone", "+70000000001");
            step2.put("birthdate", "01.01.1990");
            step2.put("typework", 0);
            step2.put("databeg", "01.01.2013");
            step2.put("inn", "000000000000");
            step2.put("snils", "11111111111");
            kassaBean.saveStep2(step2);


            // step3
            Map<String, Object> step3 = kassaBean.paramsStep3(creditRequestId);
            step3.put("matuche", 0);
            step3.put("livingFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step2.put("home", "23371");
            step2.put("builder", "23372");
            step2.put("korpus", "23373");
            step2.put("apartment", "23374");
            step3.put("RegDate", "01.01.2001");
            step3.put("ground", 1);
            step3.put("regphone", "+70000000002");
            step3.put("phone", "+70000000003");
            step3.put("available", true);
            step3.put("regavailable", true);
            kassaBean.saveStep3(step3);


            // step4
            Map<String, Object> step4 = kassaBean.paramsStep4(creditRequestId);
            step4.put("education", 0);
            step4.put("employment", 0);
            step4.put("experience", "01.01.2001");
            step4.put("profession", 1);
            step4.put("workplace", "TestWorkPlace");
            step4.put("datestartwork", "01.01.2001");
            step4.put("monthlyincome", 101);
            step4.put("jobFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step4.put("home", "13371");
            step4.put("builder", "13372");
            step4.put("korpus", "13373");
            step4.put("occupation", "TestDirector");
            step4.put("workphone", "+70000000004");
            step4.put("extsalarysource", 1);
            step4.put("extsalarysource", 101);
            step4.put("available", true);
            step4.put("car", true);
            kassaBean.saveStep4(step4);


            // step5
            Map<String, Object> step5 = kassaBean.paramsStep5(creditRequestId);
            step5.put("prevcredits", 1);
            step5.put("creditorganization", 604);
            step5.put("credittype", 1);
            step5.put("creditdate", "01.01.2001");
            step5.put("creditisover", 1);
            step5.put("currencytype", 643);
            step5.put("creditsumprev", 1000);
            step5.put("overdue", "1");
            wizardService.saveStep5(step5);


            // step6
            Step6Data step6 = wizardService.step6(creditRequestId);
            step6.setOption(Account.CONTACT_TYPE);
            wizardService.saveStep6(creditRequestId, step6);


            // step7
            Map<String, Object> step7 = kassaBean.paramsStep7(creditRequestId);
            step7.put("creditsum", 1000);
            step7.put("creditdays", 2);
//            step7.put("stake", 0.04);
//            step7.put("stakeMin", 0.01);
//            step7.put("stakeMax", 0.04);
//            step7.put("creditDaysMin", 2);
//            step7.put("creditDaysMax", 31);
//            step7.put("creditSumMin", 1000);
//            step7.put("creditSumMax", 10000);
            kassaBean.saveStep7(step7);
            workflowBean.goProcCR(creditRequestId, SignalRef.toString("procNewCR", null, "msgMore"), null);


            // step 8
            kassaBean.paramsStep8(creditRequestId);
        } catch (Throwable e) {
            collector.addError(e);
        }

        return creditRequestId;
    }

    @Test
    public void testAllSteps() {
        try {
            creditRequestId = createTestCreditReq();
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testSaveCreditApprove() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            collector.checkThat(creditRequestEntity, CoreMatchers.notNullValue());
//            collector.checkThat(creditRequestEntity.getAccepted(), CoreMatchers.nullValue());

            kassaBean.saveCreditApprove(creditRequestId, "TestSaveCreditApproveComment");
            collector.checkThat(1, CoreMatchers.equalTo(crDAO.getCreditRequestEntity(creditRequestId).getAccepted()));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testSaveCreditRefuse() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            collector.checkThat(creditRequestEntity, CoreMatchers.notNullValue());
            kassaBean.saveCreditRefuse(
                    creditRequestId,
                    "TestSaveCreditDisapproveComment",
                    refBooks.getRefuseReasons().get(0).getId());

            collector.checkThat(0, CoreMatchers.equalTo(crDAO.getCreditRequestEntity(creditRequestId).getAccepted()));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testSaveClientRefuse() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            Integer peopleId = creditRequestEntity.getPeopleMainId().getId();

            kassaBean.saveClientRefuse(creditRequestId, peopleId);

            creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            CreditStatusEntity peopleRefuseStatus = refBooks.getCreditRequestStatus(CreditStatus.CLIENT_REFUSE);

            collector.checkThat(peopleRefuseStatus, CoreMatchers.equalTo(creditRequestEntity.getStatusId()));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testStartCredit() {
        try {
            creditRequestId = createTestCreditReq();


            creditId = kassaBean.startCredit(creditRequestId);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddCreditRequest() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            CreditRequest creditRequest = new CreditRequest(creditRequestEntity);


            Integer creditDays = creditRequest.getCreditDays() > 2 ? 2 : 3;
            creditRequest.setCreditDays(creditDays);

            Double creditSum = creditRequest.getCreditSum() > 1000 ? 1000.0 : 2000.0;
            creditRequest.setCreditSum(creditSum);


            CreditRequestEntity creditRequestEntity1 = kassaBean.addCreditRequest(creditRequest);
            collector.checkThat(creditRequestEntity1.getCreditdays(), CoreMatchers.equalTo(creditDays));
            collector.checkThat(creditRequestEntity1.getCreditsum(), CoreMatchers.equalTo(creditSum));
            collector.checkThat(creditRequestEntity1.getPeopleMainId(), CoreMatchers.notNullValue());
            collector.checkThat(creditRequestEntity1.getStatusId(), CoreMatchers.notNullValue());
            collector.checkThat(creditRequestEntity1.getContest(), CoreMatchers.notNullValue());
            System.out.println("creditRequestEntity = " + creditRequestEntity1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }
}
