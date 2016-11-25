package ru.simplgroupp.reports.service;

import ru.simplgroupp.reports.model.ItogReportModel;

import java.util.Date;

/**
 *
 */
public interface GetItogReportDataService {
    public ItogReportModel getItogs(Date start,Date end);
}
