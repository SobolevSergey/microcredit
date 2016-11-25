package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.ejb.arius.AriusStatusEnum;
import ru.simplgroupp.ejb.arius.AriusTransferStatusResponse;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.AriusAcquiringBeanLocal;
import ru.simplgroupp.interfaces.service.AriusService;
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
@Local(AriusAcquiringBeanLocal.class)
public class AriusAcquiringBean extends PaymentProcessorBean implements AriusAcquiringBeanLocal {
	
	@Inject Logger logger;



    @EJB
    private PaymentDAO paymentDAO;

    @EJB
    AriusService ariusService;


    @Override
	public String getSystemName() {
		return AriusAcquiringBeanLocal.SYSTEM_NAME;
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
		return EnumSet.of(SyncMode.ASYNC);
	}

	@Override
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
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
	public boolean querySingleResponse(String businessObjectClass,Object businessObjectId, PluginExecutionContext context) throws ActionException {
        logger.info("Проверяю статус платежа для погашения кредита через Ариус " + businessObjectId);
        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity payment = null;
        if (paymentId != null) {
            payment = paymentDAO.getPayment(paymentId);
        }

        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.BUSINESS, ResultType.FATAL,null);
        }

        AriusTransferStatusResponse response = ariusService.doTransferStatus(payment.getExternalId(),payment.getExternalId2());

        AriusStatusEnum statusEnum = response.getStatus();
        if(statusEnum == null){
            logger.severe("Arius Transfer Status is null.So shouldn't be.");
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Что то не так, статус не получен.", Type.TECH, ResultType.FATAL,null);
        }else if(statusEnum.equals(AriusStatusEnum.approved)){
            //SUCCESS RESPONSE
            handleSuccess(payment, new Date());
            return true;
        }else if(statusEnum.equals(AriusStatusEnum.declined)){
            //Transfer declined
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.error)){
            //Transaction is declined but something went wrong, please inform your account manager, final status
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined,status is error", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.filtered)){
            //Transaction is declined by fraud internal or external control systems, final status
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined,status is filtered", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.processing)){
            //Transaction is being processed, you should continue polling, non final status
            return false;
        }else if(statusEnum.equals(AriusStatusEnum.unknown)){
            logger.severe("Arius Transfer Status is unknown.Please inform your account manager");
            //The status of transaction is unknown, please inform your account manager, non final status
            return false;
        }
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
		return AriusAcquiringBeanLocal.SYSTEM_DESCRIPTION;
	}
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CARD_TYPE,Account.ARIUS_TYPE};
    }

	@Override
	public Integer getPartnerId() {
		return Partner.ARIUS;
	}
}
