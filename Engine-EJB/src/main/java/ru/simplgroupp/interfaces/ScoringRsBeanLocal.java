package ru.simplgroupp.interfaces;

import java.util.Date;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;

public interface ScoringRsBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
	public static final String SYSTEM_NAME = "rusStandart";
	public static final String SYSTEM_DESCRIPTION = "Запросы в Русский Стандарт";
	
	/**
	 * новый запрос в Русский стандарт
	 * @param cre - заявка
	 * @param isWork - рабочий или тестовый
	 * @param requestPayment - вызываем сервис платежей 
	 * @param requestFMS - вызываем сервис фмс
	 * @param requestFSSP - вызываем сервис фссп
	 * @param requestFNS - вызываем сервис фнс
	 * @param requestVerify - вызываем верификацию
	 * @param requestScoring - вызываем скоринг
	 * @param requestAntifrod - вызываем антифрод
	 * @param cacheDays - сколько дней заявка хранится в кеше
	 * @param creditReportDetalization - детализация отчета
	 */
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,Boolean requestPayment,Boolean requestFMS,Boolean requestFSSP,
			Boolean requestFNS,Boolean requestVerify,Boolean requestScoring,Boolean requestAntifrod,Integer cacheDays,
			Integer creditReportDetalization)	throws ActionException;
	
	/**
	 * создаем файл удаления для выгрузки
	 * @param sendingDate - дата
	 * @param creditId - кредит, который удаляем
	 * @param isWork - рабочий или тестовый
	 * @return
	 * @throws KassaException
	 */
	public UploadingEntity createDeleteFileForUpload(Date sendingDate,Integer creditId,Boolean isWork) throws KassaException;
			
}

