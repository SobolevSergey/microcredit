package ru.simplgroupp.ejb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
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
import javax.net.ssl.SSLContext;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.AddressBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringSkbBeanLocal;
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
import ru.simplgroupp.persistence.CreditHistoryPayEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.SummaryEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.transfer.Summary;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MakeXML;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Реальный класс для работы с СКБ
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringSkbBeanLocal.class)
public class ScoringSkbBean extends AbstractPluginBean implements ScoringSkbBeanLocal {

	@Inject Logger log;
		
	@EJB 
	ReferenceBooksLocal refBooks;
		    
	@EJB
	PeopleBeanLocal peopleBean;
		
	@EJB
	PeopleDAO peopleDAO;
	 
    @EJB
	KassaBeanLocal kassaBean;

	@EJB
	UploadingService uploadService;
	 
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
	RequestsService requestsService;
	 
	@EJB
	OrganizationService orgService;
	 
	@EJB
	CreditInfoService creditInfo;
	 
	@EJB
	CreditService creditService;
	 
	@EJB
	CreditDAO creditDAO;
	 
	@EJB
	AddressBeanLocal addressBean;
	 
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
	SummaryDAO summaryDAO;
			
	@EJB
	ProductBeanLocal productBean;
	
	@Override
	public String getSystemDescription() {
		return ScoringSkbBeanLocal.SYSTEM_DESCRIPTION;
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
	public void executeSingle(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
        Integer creditRequestId = Convertor.toInteger(businessObjectId);
		
		log.info("СКБ. Заявка " + creditRequestId + " ушла в скоринг.");
		try {
			CreditRequest ccRequest;
			try {
				ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
			} catch (Exception e) {
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.NON_FATAL,e);
			}
			if (ccRequest==null){
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку, заявка null",Type.TECH, ResultType.NON_FATAL,null);
			}
			SkbPluginConfig cfg=(SkbPluginConfig) context.getPluginConfig();
			if (context.getNumRepeats() <=cfg.getNumberRepeats()) {
			    newRequest(ccRequest.getEntity(),cfg.isUseWork(),cfg.getCacheDays());
			}
			else
			{
				log.severe("СКБ. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 3 попытки",Type.TECH, ResultType.FATAL,null);
			}
		} catch (ActionException e) {
			log.severe("СКБ. Заявка " + creditRequestId + " не обработана.");
			throw new ActionException("Произошла ошибка ",e);
		} catch (Throwable e) {
			log.severe("СКБ. Заявка " + creditRequestId + " не обработана. Ошибка "+e+", "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		}
				
		log.info("СКБ. Заявка " + creditRequestId + " обработана.");	
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
		log.severe("Метод addToPacket не поддерживается для плагина RusStandart");
	}

	@Override
	public List<BusinessObjectResult> executePacket(
			PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
		log.warning("Метод sendSingleRequest не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context)
			throws ActionException {
		log.warning("Метод querySingleResponse не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BusinessObjectResult> sendPacketRequest(
			PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(
			PluginExecutionContext context) throws ActionException {
		log.warning("Метод queryPacketResponse не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
			Integer cacheDays) throws ActionException {
		//проверяем, были ли уже успешные запросы по этой заявке
			
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.SKB, new Date(), cacheDays);
	    if (inCache){
	   	    log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в КБ CКБ, он кеширован");
	   	    //save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_SKB,
				"Запрос к внешнему партнеру КБ СКБ не производился, данные кешированы",
					null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
		
	   	    return null;
	    }
		//новый запрос
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.SKB, RequestStatus.STATUS_IN_WORK, new Date());
		
		Partner parSkb = refBooks.getPartner(Partner.SKB);	
		//строка запроса
		String rmessage=createRequestString(cre,req,isWork);
		
		req.setRequestbody(rmessage.getBytes());
	    req=requestsService.saveRequestEx(req);
		
		//send request and get response
		byte[] respmessage=null;
		
		String url=isWork?parSkb.getUrlWork():parSkb.getUrlTest();
		log.info("Url "+url);
		
		//инициализируем ssl context
		SSLContext sslCon = null;
		try {
		    sslCon = initSSLContext(isWork);
		    log.info("Инициализировали ssl ");
		}
		catch (KassaException ex){
			requestsService.saveRequestError(req,ex,ErrorKeys.CANT_CREATE_SSL_CONTEXT,"Не удалось создать ssl context",Type.TECH, ResultType.NON_FATAL);
		}
		//пытаемся отправить запрос
		try {
		    	try {
		    		respmessage = HTTPUtils.sendHttp("POST",url, rmessage.getBytes(XmlUtils.ENCODING_WINDOWS),null,sslCon);
		    	} finally {
		    		returnSSLContext(isWork, sslCon);
		    		sslCon=null;
		    	}
			
		}	catch (Exception ex){
			  requestsService.saveRequestError(req,ex,ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.TECH, ResultType.NON_FATAL);
		}
		    
		//если есть ответ
		if (respmessage!=null) {
			  
		    String aanswer=new String(respmessage);
							
			//если вернулись HTML headers
			if (aanswer.toUpperCase().contains("HTML")){
				 log.severe(aanswer);
				 requestsService.saveRequestError(req,null,ErrorKeys.BAD_RESPONSE, "Вернулся ответ ошибки HTTP", Type.TECH, ResultType.FATAL);
			}//end вернулись HTML headers
				
			String answer="";
			try {
				answer = new String(respmessage,XmlUtils.ENCODING_WINDOWS);
			} catch (UnsupportedEncodingException e) {
				requestsService.saveRequestError(req,e,ErrorKeys.CANT_DECODE_MESSAGE, "Ошибка перекодирования в Windows-1251", Type.TECH, ResultType.FATAL);
			}
				
			//save answer
			if (answer.toUpperCase().contains(XmlUtils.ENCODING_UTF)){
				//если ответ почему-то в utf
				answer=new String(respmessage);
				req.setResponsebody(respmessage);
			}	else if (!answer.contains("xml")){
				//если у ответа нет заголовка, добавим
				answer="<?xml version='1.0' encoding='Windows-1251'?>"+answer;
				req.setResponsebody(answer.getBytes());
			}
				
			req.setResponsebodystring(answer);
			log.info("Вернулся ответ из Русского стандарта по заявке "+cre.getId());
			req=requestsService.saveRequestEx(req);
			
			//parse request				
			try {
                 req=saveAnswer(req,answer);
		    } catch (Exception ek){
				 requestsService.saveRequestError(req,ek,ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать xml", Type.TECH, ResultType.FATAL);
			}//end parse
		}//end если есть ответ
	
		req=requestsService.saveRequestEx(req);
			
	    //сохраняем логи
		eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_SKB,"Был выполнен запрос в СКБ",
				null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
	
		return req;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer)
			throws KassaException {
		Partner skb = refBooks.getPartner(Partner.SKB);
		//получаем документ из ответа	  
		Document doc=XmlUtils.getDocFromString(answer);
		//если вернулся нормальный xml
		if (doc!=null)
		{
			//дата ответа
			req.setResponsedate(Convertor.toDate(doc.getElementsByTagName("ResponseDateTime").item(0).getTextContent(), DatesUtils.FORMAT_ddMMYYYY_HHmmss));
			//номер ответа
			req.setResponseguid(XmlUtils.getNodeValueText(doc.getElementsByTagName("ResponseID").item(0)));
			CreditRequestEntity creReq=req.getCreditRequestId();
			CreditRequestEntity cre=creditRequestDAO.getCreditRequestEntity(creReq.getId());
			PeopleMainEntity pplmain=cre.getPeopleMainId();
			//если вернули отчет
			if (XmlUtils.isExistChildInNode(doc, "BODY", "Report", 0))
			{
				//ищем все кредиты по этой заявке
				List<CreditEntity> creditList=creditBean.findCredits(pplmain, cre, null, null, null);
				
				//ищем кредит клиента
				List<CreditEntity> lstClientCredit=creditBean.findCredits(pplmain, cre,refBooks.getPartnerEntity(Partner.CLIENT), false, null);
				CreditEntity clientCredit=null;
				if (lstClientCredit.size()>0){
					clientCredit=lstClientCredit.get(0);
				}
				
				PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
				//информация по человеку - фио
				Node pnd=XmlUtils.findChildInNode(doc.getElementsByTagName("TitlePart").item(0), "NaturalPersonSubject", 0);
				List<Node> lstper=XmlUtils.findNodeList(pnd, "Title");
				if (lstper.size()>0)
				{
					String bday=XmlUtils.getAttrubuteValue(pnd, "BirthDate");
					String bplace=XmlUtils.getAttrubuteValue(pnd, "BirthPlace");
					for (Node per:lstper)
					{
						if (!pplper.getSurname().equalsIgnoreCase(XmlUtils.getAttrubuteValue(per, "LastName"))
								||!pplper.getName().equalsIgnoreCase(XmlUtils.getAttrubuteValue(per, "FirstName"))
								||!pplper.getMidname().equalsIgnoreCase(XmlUtils.getAttrubuteValue(per, "MiddleName")))
						{
							
							try {
								Date databeg=new Date();
								if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(per, "ActualityDate"))){
									databeg=Convertor.toDate(XmlUtils.getAttrubuteValue(per, "ActualityDate"), DatesUtils.FORMAT_ddMMYYYY);
								} 
								peopleBean.newPeoplePersonal(null,pplmain.getId(), cre.getId(), Partner.RSTANDARD, 
										XmlUtils.getAttrubuteValue(per, "LastName"),
										XmlUtils.getAttrubuteValue(per, "FirstName"), 
										XmlUtils.getAttrubuteValue(per, "MiddleName"), null,
										Convertor.toDate(bday, DatesUtils.FORMAT_ddMMYYYY), bplace, null,databeg, ActiveStatus.ACTIVE);
							} catch (Exception e) {
								log.severe("Не удалось сохранить актуальные ПД по заявке "+cre.getId()+", ошибка "+e);
							}
							
						}
					}
					//заишем снилс и инн
					try {
						peopleBean.savePeopleMain(pplmain, 
								StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(pnd, "IndividualTaxpayerNumber"))?XmlUtils.getAttrubuteValue(pnd, "IndividualTaxpayerNumber"):"", 
								StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(pnd, "PensionNumber"))?XmlUtils.getAttrubuteValue(pnd, "PensionNumber"):"");
					} catch (PeopleException e) {
						log.severe("Не удалось записать инн и снилс по человеку "+pplmain.getId()+", ошибка "+e);
					}
				}
				
				//паспорта
				List<Node> lstdoc=XmlUtils.findNodeList(pnd, "IdentificationDocument");
				if(lstdoc.size()>0){
					 // ищем, писали ли документы
				    DocumentEntity pasppar = peopleBean.findDocument(pplmain, cre, Partner.SKB, ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
					for (Node nddoc:lstdoc){
						int doctype=Convertor.toInteger(XmlUtils.getAttrubuteValue(nddoc, "DocTypeID"));
					   
					    // только паспорт
					    if (Documents.PASSPORT_RF_EQUIFAX_RS==doctype) {
						
						    // если нет
						    if (pasppar == null) {
							    DocumentEntity pasp = peopleBean.findPassportActive(pplmain);
							    if (pasp!=null) {
							    	String docseries=XmlUtils.getAttrubuteValue(nddoc,"DocSer");
							    	String docnomer=XmlUtils.getAttrubuteValue(nddoc,"DocNum");
							    	if (!pasp.getSeries().equals(docseries)||!pasp.getNumber().equals(docnomer)){
							    		try {
											peopleBean.newDocument(null,pplmain.getId(), cre.getId(), Partner.SKB,docseries, docnomer, 
													Convertor.toDate(XmlUtils.getAttrubuteValue(nddoc,"DocDate"), DatesUtils.FORMAT_ddMMYYYY), 
													XmlUtils.getAttrubuteValue(nddoc,"DocDelivInstCode"), 
													XmlUtils.getAttrubuteValue(nddoc,"DocDelivInstName"), ActiveStatus.ACTIVE);
										} catch (Exception e){
											log.severe("Не удалось записать активный документ по заявке "+cre.getId()+", ошибка "+e);
										}
							    	}
								}//pasp не null

						    }//не писали данные по паспортам
					    }//end если это паспорт
					}//end for
				
				}//end if size>0
				
				//адреса
				List<Node> lstadr=XmlUtils.findNodeList(pnd, "Address");
				if (lstadr.size()>0)
				{
					//были ли записаны адреса по этой заявке
					AddressEntity adrpar=peopleBean.findAddress(pplmain, Partner.SKB, cre, BaseAddress.REGISTER_ADDRESS, ActiveStatus.ACTIVE);
					//еще не было
					if (adrpar==null)
					{
					  for (Node adr:lstadr)
					  {
						  Date databeg=StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(adr, "ActualityDate"))?
								  Convertor.toDate(XmlUtils.getAttrubuteValue(adr, "ActualityDate"), DatesUtils.FORMAT_ddMMYYYY):new Date();
						 
						  Integer addressTypeId=null;
						  if (XmlUtils.isExistChildInNode(adr, "Caption", 0))	{
							  try {
				  		         ReferenceEntity rfe=refBooks.findFromKb(RefHeader.ADDRESS_TYPE_RS, Convertor.toInteger(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Caption", 0), "ID")));
				  		         if (rfe!=null){
							       addressTypeId=rfe.getCodeinteger();
				  		         }
							  } catch (Exception e){
								 log.severe("Не удалось найти в справочнике вид адреса "+ Convertor.toInteger(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Caption", 0), "ID"))); 
							  }
					   	  }
						
						  try {
							  Node adrnd=XmlUtils.findChildInNode(adr, "StreetAddress", 0);
							
							  AddressEntity address=peopleBean.newAddressFias(null,pplmain.getId(), cre.getId(), Partner.SKB, 
									addressTypeId,XmlUtils.getNodeValueText((XmlUtils.findChildInNode(adr, "AddressString", 0))), 
									databeg, null, BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ACTIVE,null,
									adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"HouseNum"),
									adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"BuildingNum"),
									adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"BuildingSymbol"),
									adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"FlatNum"));
							  
							  peopleBean.saveAddressDataWithStrings(address, XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Region", 0),"ID"),
									  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Region", 0),"Text"),
									  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "District", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "District", 0),"Ext"), 
									  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "City", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "City", 0),"Ext"), 
									  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Village", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Village", 0),"Ext"), 
									  adrnd==null?null:XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adrnd, "Street", 0),"Caption"), 
									  adrnd==null?null:XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adrnd, "Street", 0),"Ext"));
								
						  } catch (Exception e) {
							  log.severe("Не удалось записать адрес по заявке "+cre.getId()+",ошибка "+e);
						  }
						  
						  
						  
					  }//end for
					}//end еще не писали адреса
					
				}//end в ответе есть адреса
				
				//есть ли кредитная история
				List<Node> lstcred=XmlUtils.findNodeList(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "ContractRecord");
				if (lstcred.size()>0)
				{
					//ищем кредиты от партнера
					List<CreditEntity> lstpar=creditBean.findCredits(pplmain, cre, skb.getEntity(), false, null);
					//если нет
					if (lstpar.size()==0)
					{
					//делаем старые кредиты неактивными
					creditDAO.makePartnerCreditsNotActive(Partner.SKB,pplmain.getId());
					Map<String,Object> params=productBean.getConsiderProductConfig(cre.getProductId().getId());
					Integer unique=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_CREDITS));
					//кредиты
					for (Node cred:lstcred)	{  
						
						//проверяем на принадлежность к своей организации
						String self=XmlUtils.getAttrubuteValue(cred, "Ownership");
						Boolean sameOrg=false;
						if (StringUtils.isNotEmpty(self)){
							sameOrg=Convertor.toBoolean(self);
							if (sameOrg==null){
								sameOrg=false;
							}
						}
						
						if (!sameOrg) {
						
						  Node cd=XmlUtils.findChildInNode(cred, "ContractData", 0);
						  //отношение к кредиту
						  int ri=0;
						  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "RelationID"))) {
							  ri=Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "RelationID"));
						  }
						  //если он сам брал, то записываем
						  if (ri==0||ri==BaseCredit.CREDIT_OWNER) {
							  //ищем системную верификацию, если есть кредит клиента
							  VerificationEntity verification=null;
							  if (clientCredit!=null){
								    verification=creditInfo.findOneVerification(cre.getId(), pplmain.getId(), Partner.SYSTEM, null);
							  }
							  creditList=saveSKBCredit(cd, cre,pplmain,clientCredit,creditList,
									  unique,verification);
						  }//end если брал сам (закончилась запись кредита)
						}//end не наша организация
					}//end for	
						
					}//end не писали кредиты
				}//end есть кредиты в отчете
				
				//суммарная часть отчета
				if (XmlUtils.isExistChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "Summary", 0))
				{
					//ищем, есть ли summary по данной заявке
					List<SummaryEntity> lstpar=summaryDAO.findSummary(pplmain, cre, skb.getEntity(), null);
					//если нет
					if (lstpar==null)
					{
					  Node nd=XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "Summary", 0);
					  //просрочка до 30 дней
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29").equals("0"))
					  {
						creditInfo.saveSummary(cre, pplmain, skb.getEntity(),XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29"), refBooks.getRefSummaryItem(Summary.DELAY30).getEntity(),null);
					  }
					  //просрочка до 60 дней
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59").equals("0"))
					  {
						creditInfo.saveSummary(cre, pplmain, skb.getEntity(),XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59"), refBooks.getRefSummaryItem(Summary.DELAY60).getEntity(),null);
					  }
					  //просрочка до 90 дней
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89").equals("0"))
					  {
						creditInfo.saveSummary(cre, pplmain, skb.getEntity(),XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89"), refBooks.getRefSummaryItem(Summary.DELAY90).getEntity(),null);
					  }
					  //просрочка более 90 дней
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountMore120"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountMore120").equals("0"))
					  {
						creditInfo.saveSummary(cre, pplmain, skb.getEntity(),XmlUtils.getAttrubuteValue(nd, "DebtCountMore120"), refBooks.getRefSummaryItem(Summary.DELAYMORE).getEntity(),null);
					  }
					}
				}
			
				//были ли запросы других пользователей
				if (XmlUtils.isExistChildInNode(doc, "BODY", "PreviousRequests", 0))
				{
				  //ищем, есть ли summary кол-ву запросов
				  List<SummaryEntity> lstr=summaryDAO.findSummary(pplmain, cre, skb.getEntity(), refBooks.getRefSummaryItem(Summary.REQUESTS_ALL).getEntity());
				  if (lstr==null) {
				    List<Node> lstadd=XmlUtils.findNodeList(doc.getElementsByTagName("PreviousRequests").item(0), "Request");
				    if (lstadd.size()>0) {
					  creditInfo.saveSummary(cre, pplmain, skb.getEntity(),String.valueOf(lstadd.size()), refBooks.getRefSummaryItem(Summary.REQUESTS_ALL).getEntity(),null);		
				    }
				  }
				}
				
			    req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
			    req.setResponsemessage("Данные найдены");
			} else 	{
				//если есть ошибка
				if (XmlUtils.isExistChildInNode(doc, "BODY", "Error", 0)) {
				  req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
				  Node nd=XmlUtils.findChildInNode(doc, "BODY", "Error", 0);
				  String st=XmlUtils.getAttrubuteValue(nd, "Code");
				  req.setResponsecode(st);
				  req.setResponsemessage(XmlUtils.getNodeValueText(nd));
			
				}
				//если есть предупреждение
				if (XmlUtils.isExistChildInNode(doc, "BODY", "Warning", 0))	{
				  req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
				  Node nd=XmlUtils.findChildInNode(doc, "BODY", "Warning", 0);
				  String st=XmlUtils.getAttrubuteValue(nd, "Code");
				  req.setResponsecode(st);
				  req.setResponsemessage(XmlUtils.getNodeValueText(nd));
				
				}
			}//end есть результативный ответ
			
		 }//end если документ нормальный
		 return req;
	}

	@Override
	public void uploadHistory(UploadingEntity uploading, Date sendingDate,
			Boolean isWork) throws KassaException {
 
		Partner skb = refBooks.getPartner(Partner.SKB);
		
		//есть записи
		if (uploading!=null) {
		
			if (sendingDate==null){
			   sendingDate=new Date();
			}
			  
			//урл для выгрузки
			String url=skb.getUrlUploadWork()+"?file-name="+uploading.getName();
			//устанавливаем ssl
			SSLContext sslCon = null;
			try {
				sslCon=initSSLContext(isWork);
			} catch (Exception e) {
				log.severe("Не удалось инициализировать ssl context "+e);
				throw new KassaException("Не удалось инициализировать ssl context "+e);
			}
			byte[] respmessage=null;
			//пытаемся его отправить
			try {
				try {
					respmessage = HTTPUtils.sendHttp("POST",url, uploading.getInfo().getBytes(XmlUtils.ENCODING_WINDOWS),null, sslCon);
				} finally {
					returnSSLContext(isWork, sslCon);
					sslCon=null;
				}
			} catch (Exception e) {
				log.severe("Не удалось выгрузить данные "+e);
				throw new KassaException("Не удалось выгрузить данные");
			}
			//если есть ответ
			if (respmessage!=null) {
				String answer="";
				try {
					answer = new String(respmessage,XmlUtils.ENCODING_WINDOWS);
				} catch (UnsupportedEncodingException e) {
					log.severe("Не удалось перекодировать ответ "+e);
					throw new KassaException("Не удалось перекодировать ответ "+e);
				}
				//сохранили ответ
				uploading.setReport(answer);
				log.info(answer);
				uploading=uploadService.saveUpload(uploading);
				Document doc=XmlUtils.getDocFromString(answer);
                //вернулся нормальный xml
				if (doc!=null) {
					Node nd=XmlUtils.findChildInNode(doc, "upload", "package-info", 0);
					if (nd!=null) {
						uploading.setResponseId(XmlUtils.getAttrubuteValue(nd, "upload-id"));
						uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
						uploading=uploadService.saveUpload(uploading);
						log.info("Успешно прочитали ответ по выгрузке");
					}
				}
				else {
					uploading.setStatus(UploadStatus.UPLOAD_ERROR);
					uploading=uploadService.saveUpload(uploading);
					eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_SKB,"Не удалось прочитать xml ответа при выгрузке",
							null,new Date(),null,null,null);
					log.severe("Не удалось прочитать xml ответа при выгрузке ");
				}
				eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_SKB,"Успешно завершилась загрузка в КБ СКБ",
						null,new Date(),null,null,null);
				//отмечаем записи, которые выгрузили
				uploadService.updateAfterUpload(sendingDate);
				//сохраняем статус успешно выгруженных
				uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_UPLOADED);
				//пишем в базу статус загрузки
				uploading.setDateUploading(sendingDate);
				uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
				uploading=uploadService.saveUpload(uploading);
				log.info("Успешно завершили выгрузку в КБ СКБ");
			}
			//произошла ошибка
			else {
				uploading.setStatus(UploadStatus.UPLOAD_ERROR);
				uploading=uploadService.saveUpload(uploading);
				log.severe("Не удалось загрузить данные в КБ СКБ");
				eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_SKB,"Не удалось загрузить данные в КБ СКБ",
						null,new Date(),null,null,null);
			}
		}
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
		Partner skb = refBooks.getPartner(Partner.SKB);
		//выбрали запись последней выгрузки
		if (uploading==null){
		    uploading=uploadService.getLastUploading(Partner.SKB,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		}
		//не было вообще ни одной выгрузки
		if (uploading==null){
			return;
		}
		//если нет ни одной незавершенной выгрузки
		if (uploading.getStatus()==UploadStatus.RESULT_SUCCESS){
			return;
		}
		//устанавливаем ssl context
		SSLContext sslCon = null;
		try {
			sslCon=initSSLContext(isWork);
		} catch (Exception e) {
			log.severe("Не удалось инициализировать ssl context "+e);
			throw new KassaException("Не удалось инициализировать ssl context "+e);
		}
		log.info("Инициализировали ssl ");
		//урл загрузки
		String urlFirst=isWork?skb.getUrlUploadWork():skb.getUrlUploadTest();
		String url=urlFirst+"?id="+uploading.getResponseId();
		byte[] respmessage=null;
		//пытаемся запросить статус
		try {
			try {
				respmessage = HTTPUtils.sendHttp("GET",url, null,null, sslCon);
			} finally {
				returnSSLContext(isWork, sslCon);
				sslCon=null;
			}
		} catch (Exception e) {
			log.severe("Не удалось запросить статус "+e);
			throw new KassaException("Не удалось запросить статус "+e);
		}
		//если есть ответ
		if (respmessage!=null) {
			
			String answer=new String(respmessage);
			uploading.setReport(answer);
			uploading=uploadService.saveUpload(uploading);
			Document doc=XmlUtils.getDocFromString(answer);
            //вернулся нормальный xml
			if (doc!=null)
			{
				Node nd=XmlUtils.findChildInNode(doc, "upload", "document", 0);
				if (nd!=null)
				{
					//проверяем, в какой стадии обработка
					String st=XmlUtils.getAttrubuteValue(nd, "processing-status");
					if (StringUtils.isNotEmpty(st))
					{
						//если обработка завершена
						if (Convertor.toInteger(st)==UploadStatus.RSTANDART_STATUS_DONE)
						{
							Node ndsum=XmlUtils.findChildInNode(nd, "processing-stage", 1);
							//если обработка завершена
							if (Convertor.toInteger(st)==UploadStatus.RSTANDART_STATUS_DONE)
							  //есть ошибки
							  if (XmlUtils.isExistChildInNode(ndsum, "error", 0)){
								  List<Node> lsterr=XmlUtils.findNodeList(ndsum, "error");
								  if (lsterr.size()>0){
									  for (Node err:lsterr)	{
									    // TODO обработка ошибок, не совсем понятно, что именно делать с возвращаемыми значениями
									  
										    //код ошибки
										    Integer cid=Convertor.toInteger(XmlUtils.getAttrubuteValue(err, "code"));
										
										   	//пишем запись об ошибке
											uploadService.newUploadingError(uploading.getId(),
													 null, null,"Ошибочная команда "+XmlUtils.getAttrubuteValue(err, "source-data-id")+", код ошибки "+cid);
										   	log.info("Описание ошибки - ошибочная команда "+XmlUtils.getAttrubuteValue(err, "source-data-id")+", код ошибки "+cid);
										    
								  }
							  }
							} else {
								//поставим статус успешной загрузки
								uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_RESULT_SUCCESS);
							}
							uploading.setStatus(UploadStatus.RESULT_SUCCESS);
							uploading.setResponseDate(new Date());
							uploading=uploadService.saveUpload(uploading);
						}
					}
				}
			}
		}
		
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
			throws KassaException {
		Partner skb = refBooks.getPartner(Partner.SKB);
		
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.SKB,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		//List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT);
		List<Object[]> lobj=new ArrayList<Object[]>(0);
		log.info("Всего записей для выгрузки "+lstcre.size());
		//есть записи
		if (lstcre.size()>0)
		{
			//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.SKB, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
        			null,null, null, null, null, null, null, null, null, true);
			int i=1;
			for (CreditRequestEntity cre : lstcre) {
                
                //добавляем в массив
                lobj.add(listDataForUpload(cre,uploadDateRange,i));
                //пишем запись в таблицу detail
                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                i++;

            }
            //своя организация
			OrganizationEntity org=orgService.getOrganizationActive();
			
			//делаем xml для выгрузки
			String st=MakeXML.createUploadForSendingToRStandard(uploadDateRange.getTo(), lobj, isWork?skb.getCodeWork():skb.getCodeTest(), 
					org.getName(), upl.getUploadId().toString(),skb.getUploadVersion(),uploadDateRange.getFrom());
			
			log.info("Создали файл для выгрузки в СКБ, кол-во записей "+lstcre.size());
			//название файла выгрузки
			String fname=DatesUtils.DATE_FORMAT_ddMMYYYY.format(uploadDateRange.getTo())+".xml";
			//сохранили данные в таблицу выгрузки
			upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
        			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
			return upl;
		} else {
			return null;
		}
	}

	@Override
	public String getSystemName() {
		return ScoringSkbBeanLocal.SYSTEM_NAME;
	}

	/**
	  * инициализируем ssl-context	
	  * @param isWork - рабочий сервис или тестовый
	  * @return
	  * @throws KassaException
	  */
	 private SSLContext initSSLContext(Boolean isWork) throws KassaException{
			
			try {
				if (isWork) {
					return servBean.borrowSSLContext(new String[] {CryptoSettings.SKB_CLIENT_WORK, CryptoSettings.SKB_SERVER_WORK});
				} else {
					return servBean.borrowSSLContext(new String[] {CryptoSettings.SKB_CLIENT_WORK, CryptoSettings.SKB_SERVER_WORK});			
				}
			} catch (Exception ex) {
				log.severe("Не удалось инициализировать ssl context: "+ex.getMessage());
				throw new KassaException("Не удалось инициализировать ssl context: "+ex); 
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
			settings = new String[] {CryptoSettings.SKB_CLIENT_WORK, CryptoSettings.SKB_SERVER_WORK};
		} else {
			settings = new String[] {CryptoSettings.SKB_CLIENT_WORK, CryptoSettings.SKB_SERVER_WORK};
		}
		try {
			servBean.returnSSLContext(settings, sslCon);
		} catch (Exception e) {
			log.severe("Не удалось вернуть ssl context: "+e.getMessage());
			throw new KassaException("Не удалось вернуть ssl context: "+e);
		}
	}
	
	/**
	 * строка запроса 
	 * @param creditRequest - заявка
	 * @param request - запрос
	 * @param isWork - рабочий или тестовый
	 * @return
	 */
	private String createRequestString(CreditRequestEntity creditRequest,RequestsEntity request,boolean isWork){
		Partner parSkb = refBooks.getPartner(Partner.SKB);				
		//человек
		PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
		PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
		PeopleMiscEntity pplmisc=peopleBean.findPeopleMiscActive(pplmain);
		//документ
		DocumentEntity dc=peopleBean.findPassportActive(pplmain);
		//контакт (телефон)
		PeopleContactEntity pplcont=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
		String partnerId=isWork?parSkb.getCodeWork():parSkb.getCodeTest();
		//своя организация
		OrganizationEntity org=orgService.getOrganizationActive();
		//адрес регистрации
		AddressEntity addrreg=peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
		AddressEntity addrfact=null;
		if (addrreg.getIsSame()!=null&&addrreg.getIsSame()==0){
		    addrfact=peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
		}
		//строка запроса
		String	rmessage =MakeXML.createXMLforRStandard(pplmain,pplper,pplmisc,pplcont,dc,
				creditRequest,addrreg,addrfact,partnerId,org.getName(),
				false,false,false,false,false,false,false,parSkb.getRequestVersion(),
				request.getRequestNumber().toString(),Requests.RS_REPORT_DETAILS);
		log.info(rmessage);
		log.info("Создан запрос в СКБ по заявке "+creditRequest.getId());		
		return rmessage;
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
		//человек
        PeopleMainEntity pplmain = creditRequest.getPeopleMainId();
        PeoplePersonalEntity pplper = peopleBean.findPeoplePersonalActive(pplmain);
        //документ
        DocumentEntity dc = peopleBean.findPassportActive(pplmain);
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
        //займ
        CreditEntity credit = null;
        //информация по займу
        CreditDetailsEntity creditInform=null;
        //продления
        List<CreditDetailsEntity> prolongInfo=new ArrayList<CreditDetailsEntity>(0);
        //платежи
        List<CreditDetailsEntity> pay =new ArrayList<CreditDetailsEntity>(0);
        //просрочка
        List<CreditDetailsEntity> overdue =new ArrayList<CreditDetailsEntity>(0);
        //информация по закрытию займа
        CreditDetailsEntity creditEndInfo=null;
        //передача в суд
        DebtEntity debtCourt=null;
        //есть ли решение суда
        Boolean hasCourtDecision=false;
        //если есть кредит
        if (creditRequest.getAcceptedcreditId()!=null){
            credit=creditRequest.getAcceptedcreditId();
            //информация по кредиту
            List<CreditDetailsEntity> lstCreditInfo=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_CREDIT, uploadDateRange);
            if (lstCreditInfo.size()>0){
            	creditInform=lstCreditInfo.get(0);                    	
            }
            //продления
            prolongInfo=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_PROLONG, uploadDateRange);
            //оплата за текущий промежуток времени
            pay=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_PAYMENT, uploadDateRange);
            //информация по закрытию займа
            creditEndInfo=creditBean.findLastCreditDetail(credit.getId(), uploadDateRange);
            //просрочка - берем за предыдущий день
            if (creditEndInfo!=null){
            	//если кредит закончился, смотрим, не было ли по нему просрочки в предыдущий день
            	List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, 
            			new DateRange(DateUtils.addDays(creditEndInfo.getEventDate(), -1),creditEndInfo.getEventDate()));
                if (lstOvrd.size()>0){
            	    overdue.add(lstOvrd.get(0));
                }
            } else if (prolongInfo.size()>0){
            	//если было продление, смотрим, не было ли по нему просрочки в предыдущий день
            	List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, 
            			new DateRange(DateUtils.addDays(prolongInfo.get(0).getEventDate(), -1),prolongInfo.get(0).getEventDate()));
                if (lstOvrd.size()>0){
            	    overdue.add(lstOvrd.get(0));
                }
            } else {
            	//стандартная просрочка, если не было закрытия или продления
                List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, uploadDateRange);
                if (lstOvrd.size()>0){
            	    overdue.add(lstOvrd.get(0));
                }
            }
           
            //информация по передаче дела в суд
            List<DebtEntity> debts=creditInfo.listDebts(creditRequest.getId(), null, Partner.SYSTEM, null,Debt.DEBT_COURT,null,uploadDateRange);
            if (debts.size()>0){
            	debtCourt=debts.get(0);
            	hasCourtDecision=(debtCourt.getDateDecision()!=null);
            }
        }
        //добавляем в массив
        return new Object[]{i, pplmain, pplper, dc, addrreg, addrres, creditRequest, 
        		credit, pay,creditInform,prolongInfo,overdue,creditEndInfo,debtCourt,hasCourtDecision};
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		 
		    Partner skb = refBooks.getPartner(Partner.SKB);
			
			//даты для выгрузки
			DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.SKB,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
			log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
		    //выбрали заявки для выгрузки
			List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,creditIds);
			List<Object[]> lobj=new ArrayList<Object[]>(0);
			log.info("Всего записей для выгрузки "+lstcre.size());
			//есть записи
			if (lstcre.size()>0)
			{
				//создаем новую запись в таблице загрузки
	        	UploadingEntity upl=uploadService.newUploading(null, Partner.SKB, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
	        			null,null, null, null, null, null, null, null, null, true);
				int i=1;
				for (CreditRequestEntity cre : lstcre) {
	                
	                //добавляем в массив
	                lobj.add(listDataForUpload(cre,uploadDateRange,i));
	                //пишем запись в таблицу detail
	                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
	                i++;

	            }
	            //своя организация
				OrganizationEntity org=orgService.getOrganizationActive();
				
				//делаем xml для выгрузки
				String st=MakeXML.createUploadForSendingToRStandard(uploadDateRange.getTo(), lobj, isWork?skb.getCodeWork():skb.getCodeTest(), 
						org.getName(), upl.getUploadId().toString(),skb.getUploadVersion(),uploadDateRange.getFrom());
				
				log.info("Создали файл для выгрузки в СКБ, кол-во записей "+lstcre.size());
				//название файла выгрузки
				String fname=DatesUtils.DATE_FORMAT_ddMMYYYY.format(uploadDateRange.getTo())+"-cor.xml";
				//сохранили данные в таблицу выгрузки
				upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
	        			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
				return upl;
			} else {
				return null;
			}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private List<CreditEntity> saveSKBCredit(Node cd, CreditRequestEntity cre,PeopleMainEntity pplmain,
			CreditEntity clientCredit,List<CreditEntity> creditList,Integer unique,
			VerificationEntity verification){
		
    	CreditEntity crse=new CreditEntity();
		//отношение к кредитному договору
		crse.setCreditrelationId(refBooks.getCreditRelationType(BaseCredit.CREDIT_OWNER).getEntity());
		//сумма кредита
		crse.setCreditsum(Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "ContractTotal"),CalcUtils.dformat));
		//дата начала
		crse.setCreditdatabeg(Convertor.toDate(XmlUtils.getAttrubuteValue(cd, "OpenDate"), DatesUtils.FORMAT_ddMMYYYY));
		//валюта
		if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "CurrencyID"))){
			if (refBooks.getCurrency(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "CurrencyID")))!=null){
		        crse.setIdCurrency(refBooks.findByCodeIntegerEntity(RefHeader.CURRENCY_TYPE,Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "CurrencyID"))));
		 	} else {
		   		crse.setIdCurrency(refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE,BaseCredit.CURRENCY_UNKNOWN));
		   	}
		}
		//вид кредита
		Integer typeId=0;
		if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "TypeID"))){
			ReferenceEntity rfe =null;
			try {
				rfe = refBooks.findFromKb(RefHeader.CREDIT_TYPE_RS,Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "TypeID")));
			} catch (Exception e) {
				log.severe("Не удалось найти тип кредита " + XmlUtils.getAttrubuteValue(cd, "TypeID"));
			}
		   	if (rfe!=null){
		        crse.setCredittypeId(rfe);
		  	} else {
		   		crse.setCredittypeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_TYPE,BaseCredit.ANOTHER_CREDIT));
		   	}
		} else {
		   	crse.setCredittypeId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_TYPE,BaseCredit.ANOTHER_CREDIT));
		}
		//максимальная просрочка по кредиту
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DayCountOfCurrentOverdueDebt"))){
	    	int overdueDays=Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DayCountOfCurrentOverdueDebt"));
		    crse.setMaxDelay(overdueDays);
		    if (overdueDays==0){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
		    } else if (overdueDays>1&&overdueDays<30){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		    } else if (overdueDays>30&&overdueDays<60){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		    } else if (overdueDays>60&&overdueDays<90){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		    } else if (overdueDays>90){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
		    } 
	    } else {
	    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
	    }
		//проверим на существование в БД такого же кредита
        if (creditList.size()>0){
        	for (CreditEntity credit:creditList){
        		try{
        		    if (creditService.isMatch(crse, credit)&&unique==1){
        		    	//если это кредит клиента, то запишем верификацию
        		    	if (clientCredit!=null&&credit.getPartnersId().getId()==Partner.CLIENT){
        		    		creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
        	            			clientCredit, crse);
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
              	creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
            			clientCredit, crse);
        }
		//дата окончания по графику
		if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "EstimatedCloseDate"))){
		    crse.setCreditdataend(Convertor.toDate(XmlUtils.getAttrubuteValue(cd, "EstimatedCloseDate"), DatesUtils.FORMAT_ddMMYYYY));
		}
		//текущий долг
		Double currentDebt=new Double(0);
		if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "CurrentDebt"))){
		   crse.setCreditsumdebt(Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "CurrentDebt"), CalcUtils.dformat));
		   currentDebt=Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "CurrentDebt"), CalcUtils.dformat);
		}
		//неиспользованый лимит
		if (Utils.defaultIntegerFromNull(typeId)==BaseCredit.CREDIT_CARD){
		   	crse.setCreditlimitunused(crse.getCreditsum()-Utils.defaultDoubleFromNull(currentDebt));
		}
	    //текущий просроченный долг
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "CurrentOverdueDebt"))) {
		    crse.setCurrentOverdueDebt(Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "CurrentOverdueDebt"), CalcUtils.dformat));
	    }
	    //максимальный просроченный долг
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "MaxOverdueDebt"))) {
		    crse.setMaxOverdueDebt(Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "MaxOverdueDebt"), CalcUtils.dformat));
	    }
	    		  
	    //дата окончания реальная
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "RealCloseDate"))){
		    crse.setCreditdataendfact(Convertor.toDate(XmlUtils.getAttrubuteValue(cd, "RealCloseDate"), DatesUtils.FORMAT_ddMMYYYY));
	    } 
	    //статус кредита
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "StateID"))){
	    	try {
				ReferenceEntity rfe = refBooks.findFromKb(RefHeader.CREDIT_STATUS_NBKI, Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "StateID")));
				crse.setCreditStatusId(rfe);
				if (crse.getCreditStatusId()!=null&&crse.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_CLOSED){
					crse.setIsover(true);
				} else {
					crse.setIsover(false);
				}
			} catch (Exception e) {
				log.severe("Не удалось найти статус кредита " + XmlUtils.getAttrubuteValue(cd, "StateID"));
			}
	    
	    }
	    //просрочек до 30 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom1To29"))){
		    crse.setDelay30(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom1To29")));
	    }
	    //просрочек до 60 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom30To59"))) {
		    crse.setDelay60(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom30To59")));
	    }
	    //просрочек до 90 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom60To89"))) {
		    crse.setDelay90(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom60To89")));
	    }
	    //просрочек до 120 дней
	    Integer delay=0;
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom90To119"))){
	    	delay=Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom90To119"));
	    }
	    //просрочек более 120 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountMore120"))){
		    crse.setDelaymore(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountMore120"))+Utils.defaultIntegerFromNull(delay));
	    } else {
	    	crse.setDelaymore(Utils.defaultIntegerFromNull(delay));
	    }
	    //кол-во продлений
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "ProlongationCount"))){
		    crse.setCreditlong(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "ProlongationCount")));
	    }
	    //проценты по договору
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "PercentDuration"))){
		    crse.setCreditpercent(Convertor.toDouble(XmlUtils.getAttrubuteValue(cd, "PercentDuration"), CalcUtils.dformat_xx));
	    }
	    //максимально просрочки в днях
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DayCountOfMaxOverdueDebt"))){
		    crse.setMaxDelay(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DayCountOfMaxOverdueDebt")));
	    }
	    //просрочка по текущему долгу
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DayCountOfCurrentOverdueDebt"))){
		    crse.setDelayindays(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DayCountOfCurrentOverdueDebt")));		    
		}
	    //дата последнего обновления информации
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "LastUpdatedDate"))){
		    crse.setDatelastupdate(Convertor.toDate(XmlUtils.getAttrubuteValue(cd, "LastUpdatedDate"), DatesUtils.FORMAT_ddMMYYYY));
	    }
	   
	    crse.setIssameorg(false);
	    crse.setCreditRequestId(cre);
	    crse.setPeopleMainId(pplmain);
	    crse.setPartnersId(refBooks.getPartnerEntity(Partner.SKB));
	    crse.setIsactive(ActiveStatus.ACTIVE); 
	    
	    //смотрим платежи из истории
	    String paydisc="";
	    Double paysum=new Double(0);
		Node ndp=XmlUtils.findChildInNode(cd, "StatementHistory", 0);
		if (ndp!=null)	{
			  List<Node> lstpay=XmlUtils.findNodeList(ndp, "Statement");
			  if (lstpay.size()>0)
			  {
				  for (Node ndpay:lstpay)
				  {
					  CreditHistoryPayEntity crp=new CreditHistoryPayEntity();
					  crp.setCreditId(crse);
					  //дата платежа
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(ndpay, "Date"))){
						 crp.setHistoryDate(Convertor.toDate(XmlUtils.getAttrubuteValue(ndpay, "Date"), DatesUtils.FORMAT_ddMMYYYY));
					  }
					  //просроченный долг
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(ndpay, "OverdueAmount"))){
						  crp.setOverdueSum(Convertor.toDouble(XmlUtils.getAttrubuteValue(ndpay, "OverdueAmount"), CalcUtils.dformat));
					  }
					  //основной долг
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(ndpay, "OutstandingPrincipleAmount"))){
						  crp.setAccountSum(Convertor.toDouble(XmlUtils.getAttrubuteValue(ndpay, "OutstandingPrincipleAmount"), CalcUtils.dformat));
					  }
					  //долг
					  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(ndpay, "Amount"))){
						  crp.setPaidSum(Convertor.toDouble(XmlUtils.getAttrubuteValue(ndpay, "Amount"), CalcUtils.dformat));
						  paysum+=Utils.defaultDoubleFromNull(Convertor.toDouble(XmlUtils.getAttrubuteValue(ndpay, "Amount"), CalcUtils.dformat));
						  
					  }
					  //вид просрочки
			          if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID"))) {
			            	 try {
			            		 
			            		if (refBooks.getCreditOverdueType(XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID"))!=null){
			            		
				                  crp.setPaymentHistoryId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE,XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID")));
				                  paydisc=XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID")+paydisc;
				                  
			            		}
			            	 } catch (Exception e) {
			            		 log.severe("Не удалось найти вид просрочки в справочнике "+XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID"));
			            	 }
				          }
			       //   crse.getHistorypays().add(crp);
				  }//end for
			  }//end есть платежи
		}//end есть история платежей
			
		crse.setCreditSumPaid(paysum);
		crse.setPayDiscipline(Convertor.toLimitString(paydisc,200));
		creditDAO.saveCreditEntity(crse);
		creditList.add(crse);
		return creditList;
	}


		
}
