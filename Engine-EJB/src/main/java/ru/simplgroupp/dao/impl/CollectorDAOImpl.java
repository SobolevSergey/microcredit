package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.ListContainer;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CollectorDAOImpl implements CollectorDAO {

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;	
	
	@EJB
	PeopleDAO peopleDAO;
	
	@EJB
	CreditDAO creditDAO;

	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
	ReferenceBooksLocal refBook;
	
	@EJB
	UsersDAO userDAO;

	@EJB
	EventLogService eventLog;
	
	private static HashMap<String, Object> collectorSortMapping = new HashMap<String, Object>(4);	
    static {
    	collectorSortMapping.put("databeg", "c.databeg");
    }		
    
    @Inject Logger logger;
	
	@Override
	public int countData(ListContainer<Collector> listCon) {
		int nCount = listCon.calcCount(emMicro);
		return nCount;
	}

	@Override
	public List<Collector> listData(int nFirst, int nCount,
			ListContainer<Collector> listCon) {
		return listCon.calcList(nFirst, nCount, emMicro);
	}

	@Override
	public List<? extends Number> listIds(int nFirst, int nCount,
			ListContainer<Collector> listCon) {
		return listCon.calcIds(nFirst, nCount, emMicro);
	}

	@Override
	public <T extends ListContainer<? extends Identifiable>> T genData(
			Class<T> clz) {
		try {
			T lstCon = clz.newInstance();
			lstCon.setSortMapping(collectorSortMapping);
			lstCon.setItemClass(Collector.class);
			return lstCon;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Не создали список " + clz.getName(), e);
			return null;
		}
	}

	@Override
	public CollectorEntity getCollectorEntity(int id) {
		return emMicro.find(CollectorEntity.class, id);
	}

	@Override
	public Collector getCollector(int id, Set options) {
		CollectorEntity collectorEntity=getCollectorEntity(id);
		if (collectorEntity!=null){
			Collector collector=new Collector(collectorEntity);
			if (options!=null){
				collector.init(options);
			}
			return collector;
		}
		return null;
	}

	@Override
	public CollectorEntity saveCollectorEntity(CollectorEntity collector) {
		CollectorEntity collectorNew=emMicro.merge(collector);
		emMicro.persist(collectorNew);
		return collectorNew;
	}

	@Override
	public CollectorEntity newCollectorRecord(Integer peopleMainId,
			Integer creditId, Integer userId, String collectionTypeId,
			Date databeg, Integer isActive, String comment) {
		CollectorEntity collector=new CollectorEntity();
		if (peopleMainId!=null){
			collector.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		}
		if (creditId!=null){
			collector.setCreditId(creditDAO.getCreditEntity(creditId));
		}
		if (collector.getCreditId()==null||collector.getPeopleMainId()==null){
			logger.severe("Не удалось найти кредит "+creditId+" или человека "+peopleMainId+" по id");
			return null;
		}
	    if (userId!=null){
	    	collector.setUserId(userDAO.getUsersEntity(userId));
	    }
	    if (collectionTypeId!=null){
	    	collector.setCollectionTypeId(refBook.findByCodeEntity(RefHeader.COLLECTOR_TYPE, collectionTypeId));
	    }
	    collector.setDatabeg(databeg);
	    collector.setIsActive(isActive);
	    collector.setComment(comment);
	    emMicro.persist(collector);
		return collector;
	}

	@Override
	public Collector saveCollectorRecordWithUser(Collector collector,
			Integer userId, Integer isActive, Integer eventCode) {
		CollectorEntity collectorEntity=getCollectorEntity(collector.getId());
		//если отдаем пользователю
		if (userId!=null){
			collectorEntity.setUserId(userDAO.getUsersEntity(userId));
		} else {
			//если отдаем в общие задачи
			collectorEntity.setUserId(null);
		}
		collectorEntity.setIsActive(isActive);
		collectorEntity=saveCollectorEntity(collectorEntity);

		PeopleMainEntity people = peopleDAO.getPeopleMainEntity(collector.getPeopleMain().getId());
		if (eventCode != null) {
			try {
				eventLog.saveLog(EventType.INFO, eventCode, "Взятие клиента коллектором", userId,
                        new Date(), null, people, null);
			} catch (KassaException e) {
				logger.severe("Не удалось сохранить лог" + e.getMessage());
			}
		}

		return new Collector(collectorEntity);
	}

	@Override
	public List<UsersEntity> getCollectorList() {
		return emMicro.createNamedQuery("collectorList", UsersEntity.class).getResultList();
	}

	@Override
	public List<CollectorEntity> getDelaysBefore(Integer peopleMainID) {
		return JPAUtils.getResultList(emMicro, "listDelaysBefore", Utils.mapOf("peopleMainID", peopleMainID));
	}

	@Override
	public void changeCollectorStatus(Integer creditID, Integer peopleMainID, Integer isActive) {
		Query qry = emMicro.createNamedQuery("changeCreditStatus");
		qry.setParameter("creditID", creditID);
		qry.setParameter("peopleMainID", peopleMainID);
		qry.setParameter("isActive", isActive);
		qry.executeUpdate();
	}

	@Override
	public void removeTasksByCreditID(Integer creditID) {
		Query qry = emMicro.createNamedQuery("removeTasksByCreditID");
		qry.setParameter("creditID", creditID);
		qry.executeUpdate();
	}

	@Override
	public Task createCollectorTask(Date eventDate, Integer creditRequestID, Integer creditID, Integer userID, Integer peopleMainID, Integer collectorID, Integer typeID, Integer status) {
		TaskEntity collectorTask = new TaskEntity();
		collectorTask.setEventDate(eventDate);
		collectorTask.setUserId(userDAO.getUsersEntity(userID));
		collectorTask.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainID));
		collectorTask.setCollectorId(getCollectorEntity(collectorID));
		collectorTask.setTaskType(refBook.findByCodeIntegerEntity(RefHeader.TASK_TYPE, typeID));
		collectorTask.setStatus(status);
		if (creditID != null) {
			collectorTask.setCreditId(creditDAO.getCreditEntity(creditID));
		}
		if (creditRequestID != null) {
			collectorTask.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestID));
		}

		emMicro.persist(collectorTask);
		return new Task(collectorTask);
	}

	@Override
	public Task createCollectorTask(Date eventDate, Integer userID, Integer peopleMainID, Integer collectorID, Integer typeID, Integer status) {
		return createCollectorTask(eventDate, null, null, userID, peopleMainID, collectorID, typeID, status);
	}

	@Override
	public List<Task> getListCollectorTasks(Date eventDate, Integer userID, Integer status, Set options) {
		List<TaskEntity> entityList = JPAUtils.getResultList(emMicro,"listCollectorTasks", Utils.mapOf("userID", userID,
				"eventDate", eventDate,
				"status", status));
		List<Task> result = new ArrayList<>();
		for (TaskEntity entity : entityList) {
			Task task = new Task(entity);
			task.init(options);
			result.add(task);
		}
		return result;
	}

	@Override
	public void changeTaskStatus(Integer userID, Integer peopleID, Integer status, Date endDate) {
		Query qry = JPAUtils.createNamedQuery(emMicro, "changeTaskStatus", Utils.mapOf("userID", userID,
				"peopleID", peopleID,
				"status", status,
				"endDate", endDate));
		qry.executeUpdate();
	}

	@Override
	public List<Task> getListCollectorTasks(Integer status, Set options) {
		List<TaskEntity> enities = JPAUtils.getResultList(emMicro, "listCollectorTasksByStatus", Utils.mapOf("status", status));
		List<Task> result = new ArrayList<>();
		for (TaskEntity entity : enities) {
			Task task = new Task(entity);
			task.init(options);
			result.add(task);
		}

		return result;
	}

	@Override
	public Task getCollectorTask(Integer taskID, Set options) {
		TaskEntity entity = getTaskEntity(taskID);
		Task result = new Task(entity);
		result.init(options);
		return result;
	}

	@Override
	public TaskEntity getTaskEntity(Integer taskID) {
		return emMicro.find(TaskEntity.class, taskID);
	}

	public TaskEntity findTaskByCollectorID(Integer collectorID) {
		return (TaskEntity) JPAUtils.getSingleResult(emMicro, "findTaskByCollectorID", Utils.mapOf("collectorID", collectorID));
	}

	@Override
	public void closeCollector(Integer collectorID) {
		CollectorEntity entity = getCollectorEntity(collectorID);
		entity.setDataend(new Date());
		entity.setIsActive(ActiveStatus.ARCHIVE);
		emMicro.persist(entity);
	}

	@Override
	public void assignTaskToCollector(Integer userID, Integer peopleMainID, Integer collectorID) {
		Query qry = emMicro.createNamedQuery("assignTaskToCollector");
		qry.setParameter("user", userDAO.getUsersEntity(userID));
		qry.setParameter("peopleMainID", peopleMainID);
		qry.setParameter("collectorID", collectorID);
		qry.setParameter("status", ActiveStatus.ACTIVE);
		qry.executeUpdate();
	}

	@Override
	public Collector assignRequestToCollector(Integer collectorID, Integer reqID) {
		CollectorEntity collector = getCollectorEntity(reqID);
		collector.setDataend(new Date());
		collector.setIsActive(ActiveStatus.ARCHIVE);
		emMicro.persist(collector);

		CollectorEntity newCollector = new CollectorEntity();
		newCollector.setComment(collector.getComment());
		newCollector.setUserId(userDAO.getUsersEntity(collectorID));
		newCollector.setIsActive(ActiveStatus.DRAFT);
		newCollector.setCollectionTypeId(collector.getCollectionTypeId());
		newCollector.setPeopleMainId(collector.getPeopleMainId());
		newCollector.setCreditId(collector.getCreditId());
		newCollector.setDatabeg(new Date());
		emMicro.persist(newCollector);

		assignTaskToCollector(collectorID, collector.getPeopleMainId().getId(), reqID);
		return new Collector(newCollector);
	}

	@Override
	public void changeCollectorCollectionType(Integer collectorID, Integer typeID) {
		CollectorEntity entity = getCollectorEntity(collectorID);
		ReferenceEntity type = refBook.findByCodeIntegerEntity(RefHeader.COLLECTOR_TYPE, typeID);
		entity.setCollectionTypeId(type);
		emMicro.persist(entity);
	}

	@Override
	public void removeCollectorRecord(Integer peopleMainId, Integer creditId) {
		Query qry = emMicro.createNamedQuery("removeCollectorRecord");
		qry.setParameter("peopleMainId", peopleMainId);
		qry.setParameter("creditId", creditId);
		qry.executeUpdate();
	}

	@Override
	public CollectorEntity findCollectorRecord(Integer peopleMainId, Integer creditId,Integer isActive) {
		return (CollectorEntity) JPAUtils.getSingleResult(emMicro, "findCollectorRecord",Utils.mapOf("peopleMainId", peopleMainId,
				"creditId", creditId,
				"isActive",isActive));
		
	}

	@Override
	public TaskEntity findActiveTaskByPeople(Integer peopleMainId) {
		return (TaskEntity) JPAUtils.getSingleResult(emMicro, "findActiveTaskByPeople", Utils.mapOf("peopleMainId", peopleMainId,
				"status",ActiveStatus.ACTIVE));
	}

	@Override
	public void removeTask(Integer id) {
		Query qry = emMicro.createNamedQuery("removeTask");
		qry.setParameter("id", id);
		qry.executeUpdate();
	}

	@Override
	public void passItemsToCollector(Integer toCollectorID, List<Integer> recordIDs) {
		Query qry = emMicro.createNamedQuery("passItemsToCollector");
		qry.setParameter("toCollectorID", toCollectorID);
		qry.setParameter("recordIDs", recordIDs);
		qry.executeUpdate();
	}

	@Override
	public void passTasksToCollector(Integer toCollectorID, List<Integer> collIDs) {
		Query qry = emMicro.createNamedQuery("passTasksToCollector");
		qry.setParameter("toCollectorID", toCollectorID);
		qry.setParameter("collIDs", collIDs);
		qry.executeUpdate();
	}

	@Override
	public void passTasksFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID) {
		Query qry = emMicro.createNamedQuery("passTasksFromOneCollectorToAnother");
		qry.setParameter("fromCollectorID", fromCollectorID);
		qry.setParameter("toCollectorID", toCollectorID);
		qry.executeUpdate();
	}

	@Override
	public void passFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID) {
		Query qry = emMicro.createNamedQuery("passFromOneCollectorToAnother");
		qry.setParameter("fromCollectorID", fromCollectorID);
		qry.setParameter("toCollectorID", toCollectorID);
		qry.executeUpdate();
	}
}
