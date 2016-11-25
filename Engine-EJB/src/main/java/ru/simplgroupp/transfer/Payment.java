package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Payment extends BaseTransfer<PaymentEntity> implements Serializable, Initializable, Identifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1126338835691364543L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Payment.class.getConstructor(PaymentEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int FROM_SYSTEM = 1; // мы платим клиенту
	public static final int TO_SYSTEM = 2; // клиент платит нам
	
	public static final int MAIN_SUM_TO_CLIENT = 1;//основная сумма клиенту
	public static final int COMISSION=2;//комиссия
	public static final int PENALTY=3;//штраф
	public static final int MONEY_BACK=4;//возврат
	public static final int SUM_FROM_CLIENT = 5;//основная сумма от клиента
	public static final int MONEY_REFINANCE = 6;//плата за рефинансирование
	
	//статусы платежей для выгрузки
	public static final int UPLOAD_ALL_SUM=1;
	public static final int UPLOAD_PERCENT=2;
	public static final int UPLOAD_MAIN_SUM=3;
	public static final int UPLOAD_SUM_DEBT=4;
	
    protected Credit credit; 
    protected Reference paymentType;
    protected Reference paysumId;
    protected Partner partner;
    protected Account account;
    protected Reference accountType;

    public Payment(PaymentEntity entity)  {
    	super(entity);
    	credit=new Credit(entity.getCreditId());
    	paymentType = new Reference(entity.getPaymenttypeId());
    	paysumId=new Reference(entity.getPaysumId());
    	partner = entity.getPartnersId() == null ? null : new Partner(entity.getPartnersId());
        if (entity.getAccountId() != null) {
            account = new Account(entity.getAccountId());
        }    	
        if (entity.getAccountTypeId() != null) {
        	accountType = new Reference(entity.getAccountTypeId());
        }
    }
	
    public Payment(){
    	super();
    	entity = new PaymentEntity();
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Double getAmount() {
        return entity.getAmount();
    }
    
    @XmlElement
    public void setAmount(Double value) {
    	entity.setAmount(value);
    }

    public Date getCreateDate() {
        return entity.getCreateDate();
    }
    
    @XmlElement
    public void setCreateDate(Date value) {
    	entity.setCreateDate(value);
    }

    public Date getProcessDate() {
        return entity.getProcessDate();
    }
    
    @XmlElement
    public void setProcessDate(Date value) {
        entity.setProcessDate(value);
    }    

    public Boolean getIsPaid() {
        return entity.getIsPaid();
    }

    @XmlElement
    public void setIsPaid(Boolean isPaid) {
        entity.setIsPaid(isPaid);
    }
    
    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit creditId) {
        credit=creditId;
    }
    
	@Override
	public void init(Set options) {
        credit.init(Utils.excludeOptions(options, BaseCredit.Options.INIT_PAYMENTS));
	}

    public Reference getPaymentType() {
        return paymentType;
    }

    @XmlElement
    public void setPaymentType(Reference paymentType) {
        this.paymentType = paymentType;
    }

    public Reference getPaysumId() {
        return paysumId;
    }

    @XmlElement
    public void setPaysumId(Reference paysumId) {
        this.paysumId = paysumId;
    }

    public Partner getPartner() {
		return partner;
	}
    
    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }

    public String getExternalId() {
        return this.getExternalId();
    }

    public PaymentStatus getStatus() {
        return entity.getStatus();
    }
    
    public void setStatus(PaymentStatus status) {
    	entity.setStatus(status);
    }
    
    public int getStatusId() {
    	return entity.getStatus().ordinal();
    }
    
    public void setStatusId(int statusId) {
    	PaymentStatus status = PaymentStatus.values()[statusId];
    	entity.setStatus(status);
    }
    
    public String getStatusName() {
    	return  PaymentStatus.getStatusName(entity.getStatus());
    }

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Reference getAccountType() {
		return accountType;
	}

	public void setAccountType(Reference accountType) {
        this.accountType=accountType;
        if (this.accountType == null) {
        	entity.setAccountTypeId(null);
        } else {
        	entity.setAccountTypeId(accountType.getEntity());
        }
	}
}