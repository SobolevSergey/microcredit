package ru.simplgroupp.contact.protocol.v2.response;

/**
 * Created by aro on 15.09.2014.
 */
public class ContactResponse {
    private String outXML;
    private int returnResult;

    public String getOutXML() {
        return outXML;
    }

    public void setOutXML(String outXML) {
        this.outXML = outXML;
    }

    public int getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(int returnResult) {
        this.returnResult = returnResult;
    }
}
