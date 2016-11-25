package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.CallsDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.CallResultEntity;
import ru.simplgroupp.persistence.CallsEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.transfer.*;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CallsDAOImpl implements CallsDAO {

	@Inject Logger log;
	
    @EJB
    PeopleDAO peopleDAO;

    @EJB
    ReferenceBooksLocal refBook;

    @EJB
    UsersDAO userDAO;

    @EJB
    EventLogService eventLog;
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Override
    public CallsEntity getCallsEntity(Integer id) {
        return emMicro.find(CallsEntity.class, id);
    }

    @Override
    public CallResultEntity getCallResult(Integer id) {
        return emMicro.find(CallResultEntity.class, id);
    }

    @Override
    public void removeCall(Integer id) {
        Query qry = emMicro.createNamedQuery("removeCall");
        qry.setParameter("id", id);
        qry.executeUpdate();

    }

    @Override
    public CallsEntity saveCallsEntity(CallsEntity call) {
        CallsEntity callNew = emMicro.merge(call);
        emMicro.persist(callNew);
        return callNew;
    }

    @Override
    public CallsEntity newCall(Integer peopleMainId, Integer userId,
                               Integer statusId, Date databeg, Date dataend, String phone,
                               Integer isClientCall, boolean incoming, String comment, byte[] callData) throws KassaException {
        if (peopleMainId == null && userId == null) {
            log.severe("Не найден человек и оператор для записи звонка");
            throw new KassaException("Не найден человек и оператор для записи звонка");
        }
        CallsEntity call = new CallsEntity();
        if (peopleMainId != null) {
            call.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
        }
        if (userId != null) {
            call.setUserId(userDAO.getUsersEntity(userId));
        }
        if (statusId != null) {
            call.setStatusId(refBook.findByCodeIntegerEntity(RefHeader.CALL_STATUS, statusId));
        }
        call.setCalldatabeg(databeg);
        call.setCalldataend(dataend);
        call.setCalldata(callData);
        call.setPhone(phone);
        call.setComment(comment);
        call.setIsClientCall(isClientCall);
        call.setIncoming(incoming);
        emMicro.persist(call);
        return call;
    }

    @Override
    public CallResultEntity newCallResult(Integer peopleMainId, Integer userId,
                                          Integer resultTypeId, Date dateCall, Date datePromise,
                                          Date dateNextContact, String comment) throws KassaException {
        if (peopleMainId == null || userId == null) {
            log.severe("Не найден человек или оператор для записи результата");
            throw new KassaException("Не найден человек или оператор для записи результата");
        }
        PeopleMainEntity people = peopleDAO.getPeopleMainEntity(peopleMainId);

        CallResultEntity callResult = new CallResultEntity();
        callResult.setPeopleMainId(people);
        callResult.setUserId(userDAO.getUsersEntity(userId));
        callResult.setResultTypeId(refBook.findByCodeIntegerEntity(RefHeader.CALL_RESULT_TYPE, resultTypeId));
        callResult.setDateCall(dateCall);
        callResult.setDatePromise(datePromise);
        callResult.setDateNextContact(dateNextContact);
        callResult.setComment(comment);
        emMicro.persist(callResult);
        eventLog.saveLog(EventType.INFO, EventCode.COLLECTOR_CALL_RESULT, "Создание результата обзвона коллектором", userId,
                dateCall, null, people, null);
        return callResult;
    }

    @Override
    public List<CallResult> getCallResultsByPeopleAndUserID(Integer peopleMainID, Integer userID) {
        TypedQuery qry = emMicro.createNamedQuery("callResultsByPeopleAndUserID", CallResultEntity.class);
        qry.setParameter("peopleMainID", peopleMainID);
        qry.setParameter("userID", userID);
        List<CallResultEntity> entities = qry.getResultList();
        List<CallResult> result = new ArrayList<>();
        for (CallResultEntity entity : entities) {
            CallResult call = new CallResult(entity);
            result.add(call);
        }
        return result;
    }

    @Override
    public List<CallResult> getCallResultsByPeople(Integer peopleMainID) {
        TypedQuery qry = emMicro.createNamedQuery("callResultsByPeople", CallResultEntity.class);
        qry.setParameter("peopleMainID", peopleMainID);
        List<CallResultEntity> entities = qry.getResultList();
        List<CallResult> result = new ArrayList<>();
        for (CallResultEntity entity : entities) {
            CallResult call = new CallResult(entity);
            result.add(call);
        }
        return result;
    }

    @Override
    public List<Call> getCallsByPeopleAndUserID(Integer peopleMainID, Integer userID) {
        TypedQuery qry = emMicro.createNamedQuery("callsByPeopleAndUserID", CallsEntity.class);
        qry.setParameter("peopleMainID", peopleMainID);
        qry.setParameter("userID", userID);
        List<CallsEntity> entities = qry.getResultList();
        List<Call> result = new ArrayList<>();
        for (CallsEntity entity : entities) {
            Call call = new Call(entity);
            result.add(call);
        }
        return result;
    }

    @Override
    public List<Call> getCallsByPeople(Integer peopleMainId, Set options) {
        Query qry = emMicro.createNamedQuery("callsByPeople", CallsEntity.class);
        qry.setParameter("peopleMainId", peopleMainId);
        List<CallsEntity> entities = qry.getResultList();
        List<Call> result = new ArrayList<>();
        for (CallsEntity entity : entities) {
            Call call = new Call(entity);
            call.init(options);
            result.add(call);
        }
        return result;
    }
}
