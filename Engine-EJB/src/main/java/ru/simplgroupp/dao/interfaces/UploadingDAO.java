package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Uploading;

@Local
public interface UploadingDAO {

	 /**
     * возвращаем UploadingEntuty
     * @param id - заголовок выгрузки
     */
    public UploadingEntity getUploadingEntity(Integer id);
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
     * ставим признак первоначальной загрузки
     * @param sendingDate - дата выгрузки
     */
    void updateAfterUpload(Date sendingDate);
    
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
}
