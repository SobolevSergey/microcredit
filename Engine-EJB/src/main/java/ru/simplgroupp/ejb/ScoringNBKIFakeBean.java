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
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringNBKIBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
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
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringNBKIBeanLocal.class)
public class ScoringNBKIFakeBean extends AbstractPluginBean implements ScoringNBKIBeanLocal {

	@Inject Logger log;

	protected List<CreditRequest> packet;

	@EJB
	protected KassaBeanLocal kassaBean;

	@EJB
	protected ReferenceBooksLocal refBooks;

	@EJB
	protected RequestsService requestsService;

	@EJB
	protected EventLogService eventLog;

	@EJB
	CreditRequestDAO crDAO;
	
	@Override
	public String getSystemName() {
		return ScoringNBKIBeanLocal.SYSTEM_NAME;
	}

	@Override
	public String getSystemDescription() {
		return ScoringNBKIBeanLocal.SYSTEM_DESCRIPTION;
	}

	@Override
	public boolean isFake() {
		return true;
	}

	@Override
	public EnumSet<Mode> getModesSupported() {
		return EnumSet.of(Mode.SINGLE);
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
	public String getBusinessObjectClass() {
		return CreditRequest.class.getName();
	}

	@Override
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer creditRequestId = Convertor.toInteger(businessObjectId);

		final String fakeString = isFake() ? "Эмуляция: " : "";

		log.info(fakeString + "НБКИ. Заявка " + creditRequestId + " ушла в скоринг.");
		try {
			CreditRequest ccRequest;
			try {
				ccRequest = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
			} catch (Exception e) {
				log.severe(fakeString + "НБКИ. Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку", Type.TECH,
						ResultType.NON_FATAL, e);
			}
			if (ccRequest == null) {
				log.severe(fakeString + "Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку, заявка null",
						Type.TECH, ResultType.NON_FATAL, null);
			}
			NBKIPluginConfig cfg = (NBKIPluginConfig) context.getPluginConfig();
			if (context.getNumRepeats() <= cfg.getNumberRepeats()) {
				newRequest(ccRequest.getEntity(), cfg.isUseWork(),cfg.getRequestScoring(),
						cfg.getRequestScoring()?cfg.getUrlScoring():cfg.getUrlCommon(),
						cfg.getCacheDays(),cfg.getUseSSL() );
			} else {
				log.severe(fakeString + "НБКИ. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING, "Не удалось выполнить скоринговый запрос в НБКИ за 3 попытки",
						Type.TECH, ResultType.FATAL, null);
			}
		} catch (ActionException e) {
			log.severe(fakeString + "НБКИ. Заявка " + creditRequestId + " не обработана.");
			throw new ActionException("Произошла ошибка ", e);
		} catch (Throwable e) {
			log.severe(fakeString + "НБКИ. Заявка " + creditRequestId + " не обработана. Ошибка " + e + ", " + e.getMessage());
			throw new ActionException("Произошла ошибка ", e);
		}

		log.info(fakeString + "НБКИ. Заявка " + creditRequestId + " обработана.");
	}

	@Override
	public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer creditRequestId = Convertor.toInteger(businessObjectId);
		CreditRequest ccRequest;
		try {
			ccRequest = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
			packet.add(ccRequest);
		} catch (Exception e) {
			throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку", Type.TECH,
					ResultType.NON_FATAL, e);
		}
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
		log.info("Метод sendSingleRequest не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
		log.info("Метод querySingleResponse не поддерживается");
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
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork, Integer cacheDays) throws ActionException {
		return newRequest(cre,isWork,false,"",cacheDays,false);
	}

	@Override
	public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
		final String fakeString = isFake() ? "Эмуляция НБКИ:" : "";
		log.info(fakeString + "ScoringNBKIBeanLocal.saveAnswer");
		return null;
	}

	@Override
	public void uploadHistory(UploadingEntity uploading, Date sendingDate, Boolean isWork) throws KassaException {
	
	}

	
	@Override
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork) throws KassaException {
		return null;
	}

	
	@Override
	public void checkUploadStatus(UploadingEntity uploading, Boolean isWork)
			throws KassaException {
				
	}
	
	@Override
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
			Boolean requestScoring, String urlAdditional,Integer cacheDays,
			Boolean useSSL) throws ActionException {
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.NBKI, RequestStatus.STATUS_IS_DONE_WITH_SESSION, new Date());
		
		//save request
		try {
			req.setResponsemessage("Пустой метод, нет данных");
			req=requestsService.saveRequest(req);
		} catch (KassaException ex){
		   throw new ActionException(ErrorKeys.CANT_SAVE_REQUEST, "Не удалось сохранить запрос", Type.BUSINESS, ResultType.NON_FATAL, null);
		}	
		//save log
		try {
			eventLog.saveLog(EventType.INFO,EventCode.OUTER_SCORING_NBKI,"Заглушечный запрос в КБ НБКИ",
					null,new Date(),cre,null,null);
		} catch (KassaException e) {
			throw new ActionException(ErrorKeys.CANT_WRITE_LOG,"Не удалось записать лог",Type.TECH, ResultType.NON_FATAL,e);
		} 
		return req;
	}

	@Override
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		return null;
	}

}
