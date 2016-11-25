package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.interfaces.plugins.payment.ContactCancelPayBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Payment;

import javax.ejb.*;
import java.util.EnumSet;

/**
 * Fake contact pay plugin
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ContactCancelPayFakeBean extends FakePaymentPluginBean implements ContactCancelPayBeanLocal {
  
    @Override
    public String getSystemName() {
        return ContactCancelPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CONTACT_TYPE};
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
}
