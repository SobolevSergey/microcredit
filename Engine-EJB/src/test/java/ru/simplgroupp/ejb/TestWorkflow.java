package ru.simplgroupp.ejb;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;

import junit.framework.Assert;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

@LocalClient
public class TestWorkflow {

    @EJB
    KassaBeanLocal kassa;
    
    @EJB
    ReferenceBooksLocal refBook;
    
    @EJB
    PeopleBeanLocal ppl;    
    
    @EJB
    IFIASService fiasServ;
    
    @EJB
    MailBeanLocal mail;
    
    @EJB
    WorkflowBeanLocal wfBean;
    
    @EJB
    WorkflowEngineBeanLocal wfEng;
    
    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    RulesBeanLocal rules;
    
    @EJB
    CreditCalculatorBeanLocal creditCalc;    
    
    @Before
    public void setUp() throws Exception {
//    	System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");
		
		final Properties p = new Properties();

        p.load(this.getClass().getResourceAsStream("/test.properties"));
        
        final Context context = EJBContainer.createEJBContainer(p).getContext();
//        final Context context = new InitialContext(p);
        context.bind("inject", this);
        
        kassa = (KassaBeanLocal) context.lookup("java:global/Engine-EJB/KassaBean!ru.simplgroupp.interfaces.KassaBeanLocal");			
        refBook = (ReferenceBooksLocal) context.lookup("java:global/Engine-EJB/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal");
        ppl = (PeopleBeanLocal) context.lookup("java:global/Engine-EJB/PeopleBean!ru.simplgroupp.interfaces.PeopleBeanLocal");
        fiasServ=(IFIASService) context.lookup("java:global/Fias/FiasService!ru.simplgroupp.fias.ejb.IFIASService");
        mail = (MailBeanLocal) context.lookup("java:global/Engine-EJB/MailBean!ru.simplgroupp.interfaces.MailBeanLocal");
        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = (ActionProcessorBeanLocal) context.lookup("java:global/Engine-EJB/ActionProcessorBean!ru.simplgroupp.interfaces.ActionProcessorBeanLocal");
    } 
     
