package ru.simplgroupp.util;

public class ProductKeys {

	    //виды конфигураций для продукта
		public static final int PRODUCT_CREDITREQUEST=1;//новая заявка
		public static final int PRODUCT_CREDIT_PROLONG=2;//продление
		public static final int PRODUCT_CREDIT_RETURN_PAYMENT=3;//погашение клиентом
		public static final int PRODUCT_CREDIT_REFINANCE=4;//рефинансирование
		public static final int PRODUCT_CREDIT_OVERDUE=5;//просрочка
		public static final int PRODUCT_CREDIT_MESSAGES=6;//сообщения системы
		public static final int PRODUCT_CREDIT=7;//кредит
		public static final int PRODUCT_CREDIT_DECISION=8;//принятие решения по кредиту
		public static final int PRODUCT_COLLECTOR=9;//коллектор
		public static final int PRODUCT_CREDIT_PAYMENT=10;//оплата клиенту
				
		//выдача кредита
		public static final String FIRST_REQUEST_PAYMENT_SYSTEMS = "firstRequestPaySys"; // платежные системы для первой заявки
		public static final String FIRST_REQUEST_PAY_SYS_TIMES = "firstRequestPaySysTimes";//Сколько кредитов выдаем на ограниченные платежные системы
		public static final String RETURN_CONTACT_PAYMENT_DAYS="return.contact.payment.days";//через сколько дней возвращаем неоплаченный платеж контакта
		public static final String CREDIT_WAIT_SIGN_OFERTA="waitSignOferta"; // сколько дней ждем подписание оферты

		//новая заявка
		public static final String CREDIT_STAKE_INITIAL_UNKNOWN="credit.stake.initial.unknown";//первоначальная рассчитываемая ставка в анкете
		public static final String CREDIT_STAKE_INITIAL="credit.stake.initial";//первоначальная ставка для зарегистрированного
		public static final String CREDIT_SUM_MIN="credit.sum.min";//минимальная сумма
		public static final String CREDIT_SUM_MAX="credit.sum.max";//максимальная сумма
		public static final String CREDIT_SUM_MAX_UNKNOWN="credit.sum.max.unknown";//максимальная сумма для незарегистрированного
		public static final String CREDIT_DAYS_MIN="credit.days.min";//минимально дней
		public static final String CREDIT_DAYS_MAX="credit.days.max";//максимально дней
		public static final String CREDIT_STAKE_MIN="credit.stake.min";//минимальная ставка
		public static final String CREDIT_STAKE_MAX="credit.stake.max";//максимальная ставка
		public static final String CREDIT_STAKE_DEFAULT="credit.stake.default";//ставка по умолчанию
		public static final String CREDIT_DAYS_BETWEEN_CREDITREQUESTS="credit.days.between.creditrequests";//дней между заявками при отказе
		public static final String CREDIT_SUM_ADDITION="credit.sum.addition";//сумма, добавляемая за закрытый кредит
		public static final String ADDITIONAL_DAYS_FOR_CALCULATE="additional.days.for.calculate";//сколько дней добавляем для расчета процентов по кредиту
		public static final String CREDIT_STAKE_MIN_OLDCLIENT="credit.stake.min.oldClient";//минимальная ставка для пользователя, который получал уже займы
		public static final String CREDIT_STAKE_MAX_OLDCLIENT="credit.stake.max.oldClient";//максимальная ставка для пользователя, который получал уже займы
		
		//рефинансирование
		public static final String REFINANCE_ENABLED="refinance.enabled";//возможно ли рефинансирование
		public static final String SUM_REFINANCE = "refinance.payment.sum.min"; //минимальная сумма для начала рефинансирования
		public static final String SUM_REFINANCE_REMAIN = "refinance.payment.sum.remain"; // оставшаяся сумма к выплате
		public static final String STAKE_REFINANCE = "refinance.stake";//ставка рефинансирования
		public static final String DAYS_REFINANCE_MAX = "refinance.days.max";//длительность рефинансирования
		public static final String REFINANCE_DAYS_OVERDUE_AVAILABLE = "refinance.days.overdue.available";//через сколько дней предлагать рефинансирование
		
