package ru.simplgroupp.exception;

public class KassaException extends CodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6295124900237405643L;

	public KassaException() {
		super();
	}

	public KassaException(int acode) {
		super(acode);
	}

	public KassaException(String message) {
		super(message);
	}

	public KassaException(Throwable cause) {
		super(cause);
	}

	public KassaException(int acode, String message) {
		super(acode, message);
	}

	public KassaException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public KassaException(String message, Throwable cause) {
		super(message, cause);
	}

	public KassaException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

}
