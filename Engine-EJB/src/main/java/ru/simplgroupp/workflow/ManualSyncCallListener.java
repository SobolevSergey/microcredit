package ru.simplgroupp.workflow;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PluginConfigReadOnly;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.ErrorKeys;

public class ManualSyncCallListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2430074119577962952L;

	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	@EJB
	protected WorkflowBeanLocal workflow;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
		if (workflow == null) {
			workflow = (WorkflowBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_WORKFLOW);
		}
	}		

	@Override
	public void notify(DelegateTask delegateTask) {
		Map<String, Object> varsLocal = delegateTask.getExecution().getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (! plc.getIsActive()) {
			ExceptionInfo errFatal = new ExceptionInfo(ErrorKeys.PLUGIN_OFF, "Плагин " + plc.getPluginName() + " отключен", Type.TECH, ResultType.FATAL);
			delegateTask.getExecution().setVariableLocal(ProcessKeys.VAR_LAST_ERROR, errFatal);
			return;
		}		
		
		checkActionProcessor();
		plc = actionProcessor.refreshPluginConfig(plc, actionContext);
		
		if (plc.getMode().equals(PluginSystemLocal.Mode.PACKET) && plc.getPlugin().getModesSupported().contains(PluginSystemLocal.Mode.PACKET)
				&& plc.getExecutionMode().equals(PluginSystemLocal.ExecutionMode.MANUAL) && plc.getPlugin().getExecutionModesSupported().contains(PluginSystemLocal.ExecutionMode.MANUAL) 
				&& plc.getSyncMode().equals(PluginSystemLocal.SyncMode.SYNC) && plc.getPlugin().getSyncModesSupported().contains(PluginSystemLocal.SyncMode.SYNC)
				) {
			Integer numRepeats = (Integer ) varsLocal.get(ProcessKeys.VAR_NUM_REPEATS);
			ExceptionInfo prevError = null;
			if (numRepeats != null && numRepeats.intValue() > 0) {
				// это повторный вход после ошибки
				prevError = (ExceptionInfo) varsLocal.get(ProcessKeys.VAR_LAST_ERROR);
				numRepeats++;
			} else {
				// это первый вход
				numRepeats = 1;
			}
			PluginExecutionContext context = new PluginExecutionContext((PluginConfigReadOnly) plc, prevError, numRepeats.intValue(), (Map<String, Object>) varsLocal.get(ProcessKeys.VAR_DATA), actionContext.getPluginState(plc.getPluginName()));			
			
			try {
				List<BusinessObjectResult> lstRes = plc.getPlugin().executePacket(context);
				workflow.handlePacketResults(lstRes, plc.getPluginName(), true);
				delegateTask.getExecution().setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, null, 
						ProcessKeys.VAR_NUM_REPEATS, null, 
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (ActionException ex) {
				delegateTask.getExecution().setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, ex.getInfo(), 
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
						ProcessKeys.VAR_PLUGIN, plc
				));
			}			
		} else {
			// неподдерживаемый режим работы
			ExceptionInfo errFatal = new ExceptionInfo(ErrorKeys.PLUGIN_WRONG_CONFIGURATION, "Система " + plc.getPluginName() + " неправильно сконфигурирована", Type.TECH, ResultType.FATAL);
			delegateTask.getExecution().setVariablesLocal(
				Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, errFatal,
						ProcessKeys.VAR_PLUGIN, plc
				)
					
			);				
		}
	}

}
