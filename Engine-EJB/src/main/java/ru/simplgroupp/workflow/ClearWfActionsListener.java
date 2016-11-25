package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import ru.simplgroupp.toolkit.common.Utils;

public class ClearWfActionsListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690676872429860766L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		execution.removeVariablesLocal(Utils.listOf(
				ProcessKeys.VAR_WF_ACTIONS, 
				ProcessKeys.PREFIX_WF_ACTION+"0",
				ProcessKeys.PREFIX_WF_ACTION+"1",
				ProcessKeys.PREFIX_WF_ACTION+"2",
				ProcessKeys.PREFIX_WF_ACTION+"3",
				ProcessKeys.PREFIX_WF_ACTION+"4",
				ProcessKeys.PREFIX_WF_ACTION+"5",
				ProcessKeys.PREFIX_WF_ACTION+"6"				
		));		
//		execution.removeVariableLocal(ProcessKeys.VAR_WF_ACTIONS);	
	}

}
