package ru.simplgroupp.exception;

public class WorkflowRuntimeException extends CodeRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7864786933999599354L;

	public WorkflowRuntimeException(int acode) {
		super(acode);
	}

	public WorkflowRuntimeException(int acode, String message) {
		super(acode, message);
	}

	public WorkflowRuntimeException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public WorkflowRuntimeException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public WorkflowRuntimeException() {
		super();
	}

	public WorkflowRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkflowRuntimeException(String message) {
		super(message);
	}

	public WorkflowRuntimeException(Throwable cause) {
		super(cause);
	}

}
