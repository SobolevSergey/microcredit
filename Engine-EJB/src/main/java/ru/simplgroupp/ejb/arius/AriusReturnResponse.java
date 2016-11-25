package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusReturnResponse extends AriusResponse {
    @Override
    protected void fillParameters() {
        super.fillParameters();
        parameters.put(AriusConstants.STATUS, null);
    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }

}
