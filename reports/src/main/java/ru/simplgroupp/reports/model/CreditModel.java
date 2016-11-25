package ru.simplgroupp.reports.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 *
 */
public class CreditModel {
    private static final Logger logger = LoggerFactory.getLogger(CreditModel.class);
    public static Constructor getWrapConstructor() {
        return wrapConstructor;
    }
    protected static Constructor wrapConstructor;
    static {
        try {
            wrapConstructor = CreditModel.class.getConstructor(Credit.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public CreditModel() {
    }

    public CreditModel(Credit credit) {
        number = credit.getIdCredit();
        status = credit.getCreditStatus().getName();
        creditdatabeg = credit.getCreditDataBeg();
        creditdataend = credit.getCreditDataEnd();
        creditpercent = credit.getCreditPercent()*100;
        creditsum = credit.getCreditSum();
        client = credit.getCreditRequest().getPeopleMain().getPeoplePersonalActive().getFullName();

    }
    private String number;
    private String status;
    private Date creditdatabeg;
    private Date creditdataend;
    private double creditpercent;
    private double creditsum;
    private String client;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreditdatabeg() {
        return creditdatabeg;
    }

    public void setCreditdatabeg(Date creditdatabeg) {
        this.creditdatabeg = creditdatabeg;
    }

    public Date getCreditdataend() {
        return creditdataend;
    }

    public void setCreditdataend(Date creditdataend) {
        this.creditdataend = creditdataend;
    }

    public double getCreditpercent() {
        return creditpercent;
    }

    public void setCreditpercent(double creditpercent) {
        this.creditpercent = creditpercent;
    }

    public double getCreditsum() {
        return creditsum;
    }

    public void setCreditsum(double creditsum) {
        this.creditsum = creditsum;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
