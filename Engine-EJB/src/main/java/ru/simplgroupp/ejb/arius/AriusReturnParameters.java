package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 26.12.2015.
 */
public class AriusReturnParameters extends AriusParameters {
    private String orderId;
    private String comment;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    protected String createControl(){
        StringBuilder sb = new StringBuilder(getLogin());
        sb.append(getClientOrderId());
        sb.append(orderId);
        sb.append(getMerchantKey());
        return AriusUtils.generateSha1(sb.toString());
    }



    public void addParameters(StringBuilder params){
        super.addParameters(params);
        //if(cardNumber!=null) params.append(AriusConstants.AND).append(AriusConstants.destination_card_no).append(AriusConstants.EQ).append(cardNumber);//Mandatory
        if(orderId!=null) params.append(AriusConstants.AND).append(AriusConstants.ORDERID).append(AriusConstants.EQ).append(orderId);//Mandatory
        if(comment!=null) params.append(AriusConstants.AND).append(AriusConstants.COMMENT).append(AriusConstants.EQ).append(comment);//Mandatory

    }


}
