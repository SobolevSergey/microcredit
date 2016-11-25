package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.NegativeEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Negative extends BaseTransfer<NegativeEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1156126527846184755L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Negative.class.getConstructor(NegativeEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected RefNegative articleId;
	
	public Negative(){
		super();
		entity = new NegativeEntity();
	}
	
	public Negative(NegativeEntity entity){
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		partner=new Partner(entity.getPartnersId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
		if (entity.getArticleId()!=null){
		  articleId=new RefNegative(entity.getArticleId());
		}
	}
	
	public Integer getId() {
	     return entity.getId();
	 }
	    
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
  
   public RefNegative getArticleId(){
	   return articleId;
   }
   
   @XmlElement
   public void setArticleId(RefNegative articleId){
	   this.articleId=articleId;
   }
   
   public String getModule(){
	   return entity.getModule();
   }
   
   @XmlElement
   public void setModule(String module){
	   entity.setModule(module);
   }
   
   public Integer getYearArticle(){
	   return entity.getYearArticle();
   }
   
   @XmlElement
   public void setYearArticle(Integer yearArticle){
	   entity.setYearArticle(yearArticle);
   }
   
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
