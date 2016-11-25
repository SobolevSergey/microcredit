package ru.simplgroupp.ejb;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.DebtDao;
import ru.simplgroupp.dao.interfaces.DocumentsDAO;
import ru.simplgroupp.dao.interfaces.NegativeDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.PhonePayDAO;
import ru.simplgroupp.dao.interfaces.PhonePaySummaryDAO;
import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ActionRuntimeException;
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
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
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
import ru.simplgroupp.persistence.BlacklistEntity;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditHistoryPayEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.NegativeEntity;
import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleIncapacityEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;
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
import ru.simplgroupp.transfer.BlackList;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.RefNegative;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Summary;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.transfer.VerificationConfig;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MakeXML;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Реальный класс для работы с КБ Русский стандарт
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringRsBeanLocal.class)
public class ScoringRsBean extends AbstractPluginBean implements ScoringRsBeanLocal {
	
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
	CreditRequestDAO creditRequestDAO;
	 
	@EJB
	AddressBeanLocal addressBean;
	 
	@EJB
	PeopleDAO peopleDAO;
	 
	@EJB
	DocumentsDAO documentsDAO;
	
	@EJB
	NegativeDAO negativeDAO;
	
	@EJB
	DebtDao debtDAO;
	
	@EJB
	PhonePaySummaryDAO phonePaySummaryDAO;
	
	@EJB
	PhonePayDAO phonePayDAO;
	
	@EJB
	SummaryDAO summaryDAO;
		
	@EJB
    ProductBeanLocal productBean;
		
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
	
