package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.payment.PayonlineAcquiringBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.util.EnumSet;

/**
 * Fake payonline acquiring bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PayonlineAcquiringFakeBean extends FakePaymentPluginBean implements PayonlineAcquiringBeanLocal {
    @Override
    public String getSystemName() {
        return PayonlineAcquiringBeanLocal.SYSTEM_NAME;
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
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.CARD_TYPE};
    }

    @Override
    public int paymentStatus(Long paymentId, PluginExecutionContext context) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void paymentCancel(String orderId, String transactionId, PluginExecutionContext context) throws KassaException {
        throw new UnsupportedOperationException();
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.PAYONLINE;
	}     
}
