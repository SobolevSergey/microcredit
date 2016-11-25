package ru.simplgroupp.util;

public class SettingsKeys {

	//урл для сервисов
	public static final String CONNECT_DB_URL= "connect.db.url";
		
	//настройки для БД телефонов
	public static final String PHONE_DB_URL= "phone.db.url";
	public static final String PHONE_DB_URL_LOGIN="phone.db.url.login";
	public static final String PHONE_DB_URL_PASSWORD="phone.db.url.password";
	
	//настройки для БД паспортов
	public static final String FMS_DB_URL= "fms.db.url";
	public static final String FMS_DB_URL_LOGIN="fms.db.url.login";
	public static final String FMS_DB_URL_PASSWORD="fms.db.url.password";
	
	//настройки для БД террористов
	public static final String TERRORIST_DB_URL= "terrorist.db.url";
	public static final String TERRORIST_DB_URL_LOGIN="terrorist.db.url.login";
	public static final String TERRORIST_DB_URL_PASSWORD="terrorist.db.url.password";
		
	//настройки для отправки почты
	public static final String EMAIL_LOGIN="email.login";
	public static final String EMAIL_PASSWORD="email.password";
	public static final String EMAIL_PREFIX="email.prefix";
	public static final String EMAIL_SMTP_PORT = "email.smtp.port";
	public static final String EMAIL_SMTP_PORT_NO_TLS = "email.smtp.port.no.tls";
	public static final String EMAIL_FROM = "email.from";
	public static final String EMAIL_OUR_SERVER = "email.our.server";

	//настройки для смс
	public static final String SMS_URL= "sms.url";
	public static final String SMS_LOGIN="sms.login";
	public static final String SMS_PASSWORD="sms.password";
	public static final String SMS_FROM= "sms.from";
	//настройки для смс, доп. сервис
	public static final String SMS_URL_ADDITIONAL= "sms.url.additional";
	public static final String SMS_LOGIN_ADDITIONAL="sms.login.additional";
	public static final String SMS_PASSWORD_ADDITIONAL="sms.password.additional";
	
	//настройки для соц.сетей
	public static final String SOCNETWORK_URL= "socnetwork.url";
	
	//настройки для гео сайта
	public static final String GEO_URL= "geo.url";
	
	//настройки для логина
	public static final String LOGIN_EMAIL="email";
	public static final String LOGIN_PHONE="phone";
	public static final String LOGIN_WAY="login.way";
	
	//настройки для генерирования кода
	public static final String GENERATE_REAL_DIGIT_CODE="generate.real.digit.code";//генерируем цифровой смс-код для продуктива (1), для теста (0)
	public static final String GENERATE_REAL_CHAR_CODE="generate.real.char.code";//генерируем символьно-цифровой код для продуктива (1), для теста (0)
	public static final String GENERATE_PASSWORD_ONLY_DIGITS="generate.password.only.digits";//генерируем пароль только из цифр
	public static final String GENERATE_TEST_CODE="generate.test.code";//генерируемый тестовый смс-код 
	public static final String GENERATE_PASSWORD_NUMBER_CHARS="generate.password.number.chars";//генерируемое кол-во символов в пароле
	public static final String GENERATE_CODE_NUMBER_CHARS="generate.code.number.chars";//генерируемое кол-во символов в смс-коде
}
