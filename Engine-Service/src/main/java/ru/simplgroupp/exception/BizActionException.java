package ru.simplgroupp.exception;

public class BizActionException extends CodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7195371263217223527L;

	public BizActionException(int acode) {
		super(acode);
	}

	public BizActionException(int acode, String message) {
		super(acode, message);

	}

	public BizActionException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public BizActionException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public BizActionException() {
		super();
	}

	public BizActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizActionException(String message) {
		super(message);
	}

	public BizActionException(Throwable cause) {
		super(cause);
	}

}
