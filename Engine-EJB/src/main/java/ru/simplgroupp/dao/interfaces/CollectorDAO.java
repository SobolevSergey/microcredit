package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.CollectorEntity;
import ru.simplgroupp.persistence.TaskEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.transfer.Collector;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Task;
import ru.simplgroupp.transfer.Users;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Local
public interface CollectorDAO extends ListContainerDAO<Collector>{
	/**
	 * возвращает запись у коллектора
	 * @param id - id записи
	 * @return
	 */
	public CollectorEntity getCollectorEntity(int id);
	/**
	 * возвращает запись у коллектора
	 * @param id - id записи
	 * @param options - что инициализируем
	 * @return
	 */
	public Collector getCollector(int id,Set options);
	/**
	 * сохраняем запись у коллектора
	 * @param collector - запись у коллектора
	 * @return
	 */
	public CollectorEntity saveCollectorEntity(CollectorEntity collector);
	/**
	 * новая запись для коллектора
	 * 
	 * @param peopleMainId - id человека
	 * @param creditId - id кредита
	 * @param userId - id пользователя
	 * @param collectionTypeId - id вида коллектора
	 * @param databeg - дата начала 
	 * @param isActive - активность
	 * @param comment - комментарии
	 * @return
	 */
	public CollectorEntity newCollectorRecord(Integer peopleMainId,Integer creditId,Integer userId,
			String collectionTypeId,Date databeg,Integer isActive,String comment);
	/**
	 * сохраняем запись для коллектора с пользователем и статусом
	 * @param collector - запись для коллектора
	 * @param userId - id пользователя
	 * @param isActive - активность
	 * @param eventCode - код события
	 * @return
	 */
	public Collector saveCollectorRecordWithUser(Collector collector,Integer userId,Integer isActive, Integer eventCode);

	/**
	 * Список коллекторов в системе
	 *
	 * @return список коллекторов
	 */
	List<UsersEntity> getCollectorList();

	/**
	 * Метод получает ранее переданые кредиты коллекторам, сортированым от агрессивного к легким
	 *
	 * @param peopleMainID ID заёмщика
	 * @return список работы коллекторов
	 */
	List<CollectorEntity> getDelaysBefore(Integer peopleMainID);

	/**
	 * Метод меняет статус кредита находящегося у коллектора
	 * @param creditID ID кредита
	 * @param peopleMainID кому принадлежит кредит
	 * @param isActive статус
	 */
	void changeCollectorStatus(Integer creditID, Integer peopleMainID, Integer isActive);

	/**
	 * Метод удаляет активные задания по ID кредита
	 *
	 * @param creditID ID кредита
	 */
	void removeTasksByCreditID(Integer creditID);

	/**
	 * Метод создает задание коллектору
	 *
	 * @param eventDate дата выполнения
	 * @param userID ID пользователя
	 * @param peopleMainID ID человека
	 * @param collectorID ID записи с коллектора
	 * @param typeID тип задачи
	 * @param status статус задачи
	 * @return задача коллектора
	 */
	Task createCollectorTask(Date eventDate, Integer userID, Integer peopleMainID, Integer collectorID, Integer typeID, Integer status);

	/**
	 * Метод создает задание коллектору
	 *
	 * @param eventDate       дата выполнения
	 * @param creditRequestID ID кредитной заявки
	 * @param creditID        ID кредита
	 * @param userID          ID пользователя
	 * @param peopleMainID    ID человека
	 * @param collectorID     ID записи с коллектора
	 * @param typeID          тип задачи
	 * @param status          статус задачи
	 * @return задача коллектора
	 */
	Task createCollectorTask(Date eventDate, Integer creditRequestID, Integer creditID, Integer userID, Integer peopleMainID, Integer collectorID, Integer typeID, Integer status);

