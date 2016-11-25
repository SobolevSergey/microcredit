package ru.simplgroupp.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import ru.simplgroupp.ejb.ExecutionTest;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.exception.WorkflowRuntimeException;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectState;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

@Local
public interface WorkflowEngineBeanLocal {

    public List<WorkflowObjectStateDef> getCreditProcessStatesForRequest();

    /**
     * задеплоить процесс 
     * 
     * @param source - процесс
     * @throws WorkflowException
     */
    public void deployProcess(byte[] source) throws WorkflowException;

    public int signalProcessByBusinessKey(String processDefKey, Object businessKey,
                                    String signalRef, Map<String, Object> params)
            throws WorkflowException;

    public void removeProcessByBusinessKey(String processDefKey, Object businessKey)
            throws WorkflowException;

    public List<String> getTopLevelActivityIds(String processDefKey, Object businessKey);

    public ProcessInstance startOrFindProcess(String processDefKey, String varName,
                                       Object businessObjectId, String businessObjectClass, Map<String,
            Object> variables) throws WorkflowException;

    public List<WorkflowObjectStateDef> getProcessWfActions(String processDefKey, Object businessKey,
                                                     Set<String> variableNames, final boolean bIncludeWithActions);

    public void dispatchEvent(String eventName, Object... params);

    public List<ProcessDefinition> getLatestProcessDefinitions();

    public ProcessDefinition getLatestProcessDefinition(String procDefKey);

    public void updateProcessVariables(String processDefKey, String varName, Object varValue);

    public ProcessInstance startProcess(String processDefKey, Object businessKey, Map<String,
            Object> variables) throws WorkflowException;

    public ActionProcessorBeanLocal getActionProcessor();

    public int messageProcessByInstanceId(String processInstanceId, String messageRef, Map<String,
            Object> messageParams);

    public ProcessInstance getProcessInstance(String processInstanceId);

    public List<WorkflowObjectStateDef> listTaskDefs(String assignee,
                                              List<String> candidateRoles, String businessObjectClass,
                                              Object businessObjectId);

    public long countTasks(String taskDefKey, String assignee, List<String> candidateRoles, String businessObjectClass,
                    String procDefKey, String pluginName);

    public List<WorkflowObjectState> listTasks(int nfirst, int ncount, SortCriteria[] sorting, String taskDefKey,
                                        String assignee,
                                        List<String> candidateRoles, String businessObjectClass,
                                        String procDefKey, String pluginName);

    public int messageProcessByBusinessKey(String processDefKey, Object businessKey, String signalRef, Map<String,
            Object> params)
            throws WorkflowException;

    public ProcessInstance findProcess(String processDefKey, Map<String, Object> variables);

    public Map<String, Object> getProcessVariables(String instandeId);

    public Map<String, Object> getHistoricProcessVariables(String instandeId);

	public void assingTaskTo(String taskId, String assignee);

	public List<WorkflowObjectState> getProcessWfActions1(String processDefKey,
			Object businessKey, Set<String> variableNames, final boolean bIncludeWithActions);

	void dummy();

	public ProcessInstance findProcessByBusinessKey(String processDefKey,
			String businessKey);

	public ProductMessagesEntity getProductMessageForBP(Integer productId,String bpName, String messageKey);
	
	ProcessInstance startProcessOptTx(String processDefKey, Object businessKey,
			Map<String, Object> variables) throws WorkflowException;

	void signalProcessInstance(String procInstId);

	public List<Integer> listTasksCR(String taskDefKey);

	boolean isProcessInstanceExists(String processInstanceId);

	public Map<String, Object> getExecutionVariables(String executionId);

	Map<String, Object> evaluateExpressions(Collection<String> exprTexts,
			Map<String, Object> variables);

	public int signalProcessByBusinessKey(String processDefKey, Object businessKey)
			throws WorkflowException, WorkflowRuntimeException;

	int messageProcessBySignalRef(String processDefKey, String businessKey,
			String signalRef, Map<String, Object> params)
			throws WorkflowException, WorkflowRuntimeException;

	List<ProcessInstance> findProcesses(String processDefKey,
			Map<String, Object> variables);

	int messageProcesses(List<ProcessInstance> lstPinst, String signalRef,
			Map<String, Object> params) throws WorkflowException,
			WorkflowRuntimeException;

	List<ProcessInstance> findProcessesTop(String processDefKey,
			SignalRef signalRef, String businessKey);

	List<ProcessInstance> findProcessesByWfAction(String processDefKey,
			SignalRef signalRef, String businessKey);

	List<ProcessInstance> findProcessesByMessage(String processDefKey,
			SignalRef signalRef, String businessKey);

	int messageExecutions(List<Execution> lstPinst, String signalRef,
			Map<String, Object> params) throws WorkflowException,
			WorkflowRuntimeException;

	List<Execution> findExecutionsByMessage(String processDefKey,
			SignalRef signalRef, String businessKey, Map<String, Object> variables);

	List<Execution> findExecutionsByWfAction(String processDefKey,
			SignalRef signalRef, String businessKey, Map<String, Object> variables);

	Map<String, Object> getExecutionVariables(String execId,
			Collection<String> varNames);

	void messageExecution(String executionId, String signalRef,
			Map<String, Object> params) throws WorkflowException,
			WorkflowRuntimeException;

	List<String> findExecutionIds(String eventName, List<String> businessKeys,
			String varName, Long varValue, String varName1);

	/**
	 * Возвращает Execution с данным Id
	 * @param executionId
	 * @return
	 */
	public Execution getExecution(String executionId);

	/**
	 * Проходит по дереву процессов вверх. Когда visitor.test(...)=true, итерация прекращается.
	 * @param executionId - начиная с этого Execution
	 * @param visitor - запускается для каждого шага. 
	 * @param haltOnException - прекращать итерацию в случае сбоя на любом шаге
	 * @throws Throwable
	 */
	public void iterateToParents(String executionId, ExecutionTest visitor,
			boolean haltOnException) throws Throwable;

}
