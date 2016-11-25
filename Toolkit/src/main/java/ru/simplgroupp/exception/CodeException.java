package ru.simplgroupp.exception;

public class CodeException extends Exception 
{
	
	public static final int CODE_COMMON = 1;
	
	protected int code = CODE_COMMON;

	public CodeException(int acode) {
		super();
		code = acode;
	}

	public CodeException(int acode, String message) {
		super(message);
		code = acode;
	}

	public CodeException(int acode, Throwable cause) {
		super(cause);
		code = acode;
	}

	public CodeException(int acode, String message, Throwable cause) 
	{
		super(message, cause);
		code = acode;
	}
	
	public int getCode() {
		return code;
	}

	public CodeException() {
		super();
	}

	public CodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodeException(String message) {
		super(message);
	}

	public CodeException(Throwable cause) {
		super(cause);
	}
	
}
