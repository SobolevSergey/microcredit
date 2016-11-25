package ru.simplgroupp.workflow;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

public class WorkflowTaskImpl implements Task {
	
	private String Id;
	private String Name;
	private String Description;
	private String Assignee;
	private String ProcessInstanceId;
	private String ExecutionId;
	private String ProcessDefinitionId;
	private Date CreateTime;
	private String TaskDefinitionKey;
	private Date DueDate;
	
	public WorkflowTaskImpl(String Id, String Name, String Description, String Assignee, String ProcessInstanceId, String ExecutionId, String ProcessDefinitionId, Date CreateTime, String TaskDefinitionKey, Date DueDate) {
		super();
		this.Id = Id;
		this.Name = Name;
		this.Description = Description;
		this.Assignee = Assignee;
		this.ProcessInstanceId = ProcessInstanceId;
		this.ExecutionId = ExecutionId;
		this.ProcessDefinitionId = ProcessDefinitionId;
		this.CreateTime = CreateTime;
		this.TaskDefinitionKey = TaskDefinitionKey;
		this.DueDate = DueDate;
	}

	@Override
	public String getId() {
		return Id;
	}

	@Override
	public String getName() {
		return Name;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return Description;
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPriority(int priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOwner(String owner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAssignee() {
		return Assignee;
	}

	@Override
	public void setAssignee(String assignee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DelegationState getDelegationState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDelegationState(DelegationState delegationState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProcessInstanceId() {
		return ProcessInstanceId;
	}

	@Override
	public String getExecutionId() {
		return ExecutionId;
	}

	@Override
	public String getProcessDefinitionId() {
		return ProcessDefinitionId;
	}

	@Override
	public Date getCreateTime() {
		return CreateTime;
	}

	@Override
	public String getTaskDefinitionKey() {
		return TaskDefinitionKey;
	}

	@Override
	public Date getDueDate() {
		return DueDate;
	}

	@Override
	public void setDueDate(Date dueDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCategory(String category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delegate(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentTaskId(String parentTaskId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getParentTaskId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTenantId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getTaskLocalVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getProcessVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFormKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFormKey(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTenantId(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
