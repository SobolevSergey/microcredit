package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.*;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;
import ru.simplgroupp.transfer.antifraud.AntifraudSuspicion;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

@XmlRootElement
@XmlType
/**
 * DTO для {@link ru.simplgroupp.persistence.CreditRequestEntity}
 */
public class CreditRequest extends BaseTransfer<CreditRequestEntity>
        implements Serializable, Initializable, Identifiable {

    public static final int ACCEPTED = 1;
    public static final int NOT_ACCEPTED = 0;
    //причина отказа - кредитная политика
    public static final String UPLOAD_REFUSE_REASON_RS = "0";
    public static final String UPLOAD_REFUSE_REASON_EQUIFAX = "1";
    public static final String UPLOAD_REFUSE_REASON_TEXT = "Кредитная политика займодавца (кредитора)";
    //подача дистанционно - Эквифакс
    public static final int UPLOAD_WAY_CREDITREQUEST_EQUIFAX = 2;
    //подача дистанционно - Русский стандарт
    public static final int UPLOAD_WAY_CREDITREQUEST_RS = 1;
    /**
     *
     */
    private static final long serialVersionUID = -3015998350104896589L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = CreditRequest.class.getConstructor(CreditRequestEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    /**
     * человек
     */
    protected PeopleMain peopleMain;
    /**
     * причина отказа
     */
    protected RefuseReason rejectReason;
    /**
     * адрес регистрации
     */
    protected FiasAddress registerAddress;
    /**
     * адрес проживания
     */
    protected FiasAddress residentAddress;
    /**
     * занятость
     */
    protected List<Employment> employs;
    /**
     * адреса
     */
    protected List<FiasAddress> addresses;
    /**
     * кредит по заявке
     */
    protected Credit acceptedCredit;
    /**
     * кредит клиента
     */
    protected Credit clientCredit;
    /**
     * счет
     */
    protected Account account;
    /**
     * откуда человек пришел
     */
    protected PeopleBehavior behaviorsource;
    /**
     * кредиты
     */
    protected List<Credit> credits;
    /**
     * статус заявки
     */
    protected CreditStatus status;
    /**
     * документы
     */
    protected List<Documents> documents;
    /**
     * скоринги
     */
    protected List<Scoring> scorings;
    /**
     * контакты
     */
    protected List<PeopleContact> contacts;
    /**
     * логи
     */
    protected List<EventLog> events;
    /**
     * запросы
     */
    protected List<Requests> requests;
    /**
     * долги
     */
    protected List<Debt> debts;
    /**
     * долг, переданный в суд
     */
    protected Debt courtDebt;
    /**
     * верификации
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
     * негатив
     */
    protected List<Negative> negatives;
    /**
     * поведение пользователя
     */
    protected List<PeopleBehavior> peoplebehaviors;
    /**
     * ответы на вопросы верификатора
     */
    protected List<QuestionAnswer> answers;
    /**
     * с какого устройства заходил, список
     */
    protected List<DeviceInfo> deviceInfos;
    /**
     * с какого устройства заходил
     */
    protected DeviceInfo deviceInfo;
    /**
     * регион подачи
     */
    protected FMSRegion region;
    /**
     * кредитный продукт
     */
    protected Products product;
    /**
     * партнер
     */
    protected Partner partner;
    /**
     * цель кредита
     */
    protected Reference creditPurpose;
    /**
     * способ получения заявки
     */
    protected Reference way;
    /**
     * способ получения решения
     */
    protected Reference decisionWay;
    /**
     * недееспособность
     */
    protected List<PeopleIncapacity> incapacities;
    /**
     * вопросы идентификации
     */
    protected List<IdentityQuestion> identityQuestions;
    /**
     * документы, подписанные клиентом
     */
    protected List<OfficialDocuments> officialDocuments;
    /**
     * перезаполнение полей анкеты
     */
    protected List<AntifraudField> antifraudFields;
    /**
     * Предложения
     */
    protected List<CreditOffer> creditOffers;
    /**
     * подозрения в мошенничестве, внутренние
     */
    private List<AntifraudSuspicion> antifraudSystemSuspicions;
    /**
     * подозрения в мошенничестве, от Хантера
     */
    private List<AntifraudSuspicion> antifraudHunterSuspicions;
    /**
     * мошенничества
     */
    private List<AntifraudOccasion> antifraudOccasions;


    public CreditRequest() {
        super();
        entity = new CreditRequestEntity();
    }

    public CreditRequest(CreditRequestEntity entity) {
        super(entity);

        status = new CreditStatus(entity.getStatusId());
        peopleMain = new PeopleMain(entity.getPeopleMainId());

        employs = new ArrayList<>(0);
        credits = new ArrayList<>(0);
        documents = new ArrayList<>(0);
        scorings = new ArrayList<>(0);
        requests = new ArrayList<>(0);
        contacts = new ArrayList<>(0);
        events = new ArrayList<>(0);
        addresses = new ArrayList<>(0);
        debts = new ArrayList<>(0);
        verif = new ArrayList<>(0);
        negatives = new ArrayList<>(0);
        peoplebehaviors = new ArrayList<>(0);
        summary = new ArrayList<>(0);
        phoneSummary = new ArrayList<>(0);
        answers = new ArrayList<>(0);
        deviceInfos = new ArrayList<>(0);
        incapacities = new ArrayList<>(0);
        antifraudFields = new ArrayList<>(0);
        antifraudSystemSuspicions = new ArrayList<>(0);
        antifraudHunterSuspicions = new ArrayList<>(0);
        antifraudOccasions = new ArrayList<>(0);
        identityQuestions = new ArrayList<>(0);
        officialDocuments = new ArrayList<>(0);
        creditOffers = new ArrayList<>(0);

        if (entity.getPartnersId() != null) {
            partner = new Partner(entity.getPartnersId());
        }

        if (entity.getAcceptedcreditId() != null) {
            acceptedCredit = new Credit(entity.getAcceptedcreditId());
        }

        if (entity.getAccountId() != null) {
            account = new Account(entity.getAccountId());
        }

        if (entity.getRegion() != null) {
            region = new FMSRegion(entity.getRegion());
        }

        if (entity.getProductId() != null) {
            product = new Products(entity.getProductId());
        }

        if (entity.getCreditPurposeId() != null) {
            creditPurpose = new Reference(entity.getCreditPurposeId());
        }

        if (entity.getWayId() != null) {
            way = new Reference(entity.getWayId());
        }

        if (entity.getDecisionWayId() != null) {
            decisionWay = new Reference(entity.getDecisionWayId());
        }
        
        if (entity.getRejectreasonId() != null) {
            rejectReason = new RefuseReason(entity.getRejectreasonId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {

        peopleMain.init(options);

        //адреса
        if (options != null && options.contains(Options.INIT_ADDRESS)) {
            registerAddress = null;
            residentAddress = null;
            Utils.initCollection(entity.getAddresses(), options);
            for (AddressEntity adrEnt : entity.getAddresses()) {
                if (adrEnt.getAddrtype() != null) {
                    if (adrEnt.getAddrtype().getCodeinteger() == FiasAddress.REGISTER_ADDRESS) {
                        registerAddress = new FiasAddress(adrEnt);
                        addresses.add(registerAddress);
                    } else if (adrEnt.getAddrtype().getCodeinteger() == FiasAddress.RESIDENT_ADDRESS) {
                        residentAddress = new FiasAddress(adrEnt);
                        addresses.add(residentAddress);
                    }
                }
            }
            if (registerAddress == null) {
                registerAddress = new FiasAddress(new AddressEntity(0));
            }
            if (residentAddress == null) {
                residentAddress = new FiasAddress(new AddressEntity(0));
            }
        }

        //занятость
        if (options != null && options.contains(Options.INIT_EMPLOYMENT)) {
            Utils.initCollection(entity.getEmploys(), options);
            for (EmploymentEntity empEnt : entity.getEmploys()) {
                Employment emp = new Employment(empEnt);
                emp.init(options);
                employs.add(emp);
            }
        }

        //ответы на вопросы
        if (options != null && options.contains(Options.INIT_ANSWERS)) {
            Utils.initCollection(entity.getAnswers(), options);
            for (QuestionAnswerEntity ansEnt : entity.getAnswers()) {
                QuestionAnswer qa = new QuestionAnswer(ansEnt);
                qa.init(options);
                answers.add(qa);
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

        //недееспособность
        if (options != null && options.contains(Options.INIT_INCAPACITY)) {
            Utils.initCollection(entity.getPeopleIncapacity(), options);
            for (PeopleIncapacityEntity reqEnt : entity.getPeopleIncapacity()) {
                PeopleIncapacity req = new PeopleIncapacity(reqEnt);
                req.init(options);
                incapacities.add(req);
            }
        }

        //кредит
        Utils.initRelation(entity.getAcceptedcreditId(), options, Options.INIT_CREDIT);

        //история кредита
        if (options != null && options.contains(Options.INIT_CREDIT_HISTORY)) {
            Utils.initCollection(entity.getCredits(), options);
            for (CreditEntity credEnt : entity.getCredits()) {
                Credit credit = new Credit(credEnt);
                credit.init(options);
                credits.add(credit);
                if (credit.getPartner().getId()==Partner.CLIENT){
                	clientCredit=credit;
                }
            }
        }

        //документы
        if (options != null && options.contains(Options.INIT_DOCUMENT)) {
            Utils.initCollection(entity.getDocuments(), options);
            for (DocumentEntity docEnt : entity.getDocuments()) {
                Documents doc = new Documents(docEnt);
                doc.init(options);
                documents.add(doc);
            }
        }

        //подписанные документы
        if (options != null && options.contains(Options.INIT_OFFICIAL_DOCUMENT)) {
            Utils.initCollection(entity.getOfficialDocuments(), options);
            for (OfficialDocumentsEntity doc : entity.getOfficialDocuments()) {
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

        //информация по телефонам
        if (options != null && options.contains(Options.INIT_PHONESUMMARY)) {
            Utils.initCollection(entity.getPhonesummary(), options);
            for (PhonePaySummaryEntity reqEnt : entity.getPhonesummary()) {
                PhonePaySummary req = new PhonePaySummary(reqEnt);
                req.init(options);
                phoneSummary.add(req);
            }
        }

        //долги
        if (options != null && options.contains(Options.INIT_DEBT)) {
            Utils.initCollection(entity.getDebts(), options);
            for (DebtEntity reqEnt : entity.getDebts()) {
                Debt req = new Debt(reqEnt);
                req.init(options);
                debts.add(req);
                if (reqEnt.getAuthorityId() != null && reqEnt.getPartnersId().getId() == Partner.SYSTEM && reqEnt.getAuthorityId().getCodeinteger() == Debt.DEBT_COURT) {
                    courtDebt = new Debt(reqEnt);
                    courtDebt.init(options);
                }
            }
        }

        //верификация
        if (options != null && options.contains(Options.INIT_VERIF)) {
            Utils.initCollection(entity.getVerif(), options);
            for (VerificationEntity reqEnt : entity.getVerif()) {
                Verification req = new Verification(reqEnt);
                req.init(options);
                verif.add(req);
            }
        }

        //негатив
        if (options != null && options.contains(Options.INIT_NEGATIVE)) {
            Utils.initCollection(entity.getNegatives(), options);
            for (NegativeEntity reqEnt : entity.getNegatives()) {
                Negative req = new Negative(reqEnt);
                req.init(options);
                negatives.add(req);
            }
        }

        //поведение пользователя
        if (options != null && options.contains(Options.INIT_BEHAVIOR) && options.contains(Options.INIT_BEHAVIORSOURCE)) {
            Utils.initCollection(entity.getPeoplebehavior(), options);
            for (PeopleBehaviorEntity reqEnt : entity.getPeoplebehavior()) {
                PeopleBehavior req = new PeopleBehavior(reqEnt);
                req.init(options);
                peoplebehaviors.add(req);
                if (reqEnt.getParameterId().getCode().equals("REFER.FROM"))
                    behaviorsource = new PeopleBehavior(reqEnt);
            }
        }

        //устройство подачи заявки
        if (options != null && options.contains(Options.INIT_DEVICEINFO)) {
            Utils.initCollection(entity.getDeviceInfos(), options);
            for (DeviceInfoEntity dinfoEnt : entity.getDeviceInfos()) {
                DeviceInfo info = new DeviceInfo(dinfoEnt);
                info.init(options);
                deviceInfo = info;
                deviceInfos.add(info);
            }
        }

        //скоринг
        if (options != null && options.contains(Options.INIT_SCORING)) {
            Utils.initCollection(entity.getScorings(), options);
            for (ScoringEntity scoEnt : entity.getScorings()) {
                Scoring sco = new Scoring(scoEnt);
                sco.init(options);
                scorings.add(sco);
            }
        }

        //суммарная информация по кредитам
        if (options != null && options.contains(Options.INIT_SUMMARY)) {
            Utils.initCollection(entity.getSummary(), options);
            for (SummaryEntity ent : entity.getSummary()) {
                Summary summ = new Summary(ent);
                summ.init(options);
                summary.add(summ);
            }
        }
        
        // вопросы идентификации
        if (options != null && options.contains(Options.INIT_IDENTITY_QUESTION)) {
            Utils.initCollection(entity.getIdentityQuestions(), options);
            for (IdentityQuestionEntity questionEntity : entity.getIdentityQuestions()) {
                IdentityQuestion question = new IdentityQuestion(questionEntity);
                question.init(options);
                identityQuestions.add(question);
            }
        }

        //перебивание полей
        if (options != null && options.contains(Options.INIT_ANTIFRAUD)) {
            Utils.initCollection(entity.getAntifraudFields(), options);
            for (AntifraudFieldEntity antifraudFieldEntity : entity.getAntifraudFields()) {
                AntifraudField antifraudField = new AntifraudField(antifraudFieldEntity);
                antifraudField.init(options);
                antifraudFields.add(antifraudField);
            }
        }

        // подозрения в мошенничестве
        if (options != null && options.contains(Options.INIT_ANTIFRAUD_SUSPICION)) {
            Utils.initCollection(entity.getAntifraudSuspicions(), options);
            for (AntifraudSuspicionEntity suspicionEntity : entity.getAntifraudSuspicions()) {
                AntifraudSuspicion antifraudSuspicion = new AntifraudSuspicion(suspicionEntity);
                antifraudSuspicion.init(options);

                if (antifraudSuspicion.getPartnersId().getId() == Partner.SYSTEM) {
                    antifraudSystemSuspicions.add(antifraudSuspicion);
                } else if (antifraudSuspicion.getPartnersId().getId() == Partner.OKB_HUNTER) {
                    antifraudHunterSuspicions.add(antifraudSuspicion);
                }
            }
        }

        // мошенничества
        if (options != null && options.contains(Options.INIT_ANTIFRAUD_OCCASION)) {
            Utils.initCollection(entity.getAntifraudOccasions(), options);
            for (AntifraudOccasionEntity occasionEntity : entity.getAntifraudOccasions()) {
                AntifraudOccasion antifraudOccasion = new AntifraudOccasion(occasionEntity);
                antifraudOccasion.init(options);
                antifraudOccasions.add(antifraudOccasion);
            }
        }

        // предложения
        if (options != null && options.contains(Options.INIT_CREDIT_OFFER)) {
            Utils.initCollection(entity.getCreditOffers(), options);
            for (CreditOfferEntity offerEntity : entity.getCreditOffers()) {
                creditOffers.add(new CreditOffer(offerEntity));
            }
        }
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

    public Boolean getConfirmed() {
        return entity.getConfirmed();
    }

    @XmlElement
    public void setConfirmed(Boolean confirmed) {
        entity.setConfirmed(confirmed);
    }

    public Boolean getContestAsp() {
        return entity.getContestAsp();
    }

    @XmlElement
    public void setContestAsp(Boolean contestAsp) {
        entity.setContestAsp(contestAsp);
    }

    public List<AntifraudOccasion> getAntifraudOccasions() {
        return antifraudOccasions;
    }

    public void setAntifraudOccasions(List<AntifraudOccasion> antifraudOccasions) {
        this.antifraudOccasions = antifraudOccasions;
    }

    public Boolean getContestCb() {
        return entity.getContestCb();
    }

    @XmlElement
    public void setContestCb(Boolean contestCb) {
        entity.setContestCb(contestCb);
    }

    public Boolean getContestPd() {
        return entity.getContestPd();
    }

    @XmlElement
    public void setContestPd(Boolean contestPd) {
        entity.setContestPd(contestPd);
    }

    public Boolean getIsUploaded() {
        return entity.getIsUploaded();
    }

    @XmlElement
    public void setIsUploaded(Boolean isUploaded) {
        entity.setIsUploaded(isUploaded);
    }

    public Integer getHasCredits() {
        return entity.getHasCredits();
    }

    public Integer getHasRequests() {
        return entity.getHasRequests();
    }

    public Integer getHasDebts() {
        return entity.getHasDebts();
    }

    public Integer getHasDocuments() {
        return entity.getHasDocuments();
    }

    public Integer getHasOfficialDocuments() {
        return entity.getHasOfficialDocuments();
    }

    public Integer getHasScorings() {
        return entity.getHasScorings();
    }

    public Integer getHasVerif() {
        return entity.getHasVerif();
    }

    public List<Verification> getVerif() {
        return verif;
    }

    public Date getDateContest() {
        return entity.getDatecontest();
    }

    @XmlElement
    public void setDateContest(Date datecontest) {
        entity.setDatecontest(datecontest);
    }

    public Integer getCreditDays() {
        return entity.getCreditdays();
    }

    @XmlElement
    public void setCreditDays(Integer creditdays) {
        entity.setCreditdays(creditdays);
    }

    public Integer getCreditDaysInitial() {
        return entity.getCreditdaysInitial();
    }

    @XmlElement
    public void setCreditDaysInitial(Integer creditdaysInitial) {
        entity.setCreditdaysInitial(creditdaysInitial);
    }

    public Double getCreditSum() {
        return entity.getCreditsum();
    }

    @XmlElement
    public void setCreditSum(Double creditsum) {
        entity.setCreditsum(creditsum);
    }

    public Double getCreditSumInitial() {
        return entity.getCreditsumInitial();
    }

    @XmlElement
    public void setCreditSumInitial(Double creditsumInitial) {
        entity.setCreditsumInitial(creditsumInitial);
    }

    public Double getStake() {
        return entity.getStake();
    }

    @XmlElement
    public void setStake(Double stake) {
        entity.setStake(stake);
    }

    public Double getInitialStake() {
        return entity.getInitialStake();
    }

    @XmlElement
    public void setInitialStake(Double stake) {
        entity.setInitialStake(stake);
    }

    public String getComment() {
        return entity.getComment();
    }

    @XmlElement
    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public String getCity() {
        return entity.getCity();
    }

    @XmlElement
    public void setCity(String city) {
        entity.setCity(city);
    }
    
    public String getAgreement() {
        return entity.getAgreement();
    }

    public void setAgreement(String agreement) {
        entity.setAgreement(agreement);
    }

    public String getSmsCode() {
        return entity.getSmsCode();
    }

    public void setSmsCode(String smsCode) {
        entity.setSmsCode(smsCode);
    }

    public Integer getAccepted() {
        return entity.getAccepted();
    }

    @XmlElement
    public void setAccepted(Integer accepted) {
        entity.setAccepted(accepted);
    }

    public Integer getNomer() {
        return entity.getNomer();
    }

    @XmlElement
    public void setNomer(Integer nomer) {
        entity.setNomer(nomer);
    }

    public String getUniqueNomer() {
        return entity.getUniquenomer();
    }

    @XmlElement
    public void setUniqueNomer(String uniquenomer) {
        entity.setUniquenomer(uniquenomer);
    }

    public FiasAddress getResidentAddresses() {
        return residentAddress;
    }

    public FiasAddress getRegisterAddresses() {
        return registerAddress;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public PeopleBehavior getBehaviorsource() {

        return behaviorsource;
    }

    public List<PeopleContact> getContacts() {
        return contacts;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public List<Employment> getEmploys() {
        return employs;
    }

    public RefuseReason getRejectReason() {
        return rejectReason;
    }

    @XmlElement
    public void setRejectReason(RefuseReason rejectReason) {
        this.rejectReason = rejectReason;
        if (this.rejectReason == null) {
            entity.setRejectreasonId(null);
        } else {
            entity.setRejectreasonId(this.rejectReason.getEntity());
        }
    }

    public CreditStatus getStatus() {
        return status;
    }

    @XmlElement
    public void setStatus(CreditStatus status) {
        this.status = status;
        this.entity.setStatusId(status.getEntity());
    }

    public Date getDateStatus() {
        return entity.getDateStatus();
    }

    @XmlElement
    public void setDateStatus(Date dateStatus) {
        entity.setDateStatus(dateStatus);
    }

    public Date getDateSign() {
        return entity.getDateSign();
    }

    @XmlElement
    public void setDateSign(Date dateSign) {
        entity.setDateSign(dateSign);
    }

    public Date getDateFill() {
        return entity.getDateFill();
    }

    @XmlElement
    public void setDateFill(Date dateFill) {
        entity.setDateFill(dateFill);
    }
    
    public Date getDateDecision() {
        return entity.getDateDecision();
    }

    @XmlElement
    public void setDateDecision(Date dateDecision) {
        entity.setDateDecision(dateDecision);
    }

    public List<Scoring> getScorings() {
        return scorings;
    }

    public List<Requests> getRequests() {
        return requests;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public List<Negative> getNegatives() {
        return negatives;
    }

    public List<PeopleBehavior> getPeoplebehaviors() {
        return peoplebehaviors;
    }

    public List<Summary> getSummary() {
        return summary;
    }

    public List<PhonePaySummary> getPhoneSummary() {
        return phoneSummary;
    }

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public Integer getHasSummary() {
        return entity.getHasSummary();
    }

    public Integer getHasPhoneSummary() {
        return entity.getHasPhoneSummary();
    }

    public Integer getHasNegatives() {
        return entity.getHasNegatives();
    }

    public Integer getHasPeoplebehaviors() {
        return entity.getHasPeoplebehavior();
    }

    public Integer getHasAnswers() {
        return entity.getHasAnswers();
    }

    public Integer getHasAntifraudFields() {
        return entity.getHasAntifrodFields();
    }

    public Integer getHasIdentityQuestions() {
        return entity.getHasIdentityQuestions();
    }

    public Integer getHasSystemSuspicions() {
        if (antifraudSystemSuspicions.size() > 0) {
            return antifraudSystemSuspicions.size();
        } else {
            return 0;
        }
    }

    public Integer getHasHunterSuspicions() {
        if (antifraudHunterSuspicions.size() > 0) {
            return antifraudHunterSuspicions.size();
        } else {
            return 0;
        }
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    @XmlElement
    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public Credit getAcceptedCredit() {
        return acceptedCredit;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<EventLog> getEventLogs() {
        return events;
    }

    public List<FiasAddress> getAddresses() {
        return addresses;
    }

    public Debt getCourtDebt() {
        return courtDebt;
    }

    public void setCourtDebt(Debt courtDebt) {
        this.courtDebt = courtDebt;
    }

    public List<PeopleIncapacity> getIncapacities() {
        return incapacities;
    }

    public List<AntifraudField> getAntifraudFields() {
        return antifraudFields;
    }

    public EventLog getLastEventLog() {
        if (events.size() == 0) {
            return null;
        } else {
            return events.get(events.size() - 1);
        }
    }

    public EventLog log(Date eventtime, EventType eventType, EventCode eventcode, String ipaddress,
                        String description) {
        EventLog log = new EventLog();
        log.setEventTime(eventtime);
        log.setEventType(eventType);
        log.setEventCode(eventcode);
        log.setIpaddress(ipaddress);
        log.setDescription(description);
        events.add(log);
        return log;
    }

    public boolean getRejected() {
        return (rejectReason != null && rejectReason.getEntity() != null && rejectReason.getId() != null);
    }

    public boolean isOfertaSigned() {
        return entity.isOfertaSigned();
    }

    public boolean getInDecisionProcess() {
        int accepted = 0;
        if (getAccepted() != null) {
            accepted = getAccepted();
        }
        if (getStatus().getId() == CreditStatus.IN_PROCESS) {
            return false;
        }
        return ((accepted == 0) && !getRejected());
    }

    public Integer getClientTimezone() {
        return entity.getClientTimezone();
    }

    public void setClientTimezone(Integer value) {
        entity.setClientTimezone(value);
    }

    public FMSRegion getRegion() {
        return region;
    }

    public void setRegion(FMSRegion region) {
        this.region = region;
    }

    public TimeZone getCRTimeZone() {
        if (getClientTimezone() == null) {
            return TimeZone.getTimeZone("GMT+00:00");
        } else {
            String ss = "GMT";
            if (getClientTimezone() > 0) {
                ss = ss + "+";
            } else {
                ss = ss + "-";
            }
            ss = ss + getClientTimezone().toString() + ":00";
            return TimeZone.getTimeZone(ss);
        }
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public List<DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public Products getProduct() {
        return product;
    }

    @XmlElement
    public void setProduct(Products product) {
        this.product = product;
    }

    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Reference getCreditPurpose() {
        return creditPurpose;
    }

    @XmlElement
    public void setCreditPurpose(Reference creditPurpose) {
        this.creditPurpose = creditPurpose;
    }

    public Reference getWay() {
        return way;
    }

    public void setWay(Reference way) {
        this.way = way;
    }

    public List<IdentityQuestion> getIdentityQuestions() {
        return identityQuestions;
    }

    public void setIdentityQuestions(List<IdentityQuestion> identityQuestions) {
        this.identityQuestions = identityQuestions;
    }

    public List<OfficialDocuments> getOfficialDocuments() {
        return officialDocuments;
    }

    public List<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }

    public List<AntifraudSuspicion> getAntifraudHunterSuspicions() {
        return antifraudHunterSuspicions;
    }

    public void setAntifraudHunterSuspicions(List<AntifraudSuspicion> antifraudHunterSuspicions) {
        this.antifraudHunterSuspicions = antifraudHunterSuspicions;
    }

    public List<AntifraudSuspicion> getAntifraudSystemSuspicions() {
        return antifraudSystemSuspicions;
    }

    public void setAntifraudSystemSuspicions(List<AntifraudSuspicion> antifraudSystemSuspicions) {
        this.antifraudSystemSuspicions = antifraudSystemSuspicions;
    }

    
    public Reference getDecisionWay() {
		return decisionWay;
	}

	public void setDecisionWay(Reference decisionWay) {
		this.decisionWay = decisionWay;
	}

	public Credit getClientCredit() {
		return clientCredit;
	}

	public enum Options {
        INIT_DOCUMENT,
        INIT_ADDRESS,
        INIT_CREDIT,
        INIT_CREDIT_HISTORY,
        INIT_EMPLOYMENT,
        INIT_LOGS,
        INIT_REQUESTS,
        INIT_SCORING,
        INIT_DEBT,
        INIT_VERIF,
        INIT_NEGATIVE,
        INIT_BEHAVIOR,
        INIT_BEHAVIORSOURCE,
        INIT_SUMMARY,
        INIT_PHONESUMMARY,
        INIT_ANSWERS,
        INIT_DEVICEINFO,
        INIT_INCAPACITY,
        INIT_ANTIFRAUD,
        INIT_ANTIFRAUD_SUSPICION,
        INIT_ANTIFRAUD_OCCASION,
        INIT_OFFICIAL_DOCUMENT,
        INIT_IDENTITY_QUESTION,
        INIT_CREDIT_OFFER
    }
}
