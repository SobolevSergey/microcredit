package ru.simplgroupp.reports.model;

import java.util.Date;

/**
 * модель отчета по взысканиям
 */
public class CollectorsReportModel {
    /**
     * на взыскании - количество кредитов
     */
    private Long collectorCreditCount;
    /**
     * на взыскании сумма всего
     */
    private double collectorsCreditsTotalSum;
    /**
     * на взыскании сумма од
     */
    private double collectorsCreditsCommonSumDebt;
    /**
     * на взыскании сумма % и штрафы
     */
    private double collectorPenaltiesAndPercents;
    /**
     * взыскания сборы сумма всего
     */
    private double collectorsCollectedCreditsTotalSum;
    /**
     * взыскания сборы сумма од
     */
    private double collectorsCollectedCommonSumDebt;
    /**
     * взыскания сборы % и штрафы
     */
    private double collectorsCollectedPenaltiesAndPercents;
    /**
     * на взыскании - количество кредитов soft
     */
    private long collectorCreditCountSoft;
    /**
     * на взыскании - количество кредитов hard
     */
    private long collectorCreditCountHard;
    /**
     * на взыскании - количество кредитов legal
     */
    private long collectorCreditCountLegal;
    /**
     * взыскания сборы сумма всего legal
     */
    private double collectorsCollectedCreditsTotalSumLegal;
    /**
     * взыскания сборы сумма всего hard
     */
    private double collectorsCollectedCreditsTotalSumHard;
    /**
     * взыскания сборы сумма всего soft
     */
    private double collectorsCollectedCreditsTotalSumSoft;
    /**
     * взыскания сборы сумма % и штрафы legal
     */
    private double collectorPenaltiesAndPercentsLegal;
    /**
     * на взыскании сумма % и штрафы hard
     */
    private double collectorPenaltiesAndPercentsHard;
    /**
     * на взыскании сумма % и штрафы soft
     */
    private double collectorPenaltiesAndPercentsSoft;
    /**
     * на взыскании од legal
     */
    private double collectorsCreditsCommonSumDebtLegal;
    /**
     * на взыскании од hard
     */
    private double collectorsCreditsCommonSumDebtHard;
    /**
     * на взыскании од soft
     */
    private double collectorsCreditsCommonSumDebtSoft;
    /**
     * на взыскании сумма всего legal
     */
    private double collectorsCreditsTotalSumLegal;
    /**
     * на взыскании сумма всего hard
     */
    private double collectorsCreditsTotalSumHard;
    /**
     * на взыскании сумма всего soft
     */
    private double collectorsCreditsTotalSumSoft;
    /**
     * взыскания сборы количество
     */
    private long collectorsCollectedCount;

    public Long getCollectorCreditCount() {
        return collectorCreditCount;
    }

    public void setCollectorCreditCount(Long collectorCreditCount) {
        this.collectorCreditCount = collectorCreditCount;
    }

    public double getCollectorsCreditsTotalSum() {
        return collectorsCreditsTotalSum;
    }

    public void setCollectorsCreditsTotalSum(double collectorsCreditsTotalSum) {
        this.collectorsCreditsTotalSum = collectorsCreditsTotalSum;
    }

    public double getCollectorsCreditsCommonSumDebt() {
        return collectorsCreditsCommonSumDebt;
    }

    public void setCollectorsCreditsCommonSumDebt(double collectorsCreditsCommonSumDebt) {
        this.collectorsCreditsCommonSumDebt = collectorsCreditsCommonSumDebt;
    }

    public double getCollectorPenaltiesAndPercents() {
        return collectorPenaltiesAndPercents;
    }

    public void setCollectorPenaltiesAndPercents(double collectorPenaltiesAndPercents) {
        this.collectorPenaltiesAndPercents = collectorPenaltiesAndPercents;
    }

    public double getCollectorsCollectedCreditsTotalSum() {
        return collectorsCollectedCreditsTotalSum;
    }

    public void setCollectorsCollectedCreditsTotalSum(double collectorsCollectedCreditsTotalSum) {
        this.collectorsCollectedCreditsTotalSum = collectorsCollectedCreditsTotalSum;
    }

    public double getCollectorsCollectedCommonSumDebt() {
        return collectorsCollectedCommonSumDebt;
    }

    public void setCollectorsCollectedCommonSumDebt(double collectorsCollectedCommonSumDebt) {
        this.collectorsCollectedCommonSumDebt = collectorsCollectedCommonSumDebt;
    }

    public double getCollectorsCollectedPenaltiesAndPercents() {
        return collectorsCollectedPenaltiesAndPercents;
    }

    public void setCollectorsCollectedPenaltiesAndPercents(double collectorsCollectedPenaltiesAndPercents) {
        this.collectorsCollectedPenaltiesAndPercents = collectorsCollectedPenaltiesAndPercents;
    }

    public void setCollectorCreditCountSoft(long collectorCreditCountSoft) {
        this.collectorCreditCountSoft = collectorCreditCountSoft;
    }

    public long getCollectorCreditCountSoft() {
        return collectorCreditCountSoft;
    }

