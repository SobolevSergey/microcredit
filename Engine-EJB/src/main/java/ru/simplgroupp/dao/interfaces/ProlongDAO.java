package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Prolong;

@Local
public interface ProlongDAO {

	/**
	 * сохраняем кредит
	 * @param cred - кредит
	 * @return
	 */
	public ProlongEntity saveProlongEntity(ProlongEntity prolong);
	 /**
     * возвращает продление по id
     * @param prolongId - id продления
     * @return
     */
	ProlongEntity getProlongEntity(int prolongId);
	/**
     * возвращает продление по id
     * @param prolongId - id продления
     * @param options - что инициализируем
     * @return
     */
	Prolong getProlong(int prolongId, Set options);

	/**
	 * удалить продление
	 * @param id - id продления
	 */
	void deleteProlong(int id);	
	/**
     * ищем продление
     * @param creditId - кредит
     * @param longDate - дата 
     * @param isActive - статус активности
     */	
	List<ProlongEntity> findProlong(int creditId, DateRange longDate,
			Integer isActive);

	/**
	 * Ищем продление в статусе черновика
	 * @param creditId
	 * @return
	 */
	ProlongEntity findProlongDraft(int creditId);
}
