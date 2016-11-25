package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;
import java.util.Date;

import ru.simplgroupp.persistence.CreditEntity;

public class Credit extends BaseCredit<CreditEntity>  {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4187601540938068004L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Credit.class.getConstructor(CreditEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public Credit() {
		super();
		entity = new CreditEntity();
	}

	public Credit(CreditEntity entity) {
		super(entity);
	}

	/**
	 * Была ли частичная оплата займа
	 * @return
	 */
	public boolean getPartialPayment() {
		return (getCreditDateDebt() != null);
	}
	/**
	 * была ли изменена основная сумма
	 * @param mainSum
	 * @return
	 */
	public boolean isChangedMainSum(Double mainSum){
		return entity.isChangedMainSum(mainSum);
	}
	
	public Double getSumMainRemain() {
		return entity.getSumMainRemain();
	}
	
	public Date getDateMainRemain() {
		return entity.getDateMainRemain();
	}
	
	public Date getDateLastPayment() {
		return entity.getDateLastPayment();
	}
	/**
	 * просрочен ли кредит на дату
	 * @param currentDate - дата
	 * @return
	 */
	public boolean isOverdue(Date currentDate) {
		return (getIsSameOrg() && (!getIsOver()) 
				&& (getCreditDataEndFact() == null ||(getCreditDataEndFact() != null&&getCreditDataEndFact().getTime()>currentDate.getTime()) ) 
				&& getCreditDataEnd().getTime() < currentDate.getTime() && getIsActive()==ActiveStatus.ACTIVE);
	}
	
	/**
	 * просрочен ли кредит - текущее состояние
	 * @return
	 */
	public boolean getOverdueNow(){
		return (getMaxDelay()!=null&&getMaxDelay()>0 && getIsActive()==ActiveStatus.ACTIVE);
	}
	/**
	 * передан ли кредит коллектору
	 * @return
	 */
	public boolean getForCollector(){
		return (getCreditStatus().getCodeInteger()==BaseCredit.CREDIT_COLLECTOR);
	}
	
	/**
	 * передан ли кредит в суд
	 * @return
	 */
	public boolean getForCourt(){
		return (getCreditStatus().getCodeInteger()==BaseCredit.CREDIT_COURT);
	}
}
