package ru.simplgroupp.ejb.plugins.payment;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import ru.simplgroupp.ejb.AbstractPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.ContactAcquiringBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ContactAcquiringBeanLocal.class)
public class ContactAcquiringFakeBean  extends AbstractPluginBean implements ContactAcquiringBeanLocal {
	@Inject Logger log;
	
	@EJB 
	ServiceBeanLocal servBean;	

	@EJB
	WorkflowBeanLocal workflow; 

	
	@Override
	public String getSystemName() {
		return ContactAcquiringBeanLocal.SYSTEM_NAME;
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
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer creditRequestId = (Integer) businessObjectId;
		
		// 1. Ответ сразу получен
//		log.info("Контакт, эмуляция. Запрос на заявку " + creditRequestId.toString() + " отправлен и ответ сразу получен");
//		return true;
		
		// 2. Ответ не сразу получен
		log.info("Контакт, эмуляция. Запрос на заявку " + creditRequestId.toString() + " отправлен и ответ сразу не получен");
		return false;
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer creditRequestId = (Integer) businessObjectId;
		
		// 1. Ответ на заявку получен сразу
//		log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " получен");
//		return true;
		
		// 2. Ответ на заявку получен не сразу
/*		
		if (context.getNumRepeats() == 1) {
			log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " не получен на этот раз");
			return false;
		} else {
			log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " получен");
			return true;
		}
*/		
		// 3. Ждите, нефатальная, ответ
/*		
		if (context.getNumRepeats() == 1) {
			log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " не получен на этот раз");
			return false;
		} else if (context.getNumRepeats() == 2) {
			throw new ActionException(0, "Нефатальная ошибка", Type.BUSINESS, ResultType.NON_FATAL, null);
		} else {
			log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " получен");
			return true;
		}
*/
		// 4. Ждите, нефатальная, фатальная
		if (context.getNumRepeats() == 1) {
			log.info("Контакт, эмуляция. Ответ на заявку " + creditRequestId.toString() + " не получен на этот раз");
			return false;
		} else if (context.getNumRepeats() == 2) {
			throw new ActionException(0, "Нефатальная ошибка", Type.BUSINESS, ResultType.NON_FATAL, null);
		} else {
			throw new ActionException(0, "Нефатальная ошибка", Type.TECH, ResultType.FATAL, null);
		}
		
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
	public String getBusinessObjectClass() {
		return Payment.class.getName();
	}

	@Override
	public int[] getSupportedAccountTypes() {
		return new int[] {Account.CONTACT_TYPE};
	}

	@Override
	public String getSystemDescription() {
		return ContactAcquiringBeanLocal.SYSTEM_DESCRIPTION;
	}	
	
	@Override
	public Integer getPartnerId() {
		return Partner.CONTACT;
	}	
}
