package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.util.DatesUtils;

public class TestCreditBean {
	
	@EJB
	CreditBeanLocal credServ;
	
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    
    //@Test
    public void testLC() {
    	int n = credServ.countCredit(10, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    	Assert.assertTrue(n > 0);
    	List<Credit> lst = credServ.listCredit(0, -1, null, null, 10, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    	Assert.assertEquals(n, lst.size());
    }
    
    //@Test
	public void testListOverdue() {
		Date dt=Convertor.toDate("03.04.2015", DatesUtils.FORMAT_ddMMYYYY);
		DateRange date=new DateRange(dt,DateUtils.addDays(dt, 1));
		List<CreditDetailsEntity> overdue=credServ.findCreditDetails(14298, BaseCredit.OPERATION_OVERDUE, date);
		if (overdue.size()>0){
			System.out.println(overdue.get(0).getAmountAll()+", "+overdue.get(0).getAmountMain());
		}
	}
	
		
	//@Test
	public void testChangeStatus() throws Exception{
		credServ.creditToOuterCollector(14437, new Date());
	}
	
	//@Test
	public void creditCollector() throws Exception{
	   credServ.creditToCollector(3875);
	}
	   
	@Test
	public void calcOverdueSum(){
		credServ.saveOverdueSum(24968, new Date());
	}
	
	//@Test 
	public void testListCredit()
	{
		
		List<CreditEntity> credits=credServ.listCreditsForCalcPercent(Convertor.toDate("01.02.2015", DatesUtils.FORMAT_ddMMYYYY));
		System.out.println(credits.size());
		if (credits.size()>0){
			for (CreditEntity credit:credits){
				System.out.println(credit.getCreditdatabeg()+", "+credit.getCreditdataendfact()+" "+credit.getCreditAccount());
			}
		}
		
	}
}
