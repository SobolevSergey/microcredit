package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.RepaymentScheduleEntity;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.RepaymentSchedule;

@Local
public interface PaymentDAO extends ListContainerDAO<Payment> {
    /**
     * Найти платеж клиенту
     *
     * @param creditId id кредита
     * @return платёж
     */
    public PaymentEntity findPaymentToClient(int creditId);
    
    /**
     * Найти отосланный платёж,но не проведённый
     *
     * @param creditId идентификатор кредита
     * @param partnerId идентификатор партнёра
     * @return платёж
     */
    public PaymentEntity findSendedPayment(Integer creditId, int partnerId);

    /**
     * список платежей по кредиту
     * @param creditId - id кредита
     * @param paymenttypeId - вид платежа
     * @return
     */
	List<PaymentEntity> getPaymentsByCredit(int creditId, int paymenttypeId);

	/**
	 * сохраняем платеж
	 * 
	 * @param pay - платеж
	 * @return
	 */
	PaymentEntity savePayment(PaymentEntity pay);

    /**
     * Получить платёж
     *
     * @param id      id платежа
     * @param options опции инициализации
     * @return платёж
     */	
	Payment getPayment(int id, Set options);

    /**
     * Получить платёж
     *
     * @param id id платежа
     * @return платёж
     */	
	PaymentEntity getPayment(int id);
	
    /**
     * Найти платёж по внешнему id
     *
     * @param externalId внешний id
     * @return Платёж
     */
    Payment findPaymentByExternalId(String externalId);	
    
    /**
     * список платежей по данному кредиту
     * @param creditId - кредит
     */
    List<Payment> listPayments(int creditId);    

    /**
     * список графиков платежей по данному кредиту
     * @param creditId - кредит
     */	
	List<RepaymentSchedule> listSchedules(int creditId);

    /**
     * список графиков платежей по данному кредиту
     * @param creditId - кредит
     */	
	List<RepaymentScheduleEntity> listScheduleEntities(int creditId);

	/**
	 * сохранить график платежей
	 * @param rep - график
	 * @return
	 */
	RepaymentScheduleEntity saveSchedule(RepaymentScheduleEntity rep); 
	
    /**
     * возвращает последнюю активную запись из графика платежей 
     * @param creditId - id кредита
     */
     RepaymentScheduleEntity findScheduleActive(Integer creditId);	
     /**
      * 
      * @param status
      * @param partner
      * @return
      */
     List<PaymentEntity> findExecutedPayments(PaymentStatus status,Partner partner);
}
