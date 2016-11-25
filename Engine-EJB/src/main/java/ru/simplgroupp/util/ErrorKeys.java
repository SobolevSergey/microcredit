package ru.simplgroupp.util;

public class ErrorKeys {
    public static final int SUCCESS = 0;

    public static final int CONTACT_NOT_SAME_OWNERS = 1;
    
    public static final int PLUGIN_WRONG_CONFIGURATION = 2;
    public static final int PLUGIN_OFF = 3;

    public static final int PAYMENT_SYSTEM_NOT_FOUND = 4;
    public static final Integer WAIT = 5;

    public static final int NO_CONNECTION = 10;
    public static final int BAD_REQUEST = 11;
    public static final int CANT_CREATE_SIGN = 12;
    public static final int INVALID_SIGN = 13;
    public static final int CANT_MAKE_SKORING = 14;
    public static final int BAD_RESPONSE = 15;

    public static final int PAYMENT_NOT_FOUND = 16;

    public static final int INVALID_ACCOUNT = 17;
    
    public static final int CANT_COMPLETE_SEND_HTTP = 18;
    public static final int CANT_PARSE_RESPONSE = 19;
    public static final int CANT_CREATE_SSL_CONTEXT = 20;
    public static final int CANT_WRITE_LOG = 21;
    public static final int CANT_INIT_OBJECT = 22;
    public static final int CANT_DECODE_MESSAGE = 23;
    public static final int CANT_SET_URL = 24;
    public static final int CANT_CREATE_MARSHALLER = 25;
    public static final int CANT_CREATE_SOAP_FACTORY = 26;
    public static final int CANT_CREATE_SOAP_MESSAGE = 27;
    public static final int CANT_SAVE_REQUEST = 28;
    public static final int NOTHING_TO_DECODE = 29;
    public static final int CANT_CREATE_WEBSERVICE = 30;
    public static final int CANT_CREATE_JAXB_CONTEXT = 31;
    public static final int CANT_CREATE_DATE_FACTORY = 31;
    
    public static final int AUTH_ERROR = 31;
    public static final int DISABLED_PROTOCOL = 32;
    public static final int ORDER_NOT_FOUND = 33;
    public static final int DUPLICATE_ORDER = 34;
    public static final int INVALID_AMOUNT = 35;
    public static final int TECH_ERROR = 36;
    public static final int ACCESS_DENIED = 37;
    public static final int ORDER_REJECTED = 38;
    public static final int ORDER_UNPAID = 39;
    public static final int ORDER_EXPIRED = 40;
    public static final int ORDER_FAIL = 41;
    
    public static final int MODEL_SYSTEM_ERROR = 42;
    
    public static final int CANT_VERIFY_SIGN = 43; // невозможно проверить подпись
    public static final int INVALID_AGENT = 44; // неверный агент
    public static final int INVALID_CURRENCY = 45; // неверная валюта
    public static final int INVALID_DATE = 46; //неверная дата
    public static final int INVALID_ID = 47; //неверный/отсутствует ID
    public static final int LOW_BALANCE = 48; //недостаточно средств для проведения операции.
    public static final int MAX_TOTAL_AMOUNT = 49; //превышено ограничение на максимальную сумму зачислений за период времени.
    public static final int RUNTIME_ERROR = 50; // любой exception во время выполнения
    public static final int BAD_CARD = 51; // провести платеж по банковской карте невозможно.
    public static final int BANK_ERROR = 52; // платеж отклоняется банком-эмитентом карты.
    public static final int PLUGIN_NOT_FOUND = 53;
    public static final int MAX_REPEAT_EXCEEDED = 54;

    public static final int UNKNOWN = 1000;

	public static final int PACKET_MEMBER_ERROR = 101;

    public static final int EMAIL_EXISTS = -1;
    public static final int PHONE_EXISTS = -2;
    public static final int ANOTHER_COUNTRY = -3;
    public static final int PERSON_EXISTS = -4;
    public static final int DOCUMENT_EXISTS = -5;
    
	public static final int ACTION_GENERAL_ERROR = 1001;

    public static final int BAD_STATE = 54;

    public static final int EXPERIAN_CANT_INIT = 2000;

}
