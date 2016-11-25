package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleHash;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Map;
import org.apache.log4j.Logger;
import ru.CryptoPro.JCP.JCP;

public class HashExcludeByParent extends AbstractRule implements IRuleHash {
   private static final Logger log = Logger.getLogger(HashExcludeByParent.class);
   private MessageDigest digest;
   private static boolean initialize = false;
   private Object[] excludeParentList;

   public boolean prepare(Map parameters) {
      this.excludeParentList = parameters.values().toArray();
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
         String rez;
         if(this.excludeParentList != null && this.excludeParentList.length > 0) {
            rez = this.app.currentNode.getParentNode().getNodeName();
            int ex = rez.indexOf(":");
            if(ex > 0) {
               rez = rez.substring(ex + 1);
            }

            Object[] var8 = this.excludeParentList;
            int var7 = this.excludeParentList.length;

            for(int var6 = 0; var6 < var7; ++var6) {
               Object ar = var8[var6];
               if(rez.equalsIgnoreCase((String)ar)) {
                  return in;
               }
            }
         }

         rez = "";

         try {
            byte[] var10 = in.getBytes("CP1251");
            this.digest.update(var10);
            byte[] var11 = this.digest.digest();
            rez = toHexString(var11);
         } catch (Exception var9) {
            log.error(var9.getMessage());
            var9.printStackTrace();
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
