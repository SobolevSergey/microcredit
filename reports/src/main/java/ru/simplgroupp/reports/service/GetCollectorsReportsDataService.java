package ru.simplgroupp.reports.service;
import ru.simplgroupp.reports.model.CollectorsReportModel;

import java.util.Date;
/**
 *
 */
public interface GetCollectorsReportsDataService {
    public CollectorsReportModel getCollectorsReportData(Date start,Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                         int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
