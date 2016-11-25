package ru.simplgroupp.ejb.arius;

import ru.simplgroupp.ejb.plugins.payment.AriusPayPluginConfig;

import java.util.Date;

/**
 * Created by aro on 26.12.2015.
 */
public class AriusParameters {
    private String url;
    private String merchantKey;
    private String endpoint;
    private String login;       // наш логин, используется в transfer,
    private String clientOrderId;     // наш уникальный кастомный id операции, например paymentId. Можно сделать его Random. используется в transfer,

    private String phone;      // для удобства, нужен часто бывает

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public void addParameters(StringBuilder params){
        params.append(AriusConstants.CLIENT_ORDERID).append(AriusConstants.EQ).append(clientOrderId);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.LOGIN).append(AriusConstants.EQ).append(login);
        params.append(AriusConstants.AND).append(AriusConstants.CONTROL).append(AriusConstants.EQ).append(createControl());//Mandatory
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected String createControl(){
        return "";
    }

    public void setConfigParameters(AriusPayPluginConfig config,String operation, String endpoint, Integer id){
        this.endpoint = endpoint;
        boolean sandbox = config.getSandbox();
        if(sandbox){
            this.url = config.getTestUrl();
            this.login = config.getTestLogin();
        }else{
            this.url = config.getWorkUrl();
            this.login = config.getWorkLogin();
        }
        this.url += operation + "/" + endpoint;

        this.merchantKey = config.getMerchantKey();

        Date date = new Date();
        this.clientOrderId = "M_"+operation.replace("/","")+"_"+date.getTime();
        if(id!=null){
            this.clientOrderId += AriusConstants.RQID_ + id;
        }

    }


}
