package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.impl.el.Expression;

public class EndStateToVarListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5275186704140171705L;
	Expression varName;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		ProcessInstance pinst = execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		
		String sVarName = varName.getValue(execution).toString();
		pinst.getProcessVariables().put(sVarName, execution.getCurrentActivityId());
		
	}

}
