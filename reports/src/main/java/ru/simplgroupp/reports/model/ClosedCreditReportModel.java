package ru.simplgroupp.reports.model;

import java.util.Date;

/**
 * модель отчета по закрытиям
 */
public class ClosedCreditReportModel {
    /**
     * к закрытию план количество займов
     */
    private Long plannedToCloseCreditsAmt;
    /**
     * закрытия факт количество займов
     */
    private Long closedCreditsAmt;
    /**
     * закрытия факт количество займов 1 кредит
     */
    private long closedCreditsAmtOneCredit;
    /**
     * закрытия факт количество займов 2+ кредит
     */
    private long closedCreditsAmtTwoPlusCredits;
    /**
     * к закрытию план количество займов 1 кредит
     */
    private long plannedToCloseCreditsAmtOneCredit;
    /**
     * к закрытию план количество займов 2+ кредит
     */
    private long plannedToCloseCreditsAmtTwoPlusCredits;
    /**
     * закрытия факт количество займов досрочка
     */
    private long closedCreditsAmtBeforeTerm;
    /**
     * закрытия факт количество займов срок
     */
    private long closedCreditsAmtInTerm;
    /**
     * закрытия факт количество займов просрочка 1-3 дня, остальные по аналогии
     */
    private long closedCreditsWithOverdue1_3;
    private long closedCreditsWithOverdue4_10;
    private long closedCreditsWithOverdue11_30;
    private long closedCreditsWithOverdue31_60;
    private long closedCreditsWithOverdue61_90;
    private long closedCreditsWithOverdue91_plus;

    public Long getPlannedToCloseCreditsAmt() {
        return plannedToCloseCreditsAmt;
    }

    public void setPlannedToCloseCreditsAmt(Long plannedToCloseCreditsAmt) {
        this.plannedToCloseCreditsAmt = plannedToCloseCreditsAmt;
    }

    public Long getClosedCreditsAmt() {
        return closedCreditsAmt;
    }

    public void setClosedCreditsAmt(Long closedCreditsAmt) {
        this.closedCreditsAmt = closedCreditsAmt;
    }

    public void setClosedCreditsAmtOneCredit(long closedCreditsAmtOneCredit) {
        this.closedCreditsAmtOneCredit = closedCreditsAmtOneCredit;
    }

    public long getClosedCreditsAmtOneCredit() {
        return closedCreditsAmtOneCredit;
    }

    public void setClosedCreditsAmtTwoPlusCredits(long closedCreditsAmtTwoPlusCredits) {
        this.closedCreditsAmtTwoPlusCredits = closedCreditsAmtTwoPlusCredits;
    }

    public long getClosedCreditsAmtTwoPlusCredits() {
        return closedCreditsAmtTwoPlusCredits;
    }

    public void setPlannedToCloseCreditsAmtOneCredit(long plannedToCloseCreditsAmtOneCredit) {
        this.plannedToCloseCreditsAmtOneCredit = plannedToCloseCreditsAmtOneCredit;
    }

    public long getPlannedToCloseCreditsAmtOneCredit() {
        return plannedToCloseCreditsAmtOneCredit;
    }

    public void setPlannedToCloseCreditsAmtTwoPlusCredits(long plannedToCloseCreditsAmtTwoPlusCredits) {
        this.plannedToCloseCreditsAmtTwoPlusCredits = plannedToCloseCreditsAmtTwoPlusCredits;
    }

    public long getPlannedToCloseCreditsAmtTwoPlusCredits() {
        return plannedToCloseCreditsAmtTwoPlusCredits;
    }

    public void setClosedCreditsAmtBeforeTerm(long closedCreditsAmtBeforeTerm) {
        this.closedCreditsAmtBeforeTerm = closedCreditsAmtBeforeTerm;
    }

    public long getClosedCreditsAmtBeforeTerm() {
        return closedCreditsAmtBeforeTerm;
    }

    public void setClosedCreditsAmtInTerm(long closedCreditsAmtInTerm) {
        this.closedCreditsAmtInTerm = closedCreditsAmtInTerm;
    }

    public long getClosedCreditsAmtInTerm() {
        return closedCreditsAmtInTerm;
    }

    public void setClosedCreditsWithOverdue1_3(long closedCreditsWithOverdue1_3) {
        this.closedCreditsWithOverdue1_3 = closedCreditsWithOverdue1_3;
    }

    public long getClosedCreditsWithOverdue1_3() {
        return closedCreditsWithOverdue1_3;
    }

    public void setClosedCreditsWithOverdue4_10(long closedCreditsWithOverdue4_10) {
        this.closedCreditsWithOverdue4_10 = closedCreditsWithOverdue4_10;
    }

    public long getClosedCreditsWithOverdue4_10() {
        return closedCreditsWithOverdue4_10;
    }

    public void setClosedCreditsWithOverdue11_30(long closedCreditsWithOverdue11_30) {
        this.closedCreditsWithOverdue11_30 = closedCreditsWithOverdue11_30;
    }

    public long getClosedCreditsWithOverdue11_30() {
        return closedCreditsWithOverdue11_30;
    }

    public void setClosedCreditsWithOverdue31_60(long closedCreditsWithOverdue30_60) {
        this.closedCreditsWithOverdue31_60 = closedCreditsWithOverdue30_60;
    }

    public long getClosedCreditsWithOverdue31_60() {
        return closedCreditsWithOverdue31_60;
    }

    public void setClosedCreditsWithOverdue61_90(long closedCreditsWithOverdue61_90) {
        this.closedCreditsWithOverdue61_90 = closedCreditsWithOverdue61_90;
    }

    public long getClosedCreditsWithOverdue61_90() {
        return closedCreditsWithOverdue61_90;
    }

    public void setClosedCreditsWithOverdue91_plus(long closedCreditsWithOverdue91_plus) {
        this.closedCreditsWithOverdue91_plus = closedCreditsWithOverdue91_plus;
    }

    public long getClosedCreditsWithOverdue91_plus() {
        return closedCreditsWithOverdue91_plus;
    }
}
