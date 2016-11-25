package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.odins.model.rastorop.OnlineCreditExchange;

import java.util.Date;

public interface OdinSServiceRastorop {

    /**
     * Собрать в XML отчет для 1С
     * @param date - день, за котоорый предоставляются данные
     * @return объект, который будет преобразован в XML с помощью JAXB
     */
    OnlineCreditExchange generateXMLResponse(Date date);

    /**
     * Версия 2 - Собрать в XML отчет для 1С
     * Используя новейшую чудо-таблицу от Елены  - creditdetails
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    OnlineCreditExchange generateXMLResponseV2(Date date);


    /**
     * Собирает в XML начисленные проценты со дня открытия кредита на текущую дату, по всем открытым кредитам
     * @param date - день, за котоорый предоставляются данные
     * @return
     */
    OnlineCreditExchange generateXMLAccruedInterest(Date date);

}
