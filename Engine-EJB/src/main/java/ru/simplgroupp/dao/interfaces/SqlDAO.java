package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SqlDAO {
	
	/**
	 * Удаляет view, если оно существует
	 * @param viewName
	 */
	public void dropView(String viewName);

	/**
	 * Существует ли данная таблица или view
	 */
	public boolean isTableOrViewExists(String tableName);

	/**
	 * Создаёт или заменяет view с данным именем
	 * @param viewName
	 * @param sql
	 */
	public void createView(String viewName, String sql);

	/**
	 * Выдает результаты выборки из заданной таблицы по заданным условиям
	 * @param table
	 * @param where
	 * @param columns
	 * @return
	 */
	public List<Object[]> selectSingle(String table, String where, String columns);
}
