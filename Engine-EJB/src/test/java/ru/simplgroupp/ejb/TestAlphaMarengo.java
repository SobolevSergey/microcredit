package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.AlfaBankMarengoBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.ProcessKeys;

@LocalClient
public class TestAlphaMarengo {

    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    WorkflowEngineBeanLocal wfEng;    
    
    @EJB
    WorkflowBeanLocal wfBean;    
    
    @Before
    public void setUp() throws Exception {
    	System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");
		
		final Properties p = new Properties();
        p.put("CryptosignDS", "new://Resource?type=DataSource");
        p.put("CryptosignDS.JdbcDriver", "org.postgresql.Driver");
        p.put("CryptosignDS.JdbcUrl", "jdbc:postgresql://localhost:5432/criptosign?user=sa&password=drakon");			
		
        p.put("FiasDS", "new://Resource?type=DataSource");
        p.put("FiasDS.JdbcDriver", "org.postgresql.Driver");
//        p.put("RSmevDS.JdbcUrl", "jdbc:postgresql://localhost:5432/rsmev?user=postgres&password=anton4");
        p.put("FiasDS.JdbcUrl", "jdbc:postgresql://localhost:5432/kladr?user=sa&password=drakon");        
		
        p.put("MicroDS", "new://Resource?type=DataSource");
        p.put("MicroDS.JdbcDriver", "org.postgresql.Driver");
//        p.put("CryptoDS.JdbcUrl", "jdbc:postgresql://localhost:5432/crypto?user=postgres&password=anton4");
        p.put("MicroDS.JdbcUrl", "jdbc:postgresql://localhost:5432/micro?user=sa&password=drakon");
           
        
        p.put("DesignDS", "new://Resource?type=DataSource");
        p.put("DesignDS.JdbcDriver", "org.postgresql.Driver");
        p.put("DesignDS.JdbcUrl", "jdbc:postgresql://localhost:5432/design?user=sa&password=drakon");
//        p.put("KladrDS.JdbcUrl", "jdbc:postgresql://localhost:5432/kladr?user=postgres&password=anton4");
             
        p.put("BpmDS", "new://Resource?type=DataSource");
        p.put("BpmDS.JdbcDriver", "org.postgresql.Driver");
//        p.put("CryptoDS.JdbcUrl", "jdbc:postgresql://localhost:5432/crypto?user=postgres&password=anton4");
        p.put("BpmDS.JdbcUrl", "jdbc:postgresql://localhost:5432/microbpm?user=sa&password=drakon");        
        
        p.put("hibernate.show_sql", "true");
        p.put("hibernate.format_sql", "true");
        
//        p.put("jboss/TransactionManager", value);
        
        p.setProperty(Context.INITIAL_CONTEXT_FACTORY,  "org.apache.openejb.client.LocalInitialContextFactory");
//        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");
        p.put("log4j.rootLogger", "fatal,C");
        p.put("log4j.category.OpenEJB", "warn");
        p.put("log4j.category.OpenEJB.options", "info");
        p.put("log4j.category.OpenEJB.server", "info");
        p.put("log4j.category.OpenEJB.startup", "info");
        p.put("log4j.category.OpenEJB.startup.service", "warn");
        p.put("log4j.category.OpenEJB.startup.config", "info");
        p.put("log4j.category.OpenEJB.hsql", "info");
        p.put("log4j.category.CORBA-Adapter", "info");
        p.put("log4j.category.Transaction", "warn");
        p.put("log4j.category.org.apache.activemq", "error");
        p.put("log4j.category.org.apache.geronimo", "error");
        p.put("log4j.category.openjpa", "error");
        p.put("log4j.category.ru.simplgroupp", "info");
        p.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
        p.put("log4j.appender.C.layout", "org.apache.log4j.SimpleLayout");  
        
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
    	actProc.runPlugin(AlfaBankMarengoBeanLocal.SYSTEM_NAME, Payment.class.getName(), new Integer(1), null, context);
    }
}
