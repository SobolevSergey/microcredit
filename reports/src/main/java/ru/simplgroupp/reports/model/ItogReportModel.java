package ru.simplgroupp.reports.model;

/**
 * модель по отчету по итогам
 */
public class ItogReportModel {
    /**
     * портфель займов на начало периода - кол-во
     */
    private long portfelNumBegPeriod;
    /**
     * портфель займов на начало периода - сумма од
     */
    private double portfelSumBegPeriod;
    /**
     * прирост портфеля - количество
     */
    private long portfelNumGrow;
    /**
     * прирост портфеля - сумма од
     */
    private double portfelSumGrow;
    /**
     * заявки - количество
     */
    private long totalRequestsNum;
    /**
     * заявки - сумма од
     */
    private double totalRequestsSum;
    /**
     * заявки - первые кол-во
     */
    private long firstRequestsNum;
    /**
     * заявки - первые сумма
     */
    private double firstRequestsSum;
    /**
     * заявки - остальные -кол-во
     */
    private long secondRequestsNum;
    /**
     * заявки - остальные -сумма од
     */
    private double secondRequestsSum;
    /**
     * заявки - первые средняя сумма
     */
    private double avgSumFirstRequests;
    /**
     * заявки - вторые средняя сумма
     */
    private double avgSumSecondRequests;
    /**
     * заявки средний срок
     */
    private double avgRequestTerm;
    /**
     * одобрено заявок
     */
    private long approvedRequests;
    /**
     * одобрено заявок первые
     */
    private long approvedRequestsFirst;
    /**
     * одобрено заявок остальные
     */
    private long approvedRequestsOther;
    /**
     * отказы клиентов
     */
    private long clientRejectsNum;
    /**
     * просрочка первые
     */
    private double overdueFirst;
    /**
     * просрочка остальные
     */
    private double overdueOthers;
    /**
     * % к выдаче
     */
    private double percentsToGive;
    /**
     * % к выдаче остальные
     */
    private double percentsToGiveOthers;
    /**
     * % к выдаче первые
     */
    private double percentsToGiveFirst;
    /**
     * закрытая просрочка прошлых периодов
     */
    private double closedPrevOverdue;
    /**
     * возврат од
     */
    private double returnedCommonDebt;
    /**
     * возврат проценты
     */
    private double returnedPercents;
    /**
     * возврат штрафы
     */
    private double returnedPenalties;
    /**
     * одобрено заявок - сумма од
     */
    private double approvedRequestsSum;
    /**
     * одобрено заявок - сумма од -первые заявки
     */
    private double approvedRequestsSumFirst;
    /**
     * одобрено заявок - сумма од -остальные
     */
    private double approvedRequestsSumOther;
    /**
     * отказы клиента - сумма од
     */
    private double clientRejectsSum;
    /**
     * день - если отчет по итогам выводится по всем дням периода, выставляется напримре в jsf-контроллере после заполнения
     * модели
     */
    private String day;
    private double plannedToPaySum;
    private long newCreditsCount;
    private double newCreditsSum;
    private long clientRejectsNumAutomatic;
    private double clientRejectsSumAutomatic;

    public double getPlannedToPaySum() {
        return plannedToPaySum;
    }

    public void setPlannedToPaySum(double plannedToPaySum) {
        this.plannedToPaySum = plannedToPaySum;
    }

    public long getNewCreditsCount() {
        return newCreditsCount;
    }

    public void setNewCreditsCount(long newCreditsCount) {
        this.newCreditsCount = newCreditsCount;
    }

    public double getNewCreditsSum() {
        return newCreditsSum;
    }

    public void setNewCreditsSum(double newCreditsSum) {
        this.newCreditsSum = newCreditsSum;
    }

    public long getClientRejectsNumAutomatic() {
        return clientRejectsNumAutomatic;
    }

    public void setClientRejectsNumAutomatic(long clientRejectsNumAutomatic) {
        this.clientRejectsNumAutomatic = clientRejectsNumAutomatic;
    }

    public double getClientRejectsSumAutomatic() {
        return clientRejectsSumAutomatic;
    }

