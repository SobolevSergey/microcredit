package ru.simplgroupp.persistence;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;

import javax.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Заявка на кредит
 */
public class CreditRequestEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7830068906724729487L;

    protected Integer txVersion = 0;

    /**
     * согласие
     */
    private Boolean contest;

    /**
     * подтверждение заявки
     */
    private Boolean confirmed;

    /**
     * согласия - АСП, ПД, КБ
     */
    private Boolean contestAsp;

    private Boolean contestPd;

    private Boolean contestCb;

    /**
     * дата согласия
     */
    private Date datecontest;

    /**
     * дней заявки
     */
    private Integer creditdays;

    /**
     * дней заявки запрошенных
     */
    private Integer creditdaysInitial;

    /**
     * сумма заявки
     */
    private Double creditsum;

    /**
     * сумма заявки запрошенная
     */
    private Double creditsumInitial;

    /**
     * ставка заявки
     */
    private Double stake;

    /**
     * предложеная ставка заявки
     */
    private Double initialStake;

    /**
     * комментарии
     */
    private String comment;

    /**
     * уникальный номер
     */
    private String uniquenomer;

    /**
     * оферта, текст
     */
    private String agreement;

    /**
     * код смс
     */
    private String smsCode;

    /**
     * была ли одобрена
     */
    private Integer accepted;

    /**
     * номер в течение дня
     */
    private Integer nomer;

    /**
     * дата изменения статуса
     */
    private Date dateStatus;

    /**
     * дата подписания оферты
     */
    private Date dateSign;

    /**
     * дата окончательного заполнения анкеты
     */
    private Date dateFill;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * причина отказа по справочнику
     */
    private RefuseReasonEntity rejectreasonId;

    /**
     * статус заявки
     */
    private CreditStatusEntity statusId;

    /**
     * одобренный кредит
     */
    private CreditEntity acceptedcreditId;

    /**
     * счет для перечисления денег
     */
    private AccountEntity accountId;

    /**
     * была ли выгружена заявка
     */
    private Boolean isUploaded;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * Часовой пояс клиента, откуда была отправлена заявка
     */
    private Integer clientTimezone;

    /**
     * код региона
     */
    private RegionsEntity region;

    /**
     * город, откуда отправлена заявка
     */
    private String city;

    /**
     * продукт
     */
    private ProductsEntity productId;

    /**
     * цель кредитования
     */
    private ReferenceEntity creditPurposeId;

    /**
     * способ получения заявки. справочник 66
     */
    private ReferenceEntity wayId;
    /**
     * дата-время принятия решения по заявке
     */
    private Date dateDecision;
    /**
     * как было послано
     */
    private ReferenceEntity decisionWayId;
    /**
     * адреса
     */
    private List<AddressEntity> addresses = new ArrayList<>(0);

    /**
     * кредиты
     */
    private List<CreditEntity> credits = new ArrayList<>(0);

    /**
     * документы
     */
    private List<DocumentEntity> documents = new ArrayList<>(0);

    /**
     * подписанные человеком документы
     */
    private List<OfficialDocumentsEntity> officialDocuments = new ArrayList<>(0);

    /**
     * занятость
     */
    private List<EmploymentEntity> employs = new ArrayList<>(0);

    /**
     * логи
     */
    private List<EventLogEntity> logs = new ArrayList<>(0);

    /**
     * персональные данные
     */
    private List<PeoplePersonalEntity> peoplepersonal = new ArrayList<>(0);

    /**
     * люди - дополнительные данные
     */
    private List<PeopleMiscEntity> peoplemisc = new ArrayList<>(0);

    /**
     * запросы
     */
    private List<RequestsEntity> requests = new ArrayList<>(0);

    /**
     * скоринг
     */
    private List<ScoringEntity> scorings = new ArrayList<>(0);

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
     * ответы на вопросы по заявке
     */
    private List<QuestionAnswerEntity> answers = new ArrayList<>(0);

    /**
     * информация об устройстве, с которого подается заявка
     */
    private List<DeviceInfoEntity> deviceInfos = new ArrayList<>(0);

    /**
     * недееспособность
     */
    private List<PeopleIncapacityEntity> peopleIncapacity = new ArrayList<>(0);

    /**
     * поля по антифроду
     */
    private List<AntifraudFieldEntity> antifraudFields = new ArrayList<>(0);

    /**
     * страницы по антифроду
     */
    private List<AntifraudPageEntity> antifraudPages = new ArrayList<>(0);

    /**
     * сработавшие антимошеннические правила для заявки
     */
    private List<AntifraudSuspicionEntity> antifraudSuspicions = new ArrayList<>(0);

    /**
     * список мошшенничеств
     */
    private List<AntifraudOccasionEntity> antifraudOccasions = new ArrayList<>(0);

    /**
     * вопросы для идентификации
     */
    private List<IdentityQuestionEntity> identityQuestions = new ArrayList<>(0);

    /**
     * кредиты
     */
    private List<CreditOfferEntity> creditOffers = new ArrayList<>(0);

    /**
     * Подразделение
     */
    private WorkplaceEntity workplace;

    /**
     * Автор
     */
    private UsersEntity user;


    public CreditRequestEntity() {
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Boolean getContest() {
        return contest;
    }

    public void setContest(Boolean contest) {
        this.contest = contest;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getContestAsp() {
        return contestAsp;
    }

    public void setContestAsp(Boolean contestAsp) {
        this.contestAsp = contestAsp;
    }

    public Boolean getContestCb() {
        return contestCb;
    }

    public void setContestCb(Boolean contestCb) {
        this.contestCb = contestCb;
    }

    public Boolean getContestPd() {
        return contestPd;
    }

    public void setContestPd(Boolean contestPd) {
        this.contestPd = contestPd;
    }

    public Date getDatecontest() {
        return datecontest;
    }

    public void setDatecontest(Date datecontest) {
        this.datecontest = datecontest;
    }

    public Integer getCreditdays() {
        return creditdays;
    }

    public void setCreditdays(Integer creditdays) {
        this.creditdays = creditdays;
    }

    public Double getCreditsum() {
        return creditsum;
    }

    public void setCreditsum(Double creditsum) {
        this.creditsum = creditsum;
    }

    @XmlElement
    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public Double getStake() {
        return stake;
    }

    public void setStake(Double stake) {
        this.stake = stake;
    }

    public Double getInitialStake() {
        return initialStake;
    }

    public void setInitialStake(Double initialStake) {
        this.initialStake = initialStake;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUniquenomer() {
        return uniquenomer;
    }

    public void setUniquenomer(String uniquenomer) {
        this.uniquenomer = uniquenomer;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public Integer getNomer() {
        return nomer;
    }

    public void setNomer(Integer nomer) {
        this.nomer = nomer;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<VerificationEntity> getVerif() {
        return verif;
    }

    public void setVerif(List<VerificationEntity> verif) {
        this.verif = verif;
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

    public List<CreditEntity> getCredits() {
        return credits;
    }

    public void setCredits(List<CreditEntity> credits) {
        this.credits = credits;
    }

    public List<SummaryEntity> getSummary() {
        return summary;
    }

    public void setSummary(List<SummaryEntity> summary) {
        this.summary = summary;
    }

    public List<PhonePaySummaryEntity> getPhonesummary() {
        return phonesummary;
    }

    public void setPhonesummary(List<PhonePaySummaryEntity> phonesummary) {
        this.phonesummary = phonesummary;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public List<EmploymentEntity> getEmploys() {
        return employs;
    }

    public void setEmploys(List<EmploymentEntity> employs) {
        this.employs = employs;
    }

    public List<EventLogEntity> getLogs() {
        return logs;
    }

    public void setLogs(List<EventLogEntity> logs) {
        this.logs = logs;
    }

    public List<DebtEntity> getDebts() {
        return debts;
    }

    public void setDebts(List<DebtEntity> debts) {
        this.debts = debts;
    }

    public Date getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public RefuseReasonEntity getRejectreasonId() {
        return rejectreasonId;
    }

    public void setRejectreasonId(RefuseReasonEntity rejectreasonId) {
        this.rejectreasonId = rejectreasonId;
    }

    public CreditStatusEntity getStatusId() {
        return statusId;
    }

    public void setStatusId(CreditStatusEntity statusId) {
        this.statusId = statusId;
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

    public List<RequestsEntity> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestsEntity> requests) {
        this.requests = requests;
    }

    public List<ScoringEntity> getScorings() {
        return scorings;
    }

    public void setScorings(List<ScoringEntity> scorings) {
        this.scorings = scorings;
    }

    public List<QuestionAnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerEntity> answers) {
        this.answers = answers;
    }

    public List<DeviceInfoEntity> getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(List<DeviceInfoEntity> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }


    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public RegionsEntity getRegion() {
        return region;
    }

    public void setRegion(RegionsEntity region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getHasCredits() {
        if (getCredits().size() > 0) {
            return getCredits().size();
        } else {
            return 0;
        }
    }

    public Integer getHasCreditOffers() {
        if (getCreditOffers().size() > 0) {
            return getCreditOffers().size();
        } else {
            return 0;
        }
    }

    public Integer getHasVerif() {
        if (getVerif().size() > 0) {
            return getVerif().size();
        } else {
            return 0;
        }
    }

    public Integer getHasNegatives() {
        if (getNegatives().size() > 0) {
            return getNegatives().size();
        } else {
            return 0;
        }
    }

    public Integer getHasPeoplebehavior() {
        if (getPeoplebehavior().size() > 0) {
            return getPeoplebehavior().size();
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

    public Integer getHasSummary() {
        if (getSummary().size() > 0) {
            return getSummary().size();
        } else {
            return 0;
        }
    }

    public Integer getHasPhoneSummary() {
        if (getPhonesummary().size() > 0) {
            return getPhonesummary().size();
        } else {
            return 0;
        }
    }

    public Integer getHasDebts() {
        if (getDebts().size() > 0) {
            return getDebts().size();
        } else {
            return 0;
        }
    }

    public Integer getHasOfficialDocuments() {
        if (getOfficialDocuments().size() > 0) {
            return getOfficialDocuments().size();
        } else {
            return 0;
        }
    }

    public Integer getHasRequests() {
        if (getRequests().size() > 0) {
            return getRequests().size();
        } else {
            return 0;
        }
    }

    public Integer getHasScorings() {
        if (getScorings().size() > 0) {
            return getScorings().size();
        } else {
            return 0;
        }
    }

    public Integer getHasAnswers() {
        if (getAnswers().size() > 0) {
            return getAnswers().size();
        } else {
            return 0;
        }
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

    public Integer getHasIdentityQuestions() {
        if (getIdentityQuestions().size() > 0) {
            return getIdentityQuestions().size();
        } else {
            return 0;
        }
    }

    public AccountEntity getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountEntity accountId) {
        this.accountId = accountId;
    }

    public CreditEntity getAcceptedcreditId() {
        return acceptedcreditId;
    }

    public void setAcceptedcreditId(CreditEntity acceptedcreditId) {
        this.acceptedcreditId = acceptedcreditId;
    }

    @Override
    public String toString() {
        return "Заявка на " + creditsum.toString() + " руб, на " + creditdays.toString() + " дней, " +
                "подана " + datecontest.toString();
    }

    public boolean isOfertaSigned() {
        return (StringUtils.isNotEmpty(smsCode));
    }

    public Integer getClientTimezone() {
        return clientTimezone;
    }

    public void setClientTimezone(Integer clientTimezone) {
        this.clientTimezone = clientTimezone;
    }


    public Date getDateFill() {
        return dateFill;
    }

    public void setDateFill(Date dateFill) {
        this.dateFill = dateFill;
    }

    public ProductsEntity getProductId() {
        return productId;
    }

    public void setProductId(ProductsEntity productId) {
        this.productId = productId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public List<PeopleIncapacityEntity> getPeopleIncapacity() {
        return peopleIncapacity;
    }

    public void setPeopleIncapacity(List<PeopleIncapacityEntity> peopleIncapacity) {
        this.peopleIncapacity = peopleIncapacity;
    }

    public ReferenceEntity getCreditPurposeId() {
        return creditPurposeId;
    }

    public void setCreditPurposeId(ReferenceEntity creditPurposeId) {
        this.creditPurposeId = creditPurposeId;
    }

    public ReferenceEntity getWayId() {
        return wayId;
    }

    public void setWayId(ReferenceEntity wayId) {
        this.wayId = wayId;
    }


    public Integer getCreditdaysInitial() {
        return creditdaysInitial;
    }

    public void setCreditdaysInitial(Integer creditdaysInitial) {
        this.creditdaysInitial = creditdaysInitial;
    }

    public Double getCreditsumInitial() {
        return creditsumInitial;
    }

    public void setCreditsumInitial(Double creditsumInitial) {
        this.creditsumInitial = creditsumInitial;
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


    public List<IdentityQuestionEntity> getIdentityQuestions() {
        return identityQuestions;
    }

    public void setIdentityQuestions(List<IdentityQuestionEntity> identityQuestions) {
        this.identityQuestions = identityQuestions;
    }

    public List<OfficialDocumentsEntity> getOfficialDocuments() {
        return officialDocuments;
    }

    public void setOfficialDocuments(List<OfficialDocumentsEntity> officialDocuments) {
        this.officialDocuments = officialDocuments;
    }

    public List<CreditOfferEntity> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOfferEntity> creditOffers) {
        this.creditOffers = creditOffers;
    }

    public Integer getHasDocuments() {
        if (getDocuments().size() > 0) {
            return getDocuments().size();
        } else {
            return 0;
        }
    }

    public List<AntifraudSuspicionEntity> getAntifraudSuspicions() {
        return antifraudSuspicions;
    }

    public void setAntifraudSuspicions(List<AntifraudSuspicionEntity> antifraudSuspicions) {
        this.antifraudSuspicions = antifraudSuspicions;
    }

    public List<AntifraudOccasionEntity> getAntifraudOccasions() {
        return antifraudOccasions;
    }

    public void setAntifraudOccasions(List<AntifraudOccasionEntity> antifraudOccasions) {
        this.antifraudOccasions = antifraudOccasions;
    }

    public WorkplaceEntity getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

	public Date getDateDecision() {
		return dateDecision;
	}

	public void setDateDecision(Date dateDecision) {
		this.dateDecision = dateDecision;
	}

	public ReferenceEntity getDecisionWayId() {
		return decisionWayId;
	}

	public void setDecisionWayId(ReferenceEntity decisionWayId) {
		this.decisionWayId = decisionWayId;
	}
    
    
}
