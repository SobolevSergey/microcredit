package ru.simplgroupp.workflow;

import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.Expression;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.toolkit.TransientMap;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Prolong;
import ru.simplgroupp.transfer.Refinance;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.util.WorkflowUtil;

/**
 * Посылаем email
 * @author irina
 *
 */
public class SendEmailAction implements JavaDelegate {
	
	Expression emailCode;
	Expression param0;
	Expression param1;
	Expression param2;
	Expression param3;
	Expression param4;
	Expression param5;
	Expression param6;
	Expression param7;
	Expression param8;
	Expression param9;
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}		

	@Override
	public void execute(DelegateExecution execution) throws Exception {		
		checkActionProcessor();
		
		//ищем в переменных процесса класс бизнес-объекта и его id
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		Object businessObjectid = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		
		Integer peopleMainId = null;
		Integer productId=null;

		TransientMap<String,Object> trm = (TransientMap<String,Object>) execution.getVariable(ProcessKeys.VAR_RUNTIME_VARS);
		if (trm == null) {
			trm = new TransientMap<String,Object>(1);
		}
		
		PeopleMain client = null;
		if (Credit.class.getName().equals(businessObjectClass)) {
			Credit credit = actionProcessor.getCredit(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (credit != null) {
				client = credit.getPeopleMain();
				peopleMainId = credit.getPeopleMain().getId();
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
				trm.put(ProcessKeys.VAR_CREDIT, credit);
				trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());				
			}
		} else if (CreditRequest.class.getName().equals(businessObjectClass)) {
			CreditRequest ccRequest = actionProcessor.getCreditRequest(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (ccRequest != null) {
				client = ccRequest.getPeopleMain();
				peopleMainId = ccRequest.getPeopleMain().getId();
				if (ccRequest.getProduct()!=null){
				    productId=ccRequest.getProduct().getId();
				}
				trm.put(ProcessKeys.VAR_CREDIT_REQUEST, ccRequest);
			}
		} else if (Payment.class.getName().equals(businessObjectClass)) {
			Payment pay = actionProcessor.getPayment(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (pay != null) {
				client = pay.getCredit().getPeopleMain();
				peopleMainId = pay.getCredit().getPeopleMain().getId();
				if (pay.getCredit().getProduct()!=null){
				    productId=pay.getCredit().getProduct().getId();
				}
				trm.put(ProcessKeys.VAR_PAYMENT, pay);
				Credit credit = pay.getCredit();
				trm.put(ProcessKeys.VAR_CREDIT, credit);
				trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());								
			}
		} else if (Prolong.class.getName().equals(businessObjectClass)) {
			Prolong prolong = actionProcessor.getProlong(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
			if (prolong != null) {
				trm.put(ProcessKeys.VAR_PROLONG, prolong);
				Credit credit = prolong.getCredit();
				trm.put(ProcessKeys.VAR_CREDIT, credit);
				trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
				client = credit.getPeopleMain();
				peopleMainId = client.getId();
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
			}
		} else if (Refinance.class.getName().equals(businessObjectClass)) {
			Refinance refinance = actionProcessor.getRefinance(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
			if (refinance != null) {
				trm.put(ProcessKeys.VAR_REFINANCE, refinance);
				Credit credit = refinance.getCredit();
				trm.put(ProcessKeys.VAR_CREDIT, credit);
				trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
				client = credit.getPeopleMain();
				peopleMainId = client.getId();
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
			}
		}
		
		//если человек не найден,возвращаемся
		if (peopleMainId == null) {
			if (varsLocal.containsKey(ProcessKeys.VAR_RUNTIME_VARS)) {
				execution.removeVariable(ProcessKeys.VAR_RUNTIME_VARS);
			}
			return;
		} else {
			trm.put(ProcessKeys.VAR_CLIENT, client);
			Users usr = actionProcessor.getUserByPeople(peopleMainId);
			trm.put(ProcessKeys.VAR_CLIENT_USER, usr);
		}		
		execution.setVariable(ProcessKeys.VAR_RUNTIME_VARS, trm);
		
		String smsMessageCode = emailCode.getValue(execution).toString();
		String procDefKey = WorkflowUtil.extractProcessDefKey(execution.getProcessDefinitionId());
		
		Object oParam0 = (param0 == null)?null: param0.getValue(execution);
		Object oParam1 = (param1 == null)?null: param1.getValue(execution);
		Object oParam2 = (param2 == null)?null: param2.getValue(execution);
		Object oParam3 = (param3 == null)?null: param3.getValue(execution);
		Object oParam4 = (param4 == null)?null: param4.getValue(execution);
		Object oParam5 = (param5 == null)?null: param5.getValue(execution);
		Object oParam6 = (param6 == null)?null: param6.getValue(execution);
		Object oParam7 = (param7 == null)?null: param7.getValue(execution);
		Object oParam8 = (param8 == null)?null: param8.getValue(execution);
		Object oParam9 = (param9 == null)?null: param9.getValue(execution);
		
		execution.removeVariable(ProcessKeys.VAR_RUNTIME_VARS);
		
		actionProcessor.sendEmailKProduct(productId,peopleMainId, procDefKey, smsMessageCode, new Object[] {oParam0, oParam1, oParam2, oParam3, oParam4, oParam5, oParam6, oParam7, oParam8, oParam9 });	
	}

}
