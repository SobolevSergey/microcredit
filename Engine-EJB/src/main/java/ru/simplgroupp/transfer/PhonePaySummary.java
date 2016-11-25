package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PhonePayEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;

public class PhonePaySummary extends BaseTransfer<PhonePaySummaryEntity> implements Serializable, Initializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3024009252040229201L;

	/**
	 * 
	 */
	 public enum Options {
	        INIT_PHONEPAYS;
	 }
	 
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PhonePaySummary.class.getConstructor(PhonePaySummaryEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected Reference paymentType;
	protected Reference paymentPeriodType;
	protected List<PhonePay> phonePays;
	
	public PhonePaySummary(){
		super();
		entity = new PhonePaySummaryEntity();
	}
	
	public PhonePaySummary(PhonePaySummaryEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		partner=new Partner(entity.getPartnersId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
		if (entity.getPaymentType()!=null){
		  paymentType=new Reference(entity.getPaymentType());
		}
		if (entity.getPaymentPeriodType()!=null){
		  paymentPeriodType=new Reference(entity.getPaymentPeriodType());
		}
		phonePays=new ArrayList<PhonePay>(0);
	}
	
	public Integer getId() {
	     return entity.getId();
	 }

	@XmlElement
	public void setId(Integer id) {
	     entity.setId(id);
	}
	 
	public PeopleMain getPeopleMain() {
	      return peopleMain;
	}

	@XmlElement
	public void setPeopleMain(PeopleMain peopleMain) {
	      this.peopleMain=peopleMain;
	}
	    
	public CreditRequest getCreditRequest() {
	      return creditRequest;
	}

	@XmlElement
	public void setCreditRequest(CreditRequest creditRequest) {
	      this.creditRequest=creditRequest;
	}
	    
	public Partner getPartner() {
	     return partner;
	}

	@XmlElement
    public void setPartner(Partner partner) {
      this.partner=partner;
    }
    
    public Reference getPaymentType(){
    	return paymentType;
    }

    @XmlElement
    public void setPaymentType(Reference paymentType){
    	this.paymentType=paymentType;
    }
    
    public Reference getPaymentPeriodType(){
    	return paymentPeriodType;
    }

    @XmlElement
    public void setPaymentPeriodType(Reference paymentPeriodType){
    	this.paymentPeriodType=paymentPeriodType;
    }
    
    public Integer getPeriodCount(){
    	return entity.getPeriodCount();
    }

    @XmlElement
    public void setPeriodCount(Integer periodCount){
    	entity.setPeriodCount(periodCount);
    }
    
    public List<PhonePay> getPhonePays(){
    	return phonePays;
    }
    
    public void setPhonePays(List<PhonePay> value) {
    	phonePays = value;
    }    
    
	@Override
	public void init(Set options) {
		if (options != null && options.contains(Options.INIT_PHONEPAYS)) {
        	Utils.initCollection(entity.getPhonepays(), options);
        	for (PhonePayEntity reqEnt: entity.getPhonepays()) {
        		PhonePay req = new PhonePay(reqEnt);
        		req.init(options);
        		phonePays.add(req);
      	}
      }
		
	}

	public String getPayDescription(){
		String st="";
		if (phonePays.size()>0){
			for (PhonePay phonePay:phonePays){
				if (phonePay.getPeriodFirstDate()!=null){
					st+=DatesUtils.DATE_FORMAT_ddMMYYYY.format(phonePay.getPeriodFirstDate())+" г.";
				}
				if (phonePay.getPaymentSum()!=null){
					st+=" платеж "+CalcUtils.sumToString(phonePay.getPaymentSum())+" р.; ";
				}
			}
		}
		return st;
	}
}
