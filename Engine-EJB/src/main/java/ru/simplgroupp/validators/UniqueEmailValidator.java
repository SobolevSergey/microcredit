package ru.simplgroupp.validators;

import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.PeopleContact;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Unique identity validator
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	private PeopleBeanLocal peopleBean = (PeopleBeanLocal) EJBUtil.findEJB("java:app/engine-ejb/PeopleBean!" +
	            "ru.simplgroupp.interfaces.PeopleBeanLocal");

	private UsersBeanLocal userBean = (UsersBeanLocal) EJBUtil.findEJB("java:app/engine-ejb/UsersBean!" +
	            "ru.simplgroupp.interfaces.UsersBeanLocal");

    @Override
    public void initialize(UniqueEmail uniqueIdentity) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    	 PeopleContactEntity contactEmail = peopleBean.findPeopleByContactClient(PeopleContact.CONTACT_EMAIL, email);
         if (contactEmail!=null){
        	//ищем человека в пользователях
         	UsersEntity user=userBean.findUserByPeopleId(contactEmail.getPeopleMainId().getId());
         	if (user==null){
         		contactEmail=null;
         	}
         }
         return contactEmail == null;
    }
}
