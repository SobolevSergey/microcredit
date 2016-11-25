package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.payment.YandexIdentificationPayBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.EnumSet;

/**
 * Fake yandex pay bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(YandexIdentificationPayBeanLocal.class)
public class YandexIdentificationPayFakeBean extends FakePaymentPluginBean implements YandexIdentificationPayBeanLocal {
    @Override
    public String getSystemName() {
        return YandexIdentificationPayBeanLocal.SYSTEM_NAME;
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
    public String status(Integer paymentId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendSingleRequest(PaymentEntity entity, PluginExecutionContext plctx) throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(PaymentEntity entity, PluginExecutionContext plctx) throws ActionException {
        return false;
    }

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.YANDEX_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.YANDEX;
	}     
}
