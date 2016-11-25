package ru.simplgroupp.reports.dao;
import java.util.Date;
/**
 *
 */
public interface PortfailReportDao {
    public long getPortfailCount(Date end);
    public double getPortfailSumCommonDebt(Date end);
    public double getPortfailCommonSumDebtGrov(Date end);
}
