package ru.simplgroupp.persistence.reports.model;

/**
 *
 */
public class ChannelSearchModel {
    public ChannelSearchModel(String name,String paramvalue) {
        this.paramvalue = paramvalue;
        this.name = name;
    }
    private String name;
    private String paramvalue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamvalue() {
        return paramvalue;
    }

    public void setParamvalue(String paramvalue) {
        this.paramvalue = paramvalue;
    }
}
