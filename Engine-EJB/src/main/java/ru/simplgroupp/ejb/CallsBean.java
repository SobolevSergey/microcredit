package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.CallsDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.CallsLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.persistence.CallsEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.UsersEntity;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CallsBean implements CallsLocal {
	@Inject Logger logger;

    @EJB
    private CallsDAO callsDAO;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private UsersDAO usersDAO;


    @Override
    public CallsEntity saveCall(Date startCall, Date endCall, Integer status, String phone, boolean incoming, String uuid) {
        UsersEntity user = usersDAO.findByOktellUuid(uuid);
        if (user == null) {
            logger.severe("пользователь с oktell uuid = " + uuid + " не найден");
            return null;
        }

        PeopleMainEntity peopleMain = peopleBean.findPeopleByPhone(phone);
        Integer peopleMainId = null;
        if (peopleMain != null) {
            peopleMainId = peopleMain.getId();
        }

        try {
            return callsDAO.newCall(peopleMainId, user.getId(), status, startCall, endCall, phone, 0, incoming, null, null);
        } catch (KassaException e) {
            logger.severe("ошибка сохранения звонка " + e);
            return null;
        }
    }


}
