package ru.simplgroupp.ejb.arius;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aro on 29.12.2015.
 * Класс родитель для всех ответов от Ариуса
 * Парсит ответ от Ариуса и складывает его во внутренний Map
 */
public class AriusResponse {


    protected Map<String,String> parameters = new HashMap<String,String>();

    public Map<String, String> getParameters() {
        return parameters;
    }

    protected void fillParameters(){
        parameters.put(AriusConstants.TYPE, null);
        parameters.put(AriusConstants.ERROR_MESSAGE, null);
        parameters.put(AriusConstants.ERROR_CODE, null);
        parameters.put(AriusConstants.SERIAL_NUMBER, null);
        parameters.put(AriusConstants.MERCHANT_ORDER_ID, null);
        parameters.put(AriusConstants.PAYNET_ORDER_ID, null);
    }


    /**
     * Парсит длиннющую строка респонса от Ариуса, выдергивая оттуда параметры и складывая их в Map
     * @param responseString
     */
    public void parse(String responseString){
        this.fillParameters();

        String rs = "&"+responseString+"&";
        Set<String> usedParameters = parameters.keySet();
        for(String up: usedParameters){
            String findString = "&"+up+"=";
            int index = rs.indexOf(findString);
            if(index>=0){
                int startPos = index+findString.length();
                int endPos   = rs.indexOf("&",startPos);
                String val = rs.substring(startPos,endPos);
                if(val!=null){
                    parameters.put(up,val.trim().replace("\n",""));
                }
            }
        }
    }

    /**
     * Перекладывает параметры из HttpServletRequest.getParameterMap() в наш Map
     * @param responseMap
     */
    public void parse(Map<String,String[]> responseMap){
        this.fillParameters();
        if(responseMap!=null){
            for(String key: responseMap.keySet()){
                String[] val = responseMap.get(key);
                if(val!=null && val.length>0){
                    parameters.put(key,val[0]);
                }

            }
        }
    }



    public String getVal(String key){
        return parameters.get(key);
    }

    public void setVal(String key,String val){
        if(key!=null){
            parameters.put(key,val);
        }
    }

    public AriusResponseTypeEnum getType(){
        return AriusResponseTypeEnum.getInstanceByRealName(parameters.get(AriusConstants.TYPE));
    }

    protected String createControl(){
        return "";
    }

    private String merchantKey;

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }
}
