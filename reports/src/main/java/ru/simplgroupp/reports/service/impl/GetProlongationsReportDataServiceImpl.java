package ru.simplgroupp.reports.service.impl;

import ru.simplgroupp.reports.dao.ProlongationReportDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.ProlongationReportModel;
import ru.simplgroupp.reports.service.GetProlongationsReportDataService;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;

/**
 * данный сервис возвращает модель отчета по продлениям, вызывая dao методы
 * для заполнения каждого поля модели
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetProlongationsReportDataServiceImpl implements GetProlongationsReportDataService {
    @EJB
    private ProlongationReportDao prolongationReportDao;
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
    public ProlongationReportModel getProlongationsReportData(Date start,Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                              int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);//здесь мы с помощью вспомогательного класса получили минимальную и максимальную даты рождения по максимальному и минимальному возрасту
        ProlongationReportModel prolongationReportModel = new ProlongationReportModel();
        prolongationReportModel.setProlongationsAmount(prolongationReportDao.getProlongationsAmt(start,end,0 , Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationsAmountOneCredit(prolongationReportDao.getProlongationsAmt(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationsAmountTwoPlusCredits(prolongationReportDao.getProlongationsAmt(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAmount(prolongationReportDao.getProlongationsCreditAmt(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAmountOneCredit(prolongationReportDao.getProlongationsCreditAmt(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAmountTwoPlusCredits(prolongationReportDao.getProlongationsCreditAmt(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAvgSum(prolongationReportDao.getProlongationsCreditAvgSum(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary ,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAvgSumOneCredit(prolongationReportDao.getProlongationsCreditAvgSum(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setCreditAvgSumTwoPlusCredits(prolongationReportDao.getProlongationsCreditAvgSum(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary ,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationAvgTerm(prolongationReportDao.getProlongationsAvgTerm(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationAvgTermOneCredit(prolongationReportDao.getProlongationsAvgTerm(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationAvgTermTwoPlusCredits(prolongationReportDao.getProlongationsAvgTerm(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        prolongationReportModel.setProlongationPaymentAvgSum(prolongationReportDao.getProlongationsAvgPayment(start,end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary ,manager,product,region,city,place,channel,paymentWay));
        return prolongationReportModel;
    }
}
