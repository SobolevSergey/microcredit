package ru.simplgroupp.interfaces;

import java.util.List;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.hunter.hashing.SCL;
import ru.simplgroupp.hunter.onlinematching.wsdl.Match;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.workflow.PluginExecutionContext;

public interface ScoringOkbHunterBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
    String SYSTEM_NAME = "okbHunter";
    String SYSTEM_DESCRIPTION = "Запросы в ОКБ Национальный Хантер";

    Match createRequest(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException;

    Match createMatch(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException;

    void hash(Match match) throws ActionException;

    void changePassword(PluginExecutionContext context) throws ActionException;

    void uploadCreditRequests(PluginExecutionContext context) throws ActionException;

    void uploadCreditRequests(List<CreditRequestEntity> creditRequestEntities, PluginExecutionContext context) throws ActionException;
}
