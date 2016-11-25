package ru.simplgroupp.exception;

import java.io.Serializable;

import ru.simplgroupp.toolkit.common.Utils;

public class ExceptionInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5963051423681503011L;
	private int code;
	private Type type;
	private ResultType resultType;
	private String message;
    private int state;

	public ExceptionInfo() {
		super();
	}

	public ExceptionInfo(int acode, String amessage, Type atype, ResultType aresType) {
		super();
		message = amessage;
		code = acode;
		type = atype;
		resultType = aresType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
	public String toString() {
		return "ExceptionInfo{" +
				"code=" + code +
				", type=" + type +
				", resultType=" + resultType +
				", message='" + message + '\'' +
                ", state='" + state + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		ExceptionInfo other = (ExceptionInfo) obj;
		return (this.code == other.code) 
				&& (this.state == other.state) 
				&& Utils.equalsNull(this.message, other.message) 
				&& Utils.equalsNull(this.type, other.type) 
				&& Utils.equalsNull(this.resultType, other.resultType);  
	}
}
