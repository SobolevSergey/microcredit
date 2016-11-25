package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.QiwiGateBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;


/**
 * Плугин для передачи займа через qiwi в качестве провайдера
 */
@Stateless
@Local(QiwiGateBeanLocal.class)
public class QiwiGatePayBean extends PaymentProcessorBean implements QiwiGateBeanLocal {
    @EJB
    private WorkflowBeanLocal workflow;

    @EJB
    PaymentDAO paymentDAO;
    
    @Inject Logger logger;
    		
    @Override
    public String getSystemName() {
        return QiwiGateBeanLocal.SYSTEM_NAME;
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
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
    	logger.info("Send single request from Qiwi gate");
    	PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
             logger.info("PaymentEntity with id " + businessObjectId + " not found");
             return false;
        }
    	return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
    	logger.info("Query single response from Qiwi gate");
   	    PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
	    if (payment == null) {
             logger.info("PaymentEntity with id " + businessObjectId + " not found");
             return false;
         }

        handleSuccess(payment, new Date());
    	return true;
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
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.QIWI_TYPE};
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        logger.info("Execute single from Qiwi gate");
        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity paymentEntity = null;
        if (paymentId != null) {
            paymentEntity = paymentDAO.getPayment(paymentId);
        }


        if (paymentEntity == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.BUSINESS,
                    ResultType.FATAL, null);
        
        }
        if (context.getNumRepeats()<=5){
            handleSuccess(paymentEntity, new Date());
        } else {
    		throw new ActionException(ErrorKeys.TECH_ERROR, "Произошла техническая ошибка с платежом "+paymentId, 
    				Type.TECH,  ResultType.FATAL, null);
    	}
    }

    @Override
    public String getSystemDescription() {
        return QiwiGateBeanLocal.SYSTEM_DESCRIPTION;
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.QIWI;
	}     
}
