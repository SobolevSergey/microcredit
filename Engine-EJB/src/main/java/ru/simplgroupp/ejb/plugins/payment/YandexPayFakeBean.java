package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.util.EnumSet;

/**
 * Fake yandex pay bean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(YandexPayBeanLocal.class)
public class YandexPayFakeBean extends FakePaymentPluginBean implements YandexPayBeanLocal {
    @Override
    public String getSystemName() {
        return YandexPayBeanLocal.SYSTEM_NAME;
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
    public boolean sendSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
    	return new Double(50000);
    }

    @Override
    public boolean isWalletIdentified(String number, PluginExecutionContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkWalletData(String number, PeopleMainEntity person, PluginExecutionContext context) throws ActionException {
        return true;
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
