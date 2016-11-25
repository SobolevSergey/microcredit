package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Debt extends BaseTransfer<DebtEntity> implements Initializable, Serializable {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5234826759788387891L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Debt.class.getConstructor(DebtEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
	protected Reference authorityId;
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected Organization organization;
	protected Credit credit; 
	 
    public static final int DEBT_FNS=1;
    public static final int DEBT_FSSP=2;
    public static final int DEBT_COURT=3;
    public static final int DEBT_COLLECTOR=4;
    
    //долг по кредиту, переданный на взыскание
    public static final int DEBT_UPLOAD=5;
    
    public Debt() {
		super();
		entity = new DebtEntity();
	}

	public Debt(DebtEntity entity) {
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
		partner=new Partner(entity.getPartnersId());
		if (entity.getAuthorityId()!=null) {
			authorityId = new Reference(entity.getAuthorityId());
		}
		if (entity.getOrganizationId()!=null){
			organization=new Organization(entity.getOrganizationId());
		}
		if (entity.getCreditId()!=null){
			credit=new Credit(entity.getCreditId());
		}
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

	 public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Partner getPartner() {
	        return partner;
	 }

	 @XmlElement
	 public void setPartner(Partner partner) {
	        this.partner=partner;
	 } 

	public Double getAmount() {
		return entity.getAmount();
	}
	
	@XmlElement
	public void setAmount(Double amount){
		entity.setAmount(amount);
	}
	
	public Double getAmountPenalty() {
		return entity.getAmountPenalty();
	}
	
	public void setAmountPenalty(Double amountPenalty){
		entity.setAmountPenalty(amountPenalty);
	}
	
	public String getTypeDebt(){
		return entity.getTypeDebt();
	}

	@XmlElement
	public void setTypeDebt(String typeDebt){
		entity.setTypeDebt(typeDebt);
	}

	public String getNameDebt(){
		return entity.getNameDebt();
	}

	@XmlElement
	public void setNameDebt(String nameDebt){
		entity.setNameDebt(nameDebt);
	}

	public String getDebtNumber(){
		return entity.getDebtNumber();
	}

	@XmlElement
	public void setDebtNumber(String debtNumber){
		entity.setDebtNumber(debtNumber);
	}

	public String getComment(){
		return entity.getComment();
	}

	@XmlElement
	public void setComment(String comment){
		entity.setComment(comment);
	}

	
	public Integer getAuthorityCode(){
	  return entity.getAuthorityCode();	
	}

	@XmlElement
	public void setAuthorityCode(Integer authorityCode){
		entity.setAuthorityCode(authorityCode);
	}

	public String getAuthorityName(){
	  return entity.getAuthorityName();	
	}

	@XmlElement
	public void setAuthorityName(String authorityName){
		entity.setAuthorityName(authorityName);
	}

	public Integer getIsActive() {
	    return entity.getIsActive();
	}

    public void setIsActive(Integer isActive) {
	    entity.setIsActive(isActive);
	}
    
	public Date getDateDebt(){
		return entity.getDateDebt();
	}

	@XmlElement
	public void setDateDebt(Date dateDebt){
		entity.setDateDebt(dateDebt);
	}

	public Date getDateDecision(){
		return entity.getDateDecision();
	}

	@XmlElement
	public void setDateDecision(Date dateDecision){
		entity.setDateDecision(dateDecision);
	}
	
	public Reference getAuthorityId(){
		return authorityId;
	}

	@XmlElement
	public void setAuthorityId(Reference authorityId){
		this.authorityId=authorityId;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	@Override
	public void init(Set options) {
		entity.getAmount();
	}

}