	/**
	 * Метод достает список заданий для коллектора
	 *
	 * @param eventDate дата задания
	 * @param userID    ID пользователя
	 * @param options   инициализация
	 * @param status    статус
	 * @return список задач
	 */
	List<Task> getListCollectorTasks(Date eventDate, Integer userID, Integer status, Set options);

	/**
	 * Метод меняет статус у задачи
	 *
	 * @param userID   ID пользователя
	 * @param peopleID ID клиента
	 * @param status   статус
	 * @param endDate  дата закрытия, null если ненадо
	 */
	void changeTaskStatus(Integer userID, Integer peopleID, Integer status, Date endDate);

	/**
	 * Метод достает список заданий для коллектора-супервизора
	 *
	 * @param status  статус
	 * @param options инициализация
	 * @return список задач
	 */
	List<Task> getListCollectorTasks(Integer status, Set options);

	/**
	 * Метод возвращает задание по ID
	 *
	 * @param taskID  ID задания
	 * @param options инициализация
	 * @return задание
	 */
	Task getCollectorTask(Integer taskID, Set options);

	/**
	 * Метод возвращает задание по ID
	 *
	 * @param taskID ID задания
	 * @return прокси-объект задание
	 */
	TaskEntity getTaskEntity(Integer taskID);

	/**
	 * Закрыть запись у коллектора
	 *
	 * @param collectorID ID записи
	 */
	void closeCollector(Integer collectorID);

	/**
	 * Метод назначает задание на коллектора
	 *
	 * @param collectorID ID коллектора
	 * @param taskID      ID задания
	 */
	void assignTaskToCollector(Integer userID, Integer peopleMainID, Integer collectorID);

	/**
	 * Метод назначает задание на коллектора
	 *
	 * @param collectorID ID коллектора
	 * @param reqID       ID заявки
	 */
	Collector assignRequestToCollector(Integer collectorID, Integer reqID);

	/**
	 * Метод изменяет вид коллекторской деятельности
	 *
	 * @param collectorID ID коллекторской записи
	 * @param typeID тип деятельности
	 */
	void changeCollectorCollectionType(Integer collectorID, Integer typeID);
	
	/**
	 * удаляем запись у коллектора
	 * @param peopleMainId - id человека
	 * @param creditId - id кредита
	 */
	void removeCollectorRecord(Integer peopleMainId,Integer creditId);
	/**
	 * ищем запись у коллектора
	 * @param peopleMainId - id человека
	 * @param creditId - id кредита
	 * @param isActive - активный статус
	 */
	CollectorEntity findCollectorRecord(Integer peopleMainId,Integer creditId,Integer isActive);
	
	/**
	 * ищем активную задачу по id человека
	 * @param peopleMainId - id человека
	 * @return
	 */
	TaskEntity findActiveTaskByPeople(Integer peopleMainId);
	/**
	 * удаляем задачу по id 
	 * @param id - id задачи
	 */
	void removeTask(Integer id);

	/**
	 * Метод устанавливает у записей в records исполнителя toCollectorID
	 *
	 * @param toCollectorID ID коллектора кому передаем записи
	 * @param ids           записи
	 */
	void passItemsToCollector(Integer toCollectorID, List<Integer> ids);

	/**
	 * Метод передает задачи выбранному коллектору
	 *
	 * @param toCollectorID кому передаем задачи
	 * @param collIDs       ID коллекторких записей, чьи задания передаем
	 */
	void passTasksToCollector(Integer toCollectorID, List<Integer> collIDs);

	/**
	 * Метод передает задачи от одного коллектора другому
	 *
	 * @param fromCollectorID с кого берем задачи
	 * @param toCollectorID   кому передаем задачи
	 */
	void passTasksFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID);

	/**
	 * Метод обновляет все записи fromCollectorID в toCollectorID
	 *
	 * @param fromCollectorID коллектор чьи записи передаем
	 * @param toCollectorID   коллектор кому передаем все записи
	 */
	void passFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID);
}
