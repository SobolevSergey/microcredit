package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusSaleFormResponse extends AriusResponse {
    @Override
    protected void fillParameters() {
        super.fillParameters();

        parameters.put(AriusConstants.STATUS, null);
        parameters.put(AriusConstants.REDIRECT_TIRE_URL, null);

    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }
    public String getRedirectUrl(){
        return parameters.get(AriusConstants.REDIRECT_TIRE_URL);
    }

}
