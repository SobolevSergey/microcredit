package ru.simplgroupp.reports.dao.impl;

import ru.simplgroupp.reports.dao.ClosedCreditsReportsDao;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

/**
 * dao получает данные по закрытиям
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ClosedCreditsReportDaoImpl implements ClosedCreditsReportsDao{
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    /**
     *получает план числа закрытия кредитов
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
    public long getPlannedToCloseCreditsCount(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPlannedToCloseCreditsCount");
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
        return (Long)qry.getSingleResult();
    }
    /**
     *получает число закрытых кредитов за период
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
    public long getClosedCreditsCount(Date start, Date end, int minCredits, int maxCredits,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getClosedCreditCount");
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
        return (Long)qry.getSingleResult();
    }
    /**
     *получает число закрытых кредитов за период в срок
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
    @Override
    public long getClosedCreditsCountInTerm(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getClosedCreditCountInTerm");
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
        return (Long)qry.getSingleResult();
    }
    /**
     получает факт числа закрытых кредитов досрочно
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
    @Override
    public long getClosedCreditsCountBeforeTerm(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getClosedCreditCountBeforeTerm");
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
        return (Long)qry.getSingleResult();
    }
    /**
     получает факт числа закрытых кредитов с просрочкой
     * @param minDays минимальной число дней просрочки
     * @param maxDays максимальное число дней просрочки
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
    @Override
    public long getClosedCreditsCountWithOverdueDays(Date start, Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getClosedCreditCountWithOverdueDays");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
        qry.setParameter("maxDays", maxDays);
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
     получает факт числа закрытых кредитов с просрочкой
     * @param minDays минимальной число дней просрочки
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
    @Override
    public long getClosedCreditsCountWithOverdueDaysPlus(Date start, Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getClosedCreditCountWithOverdueDaysPlus");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
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
}
