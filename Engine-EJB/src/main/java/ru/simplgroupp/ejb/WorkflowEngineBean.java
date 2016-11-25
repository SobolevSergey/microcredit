package ru.simplgroupp.ejb;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.TransactionSynchronizationRegistry;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.exception.WorkflowRuntimeException;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.WorkflowUtil;
import ru.simplgroupp.workflow.ActivitiEventCustomImpl;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectState;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
//@AccessTimeout(15000)
@Local(WorkflowEngineBeanLocal.class)
public class WorkflowEngineBean implements WorkflowEngineBeanLocal {
	
	@Inject Logger log;
	
    @PersistenceContext(unitName = "BpmPU")
    EntityManager emBpm;	
    
    @EJB
    RulesBeanLocal rulesBean;
    
    @EJB
    ProductDAO productDAO;
    
    @Resource
    TransactionSynchronizationRegistry txReg;
    
    @Resource
    SessionContext sessionCtx;    
	
//    @Inject
	ProcessEngine wfEngine;
	
	private RuntimeService wfRuntimeServ;
	private RepositoryService wfRepServ;
	private TaskService wfTaskServ;
	private HistoryService wfHistServ;
	
	private ProcessEngineConfiguration engineCfg;
	
	private Map<String, List<ProductMessagesEntity>> bpMessagesCacheProduct=new HashMap<String, List<ProductMessagesEntity>>(1);
	
	@PostConstruct
	public void init() {
		engineCfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
//		engineCfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		engineCfg.setJpaEntityManagerFactory(emBpm.getEntityManagerFactory());
/*
		engineCfg.setJpaEntityManagerFactory(emBpm.getEntityManagerFactory())
				.setJpaCloseEntityManager(false)
				.setJpaHandleTransaction(false)
				.setJpaPersistenceUnitName("BpmPU");
*/		
		wfEngine = engineCfg.buildProcessEngine();
		wfRuntimeServ = wfEngine.getRuntimeService();
		wfRepServ = wfEngine.getRepositoryService();
		wfTaskServ = wfEngine.getTaskService();
		wfHistServ = wfEngine.getHistoryService();
	}
	
	/**
	 * Загрузить новую версию процесса. 
	 * source - zip-файл с определением процесса
	 * @param source
	 */
	@Override
	public void deployProcess(byte[] source) throws WorkflowException {
		ByteArrayInputStream stm1 = new ByteArrayInputStream(source);		
		ZipInputStream stm = new ZipInputStream(stm1);
		try {
			try {
				Deployment dep = wfRepServ.createDeployment().addZipInputStream(stm).deploy();
			} finally {
				IOUtils.closeQuietly(stm);
				IOUtils.closeQuietly(stm1);
			}
		} catch (Exception ex) {
			log.severe("Не удалось задеплоить процесс, ошибка "+ex.getMessage());
			throw new WorkflowException(ex);
		}
	}
	
	@Override
	public List<WorkflowObjectStateDef> getCreditProcessStatesForRequest() {
		
		ArrayList<WorkflowObjectStateDef> lst = new ArrayList<WorkflowObjectStateDef>(9);
		// TODO брать из wfEngine
		lst.add(new WorkflowObjectStateDef("stateAll"));
		lst.add(new WorkflowObjectStateDef("stateNew"));
		lst.add(new WorkflowObjectStateDef("stateInput"));
		lst.add(new WorkflowObjectStateDef("stateAccepted"));
		lst.add(new WorkflowObjectStateDef("stateDeclined"));
		lst.add(new WorkflowObjectStateDef("stateOverdue"));
		lst.add(new WorkflowObjectStateDef("stateCompleted"));
		lst.add(new WorkflowObjectStateDef("stateCollector"));
		lst.add(new WorkflowObjectStateDef("stateCourt"));
		
		return lst;
	}
	
	@Override
	public ActionProcessorBeanLocal getActionProcessor() {
		ProcessEngineConfigurationImpl cfg = (ProcessEngineConfigurationImpl) engineCfg;
		return (ActionProcessorBeanLocal) cfg.getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
	}
	
	private ProcessInstance internalStartProcess(String processDefKey, Object businessKey, Map<String, Object> variables) throws WorkflowException {
		String descr=(businessKey!=null)?businessKey.toString():"нет объекта";
		log.info("Стартовал процесс "+processDefKey+" по объекту - "+descr);
		ProcessInstance procInst = null;
		try {
			if (businessKey == null) {
				procInst = wfRuntimeServ.startProcessInstanceByKey(processDefKey, variables);
			} else {
				procInst = wfRuntimeServ.startProcessInstanceByKey(processDefKey, businessKey.toString() , variables);
			}
			return procInst;
		} catch (Exception ex) {
			log.severe("Не удалось стартовать процесс "+processDefKey+", ошибка "+ex.getMessage());
			throw new WorkflowException(ex);
		}		
	}	 
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ProcessInstance startProcessOptTx(String processDefKey, Object businessKey, Map<String, Object> variables) throws WorkflowException {
		return internalStartProcess(processDefKey, businessKey, variables);
	}	
	
	@Override
	@AccessTimeout(value = 50000)
	public ProcessInstance startProcess(String processDefKey, Object businessKey, Map<String, Object> variables) throws WorkflowException {
		return internalStartProcess(processDefKey, businessKey, variables);
	}
	
	@Override
	public ProcessInstance findProcess(String processDefKey, Map<String, Object> variables) {
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		qry.processDefinitionKey(processDefKey);
		for (Map.Entry<String, Object> entry: variables.entrySet()) {
			qry.variableValueEquals(entry.getKey(), entry.getValue());	
		}
		List<ProcessInstance> lst = qry.list();
		if (lst.size() == 0) {
			return null;
		} else {
			return lst.get(0);
		}
	}
	
	@Override
	public List<ProcessInstance> findProcesses(String processDefKey, Map<String, Object> variables) {
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		qry.processDefinitionKey(processDefKey);
		for (Map.Entry<String, Object> entry: variables.entrySet()) {
			qry.variableValueEquals(entry.getKey(), entry.getValue());	
		}
		List<ProcessInstance> lst = qry.list();
		return lst;
	}	
	
	@Override
	public List<ProcessInstance> findProcessesTop(String processDefKey, SignalRef signalRef, String businessKey) {
		ExecutionQuery qry1 = wfRuntimeServ.createExecutionQuery();
		if (signalRef != null) {
			qry1 = qry1.messageEventSubscriptionName(signalRef.getName());
		}
		if (StringUtils.isNotBlank(processDefKey)) {
			qry1.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry1.processInstanceBusinessKey(businessKey);
		}
		List<Execution> lstE = qry1.list();
		Set<String> lstProcIds = findTopProcIds(WorkflowUtil.extractExecutionIds(lstE));
		
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		qry.processInstanceIds(lstProcIds);
		return qry.list();
	}
	
