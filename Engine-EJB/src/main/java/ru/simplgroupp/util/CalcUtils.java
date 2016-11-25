package ru.simplgroupp.util;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.Range;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

public class CalcUtils {
    public static final int MODE_ALL = 1;
    public static final int MODE_PERCENT = 2;
    public static final String dformat = "##############.00";
    public static final String dformat_xx = "##############.###";
    public static final DecimalFormat dformat_00 = new DecimalFormat("##############.00");
    public static final DecimalFormat dformat_XX = new DecimalFormat("##############.##");
    private static final Logger logger = LoggerFactory.getLogger(CalcUtils.class);
    private static DecimalFormat moneyForm = new DecimalFormat("0.00");
    private static DecimalFormat psk = new DecimalFormat("0.000");

    static {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        moneyForm.setDecimalFormatSymbols(dfs);
        psk.setDecimalFormatSymbols(dfs);
    }

    /**
     * Округление, при котором 5.5 округляется до 6
     *
     * @param source - округляемое число
     * @param nscale - число знаков после запятой
     * @return
     */
    public static double roundHalfUp(double source, int nscale) {
        return new BigDecimal(source).setScale(nscale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * округление 1.1 до 2
     *
     * @param source число
     * @param nscale число знаков после запятой
     */
    public static double roundUp(double source, int nscale) {
        return new BigDecimal(source).setScale(nscale, RoundingMode.UP).doubleValue();
    }

    /**
     * Округление, при котором 5.5 округляется до 5
     *
     * @param source - округляемое число
     * @param nscale - число знаков после запятой
     * @return
     */
    public static double roundFloor(Double source, int nscale) {
        if (source == null) {
            return 0D;
        }
        return new BigDecimal(source).setScale(nscale, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * считаем ставку за год
     *
     * @param stake - ставка в день
     * @param date  -  дата
     */
    public static final Double calcYearStake(Double stake, Date date) {
        return roundHalfUp(stake * 100 * DatesUtils.getDaysOfYear(date), 1);
    }

    /**
     * считаем ставку за год, берем всегда 360 дней
     *
     * @param stake - ставка в день
     */
    public static final Double calcYearStake(Double stake) {
        return roundHalfUp(stake * 100 * 360, 1);
    }

    /**
     * переводим сумму в строку, 2 знака после запятой
     *
     * @param sum - сумма
     * @return
     */
    public static final String sumToString(Double sum) {
        if (sum == null) {
            return "0.00";
        }
        return moneyForm.format(sum);
    }

    /**
     * переводим сумму в строку, 3 знака после запятой
     *
     * @param sum - сумма
     * @return
     */
    public static final String pskToString(Double sum) {
        if (sum == null) {
            return "0.000";
        }
        return psk.format(sum);
    }

    /**
     * Конвертируем интервал в днях в интервал в датах
     *
     * @param dateStart
     * @param days
     * @return интервал дат
     */
    public static DateRange daysToDates(Date dateStart, IntegerRange days) {
        DateRange res = new DateRange(dateStart, DateUtils.addDays(dateStart, days.getTo() - days.getFrom()));
        return res;
    }

    /**
     * Конвертируем шкалу дней в шкалу дат
     *
     * @param dateStart - дата начала
     * @param days      - кол-во дней
     * @return
     */
    public static Object[] daysToDatesScale(Date dateStart, Object[] days) {
        Object[] res = new Object[days.length];

        for (int i = 0; i < days.length; i++) {
            if (days[i] == null) {

                res[i] = null;
                continue;
            }
            if (days[i] instanceof Range) {
                Range r = (Range) days[i];

                DateRange dr = new DateRange(null, null);
                if (r.getFrom() != null) {
                    Number n = (Number) r.getFrom();
                    dr.setFrom(DateUtils.addDays(dateStart, n.intValue()));

                }
                if (r.getTo() != null) {
                    Number n = (Number) r.getTo();
                    dr.setTo(DateUtils.addDays(dateStart, n.intValue()));

                }
                res[i] = dr;
                continue;
            }
            if (days[i] instanceof Number) {
                Number n = (Number) days[i];
                res[i] = DateUtils.addDays(dateStart, n.intValue());

            }
        }
        return res;
    }

    /**
     * Создаём шкалу из целых интервалов
     *
     * @param vals
     * @return массив IntegerRange
     */
    public static Object[] intRangeScale(Integer... vals) {
        Object[] res = new Object[vals.length / 2];

        int n = 0;
        for (int i = 0; i < vals.length; i = i + 2) {
            IntegerRange r = new IntegerRange(vals[i], vals[i + 1]);
            res[n] = r;
            n++;
        }
        return res;
    }

    /**
     * Определяем, в каком месте шкалы находится данное значение
     *
     * @param scale  - шкала значений. Содержит или значения, или интервалы (Range).
     * @param xValue - значение для поиска
     * @return - индекс в массиве scale, или -1 если не найдено
     */
    public static final int getScalePos(Object[] scale, Object xValue) {

        for (int i = 0; i < scale.length; i++) {

            if (scale[i] == null) {

                if (xValue == null) {
                    return i;
                } else {
                    continue;
                }
            }
            if (scale[i] instanceof Range) {
                Range r = (Range) scale[i];
                if (r instanceof DateRange) {
                    if (((DateRange) r).betweenDays(xValue)) {
                        return i;
                    }
                } else if (r.between(xValue)) {
                    return i;
                }
            } else if (scale[i].equals(xValue)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * посчитать сумму процентов для правила
     *
     * @param sumMain     - основная сумма
     * @param dateStart   - дата начала
     * @param stakeValues - массив ставок
     * @param scaleDates  - массив дат
     * @param dateCalc    - на какую дату считаем
     * @return
     */
    public static double calcSumPercent(double sumMain, Date dateStart, Double[] stakeValues, Object[] scaleDates, Date dateCalc) {
        double sumPercent = 0;
        logger.info("calcSumPercent dateStart " + dateStart);
        logger.info("calcSumPercent dateCalc " + dateCalc);
        for (int n = 0; n < scaleDates.length; n++) {

            DateRange dr = (DateRange) scaleDates[n];
            logger.info("Интервал дат при расчете " + dr.getFrom() + " " + dr.getTo());

            if (dateStart.after(dr.getTo())) {

                // период, который уже прошёл и не входит в рассчёт
                continue;
            }
            if (dr.betweenDays(dateStart) && !dr.betweenDays(dateCalc)) {
                // начальный период
                int ndays = DatesUtils.daysDiff(dr.getTo(), dateStart);
                sumPercent = sumPercent + sumMain * ndays * stakeValues[n].doubleValue();
                logger.info("calcSumPercent попали в начальный период, дней " + ndays + " сумма " + sumPercent + " ставка по кредиту " + stakeValues[n]);
                continue;
            }
            if ((dr.betweenDays(dateStart)) && (dr.betweenDays(dateCalc))) {
                // период полностью внутри рассчётного интервала, считаем между датой начала и датой рассчета
                int ndays = DatesUtils.daysDiff(dateCalc, dateStart);
                sumPercent = sumPercent + sumMain * ndays * stakeValues[n].doubleValue();
                logger.info("calcSumPercent попали в рассчетный интервал между датой начала и датой рассчета, дней " + ndays + " сумма " + sumPercent + " ставка по кредиту " + stakeValues[n]);
                continue;
            }
            if (dr.getFrom().after(dateStart) && dr.getTo().before(dateCalc)) {
                // период полностью внутри рассчётного интервала
                int ndays = DatesUtils.daysDiff(dr.getTo(), dr.getFrom());
                sumPercent = sumPercent + sumMain * ndays * stakeValues[n].doubleValue();
                logger.info("calcSumPercent попали в рассчетный интервал, дней " + ndays + " сумма " + sumPercent + " ставка по кредиту " + stakeValues[n]);
                continue;
            }
            if (!dr.betweenDays(dateStart) && dr.betweenDays(dateCalc)) {
                // конечный период
                int ndays = DatesUtils.daysDiff(dateCalc, dr.getFrom());
                sumPercent = sumPercent + sumMain * ndays * stakeValues[n].doubleValue();
                logger.info("calcSumPercent попали в конечный период, дней " + ndays + " сумма " + sumPercent + " ставка по кредиту " + stakeValues[n]);
            }
            if (dateCalc.before(dr.getFrom())) {
                // период, который ещё не начался и не входит в рассчёт
                continue;
            }
        }

        logger.info("Вернули сумму " + roundFloor(Convertor.toDouble(sumPercent, dformat), 0));
        return roundFloor(Convertor.toDouble(sumPercent, dformat), 0);
    }

    /**
     * посчитать сумму процентов простым способои
     *
     * @param sumMain - основная сумма
     * @param days    - кол-во дней
     * @param addOne  - добавляем день или нет
     * @param stake   - ставка в день
     * @return
     */
    public static double calcSumPercentSimple(Double sumMain, Integer days, boolean addOne, Double stake) {
        if (sumMain == null || days == null || stake == null) {
            return 0d;
        }
        if (addOne) {
            days += 1;
        }
        return roundFloor(sumMain * days * stake, 0);
    }

    /**
     * сумма к возврату когда считаем просрочку, если можно делать любой платеж
     * и сумма платежа меньше процентов
     *
     * @param sumMain     - основная сумма
     * @param sumBackLast - последняя сумма к возврату
     * @param stake       - ставка
     * @return
     */
    public static double calcSumBack(Double sumMain, Double sumBackLast, Double stake,
                                     Date date1, Date date2) {
        double sumPercent = sumMain * stake * DatesUtils.daysDiff(date1, date2) + sumBackLast - sumMain;
        return roundFloor(sumMain + sumPercent, 0);
    }

    /**
     * сумма к возврату по графику, если можно делать любой платеж
     * и сумма платежа меньше процентов
     *
     * @param sumBackAll - сумма возврата по графику
     * @param sumPays    - сумма платежа
     * @return
     */
    public static double calcSumBack(Double sumBackAll, Double sumPays) {
        return roundFloor(sumBackAll - sumPays, 0);
    }

    /**
     * сумма процентов - разница на текущий день с датой окончания кредита
     * только для непросроченных кредитов
     *
     * @param today   - текущий день
     * @param dataend - дата окончания кредита
     * @param sumMain - основная сумма
     * @param stake   - ставка
     * @return
     */
    public static double calcSumPercentForSubtract(Date today, Date dataend, Double sumMain, Double stake) {
        Integer days = DatesUtils.daysDiff(dataend, today);
        return roundFloor(sumMain * stake * days, 0);
    }
}
