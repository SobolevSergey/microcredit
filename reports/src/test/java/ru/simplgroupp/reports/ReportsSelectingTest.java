package ru.simplgroupp.reports;


import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.service.*;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static junit.framework.Assert.assertNotNull;

public class ReportsSelectingTest {
    private static final Logger logger = Logger.getLogger(ReportsSelectingTest.class.getName());
    @Inject
    private GetClosedCreditsReportsDataService getClosedCreditsReportsDataService;
    @Inject
    private GetCollectorsReportsDataService getCollectorsReportsDataService;
    @Inject
    private GetReportsDataService getReportDataService;
    @Inject
    private CreditRequestReportDao creditRequestReportDao;
    @Inject
    private GetPaymentsDataService getPaymentsDataService;
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager em;
    private Context context;
    @Inject
    private GetProlongationsReportDataService getProlongationsReportDataService;
    @Inject
    private GetReportPortfailDataService getReportPortfailDataService;
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    @Test
    public void testPrintCreditRequest() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        /*logger.info("totalCount: "+creditRequestReportDao.getRequestsCount(start,end));
        logger.info("rejects count: "+creditRequestReportDao.getRequestsRejectsRejectsCount(start, end));
        logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end));
        logger.info("new credits count: "+creditRequestReportDao.getCreditsCount(start, end));
        logger.info("common debt credits: "+creditRequestReportDao.getCreditsCommonSumDebt(start, end));
        logger.info("common avg debt credits: "+creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end));
        logger.info("weighted avg percent: "+creditRequestReportDao.getAvgWeightedPercent(start, end));
        logger.info("weighted avg term: "+creditRequestReportDao.getAvgTerm(start, end));*/
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -18, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-100, Calendar.YEAR);
        //logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1));
        //logger.info(printClassFields(getReportDataService.getCreditsStatistics(start, end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));


    }
   // @Test
    public void testPrintPayments() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        logger.info(printClassFields(getPaymentsDataService.getPaymentStatistics(start, end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));


    }
    ///@Test
    public void testPrintProlongations() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        /*logger.info("totalCount: "+creditRequestReportDao.getRequestsCount(start,end));
        logger.info("rejects count: "+creditRequestReportDao.getRequestsRejectsRejectsCount(start, end));
        logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end));
        logger.info("new credits count: "+creditRequestReportDao.getCreditsCount(start, end));
        logger.info("common debt credits: "+creditRequestReportDao.getCreditsCommonSumDebt(start, end));
        logger.info("common avg debt credits: "+creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end));
        logger.info("weighted avg percent: "+creditRequestReportDao.getAvgWeightedPercent(start, end));
        logger.info("weighted avg term: "+creditRequestReportDao.getAvgTerm(start, end));*/
        logger.info(printClassFields(getProlongationsReportDataService.getProlongationsReportData(start, end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));


    }
    //@Test
    public void testPrintClosedReports() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        /*logger.info("totalCount: "+creditRequestReportDao.getRequestsCount(start,end));
        logger.info("rejects count: "+creditRequestReportDao.getRequestsRejectsRejectsCount(start, end));
        logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end));
        logger.info("new credits count: "+creditRequestReportDao.getCreditsCount(start, end));
        logger.info("common debt credits: "+creditRequestReportDao.getCreditsCommonSumDebt(start, end));
        logger.info("common avg debt credits: "+creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end));
        logger.info("weighted avg percent: "+creditRequestReportDao.getAvgWeightedPercent(start, end));
        logger.info("weighted avg term: "+creditRequestReportDao.getAvgTerm(start, end));*/
        logger.info(printClassFields(getClosedCreditsReportsDataService.getClosedCreditReports(start, end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));
        //logger.info((getClosedCreditsReportsDataService.getClosedCreditReports(start, end).getPlannedToCloseCreditsAmt())+"");


    }
     //@Test
    public void testPrintPortfail() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        /*logger.info("totalCount: "+creditRequestReportDao.getRequestsCount(start,end));
        logger.info("rejects count: "+creditRequestReportDao.getRequestsRejectsRejectsCount(start, end));
        logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end));
        logger.info("new credits count: "+creditRequestReportDao.getCreditsCount(start, end));
        logger.info("common debt credits: "+creditRequestReportDao.getCreditsCommonSumDebt(start, end));
        logger.info("common avg debt credits: "+creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end));
        logger.info("weighted avg percent: "+creditRequestReportDao.getAvgWeightedPercent(start, end));
        logger.info("weighted avg term: "+creditRequestReportDao.getAvgTerm(start, end));*/
        logger.info(printClassFields(getReportPortfailDataService.getPortfelReportDate(start,end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));
        //logger.info((getClosedCreditsReportsDataService.getClosedCreditReports(start, end).getPlannedToCloseCreditsAmt())+"");


    }
    //@Test
    public void testPrintCollectors() throws ParseException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date start = simpleDateFormat.parse("01-01-2015 00:00");
        Date end = simpleDateFormat.parse("01-12-2015 00:00");
        /*logger.info("totalCount: "+creditRequestReportDao.getRequestsCount(start,end));
        logger.info("rejects count: "+creditRequestReportDao.getRequestsRejectsRejectsCount(start, end));
        logger.info("new clients count: "+creditRequestReportDao.getNewClientsCount(start, end));
        logger.info("new credits count: "+creditRequestReportDao.getCreditsCount(start, end));
        logger.info("common debt credits: "+creditRequestReportDao.getCreditsCommonSumDebt(start, end));
        logger.info("common avg debt credits: "+creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end));
        logger.info("weighted avg percent: "+creditRequestReportDao.getAvgWeightedPercent(start, end));
        logger.info("weighted avg term: "+creditRequestReportDao.getAvgTerm(start, end));*/
        logger.info(printClassFields(getCollectorsReportsDataService.getCollectorsReportData(start,end,0,150,"",-1,-1,-1,-1,-1,-1,"","","","",-1)));
        //logger.info((getClosedCreditsReportsDataService.getClosedCreditReports(start, end).getPlannedToCloseCreditsAmt())+"");


    }
    private String printClassFields(Object object) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(object);
            result.append(String.format("Field name: %s, Field value: %s%n", name, value));
        }
        return result.toString();
    }
}

