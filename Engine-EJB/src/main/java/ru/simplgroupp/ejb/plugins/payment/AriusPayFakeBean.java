package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.payment.AriusPayBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Payment;

import javax.ejb.*;
import java.util.EnumSet;

/**
 * Fake arius pay plugin
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AriusPayFakeBean extends FakePaymentPluginBean implements AriusPayBeanLocal {
   

    @Override
    public String getSystemName() {
        return AriusPayBeanLocal.SYSTEM_NAME;
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
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.ARIUS_TYPE};
    }

    @Override
    public boolean doSale(boolean test) throws ActionException {
        return false;
    }
}
