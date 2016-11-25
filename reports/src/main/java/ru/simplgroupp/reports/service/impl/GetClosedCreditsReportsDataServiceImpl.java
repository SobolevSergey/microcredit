package ru.simplgroupp.reports.service.impl;

import ru.simplgroupp.reports.dao.ClosedCreditsReportsDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.ClosedCreditReportModel;
import ru.simplgroupp.reports.service.GetClosedCreditsReportsDataService;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetClosedCreditsReportsDataServiceImpl implements GetClosedCreditsReportsDataService {
    @EJB
    private ClosedCreditsReportsDao closedCreditsReportsDao;
    /**
     *
     * @param start дата начала периода
     * @param end дата окончания периода
     * @param minAge минимальный возраст клиента
     * @param maxAge максимальный возраст клиента
     * @param gender пол клиента или пустая строка если не важно(все возможные значения находятся в справочнике см. запрос getAllGenders)
     * @param marriage семейный статус - codeinteger из справочника или -1 если не важно(см. запрос getAllMarriages)
     * @param profession профессия - codeinteger из справочника или -1 если не важно(см. запрос getAllProfessions)
     * @param minSalary минимальная зарплата клиента или -1 если не важно
     * @param maxSalary максимальная зарплата клиента
     * @param manager id менеджера(таблица users_entity) или -1 если не важно (см. запрос getAllManagers)
     * @param product id продукта или -1 если не важно(см. запрос getAllProducts)
     * @param region название региона или пустая строка если не важно. Регионы берутся из таблицы address_entity(см. запрос getAllRegions). Поле place в AddressEntity
     * @param city название города или пустая строка если не важно. Города берутся из таблицы address_entity(см. запрос getAllCities).Поле city в AddressEntity.
     * @param place название места или пустая строка если не важно. Места берутся из таблицы address_entity(см. запрос getAllPlaces).Поле place в AddressEntity.
     * @param channel канал поступления средств клиенту или пустая строка если не важно.Список возможных значений получаем в запросе getAllChannels.Фильтр смотрит на значение paramvalue самой последней записи PeopleBehaviourEntity для клиента и у которого parameter_id соотвествует записи справочника где ref_header_id 61 и codeinteger 1. Все возможные значения см. в запросе getAllChannels
     * @param paymentWay способ оплаты кредита codeinteger из справочника - есть ли платежи по данному кредиту оплаченные таким способом. Или -1 если не важно.В запросе getAllPaymentWays выбираюься все возможные значения из справочника
     * @return
     */
    @Override
    public ClosedCreditReportModel getClosedCreditReports(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                          int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);
        ClosedCreditReportModel closedCreditReportModel = new ClosedCreditReportModel();
        closedCreditReportModel.setClosedCreditsAmt(closedCreditsReportsDao.getClosedCreditsCount(start,end,0 , Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsAmtOneCredit(closedCreditsReportsDao.getClosedCreditsCount(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsAmtTwoPlusCredits(closedCreditsReportsDao.getClosedCreditsCount(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsAmtBeforeTerm(closedCreditsReportsDao.getClosedCreditsCountBeforeTerm(start,end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsAmtInTerm(closedCreditsReportsDao.getClosedCreditsCountInTerm(start, end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue1_3(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDays(start, end, 1, 4,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue4_10(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDays(start, end, 4, 11,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue11_30(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDays(start, end, 11, 31,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue31_60(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDays(start, end, 31, 61,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue61_90(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDays(start, end, 61, 91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setClosedCreditsWithOverdue91_plus(closedCreditsReportsDao.getClosedCreditsCountWithOverdueDaysPlus(start,end,91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setPlannedToCloseCreditsAmt(closedCreditsReportsDao.getPlannedToCloseCreditsCount(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setPlannedToCloseCreditsAmtOneCredit(closedCreditsReportsDao.getPlannedToCloseCreditsCount(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        closedCreditReportModel.setPlannedToCloseCreditsAmtTwoPlusCredits(closedCreditsReportsDao.getPlannedToCloseCreditsCount(start,end,2 ,Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        return closedCreditReportModel;
    }


}
