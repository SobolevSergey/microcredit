package ru.simplgroupp.ejb;

import java.io.File;
import java.io.IOException;
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

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.codec.binary.Base64;
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
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
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
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.ScoringEntity;
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
import ru.simplgroupp.transfer.Employment;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.Report;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;
import ru.simplgroupp.transfer.Summary;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.transfer.Uploading;
import ru.simplgroupp.transfer.VerificationConfig;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MakeXML;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.SettingsKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Реальный класс для работы с ОКБ, сервис кредитные истории
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringOkbCaisBeanLocal.class)
public class ScoringOkbCaisBean  extends AbstractPluginBean implements ScoringOkbCaisBeanLocal {

	@Inject Logger log;
	
	@EJB 
	ReferenceBooksLocal refBooks;
	    
	@EJB
	PeopleBeanLocal peopleBean;
	
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
	MailBeanLocal mail;
	
	@EJB 
	EventLogService eventLog;
	
	@EJB
	RequestsService requestsService;
	
	@EJB
	CreditInfoService creditInfo;
	
	@EJB
	CreditService creditService;
	
	@EJB
    OrganizationService orgService;
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
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
     	return newRequest(cre,isWork,cacheDays,Requests.FUNCTION_CAIS);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer)	throws KassaException {
		 
		Partner parOkbCais = refBooks.getPartner(Partner.OKB_CAIS);
		
		Document doc=XmlUtils.getDocFromString(answer, XmlUtils.HEADER_XML_WINDOWS);
		if (doc!=null)
		{
			int isSameReqCred=0;
			int isSameReqAddr=0;
			
			//проверяем, работает ли сайт с БД телефонов
			Map<String,Object> sparams=rulesBean.getSiteConstants();
		    String site = (String) sparams.get(SettingsKeys.CONNECT_DB_URL); 
		    boolean hasConnection=false;
		    try {
			    hasConnection=HTTPUtils.isConnected(site);
			} catch (Exception e1) {
				log.severe("Не удалось соединиться с сайтом "+site);
			}  
		         
			//дата ответа
		    if (StringUtils.isNotEmpty(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "responseDate")))){	
			  req.setResponsedate(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "responseDate")),DatesUtils.FORMAT_YYYYMMddHHmmss));
		    }
		    
		    req.setPartnersId(parOkbCais.getEntity());
		    //номер ответа
			req.setResponseguid(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "streamID")));
			int i=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "errorCode")));
			//нет ошибок
			if (i==Requests.RESPONSE_CODE_CAIS_OK)
			{
				
				//заявка
				CreditRequestEntity cre=creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
				//человек
				PeopleMainEntity pplmain=cre.getPeopleMainId();
				//ищем все кредиты по этой заявке
				List<CreditEntity> creditList=creditBean.findCredits(pplmain, cre, null, null, null);
			
				//ищем кредит клиента
				List<CreditEntity> lstClientCredit=creditBean.findCredits(pplmain, cre,refBooks.getPartnerEntity(Partner.CLIENT), false, null);
				CreditEntity clientCredit=null;
				if (lstClientCredit.size()>0){
					clientCredit=lstClientCredit.get(0);
				}
			
				//параметры для разбора ответа
				Map<String,Object> params=productBean.getConsiderProductConfig(cre.getProductId().getId());
				Integer uniqueCredits=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_CREDITS));
				Integer uniqueDocuments=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_DOCUMENTS));
				String ourName=Convertor.toString(params.get(ProductKeys.NAME_FOR_COMPARISON));
				  
				//Score record
				Node ndscore=XmlUtils.getNodeFromAttributeValue(doc, "c", "n", "BureauScore");
				List<Node> lsts=XmlUtils.findNodeList(ndscore, "s");
				if (lsts.size()>0){
					//ищем, были ли записаны скоринги по заявке
					List<ScoringEntity> scorings=creditInfo.findScorings(cre.getId(), pplmain.getId(), Partner.OKB_CAIS, null);
					if (scorings.size()==0){
					  for (Node nds:lsts){
						List<Node> lns=XmlUtils.findNodeList(nds, "a");
						
						creditInfo.saveScoring(req.getCreditRequestId().getId(), pplmain.getId(), 
								Partner.OKB_CAIS, null,getValueFromNode(lns,"scoreCardType"), 
								Convertor.toDouble(getValueFromNode(lns,"scoreNumber")), 
								null,Convertor.toDouble(getValueFromNode(lns,"scoreInterval")));
											
					  }//end for
					}//end не писали скоринг
				}//end если есть данные по скорингу
				
				//all CAIS records
				Node ndcais=XmlUtils.getNodeFromAttributeValue(doc, "c", "n", "CAIS");
				List<Node> lstrec=XmlUtils.findNodeList(ndcais, "s");
				//есть записи cais
				if (lstrec.size()>0)
				{
					// документы
					List<DocumentEntity> lstDoc=peopleBean.findDocuments(pplmain, null, uniqueDocuments==1?null:Partner.OKB_CAIS, null, Documents.PASSPORT_RF);
					for (Node ndcs:lstrec)
					{
				      //person
				      Node ndc=XmlUtils.getNodeFromAttributeValue(ndcs, "c", "n", "Consumer");
				      //data in attributes - credit and so on
				      List<Node> lndcr=XmlUtils.findNodeList(ndcs, "a");
				      //персональные данные
				      List<Node> lstpers=XmlUtils.findNodeList(ndc, "s");
				      if (lstpers.size()>0)
				      {
					      for (Node nd:lstpers)
					      {
						       String sto="";
						      //all attributes
						      List<Node> lndp=XmlUtils.findNodeList(nd, "a");
						    
						      if (getValueFromNode(lndp,"primaryIDType").equals(Documents.PASSPORT_RF_OKB))
						      {
						    	 //ищем, писали ли документ по этой заявке
						    	DocumentEntity dcpar=peopleBean.findDocument(pplmain, cre, Partner.OKB_CAIS, ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
						    	 //если не писали
						        if (dcpar==null) {
						           //если есть номер документа	
						    	   if (StringUtils.isNotEmpty(getValueFromNode(lndp,"primaryID"))) {
						    		    int docs=0; 
						    		    //данные документа из ответа
						    		    String spasp=getValueFromNode(lndp,"primaryID").substring(0, 4);
						    	        String npasp=getValueFromNode(lndp,"primaryID").substring(4, 10);
						    		    //проверяем документы на дубликаты
					                    for (int l=0;l<lstDoc.size();l++){  
						    	          
						    	          DocumentEntity clientDoc=lstDoc.get(l);
						    	          boolean b=false;
						    	          if (clientDoc!=null){
						    		         b=spasp.equalsIgnoreCase(clientDoc.getSeries())&&npasp.equalsIgnoreCase(clientDoc.getNumber()); 
						    	          }
						    	          //если документ не соответствует нашему из БД, запишем его
						    	          if (!b) {
						    	    	     docs++;						              
						    	          }//end not b
					                    }//end for проверяем на дубликаты  
					                    if (docs==lstDoc.size()){
					                      try {
											   DocumentEntity newPasp= peopleBean.newDocument(null,pplmain.getId(), cre.getId(), Partner.OKB_CAIS,spasp,npasp, 
													Convertor.toDate(getValueFromNode(lndp,"primaryIDIssueDate"), DatesUtils.FORMAT_YYYYMMddHHmmss), 
													null, getValueFromNode(lndp,"primaryIDAuthority"), ActiveStatus.ACTIVE);
											   lstDoc.add(newPasp);
											} catch (Exception e){
												log.severe("Не удалось записать активный документ по заявке "+cre.getId()+", ошибка "+e);
											} 
					                    }//end пишем документ в БД
						    	 }//end not empty ndoc
						      }//end don't have this doc in db
						    }//end if pasport
					      
						        sto=getValueFromNode(lndp,"accountHolderType");
						        PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
						        //ищем, писали ли информацию по человеку
						        List<PeoplePersonalEntity> ppl=peopleBean.findPeoplePersonal(pplmain, cre, Partner.OKB_CAIS, ActiveStatus.ACTIVE);
						        //не писали
						        if (ppl.size()==0) {
						          //если ФИО из ответа не соответствует нашему из БД, запишем его	
						          if (!pplper.getSurname().equalsIgnoreCase(getValueFromNode(lndp,"surname"))
						        		  ||!pplper.getName().equalsIgnoreCase(getValueFromNode(lndp,"name1"))
						        		  ||!pplper.getMidname().equalsIgnoreCase(getValueFromNode(lndp,"name2"))) {
						            
						        	  try {
											peopleBean.newPeoplePersonal(null,pplmain.getId(), cre.getId(), Partner.OKB_CAIS, 
													getValueFromNode(lndp,"surname"), getValueFromNode(lndp,"name1"), 
													getValueFromNode(lndp,"name2"), getValueFromNode(lndp,"aliasName"),
													Convertor.toDate(getValueFromNode(lndp,"dateOfBirth"), DatesUtils.FORMAT_YYYYMMddHHmmss), 
													getValueFromNode(lndp,"placeOfBirth"), Convertor.toInteger(getValueFromNode(lndp,"sex")),
													new Date(), ActiveStatus.ACTIVE);
										} catch (Exception e) {
											log.severe("Не удалось сохранить актуальные ПД по заявке "+cre.getId());
										}
							    	
						          }//end если не соответствует
						        }//end если не писали
						      
						        
						        //заишем снилс 
								try {
									peopleBean.savePeopleMain(pplmain,"", StringUtils.isNotEmpty(getValueFromNode(lndp,"pensionNumber"))?getValueFromNode(lndp,"pensionNumber"):"");
								} catch (Exception e) {
									log.severe("Не удалось записать снилс по человеку "+pplmain.getId()+", ошибка "+e);
								}
								
						        //если есть номер мобильника и он не пустой
						        if (StringUtils.isNotEmpty(getValueFromNode(lndp,"mobileTelNbr"))){
						        	// TODO если этот телефон был дан клиентом, записывать соответствие с ним в БД
						        	if (!peopleBean.phoneExists(pplmain.getId(), getValueFromNode(lndp,"mobileTelNbr"))){
						        	    try{
						        		    peopleBean.setPeopleContact(pplmain, refBooks.getPartnerEntity(Partner.OKB_CAIS), PeopleContact.CONTACT_CELL_PHONE, 
						        				getValueFromNode(lndp,"mobileTelNbr"), false, cre.getId(), new Date(),hasConnection);
						        	    } catch (Exception e) {
										    log.severe("Не удалось сохранить актуальный телефон по заявке "+cre.getId());
									    }
						        	}
						        }//end если есть номер мобильника
						       
						      //информация из судов
							  Node ndcourt=XmlUtils.getNodeFromAttributeValue(nd, "c", "n", "Legal");
							  List<Node> lstcourt=XmlUtils.findNodeList(ndcourt, "s");	  
							  //записи из судов есть
							  if (lstcourt.size()>0){
								  for (Node ndct:lstcourt) {
									//all attributes
									List<Node> lnd=XmlUtils.findNodeList(ndct, "a"); 
									//проверяем, был ли записан долг ранее
				            		DebtEntity oldDebt=debtDAO.findDebt(pplmain.getId(), Partner.OKB_CAIS, Convertor.toDouble(getValueFromNode(lnd,"amountOfRecovery"), CalcUtils.dformat), 
				            				 Debt.DEBT_COURT,Convertor.toDate(getValueFromNode(lnd,"dateOfJudgment"), DatesUtils.FORMAT_YYYYMMdd));
									if (oldDebt==null){
									    creditInfo.newDebt(cre.getId(), pplmain.getId(), Partner.OKB_CAIS, null,
								    		Convertor.toDouble(getValueFromNode(lnd,"amountOfRecovery"), CalcUtils.dformat), 
											null,null, getValueFromNode(lnd,"courtName"),null, 
											getValueFromNode(lnd,"courtJudgmentNumber"), null, null,null,Debt.DEBT_COURT,
											Convertor.toDate(getValueFromNode(lnd,"dateOfJudgment"), DatesUtils.FORMAT_YYYYMMdd),ActiveStatus.ACTIVE);
									}
								  }//end for
							  }//end если есть записи из судов
							  
						      //address
							  Node ndadr=XmlUtils.getNodeFromAttributeValue(nd, "c", "n", "Address");
							  List<Node> lstadr=XmlUtils.findNodeList(ndadr, "s");
							  //адреса есть
							  if (lstadr.size()>0 ) {
								  //писали ли адреса
								  List<AddressEntity> addrpar=peopleBean.findAddresses(pplmain, Partner.OKB_CAIS, cre, null, null);
								  //не писали
								  if (addrpar.size()==0||isSameReqAddr==1) {	 
									isSameReqAddr=1;
								    for (Node nda:lstadr) {
										
										//all attributes
										List<Node> lnd=XmlUtils.findNodeList(nda, "a");
										
										Integer addrTypeId=null;
										//вид адреса
										if (StringUtils.isNotEmpty(getValueFromNode(lnd,"type"))) {
											try {
											    ReferenceEntity rfe=refBooks.findFromKb(RefHeader.ADDRESS_TYPE_OKB, Convertor.toInteger(getValueFromNode(lnd,"type")));
											    if (rfe!=null){
											    	addrTypeId=rfe.getCodeinteger();
											    }
											} catch (Exception e) {
												log.severe("Не удалось найти вид адреса "+getValueFromNode(lnd,"type"));
											}
										}
										//если он null, то все равно что-нибудь поставим
										if (addrTypeId==null){
											addrTypeId=FiasAddress.REGISTER_ADDRESS;
										}
										
										try{
										    AddressEntity address=peopleBean.newAddressFias(null,pplmain.getId(), cre.getId(), Partner.OKB_CAIS, 
													addrTypeId,null, 
													StringUtils.isNotEmpty(getValueFromNode(lnd,"startDate"))?Convertor.toDate(getValueFromNode(lnd,"startDate"), DatesUtils.FORMAT_YYYYMMddHHmmss):null, 
													StringUtils.isNotEmpty(getValueFromNode(lnd,"endDate"))?Convertor.toDate(getValueFromNode(lnd,"endDate"), DatesUtils.FORMAT_YYYYMMddHHmmss):null,  
													BaseAddress.COUNTRY_RUSSIA,
													StringUtils.isNotEmpty(getValueFromNode(lnd,"addressCurrPrev"))&&(Convertor.toInteger(getValueFromNode(lnd,"addressCurrPrev"))==ActiveStatus.ACTIVE)?ActiveStatus.ARCHIVE:ActiveStatus.ACTIVE,
													null,getValueFromNode(lnd,"houseNbr"),getValueFromNode(lnd,"line1"),
													getValueFromNode(lnd,"buildingNbr"),getValueFromNode(lnd,"flatNbr"));
											  
										    peopleBean.saveAddressDataWithStrings(address, getValueFromNode(lnd,"regionCode"),null,
												      StringUtils.isNotEmpty(getValueFromNode(lnd,"line4"))?getValueFromNode(lnd,"line4"):null, null, 
												      StringUtils.isEmpty(getValueFromNode(lnd,"line4"))?getValueFromNode(lnd,"line3"):null, null, 
													  StringUtils.isNotEmpty(getValueFromNode(lnd,"line4"))?getValueFromNode(lnd,"line3"):null, null, 
													  StringUtils.isNotEmpty(getValueFromNode(lnd,"line2"))?getValueFromNode(lnd,"line2"):null,null);
										}  catch (Exception e) {
											  log.severe("Не удалось записать адрес по заявке "+cre.getId()+",ошибка "+e);
									    }
										  		
																			
										//если есть номер домашнего телефона и он не пустой
								        if (StringUtils.isNotEmpty(getValueFromNode(lnd,"homeTelNbr"))){
								        	// TODO если этот телефон был дан клиентом, записывать соответствие с ним в БД
								        	if (!peopleBean.phoneExists(pplmain.getId(), getValueFromNode(lndp,"homeTelNbr"))){
								        	    try{
								        		    peopleBean.setPeopleContact(pplmain, refBooks.getPartnerEntity(Partner.OKB_CAIS), PeopleContact.CONTACT_HOME_PHONE, 
								        				getValueFromNode(lnd,"homeTelNbr"), false, cre.getId(), new Date(),hasConnection);
								        	    } catch (Exception e) {
												    log.severe("Не удалось сохранить актуальный телефон по заявке "+cre.getId());
											    }
								        	}
								        }//end если есть домашний телефон
								        
				                    }//end for
								  }//end если не писали адреса
								}//end если есть адреса
								
							  //писали ли кредиты
							  List<CreditEntity> credpar=creditBean.findCredits(pplmain, cre, parOkbCais.getEntity(), false, null);
							  if (credpar.size()==0&&isSameReqCred==0) {
								  //делаем старые кредиты неактивными
								  creditDAO.makePartnerCreditsNotActive(Partner.OKB_CAIS,pplmain.getId()); 
							  }
							  //не писали
							  if (credpar.size()==0||isSameReqCred==1) {
								  isSameReqCred=1;
								  //наши кредиты
								  String oname=getValueFromNode(lndcr,"subscriberName");
								  if (StringUtils.isEmpty(oname)){
									  oname="ПУСТО";
								  }
								  
								  //если это наш запрос, то здесь будет наше имя						  
								  if (!oname.toUpperCase().contains(ourName)){
									   //ищем системную верификацию, если есть кредит клиента
										VerificationEntity verification=null;
										if (clientCredit!=null){
										    verification=creditInfo.findOneVerification(cre.getId(), pplmain.getId(), Partner.SYSTEM, null);
										}
										creditList=saveOkbCredit(lndcr,ndcs,sto,oname,cre, pplmain,
												clientCredit,creditList,uniqueCredits,verification);
								  }//end не наш запрос
							  }//end не писали кредиты
							
				         }//end for
					      
				    }//end есть ПД
				
				
					}//end for
					 req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
					 req.setResponsemessage("Данные найдены");
				}//end есть записи cais
				//не нашлось записей
				else {
					 req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
					 req.setResponsemessage("Данные по кредитным историям не найдены");
				}

				//summary
				Node nds=XmlUtils.getNodeFromAttributeValue(doc, "s", "n", "Summary");
				if (nds!=null) {
					//ищем, писали ли summary
					List<SummaryEntity> lstpar=summaryDAO.findSummary(pplmain, cre, parOkbCais.getEntity(), null);
					if (lstpar==null) {
					  List<Node> lnds=XmlUtils.findNodeList(nds, "a");
					  //клиент - основной заемщик, кол-во записей
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISRecordsOwner"))) {
						creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISRecordsOwner"), refBooks.getRefSummaryItem(Summary.CLIENT_OWNER).getEntity(),null);
					  }
					  //клиент - созаемщик, кол-во записей
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISRecordsJoint"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISRecordsJoint"), refBooks.getRefSummaryItem(Summary.CLIENT_COOWNER).getEntity(),null);
					  }
					  //текущий худший платежный статус
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstCurrentPayStatusOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstCurrentPayStatusOwner"), refBooks.getRefSummaryItem(Summary.CURRENT_DELAY_STATUS).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
					  }
					  //исторический худший платежный статус
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstEverPayStatusOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstEverPayStatusOwner"), refBooks.getRefSummaryItem(Summary.HISTORY_DELAY_STATUS).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
					  }
					  //кол-во доступных записей запросов
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAPSRecordsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAPSRecordsOwner"), refBooks.getRefSummaryItem(Summary.REQUESTS).getEntity(),null);
					  }
					  //кол-во записей запросов
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAPSRecordsOwnerBeforeFilter"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAPSRecordsOwnerBeforeFilter"), refBooks.getRefSummaryItem(Summary.REQUESTS_ALL).getEntity(),null);
					  }
					  //кол-во записей запросов, сделанных человеком 
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"ConsumerEnquiryCountLast12Month"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"ConsumerEnquiryCountLast12Month"), refBooks.getRefSummaryItem(Summary.REQUESTS_PERSON).getEntity(),null);
					  }
					  //кол-во доступных записей запросов за последние 3 месяца
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAPSLast3MonthsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAPSLast3MonthsOwner"), refBooks.getRefSummaryItem(Summary.REQUESTS_3MONTH).getEntity(),null);
					  }
					  //кол-во доступных записей запросов за последние 6 месяца
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAPSLast6MonthsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAPSLast6MonthsOwner"), refBooks.getRefSummaryItem(Summary.REQUESTS_6MONTH).getEntity(),null);
					  }
					  //кол-во доступных записей запросов за последние 12 месяца
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAPSLast12MonthsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAPSLast12MonthsOwner"), refBooks.getRefSummaryItem(Summary.REQUESTS_12MONTH).getEntity(),null);
					  }
					  //текущий худший платежный статус для созаемщика
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstCurrentPayStatusJoint"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstCurrentPayStatusJoint"), refBooks.getRefSummaryItem(Summary.CURRENT_DELAY_STATUS_COOWNER).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
					  }
					  //исторический худший платежный статус для созаемщика
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstEverPayStatusJoint"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstEverPayStatusJoint"), refBooks.getRefSummaryItem(Summary.HISTORY_DELAY_STATUS_COOWNER).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
					  }
					  //клиент - гарант, кол-во записей
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISRecordsGuarantor"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISRecordsGuarantor"), refBooks.getRefSummaryItem(Summary.CLIENT_GARANT).getEntity(),null);
					  }
					  //текущий худший платежный статус для гаранта
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstCurrentPayStatusGuarantor"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstCurrentPayStatusGuarantor"), refBooks.getRefSummaryItem(Summary.CURRENT_DELAY_STATUS_GARANT).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
			          }
					  //исторический худший платежный статус для гаранта
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"WorstEverPayStatusGuarantor"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"WorstEverPayStatusGuarantor"), refBooks.getRefSummaryItem(Summary.HISTORY_DELAY_STATUS_GARANT).getEntity(),RefHeader.CREDIT_OVERDUE_STATE_OKB);
				      }
					  //Ежемесячный платеж субъекта сумма по всем обязательствам, в которых субъект является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"TotalMonthlyInstalmentsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"TotalMonthlyInstalmentsOwner"), refBooks.getRefSummaryItem(Summary.TOTAL_MONTHLY_OWNER).getEntity(),null);
					  }
					  //Текущий непогашенный остаток  - непогашенная сумма по всем обязательствам, в которых субъект является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"TotalOutstandingBalanceOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"TotalOutstandingBalanceOwner"), refBooks.getRefSummaryItem(Summary.TOTAL_BALANCE_OWNER).getEntity(),null);
					  }
					  //Потенциальный платеж субъекта сумма по всем обязательствам, в которых субъект является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"PotentialMonthlyInstalmentsOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"PotentialMonthlyInstalmentsOwner"), refBooks.getRefSummaryItem(Summary.POTENTIAL_MONTHLY_OWNER).getEntity(),null);
					  }
					  //Потенциальный непогашенный остаток  - непогашенная сумма по всем обязательствам, в которых субъект является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"PotentialOutstandingBalanceOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"PotentialOutstandingBalanceOwner"), refBooks.getRefSummaryItem(Summary.POTENTIAL_BALANCE_OWNER).getEntity(),null);
				      }
					  //Ежемесячный платеж субъекта сумма по всем обязательствам, в которых субъект не является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"TotalMonthlyInstalmentsAllButOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"TotalMonthlyInstalmentsAllButOwner"), refBooks.getRefSummaryItem(Summary.TOTAL_MONTHLY_NOT_OWNER).getEntity(),null);
					  }
					  //Текущий непогашенный остаток  - непогашенная сумма по всем обязательствам, в которых субъект не является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"TotalOutstandingBalanceAllButOwner"))){
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"TotalOutstandingBalanceAllButOwner"), refBooks.getRefSummaryItem(Summary.TOTAL_BALANCE_NOT_OWNER).getEntity(),null);
					  }
					  //Потенциальный платеж субъекта сумма по всем обязательствам, в которых субъект не является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"PotentialMonthlyInstalmentsAllButOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"PotentialMonthlyInstalmentsAllButOwner"), refBooks.getRefSummaryItem(Summary.POTENTIAL_MONTHLY_NOT_OWNER).getEntity(),null);
					  }
					  //Потенциальный непогашенный остаток  - непогашенная сумма по всем обязательствам, в которых субъект не является основным заемщиком
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"PotentialOutstandingBalanceAllButOwner"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"PotentialOutstandingBalanceAllButOwner"), refBooks.getRefSummaryItem(Summary.POTENTIAL_BALANCE_NOT_OWNER).getEntity(),null);
					  }
					  //Количество подписчиков передавших 1 запись CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution1"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution1"), refBooks.getRefSummaryItem(Summary.RECORD1).getEntity(),null);
					  }
					  //Количество подписчиков передавших 2 записи CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution2"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution2"), refBooks.getRefSummaryItem(Summary.RECORD2).getEntity(),null);
					  }
					  //Количество подписчиков передавших 3 запись CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution3"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution3"), refBooks.getRefSummaryItem(Summary.RECORD3).getEntity(),null);
				      }
					  //Количество подписчиков передавших 4 запись CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution4"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution4"), refBooks.getRefSummaryItem(Summary.RECORD4).getEntity(),null);
					  }
					  //Количество подписчиков передавших 5 запись CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution5"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution5"), refBooks.getRefSummaryItem(Summary.RECORD5).getEntity(),null);
					  }
					  //Количество подписчиков передавших более 5 записей CAIS (фактически имеющихся кредитов)
					  if (StringUtils.isNotEmpty(getValueFromNode(lnds,"CAISDistribution5Plus"))) {
						  creditInfo.saveSummary(cre, pplmain, parOkbCais.getEntity(),getValueFromNode(lnds,"CAISDistribution5Plus"), refBooks.getRefSummaryItem(Summary.RECORD5MORE).getEntity(),null);
					  }
					}//end не писали summary
				}//end есть summary
			
			  
			}//end response code ok
			//есть ошибки
			else {
				Node nderr=XmlUtils.getNodeFromAttributeValue(doc, "c", "n", "ValidationErrors");
				List<Node> lsterr=XmlUtils.findNodeList(nderr, "s");
				if (lsterr.size()!=0){
					req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
					req.setResponsemessage("");
				}
			}
		
		}
		
		return req;
	}

	
	@Override
	public String getSystemName() {
		return ScoringOkbCaisBeanLocal.SYSTEM_NAME;
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
	
	/**
	 * возвращает значение из ноды, работает только для xml ОКБ
	 * @param lnd - список нод
	 * @param attrName - название атрибута
	 * @return
	 */
	private String getValueFromNode(List<Node> lnd,String attrName)	{
		String st=XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(lnd, "a", "n", attrName));
		if (StringUtils.isEmpty(st)){
			return "";
		}
		return st;
	}
	
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
	}

	@Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
      
		Integer creditRequestId = Convertor.toInteger(businessObjectId);
		
		log.info("ОКБ, кредитные истории. Заявка " + creditRequestId + " ушла в скоринг.");
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
			OkbCaisPluginConfig cfg=(OkbCaisPluginConfig) context.getPluginConfig();
			if (context.getNumRepeats() <=cfg.getNumberRepeats()) {
			    newRequest(ccRequest.getEntity(),cfg.isUseWork(),
			    		cfg.getCacheDays(),cfg.getNumberFunction());
			}
			else {
				log.severe("ОКБ, кредитные истории. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 3 попытки",Type.TECH, ResultType.FATAL,null);
			}
		} catch (ActionException e) {
			log.severe("ОКБ, кредитные истории. Заявка " + creditRequestId + " не обработана.");
			throw new ActionException("Произошла ошибка ",e);
		} catch (Throwable e) {
			log.severe("ОКБ, кредитные истории. Заявка " + creditRequestId + " не обработана. Ошибка "+e+", "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		}
		log.info("ОКБ, кредитные истории. Заявка " + creditRequestId + " обработана.");
		
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
	
	/**
      * инициализируем ssl-context	
	  * @param isWork - рабочий сервис или тестовый
	  * @return
	  * @throws KassaException
	  */
	private SSLContext initSSLContext(Boolean isWork) throws KassaException{
		
		String[] settings = null;
		if (isWork) {
			settings = new String[] {CryptoSettings.OKB_CLIENT_WORK, CryptoSettings.OKB_SERVER_WORK};
		} else {
			settings = new String[] {CryptoSettings.OKB_CLIENT_TEST, CryptoSettings.OKB_SERVER_TEST};
		}
		try {
			return servBean.borrowSSLContext(settings);
		} catch (Exception e) {
			log.severe("Не удалось инициализировать ssl context: "+e.getMessage());
			throw new KassaException("Не удалось инициализировать ssl context: "+e); 
		}
			  
	}
	
	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		 Integer crequestId = Convertor.toInteger(businessObjectId);
		 if (crequestId!=null){
			try {
				CreditRequest ccRequest = creditRequestDAO.getCreditRequest(crequestId, Utils.setOf());
				
			} catch (Exception e) {
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.NON_FATAL,e);
			}
		 }
		 
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		log.info("Метод sendSingleRequest не поддерживается");
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadHistory(UploadingEntity uploading,Date sendingDate,Boolean isWork) throws KassaException {
		Partner parOkbCais=refBooks.getPartner(Partner.OKB_CAIS);
		
		if (uploading!=null){
		  byte[] bs=null;
		  try {
		  
			  if (sendingDate==null){
				  sendingDate=new Date();
			  }
			  
		 	  File file=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".cds");
			  FileUtils.writeByteArrayToFile(file, uploading.getInfo().getBytes(XmlUtils.ENCODING_WINDOWS));
		     
		      //зипуем
		      bs=ZipUtils.ZipSingleFile(file);
		      //подписываем
		      byte[] sign=null;
		      try {
			      sign=crypto.createJCPCMS(bs, CryptoSettings.OKB_CLIENT_WORK,false);
		      } catch (Exception e) {
		    	  log.severe("Не удалось подписать файл "+e);
			      throw new KassaException("Не удалось подписать файл "+e);
		      }
	          //шифруем
		      byte[] enc=null;
		      try {
			     enc=crypto.encryptPKCS7(sign, CryptoSettings.OKB_SERVER_ENCRYPT_WORK, null);
		          
		      } catch (Exception e) {
		    	  log.severe("Не удалось зашифровать файл "+e);
			      throw new KassaException("Не удалось зашифровать файл "+e);
		      }  
		    /*  File fileNew=new File(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".zip.pem.pem");
		      StringBuilder sb=new StringBuilder();
		      sb.append("----- BEGIN PKCS7 ENCRYPTED -----"); 
			  sb.append(new String(Base64.encodeBase64(enc)));
			  sb.append("----- END PKCS7 ENCRYPTED -----");
		      FileUtils.writeStringToFile(fileNew, sb.toString());*/
		      if (enc==null){
		    	  log.severe("Пустой файл при шифровании ");
			      throw new KassaException("Пустой файл при шифровании ");
		      }
		      //отправляем
		      			  
		      //инициализируем ssl context, всегда только с рабочим сертификатом
		      SSLContext sslCon = null;
		      try {
			      sslCon=initSSLContext(true);
		      } catch (KassaException ex){
			      log.severe("Не удалось создать ssl context"+ex);
			      throw new KassaException("Не удалось создать ssl context"+ex);
		      }
		  
		      //строим форму
		      String st=buildForm(parOkbCais.getCodeWork(),parOkbCais.getGroupWork(),parOkbCais.getLoginWork(),parOkbCais.getPasswordWork(),uploading.getName()+".zip.pem.pem",enc);
		  		      
		      //параметры для httpPost
		      Map<String,String> rparams=new HashMap<String,String>();
		      rparams.put("Content-Type", "multipart/form-data; boundary=---------------------------7d63d7371a0494");
		      rparams.put("Content-Length", String.valueOf(st.length()));
		    		      
		      //послали запрос, получили синхронный ответ
 		      byte[] respmessage=null;
		      try {
			      try {
	 		           respmessage = HTTPUtils.sendHttp("POST",parOkbCais.getUrlUploadWork(), st.getBytes(),rparams,sslCon);
			      }  finally {
				       returnSSLContext(true, sslCon);
		    	       sslCon=null; 
			      }
		      } 
		      catch (Exception ex){
		    	  log.severe("Не удалось отправить либо получить данные, ошибка "+ex);
		   	      throw new KassaException("Не удалось отправить либо получить данные, ошибка "+ex);
		      }
		 
		      //удаляем файл
		      FileUtil.deleteFile(XmlUtils.checkSlash(FileUtils.getTempDirectoryPath())+uploading.getName()+".cds");
		     			 
		      //если есть результативный ответ
		      if (respmessage!=null) {
			     String answerr=new String(respmessage);
			     if (StringUtils.isNotEmpty(answerr)) {
				    log.info(answerr); 
				    uploading.setReport(answerr);
				    uploading=uploadService.saveUpload(uploading);
				    Document doc=XmlUtils.getDocFromString(answerr, "<?xml version='1.0' encoding='windows-1251'?>");
				    if (doc!=null){
				    	//дата ответа
				    	if (StringUtils.isNotEmpty(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "responseDate")))) {
							uploading.setResponseDate(Convertor.toDate(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "responseDate")),DatesUtils.FORMAT_YYYYMMddHHmmss));
				    	}
				    	//номер ответа
				    	uploading.setResponseId(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "StreamID")));
				    	//код ошибки
				    	Integer errorCode=Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.getNodeFromAttributeValue(doc, "a", "n", "errorCode")));
				    	//если ошибок не было
				    	if (errorCode==Requests.RESPONSE_CODE_CAIS_OK){
				    		//делаем форму для КБ
				    	    //generateReport(uploading.getName(),enc.length);
						 	//пишем лог	  
							eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_OKB,"Выгрузка кредитных историй в КБ ОКБ прошла успешно",
									null,new Date(),null,null,null);
							//сохраняем статус успешно выгруженных
							uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_UPLOADED);
							//пишем в базу статус загрузки
							uploading.setDateUploading(sendingDate);
							uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
							uploading=uploadService.saveUpload(uploading);
							log.info("Успешно выгрузили файл в КБ ОКБ");
				    	} else {
				    		//не выгрузили
							uploading.setStatus(UploadStatus.UPLOAD_ERROR);
							uploading=uploadService.saveUpload(uploading);
							//пишем лог
							eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_OKB,"Не удалось выгрузить кредитные истории",
									null,new Date(),null,null,null);
							log.severe("Не выгрузили файл в КБ ОКБ, ошибка "+errorCode);
				    	}
				    } else {
				    	//вернулся непонятный документ
				    	uploading.setStatus(UploadStatus.UPLOAD_ERROR);
						uploading=uploadService.saveUpload(uploading);
						eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_OKB,"Не удалось получить документ из ответа при выгрузке данных в КБ ОКБ",
								null,new Date(),null,null,null);	
				    	log.severe("Не удалось получить документ из ответа при выгрузке данных в КБ ОКБ");
				    	throw new KassaException("Не удалось получить документ из ответа при выгрузке данных в КБ ОКБ");
				    }
			     }
		     }
		  
		     
		
		} catch (IOException e) {
			uploading.setStatus(UploadStatus.UPLOAD_ERROR);
			uploading=uploadService.saveUpload(uploading);
			eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_OKB,e.toString(),
					null,new Date(),null,null,null);	
		    throw new KassaException("не записался файл");
		}
	}
	
}
	
	/**
	 * генерим имя для выгрузки
	 * @param dt - дата выгрузки
	 * @param isWork - рабочий сервис или тестовый
	 * @return
	 */
	private String generateName(Date dt,Boolean isWork){
		Partner parOkb = refBooks.getPartner(Partner.OKB_CAIS);
		String code=isWork?parOkb.getCodeWork():parOkb.getCodeTest();
		String prefix=isWork?"SUB_":"TST_";
		String st=prefix+"00"+code+"_"+DatesUtils.DATE_FORMAT_YYYYMMdd.format(dt)+"_"+BaseCredit.COMMON_CREDIT_CODE+"_001";
		return st;
	}
	
	@Override
	public String getBusinessObjectClass() {
		return CreditRequest.class.getName();
	}
	
	@Override
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
		//у ОКБ нет этого функционала		
	}

	@Override
	public String getSystemDescription() {
		return ScoringOkbCaisBeanLocal.SYSTEM_DESCRIPTION;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
			throws KassaException {
		
		PartnersEntity parOkbCais=refBooks.getPartnerEntity(Partner.OKB_CAIS);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.OKB_CAIS,UploadStatus.UPLOAD_CREDIT);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
		DateRange chooseDateRange=new DateRange();
		if (DatesUtils.isSameDay(uploadDateRange.getFrom(), uploadDateRange.getTo())){
			chooseDateRange=uploadDateRange;
		}else{
			chooseDateRange.setFrom(uploadDateRange.getFrom());
			chooseDateRange.setTo(DateUtils.addDays(uploadDateRange.getTo(), -1));
		}
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT);
		List<Object[]> lobj=new ArrayList<Object[]>(0);
		log.info("Всего записей для выгрузки "+lstcre.size());
        if (lstcre.size() > 0) {
        	
        	UploadingEntity upl=uploadService.newUploading(null, Partner.OKB_CAIS, UploadStatus.UPLOAD_CREDIT, 
        			null,null, null, null, null, null, null, null, null, true);
            int i = 1;
            for (CreditRequestEntity cre : lstcre) {
              
                lobj.add(listDataForUpload(cre,uploadDateRange,chooseDateRange,i));
                //пишем запись в таблицу detail
                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                i++;
            }
            
           //своя организация
           OrganizationEntity org=orgService.getOrganizationActive();
    	   //файл выгрузки	
    	   String st=MakeXML.createUploadForSendingToOkbCaisNew(uploadDateRange.getTo(), lobj, isWork?parOkbCais.getCodework():parOkbCais.getCodetest(), isWork?parOkbCais.getGroupWork():parOkbCais.getGroupTest(), isWork?parOkbCais.getLoginwork():parOkbCais.getLogintest(), org.getName(), parOkbCais.getUploadVersion());
    	   //имя файла
    	   String fname=generateName(uploadDateRange.getTo(),isWork);
    	   log.info("Записали файл для выгрузки в OKB");
    	   log.info("Название файла выгрузки "+fname);
    	   //сохранили данные в таблицу выгрузки
		   upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT, 
       			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
 		      		  
    	   return upl;
        } else {		
		   return null;
        }
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
	private String buildForm(String subscriber,String group,String user,String password,
			String filename,byte[] file) {
		
		 StringBuilder sb = new StringBuilder();
		
		 sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"Subscriber\"\r\n\r\n");
         sb.append(Convertor.toEscape(subscriber) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"Group\"\r\n\r\n");
         sb.append(Convertor.toEscape(group) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"User\"\r\n\r\n");
         sb.append(Convertor.toEscape(user) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"Password\"\r\n\r\n");
         sb.append(Convertor.toEscape(password) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"Function\"\r\n\r\n");
         sb.append(Convertor.toEscape(String.valueOf(Uploading.FUNCTION_UPLOAD_CAIS)) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"ActionFlag\"\r\n\r\n");
         sb.append(Convertor.toEscape(String.valueOf(Uploading.FLAG_UPLOAD_CAIS)) + "\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"FileBody\"; filename=\"" + Convertor.toEscape(filename) + "\"\r\n");
         sb.append("Content-Type: application/octet-stream\r\n\r\n");
         try {
        	sb.append("----- BEGIN PKCS7 ENCRYPTED -----"); 
			sb.append(new String(Base64.encodeBase64(file)));
			sb.append("----- END PKCS7 ENCRYPTED -----");
		 } catch (Exception e) {
			log.severe("Не удалось перекодировать файл "+e);
		 }
         sb.append("\r\n");
         sb.append("---------------------------7d63d7371a0494\r\n");
         sb.append("Content-Disposition: form-data; name=\"FileDescription\"\r\n\r\n");
         sb.append(Convertor.toEscape("Upload data") + "\r\n---------------------------7d63d7371a0494--");
         
		return sb.toString();
	}
	
	/**
	 * генерируем письмо, пока не требуют, возможно пригодится
	 * @param fileName - название файла выгрузки
	 * @param fileSize - размер файла выгрузки
	 */
	private void generateReport(String fileName,int fileSize){
		File addition=new File(fileName+".txt");
	    Map<String,Object> params=new HashMap<String,Object>();
	    OrganizationEntity org=orgService.getOrganizationActive();
	    params.put("orgname", org.getName());
	    params.put("orgemail", org.getEmail());
	    params.put("orgphone", org.getPhone());
	    params.put("dateupload", DatesUtils.DATE_FORMAT_YYYYMMdd.format(new Date()));
	    params.put("creditcode", BaseCredit.COMMON_CREDIT_CODE);
	    params.put("filename", fileName+".zip.pem.pem");
	    params.put("filesize", fileSize);
	    try{
	        String sta=kassaBean.generateEmail(null, Report.EMAILOKB_ID, params);
	        //записываем в файл
	        FileUtils.writeByteArrayToFile(addition, sta.getBytes(XmlUtils.ENCODING_WINDOWS));
	    } catch (Exception e){
	    	log.severe("Не удалось сгенерировать письмо и записать в файл");
	    }
	    //посылаем письмо в КБ, файл прикрепляем как вложение 
	    OrganizationEntity okb=orgService.getOrganizationPartnerActive(Partner.OKB_CAIS);
	    if (okb!=null){
		
	        mail.sendAttachment("Форма CAIS", "Форма CAIS",   okb.getEmail(), fileName+".txt");
	    }
	}
	
	/**
	 * создаем строку запроса
	 * @param creditRequest - заявка
	 * @param request - запрос
	 * @return
	 */
	private String createRequestString(CreditRequestEntity creditRequest,
			RequestsEntity request,Integer numberFunction){
		
		//персональные данные
		PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
		PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
		//дополнительные данные
		PeopleMiscEntity pplmsc=peopleBean.findPeopleMiscActive(pplmain);
		//документ
		DocumentEntity dc=peopleBean.findPassportActive(pplmain);
		//адрес регистрации	
		AddressEntity addrreg=peopleBean.findAddressActive(pplmain, BaseAddress.REGISTER_ADDRESS);
        //адрес проживания
		AddressEntity addrres=null;
		if (addrreg.getIsSame()==BaseAddress.IS_SAME) {
			addrres=addrreg;
		} else {
			addrres=peopleBean.findAddressActive(pplmain, BaseAddress.RESIDENT_ADDRESS);
		}
		//контакт
		PeopleContactEntity pplcont=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
		//занятость
		EmploymentEntity employ=peopleBean.findEmployment(pplmain, null, 
				refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);
		
		//строка запроса
		String rmessagexml=MakeXML.createXMLforOKBCais(pplmain,pplper,pplmsc,pplcont,dc,addrreg,addrres,employ,
				creditRequest,request,numberFunction);
	    log.info(rmessagexml);
	    return rmessagexml;
	}

	/**
	 * возвращаем запись для выгрузки
	 * @param creditRequest - заявка
	 * @param uploadDateRange - даты для выгрузки
	 * @param i - номер по порядку
	 * @return
	 */
	private Object[] listDataForUpload(CreditRequestEntity creditRequest,
			DateRange uploadDateRange,DateRange chooseDateRange,int i){	
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
     //оплата
     PaymentEntity pay = paymentService.getLastCreditPayment(cr.getId(), null);
     //телефон
     PeopleContactEntity phone = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
     //сумма всех платежей по кредиту
     Double sm=0d;
     //есть история
     boolean isHistory=false;
     boolean isHistorytwo=false;
     
     //сколько было платежей за месяц (не можем передавать больше 3 записей истории)
     Integer cntPays=paymentService.getCountPaymentsInMonth(cr.getId(), chooseDateRange.getTo());
     
     List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(cr.getId(), BaseCredit.OPERATION_OVERDUE, chooseDateRange);
     CreditDetailsEntity overdue=null;
     //если была оплата
     if (pay!=null&&cntPays<3){
    	 if (chooseDateRange.between(pay.getProcessDate())){
    	//	 isHistory=true;
    	 }
     }
     //запишем историю если кредит начался в промежутке между выгрузками и тогда же случилась просрочка
     if (lstOvrd.size()>0){
     	//если кредит только начался и уже есть просрочка
     	if (cr.getCreditdatabeg().after(uploadDateRange.getFrom())){
     	    overdue=lstOvrd.get(lstOvrd.size()-1);
     	  //  isHistory=true;
     	}
     }
     
     //передача в суд
     DebtEntity debtCourt=null;
     //есть ли решение суда
     Boolean hasCourtDecision=false;
     //информация по передаче дела в суд
     List<DebtEntity> debts=creditInfo.listDebts(creditRequest.getId(), null, Partner.SYSTEM, null,Debt.DEBT_COURT,null,uploadDateRange);
     if (debts.size()>0){
     	debtCourt=debts.get(0);
     	hasCourtDecision=(debtCourt.getDateDecision()!=null);
     }
     CreditDetailsEntity creditInform=creditBean.findCreditOperation(cr.getId(),null);
     return new Object[]{i, pplmain, pplper, pplmsc, dc, addrreg, addrres, creditRequest, cr, pay, phone,
     		sm,isHistory,isHistorytwo,debtCourt,hasCourtDecision,creditInform,overdue};
  }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate,
			Boolean isWork, List<Integer> creditIds) throws KassaException {
		PartnersEntity parOkbCais=refBooks.getPartnerEntity(Partner.OKB_CAIS);
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.OKB_CAIS,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
		Date firstDate=DatesUtils.makeDate(2010, 1, 1);
		uploadDateRange.setFrom(firstDate);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
		DateRange chooseDateRange=new DateRange();
		if (DatesUtils.isSameDay(uploadDateRange.getFrom(), uploadDateRange.getTo())){
			chooseDateRange=uploadDateRange;
		}else{
			chooseDateRange.setFrom(uploadDateRange.getFrom());
			chooseDateRange.setTo(DateUtils.addDays(uploadDateRange.getTo(), -1));
		}
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,creditIds);
		List<Object[]> lobj=new ArrayList<Object[]>(0);
		log.info("Всего записей для выгрузки "+lstcre.size());
        if (lstcre.size() > 0) {
        	
        	UploadingEntity upl=uploadService.newUploading(null, Partner.OKB_CAIS, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
        			null,null, null, null, null, null, null, null, null, true);
            int i = 1;
            for (CreditRequestEntity cre : lstcre) {
              
                lobj.add(listDataForUpload(cre,uploadDateRange,chooseDateRange,i));
                //пишем запись в таблицу detail
                uploadService.saveUploadingDetail(cre, upl, UploadStatus.UPLOADDETAIL_ADDED);
                i++;
            }
            
           //своя организация
           OrganizationEntity org=orgService.getOrganizationActive();
    	   //файл выгрузки	
    	   String st=MakeXML.createUploadForSendingToOkbCaisNew(uploadDateRange.getTo(), lobj, isWork?parOkbCais.getCodework():parOkbCais.getCodetest(), isWork?parOkbCais.getGroupWork():parOkbCais.getGroupTest(), isWork?parOkbCais.getLoginwork():parOkbCais.getLogintest(), org.getName(), parOkbCais.getUploadVersion());
    	   //имя файла
    	   String fname=generateName(uploadDateRange.getTo(),isWork);
    	   log.info("Записали файл для выгрузки в OKB");
    	   log.info("Название файла выгрузки "+fname);
    	   //сохранили данные в таблицу выгрузки
		   upl=uploadService.newUploading(upl, null, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
       			fname,st, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
 		      		  
    	   return upl;
        } else {		
		   return null;
        }
	}
	
	/**
	 * пишем кредит
	 * @param lndcr - список нод, из которых берем значения
	 * @param ndcs - нода, в которой содержатся помесячные выплаты
	 * @param sto - отношение к кредиту
	 * @param oname - название организации, выдавшей кредит
	 * @param cre - заявка
	 * @param pplmain - человек
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private List<CreditEntity> saveOkbCredit(List<Node> lndcr,Node ndcs,String sto,String oname,
			CreditRequestEntity cre,PeopleMainEntity pplmain,CreditEntity clientCredit,
			List<CreditEntity> creditList,Integer unique,VerificationEntity verification){
		//credit
        CreditEntity cred=new CreditEntity();
      
        //отношение к кредиту
        if (StringUtils.isNotEmpty(sto)) {
            cred.setCreditrelationId(refBooks.findByCodeEntity(RefHeader.CREDIT_RELATION_STATE, sto));
        }
        //дата начала
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"openDate"))){
        	if (getValueFromNode(lndcr,"openDate").length()==8){
	            cred.setCreditdatabeg(Convertor.toDate(getValueFromNode(lndcr,"openDate"), DatesUtils.FORMAT_YYYYMMdd));
        	} else {
        		cred.setCreditdatabeg(Convertor.toDate(getValueFromNode(lndcr,"openDate"), DatesUtils.FORMAT_YYYYMMddHHmmss));
        	}
        }
        //просроченный статус
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"accountPaymentStatus"))) {
        	try {
                ReferenceEntity rfe=refBooks.findFromKbCode(RefHeader.CREDIT_OVERDUE_STATE_OKB, getValueFromNode(lndcr,"accountPaymentStatus"));
               	cred.setOverduestateId(rfe);
                            
        	} catch (Exception e) {
        		log.severe("Не удалось найти просроченный статус в справочнике "+getValueFromNode(lndcr,"accountPaymentStatus"));
        	}
        }
        //худший просроченный статус
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"worstPaymentStatus"))) {
        	try {
                ReferenceEntity rfe=refBooks.findFromKbCode(RefHeader.CREDIT_OVERDUE_STATE_OKB, getValueFromNode(lndcr,"accountPaymentStatus"));
                cred.setWorstOverdueStateId(rfe);
                                
        	} catch (Exception e) {
        		log.severe("Не удалось найти просроченный статус в справочнике "+getValueFromNode(lndcr,"accountPaymentStatus"));
        	}
        }
        //сумма
        cred.setCreditsum(Convertor.toDouble(getValueFromNode(lndcr,"amountOfFinance"),CalcUtils.dformat));
        //лимит кредитной карты (пишем в сумму кредита)
        Double creditLimit=new Double(0);
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"creditLimit"))){
          cred.setCreditsum(Convertor.toDouble(getValueFromNode(lndcr,"creditLimit"),CalcUtils.dformat));
          creditLimit=Convertor.toDouble(getValueFromNode(lndcr,"creditLimit"),CalcUtils.dformat);
        }
        //дата окончания фактическая
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"accountClosedDate"))){
        	if (getValueFromNode(lndcr,"accountClosedDate").length()==8){
  	            cred.setCreditdataendfact(Convertor.toDate(getValueFromNode(lndcr,"accountClosedDate"), DatesUtils.FORMAT_YYYYMMdd));
        	} else {
        		cred.setCreditdataendfact(Convertor.toDate(getValueFromNode(lndcr,"accountClosedDate"), DatesUtils.FORMAT_YYYYMMddHHmmss));
        	}
        	cred.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE,BaseCredit.CREDIT_CLOSED));
  	        cred.setIsover(true);
        } else {
        	cred.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE,BaseCredit.CREDIT_ACTIVE));
        	cred.setIsover(false);
        }
        //номер счета
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"consumerAccountNumber"))){
            cred.setCreditAccount(getValueFromNode(lndcr,"consumerAccountNumber"));
        }
        //тип кредита
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"financeType"))) {
        	try {
                ReferenceEntity rfe=refBooks.findFromKbCode(RefHeader.CREDIT_TYPE_OKB, getValueFromNode(lndcr,"financeType"));
               	if (rfe!=null){
                    cred.setCredittypeId(rfe);
               	} else {
               		cred.setCredittypeId(refBooks.getCreditType(BaseCredit.ANOTHER_CREDIT).getEntity());
               	}
       
                
        	} catch (Exception e) {
        		log.severe("Не удалось найти код финансирования "+getValueFromNode(lndcr,"financeType"));
        	}
        } else {
        	cred.setCredittypeId(refBooks.getCreditType(BaseCredit.ANOTHER_CREDIT).getEntity());
        }
        //ставим микрокредит, если в названии орг. записано МФО
       	if (oname.equalsIgnoreCase("Finance house")){
        	cred.setCredittypeId(refBooks.getCreditType(BaseCredit.MICRO_CREDIT).getEntity());
       	}
        //валюта
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"currency"))) {
        	try {
                ReferenceEntity rfe=refBooks.findFromKbCode(RefHeader.CURRENCY_TYPE_OKB, getValueFromNode(lndcr,"currency"));
                cred.setIdCurrency(rfe);
                //если нет в справочнике
                if (rfe==null){
                	cred.setIdCurrency(refBooks.getCurrency(BaseCredit.CURRENCY_UNKNOWN).getEntity());
                }
        	} catch (Exception e) {
        		log.severe("Не удалось найти валюту кредита "+getValueFromNode(lndcr,"currency"));
        	}
        }
        
        //дата окончания по графику
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"fulfilmentDueDate"))){
        	if (getValueFromNode(lndcr,"fulfilmentDueDate").length()==8){
	            cred.setCreditdataend(Convertor.toDate(getValueFromNode(lndcr,"fulfilmentDueDate"), DatesUtils.FORMAT_YYYYMMdd));
        	} else {
        		cred.setCreditdataend(Convertor.toDate(getValueFromNode(lndcr,"fulfilmentDueDate"), DatesUtils.FORMAT_YYYYMMddHHmmss));
        	}
        }
        
        //проверим на существование в БД такого же кредита
        if (creditList.size()>0){
        	for (CreditEntity credit:creditList){
        		try{
        		    if (creditService.isMatch(cred, credit)&&unique==1){
        		    	//если это кредит клиента, то запишем верификацию
        		    	if (clientCredit!=null&&credit.getPartnersId().getId()==Partner.CLIENT){
        		    		log.info("запишем системную верификацию ОКБ по заявке "+cre.getId());
        		    		creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
        	            			credit, cred,VerificationConfig.MARK_FOR_BANK);
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
        	    log.info("запишем системную верификацию ОКБ по заявке "+cre.getId());
              	creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
            			clientCredit, cred,VerificationConfig.MARK_FOR_BANK);
        }
      
        //текущая просроченная задолженность
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"arrearsBalance"))){
            cred.setCurrentOverdueDebt(Convertor.toDouble(getValueFromNode(lndcr,"arrearsBalance"),CalcUtils.dformat));
        }
        //общая сумма задолженности
        Double outstandingBalance=new Double(0);
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"outstandingBalance"))){
            cred.setCreditsumdebt(Convertor.toDouble(getValueFromNode(lndcr,"outstandingBalance"),CalcUtils.dformat));
            outstandingBalance=Convertor.toDouble(getValueFromNode(lndcr,"outstandingBalance"),CalcUtils.dformat);
        }
        
        //неиспользованный кредитный лимит
        if (cred.getCredittypeId()!=null&&cred.getCredittypeId().getCodeinteger()!=null&&cred.getCredittypeId().getCodeinteger()==BaseCredit.CREDIT_CARD){
        	cred.setCreditlimitunused(Utils.defaultDoubleFromNull(creditLimit)-Utils.defaultDoubleFromNull(outstandingBalance));
        }
        //частота платежей
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"paymentFrequency"))){
          cred.setCreditfreqpaymentId(refBooks.findByCodeEntity(RefHeader.CREDIT_PAYMENT_FREQ, getValueFromNode(lndcr,"paymentFrequency")));
        }
        //цель финансирования
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"purposeOfFinance"))){
        	// TODO добавить цель финансирования
        }
        //погашение за счет обеспечения
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"reasonForClosure"))){
        	if (Utils.defaultIntegerFromNull(Convertor.toInteger(getValueFromNode(lndcr,"reasonForClosure")))==BaseCredit.MONEY_BACK){
        		cred.setCreditmoneyback(1);
        	}
        }
        //размер периодического платежа
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"instalment"))){
          cred.setCreditMonthlyPayment(Convertor.toDouble(getValueFromNode(lndcr,"instalment"),CalcUtils.dformat));
        }
        //ставка кредита
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"interestRate"))){
          cred.setCreditpercent(Convertor.toDouble(getValueFromNode(lndcr,"interestRate"),CalcUtils.dformat));
        }
        //полная стоимость кредита в процентах
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"totalCostOfFinanceInPercent"))){
          cred.setCreditFullCost(Convertor.toDouble(getValueFromNode(lndcr,"totalCostOfFinanceInPercent"),CalcUtils.dformat_xx));
        }
        //дата последнего обновления
        if (StringUtils.isNotEmpty(getValueFromNode(lndcr,"lastUpdateTS"))){
	        cred.setDatelastupdate(Convertor.toDate(getValueFromNode(lndcr,"lastUpdateTS"), DatesUtils.FORMAT_YYYYMMddHHmmss));
        }
      
        //monthly history
        Node ndm=XmlUtils.getNodeFromAttributeValue(ndcs, "c", "n", "MonthlyHistory");
			
        String paydisc="";
        Integer delay30=0;
        Integer delay60=0;
        Integer delay90=0;
        Integer delay120=0;
        Integer delaymore=0;
        Double overdueDebt=new Double(0);
        List<Node> lsthist=XmlUtils.findNodeList(ndm, "s");
        if (lsthist.size()>0) {
          for (Node ndh:lsthist) {
	         //all attributes
             List<Node> lndh=XmlUtils.findNodeList(ndh, "a");
             CreditHistoryPayEntity ch=new CreditHistoryPayEntity();
             ch.setCreditId(cred);
             //лимит кредитной карты
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"creditLimit"))){
    	         ch.setCreditlimitSum(Convertor.toDouble(getValueFromNode(lndh,"creditLimit"), CalcUtils.dformat));
             }
             //оставшаяся сумма
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"accountBalance"))) {
    	         ch.setAccountSum(Convertor.toDouble(getValueFromNode(lndh,"accountBalance"), CalcUtils.dformat));
             }
             //просроченная сумма
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"arrearsBalance"))){
    	         ch.setOverdueSum(Convertor.toDouble(getValueFromNode(lndh,"arrearsBalance"), CalcUtils.dformat));
    	         if(overdueDebt<ch.getOverdueSum()){
    	        	 overdueDebt=ch.getOverdueSum();
    	         }
             }
             //оплаченная сумма
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"instalment"))){
    	         ch.setPaidSum(Convertor.toDouble(getValueFromNode(lndh,"instalment"), CalcUtils.dformat));
             }
             //дата
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"historyDate"))){
			        ch.setHistoryDate(Convertor.toDate(getValueFromNode(lndh,"historyDate"), DatesUtils.FORMAT_YYYYMMdd));
             }
             //вид просрочки
            
             if (StringUtils.isNotEmpty(getValueFromNode(lndh,"accountPaymentHistory"))) {
            	 try {
            		
	                ReferenceEntity rfe=refBooks.findFromKbCode(RefHeader.CREDIT_OVERDUE_STATE_OKB, getValueFromNode(lndh,"accountPaymentHistory"));
	                ch.setPaymentHistoryId(rfe);
	                if (rfe!=null){
	                  
	                  //ищем закрытые просрочки	
	                  if (paydisc.length()!=0){
	                	//если последний символ нашей записи отличается от текущего символа в разборе  
	                    if (!paydisc.substring(paydisc.length()-1).equalsIgnoreCase(getValueFromNode(lndh,"accountPaymentHistory")) 
	                    		&&paydisc.substring(paydisc.length()-1).equalsIgnoreCase(BaseCredit.NEW_OVERDUE_RSTANDART)){
	                	 
	                    
	                	  //закрытая просрочка до 30 дней
	                      if (rfe.getCode().equalsIgnoreCase(BaseCredit.OVERDUE_BEFORE_MONTH)){
	                    	  delay30+=1;
	                		
	                      } else if (rfe.getCode().equalsIgnoreCase(BaseCredit.OVERDUE_MONTH)){
	                    	//закрытая просрочка до 60 дней
	               			  delay60+=1;
	                		
	                      } else  if (rfe.getCode().equalsIgnoreCase(BaseCredit.OVERDUE_TWO_MONTH)){
	                    	  //закрытая просрочка до 90 дней
	                    	   delay90+=1;
	                    	  
	                      } else  if (rfe.getCode().equalsIgnoreCase(BaseCredit.OVERDUE_THREE_MONTH)){
	                    	  //закрытая просрочка до 120 дней
	                    	   delay120+=1;
	                    	  
	                      } else {
	                           //закрытая просрочка более 120 дней
	                		   delaymore+=1;
	                      }
	                     
	                    }// end if not equals
	                  }//end if length>0
	                  paydisc+=getValueFromNode(lndh,"accountPaymentHistory");
	                }
            	 } catch (Exception e) {
            		 log.severe("Не удалось найти вид просрочки в справочнике "+getValueFromNode(lndh,"accountPaymentHistory"));
            	 }
	          }//end есть account payment history
            // emMicro.persist(ch);
           }//end for
        }//end history
        
        //платежная дисциплина
        cred.setPayDiscipline(Convertor.toLimitString(paydisc,200));
        //просрочек до 30 дней
        cred.setDelay30(delay30);
        //просрочек до 60 дней
        cred.setDelay60(delay60);
        //просрочек до 90 дней
        cred.setDelay90(delay90);
        //просрочек до 120 дней
        cred.setDelay120(delay120);
        //просрочек более 90 дней
        cred.setDelaymore(delaymore);
        //максимальный просроченный долг
        cred.setMaxOverdueDebt(overdueDebt);
        cred.setPeopleMainId(pplmain);
        cred.setCreditRequestId(cre);
        cred.setPartnersId(refBooks.getPartnerEntity(Partner.OKB_CAIS));
        cred.setIssameorg(false);
        cred.setIsactive(ActiveStatus.ACTIVE);
        creditDAO.saveCreditEntity(cred);
        creditList.add(cred);
        return creditList;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
			Integer cacheDays, Integer numberFunction) throws ActionException {
		//проверяем, кеширован ли запрос
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
		    		Partner.OKB_CAIS, new Date(), cacheDays);
		if (inCache){
		   	log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в КБ ОКБ, он кеширован");
		    //save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_CAIS,"Запрос к внешнему партнеру КБ ОКБ не производился, данные кешированы",
						null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
			RequestsEntity reqOld=requestsService.getLastPeopleRequestByPartner(cre.getPeopleMainId().getId(), Partner.OKB_CAIS);
			if (reqOld!=null){
				try {
					saveAnswer(reqOld,reqOld.getResponsebodystring());
				} catch (Exception e){
					log.severe("Не удалось разобрать старый запрос, ошибка "+e);
				}
			}
		   	return null;
		}
		//new cais request
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.OKB_CAIS, RequestStatus.STATUS_IN_WORK, new Date());
					
		Partner parOkbCais = refBooks.getPartner(Partner.OKB_CAIS);
				
		//строка запроса
		String rmessagexml=createRequestString(cre,req,numberFunction);
	    //параметры для запроса
	  	String partnerId=isWork?parOkbCais.getCodeWork():parOkbCais.getCodeTest();
	   	String login=isWork?parOkbCais.getLoginWork():parOkbCais.getLoginTest();
	    String password=isWork?parOkbCais.getPasswordWork():parOkbCais.getPasswordTest();
	    String group=isWork?parOkbCais.getGroupWork():parOkbCais.getGroupTest();
	    //закодированная строка запроса с параметрами
		String rmessage=MakeXML.createEncodedStringForCais(partnerId, group, login, password, numberFunction.toString(),rmessagexml);
			 
		req.setRequestbody(rmessage.getBytes());
			
		//save request
		req=requestsService.saveRequestEx(req);
			   
	    //send request and get response
	    byte[] respmessage=null;
			
	    //параметры для httpPost
	    Map<String,String> rparams=new HashMap<String,String>();
	    rparams.put("Content-Type", "application/x-www-form-urlencoded; Charset=windows-1251");
	    rparams.put("Content-Length", String.valueOf(rmessage.length()));
	    rparams.put("Connection", "Close");
	    String url=isWork?parOkbCais.getUrlWork():parOkbCais.getUrlTest();
	    log.info("Url для запроса "+url);
	    //инициализируем ssl context
	    SSLContext sslCon = null;
	    try {
	        sslCon = initSSLContext(isWork);
	    }  catch (KassaException ex){
	    	requestsService.saveRequestError(req,ex,ErrorKeys.CANT_CREATE_SSL_CONTEXT,"Не удалось создать ssl context",Type.TECH, ResultType.NON_FATAL);
		}
		
	    //послали запрос, получили синхронный ответ
		try {
		  	try {
		   		respmessage = HTTPUtils.sendHttp("POST",url, rmessage.getBytes(),rparams,sslCon);
		   	} finally {
		   		returnSSLContext(isWork, sslCon);
		   		sslCon=null;
		   	}
		} catch (Exception e) {
			requestsService.saveRequestError(req,e,ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.TECH, ResultType.NON_FATAL);
		}
				
		//если есть ответ
		if (respmessage!=null) {
		  	//расшифровываем
			String sxmlContent="";
			try {
				String answerr=new String(respmessage);
				//есть ответ
				if (StringUtils.isNotEmpty(answerr)) {
				    String answer = new String(Base64.decodeBase64(answerr));
				    String xmlContent=XmlUtils.getXmlFromRaw(answer, "<s>", "s");
				    sxmlContent=Convertor.fromEscapeReplace(xmlContent);
				    log.info("Получили данные из ОКБ по заявке "+cre.getId());
				} else {
					requestsService.saveRequestError(req,null,ErrorKeys.NOTHING_TO_DECODE, "Нет сообщения, которое можно расшифровать", Type.TECH, ResultType.FATAL);
				}
			  } catch (Exception e) {
				    requestsService.saveRequestError(req,e,ErrorKeys.CANT_DECODE_MESSAGE, "Не удалось раскодировать xml", Type.TECH, ResultType.FATAL);
			}
					 
			//записали ответ в БД
			req.setResponsebody(respmessage);
			req.setResponsebodystring(sxmlContent);
            //save request
			req=requestsService.saveRequestEx(req);
		
			// разбираем			  
			try {
			    req=saveAnswer(req,sxmlContent);
			} catch (Exception ek){
				requestsService.saveRequestError(req,ek,ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать xml", Type.TECH, ResultType.FATAL);
			}
				
		}//end если есть ответ
				
		req=requestsService.saveRequestEx(req);
			  
		//пишем в лог
		eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_OKB_CAIS,"Произведен вызов внешней системы КБ ОКБ", 
				null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
		
		return req;
	}
	
	
}

