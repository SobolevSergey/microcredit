package ru.simplgroupp.exception;

public class ReportRuntimeException extends CodeRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2959139972392311002L;

	public ReportRuntimeException(int acode) {
		super(acode);
	}

	public ReportRuntimeException(int acode, String message) {
		super(acode, message);
	}

	public ReportRuntimeException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public ReportRuntimeException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public ReportRuntimeException() {
		super();
	}

	public ReportRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportRuntimeException(String message) {
		super(message);
	}

	public ReportRuntimeException(Throwable cause) {
		super(cause);
	}

}
