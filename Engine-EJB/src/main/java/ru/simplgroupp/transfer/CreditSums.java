package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.CreditSumsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class CreditSums extends BaseTransfer<CreditSumsEntity> implements Serializable, Initializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8417280980222427281L;

	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = CreditSums.class.getConstructor(CreditSumsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
    //константы записаны в BaseCredit, если нужны здесь, можно перенести сюда
    
    protected Reference sum;
    protected Reference operation;
    protected Credit credit;
    
    
	public CreditSums(){
		super();
		entity = new CreditSumsEntity();
	}
	
	public CreditSums(CreditSumsEntity entity){
		super(entity);
		credit=new Credit(entity.getCreditId());
		if (entity.getSumId()!=null){
			sum=new Reference(entity.getSumId());
		}
		if (entity.getOperationId()!=null){
			operation=new Reference(entity.getOperationId());
		}
	}
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

	public Reference getSum() {
		return sum;
	}

	public void setSum(Reference sum) {
		this.sum = sum;
	}

	public Reference getOperation() {
		return operation;
	}

	public void setOperation(Reference operation) {
		this.operation = operation;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	
}
