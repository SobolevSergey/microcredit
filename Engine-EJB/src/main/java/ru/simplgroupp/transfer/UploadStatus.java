package ru.simplgroupp.transfer;

public class UploadStatus {

	 //статусы для сохранения в БД в основную таблицу
	 public static final int UPLOAD_CREATE=0;
	 public static final int UPLOAD_SUCCESS = 1;
	 public static final int UPLOAD_ERROR = 2;
	 public static final int RESULT_SUCCESS = 3;
	 public static final int RESULT_WITH_ERROR = 4;
	 
	 //типы выгрузки
	 public static final int UPLOAD_CREDIT=1;
	 public static final int UPLOAD_CREDITREQUEST=2;
	 public static final int UPLOAD_CREDIT_CREDITREQUEST=3;
	 public static final int UPLOAD_CREDIT_CORRECTION=4;
	 public static final int UPLOAD_CREDIT_DELETE=5;
	 public static final int UPLOAD_CREDIT_CORRECT_ERRORS=6;
	 
	 //статусы для сохранения в БД в таблицу с деталями
	 public static final int UPLOADDETAIL_ADDED = 101;
	 public static final int UPLOADDETAIL_UPLOADED = 102;
	 public static final int UPLOADDETAIL_RESULT_SUCCESS = 103;
	 public static final int UPLOADDETAIL_RESULT_ERROR = 104;
	 //Статусы Русского стандарта 
	 public static final int RSTANDART_STATUS_INWORK=0;
	 public static final int RSTANDART_STATUS_POSTPONED=10;
	 public static final int RSTANDART_STATUS_INQUEUE=11;
	 public static final int RSTANDART_STATUS_STOPPED=20;
	 public static final int RSTANDART_STATUS_ERROR=21;
	 public static final int RSTANDART_STATUS_DONE=100;
	 
}
