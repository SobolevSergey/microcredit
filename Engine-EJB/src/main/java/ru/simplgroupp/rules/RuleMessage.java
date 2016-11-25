package ru.simplgroupp.rules;

public class RuleMessage {
	
	public enum Level {
		INFO,
		WARNING,
		ERROR;
	}

	private int code;
	private String messageText;
	private Level severity;
	
	public RuleMessage(Level alevel, int acode, String amessageText) {		
		super();
		severity = alevel;
		code = acode;
		messageText = amessageText;
	}
	
	public static RuleMessage info(int acode, String amessageText) {
		return new RuleMessage(Level.INFO, acode, amessageText);
	}
	
	public static RuleMessage warning(int acode, String amessageText) {
		return new RuleMessage(Level.WARNING, acode, amessageText);
	}
	
	public static RuleMessage error(int acode, String amessageText) {
		return new RuleMessage(Level.ERROR, acode, amessageText);
	}	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Level getSeverity() {
		return severity;
	}

	public void setSeverity(Level severity) {
		this.severity = severity;
	}

}
