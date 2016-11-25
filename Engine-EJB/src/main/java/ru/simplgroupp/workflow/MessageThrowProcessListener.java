package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.lang.StringUtils;

public class MessageThrowProcessListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5691630787522099113L;
	Expression messageRef;

	@Override
	public void notify(DelegateExecution delegate) throws Exception {
		String sMessageRef = messageRef.getValue(delegate).toString(); 
		if (StringUtils.isBlank(sMessageRef)) {
			return;
		}
		// TODO Auto-generated method stub
		Execution rootExec = delegate.getEngineServices().getRuntimeService().createExecutionQuery().executionId(delegate.getProcessInstanceId()).singleResult();
		if (rootExec != null) {
			delegate.getEngineServices().getRuntimeService().messageEventReceived(sMessageRef, rootExec.getId(), null);
		}
	}

}
