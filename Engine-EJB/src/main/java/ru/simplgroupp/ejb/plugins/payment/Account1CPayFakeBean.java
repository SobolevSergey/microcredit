package ru.simplgroupp.ejb.plugins.payment;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ru.simplgroupp.ejb.AbstractPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.payment.Account1CPayBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(Account1CPayBeanLocal.class)
public class Account1CPayFakeBean  extends AbstractPluginBean implements Account1CPayBeanLocal {
	
	 @EJB 
	 ServiceBeanLocal servBean;	

	@EJB
	WorkflowBeanLocal workflow; 
		
	@Override
	public String getSystemName() {
		return Account1CPayBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return true;
	}
	
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf();
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
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
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
		return Account1CPayBeanLocal.SYSTEM_DESCRIPTION;
	}
	
}
