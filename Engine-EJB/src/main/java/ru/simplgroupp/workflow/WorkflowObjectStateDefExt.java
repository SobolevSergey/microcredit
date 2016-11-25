package ru.simplgroupp.workflow;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.simplgroupp.toolkit.common.PermissionType;

public class WorkflowObjectStateDefExt {
	
	protected WorkflowObjectStateDef stateDef;
	protected Set<StateRef> jumps = new HashSet<>(3);
	protected Map<String, EnumSet<PermissionType>> fieldPerms = new HashMap<>(20);

	public WorkflowObjectStateDefExt(WorkflowObjectStateDef adef) {
		stateDef = adef;
	}

	public WorkflowObjectStateDef getStateDef() {
		return stateDef;
	}

	public Set<StateRef> getJumps() {
		return jumps;
	}
	
	public Map<String, EnumSet<PermissionType>> getFieldPermissions() {
		return fieldPerms;
	}
	
	public EnumSet<PermissionType> getFieldPermission(String fieldRef) {
		EnumSet<PermissionType> res = fieldPerms.get(fieldRef);
		if (res == null) {
			return EnumSet.noneOf(PermissionType.class);
		} else {
			return res;
		}
	}
}
