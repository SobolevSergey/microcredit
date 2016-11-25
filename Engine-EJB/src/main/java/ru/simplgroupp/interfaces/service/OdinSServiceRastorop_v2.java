package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.odins.model.rastorop_v2.OnlineCreditExchange;

import java.util.Date;

public interface OdinSServiceRastorop_v2 {

    /**
     * Версия 2 - Собрать в XML отчет для 1С
     * Используя новейшую чудо-таблицу от Елены  - creditdetails
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    OnlineCreditExchange generateXMLResponse(Date date);


    /**
     * Собирает в XML начисленные проценты со дня открытия кредита на текущую дату, по всем открытым кредитам
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    OnlineCreditExchange generateXMLAccruedInterest(Date date);

}
