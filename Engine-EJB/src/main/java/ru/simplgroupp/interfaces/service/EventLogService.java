package ru.simplgroupp.interfaces.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventLog;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.toolkit.common.DateRange;

public interface EventLogService {

  /**
   * список логов действия системы - для поиска в веб-интерфейсе
   * @param options - 
   * @param userId - пользователь
   * @param roleId - роль пользователя
   * @param usertypeId - вид пользователя
   * @param surname - фамилия
   * @param crequestNumber - № заявки 
   * @param crequestId - заявка
   * @param eventCode - код события
   * @param description - описание
   * @param os - ОС
   * @param browser - браузер
   * @param geoPlace - место нахождения
   * @param dateEvent - дата события
   */
  List<EventLog> listLogs(int nFirst, int nCount, Set options,Integer userId,Integer roleId,Integer usertypeId,
		  String surname, String crequestNumber, Integer crequestId,
		  Integer eventCode,String description, String os,String browser, 
		  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress);
  /**
   * список логов действия системы - для поиска в скрипте
   * @param options - 
   * @param userId - пользователь
   * @param roleId - роль пользователя
   * @param usertypeId - вид пользователя
   * @param surname - фамилия
   * @param crequestNumber - № заявки 
   * @param crequestId - заявка
   * @param eventCode - код события
   * @param description - описание
   * @param os - ОС
   * @param browser - браузер
   * @param geoPlace - место нахождения
   * @param dateEvent - дата события
   */
  List<EventLog> listLogsTime(int nFirst, int nCount, Set options,Integer userId,Integer roleId,Integer usertypeId,
		  String surname, String crequestNumber, Integer crequestId,
		  Integer eventCode,String description, String os,String browser, 
		  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress);
  /**
   * сколько было логов действий системы - для поиска в веб-интерфейсе
   * @param userId - пользователь
   * @param roleId - роль пользователя
   * @param usertypeId - вид пользователя
   * @param surname - фамилия
   * @param crequestNumber - № заявки 
   * @param crequestId - заявка
   * @param eventCode - код события
   * @param description - описание
   * @param os - ОС
   * @param browser - браузер
   * @param geoPlace - место нахождения
   * @param dateEvent - дата события
   */
  int countLogs(Integer userId,Integer roleId,Integer usertypeId,
		  String surname, String crequestNumber, Integer crequestId,
		  Integer eventCode,String description, String os,String browser, 
		  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress);
  
  /**
   * сколько было логов действий системы - для поиска в скрипте
   * @param userId - пользователь
   * @param roleId - роль пользователя
   * @param usertypeId - вид пользователя
   * @param surname - фамилия
   * @param crequestNumber - № заявки 
   * @param crequestId - заявка
   * @param eventCode - код события
   * @param description - описание
   * @param os - ОС
   * @param browser - браузер
   * @param geoPlace - место нахождения
   * @param dateEvent - дата события
   */
  int countLogsTime(Integer userId,Integer roleId,Integer usertypeId,
		  String surname, String crequestNumber, Integer crequestId,
		  Integer eventCode,String description, String os,String browser, 
		  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress);
  /**
   * сохраняем логи о совершенных действиях, 
   * @param description - пишем комментарии
   * @param cre - запись о заявке
   * @param pplmain - запись о человеке
   * @param usr - запись о пользователе
   * @param geoPlace - место расположения человека
   * @param browser - браузер пользователя
   * @param userAgent - строка заголовка пользователя
   * @param os - OS пользователя
   * @param credit - запись о кредите
   */
  void saveLog(String ipAddress, EventTypeEntity evType, EventCodeEntity evCode, String description,
               CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr,CreditEntity credit,
               String browser,String userAgent,String os,String geoPlace) throws KassaException;

