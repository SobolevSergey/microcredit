package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public class AriusTransferStatusResponse extends AriusResponse {


    @Override
    protected void fillParameters() {
        super.fillParameters();

        parameters.put(AriusConstants.STATUS, null);
        parameters.put(AriusConstants.AMOUNT, null);
        parameters.put(AriusConstants.PHONE, null);
        parameters.put(AriusConstants.HTML, null);
        parameters.put(AriusConstants.GATE_PARTIAL_REVERSAL, null);
        parameters.put(AriusConstants.GATE_PARTIAL_CAPTURE, null);
        parameters.put(AriusConstants.TRANSACTION_TYPE, null);
        parameters.put(AriusConstants.PROCESSOR_RRN, null);
        parameters.put(AriusConstants.PROCESSOR_TX_ID, null);
        parameters.put(AriusConstants.RECEIPT_ID, null);
        parameters.put(AriusConstants.NAME, null);
        parameters.put(AriusConstants.CARDHOLDER_NAME, null);
        parameters.put(AriusConstants.CARD_EXP_MONTH, null);
        parameters.put(AriusConstants.CARD_EXP_YEAR, null);
        parameters.put(AriusConstants.CARD_HASH_ID, null);
        parameters.put(AriusConstants.DESTINATION_HASH_ID, null);
        parameters.put(AriusConstants.BANK_NAME, null);
        parameters.put(AriusConstants.LAST_FOUR_DIGITS, null);
        parameters.put(AriusConstants.BIN, null);
        parameters.put(AriusConstants.CARD_TYPE, null);
        parameters.put(AriusConstants.DEST_BANK_NAME, null);
        parameters.put(AriusConstants.DEST_LAST_FOUR_DIGITS, null);
        parameters.put(AriusConstants.DEST_BIN, null);
        parameters.put(AriusConstants.DEST_CARD_TYPE, null);
        parameters.put(AriusConstants.TERMINAL_ID, null);
        parameters.put(AriusConstants.PAYNET_PROCESSING_DATE, null);
        parameters.put(AriusConstants.APPROVAL_CODE, null);
        parameters.put(AriusConstants.ORDER_STAGE, null);
        parameters.put(AriusConstants.LOYALTY_BALANCE, null);
        parameters.put(AriusConstants.LOYALTY_MESSAGE, null);
        parameters.put(AriusConstants.LOYALTY_BONUS, null);
        parameters.put(AriusConstants.LOYALTY_PROGRAM, null);
        parameters.put(AriusConstants.DESCRIPTOR, null);
        parameters.put(AriusConstants.BY_REQUEST_SN, null);
        parameters.put(AriusConstants.VERIFIED_3D_STATUS, null);
        parameters.put(AriusConstants.VERIFIED_RSC_STATUS, null);

    }

    public AriusStatusEnum getStatus(){
        return AriusStatusEnum.valueOf(parameters.get(AriusConstants.STATUS));
    }

}

