package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CallBackEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PeopleBonusEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleFriendEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.RefBonusEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.transfer.CallBack;
import ru.simplgroupp.transfer.PeopleMain;

@Local
public interface PeopleDAO extends ListContainerDAO<PeopleMain> {

	/**
	 * ищем человека по id
	 * @param id - id человека
	 * @return
	 */
	PeopleMainEntity getPeopleMainEntity(int id);
	/**
	 * ищем человека по id
	 * @param id - id человека
	 * @param options - что инициализируем 
	 * @return
	 */
	PeopleMain getPeopleMain(int id, Set options);

	/**
	 * добавляем бонусы
	 * @param peopleMainId - id человека
	 * @param bonusId -id бонуса 
	 * @param operationId - id операции
	 * @param amount - кол-во
	 * @param amountrub - кол-во в рублях
	 * @param dateBonus - дата события
	 * @param creditId - id кредита, за который добавляем бонус
	 * @param peopleMainBonusId - id человека, за которого добавляем бонус
	 * @return
	 */
	PeopleBonusEntity addBonus(PeopleMainEntity peopleMainId,
			RefBonusEntity bonusId, ReferenceEntity operationId,
			Double amount, Double amountrub, Date dateBonus, CreditEntity creditId,
			PeopleMainEntity peopleMainBonusId);
	
	/**
	     * сумма бонусов в системе
	     * @param peopleMainId - id человека
	     * @return
	     */
	Double getPeopleBonusInSystem(Integer peopleMainId);
	
	/**
     * обновление настройки бонусов
     * @param peopleMainId - id человека
     * @return
     */
	int updatePeopleBonusProperties(Integer peopleMainId, Integer bonuspay);
	
	/**
     * получить настройку оплаты бонусами
     * @param peopleMainId - id человека
     * @return
     */
	Integer getUserBonusPayProperties(Integer peopleMainId);
	/**
	 * удаление друга
	 * @param id - id друга
	 */
	public void deleteFriend(Integer id);
	/**
	 * возвращаем ПД по id
	 * 
	 * @param id - id ПД человека
	 * @return
	 */
	public PeoplePersonalEntity getPeoplePersonalEntity(Integer id);
	/**
	 * возвращаем доп.данные по id
	 * 
	 * @param id - id доп.данных человека
	 * @return
	 */
	public PeopleMiscEntity getPeopleMisc(Integer id);

	/**
	 * возвращаем другие доп данные по id
	 * @param id - id
	 * @return
	 */
	public PeopleOtherEntity getPeopleOther(Integer id);
	/**
     * возвращаем запись из контактов по id
     * @param id - id контакта
     */
	public PeopleContactEntity getPeopleContactEntity(int id);
	/**
	 * ищем занятость по id
	 * 
	 * @param id - id занятости
	 * @return
	 */
	public EmploymentEntity getEmployment(Integer id);
	 /**
     * ищем данные по обратному звонку
     * @param id - id данных по обратному звонку
     * @return
     */
    public CallBackEntity getCallBackEntity(Integer id);
	/**
	 * ищем данные по обратному звонку
	 * @param id - id данных по обратному звонку
	 * @return
	 */
	public CallBack getCallBack(Integer id);
    /**
     * ищем данные по обратному звонку
     * @param id - id данных по обратному звонку
	 * @param options - инициализируемые параметры
     */
    public CallBack getCallBack(Integer id, Set options);
    /**
     * ищем друга по id
     * @param id - id друга
     * @return
     */
    public PeopleFriendEntity getPeopleFriend(Integer id);
    /**
     * ищем человека по идентификатору - инн или снилс
     * @param inn - инн
     * @param snils - снилс
     */
	public PeopleMainEntity findPeopleMain(String inn, String snils);

	/**
	 * Метод удаляет один бонус
	 *
	 * @param peopleMainID человек
	 * @param bonusCode    код бонуса
	 */
	void removeBonus(Integer peopleMainID, Integer bonusCode);

	/**
	 * Метод находит все операции бонусов при платеже по кредиту
	 * @param credit_id
	 * @param paydate
	 * @param ref_id тип операции
	 * @return
	 */
	public List<PeopleBonusEntity> findCreditPayBonus(Integer credit_id, Date paydate, Integer ref_id);

	/**
	 * Поиск человека по ИИН
	 *
	 * @param iin ИНН
	 * @return человек если найден, null если нет
	 */
	PeopleMainEntity findPeopleMainByIin(String iin);

	/**
	 * Достает персональные данные людей по ID
	 *
	 * @param peopleMainIDs список ID людей
	 * @return найденые персональные данные
	 */
	List<PeoplePersonalEntity> getPeoplePersonalByPeopleMainIDs(List<Integer> peopleMainIDs);

	/**
	 * Метод достает ID людей по роли и eventCode
	 *
	 * @param eventCode код
	 * @param roleID    ID роли
	 * @return список ID людей
	 */
	List<Integer> getPeopleMainIDsByRoleIDAndEventCodeID(Integer eventCode, Integer roleID);
}
