package ru.simplgroupp.exception;

public class ReportException extends CodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1935804031279959727L;

	public ReportException(int acode) {
		super(acode);
	}

	public ReportException(int acode, String message) {
		super(acode, message);
	}

	public ReportException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public ReportException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public ReportException() {
		super();
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

}
