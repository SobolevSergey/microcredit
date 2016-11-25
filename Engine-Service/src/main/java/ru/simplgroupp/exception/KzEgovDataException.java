package ru.simplgroupp.exception;

/**
 * Ошибка интеграции с Порталом Открытые данные Электронного правительства Республики Казахстан
 * @author Rustem Saidaliyev
 */
public class KzEgovDataException extends CodeException {

	/**
	 * TODO сгенерировать новый
	 */
	private static final long serialVersionUID = -2520L;

	public KzEgovDataException(Throwable cause) {
		super(cause);
	}

	public KzEgovDataException(String message) {
		super(message);
	}
}
