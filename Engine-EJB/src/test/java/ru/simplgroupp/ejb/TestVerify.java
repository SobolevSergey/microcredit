package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.VerificationBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.workflow.ProcessKeys;

@LocalClient
public class TestVerify {

    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    WorkflowEngineBeanLocal wfEng;    
    
    @EJB
    WorkflowBeanLocal wfBean;    
    
    @Before
    public void setUp() throws Exception {
    	System.setProperty("openejb.validation.output.level","VERBOSE");
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
    	wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART, new Integer(759).toString());   	
    	actProc.runPlugin(VerificationBeanLocal.SYSTEM_NAME, CreditRequest.class.getName(), new Integer(759), null, context);
//    	ProcessInstance pinst = wfEng.startOrFindProcess(ProcessKeys.DEF_SUB_STANDART, "creditRequestId", new Integer(759), CreditRequest.class.getName(), Utils.mapOf());
    }
}
