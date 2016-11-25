package ru.simplgroupp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.Organization;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.PeopleMisc;
import ru.simplgroupp.transfer.PeoplePersonal;
import ru.simplgroupp.transfer.RefCreditRequestWay;
import ru.simplgroupp.transfer.Requests;

public class MakeXML {

	private final static String APPLICANT="01";
	private final static String EQUIFAX_VERSION="3.0";
	private final static String HEADER_TUTDF="TUTDF";
	
	/**
	 * пишем ветку с текстом, если поле не пустое
	 */
	public static void WriteNodeWithText(Element el,String nodename,String nodetext){
		if (StringUtils.isNotEmpty(nodetext) ) {
	          Element pc=el.addElement(nodename);
	          pc.addText(nodetext);
		}	
	}
	
	/**
	 * пишем обязательную ветку с текстом
	 */
	public static void WriteNodeWithMandatoryText(Element el,String nodename,String nodetext){
	
	    Element pc=el.addElement(nodename);
	    pc.addText(nodetext);
		
	}
	
	/**
	 * пишем ветку с атрибутом, если поле не пустое
	 */
	public static void WriteNodeWithAttributesValue(Element el,String nodename,String nodetype,String nodetext){
		if (StringUtils.isNotEmpty(nodetext) ) {
	      	  Element alv=el.addElement(nodename);
	          alv.addAttribute(nodetype,nodetext);
	          
		}
	}
		
	/**
	 * пишем ветку с атрибутом, если поле не пустое
	 */
	public static void WriteNodeWithAttributes(Element el,String nodetype,String nodetext){
		if (StringUtils.isNotEmpty(nodetext) ) {
	          el.addAttribute(nodetype,nodetext);
		}
	}	
	
	/**
	 * пишем ветку с атрибутом и значением, если поле не пустое
	 * 
     * @param el - элемент, к которому добавляем ноду
	 * @param nodename - название узла, который добавляем
	 * @param attrname - название атрибута, который добавляем
	 * @param attrtext - текст, котоый пишем в атрибут
	 * @param nodetext - текст, который пишем в ноду
	 */
	public static void WriteNodeWithAttributeAndValue(Element el,String nodename,String attrname,String attrtext,String nodetext){
		if (StringUtils.isNotEmpty(nodetext) ) {
			  WriteMandatoryNodeWithAttributeAndValue(el,nodename,attrname,attrtext,nodetext);
		}
	}	

	/**
	 * пишем обязательную ветку с атрибутом и значением
	 *
	 * @param el - элемент, к которому добавляем ноду
	 * @param nodename - название узла, который добавляем
	 * @param attrname - название атрибута, который добавляем
	 * @param attrtext - текст, котоый пишем в атрибут
	 * @param nodetext - текст, который пишем в ноду
	 */
	public static Element WriteMandatoryNodeWithAttributeAndValue(Element el,String nodename,String attrname,String attrtext,String nodetext){
		
			  Element pc=el.addElement(nodename);
			  pc.addAttribute(attrname,attrtext);
	          pc.addText(nodetext);
	          return pc;
	}	

	/**
	 * пишем обязательную ветку с атрибутом и значением
	 *
	 * @param el - элемент, к которому добавляем ноду
	 * @param nodename - название узла, который добавляем
	 * @param attrname - название атрибута, который добавляем
	 * @param attrtext - текст, котоый пишем в атрибут
	 * @param attrname1 - название атрибута, который добавляем
	 * @param attrtext1 - текст, котоый пишем в атрибут
	 * @param nodetext - текст, который пишем в ноду
	 */
	public static Element WriteMandatoryNodeWithTwoAttributesAndValue(Element el,String nodename,String attrname,String attrtext,String attrname1,String attrtext1,String nodetext){
		
		  Element pc=el.addElement(nodename);
		  pc.addAttribute(attrname,attrtext);
		  pc.addAttribute(attrname1,attrtext1);
          pc.addText(nodetext);
          return pc;
    }	
	/**
	 * пишем обязательную ветку с атрибутом и значением
	 *
	 * @param el - элемент, к которому добавляем ноду
	 * @param nodename - название узла, который добавляем
	 * @param attrname - название атрибута, который добавляем
	 * @param attrtext - текст, котоый пишем в атрибут
	 */
	public static Element WriteMandatoryNodeWithAttribute(Element el,String nodename,String attrname,String attrtext){
		
			  Element pc=el.addElement(nodename);
			  pc.addAttribute(attrname,attrtext);
			  return pc;
	}	
	
	/**
	 * пишем xml в массив байтов
	 */
	public static byte[] writeXMLToBytes(Document doc) throws IOException {
	
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(fos,format);
		byte[] res = null;
		try {
			writer.write(doc);
			writer.flush();
			res = fos.toByteArray();
		} finally {
			writer.close();
		}
		return res;
	}

