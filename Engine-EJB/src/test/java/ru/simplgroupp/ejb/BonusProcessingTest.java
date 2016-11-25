package ru.simplgroupp.ejb;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.PeopleFriend;
import ru.simplgroupp.transfer.Roles;
import ru.simplgroupp.transfer.Users;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BonusProcessingTest {

    @EJB
    UsersBeanLocal usersBean;

	@EJB
	PeopleBeanLocal peopleBean;

    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
    }

    @Test
    public void testInviteFriend() throws KassaException {
    	
    	Users auser = usersBean.findUserByLogin("ivanov@1.ru", Utils.setOf());
    	peopleBean.saveFriend(auser.getPeopleMain().getId(), "Сидоров", "Петр", "petr1970@mail.ru",null,false,PeopleFriend.FOR_BONUS);
    	//usersBean.inviteFriend(auser, "Сидоров", "Петр", "petr1970@mail.ru");

        RolesEntity r = usersBean.getRole(Roles.ROLE_DECISION).getEntity();
        RolesEntity r1 = usersBean.getRole(Roles.ROLE_CALL).getEntity();
        List<RolesEntity> lst = new ArrayList<RolesEntity>(0);
        lst.add(r);
        lst.add(r1);
        UsersEntity user = usersBean.addUser(null, "Сидоров", "Семен", "Петрович", Users.USER_SPECIALIST, "qwerty", "123", lst);
        
        
    }

}
