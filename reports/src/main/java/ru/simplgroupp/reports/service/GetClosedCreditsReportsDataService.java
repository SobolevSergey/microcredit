package ru.simplgroupp.reports.service;
import java.util.Date;

import ru.simplgroupp.reports.model.ClosedCreditReportModel;

/**
 *
 */
public interface GetClosedCreditsReportsDataService {
    public ClosedCreditReportModel getClosedCreditReports(Date start,Date end,  int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                          int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
