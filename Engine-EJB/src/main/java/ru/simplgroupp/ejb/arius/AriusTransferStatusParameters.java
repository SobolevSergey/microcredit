package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 26.12.2015.
 */
public class AriusTransferStatusParameters extends AriusParameters {
    private String sn;
    private String paynetOrderId;  // id нашего платежа в системе arius


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPaynetOrderId() {
        return paynetOrderId;
    }

    public void setPaynetOrderId(String paynetOrderId) {
        this.paynetOrderId = paynetOrderId;
    }

    /**
     * Checksum used to ensure that it is Arius (and not a fraudster) that initiates the callback for a particular Merchant.
     * This is SHA-1 checksum of the concatenation login + client-order-id + paynet-order-id + merchant-control.
     * See Order status API call authorization through control parameter for more details about generating control checksum.
     * @return String
     */
    protected String createControl(){
        StringBuilder sb = new StringBuilder(getLogin());
        sb.append(getClientOrderId());
        sb.append(paynetOrderId);
        sb.append(getMerchantKey());
        return AriusUtils.generateSha1(sb.toString());
    }



    public void addParameters(StringBuilder params){
        super.addParameters(params);
        params.append(AriusConstants.AND).append(AriusConstants.ORDERID).append(AriusConstants.EQ).append(paynetOrderId);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.BY_REQUEST_SN).append(AriusConstants.EQ).append(sn);//Mandatory
    }



}
