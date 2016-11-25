package ru.simplgroupp.reports.dao.impl;

import ru.simplgroupp.reports.dao.CollectorsReportDao;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

/**
 *dao для получения показателей по взысканиям
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CollectorsReportDaoImpl implements CollectorsReportDao{
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    /**
     *количество кредитов на взыскании
     * @param collectorType codeinteger для фильтрации collectorTypeId, который ссылается на справочник - тип коллектора
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
    public long getCollectorsCreditsCount(Date start, Date end, Integer collectorType,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCount");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("code", collectorType == null ? -1 : collectorType);
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
     *сумма кредитов на взыскании
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
    public double getCollectorsCreditsTotalSum(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorTotalDebt");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("code", code == null ? -1 : code);
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
     *од кредитов на взыскании
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
    public double getCollectorsCreditsCommonSumDebt(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCommonDebt");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("code", code == null ? -1 : code);
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
     *сумма штрафов и процентов кредитов на взыскании
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
    public double getCollectorPenaltiesAndPercents(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorPenaltiesAndPercents");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("code", code == null ? -1 : code);
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
     *взыскания сборы сумма всего
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
    public double getCollectorsCollectedCreditsTotalSum(Date start, Date end, Integer code,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCollectedTotal");
        qry.setParameter("dateStart", start);
        qry.setParameter("dateEnd", end);
        qry.setParameter("code", code == null ? -1 : code);
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
     *взыскания сборы сумма од
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
    public double getCollectorsCollectedCommonSumDebt(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCollectedCommonDebt");
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
     *взыскания сборы - проценты и штрафы
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
    public double getCollectorsCollectedPenaltiesAndPercents(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCollectedPenaltiesAndPercents");
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
     *взыскания сборы кол-во
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
    public long getCollectorsCollectedCount(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getCollectorCollectedCount");
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
        return (long)qry.getSingleResult();
    }
}
