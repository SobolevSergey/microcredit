package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ClearErrorsListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839487757823525659L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		execution.setVariableLocal(ProcessKeys.VAR_LAST_ERROR, null);	
	}

}
