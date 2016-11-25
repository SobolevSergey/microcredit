package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.payment.QiwiAcquiringBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.util.EnumSet;

/**
 * Fake qiwi acquiring bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class QiwiAcquiringFakeBean extends FakePaymentPluginBean implements QiwiAcquiringBeanLocal {

    @Override
    public String getSystemName() {
        return QiwiAcquiringBeanLocal.SYSTEM_NAME;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC, ExecutionMode.MANUAL);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.ASYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return Credit.class.getName();
    }

    @Override
    public String doOrder(String phone, Integer paymentId, PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public String getPayUrl(PluginExecutionContext context) {
        return null;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.QIWI_TYPE};
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.QIWI;
	}     
}
