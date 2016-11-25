package ru.simplgroupp.ejb.arius;

/**
 * Created by aro on 29.12.2015.
 */
public enum AriusStatusEnum {
    approved, //	Transaction is approved, final status
    declined, //  	Transaction is declined, final status
    error,    //    Transaction is declined but something went wrong, please inform your account manager, final status
    filtered,  //   Transaction is declined by fraud internal or external control systems, final status
    processing, //  Transaction is being processed, you should continue polling, non final status
    unknown     //  The status of transaction is unknown, please inform your account manager, non final status
}

