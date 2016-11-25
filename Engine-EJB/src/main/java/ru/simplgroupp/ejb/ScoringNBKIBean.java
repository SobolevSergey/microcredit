package ru.simplgroupp.ejb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.exception.ReferenceException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringNBKIBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.services.CreditService;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PaymentEntity;
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
import ru.simplgroupp.toolkit.common.FileUtil;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.toolkit.common.ZipUtils;
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
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.transfer.Summary;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.transfer.Uploading;
import ru.simplgroupp.transfer.VerificationConfig;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MakeXML;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.SettingsKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;
import ru.simplgroupp.xsd.nbki.Account;
import ru.simplgroupp.xsd.nbki.Address;
import ru.simplgroupp.xsd.nbki.CodeText;
import ru.simplgroupp.xsd.nbki.ConsentReq;
import ru.simplgroupp.xsd.nbki.CreditReport;
import ru.simplgroupp.xsd.nbki.CreditReportRequest;
import ru.simplgroupp.xsd.nbki.Id;
import ru.simplgroupp.xsd.nbki.Inquiry;
import ru.simplgroupp.xsd.nbki.LegalItem;
import ru.simplgroupp.xsd.nbki.NbchScoringReportType;
import ru.simplgroupp.xsd.nbki.Person;
import ru.simplgroupp.xsd.nbki.Phone;
import ru.simplgroupp.xsd.nbki.ProductRequest;
import ru.simplgroupp.xsd.nbki.ProductType;
import ru.simplgroupp.xsd.nbki.Reference;
import ru.simplgroupp.xsd.nbki.Requestor;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringNBKIBeanLocal.class)
public class ScoringNBKIBean extends ScoringNBKIFakeBean {

	private static final char NO_PAYMENTS_IN_MONTH = 'X';
	private static final String[] SSL_SETTINGS_TEST = new String[] { CryptoSettings.NBKI_CLIENT_TEST, CryptoSettings.NBKI_SERVER_TEST };
	private static final String[] SSL_SETTINGS_WORK = new String[] { CryptoSettings.NBKI_CLIENT_WORK, CryptoSettings.NBKI_SERVER_WORK };
	private static final String[] SSL_SETTINGS_ENCRYPT_WORK = new String[] { CryptoSettings.NBKI_CLIENT_WORK, CryptoSettings.NBKI_SERVER_WORK };
	
	@Inject Logger logger;
	
	@EJB
	private ServiceBeanLocal servBean;

	@EJB
	private PeopleBeanLocal peopleBean;

	@EJB
	PeopleDAO peopleDAO;

	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	CreditBeanLocal creditBean;

	@EJB
	ICryptoService crypto;
	
	@EJB
	CreditInfoService creditInfo;
	
	@EJB
	UploadingService uploadService;
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
	PaymentService paymentService;
		
	@EJB
	CreditService creditService;
	
	@EJB
	SummaryDAO summaryDAO;
	
	@EJB
	ProductBeanLocal productBean;
		
	@EJB
	RulesBeanLocal rulesBean;
	
	@Override
	public boolean isFake() {
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork, Integer cacheDays) throws ActionException {
		return newRequest(cre,isWork,false,"",cacheDays,false);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork, Boolean requestScoring,
			String urlAdditional,Integer cacheDays,Boolean useSSL) throws ActionException {
		
		//проверяем, кеширован ли запрос
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
				    		Partner.NBKI, new Date(), cacheDays);
		if (inCache){
		   	log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в КБ НБКИ, он кеширован");
			//save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_NBKI,
				"Запрос к внешнему партнеру КБ НБКИ не производился, данные кешированы",
						null,new Date(),crDAO.getCreditRequestEntity(cre.getId()),null,null);
		
