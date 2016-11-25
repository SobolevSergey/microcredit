package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 26.12.2015.
 */
public enum AriusResponseTypeEnum {
    async_response("async-response"),     //transfer success
    validation_error("validation-error"), //transfer
    error("error"),                       //transfer
    status_response("status-response");   //transfer status

    private AriusResponseTypeEnum(String realName) {
        this.realName = realName;
    }

    private String realName;

    public String getRealName() {
        return realName;
    }

    public static AriusResponseTypeEnum getInstanceByRealName(String realName){
        if(realName==null){
            return null;
        }
        for(AriusResponseTypeEnum en :AriusResponseTypeEnum.values()){
            if(realName.equals(en.getRealName())){
                return en;
            }
        }
        return null;
    }

}
