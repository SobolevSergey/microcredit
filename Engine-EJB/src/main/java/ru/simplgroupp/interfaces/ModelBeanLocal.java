package ru.simplgroupp.interfaces;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ModelException;
import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.toolkit.common.ExecutionProgress;
import ru.simplgroupp.transfer.AIModel;
import ru.simplgroupp.transfer.CreditRequest;

public interface ModelBeanLocal {

	/**
	 * возвращает список активных моделей
	 */
	public List<AIModel> getActiveModels();

	/**
	 * возвращает список активных моделей продукта
	 *
	 * @param productId - id продукта
	 */
	public List<AIModel> getActiveProductModels(Integer productId);

	/**
	 * возвращает активную модель
	 * 
	 * @param target - название
	 * @return
	 */
	public AIModel getActiveModel(String target);
	/**
	 * возвращает активную модель
	 * 
	 * @param target - название
	 * @param productId - id продукта
	 * @return
	 */
	public AIModel getActiveProductModel(String target,Integer productId);
	/**
	 * возвращает активную модель
	 * 
	 * @param target - название
	 * @param productId - id продукта
	 * @return
	 */
	public AIModelEntity getActiveProductModelEntity(String target,Integer productId);
	/**
	 * возвращает список моделей
	 * 
	 * @param target - модель
	 * @param activeStatuses - статусы
	 * @param version - версия
	 * @param bodyTpl - текст 
	 * @param productId - id продукта
	 * @param wayId - id способа подачи заявки
	 * @return
	 */
	public List<AIModelEntity> listModels(String target, Integer[] activeStatuses,
			String version, String bodyTpl,Integer productId, Integer wayId);

	/**
	 * возвращает модель
	 * @param id - id модели
	 * @return
	 */
	public AIModelEntity getModel(int id);

	/**
	 * возвращает версии модели
	 * @param target 
	 * @param bIncludeActive
	 * @return
	 */
	public List<AIModel> getModelVersions(String target, boolean bIncludeActive);

	/**
	 * возвращает модель
	 * @param id - id модели
	 * @param options - что инициализируем
	 * @return
	 */
	public AIModel getModel(int id, Set options);

	/**
	 * создаем черновик
	 * @param sourceModelId - id модели, из которой создаем
	 * @return
	 */
	public AIModelEntity createDraftFrom(int sourceModelId);

	/**
	 * поддерживаемые версии скрипта системой
	 * @return
	 */
	public List<String> getScriptTypesSupported();
    /**
     * удаляем модель
     * @param modelId - id модели
     */
	public void removeModel(int modelId);

	/**
	 * сохраняем модель
	 * @param source - модель для сохранения
	 * @return
	 */
	public AIModel saveModel(AIModel source);

	/**
	 * сделать модель активной
	 * @param modelId - id модели
	 */
	public void makeActive(int modelId);

	/**
	 * удаляет все существующие черновики
	 */
	public void removeDraftModels();

	/**
	 * создает модель
	 * @param name - название модели
	 * @param productId - id продукта
	 * @param wayId - способ подачи заявки
	 * @return
	 * @throws ModelException
	 */
	public AIModelEntity createModel(String name,Integer productId, Integer wayId) throws ModelException;

	/**
	 * удаляет активную модель
	 * @param modelId - id модели
	 * @throws ModelException
	 */
	public void removeActiveModel(int modelId) throws ModelException;

	/**
	 * выполняет модель в асинхронном режиме
	 * @param source - заявки
	 * @param prmInTarget 
	 * @param actionContext - контекст
	 * @param target
	 * @param progress
	 * @param modelId -id модели
	 * @return
	 * @throws ActionException
	 */
	public Future<Map<String, Object>> executeModelsAsync(Iterator<CreditRequest> source, int prmInTarget, ActionContext actionContext,
			List<String> target, ExecutionProgress progress, int modelId) throws ActionException;

	/**
	 * выполняет модель в синхронном режиме
	 * @param source - заявки
	 * @param prmInTarget 
	 * @param actionContext - контекст
	 * @param target
	 * @param progress
	 * @param modelId -id модели
	 * @throws ActionException
	 */
	public void executeModelsSync(Iterator<CreditRequest> source, int prmInTarget, ActionContext actionContext,
			List<String> target, ExecutionProgress progress, int modelId) throws ActionException;

	List<AIModel> findModels(String target, Boolean active, String version,
			String bodyTpl, Integer productId, Integer wayId);

	AIModel addModelParam(int modelId, AIModelParamEntity source) throws ActionException;

	AIModel saveModelParam(AIModelParamEntity source) throws ActionException;

	AIModel deleteModelParam(int modelId, String name) throws ActionException;

	void loadModelParams(int modelId, String customKey, Map<String, Object> dest);

	void closeModelSession(AIModel model, String customKey);

	List<AIModelParamEntity> openModelSession(int modelId, String customKey);

	void applyModelParams(int modelId, String customKeys);

	List<AIModelParamEntity> revertModelParams(int modelId, String customKeys);

	List<AIModelParamEntity> getModelParams(int modelId, String customKey);

	public int saveModelParamValues(int modelId, String customKey, Map<String, Object> mpVars, Integer runId);

	void checkModelParam(int modelId, String customKey, String paramName,
			String paramType);

	void checkDefaultModelParams(int modelId, String customKey);

	void checkModelParam(AIModelEntity model, String customKey,
			String paramName, String paramType);

	boolean isVariableStandart(String varName);

	boolean isVariableEditable(String varName);

    /**
     * Ищем активную модель по названию, продукту и способу запуска
     * @param modelKey - обязательно
     * @param productId - если null, то берем модель с данным названием для любого продукта
     * @param wayId - если null, то берем модель с данным названием для productId с любым способом запуска. 
     * @return
     */
	public AIModel findActiveModel(String modelKey, Integer productId, Integer wayId);

	/**
	 * Возвращаем список стратегий, у которых есть сохраненные результаты
	 * @return
	 */
	public List<AIModel> findModelsWithResults();

	void deleteResults(String businessObjectClass, Integer businessObjectId);

	public List<Map> listLastParamValues(String businessObjectClass, Integer businessObjectId);

	public AIModel getLastModel(int paramId);

	public void saveModelParams(int modelId, String customKey, Map<String, Object> mpVars);


}
