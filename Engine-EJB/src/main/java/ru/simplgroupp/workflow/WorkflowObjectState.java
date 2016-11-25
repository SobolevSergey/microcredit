package ru.simplgroupp.workflow;

import java.io.Serializable;
import java.util.Date;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Identifiable;

public class WorkflowObjectState implements Serializable, Identifiable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5599581547105165865L;
	/**
	 * задача
	 */
	private Task task;
	/**
	 * id задачи
	 */
	private String taskId;
	/**
	 * время создания
	 */
	private Date createTime;
	/**
	 * кому назначена
	 */
	private String assignee;
	/**
	 * определение
	 */
	private WorkflowObjectStateDef definition;
	/**
	 * класс бизнес-объекта
	 */
	private String businessObjectClass;
	/**
	 * id бизнес-объекта
	 */
	private Object businessObjectId;
	/**
	 * бизнес-объект
	 */
	private Object businessObject;
	
	public WorkflowObjectState(WorkflowObjectStateDef adef) {
		super();
		definition = adef;
	}
	public WorkflowObjectState(Task atask) {
		super();
		task = atask;
	}	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task){
		this.task=task;
	}
	
	public WorkflowObjectStateDef getDefinition() {
		return definition;
	}
	public String getBusinessObjectClass() {
		return businessObjectClass;
	}
	public Object getBusinessObjectId() {
		return businessObjectId;
	}
	public void setBusinessObjectClass(String businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}
	public void setBusinessObjectId(Object businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
	public Object getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(Object businessObject) {
		this.businessObject = businessObject;
	}
	@Override
	public Object getId() {
		String sid = "";
		if (task != null) {
			sid =  "task:" + task.getId();
		} else if (definition != null) {
			sid = "state:" + definition.getName();
		} else {
			return null;
		}
		sid = sid + ":" + businessObjectClass;
		if (businessObjectId != null) {
			sid = sid + ":" + businessObjectId.toString();
		}
		return sid;
	}
	public String getTaskId() {
		if (task == null) {
			return taskId;
		} else {
			return task.getId();
		}
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getPK() {
		String spk = "";
		if (definition != null) {
			spk = spk + definition.getName();
		}
		spk = spk + ":" + StringUtils.defaultString(taskId, "");
		spk = spk + ":" + businessObjectClass;
		spk = spk + ":" + businessObjectId.toString();
		return spk;
	}
	public Date getCreateTime() {
		if (task == null) {
			return createTime;
		} else {
			return task.getCreateTime();
		}
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAssignee() {
		if (task == null) {
			return assignee;
		} else {
			return task.getAssignee();
		}
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
	public Integer getAssigneeUserId() {
		if (StringUtils.isBlank(getAssignee())) {
			return 0;
		} else {
			return Convertor.toInteger(getAssignee().trim());
		}
	}
}
