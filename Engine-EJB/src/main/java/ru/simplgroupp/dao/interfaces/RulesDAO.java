package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.AIConstantEntity;
import ru.simplgroupp.persistence.AIRuleEntity;
import ru.simplgroupp.transfer.AIRule;

@Local
public interface RulesDAO {

    /**
     * удаляем константу
     * @param constantId - id константы
     */	
	void deleteConstant(Integer constantId);

    /**
     * возвращает правило по id
     * @param id - id правила
     * @param options - что инициализируем
     */	
	AIRuleEntity getAIRule(int id, Set options);

    /**
     * удалить правило
     * @param id - id правила
     */	
	void deleteRule(int id);

	/**
	 * сколько правил
	 * @param packageName - название пакета
	 * @param description - описание
	 * @param ruleType - вид правила
	 * @param kbase 
	 */	
	int countRules(String packageName, String description, Integer ruleType,
			String kbase);

	/**
	 * возвращает список правил
	 * @param packageName - название пакета
	 * @param description - описание
	 * @param ruleType - вид правила
	 * @return
	 */
	List<AIRuleEntity> listRules(String packageName, String description,
			Integer ruleType, String kbase);

    /**
     * ищем константу
     * @param packageName - пакет
     * @param constantName - название константы
     */	
	AIConstantEntity findConstant(String packageName, String constantName);

	/**
	 * Удаляет все константы из правила, оставляя само правило
	 * @param ruleid
	 */
	void deleteRuleConstants(int ruleid);

	/**
	 * возвращает константу по id
	 * @param id - id константы
	 * @return
	 */	
	AIConstantEntity getConstant(int id);

    /**
     * сохранить правило
     * @param rule - правило
     */	
	AIRuleEntity saveRule(AIRuleEntity rule);

	/**
	 * Сохранить константу
	 * @param acon
	 * @return
	 */
	AIConstantEntity saveConstant(AIConstantEntity acon);

    /**
     * ищем правило по названию 
     * @param packageName - название пакета
     */	
	AIRuleEntity findRuleByName(String packageName);

    /**
     * возвращает список констант
     * @param packageName - название пакета
     * @param constNameTpl - название константы
     */	
	List<AIConstantEntity> listConstants(String packageName, String constNameTpl);

	/**
	 * возвращает правило по id 
	 * @param id - id правила
	 * @return
	 */
	AIRuleEntity getAIRule(int id);

	/**
	 * ищем правило по названию пакета
	 * @param packageName - название пакета
	 * @param options - что инициализируем
	 * @return
	 */
	AIRule findRuleByName(String packageName, Set options);

}
