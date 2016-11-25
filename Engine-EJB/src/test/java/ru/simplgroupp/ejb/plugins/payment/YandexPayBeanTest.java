package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PeopleBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.fixtures.AccountFixture;
import ru.simplgroupp.fixtures.CreditFixture;
import ru.simplgroupp.fixtures.CreditRequestFixture;
import ru.simplgroupp.fixtures.PaymentFixture;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author Khodyrev DS
 */
@LocalClient
public class YandexPayBeanTest {

    @EJB
    private ActionProcessorBeanLocal actProc;

    @EJB
    private WorkflowEngineBeanLocal wfEng;

    @EJB
    private WorkflowBeanLocal wfBean;

    @EJB
    private YandexPayBeanLocal yandexPay;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private ICryptoService cryptoService;

    @PersistenceContext(unitName="MicroPU")
    private EntityManager emMicro;

    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = wfEng.getActionProcessor();
    }

    @Test
    public void sendSingleRequestTest() {
    	ActionContext context = actProc.createActionContext(null, true);
    	PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(YandexPayBeanLocal.SYSTEM_NAME), null, 0, Utils.<String, Object>mapOf(), context.getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
    	
        yandexPay = context.getPlugins().yandexPay;

        Account invalidAccount = AccountFixture.getYandexInvalidAccount();
        CreditRequest creditRequest = CreditRequestFixture.createCreditRequest(invalidAccount);
        Credit credit = CreditFixture.createCredit(creditRequest);
        Payment payment = PaymentFixture.createPayment(credit);
        try {
            yandexPay.sendSingleRequest(payment.getEntity(), plctx);
            Assert.assertEquals(1,2);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }

        Account validAccount = AccountFixture.getYandexValidAccount();
        creditRequest = CreditRequestFixture.createCreditRequest(validAccount);
        credit = CreditFixture.createCredit(creditRequest);
        payment = PaymentFixture.createPayment(credit);

        try {
            yandexPay.sendSingleRequest(payment.getEntity(), plctx);
        } catch (ActionException e) {
            Assert.assertEquals(1,2);
        }
    }

    @Test
    public void querySingleResponseTest() {
    	ActionContext context = actProc.createActionContext(null, true);
    	PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(YandexPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), context.getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
        yandexPay = context.getPlugins().yandexPay;

        Account invalidAccount = AccountFixture.getYandexInvalidAccount();
        CreditRequest creditRequest = CreditRequestFixture.createCreditRequest(invalidAccount);
        Credit credit = CreditFixture.createCredit(creditRequest);
        Payment payment = PaymentFixture.createPayment(credit);
        try {
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);
            Assert.assertEquals(1,2);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }

        Account validAccount = AccountFixture.getYandexValidAccount();
        creditRequest = CreditRequestFixture.createCreditRequest(validAccount);
        credit = CreditFixture.createCredit(creditRequest);
        payment = PaymentFixture.createPayment(credit);

        try {
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);
        } catch (ActionException e) {
            Assert.assertEquals(1,2);
        }

        payment = PaymentFixture.createPayment(credit);
        payment.getEntity().setAmount(1001.0);
        try {
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);
            Assert.assertEquals(1,2);
        } catch (ActionException e) {
        }

        try {
            payment = PaymentFixture.createPayment(credit);
            payment.getEntity().setAmount(999.0);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);

            payment = PaymentFixture.createPayment(credit);
            payment.getEntity().setAmount(999.0);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);

            payment = PaymentFixture.createPayment(credit);
            payment.getEntity().setAmount(999.0);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);

            payment = PaymentFixture.createPayment(credit);
            payment.getEntity().setAmount(999.0);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);

            payment = PaymentFixture.createPayment(credit);
            payment.getEntity().setAmount(999.0);
            yandexPay.querySingleResponse("CreditRequest", credit.getCreditRequest().getId(), plctx);
            Assert.assertEquals(1,2);
        } catch (ActionException e) {

        }
    }

    @Test
    public void balanceTest() throws ActionException, KassaException {
    	ActionContext context = actProc.createActionContext(null, true);
    	PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(YandexPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), context.getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
        yandexPay = context.getPlugins().yandexPay;

        double f = yandexPay.balance(plctx);
        System.out.println("Баланс: " + f);
    }

    @Test
    public void checkWalletDataTest() throws ActionException, KassaException {
        YandexPayPluginConfig yandexConfig = new YandexPayPluginConfig();
        yandexConfig.setUseVerification(true);
        PluginExecutionContext pluginContext = new PluginExecutionContext(yandexConfig, null, 0, null, null);
        YandexPayBean mock = spy(new YandexPayBean());

        PeoplePersonalEntity personalEntity = new PeoplePersonalEntity();
        personalEntity.setName("Иван");
        personalEntity.setSurname("Иванов");
        personalEntity.setMidname("Иванович");

        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setSeries("4541");
        documentEntity.setNumber("190871");

        PeopleBean peopleBean = mock(PeopleBean.class);
        doReturn(personalEntity).when(peopleBean).findPeoplePersonalActive(any(PeopleMainEntity.class));
        doReturn(documentEntity).when(peopleBean).findPassportActive(any(PeopleMainEntity.class));
        mock.peopleBean = peopleBean;

        try {
            String validAnswer = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
                    "<response code=\"0\" performed-datetime=\"2016-02-16T10:00:14.942Z\">\n" +
                    "  <error code=\"0\">\n" +
                    "    <message>Сообщение об успехе.</message>\n" +
                    "    <tech-message>Технических проблем нет.</tech-message>\n" +
                    "  </error>\n" +
                    "</response>\n";
            String errorAnswer = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
                    "<response code=\"3\" performed-datetime=\"2016-02-16T10:00:14.942Z\">\n" +
                    "  <error code=\"104\">\n" +
                    "    <message>Сообщение с ошибкой.</message>\n" +
                    "    <tech-message>Детали сообщения с ошибкой.</tech-message>\n" +
                    "  </error>\n" +
                    "</response>\n";
            doReturn(errorAnswer.getBytes("Cp1251")).when(mock).postCheckWalletRequest(contains("-invalid"), (Map<String, String>) any(), (SSLContext) any());
            doReturn(validAnswer.getBytes("Cp1251")).when(mock).postCheckWalletRequest(contains("-valid"), (Map<String, String>) any(), (SSLContext) any());
            doReturn(null).when(mock).createSSLContext(anyString(), anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertFalse(mock.checkWalletData("410011935683423-invalid", new PeopleMainEntity(28), pluginContext));
        Assert.assertTrue(mock.checkWalletData("410011935683423-valid", new PeopleMainEntity(28), pluginContext));
    }

    //@Test
    public void testRun() throws Exception {


    	ActionContext context = actProc.createActionContext(null, true);
        //wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART,Integer.toString(1316));
        //actProc.runPlugin(YandexPayBeanLocal.SYSTEM_NAME, Payment.class.getName(), new Integer(1316), null);
        yandexPay = context.getPlugins().yandexPay;

        yandexPay.sendSingleRequest(Payment.class.getName(), 1318, null);
    }
}
