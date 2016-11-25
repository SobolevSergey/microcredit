package ru.simplgroupp.rules;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractRuleResult {
	
	public List<RuleMessage> messages;

	public AbstractRuleResult() {
		super();
	}
	
	public List<RuleMessage> getMessages() {
		return messages;
	}
	
	public List<RuleMessage> getInfoMessages() {
		if (messages == null) {
			return new ArrayList<RuleMessage>(0);
		}
		
		List<RuleMessage> lst1 = new ArrayList<RuleMessage>(messages.size());
		for (RuleMessage rmsg: messages) {
			if (rmsg.getSeverity().equals(RuleMessage.Level.INFO)) {
				lst1.add(rmsg);
			}
		}
		return lst1;
	}

	public RuleMessage addInfo(int acode, String atext) {
		if (messages == null) {
			messages = new ArrayList<RuleMessage>(3);
		}
		RuleMessage msg = RuleMessage.info(acode, atext);
		messages.add(msg);
		return msg;
	}
	
	public RuleMessage addWarning(int acode, String atext) {
		if (messages == null) {
			messages = new ArrayList<RuleMessage>(3);
		}
		RuleMessage msg = RuleMessage.warning(acode, atext);
		messages.add(msg);
		return msg;
	}	
	
	public RuleMessage addError(int acode, String atext) {
		if (messages == null) {
			messages = new ArrayList<RuleMessage>(3);
		}
		RuleMessage msg = RuleMessage.error(acode, atext);
		messages.add(msg);
		return msg;
	}
	
	public RuleMessage getLastError() {
		if (messages == null) {
			return null;
		}
		for (RuleMessage msg: messages) {
			if (msg.getSeverity().equals(RuleMessage.Level.ERROR)) {
				return msg;
			}
		}
		return null;
	}
	
	public boolean hasErrors() {
		RuleMessage msg = getLastError();
		return (msg != null);
	}
}
