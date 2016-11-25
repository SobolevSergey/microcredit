package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;

public interface ScoringOkbCaisBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
	public static final String SYSTEM_NAME = "okbCais";
	public static final String SYSTEM_DESCRIPTION = "Запросы в ОКБ к сервису CAIS";
	/**
	 * запрос
	 * @param cre - кредитная заявка
	 * @param isWork - рабочий сервис или нет
	 * @param cacheDays - дней в кеше
	 * @param numberFunction - номер функции
	 * @return
	 * @throws ActionException
	 */
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays, Integer numberFunction) throws ActionException;
				
}
