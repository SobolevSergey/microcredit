package ru.simplgroupp.ejb;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ModelBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.ExecutionMode;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.SyncMode;
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
import ru.simplgroupp.interfaces.VerificationBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.toolkit.common.ExecutionProgress;
import ru.simplgroupp.toolkit.common.FileDataHolder;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.toolkit.common.ZipUtils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.CreditRequestIteratorDB;
import ru.simplgroupp.util.CreditRequestIteratorXml;

@LocalClient
public class TestDebugModel {
    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    WorkflowEngineBeanLocal wfEng; 
    
    @EJB
    WorkflowBeanLocal wfBean;    
    
    @EJB
    KassaBeanLocal kassaBean;
    
    @EJB
    ModelBeanLocal modelBean;
    
    @EJB
    CreditRequestDAO crDAO;
    @Before
    public void setUp() throws Exception {
    	System.setProperty("openejb.validation.output.level","VERBOSE");
		final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
	
        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
        
        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = (ActionProcessorBeanLocal) context.lookup("java:global/Engine-EJB/ActionProcessorBean!ru.simplgroupp.interfaces.ActionProcessorBeanLocal");
    }   
    
    @Test
    public void testServices() {
    	Assert.assertNotNull(actProc);
    }
    
    @Test
    public void testDebugModel() throws Exception {
    	String customKey = UUID.randomUUID().toString();
    	
    	List<String> lstXmls = new ArrayList<String>(10);
    	CreditRequestIteratorDB iter1 = new CreditRequestIteratorDB("2574", crDAO);
    	
    	ActionContext ctx = actProc.createActionContext(customKey, false);
    	PluginConfig plc = ctx.getPlugins().getPluginConfig(VerificationBeanLocal.SYSTEM_NAME);
    	plc.setExecutionMode(ExecutionMode.AUTOMATIC);
    	plc.setUseFake(true);
    	plc.setSyncMode(SyncMode.SYNC);
    	
		modelBean.executeModelsSync(iter1, 3, ctx, lstXmls, new ExecutionProgress() {

			@Override
			public void progress(Object indicator) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void finished() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean finishRequest() {
				// TODO Auto-generated method stub
				return false;
			}}, 5);
		
		if (lstXmls.size() > 0) {
			File resFile = File.createTempFile("debug-results", ".zip");
			
			ZipUtils.zipTxtFiles(lstXmls, "debug-results", ".xml", resFile);		
		}		
    }
    
//    @Test
    public void testMapping() throws Exception {

    	// write to xml
    	String sxml = null;
    	    	
		JAXBContext jaxbContext = JAXBContext.newInstance("ru.simplgroupp.transfer");
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		
    	CreditRequest ccRequest = crDAO.getCreditRequest(1722, Utils.setOf(
    			CreditRequest.Options.INIT_EMPLOYMENT, 
    			CreditRequest.Options.INIT_CREDIT_HISTORY,    
    			CreditRequest.Options.INIT_NEGATIVE,
    			PeopleMain.Options.INIT_PEOPLE_PERSONAL, 
    			PeopleMain.Options.INIT_ADDRESS, 
    			PeopleMain.Options.INIT_ACCOUNT, 
    			PeopleMain.Options.INIT_NEGATIVE, 
    			PeopleMain.Options.INIT_PEOPLE_CONTACT, 
    			PeopleMain.Options.INIT_PEOPLE_MISC, 
    			PeopleMain.Options.INIT_SCORING, 
    			PeopleMain.Options.INIT_SPOUSE,
    			PeopleMain.Options.INIT_NEGATIVE,
    			PeopleMain.Options.INIT_SUMMARY,
    			PeopleMain.Options.INIT_VERIF
    	));
		StringWriter wrt = new StringWriter();		
		jaxbMarshaller.marshal(ccRequest, wrt);
		sxml = wrt.toString();
		
		System.out.println(sxml);
		
		// читаем xml
		List<FileDataHolder> lstXmls = new ArrayList<FileDataHolder>(1);
		FileDataHolder fdh = new FileDataHolder();
		fdh.setData(sxml.getBytes());
		lstXmls.add(fdh);
		
		CreditRequestIteratorXml iter = new CreditRequestIteratorXml(lstXmls, kassaBean);
		while (iter.hasNext()) {
			ccRequest = iter.next();
			System.out.println(ccRequest.getId());
		}
    }
}
