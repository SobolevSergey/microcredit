package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.kzegovdata.Address;
import ru.simplgroupp.util.DatesUtils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class PeopleMain extends BaseTransfer<PeopleMainEntity> implements Serializable, Initializable, Identifiable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 6977602670287197156L;

	public enum Options {
	        
	        INIT_ADDRESS,
	        INIT_ACCOUNT,
	        INIT_ANTIFRAUD,
	        INIT_ACCOUNTACTIVE,
	        INIT_BLACKLIST,
	        INIT_COLLECTORS,
	        INIT_CREDIT,
	        INIT_DOCUMENT,
            INIT_DOCUMENTOTHER,
	        INIT_DOCUMENTMEDIA,
	        INIT_OFFICIAL_DOCUMENT,
	        INIT_EMPLOYMENT,
	        INIT_LOGS,
	        INIT_PEOPLE_CONTACT,
	        INIT_PEOPLE_SUMS,
	        INIT_PEOPLE_CONTACT_ALL,
	        INIT_PEOPLE_PERSONAL,
	        INIT_PEOPLE_MISC,
            INIT_PEOPLE_OTHER,
	        INIT_REQUESTS,
	        INIT_CREDIT_REQUEST,
	        INIT_SPOUSE,
	        INIT_DEBT,
	        INIT_VERIF,
	        INIT_NEGATIVE,
	        INIT_BEHAVIOR,
	        INIT_BONUS,
	        INIT_FRIENDS,
	        INIT_SUMMARY,
	        INIT_PHONESUMMARY,
	        INIT_SCORING,
	        INIT_MESSAGES,
	        INIT_INCAPACITY,
            INIT_ANSWERS,
            INIT_ADDRESS_KZEGOVDATA;
	    }
	 
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeopleMain.class.getConstructor(PeopleMainEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
   
    public static final int INN_NBKI=81;
    public static final int SNILS_NBKI=32;
    
    /**
     * контакты
     */
    protected List<PeopleContact> peopleContacts;
    /**
     * все контакты
     */
    protected List<PeopleContact> peopleContactsAll;
    /**
     * счета
     */
    protected List<Account> accounts;
    /**
     * активные счета
     */
    protected List<Account> accountsActive;
    /**
     * черные списки
     */
    protected List<BlackList> blackLists;
    /**
     * кредиты
     */
    protected List<Credit> credits;
    /**
     * кредиты от бюро, активные
     */
    protected List<Credit> creditsCB;
    /**
     * у коллектора
     */
    protected List<Collector> collectors;
    /**
     * кредиты системы
     */
    protected List<Credit> systemCredits;
    /**
     * активный кредит системы
     */
    protected Credit systemCreditActive;
    /**
     * заявки
     */
    protected List<CreditRequest> creditreq;
    /**
     * документы
     */
    protected List<Documents> documents;
    /**
     * документы
     */
    protected List<DocumentsOther> documentsother;
    /**
     * документы, подписанные клиентом
     */
    protected List<OfficialDocuments> officialDocuments;
    /**
     * сканы документов
     */
    protected List<DocumentMedia> documentMedia;
    /**
     * занятость
     */
    protected List<Employment> employs;
    /**
     * доп.данные
     */
    protected List<PeopleMisc> peopleMisc;
    /**
     * доп.данные
     */
    protected List<PeopleOther> peopleOther;
    /**
     * персональные данные
     */
    protected List<PeoplePersonal> peoplePersonal;
    /**
     * ПД из КБ, если отличаются от наших
     */
    protected List<PeoplePersonal> peoplePersonalActiveKB;
    /**
     * паспорт из КБ, если отличается от нашего
     */
    protected List<Documents> activePassportKB;
    /**
     * активные данные по человеку
     */
    protected PeoplePersonal peoplePersonalActive;
    /**
     * активные данные паспорта
     */
    protected Documents activePassport;
    /**
     * текущая занятость
     */
    protected Employment currentEmployment;
    /**
     * суммы в системе
     */
    protected List<PeopleSums> peopleSums;
    /**
     * скоринги
     */
    protected List<Scoring> scorings;
    /**
     * адрес регистрации
     */
    protected FiasAddress registerAddress;
    /**
     * адрес проживания
     */
    protected FiasAddress residentAddress;
    /**
     * адрес места работы
     */
    protected FiasAddress workAddress;
    /**
     * все адреса
     */
    protected List<FiasAddress> addresses;
    /**
     * все адреса
     */
    protected List<FiasAddress> addressesActive;
    /**
     * активный мобильный телефон
     */
    protected PeopleContact cellPhone;
    /**
     * активный email
     */
    protected PeopleContact email;
    /**
     * активный телефон по адресу проживания    
     */
    protected PeopleContact homePhone;
    /**
     * активный телефон по адресу регистрации    
     */
    protected PeopleContact homePhoneReg;
    /**
     * активный рабочий телефон    
     */
    protected PeopleContact workPhone;
    /**
     * контакты из соц.сетей
     */
    protected PeopleContact vk;
    protected PeopleContact odk;
    protected PeopleContact mm;
    protected PeopleContact fb;
    protected PeopleContact tt;
    /**
     * активные доп.данные
     */
    protected PeopleMisc peopleMiscActive;
    /**
     * активные доп.данные
     */
    protected PeopleOther peopleOtherActive;

    protected DocumentsOther documentsOtherActive;
    /**
     * супруги
     */
    protected List<Spouse> spouses;
    /**
     * долги
     */
    protected List<Debt> debts;
    /**
     * верификация
     */
    protected List<Verification> verif;
    /**
     * суммарная информация
     */
    protected List<Summary> summary;
    /**
     * платежи за телефон
     */
    protected List<PhonePaySummary> phoneSummary;
    /**
     * недееспособность
     */
    protected List<PeopleIncapacity> incapacities;
    /**
     * активный партнер
     */
    protected Spouse spouseActive;
    /**
     * негатив
     */
    protected List<Negative> negatives;
    /**
     * поведение пользователя
     */
    protected List<PeopleBehavior> peoplebehaviors;
    /**
     * Все вопросы верификатора
     */
    private List<QuestionAnswer> answers;
    /**
     * бонусы
     */
    protected List<PeopleBonus> peoplebonus;
    protected Double bonusamount;
    
    /**
     * приглашенные друзья за бонусы
     */
    protected List<PeopleFriend> peopleFriendsForBonus;
    /**
     * записанные человеком друзья 
     * */
    protected List<PeopleFriend> peopleFriendsNotForBonus;
    /**
     * кредит, который клиент указал в анкете
     */
    protected Credit clientCredit;
    /**
     * кредиты, которые человек указывал сам
     */
    protected List<Credit> clientCredits;
    /**
     * запросы
     */
    protected List<Requests> requests;
    /**
     * сообщения, рассылки, посланные клиенту из системы
     */
    protected List<Messages> messages;
    /**
     * Емаил рассылка
     */
    protected List<Messages> autoSendedEmailMessages = new ArrayList<>(0);
    /**
     * SMS рассылка
     */
    protected List<Messages> autoSendedSmsMessages = new ArrayList<>(0);
    /**
     * последний взятый кредит в системе
     */
    protected Credit lastCredit;
    /**
     * логи
     */
    protected List<EventLog> events;
    /**
     * средний срок кредита в системе
     */
    protected Integer averageSystemCreditDays=0;
    /**
     * максимальная сумма по кредитам системы
     */
    protected Double maxSystemCreditSum=0d;
    
	public PeopleMain(){
		super();
		entity = new PeopleMainEntity();
	}
	
	public PeopleMain(PeopleMainEntity value){
		super(value);
		
		peopleSums = new ArrayList<>(0);
		peopleContacts = new ArrayList<>(0);
		peopleContactsAll = new ArrayList<>(0);
		accounts=new ArrayList<>(0);
		accountsActive=new ArrayList<>(0);
		requests = new ArrayList<>(0);
		blackLists=new ArrayList<>(0);
		events=new ArrayList<>(0);
		credits=new ArrayList<>(0);
		creditsCB=new ArrayList<>(0);
	    systemCredits=new ArrayList<>(0);
	    clientCredits=new ArrayList<>(0);
	    creditreq=new ArrayList<>(0);
		documents=new ArrayList<>(0);
        documentsother = new ArrayList<>(0);
        
		officialDocuments=new ArrayList<>(0);
		documentMedia=new ArrayList<>(0);
		activePassportKB=new ArrayList<>(0);
		employs=new ArrayList<>(0);
		peopleMisc=new ArrayList<>(0);
        peopleOther=new ArrayList<>(0);
		peoplePersonal=new ArrayList<>(0);
		peoplePersonalActiveKB=new ArrayList<>(0);
		debts=new ArrayList<>(0);
		verif=new ArrayList<>(0);
	    scorings=new ArrayList<>(0);
	    negatives=new ArrayList<>(0);
	    peoplebehaviors = new ArrayList<>(0);
	    peoplebonus = new ArrayList<>(0);
	    peopleFriendsForBonus = new ArrayList<>(0);
	    peopleFriendsNotForBonus = new ArrayList<>(0);
	    addresses=new ArrayList<>(0);
	    addressesActive=new ArrayList<>(0);
        answers=new ArrayList<>(0);
	    
	    spouses=new ArrayList<>(0);
	    summary=new ArrayList<>(0);
	    phoneSummary=new ArrayList<>(0);
	    incapacities=new ArrayList<>(0);
	    messages=new ArrayList<>(0);
	    collectors=new ArrayList<>(0);
	}
	
	public Integer getId() {
        return entity.getId();
    }

	@XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getInn() {
        return entity.getInn();
    }

    @XmlElement
    public void setIin(String iin) {
        entity.setIin(iin);
    }

    public String getIin() {
        return entity.getIin();
    }

    @XmlElement
    public void setInn(String inn) {
        entity.setInn(inn);
    }

    public String getSnils() {
        return entity.getSnils();
    }

    @XmlElement
    public void setSnils(String snils) {
        entity.setSnils(snils);
    }
    
    public PeopleMainEntity getEntity() {
    	return entity;
    }
    
    public List<PeopleContact> getPeopleContacts() {
    	return peopleContacts;
    }
    
    public List<PeopleContact> getPeopleContactsAll() {
    	return peopleContactsAll;
    }
    
    @XmlElement
    public void setPeopleContactsAll(List<PeopleContact> value) {
    	peopleContactsAll = value;
    }    
    
    public List<Account> getAccounts() {
    	return accounts;
    }
    
    @XmlElement
    public void setAccounts(List<Account> value) {
    	accounts = value;
    }
    
    public List<Account> getAccountsActive() {
    	return accountsActive;
    }
    
    public Credit getClientCredit(){
    	return clientCredit;
    }
    
     public FiasAddress getResidentAddress() {
         return residentAddress;
     }
     
     public FiasAddress getRegisterAddress() {
         return registerAddress;
     }    
    
     public FiasAddress getWorkAddress(){
         return workAddress;
     }    
     
     public List<BlackList> getBlackLists(){
         return blackLists;
     }
     
     @XmlElement
     public void setBlackLists(List<BlackList> value) {
    	 blackLists = value;
     }
    
     public List<Debt> getDebts(){
     	return debts;
     }
     
     public List<Credit> getCredits() {
    	return credits;
     }
     
     @XmlElement
     public void setCredits(List<Credit> value) {
    	 credits = value;
     }
    
     public List<Credit> getSystemCredits(){
     	return systemCredits;
     }
     
     @XmlElement
     public void setSystemCredits(List<Credit> value) {
    	 systemCredits = value;
     }     
     
     public List<Credit> getClientCredits(){
      	return clientCredits;
      }
      
      @XmlElement
      public void setClientCredits(List<Credit> value) {
     	 clientCredits = value;
      }     
      
     public List<FiasAddress> getAddresses() {
    	 return addresses;
     }
     
     @XmlElement
     public void setAddresses(List<FiasAddress> value) {
    	 addresses = value;
     }
     
     public List<CreditRequest> getCreditreq() {
     	return creditreq;
     }
     
     public List<DocumentsOther> getDocumentsother() {
    	return documentsother;
     }
     
     @XmlElement
     public void setDocumentsother(List<DocumentsOther> value){
    	 documentsother=value;
     }

    public List<Documents> getDocuments() {
        return documents;
    }

    @XmlElement
    public void setDocuments(List<Documents> value){
        documents=value;
    }
     
    public List<Employment> getEmploys(){
    	return employs;
    }
    
    @XmlElement
    public void setEmploys(List<Employment> value) {
    	employs = value;
    }
    
    public List<Summary> getSummary(){
    	return summary;
    }
    
    @XmlElement
    public void setSummary(List<Summary> value) {
    	summary = value;
    }
    
    public List<PhonePaySummary> getPhoneSummary(){
    	return phoneSummary;
    }
    
    @XmlElement
    public void setPhoneSummary(List<PhonePaySummary> value) {
    	phoneSummary = value;
    }
    
    public List<Verification> getVerif(){
    	return verif;
    }
    
    @XmlElement
    public void setVerif(List<Verification> value) {
    	verif = value;
    }
    
    public List<PeopleMisc> getPeopleMisc(){
    	return peopleMisc;
    }
    
    @XmlElement
    public void setPeopleMisc(List<PeopleMisc> value) {
    	peopleMisc = value;
    }

    public List<PeopleOther> getPeopleOther(){
        return peopleOther;
    }

    @XmlElement
    public void setPeopleOther(List<PeopleOther> value) {
        peopleOther = value;
    }
    
    public List<PeoplePersonal> getPeoplePersonalActiveKB() {
		return peoplePersonalActiveKB;
	}

    @XmlElement
	public void setPeoplePersonalActiveKB(List<PeoplePersonal> value) {
		peoplePersonalActiveKB = value;
	}

	public List<Documents> getActivePassportKB() {
		return activePassportKB;
	}

	@XmlElement
	public void setActivePassportKB(List<Documents> value) {
		activePassportKB = value;
	}

	public List<PeoplePersonal> getPeoplePersonal(){
    	return peoplePersonal;
    }
    
    @XmlElement
    public void setPeoplePersonal(List<PeoplePersonal> value) {
    	peoplePersonal = value;
    }
    
    public List<PeopleIncapacity> getIncapacities() {
		return incapacities;
	}

    @XmlElement
	public void setIncapacities(List<PeopleIncapacity> incapacities) {
		this.incapacities = incapacities;
	}

	public PeoplePersonal getPeoplePersonalActive() {
    	return peoplePersonalActive;
    }
    
    public PeopleMisc getPeopleMiscActive() {
    	return peopleMiscActive;
    }

    public PeopleOther getPeopleOtherActive(){ return  peopleOtherActive; }
    
    public Documents getActivePassport() {
    	return activePassport;
    }

    public DocumentsOther getDocumentsOtherActive(){ return documentsOtherActive; }
    
    public Employment getCurrentEmployment() {
    	return currentEmployment;
    }
    
    public Spouse getSpouseActive() {
    	return spouseActive;
    }
    
    public PeopleContact getCellPhone() {
    	return cellPhone;
    }
    
    @XmlElement
    public void setCellPhone(PeopleContact value) {
    	cellPhone = value;
    }
    
    public PeopleContact getEmail() {
    	return email;
    }
    
    /** 
     * Домашний телефон по адресу регистрации
     * @return
     */
    public PeopleContact getHomePhoneReg() {
    	return homePhoneReg;
    }
    
    public PeopleContact getHomePhone() {
    	return homePhone;
    }
    
    public PeopleContact getWorkPhone() {
    	return workPhone;
    }
    
    /**
     * В контакте
     * @return
     */
    public PeopleContact getVk(){
    	return vk;
    }
    
    /**
     * Одноклассники
     * @return
     */
    public PeopleContact getOdk(){
    	return odk;
    }
    
    /**
     * Мой мир
     * @return
     */
    public PeopleContact getMm(){
    	return mm;
    }
    
    /**
     * Фейсбук
     * @return
     */
    public PeopleContact getFb(){
    	return fb;
    }
    
    /**
     * Твиттер
     * @return
     */
    public PeopleContact getTt(){
    	return tt;
    }
    
    public List<Spouse> getSpouses(){
    	return spouses;
    }
    
     public List<Scoring> getScorings(){
    	return scorings;
    }
    
     @XmlElement
     public void setScorings(List<Scoring> scorings){
    	 this.scorings=scorings;
     }
     
     public List<Negative> getNegatives(){
     	return negatives;
     }
     
     public Integer getHasNegatives(){
    	 return entity.getHasNegatives();
     }
     
     public Integer getHasMessages(){
    	 return entity.getHasMessages();
     }
     
     public Integer getHasRequests() {
         return entity.getHasRequests();
     }
     
 	 public List<PeopleBehavior> getPeoplebehaviors() {
		 return peoplebehaviors;
	 }
 	 
 	 public List<PeopleBonus> getPeoplebonus() {
		 return peoplebonus;
	 }
 	 
 	 public List<PeopleFriend> getPeopleFriendsForBonus() {
		 return peopleFriendsForBonus;
	 }
 	 
 	 public List<PeopleFriend> getPeopleFriendsNotForBonus() {
		 return peopleFriendsNotForBonus;
	 }
 	 
 	 public Integer getHasPeoplebehaviors(){
 		 return entity.getHasPeoplebehavior();
 	 }
 	 
 	 public Integer getHasPeoplebonus(){
		 return entity.getHasBonus();
	 }
 	 
 	 public Integer getHasPeoplefriends(){
		 return entity.getHasInviteFriends();
	 }
 	
     public Integer getHasCredits() {
    	 return entity.getHasCredits();
     }
     
     public Integer getHasCreditreq(){
    	 return entity.getHasCreditreq();
     }
     
     public Integer getHasScorings() {
    	 return entity.getHasScorings();
     }
     
     public Integer getHasDocumentMedia(){
    	 return entity.getHasDocumentMedia();
     }
     
     public Integer getHasCollectors() {
    	 return entity.getHasCollectors();
     }
     
     public Integer getHasSpouses() {
    	 return entity.getHasSpouses();
     }
     
    public Integer getHasBlacklists(){
    	return entity.getHasBlacklists();
    }
    
    public Integer getHasAccounts(){
    	return entity.getHasAccounts();
    }
    
    public Integer getHasDebts(){
    	return entity.getHasDebts();
    }
    
    public Integer getHasSummary(){
    	return entity.getHasSummary();
    }
    
    public Integer getHasPhoneSummary(){
    	return entity.getHasPhoneSummary();
    }
    
    public Integer getHasPeopleContacts(){
    	return entity.getHasPeopleContacts();
    }
    
    public Integer getHasAddresses(){
    	return entity.getHasAddresses();
    }
    
    public Integer getHasDocuments(){
    	return entity.getHasDocuments();
    }
    
    public Integer getHasEmploys(){
    	return entity.getHasEmploys();
    }
    
    public Integer getHasVerif(){
    	return entity.getHasVerif();
    }
    
    public Integer getHasMisc() {
    	return entity.getHasMisc();
    }
    
    public Integer getHasIncapacity(){
    	return entity.getHasIncapacity();
    }
    
    public Integer getActiveCreditsCount(){
    	return entity.getActiveCreditsCount();
    }
    
    public Integer getClosedCreditsCount(){
    	return entity.getClosedCreditsCount();
    }
    
    public Integer getReallyClosedCreditsCount(){
    	return entity.getReallyClosedCreditsCount();
    }
    
    /**
     * клиент не новый (имеет закрытые кредиты)
     * @return
     */
    public Boolean getOldClient(){
    	return (entity.getReallyClosedCreditsCount()!=0);
    }
    
    public Integer getClosedCreditsWithoutDelayCount(){
    	return entity.getClosedCreditsWithoutDelayCount();
    }
    
    public Integer getDelayCreditsCount(){
    	return entity.getDelayCreditsCount();
    }
    
    
	@Override
	public void init(Set options) {
		
		//официальные документы
		if (options != null && options.contains(Options.INIT_DOCUMENT)) {
			Utils.initCollection(entity.getDocuments(), options);
        	for (DocumentEntity docEnt: entity.getDocuments()) {
        		Documents doc = new Documents(docEnt);
        		doc.init(options);
        		documents.add(doc);
        		if (docEnt.getIsactive()==ActiveStatus.ACTIVE && docEnt.getDocumenttypeId().getCodeinteger()==Documents.PASSPORT_RF && docEnt.getPartnersId().getId() ==Partner.CLIENT){
        			activePassport=new Documents(docEnt);
        		}
        		if (docEnt.getIsactive()==ActiveStatus.ACTIVE && docEnt.getDocumenttypeId().getCodeinteger()==Documents.PASSPORT_RF && docEnt.getPartnersId().getId()!=Partner.CLIENT){
        			activePassportKB.add(doc);
        		}
        	}
		}

		//другие документы
        if (options != null && options.contains(Options.INIT_DOCUMENTOTHER)) {
            Utils.initCollection(entity.getDocuments(), options);
            for (DocumentOtherEntity docEnt: entity.getDocumentsother()) {
                DocumentsOther doc = new DocumentsOther(docEnt);
                doc.init(options);
                documentsother.add(doc);
                if (doc.getIsActive()==ActiveStatus.ACTIVE && doc.getPartner().getId()==Partner.CLIENT){
                    documentsOtherActive = doc;
                }
            }
        }
		
		//сканы документов
		if (options != null && options.contains(Options.INIT_DOCUMENTMEDIA)) {
			Utils.initCollection(entity.getDocumentMedia(), options);
		    for (DocumentMediaEntity docEnt: entity.getDocumentMedia()) {
		     	DocumentMedia doc = new DocumentMedia(docEnt);
		        doc.init(options);
		        documentMedia.add(doc);
		       
		   	}
		}
				
		//подписанные документы
		if (options != null && options.contains(Options.INIT_OFFICIAL_DOCUMENT)) {
			Utils.initCollection(entity.getOfficialDocuments(), options);
			for (OfficialDocumentsEntity doc: entity.getOfficialDocuments()) {
			   	OfficialDocuments ofdoc = new OfficialDocuments(doc);
				ofdoc.init(options);
				officialDocuments.add(ofdoc);
				       
		   	}
		}	
		
		//запросы
        if (options != null && options.contains(Options.INIT_REQUESTS)) {
            Utils.initCollection(entity.getRequests(), options);
            for (RequestsEntity reqEnt : entity.getRequests()) {
                Requests req = new Requests(reqEnt);
                req.init(options);
                requests.add(req);
            }
        }
        
        //логи
        if (options != null && options.contains(Options.INIT_LOGS)) {
            Utils.initCollection(entity.getLogs(), options);
            for (EventLogEntity empEnt : entity.getLogs()) {
                EventLog evt = new EventLog(empEnt);
                evt.init(options);
                events.add(evt);
            }
        }
        
        //сообщения, рассылки
        if (options != null && options.contains(Options.INIT_MESSAGES)) {
            Utils.initCollection(entity.getMessages(), options);
            for (MessagesEntity ent : entity.getMessages()) {
                Messages m = new Messages(ent);
                messages.add(m);
                if (Messages.AUTO.equals(ent.getMessageWayId().getCodeinteger()) && (Messages.EMAIL == ent.getMessageTypeId().getCodeinteger())) {
                    autoSendedEmailMessages.add(m);
                }
                if (Messages.AUTO.equals(ent.getMessageWayId().getCodeinteger()) && (Messages.SMS == ent.getMessageTypeId().getCodeinteger())) {
                    autoSendedSmsMessages.add(m);
                }
            }
        }
        
        //у коллектора
        if (options != null && options.contains(Options.INIT_COLLECTORS)) {
            Utils.initCollection(entity.getCollectors(), options);
            for (CollectorEntity ent : entity.getCollectors()) {
                Collector collector = new Collector(ent);
                collector.init(options);
                collectors.add(collector);
            }
        }
        
		//контакты
		if (options != null && options.contains(Options.INIT_PEOPLE_CONTACT)) {
			
			Utils.initCollection(entity.getPeoplecontact(), options);
		
        	for (PeopleContactEntity pcont1: entity.getPeoplecontact()) {
        		if (pcont1.getPartnersId().getId() == Partner.CLIENT && pcont1.getIsactive() == 1) {
        			if (pcont1.getContactId().getCodeinteger()==PeopleContact.CONTACT_EMAIL ){
            			email=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.CONTACT_CELL_PHONE ){
            			cellPhone=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.CONTACT_HOME_PHONE ){
            			homePhone=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.CONTACT_HOME_REGISTER_PHONE ){
            			homePhoneReg=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.CONTACT_WORK_PHONE ){
            			workPhone=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.NETWORK_VK ){
            			vk=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.NETWORK_OK ){
            			odk=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.NETWORK_MM ){
            			mm=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.NETWORK_FB ){
            			fb=new PeopleContact(pcont1);
            		}
            		if (pcont1.getContactId().getCodeinteger()==PeopleContact.NETWORK_TT ){
            			tt=new PeopleContact(pcont1);
            		}
        			PeopleContact pcont = new PeopleContact(pcont1);
        			pcont.init(options);
        			peopleContacts.add(pcont);
        		}
        	}
		}
		
		//все контакты
		if (options != null && options.contains(Options.INIT_PEOPLE_CONTACT_ALL)) {
			
			Utils.initCollection(entity.getPeoplecontact(), options);
		
        	for (PeopleContactEntity pcont1: entity.getPeoplecontact()) {
        		PeopleContact pcont = new PeopleContact(pcont1);
        		pcont.init(options);
        		peopleContactsAll.add(pcont);
        	}
            // сортировка по ID партнера
            if (peopleContactsAll.size() != 0) {
                Collections.sort(peopleContactsAll, new Comparator<PeopleContact>() {
                    @Override
                    public int compare(PeopleContact o1, PeopleContact o2) {
                        return o1.getPartner().getId().compareTo(o2.getPartner().getId());
                    }
                });
            }
		}		
		
		//адреса
		if (options != null && options.contains(Options.INIT_ADDRESS)) {
	            registerAddress = null;
	            residentAddress = null;
	            workAddress=null;
	            Utils.initCollection(entity.getAddresses(), options);
	            for (AddressEntity adrEnt: entity.getAddresses()) {
	            	//ищем адреса клиента
	            	if (adrEnt.getAddrtype()!=null&& adrEnt.getPartnersId().getId()==Partner.CLIENT){
	                  if (adrEnt.getAddrtype().getCodeinteger() == FiasAddress.REGISTER_ADDRESS && adrEnt.getIsactive()==ActiveStatus.ACTIVE ) {
	                    registerAddress = new FiasAddress(adrEnt);
	                    
	                  } else if (adrEnt.getAddrtype().getCodeinteger() == FiasAddress.RESIDENT_ADDRESS && adrEnt.getIsactive()==ActiveStatus.ACTIVE ) {
	                    residentAddress = new FiasAddress(adrEnt);
	                  } else if (adrEnt.getAddrtype().getCodeinteger() == FiasAddress.WORKING_ADDRESS && adrEnt.getIsactive()==ActiveStatus.ACTIVE ) {
	                    workAddress = new FiasAddress(adrEnt);
	                  }
	                 
	                  FiasAddress fa=new FiasAddress(adrEnt);
		              fa.init(options);
		              addressesActive.add(fa);
	            	}
	            	FiasAddress fa=new FiasAddress(adrEnt);
	                fa.init(options);
	                addresses.add(fa);
	            }
    
	     }

        // адреса Адресного регистра
        if (options != null && options.contains(Options.INIT_ADDRESS_KZEGOVDATA)) {
            registerAddress = null;
            residentAddress = null;
            workAddress=null;
            Utils.initCollection(entity.getAddresses(), options);
            for (AddressEntity addressEntity : entity.getAddresses()) {
                //ищем адреса клиента
                if (addressEntity.getAddrtype()!=null&& addressEntity.getPartnersId().getId()==Partner.CLIENT){
                    if (addressEntity.getAddrtype().getCodeinteger() == BaseAddress.REGISTER_ADDRESS && addressEntity.getIsactive()==ActiveStatus.ACTIVE ) {
                        registerAddress = new Address(addressEntity);

                    } else if (addressEntity.getAddrtype().getCodeinteger() == BaseAddress.RESIDENT_ADDRESS && addressEntity.getIsactive()==ActiveStatus.ACTIVE ) {
                        residentAddress = new Address(addressEntity);
                    } else if (addressEntity.getAddrtype().getCodeinteger() == BaseAddress.WORKING_ADDRESS && addressEntity.getIsactive()==ActiveStatus.ACTIVE ) {
                        workAddress = new Address(addressEntity);
                    }

                    Address address = new Address(addressEntity);
                    address.init(options);
                    addressesActive.add(address);
                }
                Address address = new Address(addressEntity);
                address.init(options);
                addresses.add(address);
            }

        }

		 //доп.данные
		 if (options != null && options.contains(Options.INIT_PEOPLE_MISC)) {
				
				Utils.initCollection(entity.getPeoplemisc(), options);
	        	for (PeopleMiscEntity pmisc1: entity.getPeoplemisc()) {
	        		PeopleMisc pmisc = new PeopleMisc(pmisc1);
	        		pmisc.init(options);
	        		peopleMisc.add(pmisc);
	        		
	        		if (pmisc1.getIsactive()==ActiveStatus.ACTIVE && pmisc1.getPartnersId().getId()==Partner.CLIENT){
	        			peopleMiscActive=pmisc;
	        		}
	        	}
		}

		//еще доп.данные 
        if (options != null && options.contains(Options.INIT_PEOPLE_OTHER)) {

            Utils.initCollection(entity.getPeopleother(), options);
            for (PeopleOtherEntity pmisc1: entity.getPeopleother()) {
                PeopleOther pmisc = new PeopleOther(pmisc1);
                pmisc.init(options);
                peopleOther.add(pmisc);

                if (pmisc1.getIsactive()==ActiveStatus.ACTIVE && pmisc1.getPartnersId().getId()==Partner.CLIENT){
                    peopleOtherActive=pmisc;
                }
            }
        }
		 
		 //партнер
		 if (options != null && options.contains(Options.INIT_SPOUSE)) {
				
				Utils.initCollection(entity.getSpouses(), options);
	        	for (SpouseEntity sp1: entity.getSpouses()) {
	        		
	        		Spouse sp = new Spouse(sp1);
	        		sp.init(options);
	        		spouses.add(sp);
	        		
	        		if (sp1.getIsactive()==ActiveStatus.ACTIVE && sp1.getSpouseId().getCodeinteger() == Spouse.CODE_SPOUSE ){
	        			spouseActive=sp;
	        		}
	        	}
		}
		 
		//персональные данные 
		if (options != null && options.contains(Options.INIT_PEOPLE_PERSONAL)) {
			
			Utils.initCollection(entity.getPeoplepersonal(), options);
        	for (PeoplePersonalEntity pper1: entity.getPeoplepersonal()) {
        		PeoplePersonal pper = new PeoplePersonal(pper1);
        		pper.init(options);
        		peoplePersonal.add(pper);
        		
        		if (pper1.getIsactive() == ActiveStatus.ACTIVE && pper1.getPartnersId().getId() == Partner.CLIENT) {
        			peoplePersonalActive = pper;
        		}
        		if (pper1.getIsactive() == ActiveStatus.ACTIVE && pper1.getPartnersId().getId() != Partner.CLIENT) {
        			peoplePersonalActiveKB.add(pper);
        		}
        	}
		}
		
		//счета
		if (options != null && options.contains(Options.INIT_ACCOUNT)) {
			Utils.initCollection(entity.getAccounts(), options);
        	for (AccountEntity acc1: entity.getAccounts()) {
        		Account acc = new Account(acc1);
        		acc.init(options);
        		accounts.add(acc);
        	}
		}
		
		//активный счет
		if (options != null && options.contains(Options.INIT_ACCOUNTACTIVE)) {
			Utils.initCollection(entity.getAccounts(), options);
        	for (AccountEntity acc1: entity.getAccounts()) {
        		if (acc1.getIsactive() == ActiveStatus.ACTIVE && acc1.getAccountTypeId().getIsactive()==ActiveStatus.ACTIVE) {
	        		Account acc = new Account(acc1);
	        		acc.init(options);
	        		accountsActive.add(acc);
        		}
        	}
		}
		
		//черные списки
		if (options != null && options.contains(Options.INIT_BLACKLIST)) {
			
			Utils.initCollection(entity.getBlacklists(), options);
			for (BlacklistEntity bl1: entity.getBlacklists()) {
        		BlackList bl = new BlackList(bl1);
        		bl.init(options);
        		blackLists.add(bl);
        	}
		}
		
		//долги
		if (options != null && options.contains(Options.INIT_DEBT)) {
	        	Utils.initCollection(entity.getDebts(), options);
	        	for (DebtEntity reqEnt: entity.getDebts()) {
	        		Debt req = new Debt(reqEnt);
	        		req.init(options);
	        		debts.add(req);
	      	}
	    }
		 
		//верификация
		if (options != null && options.contains(Options.INIT_VERIF)) {
	        	Utils.initCollection(entity.getVerif(), options);
	        	for (VerificationEntity reqEnt: entity.getVerif()) {
	        		Verification req = new Verification(reqEnt);
	        		req.init(options);
	        		verif.add(req);
	      	}
	    }
		
		//недееспособность
		if (options != null && options.contains(Options.INIT_INCAPACITY)) {
        	Utils.initCollection(entity.getPeopleIncapacity(), options);
        	for (PeopleIncapacityEntity reqEnt: entity.getPeopleIncapacity()) {
        		PeopleIncapacity req = new PeopleIncapacity(reqEnt);
        		req.init(options);
        		incapacities.add(req);
      	  }
        }
		
		//суммы в системе
		if (options != null && options.contains(Options.INIT_PEOPLE_SUMS)) {
		   	Utils.initCollection(entity.getPeopleSums(), options);
		   	for (PeopleSumsEntity ent: entity.getPeopleSums()) {
		   		PeopleSums req = new PeopleSums(ent);
		   		req.init(options);
		   		peopleSums.add(req);
		    }
	    }
				
		//суммарная информация
		if (options != null && options.contains(Options.INIT_SUMMARY)) {
	        	Utils.initCollection(entity.getSummary(), options);
	        	for (SummaryEntity reqEnt: entity.getSummary()) {
	        		Summary req = new Summary(reqEnt);
	        		req.init(options);
	        		summary.add(req);
	      	}
	    }
		
		//платежи за телефоны
		if (options != null && options.contains(Options.INIT_PHONESUMMARY)) {
	        	Utils.initCollection(entity.getPhonesummary(), options);
	        	for (PhonePaySummaryEntity reqEnt: entity.getPhonesummary()) {
	        		PhonePaySummary req = new PhonePaySummary(reqEnt);
	        		req.init(options);
	        		phoneSummary.add(req);
	      	}
	    }
		
		//негатив
		if (options != null && options.contains(Options.INIT_NEGATIVE)) {
	        	Utils.initCollection(entity.getNegatives(), options);
	        	for (NegativeEntity reqEnt: entity.getNegatives()) {
	        		Negative req = new Negative(reqEnt);
	        		req.init(options);
	        		negatives.add(req);
	      	}
	      }
		 
		//поведение пользователя 
		if (options != null && options.contains(Options.INIT_BEHAVIOR)) {
			 	Utils.initCollection(entity.getPeoplebehavior(), options);
	        	for (PeopleBehaviorEntity reqEnt: entity.getPeoplebehavior()) {
	        		PeopleBehavior req = new PeopleBehavior(reqEnt);
	        		req.init(options);
	        		peoplebehaviors.add(req);
	        	}
		}
		
		//бонусы
		 if (options != null && options.contains(Options.INIT_BONUS)) {
			 	Utils.initCollection(entity.getPeopleBonus(), options);
			 	bonusamount = new Double(0);
	        	for (PeopleBonusEntity reqEnt: entity.getPeopleBonus()) {
	        		PeopleBonus req = new PeopleBonus(reqEnt);
	        		req.init(options);
	        	        			
	        		if(reqEnt.getEventDate().compareTo(new Date()) <= 0)
	        			bonusamount = bonusamount + reqEnt.getAmount();
	        		
	        		peoplebonus.add(req);
	        	}
			}
		 
		 //друзья
		 if (options != null && options.contains(Options.INIT_FRIENDS)) {
			 	Utils.initCollection(entity.getPeoplefriends(), options);
	        	for (PeopleFriendEntity reqEnt: entity.getPeoplefriends()) {
	        		PeopleFriend req = new PeopleFriend(reqEnt);
	        		req.init(options);
	        		if (reqEnt.getForBonus()!=null){
	        		  if (reqEnt.getForBonus()==PeopleFriend.FOR_BONUS)	{
	        		      peopleFriendsForBonus.add(req);
	        		  } else {
	        			  peopleFriendsNotForBonus.add(req);
	        		  }
	        		}
	        	}
			}

        // заявки
        if (options != null && options.contains(Options.INIT_CREDIT_REQUEST)) {
            Utils.initCollection(entity.getCreditreq(), options);
            for (CreditRequestEntity req : entity.getCreditreq()) {
                CreditRequest request = new CreditRequest(req);
                creditreq.add(request);
            }
        }

		//кредиты 
		if (options != null && options.contains(Options.INIT_CREDIT)) {
			
			Utils.initCollection(entity.getCredits(), options);
			Integer systemCreditDays=0;
			for (CreditEntity cr1: entity.getCredits()) {
        		Credit cr = new Credit(cr1);
        		cr.init(options);
        		credits.add(cr);
        		if (cr.getPartner().getId()!=Partner.CLIENT&&cr.getPartner().getId()!=Partner.SYSTEM
        				&&cr.getIsActive()!=null&&cr.getIsActive()==ActiveStatus.ACTIVE){
        			creditsCB.add(cr);
        		}
        		
        		if (cr.getPartner().getId()==Partner.CLIENT){
        			clientCredit=cr;
        			clientCredits.add(cr);
        		}
        			
        		if (cr.getPartner().getId()==Partner.SYSTEM){
        			if (cr.getCreditDataEndFact()!=null){
        			    systemCreditDays+=DatesUtils.daysDiff(cr.getCreditDataEndFact(), cr.getCreditDataBeg());
        			}
        			if (cr.getCreditSum()>maxSystemCreditSum){
        				maxSystemCreditSum=cr.getCreditSum();
        			}
        			systemCredits.add(cr);
        			if (cr.getIsActive()!=null&&cr.getIsActive()==ActiveStatus.ACTIVE
        					&&cr.getIsOver()!=null&&!cr.getIsOver()){
        				systemCreditActive=cr;
        			}
        			if (cr.getIsActive()!=null&&cr.getIsActive()==ActiveStatus.ACTIVE
        					&&cr.getIsOver()!=null&&cr.getIsOver()&&cr.getCreditDataEndFact()!=null
        					&&cr.getCreditStatus().getCodeInteger()==BaseCredit.CREDIT_CLOSED&&
        					(lastCredit==null||(lastCredit!=null&&lastCredit.getCreditDataBeg().before(cr.getCreditDataBeg())))){
        				
        				    lastCredit=cr;
        				
        			}
        		}
        	}//end for
			if (systemCreditDays>0&&getReallyClosedCreditsCount()>0){
				averageSystemCreditDays=systemCreditDays/getReallyClosedCreditsCount();
			}
            if (systemCredits.size() != 0) {
                Collections.sort(systemCredits, new Comparator<Credit>() {
                    @Override
                    public int compare(Credit o1, Credit o2) {
                        return o2.getCreditDataBeg().compareTo(o1.getCreditDataBeg());
                    }
                });
            }
		}

        // вопросы человеку
        if (options != null && options.contains(Options.INIT_ANSWERS)) {
            Utils.initCollection(entity.getAnswers(), options);
            for (QuestionAnswerEntity en: entity.getAnswers()) {
                answers.add(new QuestionAnswer(en));
            }
            Collections.sort(answers, new Comparator<QuestionAnswer>() {
                @Override
                public int compare(QuestionAnswer o1, QuestionAnswer o2) {
                    if (o2.getCreditRequest() != null && o1.getCreditRequest() != null) {
                        return o2.getCreditRequest().getDateContest().compareTo(o1.getCreditRequest().getDateContest());
                    }
                    return 0;
                }
            });
        }
	
		//занятость
		if (options != null && options.contains(Options.INIT_EMPLOYMENT)) {
		Utils.initCollection(entity.getEmploys(), options);
        	for (EmploymentEntity empEnt: entity.getEmploys()) {
        		Employment emp = new Employment(empEnt);
        		emp.init(options);
        		employs.add(emp);
        		if (emp.getCurrent()==Employment.CURRENT && emp.getPartner().getId()==Partner.CLIENT){
        			currentEmployment=emp;
        		}
        	}
		}
		
		//скоринг
		if (options != null && options.contains(Options.INIT_SCORING)) {
		Utils.initCollection(entity.getScorings(), options);
        	for (ScoringEntity scoEnt: entity.getScorings()) {
        		Scoring sco = new Scoring(scoEnt);
        		sco.init(options);
        		scorings.add(sco);
        	}
		}

  }
	
	@XmlElement
	public void setPeopleContacts(List<PeopleContact> peopleContacts) {
		this.peopleContacts = peopleContacts;
	}

	@XmlElement
	public void setRegisterAddress(FiasAddress registerAddress) {
		this.registerAddress = registerAddress;
	}

	@XmlElement
	public void setResidentAddress(FiasAddress residentAddress) {
		this.residentAddress = residentAddress;
	}

	@XmlElement
	public void setEmail(PeopleContact email) {
		this.email = email;
	}

	@XmlElement
	public void setHomePhone(PeopleContact homePhone) {
		this.homePhone = homePhone;
	}

	@XmlElement
	public void setHomePhoneReg(PeopleContact homePhoneReg) {
		this.homePhoneReg = homePhoneReg;
	}

	@XmlElement
	public void setVk(PeopleContact vk) {
		this.vk = vk;
	}

	@XmlElement
	public void setOdk(PeopleContact odk) {
		this.odk = odk;
	}

	@XmlElement
	public void setMm(PeopleContact mm) {
		this.mm = mm;
	}

	@XmlElement
	public void setFb(PeopleContact fb) {
		this.fb = fb;
	}

	@XmlElement
	public void setTt(PeopleContact tt) {
		this.tt = tt;
	}

	@XmlElement
	public void setSpouses(List<Spouse> spouses) {
		this.spouses = spouses;
	}

	@XmlElement
	public void setNegatives(List<Negative> negatives) {
		this.negatives = negatives;
	}

	@XmlElement
	public void setPeoplebehaviors(List<PeopleBehavior> peoplebehaviors) {
		this.peoplebehaviors = peoplebehaviors;
	}
	
	@XmlElement
	public void setPeoplebonus(List<PeopleBonus> peoplebonus) {
		this.peoplebonus = peoplebonus;
	}
	
	@XmlElement
	public void setPeopleFriendsForBonus(List<PeopleFriend> peopleFriendsForBonus) {
		this.peopleFriendsForBonus = peopleFriendsForBonus;
	}
	
	@XmlElement
	public void setPeopleFriendsNotForBonus(List<PeopleFriend> peopleFriendsNotForBonus) {
		this.peopleFriendsNotForBonus = peopleFriendsNotForBonus;
	}
	
	public List<Requests> getRequests() {
	    return requests;
	}

	public List<Messages> getMessages() {
		return messages;
	}

	public Double getBonusamount() {
		return bonusamount;
	}
	
	public Credit getSystemCreditActive() {
		return systemCreditActive;
	}

	public void setSystemCreditActive(Credit systemCreditActive) {
		this.systemCreditActive = systemCreditActive;
	}

	public Credit getLastCredit() {
		return lastCredit;
	}

	public void setLastCredit(Credit lastCredit) {
		this.lastCredit = lastCredit;
	}

	public List<DocumentMedia> getDocumentMedia() {
		return documentMedia;
	}

	public void setDocumentMedia(List<DocumentMedia> documentMedia) {
		this.documentMedia = documentMedia;
	}

	public List<Collector> getCollectors() {
		return collectors;
	}

	public void setCollectors(List<Collector> collectors) {
		this.collectors = collectors;
	}

	public List<OfficialDocuments> getOfficialDocuments() {
		return officialDocuments;
	}

	public void setOfficialDocuments(List<OfficialDocuments> officialDocuments) {
		this.officialDocuments = officialDocuments;
	}

	public List<FiasAddress> getAddressesActive() {
		return addressesActive;
	}

	public void setAddressesActive(List<FiasAddress> addressesActive) {
		this.addressesActive = addressesActive;
	}

	public List<EventLog> getEvents() {
		return events;
	}

	public List<Credit> getCreditsCB() {
		return creditsCB;
	}

	public void setCreditsCB(List<Credit> creditsCB) {
		this.creditsCB = creditsCB;
	}

	public List<PeopleSums> getPeopleSums() {
		return peopleSums;
	}

	public void setPeopleSums(List<PeopleSums> peopleSums) {
		this.peopleSums = peopleSums;
	}

	public Boolean getInBlacklist() {
        if (blackLists == null) {
            return null;
        }

        for (BlackList blackList : blackLists) {
            if (blackList.isActive()) {
                return true;
            }
        }

        return false;
    }

	public Integer getAverageSystemCreditDays() {
		return averageSystemCreditDays;
	}

	public Double getMaxSystemCreditSum() {
		return maxSystemCreditSum;
	}

	public void setMaxSystemCreditSum(Double maxSystemCreditSum) {
		this.maxSystemCreditSum = maxSystemCreditSum;
	}

    public List<Messages> getAutoSendedEmailMessages() {
        return autoSendedEmailMessages;
    }

    public void setAutoSendedEmailMessages(List<Messages> autoSendedEmailMessages) {
        this.autoSendedEmailMessages = autoSendedEmailMessages;
    }

    public List<Messages> getAutoSendedSmsMessages() {
        return autoSendedSmsMessages;
    }

    public void setAutoSendedSmsMessages(List<Messages> autoSendedSmsMessages) {
        this.autoSendedSmsMessages = autoSendedSmsMessages;
    }

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }
}

