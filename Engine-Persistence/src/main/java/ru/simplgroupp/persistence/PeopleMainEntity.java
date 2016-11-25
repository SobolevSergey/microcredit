package ru.simplgroupp.persistence;

import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

/**
 * Заголовок для человека, здесь храним только идентификаторы
 */
public class PeopleMainEntity extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 6899578102640537194L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * инн
     */
    private String inn;

    /**
     * ИИН
     */
    private String iin;

    /**
     * снилс
     */
    private String snils;

    /**
     * счета
     */
    private List<AccountEntity> accounts = new ArrayList<>(0);

    /**
     * персональные данные
     */
    private List<PeoplePersonalEntity> peoplepersonal = new ArrayList<>(0);

    /**
     * адреса
     */
    private List<AddressEntity> addresses = new ArrayList<>(0);

    /**
     * черные списки
     */
    private List<BlacklistEntity> blacklists = new ArrayList<>(0);

    /**
     * кредиты
     */
    private List<CreditEntity> credits = new ArrayList<>(0);
    /**
     * кредиты, выданные системой
     */
    private List<CreditEntity> systemCredits = new ArrayList<>(0);
    /**
     * логи
     */
    private List<EventLogEntity> logs = new ArrayList<>(0);
    /**
     * документы
     */
    private List<DocumentEntity> documents = new ArrayList<>(0);
    /**
     *  другие документы
     */
    private List<DocumentOtherEntity> documentsother = new ArrayList<>(0);
    /**
     * документы
     */
    private List<OfficialDocumentsEntity> officialDocuments = new ArrayList<>(0);

    /**
     * сканы документов
     */
    private List<DocumentMediaEntity> documentMedia = new ArrayList<>(0);

    /**
     * занятость
     */
    private List<EmploymentEntity> employs = new ArrayList<>(0);

    /**
     * люди - дополнительные данные
     */
    private List<PeopleMiscEntity> peoplemisc = new ArrayList<>(0);

    /**
     * люди - Другие дополнительные данные
     */
    private List<PeopleOtherEntity> peopleother = new ArrayList<>(0);

    /**
     * люди - контактные данные
     */
    private List<PeopleContactEntity> peoplecontact = new ArrayList<>(0);

    /**
     * скоринг
     */
    private List<ScoringEntity> scorings = new ArrayList<>(0);

    /**
     * Заявки
     */
    private List<CreditRequestEntity> creditreq = new ArrayList<>(0);

    /**
     * супруги
     */
    private List<SpouseEntity> spouses = new ArrayList<>(0);

    /**
     * долги
     */
    private List<DebtEntity> debts = new ArrayList<>(0);

    /**
     * верификация
     */
    private List<VerificationEntity> verif = new ArrayList<>(0);

    /**
     * суммарная информация по кредиту
     */
    private List<SummaryEntity> summary = new ArrayList<>(0);

    /**
     * информация по негативу
     */
    private List<NegativeEntity> negatives = new ArrayList<>(0);

    /**
     * информация по поведению пользователя
     */
    private List<PeopleBehaviorEntity> peoplebehavior = new ArrayList<>(0);

    /**
     * суммарная информация по платежам за телефон
     */
    private List<PhonePaySummaryEntity> phonesummary = new ArrayList<>(0);

    /**
     * бонусы
     */
    private List<PeopleBonusEntity> peopleBonus = new ArrayList<>(0);

    /**
     * друзья
     */
    private List<PeopleFriendEntity> peoplefriends = new ArrayList<>(0);
    /**
     * суммы в системе
     */
    private List<PeopleSumsEntity> peopleSums=new ArrayList<>(0); 
    /**
     * недееспособность
     */
    private List<PeopleIncapacityEntity> peopleIncapacity = new ArrayList<>(0);

    /**
     * запросы
     */
    private List<RequestsEntity> requests = new ArrayList<>(0);

    /**
     * сообщения человеку
     */
    private List<MessagesEntity> messages = new ArrayList<>(0);

    /**
     * установки для пользователя
     */
    private UserPropertiesEntity userproperties;

    /**
     * у коллектора
     */
    private List<CollectorEntity> collectors = new ArrayList<>(0);

    /**
     * поля по антифроду
     */
    private List<AntifraudFieldEntity> antifraudFields = new ArrayList<>(0);
    
    /**
     * страницы по антифроду
     */
    private List<AntifraudPageEntity> antifraudPages = new ArrayList<>(0);

    /**
     * все вопросы верификатора по человку
     */
    private List<QuestionAnswerEntity> answers = new ArrayList<>(0);
    
    public PeopleMainEntity() {
    }

    public PeopleMainEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public List<PeoplePersonalEntity> getPeoplepersonal() {
        return peoplepersonal;
    }

    public void setPeoplepersonal(List<PeoplePersonalEntity> peoplepersonal) {
        this.peoplepersonal = peoplepersonal;
    }

    public List<PeopleMiscEntity> getPeoplemisc() {
        return peoplemisc;
    }

    public void setPeoplemisc(List<PeopleMiscEntity> peoplemisc) {
        this.peoplemisc = peoplemisc;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<BlacklistEntity> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(List<BlacklistEntity> blacklists) {
        this.blacklists = blacklists;
    }

    public List<CreditEntity> getCredits() {
        return credits;
    }

    public List<CreditEntity> getSystemCredits() {
        return systemCredits;
    }

    public void setSystemCredits(List<CreditEntity> systemCredits) {
        this.systemCredits = systemCredits;
    }

    public void setCredits(List<CreditEntity> credits) {
        this.credits = credits;
    }

    public List<VerificationEntity> getVerif() {
        return verif;
    }

    public void setVerif(List<VerificationEntity> verif) {
        this.verif = verif;
    }

    public List<CreditRequestEntity> getCreditreq() {
        return creditreq;
    }

    public void setCreditreq(List<CreditRequestEntity> creditreq) {
        this.creditreq = creditreq;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public List<PeopleContactEntity> getPeoplecontact() {
        return peoplecontact;
    }

    public void setPeoplecontact(List<PeopleContactEntity> peoplecontact) {
        this.peoplecontact = peoplecontact;
    }


    public List<EmploymentEntity> getEmploys() {
        return employs;
    }

    public void setEmploys(List<EmploymentEntity> employs) {
        this.employs = employs;
    }

    public List<PhonePaySummaryEntity> getPhonesummary() {
        return phonesummary;
    }

    public void setPhonesummary(List<PhonePaySummaryEntity> phonesummary) {
        this.phonesummary = phonesummary;
    }

    public List<ScoringEntity> getScorings() {
        return scorings;
    }

    public void setScorings(List<ScoringEntity> scorings) {
        this.scorings = scorings;
    }

    public List<SpouseEntity> getSpouses() {
        return spouses;
    }

    public void setSpouses(List<SpouseEntity> spouses) {
        this.spouses = spouses;
    }

    public List<SummaryEntity> getSummary() {
        return summary;
    }

    public void setSummary(List<SummaryEntity> summary) {
        this.summary = summary;
    }

    public List<NegativeEntity> getNegatives() {
        return negatives;
    }

    public void setNegatives(List<NegativeEntity> negatives) {
        this.negatives = negatives;
    }

    public List<PeopleBehaviorEntity> getPeoplebehavior() {
        return peoplebehavior;
    }

    public void setPeoplebehavior(List<PeopleBehaviorEntity> peoplebehavior) {
        this.peoplebehavior = peoplebehavior;
    }

    public List<PeopleBonusEntity> getPeopleBonus() {
        return peopleBonus;
    }

    public void setPeopleBonus(List<PeopleBonusEntity> peopleBonus) {
        this.peopleBonus = peopleBonus;
    }

    public List<PeopleFriendEntity> getPeoplefriends() {
        return peoplefriends;
    }

    public void setPeoplefriends(List<PeopleFriendEntity> peoplefriends) {
        this.peoplefriends = peoplefriends;
    }

    public List<DocumentMediaEntity> getDocumentMedia() {
        return documentMedia;
    }

    public void setDocumentMedia(List<DocumentMediaEntity> documentMedia) {
        this.documentMedia = documentMedia;
    }

    public List<CollectorEntity> getCollectors() {
        return collectors;
    }

    public void setCollectors(List<CollectorEntity> collectors) {
        this.collectors = collectors;
    }

    public List<DocumentOtherEntity> getDocumentsother() {
        return documentsother;
    }

    public void setDocumentsother(List<DocumentOtherEntity> documentsother) {
        this.documentsother = documentsother;
    }

    //кол-во активных займов, выданных системой
    public Integer getActiveCreditsCount() {
        Integer acr = 0;
        if (getCredits().size() > 0) {
            for (CreditEntity credit : getCredits()) {
                if (credit.getIsover() != null) {
                    if (credit.getIsover() == false && credit.getPartnersId().getId() == 1) {
                        acr = acr + 1;
                    }
                }
            }
        }
        return acr;
    }

    //кол-во закрытых займов, выданных системой
    public Integer getClosedCreditsCount() {
        Integer acr = 0;
        if (getCredits().size() > 0) {
            for (CreditEntity credit : getCredits()) {
                if (credit.getIsover() != null) {
                    if (credit.getIsover() == true && credit.getPartnersId().getId() == 1) {
                        acr = acr + 1;
                    }
                }
            }
        }
        return acr;
    }

    //кол-во закрытых займов, выданных системой, не считая отмененные
    public Integer getReallyClosedCreditsCount() {
        Integer acr = 0;
        if (getCredits().size() > 0) {
            for (CreditEntity credit : getCredits()) {
                if (credit.getIsover() != null) {
                    if (credit.getIsover() == true && credit.getPartnersId().getId() == 1
                    		&&credit.getCreditStatusId()!=null&&credit.getCreditStatusId().getCodeinteger()!=7) {
                        acr = acr + 1;
                    }
                }
            }
        }
        return acr;
    }
    
    //кол-во закрытых займов без просрочки, выданных системой
    public Integer getClosedCreditsWithoutDelayCount() {
        Integer acr = 0;
        if (getCredits().size() > 0) {
            for (CreditEntity credit : getCredits()) {
                if (credit.getIsover() != null) {
                    if (credit.getIsover() == true && credit.getPartnersId().getId() == 1 &&
                            credit.getCreditdataendfact().before(credit.getCreditdataend())) {
                        acr = acr + 1;
                    }
                }
            }
        }
        return acr;
    }

    //кол-во просроченных займов, выданных системой
    public Integer getDelayCreditsCount() {
        Integer acr = 0;
        if (getCredits().size() > 0) {
            for (CreditEntity credit : getCredits()) {
                if (credit.getIsover() != null && credit.getCreditdataend() != null) {
                    if (((credit.getIsover() == false && credit.getCreditdataend().before(new Date()))) &&
                            credit.getPartnersId().getId() == 1) {
                        acr = acr + 1;
                    } else {
                        if (credit.getCreditdataendfact() != null) {
                            if (((credit.getIsover() == true && credit.getPartnersId().getId() == 1 &&
                                    (credit.getCreditdataend().before(credit.getCreditdataendfact())&&
                                     !DateUtils.isSameDay(credit.getCreditdataend(), credit.getCreditdataendfact())	)))) {
                                acr = acr + 1;
                            }
                        }
                    }
                }
            }
        }
        return acr;
    }

    public Integer getHasPhoneSummary() {
        if (getPhonesummary().size() > 0) {
            return getPhonesummary().size();
        }
        return 0;
    }

    public Integer getHasSummary() {
        if (getSummary().size() > 0) {
            return getSummary().size();
        }
        return 0;
    }

    public Integer getHasCredits() {
        if (getCredits().size() > 0) {
            return getCredits().size();
        }
        return 0;
    }

    public Integer getHasCollectors() {
        if (getCollectors().size() > 0) {
            return getCollectors().size();
        }
        return 0;
    }

    public Integer getHasCreditreq() {
        if (getCreditreq().size() > 0) {
            return getCreditreq().size();
        }
        return 0;
    }

    public Integer getHasVerif() {
        if (getVerif().size() > 0) {
            return getVerif().size();
        }
        return 0;
    }

    public Integer getHasScorings() {
        if (getScorings().size() > 0) {
            return getScorings().size();
        }
        return 0;
    }

    public Integer getHasAccounts() {
        if (getAccounts().size() > 0) {
            return getAccounts().size();
        }
        return 0;
    }

    public Integer getHasPeopleContacts() {
        if (getPeoplecontact().size() > 0) {
            return getPeoplecontact().size();
        }
        return 0;
    }

    public Integer getHasAddresses() {
        if (getAddresses().size() > 0) {
            return getAddresses().size();
        }
        return 0;
    }

    public Integer getHasDocuments() {
        if (getDocuments().size() > 0) {
            return getDocuments().size();
        }
        return 0;
    }

    public Integer getHasOfficialDocuments() {
        if (getOfficialDocuments().size() > 0) {
            return getOfficialDocuments().size();
        }
        return 0;
    }
    
    public Integer getHasDocumentMedia() {
        if (getDocumentMedia().size() > 0) {
            return getDocumentMedia().size();
        }
        return 0;
    }

    public Integer getHasEmploys() {
        if (getEmploys().size() > 0) {
            return getEmploys().size();
        }
        return 0;
    }

    public Integer getHasNegatives() {
        if (getNegatives().size() > 0) {
            return getNegatives().size();
        }
        return 0;
    }

    public Integer getHasPeoplebehavior() {
        if (getPeoplebehavior().size() > 0) {
            return getPeoplebehavior().size();
        }
        return 0;
    }

    public Integer getHasMisc() {
        if (getPeoplemisc().size() > 0) {
            return getPeoplemisc().size();
        }
        return 0;
    }

    public Integer getHasBlacklists() {
        if (getBlacklists().size() > 0) {
            return getBlacklists().size();
        }
        return 0;
    }

    public Integer getHasSpouses() {
        if (getSpouses().size() > 0) {
            return getSpouses().size();
        }
        return 0;
    }

    public Integer getHasDebts() {
        if (getDebts().size() > 0) {
            return getDebts().size();
        }
        return 0;
    }

    public Integer getHasBonus() {
        if (getPeopleBonus().size() > 0) {
            return getPeopleBonus().size();
        }
        return 0;
    }

    public Integer getHasInviteFriends() {
        if (getPeoplefriends().size() > 0) {
            return getPeoplefriends().size();
        } else {
            return 0;
        }
    }

    public Integer getHasIncapacity() {
        if (getPeopleIncapacity().size() > 0) {
            return getPeopleIncapacity().size();
        }
        return 0;
    }

    public Integer getHasRequests() {
        if (getRequests().size() > 0) {
            return getRequests().size();
        }
        return 0;
    }

    public Integer getHasMessages() {
        if (getMessages().size() > 0) {
            return getMessages().size();
        }
        return 0;
    }

    public Integer getHasAntifrodFields() {
        if (getAntifraudFields().size() > 0) {
            return getAntifraudFields().size();
        } else {
            return 0;
        }
    }
    
    public Integer getHasAntifrodPages() {
        if (getAntifraudPages().size() > 0) {
            return getAntifraudPages().size();
        } else {
            return 0;
        }
    }
    
    public List<DebtEntity> getDebts() {
        return debts;
    }

    public void setDebts(List<DebtEntity> debts) {
        this.debts = debts;
    }

    public List<PeopleIncapacityEntity> getPeopleIncapacity() {
        return peopleIncapacity;
    }

    public void setPeopleIncapacity(List<PeopleIncapacityEntity> peopleIncapacity) {
        this.peopleIncapacity = peopleIncapacity;
    }


    public List<RequestsEntity> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestsEntity> requests) {
        this.requests = requests;
    }

    public List<MessagesEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesEntity> messages) {
        this.messages = messages;
    }

    public UserPropertiesEntity getUserproperties() {
        return userproperties;
    }

    public void setUserproperties(UserPropertiesEntity userproperties) {
        this.userproperties = userproperties;
    }

    public List<AntifraudFieldEntity> getAntifraudFields() {
		return antifraudFields;
	}

	public void setAntifraudFields(List<AntifraudFieldEntity> antifraudFields) {
		this.antifraudFields = antifraudFields;
	}

	public List<AntifraudPageEntity> getAntifraudPages() {
		return antifraudPages;
	}

	public void setAntifraudPages(List<AntifraudPageEntity> antifraudPages) {
		this.antifraudPages = antifraudPages;
	}

    public List<QuestionAnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerEntity> answers) {
        this.answers = answers;
    }

    public List<OfficialDocumentsEntity> getOfficialDocuments() {
		return officialDocuments;
	}

	public void setOfficialDocuments(List<OfficialDocumentsEntity> officialDocuments) {
		this.officialDocuments = officialDocuments;
	}

	public List<PeopleSumsEntity> getPeopleSums() {
		return peopleSums;
	}

	public void setPeopleSums(List<PeopleSumsEntity> peopleSums) {
		this.peopleSums = peopleSums;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (!(other.getClass() == getClass())) {
            return false;
        }

        PeopleMainEntity cast = (PeopleMainEntity) other;

        if (this.id == null) {
            return false;
        }

        if (cast.getId() == null) {
            return false;
        }

        return this.id.intValue() == cast.getId().intValue();

    }

    public List<PeopleOtherEntity> getPeopleother() {
        return peopleother;
    }

    public void setPeopleother(List<PeopleOtherEntity> peopleother) {
        this.peopleother = peopleother;
    }

	public List<EventLogEntity> getLogs() {
		return logs;
	}

	public void setLogs(List<EventLogEntity> logs) {
		this.logs = logs;
	}

    
}