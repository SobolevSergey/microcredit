package ru.simplgroupp.ejb;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.ejb.plugins.payment.QiwiAcquiringBean;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.AlfaBankMarengoBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.QiwiAcquiringBeanLocal;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;
import ru.simplgroupp.workflow.ProcessKeys;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Khodyrev DS
 */
@LocalClient
public class QiwiAcquiringBeanTest {

    @EJB
    ActionProcessorBeanLocal actProc;

    @EJB
    WorkflowEngineBeanLocal wfEng;

    @EJB
    WorkflowBeanLocal wfBean;

    @EJB
    AlfaBankMarengoBeanLocal alfaBank;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    PaymentService paymentService;

    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = wfEng.getActionProcessor();
    }

    @Test
    public void doOrder() throws Exception {
        int paymentId = paymentService.createPayment(1063, Account.QIWI_TYPE, Payment.SUM_FROM_CLIENT, 200.0,
                Payment.TO_SYSTEM, Partner.QIWI).getId();

        ActionContext context = actProc.createActionContext(null, true);
        PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(QiwiAcquiringBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), context.getPluginState(QiwiAcquiringBeanLocal.SYSTEM_NAME));
        
        QiwiAcquiringBeanLocal qiwiAcquiringBean = context.getPlugins().getQiwiAcquiring();

        qiwiAcquiringBean.doOrder("79132411981", paymentId, plctx);
    }

    @Test
    public void testRun() throws Exception {

    	ActionContext context = actProc.createActionContext(null, true);

        wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART, Integer.toString(901));
        actProc.runPlugin(QiwiAcquiringBean.SYSTEM_NAME, PaymentEntity.class.getName(), new Integer(901), null, context);
    }
}
