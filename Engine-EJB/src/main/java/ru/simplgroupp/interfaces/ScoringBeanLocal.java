package ru.simplgroupp.interfaces;

import java.util.Date;
import java.util.List;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;

public interface ScoringBeanLocal {

	/**
	 * новый запрос со стандартными параметрами
	 * 
	 * @param cre - заявка
	 * @param isWork - рабочий или тестовый
	 * @param cacheDays - сколько дней кешируется запрос
	 */
	public RequestsEntity newRequest(CreditRequestEntity cre,Boolean isWork,Integer cacheDays) throws ActionException;
	 /**
     * разбирает и сохраняет ответ 
     * 
     * @param req - запрос
     * @param answer - ответ строкой
     */
	public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException;
	/**
	 * выгружаем историю в КБ
	 * 
	 * @param uploading - запись в таблице выгрузки
	 * @param sendingDate - дата, на которую берем изменения
	 * @param isWork - рабочий или тестовый сервис
	 */
	public void uploadHistory(UploadingEntity uploading,Date sendingDate,Boolean isWork) throws KassaException;
	/**
	 * проверяем статус выгрузки
	 * 
	 * @param uploading - запись в таблице выгрузки 
	 * @param isWork - рабочий или тестовый сервис
	 */
	public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException;
	/**
	 * делаем файл для выгрузки
	 * 
	 * @param sendingDate - дата, с которой начинаем выгрузку
	 * @param isWork - рабочий или тестовый сервис
	 */
	public UploadingEntity createFileForUpload(Date sendingDate,Boolean isWork) throws KassaException;
	/**
	 * делаем файл для выгрузки
	 * 
	 * @param sendingDate - дата, с которой начинаем выгрузку
	 * @param isWork - рабочий или тестовый сервис
	 * @param creditIds - id для выгрузки
	 */
	public UploadingEntity createFileForUpload(Date sendingDate,Boolean isWork,List<Integer> creditIds) throws KassaException;

}