    public void setCollectorCreditCountHard(long collectorCreditCountHard) {
        this.collectorCreditCountHard = collectorCreditCountHard;
    }

    public long getCollectorCreditCountHard() {
        return collectorCreditCountHard;
    }

    public void setCollectorCreditCountLegal(long collectorCreditCountLegal) {
        this.collectorCreditCountLegal = collectorCreditCountLegal;
    }

    public long getCollectorCreditCountLegal() {
        return collectorCreditCountLegal;
    }

    public void setCollectorsCollectedCreditsTotalSumLegal(double collectorsCollectedCreditsTotalSumLegal) {
        this.collectorsCollectedCreditsTotalSumLegal = collectorsCollectedCreditsTotalSumLegal;
    }

    public double getCollectorsCollectedCreditsTotalSumLegal() {
        return collectorsCollectedCreditsTotalSumLegal;
    }

    public void setCollectorsCollectedCreditsTotalSumHard(double collectorsCollectedCreditsTotalSumHard) {
        this.collectorsCollectedCreditsTotalSumHard = collectorsCollectedCreditsTotalSumHard;
    }

    public double getCollectorsCollectedCreditsTotalSumHard() {
        return collectorsCollectedCreditsTotalSumHard;
    }

    public void setCollectorsCollectedCreditsTotalSumSoft(double collectorsCollectedCreditsTotalSumSoft) {
        this.collectorsCollectedCreditsTotalSumSoft = collectorsCollectedCreditsTotalSumSoft;
    }

    public double getCollectorsCollectedCreditsTotalSumSoft() {
        return collectorsCollectedCreditsTotalSumSoft;
    }

    public void setCollectorPenaltiesAndPercentsLegal(double collectorPenaltiesAndPercentsLegal) {
        this.collectorPenaltiesAndPercentsLegal = collectorPenaltiesAndPercentsLegal;
    }

    public double getCollectorPenaltiesAndPercentsLegal() {
        return collectorPenaltiesAndPercentsLegal;
    }

    public void setCollectorPenaltiesAndPercentsHard(double collectorPenaltiesAndPercentsHard) {
        this.collectorPenaltiesAndPercentsHard = collectorPenaltiesAndPercentsHard;
    }

    public double getCollectorPenaltiesAndPercentsHard() {
        return collectorPenaltiesAndPercentsHard;
    }

    public void setCollectorPenaltiesAndPercentsSoft(double collectorPenaltiesAndPercentsSoft) {
        this.collectorPenaltiesAndPercentsSoft = collectorPenaltiesAndPercentsSoft;
    }

    public double getCollectorPenaltiesAndPercentsSoft() {
        return collectorPenaltiesAndPercentsSoft;
    }

    public void setCollectorsCreditsCommonSumDebtLegal(double collectorsCreditsCommonSumDebtLegal) {
        this.collectorsCreditsCommonSumDebtLegal = collectorsCreditsCommonSumDebtLegal;
    }

    public double getCollectorsCreditsCommonSumDebtLegal() {
        return collectorsCreditsCommonSumDebtLegal;
    }

    public void setCollectorsCreditsCommonSumDebtHard(double collectorsCreditsCommonSumDebtHard) {
        this.collectorsCreditsCommonSumDebtHard = collectorsCreditsCommonSumDebtHard;
    }

    public double getCollectorsCreditsCommonSumDebtHard() {
        return collectorsCreditsCommonSumDebtHard;
    }

    public void setCollectorsCreditsCommonSumDebtSoft(double collectorsCreditsCommonSumDebtSoft) {
        this.collectorsCreditsCommonSumDebtSoft = collectorsCreditsCommonSumDebtSoft;
    }

    public double getCollectorsCreditsCommonSumDebtSoft() {
        return collectorsCreditsCommonSumDebtSoft;
    }

    public void setCollectorsCreditsTotalSumLegal(double collectorsCreditsTotalSumLegal) {
        this.collectorsCreditsTotalSumLegal = collectorsCreditsTotalSumLegal;
    }

    public double getCollectorsCreditsTotalSumLegal() {
        return collectorsCreditsTotalSumLegal;
    }

    public void setCollectorsCreditsTotalSumHard(double collectorsCreditsTotalSumHard) {
        this.collectorsCreditsTotalSumHard = collectorsCreditsTotalSumHard;
    }

    public double getCollectorsCreditsTotalSumHard() {
        return collectorsCreditsTotalSumHard;
    }

    public void setCollectorsCreditsTotalSumSoft(double collectorsCreditsTotalSumSoft) {
        this.collectorsCreditsTotalSumSoft = collectorsCreditsTotalSumSoft;
    }

    public double getCollectorsCreditsTotalSumSoft() {
        return collectorsCreditsTotalSumSoft;
    }

    public void setCollectorsCollectedCount(long collectorsCollectedCount) {
        this.collectorsCollectedCount = collectorsCollectedCount;
    }

    public long getCollectorsCollectedCount() {
        return collectorsCollectedCount;
    }
}
