package ru.simplgroupp.dao.interfaces;

import java.util.Date;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PhonePayEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;

@Local
public interface PhonePayDAO {

	/**
	 * сохраняем платежи за телефон
	 * @param summary - суммарная информация
	 * @param periodNumber - номер периода
	 * @param firstDate - дата начала
	 * @param count - кол-во
	 * @param type - вид 
	 * @param sum - сумма
	 * @throws KassaException
	 */
	void savePhonePays(PhonePaySummaryEntity summary,Integer periodNumber,Date firstDate,
			Integer count,Integer type,Double sum) throws KassaException;
	
	/**
	 * возвращаем платеж за телефон по id
	 * @param id - id платежа
	 * @return
	 */
	PhonePayEntity getPhonePayEntity(Integer id);
}
