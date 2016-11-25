package ru.simplgroupp.exception;


public class CodeRuntimeException extends RuntimeException 
{
	
	public static final int CODE_COMMON = 1;
	
	protected int code = CODE_COMMON;

	public CodeRuntimeException(int acode) {
		super();
		code = acode;
	}

	public CodeRuntimeException(int acode, String message) {
		super(message);
		code = acode;
	}

	public CodeRuntimeException(int acode, Throwable cause) {
		super(cause);
		code = acode;
	}

	public CodeRuntimeException(int acode, String message, Throwable cause) 
	{
		super(message, cause);
		code = acode;
	}
	
	public int getCode() {
		return code;
	}

	public CodeRuntimeException() {
		super();
	}

	public CodeRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodeRuntimeException(String message) {
		super(message);
	}

	public CodeRuntimeException(Throwable cause) {
		super(cause);
	}	
}

