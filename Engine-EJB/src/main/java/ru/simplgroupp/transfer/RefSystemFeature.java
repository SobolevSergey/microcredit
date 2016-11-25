package ru.simplgroupp.transfer;

public class RefSystemFeature extends Reference {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7634831454991976778L;
	
	public static final int CREDIT_REQUEST_MAIN = 1000;
	public static final int CREDIT_MAIN = 1001;
	public static final int PEOPLE_MAIN = 1002;
	public static final int PAYMENT_MAIN = 1003;
	public static final int ADMIN_MAIN = 1004;
	public static final int REPORT_MAIN = 1005;
	public static final int CALLBACK_MAIN = 1006;
	public static final int COLLECTOR_MAIN = 1007;
	public static final int COLLECTOR_SUPERVISER_MAIN = 1008;
	public static final int MESSAGE_MAIN = 1009;

	// заявка
	public static final int CR_CREATE = 1;
	public static final int CR_VIEW = 2;
	public static final int CR_EDIT = 3;
	public static final int CR_DELETE = 4;	
	public static final int CR_DECISION = 5;
	public static final int CR_VERIFY = 6;
	public static final int CR_VIEW_ANTIFRAUD = 30;
	public static final int CR_VIEW_HISTORY = 31;
	public static final int CR_VIEW_VARIABLE = 32;
	public static final int CR_VIEW_DATA = 33;
	public static final int CR_VIEW_EMPLOYEE = 34;
	public static final int CR_VIEW_ADDITIONAL = 35;
	public static final int CR_VIEW_SUMMARY = 36;
	public static final int CR_VIEW_BLACK_LIST = 37;
	public static final int CR_VIEW_LOG = 38;
	public static final int CR_VIEW_REQUEST = 39;
	public static final int CR_VIEW_SCORING = 40;
	public static final int CR_VIEW_DEBT = 41;
	public static final int CR_VIEW_NEGATIVE = 42;
	public static final int CR_VIEW_PHONE_PAY = 43;
	public static final int CR_VIEW_BEHAVIOR = 44;
	public static final int CR_VIEW_VERIFY = 45;
	
	// займ
	public static final int C_VIEW = 7;
	public static final int C_EDIT = 8;
	public static final int C_DELETE = 9;
	
	// клиент
	public static final int PE_VIEW = 10;
	public static final int PE_EDIT = 11;
	public static final int PE_CREATE = 12;
	public static final int PE_DELETE = 13;
	public static final int PE_VIEW_CONTACT = 25;
	public static final int PE_VIEW_PASSPORT = 26;
	public static final int PE_VIEW_ADDRESS = 27;
	public static final int PE_VIEW_WORK_CONTACT = 28;
	public static final int PE_VIEW_WORK_ADDRESS = 29;
	
	// платёж
	public static final int P_VIEW = 14;
	public static final int P_EDIT = 15;
	public static final int P_DELETE = 16;
	public static final int P_CREATE = 17;
	
	// администрирование
	public static final int A_USER = 18;
	public static final int A_ROLE = 19;
	public static final int A_SETTINGS = 20;
	
	// отчеты
	public static final int REPORT_STAT = 21;
	public static final int REPORT_LIST = 22;
	public static final int REPORT_COLLECTOR = 23;
	public static final int REPORT_TEMPLATE = 24;
}
