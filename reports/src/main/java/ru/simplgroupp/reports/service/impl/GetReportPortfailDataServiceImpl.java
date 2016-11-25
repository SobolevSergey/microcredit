package ru.simplgroupp.reports.service.impl;

import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.dao.ReportPortfailDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.PortfelReportModel;
import ru.simplgroupp.reports.service.GetReportPortfailDataService;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;

/**
 * возвращает модель отчета по портфелю,вывзывая нужные dao методы для каждого поля модели
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetReportPortfailDataServiceImpl implements GetReportPortfailDataService {
    @EJB
    private ReportPortfailDao reportPortfailDao;
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
    public PortfelReportModel getPortfelReportDate(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                   int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);
        PortfelReportModel portfelReportModel = new PortfelReportModel();
        portfelReportModel.setAmount(reportPortfailDao.getPortfailCount(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountInTerm(reportPortfailDao.getPortfailCountInTerm(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue(reportPortfailDao.getPortfailCountWithOverdue(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountActive(reportPortfailDao.getPortfailCountActive(start, end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue1_3(reportPortfailDao.getPortfailCountWithOverdueInterval(end, 1, 4,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue4_10(reportPortfailDao.getPortfailCountWithOverdueInterval(end, 4, 11,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue4_10(reportPortfailDao.getPortfailCountWithOverdueInterval(end,4,11,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue11_30(reportPortfailDao.getPortfailCountWithOverdueInterval(end, 11, 31,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue31_60(reportPortfailDao.getPortfailCountWithOverdueInterval(end, 31, 61,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue61_90(reportPortfailDao.getPortfailCountWithOverdueInterval(end, 61, 91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setAmountWithOverdue91_plus(reportPortfailDao.getPortfailCountWithOverduePlus(end, 91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebt(reportPortfailDao.getCommonSumDebt(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtActive(reportPortfailDao.getCommonSumDebtActive(start,end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtInTerm(reportPortfailDao.getCommonSumDebtInTerm(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue(reportPortfailDao.getCommonSumDebtWithOverdue(end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue1_3(reportPortfailDao.getCommonSumDebtWithOverdueInterval(end, 1, 4,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue4_10(reportPortfailDao.getCommonSumDebtWithOverdueInterval(end, 4, 11,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue11_30(reportPortfailDao.getCommonSumDebtWithOverdueInterval(end, 11, 31,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue31_60(reportPortfailDao.getCommonSumDebtWithOverdueInterval(end, 31, 61,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue61_90(reportPortfailDao.getCommonSumDebtWithOverdueInterval(end, 61, 91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtWithOverdue91_plus(reportPortfailDao.getCommonSumDebtWithOverduePlus(end,91,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtIncrement(reportPortfailDao.getCommonSumGrowDebt(start,end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtIncrementOneCredit(reportPortfailDao.getCommonSumGrowDebt(start,end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        portfelReportModel.setCommonDebtIncrementTwoPlusCredits(reportPortfailDao.getCommonSumGrowDebt(start,end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        return portfelReportModel;
    }
}
