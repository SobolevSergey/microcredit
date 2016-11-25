package ru.simplgroupp.reports.model;

import java.util.Date;

/**
 * модель отчета по платежам
 *
 */
public class PaymentReportModel {
    /**
     * к оплате план количество
     */
    private Long plannedToPayAmount;
    /**
     * к оплате план количество - 1 займ
     */
    private Long plannedToPayAmountOneCredit;
    /**
     * к оплате план количество - 2 и более займа
     */
    private Long plannedToPayAmountTwoPlusCredits;
    /**
     * к оплате план сумма всего
     */
    private Double plannedToPaySum;
    /**
     * к оплате план основной долг
     */
    private Double plannedToPaySumCommonDebt;
    /**
     * к оплате план штрафы
     */
    private Double plannedToPaySumPenalties;
    /**
     * к оплате план проценты
     */
    private Double plannedToPayPercents;
    /**
     * оплаты факт кол-во займов
     */
    private Long payedAmount;
    /**
     * оплаты факт сумма всего
     */
    private Double payedSum;
    /**
     * оплаты денежный поток
     */
    private Double moneyFlow;
    /**
     * оплаты факт кол-во 2+ займа
     */
    private long payedAmountTwoPlusCredits;
    /**
     * оплаты факт кол-во 1 займ
     */
    private long payedAmountOneCredit;
    /**
     * оплаты факт сумма од
     */
    private double payedSumCommonDebt;
    /**
     * оплаты факт проценты
     */
    private double payedSumPercents;
    /**
     * оплаты факт штрафы
     */
    private double payedSumPenalties;
    /**
     * сумма оплаты досрочка
     */
    private double payedSumBeforeTerm;
    /**
     * сумма оплаты в срок
     */
    private double payedSumInTerm;
    /**
     * сумма оплаты просрочка 1-3 дня, остальные поля по аналогии
     */
    private double payedSumOverdue1_3;
    private double payedSumOverdue4_10;
    private double payedSumOverdue11_30;
    private double payedSumOverdue31_60;
    private double payedSumOverdue61_90;
    private double payedSumOverdue91_plus;

    public Double getPlannedToPaySumCommonDebt() {
        return plannedToPaySumCommonDebt;
    }

    public void setPlannedToPaySumCommonDebt(Double plannedToPaySumCommonDebt) {
        this.plannedToPaySumCommonDebt = plannedToPaySumCommonDebt;
    }

    public Double getPlannedToPaySumPenalties() {
        return plannedToPaySumPenalties;
    }

    public void setPlannedToPaySumPenalties(Double plannedToPaySumPenalties) {
        this.plannedToPaySumPenalties = plannedToPaySumPenalties;
    }

    public Double getPlannedToPayPercents() {
        return plannedToPayPercents;
    }

    public void setPlannedToPayPercents(Double plannedToPayPercents) {
        this.plannedToPayPercents = plannedToPayPercents;
    }

    public Long getPlannedToPayAmountOneCredit() {
        return plannedToPayAmountOneCredit;
    }

    public void setPlannedToPayAmountOneCredit(Long plannedToPayAmountOneCredit) {
        this.plannedToPayAmountOneCredit = plannedToPayAmountOneCredit;
    }

    public Long getPlannedToPayAmountTwoPlusCredits() {
        return plannedToPayAmountTwoPlusCredits;
    }

    public void setPlannedToPayAmountTwoPlusCredits(Long plannedToPayAmountTwoPlusCredits) {
        this.plannedToPayAmountTwoPlusCredits = plannedToPayAmountTwoPlusCredits;
    }

    public void setPayedSum(Double payedSum) {
        this.payedSum = payedSum;
    }

    public void setMoneyFlow(Double moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public Long getPlannedToPayAmount() {
        return plannedToPayAmount;
    }

    public void setPlannedToPayAmount(Long plannedToPayAmount) {
        this.plannedToPayAmount = plannedToPayAmount;
    }

    public Double getPlannedToPaySum() {
        return plannedToPaySum;
    }

    public void setPlannedToPaySum(Double plannedToPaySum) {
        this.plannedToPaySum = plannedToPaySum;
    }

    public Long getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(Long payedAmount) {
        this.payedAmount = payedAmount;
    }

    public Double getPayedSum() {
        return payedSum;
    }

    public Double getMoneyFlow() {
        return moneyFlow;
    }

    public void setPayedAmountTwoPlusCredits(long payedAmountTwoPlusCredits) {
        this.payedAmountTwoPlusCredits = payedAmountTwoPlusCredits;
    }

    public long getPayedAmountTwoPlusCredits() {
        return payedAmountTwoPlusCredits;
    }

    public void setPayedAmountOneCredit(long payedAmountOneCredit) {
        this.payedAmountOneCredit = payedAmountOneCredit;
    }

    public long getPayedAmountOneCredit() {
        return payedAmountOneCredit;
    }

    public void setPayedSumCommonDebt(double payedSumCommonDebt) {
        this.payedSumCommonDebt = payedSumCommonDebt;
    }

    public double getPayedSumCommonDebt() {
        return payedSumCommonDebt;
    }

    public void setPayedSumPercents(double payedSumPercents) {
        this.payedSumPercents = payedSumPercents;
    }

    public double getPayedSumPercents() {
        return payedSumPercents;
    }

    public void setPayedSumPenalties(double payedSumPenalties) {
        this.payedSumPenalties = payedSumPenalties;
    }

    public double getPayedSumPenalties() {
        return payedSumPenalties;
    }

    public void setPayedSumBeforeTerm(double payedSumBeforeTerm) {
        this.payedSumBeforeTerm = payedSumBeforeTerm;
    }

    public double getPayedSumBeforeTerm() {
        return payedSumBeforeTerm;
    }

    public void setPayedSumInTerm(double payedSumInTerm) {
        this.payedSumInTerm = payedSumInTerm;
    }

    public double getPayedSumInTerm() {
        return payedSumInTerm;
    }

    public void setPayedSumOverdue1_3(double payedSumOverdue1_3) {
        this.payedSumOverdue1_3 = payedSumOverdue1_3;
    }

    public double getPayedSumOverdue1_3() {
        return payedSumOverdue1_3;
    }

    public void setPayedSumOverdue4_10(double payedSumOverdue4_10) {
        this.payedSumOverdue4_10 = payedSumOverdue4_10;
    }

    public double getPayedSumOverdue4_10() {
        return payedSumOverdue4_10;
    }

    public void setPayedSumOverdue11_30(double payedSumOverdue11_30) {
        this.payedSumOverdue11_30 = payedSumOverdue11_30;
    }

    public double getPayedSumOverdue11_30() {
        return payedSumOverdue11_30;
    }

    public void setPayedSumOverdue31_60(double payedSumOverdue31_60) {
        this.payedSumOverdue31_60 = payedSumOverdue31_60;
    }

    public double getPayedSumOverdue31_60() {
        return payedSumOverdue31_60;
    }

    public void setPayedSumOverdue61_90(double payedSumOverdue61_90) {
        this.payedSumOverdue61_90 = payedSumOverdue61_90;
    }

    public double getPayedSumOverdue61_90() {
        return payedSumOverdue61_90;
    }

    public void setPayedSumOverdue91_plus(double payedSumOverdue91_plus) {
        this.payedSumOverdue91_plus = payedSumOverdue91_plus;
    }

    public double getPayedSumOverdue91_plus() {
        return payedSumOverdue91_plus;
    }
}
