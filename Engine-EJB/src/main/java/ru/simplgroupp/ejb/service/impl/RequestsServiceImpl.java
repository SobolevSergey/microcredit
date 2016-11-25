package ru.simplgroupp.ejb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.RequestsDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.RequestStatusEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;

/**
 * сервис для работы с запросами в КБ
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class RequestsServiceImpl implements RequestsService{

		
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB 
	ReferenceBooksLocal refBooks;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@EJB 
	RequestsDAO requestsDAO;
	
	@Inject Logger log;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveRequest(RequestsEntity req) throws KassaException {
		return requestsDAO.saveRequest(req);
	 }
	
	@Override
	public Integer findMaxRequestNumber() {
	    return requestsDAO.findMaxRequestNumber();

	}
	
	@Override
    public RequestsEntity getRequest(int id) {
	    return requestsDAO.getRequest(id);
	}
	 
	@Override
	public Requests getRequests(int id) {
		RequestsEntity requests=getRequest(id);
		if (requests!=null){
			return new Requests(requests);
		}
		return null;
	}

	@Override
	public RequestStatus getRequestStatus(int id) {
		 RequestStatusEntity ent = requestsDAO.getRequestStatusEntity(id);
		 if (ent == null) {
		     return null;
		 } else {
		     return new RequestStatus(ent);
		 }
	}

	@Override
	public List<RequestStatus> getRequestStatuses() {
	    return requestsDAO.getRequestStatuses();
	}

	@Override
	public List<Requests> listKBRequests(int nFirst, int nCount,Integer creditRequestId,
				Integer partnerId, Integer statusId, String requestGuid,
				DateRange requestDate, DateRange responseDate,String creditRequestNumber) {
			
		 return requestsDAO.listKBRequests(nFirst, nCount, creditRequestId, partnerId, 
				 statusId, requestGuid, requestDate, responseDate, creditRequestNumber);
	}

	@Override
	public int countKBRequests(Integer creditRequestId, Integer partnerId,
				Integer statusId, String requestGuid, DateRange requestDate,
				DateRange responseDate,String creditRequestNumber) {
		return requestsDAO.countKBRequests(creditRequestId, partnerId, statusId, 
				requestGuid, requestDate, responseDate, creditRequestNumber);
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeRequest(int id) {
	    requestsDAO.removeRequest(id);
 	}

	@Override
	public List<RequestsEntity> listPartnersRequests(Integer partnerId) {
		return requestsDAO.listPartnersRequests(partnerId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity addRequest(Integer peopleMainId,Integer creditRequestId,
			Integer partnerId, Integer statusId, Date requestDate) {
		return requestsDAO.addRequest(peopleMainId, creditRequestId, partnerId, statusId, requestDate);
	}

	@Override
	public RequestsEntity getLastPeopleRequestByPartner(Integer peopleMainId,
			Integer partnerId) {
		return requestsDAO.getLastPeopleRequestByPartner(peopleMainId, partnerId);
	}

	@Override
	public boolean isRequestInCache(Integer peopleMainId,Integer partnerId,
			Date dateRequest, Integer cacheDays) {
		RequestsEntity request=getLastPeopleRequestByPartner(peopleMainId,partnerId);
		//если по человеку были запросы
		if (request!=null){
			Integer days=DatesUtils.daysDiff(dateRequest, request.getRequestdate());
			//прошло меньше времени, чем мы кешируем запрос
			if (days<=cacheDays){
				return true;
			}
		}
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveRequestEx(RequestsEntity request) throws ActionException{
		   //save request
		   try {
				request=saveRequest(request);
			} catch (KassaException e1){
				  log.severe("Не удалось сохранить запрос "+e1);
				  throw new ActionException(ErrorKeys.CANT_SAVE_REQUEST, "Не удалось сохранить запрос", Type.TECH, ResultType.NON_FATAL, e1);
			}	
		   return request;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveRequestError(RequestsEntity request,Exception ex,int errorKey,
			String errorText,Type typeError,ResultType resultTypeError) throws ActionException{
		   
		request.setRequeststatus(requestsDAO.getRequestStatusEntity(RequestStatus.STATUS_ERROR));
		request= saveRequestEx(request);
		log.severe(errorText+" "+ex);
		throw new ActionException(errorKey, errorText, typeError,resultTypeError, ex);
	}
}
