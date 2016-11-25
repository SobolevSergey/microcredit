package ru.simplgroupp.exception;

public class ReferenceException extends CodeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5264055030790592946L;

	public ReferenceException(int acode) {
        super(acode);
    }

    public ReferenceException(int acode, String message) {
        super(acode, message);
    }

    public ReferenceException(int acode, Throwable cause) {
        super(acode, cause);
    }

    public ReferenceException(int acode, String message, Throwable cause) {
        super(acode, message, cause);
    }

    public ReferenceException() {
        super();
    }

    public ReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceException(String message) {
        super(message);
    }

    public ReferenceException(Throwable cause) {
        super(cause);
    }


}
