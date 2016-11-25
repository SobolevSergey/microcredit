package ru.simplgroupp.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.util.WorkflowUtil;

/**
 * Определение состояния объекта, участвующего в процессе
 * @author irina
 *
 */
public class WorkflowObjectStateDef implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3098976129378638974L;
	private String name;
	private String description;
	private Integer itemCount;
	private Map<String, Object> variablesLocal;
	private List<WorkflowObjectActionDef> actions = new ArrayList<WorkflowObjectActionDef>(0);
	private String businessObjectClass;
	// TODO набор условий
	
	public WorkflowObjectStateDef(String name) {
		super();
		this.name = name;
	}
	
	public WorkflowObjectStateDef(StateRef ref) {
		super();
		this.name = ref.toString();
	}

	public String getName() {
		return name;
	}
	
	public String getNameEscaped() {
		return name.replace(':', '-');
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProcessDefinitionKey() {
		StateRef data = StateRef.valueOf(name);
		return data.getProcessDefKey();
	}
	
	public String getStateName() {
		StateRef data = StateRef.valueOf(name);
		return data.getName();
	}
	
	public StateRef getStateRef() {
		return StateRef.valueOf(name);
	}
	
	public String getPluginName() {
		StateRef data = StateRef.valueOf(name);
		return data.getPluginName();
	}	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public List<WorkflowObjectActionDef> getActions() {
		return actions;
	}
	
	public WorkflowObjectActionDef addActionDef(String definition) {
		WorkflowObjectActionDef act = new WorkflowObjectActionDef();
		act.loadFromString(definition);
		actions.add(act);
		return act;
	}	
	
	public WorkflowObjectActionDef addActionDef(String signalRef, String name, String assignee, String candidateGroups, String candidateUsers) {
		WorkflowObjectActionDef act = new WorkflowObjectActionDef();
		act.setSignalRef(signalRef);
		act.setName(name);
		act.setAssignee(assignee);
		act.setCandidateGroups(candidateGroups);
		act.setCandidateUsers(candidateUsers);
		actions.add(act);
		return act;
	}	
	
	public WorkflowObjectActionDef addActionDef(String signalRef, String name) {
		WorkflowObjectActionDef act = new WorkflowObjectActionDef();
		act.setSignalRef(signalRef);
		act.setName(name);
		actions.add(act);
		return act;
	}
	
	/**
	 * Возвращает все возможные роли
	 * @return
	 */	
	public Set<String> getCandidateGroups() {
		HashSet<String> res = new HashSet<String>(5);
		for (WorkflowObjectActionDef act: actions) {
			if (! StringUtils.isBlank(act.getCandidateGroups())) {
				String[] grps = act.getCandidateGroups().split(",");
				for (String ss: grps) {
					res.add(ss);
				}
			}
		}
		return res;
	}
	
	public WorkflowObjectStateDef copy() {
		try {
			return (WorkflowObjectStateDef) clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		WorkflowObjectStateDef other = new WorkflowObjectStateDef(this.name);
		other.description = this.description;
		for (WorkflowObjectActionDef act: actions) {
			other.actions.add((WorkflowObjectActionDef) act.clone());
		}
		return super.clone();
	}
	
	public WorkflowObjectActionDef findBySignalRef(String signalRef) {
		return WorkflowUtil.findWfActionA(actions, signalRef);
	}

	public Map<String, Object> getVariablesLocal() {
		return variablesLocal;
	}

	public void setVariablesLocal(Map<String, Object> variablesLocal) {
		this.variablesLocal = variablesLocal;
	}

	public String getBusinessObjectClass() {
		return businessObjectClass;
	}

	public void setBusinessObjectClass(String businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}

}
