package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ScoringSociohubBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.workflow.ProcessKeys;

@LocalClient
public class TestSociohub {

  @EJB
  ActionProcessorBeanLocal actProc;

  @EJB
  WorkflowEngineBeanLocal wfEng;

  @EJB
  WorkflowBeanLocal wfBean;

  @EJB
  ScoringSociohubBeanLocal sociohub;

  @EJB
  KassaBeanLocal kassa;
  
  @EJB
  CreditRequestDAO crDAO;
  
  @Before
  public void setUp() throws Exception {
    System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

    System.setProperty("openejb.validation.output.level", "VERBOSE");
    final Properties p = new Properties();
    p.load(this.getClass().getResourceAsStream("/test.properties"));

    final Context context = EJBContainer.createEJBContainer(p).getContext();
    context.bind("inject", this);

    wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
    wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
    sociohub = (ScoringSociohubBeanLocal) context.lookup("java:global/Engine-EJB/ScoringSociohubBean!ru.simplgroupp.interfaces.ScoringSociohubBeanLocal");
    kassa = (KassaBeanLocal) context.lookup("java:global/Engine-EJB/KassaBean!ru.simplgroupp.interfaces.KassaBeanLocal");		
    actProc = wfEng.getActionProcessor();
  }

  //@Test
  public void testRun() throws Exception {
    ActionContext context = actProc.createActionContext(null, true);
    wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART, new Integer(20123).toString());
    System.out.println("Запускаем плагин");
    actProc.runPlugin(ScoringSociohubBeanLocal.SYSTEM_NAME, CreditRequest.class.getName(), new Integer(20123), null, context);
    System.out.println("Готово");
  }
  
  @Test
  public void testRequest() throws Exception{
	 // CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(16);
	  CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(48093);
	  sociohub.newRequest(creditRequest, false,10);
	  System.out.println("Готово");
  }
}
