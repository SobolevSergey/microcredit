package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.RequestStatusEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;

@Local
public interface RequestsDAO {


	 /**
    * сохраняет запрос в КБ
    * @param req - запрос
    */
   RequestsEntity saveRequest(RequestsEntity req) throws KassaException;
 
   /** 
    * ищем последний уникальный номер запроса 
    */
   Integer findMaxRequestNumber();
   

   /**
    * возвращает запрос к партнеру
    * @param id
    */
   RequestsEntity getRequest(int id);
   
   /**
    * возвращает список статусов запроса 
    */
   public List<RequestStatus> getRequestStatuses();
   /**
    * список запросов в КБ по параметрам
    * 
    * @param creditRequestId - заявка
    * @param partnerId - партнер
    * @param statusId - статус запроса
    * @param requestGuid - № запроса
    * @param requestDate - дата запроса
    * @param responseDate - дата ответа
    */
   List<Requests> listKBRequests(int nFirst, int nCount,Integer creditRequestId,Integer partnerId,Integer statusId,String requestGuid,
   		DateRange requestDate,DateRange responseDate,String creditRequestNumber);
  
   /**
    * кол-во запросов в КБ по параметрам
    * 
    * @param creditRequestId - заявка
    * @param partnerId - партнер
    * @param statusId - статус запроса
    * @param requestGuid - № запроса
    * @param requestDate - дата запроса
    * @param responseDate - дата ответа
    */
   int countKBRequests(Integer creditRequestId,Integer partnerId,Integer statusId,String requestGuid,
   		DateRange requestDate,DateRange responseDate,String creditRequestNumber);
   
   /**
    * удаляем запрос в КБ
    * @param id - id запроса
    */
   public void removeRequest(int id);
   /**
    * список запросов к партнеру 
    * @param partnerId - id партнера
    * @return возвращает entity
    */
   List<RequestsEntity> listPartnersRequests(Integer partnerId);
   /**
    * добавляем новый запрос
    * @param peopleMainId - id человека
    * @param creditRequestId - id заявки
    * @param partnerId - id партнера
    * @param statusId - id статуса
    * @param requestDate - дата запроса
    * @return
    */
   public RequestsEntity addRequest(Integer peopleMainId, Integer creditRequestId,Integer partnerId,
   		Integer statusId,Date requestDate);
   /**
    * возвращает последний запрос по человеку к партнеру
    * @param peopleMainId - id человека
    * @param partnerId - id партнера
    * @return
    */
   public RequestsEntity getLastPeopleRequestByPartner(Integer peopleMainId,Integer partnerId);
   
   /**
    * возвращает статус запроса
    * @param id - id статуса
    * @return
    */
   public RequestStatusEntity getRequestStatusEntity(int id);
}
