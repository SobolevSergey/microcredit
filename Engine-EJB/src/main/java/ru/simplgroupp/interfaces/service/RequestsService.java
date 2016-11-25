package ru.simplgroupp.interfaces.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;

@Local
public interface RequestsService {

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
     * возвращает запрос к партнеру
     * @param id
     */
    Requests getRequests(int id);
    
    /**
     * возвращает список статусов запроса 
     */
    public List<RequestStatus> getRequestStatuses();
    
    /**
     * возвращает запись конкретного статуса запроса 
     * @param id - id статуса
     */
    public RequestStatus getRequestStatus(int id);
    
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
     * кеширован ли запрос
     * @param peopleMainId - id человека
     * @param partnerId - id партнера 
     * @param dateRequest - дата нового запроса
     * @param cacheDays - дней кеша
     * @return
     */
    public boolean isRequestInCache(Integer peopleMainId,Integer partnerId,	Date dateRequest,Integer cacheDays);
    /**
     * сохраняем запрос, в случае неудачи кидаем ActionException
     * @param request - запрос
     * @return
     * @throws ActionException
     */
    RequestsEntity saveRequestEx(RequestsEntity request) throws ActionException;
    /**
     * сохраняем запрос с ошибкой
     * @param request - запрос
     * @param ex - ошибка
     * @param errorKey - вид ошибки
     * @param errorText - текст ошибки
     * @param typeError - тип ошибки
     * @param resultTypeError - результат ошибки
     * @return
     * @throws ActionException
     */
    RequestsEntity saveRequestError(RequestsEntity request,Exception ex,int errorKey,
			String errorText,Type typeError,ResultType resultTypeError) throws ActionException;
}
