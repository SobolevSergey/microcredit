package ru.simplgroupp.exception;

public class PeopleException extends CodeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5894418980384300161L;

	public PeopleException(int acode) {
        super(acode);
    }

    public PeopleException(int acode, String message) {
        super(acode, message);
    }

    public PeopleException(int acode, Throwable cause) {
        super(acode, cause);
    }

    public PeopleException(int acode, String message, Throwable cause) {
        super(acode, message, cause);
    }

    public PeopleException() {
        super();
    }

    public PeopleException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeopleException(String message) {
        super(message);
    }

    public PeopleException(Throwable cause) {
        super(cause);
    }

}
