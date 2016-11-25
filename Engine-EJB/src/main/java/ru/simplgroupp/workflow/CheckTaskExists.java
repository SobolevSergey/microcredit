package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Task;

public class CheckTaskExists implements JavaDelegate {
	
	Expression taskDefinitionKey;
	Expression varIndicatorName;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String taskDefKey = taskDefinitionKey.getValue(execution).toString();
		String varResName = varIndicatorName.getValue(execution).toString();
		
		Task task = execution.getEngineServices().getTaskService().createTaskQuery().executionId(execution.getId()).taskDefinitionKey(taskDefKey).singleResult();
		if (task == null) {
			execution.setVariableLocal(varResName, false);
		} else {
			execution.setVariableLocal(varResName, true);
		}
	}

}
