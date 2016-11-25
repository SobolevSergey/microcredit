package ru.simplgroupp.ejb;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.payment.AlfaBankMarengoBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.workflow.ProcessKeys;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Properties;

/**
 * @author Khodyrev DS
 */
@LocalClient
public class AlfaBankMarengoBeanTest {

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
    KassaBeanLocal kassaBean;

    @PersistenceContext(unitName="MicroPU")
    protected EntityManager emMicro;

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
    public void testRun() throws Exception {

    	ActionContext context = actProc.createActionContext(null, true);

        wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART, new Integer(1128).toString());
        actProc.runPlugin(AlfaBankMarengoBeanLocal.SYSTEM_NAME, PaymentEntity.class.getName(), new Integer(1128), null, context);
    }
}
