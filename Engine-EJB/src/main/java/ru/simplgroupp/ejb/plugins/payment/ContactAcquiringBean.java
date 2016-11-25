package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.ContactAcquiringBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.*;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ContactAcquiringBeanLocal.class)
public class ContactAcquiringBean  extends PaymentProcessorBean implements ContactAcquiringBeanLocal {
	
	@Inject Logger logger;

	@EJB 
	ServiceBeanLocal servBean;	
	 
    @EJB
    WorkflowBeanLocal workflow;
 
    @EJB
    private PaymentDAO paymentDAO;

	@Override
	public String getSystemName() {
		return ContactAcquiringBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return false;
	}
	
/*
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf();
	}	
*/

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
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        
        logger.info("Contact payment for creditrequest " + businessObjectId);
        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity paymentEntity = null;
        if (paymentId != null) {
            paymentEntity = paymentDAO.getPayment(paymentId);
        }

        if (paymentEntity != null) {
            handleSuccess(paymentEntity, (Date) data.get("date"));
        } else {
            logger.info("Ошибка платежа " + businessObjectId + ". Причина: платёж не найден");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.BUSINESS,ResultType.FATAL, null);
        }
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getBusinessObjectClass() {
		return Payment.class.getName();
	}


	@Override
	public String getSystemDescription() {
		return ContactAcquiringBeanLocal.SYSTEM_DESCRIPTION;
	}
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CONTACT_TYPE};
    }

	@Override
	public Integer getPartnerId() {
		return Partner.CONTACT;
	}
}
