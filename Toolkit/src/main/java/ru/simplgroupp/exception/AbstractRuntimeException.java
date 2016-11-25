package ru.simplgroupp.exception;

abstract public class AbstractRuntimeException extends RuntimeException {

	protected ExceptionInfo info = new ExceptionInfo();
	
	public AbstractRuntimeException(String message, Throwable cause) {
		super(message, cause);
		info.setCode(0);
		info.setMessage(message);
		info.setType(Type.TECH);
		info.setResultType(ResultType.FATAL);
	}

	public AbstractRuntimeException(int acode, String message, Type atype, ResultType aresType, Throwable cause) {
		super(message, cause);
		info.setCode(acode);
		info.setMessage(message);
		info.setType(atype);
		info.setResultType(aresType);
	}	

	public ExceptionInfo getInfo() {
		return info;
	}	

}
