package ru.simplgroupp.ejb.plugins.payment;

import java.util.Map;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

public class ManualPluginConfig extends PluginConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8135143732445493256L;
	// для ручного режима
	protected WorkflowObjectStateDef taskSyncSingleExecute = new WorkflowObjectStateDef((String) null); 
	protected WorkflowObjectStateDef taskSyncPacketExecute = new WorkflowObjectStateDef((String) null); 
	protected WorkflowObjectStateDef taskASyncSingleSend = new WorkflowObjectStateDef((String) null); 
	protected WorkflowObjectStateDef taskASyncSingleReceive = new WorkflowObjectStateDef((String) null); 
	
	public ManualPluginConfig() {
		super();
		taskSyncSingleExecute = new WorkflowObjectStateDef((String) null); 
		taskASyncSingleSend = new WorkflowObjectStateDef((String) null); 
		taskASyncSingleReceive = new WorkflowObjectStateDef((String) null); 		
	}
	
	public ManualPluginConfig(String plName) {
		super(plName);
		taskSyncSingleExecute = new WorkflowObjectStateDef((String) null); 
		taskASyncSingleSend = new WorkflowObjectStateDef((String) null); 
		taskASyncSingleReceive = new WorkflowObjectStateDef((String) null); 			
	}
	
	public WorkflowObjectStateDef getTaskSyncSingleExecute() {
		return taskSyncSingleExecute;
	}
	public WorkflowObjectStateDef getTaskASyncSingleSend() {
		return taskASyncSingleSend;
	}
	public WorkflowObjectStateDef getTaskASyncSingleReceive() {
		return taskASyncSingleReceive;
	}
	
	private WorkflowObjectStateDef loadTask(WorkflowObjectStateDef task, String taskDefKey, String prefix, Map<String, Object> source) {
		if (source.containsKey(prefix + "." + taskDefKey + ".name")) {
			task.setName(pluginName + ":" + taskDefKey);
			task.setDescription((String) source.get(prefix + "." + taskDefKey + ".description"));
			
			int n = 0;
			String actDef = (String) source.get(prefix + "." + taskDefKey + "." + ProcessKeys.PREFIX_WF_ACTION + String.valueOf(n));
			while (actDef != null) {
				task.addActionDef(actDef);			
				n++;
				actDef = (String) source.get(prefix + "." + taskDefKey + "." + ProcessKeys.PREFIX_WF_ACTION + String.valueOf(n));
			}
		} else {
			task = new WorkflowObjectStateDef(taskDefKey);
		}		
		return task;
	}
	
	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		
		taskSyncSingleExecute = loadTask(taskSyncSingleExecute, ProcessKeys.TASK_EXECUTE_SINGLE, prefix, source);
		taskSyncPacketExecute = loadTask(taskSyncPacketExecute, ProcessKeys.TASK_EXECUTE_PACKET, prefix, source);
	}
	
	@Override
	public void save(String prefix, Map<String, Object> dest) {
		if (taskSyncSingleExecute != null) {
			dest.put(prefix + "." + ProcessKeys.TASK_EXECUTE_SINGLE + ".name", ProcessKeys.TASK_EXECUTE_SINGLE);
			dest.put(prefix + "." + ProcessKeys.TASK_EXECUTE_SINGLE + ".description", taskSyncSingleExecute.getDescription());
			int n = 0;
			for (WorkflowObjectActionDef act: taskSyncSingleExecute.getActions()) {
				dest.put(prefix + "." + ProcessKeys.TASK_EXECUTE_SINGLE + "." + ProcessKeys.PREFIX_WF_ACTION + String.valueOf(n), act.saveToString());
				n++;
			}
		}
		super.save(prefix, dest);
	}
	
	public WorkflowObjectStateDef getWFDefByKey(String taskDefKey) {
		if (ProcessKeys.TASK_EXECUTE_SINGLE.equals(taskDefKey)) {
			return taskSyncSingleExecute;
		} else if (ProcessKeys.TASK_EXECUTE_PACKET.equals(taskDefKey)) {
			return taskSyncPacketExecute;
		} else {
			return null;
		}
	}
}
