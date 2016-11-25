package ru.simplgroupp.reports.service.impl;

import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.dao.PaymentReportDao;
import ru.simplgroupp.reports.date.util.DateUtil;
import ru.simplgroupp.reports.model.PaymentModel;
import ru.simplgroupp.reports.model.PaymentReportModel;
import ru.simplgroupp.reports.service.GetPaymentsDataService;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.AppUtil;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * данный сервис возвращает модель отчета по платежам, вызывая dao методы
 * для заполнения каждого поля модели
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class GetPaymentDataServiceImpl implements GetPaymentsDataService {
    @EJB
    private PaymentReportDao paymentReportDao;
    @EJB
    private CreditRequestReportDao creditRequestReportDao;
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
    public PaymentReportModel getPaymentStatistics(Date start,Date end, int minAge, int maxAge, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                                   int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Date now = new Date();
        Date minBirthDate = DateUtil.getDateMinusInterval(now, -maxAge, Calendar.YEAR);
        Date maxBirthDate = DateUtil.getDateMinusInterval(now,-minAge, Calendar.YEAR);//здесь мы с помощью вспомогательного класса получили минимальную и максимальную даты рождения по максимальному и минимальному возрасту
        PaymentReportModel paymentReportModel = new PaymentReportModel();
        paymentReportModel.setPlannedToPayAmount(paymentReportDao.getPlannedToPayAmt(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPlannedToPayAmountOneCredit(paymentReportDao.getPlannedToPayAmt(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPlannedToPayAmountTwoPlusCredits(paymentReportDao.getPlannedToPayAmt(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPlannedToPaySum(paymentReportDao.getPlannedToPayASum(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPlannedToPaySumCommonDebt(paymentReportDao.getPlannedToPayASumCommonDebt(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay, 0,Integer.MAX_VALUE ));
        paymentReportModel.setPlannedToPaySumPenalties(paymentReportDao.getPlannedToPayASumPenalties(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPlannedToPayPercents(paymentReportModel.getPlannedToPaySum() - paymentReportModel.getPlannedToPaySumCommonDebt());
        paymentReportModel.setPayedAmount(paymentReportDao.getPayedAmtFact(start, end, 0, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedAmountOneCredit(paymentReportDao.getPayedAmtFact(start, end, 1, 1,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedAmountTwoPlusCredits(paymentReportDao.getPayedAmtFact(start, end, 2, Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSum(paymentReportDao.getPayedSumFact(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumBeforeTerm(paymentReportDao.getPayedSumFactBeforeTerm(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumInTerm(paymentReportDao.getPayedSumFactInTerm(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue1_3(paymentReportDao.getPayedSumFactOverdueDays(start, end, 1, 4,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue4_10(paymentReportDao.getPayedSumFactOverdueDays(start, end, 4, 11,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue11_30(paymentReportDao.getPayedSumFactOverdueDays(start, end, 11, 31,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue31_60(paymentReportDao.getPayedSumFactOverdueDays(start, end, 31, 61,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue61_90(paymentReportDao.getPayedSumFactOverdueDays(start, end, 61, 91,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumOverdue91_plus(paymentReportDao.getPayedSumFactOverdueDays(start, end, 91,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setPayedSumCommonDebt(paymentReportDao.getPayedSumFactCommonDebt(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay,0 ,Integer.MAX_VALUE ));
        paymentReportModel.setPayedSumPercents(paymentReportDao.getPayedSumFactPercents(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));

        paymentReportModel.setPayedSumPenalties(paymentReportDao.getPayedSumFactPenalties(start, end,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));
        paymentReportModel.setMoneyFlow(paymentReportModel.getPayedSum() - creditRequestReportDao.getCreditsCommonSumDebt(start,end, 0,Integer.MAX_VALUE,minBirthDate,maxBirthDate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay));//денежный поток вычисляется как сумма оплат за данный период минус сумма од новых кредитов
        return paymentReportModel;
    }
    @Override
    public List<PaymentModel> getPaymentsHistory(Date start, Date end) throws Exception {
        List<Payment> payments = paymentReportDao.getPaymentsHistory(start, end);
        List<PaymentModel> result = AppUtil.wrapCollection(PaymentModel.getWrapConstructor(), payments);
        return result;
    }
}
