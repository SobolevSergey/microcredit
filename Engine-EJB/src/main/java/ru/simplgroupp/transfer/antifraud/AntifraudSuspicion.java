package ru.simplgroupp.transfer.antifraud;

import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class AntifraudSuspicion extends BaseTransfer<AntifraudSuspicionEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 3180894000424079359L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = AntifraudSuspicion.class.getConstructor(AntifraudSuspicionEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private CreditRequest creditRequestId;

    private PeopleMain peopleMainId;

    private Partner partnersId;

    private PeopleMain secondPeople;

    private RefHunterRule rule;

    private RefAntifraudRules internalRule;


    public AntifraudSuspicion() {
        super();
        entity = new AntifraudSuspicionEntity();
    }

    public AntifraudSuspicion(AntifraudSuspicionEntity value) {
        super(value);

        creditRequestId = new CreditRequest(entity.getCreditRequestId());
        peopleMainId = new PeopleMain(entity.getPeopleMainId());
        partnersId = new Partner(entity.getPartnersId());

        if (entity.getRule() != null) {
            rule = new RefHunterRule(entity.getRule());
        }

        if (entity.getSecondPeople() != null) {
            secondPeople = new PeopleMain(entity.getSecondPeople());
        }

        if (entity.getInternalRule() != null) {
            internalRule = new RefAntifraudRules(entity.getInternalRule());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        // так как инициализация может проводится не через явный вызов из dao, а по средством запроса из других классов
        // то нужно предотвратить stack overflow поэтому инициализироваться будут только явно указанные параметры, а не весь сет

        //персональные данные людей
        if (options != null && options.contains(PeopleMain.Options.INIT_PEOPLE_PERSONAL)) {
            if (secondPeople != null) {
                secondPeople.init(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
            }
            peopleMainId.init(Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL));
        }

        // заявки людей
        if (options != null && options.contains(PeopleMain.Options.INIT_CREDIT_REQUEST)) {
            if (secondPeople != null) {
                secondPeople.init(Utils.setOf(PeopleMain.Options.INIT_CREDIT_REQUEST));
            }
            peopleMainId.init(Utils.setOf(PeopleMain.Options.INIT_CREDIT_REQUEST));
        }
    }

    @Override
    public Object getId() {
        return entity.getId();
    }

    public PeopleMain getSecondPeople() {
        return secondPeople;
    }

    public void setSecondPeople(PeopleMain secondPeople) {
        this.secondPeople = secondPeople;
    }

    public boolean isFraud() {
        return entity.isFraud();
    }

    public void setFraud(boolean fraud) {
        entity.setFraud(fraud);
    }

    public String getComment() {
        return entity.getComment();
    }

    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public RefHunterRule getRule() {
        return rule;
    }

    public void setRule(RefHunterRule rule) {
        this.rule = rule;
    }

    public Double getScore() {
        return entity.getScore();
    }

    public void setScore(Double score) {
        entity.setScore(score);
    }

    public Date getCreatedAt() {
        return entity.getCreatedAt();
    }

    public void setCreatedAt(Date createdAt) {
        entity.setCreatedAt(createdAt);
    }

    public CreditRequest getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequest creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public PeopleMain getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMain peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public Partner getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(Partner partnersId) {
        this.partnersId = partnersId;
    }

    public RefAntifraudRules getInternalRule() {
        return internalRule;
    }

    public void setInternalRule(RefAntifraudRules internalRule) {
        this.internalRule = internalRule;
    }


    public enum Options {
    }
}

