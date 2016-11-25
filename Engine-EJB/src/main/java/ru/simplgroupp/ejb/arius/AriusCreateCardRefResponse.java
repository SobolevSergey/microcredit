package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusCreateCardRefResponse extends AriusResponse {
    @Override
    protected void fillParameters() {
        super.fillParameters();
        parameters.put(AriusConstants.STATUS, null);
        parameters.put(AriusConstants.CARD_REF_ID, null);
    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }


    public String getCardRefId(){
        return parameters.get(AriusConstants.CARD_REF_ID);
    }

}
