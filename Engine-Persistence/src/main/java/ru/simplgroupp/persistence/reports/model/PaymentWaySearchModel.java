package ru.simplgroupp.persistence.reports.model;


public class PaymentWaySearchModel {
    public PaymentWaySearchModel(int codeinteger, String name) {
        this.codeinteger = codeinteger;
        this.name = name;
    }

    public int getCodeinteger() {
        return codeinteger;
    }

    public void setCodeinteger(int codeinteger) {
        this.codeinteger = codeinteger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int codeinteger;
    private String name;
}

