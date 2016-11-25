package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.plugins.payment.WinpayPayBeanLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import java.util.EnumSet;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Заглушка для Win-pay
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(WinpayPayBeanLocal.class)
public class WinpayPayFakeBean extends FakePaymentPluginBean implements WinpayPayBeanLocal {
    @Override
    public String getSystemName() {
        return WinpayPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public EnumSet<PluginSystemLocal.Mode> getModesSupported() {
        return EnumSet.of(PluginSystemLocal.Mode.SINGLE);
    }

    @Override
    public EnumSet<PluginSystemLocal.ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(PluginSystemLocal.ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<PluginSystemLocal.SyncMode> getSyncModesSupported() {
        return EnumSet.of(PluginSystemLocal.SyncMode.ASYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public boolean pay(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public boolean status(PaymentEntity payment, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
    	return new Double(0);
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.CARD_TYPE};
    }

    @Override
    public Integer getPartnerId() {
        return Partner.WINPAY;
    }

	@Override
	public String status(Integer paymentId, PluginExecutionContext context)
			throws ActionException {
		throw new UnsupportedOperationException();

	}
}
