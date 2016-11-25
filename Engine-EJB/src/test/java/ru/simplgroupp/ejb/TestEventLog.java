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

import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.script.EventLogSearchScriptObject;
import ru.simplgroupp.script.SearchResult;
import ru.simplgroupp.transfer.EventLog;

public class TestEventLog {
	
	@EJB
	EventLogService logServ;
	
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
//    	System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    
    @Test
    public void testFindLatest() {
    	EventLog log = logServ.findLatestByCreditId(6, 3867);
    	Assert.assertNotNull(log);
    }
    
    // @Test
    public void testSearchLog(){
 	   EventLogSearchScriptObject eo=new EventLogSearchScriptObject(logServ);
 	   Date dt1=DateUtils.addDays(new Date(), -2);
 	   Date dt2=DateUtils.addDays(new Date(), -1);
 	   SearchResult<EventLog> sr=eo.search("eventTime.from", dt1, "eventTime.to", dt2);
 	   System.out.println(dt1.getTime());
 	   System.out.println(dt2.getTime());
 	   int i=sr.getCount();
 	   System.out.println(i);
 	   List<EventLog> el=sr.getData();
 	   int k=1;
 	   for (EventLog l:el){
 	     System.out.println(l.getCreditRequest().getId());
 	     System.out.println(l.getEventTime());
 	     System.out.println(l.getDescription());
 	     System.out.println(k);
 	     k++;
 	   }
    }
}
