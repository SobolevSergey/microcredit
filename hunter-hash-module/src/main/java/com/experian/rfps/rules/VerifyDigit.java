package com.experian.rfps.rules;

import org.apache.log4j.Logger;

public class VerifyDigit {
   private static final Logger log = Logger.getLogger(VerifyDigit.class);
   private static int[] weightNumber = new int[]{0, 8, 6, 4, 9, 5, 3, 10, 4, 2, 7, 3};

   public static boolean checkSPIN(String number) {
      boolean result = false;

      try {
         if(number != null && number.length() > 9) {
            long ex = getControlSPIN(number);
            if(ex == 100L) {
               ex = 0L;
            }

            if(ex == (long)Integer.parseInt(number.substring(9))) {
               result = true;
            }
         }
      } catch (NumberFormatException var4) {
         log.error("Error SPIN format " + number + "." + var4.getMessage());
      }

      return result;
   }

   public static long getControlSPIN(String number) {
      long sum = 0L;
      int j = 1;
      boolean result = false;

      for(int remainder = 8; remainder >= 0; --remainder) {
         sum += (long)(Integer.parseInt(String.valueOf(number.charAt(remainder))) * j);
         ++j;
      }

      long var7 = sum % 101L;
      return var7;
   }

   public static boolean checkTax(String number, int digit) {
      boolean result = false;
      --digit;

      try {
         long ex = getControlTax(number, digit);
         if(ex == (long)Integer.parseInt(String.valueOf(number.charAt(digit)))) {
            result = true;
         }
      } catch (NumberFormatException var5) {
         log.error("Error TAX format " + number + "." + var5.getMessage());
      }

      return result;
   }

   public static long getControlTax(String number, int digit) {
      long sum = 0L;
      int j = 0;
      boolean result = false;

      for(int remainder = digit; remainder >= 0; --remainder) {
         sum += (long)(Integer.parseInt(String.valueOf(number.charAt(remainder))) * weightNumber[j++]);
      }

      long var8 = sum % 11L;
      if(var8 > 9L) {
         var8 %= 10L;
      }

      return var8;
   }
}
