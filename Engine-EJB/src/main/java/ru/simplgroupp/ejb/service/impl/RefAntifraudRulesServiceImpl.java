package ru.simplgroupp.ejb.service.impl;

import ru.simplgroupp.dao.interfaces.AntifraudSuspicionDAO;
import ru.simplgroupp.dao.interfaces.RefAntifraudRulesDAO;
import ru.simplgroupp.interfaces.service.RefAntifraudRulesService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RefAntifraudRules;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class RefAntifraudRulesServiceImpl implements RefAntifraudRulesService {

	@Inject Logger log;

    @EJB
    private RefAntifraudRulesDAO antifraudRulesDAO;

    @EJB
    private AntifraudSuspicionDAO antifraudSuspicionDAO;


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void getMatchesForAntifraudRules(Integer peopleMainId, Integer creditRequestId) {
        List<RefAntifraudRulesEntity> lstRules = antifraudRulesDAO.findActiveRules();
        for (RefAntifraudRulesEntity rule : lstRules) {
            getMatchesForAntifraudRule(peopleMainId, creditRequestId, rule);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void getMatchesForAntifraudRule(Integer peopleMainId, Integer creditRequestId, RefAntifraudRulesEntity rule) {
        ExtendedBaseEntity entity = antifraudRulesDAO.createAndExecuteSqlForRequest(rule, peopleMainId);
        if (entity == null) {
            log.severe("Не удалось найти данных по клиенту " + peopleMainId
                    + " заявка " + creditRequestId + " правило " + rule.getName());
        } else {
            Map<String, Object> map = getParamsFromRequest(entity);

            // ищем дочерние правила и выполняем их
            if (rule.getChildRulesList().size() != 0) {
                for (RefAntifraudRulesEntity subQueryRule : rule.getChildRulesList()) {
                    ExtendedBaseEntity subQueryResult = antifraudRulesDAO.createAndExecuteSqlForRequest(subQueryRule, peopleMainId);
                    // берем нужные нам параметры
                    getParamsFromRequest(subQueryResult, map);
                }
            }

            List<ExtendedBaseEntity> lstEntity = antifraudRulesDAO.createAndExecuteSqlForResponse(rule, peopleMainId, map);
            //если что-то нашлось и записей не меньше необходимого количества совпадений, надо записать в БД
            if (lstEntity.size() >= rule.getTimes()) {
                log.info("Нашлось записей " + lstEntity.size() + " по клиенту " + peopleMainId
                        + " заявка " + creditRequestId + " правило " + rule.getName());
                for (ExtendedBaseEntity newEntity : lstEntity) {
                    AntifraudSuspicionEntity suspicionEntity = antifraudSuspicionDAO.buildSuspicion(creditRequestId, peopleMainId,
                            Partner.SYSTEM, rule, newEntity.getPeopleMainId().getId(), null, null, null, null);
                    antifraudSuspicionDAO.saveSuspicion(suspicionEntity);
                }//end for
            } else {
            	log.info("Не нашлось нужного количества записей " + lstEntity.size() + " по клиенту " + peopleMainId
                        + " заявка " + creditRequestId + " правило " + rule.getName());
            }//end if records no less than times
        }//end entity is null
    }

    /**
     * заполняем параметры для запроса по БД
     *
     * @param entity - сущность по клиенту, которого проверяем
     * @return
     */
    @Override
    public <T extends ExtendedBaseEntity> Map<String, Object> getParamsFromRequest(T entity) {
        Map<String, Object> map = new HashMap<String, Object>(10);
        return getParamsFromRequest(entity, map);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<RefAntifraudRules> getActiveRefAntifraudRules(Set options) {
        List<RefAntifraudRulesEntity> list = antifraudRulesDAO.findActiveRules();
        List<RefAntifraudRules> result = new ArrayList<>();
        for (RefAntifraudRulesEntity entity : list) {
            RefAntifraudRules rule = new RefAntifraudRules(entity);
            rule.init(options);

            result.add(rule);
        }
        return result;
    }

    /**
     * заполняем параметры для запроса по БД
     *
     * @param entity - сущность по клиенту, которого проверяем
     * @param map - карта с параметрами
     * @return
     */
    private <T extends ExtendedBaseEntity> Map<String, Object> getParamsFromRequest(T entity, Map<String, Object> map) {
        if (entity instanceof PeopleContactEntity) {
            map.put("value", ((PeopleContactEntity) entity).getValue());
        } else if (entity instanceof EmploymentEntity) {
            map.put("salary", ((EmploymentEntity) entity).getSalary());
        } else if (entity instanceof DocumentEntity) {
            map.put("series", ((DocumentEntity) entity).getSeries());
            map.put("number", ((DocumentEntity) entity).getNumber());
        } else if (entity instanceof AddressEntity) {
            map.put("region", ((AddressEntity) entity).getRegion());
            map.put("area", StringUtils.isEmpty(((AddressEntity) entity).getArea()) ? null : ((AddressEntity) entity).getArea());
            map.put("place", StringUtils.isEmpty(((AddressEntity) entity).getPlace()) ? null : ((AddressEntity) entity).getPlace());
            map.put("city", StringUtils.isEmpty(((AddressEntity) entity).getCity()) ? null : ((AddressEntity) entity).getCity());
            map.put("street", StringUtils.isEmpty(((AddressEntity) entity).getStreet()) ? null : ((AddressEntity) entity).getStreet());
            map.put("house", ((AddressEntity) entity).getHouse());
            map.put("building", StringUtils.isEmpty(((AddressEntity) entity).getBuilding()) ? null : ((AddressEntity) entity).getBuilding());
            map.put("corpus", StringUtils.isEmpty(((AddressEntity) entity).getCorpus()) ? null : ((AddressEntity) entity).getCorpus());
            map.put("flat", StringUtils.isEmpty(((AddressEntity) entity).getFlat()) ? null : ((AddressEntity) entity).getFlat());
            map.put("index", StringUtils.isEmpty(((AddressEntity) entity).getIndex()) ? null : ((AddressEntity) entity).getIndex());
        } else if (entity instanceof PeoplePersonalEntity) {
            map.put("name", ((PeoplePersonalEntity) entity).getName());
            map.put("surname", ((PeoplePersonalEntity) entity).getSurname());
            map.put("midname", StringUtils.isEmpty(((PeoplePersonalEntity) entity).getMidname()) ? null : ((PeoplePersonalEntity) entity).getMidname());
            map.put("birthdate", ((PeoplePersonalEntity) entity).getBirthdate());
        } else if (entity instanceof AntifraudOccasionEntity) {

        } else if (entity instanceof EventLogEntity) {
            map.put("ipaddress", ((EventLogEntity) entity).getIpaddress());
        }
        return map;
    }
}
