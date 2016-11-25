package ru.simplgroupp.webapp.exception;

public class WebAppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4175458326791203884L;

	public WebAppException() {
		super();
	}

	public WebAppException(String message) {
		super(message);
	}

	public WebAppException(Throwable cause) {
		super(cause);
	}

	public WebAppException(String message, Throwable cause) {
		super(message, cause);
	}

	/*public WebAppException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}*/

}
