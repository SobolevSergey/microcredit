package ru.simplgroupp.reports.service;

import ru.simplgroupp.reports.model.PaymentModel;
import ru.simplgroupp.reports.model.PaymentReportModel;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 17.09.15.
 */
public interface GetPaymentsDataService {
    public PaymentReportModel getPaymentStatistics(Date start,Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                   int manager,int product,String region,String city,String place,String channel,int paymentWay);

    List<PaymentModel> getPaymentsHistory(Date start, Date end) throws Exception;
}
