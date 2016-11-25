package ru.simplgroupp.workflow;

import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class ExecuteBizAction implements JavaDelegate {
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected BizActionProcessorBeanLocal bizProc;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
		if (bizProc == null) {
			bizProc = (BizActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_BIZACTION_PROCESSOR);
		}
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariables();		
		checkActionProcessor();			
		
		Integer actionId = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
		BizActionEntity bizAct = actionProcessor.getBizActionEntity(actionId);
		
		Integer numRepeats = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_NUM_REPEATS));
		ExceptionInfo prevError = null;
		if (numRepeats != null && numRepeats.intValue() > 0) {
			// это повторный вход после ошибки
			prevError = (ExceptionInfo) varsLocal.get(ProcessKeys.VAR_LAST_ERROR);
			numRepeats++;
		} else {
			// это первый вход
			numRepeats = 1;
		}		
		
		try {
			bizProc.executeBizAction(bizAct, null, null);
		} catch (ActionException ex) {
			execution.setVariables(Utils.<String, Object>mapOf(
					ProcessKeys.VAR_LAST_ERROR, ex.getInfo(), 
					ProcessKeys.VAR_NUM_REPEATS, numRepeats
			));				
			return;
		}
		
		execution.setVariables(Utils.<String, Object>mapOf(
				ProcessKeys.VAR_LAST_ERROR, null,
				ProcessKeys.VAR_NUM_REPEATS, numRepeats
		));		
	}
	
}
