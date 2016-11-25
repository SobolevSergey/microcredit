package ru.simplgroupp.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CallBackEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.PeopleContact;

public class TestPeopleBean {

	@EJB
	PeopleBeanLocal peopleBean;
	
	@Before
	public void setUp() throws Exception {
	     System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
	        final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));

	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);
	}
	
	//@Test
	public void testClientContact(){
		PeopleContactEntity people=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, 306);
		if (people!=null){
			 System.out.println("phone - "+people.getValue());
		}
	}
	
	//@Test
	public void testCallBack() throws Exception{
		CallBackEntity newCallBack=peopleBean.newCallBack("Иванов","Иван",
				"79001234567","eee@rrr.ttt",null,null,1) ;
		System.out.println(newCallBack.getId());
	}
	
	@Test
	public void testAddressArchive(){
		AddressEntity address=peopleBean.findLastArchiveAddress(28938, FiasAddress.REGISTER_ADDRESS);
		if (address!=null){
		    System.out.println(address.getId());
		}
		Assert.assertTrue(address!=null);
	}
}
