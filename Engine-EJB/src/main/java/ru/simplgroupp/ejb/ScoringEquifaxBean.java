package ru.simplgroupp.ejb;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.DebtDao;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.services.CreditService;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleIncapacityEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.persistence.SummaryEntity;
import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.FileUtil;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.toolkit.common.ZipUtils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.Employment;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.transfer.ScoreModel;
import ru.simplgroupp.transfer.Summary;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.transfer.VerificationConfig;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.FTPUtils;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MakeXML;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.SettingsKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Реальный класс для работы с Equifax
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringEquifaxBeanLocal.class)
public class ScoringEquifaxBean  extends AbstractPluginBean implements ScoringEquifaxBeanLocal {
	
	private static HashMap<Integer,String> monthes = new HashMap<Integer,String>(12);
	private static HashMap<Integer,String> days = new HashMap<Integer,String>(31);
	
	@Inject Logger log;
		
	static
	{
		//месяц для названия выгрузки
 		monthes.put(1,"1");
 		monthes.put(2,"2");
 		monthes.put(3,"3");
 		monthes.put(4,"4");
 		monthes.put(5,"5");
 		monthes.put(6,"6");
 		monthes.put(7,"7");
 		monthes.put(8,"8");
 		monthes.put(9,"9");
 		monthes.put(10,"A");
 		monthes.put(11,"B");
 		monthes.put(12,"C");
 		//день для названия выгрузки
 		days.put(1, "1");
 		days.put(2, "2");
 		days.put(3, "3");
 		days.put(4, "4");
 		days.put(5, "5");
 		days.put(6, "6");
 		days.put(7, "7");
 		days.put(8, "8");
 		days.put(9, "9");
 		days.put(10, "A");
 		days.put(11, "B");
 		days.put(12, "C");
 		days.put(13, "D");
 		days.put(14, "E");
 		days.put(15, "F");
 		days.put(16, "G");
 		days.put(17, "H");
 		days.put(18, "I");
 		days.put(19, "J");
 		days.put(20, "K");
 		days.put(21, "L");
 		days.put(22, "M");
 		days.put(23, "N");
 		days.put(24, "O");
 		days.put(25, "P");
 		days.put(26, "Q");
 		days.put(27, "R");
 		days.put(28, "S");
 		days.put(29, "T");
 		days.put(30, "U");
 		days.put(31, "V");
	}
	
  	@EJB 
	ReferenceBooksLocal refBooks;
	    
	@EJB
	PeopleBeanLocal peopleBean;
	
	@EJB
	KassaBeanLocal kassaBean;

	@EJB
	UploadingService uploadService;
	
	@EJB
	RequestsService requestsService;
	
	@EJB
	CreditBeanLocal creditBean;
	
    @EJB
    PaymentService paymentService;

	@EJB 
	ServiceBeanLocal servBean;
	 
	@EJB
	WorkflowBeanLocal workflow; 
	 
	@EJB
	ICryptoService crypto;
	 
	@EJB 
	EventLogService eventLog;
	
	@EJB
	CreditInfoService creditInfo;
	
	@EJB
	CreditService creditService;
	
	@EJB
    OrganizationService orgService;
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	PeopleDAO peopleDAO;
	 
	@EJB
	SummaryDAO summaryDAO;
	
	@EJB
	DebtDao debtDAO;
	
	@EJB
	ProductBeanLocal productBean;
		
