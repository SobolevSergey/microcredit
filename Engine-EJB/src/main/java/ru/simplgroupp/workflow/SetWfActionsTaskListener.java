package ru.simplgroupp.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Utils;

public class SetWfActionsTaskListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2723557927824320127L;
	
	Expression wfAction0;
	Expression wfAction1;
	Expression wfAction2;
	Expression wfAction3;
	Expression wfAction4;
	Expression wfAction5;
	Expression wfAction6;

	private static void putdef(List<WorkflowObjectActionDef> lstRes, Expression wfAction, DelegateExecution execution, DelegateTask task, Map<String,Object> mpVars, int idx) {
		if (wfAction == null) {
			return;
		}
		
		String wfa =  wfAction.getValue(execution).toString();
		if ( StringUtils.isBlank(wfa)) {
			return;
		}
		
		WorkflowObjectActionDef def = new WorkflowObjectActionDef();
		def.loadFromString(wfa);
		def.setAssignee(task.getAssignee());
		def.setCandidateGroups("");
		def.setCandidateUsers("");
		for (IdentityLink lnk: task.getCandidates()) {
			if (! StringUtils.isBlank(lnk.getGroupId())) {
				def.setCandidateGroups(def.getCandidateGroups() + lnk.getGroupId() + ",");
			}
			if (! StringUtils.isBlank(lnk.getUserId())) {
				def.setCandidateUsers(def.getCandidateUsers() + lnk.getUserId() + ",");
			}
			
		}
		lstRes.add(def);

		mpVars.put(ProcessKeys.PREFIX_WF_ACTION+ String.valueOf(idx), def.getSignalRef());
	}

	@Override
	public void notify(DelegateTask task) {
		Map<String,Object> mpVars = new HashMap<>(2);
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
//		task.getExecution().removeVariable(ProcessKeys.VAR_WF_ACTIONS);
		
		List<WorkflowObjectActionDef> lstRes = new ArrayList<WorkflowObjectActionDef>(7);
		putdef(lstRes, wfAction0, task.getExecution(), task, mpVars, 0);
		putdef(lstRes, wfAction1, task.getExecution(), task, mpVars, 1);
		putdef(lstRes, wfAction2, task.getExecution(), task, mpVars, 2);
		putdef(lstRes, wfAction3, task.getExecution(), task, mpVars, 3);
		putdef(lstRes, wfAction4, task.getExecution(), task, mpVars, 4);
		putdef(lstRes, wfAction5, task.getExecution(), task, mpVars, 5);
		putdef(lstRes, wfAction6, task.getExecution(), task, mpVars, 6);	
		
//		task.getExecution().setVariableLocal(ProcessKeys.VAR_WF_ACTIONS, lstRes);
		mpVars.put(ProcessKeys.VAR_WF_ACTIONS, lstRes);
		
		task.getExecution().setVariablesLocal(mpVars);		
	}

}
