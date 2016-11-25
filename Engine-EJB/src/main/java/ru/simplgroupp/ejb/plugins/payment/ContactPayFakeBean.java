package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.payment.ContactPayBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.util.EnumSet;

/**
 * Fake contact pay plugin
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ContactPayFakeBean extends FakePaymentPluginBean implements ContactPayBeanLocal {
   
    @EJB
    ReferenceBooksLocal refBooks;

    @Override
    public String getSystemName() {
        return ContactPayBeanLocal.SYSTEM_NAME;
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
        return new int[] {Account.CONTACT_TYPE};
    }

    @Override
    public boolean getDictionaries(boolean test) throws ActionException {
        return false;
    }

    @Override
    public CdtrnCXR doPing(boolean test) throws ActionException {
        return null;
    }

    @Override
    public boolean getFullDicByParts() throws ActionException {
        return false;
    }

    @Override
	public Integer getPartnerId() {
		return Partner.CONTACT;
	}

    @Override
    public double doCheckRest(PluginExecutionContext config) throws ActionException {
        return new Double(0);
    }

    @Override
    public String doGetPaymentInfoString(Integer paymentId, ContactPayPluginConfig pluginConfig) throws ActionException {
        return null;
    }
}
