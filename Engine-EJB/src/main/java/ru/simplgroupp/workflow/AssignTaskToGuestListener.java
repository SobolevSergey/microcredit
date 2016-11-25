package ru.simplgroupp.workflow;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.transfer.Users;

public class AssignTaskToGuestListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -932840676835926983L;
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}		

	@Override
	public void notify(DelegateTask delegateTask) {
		checkActionProcessor();

		Integer clientUserId = (Integer) delegateTask.getExecution().getEngineServices().getRuntimeService().getVariableLocal(delegateTask.getExecution().getId(), ProcessKeys.VAR_CLIENT_USER_ID);
		if (clientUserId == null) {
			Users guest = actionProcessor.getGuestUser();
			delegateTask.setAssignee(guest.getId().toString());
		} else {
			delegateTask.setAssignee(clientUserId.toString());
		}
	}

}
