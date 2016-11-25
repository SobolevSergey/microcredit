package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.util.WorkflowUtil;

public class InitOptionsListener extends AbstractExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6158618810926939364L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		checkActionProcessor();
		
//		ProcessInstance procInst = execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String procDefKey = WorkflowUtil.extractProcessDefKey( execution.getProcessDefinitionId() );
		
		//Map<String, Object> opts = actionProcessor.getRulesBean().getOptionsForBP(procDefKey);
		// TODO вопрос - как при инициализации процесса определить, какой у нас продукт, наверное нужно опции потом перезапрашивать
		Map<String, Object> opts=actionProcessor.getProductBean().getProductConfigForBP(Products.PRODUCT_PDL, procDefKey);
		execution.setVariables(Utils.<String, Object>mapOf(ProcessKeys.VAR_OPTIONS, opts, ProcessKeys.VAR_LAST_ERROR, null));
	}

}
