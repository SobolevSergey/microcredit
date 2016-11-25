package ru.simplgroupp.ejb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.soap.SOAPFaultException;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ActionRuntimeException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.idv.HSMSWebService1;
import ru.simplgroupp.idv.IDVApplicantCH;
import ru.simplgroupp.idv.IDVProcess;
import ru.simplgroupp.idv.IDVProcessResponse;
import ru.simplgroupp.idv.IDVReqBusinesInfo;
import ru.simplgroupp.idv.IDVRequestRoot;
import ru.simplgroupp.idv.IDVRespBusinesInfo;
import ru.simplgroupp.idv.IDVResponseRoot;
import ru.simplgroupp.idv.ObjectFactory;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MimeTypeKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Реальный класс для работы с сервисом верификации ОКБ
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScoringOkbIdvBean  extends AbstractPluginBean implements ScoringOkbIdvBeanLocal{

	@Inject Logger log;
	
	private ObjectFactory factory = new ObjectFactory();
	protected static MessageFactory messFactory = null;
	
	protected Unmarshaller unMar;
	protected Marshaller mar;
		
	protected static String SOAP_ACTION_IDV="http://experian.com/bureau/hosted/nbsm/IDVProcess";
	
	@EJB 
	ReferenceBooksLocal refBooks;
	    
	@EJB
	PeopleBeanLocal peopleBean;
	
	@EJB
	KassaBeanLocal kassaBean;
	
	@EJB
	CreditBeanLocal creditBean;
	
	@EJB 
	ServiceBeanLocal servBean;	
	 
	@EJB
	WorkflowBeanLocal workflow;
	
	@EJB
	ICryptoService crypto;
	
	@EJB 
	EventLogService eventLog;
	
	@EJB
	RequestsService requestsService;
	
	@EJB
	CreditInfoService creditInfo;
	
	@EJB
	PeopleDAO peopleDAO;
	 
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequestSoap(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays) throws ActionException {
		
		//проверяем, кеширован ли запрос
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.OKB_IDV, new Date(), cacheDays);
	    if (inCache){
	   	    log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в ОКБ IDV, он кеширован");
	   	    //save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_IDV,
				"Запрос к внешнему партнеру ОКБ IDV не производился, данные кешированы",
						null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
			
	   	    return null;
	    }
		//preparing for making request
		PartnersEntity parOkbIdv = refBooks.getPartnerEntity(Partner.OKB_IDV);
	
		//new request
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.OKB_IDV, RequestStatus.STATUS_IN_WORK, new Date());
	
		//инициализируем ssl context
		SSLContext sslCon = null;
	    try {
		    sslCon = initSSLContext(isWork);
		}
	    catch (KassaException ex){
	    	requestsService.saveRequestError(req,ex,ErrorKeys.CANT_CREATE_SSL_CONTEXT,"Не удалось создать ssl context",Type.TECH, ResultType.NON_FATAL);
		}
	    
	    try {
		  //initialize sslsocketfactory
	      SSLSocketFactory fact = sslCon.getSocketFactory();
	      URL url = null;
		  String urlString=isWork?parOkbIdv.getUrlwork():parOkbIdv.getUrltest();
		
		  //set url
	      try {
	    	log.info("инициализируем url");
			url = new URL(urlString);
		  } catch (MalformedURLException e) {
			  requestsService.saveRequestError(req,e,ErrorKeys.CANT_SET_URL,"Не удалось установить url",Type.TECH, ResultType.NON_FATAL);  
		  }	
		
          HttpsURLConnection.setDefaultSSLSocketFactory(fact);	
          HttpsURLConnection.setDefaultHostnameVerifier(HTTPUtils.allHostsValid());
  
  	      
          HSMSWebService1 hws=null;
          Dispatch<SOAPMessage> disp=null;
          try {
        	  //create web-service	
             log.info("открываем web-service "+url);
             hws=new HSMSWebService1(url);
             //create dispatcher for soap message    	
             log.info("открываем dispatcher");
   		     disp=hws.createDispatch(new QName("http://experian.com/bureau/hosted/nbsm", "HSMSWebService1Soap12"), SOAPMessage.class, HSMSWebService1.Mode.MESSAGE);
   		     disp.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
   		     disp.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
          } catch (Exception we) {
        	  requestsService.saveRequestError(req,we,ErrorKeys.CANT_CREATE_WEBSERVICE,"Не удалось создать веб-сервис ",Type.TECH, ResultType.FATAL);  	
          }
        
    	
		  //create jaxb, marshaller and unmarshaller from ws declaration
		  JAXBContext jaCtx=null;
		  try {
			log.info("устанавливаем jaxbcontext");
			jaCtx = JAXBContext.newInstance(HSMSWebService1.class.getPackage().getName());
			unMar = jaCtx.createUnmarshaller();
			mar = jaCtx.createMarshaller();	
		  } catch (JAXBException e1) {
			  requestsService.saveRequestError(req,e1,ErrorKeys.CANT_CREATE_MARSHALLER,"Не удалось создать marshaller ",Type.TECH, ResultType.FATAL);  	
		  }
		  
	      //create message factory
		  if (messFactory == null) {
	    	try {
	    		log.info("создаем message factory");
				messFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			} catch (Exception e) {
				requestsService.saveRequestError(req,e,ErrorKeys.CANT_CREATE_SOAP_FACTORY,"Не удалось создать soap factory ",Type.TECH, ResultType.FATAL);  	
			 }
		  }
		
		  //create request from db and ws classes
		  IDVProcess proc=createRequest(cre,req,isWork);
			
		  SOAPMessage msg = null;
    	  SOAPMessage resp = null;
    	
    	  try {
    		//create message
			msg = messFactory.createMessage();
			SOAPElement eroot = msg.getSOAPBody();
			//insert data into soap message
			mar.marshal((Object) proc, eroot);
		    String smsg = Convertor.soapToString(msg);
		    log.info(smsg);
			req.setRequestbody(smsg.getBytes());
			req=requestsService.saveRequestEx(req);
					
			//send and recieve answer
			try	{
			  log.info("отправляем сообщение");	
			  resp=disp.invoke( msg);
			}
			catch (SOAPFaultException fex){
				log.severe("Soap fault: "+Convertor.xmlToString(fex.getFault().getOwnerDocument()));
				throw new ActionException("Получили soap fault "+Convertor.xmlToString(fex.getFault().getOwnerDocument()),fex);
			} catch (Exception e){
				log.severe("Ошибка при получении ответа: "+e.getMessage());
				throw new ActionException(ErrorKeys.TECH_ERROR,"Ошибка при получении ответа: "+e.getMessage(),Type.TECH, ResultType.FATAL,e);
			}
			//result
			if (resp!=null)	{
				log.info("получаем результат");
				String rmsg = Convertor.soapToString(resp);
				log.info(rmsg);
				req.setResponsebody(rmsg.getBytes());
				req.setResponsedate(new Date());
				req.setResponsebodystring(rmsg);
				req=requestsService.saveRequestEx(req);
	
				//unmarshal response
				Element nd=(Element) resp.getSOAPBody().getChildNodes().item(0);
				Object  rmess =  unMar.unmarshal(nd);
				IDVProcessResponse aresp= (IDVProcessResponse) rmess;
				
				IDVResponseRoot resproot=aresp.getResponse();
				int i=resproot.getStatusInfo().getErrorCode();
				//нет ошибок
				if (i==Requests.RESPONSE_CODE_CAIS_OK) {
					
					log.info("нет ошибок");
					IDVRespBusinesInfo respinfo=resproot.getResult();
					//people id
					PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());
					//save result
					creditInfo.saveVerification(creditRequestDAO.getCreditRequestEntity(cre.getId()),
							pplmain, parOkbIdv, respinfo.getVerificationScore().doubleValue(), respinfo.getValidationScore().doubleValue(),0,0,0,0,0);
			    
				    req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
				   
				    req.setResponsemessage("Данные найдены");
					
				}
				else {
				   log.info("есть ошибки");	
				   req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
				   req.setResponsecode(String.valueOf(i));
				   req.setResponsemessage(Convertor.toLimitString(resproot.getStatusInfo().getErrorDescription(),250));
				}
				
			}
			//save request
			req=requestsService.saveRequestEx(req);
		  
		} catch (Throwable e) {
			log.severe("Не удалось создать сообщение: "+e.getMessage());
			throw new ActionException(ErrorKeys.CANT_CREATE_SOAP_MESSAGE,"Не удалось создать сообщение",Type.TECH, ResultType.NON_FATAL,e);
		}
	    } finally {
	    	try {
				returnSSLContext(isWork, sslCon);
				sslCon=null;
			} catch (KassaException e) {
				log.severe("Не удалось вернуть ssl context: "+e.getMessage());
			}
	    }
    	//save log
    	eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_IDV, "Был выполнен запрос к сервису верификации ОКБ",
    			  null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
    	
    	 
		return req;
		
	}

	@Override
	public String getSystemName() {
		return ScoringOkbIdvBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return false;
	}

	@Override
	public EnumSet<Mode> getModesSupported() {
		return EnumSet.of(Mode.SINGLE);
	}

	@Override
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException, ActionRuntimeException {
		    
		    Integer creditRequestId = Convertor.toInteger(businessObjectId);
			
			log.info("ОКБ, верификация. Заявка " + creditRequestId + " передана на скоринг.");
			try {
				CreditRequest ccRequest;
				try {
					ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
				} catch (Exception e) {
					throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.FATAL,e);
				}
				if (ccRequest==null){
					throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку, заявка null",Type.TECH, ResultType.NON_FATAL,null);
				}
				OkbIdvPluginConfig cfg=(OkbIdvPluginConfig) context.getPluginConfig();
				
			    if (context.getNumRepeats() < cfg.getNumberRepeats()) {
				    newRequest(ccRequest.getEntity(),cfg.isUseWork(),cfg.getCacheDays());
				}
				else {
					log.info("ОКБ, верификация. Заявка " + creditRequestId + " не обработана.");
					throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 5 попыток",Type.TECH, ResultType.FATAL,null);
				}
			} catch (ActionException e) {
				log.info("ОКБ, верификация. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException("Произошла ошибка ",e);
			} catch (Throwable e) {
				log.info("ОКБ, верификация. Заявка " + creditRequestId + " не обработана. Ошибка "+e.getMessage());
				throw new ActionException("Произошла ошибка ",e);
			}
			log.info("ОКБ, верификация. Заявка " + creditRequestId + " обработана.");
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
		log.warning("Метод sendSingleRequest не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		log.warning("Метод querySingleResponse не поддерживается");
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
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSystemDescription() {
		return ScoringOkbIdvBeanLocal.SYSTEM_DESCRIPTION;
	}	

  /**
   * инициализируем ssl-context	
   * @param isWork - рабочий сервис или тестовый
   * @return
   * @throws KassaException
   * 
   **/
  private SSLContext initSSLContext(Boolean isWork) throws KassaException{
		String[] settings = null;
		
		if (isWork) {
			settings = new String[] {CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK};
		} else {
			settings = new String[] {CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST};
		}
		try {
			return servBean.borrowSSLContext(settings);
		} catch (Exception e1) {
			log.severe("Не удалось инициализировать ssl context: "+e1.getMessage());
			throw new KassaException("Не удалось инициализировать ssl context: "+e1); 
		}
		  
	}
  
	
	/**
	 * возвращаем ssl-context
	 * @param isWork - рабочий сервис или тестовый
	 * @param sslCon - ssl context
	 * @throws KassaException
	 */
	private void returnSSLContext(Boolean isWork, SSLContext sslCon) throws KassaException {
		String[] settings = null;
		if (isWork) {
			settings = new String[] {CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK};
		} else {
			settings = new String[] {CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST};
		}
		try {
			servBean.returnSSLContext(settings, sslCon);
		} catch (Exception e) {
			log.severe("Не удалось вернуть ssl context: "+e.getMessage());
			throw new KassaException("Не удалось вернуть ssl context: "+e);
			
		}
	}  

	@Override
	public void uploadHistory(UploadingEntity uploading, Date sendingDate,
			Boolean isWork) throws KassaException {
		 log.info("Был вызван неработающий метод загрузки");
		
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer)
			throws KassaException {
		//получаем документ из ответа	  
		 Document doc=XmlUtils.getDocFromString(answer);
		 //если вернулся нормальный xml
		 if (doc!=null) {
			 Node response=doc.getElementsByTagName("Response").item(0);
			 if (response!=null){
				Node status=XmlUtils.findChildInNode(response, "StatusInfo", 0); 
			    Integer i=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(status,"ErrorCode",0)));
				//нет ошибок
				if (i==Requests.RESPONSE_CODE_CAIS_OK) {
					
					log.info("нет ошибок");
					Node result=XmlUtils.findChildInNode(response, "Result", 0); 
					//people id
					PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(req.getPeopleMainId().getId());
					//save result
					creditInfo.saveVerification(creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId()),
							pplmain, refBooks.getPartnerEntity(Partner.OKB_IDV), 
							Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(result, "Verification_Score", 0))), 
							Convertor.toDouble(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(result, "Validation_Score", 0))), 
							0,0,0,0,0);
			    
				    req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
				   
				    req.setResponsemessage("Запрос успешен");
					
				}
				else {
				   log.info("есть ошибки");	
				   req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
				   req.setResponsecode(String.valueOf(i));
				   req.setResponsemessage(Convertor.toLimitString(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(XmlUtils.findChildInNode(response, "StatusInfo", 0),"ErrorDescription",0)),250));
				} 
			 }
		 }
		return req;
	}

	@Override
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
			throws KassaException {
		return null;
	}

	private IDVProcess createRequest(CreditRequestEntity cre,RequestsEntity req,Boolean isWork){
		PartnersEntity parOkbIdv = refBooks.getPartnerEntity(Partner.OKB_IDV);
		//create request from db and ws classes
		IDVProcess proc=factory.createIDVProcess();
		IDVRequestRoot root=factory.createIDVRequestRoot();
		IDVReqBusinesInfo info=factory.createIDVReqBusinesInfo();
		//login and password
		if (isWork)	{
			info.setLogin(parOkbIdv.getLoginwork());
			info.setPassword(parOkbIdv.getPasswordwork());
		}
		else {
		    info.setLogin(parOkbIdv.getLogintest());
		    info.setPassword(parOkbIdv.getPasswordtest());
		}
		//people id
		PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());
		//personal data
		PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
		info.setFirstName(pplper.getName());
		info.setLastName(pplper.getSurname());
		info.setPatronymicName(pplper.getMidname());
		info.setBirthPlace(Convertor.toLimitString(pplper.getBirthplace(),30));
		info.setDateOfBirth(Convertor.dateToGreg(pplper.getBirthdate(),false));
		//request data
		info.setAppNum(req.getRequestquid());
		info.setAppDate(Convertor.dateToGreg(req.getRequestdate(),false));
		//passport
		DocumentEntity dc=peopleBean.findPassportActive(pplmain);
		info.setPrimaryID(dc.getSeries().trim()+dc.getNumber().trim());
		info.setPrimaryIDAuthorityCode(dc.getDocorgcode());
		info.setPrimaryIDAuthority(Convertor.toLimitString(dc.getDocorg(),100));
		info.setPrimaryIDIssueDate(Convertor.dateToGreg(dc.getDocdate(),false));
		//credit
		CreditEntity cr=null;
		List<CreditEntity> lstcred = creditBean.findCredits(pplmain, cre, refBooks.getPartnerEntity(Partner.CLIENT), false, null);
		if (lstcred.size()>0) {
			cr=lstcred.get(0);
		    IDVApplicantCH hist=factory.createIDVApplicantCH();
		    hist.setBankID(cr.getCreditOrganizationId().getId().toString());
		    ReferenceEntity f=refBooks.findByCodeIntegerMain(RefHeader.CREDIT_TYPE, cr.getCredittypeId().getCodeinteger());
		    if (f!=null) {
      	        hist.setFinanceType(f.getCode());
      	        if (StringUtils.isEmpty(f.getCode())){
      	        	hist.setFinanceType(BaseCredit.COMMON_CREDIT_CODE);
      	        }
		    } else {
		    	hist.setFinanceType(BaseCredit.COMMON_CREDIT_CODE);
		    }
		    hist.setOpenDate(Convertor.dateToGreg(cr.getCreditdatabeg(),false));
		    hist.setAccountStatus(cr.getIsover()?"N":"Y");
		    hist.setCurrency(refBooks.findByCodeMain(RefHeader.CURRENCY_TYPE, cr.getIdCurrency().getCode()).getCode());
		    hist.setLoanAmountCreditLimit(cr.getCreditsum().intValue());
		    if (cr.getOverduestateId()!=null){
		    	ReferenceEntity re=refBooks.findByCodeMain(RefHeader.CREDIT_OVERDUE_STATE, cr.getOverduestateId().getCode());
		    	if (re!=null){
		            hist.setWorstPaymentStatus(refBooks.findByCodeMain(RefHeader.CREDIT_OVERDUE_STATE, cr.getOverduestateId().getCode()).getCode());
		    	} else {
		    		hist.setWorstPaymentStatus("1");
		    	}
		    } else {
	    		hist.setWorstPaymentStatus("1");
	    	}
		    info.getCreditHistory().add(hist);
		}
		root.setApplication(info);
		proc.setRequest(root);
		return proc;
	}

	@Override
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		return null;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays) throws ActionException {
		
		//проверяем, кеширован ли запрос
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.OKB_IDV, new Date(), cacheDays);
	    if (inCache){
	   	    log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в ОКБ IDV, он кеширован");
	   	    //save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_IDV,
				"Запрос к внешнему партнеру ОКБ IDV не производился, данные кешированы",
						null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
			
	   	    return null;
	    }
		//preparing for making request
		PartnersEntity parOkbIdv = refBooks.getPartnerEntity(Partner.OKB_IDV);
	
		//new request
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.OKB_IDV, RequestStatus.STATUS_IN_WORK, new Date());
	
		String url=isWork?parOkbIdv.getUrlwork():parOkbIdv.getUrltest();
		
		//create jaxb, marshaller and unmarshaller from ws declaration
		JAXBContext jaCtx=null;
		try {
			log.info("устанавливаем jaxbcontext");
			jaCtx = JAXBContext.newInstance(HSMSWebService1.class.getPackage().getName());
			unMar = jaCtx.createUnmarshaller();
			mar = jaCtx.createMarshaller();	
		} catch (JAXBException e1) {
			requestsService.saveRequestError(req,e1,ErrorKeys.CANT_CREATE_MARSHALLER,"Не удалось создать marshaller ",Type.TECH, ResultType.FATAL);  	
		}
		  
		//create message factory
		if (messFactory == null) {
	    	try {
	    		log.info("создаем message factory");
				messFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			} catch (Exception e) {
				requestsService.saveRequestError(req,e,ErrorKeys.CANT_CREATE_SOAP_FACTORY,"Не удалось создать soap factory ",Type.TECH, ResultType.FATAL);  	
			 }
		}
		
		//create request from db and ws classes
		IDVProcess proc=createRequest(cre,req,isWork);
			
		SOAPMessage msg = null;
  	  	  	  
  	    //create message
  	    String smsg ="";
  	    try{
		    msg = messFactory.createMessage();
		    SOAPElement eroot = msg.getSOAPBody();
		    //insert data into soap message
		    mar.marshal((Object) proc, eroot);
	        smsg = Convertor.soapToString(msg);
	        smsg=XmlUtils.HEADER_XML_UTF+smsg;
  	    } catch (Throwable e){
  	    	log.severe("Не удалось создать сообщение: "+e.getMessage());
			throw new ActionException(ErrorKeys.CANT_CREATE_SOAP_MESSAGE,"Не удалось создать сообщение",Type.TECH, ResultType.NON_FATAL,e);
	    }
	    log.info("Запрос в IDV "+smsg);
	    
		req.setRequestbody(smsg.getBytes());
		req=requestsService.saveRequestEx(req);
		
		//инициализируем ssl context
		SSLContext sslCon = null;
	    try {
		    sslCon = initSSLContext(isWork);
		}
	    catch (KassaException ex){
	    	requestsService.saveRequestError(req,ex,ErrorKeys.CANT_CREATE_SSL_CONTEXT,"Не удалось создать ssl context",Type.TECH, ResultType.NON_FATAL);
		}
	    
	    byte[] respmessage=null;
		try {
		    try {
		    	Map<String, String> params = new HashMap<>();
		        params.put("Content-Type","text/xml; charset=utf-8");
		        params.put("SOAPAction",SOAP_ACTION_IDV);
		        params.put("Content-Length", String.valueOf(smsg.length()));
		     	respmessage = HTTPUtils.sendHttp("POST",url, smsg.getBytes(),params,sslCon);
		    } finally {
		    	returnSSLContext(isWork, sslCon);
		    	sslCon=null;
		    }
			
		} catch (Exception ex){
			requestsService.saveRequestError(req,ex,ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.TECH, ResultType.NON_FATAL);
		}
		//если есть ответ
		if (respmessage!=null) {
	        String answer=new String(respmessage);
	        log.info(answer);
	        	        
	        req.setResponsebody(respmessage);
			req.setResponsedate(new Date());
			req.setResponsebodystring(answer);
			req=requestsService.saveRequestEx(req);

	    	//parse request				
			try {
                req=saveAnswer(req,answer);
		    } catch (Exception ek){
				requestsService.saveRequestError(req,ek,ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать xml", Type.TECH, ResultType.FATAL);
			}//end parse
		
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_IDV,"Произведен вызов внешней системы ОКБ IDV",
							null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
				
		}
	    return req;
	}
	
}
