package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.BusinessListEntity;

/**
 * DAO для бизнес-списков
 * @author irina
 *
 */
@Local
public interface ListDAO {

	public BusinessListEntity getList(int id);

	BusinessListEntity saveList(BusinessListEntity source);

	List<BusinessListEntity> findLists(String businessObjectClass, String subtype, Integer isExplicit);

	void clearListItems(int listId);

	void addListItems(BusinessListEntity biz, List source);

	boolean isItemInList(Integer listId, Integer itemId);

	void removeListItem(Integer listId, Integer itemId);

	void addListItem(Integer listId, Integer itemId);

	void saveCond(int listId, String cond);

	String loadCond(int listId);

	Integer countItems(int listId);

	/**
	 * Удаляем список
	 * @param biz
	 */
	public void removeList(BusinessListEntity biz);

	/**
	 * Удаляем список
	 * @param listId
	 */
	void removeList(int listId);

}
