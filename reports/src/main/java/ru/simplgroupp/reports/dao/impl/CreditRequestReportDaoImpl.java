package ru.simplgroupp.reports.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.reports.dao.CreditRequestReportDao;
import ru.simplgroupp.reports.dao.PaymentReportDao;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.AppUtil;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

/**
 * dao для получения показателей по кредитам и заявкам
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CreditRequestReportDaoImpl implements CreditRequestReportDao{
    private static final Logger logger = LoggerFactory.getLogger(CreditRequestReportDao.class);
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    @EJB
    private PaymentReportDao paymentReportDao;

    /**
     *получает количество заявок за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @param minRequests минимальное количество заявок клиента
     * @param maxRequests максимальное количество заявок клиента
     * @return
     */
    public long getRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                 int manager, int product, String region, String city, String place, String channel, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRequestsEntityCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalar" +
                "y",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Long)qry.getSingleResult();
    }

    /**
     *получает количество отказов по заявкам за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public long getRequestsRejectsRejectsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,
                                               int manager,int product,String region,String city,String place,String channel) {
        Query qry=emMicro.createNamedQuery("getRequestsRejectsCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("city",city);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        return (Long)qry.getSingleResult();
    }
    /**
     *получает число новых клиентов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public long getNewClientsCount(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getNewClientsCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
       // qry.setParameter("paymentWay",paymentWay);
        return (Long)qry.getSingleResult();
    }
    /**
     *получает количество новых кредитов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public long getCreditsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCreditsCount");
        CriteriaBuilder builder = emMicro.getCriteriaBuilder();
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Long)qry.getSingleResult();
    }
    /**
     *од новых кредитов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public double getCreditsCommonSumDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCreditsCommonDebt");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Double)qry.getSingleResult();
    }
    /**
     *средний од новых кредитов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public double getCreditsAvgCommonSumDebt(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCreditsAvgCommonDebt");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Double)qry.getSingleResult();
    }
    /**
     *средневзвешенная ставка новых кредитов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public double getAvgWeightedPercent(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCreditAvgWeightedPercent");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits", minCredits);
        qry.setParameter("maxCredits", maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Double)qry.getSingleResult();
    }
    /**
     *средний срок новых кредитов за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    public double getAvgTerm(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCreditAvgTerm");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Double)qry.getSingleResult();
    }
    /**
     *все заявки за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @return
     */
    @Override
    public List<CreditRequestEntity> getCreditRequestsHistoryEntities(Date start, Date end) {
        Query qry=emMicro.createNamedQuery("creditsRequestsHistory");
        qry.setParameter("start",start);
        qry.setParameter("end",end);
        qry.setMaxResults(10000);
        return (List<CreditRequestEntity>) qry.getResultList();

    }
    /**
     *все новые заявки за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @return
     */
    @Override
    public List<CreditRequest> getCreditRequestsHistory(Date start,Date end) throws Exception{
        List entities = getCreditRequestsHistoryEntities(start,end);
        List<CreditRequest> result = AppUtil.wrapCollection(CreditRequest.getWrapConstructor(), entities);
        Utils.initCollection(result, Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
        return result;
    }
    /**
     *все новые кредиты за период, entities
     * @param start дата начала периода
     * @param end дата окончания период
     * @return
     */
    @Override
    public List<CreditRequestEntity> getCreditsHistoryEntities(Date start, Date end) {
        Query qry=emMicro.createNamedQuery("creditsHistory");
        qry.setParameter("start",start);
        qry.setParameter("end",end);
        qry.setMaxResults(10000);
        return (List<CreditRequestEntity>)qry.getResultList();

    }

    /**
     *все новые кредиты за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @return
     */
    @Override
    public List<Credit> getCreditsHistory(Date start, Date end) throws Exception {
        List entities = getCreditsHistoryEntities(start, end);
        List<Credit> result = AppUtil.wrapCollection(Credit.getWrapConstructor(), entities);
        Utils.initCollection(result, Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL, BaseCredit.Options.INIT_CREDIT_REQUEST));
        return result;

    }
    /**
     *сумма новых заявок за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRequestsSum");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     *средняя сумма новых заявок за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getRequestsAvgSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRequestsAvgSum");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     *средний срок новых заявок за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getRequestsAvgTerm(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRequestsAvgTerm");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     *количество принятых заявок за период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public long getApprovedRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getAcceptedRequestsCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Long) qry.getSingleResult();
    }
    /**
     *сумма принятых заявок за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getApprovedRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getAcceptedRequestsSum");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     * число отказов по заявкам за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public long getRejectedRequestsCount(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedRequestsCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Long) qry.getSingleResult();
    }
    /**
     * число отказов клиента по заявкам за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public long getRejectedRequestsCountByClient(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedByClientRequestsCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Long) qry.getSingleResult();
    }
    @Override
    public long getRejectedRequestsCountByClientAutomatic(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedByClientRequestsCountAutomatic");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Long) qry.getSingleResult();
    }
    /**
     * сумма заявок по которым был отказ
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getRejectedRequestsSum(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedRequestsSum");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     * сумма заявок по которым был отказ клиента за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getRejectedRequestsSumByClient(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedByClientRequestsSum");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    @Override
    public double getRejectedRequestsSumByClientAutomatic(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay, int minRequests, int maxRequests) {
        Query qry=emMicro.createNamedQuery("getRejectedByClientRequestsSumAutomatic");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("minRequests",(long)minRequests);
        qry.setParameter("maxRequests",(long)maxRequests);
        return (Double) qry.getSingleResult();
    }
    /**
     * просрочка по кредитам на конец периода
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getOverdueCredits(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay) {
        Query payedCommonDebt=emMicro.createNamedQuery("getPayedCreditsSumCommonDebtForPlannedToCloseCredits");
        payedCommonDebt.setParameter("dateStart", start);
        payedCommonDebt.setParameter("dateEnd", end);
        payedCommonDebt.setParameter("minCredits",minCredits);
        payedCommonDebt.setParameter("maxCredits",maxCredits);
        payedCommonDebt.setParameter("minBirthdate", minBirthdate);
        payedCommonDebt.setParameter("maxBirthdate", maxBirthdate);
        payedCommonDebt.setParameter("gender",gender);
        payedCommonDebt.setParameter("marriage",marriage);
        payedCommonDebt.setParameter("profession",profession);
        payedCommonDebt.setParameter("minSalary",minSalary);
        payedCommonDebt.setParameter("maxSalary",maxSalary);
        payedCommonDebt.setParameter("manager",manager);
        payedCommonDebt.setParameter("product",product);
        payedCommonDebt.setParameter("region",region);
        payedCommonDebt.setParameter("place",place);
        payedCommonDebt.setParameter("channel",channel);
        payedCommonDebt.setParameter("city",city);
        payedCommonDebt.setParameter("paymentWay",paymentWay);

        /*Query plannedToPayCommonDebt=emMicro.createNamedQuery("getPlannedToPaySumCommonDebt");
        plannedToPayCommonDebt.setParameter("dateStart", start);
        plannedToPayCommonDebt.setParameter("dateEnd", end);
        plannedToPayCommonDebt.setParameter("minCredits",minCredits);
        plannedToPayCommonDebt.setParameter("maxCredits",maxCredits);
        plannedToPayCommonDebt.setParameter("minBirthdate", minBirthdate);
        plannedToPayCommonDebt.setParameter("maxBirthdate", maxBirthdate);
        plannedToPayCommonDebt.setParameter("gender",gender);
        plannedToPayCommonDebt.setParameter("marriage",marriage);
        plannedToPayCommonDebt.setParameter("profession",profession);
        plannedToPayCommonDebt.setParameter("minSalary",minSalary);
        plannedToPayCommonDebt.setParameter("maxSalary",maxSalary);
        plannedToPayCommonDebt.setParameter("manager",manager);
        plannedToPayCommonDebt.setParameter("product",product);
        plannedToPayCommonDebt.setParameter("region",region);
        plannedToPayCommonDebt.setParameter("place",place);
        plannedToPayCommonDebt.setParameter("channel",channel);
        plannedToPayCommonDebt.setParameter("city",city);
        plannedToPayCommonDebt.setParameter("paymentWay",paymentWay);*/
        double plannedToPayCommonDebtResult = paymentReportDao.getPlannedToPayASumCommonDebt(start,end,minBirthdate,maxBirthdate,gender,marriage,profession,minSalary,maxSalary,manager,product,region,city,place,channel,paymentWay,minCredits,maxCredits);
        double payedCommonDebtResult =  (Double) payedCommonDebt.getSingleResult();
        logger.error("calculating overdue");
        logger.error("planned to pay "+plannedToPayCommonDebtResult);
        logger.error("payed "+payedCommonDebtResult );
        logger.error("difference "+(plannedToPayCommonDebtResult - payedCommonDebtResult));
        return plannedToPayCommonDebtResult - payedCommonDebtResult ;
    }
    /**
     * од кредитов закрытых за данный период
     * @param start дата начала периода
     * @param end дата окончания период
     * @param minCredits минимальное количество кредитов у клиента
     * @param maxCredits максимальное количество кредитов у клиента
     * @param minBirthdate минимальная дата рождения
     * @param maxBirthdate максимальная дата рождения
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
     * @return
     */
    @Override
    public double getFinishedCreditsCommonDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay) {
        Query qry=emMicro.createNamedQuery("getFinishedCreditsCommonDebt");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minCredits",minCredits);
        qry.setParameter("maxCredits",maxCredits);
        qry.setParameter("minBirthdate", minBirthdate);
        qry.setParameter("maxBirthdate", maxBirthdate);
        qry.setParameter("gender",gender);
        qry.setParameter("marriage",marriage);
        qry.setParameter("profession",profession);
        qry.setParameter("minSalary",minSalary);
        qry.setParameter("maxSalary",maxSalary);
        qry.setParameter("manager",manager);
        qry.setParameter("product",product);
        qry.setParameter("region",region);
        qry.setParameter("place",place);
        qry.setParameter("channel",channel);
        qry.setParameter("city",city);
        qry.setParameter("paymentWay",paymentWay);
        return (Double) qry.getSingleResult();
    }

}
