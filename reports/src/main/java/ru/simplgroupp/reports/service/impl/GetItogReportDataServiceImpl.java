package ru.simplgroupp.reports.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.dao.PaymentReportDao;
import ru.simplgroupp.reports.dao.ReportPortfailDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.ItogReportModel;
import ru.simplgroupp.reports.service.GetItogReportDataService;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;

/**
 * сервис, заполняющий модель отчетов по итогам
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetItogReportDataServiceImpl implements GetItogReportDataService{
    private static final Logger logger = LoggerFactory.getLogger(GetItogReportDataService.class);
    @EJB
    private CreditRequestReportDao creditRequestReportDao;
    @EJB
    private ReportPortfailDao portfelReportDao;
    @EJB
    private PaymentReportDao paymentReportDao;
    @Override
    public ItogReportModel getItogs(Date start, Date end) {
        Date now = new Date();
        int maxAge = 200;//для фильтра по дате рождения выставляем наиболее максимальный с запасом интервал возраста
        int minAge = 0;
        Date minBirthDate = DateUtil.getDateMinusInterval(now,-maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);
        ItogReportModel itogReportModel = new ItogReportModel();
        logger.error("getting total requests num");
        itogReportModel.setTotalRequestsNum(creditRequestReportDao.getRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1 , -1));
        logger.error("getting requests sum");
        itogReportModel.setTotalRequestsSum(creditRequestReportDao.getRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1,-1,-1));
        logger.error("getting first requests num");
        itogReportModel.setFirstRequestsNum(creditRequestReportDao.getRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",1,1));
        logger.error("getting first requests sum");
        itogReportModel.setFirstRequestsSum(creditRequestReportDao.getRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 1,1 ));
        logger.error("getting second requests num");
        itogReportModel.setSecondRequestsNum(creditRequestReportDao.getRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",2,Integer.MAX_VALUE));
        logger.error("getting second requests sum");
        itogReportModel.setSecondRequestsSum(creditRequestReportDao.getRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 2,Integer.MAX_VALUE ));
        logger.error("getting portfel data");
        itogReportModel.setPortfelNumBegPeriod(portfelReportDao.getPortfailCount(start,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setPortfelSumBegPeriod(portfelReportDao.getCommonSumDebt(start,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));

        Long portfelCountEnd = portfelReportDao.getPortfailCount(end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1);
        Double portfelSumEnd = portfelReportDao.getCommonSumDebt(end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1);

        itogReportModel.setPortfelNumGrow(portfelCountEnd - itogReportModel.getPortfelNumBegPeriod());
        itogReportModel.setPortfelSumGrow(portfelSumEnd - itogReportModel.getPortfelSumBegPeriod());
        logger.error("getting avg requestsTerm");
        itogReportModel.setAvgRequestTerm(creditRequestReportDao.getRequestsAvgTerm(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        logger.error("getting avg requests first");
        itogReportModel.setAvgSumFirstRequests(creditRequestReportDao.getRequestsAvgSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1,1,1));
        logger.error("getting requests second");
        itogReportModel.setAvgSumSecondRequests(creditRequestReportDao.getRequestsAvgSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 2,2));
        logger.error("getting approved requests number");
        itogReportModel.setApprovedRequests(creditRequestReportDao.getApprovedRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        logger.error("getting approved requests sum");
        itogReportModel.setApprovedRequestsSum(creditRequestReportDao.getApprovedRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        logger.error("getting approved requests first");
        itogReportModel.setApprovedRequestsFirst(creditRequestReportDao.getApprovedRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 1,1));
        logger.error("getting approved requests sum first");
        itogReportModel.setApprovedRequestsSumFirst(creditRequestReportDao.getApprovedRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 1,1));
        logger.error("getting approved requests num other");
        itogReportModel.setApprovedRequestsOther(creditRequestReportDao.getApprovedRequestsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 2,Integer.MAX_VALUE));
        logger.error("getting approved requests sum other");
        itogReportModel.setApprovedRequestsSumOther(creditRequestReportDao.getApprovedRequestsSum(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 2,Integer.MAX_VALUE));
        logger.error("getting approved requests num");
        itogReportModel.setClientRejectsNum(creditRequestReportDao.getRejectedRequestsCountByClient(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        logger.error("getting clients rejects");
        itogReportModel.setClientRejectsSum(creditRequestReportDao.getRejectedRequestsSumByClient(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        itogReportModel.setOverdueFirst(creditRequestReportDao.getOverdueCredits(start,end,1,1,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setOverdueOthers(creditRequestReportDao.getOverdueCredits(start,end,2,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setReturnedCommonDebt(paymentReportDao.getPayedSumFactCommonDebt(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1,0 , Integer.MAX_VALUE));
        itogReportModel.setReturnedPenalties(paymentReportDao.getPayedSumFactPenalties(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setReturnedPercents(paymentReportDao.getPayedSumFactPercents(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        double plannedToPayCommonDebt = paymentReportDao.getPlannedToPayASumCommonDebt(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 0,Integer.MAX_VALUE);//сумма од кредитов планируемых к погашению в данный период
        if (plannedToPayCommonDebt != 0) {
            itogReportModel.setPercentsToGive(Math.max((((creditRequestReportDao.getOverdueCredits(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1)) / plannedToPayCommonDebt) * 100),0));//% к выдаче считаем следующим образом (планируется к погашению - погашено)/планируется к погашению)*100%. При этом если сумма погашенных кредитов оказалось больше чем сумма планируемая к погашению в данном периоде может дать отрицательный процент, это тоже учитывается и в этом случае процент ставится как 0

        }
        plannedToPayCommonDebt = paymentReportDao.getPlannedToPayASumCommonDebt(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 1,1);
        if (plannedToPayCommonDebt != 0) {
            itogReportModel.setPercentsToGiveFirst(Math.max((((itogReportModel.getOverdueFirst()) / plannedToPayCommonDebt) * 100),0));
        }
        plannedToPayCommonDebt = paymentReportDao.getPlannedToPayASumCommonDebt(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, 2,Integer.MAX_VALUE);
        if (plannedToPayCommonDebt != 0) {
            itogReportModel.setPercentsToGiveOthers(Math.max((((itogReportModel.getOverdueOthers()) / plannedToPayCommonDebt) * 100),0));
        }
        itogReportModel.setClosedPrevOverdue(paymentReportDao.getClosedOverduePrevPeriods(start,end));
        itogReportModel.setPlannedToPaySum(paymentReportDao.getPlannedToPayASumCommonDebt(start,end,minBirthDate,maxBirthDate,"",-1,-1,-1,-1.-1,-1,-1,"","","","",-1,0,Integer.MAX_VALUE));
        itogReportModel.setNewCreditsCount(creditRequestReportDao.getCreditsCount(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setNewCreditsSum(creditRequestReportDao.getCreditsCommonSumDebt(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1));
        itogReportModel.setClientRejectsNumAutomatic(creditRequestReportDao.getRejectedRequestsCountByClientAutomatic(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        itogReportModel.setClientRejectsSumAutomatic(creditRequestReportDao.getRejectedRequestsSumByClientAutomatic(start,end,0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,"",-1,-1,-1,-1,-1,-1,"","","","",-1, -1,-1));
        return itogReportModel;
    }
}
