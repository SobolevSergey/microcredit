package ru.simplgroupp.exception;

public class DeployerException extends Exception {

	public DeployerException() {
		super();
	}

	public DeployerException(String message) {
		super(message);
	}

	public DeployerException(Throwable cause) {
		super(cause);
	}

	public DeployerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeployerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
