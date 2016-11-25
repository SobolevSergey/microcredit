package ru.simplgroupp.workflow;

import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import ru.simplgroupp.interfaces.BusinessObjectResult;

public class CheckWFTaskResults implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6985911902880675839L;

	@Override
	public void notify(DelegateTask delegateTask) {
		delegateTask.getExecution().setVariableLocal(ProcessKeys.VAR_TASK_RESULT, new ArrayList<BusinessObjectResult>(10));
	}

}
