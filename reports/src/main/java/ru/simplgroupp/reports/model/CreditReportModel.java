package ru.simplgroupp.reports.model;

import java.util.Date;

/**
 * модель отчета выдачи
 * Содержит данные по заявкам и кредитам
 */
public class CreditReportModel {
    /**
     * количество заявок за отчетный период
     */
    private Long requestsNumber;
    /**
     * количество отказов по заявкам за отчетный период
     */
    private Long rejectsNumber;
    /**
     * процент новых клиентов
     */
    private String newClientsPercent;
    /**
     * всего новых кредитов за отчетный период
     */
    private Long totalCredits;
    /**
     * основной долг новых кредитов за отчетный период
     */
    private Double totalDebt;
    /**
     * средний основной долг новых кредитов за отчетный период
     */
    private Double avgTotalDebt;
    /**
     * средневзвешенная ставка новых кредитов за отчетный период
     */
    private Double weightedAvgRate;
    /**
     * средний период в днях новых кредитов за отчетный период
     */
    private Double avgPeriod;
    /**
     * число заявок - клиенты у которых 2 и более кредитов
     */
    private long requestsNumberTwoPlusCredits;
    /**
     * число заявок - клиенты у которых 1 кредит
     */
    private long requestsNumberOneCredit;
    /**
     * число отказов - клиенты у которых 2 и более  кредитов
     */
    private long rejectsNumberTwoPlusCredits;
    /**
     * число отказов - клиенты у которых 1 кредит
     */
    private long rejectsNumberOneCredit;
    /**
     * процент отказов - клиенты у которых 1 кредит
     */
    private double rejectsPercentOneCredit;
    /**
     * процент отказов - клиенты у которых 2+ кредитов
     */
    private double rejectsPercentTwoPlusCredits;
    /**
     * число новых кредитов - клиенты у которых 1 кредит
     */
    private long totalCreditsOneCredit;
    /**
     * число новых кредитов - клиенты у которых 2+ кредитов
     */
    private long totalCreditsTwoPlusCredits;
    /**
     * од новых кредиов - клиенты у которых 1 кредит
     */
    private double totalDebtOneCredit;
    /**
     * од новых кредиов - клиенты у которых 2+ кредитов
     */
    private double totalDebtTwoPlusCredits;
    /**
     *средний од новых кредиов - клиенты у которых 1 кредит
     */
    private double avgTotalDebtOneCredit;
    /**
     *средний од новых кредиов - клиенты у которых 2+ кредитов
     */
    private double avgTotalDebtTwoPlusCredits;
    /**
     *средневзвешенная ставка новых кредитов - клиенты у которых 1 кредит
     */
    private double weightedAvgRateOneCredit;
    /**
     *средневзвешенная ставка новых кредитов - клиенты у которых 2+ кредитов
     */
    private double weightedAvgRateTwoPlusCredits;

    public Long getRequestsNumber() {
        return requestsNumber;
    }

    public void setRequestsNumber(Long requestsNumber) {
        this.requestsNumber = requestsNumber;
    }

    public Long getRejectsNumber() {
        return rejectsNumber;
    }

    public void setRejectsNumber(Long rejectsNumber) {
        this.rejectsNumber = rejectsNumber;
    }

    public String getNewClientsPercent() {
        return newClientsPercent;
    }

    public void setNewClientsPercent(String newClientsPercent) {
        this.newClientsPercent = newClientsPercent;
    }

    public Long getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Long totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(Double totalDebt) {
        this.totalDebt = totalDebt;
    }

    public Double getAvgTotalDebt() {
        return avgTotalDebt;
    }

    public void setAvgTotalDebt(Double avgTotalDebt) {
        this.avgTotalDebt = avgTotalDebt;
    }

    public Double getWeightedAvgRate() {
        return weightedAvgRate;
    }

    public void setWeightedAvgRate(Double weightedAvgRate) {
        this.weightedAvgRate = weightedAvgRate;
    }

    public Double getAvgPeriod() {
        return avgPeriod;
    }

    public void setAvgPeriod(Double avgPeriod) {
        this.avgPeriod = avgPeriod;
    }

