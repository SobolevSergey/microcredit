package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Task;

public class TaskCompleteByMsgListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6056465576105508693L;
	Expression taskId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		String sTaskId = taskId.getValue(execution).toString();
		
		Task parentTask = execution.getEngineServices().getTaskService().createTaskQuery().taskId(sTaskId).executionId(execution.getId()).singleResult();
		if (parentTask == null) {
			return;
		}
		execution.getEngineServices().getTaskService().complete(parentTask.getId());
		
	}

}
