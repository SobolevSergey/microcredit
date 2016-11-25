package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Payment;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Абстрактный плагин обработки платежей
 */
public abstract class PaymentProcessorBean extends AbstractPluginBean implements PluginSystemLocal {

	@Inject Logger logger;

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    protected ServiceBeanLocal servBean;

    @EJB
    protected PaymentService paymentService;

    @EJB
    protected CreditDAO creditDAO;
    
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf();
	}
	
    protected BusinessObjectResult handleSuccess(PaymentEntity payment, Date processDate) {
        paymentService.processSuccessPayment(payment.getId(), processDate);
        logger.info("Handle success payment ");
        //если это платеж клиента, то пересчитаем суммы
        if (payment.getPaymenttypeId().getCodeinteger()==Payment.TO_SYSTEM){
        	paymentService.calcCreditAfterPayment(payment.getCreditId().getId(), payment);
        }
        return new BusinessObjectResult(payment.getClass().getName(), payment.getId(), true, null);
    }
    
    protected BusinessObjectResult handleSuccessWait(PaymentEntity payment, Date processDate) {
        paymentService.processSuccessPaymentWait(payment.getId(), processDate);
        return new BusinessObjectResult(payment.getClass().getName(), payment.getId(), true, null);
    }    

    public BusinessObjectResult handleError(PaymentEntity payment, Date processDate, ExceptionInfo exceptionInfo) {
        paymentService.processFailPayment(payment.getId(), processDate, exceptionInfo);
        return new BusinessObjectResult(payment.getClass().getName(), payment.getId(),
                ResultType.FATAL.equals(exceptionInfo.getResultType()), exceptionInfo);
    }

}