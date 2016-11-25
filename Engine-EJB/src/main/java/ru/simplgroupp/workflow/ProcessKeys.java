package ru.simplgroupp.workflow;

public class ProcessKeys {

	//процессы
//	public static final String DEF_NEW_CREDIT_REQUEST = "procNewCR";
	public static final String DEF_CREDIT_REQUEST_OFFLINE = "procCROffline";
	public static final String DEF_CREDIT_REQUEST = "procCR";
	public static final String DEF_SUB_STANDART = "subStandart";
	public static final String PREFIX_DEF_SUB_PACKET = "subPacket";
	public static final String DEF_WORK_MODEL = "procWorkModel";
	public static final String DEF_DEBUG_MODEL = "procDebugModel";
	public static final String DEF_CREDIT_PROLONG = "procProlongCR";
	public static final String DEF_PAYMENT = "procPayment";
	public static final String DEF_CREDIT_RETURN = "procReturnCR";
	public static final String DEF_CREDIT_CONSIDER = "procConsiderCR";
	public static final String DEF_CREDIT_CONSIDER_OFFLINE = "procConsiderCROffline";
	public static final String DEF_ACT_STANDART = "actStandart";	
	public static final String DEF_REFINANCE = "procRefinance";
	
	public static final String PREFIX_PLUGIN = "plugin_";
	public static final String PREFIX_TASK = "task_";
	
	public static final String PREFIX_WF_ACTION = "wfAction";
	public static final String VAR_WF_ACTIONS = "wfActions";
	
	//для стратегии - поиск итд
	public static final String VAR_DECISION_STATE = "decisionState";
	public static final String VAR_PEOPLE_SEARCH = "PeopleSearch";
	public static final String VAR_CREDIT_SEARCH = "CreditSearch";
	public static final String VAR_CREDIT_REQUEST_SEARCH = "CreditRequestSearch";
	public static final String VAR_EVENTLOG_SEARCH = "EventLogSearch";
	public static final String VAR_SUMMARY_SEARCH = "SummarySearch";
	public static final String VAR_SCORING_SEARCH = "ScoringSearch";
	public static final String VAR_VERIFICATION_SEARCH = "VerificationSearch";
	public static final String VAR_NEGATIVE_SEARCH = "NegativeSearch";
	public static final String VAR_DEBT_SEARCH = "DebtSearch";
	public static final String VAR_DATESUTILS = "DatesUtils";
    public static final String VAR_ANTIFRAUD_SEARCH = "AntifraudSearch";
	public static final String VAR_IDENTITY_QUESTION_SEARCH = "IdentityQuestionSearch";
	public static final String VAR_NO_AUTO_REFUSE = "noAutomaticRefuse";
	public static final String VAR_SAVE_VARIABLES = "autoSaveVariables";

	public static final String VAR_ACTION_PROCESSOR = "actionProcessor";
	public static final String VAR_BIZACTION_PROCESSOR = "bizActionProcessor";
	public static final String VAR_ACTION_CONTEXT = "actionContext";
	public static final Object VAR_WORKFLOW = "workflow";
	public static final String VAR_RUNTIME_VARS = "runtime";
	public static final String VAR_CREDIT_REQUEST_ID = "creditRequestId";
	public static final String VAR_CREDIT_REQUEST = "creditRequest";
	public static final String VAR_CREDIT = "credit";
	public static final String VAR_PAYMENT = "payment";
	public static final String VAR_PROLONG = "prolong";
	public static final String VAR_REFINANCE = "refinance";
	public static final String VAR_CLIENT_USER_ID = "clientUserId";
	public static final String VAR_CLIENT = "client";
	public static final String VAR_CLIENT_USER = "clientUser";
	public static final String VAR_OPTIONS = "options";
	public static final String VAR_PAYMENT_FORM = "paymentForm";

	// для стандартного подпроцесса для работы с плагинами
	public static final String VAR_MODEL_KEY = "modelKey";
	public static final String VAR_PLUGIN = "plugin";
	public static final String VAR_PLUGIN_NAME = "pluginName";
	public static final String VAR_ACTIVE_PLUGIN_CALLS = "activePluginCalls";
	public static final String VAR_BUSINESS_OBJECT_ID = "businessObjectId";
	public static final String VAR_BUSINESS_OBJECT_CLASS = "businessObjectClass";
	public static final String VAR_BUSINESS_OBJECT = "businessObject";
	public static final String VAR_BUSINESS_ERRORS = "businessErrors";
	public static final String VAR_DATA = "data";
	public static final String VAR_LAST_ERROR = "lastError";
	public static final String VAR_NUM_REPEATS = "numRepeats";
	public static final String VAR_ASYNC_ANSWERED = "asyncAnswered";
	public static final String VAR_TASK_RESULT = "taskResult";
	public static final String VAR_CUSTOM_KEY = "customizationKey";
	
	public static final Object VAR_MODEL_ID = "modelId";
	public static final String VAR_MODEL_PARAMS = "modelParams";
	public static final Object VAR_PRODUCT_ID = "productId";
	
	public static final String EVENT_OPTIONS_CHANGED = "OPTIONS_CHANGED"; // изменены значения runtime-настроек для процесса
	
	// сообщения
	public static final String MSG_SUB_PACKET_END = "msgPluginPacketEnd";
	public static final String MSG_PACKET_FATAL_ERROR = "msgPacketFatalError";
	public static final String MSG_PACKET_SENDED = "msgPacketSended";
	public static final String MSG_ADD_TO_PACKET = "msgAddToPacket";
	public static final String MSG_ASYNC_PACKET_RECEIVED = "msgAsyncPacketReceived";
	public static final String MSG_RETURN = "msgReturn";
	public static final String MSG_PROLONG = "msgProlong";
	public static final String MSG_PROLONG_PAY = "msgProlongPay";
	public static final String MSG_PAYMENT_RECEIVED = "msgPaymentReceived";
	public static final String MSG_PROLONG_CANCEL = "msgProlongCancel";
	public static final String MSG_ACCEPT = "msgAccept";
	public static final String MSG_REJECT = "msgReject";
	public static final String MSG_REMOVE = "msgRemove";
	public static final String MSG_CLIENT_REJECT = "msgClientReject";
	public static final String MSG_ADD_CREDIT_REQUEST = "msgAddCreditRequest";
	public static final String MSG_SUSPEND = "msgSuspend";
	public static final String MSG_ACTIVATE = "msgActivate";
	public static final String MSG_TIMER = "msgTimer";
	public static final String MSG_END = "msgEnd";
	public static final String MSG_REFINANCE = "msgRefinance";
	public static final String MSG_REFINANCE_RUN = "msgRefinanceRun";
	public static final String MSG_CANCEL_PAYMENT_CONTACT ="msgCancelPaymentContact";
	//префиксы для стандартных сигналов
	public static final String PREFIX_SIGNAL_SIGNAL = "sig";
	public static final String PREFIX_SIGNAL_MESSAGE = "msg";
	
	//задачи
	public static final String TASK_EXECUTE_SINGLE = "taskExecuteSingle";
	public static final String TASK_EXECUTE_PACKET = "taskExecutePacket";
	public static final String TASK_SIGN_OFERTA="taskSignOferta";
	
}
