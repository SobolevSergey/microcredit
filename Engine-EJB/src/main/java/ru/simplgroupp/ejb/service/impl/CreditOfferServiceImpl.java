package ru.simplgroupp.ejb.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ru.simplgroupp.dao.interfaces.CreditOfferDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.CreditOfferService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.CreditOfferEntity;
import ru.simplgroupp.transfer.CreditOffer;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CreditOfferServiceImpl implements CreditOfferService{

	@EJB
	PeopleDAO peopleDAO;
	
	@EJB
	CreditOfferDAO creditOfferDAO;
	
	@EJB
	EventLogService eventLog;
	
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	KassaBeanLocal kassaBean;
	
	@EJB
	UsersDAO userDAO;
	
	@EJB
	WorkflowBeanLocal workflow;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditOfferEntity saveNewCreditOffer(Integer creditRequestId,
			Integer peopleMainId, Integer userId, Integer offerWayId,
			Date offerTime, Integer creditDays, Double creditSum,
			Double creditStake, String comment) throws KassaException{
		//добавили предложение других условий
		CreditOfferEntity creditOffer=creditOfferDAO.newCreditOffer(creditRequestId, peopleMainId, 
				userId, offerWayId, offerTime, creditDays, creditSum, creditStake, comment);
		//сохранили лог
		eventLog.saveLog(EventType.INFO, EventCode.OFFER_ANOTHER_CREDIT_CONDITIONS, "Предложение клиенту других условий, сумма "+creditSum+", дней "+creditDays, 
				userId, new Date(), crDAO.getCreditRequestEntity(creditRequestId), peopleDAO.getPeopleMainEntity(peopleMainId), null);
		return creditOffer;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditOfferEntity saveAcceptOffer(int id) throws KassaException{
		//приняли предложение
		CreditOfferEntity creditOffer=creditOfferDAO.acceptOffer(id);
		//написали лог
		eventLog.saveLog(EventType.INFO, EventCode.ACCEPT_ANOTHER_CREDIT_CONDITIONS, 
				"Предложение клиенту других условий принято "+creditOffer.getCreditSum()+", дней "+creditOffer.getCreditDays(), 
				null, new Date(), crDAO.getCreditRequestEntity(creditOffer.getCreditRequestId().getId()), 
				peopleDAO.getPeopleMainEntity(creditOffer.getPeopleMainId().getId()), null);
		//поменяли в заявке
		crDAO.saveAnotherSumAndDays(creditOffer.getCreditRequestId().getId(), creditOffer.getCreditSum(), creditOffer.getCreditStake(), creditOffer.getCreditDays());
		return creditOffer;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditOfferEntity saveDeclineOffer(int id) throws KassaException,WorkflowException{
		//отклонили предложение
		CreditOfferEntity creditOffer=creditOfferDAO.declineOffer(id);
		//написали лог
		eventLog.saveLog(EventType.INFO, EventCode.DECLINE_ANOTHER_CREDIT_CONDITIONS, 
				"Предложение клиенту других условий отклонено "+creditOffer.getCreditSum()+", дней "+creditOffer.getCreditDays(), 
				null, new Date(), crDAO.getCreditRequestEntity(creditOffer.getCreditRequestId().getId()), 
				peopleDAO.getPeopleMainEntity(creditOffer.getPeopleMainId().getId()), null);
		//сохранили отказ для заявки
		kassaBean.saveClientRefuse(creditOffer.getCreditRequestId().getId(),userDAO.findUserByPeopleId(creditOffer.getPeopleMainId().getId()).getId());
		//просигналили в процесс об отказе
		workflow.goProcCR(creditOffer.getCreditRequestId().getId(), SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST, null, ProcessKeys.MSG_CLIENT_REJECT), Collections.<String, Object>emptyMap());
		return creditOffer;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void declineAllOffer(Integer creditRequestID) {
		creditOfferDAO.declineAllOffer(creditRequestID);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditOffer getCreditOfferByCreditRequest(Integer creditRequestID){
		List<CreditOffer> offers = creditOfferDAO.getCreditOfferByCreditRequest(creditRequestID);
		if(offers!=null) {
			for (CreditOffer offer : offers) {
				if (offer.getAccepted() == null) {
					return offer;
				}

			}
		}
		return null;
	}
}
