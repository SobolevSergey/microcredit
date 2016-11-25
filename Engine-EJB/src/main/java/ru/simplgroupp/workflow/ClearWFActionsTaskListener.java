package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import ru.simplgroupp.toolkit.common.Utils;

public class ClearWFActionsTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4950289985042542784L;

	@Override
	public void notify(DelegateTask task) {
//		task.getExecution().removeVariableLocal(ProcessKeys.VAR_WF_ACTIONS);
		task.getExecution().removeVariablesLocal(Utils.listOf(
				ProcessKeys.VAR_WF_ACTIONS, 
				ProcessKeys.PREFIX_WF_ACTION+"0",
				ProcessKeys.PREFIX_WF_ACTION+"1",
				ProcessKeys.PREFIX_WF_ACTION+"2",
				ProcessKeys.PREFIX_WF_ACTION+"3",
				ProcessKeys.PREFIX_WF_ACTION+"4",
				ProcessKeys.PREFIX_WF_ACTION+"5",
				ProcessKeys.PREFIX_WF_ACTION+"6"				
		));		
	}

}
