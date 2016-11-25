package ru.simplgroupp.reports.dao;

import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.transfer.Payment;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface PaymentReportDao {
    public long getPlannedToPayAmt(Date start, Date end, int minUserCredits, int maxUserCredits,  Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getPlannedToPayASum(Date start,Date end,  Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPlannedToPayASumCommonDebt(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minCredits,int maxCredits);

    double getPlannedToPayASumPenalties(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    public long getPayedAmtFact(Date start, Date end, int minUserCredits, int maxUserCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getPayedSumFact(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPayedSumFactBeforeTerm(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPayedSumFactInTerm(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);


    double getPayedSumFactOverdueDays(Date start, Date end, long minDays, long maxDays, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPayedSumFactOverdueDays(Date start, Date end, long minDays, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPayedSumFactCommonDebt(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minCredits, int maxCredits);

    double getPayedSumFactPercents(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getPayedSumFactPenalties(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getClosedOverduePrevPeriods(Date start, Date end);

    List<PaymentEntity> getPaymentsHistoryEntities(Date start, Date end);

    List<Payment> getPaymentsHistory(Date start, Date end) throws Exception;
}
