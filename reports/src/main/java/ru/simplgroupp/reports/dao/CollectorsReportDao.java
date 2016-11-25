package ru.simplgroupp.reports.dao;
import java.util.Date;

/**
 *
 */
public interface CollectorsReportDao {
    public long getCollectorsCreditsCount(Date start, Date end, Integer collectorType,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorsCreditsTotalSum(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorsCreditsCommonSumDebt(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorPenaltiesAndPercents(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorsCollectedCreditsTotalSum(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorsCollectedCommonSumDebt(Date start,Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getCollectorsCollectedPenaltiesAndPercents(Date start,Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getCollectorsCollectedCount(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
