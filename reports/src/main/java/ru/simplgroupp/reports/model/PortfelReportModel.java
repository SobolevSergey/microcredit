package ru.simplgroupp.reports.model;

import java.util.Date;
/**
 * модель отчета по портфелю
 */
public class PortfelReportModel {
    /**
     * портфель всего количество
     */
    private Long amount;
    /**
     * портфель од
     */
    private Double commonDebt;
    /**
     * портфель просрочка сумма од прирост
     */
    private Double commonDebtIncrement;
    /**
     * портфель кол-во срок
     */
    private long amountInTerm;
    /**
     * портфель кол-во просрочка
     */
    private long amountWithOverdue;
    /**
     * портфель кол-во активные
     */
    private long amountActive;
    /**
     * портфель просрочка сроки кол-во 1-3 дня, остальные по аналогии
     */
    private long amountWithOverdue1_3;
    private long amountWithOverdue4_10;
    private long amountWithOverdue11_30;
    private long amountWithOverdue31_60;
    private long amountWithOverdue61_90;
    private long amountWithOverdue91_plus;
    /**
     * портфель просрочка сумма од прирост 1 кредит
     */
    private double commonDebtIncrementOneCredit;
    /**
     * портфель просрочка сумма од прирост 2+ кредитов
     */
    private double commonDebtIncrementTwoPlusCredits;
    /**
     * портфель сумма од активные
     */
    private double commonDebtActive;
    /**
     * портфель сумма од срок
     */
    private double commonDebtInTerm;
    /**
     * портфель сумма од просрочка
     */
    private double commonDebtWithOverdue;
    /**
     * портфель сроки сумма од 1-3 дня, остальные по аналогии
     */
    private double commonDebtWithOverdue1_3;
    private double commonDebtWithOverdue4_10;
    private double commonDebtWithOverdue11_30;
    private double commonDebtWithOverdue31_60;
    private double commonDebtWithOverdue61_90;
    private double commonDebtWithOverdue91_plus;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getCommonDebt() {
        return commonDebt;
    }

    public void setCommonDebt(Double commonDebt) {
        this.commonDebt = commonDebt;
    }

    public Double getCommonDebtIncrement() {
        return commonDebtIncrement;
    }

    public void setCommonDebtIncrement(Double commonDebtIncrement) {
        this.commonDebtIncrement = commonDebtIncrement;
    }

    public void setAmountInTerm(long amountInTerm) {
        this.amountInTerm = amountInTerm;
    }

    public long getAmountInTerm() {
        return amountInTerm;
    }

    public void setAmountWithOverdue(long amountWithOverdue) {
        this.amountWithOverdue = amountWithOverdue;
    }

    public long getAmountWithOverdue() {
        return amountWithOverdue;
    }

    public void setAmountActive(long amountActive) {
        this.amountActive = amountActive;
    }

    public long getAmountActive() {
        return amountActive;
    }

    public void setAmountWithOverdue1_3(long amountWithOverdue1_3) {
        this.amountWithOverdue1_3 = amountWithOverdue1_3;
    }

    public long getAmountWithOverdue1_3() {
        return amountWithOverdue1_3;
    }

    public void setAmountWithOverdue4_10(long amountWithOverdue4_10) {
        this.amountWithOverdue4_10 = amountWithOverdue4_10;
    }

    public long getAmountWithOverdue4_10() {
        return amountWithOverdue4_10;
    }

    public void setAmountWithOverdue11_30(long amountWithOverdue11_30) {
        this.amountWithOverdue11_30 = amountWithOverdue11_30;
    }

    public long getAmountWithOverdue11_30() {
        return amountWithOverdue11_30;
    }

    public void setAmountWithOverdue31_60(long amountWithOverdue31_60) {
        this.amountWithOverdue31_60 = amountWithOverdue31_60;
    }

    public long getAmountWithOverdue31_60() {
        return amountWithOverdue31_60;
    }

    public void setAmountWithOverdue61_90(long amountWithOverdue61_90) {
        this.amountWithOverdue61_90 = amountWithOverdue61_90;
    }

    public long getAmountWithOverdue61_90() {
        return amountWithOverdue61_90;
    }

    public void setAmountWithOverdue91_plus(long amountWithOverdue91_plus) {
        this.amountWithOverdue91_plus = amountWithOverdue91_plus;
    }

    public long getAmountWithOverdue91_plus() {
        return amountWithOverdue91_plus;
    }

    public void setCommonDebtIncrementOneCredit(double commonDebtIncrementOneCredit) {
        this.commonDebtIncrementOneCredit = commonDebtIncrementOneCredit;
    }

    public double getCommonDebtIncrementOneCredit() {
        return commonDebtIncrementOneCredit;
    }

    public void setCommonDebtIncrementTwoPlusCredits(double commonDebtIncrementTwoPlusCredits) {
        this.commonDebtIncrementTwoPlusCredits = commonDebtIncrementTwoPlusCredits;
    }

    public double getCommonDebtIncrementTwoPlusCredits() {
        return commonDebtIncrementTwoPlusCredits;
    }

    public void setCommonDebtActive(double commonDebtActive) {
        this.commonDebtActive = commonDebtActive;
    }

    public double getCommonDebtActive() {
        return commonDebtActive;
    }

    public void setCommonDebtInTerm(double commonDebtInTerm) {
        this.commonDebtInTerm = commonDebtInTerm;
    }

    public double getCommonDebtInTerm() {
        return commonDebtInTerm;
    }

    public void setCommonDebtWithOverdue(double commonDebtWithOverdue) {
        this.commonDebtWithOverdue = commonDebtWithOverdue;
    }

    public double getCommonDebtWithOverdue() {
        return commonDebtWithOverdue;
    }

    public void setCommonDebtWithOverdue1_3(double commonDebtWithOverdue1_3) {
        this.commonDebtWithOverdue1_3 = commonDebtWithOverdue1_3;
    }

    public double getCommonDebtWithOverdue1_3() {
        return commonDebtWithOverdue1_3;
    }

    public void setCommonDebtWithOverdue4_10(double commonDebtWithOverdue4_10) {
        this.commonDebtWithOverdue4_10 = commonDebtWithOverdue4_10;
    }

    public double getCommonDebtWithOverdue4_10() {
        return commonDebtWithOverdue4_10;
    }

    public void setCommonDebtWithOverdue11_30(double commonDebtWithOverdue11_30) {
        this.commonDebtWithOverdue11_30 = commonDebtWithOverdue11_30;
    }

    public double getCommonDebtWithOverdue11_30() {
        return commonDebtWithOverdue11_30;
    }

    public void setCommonDebtWithOverdue31_60(double commonDebtWithOverdue31_60) {
        this.commonDebtWithOverdue31_60 = commonDebtWithOverdue31_60;
    }

    public double getCommonDebtWithOverdue31_60() {
        return commonDebtWithOverdue31_60;
    }

    public void setCommonDebtWithOverdue61_90(double commonDebtWithOverdue61_90) {
        this.commonDebtWithOverdue61_90 = commonDebtWithOverdue61_90;
    }

    public double getCommonDebtWithOverdue61_90() {
        return commonDebtWithOverdue61_90;
    }

    public void setCommonDebtWithOverdue91_plus(double commonDebtWithOverdue91_plus) {
        this.commonDebtWithOverdue91_plus = commonDebtWithOverdue91_plus;
    }

    public double getCommonDebtWithOverdue91_plus() {
        return commonDebtWithOverdue91_plus;
    }
}
