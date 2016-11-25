package ru.simplgroupp.dao.interfaces;


import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageEntity;

import javax.ejb.Local;

@Local
public interface AntifraudDAO {

    AntifraudFieldEntity saveAntifraudField(String fieldName, String fieldValueBefore, String fieldValueAfter, Integer creditRequestId, String sessionId);

    AntifraudPageEntity saveAntifraudPage(String pageName, boolean isLeaving, Integer creditRequestId, String sessionId);

    void updateFieldsRequestId(CreditRequestEntity creditRequestEntity, String sessionId);
}
