package ru.simplgroupp.interfaces;

import java.util.Date;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.UploadingEntity;

public interface ScoringEquifaxBeanLocal extends PluginSystemLocal, ScoringBeanLocal {
	
	public static final String SYSTEM_NAME = "equifax";
	public static final String SYSTEM_DESCRIPTION = "Запросы в Эквифакс";
   
	/**
	 * создаем файл для выгрузки информационной части (кредитных заявок)  
	 * @param sendingDate - дата
	 * @param isWork - рабочий или тестовый
	 * @return
	 * @throws KassaException
	 */
	public  UploadingEntity createFileForUploadCreditRequest(Date sendingDate, Boolean isWork)	throws KassaException;
	/**
	 * создаем коррекционный файл для выгрузки
	 * @param sendingDate - дата
	 * @param creditId - кредит, по которому делаем коррекцию
	 * @param isWork - рабочий или тестовый
	 * @return
	 * @throws KassaException
	 */
	public UploadingEntity createCorrectionFileForUpload(Date sendingDate,Integer creditId,Boolean isWork) throws KassaException;
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
