package ru.simplgroupp.persistence.antifraud;

import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * подозрение в мошенничестве
 */
public class AntifraudSuspicionEntity extends ExtendedBaseEntity implements Serializable {
    private static final long serialVersionUID = -3357232413440497541L;

    protected Integer txVersion = 0;

    /**
     * правило по которому определили мошенничество, от Хантера
     */
    private RefHunterRuleEntity rule;

    /**
     * правило по которому определили мошенничество, может и не быть в хантере
     */
    private RefAntifraudRulesEntity internalRule;

    /**
     * человек с которым сработало правило
     */
    private PeopleMainEntity secondPeople;

    /**
     * мошенничество ли
     */
    private boolean fraud;

    /**
     * комментарий
     */
    private String comment;

    /**
     * оценка
     */
    private Double score;

    /**
     * создано
     */
    private Date createdAt;


    public AntifraudSuspicionEntity() {
    }

    public RefHunterRuleEntity getRule() {
        return rule;
    }

    public void setRule(RefHunterRuleEntity rule) {
        this.rule = rule;
    }

    public PeopleMainEntity getSecondPeople() {
        return secondPeople;
    }

    public void setSecondPeople(PeopleMainEntity secondPeople) {
        this.secondPeople = secondPeople;
    }

    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public RefAntifraudRulesEntity getInternalRule() {
        return internalRule;
    }

    public void setInternalRule(RefAntifraudRulesEntity internalRule) {
        this.internalRule = internalRule;
    }
}
