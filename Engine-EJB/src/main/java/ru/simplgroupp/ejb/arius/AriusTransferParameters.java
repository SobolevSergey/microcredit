package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 26.12.2015.
 */
public class AriusTransferParameters extends AriusParameters {
    private String cardRefId;  // используется в transfer,
    private String order_desc;  //используется в transfer,
    private String amount;      // используется в transfer,
    private String currency;    // используется в transfer,
    private String ipaddress;   // используется в transfer,


    private String firstName;
    private String lastName;
    private String purpose;
    private String isresident;
    private String redirectUrl;
    private String serverCallbackUrl;
    private String email;
    private String country;
    private String zipCode;
    private String city;
    private String address1;


    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }


    public String getIsresident() {
        return isresident;
    }

    public void setIsresident(String isresident) {
        this.isresident = isresident;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getServerCallbackUrl() {
        return serverCallbackUrl;
    }

    public void setServerCallbackUrl(String serverCallbackUrl) {
        this.serverCallbackUrl = serverCallbackUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCardRefId() {
        return cardRefId;
    }

    public void setCardRefId(String cardRefId) {
        this.cardRefId = cardRefId;
    }




    /**
     * 	Checksum generated by SHA-1. This is SHA-1 checksum of the concatenation login + client_orderid + source-card-ref-id
     * 	(if present) + destination-card-ref-id (if present) + amount_in_cents + currency + merchant_control.
     * @return
     */
    protected String createControl(){
        StringBuilder sb = new StringBuilder(getLogin());
        sb.append(getClientOrderId());
        sb.append(getCardRefId());
        sb.append(AriusUtils.convertToKopeyki(amount));
        sb.append(currency);
        sb.append(getMerchantKey());
        return AriusUtils.generateSha1(sb.toString());
    }



    public void addParameters(StringBuilder params){
        super.addParameters(params);
        if(cardRefId!=null) params.append(AriusConstants.AND).append(AriusConstants.DESTINATION_CARD_REF_ID).append(AriusConstants.EQ).append(cardRefId);//Mandatory
        if(order_desc!=null) params.append(AriusConstants.AND).append(AriusConstants.ORDER_DESC).append(AriusConstants.EQ).append(order_desc);//Mandatory
        if(amount!=null) params.append(AriusConstants.AND).append(AriusConstants.AMOUNT).append(AriusConstants.EQ).append(amount);//Mandatory
        if(currency!=null) params.append(AriusConstants.AND).append(AriusConstants.CURRENCY).append(AriusConstants.EQ).append(currency);//Mandatory
        if(ipaddress!=null) params.append(AriusConstants.AND).append(AriusConstants.IPADDRESS).append(AriusConstants.EQ).append(ipaddress);//Mandatory
        if(firstName!=null) params.append(AriusConstants.AND).append(AriusConstants.RECEIVER_FIRST_NAME).append(AriusConstants.EQ).append(firstName);
        if(lastName!=null) params.append(AriusConstants.AND).append(AriusConstants.RECEIVER_LAST_NAME).append(AriusConstants.EQ).append(lastName);
        if(purpose!=null) params.append(AriusConstants.AND).append(AriusConstants.PURPOSE).append(AriusConstants.EQ).append(purpose);
        if(isresident!=null) params.append(AriusConstants.AND).append(AriusConstants.RECEIVER_RESIDENT).append(AriusConstants.EQ).append(isresident);

        //if(redirectUrl!=null) params.append(AriusConstants.AND).append(AriusConstants.REDIRECT_URL).append(AriusConstants.EQ).append(redirectUrl);
        //if(serverCallbackUrl!=null) params.append(AriusConstants.AND).append(AriusConstants.SERVER_CALLBACK_URL).append(AriusConstants.EQ).append(serverCallbackUrl);
        //if(email!=null) params.append(AriusConstants.AND).append(AriusConstants.EMAIL).append(AriusConstants.EQ).append(email);
        //if(country!=null) params.append(AriusConstants.AND).append(AriusConstants.COUNTRY).append(AriusConstants.EQ).append(country);
        //if(zipCode!=null) params.append(AriusConstants.AND).append(AriusConstants.ZIP_CODE).append(AriusConstants.EQ).append(zipCode);
        //if(city!=null) params.append(AriusConstants.AND).append(AriusConstants.CITY).append(AriusConstants.EQ).append(city);
        //if(address1!=null) params.append(AriusConstants.AND).append(AriusConstants.ADDRESS1).append(AriusConstants.EQ).append(address1);

    }


}
