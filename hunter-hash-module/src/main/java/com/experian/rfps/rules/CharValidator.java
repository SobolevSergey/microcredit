package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleValidation;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharValidator extends AbstractRule implements IRuleValidation {
   public static String PARAM_MASK = "MASK";
   private String maska;
   private Pattern p;

   public boolean prepare(Map parameters) {
      this.maska = (String)parameters.get(PARAM_MASK);
      if(this.maska != null) {
         this.p = Pattern.compile(this.maska);
         this.isPrepared = true;
      }

      return this.isPrepared;
   }

   public boolean validation(Object inputObject, Map record) {
      if(this.isPrepared) {
         Matcher m = this.p.matcher((String)inputObject);
         return m.matches();
      } else {
         return false;
      }
   }
}
