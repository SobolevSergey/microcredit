package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Account extends BaseTransfer<AccountEntity> implements Serializable, Initializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2173054601298961563L;
	/**
	 * 
	 */
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Account.class.getConstructor(AccountEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int BANK_TYPE = 1;//счет в банке
    public static final int CARD_TYPE = 2;//карта 
    public static final int CONTACT_TYPE = 3;//контакт
    public static final int YANDEX_TYPE = 4; //счёт в системе yandex
    public static final int QIWI_TYPE = 5; //счёт в системе qiwi
    public static final int PAYONLINE_CARD_TYPE = 6;//карта привязанная к payonline
    public static final int GOLDCORONA_TYPE = 7;//счет золотая корона
    public static final int UNISTREAM_TYPE = 8;//счет Unistream
    public static final int YANDEX_CARD_TYPE = 9;//карта привязанная к yandex
    public static final int ARIUS_TYPE = 10;//ариус


    protected Reference accountType;
    protected PeopleMain peopleMain;
    protected Organization organization;
    protected ContactPoints point;
    
    public Account() {
        super();
        entity = new AccountEntity();
    }
    
    public Account(AccountEntity entity) {
        super(entity);
        accountType = new Reference(entity.getAccountTypeId());
        if (entity.getPeopleMainId()!=null)
          peopleMain=new PeopleMain(entity.getPeopleMainId());
        if (entity.getOrganizationId()!=null)
        	organization=new Organization(entity.getOrganizationId());
        if (entity.getPointId()!=null)
        	point=new ContactPoints(entity.getPointId());
    }

    @Override
    public void init(Set options) {
        entity.getAccountnumber();        
    }
     
    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Boolean getContest() {
        return entity.getContest();
    }

    @XmlElement
    public void setContest(Boolean contest) {
        entity.setContest(contest);
    }

    public Reference getAccountType() {
        return accountType;
    }

    @XmlElement
    public void setAccountType(Reference accountType) {
        this.accountType=accountType;
        if (this.accountType == null) {
        	entity.setAccountTypeId(null);
        } else {
        	entity.setAccountTypeId(accountType.getEntity());
        }
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }
    
    public Integer getIsWork() {
        return entity.getIsWork();
    }

    @XmlElement
    public void setIsWork(Integer isWork) {
        entity.setIsWork(isWork);
    }
    public String getAccountNumber() {
        return entity.getAccountnumber();
    }

    @XmlElement
    public void setAccountNumber(String accountnumber) {
        entity.setAccountnumber(accountnumber);
    }
    
    public String getCorrAccountNumber() {
        return entity.getCorraccountnumber();
    }

    @XmlElement
    public void setCorrAccountNumber(String corraccountnumber) {
        entity.setCorraccountnumber(corraccountnumber);
    }
    
    public String getBik() {
        return entity.getBik();
    }

    @XmlElement
    public void setBik(String bik) {
        entity.setBik(bik);
    }
    
    public String getSwift() {
        return entity.getSwift();
    }

    @XmlElement
    public void setSwift(String swift) {
        entity.setSwift(swift);
    }
    
    public String getBankName() {
        return entity.getBankname();
    }

    @XmlElement
    public void setBankName(String bankname) {
        entity.setBankname(bankname);
    }
    
    public String getCardName() {
        return entity.getCardname();
    }

    @XmlElement
    public void setCardName(String cardname) {
        entity.setCardname(cardname);
    }
    
    public String getCardHolder() {
        return entity.getCardHolder();
    }

    public void setCardHolder(String cardHolder) {
        entity.setCardHolder(cardHolder);
    }
    
    public String getCardNumber() {
        return entity.getCardnumber();
    }

    @XmlElement
    public void setCardNumber(String cardnumber) {
        entity.setCardnumber(cardnumber);
    }
    
    public String getCardNumberMasked() {
        return entity.getCardNumberMasked();
    }

    @XmlElement
    public void setCardNumberMasked(String cardNumberMasked) {
        entity.setCardNumberMasked(cardNumberMasked);
    }
    
    public String getPayonlineCardHash() {
        return entity.getPayonlineCardHash();
    }

    @XmlElement
    public void setPayonlineCardHash(String payonlineCardHash) {
        entity.setPayonlineCardHash(payonlineCardHash);
    }
    
    public Integer getCardMonthEnd() {
        return entity.getCardmonthend();
    }

    @XmlElement
    public void setCardMonthEnd(Integer cardmonthend) {
        entity.setCardmonthend(cardmonthend);
    }
    
    public Integer getCardYearEnd() {        
        return entity.getCardyearend();
    }

    @XmlElement
    public void setCardYearEnd(Integer cardyearend) {
        entity.setCardyearend(cardyearend);
    }
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }
    
    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
    public ContactPoints getPoint(){
    	return point;
    }
    
    public void setPoint(ContactPoints point){
    	this.point=point;
    }
    
    public Organization getOrganization(){
    	return organization;
    }
    
    @XmlElement
    public void setOrganization(Organization organization){
    	this.organization=organization;
    }
    
    public Date getDateAdd() {
    	return entity.getDateAdd();
    }
    
    @XmlElement
    public void setDateAdd(Date dateAdd) {
    	entity.setDateAdd(dateAdd);
    }
    
    public String getDescription() {
    	return entity.getDescription();
    }
    
    public String getBankDescription() {
    	return entity.getBankDescription();
    }
    
         
}
