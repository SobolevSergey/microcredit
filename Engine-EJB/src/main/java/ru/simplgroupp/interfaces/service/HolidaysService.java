package ru.simplgroupp.interfaces.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.persistence.HolidaysEntity;

public interface HolidaysService {

	/**
	 * возвращает запись из таблицы праздников, если сегодня праздник, либо он наступит завтра, а сегодня предпраздничное после обеда
	 * @param date - дата
	 */
	public HolidaysEntity getHolidays(Date date);
	/**
	 * возвращает есть ли сегодня праздник, либо он наступит завтра, а сегодня предпраздничное после обеда
	 * @param date - дата
	 */
	public boolean isHoliday(Date date);
	/**
	 * возвращает кол-во праздничных дней, если сегодня праздник, либо он наступит завтра, а сегодня предпраздничное после обеда
	 * @param date - дата
	 */
	public int getDaysOfHolidays(Date date);
	/**
	 * добавляем диапазон праздничных (выходных дней)
	 * @param databeg - дата начала
	 * @param dataend - дата окончания
	 * @param kind - что это, праздник или выходной
	 * @param name - название праздников (опционально)
	 */
	public HolidaysEntity addHolidays(Date databeg,Date dataend,Integer kind,String name);
	/**
	 * удаляем запись с праздничными или выходными днями
	 * @param holidaysId - id из таблицы
	 */
	public void deleteHolidays(int holidaysId);
	/**
	 * список праздников по параметрам
	 * @param month - месяц
	 * @param year - код 
	 * @param name - название
	 * @param kind - что это, праздник или выходной
	 */
	public List<HolidaysEntity> listHolidays(Integer month,Integer year,String name,Integer kind);
	/**
	 * сколько праздников по параметрам
	 * @param month - месяц
	 * @param year - код 
	 * @param name - название
	 * @param kind - что это, праздник или выходной
	 */
	public int countHolidays(Integer month,Integer year,String name,Integer kind);
	/**
	 * возвращает запись по id
	 * @param id
	 * @param options
	 */
	public HolidaysEntity getHolidaysEntity(Integer id, Set options);
}