    @Test
    public void testNewCR() throws Exception {
    	Assert.assertNotNull(wfEng);
    	Assert.assertNotNull(wfBean);

    }
    
//	  @Test
	  public void testRejectCR() throws Exception {
	  	int creditrequestid = 1850;
	  	wfBean.removeProcCR(creditrequestid);
	  	ProcessInstance pinst = wfBean.startOrFindProcCR(creditrequestid, null);
	  	Assert.assertNotNull(pinst);
/*	  	
	  	ActionContext ctx = (ActionContext) pinst.getProcessVariables().get(ProcessKeys.VAR_ACTION_CONTEXT);
    	PluginConfig plc = ctx.getPlugins().getPluginConfig(VerificationBeanLocal.SYSTEM_NAME);
    	plc.setExecutionMode(ExecutionMode.AUTOMATIC);
    	plc.setUseFake(true);
    	plc.setSyncMode(SyncMode.SYNC);	
*/	  	
	  	List<WorkflowObjectStateDef> lstAct = wfBean.getProcCRWfActions(creditrequestid, null, true);
	  	Assert.assertTrue(lstAct.size() == 1);
	  	String ssig = SignalRef.toString("procNewCR", null, "msgMore");
	  	Assert.assertTrue(lstAct.get(0).getActions().get(0).getSignalRef().equals(ssig));
	  	wfBean.goProcCR(creditrequestid, ssig, null);

	  	lstAct = wfBean.getProcCRWfActions(creditrequestid, null, true);
	  	Assert.assertTrue(lstAct.size() == 1);
	  	ssig = SignalRef.toString("subStandart", "verify", "msgMore");
	  	BusinessObjectResult bres = new BusinessObjectResult(CreditRequest.class.getName(), new Integer(creditrequestid), true, null);
	  	wfBean.goProcCR(creditrequestid, ssig, Utils.<String, Object>mapOf(ProcessKeys.VAR_TASK_RESULT, bres));

	  	// taskVerificationEdit
	  	lstAct = wfBean.getProcCRWfActions(creditrequestid, null, true);
	  	Assert.assertTrue(lstAct.size() == 1);
	  	ssig = SignalRef.toString("procConsiderCR", null, "msgMore");
	  	bres = new BusinessObjectResult(CreditRequest.class.getName(), new Integer(creditrequestid), true, null);
	  	wfBean.goProcCR(creditrequestid, ssig, null);	  	
	  	
	  	lstAct = wfBean.getProcCRWfActions(creditrequestid, null, true);
	  	ssig = SignalRef.toString("procConsiderCR", null, "msgReject");
	  	Assert.assertTrue(lstAct.get(0).getActions().get(1).getSignalRef().equals("procConsiderCR::msgReject"));	  	
	  	kassa.saveCreditRefuse(creditrequestid, "аПбаОаВаЕбаКаА аОбаКаАаЗаА", 2);
	  	wfBean.goProcCR(creditrequestid, "procConsiderCR::msgReject", null);
	  }    
    
//    @Test
    public void testAcceptCR() throws Exception {
    	int creditrequestid = 1850;

        kassa.saveCreditApprove(creditrequestid, "test");
//    	wfBean.removeProcCR(creditrequestid);
//    	ProcessInstance pinst = wfBean.startOrFindProcCR(creditrequestid, null);
//    	Assert.assertNotNull(pinst);
//    	List<WorkflowObjectStateDef> lstAct = wfBean.getProcCRWfActions(creditrequestid);
//    	Assert.assertTrue(lstAct.size() == 1);
//    	String ssig = SignalRef.toString("procNewCR", null, "msgMore");
//    	Assert.assertTrue(lstAct.get(0).getActions().get(0).getSignalRef().equals(ssig));
//    	wfBean.goProcCR(creditrequestid, ssig, null);
//    	lstAct = wfBean.getProcCRWfActions(creditrequestid);
//    	Assert.assertTrue(lstAct.size() == 1);
//    	ssig = SignalRef.toString("procConsiderCR", null, "msgReject");
//    	Assert.assertTrue(lstAct.get(0).getActions().get(1).getSignalRef().equals(ssig));
//
//    	kassa.saveCreditDecision(creditrequestid, true, null);
//    	ssig = SignalRef.toString("procConsiderCR", null, "msgAccept");
//    	wfBean.goProcCR(creditrequestid, ssig, null);
//
//    	lstAct = wfBean.getProcCRWfActions(creditrequestid);
//    	Assert.assertTrue(lstAct.size() == 1 && lstAct.get(0).getActions().size() == 2);
    }
    
//    @Test
    public void testMoreCR() throws Exception {
    	int creditrequestid = 935;
    	ProcessInstance pinst = wfBean.startOrFindProcCR(creditrequestid, null);
    	Assert.assertNotNull(pinst);
    	List<WorkflowObjectStateDef> lstAct = wfBean.getProcCRWfActions(creditrequestid, null, true);
    	Assert.assertTrue(lstAct.size() == 1);
    	Assert.assertTrue(lstAct.get(0).getActions().get(0).getSignalRef().equals("procNewCR::msgMore"));
    	wfBean.goProcCR(creditrequestid, "procNewCR::msgMore", null);    	
    	Assert.assertTrue(lstAct.size() == 1);
/*    	
    	Assert.assertTrue(lstAct.get(0).getActions().get(1).getSignalRef().equals("procConsiderCR:msgReject"));
    	wfBean.signalProcCR(creditrequestid, "procConsiderCR:msgReject", null);
    	lstAct = wfBean.getProcCRWfActions(creditrequestid);  
*/    	
    }
    
  //  @Test
    public void testListTask() throws Exception {
    	List<WorkflowObjectStateDef> lst = wfEng.listTaskDefs(null,  Utils.listOf("creditmanager","verificator"), CreditRequest.class.getName(), null);
    	Assert.assertTrue( lst.size() > 0);
    	for (int i=0;i<lst.size();i++)
    	{
    	  System.out.println(lst.get(i).getDescription());
     	  System.out.println(lst.get(i).getName());
    	  System.out.println(lst.get(i).getPluginName());
    	  System.out.println(lst.get(i).getProcessDefinitionKey());
    	  System.out.println(lst.get(i).getStateName());
    	}
  //  	List<WorkflowObjectState> lstw = wfEng.listTasks(0, 10, null, "taskCreditDecision", null, Utils.listOf("creditmanager"), CreditRequest.class.getName(), "procConsiderCR", null);
  //  	Assert.assertTrue(lstw.size() > 0);
//    	lst = wfEng.listTasks(null, null, Utils.listOf("verificator", "creditmanager"), CreditRequest.class.getName(), null);
//    	Assert.assertTrue(lst.size() > 0);
    }
}
