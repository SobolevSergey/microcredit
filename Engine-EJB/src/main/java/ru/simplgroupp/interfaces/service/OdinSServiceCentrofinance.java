package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.odins.model.centrofinance.SputnikExchange;

import java.util.Date;

public interface OdinSServiceCentrofinance {

    /**
     * Версия 2 - Собрать в XML отчет для 1С
     * Используя новейшую чудо-таблицу от Елены  - creditdetails
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    SputnikExchange generateXMLResponse(Date date);


    /**
     * Собирает в XML начисленные проценты со дня открытия кредита на текущую дату, по всем открытым кредитам
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    SputnikExchange generateXMLAccruedInterest(Date date);

}
