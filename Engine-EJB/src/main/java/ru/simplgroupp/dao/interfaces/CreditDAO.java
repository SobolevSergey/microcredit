package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditSumsEntity;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Refinance;

@Local
public interface CreditDAO extends ListContainerDAO<Credit> {

	/**
	 * сохраняем кредит
	 * @param cred - кредит
	 * @return
	 */
	public CreditEntity saveCreditEntity(CreditEntity cred);

	/**
	 * удаляем кредит
	 * @param id - id кредита
	 */
	void removeCredit(int id);
	/**
	 * удаляем кредиты партнера
	 * @param partnersId - id партнера
	 * @throws KassaException
	 */
	void removePartnerCredits(Integer partnersId) throws KassaException;

	/**
	 * ищем кредит по id
	 * @param id - id кредита
	 * @param options - что инициализируем
	 * @return
	 * @throws KassaException
	 */
	Credit getCredit(int id, Set options);

	/**
	 * ищем credit по id
	 * @param creditId - id кредита
	 * @return
	 */
	CreditEntity getCreditEntity(int creditId);
	/**
	 * ищем детали кредита по id
	 * @param id - id деталей
	 * @return
	 */
    CreditDetailsEntity getCreditDetailsEntity(int id); 
	/**
	 * сохраняем creditDetails
	 * @param creditDetail - данные creditDetails
	 * @return
	 */
	CreditDetailsEntity saveCreditDetails(CreditDetailsEntity creditDetail);
	/**
	 * сохраняем разбивку сумм по кредиту
	 * @param creditSums - данные creditSums
	 * @return
	 */
	CreditSumsEntity saveCreditSums(CreditSumsEntity creditSums);
	/**
     * возвращает сумму дней продления по данному кредиту
     * @param creditId - кредит
     */
	public int getProlongDaysSum(int creditId);
	
	/**
	 * возвращает сумму денег, которые есть на счете человека в системе
	 * @param peopleMainId - человек
	 */
	public Double getPeopleSumsInSystem(Integer peopleMainId);	
	
    /**
     * По номеру кредита возвращает CreditEntity
     * @param accountNumber
     * @return CreditEntity
     */
    public CreditEntity findCreditByAccountNumber(String accountNumber);

   
	/**
	 * удалить рефинансирование
	 * @param id - id рефинансирования
	 */
	void deleteRefinance(int id);	
	/**
	 * ищем рефинансирование в статусе черновика
	 * @param creditId - кредит
	 */
	public RefinanceEntity findRefinanceDraft(int creditId);
	 /**
     * возвращает рефинансирование по id
     * @param refinanceId - id рефинансирования
     * @return
     */
	RefinanceEntity getRefinanceEntity(int refinanceId);
	/**
     * возвращает рефинансирование по id
     * @param refinanceId - id рефинансирования
     * @param options - что инициализируем
     * @return
     */
	Refinance getRefinance(int refinanceId, Set options);
	/**
	 * первый кредит, выданный системой
	 * @return
	 */
	CreditEntity findFirstSystemCredit();

	/**
	 * кредиты человека
	 * @param peopleMainId - id человека
	 * @param partner - id партнера
	 * @param issameorg - своя организация или нет
	 * @param isover - завершен или нет
	 * @return
	 */
	List<CreditEntity> findCreditByPeople(int peopleMainId, int partner,
			boolean issameorg, boolean isover);

    
    /**
     * ищем рефинансирование
     * @param creditId - кредит
     * @param refDate - дата 
     * @param isActive - статус активности
     */
	List<RefinanceEntity> findRefinance(int creditId, DateRange refDate,
			Integer isActive);

	
    /**
     * сохраняем рефинансирование
     * @param source - рефинансирование
     * @return
     */
	RefinanceEntity saveRefinance(RefinanceEntity source);
	
	/**
	 * ставим кредитам партнера архивность
	 * @param partnersId - id партнера
	 */
	void makePartnerCreditsNotActive(Integer partnersId,Integer peopleMainId);
	
	/**
	 * ищем детали кредита по другому id
	 * @param anotherId - другой id 
	 * @return
	 */
	CreditDetailsEntity getCreditDetailByAnotherId(Integer anotherId);

}