			return null;
		}
				
		RequestsEntity req = requestsService.addRequest(cre.getPeopleMainId().getId(), cre.getId(), Partner.NBKI,
				RequestStatus.STATUS_IN_WORK, new Date());
		Partner partner = refBooks.getPartner(Partner.NBKI);

		// параметры для запроса
		String url = (isWork ? partner.getUrlWork() : partner.getUrlTest())+urlAdditional;
		logger.info("url for request "+url);
		String login = isWork ? partner.getLoginWork() : partner.getLoginTest();
		String password = isWork ? partner.getPasswordWork() : partner.getPasswordTest();
		String membercode = isWork ? partner.getCodeWork() : partner.getCodeTest();
		byte[] message;

		try {
			message = makeNBKIRequest(cre, req, login, password, membercode);
		} catch (Exception e) {
			log.severe("Не удалось создать запрос к КБ НБКИ " + e);
			throw new ActionException(ErrorKeys.CANT_CREATE_SSL_CONTEXT, "Не удалось создать запрос к КБ НБКИ", Type.TECH,
					ResultType.NON_FATAL, e);
		}

		String rmessage="";
		try {
			rmessage=new String(message,XmlUtils.ENCODING_WINDOWS);
		} catch (UnsupportedEncodingException e1) {
			logger.severe("Не удалось преобразовать xml в строку");
		}
		
		logger.info(rmessage);
		
		req.setRequestbody(message);
		req = requestsService.saveRequestEx(req);

		// параметры для httpPost
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("Content-Type", "text/xml");

		// инициализируем ssl context
		SSLContext sslContext = null;
		//если работаем с ssl
        if (useSSL){
		    try {
			    sslContext = servBean.borrowSSLContext(isWork ? SSL_SETTINGS_WORK : SSL_SETTINGS_TEST );
		    } catch (Exception ex) {
			    // set request status and save it
		    	requestsService.saveRequestError(req,ex,ErrorKeys.CANT_CREATE_SSL_CONTEXT,"Не удалось создать ssl context",Type.TECH, ResultType.NON_FATAL);
		    }
        } else {
        	sslContext=crypto.createTrustedEmptyGostSSLContext();
        }

		// послали запрос, получили синхронный ответ
		byte[] response = null;
		try {
			try {
				response = HTTPUtils.sendHttp("POST", url, message, postParams, sslContext);
			} finally {
				if (useSSL){
				    servBean.returnSSLContext(isWork ? SSL_SETTINGS_WORK : SSL_SETTINGS_TEST, sslContext);
				}
				sslContext = null;
			}
		} catch (Exception e) {
			// set request status and save it
			requestsService.saveRequestError(req,e,ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.TECH, ResultType.NON_FATAL);
		}

		if (response != null) {
			//преобразуем xml
			String xmlResponse ="";
			try {
				xmlResponse =new String(response,XmlUtils.ENCODING_WINDOWS);
			} catch (UnsupportedEncodingException e2) {
				logger.severe("Не удалось преобразовать ответ в строку");
			}
			//если в ответ почему-то пришла html страница
			if (xmlResponse.toUpperCase().contains("HTML")){
				 logger.severe(xmlResponse);
				 requestsService.saveRequestError(req,null,ErrorKeys.BAD_RESPONSE, "Вернулся ответ ошибки HTTP", Type.TECH, ResultType.FATAL);
			}
			
			String xmlContent="";
			try {
				xmlContent=crypto.extractXmlFromPkcs7(response, XmlUtils.ENCODING_WINDOWS);
			} catch (CryptoException e2) {
				logger.severe("Не удалось преобразовать ответ в строку "+e2);
			}
						
			req.setResponsebody(response);
			req.setResponsebodystring(xmlContent);
			req = requestsService.saveRequestEx(req);
			
			boolean isGood = true;

			//проверяем подпись
			try {
				isGood = crypto.verifyJCPCms(response, isWork ? CryptoSettings.NBKI_CHECK_WORK : CryptoSettings.NBKI_CHECK_TEST );
			} catch (Exception e) {
				logger.severe("Подпись ответа неверна "+e);
			}

			StreamSource xml=null;
			try {
				xml = new StreamSource(new ByteArrayInputStream(xmlContent.getBytes(XmlUtils.ENCODING_WINDOWS)));
			} catch (UnsupportedEncodingException e1) {
				logger.severe("Не удалось получить xml для обработки из ответа");
			}
			
			JAXBContext jc;
			JAXBElement<ProductType> je = null;
			JAXBElement<NbchScoringReportType> scoreReport = null;
			ProductType product = null;
			try {
				if (requestScoring){
					jc = JAXBContext.newInstance(NbchScoringReportType.class);
				    Unmarshaller unmarshaller = jc.createUnmarshaller();
				    scoreReport=unmarshaller.unmarshal(xml, NbchScoringReportType.class);
				    product=scoreReport.getValue().getProduct();	
				} else {
				    jc = JAXBContext.newInstance(ProductType.class);
				    Unmarshaller unmarshaller = jc.createUnmarshaller();
				    je = unmarshaller.unmarshal(xml, ProductType.class);
				    product=je.getValue();
				}
			} catch (JAXBException e) {
				// set request status and save it
				requestsService.saveRequestError(req,e,ErrorKeys.BAD_RESPONSE, "Не удалось разобрать ответ ", Type.TECH, ResultType.FATAL);
			}

			
			if (product.getPreply().getErr() == null) {
				log.info("Получили данные из НБКИ по заявке " + cre.getId());
	
				try {
					req = saveAnswer(req, xmlContent,requestScoring);
				} catch (Exception ek) {
					requestsService.saveRequestError(req,ek,ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать xml", Type.TECH, ResultType.FATAL);
				}
			} else {
				final CodeText ct = product.getPreply().getErr().getCtErr().get(0);
				
				req.setResponsecode(ct.getCode());
				req.setResponsemessage(ct.getText());
				req.setResponsedate(new Date());
				req = requestsService.saveRequestEx(req);
				
				//если  ошибка не связана с тем, что заемщик не найден
				if (StringUtils.isNotEmpty(req.getResponsecode())
						&&Convertor.toInteger(req.getResponsecode())!=null&&Convertor.toInteger(req.getResponsecode())!=Requests.ERROR_CODE_NBKI_NOT_FOUND){
					requestsService.saveRequestError(req,null,ErrorKeys.BAD_RESPONSE, "Ошибка в ответе: (" + ct.getCode() + ") " + ct.getText(), Type.TECH, ResultType.FATAL);
				}
			}//end если нет ошибки
		}//end если есть ответ

		req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
		req = requestsService.saveRequestEx(req);

		eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_NBKI, "Произведен вызов внешней системы КБ НБКИ",
					null,new Date(),crDAO.getCreditRequestEntity(cre.getId()), null, null);
		return req;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
		return saveAnswer(req,answer,false);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer,Boolean requestScoring) throws KassaException {
		JAXBContext jc;
		JAXBElement<ProductType> je = null;
		JAXBElement<NbchScoringReportType> scoreReport = null;
		try {
			StreamSource xml=null;
			try {
				xml = new StreamSource(new ByteArrayInputStream(answer.getBytes(XmlUtils.ENCODING_WINDOWS)));
			} catch (UnsupportedEncodingException e) {
				logger.severe("Не удалось преобразовать xml "+e);
			}
			
			//если есть скоринг
			if (requestScoring){
					jc = JAXBContext.newInstance(NbchScoringReportType.class);
				    Unmarshaller unmarshaller = jc.createUnmarshaller();
				    scoreReport=unmarshaller.unmarshal(xml, NbchScoringReportType.class);
			} else {
				    jc = JAXBContext.newInstance(ProductType.class);
				    Unmarshaller unmarshaller = jc.createUnmarshaller();
				    je = unmarshaller.unmarshal(xml, ProductType.class);
			}
			
			
		} catch (JAXBException e) {
			// set request status and save it
			
			try {
				requestsService.saveRequestError(req, e, ErrorKeys.BAD_RESPONSE, "Не удалось разобрать ответ"+e, Type.TECH, ResultType.FATAL);
			} catch (Exception e1) {
				throw new KassaException(e1);
			}

		}

		ProductType product = null;
		
		//если есть скоринг
		if (requestScoring){
			product=scoreReport.getValue().getProduct();
			if (scoreReport.getValue().getFicoRisk()!=null){
			    //запишем в БД
			    creditInfo.saveScoring(req.getCreditRequestId().getId(), req.getCreditRequestId().getPeopleMainId().getId(), 
					Partner.NBKI, null,null, scoreReport.getValue().getFicoRisk().getScore().doubleValue(),	
					scoreReport.getValue().getFicoRisk().getErrorText(),null);
			}
		} else {
		    product=je.getValue();
		}

		// кредитный отчёт
		CreditReport report = product.getPreply().getReport();

		//номер отчета и дата
		req.setResponseguid(report.getInqControlNum());
		req.setResponsedate(new Date());
		
		// заявка
		CreditRequestEntity ccre = crDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
		// человек
		PeopleMainEntity pplmain = ccre.getPeopleMainId();

		//ищем все кредиты по этой заявке
		List<CreditEntity> creditList=creditBean.findCredits(pplmain, ccre, null, null, null);
		
		//ищем кредит клиента
		List<CreditEntity> lstClientCredit=creditBean.findCredits(pplmain, ccre,refBooks.getPartnerEntity(Partner.CLIENT), false, null);
		CreditEntity clientCredit=null;
		if (lstClientCredit.size()>0){
			clientCredit=lstClientCredit.get(0);
		}
	
		Map<String,Object> params=productBean.getConsiderProductConfig(ccre.getProductId().getId());
		Integer uniqueCredits=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_CREDITS));
		Integer uniqueDocuments=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_DOCUMENTS));
		
		PeoplePersonalEntity ppl = peopleBean.findPeoplePersonalActive(pplmain);
		// ищем, были ли записаны ПД
		List<PeoplePersonalEntity> lstpplpar = peopleBean.findPeoplePersonal(pplmain, req.getCreditRequestId(), Partner.NBKI,
				ActiveStatus.ACTIVE);

		// Персональные данные
		if (lstpplpar.size() == 0) {
			for (Person person : report.getPersonReply()) {
				// если что-то в персональных данных не совпало, пишем запись в
				// БД
				if (!ppl.getSurname().trim().equalsIgnoreCase(person.getName1()) || !ppl.getName().trim().equalsIgnoreCase(person.getFirst())
						|| !ppl.getMidname().equalsIgnoreCase(person.getPaternal())
						|| !DatesUtils.isSameDay(ppl.getBirthdate(),person.getBirthDt().toGregorianCalendar().getTime()) ) {

					try {
						peopleBean.newPeoplePersonal(null, pplmain.getId(), ccre.getId(), Partner.NBKI, person.getName1(), person.getFirst(),
								person.getPaternal(), null, person.getBirthDt().toGregorianCalendar().getTime(), person.getPlaceOfBirth(), 
								Convertor.toInteger(person.getGender()), new Date(),
								ActiveStatus.ACTIVE);
					} catch (Exception e) {
						log.severe("Не удалось сохранить актуальные ПД по заявке " + ccre.getId());
					}
				}// end если что-то не совпало
				break;
			}
		}

		// документы
		// ищем, писали ли документы
		DocumentEntity pasppar = peopleBean.findDocument(pplmain, ccre, Partner.NBKI, ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
		// если нет
		if (pasppar == null) {
		  List<DocumentEntity> lstDoc=peopleBean.findDocuments(pplmain, null, uniqueDocuments==1?null:Partner.NBKI, null, Documents.PASSPORT_RF);
		  for (Id id : report.getIdReply()) {
			// только паспорт
			if (Integer.toString(Documents.PASSPORT_RF).equals(id.getIdType())) {
				
				    int docs=0;        
				    if (lstDoc.size()>0){
                        //проверяем документы на дубликаты
                        for (int i=0;i<lstDoc.size();i++){
					      // Если данные документа не совпадают, пишем в БД
                          DocumentEntity pasp =	lstDoc.get(i);
					      if (!pasp.getSeries().equalsIgnoreCase(Convertor.fromMask(id.getSeriesNumber())) || !pasp.getNumber()
									.equalsIgnoreCase(id.getIdNum())) {
					          docs++;
						  
					      }
                        }//end for
				    }//end lstDoc.size>0
                    if (docs==lstDoc.size()){
              	      try {
						 DocumentEntity newPasp=peopleBean.newDocument(null,pplmain.getId(), ccre.getId(), Partner.NBKI, id.getSeriesNumber(), id.getIdNum(), 
								id.getIssueDate().toGregorianCalendar().getTime(), null, id.getIssueAuthority(), ActiveStatus.ACTIVE);
						 lstDoc.add(newPasp);
					  } catch (Exception e) {
						log.severe("Не удалось записать активный документ по заявке " + ccre.getId() + ", ошибка " + e);
					  }
                    }//end записали паспорт
			
			} else if (Integer.toString(PeopleMain.INN_NBKI).equals(id.getIdType())){
				try {
					peopleBean.savePeopleMain(pplmain,id.getIdNum(), null);
				} catch (PeopleException e) {
					log.severe("Не удалось записать инн по человеку "+pplmain.getId()+", ошибка "+e);
				}
			} else if (Integer.toString(PeopleMain.SNILS_NBKI).equals(id.getIdType())){
				try {
					peopleBean.savePeopleMain(pplmain,null, id.getIdNum());
				} catch (PeopleException e) {
					log.severe("Не удалось записать снилс по человеку "+pplmain.getId()+", ошибка "+e);
				}
			}
		  }//end for документы
		}// end если не писали документы
		
		// адреса
		for (Address a : report.getAddressReply()) {
			if (String.valueOf(BaseAddress.REGISTER_ADDRESS_RS).equals(a.getAddressType())) {
				checkAddress(pplmain, ccre, Partner.NBKI, a, BaseAddress.REGISTER_ADDRESS);
			} else if (String.valueOf(BaseAddress.RESIDENT_ADDRESS_RS).equals(a.getAddressType())) {
				checkAddress(pplmain, ccre, Partner.NBKI, a, BaseAddress.RESIDENT_ADDRESS);
			}
		}

		//телефоны
		if (report.getPhoneReply()!=null&&report.getPhoneReply().size()>0){
		  //писали ли телефоны
		  List<PeopleContactEntity> lstContact=peopleBean.findPeopleByContact(null, null, pplmain.getId(), 
				  Partner.NBKI, ActiveStatus.ACTIVE, ccre.getId());
		  //если не писали
		  if (lstContact.size()==0){
			 Map<String,Object> sparams=rulesBean.getSiteConstants();
	         String site = (String) sparams.get(SettingsKeys.CONNECT_DB_URL); 
	         boolean hasConnection=false;
	         try {
				hasConnection=HTTPUtils.isConnected(site);
			} catch (Exception e1) {
				logger.severe("Не удалось соединиться с сайтом "+site);
			}
			 for(Phone phone:report.getPhoneReply()){
				 //если этого телефона нет у нас в БД
				 String phoneNumber=StringUtils.isNotEmpty(phone.getNumber())?phone.getNumber():" ";
			     if (StringUtils.isEmpty(phoneNumber)||phoneNumber.length()<10||phoneNumber.length()>11){
			    	continue;
			     } else if (phoneNumber.length()==10){
			    	phoneNumber=String.valueOf(PeopleContact.PHONE_COUNTRY_CODE_RU)+phoneNumber;
			     } 
				 if (!peopleBean.phoneExists(pplmain.getId(), phoneNumber)){								  
				      try {
					  
					    ReferenceEntity ref=refBooks.findFromKb(RefHeader.CONTACT_TYPE_NBKI, Convertor.toInteger(phone.getPhoneType())); 
					    Integer phoneType=PeopleContact.CONTACT_OTHER;
					    if (ref!=null){
						  phoneType=ref.getCodeinteger();
					    }
					    peopleBean.setPeopleContact(pplmain, refBooks.getPartnerEntity(Partner.NBKI), phoneType, phoneNumber, 
					      false, ccre.getId(), phone.getLastUpdatedDt()!=null?phone.getLastUpdatedDt().toGregorianCalendar().getTime():new Date(),
					    		  hasConnection);
				    } catch (Exception e) {
					    log.severe("Не удалось записать телефоны, ошибка "+e);
				    } 
				  }//end если этого телефона нет
			 }
		  }//end не писали телефоны
		}//end если есть телефоны
		
		//суды
		if (report.getLegalItemsReply()!=null &&report.getLegalItemsReply().size()>0){
			//ищем, писали ли данные 	
			  List<DebtEntity> lstDebt=creditInfo.listDebts(ccre.getId(), pplmain.getId(), Partner.NBKI, null, Debt.DEBT_COURT, null, null);
			  //если не писали
			  if (lstDebt.size()==0) {
				for(LegalItem legal:report.getLegalItemsReply()){
					creditInfo.newDebt(ccre.getId(), pplmain.getId(), Partner.NBKI, null,
							null, null,null, legal.getCourt(), 
							legal.getReportedDt()!=null?legal.getReportedDt().toGregorianCalendar().getTime():null,
							legal.getFilingNum(),legal.getResolution(), null, null,	Debt.DEBT_COURT,
							legal.getConsideredDt()!=null?legal.getConsideredDt().toGregorianCalendar().getTime():null,
							ActiveStatus.ACTIVE);
				}
			  }//end если не писали
		}//end если есть записи по судам
		
		//кредиты
		List<CreditEntity> credpar = creditBean.findCredits(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), false, null);
		// не писали
		if (credpar.size() == 0) {
			//делаем старые кредиты неактивными
			creditDAO.makePartnerCreditsNotActive(Partner.NBKI,pplmain.getId());
			
			// Кредиты
			for (Account credit : report.getAccountReply()) {
				//ищем системную верификацию, если есть кредит клиента
				VerificationEntity verification=null;
				if (clientCredit!=null){
				    verification=creditInfo.findOneVerification(ccre.getId(), pplmain.getId(), Partner.SYSTEM, null);
				}
				creditList=saveNbkiCredit(credit,pplmain,ccre,clientCredit,creditList,uniqueCredits,verification);
		
			}//end for
		}//end если не писали

		//суммарная информация
		if (report.getCalc()!=null){
			//дата последнего кредита
			List<SummaryEntity> lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.DATE_MOST_RECENT_CREDIT).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getMostRecentAcc()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							Convertor.dateToString(report.getCalc().getMostRecentAcc().toGregorianCalendar().getTime(), DatesUtils.FORMAT_ddMMYYYY), 
							refBooks.getRefSummaryItem(Summary.DATE_MOST_RECENT_CREDIT).getEntity(),null);	
				}
			}
			//число счетов с негативным рейтингом
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.NEGATIVE_RATING).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getNegativeRating()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getNegativeRating().toString(), 
							refBooks.getRefSummaryItem(Summary.NEGATIVE_RATING).getEntity(),null);	
				}
			}
			//число активных кредитов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.ACTIVE_CREDITS_NUMBER).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalActiveBalanceAccounts()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalActiveBalanceAccounts().toString(), 
							refBooks.getRefSummaryItem(Summary.ACTIVE_CREDITS_NUMBER).getEntity(),null);	
				}
			}
			//число  кредитов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.CREDITS_NUMBER).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalAccts()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalAccts().toString(), 
							refBooks.getRefSummaryItem(Summary.CREDITS_NUMBER).getEntity(),null);	
				}
			}
			//число официальных элементов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_OFFICIAL).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalOfficialInfo()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalOfficialInfo().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_OFFICIAL).getEntity(),null);	
				}
			}
			//число банкротств
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_BANKRUPCY).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalBankruptcies()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalBankruptcies().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_BANKRUPCY).getEntity(),null);	
				}
			}
			//число судебных элементов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_COURT).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalLegalItems()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalLegalItems().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_COURT).getEntity(),null);	
				}
			}
			//число спорных официальных элементов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_DISPUTED_OFFICIAL).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalDisputedOfficialInfo()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalDisputedOfficialInfo().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_DISPUTED_OFFICIAL).getEntity(),null);	
				}
			}
			//число спорных элементов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_DISPUTED).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalDisputedAccounts()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalDisputedAccounts().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_DISPUTED).getEntity(),null);	
				}
			}
			//число спорных судебных элементов
			lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.INFO_DISPUTED_COURT).getEntity());
			if (lstSummary==null){
				if (report.getCalc().getTotalDisputedLegalItem()!=null){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),
							report.getCalc().getTotalDisputedLegalItem().toString(), 
							refBooks.getRefSummaryItem(Summary.INFO_DISPUTED_COURT).getEntity(),null);	
				}
			}
		}
		//запросы
		if (report.getInquiryReply()!=null&&report.getInquiryReply().size()>0){
			List<SummaryEntity> lstSummary=summaryDAO.findSummary(pplmain, ccre, refBooks.getPartnerEntity(Partner.NBKI), refBooks.getRefSummaryItem(Summary.REQUESTS_ALL).getEntity());
			//если не писали кол-во запросов по заявке
			if (lstSummary==null){
				int reccnt=0;
				int reccntM=0;
				for(Inquiry inquiry:report.getInquiryReply()){
					//если передали дату
					if(inquiry.getInquiryDt()!=null){
						if (DatesUtils.daysDiff(new Date(),inquiry.getInquiryDt().toGregorianCalendar().getTime())<=7
								&&(StringUtils.isEmpty(inquiry.getUserReference())||(StringUtils.isNotEmpty(inquiry.getUserReference())
								&&!inquiry.getUserReference().equalsIgnoreCase(refBooks.getPartnerEntity(Partner.NBKI).getCodework())))){
							reccnt++;
						} else if (DatesUtils.daysDiff(new Date(),inquiry.getInquiryDt().toGregorianCalendar().getTime())<=30
								&&(StringUtils.isEmpty(inquiry.getUserReference())||(StringUtils.isNotEmpty(inquiry.getUserReference())
								&&!inquiry.getUserReference().equalsIgnoreCase(refBooks.getPartnerEntity(Partner.NBKI).getCodework())))){
							reccntM++;
						}
					} else {
						if (inquiry.getInquiryPeriod().contains("7 дней")&&(StringUtils.isEmpty(inquiry.getUserReference())||(StringUtils.isNotEmpty(inquiry.getUserReference())
								&&!inquiry.getUserReference().equalsIgnoreCase(refBooks.getPartnerEntity(Partner.NBKI).getCodework())))){
							reccnt++;
						} else if (inquiry.getInquiryPeriod().contains("месяц")&&(StringUtils.isEmpty(inquiry.getUserReference())||(StringUtils.isNotEmpty(inquiry.getUserReference())
								&&!inquiry.getUserReference().equalsIgnoreCase(refBooks.getPartnerEntity(Partner.NBKI).getCodework())))){
							reccntM++;
						}
					}//end если передали дату
				}//end for
				//если количество запросов больше нуля, запишем
				if (reccnt>0){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),String.valueOf(reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_WEEK).getEntity(),null);	
				}
				//если количество запросов больше нуля, запишем
				if (reccntM>0){
					creditInfo.saveSummary(ccre, pplmain, refBooks.getPartnerEntity(Partner.NBKI),String.valueOf(reccntM+reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_MONTH).getEntity(),null);	
				}
			}//end если не писали
		}//end если были запросы по клиенту
		
		return req;
	}

	private void checkAddress(PeopleMainEntity pplmain, CreditRequestEntity ccre, Integer partner, Address a, int addrtype) {
		AddressEntity addrpar = peopleBean.findAddress(pplmain, Partner.NBKI, ccre, addrtype, ActiveStatus.ACTIVE);

		if (addrpar == null) {
		
			Integer addressTypeId=null;
			try {
				ReferenceEntity rfe = refBooks.findFromKb(RefHeader.ADDRESS_TYPE_RS, Convertor.toInteger(a.getAddressType()));
				if (rfe!=null){
					addressTypeId=rfe.getCodeinteger();
				}
			} catch (ReferenceException e) {
				log.severe("Не удалось найти вид адреса: " + a.getAddressType());
			}
            
			Date databeg=null;
			if (a.getAddrSinceDt()!=null){
				databeg=a.getAddrSinceDt().toGregorianCalendar().getTime();
			} else if (a.getLastUpdatedDt()!=null){
			    databeg=a.getLastUpdatedDt().toGregorianCalendar().getTime();
			} 
			
			  try {
								
				  AddressEntity address=peopleBean.newAddressFias(null,pplmain.getId(), ccre.getId(), Partner.NBKI, 
						addressTypeId,null, databeg, null, BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ACTIVE,null,
						a.getHouseNumber(),	a.getBlock(),a.getBuilding(),a.getApartment());
				  
				  peopleBean.saveAddressDataWithStrings(address, StringUtils.isNotEmpty(a.getProv())?a.getProv():"неизвестно", null,
						  StringUtils.isNotEmpty(a.getSubDistrict())?a.getSubDistrict():null, null, 
						  StringUtils.isEmpty(a.getSubDistrict())?a.getCity():null, null, 
						  StringUtils.isNotEmpty(a.getSubDistrict())?a.getDistrict():null, null, 
						  StringUtils.isNotEmpty(a.getStreet())?a.getStreet():null, 
						  StringUtils.isNotEmpty(a.getStreet())?a.getStreetTypeText():null);
					
			  } catch (Exception e) {
				  log.severe("Не удалось записать адрес по заявке "+ccre.getId()+",ошибка "+e);
			  }
			  
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadHistory(UploadingEntity uploading, Date sendingDate, Boolean isWork) throws KassaException {
		Partner nbki=refBooks.getPartner(Partner.NBKI);
			
		if (uploading!=null){
		
		  try {
		  
			  if (sendingDate==null){
				  sendingDate=new Date();
			  }
			  List<File> fileList=new ArrayList<File>();
			  File file=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName());
			  fileList.add(file);
			  FileUtils.writeStringToFile(file, uploading.getInfo(), XmlUtils.ENCODING_WINDOWS);
			  //подписываем, подпись отсоединенная
			  try {
				 crypto.createJCPCMS(FileUtils.readFileToByteArray(file), CryptoSettings.NBKI_ENCRYPT_CLIENT_WORK,true, XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".sig");
		      } catch (Exception e) {
		    	  logger.severe("Не удалось подписать файл "+e);
			      throw new KassaException("Не удалось подписать файл "+e);
		      }
			  File file1=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".sig");
			  fileList.add(file1);   
		      //зипуем
			  File file2=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip");
		      ZipUtils.ZipMultiFiles(fileList, file2);
		      
		      //шифруем
		      byte[] enc=null;
		      try {
			     enc=crypto.encryptPKCS7(FileUtils.readFileToByteArray(file2), CryptoSettings.NBKI_ENCRYPT_WORK, XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.enc");
		      } catch (Exception e) {
		    	  logger.severe("Не удалось зашифровать файл "+e);
			      throw new KassaException("Не удалось зашифровать файл "+e);
		      }  
		      
		      if (enc==null){
		    	  logger.severe("Пустой файл при шифровании ");
			      throw new KassaException("Пустой файл при шифровании ");
		      }
		      //отправляем
		      // TODO выяснить как строить форму
		 /*     String st=buildForm(uploading.getName(),enc);
		      
		      //инициализируем ssl context, всегда только с рабочим сертификатом
		      SSLContext sslCon = null;
		      try {
			      sslCon= servBean.borrowSSLContext(SSL_SETTINGS_ENCRYPT_WORK);
		      } catch (KassaException ex){
			      logger.severe("Не удалось создать ssl context"+ex);
			      throw new KassaException("Не удалось создать ssl context"+ex);
		      }
		    
		      //послали запрос, получили синхронный ответ
 		      byte[] respmessage=null;
		      try {
			      try {
			    	   Map<String,String> rparams=new HashMap<String,String>();
				       rparams.put("Content-Type", "multipart/form-data");
	 		           respmessage = HTTPUtils.sendHttp("POST",nbki.getUrlUploadWork(), st.getBytes(),rparams,sslCon);
			      }  finally {
			    	   servBean.returnSSLContext(SSL_SETTINGS_ENCRYPT_WORK, sslCon);
		    	       sslCon=null; 
			      }
		      } catch (Exception ex){
		    	  logger.severe("Не удалось отправить либо получить данные, ошибка "+ex);
		   	      throw new KassaException("Не удалось отправить либо получить данные, ошибка "+ex);
		      }
		      
		      //удаляем файл подписанный
		      FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".sig");
		      //удаляем файл зашифрованный
		      FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.enc");			 
		      //удаляем архив
		      FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip");
		      
		      //если есть результативный ответ
		      if (respmessage!=null) {
		    	  String answerr=new String(respmessage);
				  if (StringUtils.isNotEmpty(answerr)) {
				     logger.info(answerr);	 
				  }
				  eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_NBKI,"Успешно завершилась загрузка в КБ НБКИ",
						  null,new Date(),null,null,null);
				  //сохраняем статус успешно выгруженных
				  uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_UPLOADED);
				  //пишем в базу статус загрузки
				  uploading.setDateUploading(sendingDate);
				  uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
				  uploading=uploadService.saveUpload(uploading);
				  logger.info("Успешно завершили выгрузку в КБ НБКИ");
		      } else {
		    	  uploading.setStatus(UploadStatus.UPLOAD_ERROR);
				  uploading=uploadService.saveUpload(uploading);
				  logger.severe("Не удалось загрузить данные в КБ НБКИ");
				  eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_NBKI,"Не удалось загрузить данные в КБ НБКИ",
						  null,new Date(),null,null,null);

		      }*/
		    		      
		  } catch (Exception e){
			 logger.severe("Не удалось выгрузить файл, ошибка "+e);	 
			 eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_NBKI,"Не удалось загрузить данные в КБ НБКИ",
					 null,new Date(),null,null,null);
		  }//end try
		  
		}//end если uploading не null
	}

	@Override
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
      

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork) throws KassaException {
		PartnersEntity nbki=refBooks.getPartnerEntity(Partner.NBKI);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.NBKI,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		log.info("Всего записей для выгрузки "+lstcre.size());
        if (lstcre.size() > 0) {
        	
        	//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.NBKI, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
        			null,null, null, null, null, null, null, null, null, true);
        	
        	StringBuilder sb=MakeXML.createUploadHeaderForSendingToNbki(uploadDateRange.getTo(), 
            		isWork?nbki.getGroupWork():nbki.getGroupTest(), isWork?nbki.getPasswordUploadWork():nbki.getPasswordUploadTest(), 
            		nbki.getUploadVersion(), nbki.getDateVersion());
            int i = 1;
            for (CreditRequestEntity cre : lstcre) {
                
                 sb=MakeXML.createUploadRecordForSendingToNbki(isWork?nbki.getGroupWork():nbki.getGroupTest(), 
                		 uploadDateRange.getFrom(),uploadDateRange.getTo(), listDataForUpload(cre,uploadDateRange,i), sb);
                 //пишем запись в таблицу detail
                 uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                 i++;
            }
        	
            String st=MakeXML.createUploadFooterForSendingToNbki(sb);
            
            String fname=generateName(uploadDateRange.getTo());
     	    log.info("Записали файл для выгрузки в НБКИ");
     	    log.info("Название файла выгрузки "+fname);
     	    //сохранили данные в таблицу выгрузки
			upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
        			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
     		  
     	    return upl;
        } else {		
 		    return null;
        }
	}

	private byte[] makeNBKIRequest(CreditRequestEntity cre, RequestsEntity req, String login, String password, String membercode)
			throws KassaException {
		CreditReportRequest request = new CreditReportRequest();

		// Login data
		Requestor requestor = new Requestor();
		requestor.setMemberCode(membercode);
		requestor.setUserID(login);
		requestor.setPassword(password);
		request.setRequestorReq(requestor);

		//request
		Inquiry inquery = new Inquiry();
		
		/*ConsentReq consent=new ConsentReq();
		consent.setConsentFlag("Y");
		consent.setConsentDate(Convertor.dateToGreg(cre.getDatecontest(), false));
		consent.setConsentExpireDate(Convertor.dateToGreg(DateUtils.addYears(cre.getDatecontest(),1), false));
		consent.setConsentPurpose("1");
		consent.setReportUser(membercode);
		consent.setLiability("Y");
		inquery.setConsentReq(consent);*/
		
		inquery.setCurrencyCode(BaseCredit.CURRENCY_RUB);
		
		inquery.setInqAmount(BigInteger.valueOf(cre.getCreditsum().longValue()));
		inquery.setInqPurpose(String.valueOf(BaseCredit.MICRO_CREDIT_NBKI));
		request.setInquiryReq(inquery);
		
		// Personal data
		Person person = new Person();
		fillPersonalData(cre, person);
		request.setPersonReq(person);

		// Document
		Id id = new Id();
		fillDocumentData(cre, id);
		request.getIdReq().add(id);

		// Address
		fillAddressData(cre, request.getAddressReq());

		// System data
		Reference ref = new Reference();
		ref.setProduct("CHST");
		request.setRefReq(ref);
		request.setIOType("B2B");
		request.setOutputFormat("XML");
		request.setLang("ru");

		ProductRequest productReq = new ProductRequest();
		productReq.setReq(request);

		ProductType product = new ProductType();
		product.setPrequest(productReq);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(ProductType.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, XmlUtils.ENCODING_WINDOWS);
			marshaller.marshal(product, os);
		} catch (JAXBException e) {
			final String s = "Не удалось сформировать запрос: " + e;
			log.severe(s);
			throw new KassaException(s);
		}

		return os.toByteArray();
	}

	private void fillAddressData(CreditRequestEntity cre, List<Address> addressReq) {
		PeopleMainEntity pplmain = peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());

		// адрес регистрации
		AddressEntity addrreg = peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
		// адрес проживания
		AddressEntity addrres = null;
		
		//если есть адрес проживания, отличный от адреса регистрации
		if( addrreg.getIsSame()!=null&&addrreg.getIsSame() != BaseAddress.IS_SAME ) {
			addrres =peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
		} else {
			addrres = addrreg;
		}
		
		Address a1 = new Address();
		fillAddressData(a1, addrreg, String.valueOf(BaseAddress.REGISTER_ADDRESS_RS));
		addressReq.add(a1);

		Address a2 = new Address();
		fillAddressData(a2, addrres, String.valueOf(BaseAddress.RESIDENT_ADDRESS_RS));
		addressReq.add(a2);
	}

	private void fillAddressData(final Address address, final AddressEntity aentity, final String type) {
		address.setAddressType(type);

		address.setPostal(StringUtils.isNotEmpty(aentity.getIndex())?aentity.getIndex():"000000");
		//город
		
		if (StringUtils.isNotEmpty(aentity.getStreetName())){
		    address.setStreet(aentity.getStreetName());
		} else {
			address.setStreet("неизвестно");
		}
		if (StringUtils.isNotEmpty(aentity.getStreetExt())){
		    address.setStreetType(aentity.getStreetExt());
		}
		address.setHouseNumber(aentity.getHouse());
		if (StringUtils.isNotEmpty(aentity.getFlat())){
		    address.setApartment(aentity.getFlat());
		}
		if (StringUtils.isNotEmpty(aentity.getCorpus())){
		    address.setBlock(aentity.getCorpus());
		}
		if (StringUtils.isNotEmpty(aentity.getBuilding())){
		    address.setBuilding(aentity.getBuilding());
		}
		//сельский район
		if (StringUtils.isNotEmpty(aentity.getArea())){
		    address.setDistrict(aentity.getAreaName());
		}
		//село
		if (StringUtils.isNotEmpty(aentity.getPlace())){
		    address.setSubDistrict(aentity.getPlaceName());
		}
		//город
		//Москва или Питер
		/*if (StringUtils.isEmpty(aentity.getArea())&&StringUtils.isEmpty(aentity.getCity())&&StringUtils.isEmpty(aentity.getPlace())){
			address.setCity(aentity.getRegionName());
		} else if (StringUtils.isNotEmpty(aentity.getCity())){
		    address.setCity(aentity.getCityName());
		} else if (StringUtils.isNotEmpty(aentity.getPlace())&&StringUtils.isEmpty(aentity.getCity())){
		    address.setCity(aentity.getPlaceName());
		}*/
		address.setCity(aentity.getMandatoryCity());
		address.setProv(aentity.getRegionShort().getCodereg());
	}

	private void fillDocumentData(CreditRequestEntity cre, Id id) throws KassaException {
		PeopleMainEntity pplmain = peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());
		DocumentEntity dc = peopleBean.findPassportActive(pplmain);

		id.setIdType(dc.getDocumenttypeId().getCodeinteger().toString());
		id.setIdNum(dc.getNumber());
		id.setSeriesNumber(dc.getSeries());

		// TODO выяснить все же вопрос, что сюда пишем
		 id.setIssueCountry("г. Москва");

		id.setIssueDate(Convertor.dateToGreg(dc.getDocdate(), false));
		id.setIssueAuthority(dc.getDocorg());
	}

	private void fillPersonalData(CreditRequestEntity cre, Person person) throws KassaException {
		// персональные данные
		PeopleMainEntity pplmain = peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());
		PeoplePersonalEntity identity = peopleBean.findPeoplePersonalActive(pplmain);

		// фамилия
		person.setName1(identity.getSurname());

		// имя
		person.setFirst(identity.getName());

		// отчество
		person.setPaternal(identity.getMidname());

		// пол
		person.setGender(identity.getGender().getCodeinteger().toString());

		// дата и место рождения
		person.setBirthDt(Convertor.dateToGreg(identity.getBirthdate(), false));
		person.setPlaceOfBirth(identity.getBirthplace());
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
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
	}
	@Override
	public String getSystemDescription() {
		return ScoringNBKIBeanLocal.SYSTEM_DESCRIPTION;
	}
	@Override
	public String getSystemName() {
		return ScoringNBKIBeanLocal.SYSTEM_NAME;
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
        //паспорт
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
        //кредит 
        CreditEntity cr = creditRequest.getAcceptedcreditId();
        //телефон мобильный
        PeopleContactEntity phoneMobile = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
        //телефон домашний
        PeopleContactEntity phoneHome = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_HOME_PHONE, pplmain.getId());
        //передача в суд
        DebtEntity debtCourt=null;
        //есть ли решение суда
        Boolean hasCourtDecision=false;
        //сумма платежей по кредиту
        PaymentEntity pay=null;
        Double sumPays=0d;
        //если кредит есть
        if (cr!=null){
            
            //оплата 
            pay=paymentService.getLastCreditPayment(cr.getId(), null);
            //сумма всех платежей по кредиту
            sumPays=paymentService.getCreditPaymentSum(cr.getId());
            //информация по передаче дела в суд
            List<DebtEntity> debts=creditInfo.listDebts(creditRequest.getId(), null, Partner.SYSTEM, null,Debt.DEBT_COURT,null,uploadDateRange);
            if (debts.size()>0){
            	debtCourt=debts.get(0);
            	hasCourtDecision=(debtCourt.getDateDecision()!=null);
            }
        }
        return new Object[]{i, pplmain, pplper, pplmsc, dc, addrreg, addrres, creditRequest, cr, pay,
       		phoneMobile, phoneHome,sumPays,	debtCourt,hasCourtDecision};
	}
	
	/**
	 * генерим имя для выгрузки
	 * @param dt - дата выгрузки
	 * @return
	 */
	private String generateName(Date dt){
		Partner nbki = refBooks.getPartner(Partner.NBKI);
		String st=nbki.getCodeWork()+"_"+DatesUtils.DATE_FORMAT_YYYYMMdd.format(dt)+"_"+DatesUtils.DATE_FORMAT_HHmmss.format(dt);
		return st;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		PartnersEntity nbki=refBooks.getPartnerEntity(Partner.NBKI);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.NBKI,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
		Date firstDate=DatesUtils.makeDate(2010, 1, 1);
		uploadDateRange.setFrom(firstDate);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,creditIds);
		
		log.info("Всего записей для выгрузки "+lstcre.size());
        if (lstcre.size() > 0) {
        	
        	//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.NBKI, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
        			null,null, null, null, null, null, null, null, null, true);
        	    		
            int i = 1;
            StringBuilder sb=MakeXML.createUploadHeaderForSendingToNbki(uploadDateRange.getTo(), 
            		isWork?nbki.getGroupWork():nbki.getGroupTest(), isWork?nbki.getPasswordUploadWork():nbki.getPasswordUploadTest(), 
            		nbki.getUploadVersion(), nbki.getDateVersion());
            for (CreditRequestEntity cre : lstcre) {
                 sb=MakeXML.createUploadRecordForSendingToNbki(isWork?nbki.getGroupWork():nbki.getGroupTest(), 
                		 uploadDateRange.getFrom(),uploadDateRange.getTo(), listDataForUpload(cre,uploadDateRange,i), sb);
                 //пишем запись в таблицу detail
                 uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                 i++;
            }
            String st=MakeXML.createUploadFooterForSendingToNbki(sb);
            
            String fname=generateName(uploadDateRange.getTo());
     	    log.info("Записали файл для выгрузки в НБКИ");
     	    log.info("Название файла выгрузки "+fname);
     	    //сохранили данные в таблицу выгрузки
			upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
        			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
     		  
     	    return upl;
        } else {		
 		    return null;
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private List<CreditEntity> saveNbkiCredit(Account credit,PeopleMainEntity pplmain,CreditRequestEntity ccre,
			CreditEntity clientCredit,List<CreditEntity> creditList,Integer unique,
			VerificationEntity verification){
		if (credit.getOpenedDt() == null&&credit.getCreditLimit()==null){
			return creditList;
		}
		// credit
		CreditEntity cred = new CreditEntity();
		
		// отношение к кредиту
		if (credit.getOwnerIndic() != null) {
			cred.setCreditrelationId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_RELATION_STATE, Convertor.toInteger(credit.getOwnerIndic())));
		}

		// Текущий просроченный статус
		if (StringUtils.isNotEmpty(credit.getPaymtPat())) {
			
			String statusOvrd="";
			String lastOverdue=" ";
			int delay30=0;
			//ищем первый непустой статус
			for (int ind=0;ind< credit.getPaymtPat().length();ind++){
				if (credit.getPaymtPat().charAt(ind) != NO_PAYMENTS_IN_MONTH&&StringUtils.isEmpty(statusOvrd)){
					statusOvrd=credit.getPaymtPat().substring(ind, ind+1);
					//break;
				}
			    if (credit.getPaymtPat().substring(ind, ind+1).equalsIgnoreCase(BaseCredit.WITHOUT_OVERDUE)){
			    	lastOverdue=credit.getPaymtPat().substring(ind, ind+1);
			    	continue;
			    }
			    //если это не первый символ платежной дисциплины, и просрочка закрыта
			    if (ind>0&&credit.getPaymtPat().substring(ind, ind+1).equalsIgnoreCase(BaseCredit.OVERDUE_BEFORE_MONTH)
			    		&&lastOverdue.equalsIgnoreCase(BaseCredit.WITHOUT_OVERDUE)){
			    	delay30++;
			    }
			    lastOverdue=credit.getPaymtPat().substring(ind, ind+1);
			}
			
			ReferenceEntity rfe=refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, statusOvrd);
	    	//текущий просроченный статус
			cred.setOverduestateId(rfe);
			//если нет статуса или платежей не было, поставим статус платеж в срок
			if (rfe==null||statusOvrd.equals(BaseCredit.NEW_OVERDUE_RSTANDART)){
				cred.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
			}
	    	//кол-во просрочек до 30 дней
		    cred.setDelay30(delay30);
		    
		    //худший просроченный статус
		    if (credit.getPaymtPat().contains(BaseCredit.OVERDUE_FOUR_MONTH)){
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_FOUR_MONTH));
		    } else if (credit.getPaymtPat().contains(BaseCredit.OVERDUE_THREE_MONTH)){
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
		    } else if (credit.getPaymtPat().contains(BaseCredit.OVERDUE_TWO_MONTH)){
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		    } else if (credit.getPaymtPat().contains(BaseCredit.OVERDUE_MONTH)){
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		    } else if (credit.getPaymtPat().contains(BaseCredit.OVERDUE_BEFORE_MONTH)){
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		    } else {
		    	cred.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
		    }
		}

		// дата начала
		if (credit.getOpenedDt() != null) {
			cred.setCreditdatabeg(credit.getOpenedDt().toGregorianCalendar().getTime());
		}

		// сумма кредита
		Double creditLimit = new Double(0);
		if (credit.getCreditLimit()!=null){
		    cred.setCreditsum(credit.getCreditLimit().doubleValue());
		    creditLimit = credit.getCreditLimit().doubleValue();
		}

		// номер счета
		if (StringUtils.isNotEmpty(credit.getAcctNum())){
		    cred.setCreditAccount(credit.getAcctNum());
		}

	    // тип кредита
		if (credit.getAcctType() != null) {
			try {
				ReferenceEntity rfe = refBooks.findFromKb(RefHeader.CREDIT_TYPE_NBKI, Convertor.toInteger(credit.getAcctType()));
				if (rfe!=null){
				    cred.setCredittypeId(rfe);
				} else{
					cred.setCredittypeId(refBooks.getCreditType(BaseCredit.ANOTHER_CREDIT).getEntity());
				}
			} catch (Exception e) {
				log.severe("Не удалось найти тип кредита " + credit.getAcctType());
			}
		} else {
			cred.setCredittypeId(refBooks.getCreditType(BaseCredit.ANOTHER_CREDIT).getEntity());
		}
		
		// валюта
		ReferenceEntity rfe = refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE, credit.getCurrencyCode());
		if (rfe == null) {
			rfe = refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE,BaseCredit.CURRENCY_UNKNOWN);
		} else {
			if (rfe.getCode().equalsIgnoreCase(BaseCredit.CURRENCY_RUB)){
				rfe = refBooks.findByCodeEntity(RefHeader.CURRENCY_TYPE,BaseCredit.CURRENCY_RUR);
			}
		}
		cred.setIdCurrency(rfe);
		
		// дата окончания по графику
		if (credit.getClosedDt() != null) {
			cred.setCreditdataend(credit.getClosedDt().toGregorianCalendar().getTime());
		}
		if (cred.getCreditdataend()==null&&credit.getPaymentDueDate()!=null){
			cred.setCreditdataend(credit.getPaymentDueDate().toGregorianCalendar().getTime());
		}
		
		//проверим на существование в БД такого же кредита
        if (creditList.size()>0){
        	for (CreditEntity ccredit:creditList){
        		try{
        		    if (creditService.isMatch(cred, ccredit)&&unique==1){
        		    	//если это кредит клиента, то запишем верификацию
        		    	if (clientCredit!=null&&ccredit.getPartnersId().getId()==Partner.CLIENT){
        		    		log.info("запишем системную верификацию НБКИ по заявке "+ccre.getId());
        		    		creditInfo.saveSystemVerification(verification, ccre.getId(), pplmain.getId(), 
        	            			ccredit, cred,VerificationConfig.MARK_FOR_BANK);
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
        	log.info("запишем системную верификацию НБКИ по заявке "+ccre.getId());
              	creditInfo.saveSystemVerification(verification, ccre.getId(), pplmain.getId(), 
            			clientCredit, cred,VerificationConfig.MARK_FOR_BANK);
        }
        
		// дата окончания фактическая
		if (credit.getCompletePerformDt() != null) {
			cred.setCreditdataendfact(credit.getCompletePerformDt().toGregorianCalendar().getTime());
		}
		// текущая просроченная задолженность
		if (credit.getAmtPastDue() != null) {
			cred.setCurrentOverdueDebt(credit.getAmtPastDue().doubleValue());
		}
		
		
		// сумма основного долга на дату отчета
		if (credit.getCurBalanceAmt() != null) {
			cred.setCreditsumdebt(credit.getCurBalanceAmt().doubleValue());
		}
	
		//статус кредита
		if(StringUtils.isNotEmpty(credit.getAccountRating())){
			try {
				rfe = refBooks.findFromKb(RefHeader.CREDIT_STATUS_NBKI, Convertor.toInteger(credit.getAccountRating()));
				cred.setCreditStatusId(rfe);
				if (cred.getCreditStatusId()!=null&&cred.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_CLOSED){
					cred.setIsover(true);
				} else {
					cred.setIsover(false);
				}
			} catch (Exception e) {
				log.severe("Не удалось найти статус кредита " + credit.getAccountRating());
			}
		}
		
		//дата статуса
		if(credit.getAccountRatingDate()!=null){
		    cred.setDateStatus(credit.getAccountRatingDate().toGregorianCalendar().getTime());
		}
		
		// общая сумма задолженности
		Double outstandingBalance = new Double(0);
		//сумма к возврату
		if (credit.getAmtOutstanding()!=null){
			cred.setCreditsumback(credit.getAmtOutstanding().doubleValue());
			outstandingBalance = credit.getAmtOutstanding().doubleValue();
		}
		
		// неиспользованный кредитный лимит
		if (cred.getCredittypeId()!=null&&cred.getCredittypeId().getCodeinteger()!=null&&cred.getCredittypeId().getCodeinteger()==BaseCredit.CREDIT_CARD) {
			cred.setCreditlimitunused(Utils.defaultDoubleFromNull(creditLimit) - Utils.defaultDoubleFromNull(outstandingBalance));
		}

		// частота платежей
		if (credit.getTermsFrequency() != null) {
			cred.setCreditfreqpaymentId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_PAYMENT_FREQ,
					Convertor.toInteger(credit.getTermsFrequency())));
		}

		// сумма следующего платежа
		if (credit.getTermsAmt()!=null){
		    cred.setCreditMonthlyPayment(credit.getTermsAmt().doubleValue());
		}
		
		// дата последнего обновления
		if (credit.getLastUpdatedDt() != null ) {
			cred.setDatelastupdate(credit.getLastUpdatedDt().toGregorianCalendar().getTime());
		}
		
		// платежная дисциплина
		cred.setPayDiscipline( credit.getPaymtPat() );
		// просрочек до 60 дней
		if (credit.getNumDays30()!=null){
		    cred.setDelay60(credit.getNumDays30().intValue()); 
		}
		// просрочек до 90 дней
		if (credit.getNumDays60()!=null){
		    cred.setDelay90(credit.getNumDays60().intValue()); 
		}
		// просрочек более 90 дней
		if (credit.getNumDays90()!=null){
		    cred.setDelaymore(credit.getNumDays90().intValue()); 
		}
		cred.setPeopleMainId(pplmain);
		cred.setCreditRequestId(ccre);
		cred.setPartnersId(refBooks.getPartnerEntity(Partner.NBKI));
		cred.setIssameorg(false);
		cred.setIsactive(ActiveStatus.ACTIVE);
		
		creditDAO.saveCreditEntity(cred);
		creditList.add(cred);	
		return creditList;
	}

	
	/**
	 * рисуем форму для выгрузки
	 * @param subscriber - подписчик
	 * @param group - группа
	 * @param user - пользователь
	 * @param password - пароль
	 * @param filename - название файла
	 * @param file - зашифрованный файл, массив byte
	 * @return
	 */
	private String buildForm(String filename,byte[] file) {
		
		 StringBuilder sb = new StringBuilder();
	
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"file1\"; filename=\"" + Convertor.toEscape(filename) + "\"\r\n");
         sb.append("Content-Type: application/octet-stream\r\n\r\n");
         try {
        	
			sb.append(new String(file));
			
		 } catch (Exception e) {
			log.severe("Не удалось перекодировать файл "+e);
		 }
         sb.append("\r\n");
         sb.append("---------------------------7d63d7371a0494--");
         
		return sb.toString();
	}
	
}
