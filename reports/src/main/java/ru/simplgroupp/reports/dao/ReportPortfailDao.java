package ru.simplgroupp.reports.dao;
import java.util.Date;
/**
 *
 */
public interface ReportPortfailDao {
    public long getPortfailCount(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountActive(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountInTerm(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountWithOverdue(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountWithOverdueInterval(Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountWithOverduePlus(Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    public double getCommonSumDebt(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getCommonSumDebtActive(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getCommonSumDebtInTerm(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getCommonSumDebtWithOverdue(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getCommonSumDebtWithOverdueInterval(Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    double getCommonSumDebtWithOverduePlus(Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getPortfailCountGrow(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay);

    double getPortfailSumGrow(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay);

    double getCommonSumGrowDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay);
}
