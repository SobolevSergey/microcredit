package ru.simplgroupp.reports.service;

import ru.simplgroupp.reports.model.CreditModel;
import ru.simplgroupp.reports.model.CreditReportModel;
import ru.simplgroupp.reports.model.CreditRequestModel;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface  GetReportsDataService {
    public CreditReportModel getCreditsStatistics(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                  int manager,int product,String region,String city,String place,String channel,int paymentWay);

    List<CreditRequestModel> getCreditRequestHistory(Date start, Date end) throws Exception;

    List<CreditModel> getCreditsHistory(Date start, Date end) throws Exception;
}
