package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusRedirectResponse extends AriusResponse {


    @Override
    protected void fillParameters() {
        super.fillParameters();
        parameters.put(AriusConstants.LAST_FOUR_DIGITS, null);
        parameters.put(AriusConstants.MERCHANT_ORDER, null);
        parameters.put(AriusConstants.CLIENT_ORDERID, null);
        parameters.put(AriusConstants.CONTROL, null);
        parameters.put(AriusConstants.PHONE, null);
        parameters.put(AriusConstants.STATUS, null);
        parameters.put(AriusConstants.DESCRIPTOR, null);
        parameters.put(AriusConstants.GATE_PARTIAL_REVERSAL, null);
        parameters.put(AriusConstants.CARD_HOLDER_NAME, null);
        parameters.put(AriusConstants.DEST_BIN, null);
        parameters.put(AriusConstants.PROCESSOR_TX_ID, null);
        parameters.put(AriusConstants.CARD_TYPE, null);
        parameters.put(AriusConstants.BIN, null);
        parameters.put(AriusConstants.ORDERID, null);



    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }
    public String getOrderId(){
        return parameters.get(AriusConstants.ORDERID);
    }

}

