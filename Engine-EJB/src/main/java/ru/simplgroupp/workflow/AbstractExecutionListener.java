package ru.simplgroupp.workflow;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;

public class AbstractExecutionListener implements ExecutionListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2593317069528026447L;
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;	

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
	}

	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}
}
