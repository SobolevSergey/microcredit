package ru.simplgroupp.exception;

abstract public class AbstractException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8043278749406333566L;
	protected ExceptionInfo info = new ExceptionInfo();
	
	public AbstractException(String message, Throwable cause) {
		super(message, cause);
		if (cause != null) {
			if (cause instanceof AbstractException) {
				AbstractException ae = (AbstractException) cause;
				info.setCode(ae.getInfo().getCode());
				info.setMessage(message);
				info.setType(ae.getInfo().getType());
				info.setResultType(ae.getInfo().getResultType());
			} else {
				info.setCode(0);
				info.setMessage(message);
				info.setType(Type.TECH);
				info.setResultType(ResultType.FATAL);
			}
		}
	}

	public AbstractException(int acode, String message, Type atype, ResultType aresType, Throwable cause) {
		super(message, cause);
		info.setCode(acode);
		info.setMessage(message);
		info.setType(atype);
		info.setResultType(aresType);
	}
    public AbstractException(int acode, String message, Type atype, ResultType aresType, Throwable cause,int state) {
        super(message, cause);
        info.setCode(acode);
        info.setMessage(message);
        info.setType(atype);
        info.setResultType(aresType);
        info.setState(state);
    }


    public ExceptionInfo getInfo() {
		return info;
	}	

}
