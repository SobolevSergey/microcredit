package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.FakePaymentPluginBean;
import ru.simplgroupp.interfaces.plugins.payment.AlfaBankMarengoBeanLocal;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Payment;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionAttributeType;

/**
 * Заглушечный класс для работы с Marengo
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(AlfaBankMarengoBeanLocal.class)
public class AlfaBankMarengoFakeBean  extends FakePaymentPluginBean implements AlfaBankMarengoBeanLocal {
	
	@Override
	public String getSystemName() {
		return AlfaBankMarengoBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return true;
	}
	
	@Override
	public Set<String> getModelTargetsSupported() {
		return Collections.emptySet();
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
	public int[] getSupportedAccountTypes() {
		return new int[] {Account.BANK_TYPE, Account.CARD_TYPE};
	}
	
	@Override
	public String getBusinessObjectClass() {
		return Payment.class.getName();
	}

	@Override
	public String getSystemDescription() {
		return AlfaBankMarengoBeanLocal.SYSTEM_DESCRIPTION;
	}
	
}
