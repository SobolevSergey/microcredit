package ru.simplgroupp.interfaces.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Uploading;

public interface UploadingService {

	/**
     * возвращает список кредитных заявок для выгрузки в КБ
     * @param sendingDate - дата выгрузки
     * @param creditRequestOnly - делаем только по заявкам
     */
    List<CreditRequestEntity> getListForUpload(DateRange sendingDate,Integer creditRequestOnly);
    /**
     * возвращает список кредитных заявок для выгрузки в КБ
     * @param sendingDate - дата выгрузки
     * @param creditIds - id кредитов
     */
    List<CreditRequestEntity> getListForUpload(DateRange sendingDate,List<Integer> creditIds);
    /**
     * возвращает список кредитных заявок для выгрузки в КБ
     * для случаев, когда в выгрузке передаем всю информацию сразу,
     * по всем кредитам, неазвисимо от того, передавали ее уже или нет
     * @param creditOnly - берем заявки только с кредитами
     */
    List<CreditRequestEntity> getListForUploadAll(Integer creditOnly);
    /**
     * возвращает список кредитных заявок для выгрузки в КБ - ежедневно
     * @param sendingDate - дата выгрузки
     */
    List<CreditRequestEntity> getListForUploadDaily(Date sendingDate);
	/**
     * выбираем последнюю выгрузку информации партнеру
     * @param partnerId - партнер
     */
    UploadingEntity getLastUploading(Integer partnerId);
    /**
     * выбираем последнюю выгрузку информации партнеру по типу
     * @param partnerId - партнер
     * @param type - тип выгрузки
      */
    UploadingEntity getLastUploading(Integer partnerId,Integer type);

    /**
     * сохраняет запись с выгружаемой информацией в КБ
     * @param upl - заголовок выгрузки
     */
    UploadingEntity saveUpload(UploadingEntity upl) throws KassaException;
    /**
     * сохраняем запись выгрузки с информацией по записи
     * @param detail - запись в выгрузке
     * @throws KassaException
     */
    UploadingDetailEntity saveDetail(UploadingDetailEntity detail) throws KassaException;
    /**
     * ставим признак первоначальной загрузки
     * @param sendingDate - дата выгрузки
     */
    void updateAfterUpload(Date sendingDate);

    /** 
     * ищем последний уникальный номер выгрузки по партнеру
     * @param partnerId - партнер
     */
    Integer findMaxUploadId(Integer partnerId);
    /** 
     * ищем последний уникальный номер выгрузки по партнеру и виду выгрузки
     * @param partnerId - партнер
     * @param typeId - вид выгрузки
     */
    Integer findMaxUploadId(Integer partnerId,Integer typeId);
    /**
     * ищем выгруженную запись в файле с деталями
     * @param creditRequestId - заявка
     * @param uploadingId - выгрузка
     * @param status - статус
     */
    public List<UploadingDetailEntity> findUploadingDetail(Integer creditRequestId,Integer uploadingId,Integer status);
    /**
     * сохраняем запись в файле с деталями
     * @param creditRequest - заявка
     * @param uploading - выгрузка
     * @param status - статус
     */
    public void saveUploadingDetail(CreditRequestEntity creditRequest,UploadingEntity uploading,Integer status);
    /**
     * меняем статус для uploading_detail
     * @param creditRequest - заявка
     * @param uploading - выгрузка
     * @param status - статус
     */
    public void changeUploadingDetail(CreditRequestEntity creditRequest,UploadingEntity uploading,Integer status);
    /**
     * список выгрузок
     * @param nFirst - первый номер
     * @param nCount - кол-во на стр.
     * @param options - опции для инициализации
     * @param uploadingDate - дата выгрузки
     * @param responseDate - дата ответа
     * @param partnerId - партнер
     * @param uploadId - № выгрузки
     * @param status - статус выгрузки
     * @param typeId - вид выгрузки
     */
    public List<Uploading> listUploading(int nFirst, int nCount,Set options,DateRange uploadingDate,DateRange responseDate,Integer partnerId,Integer uploadId,Integer status,Integer typeId);
    /**
     * сколько было выгрузок по условиям
     * @param uploadingDate - дата выгрузки
     * @param responseDate - дата ответа
     * @param partnerId - партнер
     * @param uploadId - № выгрузки
     * @param status - статус выгрузки
     * @param typeId - вид выгрузки
     */
    public int countUploading(DateRange uploadingDate,DateRange responseDate,Integer partnerId,Integer uploadId,Integer status,Integer typeId);
    
    /**
     * возвращаем Uploading
     * @param id - заголовок выгрузки
     */
    public Uploading getUploading(Integer id,Set options);
    /**
	 * возвращает диапазон дат для выгрузки
	 * @param sendingDate - дата выгрузки
	 * @param partnerId - id партнера
	 * @param uploadTypeId - что выгружаем
	 * @return
	 */
	public DateRange getDatesForUpload(Date sendingDate,Integer partnerId,Integer uploadTypeId);
    /**
     * добавляем (изменяем) запись выгрузки
     * @param uploading - запись выгрузки
     * @param partnersId - парнтер
     * @param uploadTypeId - вид выгрузки
     * @param name - название
     * @param info - передаваемые данные
     * @param dateUploading - дата выгрузки
     * @param statusId - статус
     * @param responseDate - дата ответа
     * @param report - данные ответа
     * @param recordsAll - всего записей
     * @param recordsCorrect - корректных записей
     * @param recordsError - записей с ошибками
     * @param isCreated - только создан
     * @return
     */
	public UploadingEntity newUploading(UploadingEntity uploading,Integer partnersId,Integer uploadTypeId,
			String name,String info,Date dateUploading,Integer statusId,Date responseDate,String report,
			Integer recordsAll,Integer recordsCorrect,Integer recordsError,boolean isCreated) throws KassaException;
	
	/**
	 * добавляем ошибки по выгрузке
	 * @param uploadingId - id выгрузки
	 * @param creditId - id кредита
	 * @param description - описание ошибки
	 * @return
	 * @throws KassaException
	 */
	public UploadingErrorEntity newUploadingError(Integer uploadingId,Integer creditId,Integer creditRequestId,
			String description) throws KassaException;
}
