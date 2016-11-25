package ru.simplgroupp.dao.interfaces;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.UploadingErrorEntity;

@Local
public interface UploadingErrorDAO {

	/**
	 * добавляем ошибки по выгрузке
	 * @param uploadingId - id выгрузки
	 * @param creditId - id кредита
	 * @param description - описание ошибки
	 * @return
	 * @throws KassaException
	 */
	UploadingErrorEntity newUploadingError(Integer uploadingId,Integer creditId,Integer creditRequestId,
			String description) throws KassaException;
	
	/**
	 * возвращаем запись ошибки выгрузки по id
	 * @param id - id записи ошибки выгрузки
	 * @return
	 */
	UploadingErrorEntity getUploadingErrorEntity(Integer id);
}
