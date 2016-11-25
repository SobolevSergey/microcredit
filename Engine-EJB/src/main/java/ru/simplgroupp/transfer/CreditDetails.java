package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.CreditDetailsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

/**
 * Операции по кредиту
 */
public class CreditDetails extends BaseTransfer<CreditDetailsEntity> implements Serializable, Initializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 2927868933399283511L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = CreditHistoryPay.class.getConstructor(CreditDetailsEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public CreditDetails() {
        super();
        entity = new CreditDetailsEntity();
    }

    public CreditDetails(CreditDetailsEntity entity) {
        super(entity);
        operationId = new Reference(entity.getOperationId());
    }

    /**
     * тип операции
     */
    private Reference operationId;

    public Double getAmountAll() {
        return entity.getAmountAll();
    }

    public void setAmountAll(Double amountAll) {
        entity.setAmountAll(amountAll);
    }

    public Double getAmountMain() {
        return entity.getAmountMain();
    }

    public void setAmountMain(Double amountMain) {
        entity.setAmountMain(amountMain);
    }

    public Double getAmountOperation() {
        return entity.getAmountOperation();
    }

    public void setAmountOperation(Double amountOperation) {
        entity.setAmountOperation(amountOperation);
    }

    public Double getAmountOverpay() {
        return entity.getAmountOverpay();
    }

    public void setAmountOverpay(Double amountOverpay) {
        entity.setAmountOverpay(amountOverpay);
    }
    
    public Double getAmountPenalty() {
        return entity.getAmountPenalty();
    }

    public void setAmountPenalty(Double amountPenalty) {
        entity.setAmountPenalty(amountPenalty);
    }

    public Double getAmountPercent() {
        return entity.getAmountPercent();
    }

    public void setAmountPercent(Double amountPercent) {
        entity.setAmountPercent(amountPercent);
    }

    public Integer getAnotherId() {
        return entity.getAnotherId();
    }

    public void setAnotherId(Integer anotherId) {
        entity.setAnotherId(anotherId);
    }

    public Integer getDelay() {
        return entity.getDelay();
    }

    public void setDelay(Integer delay) {
        entity.setDelay(delay);
    }

    public Date getEventDate() {
        return entity.getEventDate();
    }

    public void setEventDate(Date eventDate) {
        entity.setEventDate(eventDate);
    }

    public Date getEventEndDate() {
        return entity.getEventEndDate();
    }

    public void setEventEndDate(Date eventEndDate) {
        entity.setEventEndDate(eventEndDate);
    }

    public Reference getOperationId() {
        return operationId;
    }

    public void setOperationId(Reference operationId) {
        this.operationId = operationId;
    }

    @Override
    public void init(Set options) {

    }
}
