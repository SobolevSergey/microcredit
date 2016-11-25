package ru.simplgroupp.interfaces;


import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;

public interface ScoringNBKIBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
	public static final String SYSTEM_NAME = "nbki";
	public static final String SYSTEM_DESCRIPTION = "Запросы в КБ НБКИ";
	
	/**
	 * делаем запрос в КБ
	 * @param cre - кредитная заявка
	 * @param isWork - рабочий или тестовый сервис
	 * @param requestScoring - запрашиваем ли скоринг
	 * @param urlAdditional - url для запросов
	 * @param cacheDays - сколько дней держим в кеше
	 * @param useSSL - используем ли SSL
	 * @return
	 * @throws ActionException
	 */
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,Boolean requestScoring,
			String urlAdditional,Integer cacheDays,Boolean useSSL)	throws ActionException;
			
}
