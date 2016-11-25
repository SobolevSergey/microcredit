package ru.simplgroupp.util;

public interface RulesKeys {
	
	public static final String AC_PREFIX = "ru.simplgroupp.ac";
	
	public static final String BP_ACTIONS_PREFIX = "ru.simplgroupp.bp.actions";
	
	public static final String SYSTEM_MESSAGES_PREFIX = "ru.simplgroupp.message.formatted";

	public static final String BP_OPTIONS_PREFIX = "ru.simplgroupp.bp.options";

	public static final String BP_MESSAGES_PREFIX = "ru.simplgroupp.bp.messages";

	public static final String LIMITS_BONUS = "ru.simplgroupp.rules.bonus.limits";//работа с бонусами

	public static final String CALLBACK = "ru.simplgroupp.callback"; // работа с обратной связью

	public static final String PLUGINS_PREFIX = "ru.simplgroupp.plugins";//настройки плагинов

	public static final String CRYPTO_COMMON = "ru.simplgroupp.crypto.common";//параметры для криптографии

	public static final String PAYMENT_SYSTEM_ORDER = "ru.simplgroupp.payment.systems.order";

	public static final String CREDIT_RETURN_ENABLED = "ru.simplgroupp.rules.creditreturn.condition";
	
	public static final String CREDIT_RETURN_LIMITS = "ru.simplgroupp.rules.creditreturn.limits";
	
	public static final String CREDIT_CALC_INITIAL = "ru.simplgroupp.rules.credit.calculator.initial";//рассчет первоначального кредита
	
	public static final String CREDIT_CALC_INITIAL_STAKE = "ru.simplgroupp.rules.credit.calculator.stake1";//рассчет кредита и ставки
	
	public static final String BO_OPTIONS_PREFIX = "ru.simplgroupp.bo.options";
	
	public static final String BO_ACTIONS_PREFIX = "ru.simplgroupp.bo.actions";
	
	public static final String BA_MESSAGES_PREFIX = "ru.simplgroupp.ba.messages";
	
	public static final String BA_OPTIONS_PREFIX = "ru.simplgroupp.ba.options";

	public static final String SMS_COMMON = "ru.simplgroupp.sms.common";//настройка параметров отправки смс
	
	public static final String EMAIL_COMMON = "ru.simplgroupp.email.common";//настройка параметров отправки email
	
	public static final String SOCNETWORK_COMMON = "ru.simplgroupp.socnetwork.common";//настройка для соц.сетей
	
	public static final String SITE_COMMON = "ru.simplgroupp.site.common";//настройка работы с внешними БД

	public static final String AC_MAIN = AC_PREFIX + ".mainWeb";
	
	public static final String PAYMENT_ORDER_TO = "ru.simplgroup.payment.to.order";
	
	public static final String LOGIN_COMMON ="ru.simplgroupp.login";//настройка как логинимся
	
	public static final String GENERATE_COMMON ="ru.simplgroupp.generate";//настройка как генерим разные коды и пароли

	// настройки для мошеннической системы
	public static final String ANTIFRAUD_SETTINGS = "ru.simplgroupp.antifraud";
    public static final String ANTIFRAUD_TRANSFER_EXTERNAL="antifraud.transferToExternalSystems";//передаем ли данные во внешние системы
    public static final String ANTIFRAUD_CHECK_INTERNAL_RULES="antifraud.checkInternalRules";//проверяем ли на внутренние АМ правила

	// обратная связь
	public static final String CALLBACK_ENABLED = "callback.enabled"; // включение/отключение уведомлений
	public static final String CALLBACK_EMAIL = "callback.email"; // email для отправки уведомлений

	//сессии для рассчета
	public static final String SESSION_BONUS_CALC="kbBonusCalc";
	public static final String SESSION_CREDIT_PARAMS="kbCreditParams";
	public static final String SESSION_CREDIT_CALC="kbCreditCalc";
	public static final String SESSION_RETURN="kbReturn";
	public static final String SESSION_WATCHED_FIELDS="kbWatchedFields";
	public static final String SESSION_LONG_DAYS_CALC="kbLongdaysCalc";
	public static final String SESSION_CREDIT_COLLETOR = "kbCreditCollector";
	

}
