package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.transfer.AIModel;

@Local
public interface AIModelDAO {

	AIModelEntity getModel(int id);

	AIModel getModel(int id, Set options);

	AIModelEntity saveModelEntity(AIModelEntity source);

	AIModelParamEntity findModelParam(int modelId, String name, String customKey);

	void deleteModelParam(int modelId, String name);

	void saveModelParam(AIModelParamEntity source);

	List<AIModelParamEntity> listModelParamsCK(int modelId, String customKey);

	void deleteModelParams(int modelId, String customKey);

	/**
	 * Копирует параметры для рабочего запуска. Только те, которых ещё нет в рабочем запуске.
	 * @param fromId
	 * @param fromCustomKey
	 * @param toId
	 * @param toCustomKey
	 */
	public void copyModelParams(int fromId, String fromCustomKey, int toId,
			String toCustomKey);

	int getNextRunId();

	void putModelParamValue(int runId, int paramId, String paramValue);

	/**
	 * Модели, для которых есть хотя бы один сохранённый результат запуска
	 * @return
	 */
	public List<Integer> listModelIdsWithResults();

	/**
	 * Удаляет все сохраненные результаты для данного объекта
	 * @param businessObjectClass
	 * @param businessObjectId
	 */
	public void deleteResults(String businessObjectClass, Integer businessObjectId);

	/**
	 * Список переменных из модели с их значениями. name, description, datatype, value
	 * @param businessObjectClass
	 * @param businessObjectId
	 * @return
	 */
	public List<Object[]> listLastResults(String businessObjectClass,
			Integer businessObjectId);

	public Integer findModelIdByParam(int paramId);

}
