package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;

@Local
public interface UploadingDetailDAO {

	  /**
     * меняем статус для uploading_detail
     * @param creditRequest - заявка
     * @param uploading - выгрузка
     * @param status - статус
     */
    void changeUploadingDetail(CreditRequestEntity creditRequest,UploadingEntity uploading,Integer status);
    
    /**
     * ищем выгруженную запись в файле с деталями
     * @param creditRequestId - заявка
     * @param uploadingId - выгрузка
     * @param status - статус
     */
    List<UploadingDetailEntity> findUploadingDetail(Integer creditRequestId,Integer uploadingId,Integer status);
    /**
     * сохраняем запись в файле с деталями
     * @param creditRequest - заявка
     * @param uploading - выгрузка
     * @param status - статус
     */
    void saveUploadingDetail(CreditRequestEntity creditRequest,UploadingEntity uploading,Integer status);
    /**
     * сохраняем запись выгрузки с информацией по записи
     * @param detail - запись в выгрузке
     * @throws KassaException
     */
    UploadingDetailEntity saveDetail(UploadingDetailEntity detail) throws KassaException;
    /**
     * ищем запись в выгрузке по id
     * @param id - id записи в выгрузке
     * @return
     */
    UploadingDetailEntity getUploadingDetailEntity(Integer id);
    
}
