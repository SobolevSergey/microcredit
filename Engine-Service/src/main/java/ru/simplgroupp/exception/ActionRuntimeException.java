package ru.simplgroupp.exception;

public class ActionRuntimeException extends AbstractRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6568922104594918755L;

	public ActionRuntimeException(int acode, String message, Type atype,
			ResultType aresType, Throwable cause) {
		super(acode, message, atype, aresType, cause);
	}

	public ActionRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}