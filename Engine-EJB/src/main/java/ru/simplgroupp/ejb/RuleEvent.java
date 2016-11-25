package ru.simplgroupp.ejb;

import java.io.Serializable;

public class RuleEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2460235146710342545L;

	public enum ActionType {
		CREATED,
		UPDATED,
		DELETED,
		UPDATED_CONSTANT;
	}

	private String ruleName;
	private ActionType actionType;
	
	public RuleEvent(String aname, ActionType act) {
		ruleName = aname;
		actionType = act;
	}

	public String getRuleName() {
		return ruleName;
	}

	public ActionType getActionType() {
		return actionType;
	}

}
