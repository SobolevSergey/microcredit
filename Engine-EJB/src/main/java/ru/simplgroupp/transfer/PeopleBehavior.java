package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PeopleBehaviorEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PeopleBehavior extends BaseTransfer<PeopleBehaviorEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3830882173395531169L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    }     
	static {
    	try {
			wrapConstructor = PeopleBehavior.class.getConstructor(PeopleBehaviorEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected Reference parameter;
	
	public PeopleBehavior(){
		super();
		entity = new PeopleBehaviorEntity();
	}
	
	public PeopleBehavior(PeopleBehaviorEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		partner=new Partner(entity.getPartnersId());
		parameter = new Reference(entity.getParameterId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
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

	public void setPeopleMain(PeopleMain peopleMain) {
	      this.peopleMain=peopleMain;
	}
	    
	public CreditRequest getCreditRequest() {
	      return creditRequest;
	}

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
  
	public String getWeboject() {
		return entity.getWeboject();
	}

	@XmlElement
	public void setWeboject(String weboject) {
		entity.setWeboject(weboject);
	}

	public String getParamvalue() {
		return entity.getParamvalue();
	}

	@XmlElement
	public void setParamvalue(String paramvalue) {
		entity.setParamvalue(paramvalue);
	}

	public Date getDateactual() {
		return entity.getDateactual();
	}
	
	@XmlElement
	public void setDateactual(Date dateactual) {
		entity.setDateactual(dateactual);
	}

	public Reference getParameter() {
		return parameter;
	}

	@XmlElement
	public void setParameter(Reference parameter_ID) {
		this.parameter = parameter_ID;
	}

	
	public Long getParamvaluelong() {
		return entity.getParamvaluelong();
	}

	@XmlElement
	public void setParamvaluelong(Long paramvaluelong) {
		entity.setParamvaluelong(paramvaluelong);
	}
	
	public Date getParamvaluedate() {
		return entity.getParamvaluedate();
	}

	@XmlElement
	public void setParamvaluedate(Date paramvaluedate) {
		entity.setParamvaluedate(paramvaluedate);
	}
 
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
