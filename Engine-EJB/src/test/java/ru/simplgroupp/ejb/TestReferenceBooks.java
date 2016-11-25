package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.transfer.RefHeader;

public class TestReferenceBooks {
	
	@EJB
	ReferenceBooksLocal refBooks;
	
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }   
    
    @Test
    public void testFind1() {
    	refBooks.findByCodeInteger(RefHeader.PAYMENT_TYPE, 1);
    	refBooks.listReference(RefHeader.PAYMENT_TYPE);
    	refBooks.findByCodeIntegerEntity(RefHeader.PAYMENT_TYPE,1);
    	refBooks.findByCodeEntity(RefHeader.PAYMENT_TYPE,"a");
    	refBooks.getCreditStatusTypes();
    	refBooks.getPartners();
    }
    
    // @Test
    public void testRefConvert() throws Exception
    {
	 ReferenceEntity rf=refBooks.findFromKb(44, 0);
	 System.out.println(rf.getRefHeaderId().getId()+" "+rf.getName()+" "+rf.getCodeinteger());
	 ReferenceEntity ref=refBooks.findByCodeIntegerMain(5, 1);
	 System.out.println(ref.getRefHeaderId().getId()+" "+ref.getName()+" "+ref.getCodeinteger());
        ReferenceEntity reff=refBooks.getReferenceEntity(173);
        PartnersEntity par=refBooks.getPartnerFromLink(reff);
        System.out.println(par.getName());
    }
}
