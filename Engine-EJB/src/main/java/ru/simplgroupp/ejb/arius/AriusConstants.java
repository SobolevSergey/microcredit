package ru.simplgroupp.ejb.arius;

/**
 * Туча констант ариуса, в основном названия параметров для запроса к их сервисам
 * Created by aro on 18.12.2015.
 */
public class AriusConstants {
    public static final String ARIUS_OPERATION_PREAUTH_FORM="preauth-form";
    public static final String ARIUS_OPERATION_TRANSFER="transfer";
    public static final String ARIUS_OPERATION_RETURN="return";
    public static final String ARIUS_OPERATION_CREATE_CARD_REF="create-card-ref";
    public static final String ARIUS_OPERATION_TRANSFER_BY_REF="transfer-by-ref";
    public static final String ARIUS_OPERATION_STATUS="status";
    public static final String ARIUS_OPERATION_SALE_FORM="sale-form";


    public static final String EQ = "=";
    public static final String AND = "&";

    public static final String ORDER_DESC = "order_desc";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String SSN = "ssn";
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS1 = "address1";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP_CODE = "zip_code";
    public static final String COUNTRY = "country";
    public static final String CELL_PHONE = "cell_phone";
    public static final String CURRENCY = "currency";
    public static final String IPADDRESS = "ipaddress";
    public static final String SITE_URL = "site_url";
    public static final String CREDIT_CARD_NUMBER = "credit_card_number";
    public static final String CARD_PRINTED_NAME = "card_printed_name";
    public static final String EXPIRE_MONTH = "expire_month";
    public static final String EXPIRE_YEAR = "expire_year";
    public static final String CVV2 = "cvv2";
    public static final String PURPOSE = "purpose";
    public static final String SERVER_CALLBACK_URL = "server_callback_url";
    public static final String MERCHANT_DATA = "merchant_data";

    public static final String LOGIN = "login";

    public static final String ENDPOINT3DS = "848";
    public static final String ENDPOINTCARDREG = "849";
    public static final String ENDPOINTDEPOSIT2CARD = "852";
    public static final String ENDPOINTRECURRENT = "851";

    public static final String DESTINATION_CARD_NO = "destination-card-no";

    public static final String RECEIVER_FIRST_NAME = "receiver_first_name";
    public static final String RECEIVER_MIDDLE_NAME = "receiver_middle_name";

    public static final String RECEIVER_LAST_NAME = "receiver_last_name";
    public static final String RECEIVER_RESIDENT = "receiver_resident";



    public static final String TYPE = "type";                           //The type of response.
    public static final String ERROR_MESSAGE = "error-message";         //this parameter contains the reason for decline
    public static final String ERROR_CODE = "error-code";               //	The error code is case of failed status
    public static final String SERIAL_NUMBER = "serial-number";         //Unique number assigned by Arius server to particular request from the Merchant.
    public static final String MERCHANT_ORDER_ID = "merchant-order-id"; //	Merchant order id
    public static final String PAYNET_ORDER_ID = "paynet-order-id";      // Order id assigned to the order by Arius

