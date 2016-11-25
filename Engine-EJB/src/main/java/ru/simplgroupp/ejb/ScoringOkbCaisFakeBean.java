package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Заглушечный класс для работы с ОКБ, сервис кредитные истории
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringOkbCaisBeanLocal.class)
public class ScoringOkbCaisFakeBean  extends AbstractPluginBean implements ScoringOkbCaisBeanLocal {

	 @Inject Logger log;
	
	 @EJB 
	 ReferenceBooksLocal refBooks;
		    
	 @EJB
	 PeopleBeanLocal peopleBean;
		
	 @EJB
	 KassaBeanLocal kassaBean;
		
	 @EJB 
	 ServiceBeanLocal servBean;	 
	 
	 @EJB
	 WorkflowBeanLocal workflow; 
	 
	 @EJB 
	 EventLogService eventLog;
	 
	 @EJB
	 UploadingService uploadService;
	 
	 @EJB
	 RequestsService requestsService;
	 
	 @EJB
	 CreditRequestDAO creditRequestDAO;
	 
	@Override
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
			Integer cacheDays)	throws ActionException {
	
		return newRequest(cre,isWork,cacheDays,50);
	}

	@Override
	public String getSystemName() {
		return ScoringOkbCaisBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return true;
	}

	@Override
	public EnumSet<Mode> getModesSupported() {
		return EnumSet.of(Mode.SINGLE, Mode.PACKET);
	}

	@Override
	public EnumSet<ExecutionMode> getExecutionModesSupported() {
		return EnumSet.of(ExecutionMode.AUTOMATIC, ExecutionMode.MANUAL);
	}

	@Override
	public EnumSet<SyncMode> getSyncModesSupported() {
		return EnumSet.of(SyncMode.SYNC);
	}

	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
	}

	@Override
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer creditRequestId = Convertor.toInteger(businessObjectId);
		log.info("ОКБ кредитные истории, эмуляция. Заявка " + creditRequestId + " передана на скоринг.");
		CreditRequest ccRequest;
		try {
			ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
		} catch (Exception e) {
			throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.NON_FATAL,e);
		}
		if (ccRequest==null){
			throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку, заявка null",Type.TECH, ResultType.NON_FATAL,null);
		}
		try {
			if (context.getNumRepeats() <3)	
		      newRequest(ccRequest.getEntity(),false,10,50);
			else
			{
				log.info("ОКБ, кредитные истории, эмуляция. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 3 попытки",Type.TECH, ResultType.FATAL,null);
			}
		} catch  (ActionException e) {
			log.info("ОКБ, кредитные истории, эмуляция. Заявка " + creditRequestId + " не обработана.");
			throw new ActionException("Произошла ошибка ",e);
		}
		log.info("ОКБ кредитные истории, эмуляция. Заявка " + creditRequestId + " обработана.");
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
		return null;
	}

	
	
	@Override
	public String getBusinessObjectClass() {
		return CreditRequest.class.getName();
	}
	
	@Override
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
			
	}

	@Override
	public String getSystemDescription() {
		return ScoringOkbCaisBeanLocal.SYSTEM_DESCRIPTION;
	}

	@Override
	public void uploadHistory(UploadingEntity uploading, Date sendingDate,
			Boolean isWork) throws KassaException {
		  
		  uploading.setResponseDate(new Date());
		  uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
		  uploading=uploadService.saveUpload(uploading);
		  eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_OKB,"Заглушечная выгрузка кредитных историй в КБ ОКБ прошла успешно",
				  	null,new Date(),null,null,null);
		  log.info("Был вызван заглушечный метод загрузки в КБ ОКБ");
		
	}

	@Override
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
			throws KassaException {
		Partner parOkb=refBooks.getPartner(Partner.OKB_CAIS);
		UploadingEntity uploading=new UploadingEntity();
		uploading.setDateUploading(new Date());
		uploading.setName("Okb fake uploading");
		uploading.setPartnersId(parOkb.getEntity());
		uploading=uploadService.saveUpload(uploading);
		log.info("Был вызван заглушечный метод создания файла для загрузки в ОКБ");
		return uploading;
	}

	@Override
	public RequestsEntity saveAnswer(RequestsEntity req, String answer)
			throws KassaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
			Integer cacheDays, Integer numberFunction) throws ActionException {
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.OKB_CAIS, RequestStatus.STATUS_IS_DONE_WITH_SESSION, new Date());
	
		//save request
		try {
			req.setResponsemessage("Пустой метод, нет данных");
			req=requestsService.saveRequest(req);
		} catch (KassaException ex){
		   throw new ActionException(ErrorKeys.CANT_SAVE_REQUEST, "Не удалось сохранить запрос", Type.BUSINESS, ResultType.NON_FATAL, null);
		}
		
		try {
			eventLog.saveLog(EventType.INFO,EventCode.OUTER_SCORING_OKB_CAIS,"Заглушечный запрос в КБ ОКБ",
					null,new Date(),cre,null,null);
		} catch (KassaException e) {
			throw new ActionException(ErrorKeys.CANT_WRITE_LOG,"Не удалось записать лог",Type.TECH, ResultType.NON_FATAL,e);
		} 
		return req;
	}	
}
