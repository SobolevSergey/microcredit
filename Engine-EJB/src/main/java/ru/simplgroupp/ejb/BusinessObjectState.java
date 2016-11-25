package ru.simplgroupp.ejb;

import java.util.EnumSet;
import java.util.HashMap;

import ru.simplgroupp.toolkit.common.PermissionType;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

public class BusinessObjectState {
	protected String Id;
	protected String Name;
	protected String returnProcessPoint;
	protected HashMap<String, EnumSet<PermissionType>> fieldAccess = new HashMap<>(20);
	protected HashMap<String, WorkflowObjectActionDef> jump = new HashMap<>(5);
	
	public EnumSet<PermissionType> getFieldAccess(String fieldName) {
		EnumSet<PermissionType> sacc = fieldAccess.get(fieldName);
		if (sacc == null) {
			return EnumSet.noneOf(PermissionType.class);
		} else {
			return sacc;
		}
	}
	
	public String[] getJumpStateIds() {
		// TODO 
		return null;
	}

	public String getId() {
		return Id;
	}

	public String getName() {
		return Name;
	}

	public String getReturnProcessPoint() {
		return returnProcessPoint;
	}
}
