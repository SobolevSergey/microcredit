package ru.simplgroupp.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import ru.simplgroupp.ejb.plugins.payment.ManualPluginConfig;
import ru.simplgroupp.ejb.PluginConfig;

public class SetWfTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5234494075633027755L;

	@Override
	public void notify(DelegateTask delegateTask) {
		
		Map<String, Object> varsLocal = delegateTask.getExecution().getVariablesLocal();
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (plc != null && plc instanceof ManualPluginConfig) {
			ManualPluginConfig mplc = (ManualPluginConfig) plc;
			
			WorkflowObjectStateDef wfDef = mplc.getWFDefByKey(delegateTask.getTaskDefinitionKey());
			delegateTask.setName(wfDef.getDescription());
			delegateTask.addCandidateGroups(wfDef.getCandidateGroups());
			
			List<WorkflowObjectActionDef> lstRes = new ArrayList<WorkflowObjectActionDef>(wfDef.getActions().size());
			lstRes.addAll(wfDef.getActions());
			delegateTask.getExecution().setVariable(ProcessKeys.VAR_WF_ACTIONS, lstRes);
		}
	}

}
