package ru.simplgroupp.reports.service;


import ru.simplgroupp.reports.model.PortfelReportModel;
import java.util.Date;

public interface GetReportPortfailDataService {
    public PortfelReportModel getPortfelReportDate(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                   int manager,int product,String region,String city,String place,String channel,int paymentWay);
}
