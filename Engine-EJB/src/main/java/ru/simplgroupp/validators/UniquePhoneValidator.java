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
 * Unique phone validator
 */
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private PeopleBeanLocal peopleBean = (PeopleBeanLocal) EJBUtil.findEJB("java:app/engine-ejb/PeopleBean!" +
            "ru.simplgroupp.interfaces.PeopleBeanLocal");

    private UsersBeanLocal userBean = (UsersBeanLocal) EJBUtil.findEJB("java:app/engine-ejb/UsersBean!" +
            "ru.simplgroupp.interfaces.UsersBeanLocal");
    
    @Override
    public void initialize(UniquePhone uniquePhone) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        PeopleContactEntity contactPhone = peopleBean.findPeopleByContactClient(PeopleContact.CONTACT_CELL_PHONE, phone);
        if (contactPhone!=null){
        	//ищем человека в пользователях
        	UsersEntity user=userBean.findUserByPeopleId(contactPhone.getPeopleMainId().getId());
        	if (user==null){
        		contactPhone=null;
        	}
        }
        return contactPhone == null;
    }
}
