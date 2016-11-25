package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.interfaces.plugins.payment.WinpayAcquiringBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.EnumSet;

/**
 * Fake winpay acquiring bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WinpayAcquiringFakeBean extends FakePaymentPluginBean implements WinpayAcquiringBeanLocal {
    @Override
    public String getSystemName() {
        return WinpayAcquiringBeanLocal.SYSTEM_NAME;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE, Mode.PACKET);
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
	public Integer getPartnerId() {
		return Partner.WINPAY;
	}     
}
