package ru.simplgroupp.dao.interfaces;

import java.util.List;

import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.util.ListContainer;

public interface ListContainerDAO<T extends Identifiable> {
	/**
	 * Посчитать количество записей для данного списка
	 * @param listCon - контейнер данных
	 * @return
	 */
	public int countData(ListContainer<T> listCon);
	
	/**
	 * Список записей для данного списка
	 * @param listCon - контейнер данных
	 * @return
	 */	
	public List<T> listData(int nFirst, int nCount, ListContainer<T> listCon);
	
	/**
	 * Список id записей для данного списка
	 * @param listCon - контейнер данных
	 * @return
	 */	
	public List<? extends Number> listIds(int nFirst, int nCount, ListContainer<T> listCon);	
	
	/**
	 * Создать список данного типа
	 * @param clz
	 * @return
	 */
	public <T extends ListContainer<? extends Identifiable>> T genData(Class<T> clz);

}
