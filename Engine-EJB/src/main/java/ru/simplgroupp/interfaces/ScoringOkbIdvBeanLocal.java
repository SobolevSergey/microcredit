package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;

public interface ScoringOkbIdvBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
	public static final String SYSTEM_NAME = "okbIdv";
	public static final String SYSTEM_DESCRIPTION = "Запросы в ОКБ к сервису IDV";
	
	/**
	 * запрос соап
	 * @param cre - заявка
	 * @param isWork - рабочий сервис или тестовый
	 * @param cacheDays - сколько дней в кеше
	 * @return
	 * @throws ActionException
	 */
	RequestsEntity newRequestSoap(CreditRequestEntity cre,Boolean isWork,
			Integer cacheDays) throws ActionException;
}
