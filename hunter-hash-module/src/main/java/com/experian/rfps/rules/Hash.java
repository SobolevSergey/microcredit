package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleHash;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Map;
import org.apache.log4j.Logger;
import ru.CryptoPro.JCP.JCP;

public class Hash extends AbstractRule implements IRuleHash {
   private static final Logger log = Logger.getLogger(Hash.class);
   private MessageDigest digest;
   private static boolean initialize = false;

   public boolean prepare(Map parameters) {
      if(!initialize) {
         Security.addProvider(new JCP());
         initialize = true;
      }

      try {
         this.digest = MessageDigest.getInstance("GOST3411");
         this.isPrepared = true;
      } catch (NoSuchAlgorithmException var3) {
         log.error(var3.getMessage());
      }

      return this.isPrepared;
   }

   public String encode(String in, Map parameters) {
      if(this.isPrepared && in != null && in.length() > 0) {
         String rez = "";

         try {
            byte[] ex = in.getBytes("CP1251");
            this.digest.update(ex);
            byte[] ar = this.digest.digest();
            rez = toHexString(ar);
         } catch (Exception var6) {
            log.error(var6.getMessage());
            var6.printStackTrace();
         }

         return rez;
      } else {
         return in;
      }
   }

   public static String toHexString(byte[] array) {
      char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer ss = new StringBuffer(array.length * 3);

      for(int i = 0; i < array.length; ++i) {
         ss.append(hex[array[i] >>> 4 & 15]);
         ss.append(hex[array[i] & 15]);
      }

      return ss.toString();
   }
}
