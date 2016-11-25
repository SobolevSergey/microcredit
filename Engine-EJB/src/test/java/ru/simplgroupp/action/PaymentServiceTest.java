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
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;
import ru.simplgroupp.workflow.SignalRef;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;


@LocalClient
public class PaymentServiceTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @EJB
    private CreditRequestDAO crDAO;

    @EJB
    private WorkflowBeanLocal workflowBean;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private CreditRequestWizardService wizardService;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private PaymentService paymentService;

    private Integer creditRequestId;
    private Integer creditId;


    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        assertNotNull(creditBean);
        assertNotNull(wizardService);
        assertNotNull(workflowBean);
        assertNotNull(refBooks);
    }

    @After
    public void tearDown() throws Exception {
        collector.checkThat(creditRequestId, CoreMatchers.notNullValue());
        collector.checkThat(creditId, CoreMatchers.notNullValue());

        creditBean.deleteCredit(creditId);
        kassaBean.deleteCreditRequest(creditRequestId);
    }

    private Integer startTestCredit() {
        Integer creditRequestId = null;

        try {
            // step0
            Step0Data step0 = wizardService.step0(0, true);
            creditRequestId = wizardService.saveStep0(null, step0);
            workflowBean.startOrFindProcCR(creditRequestId, null);


            // step1
            Step1Data step1 = wizardService.step1(creditRequestId);
            step1.setFirstName("Testfirstname");
            step1.setMiddleName("Testmiddlename");
            step1.setLastName("Testlastname");
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
            step2.put("inn", "");
            step2.put("snils", "");
            step2.put("marriage", 0);
            step2.put("children", null);
            step2.put("registrationFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step2.put("home", "1337");
            step2.put("typework", null);
            step2.put("databeg", null);
            step2.put("RealtyDate", "01.01.2001");
            kassaBean.saveStep2(step2);


            // step3
            Map<String, Object> step3 = kassaBean.paramsStep3(creditRequestId);
            step3.put("matuche", 1);
            step3.put("ground", 0);
            step3.put("RegDate", "01.01.2001");
            kassaBean.saveStep3(step3);


            // sty =
            Map<String, Object> step4 = kassaBean.paramsStep4(creditRequestId);
            step4.put("education", 0);
            step4.put("employment", 0);
            step4.put("experience", "01.01.2001");
            step4.put("profession", 1);
            step4.put("workplace", "TestWorkPlace");
            step4.put("datestartwork", "01.01.2001");
            step4.put("monthlyincome", 101);
            step4.put("car", false);
            kassaBean.saveStep4(step4);


            // step5
            Map<String, Object> step5 = kassaBean.paramsStep5(creditRequestId);
            step5.put("prevcredits", 0);
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


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            creditRequestEntity.setDateSign(new Date());
            kassaBean.saveCreditRequest(creditRequestEntity);
            // start credit
            creditId = kassaBean.startCredit(creditRequestId);
        } catch (Exception e) {
            collector.addError(e);
        }

        return creditRequestId;
    }

    @Test
    public void testCreatePayment() {
        try {
            creditRequestId = startTestCredit();


            CreditRequestEntity creditReqEntity = crDAO.getCreditRequestEntity(creditRequestId);
            Credit credit = creditBean.creditActive(creditReqEntity.getPeopleMainId().getId());
            credit.setCreditRequest(new CreditRequest(crDAO.getCreditRequestEntity(creditRequestId)));

            Double payAllAmount = credit.getCreditSum() * (creditReqEntity.getStake() + 1);
            Payment payment = paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE,
                    Payment.SUM_FROM_CLIENT, payAllAmount, Payment.TO_SYSTEM, Partner.YANDEX);
            Map<String, Object> data = new HashMap<>();
            data.put("paymentId", payment.getId());
            data.put("date", new Date());
            workflowBean.repaymentReceivedYandex(credit.getCreditRequest().getId(), data);
            paymentService.processSuccessPayment(payment.getId(), new Date());


            Double sum = paymentService.getCreditPaymentSum(credit.getId());
            collector.checkThat(payAllAmount, CoreMatchers.equalTo(sum));



            collector.checkThat(
                    creditBean.creditArchive(crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId()),
                    CoreMatchers.notNullValue());
        } catch (Exception e) {
            collector.addError(e);
        }
    }
}
