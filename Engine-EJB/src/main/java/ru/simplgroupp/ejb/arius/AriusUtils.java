package ru.simplgroupp.ejb.arius;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;

/**
 * Created by aro on 21.01.2016.
 */
public class AriusUtils {
    public static String generateSha1(String s){
        return new String(DigestUtils.shaHex(s.getBytes()));
    }

    public static  String convertToKopeyki(String amount){
        BigDecimal bg = new BigDecimal(amount);
        BigDecimal res = bg.multiply(new BigDecimal(100));
        return String.valueOf(res.intValue());
    }

    public static String extractRQID(String s){
        if(s == null){
            return null;
        }
        int indexStart = s.indexOf(AriusConstants.RQID_);
        if(indexStart>-1){
            return s.substring(indexStart+AriusConstants.RQID_.length());
        }

        return null;
    }

}