    public void setClientRejectsSumAutomatic(double clientRejectsSumAutomatic) {
        this.clientRejectsSumAutomatic = clientRejectsSumAutomatic;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getApprovedRequestsSum() {
        return approvedRequestsSum;
    }

    public void setApprovedRequestsSum(double approvedRequestsSum) {
        this.approvedRequestsSum = approvedRequestsSum;
    }

    public double getApprovedRequestsSumFirst() {
        return approvedRequestsSumFirst;
    }

    public void setApprovedRequestsSumFirst(double approvedRequestsSumFirst) {
        this.approvedRequestsSumFirst = approvedRequestsSumFirst;
    }

    public double getApprovedRequestsSumOther() {
        return approvedRequestsSumOther;
    }

    public void setApprovedRequestsSumOther(double approvedRequestsSumOther) {
        this.approvedRequestsSumOther = approvedRequestsSumOther;
    }

    public double getClientRejectsSum() {
        return clientRejectsSum;
    }

    public void setClientRejectsSum(double clientRejectsSum) {
        this.clientRejectsSum = clientRejectsSum;
    }

    public long getPortfelNumBegPeriod() {
        return portfelNumBegPeriod;
    }

    public void setPortfelNumBegPeriod(long portfelNumBegPeriod) {
        this.portfelNumBegPeriod = portfelNumBegPeriod;
    }

    public double getPortfelSumBegPeriod() {
        return portfelSumBegPeriod;
    }

    public void setPortfelSumBegPeriod(double portfelSumBegPeriod) {
        this.portfelSumBegPeriod = portfelSumBegPeriod;
    }

    public double getPortfelSumGrow() {
        return portfelSumGrow;
    }

    public void setPortfelSumGrow(double portfelSumGrow) {
        this.portfelSumGrow = portfelSumGrow;
    }

    public long getPortfelNumGrow() {
        return portfelNumGrow;
    }

    public void setPortfelNumGrow(long portfelNumGrow) {
        this.portfelNumGrow = portfelNumGrow;
    }

    public long getTotalRequestsNum() {
        return totalRequestsNum;
    }

    public void setTotalRequestsNum(long totalRequestsNum) {
        this.totalRequestsNum = totalRequestsNum;
    }

    public double getTotalRequestsSum() {
        return totalRequestsSum;
    }

    public void setTotalRequestsSum(double totalRequestsSum) {
        this.totalRequestsSum = totalRequestsSum;
    }

    public long getFirstRequestsNum() {
        return firstRequestsNum;
    }

    public void setFirstRequestsNum(long firstRequestsNum) {
        this.firstRequestsNum = firstRequestsNum;
    }

    public long getSecondRequestsNum() {
        return secondRequestsNum;
    }

    public void setSecondRequestsNum(long secondRequestsNum) {
        this.secondRequestsNum = secondRequestsNum;
    }

    public double getFirstRequestsSum() {
        return firstRequestsSum;
    }

    public void setFirstRequestsSum(double firstRequestsSum) {
        this.firstRequestsSum = firstRequestsSum;
    }

    public double getSecondRequestsSum() {
        return secondRequestsSum;
    }

    public void setSecondRequestsSum(double secondRequestsSum) {
        this.secondRequestsSum = secondRequestsSum;
    }

    public double getAvgSumFirstRequests() {
        return avgSumFirstRequests;
    }

    public void setAvgSumFirstRequests(double avgSumFirstRequests) {
        this.avgSumFirstRequests = avgSumFirstRequests;
    }

    public double getAvgSumSecondRequests() {
        return avgSumSecondRequests;
    }

    public void setAvgSumSecondRequests(double avgSumSecondRequests) {
        this.avgSumSecondRequests = avgSumSecondRequests;
    }

    public double getAvgRequestTerm() {
        return avgRequestTerm;
    }

    public void setAvgRequestTerm(double avgRequestTerm) {
        this.avgRequestTerm = avgRequestTerm;
    }

    public long getApprovedRequests() {
        return approvedRequests;
    }

    public void setApprovedRequests(long approvedRequests) {
        this.approvedRequests = approvedRequests;
    }

    public long getApprovedRequestsFirst() {
        return approvedRequestsFirst;
    }

    public void setApprovedRequestsFirst(long approvedRequestsFirst) {
        this.approvedRequestsFirst = approvedRequestsFirst;
    }

    public long getApprovedRequestsOther() {
        return approvedRequestsOther;
    }

    public void setApprovedRequestsOther(long approvedRequestsOther) {
        this.approvedRequestsOther = approvedRequestsOther;
    }

    public long getClientRejectsNum() {
        return clientRejectsNum;
    }

    public void setClientRejectsNum(long clientRejectsNum) {
        this.clientRejectsNum = clientRejectsNum;
    }

    public double getOverdueFirst() {
        return overdueFirst;
    }

    public void setOverdueFirst(double overdueFirst) {
        this.overdueFirst = overdueFirst;
    }

    public double getOverdueOthers() {
        return overdueOthers;
    }

    public void setOverdueOthers(double overdueOthers) {
        this.overdueOthers = overdueOthers;
    }

    public double getPercentsToGive() {
        return percentsToGive;
    }

    public void setPercentsToGive(double percentsToGive) {
        this.percentsToGive = percentsToGive;
    }

    public double getPercentsToGiveOthers() {
        return percentsToGiveOthers;
    }

    public void setPercentsToGiveOthers(double percentsToGiveOthers) {
        this.percentsToGiveOthers = percentsToGiveOthers;
    }

    public double getPercentsToGiveFirst() {
        return percentsToGiveFirst;
    }

    public void setPercentsToGiveFirst(double percentsToGiveFirst) {
        this.percentsToGiveFirst = percentsToGiveFirst;
    }

    public double getClosedPrevOverdue() {
        return closedPrevOverdue;
    }

    public void setClosedPrevOverdue(double closedPrevOverdue) {
        this.closedPrevOverdue = closedPrevOverdue;
    }

    public double getReturnedCommonDebt() {
        return returnedCommonDebt;
    }

    public void setReturnedCommonDebt(double returnedCommonDebt) {
        this.returnedCommonDebt = returnedCommonDebt;
    }

    public double getReturnedPercents() {
        return returnedPercents;
    }

    public void setReturnedPercents(double returnedPercents) {
        this.returnedPercents = returnedPercents;
    }

    public double getReturnedPenalties() {
        return returnedPenalties;
    }

    public void setReturnedPenalties(double returnedPenalties) {
        this.returnedPenalties = returnedPenalties;
    }
}