    public static final String STATUS = "status";//status of order
    public static final String AMOUNT = "amount";// Amount of the initial transaction.
    public static final String PHONE = "phone";//customer phone
    public static final String HTML = "html";    //HTML code of 3D authorization form
    public static final String GATE_PARTIAL_REVERSAL = "gate-partial-reversal";  //Processing gate support partial reversal (enabled or disabled).
    public static final String GATE_PARTIAL_CAPTURE = "gate-partial-capture";  //Processing gate support partial capture (enabled or disabled).
    public static final String TRANSACTION_TYPE = "transaction-type";         //Transaction type (sale, reversal, capture, preauth).
    public static final String PROCESSOR_RRN = "processor-rrn";              //Bank Receiver Registration Number.
    public static final String PROCESSOR_TX_ID = "processor-tx-id";     //	Acquirer transaction identifier.
    public static final String RECEIPT_ID = "receipt-id";           //	Electronical link to receipt https://gate.ariuspay.ru/paynet/view-receipt/ENDPOINTID/receipt-id/
    public static final String NAME = "name";                            //Cardholder name.
    public static final String CARDHOLDER_NAME = "cardholder-name";       //Cardholder name.
    public static final String CARD_HOLDER_NAME = "card-holder-name";       //Cardholder name.
    public static final String CARD_EXP_MONTH  = "card-exp-month";     //	Card expiration month.
    public static final String CARD_EXP_YEAR = "card-exp-year";        //	Card expiration year.
    public static final String CARD_HASH_ID = "card-hash-id";           //Unique card identifier to use for loyalty programs or fraud checks.
    public static final String DESTINATION_HASH_ID = "destination-hash-id";   //Unique card identifier to use for loyalty programs or fraud checks.
    public static final String EMAIL = "email";                              //Customer e-mail.
    public static final String BANK_NAME = "bank-name";                 //Bank name by customer card BIN.
    public static final String LAST_FOUR_DIGITS = "last-four-digits";    //Last four digits of customer credit card number.
    public static final String BIN = "bin";                            //Bank BIN of customer credit card number.
    public static final String CARD_TYPE = "card-type";                //Type of customer credit card (VISA, MASTERCARD, etc).
    public static final String DEST_BANK_NAME = "dest-bank-name";          //Destination bank name by customer card BIN.
    public static final String DEST_LAST_FOUR_DIGITS = "dest-last-four-digits";    //Destination last four digits of customer credit card number.
    public static final String DEST_BIN = "dest-bin";                             // Destination bank BIN of customer credit card number.
    public static final String DEST_CARD_TYPE = "dest-card-type";                 //Type of destination customer credit card (VISA, MASTERCARD, etc).
    public static final String TERMINAL_ID = "terminal-id";                         //  Acquirer terminal identifier to show in receipt.
    public static final String PAYNET_PROCESSING_DATE = "paynet-processing-date";     //Acquirer transaction processing date.
    public static final String APPROVAL_CODE = "approval-code";                      //  Bank approval code.
    public static final String ORDER_STAGE = "order-stage";                         //The current stage of the transaction processing. See Order Stage for details (а там туча, сотня разных stage)
    public static final String LOYALTY_BALANCE = "loyalty-balance";                // The current bonuses balance of the loyalty program for current operation. if available
    public static final String LOYALTY_MESSAGE = "loyalty-message";                // The message from the loyalty program. if available
    public static final String LOYALTY_BONUS = "loyalty-bonus";                    // The bonus value of the loyalty program for current operation. if available
    public static final String LOYALTY_PROGRAM = "loyalty-program";                // The name of the loyalty program for current operation. if available
    public static final String DESCRIPTOR = "descriptor";                          // Bank identifier of the payment recipient.
    public static final String BY_REQUEST_SN = "by-request-sn";                    //Serial number from status request, if exists in request. Warning parameter amount always shows initial transaction amount, even if status is requested by-request-sn.
    public static final String VERIFIED_3D_STATUS = "verified-3d-status";          // See:ref:3d_secure_status_list for details
    public static final String VERIFIED_RSC_STATUS = "verified-rsc-status";        //  See Random Sum Check Status List for details
    public static final String REDIRECT_URL = "redirect_url";                     //  	The URL to the page where the Merchant should redirect the client’s browser. Merchant should send HTTP 302 redirect, see General Payment Form Process Flow
    public static final String REDIRECT_TIRE_URL = "redirect-url";                     //  	The URL to the page where the Merchant should redirect the client’s browser. Merchant should send HTTP 302 redirect, see General Payment Form Process Flow

    public static final String MERCHANT_ORDER = "merchant_order"; //	Merchant order identifier, client_orderid
    public static final String CLIENT_ORDERID = "client_orderid"; //	Merchant order identifier
    public static final String ORDERID = "orderid"; //	Arius transaction id
    public static final String REASON_CODE = "reason-code"; //	Reason code for chargebak or fraud operation.
    public static final String COMMENT = "comment"; //	Comment in case of Return transaction
    public static final String RAPIDA_BALANCE = "rapida-balance"; //	Current balance for merchants registered in Rapida system (only if balance check active)
    public static final String CONTROL = "control"; //Checksum is used to ensure that it is Arius (and not a fraudster) that initiates the callback for a particular Merchant.
    // This is SHA-1 checksum of the concatenation status + orderid + merchant_order + merchant_control.
    // The callback script MUST check this parameter by comparing it to SHA-1 checksum of the above concatenation.
    // See Callback authorization through control parameter for more details about generating control checksum.
    public static final String MERCHANTDATA = "merchantdata"; //	Reserved


    public static final String CARD_REF_ID = "card-ref-id";        //	Card referense ID to used in subsequent recurrent payments

    public static final String DESTINATION_CARD_REF_ID = "destination-card-ref-id";        //	Destination card referense ID to used in subsequent recurrent payments

    public static final String RQID_ = "rqid_";



}
