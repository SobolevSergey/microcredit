package ru.simplgroupp.interfaces;

import java.io.UnsupportedEncodingException;

public interface BIKUpdaterLocal {
	
	/**
	 * Автоматическое обновление таблицы bank, накатываются изменения из последнего справочника ЦБ BIK за неделю
	 */
	public void updateBanks();
	
	/**
	 * Обновление таблицы bank из файла dbf
	 * @param dbf представление файла dbf в виде массива
	 * @throws UnsupportedEncodingException
	 */
	public void updateBanks(byte[] dbf) throws UnsupportedEncodingException;
	
}