  /**
   * сохраняем логи о совершенных действиях, 
   * @param description - пишем комментарии
   * @param cre - запись о заявке
   * @param pplmain - запись о человеке
   * @param usr - запись о пользователе
   * @param geoPlace - место расположения человека
   * @param browser - браузер пользователя
   * @param userAgent - строка заголовка пользователя
   * @param os - OS пользователя
   * @param credit - запись о кредите
   * @param date - дата события
   */
  void saveLog(String ipAddress, EventTypeEntity evType, EventCodeEntity evCode, String description,
               CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr,CreditEntity credit,
               String browser,String userAgent,String os,String geoPlace,Date date) throws KassaException;
  /**
   * сохраняем логи о совершенных действиях, 
   * @param description - пишем комментарии
   * @param cre - запись о заявке
   * @param pplmain - запись о человеке
   * @param usr - запись о пользователе
   * @param geoPlace - место расположения человека
   * @param browser - браузер пользователя
   * @param userAgent - строка заголовка пользователя
   * @param os - OS пользователя
   * @param credit - запись о кредите
   * @param date - дата события
   * @param partnerId - id партнера
   */
  void saveLog(String ipAddress, EventTypeEntity evType, EventCodeEntity evCode, String description,
               CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr,CreditEntity credit,
               String browser,String userAgent,String os,String geoPlace,Date date,
               Integer partnerId) throws KassaException;
  /**
   * ищем запись в логе
   *
   * @param creditRequestId - id заявки
   * @param peopleMainId    - id человека
   * @param eventId         - id события
   * @param creditId        - id кредита 
   */
  EventLogEntity findLog(Integer creditRequestId, Integer peopleMainId, Integer eventId,Integer creditId);
  /**
   * возвращает запись в логе по id
   * @param eventLogId - id лога
   * @param options - опции
    */
  EventLog getEventLog(Integer eventLogId,Set options);
  /**
   * возвращает запись в логе по id
   * @param eventLogId - id лога
   */
  EventLogEntity getEventLogEntity(Integer eventLogId);
  /**
   * возвращает список типов записи лога
   */
  public List<EventType> getEventTypes();
  /**
   * возвращает список операций для лога
   */
  public List<EventCode> getEventCodes();
  /**
   * возвращает запись для конкретного типа записи лога (id)
   */
  public EventType getEventType(int id);
  /**
   * возвращает запись для конкретной операции лога (id)
   */
  public EventCode getEventCode(int id);
  /**
   * возвращает запись для конкретной операции лога (id)
   */
  public EventCodeEntity getEventCodeEntity(int id);
  /**
   * возвращает запись для конкретного типа записи лога (id)
   */
  public EventTypeEntity getEventTypeEntity(int id);
  /**
   * добавляем код события 
   */
  public void addEventCode(String name);
  /**
   * сохраняем справочник кодов событий списком
   */
  public void saveEventCode(List<EventCode> lst);

  /**
   * сохраняем лог
   * @param ipAddress - ip адрес
   * @param evTypeId - вид лога
   * @param eventTypeCode - событие
   * @param description - описание
   * @param cre - заявка
   * @param pplmain - человек
   * @param userId - пользователь
   * @param credit - кредит
   * @param browser - браузер
   * @param userAgent - заголовок
   * @param os - ОС
   * @param geoPlace - место нахождения
   * @param date - дата
   * @throws KassaException
   */
  void saveLog(String ipAddress, int evTypeId, int eventTypeCode,
		String description, CreditRequestEntity cre, PeopleMainEntity pplmain,
		Integer userId, CreditEntity credit, String browser, String userAgent,
		String os, String geoPlace, Date date) throws KassaException;
  
  /**
   * сохраняем лог
   * @param evTypeId - вид лога
   * @param eventTypeCode - событие
   * @param description - описание
   * @param cre - заявка
   * @param pplmain - человек
   * @param userId - пользователь
   * @param credit - кредит
   * @param date - дата
   * @throws KassaException
   */
  void saveLog(int evTypeId, int eventTypeCode, String description,
		Integer userId, Date date, CreditRequestEntity cre,
		PeopleMainEntity pplmain, CreditEntity credit) throws KassaException;
  /**
   * ищем последний лог для кредита
   * @param eventCode - код события
   * @param creditId - id кредита
   * @return
   */
  EventLog findLatestByCreditId(int eventCode, int creditId);
  /**
   * ищем последний лог для заявки
   * @param eventCode - код события
   * @param crequestId - id заявки
   * @return
   */
  EventLog findLatestByCreditRequestId(int eventCode, int crequestId);

  /**
   * сохраняем лог
   * @param evTypeId - вид лога
   * @param eventTypeCode - событие
   * @param description - описание
   * @param cre - заявка
   * @param pplmain - человек
   * @param userId - пользователь
   * @param credit - кредит
   * @param date - дата
   * @throws KassaException
   */
  void saveLogEx(int evTypeId, int eventTypeCode, String description,
		Integer userId, Date date, CreditRequestEntity cre,
		PeopleMainEntity pplmain, CreditEntity credit) throws ActionException;
}
