package ru.simplgroupp.exception;

public class WorkflowException extends CodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2981129130477135948L;

	public WorkflowException(int acode) {
		super(acode);
	}

	public WorkflowException(int acode, String message) {
		super(acode, message);
	}

	public WorkflowException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public WorkflowException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public WorkflowException() {
		super();
	}

	public WorkflowException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkflowException(String message) {
		super(message);
	}

	public WorkflowException(Throwable cause) {
		super(cause);
	}

}
