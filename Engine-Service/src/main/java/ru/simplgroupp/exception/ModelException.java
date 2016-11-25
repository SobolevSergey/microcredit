package ru.simplgroupp.exception;

public class ModelException extends CodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5894935631322727420L;

	public ModelException(int acode) {
		super(acode);
	}

	public ModelException(int acode, String message) {
		super(acode, message);
	}

	public ModelException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public ModelException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public ModelException() {
		super();
	}

	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelException(String message) {
		super(message);
	}

	public ModelException(Throwable cause) {
		super(cause);
	}

}