	@EJB
	RulesBeanLocal rulesBean;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays) throws ActionException {
		
		//проверяем, кеширован ли запрос
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.EQUIFAX, new Date(), cacheDays);
	    if (inCache){
	    	log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в КБ Эквифакс, он кеширован");
	    	//save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_EQUIFAX,
				"Запрос к внешнему партнеру КБ Эквифакс не производился, данные кешированы",
					null,new Date(),crDAO.getCreditRequestEntity(cre.getId()),null,null);
			
			RequestsEntity reqOld=requestsService.getLastPeopleRequestByPartner(cre.getPeopleMainId().getId(), Partner.EQUIFAX);
			if (reqOld!=null){
				try {
					saveAnswer(reqOld,reqOld.getResponsebodystring());
				} catch (Exception e){
					log.severe("Не удалось разобрать старый запрос, ошибка "+e);
				}
			}
	    	return null;
	    }
		//new equifax request
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), Partner.EQUIFAX, 
				RequestStatus.STATUS_IN_WORK, new Date());
		
		Partner parEquifax = refBooks.getPartner(Partner.EQUIFAX);
		//генерим xml
		String	rmessage = createRequestString(cre,req,isWork);
		String url=isWork?parEquifax.getUrlWork():parEquifax.getUrlTest();
	    //подписываем сообщение
		byte[] smessage=null;
		try {
			smessage= crypto.createJCPCMS(rmessage.getBytes(XmlUtils.ENCODING_WINDOWS), isWork?CryptoSettings.EQUIFAX_SIGN_WORK:CryptoSettings.EQUIFAX_SIGN_TEST,false);
		} catch (Exception e1) {
			log.severe("Не удалось подписать сообщение "+e1.getMessage());
			throw new ActionException(ErrorKeys.CANT_CREATE_SIGN,"Не удалось подписать сообщение",Type.TECH, ResultType.NON_FATAL,e1);
		} 
		
		req.setRequestbody(rmessage.getBytes());
		req=requestsService.saveRequestEx(req);
				
		//send request and get response
		byte[] respmessage;
		
		//параметры для http post
		Map<String,String> rparams=new HashMap<String,String>();
		rparams.put("Content-Type", "application/octet-stream");
		try {
			respmessage = HTTPUtils.sendHttp("POST", url, smessage,rparams,null);
		} catch (Exception e) {
		    log.severe("Не удалось отправить либо получить данные "+e);	
		    throw new ActionException(ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.BUSINESS, ResultType.NON_FATAL, null);
		}
		
		//если есть ответ
		if (respmessage!=null){
		    
			boolean b=false;
		    
		   	//проверяем подпись
		    try {
				b=crypto.verifyJCPCms(respmessage, isWork?CryptoSettings.EQUIFAX_CHECK_WORK:CryptoSettings.EQUIFAX_CHECK_TEST);
			} catch (Exception e1) {
				log.severe("Не удалось проверить подпись "+e1);	
			}
			b=true;
			//если подпись не соответствует
			if (!b) {
			   try {
			        eventLog.saveLog(EventType.ERROR,EventCode.SIGNATURE_NOT_VALID_EQUIFAX,"Подпись Эквифакса некорректна",null,new Date(),cre,null,null);
				} catch (KassaException e) {
					log.severe("Не удалось записать лог "+e);	
					throw new ActionException(ErrorKeys.CANT_WRITE_LOG,"Не удалось записать лог",Type.TECH, ResultType.NON_FATAL,e);
				} 
			  }
				
			//перекодируем в Windows-1251
			String answer="";
			try {
				answer = new String(respmessage,XmlUtils.ENCODING_WINDOWS);
			} catch (UnsupportedEncodingException e) {
				log.severe("Ошибка перекодирования в Windows-1251 "+e);	
				throw new ActionException(ErrorKeys.CANT_DECODE_MESSAGE, "Ошибка перекодирования в Windows-1251", Type.BUSINESS, ResultType.FATAL, null);
			}
				
			String respmessageUtf=XmlUtils.getXmlFromRaw(answer, "<?xml", "bki_response");
				
			//запишем ответ в БД  
			req.setResponsebody(respmessage);
			req.setResponsebodystring(respmessageUtf);
			
			log.info("Вернулся ответ из Эквифакса по заявке "+cre.getId());  	
			//save request
			req=requestsService.saveRequestEx(req);
				
			//делаем разбор
			try {
				req=saveAnswer(req,respmessageUtf);
			} catch (Exception e) {
				log.severe("Не удалось разобрать xml "+e);
				throw new ActionException(ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать xml", Type.BUSINESS, ResultType.FATAL, null);
			}
			
		}
	
		//save request
		req=requestsService.saveRequestEx(req);
		
		//save log
		eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_EQUIFAX,"Был произведен запрос к внешнему партнеру КБ Эквифакс",
			null,new Date(),crDAO.getCreditRequestEntity(cre.getId()),null,null);
	
		return req;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadHistory(UploadingEntity uploading,Date sendingDate,Boolean isWork) throws KassaException {
		
		Partner parEquifax=refBooks.getPartner(Partner.EQUIFAX);
		
        if (uploading!=null) {
            
			byte[] bs=null;
			try {
				
				if (sendingDate==null){
				  sendingDate=new Date();
				}
				  
				//пишем в файл
				File file=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".xml");
				FileUtils.writeByteArrayToFile(file, uploading.getInfo().getBytes(XmlUtils.ENCODING_WINDOWS));
				//зипуем
				bs=ZipUtils.ZipSingleFile(file);
				//подписываем
				byte[] sign=null;
				try {
					sign=crypto.createJCPCMS(bs, isWork?CryptoSettings.EQUIFAX_SIGN_WORK:CryptoSettings.EQUIFAX_SIGN_TEST,false);
				} catch (Exception e) {
					log.severe("Не удалось подписать файл "+e);
					throw new KassaException("Не удалось подписать файл "+e);
				}
				
                //шифруем
				byte[] enc=null;
				try {
					enc=crypto.encryptPKCS7(sign,isWork?CryptoSettings.EQUIFAX_CHECK_WORK:CryptoSettings.EQUIFAX_CHECK_TEST,XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.sgn.enc");
				    //enc=crypto.encryptCMS(isWork?CryptoSettings.EQUIFAX_SIGN_WORK:CryptoSettings.EQUIFAX_SIGN_TEST, isWork?CryptoSettings.EQUIFAX_CHECK_WORK:CryptoSettings.EQUIFAX_CHECK_TEST, sign, XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.sgn.enc");
				} catch (Exception e) {
					log.severe("Не удалось зашифровать файл "+e);
					throw new KassaException("Не удалось зашифровать файл "+e);
				}
							
			    boolean bup=true;
			    String uploadInbox="";
			    //в какой каталог пишем
			    switch (uploading.getUploadType()){
			        case (UploadStatus.UPLOAD_CREDIT):{
			        	uploadInbox=parEquifax.getUploadInbox();
			        	break;
			        }
			        case (UploadStatus.UPLOAD_CREDITREQUEST):{
			        	uploadInbox="/fip"+parEquifax.getUploadInbox();
			        	break;
			        }	
			        case (UploadStatus.UPLOAD_CREDIT_CORRECTION):{
			        	uploadInbox="/correction"+parEquifax.getUploadInbox();
			        	break;
			        }	
			        case (UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS):{
			        	uploadInbox=parEquifax.getUploadInbox();
			        	break;
			        }
			        default:break;
			    }
				
				//загружаем на ftp
				if (isWork) {
					bup=FTPUtils.UploadFtp(parEquifax.getLoginWork(), parEquifax.getPasswordWork(), parEquifax.getUrlUploadWork(),uploadInbox, uploading.getName()+".zip.sgn.enc", XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.sgn.enc", null,true);
				} else {
					bup=FTPUtils.UploadFtp(parEquifax.getLoginTest(), parEquifax.getPasswordTest(), parEquifax.getUrlUploadTest(), uploadInbox, uploading.getName()+".zip.sgn.enc", XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.sgn.enc", null,true);
				}
				
				//удаляем файл с xml
			    FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".xml");
			    //удаляем файл зашифрованный
			    FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.sgn.enc");
				
				//если все успешно
				if (bup) {
					//пишем eventlog
					eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_EQUIFAX,"Выгрузка кредитных историй в КБ Эквифакс прошла успешно",
							null,new Date(),null,null,null);
					//сохраняем статус успешно выгруженных
					uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_UPLOADED);
					//пишем в базу статус загрузки
					uploading.setDateUploading(sendingDate);
					uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
					uploading=uploadService.saveUpload(uploading);
				}
				//не выгрузили
				else {
					uploading.setStatus(UploadStatus.UPLOAD_ERROR);
					uploading=uploadService.saveUpload(uploading);
					//пишем eventlog
					eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_EQUIFAX,"Не удалось выгрузить кредитные истории в КБ Эквифакс",
							null,new Date(),null,null,null);
				}
			} catch (IOException e) {
				uploading.setStatus(UploadStatus.UPLOAD_ERROR);
				uploading=uploadService.saveUpload(uploading);
				//пишем eventlog
				eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_EQUIFAX,e.toString(),
						null,new Date(),null,null,null);	
			    log.severe("не записался файл "+e);
				throw new KassaException("не записался файл "+e);
			}
		
		}
	
		
	}
	
	/**
	 * генерим название для выгрузки кредитов
	 * @param sendingDate - дата отправки
	 * @param isWork - рабочий сервис или тестовый
	 * @param letter - буква (в зависимости от того, что и как выгружаем)
	 * @return
	 */
	private String generateNameForUpload(Date sendingDate,Boolean isWork,String letter,String digit) {
		String st="";
		Partner parEquifax = refBooks.getPartner(Partner.EQUIFAX);
		String stWork=isWork?parEquifax.getCodeWork():parEquifax.getCodeTest();
	    st=stWork+letter+String.valueOf(DatesUtils.getYear(sendingDate)).substring(String.valueOf(DatesUtils.getYear(sendingDate)).length()-1,String.valueOf(DatesUtils.getYear(sendingDate)).toString().length())+monthes.get(DatesUtils.getMonth(sendingDate))+days.get(DatesUtils.getDay(sendingDate))+digit;
		return st;
	}
	/**
	 * генерим название для выгрузки заявок
	 * @param sendingDate - дата отправки
	 * @param isWork - рабочий сервис или тестовый
	 * @return
	 */
	private String generateNameForUploadCreditRequest(Date sendingDate,Boolean isWork){
	   String st="";
	   Partner parEquifax = refBooks.getPartner(Partner.EQUIFAX);
	   String stWork=isWork?parEquifax.getCodeWork():parEquifax.getCodeTest();
	   st=stWork+"_"+DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate)+"_0001";
	   return st;
	}
	
	@Override
	public String getSystemName() {
		return ScoringEquifaxBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return false;
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
		
		log.info("Эквифакс. Заявка " + creditRequestId + " ушла в скоринг.");
		try {
			CreditRequest ccRequest;
			try {
				ccRequest = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
			} catch (Exception e) {
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.NON_FATAL,e);
			}
			if (ccRequest==null){
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку, заявка null",Type.TECH, ResultType.NON_FATAL,null);
			}
			EquifaxPluginConfig cfg=(EquifaxPluginConfig) context.getPluginConfig();
			if (context.getNumRepeats() <=cfg.getNumberRepeats()) {
			    newRequest(ccRequest.getEntity(),cfg.isUseWork(),cfg.getCacheDays());
			} else {
				log.severe("Эквифакс. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 3 попытки",Type.TECH, ResultType.FATAL,null);
			}
		} catch (ActionException e) {
			log.severe("Эквифакс. Заявка " + creditRequestId + " не обработана.");
			throw new ActionException("Произошла ошибка ",e);
			
		} catch (Throwable e) {
			log.severe("Эквифакс. Заявка " + creditRequestId + " не обработана. Ошибка "+e+", "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		}
				
		log.info("Эквифакс. Заявка " + creditRequestId + " обработана.");	
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		 Integer creditRequestId = Convertor.toInteger(businessObjectId);
		 CreditRequest ccRequest;
			try {
				ccRequest = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
			
			} catch (Exception e) {
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.NON_FATAL,e);
			}
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// пока не используется, возможно будет при пакетных запросах
		return false;
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// пока не используется, возможно будет при пакетных запросах
		return false;
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
		
		Partner parEquifax=refBooks.getPartner(Partner.EQUIFAX);
		//ищем последнюю загрузку
		if (uploading==null){
		    uploading=uploadService.getLastUploading(Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT);
		}
		if (uploading!=null)
		{
		  String uploadOutbox="";
		  String fname=uploading.getName();
		  switch (uploading.getUploadType()){
	        case (UploadStatus.UPLOAD_CREDIT):{
	        	uploadOutbox=parEquifax.getUploadOutbox();
	        	break;
	        }
	        case (UploadStatus.UPLOAD_CREDITREQUEST):{
	        	uploadOutbox="/fip"+parEquifax.getUploadOutbox();
	        	break;
	        }	
	        case (UploadStatus.UPLOAD_CREDIT_CORRECTION):{
	        	uploadOutbox="/correction"+parEquifax.getUploadOutbox();
	        	break;
	        }	
	        case (UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS):{
	        	uploadOutbox=parEquifax.getUploadOutbox();
	        	break;
	        }
	        default:break;
	    }
		  boolean bup=false;
		  //пытаемся скачать файл отчета с ftp
		  if (isWork) {
			  bup=FTPUtils.UploadFtp(parEquifax.getLoginWork(), parEquifax.getPasswordWork(), parEquifax.getUrlUploadWork(),uploadOutbox, fname+".xml.xml", XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+fname+".xml.xml", null,false);
		  } else {
			  bup=FTPUtils.UploadFtp(parEquifax.getLoginTest(), parEquifax.getPasswordTest(), parEquifax.getUrlUploadTest(), uploadOutbox, fname+".xml.xml", XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+fname+".xml.xml", null,false);
		  }
		  //если файл загрузился
		  if (bup)
		  {
			  byte[] fup=null;
			  //читаем из файла в массив байтов
			  File file=new File( XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+fname+".xml.xml");
			  try {
				  fup=FileUtils.readFileToByteArray(file);
			  } catch (IOException e) {
				  throw new KassaException("Не удалось прочитать содержимое файла "+e);
			  }
			  if (fup!=null){
				  //читаем в строку
				  String answer="";
					try {
						answer = new String(fup,XmlUtils.ENCODING_WINDOWS);
					} catch (UnsupportedEncodingException e) {
						throw new KassaException("Ошибка перекодирования в Windows-1251 "+e);
					}
					uploading.setReport(answer);
					uploading=uploadService.saveUpload(uploading);
					//делаем документ и пытаемся разобрать
					if (StringUtils.isNotEmpty(answer))
					{
						 Document doc=XmlUtils.getDocFromString(answer);
						 //если удалось получить документ из строки
						 if (doc!=null)
						 {
							 Node nd=XmlUtils.findChildInNode(doc, "LOG", "FOOTER", 0);
							 if (nd!=null)
							 {
								 
								//сохраняем данные о загрузке
								//всего записей
								 uploading.setRecordsAll(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd, "RecordsAll", 0))));
								//корректных записей
								 uploading.setRecordsCorrect(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd, "RecordsCorrect", 0))));
								//некорректных записей
								 uploading.setRecordsError(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd, "RecordsIncorrect", 0))));
								 uploading=uploadService.saveUpload(uploading);
								 //если не все загруженные записи корректны
								 if (uploading.getRecordsAll()!=uploading.getRecordsCorrect())
								 {
									 Node ndrep=XmlUtils.findChildInNode(doc, "LOG", "REPORT", 0);
									 if (ndrep!=null) {
										 //ищем ошибки
										 List<Node> lsterr=XmlUtils.findNodeList(ndrep, "Error");
										 if (lsterr.size()>0) {
											 for(Node nderr:lsterr){
												 //пишем запись об ошибке
												 UploadingErrorEntity uerror=uploadService.newUploadingError(uploading.getId(),
														 Convertor.toInteger(XmlUtils.getAttrubuteValue(nderr, "ContractId")), 
														 null,XmlUtils.getNodeValueText(nderr));
												 //если есть данные по тому, что это за кредит
												 if (uerror.getCreditId()!=null){
												     //ставим на записи с ошибками статус о невыгрузке
												     int crequestId=uerror.getCreditId().getCreditRequestId().getId();
												     List<UploadingDetailEntity> details=uploadService.findUploadingDetail(crequestId, uploading.getId(), null);
												     UploadingDetailEntity detail=null;
												     if (details.size()>0){
													     detail=details.get(0);
													     detail.setStatus(UploadStatus.UPLOADDETAIL_RESULT_ERROR);
													     uploadService.saveDetail(detail);
												     }
												 }//end если id не null
												
											 }//end for
										 }//end если есть ошибки
									 }//end есть отчет
									 uploading.setStatus(UploadStatus.RESULT_WITH_ERROR);
									 uploading.setResponseDate(new Date());
									 uploading=uploadService.saveUpload(uploading);
									// TODO сделать  повторную отправку
								 }	 else {
									//сохраняем статус успешно выгруженных
									uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_RESULT_SUCCESS);
									uploading.setStatus(UploadStatus.RESULT_SUCCESS);
									uploading.setResponseDate(new Date());
									uploading=uploadService.saveUpload(uploading);
								 }//end если все записи корректны
							 }//end есть footer
						 }//end если удалось получить документ из строки
					}//end непустой ответ
			  }//end непустой файл
			//когда все сделали, удаляем файл  
			FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+fname+".xml");  
		  }//end успешно выгрузили отчет
		}//end есть выгрузка
	}


	
	@Override
	public String getSystemDescription() {
		return ScoringEquifaxBeanLocal.SYSTEM_DESCRIPTION;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
			throws KassaException {
		Partner parEquifax=refBooks.getPartner(Partner.EQUIFAX);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT);
		log.info("Всего записей для выгрузки "+lstcre.size());
        if (lstcre.size() > 0) {
        	//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.EQUIFAX, UploadStatus.UPLOAD_CREDIT, 
        			null,null, null, null, null, null, null, null, null, true);
    	    //создаем заголовок документа
        	org.dom4j.Document xmlDoc=MakeXML.createDocumentUploadForEquifax(uploadDateRange.getTo(), parEquifax.getUploadVersion());		
            Integer i = 1;
            for (CreditRequestEntity cre : lstcre) {
                //добавляем запись в XML
                MakeXML.createUploadRecordForSendingToEquifax(uploadDateRange.getTo(), 
            			listDataForUpload(cre,uploadDateRange,i),xmlDoc);
                //пишем запись в таблицу detail
                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                i++;
            }
            //Закончили выгрузку в xml
          			
			String str="";
			try {
				//возвращаем XML строкой
			    str=MakeXML.returnDocumentUploadForEquifax(xmlDoc, i-1);
			} catch (Exception e) {
				log.severe("Не удалось сформировать xml для выгрузки "+e);
				throw new KassaException("Не удалось сформировать xml для выгрузки "+e);
			}
				
			//сгенерили имя для выгрузки
			String fname=generateNameForUpload(uploadDateRange.getTo(),isWork,"U","1");
			//сохранили данные в таблицу выгрузки
			upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT, 
        			fname,str, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
			
			log.info("Создали файл для выгрузки в Эквифакс, кол-во записей "+lstcre.size());
			return upl;
        } else {
		    return null;
        }
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUploadCreditRequest(Date sendingDate, Boolean isWork)
			throws KassaException {
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.EQUIFAX,UploadStatus.UPLOAD_CREDITREQUEST);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	  
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDITREQUEST);
		log.info("Всего записей для выгрузки "+lstcre.size());
		if (lstcre.size() > 0) {
        	//создаем новую запись в таблице загрузки
    		UploadingEntity upl=uploadService.newUploading(null, Partner.EQUIFAX, UploadStatus.UPLOAD_CREDITREQUEST, 
        			null,null, null, null, null, null, null, null, null, true);
            int i = 1;
            //создаем заголовок документа
        	org.dom4j.Document xmlDoc=MakeXML.createDocumentUploadForEquifaxCR(uploadDateRange.getTo());		
            //добавляем записи в список для формирования файла выгрузки
            for (CreditRequestEntity cre : lstcre) {
                 MakeXML.createUploadRecordForSendingToEquifaxCreditRequest(uploadDateRange.getTo(), 
            		   listDataForUploadCR(cre,uploadDateRange,i), xmlDoc);	
                 i++;
               
            }
            //Закончили выгрузку в xml
			
			String str="";
			try {
				//пишем документ
				str=MakeXML.returnDocumentUploadForEquifax(xmlDoc, i-1);
			} catch (Exception e) {
				throw new KassaException("Не удалось сформировать xml для выгрузки "+e);
			}
				
			//сгенерили имя для выгрузки
			String fname=generateNameForUploadCreditRequest(uploadDateRange.getTo(),isWork);
			
			//сохранили данные в таблицу выгрузки
			log.info("Создали файл для выгрузки в Эквифакс, кол-во записей "+lstcre.size());
			//сохранили данные в таблицу выгрузки
			upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDITREQUEST, 
        			fname,str, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
							
			return upl;
        } else {
		    return null;
        }
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createCorrectionFileForUpload(Date sendingDate,
			Integer creditId, Boolean isWork) throws KassaException {
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
		if (credit!=null){
			Partner parEquifax=refBooks.getPartner(Partner.EQUIFAX);
			
			List<CreditDetailsEntity> lstDetails=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_CREDIT, new DateRange(DateUtils.addDays(credit.getCreditdatabeg(),-1),sendingDate));
			//кредит
			CreditDetailsEntity creditInfo=null;
			if (lstDetails.size()>0){
				creditInfo=lstDetails.get(0);
			}
			//просрочки
			List<CreditDetailsEntity> lstOverdueFirst=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_OVERDUE, new DateRange(DateUtils.addDays(creditInfo.getEventEndDate(),1),DateUtils.addDays(creditInfo.getEventEndDate(),2)));
			CreditDetailsEntity overdueFirst=null;
			if (lstOverdueFirst.size()>0){
				overdueFirst=lstOverdueFirst.get(0);
			}
			List<CreditDetailsEntity> lstOverdueSecond=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_OVERDUE, new DateRange(DateUtils.addDays(creditInfo.getEventEndDate(),6),DateUtils.addDays(creditInfo.getEventEndDate(),7)));
			CreditDetailsEntity overdueSecond=null;
			if (lstOverdueSecond.size()>0){
				overdueSecond=lstOverdueSecond.get(0);
			}
			List<CreditDetailsEntity> lstOverdueThird=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_OVERDUE, new DateRange(DateUtils.addDays(creditInfo.getEventEndDate(),30),sendingDate));
			CreditDetailsEntity overdueThird=null;
			if (lstOverdueThird.size()>0){
				overdueThird=lstOverdueThird.get(0);
			}
			List<CreditDetailsEntity> lstOverdueFourth=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_OVERDUE, new DateRange(DateUtils.addDays(creditInfo.getEventEndDate(),60),sendingDate));
			CreditDetailsEntity overdueFourth=null;
			if (lstOverdueFourth.size()>0){
				overdueFourth=lstOverdueFourth.get(0);
			}
			//продление
			List<CreditDetailsEntity> lstProlong=creditBean.findCreditDetails(creditId, BaseCredit.OPERATION_PROLONG, new DateRange(credit.getCreditdatabeg(),sendingDate));
			CreditDetailsEntity prolong=null;
			if (lstProlong.size()>0){
				prolong=lstProlong.get(0);
			}
			//последний платеж по кредиту
			PaymentEntity pay=paymentService.getLastCreditPayment(credit.getId(), null);
			CreditDetailsEntity creditEndInfo=null;
			if (credit.getIsover()){
				creditEndInfo=creditBean.findLastCreditDetail(credit.getId(), new DateRange(credit.getCreditdatabeg(),sendingDate));
			}
			//заявка
			CreditRequestEntity creditRequest=credit.getCreditRequestId();
			PeopleMainEntity pplmain = creditRequest.getPeopleMainId();
            //персональные данные
            PeoplePersonalEntity pplper = peopleBean.findPeoplePersonalActive(pplmain);
            //документ
            DocumentEntity dc = peopleBean.findPassportActive(pplmain);
            //дополнительные данные
            PeopleMiscEntity pplmsc = peopleBean.findPeopleMiscActive(pplmain);
            //адреса
            AddressEntity addrreg = peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
            AddressEntity addrres = null;
            if (addrreg.getIsSame() == BaseAddress.IS_SAME) {
                addrres = addrreg;
            } else {
                addrres = peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
                if (addrres==null){
                	addrres = addrreg;
                }
            }
            //мобильный телефон
            PeopleContactEntity phone=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
            
            //сделали xml
            String reqString=MakeXML.createCorrectionRecordForEquifax(sendingDate,
            		parEquifax.getUploadVersion(),credit,
        			pplmain,pplper,	dc,addrreg,addrres,	creditRequest,phone,
        			pplmsc,creditInfo,overdueFirst,overdueSecond,prolong,pay,
        			overdueThird,overdueFourth,creditEndInfo);
     		
    		//сгенерили имя для выгрузки
			String fname=generateNameForUpload(sendingDate,isWork,"R","1");
			    		
    		 //создаем новую запись в таблице загрузки
    		UploadingEntity upl=uploadService.newUploading(null, Partner.EQUIFAX, UploadStatus.UPLOAD_CREDIT_CORRECTION, 
        			fname,reqString, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, true);
							
    		return upl;
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createDeleteFileForUpload(Date sendingDate,
			Integer creditId, Boolean isWork) throws KassaException {
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
		if (credit!=null){
			
			PeopleMainEntity pplmain = credit.getPeopleMainId();
			DocumentEntity doc=peopleBean.findPassportActive(pplmain);
			String reqString=credit.getIdCredit()+";"+"RUR;"+
				DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg())+";"+
				DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend())+";"+
				"1;"+doc.getSeries().trim()+doc.getNumber().trim()+";"+
				"Данного контракта не существует;";
			
    		//сгенерили имя для выгрузки
			String fname=generateNameForUpload(sendingDate,isWork,"D","1");
			 //создаем новую запись в таблице загрузки
    		UploadingEntity upl=uploadService.newUploading(null, Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT_DELETE, 
        			fname,reqString, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, true);
							
			return upl;
		}
		return null;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
	  
	  Document doc=XmlUtils.getDocFromString(answer);
	
	  if (doc!=null) {
		  
		  //дата ответа и код ошибки
          req.setResponsedate(Convertor.toDate(XmlUtils.getAttrubuteValue(doc.getElementsByTagName("bki_response").item(0), "datetime"),DatesUtils.FORMAT_ddMMYYYY_HHmmss));
          Integer rc=Convertor.toInteger(XmlUtils.getNodeValueText(doc.getElementsByTagName("responsecode").item(0)));
			
		  req.setResponsecode(rc.toString());
		  req.setResponsemessage(XmlUtils.getNodeValueText(doc.getElementsByTagName("responsestring").item(0)));
			
		  //если запрос не вернул код ошибки и вернул данные
		  if (rc==Requests.RESPONSE_CODE_EQUIFAX_OK){
				
				//ПД из ответа
				String surname=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("title_part").item(0),"private",0),"lastname",0));
				String name=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("title_part").item(0),"private",0),"firstname",0));
				String midname=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("title_part").item(0),"private",0),"middlename",0));
				Date birthday = Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("title_part").item(0),"private",0),"birthday",0)), DatesUtils.FORMAT_ddMMYYYY);
				
				//заявка
				CreditRequestEntity ccre=crDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
				//человек
				PeopleMainEntity pplmain=ccre.getPeopleMainId();
				//ищем все кредиты по этой заявке
				List<CreditEntity> creditList=creditBean.findCredits(pplmain, ccre, null, null, null);
			
				//ищем кредит клиента
				List<CreditEntity> lstClientCredit=creditBean.findCredits(pplmain, ccre,refBooks.getPartnerEntity(Partner.CLIENT), false, null);
				CreditEntity clientCredit=null;
				if (lstClientCredit.size()>0){
					clientCredit=lstClientCredit.get(0);
				}
				
				//настройки для разбора данных
				Map<String,Object> params=productBean.getConsiderProductConfig(ccre.getProductId().getId());
				Integer uniqueCredits=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_CREDITS));
				Integer uniqueDocuments=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_DOCUMENTS));
				
				PeoplePersonalEntity ppl=peopleBean.findPeoplePersonalActive(pplmain);
				//ищем, были ли записаны ПД
				List<PeoplePersonalEntity> lstpplpar=peopleBean.findPeoplePersonal(pplmain, req.getCreditRequestId(), Partner.EQUIFAX, ActiveStatus.ACTIVE);
				//если нет
				if (lstpplpar.size()==0){
				  //если что-то в персональных данных не совпало, пишем запись в БД
				  if (!ppl.getSurname().trim().equalsIgnoreCase(surname)
						  ||!ppl.getName().trim().equalsIgnoreCase(name)||!ppl.getMidname().equalsIgnoreCase(midname)
						  ||ppl.getBirthdate().getTime()!=birthday.getTime()) {
					  
					try {
						peopleBean.newPeoplePersonal(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, 
								  surname, name, midname, null,birthday, null, null,new Date(), ActiveStatus.ACTIVE);
					} catch (Exception e) {
						log.severe("Не удалось сохранить актуальные ПД по заявке "+ccre.getId());
					}
					
				  }//end если что-то не совпало
				}//end если не писали ПД
				
				//все переданные документы
				List<Node> lstdoc=XmlUtils.findNodeList(XmlUtils.findChildInNode(doc.getElementsByTagName("title_part").item(0), "private", 0), "doc");
				if (lstdoc!=null){
				  //ищем, писали ли документы
				  DocumentEntity pasppar=peopleBean.findDocument(pplmain, ccre, Partner.EQUIFAX, ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
				  //если нет
				  if (pasppar==null){	
					for (Node nd:lstdoc){
						int doctype=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd, "doctype", 0)));
						//проверяем только паспорт
						if (doctype==Documents.PASSPORT_RF_EQUIFAX_RS){
							
								// все документы
								List<DocumentEntity> lstDoc=peopleBean.findDocuments(pplmain, null, uniqueDocuments==1?null:Partner.EQUIFAX, null, Documents.PASSPORT_RF);	
								int docs=0;	
								String docnomer=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd,"docno",0));
								if (lstDoc.size()>0) {
							      //проверяем документы на дубликаты
			                      for (int i=0;i<lstDoc.size();i++){
							      
							    	DocumentEntity pasp =lstDoc.get(i);
							     	// Если данные документа не совпадают, пишем в БД
							    	if (!docnomer.equals(pasp.getSeries()+pasp.getNumber())){
							    		docs++;
							    	}//end пишем в БД
								  }//end for проверяем на дубликаты
								}//есть в БД паспорта у человека
								
							
								//Если данные документа не совпадают, пишем в БД
								if (docs==lstDoc.size()){
									try {
										DocumentEntity newPasp=peopleBean.newDocument(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, 
												docnomer.substring(0, 4), docnomer.substring(4,10), 
												Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd,"docdate",0)), DatesUtils.FORMAT_ddMMYYYY), 
												null, XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nd,"docplace",0)), ActiveStatus.ACTIVE);
										lstDoc.add(newPasp);
									} catch (Exception e){
										log.severe("Не удалось записать активный документ по заявке "+ccre.getId()+", ошибка "+e);
									}
									
								}//end если данные не совпадают
						
						}//end если это паспорт
							
					}//end for документов
				  }//end если не писали документы
				}//end если документы есть в ответе
				
				//если есть блок с историей
				if (XmlUtils.isExistNode(doc, "history_title"))	{
					List<Node> lsthist=XmlUtils.findNodeList(doc.getElementsByTagName("private").item(0), "history_title");
					//ищем, есть ли архивные ПД
					if (lsthist!=null){
						//ищем, писали ли архивные данные по этому партнеру
						List<PeoplePersonalEntity> lsthistper=peopleBean.findPeoplePersonal(pplmain, ccre, Partner.EQUIFAX, ActiveStatus.ARCHIVE);
						if (lsthistper.size()==0){
						  for (Node ndhist:lsthist) {
							//данные из ответа
							String surnameh=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhist,"lastname",0));
							String nameh=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhist,"firstname",0));
							String midnameh=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhist,"middlename",0));
							Date dateh = Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhist,"date",0)), DatesUtils.FORMAT_ddMMYYYY);
							//если предыдущие ФИО не совпадают с настоящими, пишем в БД
							if (!surname.equalsIgnoreCase(surnameh)
									||!name.equalsIgnoreCase(nameh)||!midname.equalsIgnoreCase(midnameh)){
								
								try {
									peopleBean.newPeoplePersonal(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, 
											  surnameh, nameh, midnameh, null,birthday, null, null,dateh, ActiveStatus.ARCHIVE);
								} catch (Exception e) {
									log.severe("Не удалось сохранить актуальные ПД по заявке "+ccre.getId());
								}
							
							}//end если предыдущие фио не совпадают с настоящими
							
							List<Node> lsthdoc=XmlUtils.findNodeList(ndhist, "doc");
							//смотрим предыдущие документы
							if (lsthdoc!=null){
								//ищем все документы по этому человеку
								List<DocumentEntity> lstDoc=peopleBean.findDocuments(pplmain, null, null, null, Documents.PASSPORT_RF);
								
								if (lstDoc.size()>0){
								  for(Node ndhdoc:lsthdoc){
									int doctypeh=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc, "doctype", 0)));
									//проверяем только паспорт
									if (doctypeh==Documents.PASSPORT_RF_EQUIFAX_RS)	{
										String docnomer=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc,"docno",0));
										int docs=0;
										//проверяем документы на дубликаты
					                    for (int i=0;i<lstDoc.size();i++){
					                    
					                      DocumentEntity pasp =	lstDoc.get(i);
										  String dnomer=pasp.getSeries().trim()+pasp.getNumber().trim();
										  //Если данные документа не совпадают, пишем в БД
										  if (!dnomer.equalsIgnoreCase(docnomer))	{
										     docs++;
										  }//end если данные документа не совпадают
					                    }//end проверяем на дубликаты
					                    if (docs==lstDoc.size()){
					                        try {
											  DocumentEntity newPasp=peopleBean.newDocument(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, 
													XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc, "docno", 0)).substring(0, 4), 
													XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc, "docno", 0)).substring(4,10), 
													Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc,"docdate",0)), DatesUtils.FORMAT_ddMMYYYY), 
													null, XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndhdoc, "docplace", 0)), ActiveStatus.ARCHIVE);
											  lstDoc.add(newPasp);
										    } catch (Exception e){
											  log.severe("Не удалось записать архивный документ по заявке "+ccre.getId()+", ошибка "+e);
										    }
					                    }//end пишем в БД
									}//end проверяем только паспорт
								}//end for
							  }//end смотрим предыдущие документы
						    }//end for history
						  }//end паспорт не null
						}//end писали ли архивные данные
					}//end есть ли архивные ПД
						
				}//end есть ли блок с историей
			
				//если есть данные по предыдущим адресам
				if (XmlUtils.isExistNode(doc, "history_addr")){
					AddressEntity addrpar=peopleBean.findAddress(pplmain, Partner.EQUIFAX, ccre, BaseAddress.REGISTER_ADDRESS, ActiveStatus.ARCHIVE);
					if (addrpar==null){
					  //адрес регистрации - пишем всегда, если его еще не писали, т.к. возвращается текст
					  if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_reg", 0)) {
						  if (!XmlUtils.getNodeValueText(XmlUtils.findChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_reg", 0)).equalsIgnoreCase("нет данных")){
					         
							  try {
								  peopleBean.newAddressFias(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, FiasAddress.REGISTER_ADDRESS, 
										  XmlUtils.getNodeValueText(XmlUtils.findChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_reg", 0)), 
										  Convertor.toDate(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_reg", 0), "date"),DatesUtils.FORMAT_ddMMYYYY),
										  null,BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ARCHIVE,null,null,null,null,null);
							  } catch (Exception e){
								  log.severe("Не удалось записать архивный адрес по заявке "+ccre.getId()+", ошибка "+e);
							  }
							
						  }//end если есть данные
					  }//end пишем архивный адес регистрации
					
					  //адрес фактический - пишем всегда, если его еще не писали, т.к. возвращается текст
					  if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_fact", 0)) {
						  try {
							  peopleBean.newAddressFias(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, FiasAddress.RESIDENT_ADDRESS, 
									  XmlUtils.getNodeValueText(XmlUtils.findChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_fact", 0)), 
									  Convertor.toDate(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(doc.getElementsByTagName("history_addr").item(0), "addr_fact", 0), "date"),DatesUtils.FORMAT_ddMMYYYY),
									  null,BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ARCHIVE,null,null,null,null,null);
						  } catch (Exception e){
							  log.severe("Не удалось записать архивный адрес по заявке "+ccre.getId()+", ошибка "+e);
						  }
					   
					  }//end пишем архивный адрес проживания
					}//end не писали архивные адреса
				}//end если есть данные по предыдущим адресам
			
				
				//запишем снилс и инн
				if (pplmain!=null) {
					try {
						peopleBean.savePeopleMain(pplmain, 
								XmlUtils.isExistNode(doc, "inn")?XmlUtils.getNodeValueText(doc.getElementsByTagName("inn").item(0)):"", 
								XmlUtils.isExistNode(doc, "pfno")?XmlUtils.getNodeValueText(doc.getElementsByTagName("pfno").item(0)):"");
					} catch (PeopleException e) {
						log.severe("Не удалось записать инн и снилс по человеку "+pplmain.getId()+", ошибка "+e);
					}
				}//end пишем снилс и инн
				
				//запросы
				if (XmlUtils.isExistNode(doc, "add_part")){
					
					Node inforec=XmlUtils.findChildInNode(doc.getElementsByTagName("add_part").item(0), "info_requests",  0);
					//ищем, есть ли summary кол-ву запросов
					List<SummaryEntity> lstr=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.EQUIFAX), refBooks.getRefSummaryItem(Summary.REQUESTS_ALL).getEntity());
					if (lstr==null) {
					    List<Node> lstRequests=XmlUtils.findNodeList(inforec,"request");
					    if (lstRequests.size()>0){
					    	int reccnt=0;
					    	int reccntM=0;
					    	for (Node request:lstRequests){
					    		if (XmlUtils.getNodeValueText(XmlUtils.findChildInNode(request, "timeslot", 0)).equalsIgnoreCase("week")){
					    			reccnt++;
					    		}
					    		if (XmlUtils.getNodeValueText(XmlUtils.findChildInNode(request, "timeslot", 0)).equalsIgnoreCase("month")){
					    			reccntM++;
					    		}
					    	}//end for
					    	//если запросов за неделю больше нуля, запишем
					    	if (reccnt>0){
					    		creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_WEEK).getEntity(),null);	
					    	}
					    	//если запросов за месяц больше нуля, запишем
					    	if (reccntM>0){
					    		creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(reccntM+reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_MONTH).getEntity(),null);	
					    	}
					    }//end были запросы
					}//end нет summary
				}
				
				
				//если есть основная часть
				if (XmlUtils.isExistNode(doc, "base_part"))	{
				  AddressEntity addrpar=peopleBean.findAddress(pplmain, Partner.EQUIFAX, ccre, BaseAddress.REGISTER_ADDRESS, ActiveStatus.ACTIVE);
				  if (addrpar==null) {
				    //адрес регистрации	- пишем всегда, если его еще не писали, т.к. возвращается текст
				    if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("base_part").item(0), "addr_reg", 0)) {
					  try {
						  peopleBean.newAddressFias(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, FiasAddress.REGISTER_ADDRESS, 
								  XmlUtils.getNodeValueText(XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "addr_reg", 0)), 
								  new Date(),null, BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ACTIVE,null,null,null,null,null);
					  } catch (Exception e){
						  log.severe("Не удалось записать активный адрес по заявке "+ccre.getId()+", ошибка "+e);
					  }
				     
				    }//end если есть адрес регистрации
				
				    //фактический адрес - пишем всегда, если его еще не писали, т.к. возвращается текст
				    if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("base_part").item(0), "addr_fact", 0)) {
				    	 try {
							  peopleBean.newAddressFias(null,pplmain.getId(), ccre.getId(), Partner.EQUIFAX, FiasAddress.RESIDENT_ADDRESS, 
									  XmlUtils.getNodeValueText(XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "addr_fact", 0)), 
									  new Date(),null,BaseAddress.COUNTRY_RUSSIA, ActiveStatus.ACTIVE,null,null,null,null,null);
						  } catch (Exception e){
							  log.severe("Не удалось записать активный адрес по заявке "+ccre.getId()+", ошибка "+e);
						  }
				     
				    }//end если есть адрес проживания
				  }//end если не писали адреса
				  
				  //телефоны
				  if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("base_part").item(0), "phones", 0)){
					 
					  List<Node> lstphones=XmlUtils.findNodeList(doc.getElementsByTagName("phones").item(0), "phone_number");
					  if (lstphones.size()>0){
						  //писали ли телефоны
						  List<PeopleContactEntity> lstContact=peopleBean.findPeopleByContact(null, null, pplmain.getId(), 
								  Partner.EQUIFAX, ActiveStatus.ACTIVE, ccre.getId());
						  //если не писали
						  if (lstContact.size()==0){
							  //проверяем, работает ли сайт с БД телефонов
							  Map<String,Object> sparams=rulesBean.getSiteConstants();
						         String site = (String) sparams.get(SettingsKeys.CONNECT_DB_URL); 
						         boolean hasConnection=false;
						         try {
									hasConnection=HTTPUtils.isConnected(site);
								} catch (Exception e1) {
									log.severe("Не удалось соединиться с сайтом "+site);
								}  
						    for (Node ndphone:lstphones){
						    	// TODO если этот телефон был дан клиентом, записывать соответствие с ним в БД
						    	String phoneNumber=StringUtils.isNotEmpty(XmlUtils.getNodeValueText(ndphone))?XmlUtils.getNodeValueText(ndphone):" ";
						    	if (StringUtils.isEmpty(phoneNumber)||phoneNumber.length()<10||phoneNumber.length()>11){
						    		continue;
						    	} else if (phoneNumber.length()==10){
						    		phoneNumber=String.valueOf(PeopleContact.PHONE_COUNTRY_CODE_RU)+phoneNumber;
						    	} 
						    	
						    	if (!peopleBean.phoneExists(pplmain.getId(),phoneNumber)){								  
							      try {
								  
								    ReferenceEntity ref=refBooks.findFromKb(RefHeader.CONTACT_TYPE_RS, Convertor.toInteger(XmlUtils.getAttrubuteValue(ndphone, "phone_type"))); 
								    Integer phoneType=PeopleContact.CONTACT_OTHER;
								    if (ref!=null){
									  phoneType=ref.getCodeinteger();
								    }
								    peopleBean.setPeopleContact(pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX), 
								      phoneType, phoneNumber, false, ccre.getId(),
								      Convertor.toDate(XmlUtils.getAttrubuteValue(ndphone, "date"),DatesUtils.FORMAT_ddMMYYYY),
								      hasConnection);
							    } catch (Exception e) {
								    log.severe("Не удалось записать телефоны, ошибка "+e);
							    } 
							  }//end если этого телефона нет
						    }//end for
						  }//end если не писали
					  }//end if size>0
				  }//end если есть телефоны
				  
				  //недееспособность
				  if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("base_part").item(0), "incapacity", 0)){
					    Node incapacity=XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "incapacity", 0);
					    //проверяем, писали ли недееспособность
					    List<PeopleIncapacityEntity> lstIncapacity=peopleBean.findPeopleIncapacity(pplmain.getId(), ccre.getId(), Partner.EQUIFAX);
					    if (lstIncapacity.size()==0){
						    try {
							  peopleBean.newPeopleIncapacity(pplmain.getId(), ccre.getId(), Partner.EQUIFAX, 
									  Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(incapacity, "decision_date", 0)),DatesUtils.FORMAT_ddMMYYYY), 
									  Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(incapacity, "incapacity_type", 0))));
						    } catch (Exception e) {
							  log.severe("Не удалось записать недееспособность, ошибка "+e);
						    }
					    }//end если не писали
				  }//end если есть недееспособность
				  
				  //суды, коллекторы
				  if (XmlUtils.isExistChildInNode(doc.getElementsByTagName("base_part").item(0), "collection", 0)){
					  List<Node> lstc=XmlUtils.findNodeList(doc.getElementsByTagName("base_part").item(0), "collection");
					  if (lstc.size()>0){
						//ищем, писали ли данные 	
						  List<DebtEntity> lstDebt=creditInfo.listDebts(ccre.getId(), pplmain.getId(), Partner.EQUIFAX, null, Debt.DEBT_COURT, null, null);
						  //если не писали
						  if (lstDebt.size()==0) {
							  
							for (Node nodec:lstc){
								//проверяем, был ли записан долг ранее
			            		DebtEntity oldDebt=debtDAO.findDebt(pplmain.getId(), Partner.EQUIFAX, Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_sum", 0))), 
			            				 Debt.DEBT_COURT,Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_decision_date", 0)),DatesUtils.FORMAT_ddMMYYYY));
								if (oldDebt==null){
								    creditInfo.newDebt(ccre.getId(), pplmain.getId(), Partner.EQUIFAX, null,
										Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_sum", 0))), 
										null,null, XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "court_executive_name", 0)), 
										null,XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_decision_no", 0)), 
										XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_info", 0)),null, null,	
										Debt.DEBT_COURT,Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_decision_date", 0)),DatesUtils.FORMAT_ddMMYYYY),
										Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodec, "collection_paid", 0)))==0?ActiveStatus.ACTIVE:ActiveStatus.ARCHIVE);
								}
							}//end for
						  }//end если не писали
					  }//end size>0
				  }//end суды
				  
				  //общее кол-во информации из судов
				  Node ndc=XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "court", 0);
				  if (ndc!=null) {
						List<SummaryEntity> lstsum=summaryDAO.findSummary(pplmain, req.getCreditRequestId(), refBooks.getPartnerEntity(Partner.EQUIFAX), refBooks.getRefSummaryItem(Summary.INFO_COURT).getEntity());
						if (lstsum==null){
					      List<Node> lstcourt=XmlUtils.findNodeList(ndc, "info_court");
					      if (lstcourt.size()>0) {
						      creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(lstcourt.size()), refBooks.getRefSummaryItem(Summary.INFO_COURT).getEntity(),null);  
					      }
						}
					}
					//общее кол-во информации из официальных источников
					Node ndo=XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "official", 0);
					if (ndo!=null){
						List<SummaryEntity> lstsum=summaryDAO.findSummary(pplmain, req.getCreditRequestId(), refBooks.getPartnerEntity(Partner.EQUIFAX), refBooks.getRefSummaryItem(Summary.INFO_OFFICIAL).getEntity());	
						if (lstsum==null){
				  	      List<Node> lstof=XmlUtils.findNodeList(ndo, "info_official");
					      if (lstof.size()>0) {
						      creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(lstof.size()), refBooks.getRefSummaryItem(Summary.INFO_OFFICIAL).getEntity(),null);    
					      }
						}
					}
					
					//общее кол-во информации о банкротстве
					Node ndb=XmlUtils.findChildInNode(doc.getElementsByTagName("base_part").item(0), "bankruptcy", 0);
					if (ndb!=null){
						List<SummaryEntity> lstsum=summaryDAO.findSummary(pplmain, req.getCreditRequestId(), refBooks.getPartnerEntity(Partner.EQUIFAX), refBooks.getRefSummaryItem(Summary.INFO_BANKRUPCY).getEntity());
						if (lstsum==null){
					      List<Node> lstban=XmlUtils.findNodeList(ndb, "info_bankruptcy");
					      if (lstban.size()>0) {
						      creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(lstban.size()), refBooks.getRefSummaryItem(Summary.INFO_BANKRUPCY).getEntity(),null);  
					      }
						}
					}
					
					int numcredits=0;
					//кредиты
					List<Node> lstcred=XmlUtils.findNodeList(doc.getElementsByTagName("base_part").item(0), "credit");
					if (lstcred.size()>0){
						//были ли уже записаны кредиты
						List<CreditEntity> lstcredpar=creditBean.findCredits(pplmain, ccre, refBooks.getPartnerEntity(Partner.EQUIFAX), null, null);
						//если нет
						if (lstcredpar.size()==0){
						  //делаем старые кредиты неактивными
						  creditDAO.makePartnerCreditsNotActive(Partner.EQUIFAX,pplmain.getId());
						 
						  for (Node nodecr:lstcred) {
							//если этот кредит выдан не нашей организацией
							Integer i=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_owner", 0)));
							if (i==0) {
								numcredits++;
								//ищем системную верификацию, если есть кредит клиента
								VerificationEntity verification=null;
								if (clientCredit!=null){
								    verification=creditInfo.findOneVerification(ccre.getId(), pplmain.getId(), Partner.SYSTEM, null);
								}
								creditList=saveEquifaxCredit(nodecr,pplmain,ccre,doc,clientCredit,
										creditList,uniqueCredits,verification);
									
							}//end если кредит выдан чужой организацией
						  }//end for
						}//end были ли записаны кредиты
					}//end есть кредиты
					
					//запишем кол-во кредитов, если не писали
					List<SummaryEntity> lstsum=summaryDAO.findSummary(pplmain, req.getCreditRequestId(), refBooks.getPartnerEntity(Partner.EQUIFAX), refBooks.getRefSummaryItem(Summary.CLIENT_OWNER).getEntity());
					if (lstsum==null){
				      if (numcredits>0) {
					      creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.EQUIFAX),String.valueOf(numcredits), refBooks.getRefSummaryItem(Summary.CLIENT_OWNER).getEntity(),null);  
				      }
					}
					
								
					req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
					req.setResponsemessage("Данные найдены");
			  }
			  //данные найдены, но кредитной истории нет
			  else {
					req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
					req.setResponsemessage("По данному заемщику нет кредитной истории");
			  }
			
					
			}//end если вернулся продуктивный ответ
			
			else if (rc==Requests.RESPONSE_CODE_EQUIFAX_NOT_FOUND) {
					//такого человека в базе КБ нет	
					req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
					req.setResponsemessage("Данные не найдены");
			} else {
				    //если вернулась ошибка
					req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
					req.setResponsecode(rc.toString());
					req.setResponsemessage(XmlUtils.getNodeValueText(doc.getElementsByTagName("responsestring").item(0)));
								
			}
			
			//********************
			//если есть скоринг
			if (XmlUtils.isExistNode(doc, "scorings")){
				PeopleMainEntity pplmain=req.getCreditRequestId().getPeopleMainId();
				List<Node> lstscor=XmlUtils.findNodeList(doc.getElementsByTagName("scorings").item(0), "scoring");
				if (lstscor.size()>0){
					//ищем, были ли записаны скоринги по заявке
					List<ScoringEntity> scorings=creditInfo.findScorings(req.getCreditRequestId().getId(), pplmain.getId(), Partner.EQUIFAX, null);
					if (scorings.size()==0){
					  for (Node ndscor:lstscor) {
						ScoreModel model = refBooks.getModelByCode(refBooks.getPartnerEntity(Partner.EQUIFAX), XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndscor, "scor_card_id", 0)));
						if (model!=null){
						    creditInfo.saveScoring(req.getCreditRequestId().getId(), pplmain.getId(), 
								Partner.EQUIFAX, model.getId(), 
								null, Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndscor, "score", 0)), CalcUtils.dformat), 
								XmlUtils.getNodeValueText(XmlUtils.findChildInNode(ndscor, "scor_error", 0)),null);
						}
						
					  }//end for
					}//end если не писали скоринги
				}//end если есть скоринги
			}//end если в документе есть тег со скорингом			
			
		   //**************
	
		}
		return req;
	}
	
	/**
	 * генерим xml для запроса
	 * @param creditRequest - заявка
	 * @param request - запрос
	 * @param isWork - тестовый сервис или рабочий
	 * @return
	 */
	private String createRequestString(CreditRequestEntity creditRequest,
			RequestsEntity request,boolean isWork){
        
		Partner parEquifax = refBooks.getPartner(Partner.EQUIFAX);
		
		//человек
		PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
		//ПД
		PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
		//Документ
		DocumentEntity dc=peopleBean.findPassportActive(pplmain);
		//Доп.данные
		PeopleMiscEntity pplmsc=peopleBean.findPeopleMiscActive(pplmain);
		//Адрес регистрации
		AddressEntity addrreg=peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
		//Адрес проживания
		AddressEntity addrres=null;
		if (addrreg.getIsSame()==BaseAddress.IS_SAME){
			  addrres=addrreg;
		}   else {
			  addrres=peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
		}
		//занятость
		EmploymentEntity employ=peopleBean.findEmployment(pplmain, null, 
				refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);
		//PeopleContactEntity cellPhone=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
		//PeopleContactEntity email=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, pplmain.getId());
		PeopleContactEntity email=null;
		PeopleContactEntity cellPhone=null;
		//параметры для запроса
		String partnerId=isWork?parEquifax.getCodeWork():parEquifax.getCodeTest();
		
		String group=isWork?parEquifax.getGroupWork():parEquifax.getGroupTest();
		//формируем xml
		String	rmessage = MakeXML.createXMLforEquifax(pplmain,pplper,pplmsc,cellPhone,email,dc,addrreg,addrres,employ,
				creditRequest,request,partnerId,group,parEquifax.getRequestVersion());
		log.info(rmessage);
		log.info("Сформирован запрос в Эквифакс по заявке "+creditRequest.getId());
		return rmessage;
	}
	
	/**
	 * возвращаем запись для выгрузки
	 * @param creditRequest - заявка
	 * @param uploadDateRange - даты для выгрузки
	 * @param i - номер по порядку
	 * @return
	 */
	private Object[] listDataForUploadCR(CreditRequestEntity creditRequest,
			DateRange uploadDateRange,int i){
		 PeopleMainEntity pplmain = creditRequest.getPeopleMainId();
         //персональные данные
         PeoplePersonalEntity pplper = peopleBean.findPeoplePersonalActive(pplmain);
         //документ
         DocumentEntity dc = peopleBean.findPassportActive(pplmain);
         //дополнительные данные
         PeopleMiscEntity pplmsc = peopleBean.findPeopleMiscActive(pplmain);
         //адреса
         AddressEntity addrreg = peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
         AddressEntity addrres = null;
         if (addrreg!=null&&addrreg.getIsSame()!=null&&addrreg.getIsSame() == BaseAddress.IS_SAME) {
             addrres = addrreg;
         } else {
             addrres = peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
             if (addrres==null){
             	addrres = addrreg;
             }
         }
         //мобильный телефон
         PeopleContactEntity phone=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
         
         CreditEntity cr = null;
                    
         //если по заявке есть кредит
         if (creditRequest.getAcceptedcreditId()!=null){
         	//кредит
             cr=creditRequest.getAcceptedcreditId();
         }
         return new Object[]{i, pplmain, pplper, pplmsc, dc, addrreg, addrres, creditRequest, cr, phone};
	}
	
	/**
	 * возвращаем запись для выгрузки
	 * @param creditRequest - заявка
	 * @param uploadDateRange - даты для выгрузки
	 * @param i - номер по порядку
	 * @return
	 */
	private Object[] listDataForUpload(CreditRequestEntity creditRequest,
			DateRange uploadDateRange,int i){
        PeopleMainEntity pplmain = creditRequest.getPeopleMainId();
        //персональные данные
        PeoplePersonalEntity pplper = peopleBean.findPeoplePersonalActive(pplmain);
        //документ
        DocumentEntity dc = peopleBean.findPassportActive(pplmain);
        //дополнительные данные
        PeopleMiscEntity pplmsc = peopleBean.findPeopleMiscActive(pplmain);
        //адреса
        AddressEntity addrreg = peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
        AddressEntity addrres = null;
        if (addrreg.getIsSame() == BaseAddress.IS_SAME) {
            addrres = addrreg;
        } else {
            addrres = peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
            if (addrres==null){
        	    addrres = addrreg;
            }
        }
        //мобильный телефон
        PeopleContactEntity phone=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
    
        CreditEntity credit = null;
        PaymentEntity pay = null;
        CreditDetailsEntity payment=null;
        Double sm=new Double(0);
        //сумма сделанных платежей
        Double sumOperation=new Double(0);
        //сумма процентов по сделанным платежам
        Double sumPercent=new Double(0);
        List<DebtEntity> debts=new ArrayList<DebtEntity>(0);
        //передача в суд
        DebtEntity debtCourt=null;
        //есть ли решение суда
        Boolean hasCourtDecision=false;
        //просрочка
        List<CreditDetailsEntity> overdue =new ArrayList<CreditDetailsEntity>(0);
        //если по заявке есть кредит
        if (creditRequest.getAcceptedcreditId()!=null){
    	    //кредит
            credit=creditRequest.getAcceptedcreditId();
            //оплата 
            pay=paymentService.getLastCreditPayment(credit.getId(), null);
            /*List<CreditDetailsEntity> lst=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_PAYMENT, uploadDateRange);
            if (lst.size()>0){
        	    payment=lst.get(0);
            }*/
            //оплата кредита
            sm=paymentService.getCreditPaymentSum(credit.getId());

            debts=creditInfo.listDebts(creditRequest.getId(), null, Partner.SYSTEM, null,Debt.DEBT_COURT,null,uploadDateRange);
            //просрочка - берем за предыдущий день
            //overdue=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, new DateRange(DateUtils.addDays(sendingDate, -1),sendingDate));
        
            sumOperation=creditBean.getSumCreditDetail("amountOperation", credit.getId(), BaseCredit.OPERATION_PAYMENT, uploadDateRange);
            sumPercent=creditBean.getSumCreditDetail("amountPercent", credit.getId(), BaseCredit.OPERATION_PAYMENT, uploadDateRange);
            //информация по передаче дела в суд
        
            if (debts.size()>0){
        	    debtCourt=debts.get(0);
        	    hasCourtDecision=(debtCourt.getDateDecision()!=null);
            }
        }
        return new Object[]{i, pplmain, pplper, pplmsc, dc, addrreg, addrres, creditRequest, credit, 
    		pay,sm,phone,debts,sumOperation,sumPercent,debtCourt,hasCourtDecision,overdue,payment};
  }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		Partner parEquifax=refBooks.getPartner(Partner.EQUIFAX);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
		Date firstDate=DatesUtils.makeDate(2010, 1, 1);
		uploadDateRange.setFrom(firstDate);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
		//выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,creditIds);
		log.info("Всего записей для выгрузки "+lstcre.size());
		   if (lstcre.size() > 0) {
	        	//создаем новую запись в таблице загрузки
	        	UploadingEntity upl=uploadService.newUploading(null, Partner.EQUIFAX, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
	        			null,null, null, null, null, null, null, null, null, true);
	    	    		
	        	//создаем заголовок документа
	        	org.dom4j.Document xmlDoc=MakeXML.createDocumentUploadForEquifax(uploadDateRange.getTo(), parEquifax.getUploadVersion());		
	            Integer i = 1;
	            for (CreditRequestEntity cre : lstcre) {
	                
	            	 //добавляем запись в XML
	                MakeXML.createUploadRecordForSendingToEquifax(uploadDateRange.getTo(), 
	            			listDataForUpload(cre,uploadDateRange,i),xmlDoc);
	                //пишем запись в таблицу detail
	                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
	                i++;
	            }
	            //Закончили выгрузку в xml
	          			
				String str="";
				try {
					//возвращаем XML строкой
				    str=MakeXML.returnDocumentUploadForEquifax(xmlDoc, i-1);
				} catch (Exception e) {
					log.severe("Не удалось сформировать xml для выгрузки "+e);
					throw new KassaException("Не удалось сформировать xml для выгрузки "+e);
				}
					
				//сгенерили имя для выгрузки
				String fname=generateNameForUpload(uploadDateRange.getTo(),isWork,"U","2");
				//сохранили данные в таблицу выгрузки
				upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
	        			fname,str, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
				
				log.info("Создали файл для выгрузки в Эквифакс, кол-во записей "+lstcre.size());
				return upl;
	        } else {
			    return null;
	        }
	}
	
	/**
	 * сохраняем кредит
	 * @param nodecr - нода, в которой лежит кредит
	 * @param pplmain - человек
	 * @param ccre - заявка
	 * @param doc - документ
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private List<CreditEntity> saveEquifaxCredit(Node nodecr,PeopleMainEntity pplmain,CreditRequestEntity ccre,
			Document doc,CreditEntity clientCredit,List<CreditEntity> creditList,Integer unique,
			VerificationEntity verification){
		CreditEntity cre=new CreditEntity();
		
	    //дата начала кредита
	    cre.setCreditdatabeg(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_date", 0)), DatesUtils.FORMAT_ddMMYYYY));
	    
	    //дата окончания по графику
	    if (XmlUtils.isExistChildInNode(nodecr, "cred_enddate", 0)) {
	      cre.setCreditdataend(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_enddate", 0)), DatesUtils.FORMAT_ddMMYYYY));
	    }
	    
	    //сумма кредита
	    cre.setCreditsum(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum", 0)), CalcUtils.dformat));
	    //валюта кредита
	    cre.setIdCurrency(refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE, XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_currency", 0))));
	   
	    //id кредита у Эквифакса
	    String creditId=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_id", 0));
		cre.setCreditAccount(creditId);
		//вид кредита
		if (XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_type", 0)).equalsIgnoreCase("00")) {
			cre.setCredittypeId(refBooks.getCreditType(BaseCredit.ANOTHER_CREDIT).getEntity());
		} else {
		    cre.setCredittypeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_TYPE, Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_type", 0)))));
		}
		 //максимально просрочки
		if (StringUtils.isNotEmpty(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_day_overdue", 0)))){
			int overdueDays=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_day_overdue", 0)));
		    cre.setMaxDelay(overdueDays);
		    if (overdueDays==0){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
		    } else if (overdueDays>1&&overdueDays<30){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		    } else if (overdueDays>=30&&overdueDays<60){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		    } else if (overdueDays>=60&&overdueDays<90){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		    } else if (overdueDays>=90&&overdueDays<120){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
		    } else if (overdueDays>=120){
		    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_FOUR_MONTH));
		    } 
		} else {
	    	cre.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
	    }	
		
		//дата окончания
	    if (XmlUtils.isExistChildInNode(nodecr, "cred_enddate_fact", 0)) {
	        cre.setCreditdataendfact(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_enddate_fact", 0)), DatesUtils.FORMAT_ddMMYYYY));
	        cre.setIsover(true);
	    } else {
	    	cre.setIsover(false);
	    }
		//проверим на существование в БД такого же кредита
        if (creditList.size()>0){
        	for (CreditEntity credit:creditList){
        		try{
        			 if (creditService.isMatch(cre, credit)&&unique==1){
        				//если это кредит клиента, то запишем верификацию
         		    	if (clientCredit!=null&&credit.getPartnersId().getId()==Partner.CLIENT){
         		    		log.info("запишем системную верификацию Эквифакс по заявке "+ccre.getId());
         		    		creditInfo.saveSystemVerification(verification, ccre.getId(), pplmain.getId(), 
         	            			clientCredit, cre,VerificationConfig.MARK_FOR_BANK);
         		    	}
         			    return creditList;
          		    }
        		} catch (Exception e){
        			log.severe("Не удалось сравнить кредиты, ошибка "+e);
        		}
        	}
        }
        //если есть кредит клиента, запишем верификацию
        if (clientCredit!=null){
        	log.info("запишем системную верификацию Эквифакс по заявке "+ccre.getId());
              	creditInfo.saveSystemVerification(verification, ccre.getId(), pplmain.getId(), 
            			clientCredit, cre,VerificationConfig.MARK_FOR_BANK);
        }
		//отношение субъекта к данному кредиту
		Integer ii=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_ratio", 0)));
		if (ii!=null){
		  try {	
		      ReferenceEntity rfe=refBooks.findFromKb(RefHeader.CREDIT_RELATION_STATE_EQUIFAX, ii);
		      cre.setCreditrelationId(rfe);
		  } catch (Exception e) {
			  log.severe("Не удалось найти в справочнике значение "+ii);
		  }
		}
		 //полная стоимость займа в процентах
	    if (XmlUtils.isExistChildInNode(nodecr, "cred_full_cost", 0)) {
	    	cre.setCreditFullCost(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_full_cost", 0)), CalcUtils.dformat_xx));
	    }
	   
	    //сумма задолженности на тек. момент
	    if (!XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum_debt", 0)).equals("-")) {
	        cre.setCreditsumdebt(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum_debt", 0)), CalcUtils.dformat));
	    }
		//текущий неиспользованный лимит
		if(XmlUtils.isExistChildInNode(nodecr, "cred_sum_limit", 0)) {
			if (!XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum_limit", 0)).equals("-"))
			  cre.setCreditlimitunused(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum_limit", 0)), CalcUtils.dformat));
		}
		//статус кредита
		cre.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE, Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_active", 0)))));
		//дата статуса
		cre.setDateStatus(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_active_date", 0)), DatesUtils.FORMAT_ddMMYYYY));
	
	    //кол-во продлений
		if (XmlUtils.isExistChildInNode(nodecr, "cred_prolong", 0)){
		    cre.setCreditlong(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_prolong", 0))));
		}
	    //просрочек до 5 дней
		Integer delay5=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay5", 0)));
		cre.setDelay5(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay5", 0))));
	    //просрочек от 6 до 30 дней
		Integer delay30=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay30", 0)));
		if (Utils.defaultIntegerFromNull(delay5)>0||Utils.defaultIntegerFromNull(delay30)>0){
			cre.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		}
		cre.setDelay30(Utils.defaultIntegerFromNull(delay5)+Utils.defaultIntegerFromNull(delay30));
	    //просрочек от 31 до 60 дней
		cre.setDelay60(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay60", 0))));
		if (cre.getDelay60()!=null&&cre.getDelay60()>0){
			cre.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		}
		//просрочек от 61 до 90 дней
		cre.setDelay90(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay90", 0))));
        
		if (cre.getDelay90()!=null&&cre.getDelay90()>0){
			cre.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		}
		
		//просрочек более 90 дней
		cre.setDelaymore(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "delay_more", 0))));
		//худший просроченный статус
		if (cre.getDelaymore()!=null&&cre.getDelaymore()>0){
			cre.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
			if (cre.getOverduestateId()!=null&&cre.getOverduestateId().getCode().equalsIgnoreCase(BaseCredit.OVERDUE_FOUR_MONTH)){
				cre.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_FOUR_MONTH));
			}
		} 
		
		if ((cre.getWorstOverdueStateId()==null&&cre.getOverduestateId()!=null)
				||(cre.getWorstOverdueStateId()!=null&&cre.getOverduestateId()!=null&&cre.getWorstOverdueStateId().getId()<cre.getOverduestateId().getId())){
			cre.setWorstOverdueStateId(cre.getOverduestateId());
		} 
		
	    //текущий просроченный долг
		cre.setCurrentOverdueDebt(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_sum_overdue", 0)), CalcUtils.dformat));
	    //максимальный просроченный долг
		cre.setMaxOverdueDebt(Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_max_overdue", 0)), CalcUtils.dformat));
	   
	    //погашение за счет обеспечения
		cre.setCreditmoneyback(Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_collateral", 0))));
	    //дата обновления
		cre.setDatelastupdate(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodecr, "cred_update", 0)), DatesUtils.FORMAT_ddMMYYYY));
	    
	    String str="";
	    String str1="";
	    String str2="";
	    String strs="";
	    String str1s="";
	    String str2s="";
	    //если есть платежная дисциплина
		if (XmlUtils.isExistNode(doc, "attrs"))
		{
			//nodelist с пакетом
			List<Node> lstpack=XmlUtils.findNodeList(doc.getElementsByTagName("attrs").item(0), "package");
			if (lstpack.size()>0)
			{
				for (Node ndpack:lstpack)
				{
					//nodelist с секцией
					List<Node> lstsect=XmlUtils.findNodeList(ndpack, "section");
					if (lstsect.size()>0)
					{
						for (Node ndsect:lstsect)
						{
							//nodelist c периодом
							List<Node> lstper=XmlUtils.findNodeList(ndsect, "period");
							if (lstper.size()>0)
							{
								for (Node ndper:lstper)
								{
									//nodelist с атрибутами
									List<Node> lstattr=XmlUtils.findNodeList(ndper, "attr");
									if (lstattr.size()>0)
									{
										for (Node ndattr:lstattr)
										{
											//если это дисциплина к этому кредиту
											if (XmlUtils.getAttrubuteValue(ndattr, "c_i").equalsIgnoreCase(creditId)){
											  //код
											  str=XmlUtils.getAttrubuteValue(ndattr, "n");
											  if (str.equalsIgnoreCase(BaseCredit.CODE_PAY_DISC)){																	  
												  strs=XmlUtils.getAttrubuteValue(ndattr, "v");
											  }
											  //сумма
											  str1=XmlUtils.getAttrubuteValue(ndattr, "n");
											  if (str1.equalsIgnoreCase(BaseCredit.CODE_PAY_SUM)){
												  str1s=XmlUtils.getAttrubuteValue(ndattr, "v");
											  }
											  //месяц
											  str2=XmlUtils.getAttrubuteValue(ndattr, "n");
											  if (str2.equalsIgnoreCase(BaseCredit.CODE_PAY_MONTH)){
												  str2s=XmlUtils.getAttrubuteValue(ndattr, "v");
											  }
											}//end если дисциплина к этому кредиту
										}//end for атрибуты
									}//end есть nodelist с атрибутами
								}//end for периоды
							}//end nodelist с периодами
						}//end for секции
					}//end nodelist с секциями
					
				}//end for пакет
			}//end nodelist c пакетом
							
		}//end платежная дисциплина
		
	    cre.setPayDiscipline(strs);
	    //уплаченная сумма
	    if (StringUtils.isNotEmpty(str1s)&&!str1s.equals("-")){
	      cre.setCreditSumPaid(Convertor.toDouble(str1s, CalcUtils.dformat_xx));
	    }
	    //платеж в месяц
	    if (StringUtils.isNotEmpty(str2s)&&!str2s.equals("-")){
	   	  cre.setCreditMonthlyPayment(Convertor.toDouble(str2s, CalcUtils.dformat_xx));
	    }
	    
	    cre.setIssameorg(false);
	    cre.setPeopleMainId(pplmain);
	    cre.setPartnersId(refBooks.getPartnerEntity(Partner.EQUIFAX));
	    cre.setCreditRequestId(ccre);
	    cre.setIsactive(ActiveStatus.ACTIVE);
	    creditDAO.saveCreditEntity(cre);
	    creditList.add(cre);
	    return creditList;
	}
	
	
}
