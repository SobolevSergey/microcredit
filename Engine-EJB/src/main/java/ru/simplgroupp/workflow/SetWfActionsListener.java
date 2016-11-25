package ru.simplgroupp.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Utils;

public class SetWfActionsListener  extends AbstractExecutionListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8707945333495494092L;
	
	Expression wfAction0;
	Expression wfAction1;
	Expression wfAction2;
	Expression wfAction3;
	Expression wfAction4;
	Expression wfAction5;
	Expression wfAction6;

	private static void putdef(List<WorkflowObjectActionDef> lstRes, Expression wfAction, DelegateExecution execution, Map<String,Object> mpVars, int idx) {
		if (wfAction == null) {
			return;
		}
		
		String wfa =  wfAction.getValue(execution).toString();
		if ( StringUtils.isBlank(wfa)) {
			return;
		}
		
		WorkflowObjectActionDef def = new WorkflowObjectActionDef();
		def.loadFromString(wfa);
		lstRes.add(def);
		
		mpVars.put(ProcessKeys.PREFIX_WF_ACTION+ String.valueOf(idx), def.getSignalRef());
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		checkActionProcessor();
		
		Map<String,Object> mpVars = new HashMap<>(2);
//		execution.removeVariable(ProcessKeys.VAR_WF_ACTIONS);
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
		
		List<WorkflowObjectActionDef> lstRes = new ArrayList<WorkflowObjectActionDef>(7);
		putdef(lstRes, wfAction0, execution, mpVars, 0);
		putdef(lstRes, wfAction1, execution, mpVars, 1);
		putdef(lstRes, wfAction2, execution, mpVars, 2);
		putdef(lstRes, wfAction3, execution, mpVars, 3);
		putdef(lstRes, wfAction4, execution, mpVars, 4);
		putdef(lstRes, wfAction5, execution, mpVars, 5);
		putdef(lstRes, wfAction6, execution, mpVars, 6);	
		
//		execution.setVariableLocal(ProcessKeys.VAR_WF_ACTIONS, lstRes);
		
		mpVars.put(ProcessKeys.VAR_WF_ACTIONS, lstRes);
		
		execution.setVariablesLocal(mpVars);
	}

}