    public void setRequestsNumberTwoPlusCredits(long requestsNumberTwoPlusCredits) {
        this.requestsNumberTwoPlusCredits = requestsNumberTwoPlusCredits;
    }

    public long getRequestsNumberTwoPlusCredits() {
        return requestsNumberTwoPlusCredits;
    }

    public void setRequestsNumberOneCredit(long requestsNumberOneCredit) {
        this.requestsNumberOneCredit = requestsNumberOneCredit;
    }

    public long getRequestsNumberOneCredit() {
        return requestsNumberOneCredit;
    }

    public void setRejectsNumberTwoPlusCredits(long rejectsNumberTwoPlusCredits) {
        this.rejectsNumberTwoPlusCredits = rejectsNumberTwoPlusCredits;
    }

    public long getRejectsNumberTwoPlusCredits() {
        return rejectsNumberTwoPlusCredits;
    }

    public void setRejectsNumberOneCredit(long rejectsNumberOneCredit) {
        this.rejectsNumberOneCredit = rejectsNumberOneCredit;
    }

    public long getRejectsNumberOneCredit() {
        return rejectsNumberOneCredit;
    }



    public double getRejectsPercentOneCredit() {
        return rejectsPercentOneCredit;
    }

    public void setRejectsPercentOneCredit(double rejectsPercentOneCredit) {
        this.rejectsPercentOneCredit = rejectsPercentOneCredit;
    }

    public double getRejectsPercentTwoPlusCredits() {
        return rejectsPercentTwoPlusCredits;
    }

    public void setRejectsPercentTwoPlusCredits(double rejectsPercentTwoPlusCredits) {
        this.rejectsPercentTwoPlusCredits = rejectsPercentTwoPlusCredits;
    }





    public void setTotalCreditsOneCredit(long totalCreditsOneCredit) {
        this.totalCreditsOneCredit = totalCreditsOneCredit;
    }

    public long getTotalCreditsOneCredit() {
        return totalCreditsOneCredit;
    }

    public void setTotalCreditsTwoPlusCredits(long totalCreditsTwoPlusCredits) {
        this.totalCreditsTwoPlusCredits = totalCreditsTwoPlusCredits;
    }

    public long getTotalCreditsTwoPlusCredits() {
        return totalCreditsTwoPlusCredits;
    }

    public void setTotalDebtOneCredit(double totalDebtOneCredit) {
        this.totalDebtOneCredit = totalDebtOneCredit;
    }

    public double getTotalDebtOneCredit() {
        return totalDebtOneCredit;
    }

    public void setTotalDebtTwoPlusCredits(double totalDebtTwoPlusCredits) {
        this.totalDebtTwoPlusCredits = totalDebtTwoPlusCredits;
    }

    public double getTotalDebtTwoPlusCredits() {
        return totalDebtTwoPlusCredits;
    }

    public void setAvgTotalDebtOneCredit(double avgTotalDebtOneCredit) {
        this.avgTotalDebtOneCredit = avgTotalDebtOneCredit;
    }

    public double getAvgTotalDebtOneCredit() {
        return avgTotalDebtOneCredit;
    }

    public void setAvgTotalDebtTwoPlusCredits(double avgTotalDebtTwoPlusCredits) {
        this.avgTotalDebtTwoPlusCredits = avgTotalDebtTwoPlusCredits;
    }

    public double getAvgTotalDebtTwoPlusCredits() {
        return avgTotalDebtTwoPlusCredits;
    }

    public void setWeightedAvgRateOneCredit(double weightedAvgRateOneCredit) {
        this.weightedAvgRateOneCredit = weightedAvgRateOneCredit;
    }

    public double getWeightedAvgRateOneCredit() {
        return weightedAvgRateOneCredit;
    }

    public void setWeightedAvgRateTwoPlusCredits(double weightedAvgRateTwoPlusCredits) {
        this.weightedAvgRateTwoPlusCredits = weightedAvgRateTwoPlusCredits;
    }

    public double getWeightedAvgRateTwoPlusCredits() {
        return weightedAvgRateTwoPlusCredits;
    }
}
