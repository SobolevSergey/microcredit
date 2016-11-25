package ru.simplgroupp.fias.exception;

import ru.simplgroupp.exception.CodeException;

/**
 * Ошибка обновления БД ФИАС. Обязательно классифицируется кодом.
 * @author CVB
 */
public class FiasException extends CodeException {
	
	private static final long serialVersionUID = -2520511639123891511L;
  
  /**
   * Общая (неклассифицированная) ошибка. Фактически не используется в проекте.
   */
	public static final int COMMON_EXCEPTION = 1;
  /**
   * Требуется полное обновление БД ФИАС.
   */
	public static final int FULL_UPDATE_REQUIRED = 2;
  /**
   * Процесс обновления уже запущен.
   */
	public static final int UPDATE_IN_PROGRESS = 3;
  /**
   * Не удалось получить данные от сервиса ФИАС.
   */
	public static final int INCORRECT_RESPONSE = 4;
  /**
   * Неверный формат ответа. Исключение поднимается на этапе разбора ответа. Означает несоответствие формата ответа документации.
   */
	public static final int INCORRECT_FORMAT = 5;
  /**
   * Ошибка при скачивании файла.
   */
	public static final int DOWNLOAD_EXCEPTION = 6;
  /**
   * Ошибка при работе с локальной файловой системой. В том числе - при распаковке архива.
   */
	public static final int FILE_SYSTEM_EXCEPTION = 7;
  /**
   * Не найдено значение обязательного поля.
   */
	public static final int VALUE_REQUIRED = 8;
  /**
   * Методу сервиса обновлений (FiasUpdater) передан некорректный параметр.
   */
	public static final int INCORRECT_PARAMETERS = 9;
  /**
   * Ошибка при разборе документа XML.
   */
	public static final int XML_EXCEPTION = 10;
  /**
   * Ошибка при выполнении запроса HTTP.
   */
	public static final int HTTP_EXCEPTION = 11;
  /**
   * Ошибка при работе с базой данных.
   */
	public static final int DB_EXCEPTION = 12;

	public FiasException(int acode, String message, Throwable cause) {
		super(acode, message, cause);
	}

	public FiasException(int acode, String message) {
		super(acode, message);
	}

	public FiasException(int acode, Throwable cause) {
		super(acode, cause);
	}

	public FiasException(int acode) {
		super(acode);
	}

}
