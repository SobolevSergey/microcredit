package ru.simplgroupp.action;

import org.apache.openejb.api.LocalClient;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.ejb.ApplicationAction;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.AppServiceBeanLocal;
import ru.simplgroupp.interfaces.service.CreditRequestWizardService;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

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
public class CreditBeanTest {
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
    private ProductBeanLocal productBean;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private AppServiceBeanLocal appService;

    @EJB
    private CreditDAO creditDAO;

    @EJB
    private PaymentService paymentService;

    @EJB
    private CreditCalculatorBeanLocal creditCalc;

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
            step7.put("creditsum", 10000);
            step7.put("creditdays", 3);
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
    public void testStartStopRestartCredit() {
        try {
            creditRequestId = createTestCreditReq();


            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));

            PeopleMainEntity peopleMain = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            Credit credit = creditBean.creditActive(peopleMain.getId());
            collector.checkThat(credit.getId(), CoreMatchers.equalTo(creditId));
            collector.checkThat(credit.getIsActive(), CoreMatchers.equalTo(ActiveStatus.ACTIVE));

            creditBean.stopCredit(creditId, new Date(), StateRef.valueOf("procCR::callPaymentInitial"));
            collector.checkThat(creditBean.creditActive(peopleMain.getId()).getIsActive(), CoreMatchers.equalTo(ActiveStatus.ARCHIVE));

            creditBean.restartCredit(creditId, new Date());
            collector.checkThat(creditBean.creditActive(peopleMain.getId()).getIsActive(), CoreMatchers.equalTo(ActiveStatus.ACTIVE));

        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testCreditToCourt() {
        try {
            creditRequestId = createTestCreditReq();


            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));

            creditBean.creditToCourt(creditId, new Date());

            PeopleMainEntity peopleMain = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            Credit credit = creditBean.creditActive(peopleMain.getId());
            collector.checkThat(credit.getId(), CoreMatchers.equalTo(creditId));
            collector.checkThat(credit.getForCourt(), CoreMatchers.equalTo(true));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testCreditToCollector() {
        try {
            creditRequestId = createTestCreditReq();


            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));

            creditBean.creditToCollector(creditId);

            CreditRequestEntity creditEntity = crDAO.getCreditRequestEntity(creditRequestId);
            Credit credit = creditBean.creditActive(creditEntity.getPeopleMainId().getId());
            collector.checkThat(credit.getId(), CoreMatchers.equalTo(creditId));
            collector.checkThat(credit.getForCollector(), CoreMatchers.equalTo(true));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testStartRefinance() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditReqEntity = crDAO.getCreditRequestEntity(creditRequestId);
            creditReqEntity.setDateSign(new Date());
            kassaBean.saveCreditRequest(creditReqEntity);

            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));

            Credit credit = creditBean.creditActive(crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId().getId());

            Double amount = 200.0;

            Refinance refinance = new Refinance();
            refinance.setSmsCode("123456");
            refinance.setRefinanceDays(30);
            refinance.setRefinanceStake(0.001);
            refinance.setAgreement("Согласен");
            refinance.setRefinanceAmount(amount);
            refinance.setUniqueNomer("1040009021500001");
            creditBean.saveRefinanceRequest(creditId, refinance, new Date());
