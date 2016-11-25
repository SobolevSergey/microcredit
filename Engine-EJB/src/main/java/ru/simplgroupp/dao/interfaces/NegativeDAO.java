package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.NegativeEntity;

@Local
public interface NegativeDAO {

	/**
	 * сохраняем негатив
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param article - статья
	 * @param module - модуль
	 * @param yearArticle - год статьи
	 */
	void saveNegative(Integer creditRequestId,Integer peopleMainId,Integer partnerId,String article,
			String module,Integer yearArticle);
	
	/**
	 * ищем записи негатива
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param article - статья
	 * @param yearArticle - год статьи
	 * @return
	 */
	List<NegativeEntity> findNegative(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,String article,Integer yearArticle);
	
	/**
	 * возвращаем запись негатива по id
	 * @param negativeId - id негатива
	 * @return
	 */
	NegativeEntity getNegativeEntity(Integer negativeId);
}
