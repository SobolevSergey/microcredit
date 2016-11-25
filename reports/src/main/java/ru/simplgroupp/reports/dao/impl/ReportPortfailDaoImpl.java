package ru.simplgroupp.reports.dao.impl;

import ru.simplgroupp.reports.dao.ReportPortfailDao;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

/**
 * dao для получения показателей порфтеля
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ReportPortfailDaoImpl implements ReportPortfailDao {
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    /**
     *получает портфель - количество активных заявок на конец периода
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
    public long getPortfailCount(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query payementsToClientQry=emMicro.createNamedQuery("getPaymentsToClientsCount");
        payementsToClientQry.setParameter("dateEnd", end);
        payementsToClientQry.setParameter("minBirthdate", minBirthdate);
        payementsToClientQry.setParameter("maxBirthdate", maxBirthdate);
        payementsToClientQry.setParameter("gender",gender);
        payementsToClientQry.setParameter("marriage",marriage);
        payementsToClientQry.setParameter("profession",profession);
        payementsToClientQry.setParameter("minSalary",minSalary);
        payementsToClientQry.setParameter("maxSalary",maxSalary);
        payementsToClientQry.setParameter("manager",manager);
        payementsToClientQry.setParameter("product",product);
        payementsToClientQry.setParameter("region",region);
        payementsToClientQry.setParameter("place",place);
        payementsToClientQry.setParameter("channel",channel);
        payementsToClientQry.setParameter("city",city);
        payementsToClientQry.setParameter("paymentWay",paymentWay);
        Query closedCreditCountBeforeDate=emMicro.createNamedQuery("getClosedCreditCountBeforeDate");
        closedCreditCountBeforeDate.setParameter("dateEnd", end);
        closedCreditCountBeforeDate.setParameter("minBirthdate", minBirthdate);
        closedCreditCountBeforeDate.setParameter("maxBirthdate", maxBirthdate);
        closedCreditCountBeforeDate.setParameter("gender",gender);
        closedCreditCountBeforeDate.setParameter("marriage",marriage);
        closedCreditCountBeforeDate.setParameter("profession",profession);
        closedCreditCountBeforeDate.setParameter("minSalary",minSalary);
        closedCreditCountBeforeDate.setParameter("maxSalary",maxSalary);
        closedCreditCountBeforeDate.setParameter("manager",manager);
        closedCreditCountBeforeDate.setParameter("product",product);
        closedCreditCountBeforeDate.setParameter("region",region);
        closedCreditCountBeforeDate.setParameter("place",place);
        closedCreditCountBeforeDate.setParameter("channel",channel);
        closedCreditCountBeforeDate.setParameter("city",city);
        closedCreditCountBeforeDate.setParameter("paymentWay",paymentWay);
        return (Long)payementsToClientQry.getSingleResult() - (Long)closedCreditCountBeforeDate.getSingleResult();
    }
    /**
     *получает активный портфель - по которому были платежи в данный период  - количество активных заявок на конец периода
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
    public long getPortfailCountActive(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountActive");
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
     *портфель - срок
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
    public long getPortfailCountInTerm(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountInTerm");
        qry.setParameter("dateEnd", end);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
     *портфель кол-во - с просрочкой
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
    public long getPortfailCountWithOverdue(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountWithOverdue");
        qry.setParameter("dateEnd", end);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
     *портфель кол-во - с просрочкой в заданном интервале, причем maxDays подставлять нужно +1 дней от того что нужно
     * к примеру надо с просрочкой 1-3 дня, подставляем minDays 1, maxDays 4
     * @param minDays минимально дней просрочки
     * @param maxDays максимально дней просрочки
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
    public long getPortfailCountWithOverdueInterval(Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountWithOverdueInterval");
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
        qry.setParameter("maxDays", maxDays);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
     *портфель кол-во - с просрочкой от minDays
     * @param minDays минимально дней просрочки
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
    public long getPortfailCountWithOverduePlus(Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountWithOverduePlus");
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
     *портфель сумма од
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
    public double getCommonSumDebt(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebt");
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
        return (double)qry.getSingleResult();
    }
    /**
     *портфель сумма од активный - кредиты по которым был платеж в данный период
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
    public double getCommonSumDebtActive(Date start, Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtActive");
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
        return (double)qry.getSingleResult();
    }
    /**
     *сумма од портфель - срок
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
    public double getCommonSumDebtInTerm(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtInTerm");
        qry.setParameter("dateEnd", end);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
        return (double)qry.getSingleResult();
    }
    /**
     *сумма од портфель - просрочка
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
    public double getCommonSumDebtWithOverdue(Date end,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtWithOverdue");
        qry.setParameter("dateEnd", end);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
        return (double)qry.getSingleResult();
    }
    /**
     *сумма од портфель - просрочка интервал
     * @param minDays от скольки дней просрочка
     * @param maxDays до скольки дней просрочка. Причем подставлять надо значение +1 т.е. если просрочка до 3 дней включительно напрмер то надо подставить 4
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
    public double getCommonSumDebtWithOverdueInterval(Date end, long minDays, long maxDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtWithOverdueInterval");
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
        qry.setParameter("maxDays", maxDays);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
        return (double)qry.getSingleResult();
    }
    /**
     *сумма од портфель  просрочка от minDays
     * @param minDays от скольки дней просрочка
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
    public double getCommonSumDebtWithOverduePlus(Date end, long minDays,Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary ,int manager,int product,String region,String city,String place,String channel,int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtWithOverduePlus");
        qry.setParameter("dateEnd", end);
        qry.setParameter("minDays", minDays);
        qry.setParameter("dateEndMilliseconds",end.getTime());
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
        return (double)qry.getSingleResult();
    }
    /**
     *портфель прирост кол-во
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
    public long getPortfailCountGrow(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCountGrow");
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
    /**
     *портфель прирост сумма
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
    public double getPortfailSumGrow(Date start, Date end, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailSumGrow");
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
        return (double)qry.getSingleResult();
    }
    /**портфель просрочка сумма од прирост
     *@param start дата начала периода
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
    public double getCommonSumGrowDebt(Date start, Date end, int minCredits, int maxCredits, Date minBirthdate, Date maxBirthdate, String gender, int marriage, int profession, double minSalary, double maxSalary, int manager, int product, String region, String city, String place, String channel, int paymentWay) {
        Query qry=emMicro.createNamedQuery("getPortfailCommonSumDebtGrov");
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

        return (double)qry.getSingleResult();

    }

}
