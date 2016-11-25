package ru.simplgroupp.interfaces.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RefSummaryEntity;
import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.ChangeRequests;
import ru.simplgroupp.transfer.Debt;

@Local
public interface CreditInfoService {

	/**
     * сохраняем запись в таблицу с итогами
     *
     * @param creditRequest - заявка
     * @param peopleMain - человек
     * @param partner - партнер
     * @param value - значение итогового параметра
     * @param ref   - связь со справочником вида итога
     * @param refid - id справочника, если значение, записываемое в таблицу, связано со справочником
     */
    void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner, String value, RefSummaryEntity ref, Integer refid);
    /**
     * сохраняем запись в таблицу с итогами
     *
     * @param creditRequest - заявка
     * @param peopleMain - человек
     * @param partner - партнер
     * @param value - значение итогового параметра
     * @param ref   - связь со справочником вида итога
     * @param refid - id справочника, если значение, записываемое в таблицу, связано со справочником
     * @param summaryDate - дата информации
     */
    void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner, String value, 
    		RefSummaryEntity ref, Integer refid,Date summaryDate);
    /**
     /**
   	 * сохраняем данные верификации
     * @param creditRequest - заявка
     * @param peopleMain - человек
     * @param partner - партнер
   	 * @param verScore - скоринг верификации
   	 * @param valScore - скоринг валидации
   	 * @param verifyPersonal - верификация по ПД
     * @param verifyDocument - верификация по документу
     * @param verifyPhone - верификация по телефону
     * @param verifyAddress - верификация по адресу
     * @param verifyInn - верификация по ИНН
     */
   	VerificationEntity saveVerification(CreditRequestEntity creditRequest,PeopleMainEntity peopleMain,PartnersEntity partner,Double verScore,Double valScore,Integer verifyPersonal,Integer verifyDocument,Integer verifyPhone,Integer verifyAddress,Integer verifyInn);
   	/**
   	 * сохраняем верификацию
   	 * @param verification - верификация
   	 * @return
   	 */
   	VerificationEntity saveVerification(VerificationEntity verification);
   	/**
   	 * сохраняем системную верификацию
   	 * @param creditRequestId - заявка
   	 * @param peopleMainId - человек
   	 * @param clientCredit - кредит клиента
   	 * @param credit - любой другой кредит
   	 * @return
   	 */
   	VerificationEntity saveSystemVerification(VerificationEntity verification,Integer creditRequestId,
   			Integer peopleMainId,CreditEntity clientCredit,CreditEntity credit);
   	/**
   	 * сохраняем системную верификацию
   	 * @param creditRequestId - заявка
   	 * @param peopleMainId - человек
   	 * @param clientCredit - кредит клиента
   	 * @param credit - любой другой кредит
   	 * @param addMark - доп.оценка, которая дается всем
   	 * @return
   	 */
   	VerificationEntity saveSystemVerification(VerificationEntity verification,Integer creditRequestId,
   			Integer peopleMainId,CreditEntity clientCredit,CreditEntity credit,Integer addMark);
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
    * сохраняем данные по долгу
    * @param debt - данные по долгу
    * @throws KassaException
    */
   	void saveDebt(Debt debt) throws KassaException;
   	/**
     * поставить галочку отредактировано для успешно отредактированной заявки
     * @param creditRequestId - заявка
     * @param isEdited - статус заявки на редактирование
     */
    void saveStatusChangeRequest(Integer creditRequestId, Integer isEdited) throws KassaException;
   
    /**
     * ищем заявку, которая ждет редактирования
     * @param creditRequestId - кредитная заявка
     */
    ChangeRequests findChangeRequestWaitingEdit(Integer creditRequestId);

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
     * @param chRequestId     - id заявки на изменение
     * @param bConfirm - отредактирована ли анкета
     */
	ChangeRequests saveChangeRequest(Integer chRequestId, boolean bConfirm);
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
	 * сохраняем скоринг
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param modelId - id модели
	 * @param modelCode - код модели
	 * @param score - оценка
	 * @param error - ошибка
	 * @param scoreRisk - оценка риска
	 */
	void saveScoring(Integer creditRequestId,Integer peopleMainId,Integer partnerId,Integer modelId,
			String modelCode, Double score,String error,Double scoreRisk);
	
	/**
	 * сохраняем негатив
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param article - статья
	 * @param module - модуль
	 * @param yearArticle - год статьи
	 */
	void saveNegative(Integer creditRequestId,Integer peopleMainId,Integer partnerId,String article,
			String module,Integer yearArticle);
	
	/**
	 * ищем верификации
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param verificationDate - дата верификации
	 * @return
	 */
	List<VerificationEntity> findVerification(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange verificationDate);
	/**
	 * ищем верификации
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param verificationDate - дата верификации
	 * @return
	 */
	VerificationEntity findOneVerification(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange verificationDate);
	/**
	 * ищем скоринги
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param scoringDate - дата верификации
	 * @return
	 */
	List<ScoringEntity> findScorings(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange scoringDate);
}
