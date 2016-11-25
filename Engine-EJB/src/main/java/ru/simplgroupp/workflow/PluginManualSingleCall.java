package ru.simplgroupp.workflow;

import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.PluginConfigReadOnly;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.ErrorKeys;

public class PluginManualSingleCall implements JavaDelegate {
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (! plc.getIsActive()) {
			ExceptionInfo errFatal = new ExceptionInfo(ErrorKeys.PLUGIN_OFF, "Плагин " + plc.getPluginName() + " отключен", Type.TECH, ResultType.FATAL);
			execution.setVariableLocal(ProcessKeys.VAR_LAST_ERROR, errFatal);
			return;
		}
		
		BusinessObjectResult taskResult = (BusinessObjectResult) varsLocal.get(ProcessKeys.VAR_TASK_RESULT);
		if (taskResult.getError() == null && taskResult.getAnswered()) {
			// всё полностью сделано в задаче, пост-обработка не нужна
			execution.setVariablesLocal(Utils.<String, Object>mapOf(
					ProcessKeys.VAR_LAST_ERROR, null, 
					ProcessKeys.VAR_NUM_REPEATS, null,
					ProcessKeys.VAR_TASK_RESULT, null
			));			
			return;
		}
		
		checkActionProcessor();
		plc = actionProcessor.refreshPluginConfig(plc, actionContext);
		
		String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		Object businessObjectId = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
				
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
		PluginExecutionContext context = new PluginExecutionContext((PluginConfigReadOnly) plc, prevError, numRepeats.intValue(), (Map<String, Object>) varsLocal.get(ProcessKeys.VAR_DATA), actionContext.getPluginState(plc.getPluginName()) );
		if (taskResult.getError() != null) {
			// задача завершилась с ошибкой, пропускаем пост-обработку
			execution.setVariablesLocal(Utils.<String, Object>mapOf(
					ProcessKeys.VAR_LAST_ERROR, taskResult.getError(), 
					ProcessKeys.VAR_NUM_REPEATS, numRepeats,
					ProcessKeys.VAR_TASK_RESULT, null,
					ProcessKeys.VAR_PLUGIN, plc
			));
			return;
		}		
		
		// ошибки не было, пост-обработка нужна
		if (plc.getSyncMode().equals(PluginSystemLocal.SyncMode.SYNC) && plc.getPlugin().getSyncModesSupported().contains(PluginSystemLocal.SyncMode.SYNC)) {			
			try {
				plc.getPlugin().executeSingle(businessObjectClass, businessObjectId, context);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, null, 
						ProcessKeys.VAR_NUM_REPEATS, null,
						ProcessKeys.VAR_TASK_RESULT, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (ActionException ex) {
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, ex.getInfo(), 
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
						ProcessKeys.VAR_TASK_RESULT, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (Throwable ex) {
				ExceptionInfo exInfo = new ExceptionInfo(0, ex.getMessage(), Type.TECH, ResultType.FATAL);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, exInfo, 
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
						ProcessKeys.VAR_PLUGIN, plc
				));				
			}
		} else if (plc.getSyncMode().equals(PluginSystemLocal.SyncMode.ASYNC) && plc.getPlugin().getSyncModesSupported().contains(PluginSystemLocal.SyncMode.ASYNC) ) {
			try {
				boolean bAsyncAnswered = plc.getPlugin().sendSingleRequest(businessObjectClass, businessObjectId, context);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_ASYNC_ANSWERED, bAsyncAnswered, 
						ProcessKeys.VAR_LAST_ERROR, null, 
						ProcessKeys.VAR_NUM_REPEATS, null,
						ProcessKeys.VAR_TASK_RESULT, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (ActionException ex) {
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_ASYNC_ANSWERED, null,
						ProcessKeys.VAR_LAST_ERROR, ex.getInfo(),
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
						ProcessKeys.VAR_TASK_RESULT, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (Throwable ex) {
				ExceptionInfo exInfo = new ExceptionInfo(0, ex.getMessage(), Type.TECH, ResultType.FATAL);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, exInfo, 
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
						ProcessKeys.VAR_PLUGIN, plc
				));				
			}
		} else {
			// неподдерживаемый режим работы
			ExceptionInfo errFatal = new ExceptionInfo(ErrorKeys.PLUGIN_WRONG_CONFIGURATION, "Система " + plc.getPluginName() + " неправильно сконфигурирована", Type.TECH, ResultType.FATAL);
			execution.setVariablesLocal(Utils.<String, Object>mapOf(
					ProcessKeys.VAR_LAST_ERROR, errFatal,
					ProcessKeys.VAR_PLUGIN, plc
			));
		}
		
	}


}
