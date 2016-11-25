package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.toolkit.common.DateRange;

@Local
public interface ScoringDAO {

	/**
	 * ищем скоринги
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param scoringDate - дата верификации
	 * @return
	 */
	List<ScoringEntity> findScorings(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange scoringDate);
	
	/**
	 * сохраняем скоринг
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param modelId - id модели
	 * @param modelCode - код модели
	 * @param score - оценка
	 * @param error - ошибка
	 * @param scoreRisk - оценка риска
	 */
	void saveScoring(Integer creditRequestId,Integer peopleMainId,Integer partnerId,Integer modelId,
			String modelCode, Double score,String error,Double scoreRisk);
	
	/**
	 * возвращаем скоринг по id
	 * @param scoringId - id скоринга
	 * @return
	 */
	ScoringEntity getScoringEntity(Integer scoringId);
}
