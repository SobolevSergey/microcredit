package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.CheckRestResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.ResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.CheckRestResponse;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.Dics;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.ResponseUnp;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.RowUnp;
import ru.simplgroupp.contact.protocol.v2.tabonentobject.GetChangesFullMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tabonentobject.GetChangesFullPart0MethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tabonentobject.PingMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tclearingobject.CheckValidRestCorrMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.GetMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.GetStateMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.NewAndSendOutgoingMethodAssembler;
import ru.simplgroupp.contact.util.AUtils;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.payment.ContactPayBeanLocal;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.SoapUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.ejb.Timer;
import javax.inject.Inject;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sniffl
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ContactPayBean extends PaymentProcessorBean implements ContactPayBeanLocal {
    
	@Inject Logger logger;
       
    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    private ICryptoService cryptoService;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    ActionProcessorBeanLocal actProc;

    @EJB
    MailBeanLocal mailBean;

    @EJB
    OrganizationService orgService;
    
    @EJB
    PaymentDAO paymentDAO;
    
    private ResponseParser responseParser;
    private CheckRestResponseParser checkRestResponseParser;
    private ContactBaseBean contactBaseBean;
    private static String RESPONSE_CODE_OK="0";


    @Override
    public String getSystemName() {
        return ContactPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return ContactPayBeanLocal.SYSTEM_DESCRIPTION;
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
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.ASYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CONTACT_TYPE};
    }


    @Override
    /**
     * Делает перевод денег(сумма кредита) заемщику через систему Контакт
     * @param payment
     * @param context
     * @return
     * @throws ActionException
     */
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        logger.severe("Вызов sendSingleRequest " + businessObjectId);
        ContactPayPluginConfig config = (ContactPayPluginConfig) context.getPluginConfig();
        boolean test = !config.isUseWork();


        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.severe("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        Map<String,String> childrenMap = null;
        try {
            childrenMap = buildSingleRequest(payment,context);
        } catch (ActionException e) {
            logger.log(Level.SEVERE,"Ошибка формирования запроса. Cause: " + e.getMessage(),e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        ResponseUnp response = null;
        try {
            CdtrnCXR cdtrnCXR = doNewAndSendOutgoing(test,childrenMap,config);
            response = (ResponseUnp)getResponseParser().parseResponse(cdtrnCXR);
        } catch (ActionException e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(),e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(),e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            getContactBaseBean().parseError(payment, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка разбора ответа. Cause: " + e.getMessage(),e);
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }
        //SUCCESS!!!
        paymentService.saveExternalId(payment.getId(), response.getID());
     
        //шлю смс получателю денег
        try {
            String phoneNumber = childrenMap.get(BaseDoc.bPhone);
            String summa = childrenMap.get(BaseDoc.trnAmount);
            String numdog = childrenMap.get(BaseDoc.trnReference);

            if (StringUtils.isNotEmpty(phoneNumber) && StringUtils.isNotEmpty(summa) && StringUtils.isNotEmpty(numdog)){
                logger.severe("!!!!DEBUG ONLY !!!!! sms sending.... phone=" + Convertor.fromMask(phoneNumber) +"; sum=" + summa +"; numdog="+numdog);
                mailBean.sendSMSV2(Convertor.fromMask(phoneNumber), "Вам отправлен денежный перевод через систему Контакт, сумма " + summa + "; номер договора (перевода) " + numdog);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"СМСКА не послана получателю денег. Cause: " + e.getMessage(),e);
        }

        return false;
    }

    
    private Map<String,String> buildSingleRequest(PaymentEntity payment,PluginExecutionContext context) throws ActionException {
        
    	ContactPayPluginConfig config = (ContactPayPluginConfig) context.getPluginConfig();
        boolean test = !config.isUseWork();

        CreditEntity credit = payment.getCreditId();
        CreditRequestEntity requestCredit = credit.getCreditRequestId();
        PeopleMainEntity borrower = credit.getPeopleMainId();
        AddressEntity borAddress = peopleBean.findAddressActive(borrower,BaseAddress.RESIDENT_ADDRESS);
        if (borAddress == null){
            borAddress = peopleBean.findAddressActive(borrower,BaseAddress.REGISTER_ADDRESS);
        }

        if (borAddress == null){
        	logger.severe( "Не найден адрес заёмщика");
            throw new ActionException(ErrorKeys.INVALID_ACCOUNT, "Не найден адрес заёмщика", Type.TECH, ResultType.FATAL,null);
        }
        CountryEntity borCountry = borAddress.getCountry();
        
        if (borCountry==null){
        	logger.severe( "Не найдена страна заемщика, сами пишем RU");
        	borCountry=refBooks.getCountry("RU").getEntity();
        }
        DocumentEntity document = peopleBean.findPassportActive(borrower);
        if (document == null){
            try {
                if(borrower.getDocuments()!=null && borrower.getDocuments().size()>0){
                    document = borrower.getDocuments().get(0);
                }
            } catch (Exception e) {
                logger.severe("Документ не определен "+e.getMessage());
            }
        }
        if (document == null){
        	logger.severe( "Не найден документ заёмщика");
            throw new ActionException(ErrorKeys.INVALID_ACCOUNT, "Не найден документ заёмщика!", Type.TECH, ResultType.FATAL,null);
        }

        PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);
        PeopleContactEntity pplcont=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, borrower.getId());

        OrganizationEntity org=orgService.getOrganizationActive();

        Map<String,String> childrenMap = new HashMap<String, String>();
        //ОБЩАЯ ИНФА ********************************************************************************************
        childrenMap.put(BaseDoc.trnClCurrency, BaseCredit.CURRENCY_RUR); //нац. валюта клиента, поставил рубли
        childrenMap.put(BaseDoc.trnCurrency, BaseCredit.CURRENCY_RUR);//Валюта
        childrenMap.put(BaseDoc.trnAmount, Convertor.toDecimalString(CalcUtils.dformat,payment.getAmount()));//Сумма!!!
        childrenMap.put(BaseDoc.trnReference,requestCredit.getUniquenomer());//Номер перевода!!! Нужен

        String ppcode = test ? BaseDoc.TEST_PPCODE_VALUE : config.getPPcode();
        childrenMap.put(BaseDoc.trnSendPoint,ppcode);//Филиал отправителя
        childrenMap.put(BaseDoc.trnPickupPoint,BaseDoc.PICKUP_POINT_BEZADR_VALUE); //Филиал получателя
        try {
            childrenMap.put(BaseDoc.trnDate,DatesUtils.DATE_FORMAT_YYYYMMdd.format(new Date()));//Дата перевода, видимо тек. дата
        } catch (Exception e) {
            logger.severe("BaseDoc.trnDate "+e.getMessage());
            throw new ActionException("BaseDoc.trnDate ", e);
        }
        //код услуги - это видимо услуга из справочника Serv, которую нам надо выяснить
        childrenMap.put(BaseDoc.trnService,"2");//Код услуги, пока не знаю что это
        //childrenMap.put(BaseDoc.trnClAmount,"1.00");//Сумма какая то
        //childrenMap.put(BaseDoc.trnFeesClient,"0.02"); //Комиссия с клиента в валюте перевода
        //childrenMap.put(BaseDoc.trnFeesClientLocal,"0.02");//Комиссия с клиента в нац. валюте
        //childrenMap.put(BaseDoc.trnAdditionalInfo,"");//Дополнительная информация

        //ОТПРАВИТЕЛЬ *****************************************************************************************
        childrenMap.put(BaseDoc.sCountry,BaseCredit.COUNTRY_RU);//Страна отправителя
        childrenMap.put(BaseDoc.sResident,"1");//Страна резиденства отправителя 0-не резидент, 1-резидент
        childrenMap.put(BaseDoc.sResidentC,BaseCredit.COUNTRY_RU);//Страна резиденства отправителя 0-не резидент, 1-резидент
        childrenMap.put(BaseDoc.sCountryC,BaseCredit.COUNTRY_RU);   //Страна гражданства отправителя
        childrenMap.put(BaseDoc.sPhone,AUtils.parsePhoneToContactForm(org.getPhone()));//Телефон отправителя

        childrenMap.put(BaseDoc.sIDtype,config.getSenderFakeDocName()); //Тип документа отправителя
        childrenMap.put(BaseDoc.sName,config.getSenderFakeName());      //Имя отправителя,это видимо мы
        childrenMap.put(BaseDoc.sLastName,config.getSenderFakeLastName());//Отчество отправителя,это видимо мы
        childrenMap.put(BaseDoc.sSurName,config.getSenderFakeSurName());//Фамилия отправителя
        childrenMap.put(BaseDoc.sZipCode,config.getSenderFakeZipCode());//Почтовый индекс отправителя
        childrenMap.put(BaseDoc.sRegion,config.getSenderFakeRegion());//Регион (штат) отправителя
        childrenMap.put(BaseDoc.sCity,config.getSenderFakeCity());//Город отправителя
        childrenMap.put(BaseDoc.sAddress,config.getSenderFakeAddress());//Адрес отправителя (улица, дом, квартира)
        childrenMap.put(BaseDoc.sIDnumber,config.getSenderFakeDocSerNum()); //Серия и номер документа отправителя
        childrenMap.put(BaseDoc.sIDdate,config.getSenderFakeDocdate()); //Дата выдачи документа отправителя
        childrenMap.put(BaseDoc.sIDwhom,config.getSenderFakeDocWhom()); //Кем выдан документ отправителя
        childrenMap.put(BaseDoc.sIDexpireDate,""); //Дата истечения срока действия документа отправителя
        childrenMap.put(BaseDoc.sBirthday,config.getSenderFakeBirthday());//Дата рождения отправителя
        childrenMap.put(BaseDoc.sBirthPlace,config.getSenderFakeCity() +" " + config.getSenderFakeAddress());//Место рождения отправителя, для сумм более 15000


        //ПОЛУЧАТЕЛЬ *****************************************************************************************
        childrenMap.put(BaseDoc.bName,borrowerPersonal.getSurname()); //Фамилия получателя, странно,должно быть имя
        childrenMap.put(BaseDoc.bLastName,borrowerPersonal.getName());//Имя получателя, странно,должно быть имя
        childrenMap.put(BaseDoc.bSurName,borrowerPersonal.getMidname());//Отчество получателя
    //    childrenMap.put(BaseDoc.bCity, borAddress.getCity());//Город получателя
        childrenMap.put(BaseDoc.bCity, borAddress.getMandatoryCity());//Город получателя
     //   childrenMap.put(BaseDoc.bRegion, borAddress.getRegion());//Регион (штат) получателя
        if (borAddress.getRegionShort()!=null){
            childrenMap.put(BaseDoc.bRegion, borAddress.getRegionShort().getCodeIso());//Регион (штат) получателя
        } else {
        	childrenMap.put(BaseDoc.bRegion, "");//Регион (штат) получателя
        }
        childrenMap.put(BaseDoc.bZipCode,borAddress.getIndex());//Почтовый индекс получателя
        childrenMap.put(BaseDoc.bCountry, borCountry.getCode());//Страна получателя
        childrenMap.put(BaseDoc.bIDwhom, document.getDocorg());//Кем выдан документ получателя
        childrenMap.put(BaseDoc.bIDnumber,document.getSeries()+" "+document.getNumber());//Паспортные данные получателя
        childrenMap.put(BaseDoc.bIDtype,document.getDocumenttypeId().getName());  //Тип документа получателя

        if(pplcont!=null){
            childrenMap.put(BaseDoc.bPhone,AUtils.parsePhoneToContactForm(pplcont.getValue()));  //Телефон получателя
        }else{
            childrenMap.put(BaseDoc.bPhone,"7-9-054909090");  //Телефон получателя      Fake
        }
        childrenMap.put(BaseDoc.bAddress,borAddress.getDescriptionFromStreet()); //Адрес получателя (улица, дом, квартира)
        try {
            childrenMap.put(BaseDoc.bBirthday,DatesUtils.DATE_FORMAT_YYYYMMdd.format(borrowerPersonal.getBirthdate())); //Дата рождения получателя
        } catch (Exception e) {
           logger.severe("BaseDoc.bBirthday "+e.getMessage());
           throw new ActionException("BaseDoc.bBirthday ", e);
        }
        if (document.getDocenddate()!=null){
            try {
                childrenMap.put(BaseDoc.bIDexpireDate,DatesUtils.DATE_FORMAT_YYYYMMdd.format(document.getDocenddate())); //Дата истечения срока действия документа получателя
            } catch (Exception e) {
                logger.severe("BaseDoc.bIDexpireDate "+e.getMessage());
                throw new ActionException("BaseDoc.bIDexpireDate ", e);
            }
        }
        try {
            childrenMap.put(BaseDoc.bIDdate, DatesUtils.DATE_FORMAT_YYYYMMdd.format(document.getDocdate())); //Дата выдачи документа получателя
        } catch (Exception e) {
            logger.severe("BaseDoc.bIDdate "+e.getMessage());
            throw new ActionException("BaseDoc.bIDdate ", e);
        }
        //childrenMap.put(BaseDoc.bCountryC,"");   //Страна гражданства получателя
        //childrenMap.put(BaseDoc.trnFeesClientCurr,"RUR");//Валюта комиссии с клиента
        //childrenMap.put(BaseDoc.trnRate,"1.0000");//Курс валюты
        //childrenMap.put(BaseDoc.bResident,"");//Страна резиденства получателя

        logger.info("buildSingleRequest is generated! ");

        return childrenMap;
    }


    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        logger.info("querySingleResponse from ContactPayBean with id " + businessObjectId + " Started!");

        ContactPayPluginConfig config = (ContactPayPluginConfig) context.getPluginConfig();
        boolean test = !config.isUseWork();

        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));

        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }


        ResponseUnp response = null;
        try {
            CdtrnCXR cdtrnCXR = doCheckOrder(test,payment.getExternalId(),BaseDoc.OUT_INOUT_VALUE,config);
            response = (ResponseUnp)getResponseParser().parseResponse(cdtrnCXR);

        } catch (ActionException e) {
            logger.severe("Ошибка отправки запроса. Cause: " + e.getMessage());
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            getContactBaseBean().parseError(payment, response);
        } catch (Exception e) {
            logger.severe("Ошибка разбора ответа. Cause: " + e.getMessage());
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        //SUCCESS RESPONSE
        PaymentStatus paymentStatus = checkStateAfterGetState(response.getSTATE());
        if(paymentStatus.equals(PaymentStatus.SUCCESS)){
            handleSuccess(payment, new Date());
            return true;
        } 
        return false;
    }

    private PaymentStatus checkStateAfterGetState(String state){
        if(state == null){
            return PaymentStatus.SENDED;
        }
        int st;
        try {
            st = Convertor.toInteger(state);
        } catch (Exception e) {
            return PaymentStatus.SENDED;
        }
        //-1	Deleted	The transfer deleted before sending to Operation Center
        //0 Новый перевод, оплаченный отправителем
        //1	Перевод обрабатывается Клиринговым центром
        //2	Перевод приостановлен Клиринговым центром
        //3	Перевод отправлен в банк получателя
        //4	Перевод готов к выплате получателю
        //5	Перевод выплачивается получателю
        //6	Перевод выплачен получателю
        //7	Запрос на возврат отправлен в банк отправителя
        //8	Возвращённый перевод готов к выплате отправителю
        //9	Возвращённый перевод выплачивается отправителю
        //10 Возвращённый перевод выплачен отправителю
        //100 Новый перевод, ожидает печати документов
        //101 Новый перевод, ожидает оплаты отправителем

        if(st<0){
            return PaymentStatus.ERROR;
        }

        if(st == 0){
            return PaymentStatus.NEW;
        }

        if(st == 6){
            return PaymentStatus.SUCCESS;
        }

        return PaymentStatus.SENDED;
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

	@Override
	public Integer getPartnerId() {
		return Partner.CONTACT;
	}
	
    @Override
    public boolean getDictionaries(boolean test) throws ActionException {
        CdtrnCXR cdtrn = getFullDic("0",test);
        Dics dics = AUtils.parseDicsV2(cdtrn);
        return puDicsInDatabase(dics);
    }

    /**
     * TODO must be private
     * @param dics
     * @return
     * @throws ActionException
     */
    private boolean puDicsInDatabase(Dics dics) throws ActionException {
        //Put them in our database
        if(dics!=null){
            List<RowUnp> list = null;
            //BANKS
            if(dics.getBanksDic()!=null && dics.getBanksDic().getDataPacket()!=null){
                list = dics.getBanksDic().getDataPacket().getRows();
                if(list!=null){
                    for(RowUnp row : list){
                        refBooks.addContactPoint(row);
                    }
                }
            }
           
            //Serv
            if(dics.getServDic()!=null && dics.getServDic().getDataPacket()!=null){
                list = dics.getServDic().getDataPacket().getRows();
                if(list!=null){
                    for(RowUnp row : list){
                        refBooks.addContactServ(row);
                    }
                }
            }
            //BankServ
            if(dics.getBankServDic()!=null && dics.getBankServDic().getDataPacket()!=null){
                list = dics.getBankServDic().getDataPacket().getRows();
                if(list!=null){
                    for(RowUnp row : list){
                        refBooks.addContactBankServ(row);
                    }
                }
            }

            return true;

        }
        return false;

    }


    /**
     *
     * @param dicVersion
     * @param test
     * @return
     * @throws ActionException
     */
    private CdtrnCXR getFullDic(String dicVersion, boolean test) throws ActionException {
        String ppCode = null;
        if (test) {
            ppCode = BaseDoc.TEST_PPCODE_VALUE;
        } else {
            try {
                ActionContext context = actProc.createActionContext(null, true);
                PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(ContactPayBeanLocal.SYSTEM_NAME), null, 0, Utils.<String, Object>mapOf(), context.getPluginState(ContactPayBeanLocal.SYSTEM_NAME));
                ContactPayPluginConfig config = (ContactPayPluginConfig) plctx.getPluginConfig();
                ppCode = config.getPPcode();
            } catch (Exception e) {
                e.printStackTrace();
                logger.severe("Cannot find PPcode from config.");
            }
            if(ppCode == null){
                ppCode = BaseDoc.PPCODE_VALUE;//если ничто не работает, то хардкод всегда рулит!!!
            }
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        //data.put(BaseDoc.BOOK_ID, dicVersion);//??? TODO
        data.put(BaseDoc.VERSION, dicVersion);//??? TODO
        Partner partner = refBooks.getPartner(Partner.CONTACT);
        String urlString = test ? partner.getUrlTest() : partner.getUrlWork();

        URL contactEndpoint = SoapUtils.generateURLForSOAPConnection(urlString,0,0);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new GetChangesFullMethodAssembler(), false, null, contactEndpoint, null, null, null, null);

        logger.info("getFullDic completed");
        return cdtrn;
    }

    /**
     * Загрузка справочника по частям,вызов крайне мудрен,будем пользовать,если не будет получаться загружать весь
     * @return
     * @throws ActionException
     */
    public boolean getFullDicByParts() throws ActionException {
        String ppCode = null;
        try {
            ActionContext context = actProc.createActionContext(null, true);
            PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(ContactPayBeanLocal.SYSTEM_NAME), null, 0, Utils.<String, Object>mapOf(), context.getPluginState(ContactPayBeanLocal.SYSTEM_NAME));
            ContactPayPluginConfig config = (ContactPayPluginConfig) plctx.getPluginConfig();
            ppCode = config.getPPcode();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Cannot find PPcode from config.");
        }
        if(ppCode == null){
            ppCode = BaseDoc.PPCODE_VALUE;//если ничто не работает, то хардкод всегда рулит!!!
        }
        Partner partner = refBooks.getPartner(Partner.CONTACT);
        String url = partner.getUrlWork();


        Dics dics = loadPartOfDic(0,"",ppCode,url);
        if(dics == null){
            return false;
        }
        //int index=1;
        while(dics!=null && dics.getCurrentPart()<dics.getTotal()){
            puDicsInDatabase(dics);
            dics = loadPartOfDic(dics.getCurrentPart()+1,dics.getBookId(),ppCode,url);
        }

        logger.info("getFullDicByParts completed");
        return true;
    }


    private Dics loadPartOfDic(Integer part,String bookId,String ppCode, String url) throws ActionException {
        //<REQUEST OBJECT_CLASS="TAbonentObject" ACTION="GET_CHANGES" POINT_CODE="" VERSION="" TYPE_VERSION="" PORTION =”” PACK="" BOOK_ID=””/>
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.VERSION, "1");
        data.put(BaseDoc.BOOK_ID, bookId);
        data.put(BaseDoc.PART, String.valueOf(part));
        CdtrnCXR cdtrn =getContactBaseBean().doCall(data, new GetChangesFullPart0MethodAssembler(), false, null, url, null,null,null, null);
        Dics dics = AUtils.parseDicsPartialV2(cdtrn);

        return dics;
    }



    /**
     * Проверяет жив ли сервер Контакта
     * @param test
     * @return
     * @throws ActionException
     */
    public CdtrnCXR doPing(boolean test) throws ActionException {
        String ppCode = null;
        if(test){
            ppCode = BaseDoc.TEST_PPCODE_VALUE;
        }else{
            try {
                ActionContext context = actProc.createActionContext(null, true);
                PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(ContactPayBeanLocal.SYSTEM_NAME), null, 0, Utils.<String, Object>mapOf(), context.getPluginState(ContactPayBeanLocal.SYSTEM_NAME));
                ContactPayPluginConfig config = (ContactPayPluginConfig) plctx.getPluginConfig();
                ppCode = config.getPPcode();
            } catch (Exception e) {
                e.printStackTrace();
                logger.severe("Cannot find PPcode from config.");
            }
            if(ppCode == null){
                ppCode = BaseDoc.PPCODE_VALUE;//если ничто не работает, то хардкод всегда рулит!!!
            }
        }
        Partner partner = refBooks.getPartner(Partner.CONTACT);
        String url = test ? partner.getUrlTest() : partner.getUrlWork();

        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new PingMethodAssembler(), false, null, url, null, null, null, null);
        logger.info("doPing completed");
        return cdtrn;
    }

    /**
     * Делает платеж,главный метод
     * @param test
     * @param childrenData
     * @return
     * @throws ActionException
     */
    private CdtrnCXR doNewAndSendOutgoing(boolean test,Map<String, String> childrenData, ContactPayPluginConfig pluginConfig) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : pluginConfig.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new NewAndSendOutgoingMethodAssembler(), true, childrenData, pluginConfig, null);
        logger.info("doNewAndSendOutgoing completed");
        return cdtrn;
    }

    /**
     * Проверяет состояние сделанного платежа
     * @param test
     * @param docId
     * @param inout
     * @return
     * @throws ActionException
     */
    private CdtrnCXR doCheckOrder(boolean test,String docId,String inout,ContactPayPluginConfig config) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : config.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.DOC_ID, docId);
        data.put(BaseDoc.INOUT, inout);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new GetStateMethodAssembler(), true, null, config, null);
        logger.info("doCheckOrder completed");
        return cdtrn;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void timeout(Timer timer)
    {
        logger.info("Process contact dics for : " + new Date());
        try {
            getFullDicByParts();
        } catch (ActionException e) {
            logger.severe("Auto update of contact dics failed " + e.getCause());
            e.printStackTrace();
        }

    }

    public double doCheckRest(PluginExecutionContext config) throws ActionException {
        ContactPayPluginConfig cfg=(ContactPayPluginConfig) config.getPluginConfig();
    	String ppCode = cfg.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.DEBET, cfg.getDebetAccount());
        data.put(BaseDoc.CURR_ID, BaseDoc.CURR_ID_VALUE);   //0 рубли
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new CheckValidRestCorrMethodAssembler(), true, null, cfg, null);
        CheckRestResponse checkRestResponse = (CheckRestResponse)getCheckRestResponseParser().parseResponse(cdtrn);
        Double balance=new Double(0);
        if (checkRestResponse!=null){
        	if (checkRestResponse.getRE().equals(RESPONSE_CODE_OK)){
                balance=checkRestResponse.getAVAIL_REST();
                paymentService.saveBalance(null, Partner.CONTACT, Account.CONTACT_TYPE, balance, new Date());
        	} else {
        		logger.severe("Произошла ошибка при запросе баланса "+checkRestResponse.getERR_TEXT());
        	}
        }
        logger.info("doCheckRest completed");
        return balance;
    }


    /**
     * Метод исключительно для отладки без подключения всяких активитей
     Формат ответа:
     <RESPONSE SIGN_IT="0" RE="0" AVAIL_REST="1000.00" AVAIL_REST_CURR="1000.00" AVAIL_REST_CURR_CODE="RUR" GLOBAL_VERSION="17.04.2013 15:46:48" GLOBAL_VERSION_SERVER="23.01.2013 16:33:23"/>
     где  AVAIL_REST это доступный остаток в рублях, а AVAIL_REST_CURR - доступный остаток в указанной валюте
     в случае ошибки RE содержит код ошибки, а текст ошибки будет в ERR_TEXT
     */
    public CheckRestResponse doCheckRestTest(boolean test,String debet,Integer currencyId,String url,String testKeysDir) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : BaseDoc.PPCODE_VALUE;

        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.DEBET, debet);
        data.put(BaseDoc.CURR_ID, String.valueOf(currencyId));   //0 рубли
        //CdtrnCXR cdtrn = doCall(data, new CheckValidRestCorrMethodAssembler(),true,null,config);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new CheckValidRestCorrMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", testKeysDir);
        CheckRestResponse checkRestResponse = (CheckRestResponse)getCheckRestResponseParser().parseResponse(cdtrn);

