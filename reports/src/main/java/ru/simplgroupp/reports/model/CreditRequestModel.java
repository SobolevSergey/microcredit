package ru.simplgroupp.reports.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.BaseTransfer;
import ru.simplgroupp.transfer.CreditRequest;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * Created by victor on 16.11.15.
 */
public class CreditRequestModel {
    private static final Logger logger = LoggerFactory.getLogger(CreditRequestModel.class);
    public static Constructor getWrapConstructor() {
        return wrapConstructor;
    }
    protected static Constructor wrapConstructor;
    static {
        try {
            wrapConstructor = CreditRequestModel.class.getConstructor(CreditRequest.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public CreditRequestModel(CreditRequest creditRequest) {
        number = creditRequest.getUniqueNomer();
        status = creditRequest.getStatus().getName();
        datecontest = creditRequest.getDateContest();
        if (creditRequest.getPeopleMain() != null) {
            client = creditRequest.getPeopleMain().getPeoplePersonalActive() != null ? creditRequest.getPeopleMain().getPeoplePersonalActive().getFullName() : "";
        }
        else {
            client = "";
        }
        stake = creditRequest.getStake()*100;
        sum = creditRequest.getCreditSum();
        term = creditRequest.getCreditDays();
        rejectreason = "";
        if (creditRequest.getRejectReason() != null) {
            if ((creditRequest.getRejectReason().getNameFull() != null) && (!creditRequest.getRejectReason().getNameFull().isEmpty())) {
                rejectreason = creditRequest.getRejectReason().getNameFull();
            }
            else {
                rejectreason = creditRequest.getRejectReason().getName();
            }

        }
        comment = "";
        if (creditRequest.getComment() != null) {
            comment = creditRequest.getComment();
        }

    }

    private String number;
    private String status;
    private Date datecontest;
    private String client;
    private Double stake;
    private Double sum;
    private int term;
    private String rejectreason;
    private String comment;

    public String getRejectreason() {
        return rejectreason;
    }

    public void setRejectreason(String rejectreason) {
        this.rejectreason = rejectreason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDatecontest() {
        return datecontest;
    }

    public void setDatecontest(Date datecontest) {
        this.datecontest = datecontest;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Double getStake() {
        return stake;
    }

    public void setStake(Double stake) {
        this.stake = stake;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
