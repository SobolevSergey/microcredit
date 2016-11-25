package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.CheckRestResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.ResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.ResponseUnp;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.ReqForCancelMethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.ReturnOutgoingMethodAssembler;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.plugins.payment.ContactCancelPayBeanLocal;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ContactCancelPayBean extends PaymentProcessorBean implements ContactCancelPayBeanLocal {
	
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
    PaymentDAO paymentDAO;
    
    @EJB
    CreditDAO creditDAO;
    
    @EJB
    CreditRequestDAO creditRequestDAO;
    
    @EJB
    CreditBeanLocal creditBean;
    
    private ResponseParser responseParser;
    private CheckRestResponseParser checkRestResponseParser;
    private ContactBaseBean contactBaseBean;


    @Override
    public String getSystemName() {
        return ContactCancelPayBean.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return ContactCancelPayBean.SYSTEM_DESCRIPTION;
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
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        logger.info("Вызов sendSingleRequest ContactCancelPay " + businessObjectId);
        ContactCancelPayPluginConfig config = (ContactCancelPayPluginConfig) context.getPluginConfig();
        boolean test = !config.isUseWork();

        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.severe("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        //нечего отменять, платеж не ушел в контакт вообще
        if (StringUtils.isEmpty(payment.getExternalId())){
        	logger.info("Платеж "+businessObjectId+" не ушел в контакт, отменять нечего");
        	return true;
        }
        
        String contactState = "";
        try {
            contactState = getContactBaseBean().doGetPaymentInfoString(payment.getId(),config);
            logger.info("Возврат платежа " + payment.getId() + " статус контакта " + contactState);
        } catch (Exception e) {
            logger.severe("Не удалось запросить статус платежа по Контакту, ошибка "+e.getMessage());
        }
               
        int theState = processState(contactState);
        logger.info("При вызове sendSingleRequest статус платежа "+theState);
        
        if(theState > 0){
            if(theState==10 || theState==9){
            	payment.setStatus(PaymentStatus.REVOKED);
                paymentService.savePayment(payment);            	
                return true;
            }
            if(theState==8){
                return false;
            }
            if(theState==6){
            	logger.warning("Состояние платежа не подходит для возврата, платеж выплачен получателю, делаем платеж активным "+payment.getId());
            	//делаем платеж проведенным
            	payment.setStatus(PaymentStatus.SUCCESS);
            	payment.setIsPaid(true);
          		paymentService.savePayment(payment); 
          		//делаем кредит активным
          		CreditEntity credit=creditDAO.getCreditEntity(payment.getCreditId().getId());
          		if (credit!=null){
          		    credit.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE, BaseCredit.CREDIT_ACTIVE));
				    credit.setIsover(false);
				    credit.setDateStatus(new Date());
				    credit.setCreditdataendfact(null);
				    creditDAO.saveCreditEntity(credit);
				    logger.info("Поставили кредиту статус активен после восстановления платежа Контакта "+businessObjectId);
				    //информация по кредиту
		            CreditDetailsEntity detail=creditBean.findCreditOperation(credit.getId(),null);
		            if (detail!=null){
		            	detail.setAnotherId(payment.getId());
		            	creditDAO.saveCreditDetails(detail);
		            	logger.info("Поставили id платежа в creditdetails после восстановления платежа Контакта "+businessObjectId);
		            }
				    //делаем заявку активной
				    CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(credit.getCreditRequestId().getId());
				    if (creditRequest!=null){
					    creditRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.DECISION));
					    creditRequest.setDateStatus(new Date());
					    creditRequestDAO.saveCreditRequest(creditRequest);
					    logger.info("Поставили заявке статус одобрена после восстановления платежа Контакта "+businessObjectId);
				    }
          		}
				
                return true;
            }
        }
        //идем далее, состояние возвращабельное


          ResponseUnp response = null;
          try {
              CdtrnCXR cdtrnCXR = doRequest(test,payment.getExternalId(),config);
              response = (ResponseUnp)getResponseParser().parseResponse(cdtrnCXR);
          } catch (ActionException e) {
              logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
              throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
          } catch (Exception e) {
              logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
              throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
          }

          try {
              getContactBaseBean().checkError(response);
          } catch (Exception e) {
              logger.log(Level.SEVERE,"Ошибка разбора ответа. Cause: " + e.getMessage(), e);
              if (e instanceof ActionException) {
                  throw (ActionException) e;
              }
              throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.FATAL, e);
          }
               
          if(response!=null && "0".equals(response.getRE())){
              //OK
              if("7".equals(response.getSTATE())){
                  //Запрошен возврат - Запрос на возврат отправлен в банк отправителя
              }else if("8".equals(response.getSTATE())){
                  //Готов к возврату - Возвращённый перевод готов к выплате отправителю
              }else if("9".equals(response.getSTATE())){
                  //К возврату, ожидает выплаты - Возвращённый перевод выплачивается отправителю
              }else if("10".equals(response.getSTATE())){
                  //Возвращён - Возвращённый перевод выплачен отправителю
              		payment.setStatus(PaymentStatus.REVOKED);
              		paymentService.savePayment(payment); 
                    logger.info("Деньги возвращены по платежу " + businessObjectId);
                    return true;
              }
          }

      
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        logger.info("querySingleResponse from ContactCancelPayBean with id " + businessObjectId + " Started!");

        ContactCancelPayPluginConfig config = (ContactCancelPayPluginConfig) context.getPluginConfig();
        boolean test = !config.isUseWork();

        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));

        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        String contactState = "";
        try {
            contactState = getContactBaseBean().doGetPaymentInfoString(payment.getId(),config);
        } catch (Exception e) {
            logger.severe("Не удалось запросить статус платежа по Контакту, ошибка "+e.getMessage());
        }
        
        int theState = processState(contactState);
        logger.info("При вызове querySingleResponse статус платежа "+theState);
        if(theState > 0){
            if(theState==10 || theState==9){
                return true;
            }
            if(theState!=8){
                throw new ActionException(ErrorKeys.BAD_STATE, "Состояние платежа не подходит для возврата", Type.BUSINESS, ResultType.NON_FATAL,null,theState);
            }
        }
        //идем далее, состояние возвращабельное


        ResponseUnp response = null;
        try {
            CdtrnCXR cdtrnCXR = queryRequest(test,payment.getExternalId(),config);
            response = (ResponseUnp)getResponseParser().parseResponse(cdtrnCXR);
        } catch (ActionException e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            getContactBaseBean().checkError(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            if (e instanceof ActionException) {
                throw (ActionException) e;
            }
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        //SUCCESS RESPONSE
        if(response!=null && "0".equals(response.getRE())){
            if("10".equals(response.getSTATE())){
            	payment.setStatus(PaymentStatus.REVOKED);
                paymentService.savePayment(payment); 
                logger.info("Деньги возвращены по платежу " + businessObjectId);
                return true;
            }
        }

        return false;
    }


    /**
     * Делает платеж,главный метод
     * @param test
     * @return
     * @throws ActionException
     */
    private CdtrnCXR doRequest(boolean test,String docId,ContactPayPluginConfig pluginConfig) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : pluginConfig.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.DOC_ID, docId);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new ReqForCancelMethodAssembler(), true, null, pluginConfig, null);
        logger.info("doRequest completed");
        return cdtrn;
    }

    /**
     * Делает платеж,главный метод
     * @param test
     * @return
     * @throws ActionException
     */
    private CdtrnCXR queryRequest(boolean test,String docId,ContactPayPluginConfig pluginConfig) throws ActionException {
        String ppCode = test ? BaseDoc.TEST_PPCODE_VALUE : pluginConfig.getPPcode();
        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.DOC_ID, docId);
        CdtrnCXR cdtrn = getContactBaseBean().doCall(data, new ReturnOutgoingMethodAssembler(), true, null, pluginConfig, null);
        logger.info("queryRequest completed");
        return cdtrn;
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

    /**
     * @param contactState
     * @return
     */

    private int processState(String contactState){
       
        
        Integer state=-1;
        try {
            Document doc= XmlUtils.getDocFromString(contactState);
            if (doc!=null){
                state= Convertor.toInteger(XmlUtils.getAttrubuteValue(XmlUtils.findElement(true, doc, "RESPONSE"), "STATE"));
                if (state!=null){

                    switch (state){
                        case 0: {
                            //status="Новый перевод, оплаченный отправителем";
                            //Наверное отменить можно
                            break;
                        }
                        case 1: {
                            //status="Перевод обрабатывается Клиринговым центром";
                            //Наверное отменить можно также
                            break;
                        }
                        case 2: {
                            //status="Перевод приостановлен Клиринговым центром";
                            //Наверное отменить можно также,по крайней мере попробовать
                            break;
                        }
                        case 3: {
                            //status="Перевод отправлен в банк получателя";
                            //Наверное отменить можно
                            break;
                        }
                        case 4: {
                            //status="Перевод готов к выплате получателю";
                            //Отменить однозначно
                            break;
                        }
                        case 5: {
                            //status="Перевод выплачивается получателю";
                            //Не успели, но стоит попробовать
                            break;
                        }
                        case 6: {
                            //status="Перевод выплачен получателю";
                            //Прощайте денежки
                        	break;
                            //throw new ActionException(ErrorKeys.BAD_STATE, "Состояние платежа не подходит для возврата", Type.BUSINESS, ResultType.NON_FATAL,null,state);
                        }
                        case 7: {
                            //status="Запрос на возврат отправлен в банк отправителя";
                            //Наверное надо еще поотменять, не знаю точно
                            break;
                        }
                        case 8: {
                            //status="Возвращённый перевод готов к выплате отправителю, но еще не выплачен";
                            //Нужды в этом шаге нет, возврат уже начат, уходим отсюда
                            break;
                        }
                        case 9: {
                            //status="Возвращённый перевод выплачивается отправителю";
                            //Наверное это уже возвращено,в нашем случае этого сотояние не будет даже
                            break;
                        }
                        case 10: {
                            //status="Возвращённый перевод выплачен отправителю";
                            //Деньги у нас!
                            break;
                        }
                        case 101: {
                            //status="Новый перевод, ожидает оплаты отправителем";
                            //это что то неизвестное,вызовем Exception,ради того чтобы поймать эту ситуацию,вдруг случится она в будущем
                            throw new ActionException(ErrorKeys.BAD_STATE, "Состояние платежа не подходит для возврата", Type.BUSINESS, ResultType.NON_FATAL,null,state);
                        }
                        case 100: {
                            //status="Новый перевод, ожидает печати документов";
                            //это что то неизвестное,вызовем Exception,ради того чтобы поймать эту ситуацию,вдруг случится она в будущем
                            throw new ActionException(ErrorKeys.BAD_STATE, "Состояние платежа не подходит для возврата", Type.BUSINESS, ResultType.NON_FATAL,null,state);
                        }
                        default: {
                            //status="неизвестный статус";
                            //нет статуса нет денег, возвращаем
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Не удалось запросить статус платежа по Контакту, ошибка "+e.getMessage());
        }
        return state;
    }


}