	 PartnersEntity parRStandard = refBooks.getPartnerEntity(Partner.RSTANDARD);
	 //получаем документ из ответа	  
	 Document doc=XmlUtils.getDocFromString(answer);
	 //если вернулся нормальный xml
	 if (doc!=null) {
		 
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
			
			//настройки для разбора данных
			Map<String,Object> params=productBean.getConsiderProductConfig(cre.getProductId().getId());
			Integer uniqueCredits=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_CREDITS));
			Integer uniqueDocuments=Convertor.toInteger(params.get(ProductKeys.TAKE_UNIQUE_DOCUMENTS));
			
			PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
			//информация по человеку - фио
			Node pnd=XmlUtils.findChildInNode(doc.getElementsByTagName("TitlePart").item(0), "NaturalPersonSubject", 0);
			List<Node> lstper=XmlUtils.findNodeList(pnd, "Title");
			if (lstper.size()>0)
			{
			  //ищем, были ли записаны ПД
			  List<PeoplePersonalEntity> lstpplpar=peopleBean.findPeoplePersonal(pplmain, req.getCreditRequestId(), Partner.RSTANDARD, ActiveStatus.ACTIVE);
			  //если нет
			  if (lstpplpar.size()==0)
			  {
				for (Node per:lstper)
				{
					if (!pplper.getSurname().trim().equalsIgnoreCase(XmlUtils.getAttrubuteValue(per, "LastName"))
							||!pplper.getName().trim().equalsIgnoreCase(XmlUtils.getAttrubuteValue(per, "FirstName"))
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
									Convertor.toDate(XmlUtils.getAttrubuteValue(pnd, "BirthDate"), DatesUtils.FORMAT_ddMMYYYY), 
									XmlUtils.getAttrubuteValue(pnd, "BirthPlace"), null,databeg, ActiveStatus.ACTIVE);
						} catch (Exception e) {
							log.severe("Не удалось сохранить актуальные ПД по заявке "+cre.getId()+", ошибка "+e);
						}
						
					}//end если не совпадают
				}//end for
				
				//заишем снилс и инн
				try {
					peopleBean.savePeopleMain(pplmain, 
							StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(pnd, "IndividualTaxpayerNumber"))?XmlUtils.getAttrubuteValue(pnd, "IndividualTaxpayerNumber"):"", 
							StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(pnd, "PensionNumber"))?XmlUtils.getAttrubuteValue(pnd, "PensionNumber"):"");
				} catch (PeopleException e) {
					log.severe("Не удалось записать инн и снилс по человеку "+pplmain.getId()+", ошибка "+e);
				}
			
			  }//end если не писали
			}//end нашлись ПД
			
			//паспорта
			List<Node> lstdoc=XmlUtils.findNodeList(pnd, "IdentificationDocument");
			if(lstdoc.size()>0){
				 // ищем, писали ли документы
			    DocumentEntity pasppar = peopleBean.findDocument(pplmain, cre, Partner.RSTANDARD, ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
			    // если нет
			    if (pasppar == null) {
			      // все документы
				  List<DocumentEntity> lstDoc=peopleBean.findDocuments(pplmain, null, uniqueDocuments==1?null:Partner.RSTANDARD, null, Documents.PASSPORT_RF);
			      for (Node nddoc:lstdoc){
					int doctype=Convertor.toInteger(XmlUtils.getAttrubuteValue(nddoc, "DocTypeID"));
				   
				    // только паспорт
				    if (Documents.PASSPORT_RF_EQUIFAX_RS==doctype) {
					    	
							String docseries=XmlUtils.getAttrubuteValue(nddoc,"DocSer");
					    	String docnomer=XmlUtils.getAttrubuteValue(nddoc,"DocNum");
					    	int docs=0;	
							if (lstDoc.size()>0) {
						      //проверяем документы на дубликаты
		                      for (int i=0;i<lstDoc.size();i++){
						      
						    	DocumentEntity pasp =lstDoc.get(i);
						     	// Если данные документа не совпадают, пишем в БД
						    	if (!pasp.getSeries().equals(docseries)||!pasp.getNumber().equals(docnomer)){
						    		docs++;
						    	}//end пишем в БД
							  }//end for проверяем на дубликаты
							}//есть в БД паспорта у человека
		                    if (docs==lstDoc.size()){
									try {
										DocumentEntity newPasp=peopleBean.newDocument(null,pplmain.getId(), cre.getId(), Partner.RSTANDARD,
												docseries, docnomer, 
												Convertor.toDate(XmlUtils.getAttrubuteValue(nddoc,"DocDate"), DatesUtils.FORMAT_ddMMYYYY), 
												XmlUtils.getAttrubuteValue(nddoc,"DocDelivInstCode"), 
												XmlUtils.getAttrubuteValue(nddoc,"DocDelivInstName"), ActiveStatus.ACTIVE);
									    lstDoc.add(newPasp);
									} catch (Exception e){
										log.severe("Не удалось записать активный документ по заявке "+cre.getId()+", ошибка "+e);
									}
		                    }//end пишем паспорт в БД
		                    
					  
				    }//end если это паспорт
				  }//end for
			    }//не писали данные по паспортам
			}//end if size>0
			
			//адреса
			//List<Node> lstadr=XmlUtils.findNodeList(pnd, "Address");
			List<Node> lstadr=XmlUtils.findNodeList(doc.getElementsByTagName("MainPart").item(0), "Address");
			if (lstadr.size()>0)
			{
				//были ли записаны адреса по этой заявке
				AddressEntity adrpar=peopleBean.findAddress(pplmain, Partner.RSTANDARD, cre, BaseAddress.REGISTER_ADDRESS, ActiveStatus.ACTIVE);
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
						
						  AddressEntity address=peopleBean.newAddressFias(null,pplmain.getId(), cre.getId(), Partner.RSTANDARD, 
								addressTypeId,XmlUtils.getNodeValueText((XmlUtils.findChildInNode(adr, "AddressString", 0))), 
								databeg, null, BaseAddress.COUNTRY_RUSSIA,ActiveStatus.ACTIVE,null,
								adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"HouseNum"),
								adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"BuildingNum"),
								adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"BuildingSymbol"),
								adrnd==null?null:XmlUtils.getAttrubuteValue(adrnd,"FlatNum"));
						  
						  //если еще есть адрес из фиас, его тоже запишем
						  if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Region", 0),"ID"))){
						      peopleBean.saveAddressDataWithStrings(address, XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Region", 0),"ID"),
								  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Region", 0),"Text"),
								  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "District", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "District", 0),"Ext"), 
								  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "City", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "City", 0),"Ext"), 
								  XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Village", 0),"Caption"), XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adr, "Village", 0),"Ext"), 
								  adrnd==null?null:XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adrnd, "Street", 0),"Caption"), 
								  adrnd==null?null:XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(adrnd, "Street", 0),"Ext"));
						  }
					  } catch (Exception e) {
						  log.severe("Не удалось записать адрес по заявке "+cre.getId()+",ошибка "+e);
					  }
					  
										
				
				  }//end for
				}//end еще не писали адреса
				
			}//end в ответе есть адреса
			
			//недееспособность
			List<Node> lstinc=XmlUtils.findNodeList(doc.getElementsByTagName("MainPart").item(0), "Incapacity");
			if (lstinc.size()>0){
				//проверяем, писали ли недееспособность
			    List<PeopleIncapacityEntity> lstIncapacity=peopleBean.findPeopleIncapacity(pplmain.getId(), cre.getId(), Partner.RSTANDARD);
			    for (Node incapacity:lstinc){
			        if (lstIncapacity.size()==0){
				        try {
				          Integer incapId=null;	
				          ReferenceEntity ref=refBooks.findFromKb(RefHeader.INCAPACITY_TYPE_RS, Convertor.toInteger(XmlUtils.getNodeValueText(XmlUtils.findChildInNode(incapacity, "Status", 0))));	
				          if (ref!=null){
				        	  incapId=ref.getCodeinteger();
				          }
					      peopleBean.newPeopleIncapacity(pplmain.getId(), cre.getId(), Partner.RSTANDARD, new Date(), incapId);
				        } catch (Exception e) {
					      log.severe("Не удалось записать недееспособность, ошибка "+e);
				        }
			        }//end если не писали
			    }
			}//end size>0
			
			//долги
			List<Node> lstdebt=XmlUtils.findNodeList(doc.getElementsByTagName("MainPart").item(0), "DebtObligationRecord");
			if (lstdebt.size()>0){
				 //ищем, писали ли данные 	
				  List<DebtEntity> lstDebt=creditInfo.listDebts(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null, Debt.DEBT_COURT, null, null);
				  //если не писали
				  if (lstDebt.size()==0) {
					  
					for (Node db:lstdebt){
						Node cdebt=XmlUtils.findChildInNode(db, "ContractData", 0);
						if (cdebt!=null){
							Node cinfo=null;
							String ccomment="";
							//если есть решение суда
							if (XmlUtils.isExistChildInNode(cdebt, "DebtObligationCourtInfo", 0)){
								Node court=XmlUtils.findChildInNode(cdebt, "DebtObligationCourtInfo", 0);
								//данные записаны как положено
								if (XmlUtils.isExistChildInNode(court, "CourtData", 0)){
									cinfo=XmlUtils.findChildInNode(court, "CourtData", 0);
									ccomment=XmlUtils.getAttrubuteValue(cinfo, "Decision");
								} else {
									//данные записаны строкой
									ccomment=XmlUtils.getNodeValueText(XmlUtils.findChildInNode(court, "CourtInfo", 0));
								}
							}
							//проверяем, есть ли такой долг в БД
							DebtEntity oldDebt=debtDAO.findDebt(pplmain.getId(), Partner.RSTANDARD, Convertor.toDouble(XmlUtils.getAttrubuteValue(cdebt, "Amount")), Debt.DEBT_COURT,null);
							if (oldDebt==null){
						      creditInfo.newDebt(cre.getId(), pplmain.getId(), Partner.RSTANDARD,null, 
								Convertor.toDouble(XmlUtils.getAttrubuteValue(cdebt, "Amount")), null,
								XmlUtils.getAttrubuteValue(cdebt, "TypeCaption"), 
								cinfo==null?null:XmlUtils.getAttrubuteValue(cinfo, "Name"), 
								Convertor.toDate(XmlUtils.getAttrubuteValue(cdebt, "Date"),DatesUtils.FORMAT_ddMMYYYY), 
								cinfo==null?null:XmlUtils.getAttrubuteValue(cinfo, "CaseId"), 
								StringUtils.isEmpty(ccomment)?XmlUtils.getAttrubuteValue(cdebt, "Description"):ccomment, 
								null,null,Debt.DEBT_COURT,null,ActiveStatus.ACTIVE);
							}
						}//end есть запись по долгу кредита
					}//end for
				  }//end if debt size=0
			}//end есть долги
			
			int numcredits=0;
			//есть ли кредитная история
			List<Node> lstcred=XmlUtils.findNodeList(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "CreditContractRecord");
			if (lstcred.size()>0)
			{
				//ищем кредиты от партнера
				List<CreditEntity> lstpar=creditBean.findCredits(pplmain, cre, parRStandard, false, null);
				//если нет
				if (lstpar.size()==0){
					//делаем старые кредиты неактивными
					creditDAO.makePartnerCreditsNotActive(Partner.RSTANDARD,pplmain.getId());
					
				    //кредиты
				    for (Node cred:lstcred)	{  
					
					    //проверяем на принадлежность к своей организации
					    Boolean sameOrg=false;
					    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cred, "Ownership"))){
						    sameOrg=Convertor.toBoolean(XmlUtils.getAttrubuteValue(cred, "Ownership"));
						    if (sameOrg==null){
							    sameOrg=false;
						    }
					    }
					    //если кредит выдан не нашей организацией
					    if (!sameOrg) {
					
					      Node cd=XmlUtils.findChildInNode(cred, "ContractData", 0);
					      //отношение к кредиту
					      int ri=0;
					      if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "RelationID"))) {
						      ri=Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "RelationID"));
					      }
					      //если он сам брал, то записываем
					      if (ri==0||ri==BaseCredit.CREDIT_OWNER) {
						      numcredits++;
						      //ищем системную верификацию, если есть кредит клиента
							  VerificationEntity verification=null;
							  if (clientCredit!=null){
								    verification=creditInfo.findOneVerification(cre.getId(), pplmain.getId(), Partner.SYSTEM, null);
							  }
						      creditList=saveRSCredit(cd, cre,pplmain,clientCredit,creditList,uniqueCredits,verification);
					      }//end если брал сам (закончилась запись кредита)
					    }//end не наша организация
				    }//end for	
					
				}//end не писали кредиты
			}//end есть кредиты в отчете
			
			//суммарная часть отчета
			if (XmlUtils.isExistChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "Summary", 0))	{
				//ищем, есть ли summary по данной заявке
				List<SummaryEntity> lstpar=summaryDAO.findSummary(pplmain, cre, parRStandard, null);
				//если нет
				if (lstpar==null) {
					//всего кредитов где клиент - основной заемщик
					if (numcredits>0){
						creditInfo.saveSummary(cre, pplmain, parRStandard,String.valueOf(numcredits), refBooks.getRefSummaryItem(Summary.CLIENT_OWNER).getEntity(),null);
					}
				    Node nd=XmlUtils.findChildInNode(XmlUtils.findChildInNode(doc.getElementsByTagName("Report").item(0), "MainPart", 0), "Summary", 0);
				    //просрочка до 30 дней
				    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29").equals("0")) {
					    creditInfo.saveSummary(cre, pplmain, parRStandard,XmlUtils.getAttrubuteValue(nd, "DebtCountFrom1To29"), refBooks.getRefSummaryItem(Summary.DELAY30).getEntity(),null);
				    }
				    //просрочка до 60 дней
				    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59").equals("0")) {
					    creditInfo.saveSummary(cre, pplmain, parRStandard,XmlUtils.getAttrubuteValue(nd, "DebtCountFrom30To59"), refBooks.getRefSummaryItem(Summary.DELAY60).getEntity(),null);
				    }
				    //просрочка до 90 дней
				    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89").equals("0")) {
					    creditInfo.saveSummary(cre, pplmain, parRStandard,XmlUtils.getAttrubuteValue(nd, "DebtCountFrom60To89"), refBooks.getRefSummaryItem(Summary.DELAY90).getEntity(),null);
				    }
				    //просрочка более 90 дней
				    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nd, "DebtCountMore120"))&&!XmlUtils.getAttrubuteValue(nd, "DebtCountMore120").equals("0")) {
					    creditInfo.saveSummary(cre, pplmain, parRStandard,XmlUtils.getAttrubuteValue(nd, "DebtCountMore120"), refBooks.getRefSummaryItem(Summary.DELAYMORE).getEntity(),null);
				    }
				}//end не писали
			}//end суммарная часть отчета
		
			//были ли запросы других пользователей
			if (XmlUtils.isExistChildInNode(doc, "BODY", "PreviousRequests", 0)) {
			  //ищем, есть ли summary кол-ву запросов
			  List<SummaryEntity> lstr=summaryDAO.findSummary(pplmain, cre, parRStandard, refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_WEEK).getEntity());
			  if (lstr==null) {
				Node allRequests=XmlUtils.findChildInNode(doc.getElementsByTagName("PreviousRequests").item(0),"TotalCount", 0);
				Node selfRequests=XmlUtils.findChildInNode(doc.getElementsByTagName("PreviousRequests").item(0),"SelfTotalCount", 0);
			  
				if (allRequests!=null&&selfRequests!=null){
			    	Integer reccnt=Convertor.toInteger(XmlUtils.getAttrubuteValue(allRequests, "ByLastWeekPeriod"))-Convertor.toInteger(XmlUtils.getAttrubuteValue(selfRequests, "ByLastWeekPeriod"));
			    	Integer reccntM=Convertor.toInteger(XmlUtils.getAttrubuteValue(allRequests, "ByLastMonthPeriod"))-Convertor.toInteger(XmlUtils.getAttrubuteValue(selfRequests, "ByLastMonthPeriod"));
			    
			    	//если кол-во чужих запросов за неделю больше 0, запишем
			    	if (reccnt>0){
				      creditInfo.saveSummary(cre, pplmain, parRStandard,String.valueOf(reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_WEEK).getEntity(),null);	
			    	}//end кол-во запросов больше нуля
			    	//если кол-во чужих запросов за месяц больше 0, запишем
			    	if (reccntM>0){
				      creditInfo.saveSummary(cre, pplmain, parRStandard,String.valueOf(reccntM+reccnt), refBooks.getRefSummaryItem(Summary.REQUESTS_LAST_MONTH).getEntity(),null);	
			    	}//end кол-во запросов больше нуля
				}//end есть ноды 
			  }//end не писали summary
			}//end были запросы от других пользователей
			
		    req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
		    req.setResponsemessage("Данные найдены");
		} else 	{
			//если есть ошибка
			if (XmlUtils.isExistChildInNode(doc, "BODY", "Error", 0)) {
			  req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
			  Node nd=XmlUtils.findChildInNode(doc, "BODY", "Error", 0);
			  req.setResponsecode(XmlUtils.getAttrubuteValue(nd, "Code"));
			  req.setResponsemessage(XmlUtils.getNodeValueText(nd));
		
			} else if (XmlUtils.isExistChildInNode(doc, "BODY", "Warning", 0))	{
			  //предупреждение	
			  req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
			  Node nd=XmlUtils.findChildInNode(doc, "BODY", "Warning", 0);
			  req.setResponsecode(XmlUtils.getAttrubuteValue(nd, "Code"));
			  req.setResponsemessage(XmlUtils.getNodeValueText(nd));
			
			} else {
			  //просто берем сервисы, основной части нет	
			  req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
			}
		}//end закончилась работа с основной частью
	     
		//*********
		//дополнительные сервисы
		//*********
		
		//верификация и негатив
		if (XmlUtils.isExistChildInNode(doc, "Ratings", "PreScoringRating", 0))
		{
			//сама верификация
			Node ndv=XmlUtils.findChildInNode(doc, "PreScoringRating", "SearchResult", 0);
			if (ndv!=null) {
			  List<VerificationEntity> verification=creditInfo.findVerification(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null);
			  //если не писали верификацию по данной заявке
			  if (verification.size()==0){
				Integer i1=-1;
				Integer i2=-1;
				Integer i3=-1;
				Integer i4=-1;
				Integer i5=-1;
				Integer ih=-1;
				
				//все записи по верификации
				List<Node> lstgr=XmlUtils.findNodeList(ndv, "Record");
				if (lstgr.size()>0){
					for (Node ndRec:lstgr){
						
				
				        Node persNode=XmlUtils.findChildInNode(ndRec,"H",0);
				        //совпадение по ФИО и ДР
				        if (persNode!=null){
				        	if (Convertor.toInteger(XmlUtils.getNodeValueText(persNode))!=null&&Convertor.toInteger(XmlUtils.getNodeValueText(persNode))>i1){ 
					            i1=Convertor.toInteger(XmlUtils.getNodeValueText(persNode));
				        	}
					        ih=Convertor.toInteger(XmlUtils.getNodeValueText(persNode));
				        }
				        //если совпало фио и др
				        if (ih!=null&&ih==1){
				          //документ	
				          Node docNode=XmlUtils.findChildInNode(ndRec,"D",0);
				          if (docNode!=null){
				        	 if (Convertor.toInteger(XmlUtils.getNodeValueText(docNode))!=null&&Convertor.toInteger(XmlUtils.getNodeValueText(docNode))>i2){ 
					             i2=Convertor.toInteger(XmlUtils.getNodeValueText(docNode));
				        	 }
				          }
				          //телефон
				          Node phoneNode=XmlUtils.findChildInNode(ndRec,"P",0);
				          if (phoneNode!=null){
				        	  if (Convertor.toInteger(XmlUtils.getNodeValueText(phoneNode))!=null&&Convertor.toInteger(XmlUtils.getNodeValueText(phoneNode))>i3){ 
					             i3=Convertor.toInteger(XmlUtils.getNodeValueText(phoneNode));
				        	  }
				          }
				          //адрес
				          Node adrNode=XmlUtils.findChildInNode(ndRec,"A",0);
				          if (adrNode!=null){
				        	  if (Convertor.toInteger(XmlUtils.getNodeValueText(adrNode))!=null&&Convertor.toInteger(XmlUtils.getNodeValueText(adrNode))>i4){ 
					             i4=Convertor.toInteger(XmlUtils.getNodeValueText(adrNode));
				        	  }
				          }
				          //инн
				          Node innNode=XmlUtils.findChildInNode(ndRec,"I",0);
				          if (innNode!=null){
				        	  if (Convertor.toInteger(XmlUtils.getNodeValueText(innNode))!=null&&Convertor.toInteger(XmlUtils.getNodeValueText(innNode))>i5){ 
					             i5=Convertor.toInteger(XmlUtils.getNodeValueText(innNode));
				        	  }
				          }
				         
				          
				        }//end если совпало фио
				    			
					}//end for
					creditInfo.saveVerification(cre, pplmain, parRStandard, null, null, i1, i2, i3, i4, i5);
				}//end if xml records size>0
			  }//end if verification size 0
			}//end tag verification exists
				
			//есть негатив
			Node nd=XmlUtils.findChildInNode(doc, "PreScoringRating", "FullRating", 0);
			if (nd!=null){
			  //ищем, писали ли уже негатив по данной заявке	
			  List<NegativeEntity> lstNegative=negativeDAO.findNegative(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null,null);	
			  //если не писали
			  if (lstNegative.size()==0)
			  {	
				List<Node> lstgr=XmlUtils.findNodeList(nd, "Group");
				//есть группы негатива
				if (lstgr.size()>0)
				{
					for (Node ndgr:lstgr)
					{
						List<Node> lstsgr=XmlUtils.findNodeList(ndgr, "SubGroup");
						//есть подгруппы негатива
						if (lstsgr.size()>0)
						{
							for (Node ndsgr:lstsgr)
							{
								List<Node> lstart=XmlUtils.findNodeList(ndsgr, "Article");
								//есть статьи негатива
								if (lstart.size()>0)
								{
									for (Node ndart:lstart)
									{
										//ищем, данные по этому человеку или нет
										String module=XmlUtils.getAttrubuteValue(ndart, "Module");
										if (StringUtils.isNotEmpty(module)&&module.substring(0,1).equals("1")){
										    
										    String article=XmlUtils.getAttrubuteValue(ndart, "ID");
										    //ищем террористов
										    if (article.equalsIgnoreCase(RefNegative.ARTICLE_TERROR)){
											    BlacklistEntity bl=null;
											    try {
												    bl= peopleBean.findInBlackList(pplmain, new Date(), BlackList.REASON_TERRORISM);
												    if (bl==null){
													    peopleBean.saveBlackList(pplmain, BlackList.REASON_TERRORISM, new Date(),"Данные получены от сервиса негатива Русского Стандарта");
												    }
											    } catch (PeopleException e) {
												    log.severe("Не удалось найти человека в черном списке");;
											    }
											
										    }
										    //сохраняем данные
										    List<NegativeEntity> oldNegative=negativeDAO.findNegative(null, pplmain.getId(), Partner.RSTANDARD, article,Convertor.toInteger(XmlUtils.getAttrubuteValue(ndart, "EventYear")));	
										    if(oldNegative.size()==0){
										        creditInfo.saveNegative(cre.getId(), pplmain.getId(), Partner.RSTANDARD, article, module, 
										    		Convertor.toInteger(XmlUtils.getAttrubuteValue(ndart, "EventYear")));
										    }
										   
										}//end ищем данные по этому человеку или нет
									}//end for статьи негатива
								}//end есть статьи негаива
							}//end for подгруппы негатива
						}//end есть подгруппы негатива
					}//end for группы негатива
				}//end есть группы негатива
			  }//end no records
			}//end has negative
			
		
			
		}//end verify
		
		//проверка паспорта
		if (XmlUtils.isExistChildInNode(doc, "Ratings", "MigrationServiceDocumentCheck", 0)) {
			DocumentEntity dc=peopleBean.findPassportActive(pplmain);
			Node nddc=XmlUtils.findChildInNode(doc, "MigrationServiceDocumentCheck", "DocumentStatus", 0);
			if (nddc!=null)	{
				 if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(nddc, "Id"))){
					 
					  try {
					      dc=documentsDAO.saveDocumentValidity(dc, Convertor.toInteger(XmlUtils.getAttrubuteValue(nddc, "Id")));
					  } catch (Exception e){
						  log.severe("Не удалось сохранить валидность паспорта "+XmlUtils.getAttrubuteValue(nddc, "Id")+", ошибка "+e);
					  }
					  
				 }//end validity not empty
			}//end exists node for check
		}
		
		//платежи за телефон
		if (XmlUtils.isExistChildInNode(doc, "Ratings", "PaymentHistoryRating", 0))
		{
			Node nd=XmlUtils.findChildInNode(doc, "Ratings", "PaymentHistoryRating", 0);
			//Общая часть
			List<Node> lstsum=XmlUtils.findNodeList(nd, "Summary");
			if (lstsum.size()>0)
			{
			  //ищем, писали ли данные по этой заявке
			  List<PhonePaySummaryEntity> lstPay=phonePaySummaryDAO.findPhonePaySummary(pplmain, cre, parRStandard);
			  //если не писали
			  if (lstPay.size()==0)
			  {
					for (Node nds:lstsum)
					{
						//суммарная часть
						PhonePaySummaryEntity psum=phonePaySummaryDAO.savePhonePaySummary(cre.getId(), pplmain.getId(), Partner.RSTANDARD, 
								Convertor.toInteger(XmlUtils.getAttrubuteValue(nds, "Type")), 
								XmlUtils.getAttrubuteValue(nds, "PeriodType"), 
								Convertor.toInteger(XmlUtils.getAttrubuteValue(nds, "PeriodCount")));
					  
					    //период
					    List<Node> lstpr=XmlUtils.findNodeList(nds, "Period");
					    if (lstpr.size()>0)
					    {
						   for (Node ndpr:lstpr)
						   {
							 //сами платежи
							 List<Node> lstpays=XmlUtils.findNodeList(ndpr, "Payments");
							 for (Node ndpay:lstpays) {
								 try{
								     phonePayDAO.savePhonePays(psum, Convertor.toInteger(XmlUtils.getAttrubuteValue(ndpr, "Num")), 
										 Convertor.toDate(XmlUtils.getAttrubuteValue(ndpr, "FirstDate"), DatesUtils.FORMAT_ddMMYYYY), 
										 Convertor.toInteger(XmlUtils.getAttrubuteValue(ndpay, "Count")), 
										 Convertor.toInteger(XmlUtils.getAttrubuteValue(nds, "Type")), 
										 Convertor.toDouble(XmlUtils.getAttrubuteValue(ndpay, "Sum")));
								 } catch (Exception e){
									 log.severe("Не удалось записать платеж за телефон, ошибка "+e);
								 }
							
							 }//end for
						   }//end for
					    }//end есть платежи в периоде
					  
					}//end for
				}//end если не писали
			}//end общая часть
			
		}//end phone pays
		
		//проверка долгов
		if (XmlUtils.isExistChildInNode(doc, "Ratings", "TaxServiceDebtInfo", 0)) {
			Node nddb=XmlUtils.findChildInNode(doc, "TaxServiceDebtInfo", "Taxes", 0);
			if (nddb!=null)	{
				//если есть хоть одна задолженность
				List<Node> lstdebt=XmlUtils.findNodeList(nddb, "Tax");
				if (lstdebt.size()>0) {
				  //ищем, писали ли данные 	
				  List<DebtEntity> lstDebt=creditInfo.listDebts(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null, Debt.DEBT_FNS, null, null);
				  //если не писали
				  if (lstDebt.size()==0) {
					  
					for (Node db:lstdebt){
						
						creditInfo.newDebt(cre.getId(), pplmain.getId(), Partner.RSTANDARD,null, 
								Convertor.toDouble(XmlUtils.getAttrubuteValue(db, "Amount")), 
								null,XmlUtils.getAttrubuteValue(db, "Name"), null, 
								Convertor.toDate(XmlUtils.getAttrubuteValue(db, "Date"),DatesUtils.FORMAT_ddMMYYYY), 
								null, null, XmlUtils.getAttrubuteValue(db, "Type"), 
								Convertor.toInteger(XmlUtils.getAttrubuteValue(db, "AuthorityCode")),
								Debt.DEBT_FNS,null,ActiveStatus.ACTIVE);
					}//end for
				  }//end if debt size=0
				}//end if has debts
			}//end if exist taxes 
		}//end if exist taxes rating
		
		//проверка судебного производства
		if (XmlUtils.isExistChildInNode(doc, "Ratings", "BailiffsServiceDebtInfo", 0)) {
			//ищем, писали ли данные 	
			List<DebtEntity> lstDebt=creditInfo.listDebts(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null, Debt.DEBT_FSSP, null, null);
			//если не писали
			if (lstDebt.size()==0) {
			  Node nddb=XmlUtils.findChildInNode(doc, "BailiffsServiceDebtInfo", "Debts", 0);
			  if (nddb!=null) {
				  if (Convertor.toDouble(XmlUtils.getAttrubuteValue(nddb, "Amount"), CalcUtils.dformat)>0){
				      //ищем долги
					  List<Node> listDebt=XmlUtils.findNodeList(nddb, "Debt");
					  for (Node nodeDebt:listDebt){
						  if (XmlUtils.isExistChildInNode(XmlUtils.findChildInNode(nodeDebt, "Payments", 0), "Payment", 0)){
				             List<Node> listPays=XmlUtils.findNodeList(XmlUtils.findChildInNode(nodeDebt, "Payments", 0), "Payment");
				             if (listPays.size()>0){
				            	 for (Node nodePay:listPays){
				            		 //проверяем, был ли записан долг ранее
				            		 DebtEntity oldDebt=debtDAO.findDebt(pplmain.getId(), Partner.RSTANDARD, Convertor.toDouble(XmlUtils.getAttrubuteValue(nodePay, "Amount"), CalcUtils.dformat), 
				            				 Debt.DEBT_FSSP,Convertor.toDate(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(XmlUtils.findChildInNode(nodeDebt, "ExecutiveDocuments", 0), "ExecutiveDocument", 0), "Date"), DatesUtils.FORMAT_ddMMYYYY));
									 //не писали
				            		 if (oldDebt==null){
							           DebtEntity debt= creditInfo.newDebt(cre.getId(), pplmain.getId(), Partner.RSTANDARD, null,
				    		             Convertor.toDouble(XmlUtils.getAttrubuteValue(nodePay, "Amount"), CalcUtils.dformat), 
				    		             null,XmlUtils.getAttrubuteValue(nodePay, "Reason"), 
							             XmlUtils.getNodeValueText(XmlUtils.findChildInNode(nodeDebt, "DepartmentAddress", 0)),	
							             null, null, null, null,	null,Debt.DEBT_FSSP,
							             Convertor.toDate(XmlUtils.getAttrubuteValue(XmlUtils.findChildInNode(XmlUtils.findChildInNode(nodeDebt, "ExecutiveDocuments", 0), "ExecutiveDocument", 0), "Date"), DatesUtils.FORMAT_ddMMYYYY),
							             ActiveStatus.ACTIVE);
							           log.info("Записали долг ФССП "+debt.getId());
									 }
				            	 }//end for
				             }
						  }//end есть суммы долга
					  }//end for nodes
				  }//end if amount>0
			  }//end if node not null
			}//end если не писали
		}//end if exists debt info
		
	 }//end если doc не null
	 return req;
   }
	 

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays)	throws ActionException {
		return newRequest(cre,isWork,false,false,false,false,false,false,false,cacheDays,0);
	}

	@Override
	public String getSystemName() {
		return ScoringRsBeanLocal.SYSTEM_NAME;
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
		
		log.info("Русский стандарт. Заявка " + creditRequestId + " ушла в скоринг.");
		try {
			CreditRequest ccRequest;
			try {
				ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
				log.info("Русский стандарт. Заявка " + creditRequestId + " проинициализирована.");
			} catch (Exception e) {
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку",Type.TECH, ResultType.FATAL,e);
			}
			if (ccRequest==null){
				log.severe("Не удалось инициализировать кредитную заявку" + creditRequestId);
				throw new ActionException(ErrorKeys.CANT_INIT_OBJECT,"Не удалось инициализировать кредитную заявку, заявка null",Type.TECH, ResultType.NON_FATAL,null);
			}
			RusStandartPluginConfig cfg=(RusStandartPluginConfig) context.getPluginConfig();
			if (context.getNumRepeats() <=cfg.getNumberRepeats()) {
			    newRequest(ccRequest.getEntity(),cfg.isUseWork(),cfg.getRequestPayment(),cfg.getRequestFMS(),
			    		cfg.getRequestFSSP(),cfg.getRequestFNS(),cfg.getRequestVerify(),cfg.getRequestScoring(),
			    		cfg.getRequestAntifrod(),cfg.getCacheDays(),cfg.getCreditReportDetalization());
			} else {
				
				log.severe("Русский стандарт. Заявка " + creditRequestId + " не обработана.");
				throw new ActionException(ErrorKeys.CANT_MAKE_SKORING,"Не удалось выполнить скоринговый запрос в КБ за 3 попытки",Type.TECH, ResultType.FATAL,null);
			}
		} catch (ActionException e) {
			//rsSslContext=null;
			log.severe("Русский стандарт. Заявка " + creditRequestId + " не обработана. Ошибка "+e+", "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		} catch (Throwable e) {
			//rsSslContext=null;
			log.severe("Русский стандарт. Заявка " + creditRequestId + " не обработана. Ошибка "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		}
		//rsSslContext=null;		
		log.info("Русский стандарт. Заявка " + creditRequestId + " обработана.");
	}


	@Override
	public EnumSet<ExecutionMode> getExecutionModesSupported() {
		return EnumSet.of(ExecutionMode.AUTOMATIC,ExecutionMode.MANUAL);
	}

	@Override
	public EnumSet<SyncMode> getSyncModesSupported() {
		return EnumSet.of(SyncMode.SYNC);
	}

	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
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
			settings = new String[] {CryptoSettings.RUS_STANDART_CLIENT_WORK, CryptoSettings.RUS_STANDART_SERVER_WORK};
		} else {
			settings = new String[] {CryptoSettings.RUS_STANDART_CLIENT_TEST, CryptoSettings.RUS_STANDART_SERVER_TEST};
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
		
	try {
		if (isWork) {
			return servBean.borrowSSLContext(new String[] {CryptoSettings.RUS_STANDART_CLIENT_WORK, CryptoSettings.RUS_STANDART_SERVER_WORK});
		} else {
			return servBean.borrowSSLContext(new String[] {CryptoSettings.RUS_STANDART_CLIENT_TEST, CryptoSettings.RUS_STANDART_SERVER_TEST});			
		}
	} catch (Exception ex) {
		log.severe("Не удалось инициализировать ssl context: "+ex.getMessage());
		throw new KassaException("Не удалось инициализировать ssl context: "+ex); 
	}
  }

	/**
	 * добавить в пакет, не используется в данном случае
	 */
	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		log.severe("Метод addToPacket не поддерживается для плагина RusStandart");
	}

	/**
	 * выполнить пакетный запрос, не используется в данном случае
	 */
	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		return null;
	}

	/**
	 * асинхронный запрос, не используется в данном случае
	 */
	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		log.warning("Метод sendSingleRequest не поддерживается");
		throw new UnsupportedOperationException();
	}

	/**
	 * ответ на асинхронный запрос, не используется в данном случае
	 */
	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		log.warning("Метод querySingleResponse не поддерживается");
		throw new UnsupportedOperationException();
		
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,
			Boolean requestPayment,
			Boolean requestFMS,
			Boolean requestFSSP,
			Boolean requestFNS,
			Boolean requestVerify,
			Boolean requestScoring,
			Boolean requestAntifrod,
			Integer cacheDays,
			Integer creditReportDetalization)
			throws ActionException {
		
		//проверяем, были ли уже успешные запросы по этой заявке
		boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.RSTANDARD, new Date(), cacheDays);
	    if (inCache){
	   	    log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в КБ Русский стандарт, он кеширован");
	     	//save log
			eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_RSTANDARD,
				"Запрос к внешнему партнеру КБ Русский стандарт не производился, данные кешированы",
					null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
		
			RequestsEntity reqOld=requestsService.getLastPeopleRequestByPartner(cre.getPeopleMainId().getId(), Partner.RSTANDARD);
			if (reqOld!=null){
				try {
					saveAnswer(reqOld,reqOld.getResponsebodystring());
				} catch (Exception e){
					log.severe("Не удалось разобрать старый запрос, ошибка "+e);
				}
			}
			return null;
	    }		
		//новая запись запроса
		RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
				Partner.RSTANDARD, RequestStatus.STATUS_IN_WORK, new Date());
		Partner parRStandard = refBooks.getPartner(Partner.RSTANDARD);
		
		//строка запроса
		String rmessage=createRequestString(cre,req,isWork,	requestPayment,requestFMS,
				requestFSSP,requestFNS,requestVerify,requestScoring,requestAntifrod,creditReportDetalization);
     	req.setRequestbody(rmessage.getBytes());
     	req=requestsService.saveRequestEx(req);
		
		//send request and get response
		byte[] respmessage=null;
		
		String url=isWork?parRStandard.getUrlWork():parRStandard.getUrlTest();
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
			
		} catch (Exception ex){
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
				answer=XmlUtils.HEADER_XML_WINDOWS+answer;
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
		eventLog.saveLogEx(EventType.INFO,EventCode.OUTER_SCORING_RSTANDARD,"Был выполнен запрос в КБ Русский Стандарт",
			null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
	
		return req;
		
	}
	/**
	 * послать пакетный запрос, не используется в данном случае
	 */
	@Override
	public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
		log.warning("Метод queryPacketResponse не поддерживается");
		throw new UnsupportedOperationException();
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadHistory(UploadingEntity uploading,Date sendingDate,Boolean isWork) throws KassaException {
		
		Partner parRStandart = refBooks.getPartner(Partner.RSTANDARD);
		
		//есть записи
		if (uploading!=null) {
		
			if (sendingDate==null){
			   sendingDate=new Date();
			}
			  
			//урл для выгрузки
			String url=isWork?parRStandart.getUrlUploadWork():parRStandart.getUrlUploadTest()+"?file-name="+uploading.getName();
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
					eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_RSTANDARD,"Не удалось прочитать xml ответа при выгрузке",
							null,new Date(),null,null,null);
					log.severe("Не удалось прочитать xml ответа при выгрузке ");
				}
				eventLog.saveLog(EventType.INFO,EventCode.UPLOAD_RSTANDARD,"Успешно завершилась загрузка в КБ 'Русский стандарт'",
						null,new Date(),null,null,null);
				//отмечаем записи, которые выгрузили
				uploadService.updateAfterUpload(sendingDate);
				//сохраняем статус успешно выгруженных
				uploadService.changeUploadingDetail(null, uploading, UploadStatus.UPLOADDETAIL_UPLOADED);
				//пишем в базу статус загрузки
				uploading.setDateUploading(sendingDate);
				uploading.setStatus(UploadStatus.UPLOAD_SUCCESS);
				uploading=uploadService.saveUpload(uploading);
				log.info("Успешно завершили выгрузку в КБ Русский стандарт");
			}
			//произошла ошибка
			else {
				uploading.setStatus(UploadStatus.UPLOAD_ERROR);
				uploading=uploadService.saveUpload(uploading);
				log.severe("Не удалось загрузить данные в КБ Русский стандарт");
				eventLog.saveLog(EventType.ERROR,EventCode.ERROR_UPLOAD_RSTANDARD,"Не удалось загрузить данные в КБ Русский стандарт",
						null,new Date(),null,null,null);
			}
		}
		
	}
	
	@Override
	public String getBusinessObjectClass() {
		return CreditRequest.class.getName();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {
		Partner parRStandart = refBooks.getPartner(Partner.RSTANDARD);
		//выбрали запись последней выгрузки
		if (uploading==null){
		    uploading=uploadService.getLastUploading(Partner.RSTANDARD,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		}
		//если нет ни одной незавершенной выгрузки
		if (uploading==null||uploading.getStatus()==UploadStatus.RESULT_SUCCESS){
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
		String urlFirst=isWork?parRStandart.getUrlUploadWork():parRStandart.getUrlUploadTest();
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
			}//end вернулся нормальный xml
		}//end если есть ответ
		
	}


	@Override
	public String getSystemDescription() {
		return ScoringRsBeanLocal.SYSTEM_DESCRIPTION;
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork) throws KassaException{
		Partner parRStandart = refBooks.getPartner(Partner.RSTANDARD);
		
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.RSTANDARD,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT_CREDITREQUEST);
		//List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,UploadStatus.UPLOAD_CREDIT);
		List<Object[]> lobj=new ArrayList<Object[]>(0);
		log.info("Всего записей для выгрузки "+lstcre.size());
		//есть записи
		if (lstcre.size()>0)
		{
			//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.RSTANDARD, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
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
			String st=MakeXML.createUploadForSendingToRStandard(uploadDateRange.getTo(), lobj, isWork?parRStandart.getCodeWork():parRStandart.getCodeTest(), 
					org.getName(), upl.getUploadId().toString(),parRStandart.getUploadVersion(),uploadDateRange.getFrom());
			
			log.info("Создали файл для выгрузки в Русский стандарт, кол-во записей "+lstcre.size());
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
	
	/**
	 * генерим xml для запроса
	 * @param creditRequest - заявка
	 * @param request - запрос
	 * @param isWork - тестовый сервис или рабочий
	 * @param requestPayment - запрос сервиса платежной истории
	 * @param requestFMS - запрос сервиса ФМС
	 * @param requestFSSP - запрос ФССП по долгам
	 * @param requestFNS - запрос ФНС по долгам
	 * @param requestVerify - запрос верификации
	 * @param requestScoring - запрос скоринга
	 * @param requestAntifrod - запрос анти-фрода
	 * @return
	 */
	private String createRequestString(CreditRequestEntity creditRequest,RequestsEntity request,
			Boolean isWork,
			Boolean requestPayment,
			Boolean requestFMS,
			Boolean requestFSSP,
			Boolean requestFNS,
			Boolean requestVerify,
			Boolean requestScoring,
			Boolean requestAntifrod,
			Integer creditReportDetalization){
     
		Partner parRStandard = refBooks.getPartner(Partner.RSTANDARD);
		
		//человек
		PeopleMainEntity pplmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
		PeoplePersonalEntity pplper=peopleBean.findPeoplePersonalActive(pplmain);
		PeopleMiscEntity pplmisc=peopleBean.findPeopleMiscActive(pplmain);
		//документ
		DocumentEntity dc=peopleBean.findPassportActive(pplmain);
		//контакт (телефон)
		PeopleContactEntity pplcont=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, pplmain.getId());
		String partnerId=isWork?parRStandard.getCodeWork():parRStandard.getCodeTest();
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
				requestPayment,requestFMS,requestFSSP,requestFNS,requestVerify,requestScoring,requestAntifrod,
				parRStandard.getRequestVersion(),request.getRequestNumber().toString(),creditReportDetalization);
		log.info(rmessage);
		log.info("Сформирован запрос в Русский стандарт по заявке "+creditRequest.getId());
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
            creditInform=creditBean.findCreditOperation(credit.getId(),uploadDateRange);
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
            	for (CreditDetailsEntity prolong:prolongInfo){
            	    List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, 
            			new DateRange(DateUtils.addDays(prolong.getEventDate(), -1),prolong.getEventDate()));
                    if (lstOvrd.size()>0){
            	        overdue.add(lstOvrd.get(0));
                    }
            	}
            } 
           
            //стандартная просрочка, если не было закрытия или продления
            List<CreditDetailsEntity> lstOvrd=creditBean.findCreditDetails(credit.getId(), BaseCredit.OPERATION_OVERDUE, uploadDateRange);
            if (lstOvrd.size()>0){
            	//если мы его раньше не добавили
            	boolean f=false;
            	if (overdue.size()>0){
            	    for (int j=0;j<overdue.size();j++){
            		    if (overdue.get(j).equals(lstOvrd.get(0))){
            		      f=true;	
                          break;
            		    }
            	    }
               	} 
            	if (!f) {
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
        Partner parRStandart = refBooks.getPartner(Partner.RSTANDARD);
		
		//даты для выгрузки
		DateRange uploadDateRange=uploadService.getDatesForUpload(sendingDate,Partner.RSTANDARD,UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS);
		Date firstDate=DatesUtils.makeDate(2010, 1, 1);
		uploadDateRange.setFrom(firstDate);
		log.info("Выгружаем данные с "+uploadDateRange.getFrom()+" по "+uploadDateRange.getTo());
	    //выбрали заявки для выгрузки
		List<CreditRequestEntity> lstcre=uploadService.getListForUpload(uploadDateRange,creditIds);
		List<Object[]> lobj=new ArrayList<Object[]>(0);
		log.info("Всего записей для выгрузки "+lstcre.size());
		//есть записи
		if (lstcre.size()>0)
		{
			//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.RSTANDARD, UploadStatus.UPLOAD_CREDIT_CORRECT_ERRORS, 
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
			String st=MakeXML.createUploadForSendingToRStandard(uploadDateRange.getTo(), lobj, isWork?parRStandart.getCodeWork():parRStandart.getCodeTest(), 
					org.getName(), upl.getUploadId().toString(),parRStandart.getUploadVersion(),uploadDateRange.getFrom());
			
			log.info("Создали файл для выгрузки в Русский стандарт, кол-во записей "+lstcre.size());
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
	private List<CreditEntity> saveRSCredit(Node cd, CreditRequestEntity cre,PeopleMainEntity pplmain,
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
		    } else if (overdueDays>=1&&overdueDays<30){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		    } else if (overdueDays>=30&&overdueDays<60){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		    } else if (overdueDays>=60&&overdueDays<90){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		    } else if (overdueDays>=90){
		    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
		    } 
	    } else {
	    	crse.setOverduestateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.WITHOUT_OVERDUE));
	    }
	    //дата окончания по графику
	  	if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "EstimatedCloseDate"))){
	  	    crse.setCreditdataend(Convertor.toDate(XmlUtils.getAttrubuteValue(cd, "EstimatedCloseDate"), DatesUtils.FORMAT_ddMMYYYY));
	  	}
	  	
	    //смотрим платежи из истории
	    String paydisc="";
	    Double paysum=new Double(0);
	    ReferenceEntity overdue=null;
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
				                  overdue=refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE,XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID"));
				                  paydisc=XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID")+paydisc;
				                  
			            		}
			            	 } catch (Exception e) {
			            		 log.severe("Не удалось найти вид просрочки в справочнике "+XmlUtils.getAttrubuteValue(ndpay, "OverdueAmountDurationID"));
			            	 }
				          }
			         // crse.getHistorypays().add(crp);
				  }//end for
			  }//end есть платежи
		}//end есть история платежей
		
		//если есть последний просроченный статус в оплатах
		if (overdue!=null){
			crse.setOverduestateId(overdue);
		}
		
		//проверим на существование в БД такого же кредита
        if (creditList.size()>0){
        	for (CreditEntity credit:creditList){
        		try{
        		    if (creditService.isMatch(crse, credit)&&unique==1){
        		    	//если это кредит клиента, то запишем верификацию
        		    	if (clientCredit!=null&&credit.getPartnersId().getId()==Partner.CLIENT){
        		    		log.info("запишем системную верификацию РС по заявке "+cre.getId());
        		    		creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
        	            			credit, crse,VerificationConfig.MARK_FOR_BANK);
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
        	log.info("запишем системную верификацию РС по заявке "+cre.getId());
              	creditInfo.saveSystemVerification(verification, cre.getId(), pplmain.getId(), 
            			clientCredit, crse,VerificationConfig.MARK_FOR_BANK);
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
		    if (!XmlUtils.getAttrubuteValue(cd, "DebtCountFrom1To29").equals("0")){
		        crse.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_BEFORE_MONTH));
		    }
	    }
	    //просрочек до 60 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom30To59"))) {
		    crse.setDelay60(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom30To59")));
		    if (!XmlUtils.getAttrubuteValue(cd, "DebtCountFrom30To59").equals("0")){
		        crse.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_MONTH));
		    }
	    }
	    //просрочек до 90 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom60To89"))) {
		    crse.setDelay90(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom60To89")));
		    if (!XmlUtils.getAttrubuteValue(cd, "DebtCountFrom60To89").equals("0")){
		        crse.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_TWO_MONTH));
		    }
	    }
	    //просрочек до 120 дней
	    Integer delay=0;
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom90To119"))){
	    	delay=Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountFrom90To119"));
	    	if (!XmlUtils.getAttrubuteValue(cd, "DebtCountFrom90To119").equals("0")){
	    	    crse.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_THREE_MONTH));
	    	}
	    }
	    //просрочек более 120 дней
	    if (StringUtils.isNotEmpty(XmlUtils.getAttrubuteValue(cd, "DebtCountMore120"))){
		    crse.setDelaymore(Convertor.toInteger(XmlUtils.getAttrubuteValue(cd, "DebtCountMore120"))+Utils.defaultIntegerFromNull(delay));
		    if (!XmlUtils.getAttrubuteValue(cd, "DebtCountMore120").equals("0")){
		        crse.setWorstOverdueStateId(refBooks.findByCodeEntity(RefHeader.CREDIT_OVERDUE_STATE, BaseCredit.OVERDUE_FOUR_MONTH));
		    }
	    } else {
	    	crse.setDelaymore(Utils.defaultIntegerFromNull(delay));
	    }
	    //худший просроченный статус по текущему просроченному, если не было закрытых просрочек
	    if ((crse.getWorstOverdueStateId()==null&&crse.getOverduestateId()!=null)
				||(crse.getWorstOverdueStateId()!=null&&crse.getOverduestateId()!=null&&crse.getWorstOverdueStateId().getId()<crse.getOverduestateId().getId())){
			crse.setWorstOverdueStateId(crse.getOverduestateId());
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
	    crse.setPartnersId(refBooks.getPartnerEntity(Partner.RSTANDARD));
	    crse.setIsactive(ActiveStatus.ACTIVE);
	    
	    //выплаченная сумма
		crse.setCreditSumPaid(paysum);
		//платежная дисциплина
		crse.setPayDiscipline(Convertor.toLimitString(paydisc,200));
		creditDAO.saveCreditEntity(crse);
		creditList.add(crse);
		return creditList;
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity createDeleteFileForUpload(Date sendingDate,
			Integer creditId, Boolean isWork) throws KassaException {
		CreditEntity credit=creditDAO.getCreditEntity(creditId);
		if (credit!=null){
			//создаем новую запись в таблице загрузки
        	UploadingEntity upl=uploadService.newUploading(null, Partner.RSTANDARD, UploadStatus.UPLOAD_CREDIT_CREDITREQUEST, 
        			null,null, null, null, null, null, null, null, null, true);
		    PartnersEntity rs=refBooks.getPartnerEntity(Partner.RSTANDARD);	
		    //своя организация
		    OrganizationEntity org=orgService.getOrganizationActive();
		    String codeWork=isWork?rs.getCodework():rs.getCodetest();
		    String fname=DatesUtils.DATE_FORMAT_ddMMYYYY.format(sendingDate)+"-delete.xml";
		    String reqString=MakeXML.createDeleteRecordForSendingToRStandard(sendingDate, codeWork, 
				   org.getName(), upl.getUploadId().toString(), rs.getUploadVersion(), credit);	
		    //дописываем запись в таблицу загрузки
		    upl=uploadService.newUploading(upl, Partner.RSTANDARD,UploadStatus.UPLOAD_CREDIT_DELETE, 
    			fname,reqString, null, UploadStatus.UPLOAD_CREATE, null, null, null, null, null, false);
			log.info("Создали файл для аннулирования кредита "+creditId);			
		    return upl;
		} else {
			log.severe("Не найден кредит для аннулирования "+creditId);
		}
		return null;
	}
	
	
	
}

