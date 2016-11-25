package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
import ru.simplgroupp.interfaces.VerificationBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.ExecutionMode;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.SyncMode;
import ru.simplgroupp.toolkit.TransientMap;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.ProcessKeys;

@LocalClient
public class TestCalcModel {
    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    WorkflowEngineBeanLocal wfEng; 
    
    @EJB
    WorkflowBeanLocal wfBean; 
    
    @EJB
    RulesBeanLocal rulesBean;  
    
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
        
        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        rulesBean = (RulesBeanLocal) context.lookup("java:global/Engine-EJB/RulesBean!ru.simplgroupp.interfaces.RulesBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = (ActionProcessorBeanLocal) context.lookup("java:global/Engine-EJB/ActionProcessorBean!ru.simplgroupp.interfaces.ActionProcessorBeanLocal");
    }   
    
    @Test
    public void testServices() {
    	Assert.assertNotNull(actProc);
    }
    
   // @Test
    public void testPlugins() throws Exception{
    	
    	ActionContext actionContext = actProc.createActionContext(null, true);
    	String st=actProc.findPaymentSystem(759, actionContext);
    	System.out.print(st);
    }
    
    @Test
    public void testCalcModel() throws Exception {
	  	int creditrequestid = 31404;
	  	wfBean.removeProcCR(creditrequestid);
	  	
	  	ActionContext actionContext = actProc.createActionContext(null, true);
    	PluginConfig plc = actionContext.getPlugins().getPluginConfig(VerificationBeanLocal.SYSTEM_NAME);
    	plc.setExecutionMode(ExecutionMode.AUTOMATIC);
    	plc.setUseFake(true);
    	plc.setSyncMode(SyncMode.SYNC);
    	
	  	ProcessInstance pinst = wfEng.startProcess(ProcessKeys.DEF_WORK_MODEL, "ru.simplgroupp.transfer.CreditRequest:" + String.valueOf(creditrequestid), Utils.<String, Object>mapOf(
	  			ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, CreditRequest.class.getName(),
	  			ProcessKeys.VAR_BUSINESS_OBJECT_ID, creditrequestid,
	  			ProcessKeys.VAR_MODEL_KEY, ModelKeys.TARGET_CREDIT_DECISION,
	  			ProcessKeys.VAR_ACTION_CONTEXT, actionContext,
	  			ProcessKeys.VAR_MODEL_ID, 1
	  			));
		while ( (pinst != null) && ( ! pinst.isEnded() )) {
			// TODO
			try {
				Thread.sleep(30*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pinst = wfEng.getProcessInstance(pinst.getProcessInstanceId());
		}
    }
}