//        logger.info("doCheckRestTest completed");
        return checkRestResponse;
    }

    public ICryptoService getCryptoService() {
        return cryptoService;
    }

    public void setCryptoService(ICryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public ResponseParser getResponseParser() {
        if(responseParser == null){
            responseParser = new ResponseParser();
        }
        return responseParser;
    }

    public CheckRestResponseParser getCheckRestResponseParser() {
        if(checkRestResponseParser == null){
            checkRestResponseParser = new CheckRestResponseParser();
        }
        return checkRestResponseParser;
    }

    public ContactBaseBean getContactBaseBean() {
        if(contactBaseBean == null){
            contactBaseBean = new ContactBaseBean();
            contactBaseBean.setCryptoService(cryptoService);
            contactBaseBean.setRefBooks(refBooks);
            contactBaseBean.setBean(this);
            contactBaseBean.setEmMicro(emMicro);
        }
        return contactBaseBean;
    }

    /**
     * @param test
     * @return
     * @throws ActionException
     */
    public CdtrnCXR doGetPaymentInfo(boolean test,String trnReference,String trnSendPoint,String trnDate,ContactPayPluginConfig pluginConfig) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : pluginConfig.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.trnReference, trnReference);
        data.put(BaseDoc.trnSendPoint, trnSendPoint);
        data.put(BaseDoc.trnDate, trnDate);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new GetMethodAssembler(), true, null, pluginConfig, null);
        logger.info("doGetPaymentInfo completed");
        return cdtrn;
    }


    /**
     * @return
     * @throws ActionException
     */
    public String doGetPaymentInfoString(Integer paymentId,ContactPayPluginConfig pluginConfig) throws ActionException {
        return getContactBaseBean().doGetPaymentInfoString(paymentId,pluginConfig);
    }

}
