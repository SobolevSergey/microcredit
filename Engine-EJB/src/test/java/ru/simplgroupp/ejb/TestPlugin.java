package ru.simplgroupp.ejb;

import java.util.Collections;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.ejb.plugins.payment.ContactPayPluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.ContactPayBeanLocal;
import ru.simplgroupp.workflow.PluginExecutionContext;

@LocalClient
public class TestPlugin {
    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    ServiceBeanLocal servBean;
    
    @Before
    public void setUp() throws Exception {
/*
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
        System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties.aro"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
*/


        System.setProperty("openejb.validation.output.level","VERBOSE");
        final Properties p = new Properties();
        //p.load(this.getClass().getResourceAsStream("/test.properties"));
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
//       Context context = new InitialContext(p);
        context.bind("inject", this);
    }  
    
//    @Test
    public void testUpload() throws Exception {
    	actProc.uploadScoringEquifax1();
    }
    
    @Test
    public void testConfig() throws Exception {
    	ActionContext actCtx = servBean.createActionContext(null, true); 
    	PluginConfig cfg = actCtx.getPlugins().getEquifax().getWorkConfig();
    	Assert.assertTrue(cfg instanceof EquifaxPluginConfig);
    }
}
