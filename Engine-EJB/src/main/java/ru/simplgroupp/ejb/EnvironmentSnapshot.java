package ru.simplgroupp.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.WorkflowUtil;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectState;

public class EnvironmentSnapshot implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7330646516015444121L;
	/**
	 * выполняющиеся действия
	 */
	protected List<ApplicationAction> runningActions = new ArrayList<ApplicationAction>(0);
	/**
	 * состояния
	 */
	protected List<WorkflowObjectState> states = new ArrayList<WorkflowObjectState>(0);
	/**
	 * действия объекта
	 */
	protected List<WorkflowObjectActionDef> objectActions = new ArrayList<WorkflowObjectActionDef>(0);
	
	public List<ApplicationAction> getRunningActions() {
		return runningActions;
	}
	public List<WorkflowObjectState> getStates() {
		return states;
	}
	public List<WorkflowObjectActionDef> getObjectActions() {
		return objectActions;
	}
	
	/**
	 * возвращает действие по соответствующему сигналу
	 * @param signalRef - сигнал
	 * @return
	 */
	public WorkflowObjectActionDef getAction(SignalRef signalRef) {
		return getAction(signalRef.toString());
	}
	
	/**
	 * Возвращает список состояний, содержащих данные переменные
	 * @param vars
	 * @return
	 */
	public List<WorkflowObjectState> getStates(Map<String,Object> vars) {
		return WorkflowUtil.findWFStatesByVars(states, vars, true);	
	}
	
	public WorkflowObjectState getState(String varName, Object varValue) {
		List<WorkflowObjectState> lst = WorkflowUtil.findWFStatesByVars(states, Utils.mapOfSO(varName, varValue), true);
		return (WorkflowObjectState) Utils.firstOrNull(lst);
	
	}
	
	public List<WorkflowObjectActionDef> getActions(Map<String,Object> vars) {
		List<WorkflowObjectActionDef> lst = WorkflowUtil.findWFActionsByVars(objectActions,vars, false);
		return lst;
	}	
	
	public List<WorkflowObjectActionDef> getActions(String varName, Object varValue) {
		List<WorkflowObjectActionDef> lst = WorkflowUtil.findWFActionsByVars(objectActions, Utils.mapOfSO(varName, varValue), false);
		return lst;
	}	
	
	public WorkflowObjectActionDef getAction(String varName, Object varValue) {
		List<WorkflowObjectActionDef> lst = WorkflowUtil.findWFActionsByVars(objectActions, Utils.mapOfSO(varName, varValue), false);
		return (WorkflowObjectActionDef) Utils.firstOrNull(lst);
	}
	
	/**
	 * возвращает действие по названию сигнала
	 * @param signalRef - название сигнала
	 * @return
	 */
	public WorkflowObjectActionDef getAction(String signalRef) {
		WorkflowObjectActionDef def = WorkflowUtil.findWFAction1(states, signalRef);
		if (def == null) {
			def = WorkflowUtil.findWfActionA(objectActions, signalRef);
		}
		return def;
	}
	
	/**
	 * возвращает состояние объекта по состоянию процесса
	 * @param stateRef - состояние процесса
	 * @return
	 */
	public WorkflowObjectState getState(StateRef stateRef) {
		return WorkflowUtil.findWFState1(states, stateRef);
	}
}