//            creditBean.createRefinanceDraft(creditId, new Date(), 10, amount, 0.04);
            creditBean.startRefinance(creditId);
            creditBean.cancelRefinanceDraft(creditId);

            PeopleMainEntity peopleMain = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            Credit creditRefinance = creditBean.creditActive(peopleMain.getId());
            collector.checkThat(creditRefinance.getCreditSum(), CoreMatchers.equalTo(credit.getCreditSum() * (creditReqEntity.getStake() + 1) - amount));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testStartProlong() {
        try {
            creditRequestId = createTestCreditReq();

            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));

            PeopleMainEntity peopleMain = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            Credit credit = creditBean.creditActive(peopleMain.getId());

            Map<String, Object> limits = productBean.getProlongProductConfig(credit.getProduct().getId());
            PeopleMain people = peopleDAO.getPeopleMain(peopleMain.getId(), Utils.setOf(PeopleMain.Options.INIT_ACCOUNTACTIVE,
                    PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL,
                    PeopleMain.Options.INIT_DOCUMENT, PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_PEOPLE_MISC));
            Prolong prolong = kassaBean.initProlong(people, credit, new Date(), limits);


            ProlongEntity prolongEntity = kassaBean.saveLongRequestNew(creditId, prolong, prolong.getLongDate(), "127.0.0.1", "Chrome");
            WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef(false);
            actionDef.setBusinessObjectClass(Prolong.class.getName());
            actionDef.setSignalRef(ProcessKeys.MSG_PROLONG);
            actionDef.setRunProcessDefKey(ProcessKeys.DEF_CREDIT_PROLONG);
            workflowBean.goProc(Prolong.class.getName(), prolongEntity.getId(), actionDef, SignalRef.toString("*", null, "msgNone"), Utils.mapOfSO("creditId", creditId));
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddCredit() {
        try {
            creditRequestId = createTestCreditReq();


            creditId = kassaBean.startCredit(creditRequestId);
            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
//            PeopleMain peopleMain = peopleDAO.getPeopleMain(peopleMainEntity.getId(), Utils.setOf(PeopleMain.Options.INIT_ACCOUNTACTIVE,
//                    PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL,
//                    PeopleMain.Options.INIT_DOCUMENT, PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_PEOPLE_MISC));
//            Credit credit = creditBean.initCredit(peopleMain, creditRequestId);

            Credit credit = creditBean.creditActive(peopleMainEntity.getId());
            credit.setCreditRequest(new CreditRequest(crDAO.getCreditRequestEntity(creditRequestId)));


            Credit credit1 = creditBean.addCredit(credit, peopleMainEntity.getId());

            System.out.println("credit = " + credit1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testPayStartRefinance() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditRequestEntity = crDAO.getCreditRequestEntity(creditRequestId);
            creditRequestEntity.setDateSign(new Date());
            kassaBean.saveCreditRequest(creditRequestEntity);

            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));


            Credit credit = creditBean.creditActive(creditRequestEntity.getPeopleMainId().getId());
            Map<String, Object> limits = productBean.getRefinanceProductConfig(credit.getProduct().getId());
            Refinance refinance = creditBean.initRefinance(credit, limits);

            Double refinanceAmount = 1500.0;
            refinance.setRefinanceAmount(refinanceAmount);


            // saveRefinanceRequest
            credit.setCreditRequest(new CreditRequest(creditRequestEntity));
            ApplicationAction action = appService.startApplicationAction(
                    ProcessKeys.MSG_REFINANCE, true, "Рефинансирование",
                    new BusinessObjectRef(Credit.class.getName(), credit.getId()),
                    new BusinessObjectRef(CreditRequest.class.getName(), credit.getCreditRequest().getId())
            );

            WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef(false);
            actionDef.setBusinessObjectClass(Refinance.class.getName());
            actionDef.setSignalRef(ProcessKeys.MSG_REFINANCE_RUN);
            actionDef.setRunProcessDefKey(ProcessKeys.DEF_REFINANCE);

            RefinanceEntity refinanceEntity = creditBean.saveRefinanceRequest(creditId, refinance, refinance.getRefinanceDate());
            workflowBean.goProc(Refinance.class.getName(), refinanceEntity.getId(), actionDef,
                    SignalRef.toString("*", null, "msgNone"), Utils.mapOfSO("creditId", creditId));

            appService.endApplicationAction(action);


            Payment paymentRefinance = paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE,
                    Payment.SUM_FROM_CLIENT, refinanceAmount, Payment.TO_SYSTEM, Partner.YANDEX);
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("paymentId", paymentRefinance.getId());
            paymentData.put("date", new Date());
            workflowBean.repaymentReceivedYandex(credit.getCreditRequest().getId(), paymentData);
            paymentService.processSuccessPayment(paymentRefinance.getId(), new Date());


            // start
            creditBean.startRefinance(creditId);


            // pay all
            CreditRequestEntity creditReqEntity = crDAO.getCreditRequestEntity(creditRequestId);
            Double payAllAmount = credit.getSumMainRemain() * (creditReqEntity.getStake() + 1);
            Payment payment = paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE,
                    Payment.SUM_FROM_CLIENT, payAllAmount, Payment.TO_SYSTEM, Partner.YANDEX);
            paymentData = new HashMap<>();
            paymentData.put("paymentId", payment.getId());
            paymentData.put("date", new Date());
            workflowBean.repaymentReceivedYandex(credit.getCreditRequest().getId(), paymentData);
            paymentService.processSuccessPayment(payment.getId(), new Date());


            Double sum = paymentService.getCreditPaymentSum(credit.getId());
            collector.checkThat(payAllAmount + refinanceAmount, CoreMatchers.equalTo(sum));


            collector.checkThat(
                    creditBean.creditArchive(crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId()),
                    CoreMatchers.notNullValue());
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testPayStartProlong() {
        try {
            creditRequestId = createTestCreditReq();


            CreditRequestEntity creditReqEntity = crDAO.getCreditRequestEntity(creditRequestId);
            creditReqEntity.setDateSign(new Date());
            kassaBean.saveCreditRequest(creditReqEntity);

            creditId = kassaBean.startCredit(creditRequestId);
            collector.checkThat(creditId, CoreMatchers.not(0));


            Credit credit = creditBean.creditActive(creditReqEntity.getPeopleMainId().getId());
            Map<String, Object> limits = productBean.getProlongProductConfig(credit.getProduct().getId());

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            PeopleMain peopleMain = peopleDAO.getPeopleMain(peopleMainEntity.getId(), Utils.setOf(PeopleMain.Options.INIT_ACCOUNTACTIVE,
                    PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL,
                    PeopleMain.Options.INIT_DOCUMENT, PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_PEOPLE_MISC));
            Prolong prolong = kassaBean.initProlong(peopleMain, credit, new Date(), limits);


            Map<String, Object> cpparams = creditCalc.calcProlongDays(credit, new Date());
            Integer daysf = Convertor.toInteger(cpparams.get(CreditCalculatorBeanLocal.PROLONG_DAYS_BEFORE_CREDIT_END));

            Map<String, Object> crparams = creditCalc.calcCreditInitial(credit.getSumMainRemain(), credit.getCreditPercent(),
                    (prolong.getLongdays() == null ? 0 : prolong.getLongdays()) + daysf - 1,1);
            Double sumStake = Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT));
            Double sumAll = (credit.getSumMainRemain()) + sumStake;

            prolong.setLongamount(sumAll);
            prolong.setLongdate(new Date());


            credit.setCreditRequest(new CreditRequest(creditReqEntity));
            ApplicationAction action = appService.startApplicationAction(
                    ProcessKeys.MSG_PROLONG, true, "Продление",
                    new BusinessObjectRef(Prolong.class.getName(), prolong.getId()),
                    new BusinessObjectRef(Credit.class.getName(), credit.getId()),
                    new BusinessObjectRef(CreditRequest.class.getName(), credit.getCreditRequest().getId())
            );

            ProlongEntity prolongEntity = kassaBean.saveLongRequestNew(credit.getId(), prolong, prolong.getLongDate(), "127.0.0.1", "Test");
            //workflow.goProcCR(creditRequestId, SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST, null, ProcessKeys.MSG_PROLONG), Collections.<String, Object>emptyMap());
            WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef(false);
            actionDef.setBusinessObjectClass(Prolong.class.getName());
            actionDef.setSignalRef(ProcessKeys.MSG_PROLONG);
            actionDef.setRunProcessDefKey(ProcessKeys.DEF_CREDIT_PROLONG);
            workflowBean.goProc(Prolong.class.getName(), prolongEntity.getId(), actionDef, SignalRef.toString("*", null, "msgNone"), Utils.mapOfSO("creditId", credit.getId()));
            appService.endApplicationAction(action);


            Double prolongAmount = credit.getCreditSum() * creditReqEntity.getStake();
            Payment paymentProlong = paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE,
                    Payment.SUM_FROM_CLIENT, prolongAmount, Payment.TO_SYSTEM, Partner.YANDEX);
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("paymentId", paymentProlong.getId());
            paymentData.put("date", new Date());
            workflowBean.repaymentReceivedYandex(credit.getCreditRequest().getId(), paymentData);
            paymentService.processSuccessPayment(paymentProlong.getId(), new Date());


            // start
            kassaBean.startProlong(creditId);


            // pay all
            Double payAllAmount = credit.getSumMainRemain() * (creditReqEntity.getStake() + 1);
            Payment payment = paymentService.createPayment(credit.getId(), Account.YANDEX_TYPE,
                    Payment.SUM_FROM_CLIENT, payAllAmount, Payment.TO_SYSTEM, Partner.YANDEX);
            paymentData = new HashMap<>();
            paymentData.put("paymentId", payment.getId());
            paymentData.put("date", new Date());
            workflowBean.repaymentReceivedYandex(credit.getCreditRequest().getId(), paymentData);
            paymentService.processSuccessPayment(payment.getId(), new Date());


            Double sum = paymentService.getCreditPaymentSum(credit.getId());
            collector.checkThat(payAllAmount + prolongAmount, CoreMatchers.equalTo(sum));


            collector.checkThat(
                    creditBean.creditArchive(crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId()),
                    CoreMatchers.notNullValue());
        } catch (Throwable e) {
            collector.addError(e);
        }
    }
}
