package ru.simplgroupp.workflow;

import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class InitBizAction implements JavaDelegate {
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariables();		
		checkActionProcessor();		
		
		Integer actionId = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
		BizActionEntity ent = actionProcessor.getBizActionEntity(actionId); 
		
		Map<String, Object> opts = actionProcessor.getRulesBean().getOptionsForBOP(ent.getSignalRef());
		execution.setVariables(Utils.<String, Object>mapOf(
				ProcessKeys.VAR_OPTIONS, opts, 
				ProcessKeys.VAR_LAST_ERROR, null,
				ProcessKeys.VAR_DATA, Utils.mapOf("schedule", ent.getSchedule(), "retryInterval", ent.getRetryInterval(), "sqlFilter", ent.getSQLFilter()) 
		));		
		
	}

}
