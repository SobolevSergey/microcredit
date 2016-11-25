package ru.simplgroupp.interfaces.service;

import javax.ejb.Local;

import ru.simplgroupp.dao.interfaces.ListContainerDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.util.ListContainer;

@Local
public interface ListService {

	/**
	 * Создаём явный список
	 */
	BusinessListEntity createAndSave(ListContainer listCon, ListContainerDAO dao) throws ActionException;

	BusinessListEntity save(BusinessListEntity biz, ListContainer listCon,
			ListContainerDAO dao) throws ActionException;

	void addItemToList(BusinessListEntity biz, Integer itemId);

	void removeItemFromList(BusinessListEntity biz, Integer itemId);

	BusinessListEntity createAndSave(ListContainer listCon,
			ListContainerDAO dao, String listName, String subType, Integer isExplicit) throws ActionException;

	void loadList(int listId, ListContainer dest) throws ActionException;

	Integer countItems(int listId);

	/**
	 * Создаем неявный список
	 * @param listCon
	 * @param dao
	 * @return
	 * @throws ActionException
	 */
	BusinessListEntity createAndSaveImplicit(ListContainer listCon,
			ListContainerDAO dao, String subType ) throws ActionException;

	/**
	 * Удаляем список
	 * @param listId
	 */
	public void removeList(int listId);

}
