package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;

import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Roles;
import ru.simplgroupp.transfer.Users;

@LocalClient
public class TestUsers {

	 @EJB
	    KassaBeanLocal kassa;
	 @EJB
	    UsersBeanLocal user;
	 @EJB
	    ReferenceBooksLocal ref;
	 
	 
	 @Before
	    public void setUp() throws Exception {
		 System.setProperty("openejb.validation.output.level","VERBOSE");
			final Properties p = new Properties();
	        p.load(this.getClass().getResourceAsStream("/test.properties"));
		
	        final Context context = EJBContainer.createEJBContainer(p).getContext();
	        context.bind("inject", this);
	        

	        kassa = (KassaBeanLocal) context.lookup("java:global/Engine-EJB/KassaBean!ru.simplgroupp.interfaces.KassaBeanLocal");		
	        user= (UsersBeanLocal) context.lookup("java:global/Engine-EJB/UsersBean!ru.simplgroupp.interfaces.UsersBeanLocal");		
	        ref= (ReferenceBooksLocal) context.lookup("java:global/Engine-EJB/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal");		
	   	       
	    }
	 
	@Test
	public void test() throws Exception {
		RolesEntity r=user.getRole(Roles.ROLE_DECISION).getEntity();
		RolesEntity r1=user.getRole(Roles.ROLE_CALL).getEntity();
		List<RolesEntity> lst=new ArrayList<RolesEntity>(0);
		lst.add(r);
		lst.add(r1);
		UsersEntity u=user.addUser(null, "Сидоров", "Семен", "Петрович", Users.USER_SPECIALIST, "qwerty", "123", lst);
		String st=u.getPeopleMainId().getId().toString();
		System.out.print(st);
	}

	@Test
	public void testUser(){
		Users u=user.getUser(518, Utils.setOf(Users.Options.INIT_ROLES,PeopleMain.Options.INIT_PEOPLE_PERSONAL));
		String s1=u.getRolesDescription();
		String s2=u.getPeopleMain().getPeoplePersonalActive().getFullName();
		System.out.println(s1);
		System.out.println(s2);
	}
}
