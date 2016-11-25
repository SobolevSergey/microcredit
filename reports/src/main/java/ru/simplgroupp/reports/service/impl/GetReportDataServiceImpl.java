package ru.simplgroupp.reports.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.CreditModel;
import ru.simplgroupp.reports.model.CreditReportModel;
import ru.simplgroupp.reports.model.CreditRequestModel;
import ru.simplgroupp.reports.service.GetReportsDataService;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.util.AppUtil;


import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * данный сервис возвращает модель отчета по кредитам и заявкам, вызывая dao методы
 * для заполнения каждого поля модели
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetReportDataServiceImpl implements GetReportsDataService{
    @EJB
    private CreditRequestReportDao creditRequestReportDao;
    private static final Logger logger = LoggerFactory.getLogger(GetReportDataServiceImpl.class);

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
    public CreditReportModel getCreditsStatistics(Date start, Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                  int manager,int product,String region,String city,String place,String channel,int paymentWay){
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now,-maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);//здесь мы с помощью вспомогательного класса получили минимальную и максимальную даты рождения по максимальному и минимальному возрасту
        CreditReportModel creditReportModel = new CreditReportModel();
        creditReportModel.setRequestsNumber(creditRequestReportDao.getRequestsCount(start, end, 0,Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,-1 , -1));
        creditReportModel.setRequestsNumberOneCredit(creditRequestReportDao.getRequestsCount(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,-1,-1 ));
        creditReportModel.setRequestsNumberTwoPlusCredits(creditRequestReportDao.getRequestsCount(start, end, 2, Integer.MAX_VALUE, minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel, -1, -1));
        creditReportModel.setRejectsNumber(creditRequestReportDao.getRequestsRejectsRejectsCount(start, end, 0, Integer.MAX_VALUE, minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel));
        creditReportModel.setRejectsNumberOneCredit(creditRequestReportDao.getRequestsRejectsRejectsCount(start, end, 1, 1, minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel));
        creditReportModel.setRejectsNumberTwoPlusCredits(creditRequestReportDao.getRequestsRejectsRejectsCount(start, end, 2, Integer.MAX_VALUE, minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel));
        creditReportModel.setRejectsPercentOneCredit(((double)creditReportModel.getRejectsNumberOneCredit() / (double)Math.max(1,creditReportModel.getRequestsNumberOneCredit()))*100);//процент отказов, делим число отказов на число заявок
        creditReportModel.setRejectsPercentTwoPlusCredits(((double)creditReportModel.getRejectsNumberTwoPlusCredits() / (double)Math.max(creditReportModel.getRequestsNumberTwoPlusCredits(),1))*100);
        creditReportModel.setNewClientsPercent((long) (((float) creditRequestReportDao.getNewClientsCount(start, end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay) / Math.max(creditReportModel.getRequestsNumber(),1)) * 100) + "%");
        creditReportModel.setTotalCredits(creditRequestReportDao.getCreditsCount(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setTotalCreditsOneCredit(creditRequestReportDao.getCreditsCount(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setTotalCreditsTwoPlusCredits(creditRequestReportDao.getCreditsCount(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setTotalDebt(creditRequestReportDao.getCreditsCommonSumDebt(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setTotalDebtOneCredit(creditRequestReportDao.getCreditsCommonSumDebt(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setTotalDebtTwoPlusCredits(creditRequestReportDao.getCreditsCommonSumDebt(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setAvgTotalDebt(creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setAvgTotalDebtOneCredit(creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setAvgTotalDebtTwoPlusCredits(creditRequestReportDao.getCreditsAvgCommonSumDebt(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setWeightedAvgRate(creditRequestReportDao.getAvgWeightedPercent(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setWeightedAvgRateOneCredit(creditRequestReportDao.getAvgWeightedPercent(start, end, 1, 1,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setWeightedAvgRateTwoPlusCredits(creditRequestReportDao.getAvgWeightedPercent(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        creditReportModel.setAvgPeriod(creditRequestReportDao.getAvgTerm(start,end,minBirthDate,maxBirthDate ,gender ,marriage , profession, minSalary, maxSalary,manager,product,region,city,place,channel,paymentWay));
        return creditReportModel;
    }

    /**
     * возвращает список заявок за период
     * @param start дата начала периода
     * @param end дата окончания периода
     * @return
     * @throws Exception
     */
    @Override
    public List<CreditRequestModel> getCreditRequestHistory(Date start, Date end) throws Exception {
        List<CreditRequest> creditRequests = creditRequestReportDao.getCreditRequestsHistory(start,end);
        List<CreditRequestModel> result = AppUtil.wrapCollection(CreditRequestModel.getWrapConstructor(), creditRequests);
        return result;
    }
    /**
     * возвращает список кредитов за период
     * @param start дата начала периода
     * @param end дата окончания периода
     * @return
     * @throws Exception
     */
    @Override
    public List<CreditModel> getCreditsHistory(Date start, Date end) throws Exception {
        List<Credit> credits = creditRequestReportDao.getCreditsHistory(start, end);
        List<CreditModel> result = AppUtil.wrapCollection(CreditModel.getWrapConstructor(), credits);
        return result;
    }

}
