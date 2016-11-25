package ru.simplgroupp.exception;

public class KassaRuntimeException extends CodeRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6956700870364082194L;

	public KassaRuntimeException() {
		super();
	}

	public KassaRuntimeException(int acode) {
		super(acode);
	}

	public KassaRuntimeException(String message) {
		super(message);
	}

	public KassaRuntimeException(Throwable cause) {
		super(cause);
	}

	public KassaRuntimeException(int acode, String message) {
		super(acode, message);
	}

	public KassaRuntimeException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public KassaRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public KassaRuntimeException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

}
