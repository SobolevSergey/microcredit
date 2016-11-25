package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.CreditOfferEntity;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * 28.09.2015
 * 14:54
 */

public class CreditOffer extends BaseTransfer<CreditOfferEntity> {
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = CreditOffer.class.getConstructor(CreditOfferEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    /**
     * человек
     */
    private PeopleMain peopleMainId;
    /**
     * заявка
     */
    private CreditRequest creditRequestId;
    /**
     * пользователь
     */
    private Users userId;
    /**
     * как было предложено (автоматически или вручную)
     */
    private Reference offerWayId;

    public CreditOffer() {
    }

    public CreditOffer(CreditOfferEntity entity) {
        super(entity);
        this.peopleMainId = new PeopleMain(entity.getPeopleMainId());
        this.creditRequestId = new CreditRequest(entity.getCreditRequestId());
        this.userId = new Users(entity.getUserId());
        this.offerWayId = new Reference(entity.getOfferWayId());
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public PeopleMain getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMain peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public CreditRequest getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequest creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Reference getOfferWayId() {
        return offerWayId;
    }

    public void setOfferWayId(Reference offerWayId) {
        this.offerWayId = offerWayId;
    }

    public Integer getCreditDays() {
        return entity.getCreditDays();
    }

    public void setCreditDays(Integer creditDays) {
        entity.setCreditDays(creditDays);
    }

    public Double getCreditSum() {
        return entity.getCreditSum();
    }

    public void setCreditSum(Double creditSum) {
        entity.setCreditSum(creditSum);
    }

    public Double getCreditStake() {
        return entity.getCreditStake();
    }

    public void setCreditStake(Double creditStake) {
        entity.setCreditStake(creditStake);
    }

    public Date getOfferTime() {
        return entity.getOfferTime();
    }

    public void setOfferTime(Date offerTime) {
        entity.setOfferTime(offerTime);
    }

    public String getComment() {
        return entity.getComment();
    }

    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public Integer getAccepted() {
        return entity.getAccepted();
    }

    public void setAccepted(Integer accepted) {
        entity.setAccepted(accepted);
    }
}