	@Override
	public List<String> findExecutionIds(String eventName, List<String> businessKeys, String varName, Long varValue, String varName1) {
		String sql = "select distinct s.execution_id_ ";
		sql = sql + "from act_ru_event_subscr s inner join act_ru_execution e on (e.proc_inst_id_ = s.proc_inst_id_) inner join act_ru_variable v on (v.proc_inst_id_ = e.proc_inst_id_) "; 
		sql = sql + " where s.event_name_ = :eventName and (e.id_ = e.proc_inst_id_) and (e.business_key_ in (:businessKeys) ) and (v.name_ = :varName) and (v.long_ = :varValue) ";
		sql = sql + " union ( ";
		sql = sql + " select distinct s.execution_id_ ";
		sql = sql + " from act_ru_variable s inner join act_ru_execution e on (e.proc_inst_id_ = s.proc_inst_id_) inner join act_ru_variable v on (v.proc_inst_id_ = e.proc_inst_id_) "; 
		sql = sql + " where (s.name_ like :varName1 and s.text_ = :eventName) and (e.id_ = e.proc_inst_id_) and (e.business_key_ in (:businessKeys) ) and (v.name_ = :varName) and (v.long_ = :varValue))";
		Query qry = emBpm.createNativeQuery(sql);
		qry.setParameter("eventName", eventName);
		qry.setParameter("businessKeys", businessKeys);
		qry.setParameter("varName", varName);
		qry.setParameter("varValue", varValue);
		qry.setParameter("varName1", varName1 + "%");
		
		List<String> lst = qry.getResultList();
		return lst;
	}
	
	@Override
	public List<Execution> findExecutionsByMessage(String processDefKey, SignalRef signalRef, String businessKey, Map<String, Object> variables) {
		ExecutionQuery qry1 = wfRuntimeServ.createExecutionQuery();
		if (signalRef != null) {
			qry1 = qry1.messageEventSubscriptionName(signalRef.getName());
		}
		if (StringUtils.isNotBlank(processDefKey)) {
			qry1.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry1.processInstanceBusinessKey(businessKey);
		}
		if (variables != null) {
			for (Entry<String,Object> entry: variables.entrySet()) {
				qry1.variableValueEquals(entry.getKey(), entry.getValue());
			}
		}
		List<Execution> lstE = qry1.list();
		return lstE;
	}	
	
	@Override
	public List<Execution> findExecutionsByWfAction(String processDefKey, SignalRef signalRef, String businessKey, Map<String, Object> variables) {
		ExecutionQuery qry1 = wfRuntimeServ.createExecutionQuery();
		if (signalRef != null) {
			qry1 = qry1.variableValueEquals(signalRef.getName());
		}
		if (StringUtils.isNotBlank(processDefKey)) {
			qry1.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry1.processInstanceBusinessKey(businessKey);
		}
		if (variables != null) {
			for (Entry<String,Object> entry: variables.entrySet()) {
				qry1.variableValueEquals(entry.getKey(), entry.getValue());
			}
		}		
		List<Execution> lstE = qry1.list();
		return lstE;
	}	
	
	@Override
	public List<ProcessInstance> findProcessesByMessage(String processDefKey, SignalRef signalRef, String businessKey) {
		ExecutionQuery qry1 = wfRuntimeServ.createExecutionQuery();
		if (signalRef != null) {
			qry1 = qry1.messageEventSubscriptionName(signalRef.getName());
		}
		if (StringUtils.isNotBlank(processDefKey)) {
			qry1.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry1.processInstanceBusinessKey(businessKey);
		}
		List<Execution> lstE = qry1.list();
		Set<String> lstProcIds = new HashSet<String>(5);
		lstProcIds.addAll(WorkflowUtil.extractExecutionIds(lstE));
		
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		qry.processInstanceIds(lstProcIds);
		return qry.list();
	}	
	
	@Override
	public List<ProcessInstance> findProcessesByWfAction(String processDefKey, SignalRef signalRef, String businessKey) {
		ExecutionQuery qry1 = wfRuntimeServ.createExecutionQuery();
		if (signalRef != null) {
			qry1 = qry1.variableValueEquals(signalRef.getName());
		}
		if (StringUtils.isNotBlank(processDefKey)) {
			qry1.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry1.processInstanceBusinessKey(businessKey);
		}
		List<Execution> lstE = qry1.list();
		Set<String> lstProcIds = new HashSet<String>(5);
		lstProcIds.addAll(WorkflowUtil.extractExecutionIds(lstE));
		
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		qry.processInstanceIds(lstProcIds);
		return qry.list();
	}	
	
	@Override
	@AccessTimeout(value = 10000)
	public Map<String, Object> getExecutionVariables (String executionId) {
		return wfRuntimeServ.getVariablesLocal(executionId);
	}	
	
	@Override
	public Map<String, Object> getProcessVariables (String instandeId) {
		Map<String, Object> res = new HashMap<String, Object>(5);
		
		List<Execution> lst = wfRuntimeServ.createExecutionQuery().processInstanceId(instandeId).list();
		for (Execution exe: lst) {
			Map<String, Object> mpVars = wfRuntimeServ.getVariablesLocal(exe.getId());
			res.putAll(mpVars);
		}
		return res;
	}
	
	@Override
	public Map<String, Object> getHistoricProcessVariables (String instandeId) {
		Map<String, Object> res = new HashMap<String, Object>(5);
		
		List<HistoricVariableInstance> lst = wfHistServ.createHistoricVariableInstanceQuery().processInstanceId(instandeId).list();
		for (HistoricVariableInstance exe: lst) {
			res.put(exe.getVariableName(), exe.getValue());
		}
		return res;
	}	
	
	@Override
	@AccessTimeout(100000)
	public ProcessInstance findProcessByBusinessKey(String processDefKey, String businessKey) {
		ProcessInstanceQuery qry = wfRuntimeServ.createProcessInstanceQuery();
		if (StringUtils.isNotBlank(processDefKey)) {
			qry = qry.processDefinitionKey(processDefKey);
		}
		if (StringUtils.isNotBlank(businessKey)) {
			qry = qry.processInstanceBusinessKey(businessKey);
		}
		List<ProcessInstance> lstRes = qry.list();
		if (lstRes.size() == 0) {
			return null;
		} else {
			return lstRes.get(0);
		}
	}
	
	@Override
	public ProcessInstance startOrFindProcess(String processDefKey, String varName, Object businessObjectId, String businessObjectClass, Map<String, Object> variables)  
			throws WorkflowException, WorkflowRuntimeException {
		String businessKey = businessObjectClass + ":" + ((businessObjectId == null)?"":businessObjectId.toString());
		List<ProcessInstance> lstRes = wfRuntimeServ.createProcessInstanceQuery().processDefinitionKey(processDefKey).processInstanceBusinessKey(businessKey).list();
		if (lstRes.size() > 0) {
			return lstRes.get(0);
		} else {
			Map<String, Object> vars = new HashMap<String, Object>();
			if (variables != null) {
				vars.putAll(variables);
			}
			vars.put(varName, businessObjectId);
			vars.put(ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId);
			vars.put(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, businessObjectClass);
			
			try {
				ProcessInstance procInst = wfRuntimeServ.startProcessInstanceByKey(processDefKey, businessKey.toString() , vars);
				return procInst;
			} catch (Exception ex) {
				log.severe("Произошла ошибка "+ex.getMessage()+" при запуске процесса businessObjectClass "+businessObjectClass+",id "+businessObjectId);
				throw new WorkflowException(ex);
			}
		}
	}
	
