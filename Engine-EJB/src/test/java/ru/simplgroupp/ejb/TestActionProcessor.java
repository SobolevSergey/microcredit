package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;

@LocalClient
public class TestActionProcessor {

    @EJB
    ActionProcessorBeanLocal actProc;
    
    @EJB
    ProductBeanLocal productBean;
    
    @EJB
    PeopleDAO peopleDAO;
    
    @EJB 
    CreditDAO creditDAO;
    
    @Before
    public void setUp() throws Exception {
    	System.setProperty("openejb.validation.output.level","VERBOSE");
		final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
	
        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
        
        actProc = (ActionProcessorBeanLocal) context.lookup("java:global/Engine-EJB/ActionProcessorBean!ru.simplgroupp.interfaces.ActionProcessorBeanLocal");
    }     
    
   // @Test
    public void testPlugins() throws Exception {
    	Assert.assertNotNull(actProc);
    	actProc.test(1);
    }
    
    @Test
    public void testEmailCommon(){
    	String formattedTextSubject ="";
    	String formattedTextBody="";
    	ProductMessagesEntity message=productBean.getMessageForBusinessAction(1, "msgOverdueEmail");
		if (message!=null) {
			formattedTextSubject = message.getSubject();
			formattedTextBody = message.getBody();
		}	
		PeopleMain peopleMain = peopleDAO.getPeopleMain(6245, Utils.setOf(
				PeopleMain.Options.INIT_PEOPLE_PERSONAL
		));
		Credit credit=creditDAO.getCredit(25188, null);
		//стандартные параметры
		Object[] params = new Object[] {
				//ФИО
				peopleMain.getPeoplePersonalActive().getFullName(),
				//номер договора
				credit.getCreditAccount(),
				//сумма к возврату
				CalcUtils.dformat_XX.format(credit.getCreditSumBack()),
				//сумма основного долга
				CalcUtils.dformat_XX.format(credit.getSumMainRemain()),
				//сумма процентов
				CalcUtils.dformat_XX.format((credit.getCreditSumBack() - credit.getSumMainRemain())),
				//текущая дата
			 	DatesUtils.DATE_FORMAT_ddMMYYYY.format(new Date())
		};

		actProc.sendEmailCommon(6245, formattedTextSubject, formattedTextBody, params);
    }
}
