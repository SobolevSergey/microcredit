package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.transfer.Prolong;
import ru.simplgroupp.transfer.Refinance;
import ru.simplgroupp.util.WorkflowUtil;

/**
 * listener для процессов, где нужен продукт
 * возвращает общие настройки процесса в зависимости от продукта 
 *
 */
public class InitOptionsProductListener extends AbstractExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3214620787534715905L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		checkActionProcessor();
		
		//ищем в переменных процесса класс бизнес-объекта и его id
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		Object businessObjectid = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		
		Integer productId=null;
		if (Credit.class.getName().equals(businessObjectClass)) {
			Credit credit = actionProcessor.getCredit(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (credit != null) {
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
			}
		} else if (CreditRequest.class.getName().equals(businessObjectClass)) {
			CreditRequest ccRequest = actionProcessor.getCreditRequest(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (ccRequest != null) {
				if (ccRequest.getProduct()!=null){
				    productId=ccRequest.getProduct().getId();
				}
			}
		} else if (Payment.class.getName().equals(businessObjectClass)) {
			Payment pay = actionProcessor.getPayment(Convertor.toInteger(businessObjectid), Utils.setOf());
			if (pay != null) {
				Credit credit = pay.getCredit();
				if (credit.getProduct()!=null){
				    productId=pay.getCredit().getProduct().getId();
				}
			}
		} else if (Prolong.class.getName().equals(businessObjectClass)) {
			Prolong prolong = actionProcessor.getProlong(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
			if (prolong != null) {
				Credit credit = prolong.getCredit();
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
			}
		} else if (Refinance.class.getName().equals(businessObjectClass)) {
			Refinance refinance = actionProcessor.getRefinance(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
			if (refinance != null) {
				Credit credit = refinance.getCredit();
				if (credit.getProduct()!=null){
				    productId=credit.getProduct().getId();
				}
			}
		}//end if business class
		
        String procDefKey = WorkflowUtil.extractProcessDefKey( execution.getProcessDefinitionId() );
		
        if (procDefKey!=null && procDefKey.equals(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE)){
        	procDefKey=ProcessKeys.DEF_CREDIT_REQUEST;
        }
       
		Map<String, Object> opts=null;
		if (productId!=null){
			opts=actionProcessor.getProductBean().getProductConfigForBP(productId, procDefKey);
		} else {
			opts=actionProcessor.getProductBean().getProductConfigForBP(Products.PRODUCT_PDL, procDefKey);
		}
		execution.setVariables(Utils.<String, Object>mapOf(ProcessKeys.VAR_OPTIONS, opts, ProcessKeys.VAR_LAST_ERROR, null));
	}

}
