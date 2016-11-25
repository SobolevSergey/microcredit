package ru.simplgroupp.reports.service.impl;

import ru.simplgroupp.reports.dao.CollectorsReportDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.CollectorsReportModel;
import ru.simplgroupp.reports.service.GetCollectorsReportsDataService;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;

/**
 * сервис выдающий модель отчета по взысканиям, заполняет модель, вызывая соответствующие методы dao
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetCollectorsReportDataServiceImpl implements GetCollectorsReportsDataService {
    @EJB
    private CollectorsReportDao collectorsReportDao;
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
    public CollectorsReportModel getCollectorsReportData(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                         int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);
        CollectorsReportModel collectorsReportModel = new CollectorsReportModel();
        collectorsReportModel.setCollectorCreditCount(collectorsReportDao.getCollectorsCreditsCount(start,end,null,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorCreditCountSoft(collectorsReportDao.getCollectorsCreditsCount(start, end, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorCreditCountHard(collectorsReportDao.getCollectorsCreditsCount(start, end, 2,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorCreditCountLegal(collectorsReportDao.getCollectorsCreditsCount(start, end, 3,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));

        collectorsReportModel.setCollectorsCreditsTotalSum(collectorsReportDao.getCollectorsCreditsTotalSum(start, end, null,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsTotalSumSoft(collectorsReportDao.getCollectorsCreditsTotalSum(start, end, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsTotalSumHard(collectorsReportDao.getCollectorsCreditsTotalSum(start, end, 2,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsTotalSumLegal(collectorsReportDao.getCollectorsCreditsTotalSum(start, end, 3,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsCommonSumDebt(collectorsReportDao.getCollectorsCreditsCommonSumDebt(start, end, null,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsCommonSumDebtSoft(collectorsReportDao.getCollectorsCreditsCommonSumDebt(start, end, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsCommonSumDebtHard(collectorsReportDao.getCollectorsCreditsCommonSumDebt(start, end, 2,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCreditsCommonSumDebtLegal(collectorsReportDao.getCollectorsCreditsCommonSumDebt(start, end, 3,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorPenaltiesAndPercents(collectorsReportDao.getCollectorPenaltiesAndPercents(start, end, null,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorPenaltiesAndPercentsSoft(collectorsReportDao.getCollectorPenaltiesAndPercents(start, end, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorPenaltiesAndPercentsHard(collectorsReportDao.getCollectorPenaltiesAndPercents(start, end, 2,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorPenaltiesAndPercentsLegal(collectorsReportDao.getCollectorPenaltiesAndPercents(start, end, 3,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));


        collectorsReportModel.setCollectorsCollectedCreditsTotalSum(collectorsReportDao.getCollectorsCollectedCreditsTotalSum(start, end, null,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedCreditsTotalSumSoft(collectorsReportDao.getCollectorsCollectedCreditsTotalSum(start, end, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedCreditsTotalSumHard(collectorsReportDao.getCollectorsCollectedCreditsTotalSum(start, end, 2,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedCreditsTotalSumLegal(collectorsReportDao.getCollectorsCollectedCreditsTotalSum(start, end, 3,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedCommonSumDebt(collectorsReportDao.getCollectorsCollectedCommonSumDebt(start, end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedPenaltiesAndPercents(collectorsReportDao.getCollectorsCollectedPenaltiesAndPercents(start,end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));
        collectorsReportModel.setCollectorsCollectedCount(collectorsReportDao.getCollectorsCollectedCount(start,end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary ,manager,product,region,city,place,channel,paymentWay));

        return collectorsReportModel;
    }
}
