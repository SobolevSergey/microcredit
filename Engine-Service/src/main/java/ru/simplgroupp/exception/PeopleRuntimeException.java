package ru.simplgroupp.exception;

public class PeopleRuntimeException extends CodeRuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5387334349423915317L;

	public PeopleRuntimeException(int acode) {
        super(acode);
    }

    public PeopleRuntimeException(int acode, String message) {
        super(acode, message);
    }

    public PeopleRuntimeException(int acode, Throwable cause) {
        super(acode, cause);
    }

    public PeopleRuntimeException(int acode, String message, Throwable cause) {
        super(acode, message, cause);
    }

    public PeopleRuntimeException() {
        super();
    }

    public PeopleRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeopleRuntimeException(String message) {
        super(message);
    }

    public PeopleRuntimeException(Throwable cause) {
        super(cause);
    }

}