	/**
	 * 
	 * пишем ПД для запроса в РС
	 * @param rootElement - корневой элемент
	 * @param peoplePersonal - ПД
	 * @param passport - паспорт
	 * @param peopleMain - человек
	 * @return
	 */
	private static Element writePersonalDataForXmlRS(Element rootElement,
			PeoplePersonalEntity peoplePersonal,DocumentEntity passport,
			PeopleMainEntity peopleMain){
		
		//субъект
		  Element subj=rootElement.addElement("NaturalPersonSubject");
		  subj.addAttribute("Gender", peoplePersonal.getGender().getCodeinteger().toString());
		  subj.addAttribute("BirthDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(peoplePersonal.getBirthdate()));
		  subj.addAttribute("BirthPlace",peoplePersonal.getBirthplace());
		  WriteNodeWithAttributes(subj,"IndividualTaxpayerNumber", peopleMain.getInn());
		  WriteNodeWithAttributes(subj,"PensionNumber", peopleMain.getSnils());
		  
		  //write personal data
		  Element fio=subj.addElement("Title");
		  fio.addAttribute("LastName", peoplePersonal.getSurname());
		  fio.addAttribute("FirstName", peoplePersonal.getName());
		  WriteNodeWithAttributes(fio,"MiddleName", peoplePersonal.getMidname());
		  
		  //write passport
		  Element dc=subj.addElement("IdentificationDocument");
		  dc.addAttribute("DocTypeID", String.valueOf(Documents.PASSPORT_RF_EQUIFAX_RS));
		  dc.addAttribute("DocSer", passport.getSeries());
		  dc.addAttribute("DocNum", passport.getNumber());
		  dc.addAttribute("DocDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()));
		  if (StringUtils.isNotEmpty(passport.getDocorgcode())){
		    dc.addAttribute("DocDelivInstCode",Convertor.fromMask(passport.getDocorgcode()));
		  }
		  return subj;
	}
	
	/**
	 * пишем согласие для запроса в РС
	 * @param rootElement - корневой элемент
	 * @param creditRequest - заявка
	 * @return
	 */
	private static Element writeReasonAndConsentDataForXmlRS(Element rootElement,CreditRequestEntity creditRequest){
	   Element rsn=rootElement.addElement("Reason");
	   //сумма кредита
	   rsn.addAttribute("CustomerClaimCreditLimit",Convertor.toDecimalString(CalcUtils.dformat, creditRequest.getCreditsum()));
	   //валюта кредита
	   rsn.addAttribute("CurrencyID",String.valueOf(BaseCredit.CURRENCY_RUR_ID));
	   //вид кредита
	   rsn.addAttribute("ID",String.valueOf(BaseCredit.COMMON_CREDIT));
	   //название кредита
	   rsn.addAttribute("Text","микрозайм");
	   //цель запроса
	   WriteMandatoryNodeWithAttributeAndValue(rootElement,"RequestPurpose","type","10","иная цель");
	   //согласие
	   Element cns=rootElement.addElement("Consent");
	   cns.addAttribute("type", "1");
	   cns.addAttribute("IssueDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDatecontest()));
	   cns.addAttribute("ExpiryDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addYears(creditRequest.getDatecontest(),1)));
	   return rsn;
	}
	
	/**
	 * пишем адрес для запроса в РС
	 * @param rootElement - корневой элемент
	 * @param addressType - вид адреса
	 * @param address - адрес
	 * @return
	 */
	private static Element writeAddressDataForXmlRS(Element rootElement,int addressType,
			AddressEntity address){
		  Element addrreg=rootElement.addElement("Address");
		  Element cap=addrreg.addElement("Caption");
		  cap.addAttribute("ID", String.valueOf(addressType));
		  //регион
		  Element reg=addrreg.addElement("Region");
		  if (address.getRegionShort()==null){
			  reg.addAttribute("ID","00");  
		  } else {
		      reg.addAttribute("ID", address.getRegionShort().getCodereg());
		  }
		  //сельский район
		  if (StringUtils.isNotEmpty(address.getAreaName())) {
		    Element dist=addrreg.addElement("District");
		    dist.addAttribute("Caption", address.getAreaName());
		    dist.addAttribute("Ext", address.getAreaExt());
		  }
		  //город
		  if (StringUtils.isNotEmpty(address.getCityName())) {
		    Element ct=addrreg.addElement("City");
		    ct.addAttribute("Caption", address.getCityName());
		    ct.addAttribute("Ext", address.getCityExt());
		  }
		  //село
		  if (StringUtils.isNotEmpty(address.getPlaceName())) {
		    Element vil=addrreg.addElement("Village");
		    vil.addAttribute("Caption", address.getPlaceName());
		    vil.addAttribute("Ext", address.getPlaceExt());
		  }
		  //улица, дом-квартира
		  Element stradr=addrreg.addElement("StreetAddress");
		  WriteNodeWithAttributes(stradr, "HouseNum", address.getHouse());
		  WriteNodeWithAttributes(stradr, "FlatNum", address.getFlat());
		  WriteNodeWithAttributes(stradr, "BuildingNum", address.getCorpus());
		  Element strt=stradr.addElement("Street");
		  strt.addAttribute("Caption", StringUtils.isEmpty(address.getStreetName())?"неизвестная":address.getStreetName());
		  strt.addAttribute("Ext", StringUtils.isEmpty(address.getStreetName())?"ул":address.getStreetExt());
		  return addrreg;
		  
	}
	
	/**
	 * пишем телефон для запроса в РС
	 * @param rootElement - корневой элемент
	 * @param phone - телефон
	 * @return
	 */
	private static Element writePhoneDataForXmlRS(Element rootElement,PeopleContactEntity phone){
		 Element phn=rootElement.addElement("Phone");  
	     phn.addAttribute("ID", String.valueOf(PeopleContact.CELL_PHONE_RS));
	     //номер телефона без кода страны
	     phn.addAttribute("Number", phone.getValue().substring(1));
	     return phn;
	}
	
	/**
	 * пишем что хотим запросить у РС
	 * @param rootElement - корневой элемент
	 * @param creditReportDetalization - детализация КИ, либо без нее
	 * @param requestPayment - запрашивать историю платежей за телефон
	 * @param requestFMS - запрашивать действительность паспорта
	 * @param requestFSSP - запрашивать сервис ФССП
	 * @param requestVerify - запрашивать верификацию
	 * @param requestAntifrod - запрашивать антифрод
	 * @param requestFNS - запрашивать долги ФНС
	 * @param requestScoring - запрашивать скоринг
	 * @return
	 */
	private static Element writeResponseAttributesForXmlRs(Element rootElement,Integer creditReportDetalization,
			boolean requestPayment,boolean requestFMS,boolean requestFSSP,boolean requestVerify,
			boolean requestAntifrod,boolean requestFNS,boolean requestScoring){
		  Element resp=rootElement.addElement("ResponseCapabilities");
		  //максимально детализированный отчет, либо не берем отчет, зависит от настроек
		  resp.addAttribute("CreditReportDetalization",creditReportDetalization.toString());
		  //будем ли смотреть, делали ли по человеку запросы
          //если детальный отчет, то смотрим 20 запросов, иначе не смотрим вообще
		  if (creditReportDetalization==Requests.RS_REPORT_DETAILS){
			  resp.addAttribute("PreviousRequestsCountLimit", "20");
		  } else {
		      resp.addAttribute("PreviousRequestsCountLimit", "-1");
		  }
		  
		  //платежная история, в тестах нет
		  if (requestPayment) {
		      resp.addAttribute("getPaymentHistory", String.valueOf(Requests.RS_PAYMENT_INFO));
		  }
		  //фмс, в тестах нет
		  if (requestFMS) {
		      resp.addAttribute("getMigrationServiceDocumentCheck", "true");
		  }
		  //фссп, в тестах нет
		  if (requestFSSP) {
		      resp.addAttribute("getBailiffsServiceDebtInfo", "true");
		  }
		  //верификация
		  if (requestVerify) {
		      resp.addAttribute("getVerificationService", "true");
		  }
		  //анти-фрод
		  if (requestAntifrod) {
		      resp.addAttribute("getFraudInfo", "true");
		  }
		  //фнс, долги, в тестах нет
		  if (requestFNS) {
		      resp.addAttribute("getTaxServiceDebtInfo", "true");
		  }
		  //скоринг
		  if (requestScoring) {
		    //  resp.addAttribute("getScoringParameters", "true");
		      resp.addAttribute("buildScoring", "7");
		  }
		  return resp;
	}
	
	/**
	 * пишем данные заявки для запроса в ОКБ
	 * @param root - корневой элемент
	 * @param creditRequest - заявка
	 * @param numberFunction - номер функции
	 */
	private static void writeReasonDataForXmlOkb(Element root,CreditRequestEntity creditRequest,
			Integer numberFunction){
		  WriteNodeWithAttributes(root, "n", "EnquiryRequest");
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","accountClass",String.valueOf(BaseCredit.CREDIT_OWNER));
	      //пишем в зависимости от вида запроса
	      if (numberFunction==Requests.FUNCTION_CAIS_EXTENDED||numberFunction==Requests.FUNCTION_CAIS_SCORING){
	          //справочник для причин запроса
	          WriteMandatoryNodeWithAttributeAndValue(root,"a","n","reason",Requests.REASON_CAIS_APPLICATION);
	      } else {
	    	 //справочник для причин запроса
	          WriteMandatoryNodeWithAttributeAndValue(root,"a","n","reason",Requests.REASON_CAIS); 
	      }
	      //вид финансирования (кредита)
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","financeType",BaseCredit.COMMON_CREDIT_CODE);
	      //если это расширенный запрос, то передаем номер и дату заявки
	      if (numberFunction==Requests.FUNCTION_CAIS_EXTENDED||numberFunction==Requests.FUNCTION_CAIS_SCORING){
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","applicationNumber",creditRequest.getUniquenomer()); 
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","applicationDate",DatesUtils.DATE_FORMAT_YYYYMMdd.format(creditRequest.getDatecontest()));  
	      }
	      //валюта
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","currency",BaseCredit.CURRENCY_RUB);
	      //сумма кредита
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","amountOfFinance",CalcUtils.dformat_XX.format(creditRequest.getCreditsum()));
	      //непонятно, нужно ли это поле, и зачем
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","instalmentAmount",CalcUtils.dformat_XX.format(creditRequest.getCreditsum()));
	      //частота платежей, ставим помесячную, других вариантов нет
	      WriteMandatoryNodeWithAttributeAndValue(root,"a","n","paymentFrequency",BaseCredit.FREQ_PAYMENT_MONTH);    
	      //если это расширенный запрос, то передаем как подавали заявление и код центробанка
	      if (numberFunction==Requests.FUNCTION_CAIS_EXTENDED||numberFunction==Requests.FUNCTION_CAIS_SCORING){
	    	  //срок действия соглашения
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","duration",creditRequest.getCreditdays().toString());
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","durationUnits","01");
	    	  //как подаем заявку
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","applicationChannel",
	    			  "0"+creditRequest.getWayId().getCodeinteger().toString());
	    	  //финансирование по ЦБ
	    	  WriteMandatoryNodeWithAttributeAndValue(root,"a","n","cbrFinanceType",Requests.REASON_CBR);
	      }
	}
	
	/**
	 * пишем ПД для запроса в ОКБ
	 * @param root - корневой элемент
	 * @param peoplePersonal - ПД
	 * @param passport - паспорт
	 * @param phone - телефон
	 * @param peopleMain - человек
	 * @param creditRequest - заявка
	 * @param employ - занятость
	 * @return
	 */
	private static Element writePersonalDataForXmlOkb(Element root,PeoplePersonalEntity peoplePersonal,
			DocumentEntity passport,PeopleContactEntity phone,PeopleMainEntity peopleMain,
			CreditRequestEntity creditRequest,EmploymentEntity employ){
		  Element s=root.addElement("s");
	      //физическое лицо
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","applicantType",APPLICANT);
	      //фио
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","name1",peoplePersonal.getName());
	      WriteNodeWithAttributeAndValue(s,"a","n","name2",peoplePersonal.getMidname());
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","surname",peoplePersonal.getSurname());
	      //девичья фамилия, оставляем пустой
	      if (peoplePersonal.getGender().getCodeinteger()==PeoplePersonal.GENDER_WOMAN) {  
	    	  WriteMandatoryNodeWithAttributeAndValue(s,"a","n","aliasName","-");   
	      }
	      //пол
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","sex",peoplePersonal.getGender().getCodeinteger().toString());
	      //дата и место рождения
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","dateOfBirth",DatesUtils.DATE_FORMAT_YYYYMMdd.format(peoplePersonal.getBirthdate()));
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","placeOfBirth",peoplePersonal.getBirthplace());
	      //национальность
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","nationality","RU");
	      //согласие дано
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","consentFlag","0");
	      //дата согласия
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","dateConsentGiven",DatesUtils.DATE_FORMAT_YYYYMMdd.format(creditRequest.getDatecontest()));  
	        
	      //write passport info
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIDType",Documents.PASSPORT_RF_OKB);    
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryID",passport.getSeries().trim()+passport.getNumber().trim());    
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIDIssueDate",DatesUtils.DATE_FORMAT_YYYYMMdd.format(passport.getDocdate()));    
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIDIssuePlace",Convertor.fromMask(passport.getDocorgcode()));    
	      WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIDAuthority",Convertor.toLimitString(passport.getDocorg(),30));    
	            
	      //write people id, commented for tests
	      //WriteNodeWithAttributeAndValue(s,"a","n","pensionNumber",peopleMain.getSnils());    
	      //write people income,commented for tests
	      if (employ!=null){
	          WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncome",CalcUtils.dformat_XX.format(employ.getSalary())); 
	          if (employ.getDurationId()!=null){
	              WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncomeFreq",employ.getDurationId().getCodeinteger().toString()); 
	          } else {
	        	  WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncomeFreq","3"); 
	          }
	          WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncomeFlag","1"); 
	      } else {
	    	  //только для тестов, потом убрать
	    	  WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncome","15000");    
	          WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncomeFreq","3"); 
	          WriteMandatoryNodeWithAttributeAndValue(s,"a","n","primaryIncomeFlag","1"); 
	      }
	     
	          
	      if (phone!=null) {
	          WriteNodeWithAttributeAndValue(s,"a","n","mobileTelNbr",phone.getValue());
	      }
	      return s;
	}
	
	/**
	 * пишем адрес для запроса в ОКБ
	 * @param root - корневой элемент
	 * @param addressType - вид адреса
	 * @param address - адрес
	 * @return
	 */
	private static Element writeAddressDataForXmlOkb(Element root,int addressType,AddressEntity address){
		 
	      Element sadr=root.addElement("s");
	      
	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","addressFlag","0"); 
	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","addressType",String.valueOf(addressType)); 
	      
	      WriteNodeWithAttributeAndValue(sadr,"a","n","flatNbr",address.getFlat()); 
	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","houseNbr",address.getHouse()); 
	      WriteNodeWithAttributeAndValue(sadr,"a","n","buildingNbr",address.getBuilding()); 
	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line1",StringUtils.isEmpty(address.getCorpus())?"-":address.getCorpus()); 
	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line2",StringUtils.isNotEmpty(address.getStreetName())?address.getStreetName():"неизвестно"); 
	    
	      //сельский район, город, село
	      if (StringUtils.isNotEmpty(address.getAreaName())) {
	    	  if (StringUtils.isNotEmpty(address.getPlaceName())){
	    	      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3",address.getPlaceName()); 
	    	  } else if (StringUtils.isNotEmpty(address.getCityName())){
	    		  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3",address.getCityName());   
	    	  } else {
	    		  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3","неизвестно");   
	    	  }
	    	  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line4",address.getAreaName()); 
	      } else {
	    	  if (StringUtils.isNotEmpty(address.getCityName())){
	    		  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3",address.getCityName()); 
	    	  } else {
	    		  //если это Москва или Питер
	    		  if (address.getRegionName().indexOf(" ")>0) {
	    		      WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3",address.getRegionName().substring(0, address.getRegionName().indexOf(" ")).trim());
	    		  } else {
	    			  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","line3",address.getRegionName());  
	    		  }
	    	  }
	    	  
	    	  WriteNodeWithAttributeAndValue(sadr,"a","n","line4",address.getDistrictName()); 
	      }
	      //регион
	      if (address.getRegionShort()==null){
	    	  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","regionCode","00");  
	      } else {
	          WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","regionCode",address.getRegionShort().getCodereg()); 
	      }
	      WriteNodeWithAttributeAndValue(sadr,"a","n","postcode",address.getIndex()); 
		  WriteMandatoryNodeWithAttributeAndValue(sadr,"a","n","country","RU"); 
		  return sadr;
	}
	
	/**
	 * пишем ПД для запроса в Эквифакс
	 * @param rootElement - корневой элемент
	 * @param peoplePersonal - ПД
	 * @param passport - паспорт
	 * @param peopleMain - человек
	 * @return
	 */
	private static Element writePersonalDataForXmlEquifax(Element rootElement,
			PeoplePersonalEntity peoplePersonal,DocumentEntity passport,
			PeopleMainEntity peopleMain){
		//write personal data
        Element priv=rootElement.addElement("private");
        WriteNodeWithMandatoryText(priv,"lastname",peoplePersonal.getSurname());
        WriteNodeWithMandatoryText(priv,"firstname",peoplePersonal.getName());
        WriteNodeWithText(priv,"middlename",peoplePersonal.getMidname());
        WriteNodeWithText(priv,"gender",peoplePersonal.getGender().getCodeinteger().toString());
        WriteNodeWithMandatoryText(priv,"birthday",DatesUtils.DATE_FORMAT_ddMMYYYY.format(peoplePersonal.getBirthdate()));
        WriteNodeWithMandatoryText(priv,"birthplace",peoplePersonal.getBirthplace());
        //write passport info
        Element pasp=priv.addElement("doc");
        WriteNodeWithMandatoryText(pasp,"doctype",String.valueOf(Documents.PASSPORT_RF_EQUIFAX_RS));
        WriteNodeWithMandatoryText(pasp,"docno",passport.getSeries().trim()+passport.getNumber().trim());
        WriteNodeWithMandatoryText(pasp,"docdate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocdate()));
        WriteNodeWithText(pasp,"docenddate",passport.getDocenddate()==null?"":DatesUtils.DATE_FORMAT_ddMMYYYY.format(passport.getDocenddate()));
        WriteNodeWithMandatoryText(pasp,"docplace",passport.getDocorg());
        //write people id
        //WriteNodeWithText(priv,"inn",peopleMain.getInn());
        //WriteNodeWithText(priv,"pfno",peopleMain.getSnils());
        return priv;
	}
		
	/**
	 * пишем данные заявки для запроса в Эквифакс
	 * @param rootElement - корневой элемент
	 * @param creditRequest - заявка
	 * @param peoplePersonal - ПД
	 * @param peopleMisc - доп.данные
	 * @param phone - мобильный телефон
	 * @param email - email
	 * @return
	 */
	private static Element writeApplicationInfoForXmlEquifax(Element rootElement,
			CreditRequestEntity creditRequest,PeoplePersonalEntity peoplePersonal,
			PeopleMiscEntity peopleMisc,PeopleContactEntity phone,PeopleContactEntity email){
		
		 Element appl=rootElement.addElement("application");
         WriteNodeWithMandatoryText(appl,"consent","1");
         WriteNodeWithMandatoryText(appl,"consentdate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDatecontest()));
        
         WriteNodeWithMandatoryText(appl,"cred_type",String.valueOf(BaseCredit.MICRO_CREDIT));
         WriteNodeWithMandatoryText(appl,"cred_currency",BaseCredit.CURRENCY_RUR);
         WriteNodeWithMandatoryText(appl,"cred_sum",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
         //срок действия кредита в месяцах
         if (creditRequest.getCreditdays()<=31){
             WriteNodeWithMandatoryText(appl,"cred_duration","1");
         } else {
        	 WriteNodeWithMandatoryText(appl,"cred_duration",String.valueOf(Math.round(creditRequest.getCreditdays()/30)));
         }
         //write misc info, commented for tests
         Element miscpriv=appl.addElement("private");
         WriteNodeWithText(miscpriv,"citizenship",peoplePersonal.getCitizen().getCode());
         //семейное положение, кол-во детей
         if (peopleMisc!=null&&peopleMisc.getMarriageId()!=null){
             WriteNodeWithText(miscpriv,"marriage",peopleMisc.getMarriageId().getCodeinteger().toString());
             if (peopleMisc.getChildren()!=null){
                 WriteNodeWithText(miscpriv,"dependants_bel18",peopleMisc.getChildren().toString());
             }
         }
         //телефон
         /*if (phone!=null) {
             WriteNodeWithText(miscpriv,"phone_mobile",phone.getValue());
         }
         //email
         if (email!=null) {
             WriteNodeWithText(miscpriv,"email",email.getValue());
         }*/
         return appl;
	}
	
	/**
	 * пишем адрес для запроса в Эквифакс
	 * @param rootElement - корневой элемент
	 * @param nameElement - название тега адреса
	 * @param address - адрес
	 * @return
	 */
	private static Element writeAddressForEquifaxRequest(Element rootElement,String nameElement,
			AddressEntity address){
		 Element addrreg=rootElement.addElement(nameElement);
         WriteNodeWithMandatoryText(addrreg,"index",StringUtils.isEmpty(address.getIndex())?"000000":address.getIndex());
         WriteNodeWithMandatoryText(addrreg,"country","RU");
         //регион
         if (address.getRegionShort()==null){
       	    WriteNodeWithMandatoryText(addrreg,"region","00");
         } else if (address.getRegionShort().getCode().contains("-")){
        	WriteNodeWithMandatoryText(addrreg,"region",
        			address.getRegionShort().getCode().substring(0, address.getRegionShort().getCode().indexOf("-")));
         } else {
             WriteNodeWithMandatoryText(addrreg,"region",address.getRegionShort().getCode());
         }
         //сельский район
         if (StringUtils.isNotEmpty(address.getArea())){
       	  if (StringUtils.isNotEmpty(address.getPlaceName())){
       	      WriteNodeWithMandatoryText(addrreg,"city",address.getPlaceName());
       	  } else if (StringUtils.isNotEmpty(address.getCityName())) {
       		  WriteNodeWithMandatoryText(addrreg,"city",address.getCityName()); 
       	  } else {
       		  WriteNodeWithMandatoryText(addrreg,"city","неизвестно"); 
       	  }
             WriteNodeWithText(addrreg,"district",address.getAreaName());
         }  else {
       	  if (StringUtils.isNotEmpty(address.getCityName())){
       	      WriteNodeWithMandatoryText(addrreg,"city",address.getCityName());
       	  } else {
       		  //если это Москва или Питер
       		  if (address.getRegionName().indexOf(" ")>0) { 
       		      WriteNodeWithMandatoryText(addrreg,"city",address.getRegionName().substring(0, address.getRegionName().indexOf(" ")).trim());
       		  } else {
       			  WriteNodeWithMandatoryText(addrreg,"city",address.getRegionName());
       		  }
       	  }
             WriteNodeWithText(addrreg,"district",address.getDistrictName());
         }
         //улица
         WriteNodeWithMandatoryText(addrreg,"street",StringUtils.isNotEmpty(address.getStreetName())?address.getStreetName():"неизвестно");
         String hnum=address.getHouse().trim();
         if (StringUtils.isNotEmpty(address.getBuilding())&&StringUtils.isNotEmpty(address.getCorpus())) {
       	     hnum=hnum+"/"+address.getCorpus().trim()+"/"+address.getBuilding().trim();
         }
         if (StringUtils.isNotEmpty(address.getBuilding())&&StringUtils.isEmpty(address.getCorpus())) {
       	     hnum=hnum+"//"+address.getBuilding().trim();
         }
         if (StringUtils.isNotEmpty(address.getCorpus())&&StringUtils.isEmpty(address.getBuilding())) {
       	     hnum=hnum+"/"+address.getCorpus().trim(); 
         }
         //дом
         WriteNodeWithMandatoryText(addrreg,"house",hnum);
         //квартира
         WriteNodeWithText(addrreg,"flat",address.getFlat());
         return addrreg;
	}
	
	/**
	 * 	
	 * @param lobj
	 * 0 - PeopleMainEntity pplmain, 1-PeoplePersonalEntity pplper, 2 - PeopleMiscEntity pplmisc,
	 * 3- PeopleContactEntity pplcont, 4- DocumentEntity dc, 5 - AddressEntity adrreg, 6 - AddressEntity adrfact,
	 * 7 - EmploymentEntity employ, 8 - CreditRequestEntity cre, 9 - RequestsEntity req, 10 - PartnersEntity par
	 * @param partnerId - Id партнера
	 * @param packageId - № пакета
	 * @return
	 */
	public static String createPacketXMLforEquifax(List<Object[]> lobj,String partnerId,
			String packageId,String reportId,String version) {
		  Document document = DocumentHelper.createDocument();
		  document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
		  //write service info
          Element root = document.addElement( "bki_request" );
         
          root.addAttribute("version",version);
          root.addAttribute("partnerid",partnerId);
          
          //write attributes for package request
          if (lobj.size()>1) {
        	  root.addAttribute("package_id", packageId);
        	  root.addAttribute("package_rec_count",new Integer(lobj.size()).toString());
          }
          //write records for package
          for (Object[] row: lobj){
        	  
        	  Element rq=root.addElement("request");
              rq.addAttribute("num",((RequestsEntity) row[9]).getRequestquid());
              
              //персональные данные
              writePersonalDataForXmlEquifax(rq, ((PeoplePersonalEntity) row[1]),
            		  ((DocumentEntity) row[4]), ((PeopleMainEntity) row[0]));
              //заявка и доп.данные
              writeApplicationInfoForXmlEquifax(rq,((CreditRequestEntity) row[8]),
            		  ((PeoplePersonalEntity) row[1]),((PeopleMiscEntity) row[2]),
            		  ((PeopleContactEntity) row[11]),((PeopleContactEntity) row[12]));
             //адрес регистрации              
             writeAddressForEquifaxRequest(rq,"addr_reg", ((AddressEntity) row[5]));
             //адрес проживания              
             writeAddressForEquifaxRequest(rq,"addr_fact", ((AddressEntity) row[6]));
             //код отчета
             WriteNodeWithMandatoryText(rq,"type",reportId);
          }

          //write document to string 
          return document.asXML();
	}
	
  /**
  *
  * @param pplper - ПД
  * @param pplcont - контакт человека
  * @param dc - атрибуты документа, серия и номер передаются одним полем без пробелов
  * @param pplmisc - доп.данные по человеку
  * @param email - email
  * @param adrreg - атрибуты адреса регистрации
  * @param adrfact - атрибуты адреса проживания
  * @param employ - атрибуты занятости
  * @param cre - заявка на кредит
  * @param req - запрос
  * @param partnerId - id партнера
  * @param reportId - id отчета
  * @param version - версия запроса
  */
  public static String createXMLforEquifax(PeopleMainEntity pplmain,PeoplePersonalEntity pplper,
	   PeopleMiscEntity pplmisc, PeopleContactEntity pplcont, PeopleContactEntity email,
	   DocumentEntity dc, AddressEntity adrreg, AddressEntity adrfact, EmploymentEntity employ,
	   CreditRequestEntity cre, RequestsEntity req, String partnerId, String reportId,
	   String version) {
		  Document document = DocumentHelper.createDocument();
		  document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
		  //write service info
          Element root = document.addElement( "bki_request" );
          if (StringUtils.isNotEmpty(version)){
              root.addAttribute("version",version);
          } else {
              root.addAttribute("version",EQUIFAX_VERSION);
          }
          root.addAttribute("partnerid",partnerId);
          Element rq=root.addElement("request");
          rq.addAttribute("num", req.getRequestquid());
          
          //пишем ПД
          writePersonalDataForXmlEquifax(rq, pplper, dc, pplmain);
                   
          //пишем данные заявки
          writeApplicationInfoForXmlEquifax(rq,cre,pplper,pplmisc,pplcont,email);
              
          //адрес регистрации              
          writeAddressForEquifaxRequest(rq,"addr_reg", adrreg);
          
          //адрес проживания              
          writeAddressForEquifaxRequest(rq,"addr_fact", adrfact);
                
          //пишем groupid из partners
          WriteNodeWithMandatoryText(rq,"type",reportId);
          
          //write document to string 
          return document.asXML();
          
	}

/**
 * создаем xml с соответствующим кодированием
 * @param encoding кодировка
 */
public static Document createXMLDocument(String encoding){
	 Document document = DocumentHelper.createDocument();
	 document.setXMLEncoding(encoding);
	 return document;
}

/**
* 
* @param pplper - ПД
* @param dc - атрибуты документа, серия и номер передаются одним полем без пробелов
* @param adrreg - атрибуты адреса регистрации
* @param adrfact - атрибуты адреса проживания
* @param pplcont - контакт человека
* @param cre - заявка на кредит
* @param employ - атрибуты занятости
* @param req - запрос
*/
public static String createXMLforOKBCais( PeopleMainEntity pplmain,
  PeoplePersonalEntity pplper, PeopleMiscEntity pplmisc, PeopleContactEntity pplcont,
  DocumentEntity dc, AddressEntity adrreg, AddressEntity adrfact,
  EmploymentEntity employ, CreditRequestEntity cre, RequestsEntity req,
  Integer numberFunction) {
	
	  Document document = DocumentHelper.createDocument();
	  document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
	  //write service info
      Element root=document.addElement("s");
      //пишем данные заявки
      writeReasonDataForXmlOkb(root,cre,numberFunction);
         
      //write personal data
      Element rq=root.addElement("c");
      WriteNodeWithAttributes(rq, "n", "Consumer");
      Element s=writePersonalDataForXmlOkb(rq,pplper,	dc,pplcont,pplmain,	cre,employ);
         
      //пишем адреса 
      Element adr=s.addElement("c");
      WriteNodeWithAttributes(adr, "n", "Address");
      //адрес проживания
      writeAddressDataForXmlOkb(adr,BaseAddress.RESIDENT_ADDRESS_OKB,adrfact);
      //адрес регистрации
      writeAddressDataForXmlOkb(adr,BaseAddress.REGISTER_ADDRESS_OKB,adrreg);
      
      //write document to string 
	  String stXml=document.asXML();
	  stXml=stXml.substring(stXml.indexOf("1251")+8); 
      return stXml;
}

/**
 * строка для запроса в ОКБ
 * @param partnerId - партнер
 * @param groupId - группа
 * @param login - логин
 * @param password - пароль
 * @param xml - строка запроса
 */
public static String createEncodedStringForCais( String partnerId, String groupId, String login,String password,String function,String xml){
	return "Subscriber="+partnerId+"&Group="+groupId+"&User="+login+"&Password="+password+"&Function="+function+"&Request="+Convertor.toEscape(xml);
}

/**
 * @param pplmain - заголовок человека
 * @param pplper - ФИО
 * @param pplmisc - доп.данные
 * @param pplcont - контакт человека
 * @param dcc - атрибуты документа, серия и номер передаются одним полем без пробелов
 * @param cre - кредитная заявка
 * @param addr - атрибуты адреса регистрации
 * @param addrfact - атрибуты адреса проживания
 * @param partnerId - id партнера в Русском стандарте
 * @param nameOrg - название организации
 * @param requestPayment - запрашиваем платежи за телефон
 * @param requestFMS - запрашиваем статус паспорта ФМС
 * @param requestFSSP - запрашиваем долги ФССП
 * @param requestFNS - запрашиваем долги ФНС
 * @param requestVerify - запрашиваем верификацию
 * @param requestScoring - запрашиваем скоринг
 * @param version - версия запроса
 * @param id - идентификатор запроса
 * @return
 */
public static String createXMLforRStandard( PeopleMainEntity pplmain,
  PeoplePersonalEntity pplper, PeopleMiscEntity pplmisc, PeopleContactEntity pplcont,
  DocumentEntity dcc, CreditRequestEntity cre, AddressEntity addr,AddressEntity addrfact,
  String partnerId,String nameOrg,Boolean requestPayment,Boolean requestFMS,
  Boolean requestFSSP,Boolean requestFNS, Boolean requestVerify,
  Boolean requestScoring,Boolean requestAntifrod, String version,String id,
  Integer creditReportDetalization) {
	
  Document document = DocumentHelper.createDocument();
  document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
  //write service info
  Element root=document.addElement("REQUEST");
  //версия для схемы запроса
  if (StringUtils.isNotEmpty(version)){
      root.addAttribute("version", version);
  }
  Element hdr=root.addElement("HEADER");
  
  //Id партнера
  //WriteNodeWithMandatoryText(hdr,"UserID",partnerId);
  WriteMandatoryNodeWithAttributeAndValue(hdr,"UserID","UserType","1",partnerId);
  //название организации
  WriteNodeWithMandatoryText(hdr,"UserDescription",nameOrg);
  //тело запроса
  
  Element bd=root.addElement("BODY");
  Element reqa=WriteMandatoryNodeWithAttribute(bd,"RequestAttributes","id", id);
  //пишем, что запрашиваем
  writeResponseAttributesForXmlRs(reqa,creditReportDetalization,requestPayment,requestFMS,
		  requestFSSP,requestVerify,requestAntifrod,requestFNS,requestScoring);
   
  //пишем цель запроса и согласие субъекта
  writeReasonAndConsentDataForXmlRS(reqa,cre);
  //пишем ПД 
  Element subj=writePersonalDataForXmlRS(bd,pplper,dcc,	pplmain);
   
  //write address registration
  writeAddressDataForXmlRS(subj,BaseAddress.REGISTER_ADDRESS_RS, addr);

  //write address resident if it's not empty
  if (addrfact!=null){
	  writeAddressDataForXmlRS(subj,BaseAddress.RESIDENT_ADDRESS_RS, addrfact);
  }
  
  //write phone for history
  if (requestPayment||requestVerify) {
	  writePhoneDataForXmlRS(subj,pplcont);
  }
  
  //write document to string 
  return document.asXML();
  
}
	   	
	
	/**
	 * пишем записи для выдачи в Equifax 
	 * информационная часть по кредитным заявкам
	 * @param row - массив с данными
	 * 0 - № записи
	 * 1 - peopleMain
	 * 2 - peoplePersonal ПД
	 * 3 - peopleMisc - дополнительные данные человека
	 * 4 - document атрибуты документа, серия и номер передаются одним полем без пробелов
	 * 5 - address атрибуты адреса регистрации
	 * 6 - address атрибуты адреса проживания
	 * 7 - creditRequest - заявка на кредит
	 * 8 - credit - кредит
	 * 9 - телефон человека
	 * @param sendingDate - дата выгрузки
	 * @param document - xml document
	 */
	public static void createUploadRecordForSendingToEquifaxCreditRequest(Date sendingDate, 
			Object[] row,Document document)  {
			
		Element root = document.getRootElement();
   	    Element info=root.addElement("info");
		//номер записи
		info.addAttribute("recnumber", ((Integer) row[0]).toString());
		
		//пишем титульную часть
		createTitlePartForEquifax(info,((PeopleMainEntity) row[1]),((PeoplePersonalEntity) row[2]),
			    ((DocumentEntity) row[4]),((AddressEntity) row[5]),((AddressEntity) row[6]),
			    ((CreditRequestEntity) row[7]), ((PeopleContactEntity) row[9]),((PeopleMiscEntity) row[3]),false);
	        
        //пишем заявку
        Element infopart=info.addElement("information_part");
        //пишем инфо-часть
        createInfoPartForEquifax(infopart,((CreditRequestEntity) row[7]),((AddressEntity) row[5]));
        
        //если кредит есть
        if (((CreditRequestEntity) row[7]).getAcceptedcreditId()!=null&&((CreditRequestEntity) row[7]).getStatusId().getId()!=CreditStatus.CLIENT_REFUSE){
           createCreditPartCRForEquifax(infopart,((CreditEntity) row[8]));
        }
         
 }

	/**
	 * адрес для выгрузки в Эквифакс
	 * @param rootElement - корневой элемент, в который пишем адрес
	 * @param nameElement - название элемента для адреса
	 * @param peopleMisc - доп.данные по человеку
	 * @param address - адрес
	 * @return
	 */
	private static Element createAddressForTitlePartEquifax(Element rootElement,String nameElement,
			PeopleMiscEntity peopleMisc,AddressEntity address){
		 //write address registration
         Element addrreg=rootElement.addElement(nameElement);
         //отношение к собственности
         if (peopleMisc.getRealtyId()!=null){
             WriteNodeWithMandatoryText(addrreg,"owner",peopleMisc.getRealtyId().getCodeinteger().toString());
         } else {
        	 WriteNodeWithMandatoryText(addrreg,"owner",PeopleMisc.REALTY_UNKNOWN);
         }
         WriteNodeWithMandatoryText(addrreg,"country","RU");
         //регион
         if (address.getRegionShort()==null){
      	   WriteNodeWithMandatoryText(addrreg,"region","00");
        } else if (address.getRegionShort().getCode().contains("-")){
        	WriteNodeWithMandatoryText(addrreg,"region",
        			address.getRegionShort().getCode().substring(0, address.getRegionShort().getCode().indexOf("-")));
        } else {
            WriteNodeWithMandatoryText(addrreg,"region",address.getRegionShort().getCode());
        }
        WriteNodeWithMandatoryText(addrreg,"index",StringUtils.isEmpty(address.getIndex())?"000000":address.getIndex());
        //сельский район 
        if (StringUtils.isNotEmpty(address.getArea())){
        	if (StringUtils.isNotEmpty(address.getPlaceName())){
    	          WriteNodeWithMandatoryText(addrreg,"city",address.getPlaceName());
      	  } else if (StringUtils.isNotEmpty(address.getCityName())){
      		  WriteNodeWithMandatoryText(addrreg,"city",address.getCityName());
      	  } else {
      		  WriteNodeWithMandatoryText(addrreg,"city","неизвестно");
      	  }
           
            WriteNodeWithText(addrreg,"district",address.getAreaName());
        }
        else  {
        	if (StringUtils.isNotEmpty(address.getCityName())){
      	        WriteNodeWithMandatoryText(addrreg,"city",address.getCityName());
                WriteNodeWithText(addrreg,"district",address.getDistrictName());
        	} else {
        		//если это Москва или Питер
        		if (address.getRegionName().indexOf(" ")>0) { 
        			WriteNodeWithMandatoryText(addrreg,"city",address.getRegionName().substring(0, address.getRegionName().indexOf(" ")).trim());
        		} else {
        			WriteNodeWithMandatoryText(addrreg,"city",address.getRegionName());
        		}
        	}
        }
        
        WriteNodeWithMandatoryText(addrreg,"street",StringUtils.isNotEmpty(address.getStreetName())?address.getStreetName():"неизвестно");
        WriteNodeWithMandatoryText(addrreg,"house_no",address.getHouse());
        WriteNodeWithText(addrreg,"build_no",address.getBuilding());
        WriteNodeWithText(addrreg,"block_no",address.getCorpus());
        WriteNodeWithText(addrreg,"apartment_no",address.getFlat());
        return addrreg;
	}
	
/**
 * основная часть для передачи КИ в Эквифакс
 * @param rootElement - корневой элемент
 * @param peopleMain - человек
 * @param peoplePersonal - ПД
 * @param docum - паспорт
 * @param addressReg - адрес регистрации
 * @param addressFact - адрес проживания
 * @param creditRequest - кредитная заявка
 * @param mobilePhone - мобильный телефон
 * @param peopleMisc - доп.данные
 * @param isFull - пишем доп.данные или нет (пишем в случае кредитов)
 * @return
 */
	private static Element createTitlePartForEquifax(Element rootElement,
			PeopleMainEntity peopleMain,PeoplePersonalEntity peoplePersonal,
			DocumentEntity docum,AddressEntity addressReg,AddressEntity addressFact,
			CreditRequestEntity creditRequest,PeopleContactEntity mobilePhone,
			PeopleMiscEntity peopleMisc,boolean isFull){
		
		Element title=rootElement.addElement("title_part");
		
		//пишем для выгрузки кредитов
		if (isFull){
		    //наше id человека
		    WriteNodeWithMandatoryText(title,"number", peopleMain.getId().toString());
		}
		
		//вид заемщика
		WriteNodeWithMandatoryText(title,"type",String.valueOf(BaseCredit.CREDIT_OWNER_EQUIFAX));
		
		//write personal data
		Element priv=title.addElement("private");
		//ФИО
		WriteNodeWithMandatoryText(priv,"lastname",peoplePersonal.getSurname());
        WriteNodeWithMandatoryText(priv,"firstname",peoplePersonal.getName());
        WriteNodeWithText(priv,"middlename",peoplePersonal.getMidname());
        
        //пишем для выгрузки кредитов
        if (isFull){
            //пол
            WriteNodeWithText(priv,"gender",peoplePersonal.getGender().getCodeinteger().toString());
            //гражданство
            WriteNodeWithText(priv,"citizenship",peoplePersonal.getCitizen().getCode());
        }
        
        //дата рождения
        WriteNodeWithMandatoryText(priv,"birthday",DatesUtils.DATE_FORMAT_ddMMYYYY.format(peoplePersonal.getBirthdate()));
        //место рождения
        WriteNodeWithMandatoryText(priv,"birthplace",peoplePersonal.getBirthplace());
        //write passport info
        Element pasp=priv.addElement("doc");
        //вид документа
        WriteNodeWithMandatoryText(pasp,"doctype",String.valueOf(Documents.PASSPORT_RF_EQUIFAX_RS));
        //номер и серия документа
        WriteNodeWithMandatoryText(pasp,"docno",docum.getSeries().trim()+docum.getNumber().trim());
        //дата документа
        WriteNodeWithMandatoryText(pasp,"docdate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(docum.getDocdate()));
        //место выдачи
        WriteNodeWithMandatoryText(pasp,"docplace",docum.getDocorg());
        //кем выдан
        WriteNodeWithMandatoryText(pasp,"docwho",docum.getDocorgcode());
        //write people id
        WriteNodeWithText(priv,"inn",peopleMain.getInn());
        //разные теги для кредита и заявки
        if (isFull){
            WriteNodeWithText(priv,"pfno",peopleMain.getSnils());
        } else {
        	WriteNodeWithText(priv,"snils",peopleMain.getSnils());
        }
        //пишем для выгрузки кредитов
        if (isFull){
            //write consent
            WriteNodeWithMandatoryText(title,"consent","1");
            WriteNodeWithMandatoryText(title,"consentdate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDatecontest()));
        }
        
        //write address registration
        createAddressForTitlePartEquifax(title,"address_reg",peopleMisc,addressReg);
        //write fact address     
        createAddressForTitlePartEquifax(title,"address_fact",peopleMisc,addressFact);
            
        //write phones
        Element phones=title.addElement("phones");
        Element phone=phones.addElement("phone_number");
        WriteNodeWithMandatoryText(phone,"phone_type",String.valueOf(PeopleContact.CELL_PHONE_RS));
        WriteNodeWithMandatoryText(phone,"number",mobilePhone.getValue());
        
        return title;
	}
	
	/**
	 * пишем кредит для выгрузки заявок в Эквифакс
	 * @param rootElement - элемент, куда пишем
	 * @param credit - кредит
	 * @return
	 */
	private static Element createCreditPartCRForEquifax(Element rootElement,CreditEntity credit){
		 //write credit
        Element cred=rootElement.addElement("credit");
        //дата смены статуса
        WriteNodeWithMandatoryText(cred,"cred_update",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getDateStatus()));	
        //№ кредита
        WriteNodeWithMandatoryText(cred,"cred_no",credit.getIdCredit());
        //Это займ или кредит
        WriteNodeWithMandatoryText(cred,"loan_credit",String.valueOf(BaseCredit.MICRO_CREDIT_TYPE));
        //вид кредита
        WriteNodeWithMandatoryText(cred,"cred_type",credit.getCredittypeId().getCode());
        //тип займа
        WriteNodeWithMandatoryText(cred,"cred_typecb",String.valueOf(BaseCredit.COMMON_CREDIT_UPLOADING_EQUIFAX));
        //сумма кредита
        WriteNodeWithMandatoryText(cred,"cred_sum",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsum()));
        //валюта
        WriteNodeWithMandatoryText(cred,"cred_currency",BaseCredit.CURRENCY_RUR);
        //дата кредита
        WriteNodeWithMandatoryText(cred,"cred_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg()));
        //дата окончания кредита по графику
        WriteNodeWithText(cred,"cred_enddate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
        //пропуск 2 платежей подряд - такого не бывает
        WriteNodeWithMandatoryText(cred,"cred_missedpay_120","0");
        if (credit.getIsover()!=null&&credit.getIsover()){
        	WriteNodeWithMandatoryText(cred,"cred_full_pay","1");
        } else {
        	WriteNodeWithMandatoryText(cred,"cred_full_pay","0");
        }
        return cred;
	}
	/**
	 * пишем инфо-часть для выгрузки кредитных заявок в Эквифакс
	 * @param rootElement - элемент, куда пишем
	 * @param creditRequest - заявка
	 * @param address - адрес регистрации
	 * @return
	 */
	private static Element createInfoPartForEquifax(Element rootElement,
			CreditRequestEntity creditRequest, AddressEntity address){
		 
		 Element applic=rootElement.addElement("application");
         //номер заявки, возможно id?
         WriteNodeWithMandatoryText(applic,"application_num",creditRequest.getUniquenomer());
         //дата заявки
         WriteNodeWithMandatoryText(applic,"application_date",DatesUtils.DATE_FORMAT_ddMMYYYY_HHmmss.format(creditRequest.getDatecontest()));	
         //регион подачи, если не получилось разобрать при подаче заявки, берем из адреса
         if (creditRequest.getRegion()==null){
       	    if (address.getRegionShort()==null||(address.getRegionShort()!=null&&address.getRegionShort().getCode().contains("-"))){
                WriteNodeWithMandatoryText(applic,"region","00");
       	    } else {
       		    WriteNodeWithMandatoryText(applic,"region",address.getRegionShort().getCode());
       	    }
         }  else {
       	    if (creditRequest.getRegion().getCode().contains("-")){
       		    WriteNodeWithMandatoryText(applic,"region","00"); 
       	    } else {
       	        WriteNodeWithMandatoryText(applic,"region",creditRequest.getRegion().getCode());
       	    }
         }
         //как подавали заявку, всегда дистанционно
         WriteNodeWithMandatoryText(applic,"application_way",String.valueOf(creditRequest.getWayId().getCodeinteger()));
         //статус заявки
         if (creditRequest.getStatusId().getId()==CreditStatus.FILLED){
       	    // заявка заполнена
       	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_EQUIFAX_FILLED));
         } else if (creditRequest.getStatusId().getId()==CreditStatus.REJECTED){
       	    // заявка отказана
      	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_REJECTED));
         } else if (creditRequest.getStatusId().getId()==CreditStatus.DECISION && creditRequest.getAcceptedcreditId()==null){
       	    // заявка одобрена, но оферта не подписана
      	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_APPROVED));
         } else if (creditRequest.getStatusId().getId()==CreditStatus.CLIENT_REFUSE && creditRequest.getAccepted()!=null){
       	    // заявка одобрена, клиент отказался
      	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_EQUIFAX_APPROVED_CLIENT_REFUSE));
         } else if ((creditRequest.getStatusId().getId()==CreditStatus.DECISION||creditRequest.getStatusId().getId()==CreditStatus.CLOSED
       		  ||creditRequest.getStatusId().getId()==CreditStatus.FOR_COLLECTOR||creditRequest.getStatusId().getId()==CreditStatus.FOR_COURT)&& creditRequest.getAcceptedcreditId()!=null){
       	    //кредит выдан, если передаем историю, то делаем другие статусы
       	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_EQUIFAX_APPROVED_WITH_CREDIT));
         } else {
       	    //какой-то другой статус
       	    WriteNodeWithMandatoryText(applic,"status",String.valueOf(CreditStatus.UPLOAD_EQUIFAX_FILLED));
         }
         //дата статуса заявки
         if (creditRequest.getStatusId().getId()==CreditStatus.REJECTED
       		  ||creditRequest.getStatusId().getId()==CreditStatus.CLIENT_REFUSE
       		  ||creditRequest.getStatusId().getId()==CreditStatus.CLOSED
       		  ||creditRequest.getStatusId().getId()==CreditStatus.FOR_COLLECTOR
       		  ||creditRequest.getStatusId().getId()==CreditStatus.FOR_COURT){
             WriteNodeWithMandatoryText(applic,"status_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateStatus()));
         } else {
       	  if (creditRequest.getAcceptedcreditId()!=null){
       		  WriteNodeWithMandatoryText(applic,"status_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getAcceptedcreditId().getCreditdatabeg()));
       	  } else {
       	      WriteNodeWithMandatoryText(applic,"status_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addHours(creditRequest.getDatecontest(),1)));
       	  }
         }
         //дата окончания одобрения заявки
         if (creditRequest.getStatusId().getId()==CreditStatus.DECISION && creditRequest.getAcceptedcreditId()==null){
       	    // заявка одобрена, но оферта не подписана
        	 if (creditRequest.getDateDecision()!=null){
        		 WriteNodeWithMandatoryText(applic,"approval_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditRequest.getDateDecision(),5))); 
        	 } else {
      	        WriteNodeWithMandatoryText(applic,"approval_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditRequest.getDateStatus(),5)));
        	 }
         }
         //тип займа
         WriteNodeWithMandatoryText(applic,"app_cred_typecb",String.valueOf(BaseCredit.COMMON_CREDIT_UPLOADING_EQUIFAX));
         //сумма по заявке
         WriteNodeWithMandatoryText(applic,"app_cred_sum",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
         //срок займа, в месяцах - всегда 1
         WriteNodeWithMandatoryText(applic,"app_cred_duration","1");
         //валюта
         WriteNodeWithMandatoryText(applic,"app_cred_currency",BaseCredit.CURRENCY_RUR);
         //если заявка была отказана
         if (creditRequest.getStatusId().getId()==CreditStatus.REJECTED){
       	    Element rejectpart=applic.addElement("failure");
       	    WriteNodeWithMandatoryText(rejectpart,"failure_reason",CreditRequest.UPLOAD_REFUSE_REASON_EQUIFAX);
         }
         return applic;
	}
	
	/**
	 * запись кредита
	 * @param rootElement - элемент, в который пишем
	 * @param credit - кредит
	 * @param creditRequest - кредитная заявка
	 * @param sendingDate - дата отправки выгрузки
	 * @return
	 */
	private static Element createCreditRecordForEquifax(Element rootElement,CreditEntity credit,
			CreditRequestEntity creditRequest,Date sendingDate){
		  //write credit
        Element cred=rootElement.addElement("credit");
        //дата актуальности информации 
        if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_ACTIVE||credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_CLOSED){
            WriteNodeWithMandatoryText(cred,"cred_update",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getDateStatus()));	
        } else {
      	  WriteNodeWithMandatoryText(cred,"cred_update",DatesUtils.DATE_FORMAT_ddMMYYYY.format(sendingDate));	
        }
       
        //№ кредита
        WriteNodeWithMandatoryText(cred,"cred_no",credit.getIdCredit());
        //№ счета из credit
        WriteNodeWithMandatoryText(cred,"cred_account_no",credit.getCreditAccount());
        //внутренний № кредита из нашей системы
        WriteNodeWithMandatoryText(cred,"cred_id",credit.getId().toString());
        //статус кредита
        if (credit.getMaxDelay()!=null&&!credit.getIsover()
      		  &&credit.getMaxDelay()<=5&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 5 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","10"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),1)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>5
      		  &&credit.getMaxDelay()<30&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 30 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","11"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),6)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>=30
      		  &&credit.getMaxDelay()<60&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 60 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","12"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),30)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()
      		  &&credit.getMaxDelay()>=60&&credit.getMaxDelay()<90&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 90 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","13"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),60)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>=90
      		  &&credit.getMaxDelay()<120&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 120 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","14"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),90)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>=120
      		  &&credit.getMaxDelay()<150&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 150 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","15"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),120)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>=150
      		  &&credit.getMaxDelay()<180&&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 180 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","16"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),150)));
        } else if (credit.getMaxDelay()!=null&&!credit.getIsover()&&credit.getMaxDelay()>=180
      		  &&credit.getMaxDelay()<210 &&credit.getIsactive()==ActiveStatus.ACTIVE){
      	  //кредит активен, просрочка до 210 дней
      	  WriteNodeWithMandatoryText(cred,"cred_active","17"); 
      	  //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(credit.getCreditdataend(),180)));
        } else  {
      	  //во всех остальных случаях пишем статус из БД
            WriteNodeWithMandatoryText(cred,"cred_active",credit.getCreditStatusId().getCodeinteger().toString());
      	
            //дата статуса
            WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getDateStatus()));
        }
        
        //приобретатель кредита, если передан коллекторам или продан
        /*if (((CreditEntity) row[8]).getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_COLLECTOR){
      	  WriteNodeWithMandatoryText(cred,"cred_purchaser",((DebtEntity) row[15])); 
        }*/
        //вид кредита
        WriteNodeWithMandatoryText(cred,"cred_type",credit.getCredittypeId().getCode());
        WriteNodeWithMandatoryText(cred,"cred_facility","8");
        WriteNodeWithMandatoryText(cred,"cred_joint","0");
        //сумма кредита
        WriteNodeWithMandatoryText(cred,"cred_sum",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsum()));
        //валюта
        WriteNodeWithMandatoryText(cred,"cred_currency",BaseCredit.CURRENCY_RUR);
        //дата кредита
        WriteNodeWithMandatoryText(cred,"cred_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg()));
        //дата окончания кредита по графику
        WriteNodeWithText(cred,"cred_enddate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
        //кредитный лимит, всегда 0
        WriteNodeWithMandatoryText(cred,"cred_sum_limit","0");
        //частота выплаты
        WriteNodeWithMandatoryText(cred,"cred_amount_frequency",BaseCredit.FREQ_PAYMENT_ONCE);
        WriteNodeWithMandatoryText(cred,"cred_security","9");
        WriteNodeWithMandatoryText(cred,"cred_insured","0");
       
        //дата просрочки
        if ((credit.getCreditdataend().before(sendingDate)&&credit.getCreditdataendfact()==null)
      		  ||(credit.getCreditdataendfact()!=null&&credit.getCreditdataendfact().after(credit.getCreditdataend()))){
            WriteNodeWithMandatoryText(cred,"cred_date_missedpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
        } else {
      	  WriteNodeWithMandatoryText(cred,"cred_date_missedpayout","01.02.1900");
        }
        //полная стоимость кредита
        WriteNodeWithMandatoryText(cred,"cred_full_cost",CalcUtils.pskToString(CalcUtils.calcYearStake(credit.getCreditpercent())));
        
        //способ получения займа - дистанционный
        WriteNodeWithMandatoryText(cred,"cred_way",String.valueOf(CreditRequest.UPLOAD_WAY_CREDITREQUEST_EQUIFAX));
        //пропуск 2 платежей подряд - такого не бывает
        WriteNodeWithMandatoryText(cred,"cred_missedpay_120","0");
        //сумма кредитной заявки
        WriteNodeWithMandatoryText(cred,"cred_sum_application",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
        return cred;
	}
	
	/**
	 * пишем платежи для кредита Эквифакса
	 * @param rootElement - элемент, в который пишем
	 * @param credit - кредит
	 * @param sendingDate - дата выгрузки
	 * @param sumPay - сумма платежей
	 * @param sumOperation - сумма операции
	 * @param sumPercent - сумма процентов
	 * @param sumMain - сумма основного долга
	 * @param payment - платеж
	 * @param creditDetail - детали кредита
	 */
	private static void createCreditPaymentRecordForEquifax(Element rootElement,CreditEntity credit,
			Date sendingDate,Double sumPay,Double sumOperation,Double sumPercent,Double sumMain,
			PaymentEntity payment,CreditDetailsEntity creditDetail){
	
		//основной долг
        Element principal=rootElement.addElement("only_principle");
        // не было платежей
        if (sumPay==0) {
     	  //if (((CreditDetailsEntity) row[18])==null) {	  
      	  WriteNodeWithMandatoryText(principal,"op_cred_sum_payout","0");
      	  WriteNodeWithMandatoryText(principal,"op_cred_date_payout","01.02.1900");
      	 
      	  //кредит просрочен
      	  if (credit.getCreditdataend().before(sendingDate)){
      		  //при просрочке ставим в сумму долга ноль
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_debt","0");
          	  WriteNodeWithMandatoryText(principal,"op_cred_sum_paid","0");
      		  //просроченная сумма
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_overdue",Convertor.toDecimalString(CalcUtils.dformat,credit.getSumMainRemain()));
      		  //дата просрочки
      		  WriteNodeWithMandatoryText(principal,"op_cred_date_overdue",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_nextpayout","0");
      		  //дата следующего платежа
      		  WriteNodeWithMandatoryText(principal,"op_cred_date_nextpayout","01.02.1900");
      	  }
      	  //дата окончания по графику не наступила
      	  else {
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,credit.getSumMainRemain()));
          	  WriteNodeWithMandatoryText(principal,"op_cred_sum_paid","0");
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_overdue","0");
      	      WriteNodeWithMandatoryText(principal,"op_cred_date_overdue","01.02.1900");
      	      WriteNodeWithMandatoryText(principal,"op_cred_sum_nextpayout",Convertor.toDecimalString(CalcUtils.dformat,credit.getSumMainRemain()));
      	      WriteNodeWithMandatoryText(principal,"op_cred_date_nextpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
      	  }
      	
      	 
        }
        //были платежи
        else {
      	  //есть остаток по основному долгу
      	  if(credit.getCreditsumdebt()!=null&&!credit.getIsover()){
      		  //оплатили только проценты
      		  if (sumOperation.equals(sumPercent)){
      		  	  WriteNodeWithMandatoryText(principal,"op_cred_sum_payout","0");
      		  	  WriteNodeWithMandatoryText(principal,"op_cred_date_payout","01.02.1900");
      		  	  if (credit.getCreditdataend().before(sendingDate)){
      		  		  WriteNodeWithMandatoryText(principal,"op_cred_sum_debt","0");
      		  	  } else {
      			      WriteNodeWithMandatoryText(principal,"op_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumdebt()));
      		  	  }
      			  WriteNodeWithMandatoryText(principal,"op_cred_sum_paid","0");
      		  }else{
      			  //отнимаем от суммы операции сумму процентов - сумма выплаты в данном диапазоне дат
      			  WriteNodeWithMandatoryText(principal,"op_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,sumOperation-sumPercent));
      			  //дата оплаты - если не было, то пустая
      			  if (sumOperation-sumPercent==0d){
      				  WriteNodeWithMandatoryText(principal,"op_cred_date_payout","01.02.1900");
      			  } else {
      			      WriteNodeWithMandatoryText(principal,"op_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(payment.getProcessDate()));
      			      //WriteNodeWithMandatoryText(principal,"op_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(((CreditDetailsEntity) row[18]).getEventDate()));
      			  }
      			  //сумма основного долга
      			  if (credit.getCreditdataend().before(sendingDate)){
      		  		  WriteNodeWithMandatoryText(principal,"op_cred_sum_debt","0");
      		  	  } else {
      			      WriteNodeWithMandatoryText(principal,"op_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumdebt()));
      		  	  }
      			  //если не было платежей в этот период, ставим 0
      			  if (sumOperation-sumPercent==0d){
          			  //общая оплаченная сумма основного долга
          			  WriteNodeWithMandatoryText(principal,"op_cred_sum_paid","0"); 
      			  } else {
      			      //общая оплаченная сумма основного долга
      			      WriteNodeWithMandatoryText(principal,"op_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,sumMain));
      			  }
      		  }
      		  //кредит просрочен
      		  if (credit.getCreditdataend().before(sendingDate)){
      			  WriteNodeWithMandatoryText(principal,"op_cred_sum_overdue",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumdebt()));
          	      WriteNodeWithMandatoryText(principal,"op_cred_date_overdue",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
          	      WriteNodeWithMandatoryText(principal,"op_cred_sum_nextpayout","0");
          	      WriteNodeWithMandatoryText(principal,"op_cred_date_nextpayout","01.02.1900");
          	     
      		  } else {
      			  WriteNodeWithMandatoryText(principal,"op_cred_sum_overdue","0");
          	      WriteNodeWithMandatoryText(principal,"op_cred_date_overdue","01.02.1900");
          	      WriteNodeWithMandatoryText(principal,"op_cred_sum_nextpayout",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumdebt()));
          	      WriteNodeWithMandatoryText(principal,"op_cred_date_nextpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
          	      
      		  }
      	  }
      	  //нет остатка
      	  else {
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsum()));
      		  WriteNodeWithMandatoryText(principal,"op_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(payment.getProcessDate()));
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_debt","0");
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsum()));
      		  WriteNodeWithMandatoryText(principal,"op_cred_sum_overdue","0");
      	      WriteNodeWithMandatoryText(principal,"op_cred_date_overdue","01.02.1900");
      	      WriteNodeWithMandatoryText(principal,"op_cred_sum_nextpayout","0");
      	      WriteNodeWithMandatoryText(principal,"op_cred_date_nextpayout","01.02.1900");
      	      
      	  }
        
        }
        //общий долг, с процентами и комиссиями
        Element amount=rootElement.addElement("total_amount");
        // не было платежей
        if (sumPay==0) {
     	  //if (((CreditDetailsEntity) row[18])==null) {
      	
      	  WriteNodeWithMandatoryText(amount,"ta_cred_sum_payout","0");
      	  WriteNodeWithMandatoryText(amount,"ta_cred_date_payout","01.02.1900");
      	  
      	  //кредит просрочен
      	  if (credit.getCreditdataend().before(sendingDate)){
      		  WriteNodeWithMandatoryText(amount,"ta_cred_sum_debt","0");
          	  WriteNodeWithMandatoryText(amount,"ta_cred_sum_paid","0");
      		  WriteNodeWithMandatoryText(amount,"ta_cred_sum_overdue",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
      		  WriteNodeWithMandatoryText(amount,"ta_cred_date_overdue",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
      		  WriteNodeWithMandatoryText(amount,"ta_cred_sum_nextpayout","0");
      		  WriteNodeWithMandatoryText(amount,"ta_cred_date_nextpayout","01.02.1900");
      	  }
      	  //дата окончания по графику не наступила
      	  else {
      		  WriteNodeWithMandatoryText(amount,"ta_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
          	  WriteNodeWithMandatoryText(amount,"ta_cred_sum_paid","0");
      		  WriteNodeWithMandatoryText(amount,"ta_cred_sum_overdue","0");
      	      WriteNodeWithMandatoryText(amount,"ta_cred_date_overdue","01.02.1900");
      	      WriteNodeWithMandatoryText(amount,"ta_cred_sum_nextpayout",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
      	      WriteNodeWithMandatoryText(amount,"ta_cred_date_nextpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
      	  }
      	 
     	
        }
        //были платежи
        else
        {
      	
       	  //есть остаток - оплатили не все
      	  if(!sumMain.equals(credit.getCreditsum())&&!credit.getIsover()){
      		
      			//последний платеж
      			WriteNodeWithMandatoryText(amount,"ta_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,sumOperation));
      			//WriteNodeWithMandatoryText(amount,"ta_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,((CreditDetailsEntity) row[18]).getAmountOperation()));
      			//дата платежа- если платежей не было, то пустая
      			if (sumOperation==0d){
      				WriteNodeWithMandatoryText(amount,"ta_cred_date_payout","01.02.1900");
      			} else {
      				WriteNodeWithMandatoryText(amount,"ta_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(payment.getProcessDate()));
      			    //WriteNodeWithMandatoryText(amount,"ta_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(((CreditDetailsEntity) row[18]).getEventDate()));
      			}
      			//сумма долга
      			if (credit.getCreditdataend().before(sendingDate)){
      				WriteNodeWithMandatoryText(amount,"ta_cred_sum_debt","0");
      			} else {
      			    WriteNodeWithMandatoryText(amount,"ta_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
      			}
      			//сумма платежей - если не было в этот период, ставим 0
      			if (sumOperation==0d){
      				//заплатили всего
          			WriteNodeWithMandatoryText(amount,"ta_cred_sum_paid","0");
        			} else {
      			    //заплатили всего
      			    WriteNodeWithMandatoryText(amount,"ta_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,sumPay));
        	   }
      			
      		  //кредит просрочен
      		  if (credit.getCreditdataend().before(sendingDate)){
      			  WriteNodeWithMandatoryText(amount,"ta_cred_sum_overdue",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
          	      WriteNodeWithMandatoryText(amount,"ta_cred_date_overdue",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
          	      WriteNodeWithMandatoryText(amount,"ta_cred_sum_nextpayout","0");
          	      WriteNodeWithMandatoryText(amount,"ta_cred_date_nextpayout","01.02.1900");
      		  } else {
      			  WriteNodeWithMandatoryText(amount,"ta_cred_sum_overdue","0");
          	      WriteNodeWithMandatoryText(amount,"ta_cred_date_overdue","01.02.1900");
          	      WriteNodeWithMandatoryText(amount,"ta_cred_sum_nextpayout",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
          	      WriteNodeWithMandatoryText(amount,"ta_cred_date_nextpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataend()));
          	      
      		  }
      	}
      	//нет остатка
      	else {
      		WriteNodeWithMandatoryText(amount,"ta_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,sumOperation));
      		WriteNodeWithMandatoryText(amount,"ta_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(payment.getProcessDate()));
      		WriteNodeWithMandatoryText(amount,"ta_cred_sum_debt","0");
             	WriteNodeWithMandatoryText(amount,"ta_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,sumPay));
          	WriteNodeWithMandatoryText(amount,"ta_cred_sum_overdue","0");
      	    WriteNodeWithMandatoryText(amount,"ta_cred_date_overdue","01.02.1900");
      	    WriteNodeWithMandatoryText(amount,"ta_cred_sum_nextpayout","0");
      	    WriteNodeWithMandatoryText(amount,"ta_cred_date_nextpayout","01.02.1900");
      	   
      	}
         	
        }
	}
	
	/**
	 * передача в суд для Эквифакса
	 * @param rootElement - корневой элемент
	 * @param debt - долг
	 * @return
	 */
	private static Element createDebtInfoForEquifax(Element rootElement,DebtEntity debt){
		 //write debt info
        Element official=rootElement.addElement("official");
        Element court=official.addElement("court");
        WriteNodeWithMandatoryText(court,"court_number",debt.getPeopleMainId().getId().toString());
        WriteNodeWithMandatoryText(court,"court_no",debt.getDebtNumber());
        WriteNodeWithMandatoryText(court,"court_source",debt.getAuthorityName());
        WriteNodeWithMandatoryText(court,"court_plaintiff","0");
        WriteNodeWithMandatoryText(court,"court_resolution",debt.getComment());
        return official;
	}
	
	/**
	 * запись кредита для Эквифакса
	 * @param rootElement - элемент, в который пишем
	 * @param credit - кредит
	 * @param creditRequest - заявка
	 * @param databeg - дата начала
	 * @param dataend - дата окончания
	 * @param sendingDate - дата выгрузки
	 * @param status - статус
	 * @param statusDate - дата статуса
	 * @param isOverdue - есть ли просрочка
	 * @return
	 */
	private static Element createCreditPartForEquifax(Element rootElement,CreditEntity credit,
			CreditRequestEntity creditRequest,Date databeg,Date dataend,Date sendingDate,
			Integer status,Date statusDate,Boolean isOverdue){
		  //write credit
        Element cred=rootElement.addElement("credit");
        //дата актуальности информации 
        if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_ACTIVE||credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_CLOSED){
            WriteNodeWithMandatoryText(cred,"cred_update",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getDateStatus()));	
        } else {
      	    WriteNodeWithMandatoryText(cred,"cred_update",DatesUtils.DATE_FORMAT_ddMMYYYY.format(statusDate));	
        }
       
        //№ кредита
        WriteNodeWithMandatoryText(cred,"cred_no",credit.getIdCredit());
        //№ счета из credit
        WriteNodeWithMandatoryText(cred,"cred_account_no",credit.getCreditAccount());
        //внутренний № кредита из нашей системы
        WriteNodeWithMandatoryText(cred,"cred_id",credit.getId().toString());
        //статус кредита
        
      	WriteNodeWithMandatoryText(cred,"cred_active",status.toString()); 
      	//дата статуса
        WriteNodeWithMandatoryText(cred,"cred_active_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(statusDate));
        
        //вид кредита
        WriteNodeWithMandatoryText(cred,"cred_type",credit.getCredittypeId().getCode());
        WriteNodeWithMandatoryText(cred,"cred_facility","8");
        WriteNodeWithMandatoryText(cred,"cred_joint","0");
        //сумма кредита
        WriteNodeWithMandatoryText(cred,"cred_sum",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsum()));
        //валюта
        WriteNodeWithMandatoryText(cred,"cred_currency",BaseCredit.CURRENCY_RUR);
        //дата кредита
        WriteNodeWithMandatoryText(cred,"cred_date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg()));
        //дата окончания кредита по графику
        WriteNodeWithText(cred,"cred_enddate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(dataend));
        //кредитный лимит, всегда 0
        WriteNodeWithMandatoryText(cred,"cred_sum_limit","0");
        //частота выплаты
        WriteNodeWithMandatoryText(cred,"cred_amount_frequency",BaseCredit.FREQ_PAYMENT_ONCE);
        WriteNodeWithMandatoryText(cred,"cred_security","9");
        WriteNodeWithMandatoryText(cred,"cred_insured","0");
        
        //дата просрочки
        if (isOverdue){
            WriteNodeWithMandatoryText(cred,"cred_date_missedpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(dataend));
        } else {
      	    WriteNodeWithMandatoryText(cred,"cred_date_missedpayout","01.02.1900");
        }
        //полная стоимость кредита
        WriteNodeWithMandatoryText(cred,"cred_full_cost",CalcUtils.pskToString(CalcUtils.calcYearStake(credit.getCreditpercent())));
        
        //способ получения займа - дистанционный
        WriteNodeWithMandatoryText(cred,"cred_way",String.valueOf(CreditRequest.UPLOAD_WAY_CREDITREQUEST_EQUIFAX));
        //пропуск 2 платежей подряд - такого не бывает
        WriteNodeWithMandatoryText(cred,"cred_missedpay_120","0");
        //сумма кредитной заявки
        WriteNodeWithMandatoryText(cred,"cred_sum_application",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
        return cred;
	}
	
	/**
	 * пишем суммы для открытия кредита
	 * @param rootElement - корневой элемент
	 * @param elementName - название элемента
	 * @param prefix - префикс для элемента
	 * @param sum - сумма долга
	 * @param date - дата отдачи долга
	 * @param sumPaid - сумма платежа
	 * @param datePaid - дата платежа
	 * @param sumPaidAll - сумма всех платежей
	 * @return
	 */
	private static Element createSumsForCreditOpen(Element rootElement,
			String elementName,String prefix,Double sum,Date date,
			Double sumPaid,Date datePaid,Double sumPaidAll){
		//основной долг
        Element principal=rootElement.addElement(elementName);
        if (sumPaid==0){
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_payout","0");
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_date_payout","01.02.1900");
        } else {
        	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,sumPaid));
          	WriteNodeWithMandatoryText(principal,prefix+"_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(datePaid));
          	
        }
        if (sum==0){
        	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_debt","0");
        } else {
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_debt",Convertor.toDecimalString(CalcUtils.dformat,sum));
        }
      	if (sumPaidAll==0){
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_paid","0");
      	} else {
      		WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,sumPaidAll));
      	}
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_overdue","0");
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_date_overdue","01.02.1900");
      	if (sum==0){
      		WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_nextpayout","0");
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_date_nextpayout","01.02.1900");
      	} else {
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_nextpayout",Convertor.toDecimalString(CalcUtils.dformat,sum));
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_date_nextpayout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(date));
      	}
      	
      	return principal;
	}
	
	/**
	 * пишем суммы для просрочки кредита
	 * @param rootElement - корневой элемент
	 * @param elementName - название элемента
	 * @param prefix - префикс для элемента
	 * @param sum - сумма
	 * @param date - дата
	 * @param sumPaid - сумма платежа
	 * @param datePaid - дата платежа
	 * @return
	 */
	private static Element createSumsForCreditOverdue(Element rootElement,
			String elementName,String prefix,Double sum,Date date,
			Double sumPaid,Date datePaid,Double sumPaidAll){
		//основной долг
        Element principal=rootElement.addElement(elementName);
        if (sumPaid==0){
        	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_payout","0");
        	WriteNodeWithMandatoryText(principal,prefix+"_cred_date_payout","01.02.1900");
        } else {
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_payout",Convertor.toDecimalString(CalcUtils.dformat,sumPaid));
      	    WriteNodeWithMandatoryText(principal,prefix+"_cred_date_payout",DatesUtils.DATE_FORMAT_ddMMYYYY.format(datePaid));
        }
      	
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_debt","0");
      	if (sumPaidAll==0){
      		WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_paid","0");
      	} else {
            WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_paid",Convertor.toDecimalString(CalcUtils.dformat,sumPaidAll));
      	}
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_overdue",Convertor.toDecimalString(CalcUtils.dformat,sum));
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_date_overdue",DatesUtils.DATE_FORMAT_ddMMYYYY.format(date));
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_sum_nextpayout","0");
      	WriteNodeWithMandatoryText(principal,prefix+"_cred_date_nextpayout","01.02.1900");
      	
      	return principal;
	}
	
	/**
	 * пишем данные заявки
	 * @param root - корневой элемент
	 * @param creditRequest - заявка
	 */
	private static void createInfoDataForEquifax(Element root,CreditRequestEntity creditRequest){
		//погашение за счет обеспечения
        WriteNodeWithMandatoryText(root,"cred_collateral","0");
        //№ кредитной заявки
        WriteNodeWithMandatoryText(root,"applicationid",creditRequest.getUniquenomer());
        //дата подачи кредитной заявки
        WriteNodeWithMandatoryText(root,"applicationdate",DatesUtils.DATE_FORMAT_ddMMYYYY_HHmmss.format(creditRequest.getDatecontest()));
 
	}
	
	/**
	 * 
	 * @param sendingDate
	 * @param version
	 * @param credit
	 * @param peopleMain
	 * @param peoplePersonal
	 * @param docum
	 * @param addressReg
	 * @param addressFact
	 * @param creditRequest
	 * @param mobilePhone
	 * @param peopleMisc
	 * @param creditInfo
	 * @param overdueFirst
	 * @param overdueSecond
	 * @param prolong
	 * @param payment
	 * @param overdueThird
	 * @param overdueFourth
	 * @return
	 */
	public static String createCorrectionRecordForEquifax(Date sendingDate,
			String version,CreditEntity credit,
			PeopleMainEntity peopleMain,PeoplePersonalEntity peoplePersonal,
			DocumentEntity docum,AddressEntity addressReg,AddressEntity addressFact,
			CreditRequestEntity creditRequest,PeopleContactEntity mobilePhone,
			PeopleMiscEntity peopleMisc,CreditDetailsEntity creditInfo,
			CreditDetailsEntity overdueFirst,CreditDetailsEntity overdueSecond,
			CreditDetailsEntity prolong,PaymentEntity payment,
			CreditDetailsEntity overdueThird,CreditDetailsEntity overdueFourth,
			CreditDetailsEntity creditEndInfo){
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
		Element root = document.addElement( "fch" );
        root.addAttribute("version",version);
        
        Element hd=root.addElement("head");
        WriteNodeWithMandatoryText(hd,"date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(sendingDate));
        
        //первая запись о создании кредита, есть всегда
        Element info=root.addElement("info");
		//номер записи
        Integer i=1;
		info.addAttribute("recnumber", i.toString());
		//общая часть с адресами и пр
		createTitlePartForEquifax(info,	peopleMain,peoplePersonal,
				docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
		//кредит
		Element cred=createCreditPartForEquifax(info, credit,
				creditRequest,creditInfo.getEventDate(),creditInfo.getEventEndDate(),
				sendingDate, BaseCredit.CREDIT_ACTIVE,creditInfo.getEventDate(),false);
		//суммы
		//основной долг
		createSumsForCreditOpen(cred,"only_principle","op",creditInfo.getAmountMain(),
				creditInfo.getEventEndDate(),new Double(0),null,new Double(0));
		//общий долг
		createSumsForCreditOpen(cred,"total_amount","ta",creditInfo.getAmountAll(),
				creditInfo.getEventEndDate(),new Double(0),null,new Double(0));
		
		//погашение за счет обеспечения
        WriteNodeWithMandatoryText(cred,"cred_collateral","0");
        //№ кредитной заявки
        WriteNodeWithMandatoryText(cred,"applicationid",creditRequest.getUniquenomer());
        //дата подачи кредитной заявки
        WriteNodeWithMandatoryText(cred,"applicationdate",DatesUtils.DATE_FORMAT_ddMMYYYY_HHmmss.format(creditRequest.getDatecontest()));
      
		//ушел в просрочку, ничего не платил
		if (overdueFirst!=null){
			Element infoOverdue1=root.addElement("info");
			i++;
			
			infoOverdue1.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoOverdue1,	peopleMain,peoplePersonal,
					docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credOverdue1=createCreditPartForEquifax(infoOverdue1, credit,
					creditRequest,creditInfo.getEventDate(),creditInfo.getEventEndDate(),
					sendingDate, BaseCredit.CREDIT_OVERDUE5,
					DateUtils.addDays(creditInfo.getEventEndDate(),1),true);
			//суммы
			//основной долг
			createSumsForCreditOverdue(credOverdue1,
					"only_principle","op",overdueFirst.getAmountMain(),creditInfo.getEventEndDate(),
					new Double(0),null,new Double(0));
			//общий долг
			createSumsForCreditOverdue(credOverdue1,
					"total_amount","ta",overdueFirst.getAmountAll(),creditInfo.getEventEndDate(),
					new Double(0),null,new Double(0));
			//кредитная заявка
			createInfoDataForEquifax(credOverdue1,creditRequest);
			
		}
		
		//ушел в просрочку больше 5 дней, ничего не платил
		if (overdueSecond!=null){
			Element infoOverdue2=root.addElement("info");
			i++;
					
			infoOverdue2.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoOverdue2,	peopleMain,peoplePersonal,
			      docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credOverdue2=createCreditPartForEquifax(infoOverdue2, credit,
					creditRequest,creditInfo.getEventDate(),creditInfo.getEventEndDate(),
					sendingDate, BaseCredit.CREDIT_OVERDUE30,
					DateUtils.addDays(creditInfo.getEventEndDate(),6),true);
			//суммы
			//основной долг
			createSumsForCreditOverdue(credOverdue2,
							"only_principle","op",overdueSecond.getAmountMain(),
							creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//общий долг
			createSumsForCreditOverdue(credOverdue2,
							"total_amount","ta",overdueSecond.getAmountAll(),creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//кредитная заявка
			createInfoDataForEquifax(credOverdue2,creditRequest);
		}
		
		//ушел в просрочку больше 30 дней, ничего не платил
		if (overdueThird!=null){
			Element infoOverdue3=root.addElement("info");
			i++;
							
			infoOverdue3.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoOverdue3,	peopleMain,peoplePersonal,
				     docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credOverdue3=createCreditPartForEquifax(infoOverdue3, credit,
					creditRequest,creditInfo.getEventDate(),creditInfo.getEventEndDate(),
					sendingDate, BaseCredit.CREDIT_OVERDUE60,
					DateUtils.addDays(creditInfo.getEventEndDate(),6),true);
			//суммы
			//основной долг
			createSumsForCreditOverdue(credOverdue3,
							"only_principle","op",overdueThird.getAmountMain(),
							creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//общий долг
			createSumsForCreditOverdue(credOverdue3,
							"total_amount","ta",overdueThird.getAmountAll(),
							creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//кредитная заявка
			createInfoDataForEquifax(credOverdue3,creditRequest);
		}
		
		//ушел в просрочку больше 60 дней, ничего не платил
		if (overdueFourth!=null){
			Element infoOverdue4=root.addElement("info");
			i++;
									
			infoOverdue4.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoOverdue4,	peopleMain,peoplePersonal,
				     docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credOverdue4=createCreditPartForEquifax(infoOverdue4, credit,
					creditRequest,creditInfo.getEventDate(),creditInfo.getEventEndDate(),
					sendingDate, BaseCredit.CREDIT_OVERDUE90,
					DateUtils.addDays(creditInfo.getEventEndDate(),6),true);
			//суммы
			//основной долг
			createSumsForCreditOverdue(credOverdue4,
							"only_principle","op",overdueFourth.getAmountMain(),
							creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//общий долг
			createSumsForCreditOverdue(credOverdue4,
							"total_amount","ta",overdueThird.getAmountAll(),
							creditInfo.getEventEndDate(),
							new Double(0),null,new Double(0));
			//кредитная заявка
			createInfoDataForEquifax(credOverdue4,creditRequest);
		}
		
		//если было продление
		if (prolong!=null){
			Element infoProlong=root.addElement("info");
			i++;
					
			infoProlong.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoProlong,	peopleMain,peoplePersonal,
			      docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credProlong=createCreditPartForEquifax(infoProlong, credit,
					creditRequest,prolong.getEventDate(),prolong.getEventEndDate(),
					sendingDate, BaseCredit.CREDIT_ACTIVE,prolong.getEventDate(),false);
			//основной долг
			createSumsForCreditOpen(credProlong,"only_principle","op",prolong.getAmountMain(),
					prolong.getEventEndDate(),payment.getAmount()-prolong.getAmountMain(),
					prolong.getEventDate(),payment.getAmount()-prolong.getAmountMain());
			//общий долг
			createSumsForCreditOpen(credProlong,"total_amount","ta",prolong.getAmountAll(),
					prolong.getEventEndDate(),payment.getAmount(),payment.getProcessDate(),payment.getAmount());
			
			//кредитная заявка
			createInfoDataForEquifax(credProlong,creditRequest);
		}
		//если кредит окончен
		if (credit.getIsover()){
			Element infoEnd=root.addElement("info");
			i++;
			infoEnd.addAttribute("recnumber", i.toString());
			//общая часть с адресами и пр
			createTitlePartForEquifax(infoEnd,peopleMain,peoplePersonal,
			      docum,addressReg,addressFact,creditRequest, mobilePhone,peopleMisc,true);
			//кредит
			Element credOver=createCreditPartForEquifax(infoEnd, credit,
					creditRequest,credit.getCreditdatabeg(),credit.getCreditdataendfact(),
					sendingDate, BaseCredit.CREDIT_CLOSED,credit.getCreditdataendfact(),false);
			//суммы
			//основной долг
			createSumsForCreditOpen(credOver,"only_principle","op",new Double(0),
					null,creditEndInfo.getAmountOperation()-creditEndInfo.getAmountPercent(),
					creditEndInfo.getEventDate(),credit.getCreditsum());
			//общий долг
			createSumsForCreditOpen(credOver,"total_amount","ta",new Double(0),
					null,creditEndInfo.getAmountOperation(),creditEndInfo.getEventDate(),
					credit.getCreditsumback());
			
			//кредитная заявка
			createInfoDataForEquifax(credOver,creditRequest);
		}
		//файл закончился
		Element ft=root.addElement("footer");
		WriteNodeWithMandatoryText(ft,"reccount",i.toString());
			
		return document.asXML();
	}
	
			
	/**
	 * пишем блок с ПД для РС
	 * @param root - куда пишем
	 * @param peopleMain - человек
	 * @param peoplePersonal - ПД человека
	 * @param doc - документ человека
	 * @param commandId - № команды
	 * @return
	 */
	private static Element createIndividualForRS(Element root,PeopleMainEntity peopleMain,
			PeoplePersonalEntity peoplePersonal,DocumentEntity doc,int commandId){
		   //добавляем новый заголовок для человека
        Element tnp=root.addElement("AddIndividual");
        WriteNodeWithMandatoryText(tnp,"cmdId",String.valueOf(commandId));
        //дата актуальности
        WriteNodeWithMandatoryText(tnp,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(peoplePersonal.getDatabeg()));
        
        //пишем персональные данные
        WriteNodeWithMandatoryText(tnp,"sId",peopleMain.getId().toString()); 
        WriteNodeWithMandatoryText(tnp,"iSurname",peoplePersonal.getSurname().toUpperCase()); 
        WriteNodeWithMandatoryText(tnp,"iName",peoplePersonal.getName().toUpperCase()); 
        WriteNodeWithMandatoryText(tnp,"iPatronymic",peoplePersonal.getMidname().toUpperCase());
        // TODO старые фио, если были
        WriteNodeWithMandatoryText(tnp,"ibDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(peoplePersonal.getBirthdate())); 
        WriteNodeWithMandatoryText(tnp,"ibPlace",peoplePersonal.getBirthplace().toUpperCase());
        //пишем документ
        Element dc=tnp.addElement("iDoc");
        WriteMandatoryNodeWithAttributeAndValue(dc,"dType","id",String.valueOf(Documents.PASSPORT_RF_EQUIFAX_RS),Documents.PASSPORT_RF_TEXT.toUpperCase());
        WriteMandatoryNodeWithAttributeAndValue(dc,"dNum","dSer",doc.getSeries(),doc.getNumber());
        WriteMandatoryNodeWithTwoAttributesAndValue(dc, "dIssue", "diDate", DatesUtils.DATE_FORMAT_ddMMYYYY.format(doc.getDocdate()),
      		  "diCode", Convertor.fromMask(doc.getDocorgcode()), doc.getDocorg().toUpperCase());
        //пишем снилс и инн, если есть
        WriteNodeWithText(tnp,"iITN",peopleMain.getInn());
        WriteNodeWithText(tnp,"iPens",peopleMain.getSnils());
        return tnp;
	}
	
	/**
	 * пишем блок с адресами для РС
	 * @param root - куда пишем
	 * @param address1 - адрес регистрации
	 * @param address2 - адрес проживания
	 * @return
	 */
	private static Element createAddressForRS(Element root, AddressEntity address1,AddressEntity address2){
		//блок по человеку
        Element ind=root.addElement("Individual");
  
        //Адреса
        Element adrreg=ind.addElement("iAddress");
        //Адрес регистрации
        //вид адреса
        WriteMandatoryNodeWithAttribute(adrreg,"addrCapt","id", String.valueOf(BaseAddress.REGISTER_ADDRESS_RS));
           
        Element areg=adrreg.addElement("addrRU");
        //регион
        if (address1.getRegionShort()==null){
        	WriteMandatoryNodeWithAttributeAndValue(areg,"region","id","00","НЕИЗВЕСТНО");
        } else {
            WriteMandatoryNodeWithAttributeAndValue(areg,"region","id", 
            		address1.getRegionShort().getCodereg().substring(0, 1).equals("0")?address1.getRegionShort().getCodereg().substring(1, 2):address1.getRegionShort().getCodereg(),
            		address1.getRegionName().toUpperCase());
        }

        //сельский район
        if (StringUtils.isNotEmpty(address1.getAreaName())) {
    	    WriteMandatoryNodeWithAttributeAndValue(areg,"district","ext", address1.getAreaExt().toUpperCase(),address1.getAreaName().toUpperCase());

        }
        //город
        if (StringUtils.isNotEmpty(address1.getCityName())) {
    	    WriteMandatoryNodeWithAttributeAndValue(areg,"city","ext", address1.getCityExt().toUpperCase(),address1.getCityName().toUpperCase());
    
        }
        //село
        if (StringUtils.isNotEmpty(address1.getPlaceName())) {
    	    WriteMandatoryNodeWithAttributeAndValue(areg,"village","ext", address1.getPlaceExt().toUpperCase(),address1.getPlaceName().toUpperCase());
        }
        //улица
        WriteMandatoryNodeWithAttributeAndValue(areg,"street","ext", 
    		StringUtils.isNotEmpty(address1.getStreetExt())?address1.getStreetExt().toUpperCase():"УЛ",
    				StringUtils.isNotEmpty(address1.getStreetName())?address1.getStreetName().toUpperCase():"НЕИЗВЕСТНО");
  
        WriteNodeWithText(adrreg,"hN",address1.getHouse());
        WriteNodeWithText(adrreg,"bN",address1.getBuilding());
        WriteNodeWithText(adrreg,"bS",address1.getCorpus());
        WriteNodeWithText(adrreg,"ap",address1.getFlat());
  
        //адрес проживания
        if (address1.getIsSame()!=BaseAddress.IS_SAME&&address2!=null) {
           Element adrreg1=ind.addElement("iAddress");	
	       //вид адреса
           WriteMandatoryNodeWithAttribute(adrreg1,"addrCapt","id", String.valueOf(BaseAddress.RESIDENT_ADDRESS_RS));	
                    
           Element a1reg=adrreg1.addElement("addrRU");
           //регион
           if (address2.getRegionShort()==null){
           	   WriteMandatoryNodeWithAttributeAndValue(a1reg,"region","id","00","НЕИЗВЕСТНО");
           } else {
               WriteMandatoryNodeWithAttributeAndValue(a1reg,"region","id", 
            		   address2.getRegionShort().getCodereg().substring(0, 1).equals("0")?address2.getRegionShort().getCodereg().substring(1, 2):address2.getRegionShort().getCodereg(),
            		   address2.getRegionName().toUpperCase());
           }
           //сельский район
           if (StringUtils.isNotEmpty(address2.getAreaName())) {
    	       WriteMandatoryNodeWithAttributeAndValue(a1reg,"district","ext", address2.getAreaExt().toUpperCase(),address2.getAreaName().toUpperCase());

           }
           //город
           if (StringUtils.isNotEmpty(address2.getCityName())) {
    	       WriteMandatoryNodeWithAttributeAndValue(a1reg,"city","ext", address2.getCityExt().toUpperCase(),address2.getCityName().toUpperCase());
           } 
           //село
           if (StringUtils.isNotEmpty(address2.getPlaceName())) {
    	       WriteMandatoryNodeWithAttributeAndValue(a1reg,"village","ext", address2.getPlaceExt().toUpperCase(),address2.getPlaceName().toUpperCase());
           }
       
           //улица
           WriteMandatoryNodeWithAttributeAndValue(a1reg,"street","ext", 
      		    StringUtils.isNotEmpty(address2.getStreetExt())?address2.getStreetExt().toUpperCase():"УЛ",
      				StringUtils.isNotEmpty(address2.getStreetName())?address2.getStreetName().toUpperCase():"НЕИЗВЕСТНО");
      
           WriteNodeWithText(adrreg1,"hN",address2.getHouse());
           WriteNodeWithText(adrreg1,"bN",address2.getBuilding());
           WriteNodeWithText(adrreg1,"bS",address2.getCorpus());
           WriteNodeWithText(adrreg1,"ap",address2.getFlat());
        }
        return ind;
	}
    
	/**
	 * пишем блок с кредитом
	 * @param root - элемент, куда пишем
	 * @param creditRequest - заявка
	 * @param credit - кредит
	 * @param details - операция
	 * @return
	 */
	private static Element createCreditBlockForRS(Element root,CreditRequestEntity creditRequest,
			CreditEntity credit,CreditDetailsEntity details){
		 //блок по кредиту
        Element ccontract=root.addElement("CreditContract");
    
        //идентификатор договора,  id кредита
        WriteNodeWithMandatoryText(ccontract,"ccId",credit.getId().toString());
        //идентификатор,  id кредитной заявки
        WriteNodeWithMandatoryText(ccontract,"caId",creditRequest.getId().toString());
        //номер договора
        WriteMandatoryNodeWithAttributeAndValue(ccontract,"ccNumber","type", BaseCredit.DOGOVOR_UPLOADING_RSTANDART,credit.getCreditAccount());
       
        //сумма кредита
        WriteNodeWithMandatoryText(ccontract,"ccAmount",Convertor.toDecimalString(CalcUtils.dformat, credit.getCreditsum()));
        //полная стоимость кредита -  сюда пишем creditSumBack
        WriteNodeWithMandatoryText(ccontract,"ccTotalAmount",Convertor.toDecimalString(CalcUtils.dformat, details.getAmountAll()));
        //валюта договора
        WriteMandatoryNodeWithTwoAttributesAndValue(ccontract,"ccCurrency","id",String.valueOf(BaseCredit.CURRENCY_RUR_ID),"code", BaseCredit.CURRENCY_RUR,BaseCredit.CURRENCY_RUR_TEXT);
     
        //дата кредита
        WriteNodeWithMandatoryText(ccontract,"ccDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg()));
        //дата окончания кредита
        WriteNodeWithMandatoryText(ccontract,"ccExpirationDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(details.getEventEndDate()));
        return ccontract;
	}
	
	/**
	 * пишем инфо-часть для РС если открылся или закрылся кредит
	 * @param root - элемент, куда пишем
	 * @param creditRequest - заявка
	 * @param credit - кредит
	 * @param peopleMain - человек
	 * @param cmdId - № команды
	 * @param stageId - вид изменений
	 * @param stageDescr - описание изменений
	 * @param isOver - ставим true при передаче информации о закрытии
	 * @return
	 */
	private static Element createCreditInfoApproveCreditForRS(Element root,CreditRequestEntity creditRequest,
			CreditEntity credit, PeopleMainEntity peopleMain,int cmdId,int stageId,String stageDescr,boolean isOver){
	    Element info2=root.addElement("AddInfo");
        WriteNodeWithMandatoryText(info2,"cmdId",String.valueOf(cmdId));
        //дата актуальности
        if (isOver) {
            WriteNodeWithMandatoryText(info2,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getDateStatus()));
        } else {
        	WriteNodeWithMandatoryText(info2,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdatabeg()));
        }
        WriteNodeWithMandatoryText(info2,"sId",peopleMain.getId().toString()); 
        WriteMandatoryNodeWithAttribute(info2,"srcType","id",String.valueOf(Organization.MFO_TYPE));
        //стадия
        WriteMandatoryNodeWithAttributeAndValue(info2,"ccStage","id",String.valueOf(stageId),stageDescr);
        Element crequest2=info2.addElement("CreditApplication");
        //идентификатор,  id кредитной заявки
        WriteNodeWithMandatoryText(crequest2,"caId",creditRequest.getId().toString());
        //идентификатор договора,  id кредита
        WriteNodeWithMandatoryText(crequest2,"ccId",credit.getId().toString());
        //номер договора
        WriteMandatoryNodeWithAttributeAndValue(crequest2,"ccNumber","type",BaseCredit.DOGOVOR_UPLOADING_RSTANDART,credit.getCreditAccount());
        //дата окончания фактическая
        if (credit.getCreditdataendfact()!=null&&isOver) {
            WriteNodeWithText(info2,"ccCloseDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(credit.getCreditdataendfact()));
        }
        return info2;
	}
	
	/**
	 * пишем инфо-часть для РС, при подаче
     * @param root - элемент, куда пишем
	 * @param creditRequest - кредитная заявка
	 * @param peopleMain - человек
	 * @param cmdId - № команды
	 * @return
	 */
	private static Element createCreditInfoForRS(Element root,CreditRequestEntity creditRequest,
			PeopleMainEntity peopleMain,int cmdId){
	 
		Element info=root.addElement("AddInfo");
        WriteNodeWithMandatoryText(info,"cmdId",String.valueOf(cmdId));
     
        //дата актуальности
        WriteNodeWithMandatoryText(info,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDatecontest()));
        WriteNodeWithMandatoryText(info,"sId",peopleMain.getId().toString());  
        WriteMandatoryNodeWithAttribute(info,"srcType","id",String.valueOf(Organization.MFO_TYPE));
        WriteMandatoryNodeWithAttributeAndValue(info,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_RS_FILLED),CreditStatus.UPLOAD_FILLED_TEXT);
        //первоначальная заявка
        Element crequest=info.addElement("CreditApplication");
        //идентификатор,  id кредитной заявки
        WriteNodeWithMandatoryText(crequest,"caId",creditRequest.getId().toString());
        //идентификатор,  id кредитной заявки
        WriteNodeWithMandatoryText(crequest,"caNumber",creditRequest.getUniquenomer());
        //дата заявки
        WriteNodeWithMandatoryText(crequest,"caDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDatecontest()));
        if (creditRequest.getWayId().getCodeinteger()==RefCreditRequestWay.DIRECT){
        	//способ подачи - в офисе
        	WriteMandatoryNodeWithAttribute(crequest,"caMethod","id",String.valueOf(CreditRequest.UPLOAD_WAY_CREDITREQUEST_EQUIFAX));
        } else {
        	//способ подачи - дистанционный
            WriteMandatoryNodeWithAttribute(crequest,"caMethod","id",String.valueOf(CreditRequest.UPLOAD_WAY_CREDITREQUEST_RS));
        }
        //вид займа - онлайн займ
        WriteMandatoryNodeWithAttribute(crequest,"ccTypeRequested","id",String.valueOf(BaseCredit.MICRO_CREDIT_ONLINE_UPLOADING_RSTANDART));
        
        return info;
	} 
	
	/**
	 * пишем инфо часть, если по заявке сразу же есть решение
	 * @param root - корнево элемент
	 * @param creditRequest - заявка
	 * @param peopleMain - человек
	 * @param cmdId - id команды
	 * @return
	 */
	private static int createCreditInfoDefinedForRS(Element root,CreditRequestEntity creditRequest,
			PeopleMainEntity peopleMain,int cmdId){
	   //пишем информационную часть кредитной заявки - обязательна еще одна часть
       Element info1=root.addElement("AddInfo");
       WriteNodeWithMandatoryText(info1,"cmdId",String.valueOf(cmdId));
       //дата актуальности
       if (creditRequest.getDateDecision()!=null){
    	   WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
       } else {
           if (creditRequest.getAcceptedcreditId()==null){
               WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateStatus()));
           } else {
	           WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateSign())); 
           }
       }
       WriteNodeWithMandatoryText(info1,"sId",peopleMain.getId().toString()); 

       int stage=0;
       //заявка отказана
       if (creditRequest.getAcceptedcreditId()==null&&creditRequest.getStatusId().getId()==CreditStatus.REJECTED){
           WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_REJECTED),CreditStatus.UPLOAD_REJECTED_TEXT);
           stage=1;
       }
       //заявка одобрена и выдан кредит - сначала пишем об одобрении, если передаем исторические данные, то смотрим и завершенные заявки, и заявки с долгами
       if (creditRequest.getStatusId().getId()==CreditStatus.DECISION||creditRequest.getStatusId().getId()==CreditStatus.CLOSED
		      ||creditRequest.getStatusId().getId()==CreditStatus.FOR_COLLECTOR||creditRequest.getStatusId().getId()==CreditStatus.FOR_COURT){
           WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_APPROVED),CreditStatus.UPLOAD_APPROVED_TEXT);
           stage=2;
           if (creditRequest.getAcceptedcreditId()!=null){
              stage=5;
           }
       }
       //клиент отказался
       if (creditRequest.getStatusId().getId()==CreditStatus.CLIENT_REFUSE){
           WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_RS_APPROVED_CLIENT_REFUSE),CreditStatus.UPLOAD_RS_APPROVED_CLIENT_REFUSE_TEXT);
           stage=3;
       }

       //одобренный заем - вид
       if (stage==2||stage==5){
	       //вид займа - онлайн займ
           WriteMandatoryNodeWithAttribute(info1,"ccTypeApproved","id",String.valueOf(BaseCredit.MICRO_CREDIT_ONLINE_UPLOADING_RSTANDART));
       }

       Element crequest1=info1.addElement("CreditApplication");
       //идентификатор,  id кредитной заявки
       WriteNodeWithMandatoryText(crequest1,"caId",creditRequest.getId().toString());
       //если заявка была одобрена
       if (stage==2||stage==5){
           //дата одобрения заявки
    	   if (creditRequest.getDateDecision()!=null){
    		   WriteNodeWithMandatoryText(crequest1,"ccApprovalDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
    	   } else {
               WriteNodeWithMandatoryText(crequest1,"ccApprovalDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateFill()));
    	   }
           //дата окончания одобрения заявки - плюс 5 дней
           WriteNodeWithMandatoryText(crequest1,"ccApprovalExpirationDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditRequest.getDateFill(),5)));
       }
    
       //если по заявке был отказ
       if (creditRequest.getRejectreasonId()!=null&&stage==1){
           Element crefuse=crequest1.addElement("ccRefusal");
           //причина отказа - сделать справочник???
           WriteMandatoryNodeWithAttributeAndValue(crefuse,"rfReason","id", CreditRequest.UPLOAD_REFUSE_REASON_RS,CreditRequest.UPLOAD_REFUSE_REASON_TEXT);
           //сумма заявки
           WriteNodeWithMandatoryText(crefuse,"rfAmount",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
           //валюта договора
           WriteMandatoryNodeWithTwoAttributesAndValue(crefuse,"rfCurrency","id",String.valueOf(BaseCredit.CURRENCY_RUR_ID),"code", BaseCredit.CURRENCY_RUR,BaseCredit.CURRENCY_RUR_TEXT);
           //дата отказа
           if (creditRequest.getDateDecision()!=null){
        	   WriteNodeWithMandatoryText(crefuse,"rfDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
           } else {
               WriteNodeWithMandatoryText(crefuse,"rfDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateStatus()));
           }
       }
       return stage;
	}
	
	/**
	 * пишем инфо-часть для РС, если было одобрение
     * @param root - элемент, куда пишем
	 * @param creditRequest - кредитная заявка
	 * @param peopleMain - человек
	 * @param cmdId - № команды
	 * @return
	 */
	private static Element createCreditInfoApproveForRS(Element root,CreditRequestEntity creditRequest,
			PeopleMainEntity peopleMain,int cmdId){
	    //пишем информационную часть кредитной заявки - обязательна еще одна часть
        Element info1=root.addElement("AddInfo");
        WriteNodeWithMandatoryText(info1,"cmdId",String.valueOf(cmdId));
   
       //дата актуальности
       if (creditRequest.getDateDecision()!=null){
    	   WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
       } else {
           WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateFill()));
       }
       WriteNodeWithMandatoryText(info1,"sId",peopleMain.getId().toString()); 
   
       WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_APPROVED),CreditStatus.UPLOAD_APPROVED_TEXT);
     
       //одобренный займ - вид
       //вид займа - онлайн займ
       WriteMandatoryNodeWithAttribute(info1,"ccTypeApproved","id",String.valueOf(BaseCredit.MICRO_CREDIT_ONLINE_UPLOADING_RSTANDART));
          
       Element crequest1=info1.addElement("CreditApplication");
       //идентификатор,  id кредитной заявки
       WriteNodeWithMandatoryText(crequest1,"caId",creditRequest.getId().toString());
  
       //дата одобрения заявки
       if (creditRequest.getDateDecision()!=null){
    	   WriteNodeWithMandatoryText(crequest1,"ccApprovalDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
    	   //дата окончания одобрения заявки - плюс 5 дней
           WriteNodeWithMandatoryText(crequest1,"ccApprovalExpirationDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditRequest.getDateDecision(),5))); 
       } else {
           WriteNodeWithMandatoryText(crequest1,"ccApprovalDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateFill()));
           //дата окончания одобрения заявки - плюс 5 дней
           WriteNodeWithMandatoryText(crequest1,"ccApprovalExpirationDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(DateUtils.addDays(creditRequest.getDateFill(),5)));
       }
      
       return info1;
    }
	
	/**
	 * пишем инфо-часть для РС, если был отказ клиента или отклонение
	 * @param root - элемент, куда пишем
	 * @param creditRequest - кредитная заявка
	 * @param peopleMain - человек
	 * @param cmdId - № команды
	 * @return
	 */
    private static Element createCreditInfoRejectForRS(Element root,CreditRequestEntity creditRequest,
			PeopleMainEntity peopleMain,int cmdId){
	
		Element info1=root.addElement("AddInfo");
        WriteNodeWithMandatoryText(info1,"cmdId",String.valueOf(cmdId));
       
        //дата актуальности
        WriteNodeWithMandatoryText(info1,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateStatus()));
     
        WriteNodeWithMandatoryText(info1,"sId",peopleMain.getId().toString()); 
       
        //заявка отказана
        if (creditRequest.getStatusId().getId()==CreditStatus.REJECTED){
  	      WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_REJECTED),CreditStatus.UPLOAD_REJECTED_TEXT);
  	  
        }
        //клиент отказался
        if (creditRequest.getStatusId().getId()==CreditStatus.CLIENT_REFUSE){
  	      WriteMandatoryNodeWithAttributeAndValue(info1,"ccStage","id",String.valueOf(CreditStatus.UPLOAD_RS_APPROVED_CLIENT_REFUSE),CreditStatus.UPLOAD_RS_APPROVED_CLIENT_REFUSE_TEXT);
  	  
        }
                   
        Element crequest1=info1.addElement("CreditApplication");
        //идентификатор,  id кредитной заявки
        WriteNodeWithMandatoryText(crequest1,"caId",creditRequest.getId().toString());
       
        //если по заявке был отказ
        if (creditRequest.getRejectreasonId()!=null){
            Element crefuse=crequest1.addElement("ccRefusal");
            //причина отказа - сделать справочник???
            WriteMandatoryNodeWithAttributeAndValue(crefuse,"rfReason","id", CreditRequest.UPLOAD_REFUSE_REASON_RS,CreditRequest.UPLOAD_REFUSE_REASON_TEXT);
            //сумма заявки
            WriteNodeWithMandatoryText(crefuse,"rfAmount",Convertor.toDecimalString(CalcUtils.dformat,creditRequest.getCreditsum()));
            //валюта договора
            WriteMandatoryNodeWithTwoAttributesAndValue(crefuse,"rfCurrency","id",String.valueOf(BaseCredit.CURRENCY_RUR_ID),"code", BaseCredit.CURRENCY_RUR,BaseCredit.CURRENCY_RUR_TEXT);
            //дата отказа
            if (creditRequest.getDateDecision()!=null){
            	WriteNodeWithMandatoryText(crefuse,"rfDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateDecision()));
            } else {
                WriteNodeWithMandatoryText(crefuse,"rfDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditRequest.getDateStatus()));
            }
        }
        return info1;
	}
    
    /**
     * заголовок для выгрузки в РС
     * @param document - xml документ
     * @param version -версия выгрузки
     * @param codeWork - код компании
     * @param companyName - название компании
     * @param sendingDate - дата выгрузки
     * @param uploadId - номер выгрузки
     * @return
     */
    private static Element createHeaderRecordForRS(Document document,String version,String codeWork,
    		String companyName,Date sendingDate,String uploadId){
    	 //write header
        Element root = document.addElement( "UPLOADING" );
        //версия
        root.addAttribute("version", version);
        Element hdr=root.addElement("HEADER");
        //пишем наш id
        WriteNodeWithMandatoryText(hdr,"ownerId",codeWork);
        //пишем наше название 
        WriteNodeWithMandatoryText(hdr,"ownerDescription",companyName);
        //дата-время выгрузки
        WriteNodeWithMandatoryText(hdr,"uploadCreationDateTime",DatesUtils.DATE_FORMAT_ddMMYYYY_HHmmss.format(sendingDate));
        //уникальный номер загрузки в данное КБ
        WriteNodeWithMandatoryText(hdr,"uploadId",uploadId);
        Element cmd=root.addElement("COMMANDS");
        return cmd;
    }
    
	/**
	 * запись аннулирования кредита в РС
	 * @param root - элемент, в который пишем
	 * @param cmdId - номер команды
	 * @param credit - кредит
	 * @return
	 */
    private static Element createAnnulForRS(Element root,int cmdId,CreditEntity credit){
       Element annul=root.addElement("Annul");
   	   WriteNodeWithMandatoryText(annul,"cmdId",String.valueOf(cmdId));
   	  
   	   //идентификатор договора, id кредита
       WriteNodeWithMandatoryText(annul,"ccId",credit.getId().toString());
       //причина аннулирования
       WriteMandatoryNodeWithAttributeAndValue(annul,"annulReason","id", String.valueOf(BaseCredit.CREDIT_CANCEL_UPLOAD),BaseCredit.CREDIT_CANCEL_UPLOAD_REFUSE_TEXT);
       return annul;
    }
    
    /**
     * пишем платеж для РС
     * @param root - в какой элемент пишем
     * @param credit - кредит
     * @param peopleMain - человек
     * @param creditDetail - детали операции
     * @param cmdId - № команды
     * @param sumId - вид суммы
     * @param amount - сумма
     * @param isOut - если true, делаем сумму отрицательной
     * @return
     */
    private static Element createAddDataForRS(Element root,CreditEntity credit,
    		PeopleMainEntity peopleMain, CreditDetailsEntity creditDetail,
    		int cmdId,int sumId,Double amount,boolean isOut){
    	Element data=root.addElement("AddData");
        WriteNodeWithMandatoryText(data,"cmdId",String.valueOf(cmdId));
       
        WriteNodeWithMandatoryText(data,"sId",peopleMain.getId().toString());
        //идентификатор договора, id кредита
        WriteNodeWithMandatoryText(data,"ccId",credit.getId().toString());
        //id платежа
        WriteNodeWithMandatoryText(data,"dtId",creditDetail.getAnotherId().toString()+"-"+String.valueOf(sumId));
        //вид платежа
        WriteNodeWithMandatoryText(data,"dtType",String.valueOf(sumId));
        //дата платежа
        WriteNodeWithMandatoryText(data,"dtDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditDetail.getEventDate()));
        //сумма платежа
        if (isOut){
        	//если отдаем деньги
	        WriteNodeWithMandatoryText(data,"dtAmount",Convertor.toDecimalString(CalcUtils.dformat,-1*amount));
        } else {
        	//если получаем деньги
        	WriteNodeWithMandatoryText(data,"dtAmount",Convertor.toDecimalString(CalcUtils.dformat,amount));
        }
	    return data;    
    }
    /**
     * пишем заголовок для AddEvent
     * @param root - корневой элемент
     * @param peopleMain - человек
     * @param credit - кредит
     * @param eventDate - дата события
     * @param cmdId - № команды
     * @return
     */
    private static Element createAddEventHeaderForRS(Element root,PeopleMainEntity peopleMain,
    		CreditEntity credit,Date eventDate,int cmdId,Integer typeId){
    	 Element evt=root.addElement("AddEvent");
         WriteNodeWithMandatoryText(evt,"cmdId",String.valueOf(cmdId));
         
         WriteNodeWithMandatoryText(evt,"sId",peopleMain.getId().toString());
         //WriteNodeWithMandatoryText(evt,"srcId",peopleMain.getId().toString());
         //идентификатор договора, id кредита
         WriteNodeWithMandatoryText(evt,"ccId",credit.getId().toString());
         if (typeId!=null){
        	//вид события
             WriteNodeWithMandatoryText(evt,"evType",credit.getId().toString());
         }
         //дата начала кредита
         WriteNodeWithMandatoryText(evt,"evDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(eventDate));
         return evt;   	   
    }
    
    /**
     * пишем события для РС, касающееся долга
     * @param root - корневой элемент
     * @param peopleMain - человек
     * @param credit - кредит
     * @param cmdId - № команды
     * @return
     */
    private static Element createAddEventDebtForRS(Element root,PeopleMainEntity peopleMain,
    		CreditEntity credit,int cmdId){
    	Element evt=createAddEventHeaderForRS(root,peopleMain,credit,credit.getDateStatus(),
    			cmdId,BaseCredit.CREDIT_TYPE_COLLECTOR);
    	//пишем информацию по долгам
        Element inst=evt.addElement("evDebtCollection");
        WriteNodeWithMandatoryText(inst,"dcOtstAmnt",Convertor.toDecimalString(CalcUtils.dformat,credit.getCreditsumback()));
        return evt;
    }
   
   /**
    * пишем события для РС, касающееся денег 
    * @param root - корневой элемент
    * @param peopleMain - человек
    * @param credit - кредит
    * @param creditDetail - детали кредита
    * @param isNew - true если новый кредит
    * @param isOverdue - true если просрочка
    * @param isEnd - true если кредит завершен
    * @param cmdId - № команды
    * @return
    */
   private static Element createAddEventForRS(Element root,PeopleMainEntity peopleMain,
    		CreditEntity credit,CreditDetailsEntity creditDetail,boolean isNew,
    		boolean isOverdue,boolean isEnd,int cmdId){
    	 
    	 Element evt=createAddEventHeaderForRS(root,peopleMain,credit,
    			 creditDetail.getEventDate(),cmdId,null);
    			
         //пишем информацию по микрокредиту
         Element inst=evt.addElement("evMicrofinance");
         
         if (!isOverdue&&!isEnd){
             //дата следующего платежа - ставим дату окончания кредита
             WriteNodeWithMandatoryText(inst,"mfNextPaymentDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(creditDetail.getEventEndDate()));
         }
         
         //основной долг
         if (isEnd){
        	 WriteNodeWithMandatoryText(inst,"stOtstPrncAmnt","0.00");
         } else {
             WriteNodeWithMandatoryText(inst,"stOtstPrncAmnt",Convertor.toDecimalString(CalcUtils.dformat,creditDetail.getAmountMain()));
         }
         
         if (!isOverdue&&!isEnd){
             //сумма к возврату
             WriteNodeWithMandatoryText(inst,"stNextPayment",Convertor.toDecimalString(CalcUtils.dformat,creditDetail.getAmountAll()));
         }
         
         //если это не просрочка
         if (!isOverdue){
           
             //сумма просроченной задолженности - 0
	         WriteNodeWithMandatoryText(inst,"stOvrdAmnt","0.00");
         } else {
        	
             //сумма просроченной задолженности 
   	         WriteNodeWithMandatoryText(inst,"stOvrdAmnt",Convertor.toDecimalString(CalcUtils.dformat,creditDetail.getAmountAll()));
         }
	     
	     //длительность просроченной задолженности 
	     Element ovd=inst.addElement("stOvrdAmntDrtn");
	     ovd.addAttribute("type", BaseCredit.REF_RSTANDART);
	     //если новый кредит
	     if (isNew){
	    	 ovd.addText(BaseCredit.NEW_OVERDUE_RSTANDART); 
	     } else if (!isOverdue){
	    	 ovd.addText(BaseCredit.WITHOUT_OVERDUE); 
	     } else {
	         if (creditDetail.getDelay()!=null&&creditDetail.getDelay()>0){
	           //если статус просрочки поставлен	 
 	           if (credit.getOverduestateId()!=null){
 		           ovd.addText(credit.getOverduestateId().getCode());  
 	           } else {
 		           if (creditDetail.getDelay()<30){
	                  ovd.addText(BaseCredit.OVERDUE_BEFORE_MONTH);
 		           } else if (creditDetail.getDelay()>=30&&creditDetail.getDelay()<60){
 			          ovd.addText(BaseCredit.OVERDUE_MONTH); 
 		           } else if (creditDetail.getDelay()>=60&&creditDetail.getDelay()<90){
 			          ovd.addText(BaseCredit.OVERDUE_TWO_MONTH);
 		           } else if (creditDetail.getDelay()>=90&&creditDetail.getDelay()<120){
 			          ovd.addText(BaseCredit.OVERDUE_THREE_MONTH);
 		           } else if (creditDetail.getDelay()>=120){
 			          ovd.addText(BaseCredit.OVERDUE_FOUR_MONTH);
 		           }
 	            }
	      
            } else {
    	            ovd.addText(BaseCredit.WITHOUT_OVERDUE); 
	        }
	    }
	    return evt;    
    }
    
   /**
    * изменения контракта для РС
    * @param root - корневой элемент
    * @param date - дата изменений
    * @param status - вид
    * @return
    */
    private static Element createChangesForRS(Element root,Date date,String type){
    	 //если это не новая заявка и менялся статус, пишем данные продления
        Element cchange=root.addElement("ccChanges");
        cchange.addAttribute("date", DatesUtils.DATE_FORMAT_ddMMYYYY.format(date));
        cchange.addAttribute("id",type);
        return cchange;
    }
    
    /**
     * 
     * @param root - корневой элемент
     * @param actualDate - дата актуальности
     * @param peopleMain - человек
     * @param cmdId - команда
     * @return
     */
    private static Element createTitleAddMainForRS(Element root,Date actualDate,
    		PeopleMainEntity peopleMain,int cmdId){
    	
    	 Element mnp=root.addElement("AddMain");
    	 WriteNodeWithMandatoryText(mnp,"cmdId",String.valueOf(cmdId));
         //дата актуальности
         WriteNodeWithMandatoryText(mnp,"dateOfActuality",DatesUtils.DATE_FORMAT_ddMMYYYY.format(actualDate));
         //идентификатор человека
         WriteNodeWithMandatoryText(mnp,"sId",peopleMain.getId().toString());
         return mnp;
    }
    
    /**
     * информация о долгах для РС
     * @param root - корневой элемент
     * @param debt - долг
     * @param credit - кредит
     * @param isCourtDecision - есть ли решение суда
     * @param terminationDate - дата прекращения выгрузки
     * @return
     */
    private static Element createDebtInfoForRs(Element root,DebtEntity debt,
    		CreditEntity credit,boolean isCourtDecision,Date terminationDate){
    	//долговые обязательства в суде
        Element cdebt=root.addElement("DebtObligations");
        //id долга
        WriteNodeWithMandatoryText(cdebt,"doId",debt.getId().toString());
        //номер займа
        WriteNodeWithMandatoryText(cdebt,"doNumber",credit.getCreditAccount());
        //вид долга - неоплата по кредиту
        WriteMandatoryNodeWithAttribute(cdebt,"doType","id", String.valueOf(Debt.DEBT_UPLOAD));
        //сумма долга
        WriteNodeWithMandatoryText(cdebt,"doAmount",Convertor.toDecimalString(CalcUtils.dformat,debt.getAmount()));
        //валюта договора
        WriteMandatoryNodeWithTwoAttributesAndValue(cdebt,"doCurrency","id",String.valueOf(BaseCredit.CURRENCY_RUR_ID),"code", BaseCredit.CURRENCY_RUR,BaseCredit.CURRENCY_RUR_TEXT);
        //дата передачи на взыскание
        WriteNodeWithMandatoryText(cdebt,"doDate",DatesUtils.DATE_FORMAT_ddMMYYYY.format(debt.getDateDebt()));
        //если мы передаем решение суда
        if (isCourtDecision){
            //решение суда
            Element court=cdebt.addElement("doCourtInfo");
            WriteNodeWithMandatoryText(court,"courtInfo",debt.getComment());
            
        }
        //прекращение выгрузки
        WriteMandatoryNodeWithTwoAttributesAndValue(root,"processingTermination","id",String.valueOf(BaseCredit.CREDIT_CANCEL_UPLOAD_COURT),"date", 
        		DatesUtils.DATE_FORMAT_ddMMYYYY.format(terminationDate),BaseCredit.CREDIT_CANCEL_UPLOAD_COURT_TEXT); 
        return cdebt;
    }
    
    /**
     * пишем запись для удаления кредита в БД РС
     * @param sendingDate - дата отправки
     * @param codeWork - код в КБ
     * @param companyName - название организации
     * @param uploadId - номер выгрузки
     * @param version - версия
     * @param credit - кредит
     * @return
     */
    public static String createDeleteRecordForSendingToRStandard( Date sendingDate, 
	    String codeWork, String companyName, String uploadId,String version, CreditEntity credit) {
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
	    //пишем заголовок
        Element cmd=createHeaderRecordForRS(document,version,codeWork,companyName,sendingDate,uploadId);
        //пишем аннулирование
        createAnnulForRS(cmd,1,credit);
        return document.asXML();
    }
    
	/**
	 * пишем записи для выдачи в Русский стандарт 
	 * @param lobj - массив с данными
	 * 0 - № записи
	 * 1 - peopleMain
	 * 2 - peoplePersonal ПД
	 * 3 - document атрибуты документа, серия и номер передаются одним полем без пробелов
	 * 4 - address атрибуты адреса регистрации
	 * 5 - address атрибуты адреса проживания
	 * 6 - creditRequest - заявка на кредит
	 * 7 - credit - кредит
	 * 8- платежи за период с последней выгрузки по текущую дату
	 * 9- информация о кредите
	 * 10 - информация о продлениях
	 * 11 - информация о просрочке
	 * 12 - информация о закрытии займа
	 * 13 - информация о передаче в суд
	 * 14 - есть ли решение суда
	 * par - реквизиты для партнера
	 * @param sendingDate - дата выгрузки
	 * @param codeWork - наш id в КБ
	 * @param companyName - название нашей организации
	 * @param uploadId - уникальный номер выгрузки
	 * @param version - версия схемы выгрузки
	 * @param dat - дата предыдущей выгрузки
	 * @throws IOException 
	 */
	 
	public static String createUploadForSendingToRStandard
	( Date sendingDate, 
	  List<Object[]> lobj,
	  String codeWork,
	  String companyName, 
	  String uploadId,
	  String version,
	  Date dat) {
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
	    //пишем заголовок
        Element cmd=createHeaderRecordForRS(document,version,codeWork,companyName,sendingDate,uploadId);
        //счетчик команд
        int i=1;
        //идем по списку заявок
        for (Object[] row: lobj)
        {
          //если выгружаем первый раз данные по человеку - уточнить, возможно оставить условие только на CreditRequest
          if (!((CreditRequestEntity) row[6]).getIsUploaded()) {
              //добавляем новый заголовок для человека
              createIndividualForRS(cmd,((PeopleMainEntity) row[1]),
            		  ((PeoplePersonalEntity) row[2]),((DocumentEntity) row[3]),i);
              //увеличили счетчик
              i++; 
          }//end ПД
          
         
          int stage=0;
          //информация по заявке, если еще не передавалась
          if (!((CreditRequestEntity) row[6]).getIsUploaded()){
        	  //пишем информационную часть кредитной заявки - обязательна для всех, поступление
              
        	  createCreditInfoForRS(cmd,((CreditRequestEntity) row[6]),((PeopleMainEntity) row[1]),i);
        	  //увеличили счетчик
        	  i++;
              
              //если по заявке уже что-то определилось
              if (((CreditRequestEntity) row[6]).getStatusId().getId()!=CreditStatus.FILLED){
            	  //вернули стадию, на которой находится заявка
            	  stage=createCreditInfoDefinedForRS(cmd,((CreditRequestEntity) row[6]),
            			  ((PeopleMainEntity) row[1]),i);
                  //увеличили счетчик
                  i++;
              }
              
              //если по заявке есть кредит, пишем еще одну инфо-часть
              //смотреть по дате подписания и последней дате выгрузки???
              if (((CreditRequestEntity) row[6]).getAcceptedcreditId()!=null&&stage==5){
              	
              	    createCreditInfoApproveCreditForRS(cmd,((CreditRequestEntity) row[6]),
            	    		((CreditEntity) row[7]), ((PeopleMainEntity) row[1]),i,
            	    		CreditStatus.UPLOAD_RS_APPROVED_WITH_CREDIT,
            	    		CreditStatus.UPLOAD_RS_APPROVED_WITH_CREDIT_TEXT,false);
              	    //увеличили счетчик
                    i++;
                    
              }// end info about credit
              
        }//end информация по заявке
          
        //если заявка была отказана, и мы не выгружали эти данные, пишем еще одну инфо-часть  
        if (((CreditRequestEntity) row[6]).getAcceptedcreditId()==null
          		&&((CreditRequestEntity) row[6]).getIsUploaded()
          		&&((CreditRequestEntity) row[6]).getDateStatus().after(dat)
          		&&((((CreditRequestEntity) row[6]).getStatusId().getId()==CreditStatus.CLIENT_REFUSE)
          		||(((CreditRequestEntity) row[6]).getStatusId().getId()==CreditStatus.REJECTED))){
        	 
        	createCreditInfoRejectForRS(cmd,((CreditRequestEntity) row[6]),	
        			((PeopleMainEntity) row[1]),i);
        	//увеличили счетчик
        	i++;
            
        }//end если заявка была отказана, и мы не выгружали эти данные
        
        
        //по заявке одобрили кредит, данные не выгружали, кредита пока нет
        if (((CreditRequestEntity) row[6]).getAcceptedcreditId()==null
        		&&((CreditRequestEntity) row[6]).getDateStatus().after(dat)
        	    &&((CreditRequestEntity) row[6]).getIsUploaded()
        		&&((CreditRequestEntity) row[6]).getStatusId().getId()==CreditStatus.DECISION){
        	
           	createCreditInfoApproveForRS(cmd,((CreditRequestEntity) row[6]),
        			((PeopleMainEntity) row[1]),i);
            //увеличили счетчик
            i++;
        }
       
        
        //если по заявке есть кредит, а мы еще не выгружали данные об этом, пишем еще одну инфо-часть
        //смотреть по дате подписания и последней дате выгрузки???
        if (((CreditRequestEntity) row[6]).getAcceptedcreditId()!=null
        		&&((CreditRequestEntity) row[6]).getIsUploaded()
        		&&((CreditRequestEntity) row[6]).getStatusId().getId()!=CreditStatus.CLIENT_REFUSE
        		&&((CreditRequestEntity) row[6]).getDateSign().after(dat)
        		&&((CreditRequestEntity) row[6]).getAcceptedcreditId().getIsactive()==ActiveStatus.ACTIVE){
        	
           	  createCreditInfoApproveCreditForRS(cmd,((CreditRequestEntity) row[6]),
    	    		((CreditEntity) row[7]), ((PeopleMainEntity) row[1]),i,
    	    		CreditStatus.UPLOAD_RS_APPROVED_WITH_CREDIT,
    	    		CreditStatus.UPLOAD_RS_APPROVED_WITH_CREDIT_TEXT,false);
           	  //увеличили счетчик
              i++;
        }// end info about credit
          
        //пишем инфо часть, если займ закрылся
        if (((CreditDetailsEntity) row[12])!=null) {
        	  //пишем информационную часть кредитной заявки -для закрытого кредита
        	
        	 createCreditInfoApproveCreditForRS(cmd,((CreditRequestEntity) row[6]),
     	    		((CreditEntity) row[7]), ((PeopleMainEntity) row[1]),i,
     	    		CreditStatus.UPLOAD_RS_CLOSED,CreditStatus.UPLOAD_RS_CLOSED_TEXT,true);
        	 //увеличили счетчик
        	 i++; 
          
        }//end инфо часть,если займ закрылся
          
          
        //пишем основную часть - если у нас есть кредит - уточнить?????
        if (((CreditDetailsEntity) row[9])!=null&&((CreditRequestEntity) row[6]).getStatusId().getId()!=CreditStatus.CLIENT_REFUSE){
           
        	//основная часть
        	Element mnp=createTitleAddMainForRS(cmd,((CreditEntity) row[7]).getCreditdatabeg(),
        			((PeopleMainEntity) row[1]),i);
        
            //пишем адрес, если раньше его не выгружали
            if (!((CreditEntity) row[7]).getIsUploaded()){
                //блок по адресу человека
            	createAddressForRS(mnp, ((AddressEntity) row[4]),((AddressEntity) row[5]));
            
            }//end пишем адрес, если его не выгружали
          
            //блок по кредиту
            createCreditBlockForRS(mnp,((CreditRequestEntity) row[6]),
            		((CreditEntity) row[7]),((CreditDetailsEntity) row[9]));
            //увеличили счетчик
            i++;
                
        }//end есть кредит и его не выгружали
        
          
        //если по заявке есть кредит и он передан коллекторам
        if (((CreditRequestEntity) row[6]).getAcceptedcreditId()!=null
       		  &&((CreditRequestEntity) row[6]).getAcceptedcreditId().getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_COLLECTOR){
      
        	// TODO передача коллектору
        	//основная часть
        	Element mnp=createTitleAddMainForRS(cmd,((CreditEntity) row[7]).getDateStatus(),
        			((PeopleMainEntity) row[1]),i);
        	//прекращение выгрузки
            WriteMandatoryNodeWithTwoAttributesAndValue(mnp,"processingTermination","id",
            		String.valueOf(BaseCredit.CREDIT_CANCEL_UPLOAD_COLLECTOR),"date", DatesUtils.DATE_FORMAT_ddMMYYYY.format(((CreditEntity) row[7]).getDateStatus()),
            		BaseCredit.CREDIT_CANCEL_UPLOAD_COLLECTOR_TEXT);
           
        	//увеличили счетчик
            i++;
            //пишем событие
	   	    createAddEventDebtForRS(cmd,((PeopleMainEntity) row[1]),
          		  ((CreditEntity) row[7]),i);
	    	 
           //увеличили счетчик
           i++; 
          
        }
          
          //если по заявке есть кредит и он передан в суд, но нет решения
          if (((DebtEntity) row[13])!=null&&!((Boolean) row[14])){
        	  
          }
          
          //если по заявке есть кредит и он передан в суд и есть решение
          if (((DebtEntity) row[13])!=null&&((Boolean) row[14])){
        	  
        	  //основная часть
    		  Element mnp=createTitleAddMainForRS(cmd,((DebtEntity) row[13]).getDateDecision(),
          			((PeopleMainEntity) row[1]),i);
        	  
    		  //долговые обязательства в суде 
    		  createDebtInfoForRs(mnp,((DebtEntity) row[13]),
    				  ((CreditEntity) row[7]),true,((DebtEntity) row[13]).getDateDecision());
            
              //увеличили счетчик
              i++; 
          }
          
          //по заявке был выдан кредит, его выгрузили, но клиент не забрал деньги
          if (((CreditRequestEntity) row[6]).getAcceptedcreditId()!=null
        		  &&((CreditRequestEntity) row[6]).getStatusId().getId()==CreditStatus.CLIENT_REFUSE
        		  &&((CreditRequestEntity) row[6]).getAcceptedcreditId().getIsUploaded()){
        	  createAnnulForRS(cmd,i,((CreditEntity) row[7]));
        	  //увеличили счетчик
              i++; 
         }
          
           //передаем платежи и пр.
           //если по заявке есть кредит - уточнить?????
           if (((CreditRequestEntity) row[6]).getAcceptedcreditId()!=null&&((CreditRequestEntity) row[6]).getStatusId().getId()!=CreditStatus.CLIENT_REFUSE){
        	   
        	    //если еще не выгружали, пишем событие о кредите 
        	    if(((CreditDetailsEntity) row[9])!=null){
        	        //пишем событие
        	    	
        	    	 createAddEventForRS(cmd,((PeopleMainEntity) row[1]),
                   		  ((CreditEntity) row[7]),((CreditDetailsEntity) row[9]),true,false,false,i);
        	    	 
                    //увеличили счетчик
                    i++; 
                    
          	        //пишем платеж системы клиенту, если он уже проведен
          	        if (((CreditDetailsEntity) row[9]).getAnotherId()!=null){
          	        	
          	        	createAddDataForRS(cmd,((CreditEntity) row[7]),
          	        			((PeopleMainEntity) row[1]), ((CreditDetailsEntity) row[9]),
          	        			i,Payment.UPLOAD_ALL_SUM,((CreditDetailsEntity) row[9]).getAmountMain(),true);
          	          		
          	            //увеличили счетчик
                        i++; 
          	        }//end платеж системы клиенту
        	    }// end event по новому кредиту
              
        	    
        	   //пишем основную часть, если было продление
               if (((List<CreditDetailsEntity>) row[10]).size()>0){
              	  for (CreditDetailsEntity prolongDetail:((List<CreditDetailsEntity>) row[10])){
              	      //основная часть
              		  Element mnp=createTitleAddMainForRS(cmd,prolongDetail.getEventDate(),
                    			((PeopleMainEntity) row[1]),i);
              		  
              		
              		    //блок с кредитом
                        Element ccontract=createCreditBlockForRS(mnp,((CreditRequestEntity) row[6]),
                        		((CreditEntity) row[7]),prolongDetail);
                        
                        //если это не новая заявка и менялся статус, пишем данные продления
                        createChangesForRS(ccontract,prolongDetail.getEventDate(),BaseCredit.CHANGES_REASON);
                        //увеличили счетчик
                        i++;
                        
                        //событие
                        //пишем событие
                        createAddEventForRS(cmd,((PeopleMainEntity) row[1]),
                      		  ((CreditEntity) row[7]),prolongDetail,false,false,false,i);
                                 	      
              	        //увеличили счетчик
              	        i++;
              	  }//end for
                    
               }//end основная часть,если по заявке было продление
               
        	   //если кредит закончился, пишем событие о кредите 
        	   if(((CreditDetailsEntity) row[12])!=null){
        	        //пишем событие
        	    	
        	    	createAddEventForRS(cmd,((PeopleMainEntity) row[1]),
                      		  ((CreditEntity) row[7]),((CreditDetailsEntity) row[12]),false,false,true,i);
          	      
          	       //увеличили счетчик
                   i++; 
        	  }// end event по окончанию кредиту
        	    
        	  //если была просрочка, пишем информацию
              if (((List<CreditDetailsEntity>) row[11]).size()>0){
                	 for (CreditDetailsEntity overdue:((List<CreditDetailsEntity>) row[11])){
                		//пишем событие
                		 createAddEventForRS(cmd,((PeopleMainEntity) row[1]),
                         		  ((CreditEntity) row[7]),overdue,false,true,false,i);
              	    	 
                       //увеличили счетчик
    		           i++;  
                	 }//end for
                }//end если есть просрочка
        	    
        	    
                //если были платежи, пишем информацию
                if (((List<CreditDetailsEntity>) row[8]).size()>0){
                    for (CreditDetailsEntity payment:((List<CreditDetailsEntity>) row[8])){
                	    //пишем информацию по платежам
                    	//общая сумма
                    	
                    	createAddDataForRS(cmd,((CreditEntity) row[7]),
          	        			((PeopleMainEntity) row[1]), payment,
          	        			i,Payment.UPLOAD_ALL_SUM,payment.getAmountOperation(),false);
                            	
                      	//увеличили счетчик
                        i++; 
                  	    
                  	    //сумма процентов
                  	    if(payment.getAmountPercent()!=0d){
                  	    	
                  	    	createAddDataForRS(cmd,((CreditEntity) row[7]),
              	        			((PeopleMainEntity) row[1]), payment,
              	        			i,Payment.UPLOAD_PERCENT,payment.getAmountPercent(),true);
                            
                  	        //увеличили счетчик
                            i++; 
                        }
                        
                  	    //сумма основного долга, если по нему тоже была выплата
                  	    if(!payment.getAmountOperation().equals(payment.getAmountPercent())){
                  	    	 
                  	    	createAddDataForRS(cmd,((CreditEntity) row[7]),
              	        			((PeopleMainEntity) row[1]), payment,i,Payment.UPLOAD_MAIN_SUM,
              	        			payment.getAmountOperation()-payment.getAmountPercent(),true);
                  	                  	     
                  	        //увеличили счетчик
                            i++; 
                  	    }//end платеж основного долга
                    }//end for
                }//end платежи
                
           }//end если есть кредит
           
           
        }
        return document.asXML();
	       
	}
	
	/**
	 * пишем заголовок для выдачи в НБКИ 
	 * @param sendingDate - дата текущей выгрузки
	 * @param user - пользователь
	 * @param password - пароль
	 * @param version - версия выгрузки
	 * @param dateVersion - дата выгрузки
	 */
	public static StringBuilder createUploadHeaderForSendingToNbki (Date sendingDate, 
			String user,String password,String version,Date dateVersion) {
		StringBuilder sb=new StringBuilder();
		//заголовок для выгрузки
		sb.append(createHeaderRecordNbki(HEADER_TUTDF,user,password,
				version,dateVersion,sendingDate));
		//перевод строки
		sb.append("\r\n");
		return sb;
	}
	
	/**
	 * пишем записи для выдачи в НБКИ 
	 * @param previousDate - дата предыдущей выгрузки
	 * @param sendingDate - дата текущей выгрузки
	 * @param lobj - массив с данными
	 * 0 - № записи
	 * 1 - peopleMain
	 * 2 - peoplePersonal ПД
	 * 3 - peopleMisc - дополнительные данные человека
	 * 4 - document атрибуты документа, серия и номер передаются одним полем без пробелов
	 * 5 - addreg атрибуты адреса регистрации
	 * 6 - address атрибуты адреса проживания
	 * 7 - creditRequest - заявка на кредит
	 * 8 - credit - кредит
	 * 9 - payment - последний платеж
	 * 10 - phoneMobile - мобильный телефон
	 * 11 - phoneHome - домашний телефон
	 * 12 - сумма выплат по кредиту
	 * 13 - передача в суд
	 * 14 - есть ли решение суда
	 * 
	 */
	public static StringBuilder createUploadRecordForSendingToNbki (String user,Date previousDate, Date sendingDate, 
			Object[] row, StringBuilder sb) {
		
			//пишем заявки, если их еще не выгружали
			if (((CreditRequestEntity) row[7]).getDateStatus().after(previousDate)){
		      //документы
		      sb.append(createDocumentRecordNbki("ID01",((DocumentEntity) row[4])));
		      //перевод строки
			  sb.append("\r\n");
						  
			  //ПД
		      sb.append(createPeopleRecordNbki("NA01",((PeoplePersonalEntity) row[2])));
		      //перевод строки
			  sb.append("\r\n");
					  
			  //Адрес регистрации
			  sb.append(createAddressRecordNbki("AD01",BaseAddress.REGISTER_ADDRESS_RS,((AddressEntity) row[5]),
					((PeopleMiscEntity) row[3]),BaseAddress.REGISTER_ADDRESS_RS));
		      //перевод строки
			  sb.append("\r\n");
						  
			  //Адрес проживания
			  sb.append(createAddressRecordNbki("AD02",BaseAddress.RESIDENT_ADDRESS_RS,((AddressEntity) row[6]),
					((PeopleMiscEntity) row[3]),BaseAddress.RESIDENT_ADDRESS_RS));
		      //перевод строки
			  sb.append("\r\n");
					  
			  //телефон мобильный
			  if (((PeopleContactEntity) row[10])!=null){
			      sb.append(createPhoneRecordNbki("PN01",((PeopleContactEntity) row[10]),PeopleContact.CELL_PHONE_NBKI));
			      //перевод строки
			      sb.append("\r\n");
			  }
			  
			  //телефон домашний
			  if (((PeopleContactEntity) row[11])!=null){
				  sb.append(createPhoneRecordNbki("PN02",((PeopleContactEntity) row[11]),PeopleContact.HOME_PHONE_NBKI));
				  //перевод строки
				  sb.append("\r\n");
			  }
			  
			  
			  //кредитная заявка
			  sb.append(createCreditRequestRecordNbki("IP01",user,sendingDate,((CreditRequestEntity) row[7])));
			  //перевод строки
			  sb.append("\r\n");
			}//end пишем кредитные заявки
			
			//пишем кредит, если он есть
			if (((CreditRequestEntity) row[7]).getAcceptedcreditId()!=null){
				  //документы
			      sb.append(createDocumentRecordNbki("ID01",((DocumentEntity) row[4])));
			      //перевод строки
				  sb.append("\r\n");
							  
				  //ПД
			      sb.append(createPeopleRecordNbki("NA01",((PeoplePersonalEntity) row[2])));
			      //перевод строки
				  sb.append("\r\n");
						  
				  //Адрес регистрации
				  sb.append(createAddressRecordNbki("AD01",BaseAddress.REGISTER_ADDRESS_RS,((AddressEntity) row[5]),
						((PeopleMiscEntity) row[3]),BaseAddress.REGISTER_ADDRESS_RS));
			      //перевод строки
				  sb.append("\r\n");
						  
				  //Адрес проживания
				  sb.append(createAddressRecordNbki("AD02",BaseAddress.RESIDENT_ADDRESS_RS,((AddressEntity) row[6]),
						((PeopleMiscEntity) row[3]),BaseAddress.RESIDENT_ADDRESS_RS));
			      //перевод строки
				  sb.append("\r\n");
						  
				  //телефон мобильный
				  if (((PeopleContactEntity) row[10])!=null){
				      sb.append(createPhoneRecordNbki("PN01",((PeopleContactEntity) row[10]),PeopleContact.CELL_PHONE_NBKI));
				      //перевод строки
				      sb.append("\r\n");
				  }
				  
				  //телефон домашний
				  if (((PeopleContactEntity) row[11])!=null){
					  sb.append(createPhoneRecordNbki("PN02",((PeopleContactEntity) row[11]),PeopleContact.HOME_PHONE_NBKI));
			     	  //перевод строки
				      sb.append("\r\n");
				  }
				  
				  //кредит
				  sb.append(createCreditRecordNbki("TR01", user,sendingDate,
						((CreditEntity) row[8]),((PaymentEntity) row[9]),((Double) row[12])));
				  //перевод строки
				  sb.append("\r\n");
	
			}//end пишем кредит
			
	    return sb;
	}
	
	/**
	 * футер для выгрузки в НБКИ
	 * @param sb - StringBuilder
	 * @return
	 */
	public static String createUploadFooterForSendingToNbki(StringBuilder sb){
		int index = -1;
        int cnt = -1;
        do {
            index = sb.indexOf("\n", index + 1);
            cnt++;
        } while (index != -1);
		//заключительная запись
		sb.append("TRLR\t");
		cnt++;
		//кол-во сегментов
		sb.append(cnt);
		//перевод строки
		sb.append("\r\n");
		return sb.toString();
	}
	
	/**
	 * пишем записи для выдачи в Okb Cais 
	 * lobj - массив с данными
	 * 0 - № записи
	 * 1 - peopleMain
	 * 2 - peoplePersonal ПД
	 * 3 - peopleMisc - дополнительные данные человека
	 * 4 - document атрибуты документа, серия и номер передаются одним полем без пробелов
	 * 5 - addreg атрибуты адреса регистрации
	 * 6 - address атрибуты адреса проживания
	 * 7 - creditRequest - заявка на кредит
	 * 8 - credit - кредит
	 * 9 - payment - платеж
	 * 10 - peopleContact - мобильный телефон
	 * 11 - сумма выплат по кредиту
	 * 12 - есть ли история
	 * 13 - есть ли история за 2 месяца
	 * 14 - передача в суд
	 * 15 - есть ли решение суда
	 * 16 - запись кредита в creditDetail
	 * @throws IOException 
	 */
	 
	public static String createUploadForSendingToOkbCaisNew ( Date sendingDate, 
			List<Object[]> lobj, String code, String group,	String user,String orgname,
			String version) {
		StringBuilder sb=new StringBuilder();
		//начало записей
		sb.append(createOverallHeaderRecordOkb("300000",version,code,orgname,user,group,sendingDate));
		//перевод строки
		sb.append("\r\n");
		//количество записей истории
		Integer h=0;
		
		//количество записей из суда
		int judge=0;
		for (Object[] row: lobj)
	    {
			//начало кредитной записи
			sb.append(createHeaderRecordOkb("300100",code,version,((CreditEntity) row[8]).getCreditAccount(),false,true,false));
			//кредит
			sb.append(createCreditRecordOkb(sendingDate,((CreditEntity) row[8]),
			    ((PaymentEntity) row[9]),((CreditDetailsEntity) row[16]),((CreditRequestEntity) row[7])));
		    //перевод строки
			sb.append("\r\n");
			
			//история
			if ((boolean) row[12]){
				//есть и просрочка и платеж
			    if (((CreditDetailsEntity) row[17])!=null&&((PaymentEntity) row[9])!=null){
			    	//сначала просрочка, потом платеж
			    	if (((CreditDetailsEntity) row[17]).getEventDate().before(((PaymentEntity) row[9]).getProcessDate())){
			    	    //начало кредитной записи
					    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
					    //запись с историей
					    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),1,((CreditDetailsEntity) row[17]),null));
					   //перевод строки
						sb.append("\r\n");
						h+=1;
					    //начало кредитной записи
					    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
					    //запись с историей
					    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),2,null,((PaymentEntity) row[9])));
					    //перевод строки
						sb.append("\r\n");
						h+=1;
			    	}else{
			    		//начало кредитной записи
					    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
					    //запись с историей
					    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),1,null,((PaymentEntity) row[9])));
					    //перевод строки
						sb.append("\r\n");
						h+=1;
					    //начало кредитной записи
					    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
					    //запись с историей
					    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),2,((CreditDetailsEntity) row[17]),null));
					    //перевод строки
						sb.append("\r\n");
						h+=1;
			    	}//end сначала просрочка, потом платеж
			    }//end есть и просрочка и платеж
			    
			    //была просрочка, платежа не было
			    if (((CreditDetailsEntity) row[17])!=null&&((PaymentEntity) row[9])==null){
			    	//начало кредитной записи
				    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
				    //запись с историей
				    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),1,((CreditDetailsEntity) row[17]),null));
				   //перевод строки
					sb.append("\r\n");
					h+=1;
			    }
				
			    //был платеж, просрочки не было не было, кредит не закончен
			    if (((CreditDetailsEntity) row[17])==null&&((PaymentEntity) row[9])!=null&&((CreditEntity) row[8]).getCreditdataendfact()==null){
			    	//начало кредитной записи
				    sb.append(createHeaderRecordOkb("300210",code,version,((CreditEntity) row[8]).getCreditAccount(),false,false,false));
				    //запись с историей
				    sb.append(createHistoryRecordOkb(((CreditEntity) row[8]),1,null,((PaymentEntity) row[9])));
				    //перевод строки
					sb.append("\r\n");
					h+=1;
			    }
			}//закончилась часть с историей
			
			//субъект
			//начало кредитной записи
			sb.append(createHeaderRecordOkb("300300",code,version,((CreditEntity) row[8]).getCreditAccount(),true,true,true));
			//добавляем документ
			sb.append(createDocumentRecordOkb(((DocumentEntity) row[4])));
			//добавляем ПД		 
			sb.append(createPeopleRecordOkb(((PeopleMainEntity) row[1]),((PeoplePersonalEntity) row[2]),
					((CreditRequestEntity) row[7]).getDatecontest(),((PeopleContactEntity) row[10])));
			//перевод строки
			sb.append("\r\n");
			
			//адрес
			//начало кредитной записи
			sb.append(createHeaderRecordOkb("300500",code,version,((CreditEntity) row[8]).getCreditAccount(),true,true,false));
			//адрес проживания
			sb.append(createAddressRecordOkb(((PeopleMiscEntity) row[3]),((AddressEntity) row[6]),BaseAddress.RESIDENT_ADDRESS_OKB));
		    //перевод строки
			sb.append("\r\n");
			
			//адрес
			//начало кредитной записи
			sb.append(createHeaderRecordOkb("300500",code,version,((CreditEntity) row[8]).getCreditAccount(),true,true,false));
			//адрес регистрации
			sb.append(createAddressRecordOkb(((PeopleMiscEntity) row[3]),((AddressEntity) row[5]),BaseAddress.REGISTER_ADDRESS_OKB));
			//перевод строки
			sb.append("\r\n");
			
			//запись информации о решении суда
			if (((Boolean) row[15])){
				
				//начало кредитной записи
				sb.append(createHeaderRecordOkb("300620",code,version,((CreditEntity) row[8]).getCreditAccount(),true,false,false));
				//запись о решении
				sb.append(createCourtRecordOkb(((DebtEntity) row[14])));
				//перевод строки
				sb.append("\r\n");
				judge++;
			}
			//запись информации о переуступке прав - если передали коллектору
			// TODO информация о передаче коллектору
	    }
		//заключительная запись
		sb.append(createFooterRecordOkb("300999",version,code,lobj.size(),h,judge));
		//перевод строки
		sb.append("\r\n");
		return sb.toString();
	}
	
	/**
	 * пишем документ для выгрузки ОКБ
	 * @param document
	 * @return
	 */
	private static String createDocumentRecordOkb(DocumentEntity document){
		StringBuilder sb=new StringBuilder();
		//вид документа
		sb.append(Documents.PASSPORT_RF_OKB);
		//серия и номер документа
		String st=fillSpaceWithCharacter(document.getSeries().trim()+document.getNumber().trim(),20," ");
		sb.append(st);
		//дата окончания документа
		if (document.getDocenddate()!=null) {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(document.getDocenddate()));
		} else {
			sb.append(fillWithCharacter(" ",8));
		}
		//дата выдачи документа
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(document.getDocdate()));
		//место выдачи документа - пишем код подразделения
		st=Convertor.fromMask(document.getDocorgcode()).trim();
		st=fillSpaceWithCharacter(st,30," ");
		sb.append(st);
		//выдавший орган 
		st=Convertor.toLimitString(document.getDocorg().trim(),100);
		st=fillSpaceWithCharacter(st,100," ");
		sb.append(st);
		//второй документ - у нас его нет
		sb.append(fillWithCharacter(" ",168));
		return sb.toString();
	}
	
	/**
	 * последняя запись для ОКБ
	 * @param recNumber - номер записи
	 * @param version - версия
	 * @param code - код подписчика
	 * @param recCount - всего записей
	 * @param histCount - исторических записей
	 * @param judgeCount - записей из суда
	 * @return
	 */
	private static String createFooterRecordOkb(String recNumber,String version,String code,
			int recCount,Integer histCount,int judgeCount){
		StringBuilder sb=new StringBuilder();
		//начало кредитной записи
		sb.append(recNumber);
		//версия структуры записи 
		sb.append(version);
		//код пользователя
		sb.append(fillZero(code,5));
		//тип финансирования
		sb.append(BaseCredit.COMMON_CREDIT_CODE);
		//всего записей
		String st=fillZero(new Integer(recCount).toString(),9);
		sb.append(st);
		//всего записей, заголовок история
		sb.append(fillZero(histCount.toString(),9));
		//всего записей человек
		sb.append(st);
		//всего записей, адреса
		st=fillZero(new Integer(recCount*2).toString(),9);
		sb.append(st);
		//наполнитель0
		sb.append(fillWithCharacter("0",9));
		//всего записей работодатель
		sb.append(fillWithCharacter("0",9));
		//наполнитель1
		sb.append(fillWithCharacter("0",9));
		//наполнитель2
		sb.append(fillWithCharacter("0",20));
		//всего записей информация из суда 
		st=fillZero(new Integer(judgeCount).toString(),9);
		sb.append(st);
		//всего записей информация о поручительстве
		sb.append(fillWithCharacter("0",9));
		//всего записей история по поручительству
		sb.append(fillWithCharacter("0",9));
		//всего записей информация о договоре залога
		sb.append(fillWithCharacter("0",9));
		//всего записей банковская гарантия
		sb.append(fillWithCharacter("0",9));
		//всего записей переуступка прав - это коллектор???
		sb.append(fillWithCharacter("0",9));
		return sb.toString();
	}
	
	/**
	 * запись кредита в выгрузке ОКБ
	 * @param sendingDate - дата ртправки
	 * @param credit - кредит
	 * @param payment - последний платеж
	 * @param creditDetail - данные по операции выдачи кредита
	 * @param creditRequest - кредитная заявка
	 * @return
	 */
	private static String createCreditRecordOkb(Date sendingDate,CreditEntity credit,
			PaymentEntity payment,CreditDetailsEntity creditDetail,CreditRequestEntity creditRequest){
		StringBuilder sb=new StringBuilder();
		//новый счет, пустое поле
		sb.append(fillWithCharacter(" ",40));
		//дата открытия счета
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdatabeg()));
		//кредит физ.лица
		sb.append(BaseCredit.CREDIT_OWNER);
		//кол-во владельцев счета - всегда 1
		sb.append("01");
		//валюта - всегда рубли
		sb.append(BaseCredit.CURRENCY_RUB);
		//цель кредитования - 98 - другое
		sb.append("98");
		//размер кредита
		Integer sm=credit.getCreditsum().intValue();
		String st=fillZero(sm.toString(),15);//изменение, было 9 знаков
		sb.append(st);
		//кол-во дней
		sb.append(fillZero(String.valueOf(DatesUtils.daysDiff(credit.getCreditdataend(),credit.getCreditdatabeg())),3));
		//единицы измерения - дни
		sb.append("01");
		//частота платежей - ставим непостоянно
		sb.append(BaseCredit.FREQ_PAYMENT_STRING);
		//размер взноса, заполняем 0
		sb.append(fillWithCharacter("0",15));//изменение, было 9 знаков
		//лимит кредитования
		sb.append(fillWithCharacter(" ",15));//изменение, было 9 знаков
		//статус кредитной линии
		sb.append(" ");
		//своевременность оплаты
		int ovrd=0;
		if (credit.getMaxDelay()!=null&&credit.getMaxDelay()>0&&credit.getCreditdataendfact()==null) {
			if (credit.getMaxDelay()<30){
				sb.append(BaseCredit.OVERDUE_BEFORE_MONTH);
  		    } else if (credit.getMaxDelay()>=30&&credit.getMaxDelay()<60){
  			    sb.append(BaseCredit.WITHOUT_OVERDUE); 
  		    } else if (credit.getMaxDelay()>=60&&credit.getMaxDelay()<90){
  		    	sb.append(BaseCredit.OVERDUE_MONTH);
  		    } else if (credit.getMaxDelay()>=90&&credit.getMaxDelay()<120){
  		    	sb.append(BaseCredit.OVERDUE_TWO_MONTH);
  		    } else if (credit.getMaxDelay()>=120&&credit.getMaxDelay()<150){
  		    	sb.append(BaseCredit.OVERDUE_THREE_MONTH);
  		    } else if (credit.getMaxDelay()>=150&&credit.getMaxDelay()<180){
  		    	sb.append(BaseCredit.OVERDUE_FOUR_MONTH);
  		    } else if (credit.getMaxDelay()>=180){
  		    	sb.append(BaseCredit.OVERDUE_FIVE_MONTH);
  		    }
		    ovrd=1;
		} else {
			//без просрочки
			sb.append(BaseCredit.NEW_OVERDUE_RSTANDART);
		}
		//тип обеспечения - нет
		sb.append("  ");
		//застрахованная
		sb.append(" ");
		//размер страхового покрытия
		sb.append(fillWithCharacter(" ",15));//изменение, было 9 знаков
		//процентная годовая ставка - теперь с десятичной частью
		Double sm1= CalcUtils.calcYearStake(credit.getCreditpercent());
		sb.append(fillZero(CalcUtils.pskToString(sm1),8));//должно получиться число 0900.000
		//общая сумма задолженности - пишем 0, если кредит закрыт
		if (credit.getCreditdataendfact()!=null) {
			sb.append(fillWithCharacter("0",15));
		} else {
			sb.append(fillZero(new Integer(credit.getCreditsumback().intValue()).toString(),15));
		}//изменение, было 9 знаков
		
		//просроченная задолженность
		if (ovrd==1){
			sb.append(fillZero(new Integer(credit.getCreditsumback().intValue()).toString(),15));
		} else {
			sb.append(fillWithCharacter("0",15));
		}//изменение, было 9 знаков
		
		//дата окончания кредита
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend()));
		//дата платежа
		if (payment!=null) {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(payment.getProcessDate()));
		} else {
			sb.append(fillWithCharacter(" ",8));
		}
		//дата закрытия счета
		if (credit.getCreditdataendfact()!=null) {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataendfact()));
		} else {
			sb.append(fillWithCharacter(" ",8));
		}
		//дата неуплаты, что писать?
		sb.append(fillWithCharacter(" ",8));
		//дата начала тяжбы, что писать?
		sb.append(fillWithCharacter(" ",8));
		//дата списания - ставим безнадежный долг
		if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_HOPELESS){
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getDateStatus()));
		} else {
		    sb.append(fillWithCharacter(" ",8));
		}
		//причина закрытия
		if (credit.getCreditdataendfact()!=null) {
			if(credit.getCreditdataendfact().before(credit.getCreditdataend())){
				//досрочное погашение
				sb.append("17");
			} else if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_REFINANCE){
				//рефинансирование
				sb.append("16");
			} else {
				//закрыто клиентом
		        sb.append("10");
			}
		} else {
			sb.append(fillWithCharacter(" ",2));
		}
		//специальный статус счета
		sb.append(fillWithCharacter(" ",2));
		//дата последнего пропущенного платежа
		if (credit.getCreditdataend().before(sendingDate)&&credit.getCreditdataendfact()==null) {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend()));
		} else {
			sb.append(fillWithCharacter(" ",8));
		}
		//индикатор удаления записи
		sb.append(fillWithCharacter(" ",1));
		//индикатор записи блокировка-спор
		sb.append(fillWithCharacter(" ",1));
		
		//полная стоимость займа (сумма к возврату)
		sm=creditDetail.getAmountAll().intValue();
		st=fillZero(sm.toString(),15);
		sb.append(st);
		//обеспечение в виде поручительства
		sb.append(fillWithCharacter("0",15));
		//дата начала поручительства, нет
		sb.append(fillWithCharacter(" ",8));
		//дата окончания поручительства, нет
		sb.append(fillWithCharacter(" ",8));
		//дата уплаты процентов
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend()));
		//источник платежа - сам заемщик
		sb.append("01");
		//идентификатор кредитной заявки - непонятно, что писать, id что ли???
		sb.append(fillSpaceWithCharacter(creditRequest.getId().toString(),40," "));
		//включать в состав инфо части
		sb.append("1");
		//тип кредита ЦБ 
		sb.append("231");
		//номер договора юридический 
		sb.append(fillSpaceWithCharacter(credit.getCreditAccount(),80," "));
		//полная стоимость займа в процентах - если есть штрафы и кредит просрочен, надо пересчитывать
		sm1= CalcUtils.calcYearStake(credit.getCreditpercent());
		sb.append(fillZero(CalcUtils.pskToString(sm1),8));//должно получиться число 0900.000
		
		//комментарии подписчика, сюда можно писать инфу о суде
		sb.append(fillWithCharacter(" ",250));
		//комментарии владельца счета
		sb.append(fillWithCharacter(" ",150));
		return sb.toString();
	}
	
	/**
	 * пишем ПД для выгрузки ОКБ
	 * @param peopleMain - заголовок человека
	 * @param peoplePersonal - ПД
	 * @return
	 */
	private static String createPeopleRecordOkb(PeopleMainEntity peopleMain,PeoplePersonalEntity peoplePersonal,
			Date dateContest,PeopleContactEntity peopleContact){
		StringBuilder sb=new StringBuilder();
		//снилс
		if (StringUtils.isNotEmpty(peopleMain.getSnils()))	{
			sb.append(fillSpaceWithCharacter(peopleMain.getSnils(),20," "));
		} else {
			sb.append(fillWithCharacter(" ",20));
		}
		//инн
		if (StringUtils.isNotEmpty(peopleMain.getInn())){
			sb.append(fillSpaceWithCharacter(peopleMain.getInn(),20," "));
		} else {
			sb.append(fillWithCharacter(" ",20));
		}
		//егрн и прочее
		sb.append(fillWithCharacter(" ",48));
		//формат имени
		sb.append("1");
		//обращение к клиенту
		sb.append(fillWithCharacter(" ",2));
		//имя
		sb.append(fillSpaceWithCharacter(peoplePersonal.getName(),100," "));
		//отчество
		sb.append(fillSpaceWithCharacter(peoplePersonal.getMidname(),100," "));
		//фамилия
		sb.append(fillSpaceWithCharacter(peoplePersonal.getSurname(),150," "));
		//девичья фамилия
		sb.append(fillSpaceWithCharacter(peoplePersonal.getMaidenname(),100," "));
		//предыдущее название, для юр.лица
		sb.append(fillWithCharacter(" ",100));
		//пол
		sb.append(peoplePersonal.getGender().getCodeinteger().toString());
		//дата рождения
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(peoplePersonal.getBirthdate()));
		//место рождения
		sb.append(fillSpaceWithCharacter(peoplePersonal.getBirthplace(),100," "));
		//гражданство
		sb.append(BaseAddress.COUNTRY_RUSSIA);
		//согласие
		sb.append("0");
		//дата согласия
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(dateContest));
		//мобильный телефон - в каком формате?
		if (peopleContact!=null){
			sb.append(fillSpaceWithCharacter((peopleContact.getValue()),16," "));
		} else {
		    sb.append(fillWithCharacter(" ",16));
		}
		//код субъекта
		sb.append(fillWithCharacter(" ",15));
		//вид кода
		sb.append(fillWithCharacter(" ",1));
		return sb.toString();
	}
	
	/**
	 * пишем запись с адресом для ОКБ
	 * @param peopleMisc - доп.данные
	 * @param address - адрес 
	 * @param addressType - тип адреса
	 * @return
	 */
	private static String createAddressRecordOkb(PeopleMiscEntity peopleMisc,AddressEntity address,Integer addressType){
		StringBuilder sb=new StringBuilder();
		//вид адреса - текущий
		sb.append("0");
		//адрес проживания - какой адрес необходимо заполнять?
		sb.append(addressType);
		//дата проживания
		if (peopleMisc.getRegDate()!=null) {
		    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(peopleMisc.getRegDate()));
		} else if (peopleMisc.getRealtyDate()!=null) {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(peopleMisc.getRealtyDate()));
		} else {
		    sb.append(fillWithCharacter(" ",8));
		}
		//дата окончания регистрации
		sb.append(fillWithCharacter(" ",8));
		//формат адреса
		sb.append("1");
		//квартира
		if (StringUtils.isNotEmpty(address.getFlat()))	{
			sb.append(fillSpaceWithCharacter(address.getFlat(),6," "));
		} else {
			sb.append(fillWithCharacter(" ",6));
		}
		//дом
		if (StringUtils.isNotEmpty(address.getHouse())) {
			sb.append(fillSpaceWithCharacter(address.getHouse(),6," "));
		} else {
			sb.append(fillWithCharacter(" ",6));
		}
		//корпус
		if (StringUtils.isNotEmpty(address.getCorpus())) {
			sb.append(fillSpaceWithCharacter(address.getCorpus(),6," "));
		} else {
			sb.append(fillWithCharacter(" ",6));
		}
		//строение
		sb.append(fillWithCharacter(" ",20));
		//улица
		String st=address.getStreetName();
		if (StringUtils.isEmpty(st)) {
			st="-";
		}
		sb.append(fillSpaceWithCharacter(st,50," "));
		//город или населенный пункт
		if (StringUtils.isNotEmpty(address.getPlaceName())) {
			sb.append(fillSpaceWithCharacter(address.getPlaceName(),50," "));
		} else {
			if (StringUtils.isNotEmpty(address.getCityName())){
			    sb.append(fillSpaceWithCharacter(address.getCityName(),50," "));
			} else {
				 //если это Москва или Питер
				 if (address.getRegionName().indexOf(" ")>0) { 
        		      st=address.getRegionName().substring(0, address.getRegionName().indexOf(" ")).trim();
        		  } else {
        			 st=address.getRegionName();
        		  }
				  sb.append(fillSpaceWithCharacter(st,50," "));
			}
		}
		//сельский район
		if (StringUtils.isNotEmpty(address.getAreaName()))	{
			sb.append(fillSpaceWithCharacter(address.getAreaName(),100," "));
		} else {
			sb.append(fillWithCharacter(" ",100));
		}
		//область
		if (address.getRegionShort()==null){
			sb.append("00");
        } else {
		    sb.append(address.getRegionShort().getCodereg());
        }
		//страна 
		sb.append(address.getCountry().getCode());
		//индекс
		if (StringUtils.isEmpty(address.getIndex())){
		    sb.append(fillWithCharacter(" ",10));
		} else {
			sb.append(fillSpaceWithCharacter(address.getIndex(),10," "));
		}
		//домашний телефон
		sb.append(fillWithCharacter(" ",16));
		
		return sb.toString();
	}
	
	/**
	 * заголовок записи ОКБ
	 * @param record - стандартный заголовок
	 * @param code - код подписчика
	 * @param version - версия выгрузки
	 * @param creditAccount - номер счета
	 * @param writeOwner - пишем ли owner
	 * @param writeIndicator - пишем ли индикатор
	 * @param writeTypeOwner - пишем ли вид owner
	 * @return
	 */
	private static String createHeaderRecordOkb(String record,String code,String version,
			String creditAccount,boolean writeOwner,boolean writeIndicator,boolean writeTypeOwner){
		StringBuilder sb=new StringBuilder();
		//начало кредитной записи
		sb.append(record);
		//версия структуры записи 
		sb.append(version);
		//код пользователя
		sb.append(fillZero(code,5));
		//тип финансирования
		sb.append(BaseCredit.COMMON_CREDIT_CODE);
		//номер кредитного счета 
		sb.append(fillSpaceWithCharacter(creditAccount,40," "));
		if (writeOwner){
			sb.append("01");
		}
		if (writeIndicator){
		    //специальный индикатор 
		    sb.append("9");
		}
		if (writeTypeOwner){
			sb.append("01");
		}
		return sb.toString();
	}
	
	/**
	 * запись заголовка истории
	 * @param date1 - дата, если 2 месяца истории
	 * @param date2 - дата, если 1 месяц истории
	 * @param isTwoMonthes - берем 2 месяца
	 * @return
	 */
	private static String createHistoryHeaderOkb(Date date1,Date date2,Boolean isTwoMonthes){
		StringBuilder sb=new StringBuilder();
		
		//дата, когда начинается история
		if (isTwoMonthes){
		    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(date1));
		    //сколько месяцев истории
			sb.append("02");
		} else {
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(date2));
			//сколько месяцев истории
			sb.append("01");
		}
		return sb.toString();
	}
	
	/**
	 * запись истории
	 * @param credit - кредит
	 * @param historyDate - дата истории
	 * @param isTwoMonthes - берем 2 записи
	 * @return
	 */
	private static String createHistoryRecordOkb(CreditEntity credit,
			Integer num,CreditDetailsEntity overdue,PaymentEntity payment){
		StringBuilder sb=new StringBuilder();
		
		//номер в истории
		sb.append("000"+num.toString());
		
		//статус счета в истории
		
		//дата истории 
		if (overdue!=null){
			//просрочка до месяца
			sb.append(BaseCredit.OVERDUE_BEFORE_MONTH);
			//сумма возврата на момент
			sb.append(fillZero(new Integer(overdue.getAmountAll().intValue()).toString(),15));
			//источник платежа
			sb.append("01");
		    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(overdue.getEventDate()));
		    //размер периодического платежа
			sb.append(fillWithCharacter("0",15));
			//просрочка до месяца
			sb.append(fillZero(new Integer(overdue.getAmountAll().intValue()).toString(),15));
		} else {
			if (credit.getMaxDelay()!=null&&credit.getMaxDelay()>0&&credit.getCreditdataendfact()==null) {
				if (credit.getMaxDelay()<30){
					sb.append(BaseCredit.OVERDUE_BEFORE_MONTH);
	  		    } else if (credit.getMaxDelay()>=30&&credit.getMaxDelay()<60){
	  			    sb.append(BaseCredit.WITHOUT_OVERDUE); 
	  		    } else if (credit.getMaxDelay()>=60&&credit.getMaxDelay()<90){
	  		    	sb.append(BaseCredit.OVERDUE_MONTH);
	  		    } else if (credit.getMaxDelay()>=90&&credit.getMaxDelay()<120){
	  		    	sb.append(BaseCredit.OVERDUE_TWO_MONTH);
	  		    } else if (credit.getMaxDelay()>=120&&credit.getMaxDelay()<150){
	  		    	sb.append(BaseCredit.OVERDUE_THREE_MONTH);
	  		    } else if (credit.getMaxDelay()>=150&&credit.getMaxDelay()<180){
	  		    	sb.append(BaseCredit.OVERDUE_FOUR_MONTH);
	  		    } else if (credit.getMaxDelay()>=180){
	  		    	sb.append(BaseCredit.OVERDUE_FIVE_MONTH);
	  		    }
			} else {
				//без просрочки
				sb.append(BaseCredit.NEW_OVERDUE_RSTANDART);
			}
			//сумма возврата на момент
			sb.append(fillZero(new Integer(credit.getCreditsumback().intValue()).toString(),15));
			//источник платежа
			sb.append("01");
			sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(payment.getProcessDate()));
			//размер периодического платежа
			sb.append(fillZero(new Integer(payment.getAmount().intValue()).toString(),15));
			//просрочка до месяца
			sb.append(fillWithCharacter("0",15));
		}
		
		
		//кредитный лимит
		sb.append(fillWithCharacter(" ",15));
		return sb.toString();
	}
	
	/**
	 * пишем данные о передаче в суд ОКБ
	 * @param debt - долговые данные
	 * @return
	 */
	private static String createCourtRecordOkb(DebtEntity debt){
		StringBuilder sb=new StringBuilder();
		//номер решения
		sb.append(fillSpaceWithCharacter(debt.getDebtNumber(),60," "));
		//название судебного органа
		sb.append(fillSpaceWithCharacter(Convertor.toLimitString(debt.getAuthorityName(),200),200," "));
		//дата решения
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(debt.getDateDecision()));
		//тип решения
		sb.append("01");
		//сумма к возврату
		Integer sm=debt.getAmount().intValue();
		String st=fillZero(sm.toString(),15);
		sb.append(st);
		//валюта - всегда рубли
		sb.append(BaseCredit.CURRENCY_RUB);
		//судебное решение
		sb.append(fillSpaceWithCharacter(Convertor.toLimitString(debt.getComment(),2000),2000," "));
		return sb.toString();
	}
	
	/**
	 * общий заголовок выгрузки ОКБ
	 * @param record - запись
	 * @param version - версия
	 * @param code - код подписчика
	 * @param orgname - организация
	 * @param user - пользователь
	 * @param group - группа
	 * @param sendingDate - дата выгрузки
	 * @return
	 */
	private static String createOverallHeaderRecordOkb(String record,String version,String code,
			String orgname,String user,String group,Date sendingDate){
		StringBuilder sb=new StringBuilder();
		//начало записей
		sb.append(record);
		//версия структуры записи 
		sb.append(version);
		//код пользователя
		sb.append(fillZero(code,5));
		//тип финансирования
		sb.append(BaseCredit.COMMON_CREDIT_CODE);
		//кодировка
		sb.append("CP1251    ");
		//группа
		sb.append(fillSpaceWithCharacter(group,12," "));
		//пользователь
		sb.append(fillSpaceWithCharacter(user,12," "));
		//просто 12 пробелов, заполнитель
		sb.append(fillWithCharacter(" ",12));
		//название организации
		sb.append(fillSpaceWithCharacter(orgname,40," "));
		//дата учета, непонятно как ее брать
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate));
		//дата подготовки файла
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate));
		return sb.toString();
	}
	
	/**
	 * добавляем документ в выгрузку НБКИ
	 * @param record - запись
	 * @param document - документ
	 * @return
	 */
	private static String createDocumentRecordNbki(String record,DocumentEntity document){
		StringBuilder sb=new StringBuilder();
	    //начало записей
	    sb.append(record+"\t");
	    //вид документа
	    sb.append((document.getDocumenttypeId().getCodeinteger().toString()+"\t"));
	    //номер серии
	    sb.append(document.getSeries()+"\t");
	    //номер паспорта
	    sb.append(document.getNumber()+"\t");
	    //дата выдачи
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(document.getDocdate())+"\t");
		//выдавшая организация
	    sb.append(document.getDocorg()+"\t");
	    //место выдачи - у нас нет
	    sb.append("\t");
	    return sb.toString();
	}
	
	/**
	 * добавляем ПД человека в выгрузку НБКИ
	 * @param record - запись
	 * @param people - человек
	 * @return
	 */
	private static String createPeopleRecordNbki(String record,PeoplePersonalEntity people){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //фамилия
	    sb.append(people.getSurname()+"\t");
	    //отчество
	    sb.append(people.getMidname()+"\t");
	    //имя
	    sb.append(people.getName()+"\t");
	    //поле не используется
	    sb.append("\t");
	    //дата рождения
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(people.getBirthdate())+"\t");
		//место рождения
	    sb.append(people.getBirthplace()+"\t");
	    //поле не используется
	    sb.append("\t\t\t\t\t");
	    return sb.toString();
	}
	
	/**
	 * добавляем адрес в выгрузку НБКИ
	 * @param record - запись
	 * @param address - адрес
	 * @param addressType - вид адреса
	 * @return
	 */
	private static String createAddressRecordNbki(String record,int addrType,AddressEntity address,
			PeopleMiscEntity peopleMisc,Integer addressType){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //вид адреса
	    sb.append(addrType+"\t");
	    //почтовый индекс - нет
	    sb.append("\t");
	    //страна
	    sb.append(BaseAddress.COUNTRY_RUSSIA+"\t");
	    //регион
	    sb.append(address.getRegionShort().getCodereg()+"\t");
	    //поле не используется
	    sb.append("\t");
	    //сельская территория
	    if (StringUtils.isNotEmpty(address.getArea())){
	    	sb.append(address.getAreaName());
	    }
	    sb.append("\t");
	  //  String city="";
	    //город или населенный пункт
	    sb.append(address.getMandatoryCity());
	  /*  if (StringUtils.isNotEmpty(address.getCity())){
	    	sb.append(address.getCityName());
	    	city=address.getCityName();
	    } else  if (StringUtils.isNotEmpty(address.getPlace())){
	    	sb.append(address.getPlaceName());
	    	city=address.getPlaceName();
	    } else if (StringUtils.isEmpty(address.getPlace())&&
	    		StringUtils.isEmpty(address.getCity())&&
	    		StringUtils.isEmpty(address.getArea())){
	    	//Москва или Питер
	       sb.append(address.getRegionName());
	       city=address.getRegionName();
	    }
	    //если нет ничего кроме сельского региона
	    if (StringUtils.isEmpty(city)&&StringUtils.isNotEmpty(address.getArea())){
	    	city=address.getAreaName();
	    }*/
	    
	    sb.append("\t");
	    //код улицы - нет
	    sb.append("\t");
	    //улица
	    if (StringUtils.isNotEmpty(address.getStreet())){
	    	sb.append(address.getStreetName());
	    } else {
	    	//sb.append(city);
	    	sb.append(address.getMandatoryCity());
	    }
	    sb.append("\t");
	    //дом
	    if (StringUtils.isNotEmpty(address.getHouse())){
	    	sb.append(address.getHouse());
	    }
	    sb.append("\t");
	    //корпус
	    if (StringUtils.isNotEmpty(address.getCorpus())){
	    	sb.append(address.getCorpus());
	    }
	    sb.append("\t");
	    //строение
	    if (StringUtils.isNotEmpty(address.getBuilding())){
	    	sb.append(address.getBuilding());
	    }
	    sb.append("\t");
	    //квартира
	    if (StringUtils.isNotEmpty(address.getFlat())){
	    	sb.append(address.getFlat());
	    }
	    sb.append("\t");
	    //статус - нет
	    sb.append("\t");
	    //дата прописки
	    if (peopleMisc!=null){
	        if (peopleMisc.getRealtyDate()!=null){
	    	    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(peopleMisc.getRealtyDate()));
	        } else if (peopleMisc.getRegDate()!=null){
	    	    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(peopleMisc.getRegDate()));
	        }
	    }
		return sb.toString();
	}
	/**
	 * пишем телефон в выгрузку НБКИ
	 * @param record - запись
	 * @param phone - телефон
	 * @param contactType - вид контакта
	 * @return
	 */
	private static String createPhoneRecordNbki(String record,PeopleContactEntity phone,Integer contactType){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //номер телефона
	    sb.append(phone.getValue()+"\t");
	    //вид телефона
	    sb.append(contactType);
	    return sb.toString();
	}
	
	/**
	 * пишем кредит в выгрузку НБКИ
	 * @param record - запись
	 * @param user - пользователь
	 * @param sendingDate - дата выгрузки
	 * @param credit - кредит
	 * @param payment - последний платеж
	 * @param sumPays - сумма выплат по кредиту
	 * @return
	 */
	private static String createCreditRecordNbki(String record,String user,Date sendingDate,
			CreditEntity credit,PaymentEntity payment,Double sumPays){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //имя пользователя
	  	sb.append(user+"\t");
	    //номер счета
	  	sb.append(credit.getCreditAccount()+"\t");
	    //тип счета
	  	sb.append(BaseCredit.MICRO_CREDIT_NBKI+"\t");
	    //отношение к счету - физлицо
	  	sb.append(BaseCredit.CREDIT_OWNER+"\t");
	  	//дата начала кредита
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdatabeg())+"\t");
	  	//дата последней выплаты
	  	if (payment!=null){
	  		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(payment.getProcessDate())+"\t");
	  	} else {
	  		sb.append("19000102\t");
	  	}
	  	//статус кредита
	  	if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_CLOSED){
	  		sb.append(BaseCredit.CREDIT_CLOSED_NBKI+"\t");
	  	} else if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_COURT){
	  		sb.append(BaseCredit.CREDIT_COURT_NBKI+"\t");
	  	} else if (credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_OVERDUE||credit.getCreditStatusId().getCodeinteger()==BaseCredit.CREDIT_INNER_COLLECTOR){
	  		sb.append(BaseCredit.CREDIT_OVERDUE_NBKI+"\t");
	  	} else {
	  		sb.append(BaseCredit.CREDIT_ACTIVE_NBKI+"\t");
	  	}
	    //дата статуса
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getDateStatus())+"\t");
	    //дата составления отчета
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate)+"\t");
	    //сумма кредита
	  	sb.append(credit.getCreditsum().intValue()+"\t");
	    //сумма всех платежей (баланс)
	  	sb.append(sumPays.intValue()+"\t");
	  	//если есть просрочка, то какую сумму он должен
	  	if (credit.getCreditdataend().before(sendingDate)&&!credit.getIsover()){
	  		sb.append(credit.getSumMainRemain().intValue()+"\t");
	  		//суммы следующего платежа нет
	  		sb.append("0\t");
	  	} else {
	  		sb.append("0\t");
	  		//если кредит закрыт, то нет
	  		if (credit.getIsover()){
	  			sb.append("0\t");
	  		} else {
	  		    //сумма следующего платежа - то, что осталось оплатить
	  		    sb.append(credit.getSumMainRemain().intValue()+"\t");
	  		}
	  	}
	  	//частота платежей - другое
	  	sb.append(BaseCredit.FREQ_PAYMENT+"\t");
	  	//своевременность оплаты
	  	if (credit.getMaxDelay()!=null&&credit.getMaxDelay()>0&&credit.getCreditdataendfact()==null) {
			if (credit.getMaxDelay()<30){
				sb.append(BaseCredit.OVERDUE_BEFORE_MONTH+"\t");
  		    } else if (credit.getMaxDelay()>=30&&credit.getMaxDelay()<60){
  			    sb.append(BaseCredit.OVERDUE_MONTH+"\t"); 
  		    } else if (credit.getMaxDelay()>=60&&credit.getMaxDelay()<90){
  		    	sb.append(BaseCredit.OVERDUE_TWO_MONTH+"\t");
  		    } else if (credit.getMaxDelay()>=90&&credit.getMaxDelay()<120){
  		    	sb.append(BaseCredit.OVERDUE_THREE_MONTH+"\t");
  		    } else if (credit.getMaxDelay()>=120){
  		    	sb.append(BaseCredit.OVERDUE_FOUR_MONTH+"\t");
  		    }
		} else if (credit.getMaxDelay()==null&&credit.getCreditdataend().before(sendingDate)&&!credit.getIsover()){
			//новый кредит
			sb.append(BaseCredit.NEW_OVERDUE_RSTANDART+"\t");
		} else {
			//кредит закрыт
			sb.append(BaseCredit.WITHOUT_OVERDUE+"\t");
		}
	    //код валюты - рубли
	  	sb.append(BaseCredit.CURRENCY_RUB+"\t");
	    //код залога - нет
	  	sb.append("\t");
	  	//дата окончания кредита по графику
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend())+"\t");
	    //дата последнего платежа
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend())+"\t");
	    //дата последней выплаты процентов
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataend())+"\t");
	    //частота выплаты процентов - другое
	  	sb.append(BaseCredit.FREQ_PAYMENT+"\t");
	    //старое имя пользователя - нет
	  	sb.append("\t");
	    //старый номер счета - нет
	  	sb.append("\t");
	    //общая сумма долга
	  	if (!credit.getIsover()){
	  	    sb.append(credit.getCreditsumback().intValue()+"\t");
	  	} else {
	  		sb.append("0\t");
	  	}
	    //наличие поручителя - нет
	  	sb.append("N\t");
	    //объем обязательств поручителя - нет
	  	sb.append("\t");
	    //сумма поручительства - нет
	  	sb.append("\t");
	    //срок поручительства - нет
	  	sb.append("\t");
	    //наличие гарантии - нет
	  	sb.append("N\t");
	    //объем гарантии - нет
	  	sb.append("\t");
	    //сумма гарантии - нет
	  	sb.append("\t");
	    //срок гарантии - нет
	  	sb.append("\t");
	    //оценочная стоимость залога - нет
	  	sb.append("\t");
	    //дата оценки стоимости залога - нет
	  	sb.append("\t");
	    //срок действия договора залога - нет
	  	sb.append("\t");
	    //полная стоимость кредита
	  	sb.append(CalcUtils.calcYearStake(credit.getCreditpercent()).intValue()+"\t");
	    //наименование приобретателя права требования - пока нет
	  	sb.append("\t");
	    //идентификационные данные приобретателя права требования - пока нет
	  	sb.append("\t");
	    //Инн приобретателя права требования - пока нет
	  	sb.append("\t");
	    //снилс приобретателя права требования - пока нет
	  	sb.append("\t");
	  	//дата фактического выполнения обязательства
	  	if (credit.getCreditdataendfact()!=null&&credit.getCreditdataendfact().before(sendingDate)){
	  		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(credit.getCreditdataendfact()));
	  	}
		return sb.toString();
	}
	
	/**
	 * данные по суду для выгрузки в НБКИ
	 * @param record - запись
	 * @param debt - долг
	 * @param sendingDate - дата выгрузки
	 * @return
	 */
	private static String createCourtRecordNbki(String record,DebtEntity debt,Date sendingDate){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //номер иска
	    sb.append(debt.getDebtNumber()+"\t");
	    //название суда
	    sb.append(debt.getAuthorityName()+"\t");
	    //дата составления отчета
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate)+"\t");
	    //дата исполнения
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(debt.getDateDecision())+"\t");
	    //дата возмещения
	    sb.append("\t");
	    //истец
	    sb.append("\t");
	    //решение
	    sb.append(Convertor.toLimitString(debt.getComment(),500));
	    return sb.toString();
	}
	
	private static String createHeaderRecordNbki(String record,String user,String password,
			String version,Date dateVersion,Date sendingDate){
		StringBuilder sb=new StringBuilder();
		//начало записей
		sb.append(record+"\t");
		//версия 
		sb.append(version+"\t");
		//дата версии
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(dateVersion)+"\t");
		//имя пользователя
		sb.append(user+"\t");
		//идентификация цикла - нету
		sb.append("\t");
		//дата составления отчета
		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(sendingDate)+"\t");
		//пароль
		sb.append(password+"\t");
		return sb.toString();
	}
	
	/**
	 * данные кредитной заявки для выгрузки в НБКИ
	 * @param record - запись
	 * @param user - пользователь
	 * @param sendingDate - дата выгрузки
	 * @param creditRequest - кредитная заявка
	 * @return
	 */
	private static String createCreditRequestRecordNbki(String record,String user,Date sendingDate,
			CreditRequestEntity creditRequest){
		StringBuilder sb=new StringBuilder();
		//начало записей
	    sb.append(record+"\t");
	    //имя пользователя
	  	sb.append(user+"\t");
	    //номер заявки
	  	sb.append(creditRequest.getUniquenomer()+"\t");
	    //дата подачи заявки
	  	sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(creditRequest.getDateFill())+"\t");
	    //тип займодавца
	  	sb.append(BaseCredit.MICRO_CREDIT_ORGANIZATION+"\t");
	  	//вид обязательства - кредит
	  	sb.append("1\t");
	  	//тип кредита
	  	sb.append("201\t");
	    //способ предоставления 
	  	sb.append(creditRequest.getWayId().getCodeinteger()+"\t");
	  	//есть одобрение
	  	if (creditRequest.getAccepted()!=null&&creditRequest.getAccepted()==CreditRequest.ACCEPTED){
	  		sb.append("Y\t");
	  		if (creditRequest.getStatusId().getId()==CreditStatus.CLIENT_REFUSE
	  				||(creditRequest.getStatusId().getId()==CreditStatus.DECISION&&creditRequest.getDateSign()==null)){
	  		    //если клиент отказался
		  		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(DateUtils.addDays(creditRequest.getDateStatus(),5))+"\t");
	  		} else {
	  		    //когда надо подписать оферту
	  			if (creditRequest.getDateSign()==null){
	  				sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(DateUtils.addDays(creditRequest.getDateStatus(),5))+"\t");
	  			} else {
	  		        sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(DateUtils.addDays(creditRequest.getDateSign(),5))+"\t");
	  			}
	  		}
	  	} else {
	  	    sb.append("\t\t");
	  	}
	    //есть отказ
	  	if (creditRequest.getAccepted()!=null&&creditRequest.getAccepted()==CreditRequest.NOT_ACCEPTED
	  			&&creditRequest.getRejectreasonId()!=null){
	  	    //сумма отклоненной заявки
		  	sb.append(creditRequest.getCreditsum().intValue()+"\t");
		  	//валюта отклоненной заявки
		  	sb.append(BaseCredit.CURRENCY_RUB+"\t");
		  	//дата отклоненной заявки
		  	if (creditRequest.getDateDecision()!=null){
		  		sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(creditRequest.getDateDecision())+"\t");
		  	} else {
		  	    sb.append(DatesUtils.DATE_FORMAT_YYYYMMdd.format(creditRequest.getDateStatus())+"\t");
		  	}
		  	//причина отклоненной заявки
		  	sb.append(CreditRequest.UPLOAD_REFUSE_REASON_EQUIFAX+"\t");
	  	}else{
	  		sb.append("\t\t\t\t");
	  	}
	    //номер кредита
	  	if (creditRequest.getAccepted()!=null&&creditRequest.getAccepted()==CreditRequest.ACCEPTED
	  			&&creditRequest.getAcceptedcreditId()!=null){
	  		sb.append(creditRequest.getUniquenomer()+"\t");
	  	} else {
	  		sb.append("\t");
	  	}
	    //тип предоставленного кредита
	  	sb.append("\t");
	    //признак дефолта
	  	sb.append("\t");
	  	//кредит погашен
	  	if (creditRequest.getAcceptedcreditId()!=null&&creditRequest.getAcceptedcreditId().getIsover()){
	  		sb.append("R");	
	  	}
	  	sb.append("\t");
	  	return sb.toString();
	}
	/**
	 * заполняем остаток строки символом	
	 * @param source - строка
	 * @param len - общая длина строки
	 * @param ch - символ
	 */
	private static String fillSpaceWithCharacter(String source,Integer len,String ch){
		if (source==null) {
			source="";
		}
		if (source.length()<len) {
			for (int i=source.length();i<len;i++) {
				source+=ch;
			}
		}	
		return source;
	}
	
	private static String fillZero(String source,Integer len){
		if (source==null)
			source="";
		if (source.length()<len) {
			for (int i=source.length();i<len;i++)
				source="0"+source;
		}	
		return source;
	}
	
	/**
	 * заполняем строку символами
	 * @param chr - символ
	 * @param len - длина строки
	 */
	private static String fillWithCharacter(String chr,Integer len){
		String st="";
		for (int i=0;i<len;i++) {
			st+=chr;
		}
		return st;
	}

	/**
	 * создаем заголовок документа для выгрузки в Эквифакс
	 * @param sendingDate - дата отправки
	 * @param version - версия выгрузки
	 * @return
	 */
	public static Document createDocumentUploadForEquifax(Date sendingDate, String version) {
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
		Element root = document.addElement( "fch" );
        root.addAttribute("version",version);
        
        Element hd=root.addElement("head");
        WriteNodeWithMandatoryText(hd,"date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(sendingDate));
        return document;
	}
	
	/**
	 * создаем заголовок документа для выгрузки в Эквифакс
	 * @param sendingDate - дата отправки
	 * @return
	 */
	public static Document createDocumentUploadForEquifaxCR(Date sendingDate) {
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(XmlUtils.ENCODING_WINDOWS);
		Element root = document.addElement( "fip" );
                
        Element hd=root.addElement("head");
        WriteNodeWithMandatoryText(hd,"date",DatesUtils.DATE_FORMAT_ddMMYYYY.format(sendingDate));
        return document;
	}
	
	/**
	 * пишем записи для выдачи в Equifax 
	 * информация только по кредитам
	 * @param row - массив с данными
	 * 0 - № записи
	 * 1 - peopleMain
	 * 2 - peoplePersonal ПД
	 * 3 - peopleMisc - дополнительные данные человека
	 * 4 - document атрибуты документа, серия и номер передаются одним полем без пробелов
	 * 5 - address атрибуты адреса регистрации
	 * 6 - address атрибуты адреса проживания
	 * 7 - creditRequest - заявка на кредит
	 * 8 - credit - кредит
	 * 9 - последний платеж
	 * 10- сумма оплаты по кредиту
	 * 11 - телефон человека
	 * 12 - задолженности
	 * 13 - сумма платежей общая
	 * 14 - сумма платежей проценты
	 * 15 - долги из суда
	 * 16 - есть ли решение суда
	 * 17 - просрочка за предыдущий день
	 * 18 - платежи
	 * @param sendingDate - дата выгрузки
	 * @throws IOException
	 */
	 
	public static void createUploadRecordForSendingToEquifax(Date sendingDate, 
			Object[] row,Document document)  {
		
	    Element root=document.getRootElement();  	
		Element info=root.addElement("info");
		//номер записи
		info.addAttribute("recnumber", ((Integer) row[0]).toString());
	
		//пишем титульную часть
		createTitlePartForEquifax(info,	((PeopleMainEntity) row[1]),((PeoplePersonalEntity) row[2]),
		    ((DocumentEntity) row[4]),((AddressEntity) row[5]),((AddressEntity) row[6]),
		    ((CreditRequestEntity) row[7]), ((PeopleContactEntity) row[11]),((PeopleMiscEntity) row[3]),true);
		
	           
        //write credit
		Element cred=createCreditRecordForEquifax(info,((CreditEntity) row[8]),
				((CreditRequestEntity) row[7]),sendingDate);
                
        //оплатили сумму основного долга
        Double sumMain=((CreditEntity) row[8]).getCreditsum()-(((CreditEntity) row[8]).getCreditsumdebt()!=null?((CreditEntity) row[8]).getCreditsumdebt():((CreditEntity) row[8]).getCreditsum());
        
        //платежи по кредиту
        createCreditPaymentRecordForEquifax(cred,((CreditEntity) row[8]),
    			sendingDate,((Double) row[10]),((Double) row[13]),((Double) row[14]),sumMain,
    			((PaymentEntity) row[9]),((CreditDetailsEntity) row[18]));
        
        //кредитная заявка
		createInfoDataForEquifax(cred,((CreditRequestEntity) row[7]));
         
        //если есть информация из суда
        if ((Boolean) row[16]){
       	  createDebtInfoForEquifax(info,((DebtEntity) row[15]));
        }
    
    }
	
	/**
	 * возвращает строку документа
	 * 
	 * @param document - документ
	 * @param cnt - кол-во записей
	 * @return
	 */
	public static String returnDocumentUploadForEquifax(Document document,Integer cnt){
		
		Element root=document.getRootElement();
	    Element ft=root.addElement("footer");
		WriteNodeWithMandatoryText(ft,"reccount",cnt.toString());
		
		return document.asXML();
	}
}
	




