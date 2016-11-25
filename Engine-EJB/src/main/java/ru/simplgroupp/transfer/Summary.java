package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.SummaryEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Summary extends BaseTransfer<SummaryEntity> implements Serializable, Initializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6483031388782010142L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Summary.class.getConstructor(SummaryEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int CLIENT_OWNER=1;
	public static final int ACTIVE_CREDITS_NUMBER=2;
	public static final int NEGATIVE_RATING=3;
	public static final int DATE_MOST_RECENT_CREDIT=4;
	public static final int CURRENT_DELAY_STATUS=6;
	public static final int INFO_OFFICIAL=7;
	public static final int INFO_COURT=8;
	public static final int INFO_BANKRUPCY=9;
	public static final int INFO_DISPUTED=13;
	public static final int INFO_DISPUTED_COURT=14;
	public static final int INFO_DISPUTED_OFFICIAL=15;
	public static final int REQUESTS_3MONTH=16;
	public static final int REQUESTS_6MONTH=17;
	public static final int REQUESTS_9MONTH=18;
	public static final int REQUESTS_12MONTH=19;
	public static final int REQUESTS=20;
	public static final int CREDITS_NUMBER=21;
	public static final int HISTORY_DELAY_STATUS=22;
	public static final int DELAY5=23;
	public static final int DELAY30=24;
	public static final int DELAY60=25;
	public static final int DELAY90=26;
	public static final int DELAYMORE=27;
	public static final int CLIENT_COOWNER=32;
	
	public static final int CURRENT_DELAY_STATUS_COOWNER=41;
	public static final int HISTORY_DELAY_STATUS_COOWNER=40;
	public static final int CLIENT_GARANT=42;
	public static final int CURRENT_DELAY_STATUS_GARANT=43;
	public static final int HISTORY_DELAY_STATUS_GARANT=44;
	public static final int TOTAL_MONTHLY_OWNER=45;
	public static final int TOTAL_BALANCE_OWNER=46;
	public static final int POTENTIAL_MONTHLY_OWNER=47;
	public static final int POTENTIAL_BALANCE_OWNER=48;
	public static final int TOTAL_MONTHLY_NOT_OWNER=49;
	public static final int TOTAL_BALANCE_NOT_OWNER=50;
	public static final int POTENTIAL_MONTHLY_NOT_OWNER=51;
	public static final int POTENTIAL_BALANCE_NOT_OWNER=52;
	public static final int REQUESTS_ALL=53;
	public static final int REQUESTS_PERSON=54;
	public static final int RECORD1=55;
	public static final int RECORD2=56;
	public static final int RECORD3=57;
	public static final int RECORD4=58;
	public static final int RECORD5=59;
	public static final int RECORD5MORE=60;
	public static final int REQUESTS_LAST_WEEK=61;
	public static final int REQUESTS_TWO_LAST_WEEKS=62;
	public static final int REQUESTS_LAST_MONTH=63;
	
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected RefSummary fieldRef;
	
	public Summary(){
		super();
		entity = new SummaryEntity();
	}
	
	public Summary(SummaryEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		partner=new Partner(entity.getPartnersId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
		fieldRef=new RefSummary(entity.getFieldRef());
	}
	
	public Integer getId() {
	     return entity.getId();
	 }

	@XmlElement
	 public void setId(Integer id) {
	     entity.setId(id);
	 }
	 
	 public Integer getRefId() {
	     return entity.getRefid();
	 }

	 @XmlElement
	 public void setRefId(Integer refid) {
	     entity.setRefid(refid);
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
	    
   public String getValue(){
	   return entity.getValue();
   }
   
   @XmlElement
   public void setValue(String value){
	   entity.setValue(value);
   }
   
   public RefSummary getFieldRef(){
	   return fieldRef;
   }
   
   @XmlElement
   public void setFieldRef(RefSummary fieldRef){
	   this.fieldRef=fieldRef;
   }
   
   public Date getSummaryDate(){
	   return entity.getSummaryDate();
   }
   
   @XmlElement
   public void setSummaryDate(Date summaryDate){
	   entity.setSummaryDate(summaryDate);
   }
   
   @Override
   public void init(Set options) {
	   entity.getValue();
   }

}
