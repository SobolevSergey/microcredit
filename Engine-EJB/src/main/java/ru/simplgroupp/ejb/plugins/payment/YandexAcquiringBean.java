package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.YandexAcquiringBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class YandexAcquiringBean extends PaymentProcessorBean implements YandexAcquiringBeanLocal {

	@Inject Logger logger;

    @EJB
    PaymentDAO payDAO;
    
    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.YANDEX_TYPE};
    }

    @Override
    public String getSystemName() {
        return YandexAcquiringBeanLocal.SYSTEM_NAME;
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
        return EnumSet.of(SyncMode.SYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId,
                              PluginExecutionContext context) throws ActionException {
        logger.info("Yandex payment for creditrequest " + businessObjectId);
        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity paymentEntity = null;
        if (paymentId != null) {
            paymentEntity = payDAO.getPayment(paymentId);
        }

        if (paymentEntity != null) {
        	if (context.getNumRepeats()<=5){
                handleSuccess(paymentEntity, (Date) data.get("date"));
        	} else {
        		throw new ActionException(ErrorKeys.TECH_ERROR, "Произошла техническая ошибка с платежом Yandex "+paymentId, 
        				Type.TECH,  ResultType.FATAL, null);
        	}
        } else {
            logger.info("Ошибка платежа " + businessObjectId + ". Причина: платёж не найден");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.BUSINESS,
                    ResultType.FATAL, null);
        }
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException {
        logger.info("addToPacket called " + businessObjectId);
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        logger.info("executePacket called");
        // TODO запрашивать реестр оплаченных счетов в почтовом ящике и разбирать его
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
                                     PluginExecutionContext context) throws ActionException {
        logger.info("executePacket sendSingleRequest " + businessObjectId);
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
                                       PluginExecutionContext context) throws ActionException {

        logger.info("querySingleResponse called " + businessObjectId);
        return false;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        logger.info("sendPacketRequest called");
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        logger.info("queryPacketResponse called");
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSystemDescription() {
        return YandexAcquiringBeanLocal.SYSTEM_DESCRIPTION;
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.YANDEX;
	}     
}
