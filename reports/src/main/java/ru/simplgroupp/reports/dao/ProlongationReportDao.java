package ru.simplgroupp.reports.dao;

import java.util.Date;

/**
 *
 */
public interface ProlongationReportDao {
   public long getProlongationsAmt(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
   public long getProlongationsCreditAmt(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getProlongationsCreditAvgSum(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getProlongationsAvgTerm(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
    public double getProlongationsAvgPayment(Date start,Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
