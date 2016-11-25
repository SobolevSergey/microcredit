package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.transfer.RefAntifraudRules;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface RefAntifraudRulesService {

	/**
	 * проверяет соответствие правил АМ
	 * @param peopleMainId      - человек
	 * @param creditRequestId   - заявка
	 */
	void getMatchesForAntifraudRules(Integer peopleMainId,Integer creditRequestId);

	/**
	 * проверяет соответствие правила
	 *
	 * @param peopleMainId    - человек
	 * @param creditRequestId - заявка
	 * @param rule            - правило
	 */
	void getMatchesForAntifraudRule(Integer peopleMainId, Integer creditRequestId, RefAntifraudRulesEntity rule);

	/**
	 * заполняем параметры для запроса по БД
	 *
	 * @param entity - сущность по клиенту, которого проверяем
	 * @return
	 */
	<T extends ExtendedBaseEntity> Map<String, Object> getParamsFromRequest(T entity);

    /**
     * Метод достает активные АМ правила
     *
     * @param options параметры для инициализации
     * @return список правил
     */
    List<RefAntifraudRules> getActiveRefAntifraudRules(Set options);
}
