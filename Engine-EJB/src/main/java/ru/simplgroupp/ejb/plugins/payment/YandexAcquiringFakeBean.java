package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.interfaces.plugins.payment.YandexAcquiringBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Partner;

import javax.ejb.*;

import java.util.EnumSet;

/**
 * Fake yandex acquiring bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class YandexAcquiringFakeBean extends FakePaymentPluginBean implements YandexAcquiringBeanLocal {
    @Override
    public String getSystemName() {
        return YandexAcquiringBeanLocal.SYSTEM_NAME;
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
        return new int[]{Account.YANDEX_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.YANDEX;
	}     
}
