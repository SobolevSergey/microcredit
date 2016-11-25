package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.Map;
import org.apache.log4j.Logger;

public class PhoneNormalizator extends AbstractRule implements IRuleNormalization {
   private static final Logger log = Logger.getLogger(PhoneNormalizator.class);

   public boolean prepare(Map parameters) {
      return this.isPrepared = true;
   }

   public Object transform(Object inputObject, Map record) {
      String result;
      if(((String)inputObject).length() == 10) {
         result = "7" + (String)inputObject;
      } else if(((String)inputObject).length() > 0 && ((String)inputObject).charAt(0) == 56) {
         result = '7' + ((String)inputObject).substring(1);
      } else {
         result = (String)inputObject;
      }

      return result;
   }
}
