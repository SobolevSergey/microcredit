package ru.simplgroupp.reports.dao;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;

import java.util.Date;
import java.util.List;

/**
 * считает основные показатели для запроса по кредиту
 */
public interface CreditRequestReportDao {
    public long getRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                 int manager, int product, String region, String city, String place, String channel, int minRequests, int maxRequests);
    public long getRequestsRejectsRejectsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                               int manager,int product,String region,String city,String place,String channel);
    public long getNewClientsCount(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public long getCreditsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCreditsCommonSumDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCreditsAvgCommonSumDebt(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getAvgWeightedPercent(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getAvgTerm(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    List<CreditRequestEntity> getCreditRequestsHistoryEntities(Date start, Date end);

    List<CreditRequest> getCreditRequestsHistory(Date start, Date end) throws Exception;

    List<CreditRequestEntity> getCreditsHistoryEntities(Date start, Date end);

    List<Credit> getCreditsHistory(Date start, Date end) throws Exception;

   public double getRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getRequestsAvgSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getRequestsAvgTerm(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    long getApprovedRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getApprovedRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    long getRejectedRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    long getRejectedRequestsCountByClient(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    long getRejectedRequestsCountByClientAutomatic(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getRejectedRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);


    double getRejectedRequestsSumByClient(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getRejectedRequestsSumByClientAutomatic(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests);

    double getOverdueCredits(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay);

    double getFinishedCreditsCommonDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay);
}
