package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import ru.simplgroupp.interfaces.BusinessObjectResult;

public class CheckWFTaskResult implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8846288783347823818L;

	@Override
	public void notify(DelegateTask delegateTask) {
		Map<String, Object> varsLocal = delegateTask.getExecution().getVariablesLocal();

		String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		Object businessObjectId = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);	
		
		BusinessObjectResult taskResult =  new BusinessObjectResult(businessObjectClass, businessObjectId, null, null);
		delegateTask.getExecution().setVariableLocal(ProcessKeys.VAR_TASK_RESULT, taskResult);
	
	}

}
