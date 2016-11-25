package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.FakePaymentPluginBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.LockModeType;

import java.util.*;
import java.util.logging.Logger;

/**
 * Плагин, проводящий платежи безусловно (никуда не передавая)
 *
 * @author Khodyrev DS
 */
public abstract class FakePaymentPluginBean extends PaymentProcessorBean implements PluginSystemLocal,
        FakePaymentPluginBeanLocal {

	@Inject Logger logger;

    @EJB
    WorkflowBeanLocal workflow;

    @EJB
    PaymentDAO paymentDAO;
    
    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId,
                              PluginExecutionContext context) throws ActionException {
    	
    	logger.info("Fake payment for creditrequest " + businessObjectId);
        Map<String, Object> data = context.getData();

        logger.info("Данные контекста " + data);

        Integer paymentId = Convertor.toInteger(data.get("paymentId"));
        PaymentEntity paymentEntity = null;
        if (paymentId != null) {
            paymentEntity = paymentDAO.getPayment(paymentId);
        }


        if (paymentEntity == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            return;
        }

        handleSuccess(paymentEntity, new Date());
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        Partner partner = context.getPluginConfig().getPartner();
        List<PaymentEntity> payments = emMicro.createQuery("select p from ru.simplgroupp.persistence.PaymentEntity p " +
                "where partnersId = ? and status = ?")
                .setParameter(0, partner.getEntity()).setParameter(1, PaymentStatus.NEW).setLockMode(LockModeType
                        .PESSIMISTIC_WRITE).setMaxResults(15)
                .getResultList();


        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(payments.size());

        for (PaymentEntity payment : payments) {
            handleSuccess(payment, new Date());

            result.add(new BusinessObjectResult(payment.getClass().getName(), payment.getId(), false, null));
        }

        return result;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
                                     PluginExecutionContext context) throws ActionException {
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        logger.info("Fake payment for creditrequest, 1 step " + businessObjectId);
        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            return false;
        }

        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
                                       PluginExecutionContext context) throws ActionException {
    	 PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
    	 logger.info("Fake payment for creditrequest, 2 step " + businessObjectId);
         if (payment == null) {
             logger.info("PaymentEntity with id " + businessObjectId + " not found");
             return false;
         }

         handleSuccess(payment, new Date());
         return true;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {

        Partner partner = context.getPluginConfig().getPartner();
        List<PaymentEntity> payments = emMicro.createQuery("select p from ru.simplgroupp.persistence.PaymentEntity p " +
                "where partnersId = ? and status = ?")
                .setParameter(0, partner.getEntity()).setParameter(1, PaymentStatus.NEW).setLockMode(LockModeType
                        .PESSIMISTIC_WRITE).setMaxResults(15)
                .getResultList();

        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(payments.size());

        for (PaymentEntity payment : payments) {

            result.add(new BusinessObjectResult(payment.getClass().getName(), payment.getId(), false, null));
        }

        return result;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        Partner partner = context.getPluginConfig().getPartner();

        Collection<PaymentEntity> payments = emMicro.createQuery("select p from ru.simplgroupp.persistence" +
                ".PaymentEntity p where partnersId = ? and status = ?")
                .setParameter(0, partner.getEntity()).setParameter(1, PaymentStatus.SENDED).setLockMode(LockModeType
                        .PESSIMISTIC_WRITE).setMaxResults(15)
                .getResultList();

        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(payments.size());

        for (PaymentEntity payment : payments) {
            handleSuccess(payment, new Date());

            result.add(new BusinessObjectResult(payment.getClass().getName(), payment.getId(), false, null));
        }

        return result;
    }

    @Override
    public String getSystemDescription() {
        return FakePaymentPluginBeanLocal.SYSTEM_DESCRIPTION;
    }

}
