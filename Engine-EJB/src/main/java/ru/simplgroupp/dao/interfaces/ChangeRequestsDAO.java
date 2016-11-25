package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.ChangeRequestsEntity;
import ru.simplgroupp.transfer.ChangeRequests;

@Local
public interface ChangeRequestsDAO {

	 /**
     * список заявок на редактирование с параметрами
     * @param creditRequestId - кредитная заявка
     * @param isEdited - статус заявки на редактирование
     * @param date - дата
     * @param userId - пользователь, принявший заявку
      */
    List<ChangeRequests> listChangeRequests(Integer creditRequestId,Integer isEdited,Date date,Integer userId);
    /**
     * возвращаем запрос на изменение данных по id
     * @param id - id запроса
     * @return
     */
    ChangeRequestsEntity getChangeRequest(Integer id);
    /**
     * сохраняем запись для изменения данных пользователя
     * @param chRequestId     - id заявки на изменение
     * @param bConfirm - отредактирована ли анкета
     */
	ChangeRequests saveChangeRequest(Integer chRequestId, boolean bConfirm);
	/**
     * сохраняем запись для изменения данных пользователя
     *
     * @param chRequestId     - id заявки на изменение
     * @param creditRequestId - id кредитной заявки
     * @param userId          - id пользователя, принявшего заявку на изменение
     * @param reqDate         - дата, время
     * @param description     - что надо поменять
     */    
	ChangeRequests saveChangeRequest(Integer chRequestId, int creditRequestId,
			int userId, Date reqDate, String description);
	
	/**
	 * сохраняем запись для изменения данных пользователя
	 * @param changeRequest - изменение данных
	 * @return
	 */
	ChangeRequestsEntity saveChangeRequest(ChangeRequestsEntity changeRequest);
}