		//продление
		public static final String PROLONG_ENABLED="prolong.enabled";//возможно ли продление
		public static final String PROLONG_DAYS_MIN="prolong.days.min";//минимально дней
		public static final String PROLONG_DAYS_MAX="prolong.days.max";//максимально дней
		public static final String CREDIT_PROLONGS_MAX="credit.prolongs.max";//максимально продлений
		public static final String CREDIT_DAYS_SUM_MAX="credit.days.sum.max";//максимально дней кредита вместе с продлениями
		public static final String CREDIT_HAS_DAYS_SUM_MAX="credit.has.days.sum.max";//есть ли ограничение на кол-во дней кредита вместе с продлениями
		public static final String CREDIT_HAS_PROLONG_FROM_OVERDUE="credit.has.prolong.from.overdue";//есть продление из просрочки
		public static final String CREDIT_PAY_MIN_SUM = "credit.pay.min.sum"; // минимальная сумма оплаты кредита
		public static final String CREDIT_USE_PERCENT_MIN_SUM = "credit.use.percent.min.sum"; // использовать в качестве минимальной суммы сумму процентов
		
		//просрочка
		public static final String CREDIT_DAYS_MIN_LGOT="credit.days.min.lgot";//минимально дней льготного периода
		public static final String CREDIT_DAYS_MAX_LGOT="credit.days.max.lgot";//максимально дней льготного периода
		public static final String CREDIT_STAKE_LGOT="credit.stake.lgot";//ставка для льготного периода
		public static final String CREDIT_DAYS_MIN_COMMON="credit.days.min.common";//минимально дней периода со штрафами
		public static final String CREDIT_DAYS_MAX_COMMON="credit.days.max.common";//максимально дней периода со штрафами
		public static final String CREDIT_STAKE_COMMON="credit.stake.common";//ставка для периода со штрафами
		public static final String CREDIT_DAYS_MIN_OVERDUE="credit.days.min.overdue";//минимально дней сильно просроченного периода
		public static final String CREDIT_DAYS_MAX_OVERDUE="credit.days.max.overdue";//максимально дней сильно просроченного периода
		public static final String CREDIT_STAKE_OVERDUE="credit.stake.overdue";//ставка для сильно просроченного периода
		public static final String CREDIT_HAS_LGOT_PERIOD="credit.has.lgot.period";//есть ли льготный период
		public static final String CREDIT_HAS_OVERDUE_PERIOD="credit.has.overdue.period";//есть ли период сильной просрочки
		public static final String CREDIT_DAYS_COLLECTOR_PERIOD="credit.days.collector.period";//с какого дня просрочки передаем коллектору
		
		//коллектор
		public static final String COLLECTOR_AGENCY_ENABLED="collector.agency.enabled";//передаем ли заявки внешнему коллектору
		public static final String COLLECTOR_DAYS_SOFT="collector.days.soft";//когда передаем заявки коллектору
		public static final String COLLECTOR_DAYS_HARD="collector.days.hard";//когда передаем заявки коллектору на уровень hard
		public static final String COLLECTOR_DAYS_AGENCY="collector.days.agency";//когда передаем заявки внешнему коллектору
		public static final String COLLECTOR_AUTO_DEALING="collector.auto.dealing";//автоматически распределять заявки по коллекторам
		public static final String COLLECTOR_HAS_SUPERVISOR="collector.has.supervisor";//есть ли супервизор для распределения заявок
		
		//сообщения
		public static final String EMAIL_ADD_BONUS="email.add.bonus";//письмо о добавлении бонусов
		public static final String EMAIL_INVITE_FRIEND="email.invite.friend";//письмо о приглашении друга
		public static final String EMAIL_LOGIN_CHANGED="email.login.changed";//письмо об изменении логина
		public static final String EMAIL_PASSWORD_CHANGED="email.password.changed";//письмо об изменении пароля

		// sms сообщения
		public static final String SMS_INVITE_FRIEND = "sms.invite.friend"; // смс о приглашении друга
		public static final String SMS_LOW_PAYMENT_TEXT = "sms.low.payment.text"; // смс сообщения при малом балансе платежной системы

		//принятие решения по кредиту
		public static final String VERIFICATOR_MUST_EDIT="verificatorMustEdit";//верификатор должен редактировать анкету
		public static final String TAKE_UNIQUE_CREDITS="takeUniqueCredits";//берем кредиты без дублей при разборе ответов КБ
		public static final String TAKE_UNIQUE_DOCUMENTS="takeUniqueDocuments";//берем документы без дублей при разборе ответов КБ, сравниваем каждый документ с клиентским
		public static final String NAME_FOR_COMPARISON="nameForComparison";//название из ОКБ, чтобы отличить свои кредиты
}
