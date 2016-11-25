package ru.simplgroupp.interfaces;

import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;

import javax.ejb.Local;
import java.util.Date;
import java.util.Map;

@Local
public interface CreditCalculatorBeanLocal {

    //кредит
    public static final String SUM_BACK = "sum.back"; // полная сумма возврата
    public static final String SUM_BACK_OVERDUE = "sum.back.overdue"; // полная сумма возврата для расчета просрочки
    public static final String SUM_MAIN = "sum.main"; // основная сумма долга
    public static final String SUM_ABOVE = "sum.above"; // сумма набежавших процентов, пени и прочего, помимо основного долга
    public static final String CREDIT_CLOSED_COUNT = "credit.closed.count";//кол-во закрытых кредитов
    // TODO удалить
    public static final String CREDIT_SUM_MIN = "credit.sum.min";//минимально возможная сумма займа
    public static final String CREDIT_SUM_MAX = "credit.sum.max";//максимально возможная сумма займа
    public static final String CREDIT_SUM_MAX_UNKNOWN = "credit.sum.max.unknown";//максимально возможная сумма займа для незарегистрированного
    public static final String CREDIT_DAYS_MIN = "credit.days.min";//минимально возможное кол-во дней займа
    public static final String CREDIT_DAYS_MAX = "credit.days.max";//максимально возможное кол-во дней займа
    public static final String CREDIT_SUM_ADDITION = "credit.sum.addition";//сумма, добавляемая за закрытый кредит
    public static final String STAKE_LIMIT_MIN = "credit.stake.min"; //минимальная ставка
    public static final String STAKE_LIMIT_MAX = "credit.stake.max";//максимальная ставка
    /**
     * сумма только процентов
     */
    public static final String SUM_PERCENT = "sum.percent";
    public static final String SUM_PERCENT_REMAIN = "sum.percent.remain"; // сумма оставшихся процентов к выплате
    public static final String SUM_PENALTY = "sum.penalty";//сумма штрафов
    public static final String SUM_COMISSION = "sum.comission";//сумма комиссии

    public static final String STAKE_INITIAL = "stake.initial"; // первоначальная ставка

    public static final String STAKE_CURRENT = "stake.current"; // текущая ставка
    /**
     * первоначальный срок кредита
     */
    public static final String DAYS_INITIAL = "days.initial";
    public static final String DAYS_CURRENT_START = "days.interval.current.start";
    public static final String DAYS_CURRENT_END = "days.interval.current.end";
    public static final String DAYS_OVERDUE = "days.overdue";//дней просрочки на дату расчета
    public static final String INVERVAL_CURRENT = "interval.current"; // номер интервала на шкале на дату расчёта
    public static final String INVERVAL_START = "interval.start"; // номер интервала на шкале на дату начала расчёта
    /**
     * актуальный срок кредита в днях
     */
    public static final String DAYS_ACTUAL = "days.actual";
    public static final String DATE_START_INITIAL = "date.start.initial";
    public static final String DATE_START_ACTUAL = "date.start.actual";
    public static final String DATE_END_ACTUAL = "date.end.actual";
    public static final String DATE_CURRENT_START = "date.interval.current.start"; // дата начала текущего интервала
    public static final String DATE_CURRENT_END = "date.interval.current.end"; // дата окончания текущего интервала

    //бонусы
    public static final String SUM_BONUS_FOR_PAYMENT = "sum.bonus.for.payment";//сумма бонусов к оплате
    public static final String SUM_BONUS_MONEY_FOR_PAYMENT = "sum.bonus.money.for.payment";//сумма бонусов к оплате в рублях
    public static final String BONUS_RATE = "bonus.rate";//ставка
    public static final String BONUS_PAY_PERCENT = "bonus.pay.procent";//сколько можно платить бонусами
    public static final String BONUS_PAY_MAX_SUM = "bonus.pay.max.sum";//максимальная сумма оплаты бонусами
    public static final String BONUS_ISACTIVE = "bonus.isactive";//активна ли система бонусов
    public static final String BONUS_USE_PERCENT_ONLY = "bonus.use.percent.only";//гасим бонусами только проценты
    
    //продление
    public static final String PROLONG_DAYS = "prolong.days";//дней продления
    public static final String PROLONG_DAYS_FOR_PAYMENT = "prolong.days.for.payment";//дней для оплаты процентов по продлению
    public static final String PROLONG_DAYS_BEFORE_CREDIT_END = "prolong.days.before.credit.end";//дней до окончания кредита, если просрочка, то 0

    /**
     * посчитать кредит
     *
     * @param creditId id кредита
     * @param dateCalc дата рассчета
     */
    public Map<String, Object> calcCredit(int creditId, Date dateCalc);

    /**
     * посчитать кредит
     *
     * @param credit   кредит
     * @param dateCalc дата рассчета
     */
    public Map<String, Object> calcCredit(Credit credit, Date dateCalc);

    /**
     * посчитать начальный кредит
     *
     * @param sumMain основная сумма
     * @param stake   ставка
     * @param days    кол-во дней
     */
    public Map<String, Object> calcCreditInitial(Double sumMain, Double stake,
                                                 Integer days,Integer productId);

    /**
     * посчитать начальный кредит
     *
     * @param ccRequest заявка
     */
    public Map<String, Object> calcCreditInitial(CreditRequest ccRequest);

    /**
     * посчитать ставку
     *
     * @param creditSum  сумма
     * @param creditDays дни
     * @param limits     параметры для рассчета
     */
    public double calcInitialStake(double creditSum, int creditDays, Map<String, Object> limits);

    /**
     * посчитать параметры для кредита - мин и макс сумму, кот. данный человек может взять
     *
     * @param peopleId id
     * @param limits   параметры для рассчета
     */
    public Map<String, Object> calcCreditParams(Integer peopleId, Map<String, Object> limits);

    /**
     * посчитать рефинансирование
     *
     * @param credit   кредит
     * @param dateCalc дата рассчета
     */
    Map<String, Object> calcRefinance(Credit credit, Date dateCalc);

    /**
     * посчитать рефинансирование
     *
     * @param creditId id кредита
     * @param dateCalc дата рассчета
     */
    Map<String, Object> calcRefinance(int creditId, Date dateCalc);

    /**
     * посчитать максимальную сумму бонусов к оплате
     *
     * @param sumBack  сумма к возврату
     * @param bonusSum сумма бонусов
     * @param limits   константы
     */
    public Map<String, Object> calcBonusPaymentSum(Double sumBack, Double bonusSum, Map<String, Object> limits);
    /**
     * посчитать максимальную сумму бонусов к оплате
     *
     * @param sumBack    сумма к возврату
     * @param sumPercent сумма процентов
     * @param bonusSum   сумма бонусов
     * @param limits     константы
     */
  //  public Map<String, Object> calcBonusPaymentSum(Double sumBack, Double sumPercent,Double bonusSum, Map<String, Object> limits);

    /**
     * посчитать кол-во дней для продления
     * 
     * @param credit - кредит
     * @param longDate - дата продления
     * @return
     */
    public Map<String, Object> calcProlongDays(Credit credit, Date longDate);

    /**
     * посчитать кол-во дней для продления
     * 
     * @param creditId - id кредита
     * @param longDate - дата продления
     * @return
     */
    public Map<String, Object> calcProlongDays(Integer creditId, Date longDate);
}
