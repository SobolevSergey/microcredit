package ru.simplgroupp.reports.model;

import java.util.Date;
/**
 * модель отчета по продлениям
 *
 */
public class ProlongationReportModel {
    /**
     * продления кол-во
     */
    private Long prolongationsAmount;
    /**
     * продления кол-во займов
     */
    private Long creditAmount;
    /**
     * продления займ средняя сумма
     */
    private Double creditAvgSum;
    /**
     * продления средний срок
     */
    private Double prolongationAvgTerm;
    /**
     * продления платеж средняя сумма
     */
    private Double prolongationPaymentAvgSum;
    /**
     * продления кол-во 1 кредит
     */
    private long prolongationsAmountOneCredit;
    /**
     * продления кол-во 2+ кредита
     */
    private long prolongationsAmountTwoPlusCredits;
    /**
     * продления кол-во займов 1+ займ
     */
    private long creditAmountOneCredit;
    /**
     * продления кол-во займов 2+ займ
     */
    private long creditAmountTwoPlusCredits;
    /**
     * продления займ средняя сумма 1 кредит
     */
    private double creditAvgSumOneCredit;
    private double creditAvgSumTwoPlusCredits;
    /**
     * продления средний срок 1 кредит
     */
    private double prolongationAvgTermOneCredit;
    private double prolongationAvgTermTwoPlusCredits;

    public Long getProlongationsAmount() {
        return prolongationsAmount;
    }

    public void setProlongationsAmount(Long prolongationsAmount) {
        this.prolongationsAmount = prolongationsAmount;
    }

    public Long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Long creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getCreditAvgSum() {
        return creditAvgSum;
    }

    public void setCreditAvgSum(Double creditAvgSum) {
        this.creditAvgSum = creditAvgSum;
    }

    public Double getProlongationAvgTerm() {
        return prolongationAvgTerm;
    }

    public void setProlongationAvgTerm(Double prolongationAvgTerm) {
        this.prolongationAvgTerm = prolongationAvgTerm;
    }

    public Double getProlongationPaymentAvgSum() {
        return prolongationPaymentAvgSum;
    }

    public void setProlongationPaymentAvgSum(Double prolongationPaymentAvgSum) {
        this.prolongationPaymentAvgSum = prolongationPaymentAvgSum;
    }

    public void setProlongationsAmountOneCredit(long prolongationsAmountOneCredit) {
        this.prolongationsAmountOneCredit = prolongationsAmountOneCredit;
    }

    public long getProlongationsAmountOneCredit() {
        return prolongationsAmountOneCredit;
    }

    public void setProlongationsAmountTwoPlusCredits(long prolongationsAmountTwoPlusCredits) {
        this.prolongationsAmountTwoPlusCredits = prolongationsAmountTwoPlusCredits;
    }

    public long getProlongationsAmountTwoPlusCredits() {
        return prolongationsAmountTwoPlusCredits;
    }

    public void setCreditAmountOneCredit(long creditAmountOneCredit) {
        this.creditAmountOneCredit = creditAmountOneCredit;
    }

    public long getCreditAmountOneCredit() {
        return creditAmountOneCredit;
    }

    public void setCreditAmountTwoPlusCredits(long creditAmountTwoPlusCredits) {
        this.creditAmountTwoPlusCredits = creditAmountTwoPlusCredits;
    }

    public long getCreditAmountTwoPlusCredits() {
        return creditAmountTwoPlusCredits;
    }

    public void setCreditAvgSumOneCredit(double creditAvgSumOneCredit) {
        this.creditAvgSumOneCredit = creditAvgSumOneCredit;
    }

    public double getCreditAvgSumOneCredit() {
        return creditAvgSumOneCredit;
    }

    public void setCreditAvgSumTwoPlusCredits(double creditAvgSumTwoPlusCredits) {
        this.creditAvgSumTwoPlusCredits = creditAvgSumTwoPlusCredits;
    }

    public double getCreditAvgSumTwoPlusCredits() {
        return creditAvgSumTwoPlusCredits;
    }

    public void setProlongationAvgTermOneCredit(double prolongationAvgTermOneCredit) {
        this.prolongationAvgTermOneCredit = prolongationAvgTermOneCredit;
    }

    public double getProlongationAvgTermOneCredit() {
        return prolongationAvgTermOneCredit;
    }

    public void setProlongationAvgTermTwoPlusCredits(double prolongationAvgTermTwoPlusCredits) {
        this.prolongationAvgTermTwoPlusCredits = prolongationAvgTermTwoPlusCredits;
    }

    public double getProlongationAvgTermTwoPlusCredits() {
        return prolongationAvgTermTwoPlusCredits;
    }
}
