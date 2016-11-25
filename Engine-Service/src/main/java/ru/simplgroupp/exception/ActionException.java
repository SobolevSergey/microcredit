package ru.simplgroupp.exception;

public class ActionException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 705693985628520998L;

	public ActionException(int acode, String message, Type atype,
			ResultType aresType, Throwable cause) {
		super(acode, message, atype, aresType, cause);
	}

	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

    public ActionException(int acode, String message, Type atype,
                           ResultType aresType, Throwable cause,int state) {
        super(acode, message, atype, aresType, cause,state);
    }

    public ActionException(String message, Type atype, ResultType aresType) {
    	super(0, message, atype, aresType, null);
    }
}