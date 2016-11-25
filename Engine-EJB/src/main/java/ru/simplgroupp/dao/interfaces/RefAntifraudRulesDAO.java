package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesParamsEntity;
import ru.simplgroupp.transfer.RefAntifraudRules;
import ru.simplgroupp.transfer.RefAntifraudRulesParams;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface RefAntifraudRulesDAO {

    /**
     * возвращает правило по id
     *
     * @param id - id правила
     * @return
     */
    RefAntifraudRulesEntity getRefAntifraudRulesEntity(int id);

    /**
     * возвращает параметр по id
     *
     * @param id - id параметра
     * @return
     */
    RefAntifraudRulesParamsEntity getRefAntifraudRulesParamsEntity(int id);

    /**
     * ищем активные правила
     *
     * @return
     */
    List<RefAntifraudRulesEntity> findActiveRules();

    /**
     * возвращает все параметры для правила
     *
     * @param ruleId - id правила
     * @return
     */
    List<RefAntifraudRulesParamsEntity> findParamsForRuleAll(int ruleId);

    /**
     * возвращает параметры правила как трансферные объекты
     *
     * @param ruleId - id правила
     * @return
     */
    List<RefAntifraudRulesParams> findParams(int ruleId, Set options);

    /**
     * возвращает параметры для правила в зависимости, запрос это или ответ
     *
     * @param ruleId     - id правила
     * @param forRequest - для запроса или нет
     * @return
     */
    List<RefAntifraudRulesParamsEntity> findParamsForRule(int ruleId, int forRequest);

    /**
     * возвращает список всех правил
     *
     * @param options опции для инициализации
     * @return
     */
    List<RefAntifraudRules> findAllRules(Set options);

    /**
     * выполняет запрос
     *
     * @param ruleId       - id правила
     * @param peopleMainId - id человека
     * @return
     */
    <T extends ExtendedBaseEntity> T createAndExecuteSqlForRequest(Integer ruleId, Integer peopleMainId);

    /**
     * выполняет запрос
     *
     * @param rule         - правило
     * @param peopleMainId - id человека
     * @return
     */
    <T extends ExtendedBaseEntity> T createAndExecuteSqlForRequest(RefAntifraudRulesEntity rule, Integer peopleMainId);

    /**
     * выполняет запрос
     *
     * @param rule         - правило
     * @param peopleMainId - id человека
     * @param queryParams  - список значений из запроса с реквестом, по которым надо проводить сравнение
     * @return
     */
    <T extends ExtendedBaseEntity> List<T> createAndExecuteSqlForResponse(RefAntifraudRulesEntity rule,
                                                                          Integer peopleMainId, Map<String, Object> queryParams);

    /**
     * выполняет запрос
     *
     * @param ruleId       - id правила
     * @param peopleMainId - id человека
     * @param queryParams  - список значений из запроса с реквестом, по которым надо проводить сравнение
     * @return
     */
    <T extends ExtendedBaseEntity> List<T> createAndExecuteSqlForResponse(Integer ruleId,
                                                                          Integer peopleMainId, Map<String, Object> queryParams);

    /**
     * Метод возвращает правило по коду
     *
     * @param ruleCode - код правила
     * @return правило
     */
    RefAntifraudRulesEntity getRefAntifraudRulesEntityByCode(String ruleCode);

    /**
     * возвращает трансферный объект правила
     *
     * @param id      id объекта
     * @param options инициализация коллекций
     * @return трансреный объект или null
     */
    RefAntifraudRules getRefAntifraudRule(int id, Set options);

    /**
     * сохраняет сущность правила
     *
     * @param rule объект сущности
     * @return сохраненную сущность или null
     */
    RefAntifraudRulesEntity saveEntity(RefAntifraudRulesEntity rule);

    /**
     * сохраняет трансферный объект
     *
     * @param rule трансферный объект
     * @return возвращает сохранненый трансферный объект или null
     */
    RefAntifraudRules saveRefAntifraudRule(RefAntifraudRules rule);

    /**
     * обновляет параметры для правила (удаляет, добавляет, обновляет)
     *
     * @param ruleId id правила
     * @param params список трансферных объектов
     */
    void updateParams(int ruleId, List<RefAntifraudRulesParams> params);

    /**
     * удаляет правило
     *
     * @param ruleId id правила
     * @return true если все ок
     */
    boolean removeRule(int ruleId);
}
