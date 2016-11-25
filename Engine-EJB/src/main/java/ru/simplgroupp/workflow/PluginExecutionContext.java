package ru.simplgroupp.workflow;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginState;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PluginConfigReadOnly;

public class PluginExecutionContext {

    private PluginConfigReadOnly pluginConfig;

    private PluginState pluginState;

    private ExceptionInfo prevException;

    private int numRepeats;

    private Map<String, Object> data;

    private List<BusinessObjectResult> taskResults;

    public PluginExecutionContext(PluginConfigReadOnly plc, ExceptionInfo prev, int nrepeats, Map<String,
            Object> adata, PluginState plgState) {
        super();
        pluginConfig = plc;
        prevException = prev;
        numRepeats = nrepeats;
        data = adata;
        pluginState = plgState;
    }

    public PluginExecutionContext(PluginConfigReadOnly plc, ExceptionInfo prev, int nrepeats, Map<String,
            Object> adata, List<BusinessObjectResult> taskResults, PluginState plgState) {
        super();
        this.pluginConfig = plc;
        this.prevException = prev;
        this.numRepeats = nrepeats;
        this.data = adata;
        this.taskResults = taskResults;
        this.pluginState = plgState;
    }

    /**
     * Factory method
     *
     * @param actionContext
     * @return execution context
     */
    public static PluginExecutionContext createExecutionContext(ActionContext actionContext, String name) {
        return new PluginExecutionContext(actionContext.getPlugins()
                .getPluginConfig(name), null, 0, Collections.<String, Object>emptyMap(),
                actionContext.getPluginState(name));
    }

    /**
     * Текущие настройки плагина на момент вызова метода
     *
     * @return
     */
    public PluginConfigReadOnly getPluginConfig() {
        return pluginConfig;
    }

    /**
     * Описание ошибки, из-за которой произошёл повторый вызов. При первом вызоые возвращает null.
     *
     * @return
     */
    public ExceptionInfo getPrevException() {
        return prevException;
    }

    /**
     * Сколько раз уже вызывали метод из-за ошибок. При первом вызове numRepeats = 1.
     *
     * @return
     */
    public int getNumRepeats() {
        return numRepeats;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public List<BusinessObjectResult> getTaskResults() {
        return taskResults;
    }

    public PluginState getPluginState() {
        return pluginState;
    }

}
