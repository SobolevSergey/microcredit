package ru.simplgroupp.reports.dao;
import java.util.Date;

/**
 *
 */
public interface ClosedCreditsReportsDao {
    public long getPlannedToCloseCreditsCount(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public long getClosedCreditsCount(Date start, Date end, int minCreditsCount, int maxCreditsCount,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getClosedCreditsCountInTerm(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getClosedCreditsCountBeforeTerm(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getClosedCreditsCountWithOverdueDays(Date start, Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);

    long getClosedCreditsCountWithOverdueDaysPlus(Date start, Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
