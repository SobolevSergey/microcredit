package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.interfaces.plugins.payment.QiwiGateBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;

import java.util.EnumSet;

/**
 * Fake qiwi gate bean
 */
public class QiwiGateFakeBean extends FakePaymentPluginBean implements QiwiGateBeanLocal {
    @Override
    public String getSystemName() {
        return QiwiGateBeanLocal.SYSTEM_NAME;
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
        return new int[]{Account.QIWI_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.QIWI;
	}     
}