	@Override
	public void updateProcessVariables(String processDefKey, String varName, Object varValue) {
		List<ProcessInstance> lstRes = wfRuntimeServ.createProcessInstanceQuery().processDefinitionKey(processDefKey).list();
		for (ProcessInstance pinst: lstRes) {
			pinst.getProcessVariables().put(varName, varValue);
		}
	}
	
	@Override
	public List<String> getTopLevelActivityIds(String processDefKey, Object businessKey) {
		List<Execution> lstRes = wfRuntimeServ.createExecutionQuery().processDefinitionKey(processDefKey).processInstanceBusinessKey(businessKey.toString()).list();
		
		List<String> lstAct = new ArrayList<String>(lstRes.size());
		for (Execution exe: lstRes) {
			lstAct.add(exe.getActivityId());
		}
		return lstAct;
	}
	
	@Override
	public void removeProcessByBusinessKey(String processDefKey, Object businessKey)  throws WorkflowException, WorkflowRuntimeException {
		List<ProcessInstance> lstInst = wfRuntimeServ.createProcessInstanceQuery().processDefinitionKey(processDefKey).processInstanceBusinessKey(businessKey.toString()).list();
		if (lstInst.size() == 0) {
			return;
		}
		
		try {
			for (ProcessInstance pinst: lstInst) {
				wfRuntimeServ.deleteProcessInstance(pinst.getProcessInstanceId(), "");	
			}			
		} catch (Throwable ex) {
			throw new WorkflowException(ex);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void signalExecutions(ProcessInstance pinst, final String processDefKey) throws Throwable {
		String currentProcessDefKey = WorkflowUtil.extractProcessDefKey(pinst.getProcessDefinitionId());
		if (StringUtils.isBlank(processDefKey) || currentProcessDefKey.equals(processDefKey)) {
			List<Execution> lstExec = wfRuntimeServ.createExecutionQuery().processInstanceId(pinst.getId()).list();
			for (Execution exe: lstExec) {
				try {
					wfRuntimeServ.signal(exe.getActivityId());
				} catch (Throwable ex) {
					
				}
			}
		}

		// обходим подпроцессы	
		if (txReg.getTransactionStatus() == javax.transaction.Status.STATUS_ACTIVE) {
			List<ProcessInstance> lstProc = null;
			boolean bExists = isProcessInstanceExists(pinst.getProcessInstanceId());
			if (! bExists) {
				lstProc = Collections.EMPTY_LIST;
			} else {
				lstProc = wfRuntimeServ.createProcessInstanceQuery().superProcessInstanceId(pinst.getProcessInstanceId()).list();
			}
			for (ProcessInstance subProc: lstProc) {
				signalExecutions(subProc, processDefKey);
			}			
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void signalExecutions(ProcessInstance pinst, final String processDefKey, final String signalRef, ExecutionVisitor visitor, boolean haltOnException) throws Throwable {
		String currentProcessDefKey = WorkflowUtil.extractProcessDefKey(pinst.getProcessDefinitionId());
		if (StringUtils.isBlank(processDefKey) || currentProcessDefKey.equals(processDefKey)) {
			List<Execution> lstExec = wfRuntimeServ.createExecutionQuery().processInstanceId(pinst.getId()).list();
			for (Execution exe: lstExec) {
				
				if (haltOnException) {
					visitor.visit(exe, pinst);
				} else {
					try {
						visitor.visit(exe, pinst);
					} catch (Throwable e) {
						log.log(Level.SEVERE, "Ошибка", e);
					}
				}
			}
		}

		// обходим подпроцессы	
		if (txReg.getTransactionStatus() == javax.transaction.Status.STATUS_ACTIVE) {
			List<ProcessInstance> lstProc = null;
			boolean bExists = isProcessInstanceExists(pinst.getProcessInstanceId());
			if (! bExists) {
				lstProc = Collections.EMPTY_LIST;
			} else {
				lstProc = wfRuntimeServ.createProcessInstanceQuery().superProcessInstanceId(pinst.getProcessInstanceId()).list();
			}
			for (ProcessInstance subProc: lstProc) {
				signalExecutions(subProc, processDefKey, signalRef, visitor, haltOnException);
			}			
		}
	}
	
	@Override
	public void iterateToParents(String executionId, ExecutionTest visitor, boolean haltOnException) throws Throwable {
		
		Execution ex = wfRuntimeServ.createExecutionQuery().executionId(executionId).singleResult();
		ProcessInstance pinst = wfRuntimeServ.createProcessInstanceQuery().processInstanceId(ex.getProcessInstanceId()).singleResult();
		while (ex != null) {
			if (haltOnException) {
				boolean bOk = visitor.test(ex, pinst);
				if (bOk) {
					return;
				}
			} else {
				try {
					boolean bOk = visitor.test(ex, pinst);
					if (bOk) {
						return;
					}					
				} catch (Throwable e) {
					log.log(Level.SEVERE, "Ошибка", e);
				}
			}			
			// ищем execution уровнем выше
			executionId = ex.getParentId();
			if (executionId == null) {
				// ищем в процессе уровнем выше
				List<String> prm = new ArrayList<String>(1);
				prm.add(ex.getId());
				List<String> superExecs = findTopExecIds(prm);
				if (superExecs.size() > 0) {
					executionId = superExecs.get(0);
				}
			}
			if (executionId == null) {
				ex = null;
			} else {
				ex = wfRuntimeServ.createExecutionQuery().executionId(executionId).singleResult();
				pinst = wfRuntimeServ.createProcessInstanceQuery().processInstanceId(ex.getProcessInstanceId()).singleResult();
			}
		}
	}
	
	@Override
	public Execution getExecution(String executionId) {
		Execution ex = wfRuntimeServ.createExecutionQuery().executionId(executionId).singleResult();
		return ex;
	}
	
	@Override
	public void signalProcessInstance(String procInstId) {
		Execution ex = wfRuntimeServ.createExecutionQuery().processInstanceId(procInstId).singleResult();
		wfRuntimeServ.signal(ex.getId());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public int signalProcessByBusinessKey(String processDefKey, Object businessKey)  throws WorkflowException, WorkflowRuntimeException {
		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		List<ProcessInstance> lstPinst = pqry.processInstanceBusinessKey(businessKey.toString()).list();
		if (lstPinst.size() == 0) {
			return 0;
		}
		
		for (ProcessInstance pinst : lstPinst) {
			try {
				signalExecutions(pinst, processDefKey);
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		}
		return lstPinst.size();
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public int signalProcessByBusinessKey(String processDefKey, Object businessKey, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		List<ProcessInstance> lstPinst = pqry.processInstanceBusinessKey(businessKey.toString()).list();
		if (lstPinst.size() == 0) {
			return 0;
		}
		
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		for (ProcessInstance pinst : lstPinst) {
			try {
				signalExecutions(pinst, parsedSignalRef.getProcessDefKey(), parsedSignalRef.getName(), new ExecutionVisitor() {
	
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {					
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {									
								wfRuntimeServ.signalEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		}
		return lstPinst.size();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int messageProcesses(List<ProcessInstance> lstPinst, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		log.info("Сигнал "+signalRef);
		for (ProcessInstance pinst: lstPinst) {
			try {
				log.info("Ключ "+parsedSignalRef.getProcessDefKey());
				log.info("Название "+parsedSignalRef.getName());
				
				signalExecutions(pinst, parsedSignalRef.getProcessDefKey(), parsedSignalRef.getName(), new ExecutionVisitor() {
	                
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}					
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);					
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {
								wfRuntimeServ.messageEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		}
		return lstPinst.size();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void messageExecution(String executionId, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		log.info("Сигнал "+signalRef);

			try {
				log.info("Ключ "+parsedSignalRef.getProcessDefKey());
				log.info("Название "+parsedSignalRef.getName());
				
				List<Execution> lstPinst = wfRuntimeServ.createExecutionQuery().executionId(executionId).list();
				
				visitExecutions(lstPinst, null, new ExecutionVisitor() {
	                
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}					
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);					
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {
								wfRuntimeServ.messageEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false, true, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int messageExecutions(List<Execution> lstPinst, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		log.info("Сигнал "+signalRef);

			try {
				log.info("Ключ "+parsedSignalRef.getProcessDefKey());
				log.info("Название "+parsedSignalRef.getName());
				
				visitExecutions(lstPinst, null, new ExecutionVisitor() {
	                
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}					
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);					
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {
								wfRuntimeServ.messageEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false, true, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		return lstPinst.size();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int messageProcessByBusinessKey(String processDefKey, Object businessKey, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		List<ProcessInstance> lstPinst = pqry.processInstanceBusinessKey(businessKey.toString()).list();
		if (lstPinst.size() == 0) {
			return 0;
		}
		
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		log.info("Сигнал "+signalRef);
		for (ProcessInstance pinst: lstPinst) {
			try {
				log.info("Ключ "+parsedSignalRef.getProcessDefKey());
				log.info("Название "+parsedSignalRef.getName());
				
				signalExecutions(pinst, parsedSignalRef.getProcessDefKey(), parsedSignalRef.getName(), new ExecutionVisitor() {
	                
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}					
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);					
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {
								wfRuntimeServ.messageEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		}
		return lstPinst.size();
	}
	
	public Set<String> findTopProcIds(List<String> execIds) {
		List<String> lstRes = new ArrayList<String>(10);
		List<Object[]> lstCur = new ArrayList(50);
		for (String sid: execIds) {
			lstCur.add(new Object[] {sid, null});
		}
		
		Query qry = emBpm.createNativeQuery("select super_exec_, proc_inst_id_ from act_ru_execution where id_ in (:execIds) and super_exec_ is not null");

		while(true) {
			qry.setParameter("execIds", WorkflowUtil.extractListColumn(lstCur, 0));
			List<Object[]> lstRows = qry.getResultList();
			if (lstRows.size() == 0) {
				break;
			} else {
				lstCur.clear();
				lstCur.addAll(lstRows);
			}
		}
		return WorkflowUtil.extractSetColumn(lstCur, 1);
	}
	
	public List<String> findTopExecIds(List<String> execIds) {
		List<String> lstRes = new ArrayList<String>(10);
		List<Object[]> lstCur = new ArrayList(50);
		for (String sid: execIds) {
			lstCur.add(new Object[] {sid, null});
		}
		
		Query qry = emBpm.createNativeQuery("select super_exec_, proc_inst_id_ from act_ru_execution where id_ in (:execIds) and super_exec_ is not null");

		while(true) {
			qry.setParameter("execIds", WorkflowUtil.extractListColumn(lstCur, 0));
			List<Object[]> lstRows = qry.getResultList();
			if (lstRows.size() == 0) {
				break;
			} else {
				lstCur.clear();
				lstCur.addAll(lstRows);
			}
		}
		return WorkflowUtil.extractListColumn(lstCur, 0);
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int messageProcessBySignalRef(String processDefKey, String businessKey, final String signalRef, final Map<String,Object> params)  throws WorkflowException, WorkflowRuntimeException {
		final SignalRef parsedSignalRef = SignalRef.valueOf(signalRef);
		
		ExecutionQuery eqry = wfRuntimeServ.createExecutionQuery().messageEventSubscriptionName(parsedSignalRef.getName());
		List<Execution> lstE = eqry.list();
		if (lstE.size() == 0) {
			return 0;
		}
		Set<String> lstProcIds = findTopProcIds(WorkflowUtil.extractExecutionIds(lstE));

		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		if (! StringUtils.isBlank(businessKey)) {
			pqry.processInstanceBusinessKey(businessKey.toString());
		}		
		List<ProcessInstance> lstPinst = pqry.list();
		if (lstPinst.size() == 0) {
			return 0;
		}
		
		log.info("Сигнал "+signalRef);
		for (ProcessInstance pinst: lstPinst) {
			if (! lstProcIds.contains(pinst.getId())) {
				continue;
			}
			try {
				log.info("Ключ "+parsedSignalRef.getProcessDefKey());
				log.info("Название "+parsedSignalRef.getName());
				
				signalExecutions(pinst, parsedSignalRef.getProcessDefKey(), parsedSignalRef.getName(), new ExecutionVisitor() {
	                
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Execution exNow = getExecution(execution.getId());
						if (exNow == null) {
							return;
						}					
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
						
						if ( StringUtils.isBlank(parsedSignalRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedSignalRef.getPluginName()) ) {
							List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);					
							if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedSignalRef.getName()) != null) {
								Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
								if (task != null) {
									if (params != null) {
										wfRuntimeServ.setVariablesLocal(execution.getId(), params);
									}								
									wfTaskServ.complete(task.getId());							
									return;
								}
							}
							
							Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedSignalRef.getName()).singleResult();
							if (ex1 != null) {
								wfRuntimeServ.messageEventReceived(parsedSignalRef.getName(), execution.getId(), params );
							}
						}
					}}, false );
			} catch (Throwable e) {
				log.log(Level.SEVERE, "Ошибка", e);
			}
		}
		return lstPinst.size();
	}	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public int messageProcessByInstanceId(String processInstanceId, String messageRef, final Map<String, Object> messageParams) {
		ProcessInstance pinst = wfRuntimeServ.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();	
		if (pinst == null) {
			return 0;
		}
		
		final SignalRef parsedMessageRef = SignalRef.valueOf(messageRef);	
		try {
			signalExecutions(pinst, parsedMessageRef.getProcessDefKey(), parsedMessageRef.getName(), new ExecutionVisitor() {

				@Override
				public void visit(Execution execution, ProcessInstance process) {
					Map<String,Object> varsLocal = null;
					try {
						varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN));
					} catch (org.activiti.engine.ActivitiObjectNotFoundException e) {
						log.severe("Не удалось найти переменные в процессе ");
						// TODO logger
					}
					if (varsLocal == null) {
						return;
					}
					
					if ( StringUtils.isBlank(parsedMessageRef.getPluginName()) || WorkflowUtil.isPluginProcess(varsLocal, parsedMessageRef.getPluginName()) ) {
						List<WorkflowObjectActionDef> lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);
						if ( lst != null && WorkflowUtil.findWfActionA(lst, parsedMessageRef.getName()) != null) {
							Task task = wfTaskServ.createTaskQuery().taskDefinitionKey(execution.getActivityId()).executionId(execution.getId()).singleResult();
							if (task != null) {
								if (messageParams != null) {
									wfRuntimeServ.setVariablesLocal(execution.getId(), messageParams);
								}								
								wfTaskServ.complete(task.getId());					
								return;
							}
						}
						
						Execution ex1 = wfRuntimeServ.createExecutionQuery().executionId(execution.getId()).messageEventSubscriptionName(parsedMessageRef.getName()).singleResult();
						if (ex1 != null) {						
							wfRuntimeServ.messageEventReceived(parsedMessageRef.getName(), execution.getId(), messageParams);
						}
					}

				}}, false );
		} catch (Throwable e) {
			log.log(Level.SEVERE, "Ошибка", e);
		}		
		return 1;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProcessInstance getProcessInstance(String processInstanceId) {
		ProcessInstance pinst = wfRuntimeServ.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		return pinst;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean isProcessInstanceExists(String processInstanceId) {
		ProcessInstance pinst = getProcessInstance(processInstanceId);
		return (pinst != null);
		
	
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<WorkflowObjectStateDef> getProcessWfActions(String processDefKey, Object businessKey, final Set<String> variableNames, final boolean bIncludeWithActions) {
		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		List<ProcessInstance> lstPinst = pqry.processInstanceBusinessKey(businessKey.toString()).list();		
		
		final List<WorkflowObjectStateDef> lstRes = new ArrayList<WorkflowObjectStateDef>(2);	
		for (ProcessInstance pinst : lstPinst) {
			try {
				visitExecutions(pinst, new ExecutionVisitor() {
	
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Set<String> vars = Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN);
						if (variableNames != null) {
							vars.addAll(variableNames);
						}
						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), vars);
						String pluginName = WorkflowUtil.getPluginName(varsLocal);				
						List<WorkflowObjectActionDef> lst = null;
						if (bIncludeWithActions) {
							lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);	
						} else {
							lst = new ArrayList<WorkflowObjectActionDef>(0);
						}					
						if (lst != null) {
							String procDefKey = WorkflowUtil.extractProcessDefKey(process.getProcessDefinitionId());
							WorkflowObjectStateDef state = new WorkflowObjectStateDef((new StateRef(procDefKey, pluginName, execution.getActivityId())).toString() );
							lstRes.add(state);						
							for (WorkflowObjectActionDef def: lst) {	
								WorkflowObjectActionDef def1 = def.copy();
								def1.setSignalRef(SignalRef.toString(procDefKey, pluginName, def.getSignalRef()));
								state.getActions().add(def1);
							}
							
							if (variableNames != null) {
								Map<String,Object> mp = new HashMap<String,Object>(variableNames.size());
								for (String vname: variableNames) {
									if (varsLocal.containsKey(vname)) {
										mp.put(vname, varsLocal.get(vname));
									}
								}
								state.setVariablesLocal(mp);
							}
						}
					}}, false);
			} catch (Throwable e) {
				log.severe("Не удалось найти действия из процесса "+processDefKey+", "+e.getMessage());
			}
		}
		return lstRes;

	}
	
	@Override
	@AccessTimeout(value = 50000)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<WorkflowObjectState> getProcessWfActions1(String processDefKey, Object businessKey, final Set<String> variableNames, final boolean bIncludeWithActions) {
		ProcessInstanceQuery pqry = wfRuntimeServ.createProcessInstanceQuery();
		if (! StringUtils.isBlank(processDefKey)) {
			pqry.processDefinitionKey(processDefKey);
		}
		List<ProcessInstance> lstPinst = pqry.processInstanceBusinessKey(businessKey.toString()).list();
		
		final List<WorkflowObjectState> lstRes = new ArrayList<WorkflowObjectState>(2);
		for (ProcessInstance pinst : lstPinst) {
			try {
				visitExecutions(pinst, new ExecutionVisitor() {
	
					@Override
					public void visit(Execution execution, ProcessInstance process) {
						Set<String> vars = Utils.setOf(ProcessKeys.VAR_WF_ACTIONS, ProcessKeys.VAR_PLUGIN);
						if (variableNames != null) {
							vars.addAll(variableNames);
						}

						Map<String,Object> varsLocal = wfRuntimeServ.getVariablesLocal(execution.getId(), vars);
						String pluginName = WorkflowUtil.getPluginName(varsLocal);	
						
						List<WorkflowObjectActionDef> lst = null;
						if (bIncludeWithActions) {
							lst = (List<WorkflowObjectActionDef>) varsLocal.get(ProcessKeys.VAR_WF_ACTIONS);	
						} else {
							lst = new ArrayList<WorkflowObjectActionDef>(0);
						}
						
						if (lst != null) {
							String procDefKey = WorkflowUtil.extractProcessDefKey(process.getProcessDefinitionId());
							WorkflowObjectStateDef stateDef = new WorkflowObjectStateDef((new StateRef(procDefKey, pluginName, execution.getActivityId())).toString() );
							WorkflowObjectState state = new WorkflowObjectState(stateDef);
							lstRes.add(state);						
							for (WorkflowObjectActionDef def: lst) {	
								WorkflowObjectActionDef def1 = def.copy();
								def1.setSignalRef(SignalRef.toString(procDefKey, pluginName, def.getSignalRef()));
								stateDef.getActions().add(def1);
							}
							
							if (variableNames != null) {
								Map<String,Object> mp = new HashMap<String,Object>(variableNames.size());
								for (String vname: variableNames) {
									if (varsLocal.containsKey(vname)) {
										mp.put(vname, varsLocal.get(vname));
									}
								}
								stateDef.setVariablesLocal(mp);
							}
							
							Task curTask = wfTaskServ.createTaskQuery()
									.taskDefinitionKey(execution.getActivityId())
									.executionId(execution.getId())
									.singleResult();
							state.setTask(curTask);
							if (curTask != null) {
								stateDef.setDescription(curTask.getName());
							} else {
								// TODO
							}
						}
					}}, false);
			} catch (Throwable e) {
				log.severe("Не удалось получить действия из процесса "+processDefKey+", "+e.getMessage());
			
			}
		}
		return lstRes;

	}	
	
	protected void visitExecutions(List<Execution> lstExec, ProcessInstance process, ExecutionVisitor visitor, boolean bProc, boolean bExec, boolean haltOnException) throws Throwable {
		if (lstExec.size() == 1) {
			// это процесс без дополнительных веток
			Execution exe = lstExec.get(0);
			if (haltOnException) {
				visitor.visit(exe, process);
			} else {
				try {
					visitor.visit(exe, process);
				} catch (Throwable e) {
					log.log(Level.SEVERE, "Ошибка", e);
				}
			}			
			return;
		}
		for (Execution exe: lstExec) {
			if (bProc && exe.getId().equals(exe.getProcessInstanceId())
				|| (bExec && (! exe.getId().equals(exe.getProcessInstanceId())) )	) {
				if (haltOnException) {
					visitor.visit(exe, process);
				} else {
					try {
						visitor.visit(exe, process);
					} catch (Throwable e) {
						log.log(Level.SEVERE, "Ошибка", e);
					}
				}
				
			}
			
	
		}
	}
	
	protected void visitExecutions(ProcessInstance pinst, ExecutionVisitor visitor, boolean haltOnException) throws Throwable {
		List<Execution> lstAct = wfRuntimeServ.createExecutionQuery().processInstanceId(pinst.getProcessInstanceId()).list();
		visitExecutions(lstAct, pinst, visitor, false, true, haltOnException);
		
		// обходим подпроцессы
		List<ProcessInstance> lstProc = wfRuntimeServ.createProcessInstanceQuery().superProcessInstanceId(pinst.getProcessInstanceId()).list();
		for (ProcessInstance subProc: lstProc) {
			visitProcessInstances(subProc, visitor, haltOnException);
		}		
	}	
		
	protected void visitProcessInstances(ProcessInstance pinst, ExecutionVisitor visitor, boolean haltOnException) throws Throwable {
		List<Execution> lstAct = wfRuntimeServ.createExecutionQuery().processInstanceId(pinst.getProcessInstanceId()).list();
		visitExecutions(lstAct, pinst, visitor, true, true, haltOnException);
		
		// обходим подпроцессы
		List<ProcessInstance> lstProc = wfRuntimeServ.createProcessInstanceQuery().superProcessInstanceId(pinst.getProcessInstanceId()).list();
		for (ProcessInstance subProc: lstProc) {
			visitProcessInstances(subProc, visitor, haltOnException);
		}		
	}
	
	@Override
	public void dispatchEvent(String eventName, Object... params) {
		Map<String, Object> mp = new HashMap<String, Object>(4);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i = i + 2) {
				String sname = (String) params[i];
				Object svalue = params[i+1];
				mp.put(sname, svalue);
			}
		}
		ActivitiEvent event = new ActivitiEventCustomImpl(eventName, mp);
		wfRuntimeServ.dispatchEvent(event);
	}
	
	@Override
	@AccessTimeout(100000)
	public ProcessDefinition getLatestProcessDefinition(String procDefKey) {
		return wfRepServ.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AccessTimeout(100000)
	public List<ProcessDefinition> getLatestProcessDefinitions() {
		return wfRepServ.createProcessDefinitionQuery().latestVersion().orderByProcessDefinitionKey().asc().list();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@AccessTimeout(100000)
	public List<WorkflowObjectStateDef> listTaskDefs(String assignee, List<String> candidateRoles,  String businessObjectClass, Object businessObjectId) {
		String sql = "select d.key_, vp.text_, e.act_id_, max(t.name_), sum(1), max(vb.text_) as businessobjectclass ";
		sql = sql + " from act_ru_execution e inner join act_re_procdef d on (d.id_ = e.proc_def_id_)";
		
		sql = sql + " inner join act_ru_variable v on (v.proc_inst_id_ = e.proc_inst_id_ and v.name_ = ?1)";
		if (businessObjectClass != null) {
			sql = sql + " inner join act_ru_variable vb on (vb.proc_inst_id_ = e.proc_inst_id_ and vb.name_ = ?3 and vb.text_ = ?4)";
		} else {
			sql = sql + " left outer join act_ru_variable vb on (vb.proc_inst_id_ = e.proc_inst_id_ and vb.name_ = ?3)";
		}
		sql = sql + " left outer join act_ru_variable vp on (vp.proc_inst_id_ = e.proc_inst_id_ and vp.name_ = ?2)";
		sql = sql + " left outer join act_ru_task t on (t.execution_id_ = e.id_)";
		if (businessObjectId != null) {
			String sfield = "text_";
			if (businessObjectId instanceof Double || businessObjectId instanceof Float || businessObjectId instanceof BigInteger) {
				sfield = "double_";
			} else if (businessObjectId instanceof Number) {
				sfield = "long_";
			}
			sql = sql + " inner join act_ru_variable v2 on (v2.execution_id_ = e.id_ and v2.name_ = ?5 and v." + sfield + " = ?6)";
		}		
		
	/*	sql = sql + " inner join act_ru_variable v on (v.execution_id_ = e.id_ and v.name_ = :wfActions)";
		if (businessObjectClass != null) {
			sql = sql + " inner join act_ru_variable vb on (vb.execution_id_ = e.id_ and vb.name_ = :varBOCname and vb.text_ = :businessObjectClass)";
		}		
		sql = sql + " left outer join act_ru_variable vp on (vp.execution_id_ = e.id_ and vp.name_ = :pluginName)";
		sql = sql + " left outer join act_ru_task t on (t.execution_id_ = e.id_)";
		if (businessObjectId != null) {
			String sfield = "text_";
			if (businessObjectId instanceof Double || businessObjectId instanceof Float || businessObjectId instanceof BigInteger) {
				sfield = "double_";
			} else if (businessObjectId instanceof Number) {
				sfield = "long_";
			}
			sql = sql + " inner join act_ru_variable v2 on (v2.execution_id_ = e.id_ and v2.name_ = :varBOIname and v." + sfield + " = :businessObjectId)";
		}*/		
		
		sql = sql + " where (1=1) ";
		if (candidateRoles != null) {
			String sarr = "";
			for (String ss: candidateRoles) {
				sarr = sarr + "'" + ss + "'" + ",";
			}
			if (sarr.length() > 0) {
				sarr = sarr.substring(0, sarr.length()-1);
			}
			sql = sql + " and (exists (select * from act_ru_identitylink l where l.task_id_ = t.id_ and l.type_ = 'candidate' and l.group_id_ in (" + sarr +")) )";
		}
		
		if (assignee != null) {
			sql = sql + " and (t.assignee_ = ?7)";
		}		
		
		/*if (assignee != null) {
			sql = sql + " and (t.assignee_ = :assignee)";
		}*/		
		
		sql = sql + " group by d.key_, vp.text_, e.act_id_";
		
		Query qry = emBpm.createNativeQuery(sql);
		qry.setParameter(1, ProcessKeys.VAR_WF_ACTIONS);
		qry.setParameter(2, ProcessKeys.VAR_PLUGIN_NAME);
		if (businessObjectClass != null) {
			qry.setParameter(3, ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			qry.setParameter(4, businessObjectClass);
		} else {
			qry.setParameter(3, ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		}
		if (businessObjectId != null) {
			qry.setParameter(5, ProcessKeys.VAR_BUSINESS_OBJECT_ID);
			qry.setParameter(6, businessObjectId);			
		} 		
		if (assignee != null) {
			qry.setParameter(7, assignee);
		}	
		/*qry.setParameter("wfActions", ProcessKeys.VAR_WF_ACTIONS);
		qry.setParameter("pluginName", ProcessKeys.VAR_PLUGIN_NAME);
		if (businessObjectClass != null) {
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			qry.setParameter("businessObjectClass", businessObjectClass);
		}	
		if (businessObjectId != null) {
			qry.setParameter("varBOIname", ProcessKeys.VAR_BUSINESS_OBJECT_ID);
			qry.setParameter("businessObjectId", businessObjectId);			
		}		
		if (assignee != null) {
			qry.setParameter("assignee", assignee);
		}*/		

		List<Object[]> lst = qry.getResultList();
		List<WorkflowObjectStateDef> lstRes = new ArrayList<WorkflowObjectStateDef>(lst.size());
		for (Object[] row: lst) {
			WorkflowObjectStateDef tdef = new WorkflowObjectStateDef(StateRef.toString((String) row[0], (String) row[1], (String) row[2]) );
			tdef.setDescription((String) row[3]);
			tdef.setItemCount(((Number) row[4]).intValue() );
			tdef.setBusinessObjectClass((String) row[5]); 
			lstRes.add(tdef);
		}
		return lstRes;		
	}
/*	
	public List<WorkflowObjectStateDef> listTaskDefs(String assignee, List<String> candidateRoles,  String businessObjectClass, Object businessObjectId) {
		String sql = "select t.task_def_key_, max(t.name_), sum(1)  from act_ru_task t ";
		if (businessObjectClass != null) {
			sql = sql + " inner join act_ru_variable v on (v.proc_inst_id_ = t.proc_inst_id_ and v.name_ = :varBOCname and v.text_ = :businessObjectClass)";
		}
		if (businessObjectId != null) {
			String sfield = "text_";
			if (businessObjectId instanceof Double || businessObjectId instanceof Float || businessObjectId instanceof BigInteger) {
				sfield = "double_";
			} else if (businessObjectId instanceof Number) {
				sfield = "long_";
			}
			sql = sql + " inner join act_ru_variable v2 on (v2.proc_inst_id_ = t.proc_inst_id_ and v2.name_ = :varBOIname and v." + sfield + " = :businessObjectId)";
		}
		sql = sql + " where (1=1) ";
		if (candidateRoles != null) {
			String sarr = "";
			for (String ss: candidateRoles) {
				sarr = sarr + "'" + ss + "'" + ",";
			}
			if (sarr.length() > 0) {
				sarr = sarr.substring(0, sarr.length()-1);
			}
			sql = sql + " and (exists (select * from act_ru_identitylink l where l.task_id_ = t.id_ and l.type_ = 'candidate' and l.group_id_ in (" + sarr +")) )";
		}
		if (assignee != null) {
			sql = sql + " and (t.assignee_ = :assignee)";
		}
		sql = sql + " group by t.task_def_key_";
		Query qry = emBpm.createNativeQuery(sql);
		if (businessObjectClass != null) {
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			qry.setParameter("businessObjectClass", businessObjectClass);
		}
		if (businessObjectId != null) {
			qry.setParameter("varBOIname", ProcessKeys.VAR_BUSINESS_OBJECT_ID);
			qry.setParameter("businessObjectId", businessObjectId);			
		}
		if (assignee != null) {
			qry.setParameter("assignee", assignee);
		}	
		List<Object[]> lst = qry.getResultList();
		List<WorkflowObjectStateDef> lstRes = new ArrayList<WorkflowObjectStateDef>(lst.size());
		for (Object[] row: lst) {
			WorkflowObjectStateDef tdef = new WorkflowObjectStateDef(row[0].toString());
			tdef.setDescription((String) row[1]);
			tdef.setItemCount(((Number) row[2]).intValue() );
			lstRes.add(tdef);
		}
		return lstRes;
	}
*/	
	@Override
	public long countTasks( String taskDefKey, String assignee, List<String> candidateRoles,  String businessObjectClass, String procDefKey, String pluginName) {
		String sql = "select count(*) from act_ru_execution e inner join act_re_procdef d on (d.id_ = e.proc_def_id_)";
		sql = sql + " inner join act_ru_variable v on (v.execution_id_ = e.id_ and v.name_ = :wfActions)";
		if (businessObjectClass == null) { 
			sql = sql + " inner join act_ru_variable vb on (vb.execution_id_ = e.id_ and vb.name_ = :varBOCname)";
		} else {
			sql = sql + " inner join act_ru_variable vb on (vb.execution_id_ = e.id_ and vb.name_ = :varBOCname and vb.text_ = :businessObjectClass)";
		}
		sql = sql + " inner join act_ru_variable vi on (vi.execution_id_ = e.id_ and vi.name_ = :varBOIname)";
		sql = sql + " left outer join act_ru_variable vp on (vp.execution_id_ = e.id_ and vp.name_ = :varPLName)";
		sql = sql + " left outer join act_ru_task t on (t.execution_id_ = e.id_)";
		sql = sql + " where (1=1) ";
		if (candidateRoles != null) {
			String sarr = "";
			for (String ss: candidateRoles) {
				sarr = sarr + "'" + ss + "'" + ",";
			}
			if (sarr.length() > 0) {
				sarr = sarr.substring(0, sarr.length()-1);
			}
			sql = sql + " and (exists (select * from act_ru_identitylink l where l.task_id_ = t.id_ and l.type_ = 'candidate' and l.group_id_ in (" + sarr +")) )";
		}
		if (assignee != null) {
			sql = sql + " and (t.assignee_ = :assignee)";
		}		
		if (taskDefKey != null) {
			sql = sql + " and (t.task_def_key_ = :taskDefKey)";
		}
		if (procDefKey != null) {
			sql = sql + " and (d.key_ = :procDefKey)";
		}
		if (pluginName != null) {
			sql = sql + " and (vp.text_ = :pluginName)";
		}
		
		Query qry = emBpm.createNativeQuery(sql);
		qry.setParameter("wfActions", ProcessKeys.VAR_WF_ACTIONS);
		qry.setParameter("varPLName", ProcessKeys.VAR_PLUGIN_NAME);
		qry.setParameter("varBOIname", ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		if (businessObjectClass == null) { 
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		} else {
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			qry.setParameter("businessObjectClass", businessObjectClass);
		}
		if (assignee != null) {
			qry.setParameter("assignee", assignee);
		}
		if (taskDefKey != null) {
			qry.setParameter("taskDefKey", taskDefKey);
		}		
		if (procDefKey != null) {
			qry.setParameter("procDefKey", procDefKey);
		}	
		if (pluginName != null) {
			qry.setParameter("pluginName", pluginName);
		}		
        Number res = (Number) qry.getSingleResult();
        if (res == null)
            return 0;
        else
            return res.intValue();		
	}
	
	@Override
	@AccessTimeout(100000)
	public List<WorkflowObjectState> listTasks(int nfirst, int ncount, SortCriteria[] sorting, String taskDefKey, String assignee, List<String> candidateRoles,  String businessObjectClass, String procDefKey, String pluginName) {
		String sql = "select d.key_, vp.text_, e.act_id_, vb.text_ as businessObjectClass, vi.long_ as boi_long, t.id_, t.assignee_, t.create_time_ ";
		sql = sql + " from act_ru_execution e inner join act_re_procdef d on (d.id_ = e.proc_def_id_)";
		sql = sql + " inner join act_ru_variable v on (v.proc_inst_id_ = e.proc_inst_id_ and v.name_ = :wfActions)";
		if (businessObjectClass == null) { 
			sql = sql + " inner join act_ru_variable vb on (vb.proc_inst_id_ = e.proc_inst_id_ and vb.name_ = :varBOCname)";
		} else {
			sql = sql + " inner join act_ru_variable vb on (vb.proc_inst_id_ = e.proc_inst_id_ and vb.name_ = :varBOCname and vb.text_ = :businessObjectClass)";
		}
		sql = sql + " inner join act_ru_variable vi on (vi.proc_inst_id_ = e.proc_inst_id_ and vi.name_ = :varBOIname)";
		sql = sql + " left outer join act_ru_variable vp on (vp.proc_inst_id_ = e.proc_inst_id_ and vp.name_ = :varPLName)";
		sql = sql + " left outer join act_ru_task t on (t.execution_id_ = e.id_)";
		sql = sql + " where (1=1) ";
		if (candidateRoles != null) {
			String sarr = "";
			for (String ss: candidateRoles) {
				sarr = sarr + "'" + ss + "'" + ",";
			}
			if (sarr.length() > 0) {
				sarr = sarr.substring(0, sarr.length()-1);
			}
			sql = sql + " and (exists (select * from act_ru_identitylink l where l.task_id_ = t.id_ and l.type_ = 'candidate' and l.group_id_ in (" + sarr +")) )";
		}
		if (assignee != null) {
			sql = sql + " and (t.assignee_ = :assignee)";
		}		
		if (taskDefKey != null) {
			sql = sql + " and (t.task_def_key_ = :taskDefKey)";
		}
		if (procDefKey != null) {
			sql = sql + " and (d.key_ = :procDefKey)";
		}
		if (pluginName != null) {
			sql = sql + " and (vp.text_ = :pluginName)";
		}		
		sql = sql + " order by t.create_time_ desc";
		
		Query qry = emBpm.createNativeQuery(sql);
		qry.setParameter("wfActions", ProcessKeys.VAR_WF_ACTIONS);
		qry.setParameter("varPLName", ProcessKeys.VAR_PLUGIN_NAME);
		qry.setParameter("varBOIname", ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		if (businessObjectClass == null) { 
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		} else {
			qry.setParameter("varBOCname", ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			qry.setParameter("businessObjectClass", businessObjectClass);
		}	
		if (assignee != null) {
			qry.setParameter("assignee", assignee);
		}
		if (taskDefKey != null) {
			qry.setParameter("taskDefKey", taskDefKey);
		}
		if (procDefKey != null) {
			qry.setParameter("procDefKey", procDefKey);
		}	
		if (pluginName != null) {
			qry.setParameter("pluginName", pluginName);
		}		
		if (nfirst >= 0)
			qry.setFirstResult(nfirst);
		if (ncount > 0)
			qry.setMaxResults(ncount);
		
		List<Object[]> lstTask = qry.getResultList();
		List<WorkflowObjectState> lstRes = new ArrayList<WorkflowObjectState>(lstTask.size());
		for (Object[] row: lstTask) {
			WorkflowObjectStateDef def = new WorkflowObjectStateDef(StateRef.toString((String) row[0],(String) row[1],(String) row[2]) );
			
			WorkflowObjectState wft = new WorkflowObjectState(def);
			wft.setTaskId((String) row[5]); 
			wft.setAssignee((String) row[6]);
			wft.setCreateTime((Date) row[7] );

			wft.setBusinessObjectClass((String) row[3]); 
			wft.setBusinessObjectId( row[4]);
			lstRes.add(wft);
		}
		return lstRes;
	}
	
	@Override
	public List<Integer> listTasksCR(String taskDefKey) {
		String sql = "select max(v.long_) as creditRequestId from act_ru_task t inner join act_ru_variable v on (v.proc_inst_id_ = t.proc_inst_id_) where (t.task_def_key_ = :taskDefKey) and ((v.name_ = 'businessObjectClass' and v.text_ = 'ru.simplgroupp.transfer.CreditRequest' ) or (v.name_ = 'businessObjectId')) group by t.execution_id_";
		Query qry = emBpm.createNativeQuery(sql);
		qry.setParameter("taskDefKey", taskDefKey);
		List<Number> lst = qry.getResultList();
		ArrayList<Integer> lstRes = new ArrayList<Integer>(lst.size()); 
		for (Number nid: lst) {
			lstRes.add(new Integer(nid.intValue()));
		}
		return lstRes;
	}
	
	@Override
	public void assingTaskTo(String taskId, String assignee) {
		wfTaskServ.setAssignee(taskId, assignee);
	}
	
	@Override
	public void dummy() {
		
	}
	
	@Override
	@AccessTimeout(value = 50000)
	public ProductMessagesEntity getProductMessageForBP(Integer productId,String bpName, String messageKey){
		List<ProductMessagesEntity> vars = bpMessagesCacheProduct.get(bpName);
        if (vars == null) {
            reloadProductMessagesForBP(productId,bpName);
            vars = bpMessagesCacheProduct.get(bpName);
        }
        ProductMessagesEntity msg=productDAO.getProductMessageFromList(vars, messageKey);
        return msg;
	}
  
    /**
     * перезагружаем сообщения для продукта
     * 
     * @param productId - id продукта
     * @param bpName - название процесса
     */
    private void reloadProductMessagesForBP(Integer productId,String bpName) {
        List<ProductMessagesEntity> vars = productDAO.findProductMessagesForProcess(productId, bpName);
        bpMessagesCacheProduct.put(bpName, vars);
    }	
    
    @Override
    public Map<String,Object> evaluateExpressions(Collection<String> exprTexts, Map<String,Object> variables) {
    	
    	ExpressionManager expressionManager = ((ProcessEngineImpl) wfEngine).getProcessEngineConfiguration().getExpressionManager();
    	VariableScope scope = null; // TODO
    	
    	Map<String,Object> res = new HashMap<String, Object>(exprTexts.size());
    	Iterator<String> iter = exprTexts.iterator();
    	while (iter.hasNext()) {
    		String exprText = iter.next();
    		
    		try {
    			Expression myEx = expressionManager.createExpression("${myVar + 1}");
    			Object avalue = myEx.getValue(scope);
    			res.put(exprText, avalue);
    		} catch (Throwable ex) {
    			res.put(exprText, null);
    		}
    	}
    	return res;
    }
    
    @Override
    public Map<String, Object> getExecutionVariables(String execId, Collection<String> varNames) {
    	if (varNames == null || varNames.size() == 0) {
    		return wfRuntimeServ.getVariablesLocal(execId);
    	} else {
    		return wfRuntimeServ.getVariablesLocal(execId, varNames);
    	}
    }
}
