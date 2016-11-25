package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * Данные по веб аналитике клиента
 */
public class PeopleBehaviorEntity extends BaseEntity implements Serializable {

 
	private static final long serialVersionUID = 8361141183823158388L;

	protected Integer txVersion = 0;
      
	/**
     * Значение
     */
    private String paramvalue;
    
    /**
     * Числовое значение
     */
    private Long paramvaluelong;
    
    /**
     * Значение даты
     */
    private Date paramvaluedate;
    
    /**
     * Объект по которому собирается статистика например /main/index2#txtName
     */
    private String weboject;
   
    /**
     * дата изменений
     */
    private Date dateactual;
     /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * вид параметра
     */
    private ReferenceEntity parameterId;
    /**
     * партнер
     */
    private PartnersEntity partnersId;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    
    public PeopleBehaviorEntity() {
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }
	public String getWeboject() {
		return weboject;
	}

	public void setWeboject(String weboject) {
		this.weboject = weboject;
	}

	public String getParamvalue() {
		return paramvalue;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	public Date getDateactual() {
		return dateactual;
	}

	public void setDateactual(Date dateactual) {
		this.dateactual = dateactual;
	}

	public ReferenceEntity getParameterId() {
		return parameterId;
	}

	public void setParameterId(ReferenceEntity parameter_ID) {
		this.parameterId = parameter_ID;
	}

	public PartnersEntity getPartnersId() {
		return partnersId;
	}

	public void setPartnersId(PartnersEntity partners_ID) {
		this.partnersId = partners_ID;
	}
	
	public Long getParamvaluelong() {
		return paramvaluelong;
	}

	public void setParamvaluelong(Long paramvaluelong) {
		this.paramvaluelong = paramvaluelong;
	}
	
	public Date getParamvaluedate() {
		return paramvaluedate;
	}

	public void setParamvaluedate(Date paramvaluedate) {
		this.paramvaluedate = paramvaluedate;
	}
	
	public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
       this.creditRequestId = creditRequestId;
    }
   
  
	@Override
	public Boolean equalsContent(BaseEntity other) {
		PeopleBehaviorEntity ent = (PeopleBehaviorEntity) other;
		
		return Utils.equalsNull(paramvalue, ent.getParamvalue()) 
				&& Utils.equalsNull(dateactual.getTime(), ent.getDateactual().getTime())
				&& Utils.equalsNull(paramvaluedate!=null?paramvaluedate.getTime():null, 
						ent.getParamvaluedate()!=null?ent.getParamvaluedate().getTime():null)
				&& Utils.equalsNull(paramvaluelong, ent.getParamvaluelong())
				&& Utils.equalsNull(parameterId, ent.getParameterId());
	}

}
