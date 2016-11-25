package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusTransferCallbackResponse extends AriusResponse {
    @Override
    protected void fillParameters() {
        //type	Transaction type, sale reversal chargebac
        super.fillParameters();

        parameters.put(AriusConstants.STATUS, null);
        parameters.put(AriusConstants.MERCHANT_ORDER, null);
        parameters.put(AriusConstants.CLIENT_ORDERID, null);
        parameters.put(AriusConstants.ORDERID, null);
        parameters.put(AriusConstants.AMOUNT, null);
        parameters.put(AriusConstants.DESCRIPTOR, null);  //Payment descriptor of the gate through which the transaction has been processed.
        parameters.put(AriusConstants.NAME, null);  //Cardholder Name
        parameters.put(AriusConstants.EMAIL, null);   //Customer’s email
        parameters.put(AriusConstants.APPROVAL_CODE, null); //Authorization approval code, if any
        parameters.put(AriusConstants.LAST_FOUR_DIGITS, null);
        parameters.put(AriusConstants.BIN, null);
        parameters.put(AriusConstants.CARD_TYPE, null);   //Type of customer credit card (VISA, MASTERCARD, etc).
        parameters.put(AriusConstants.GATE_PARTIAL_REVERSAL, null);  //Processing gate support partial reversal (enabled or disabled).
        parameters.put(AriusConstants.GATE_PARTIAL_CAPTURE, null);
        parameters.put(AriusConstants.REASON_CODE, null);      //Reason code for chargebak or fraud operation.
        parameters.put(AriusConstants.PROCESSOR_RRN, null);     //Bank Receiver Registration Number.
        parameters.put(AriusConstants.COMMENT, null);  //Comment in case of Return transaction
        parameters.put(AriusConstants.RAPIDA_BALANCE, null); //Current balance for merchants registered in Rapida system (only if balance check active)


        parameters.put(AriusConstants.CONTROL, null); //Checksum is used to ensure that it is Arius (and not a fraudster) that initiates the callback for a particular Merchant.
                                       // This is SHA-1 checksum of the concatenation status + orderid + merchant_order + merchant_control.
                                       // The callback script MUST check this parameter by comparing it to SHA-1 checksum of the above concatenation.
                                       // See Callback authorization through control parameter for more details about generating control checksum.
        parameters.put(AriusConstants.MERCHANT_DATA, null);   //Reserved

    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }

    @Override
    protected String createControl() {
        StringBuilder sb = new StringBuilder(parameters.get(AriusConstants.STATUS));
        sb.append(parameters.get(AriusConstants.ORDERID));
        sb.append(parameters.get(AriusConstants.MERCHANT_ORDER));
        sb.append(getMerchantKey());
        return AriusUtils.generateSha1(sb.toString());
    }
}




