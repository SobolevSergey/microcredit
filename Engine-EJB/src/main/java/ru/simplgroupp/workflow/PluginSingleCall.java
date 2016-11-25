package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.SyncMode;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.ErrorKeys;

import javax.ejb.EJB;
import java.util.EnumSet;
import java.util.Map;

public class PluginSingleCall implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(PluginSingleCall.class);

	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.info("----> ActionProcessor "+actionProcessor);
		logger.info("----> Выполняется метод execute из Plugin Single Call");
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (! plc.getIsActive()) {
			ExceptionInfo errFatal = new ExceptionInfo(ErrorKeys.PLUGIN_OFF, "Плагин " + plc.getPluginName() + " отключен", Type.TECH, ResultType.FATAL);
			execution.setVariableLocal(ProcessKeys.VAR_LAST_ERROR, errFatal);
			return;
		}
		String customKey = (String) varsLocal.get(ProcessKeys.VAR_CUSTOM_KEY);
		
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
		PluginExecutionContext context = new PluginExecutionContext(plc, prevError, numRepeats.intValue(), (Map<String, Object>) varsLocal.get(ProcessKeys.VAR_DATA), actionContext.getPluginState(plc.getPluginName()) );

		EnumSet<SyncMode> supSyncModes = plc.getPlugin().getSyncModesSupported();
		if (plc.getSyncMode().equals(PluginSystemLocal.SyncMode.SYNC) && supSyncModes.contains(PluginSystemLocal.SyncMode.SYNC)) {
            logger.info("Запускаем синхронное выполнение " + plc.getPluginName()+", " +
                    "businessObjectClass: " + businessObjectClass + ", businessObjectId: " + businessObjectId);
			try {
				plc.getPlugin().executeSingle(businessObjectClass, businessObjectId, context);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, null, 
						ProcessKeys.VAR_NUM_REPEATS, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (ActionException ex) {
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_LAST_ERROR, ex.getInfo(), 
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
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
		} else if (plc.getSyncMode().equals(PluginSystemLocal.SyncMode.ASYNC) && supSyncModes.contains(PluginSystemLocal.SyncMode.ASYNC) ) {
            logger.info("Запускаем асинхронное выполнение " + plc.getPluginName()+", " +
                    "businessObjectClass: " + businessObjectClass + ", businessObjectId: " + businessObjectId);
			try {
				boolean bAsyncAnswered = plc.getPlugin().sendSingleRequest(businessObjectClass, businessObjectId, context);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_ASYNC_ANSWERED, bAsyncAnswered, 
						ProcessKeys.VAR_LAST_ERROR, null, 
						ProcessKeys.VAR_NUM_REPEATS, null,
						ProcessKeys.VAR_PLUGIN, plc
				));
			} catch (ActionException ex) {
				logger.error("Произошла ошибка при выполнении PluginSingleCall "+ex);
				execution.setVariablesLocal(Utils.<String, Object>mapOf(
						ProcessKeys.VAR_ASYNC_ANSWERED, null,
						ProcessKeys.VAR_LAST_ERROR, ex.getInfo(),
						ProcessKeys.VAR_NUM_REPEATS, numRepeats,
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
			logger.error("Система " + plc.getPluginName() + " неправильно сконфигурирована");
			execution.setVariablesLocal( Utils.<String, Object>mapOf(
					ProcessKeys.VAR_LAST_ERROR, errFatal,
					ProcessKeys.VAR_PLUGIN, plc
			));
		}
		
	}


}
