package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Debt;

import javax.ejb.Local;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Local
public interface DebtDao {
    public List<DebtEntity> getDebtsWithDecisionDate(Date start,Date end);
    /**
     * сохраняем данные по долгу
     * @param debt - данные по долгу
     * @throws KassaException
     */
   	void saveDebt(Debt debt) throws KassaException;
 	/**
   	 * добавляем информацию о долгах
   	 * @param creditRequestId - заявка
   	 * @param peopleMainId - человек
   	 * @param partnerId - партнер
   	 * @param creditId - кредит
   	 * @param amount - сумма
   	 * @param amountPenalty - сумма штрафов и госпошлин
   	 * @param nameDebt - название долга
   	 * @param authorityName - название судебного органа 
   	 * @param dateDebt - дата передачи
   	 * @param debtNumber - номер решения
   	 * @param comment - решение текстом
   	 * @param typeDebt - вид долга, не наше, для партнеров
   	 * @param authorityCode - код органа, как правило для ФНС
   	 * @param authorityId - вид органа
   	 * @param dateDecision - дата решения
   	 * @param isActive - активность
   	 * @throws KassaException
   	 */
   	DebtEntity newDebt(Integer creditRequestId,Integer peopleMainId,Integer partnerId,Integer creditId,
   			Double amount,Double amountPenalty,String nameDebt,	String authorityName,Date dateDebt,
   			String debtNumber,String comment,String typeDebt,Integer authorityCode,
   			Integer authorityId,Date dateDecision,Integer isActive) throws KassaException;
   	
   	/**
	 * список долгов по условиям
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param organizationId - id организации
	 * @param authorityId - id вида организации
	 * @param dateDebt - диапазон дат передачи в суд
	 * @param dateDecision - диапазон дат принятия решения
	 * @return
	 */
	List<DebtEntity> listDebts(Integer creditRequestId,Integer peopleMainId,Integer partnerId,Integer organizationId,
			Integer authorityId, DateRange dateDebt,DateRange dateDecision);
	
	/**
	 * ищем долг
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param amount - сумма
	 * @param authorityId - id вида организации
	 * @param dateDecision - диапазон дат принятия решения
	 * @return
	 */
	DebtEntity findDebt(Integer peopleMainId,Integer partnerId,Double amount,
			Integer authorityId,Date dateDecision);
	/**
	 * возвращает долг по id
	 * @param debtId - id долга
	 * @return
	 */
	DebtEntity getDebtEntity(Integer debtId);
}
