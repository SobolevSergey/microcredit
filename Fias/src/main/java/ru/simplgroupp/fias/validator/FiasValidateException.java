package ru.simplgroupp.fias.validator;

import ru.simplgroupp.fias.exception.FiasException;

public class FiasValidateException extends FiasException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5596447321272700926L;

	public static final int START_CODE = 100;
	public static final int CODE_INVALID_HOUSE = START_CODE + 1; // номер дома может содержать только цифры
	public static final int CODE_LITER_TOO_LONG = START_CODE + 2; // литера может состоять только из одной буквы
	public static final int CODE_LITER_NON_RUSSIAN = START_CODE + 3; // литера может состоять только из русской буквы
	
	public FiasValidateException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public FiasValidateException(int acode, String message) {
		super(acode, message);
	}

	public FiasValidateException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public FiasValidateException(int acode) {
		super(acode);
	}

}
